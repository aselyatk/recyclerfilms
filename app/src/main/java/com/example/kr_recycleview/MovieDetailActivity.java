package com.example.kr_recycleview;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MovieDetailActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        ImageView movieImageView = findViewById(R.id.imageView);
        TextView titleTextView = findViewById(R.id.titleTextView);
        TextView genreTextView = findViewById(R.id.genreTextView);
        TextView yearTextView = findViewById(R.id.yearTextView);
        TextView ratingTextView = findViewById(R.id.ratingTextView);
        TextView descriptionTextView = findViewById(R.id.descriptionTextView);

        // Получаем данные о фильме из Intent
        Movie movie = (Movie) getIntent().getSerializableExtra("movie");

        // Заполняем данные в UI
        titleTextView.setText(movie.getTitle());
        genreTextView.setText(movie.getGenre());
        yearTextView.setText(String.valueOf(movie.getYear()));
        ratingTextView.setText(String.valueOf(movie.getRating()));
        descriptionTextView.setText(movie.getDescription());

        // Устанавливаем изображение
        int imageResourceId = getResources().getIdentifier(movie.getImageName(), "drawable", getPackageName());
        movieImageView.setImageResource(imageResourceId);
    }
}
