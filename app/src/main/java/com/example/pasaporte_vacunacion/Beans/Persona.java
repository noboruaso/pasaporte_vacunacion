package com.example.pasaporte_vacunacion.Beans;

import com.google.gson.annotations.SerializedName;

public class Persona {
    private String dni, name, first_name, last_name, cui;

    @SerializedName("data")
    private final Data data;

    public Persona(Data data) {
        this.data = data;
    }

    public Data getData() {
        return data;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getCui() {
        return cui;
    }

    public void setCui(String cui) {
        this.cui = cui;
    }
}
