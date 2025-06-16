package com.example.kr_recycleview.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import com.example.kr_recycleview.BuildConfig;
import com.example.kr_recycleview.R;
import com.example.kr_recycleview.data.Movie;
import com.example.kr_recycleview.network.TMDBService;
import com.example.kr_recycleview.network.model.SearchResponse;
import com.example.kr_recycleview.network.model.TMDBMovie;
import com.example.kr_recycleview.viewmodel.MovieViewModel;

import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AddEditMovieActivity extends AppCompatActivity {

    private MovieViewModel vm;
    private EditText editTitle, editGenre, editYear, editPosterName, editReview;
    private RatingBar ratingBar;
    private Spinner  spStatus;                // ← НОВОЕ
    private Spinner spQuality;

    private Button   btnSearchInfo, btnSave;

    private int  movieId = -1;
    private TMDBService tmdb;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_movie);

        /* ---------- Toolbar ---------- */
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.title_add_edit);

        /* ---------- Retrofit ---------- */
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.themoviedb.org/3/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        tmdb = retrofit.create(TMDBService.class);

        /* ---------- ViewModel ---------- */
        vm = new ViewModelProvider(this).get(MovieViewModel.class);

        /* ---------- UI элементы ---------- */
        editTitle      = findViewById(R.id.editTitle);
        editGenre      = findViewById(R.id.editGenre);
        editYear       = findViewById(R.id.editYear);
        ratingBar      = findViewById(R.id.ratingBar);
        editPosterName = findViewById(R.id.editPosterName);
        editReview     = findViewById(R.id.editReview);
        spStatus       = findViewById(R.id.spinnerStatus);      // ←---
        spQuality      = findViewById(R.id.spinnerQuality);
        btnSearchInfo  = findViewById(R.id.btnSearchInfo);
        btnSave        = findViewById(R.id.btnSave);


        /* ---------- заполняем Spinner статуса ---------- */
        ArrayAdapter<CharSequence> qualityAdapter = ArrayAdapter.createFromResource(
                this,
                R.array.movie_quality_tags,
                android.R.layout.simple_spinner_item    // стандартный layout для элемента спиннера
        );
        qualityAdapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item  // layout для выпадающего списка
        );
        spQuality.setAdapter(qualityAdapter);

        /* ---------- если редактируем существующий ---------- */
        if (getIntent().hasExtra("id")) {
            movieId = getIntent().getIntExtra("id", -1);
            vm.getById(movieId).observe(this, m -> {
                if (m == null) return;
                editTitle.setText(m.title);
                editGenre.setText(m.genre);
                editYear.setText(m.year);
                ratingBar.setRating((float) m.rating);
                editPosterName.setText(m.posterUrl);
                editReview.setText(m.review);
                spStatus.setSelection(m.status);          // ←--- 0 или 1
                spQuality.setSelection(m.qualityTag != null ? m.qualityTag + 1 : 0);
            });
        }

        /* ---------- кнопка «Загрузить инфо» ---------- */
        btnSearchInfo.setOnClickListener(v -> {
            String q = editTitle.getText().toString().trim();
            if (TextUtils.isEmpty(q)) {
                editTitle.setError(getString(R.string.error_empty_title));
                return;
            }
            tmdb.searchMovies(BuildConfig.TMDB_API_KEY, q)
                    .enqueue(new Callback<SearchResponse>() {

                        @Override public void onResponse(Call<SearchResponse> c, Response<SearchResponse> r) {
                            if (!r.isSuccessful() || r.body()==null || r.body().results.isEmpty()) {
                                toast("Ничего не найдено"); return;
                            }
                            TMDBMovie first = r.body().results.get(0);
                            editGenre.setText(TextUtils.join(", ", first.getGenreNames()));
                            String rel = first.getReleaseDate();
                            if (rel!=null && rel.length()>=4) editYear.setText(rel.substring(0,4));
                            String poster = first.getPosterPath();
                            if (poster!=null && poster.startsWith("/")) poster = poster.substring(1);
                            editPosterName.setText(poster);
                            toast(String.format(Locale.getDefault(),
                                    "Найден: %s (%s)", first.getTitle(), rel));
                        }
                        @Override public void onFailure(Call<SearchResponse> c, Throwable t){
                            toast("Ошибка сети: "+t.getMessage());
                        }
                    });
        });

        /* ---------- кнопка «Сохранить» ---------- */
        btnSave.setOnClickListener(v -> {
            String title  = editTitle.getText().toString().trim();
            if (title.isEmpty()) { editTitle.setError(getString(R.string.error_empty_title)); return; }

            String genre  = editGenre.getText().toString().trim();
            String year   = editYear.getText().toString().trim();
            double rate   = ratingBar.getRating();
            String poster = editPosterName.getText().toString().trim();
            String review = editReview.getText().toString().trim();
            int    status = spStatus.getSelectedItemPosition();          // ←--- 0/1
            int rawQuality = spQuality.getSelectedItemPosition() - 1;
            Integer qualityTag = rawQuality >= 0 ? rawQuality : null;

            Movie m = new Movie(title, year, genre, poster, rate, review, status, qualityTag);

            if (movieId >= 0) { m.id = movieId; vm.update(m); } else { vm.insert(m); }
            finish();
        });
    }

    private void toast(String msg){ Toast.makeText(this,msg,Toast.LENGTH_SHORT).show(); }

    @Override public boolean onSupportNavigateUp(){ finish(); return true; }
}
