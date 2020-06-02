package com.example.powerpuffgirls.api;

import com.example.powerpuffgirls.models.News;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("top-headlines")
    Call<News> getNews(

            @Query("country") String country,
            @Query("apiKey") String apiKey,
            @Query("q") String keyword
    );
}
