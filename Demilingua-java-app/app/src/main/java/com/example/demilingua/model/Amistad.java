package com.example.demilingua.model;

import com.google.gson.annotations.SerializedName;

public class Amistad {

    @SerializedName("amigo_id")
    private int amigoId;

    @SerializedName("estado")
    private String estado;

    // Estos campos no vienen del backend directamente en el listado de amigos
    // pero pueden ser llenados manualmente si se desea.
    private String nombre;
    private int puntos;

    public int getAmigoId() {
        return amigoId;
    }

    public void setAmigoId(int amigoId) {
        this.amigoId = amigoId;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
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
}
