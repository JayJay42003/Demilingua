package com.example.demilingua.model;

import com.google.gson.annotations.SerializedName;

public class Idioma {

    @SerializedName("id")
    private int id;

    @SerializedName("nombre")
    private String nombre;

    @SerializedName("codigo_iso")
    private String codigoIso;

    public Idioma(int id, String nombre, String codigoIso) {
        this.id = id;
        this.nombre = nombre;
        this.codigoIso = codigoIso;
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

    public String getCodigoIso() {
        return codigoIso;
    }

    public void setCodigoIso(String codigoIso) {
        this.codigoIso = codigoIso;
    }

    @Override
    public String toString() {
        return "Idioma{" +
                "id=" + id +
                ", nombre='" + nombre + "'" +
                ", codigoIso='" + codigoIso + "'" +
                '}';
    }
}
