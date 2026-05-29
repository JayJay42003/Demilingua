package com.example.demilingua.model;

import com.google.gson.annotations.SerializedName;

public class Usuario {

    @SerializedName("id")
    private int id;

    @SerializedName("nombre")
    private String nombre;

    @SerializedName("correo")
    private String correo;

    @SerializedName("contrasena")
    private String contrasena; // Solo para enviar, no se debería recibir

    @SerializedName("vidas")
    private int vidas;

    @SerializedName("racha_actual")
    private int rachaActual;

    public Usuario(int id, String nombre, String correo, int vidas, int rachaActual) {
        this.id = id;
        this.nombre = nombre;
        this.correo = correo;
        this.vidas = vidas;
        this.rachaActual = rachaActual;
    }
    
    // Constructor para registro
    public Usuario(String nombre, String correo, String contrasena) {
        this.nombre = nombre;
        this.correo = correo;
        this.contrasena = contrasena;
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

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public int getVidas() {
        return vidas;
    }

    public void setVidas(int vidas) {
        this.vidas = vidas;
    }

    public int getRachaActual() {
        return rachaActual;
    }

    public void setRachaActual(int rachaActual) {
        this.rachaActual = rachaActual;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", correo='" + correo + '\'' +
                ", vidas=" + vidas +
                ", rachaActual=" + rachaActual +
                '}';
    }
}
