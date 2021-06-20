package com.example.pasaporte_vacunacion.Activities.Interfaces;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiTrackInterface {

    static final String BASE_URL_TRACK = "https://corona.lmao.ninja/v2/";

    @GET("countries")
    Call<ArrayList<ApiCountryData>> getApiCountryData();
}
