package com.example.kr_recycleview.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "movies")
public class Movie {
    public static final int STATUS_WANT_TO_WATCH = 0;
    public static final int STATUS_WATCHED      = 1;
    public static final int STATUS_ABANDONED    = 2;
    public static final int TAG_BAD     = 0; // «не очень»
    public static final int TAG_AVERAGE = 1; // «средне»
    public static final int TAG_BEST    = 2; // «лучшее»
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String title;
    public String year;
    public String genre;
    public String posterUrl;
    public double rating;
    public String review;              // ← добавили поле рецензии
    public int status;
    /** 0=BAD,1=AVERAGE,2=BEST; nullable для фильтра «все» */
    public Integer qualityTag;

    // Конструктор с новым полем review
    public Movie(String title, String year, String genre,
                 String posterUrl, double rating,
                 String review, int status, Integer qualityTag) {
        this.title     = title;
        this.year      = year;
        this.genre     = genre;
        this.posterUrl = posterUrl;
        this.rating    = rating;
        this.review    = review;
        this.status    = status;
        this.qualityTag = qualityTag;
    }
}