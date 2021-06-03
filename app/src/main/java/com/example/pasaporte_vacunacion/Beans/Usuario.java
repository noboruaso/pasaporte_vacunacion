package com.example.pasaporte_vacunacion.Beans;

import java.util.Date;

public class Usuario {
    private int Id;
    private String Dni, Correo, Password;
    private Date Dni_vencimiento, Nacimiento;

    public Usuario() {}

    public Usuario(int id, String dni, String correo, String password, Date dni_vencimiento, Date nacimiento) {
        Id = id;
        Dni = dni;
        Correo = correo;
        Password = password;
        Dni_vencimiento = dni_vencimiento;
        Nacimiento = nacimiento;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getDni() {
        return Dni;
    }

    public void setDni(String dni) {
        Dni = dni;
    }

    public String getCorreo() {
        return Correo;
    }

    public void setCorreo(String correo) {
        Correo = correo;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public Date getDni_vencimiento() {
        return Dni_vencimiento;
    }

    public void setDni_vencimiento(Date dni_vencimiento) {
        Dni_vencimiento = dni_vencimiento;
    }

    public Date getNacimiento() {
        return Nacimiento;
    }

    public void setNacimiento(Date nacimiento) {
        Nacimiento = nacimiento;
    }
}
