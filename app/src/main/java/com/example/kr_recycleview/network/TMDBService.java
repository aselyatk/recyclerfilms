package com.example.kr_recycleview.network;

import com.example.kr_recycleview.network.model.SearchResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface TMDBService {
    @GET("search/movie")
    Call<SearchResponse> searchMovies(
            @Query("api_key") String apiKey,
            @Query("query") String query
    );
}
