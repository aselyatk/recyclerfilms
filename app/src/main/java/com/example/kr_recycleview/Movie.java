package com.example.kr_recycleview;
import java.io.Serializable;

public class Movie implements Serializable {
    private String title;
    private String genre;
    private int year;
    private double rating;
    private String imageName;
    private String description;

    public Movie(String title, String genre, int year, double rating, String imageName, String description) {
        this.title = title;
        this.genre = genre;
        this.year = year;
        this.rating = rating;
        this.imageName = imageName;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public String getGenre() {
        return genre;
    }

    public int getYear() {
        return year;
    }

    public double getRating() {
        return rating;
    }

    public String getImageName() {
        return imageName;
    }

    public String getDescription() {
        return description;
    }
}

