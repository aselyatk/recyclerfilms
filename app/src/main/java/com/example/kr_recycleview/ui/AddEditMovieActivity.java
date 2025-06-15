package com.example.kr_recycleview.ui;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import com.example.kr_recycleview.R;
import com.example.kr_recycleview.data.Movie;
import com.example.kr_recycleview.viewmodel.MovieViewModel;

public class AddEditMovieActivity extends AppCompatActivity {
    private MovieViewModel vm;
    private EditText editTitle, editGenre, editYear, editPosterName, editReview;
    private RatingBar ratingBar;
    private int movieId = -1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_movie);

        // 1) Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.title_add_edit);

        // 2) ViewModel
        vm = new ViewModelProvider(this).get(MovieViewModel.class);

        // 3) Привязываем поля
        editTitle      = findViewById(R.id.editTitle);
        editGenre      = findViewById(R.id.editGenre);
        editYear       = findViewById(R.id.editYear);
        ratingBar      = findViewById(R.id.ratingBar);
        editPosterName = findViewById(R.id.editPosterName);
        editReview     = findViewById(R.id.editReview);
        Button btnSave = findViewById(R.id.btnSave);

        // 4) Если Intent содержит "id" — загружаем существующий фильм
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

        // 5) Сохранение
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

            // Создаём объект Movie с новым конструктором
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