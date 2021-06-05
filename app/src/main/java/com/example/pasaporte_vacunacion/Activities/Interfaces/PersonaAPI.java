package com.example.pasaporte_vacunacion.Activities.Interfaces;

import com.example.pasaporte_vacunacion.Beans.Persona;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface PersonaAPI {
    @GET("api/persons/{dni}")
    public Call<Persona> find(@Path("dni") String dni);
}
