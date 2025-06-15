package com.example.kr_recycleview.ui;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.kr_recycleview.R;
import com.example.kr_recycleview.data.Movie;

public class MovieDetailActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        ImageView movieImageView     = findViewById(R.id.imageView);
        TextView titleTextView       = findViewById(R.id.titleTextView);
        TextView yearTextView        = findViewById(R.id.yearTextView);
        TextView genreTextView       = findViewById(R.id.genreTextView);
        TextView ratingTextView      = findViewById(R.id.ratingTextView);
        TextView descriptionTextView = findViewById(R.id.descriptionTextView);

        // Получаем объект Movie из интента
        Movie movie = (Movie) getIntent().getSerializableExtra("movie");
        if (movie == null) {
            finish();
            return;
        }

        // Заполняем UI данными из полей сущности
        titleTextView.setText(movie.title);
        yearTextView.setText(movie.year);
        genreTextView.setText(movie.genre);
        ratingTextView.setText(String.valueOf(movie.rating));

        // Если вы добавили в Movie поле review (рецензия), выводим его:
        // descriptionTextView.setText(movie.review != null ? movie.review : "");
        // Иначе скрываем или очищаем текст:
        descriptionTextView.setText("");

        // Загружаем картинку по имени ресурса, указанному в posterUrl
        int resId = getResources()
                .getIdentifier(movie.posterUrl, "drawable", getPackageName());
        if (resId != 0) {
            movieImageView.setImageResource(resId);
        }
    }
}
