package com.example.kr_recycleview.network.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Обёртка под один фильм из TMDB
public class TMDBMovie {
    @SerializedName("title")
    private String title;

    @SerializedName("genre_ids")
    private List<Integer> genreIds;

    @SerializedName("release_date")
    private String releaseDate;

    @SerializedName("poster_path")
    private String posterPath;

    // Карта id→название жанра. Добавьте здесь те, что вам нужны:
    private static final Map<Integer,String> GENRE_MAP = new HashMap<>();
    static {
        GENRE_MAP.put(28, "Боевик");
        GENRE_MAP.put(12, "Приключения");
        GENRE_MAP.put(16, "Анимация");
        GENRE_MAP.put(35, "Комедия");
        GENRE_MAP.put(80, "Криминал");
        GENRE_MAP.put(99, "Документальный");
        GENRE_MAP.put(18, "Драма");
        GENRE_MAP.put(10751, "Семейный");
        GENRE_MAP.put(14, "Фантастика");
        GENRE_MAP.put(36, "История");
        GENRE_MAP.put(27, "Ужасы");
        GENRE_MAP.put(10402, "Музыка");
        GENRE_MAP.put(9648, "Тайна");
        GENRE_MAP.put(10749, "Мелодрама");
        GENRE_MAP.put(878, "Фантастика");
        GENRE_MAP.put(53, "Триллер");
        GENRE_MAP.put(10752, "Военный");
        GENRE_MAP.put(37, "Вестерн");
    }

    public String getTitle() {
        return title;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getPosterPath() {
        return posterPath;
    }

    /**
     * Преобразует genreIds в список строковых названий.
     */
    public List<String> getGenreNames() {
        List<String> names = new ArrayList<>();
        if (genreIds != null) {
            for (Integer id : genreIds) {
                String n = GENRE_MAP.get(id);
                if (n != null) names.add(n);
            }
        }
        return names;
    }
}
