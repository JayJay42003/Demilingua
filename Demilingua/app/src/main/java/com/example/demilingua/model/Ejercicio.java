package com.example.demilingua.model;

import com.google.gson.annotations.SerializedName;

/**
 * Clase que representa un ejercicio en el sistema
 */
public class Ejercicio {
    @SerializedName("id")
    private int id;
    @SerializedName("puntos")
    private Integer puntos; // Usamos Integer para permitir null
    @SerializedName("test_id")
    private int testId;
    @SerializedName("tipo")
    private String tipo;
    @SerializedName("contenido")
    private String contenido;
    @SerializedName("opciones")
    private String opciones;
    @SerializedName("respuesta")
    private String respuesta;

    // Constructores
    public Ejercicio() {
        // Constructor vacío necesario para frameworks
    }

    public Ejercicio(int id, Integer puntos, int testId, String tipo) {
        this.id = id;
        this.puntos = puntos;
        this.testId = testId;
        this.tipo = tipo;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    // Aliases para compatibilidad con código antiguo
    public int getEjercicioId() { return id; }
    public void setEjercicioId(int id) { this.id = id; }

    public Integer getPuntos() {
        return puntos;
    }

    public void setPuntos(Integer puntos) {
        this.puntos = puntos;
    }

    public int getTestId() {
        return testId;
    }

    public void setTestId(int testId) {
        this.testId = testId;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public String getOpciones() {
        return opciones;
    }

    public void setOpciones(String opciones) {
        this.opciones = opciones;
    }

    public String getRespuesta() {
        return respuesta;
    }

    public void setRespuesta(String respuesta) {
        this.respuesta = respuesta;
    }

    @Override
    public String toString() {
        return "Ejercicio{" +
                "id=" + id +
                ", puntos=" + puntos +
                ", test_id=" + testId +
                ", tipo='" + tipo + '\'' +
                '}';
    }
}