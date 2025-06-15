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
    @Query("SELECT * FROM movies ORDER BY title")
    LiveData<List<Movie>> getAll();

    @Query("SELECT * FROM movies WHERE title LIKE :q OR genre LIKE :q ORDER BY title")
    LiveData<List<Movie>> search(String q);

    @Query("SELECT * FROM movies WHERE id = :id")
    LiveData<Movie> getById(int id);      // ← вот он

    @Insert
    void insert(Movie movie);

    @Update
    void update(Movie movie);

    @Delete
    void delete(Movie movie);
}
