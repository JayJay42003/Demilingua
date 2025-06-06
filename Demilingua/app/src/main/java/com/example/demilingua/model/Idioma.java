package com.example.demilingua.model;

public class Idioma {
    private int icon; // ID del recurso drawable
    private String name; // Nombre del idioma (ej: "Español")
    private int id;

    // Constructor
    public Idioma(int id, String name, int icon) {
        this.icon = icon;
        this.name = name;
        this.id = id;
    }

    // Getters y Setters
    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}