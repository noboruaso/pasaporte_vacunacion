package com.example.pasaporte_vacunacion.Beans;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("nombre_completo")
    @Expose
    private String nombre_completo;

    @SerializedName("numero")
    @Expose
    private String numero;

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getNombre_completo() {
        return nombre_completo;
    }

    public void setNombre_completo(String nombre_completo) {
        this.nombre_completo = nombre_completo;
    }
}
