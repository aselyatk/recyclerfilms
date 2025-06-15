package com.example.kr_recycleview.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kr_recycleview.R;
import com.example.kr_recycleview.data.Movie;

import java.util.ArrayList;
import java.util.List;

import coil.Coil;
import coil.ImageLoader;
import coil.request.ImageRequest;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private final List<Movie> movieList = new ArrayList<>();
    private final Context context;
    private final OnMovieClickListener listener;

    public MovieAdapter(Context context, OnMovieClickListener listener) {
        this.context = context;
        this.listener = listener;
    }

    /** Вызывается из Activity для обновления списка */
    public void setMovieList(List<Movie> newMovies) {
        movieList.clear();
        if (newMovies != null) {
            movieList.addAll(newMovies);
        }
        notifyDataSetChanged();
    }

    @NonNull @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_item, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        Movie movie = movieList.get(position);
        holder.titleTextView.setText(movie.title);
        holder.genreTextView.setText(movie.genre);
        holder.yearTextView.setText(movie.year);
        holder.ratingTextView.setText(String.valueOf(movie.rating));

        // Загружаем постер с TMDB
        if (movie.posterUrl != null && !movie.posterUrl.isEmpty()) {
            String fullUrl = "https://image.tmdb.org/t/p/w500/" + movie.posterUrl;
            ImageLoader loader = Coil.imageLoader(context);
            ImageRequest request = new ImageRequest.Builder(context)
                    .data(fullUrl)
                    .placeholder(R.drawable.ic_launcher_foreground) // поставьте свой placeholder
                    .error(R.drawable.ic_launcher_foreground)       // поставьте свой error-образ
                    .target(holder.imageView)
                    .crossfade(true)
                    .build();
            loader.enqueue(request);
        } else {
            holder.imageView.setImageResource(R.drawable.ic_launcher_foreground);
        }

        holder.itemView.setOnClickListener(v -> listener.onMovieClick(movie));
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public interface OnMovieClickListener {
        void onMovieClick(Movie movie);
    }

    static class MovieViewHolder extends RecyclerView.ViewHolder {
        final TextView titleTextView;
        final TextView genreTextView;
        final TextView yearTextView;
        final TextView ratingTextView;
        final ImageView imageView;

        MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView  = itemView.findViewById(R.id.titleTextView);
            genreTextView  = itemView.findViewById(R.id.genreTextView);
            yearTextView   = itemView.findViewById(R.id.yearTextView);
            ratingTextView = itemView.findViewById(R.id.ratingTextView);
            imageView      = itemView.findViewById(R.id.imageView);
        }
    }
}
