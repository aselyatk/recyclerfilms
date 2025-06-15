package com.example.kr_recycleview.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.kr_recycleview.data.AppDatabase;
import com.example.kr_recycleview.data.Movie;
import com.example.kr_recycleview.data.MovieDao;


import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MovieRepository {
    private final MovieDao dao;
    private final ExecutorService exec = Executors.newSingleThreadExecutor();

    public MovieRepository(Application app) {
        AppDatabase db = AppDatabase.getInstance(app);
        dao = db.movieDao();
    }

    public LiveData<List<Movie>> getAll() {
        return dao.getAll();
    }

    public LiveData<Movie> getById(int id) {
        return dao.getById(id);
    }

    public LiveData<List<Movie>> search(String query) {
        return dao.search("%" + query + "%");
    }

    public void insert(Movie m) {
        exec.execute(() -> dao.insert(m));
    }

    public void update(Movie m) {
        exec.execute(() -> dao.update(m));
    }

    public void delete(Movie m) {
        exec.execute(() -> dao.delete(m));
    }
}

