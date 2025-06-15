package com.example.kr_recycleview.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
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
    private Button btnSearchInfo, btnSave;
    private int movieId = -1;
    private TMDBService tmdb;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_movie);

        // 1) Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.title_add_edit);

        // 2) Retrofit + TMDBService
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.themoviedb.org/3/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        tmdb = retrofit.create(TMDBService.class);

        // 3) ViewModel
        vm = new ViewModelProvider(this).get(MovieViewModel.class);

        // 4) Поля из layout
        editTitle      = findViewById(R.id.editTitle);
        editGenre      = findViewById(R.id.editGenre);
        editYear       = findViewById(R.id.editYear);
        ratingBar      = findViewById(R.id.ratingBar);
        editPosterName = findViewById(R.id.editPosterName);
        editReview     = findViewById(R.id.editReview);
        btnSearchInfo  = findViewById(R.id.btnSearchInfo);
        btnSave        = findViewById(R.id.btnSave);

        // 5) Загрузка существующего фильма (если id пришёл в интент)
        if (getIntent().hasExtra("id")) {
            movieId = getIntent().getIntExtra("id", -1);
            vm.getById(movieId).observe(this, m -> {
                if (m != null) {
                    editTitle.setText(m.title);
                    editGenre.setText(m.genre);
                    editYear.setText(m.year);
                    ratingBar.setRating((float) m.rating);
                    editPosterName.setText(m.posterUrl);
                    editReview.setText(m.review);
                }
            });
        }

        // 6) Поиск инфы по TMDB
        btnSearchInfo.setOnClickListener(v -> {
            String query = editTitle.getText().toString().trim();
            if (TextUtils.isEmpty(query)) {
                editTitle.setError("Введите название для поиска");
                return;
            }
            tmdb.searchMovies(BuildConfig.TMDB_API_KEY, query)
                    .enqueue(new Callback<SearchResponse>() {
                        @Override
                        public void onResponse(Call<SearchResponse> call,
                                               Response<SearchResponse> resp) {
                            if (resp.isSuccessful() && resp.body() != null
                                    && !resp.body().results.isEmpty()) {
                                TMDBMovie first = resp.body().results.get(0);
                                // Заполняем UI из первого результата
                                editGenre.setText(TextUtils.join(", ", first.getGenreNames()));
                                // У TMDBMovie должен быть getReleaseDate()
                                String date = first.getReleaseDate();
                                if (date != null && date.length() >= 4) {
                                    editYear.setText(date.substring(0, 4));
                                }
                                // В TMDBMovie.getPosterPath() возвращает строку вида "/abc.jpg"
                                // Мы сохраняем просто имя без слеша
                                String poster = first.getPosterPath();
                                if (poster != null && poster.startsWith("/")) {
                                    poster = poster.substring(1);
                                }
                                editPosterName.setText(poster);
                                Toast.makeText(AddEditMovieActivity.this,
                                        String.format(Locale.getDefault(),
                                                "Найден: %s (%s)", first.getTitle(), date),
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(AddEditMovieActivity.this,
                                        "Ничего не найдено", Toast.LENGTH_SHORT).show();
                            }
                        }
                        @Override
                        public void onFailure(Call<SearchResponse> call, Throwable t) {
                            Toast.makeText(AddEditMovieActivity.this,
                                    "Ошибка сети: " + t.getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
        });

        // 7) Сохранение локально
        btnSave.setOnClickListener(v -> {
            String title  = editTitle.getText().toString().trim();
            String genre  = editGenre.getText().toString().trim();
            String year   = editYear.getText().toString().trim();
            double rate   = ratingBar.getRating();
            String poster = editPosterName.getText().toString().trim();
            String review = editReview.getText().toString().trim();
            if (title.isEmpty()) {
                editTitle.setError(getString(R.string.error_empty_title));
                return;
            }
            Movie movie = new Movie(title, year, genre, poster, rate, review);
            if (movieId >= 0) {
                movie.id = movieId;
                vm.update(movie);
            } else {
                vm.insert(movie);
            }
            finish();
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
