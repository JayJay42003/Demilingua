package com.example.demilingua.model;

import com.google.gson.annotations.SerializedName;

public class Amigo {

    @SerializedName("id")
    private int id;

    @SerializedName("nombre")
    private String nombre;

    @SerializedName("puntos")
    private int puntos;

    public Amigo(int id, String nombre, int puntos) {
        this.id = id;
        this.nombre = nombre;
        this.puntos = puntos;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getPuntos() {
        return puntos;
    }

    public void setPuntos(int puntos) {
        this.puntos = puntos;
    }

    @Override
    public String toString() {
        return "Amigo{" +
                "id=" + id +
                ", nombre='" + nombre + "'" +
                ", puntos=" + puntos +
                '}';
    }
}
