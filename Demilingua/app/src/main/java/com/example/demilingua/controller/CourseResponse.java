package com.example.demilingua.controller;


public class CourseResponse {

    private int id;

    private String nombre;

    private String descripcion;

    private String dificultad;

    private int idiomaId;

    public CourseResponse() {
    }

    public CourseResponse(int id, String nombre, String descripcion, String dificultad, int idiomaId) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.dificultad = dificultad;
        this.idiomaId = idiomaId;
    }

    // Getters y setters

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


    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }


    public String getDificultad() {
        return dificultad;
    }

    public void setDificultad(String dificultad) {
        this.dificultad = dificultad;
    }


    public int getIdiomaId() {
        return idiomaId;
    }

    public void setIdiomaId(int idiomaId) {
        this.idiomaId = idiomaId;
    }
}
