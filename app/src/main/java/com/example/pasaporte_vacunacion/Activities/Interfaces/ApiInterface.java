package com.example.pasaporte_vacunacion.Activities.Interfaces;

import com.example.pasaporte_vacunacion.Beans.News;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("top-headlines")
    Call<News> getNews (
            @Query("q") String q,
            @Query("language") String language,
            //@Query("country") String country,
            @Query("apiKey") String apiKey
    );
}
