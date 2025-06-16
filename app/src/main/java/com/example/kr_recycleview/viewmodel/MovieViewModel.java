package com.example.kr_recycleview.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.kr_recycleview.data.Movie;
import com.example.kr_recycleview.repository.MovieRepository;

import java.util.List;

public class MovieViewModel extends AndroidViewModel {
    private final MovieRepository repo;

    public MovieViewModel(@NonNull Application application) {
        super(application);
        repo = new MovieRepository(application);
    }

    public LiveData<List<Movie>> getAll() {
        return repo.getAll();
    }

    /** Получить один фильм для редактирования */
    public LiveData<Movie> getById(int id) {
        return repo.getById(id);
    }

    public LiveData<List<Movie>> getByStatus(int status) {
        return repo.getByStatus(status);
    }
    public LiveData<List<Movie>> search(String q) {
        return repo.search(q);
    }

    public void insert(Movie m) {
        repo.insert(m);
    }

    public void update(Movie m) {
        repo.update(m);
    }


    public void delete(Movie m) {
        repo.delete(m);
    }
    public void fetchMovieInfo(String title, MovieRepository.FetchCallback cb) {
        repo.fetchFirstMatch(title, cb);
    }
}

