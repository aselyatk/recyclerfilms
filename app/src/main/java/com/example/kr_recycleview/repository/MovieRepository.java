package com.example.kr_recycleview.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.kr_recycleview.BuildConfig;
import com.example.kr_recycleview.data.AppDatabase;
import com.example.kr_recycleview.data.Movie;
import com.example.kr_recycleview.data.MovieDao;
import com.example.kr_recycleview.network.model.TMDBMovie;
import com.example.kr_recycleview.network.model.SearchResponse;
import com.example.kr_recycleview.network.TMDBService;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MovieRepository {
    private final MovieDao dao;
    private final ExecutorService exec = Executors.newSingleThreadExecutor();
    private final TMDBService tmdb;

    /** Колбэк для передачи результата поиска в TMDB */
    public interface FetchCallback {
        void onSuccess(TMDBMovie movie);
        void onError(String errorMessage);
    }

    public MovieRepository(Application app) {
        // 1) Room
        AppDatabase db = AppDatabase.getInstance(app);
        dao = db.movieDao();

        // 2) Retrofit для TMDB
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.themoviedb.org/3/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        tmdb = retrofit.create(TMDBService.class);
    }

    // === методы для работы с локальной БД ===

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

    // === метод для поиска фильма по названию в TMDB ===

    public void fetchFirstMatch(String title, FetchCallback callback) {
        tmdb.searchMovies(BuildConfig.TMDB_API_KEY, title)
                .enqueue(new Callback<SearchResponse>() {
                    @Override
                    public void onResponse(Call<SearchResponse> call,
                                           Response<SearchResponse> resp) {
                        if (resp.isSuccessful()
                                && resp.body() != null
                                && !resp.body().results.isEmpty()) {
                            callback.onSuccess(resp.body().results.get(0));
                        } else {
                            callback.onError("Ничего не найдено");
                        }
                    }
                    @Override
                    public void onFailure(Call<SearchResponse> call, Throwable t) {
                        callback.onError(t.getMessage());
                    }
                });
    }
}
