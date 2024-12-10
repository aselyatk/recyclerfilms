package com.example.kr_recycleview;

import android.content.Context;
import android.util.EventLogTags;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;


import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private List<Movie> movieList;
    private Context context;
    private OnMovieClickListener listener; // Обработчик кликов

    public MovieAdapter(List<Movie> movieList, Context context, OnMovieClickListener listener) {
        this.movieList = movieList;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_item, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        Movie movie = movieList.get(position);

        holder.titleTextView.setText(movie.getTitle());
        holder.genreTextView.setText(movie.getGenre());
        holder.yearTextView.setText(String.valueOf(movie.getYear()));
        holder.ratingTextView.setText(String.valueOf(movie.getRating()));

        int imageResourceId = context.getResources().getIdentifier(movie.getImageName(), "drawable", context.getPackageName());
        holder.imageView.setImageResource(imageResourceId);

        // Обработчик клика по элементу
        holder.itemView.setOnClickListener(v -> {
            // Создаем Intent для перехода на MovieDetailActivity
            Intent intent = new Intent(context, MovieDetailActivity.class);
            // Передаем объект Movie через Intent
            intent.putExtra("movie", movie);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    // Интерфейс для кликов
    public interface OnMovieClickListener {
        void onMovieClick(Movie movie);
    }

    public static class MovieViewHolder extends RecyclerView.ViewHolder {
        public TextView titleTextView;
        public TextView genreTextView;
        public TextView yearTextView;
        public TextView ratingTextView;
        public ImageView imageView;

        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            genreTextView = itemView.findViewById(R.id.genreTextView);
            yearTextView = itemView.findViewById(R.id.yearTextView);
            ratingTextView = itemView.findViewById(R.id.ratingTextView);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }
}
