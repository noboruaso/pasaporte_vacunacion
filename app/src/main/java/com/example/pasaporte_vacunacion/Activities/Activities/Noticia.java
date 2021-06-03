package com.example.pasaporte_vacunacion.Activities.Activities;

public class Noticia {
    private String titulo, fecha, descripcion;
    private int foto;

    public Noticia() {
    }

    public Noticia(String titulo, String fecha, String descripcion, int foto) {
        this.titulo = titulo;
        this.fecha = fecha;
        this.descripcion = descripcion;
        this.foto = foto;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getFoto() {
        return foto;
    }

    public void setFoto(int foto) {
        this.foto = foto;
    }
}
