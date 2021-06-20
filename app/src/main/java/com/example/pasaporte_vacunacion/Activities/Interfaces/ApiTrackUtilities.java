package com.example.pasaporte_vacunacion.Activities.Interfaces;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiTrackUtilities {
    private static Retrofit retrofit = null;

    public static ApiTrackInterface getApiTrackInterface(){
        if (retrofit == null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(ApiTrackInterface.BASE_URL_TRACK)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit.create(ApiTrackInterface.class);
    }
}
