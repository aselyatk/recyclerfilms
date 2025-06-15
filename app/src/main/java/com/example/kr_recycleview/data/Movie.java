package com.example.kr_recycleview.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "movies")
public class Movie {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String title;
    public String year;
    public String genre;
    public String posterUrl;
    public double rating;
    public String review;              // ← добавили поле рецензии

    // Конструктор с новым полем review
    public Movie(String title, String year, String genre,
                 String posterUrl, double rating,
                 String review) {
        this.title     = title;
        this.year      = year;
        this.genre     = genre;
        this.posterUrl = posterUrl;
        this.rating    = rating;
        this.review    = review;
    }
}