package com.example.kr_recycleview.network.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class SearchResponse {
    @SerializedName("results")
    public List<TMDBMovie> results;
}