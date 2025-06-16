package com.example.kr_recycleview.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface MovieDao {

    /* ---------- выборки ---------- */

    @Query("SELECT * FROM movies ORDER BY title")
    LiveData<List<Movie>> getAll();

    // 0 = «Хочу посмотреть», 1 = «Просмотрено»
    @Query("SELECT * FROM movies WHERE status = :status ORDER BY title")
    LiveData<List<Movie>> getByStatus(int status);

    @Query("SELECT * FROM movies WHERE qualityTag = :qualityTag ORDER BY title")
    LiveData<List<Movie>> getByQuality(int qualityTag);
    @Query("SELECT * FROM movies WHERE status = :status AND qualityTag = :qualityTag ORDER BY title")
    LiveData<List<Movie>> getByStatusAndQuality(int status, int qualityTag);
    @Query("SELECT * FROM movies WHERE title LIKE :q OR genre LIKE :q ORDER BY title")
    LiveData<List<Movie>> search(String q);

    @Query("SELECT * FROM movies WHERE id = :id LIMIT 1")
    LiveData<Movie> getById(int id);

    /* ---------- изменения ---------- */

    @Insert
    void insert(Movie movie);

    @Update
    void update(Movie movie);

    @Delete
    void delete(Movie movie);
}
