
package com.example.demilingua.model;

/**
 * Clase que representa un curso en el sistema
 */
public class Curso {
    private int id;
    private String nombre;
    private String descripcion;
    private String dificultad;
    private int idiomaId;

    // Constructores
    public Curso() {
        // Constructor vacío necesario para frameworks
    }

    public Curso(int id, String nombre, String descripcion, String dificultad, int idiomaId) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.dificultad = dificultad;
        this.idiomaId = idiomaId;
    }

    // Getters y Setters
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
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del curso no puede estar vacío");
        }
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
        if (!dificultad.matches("A[1-2]|B[1-2]|C[1-2]")) {
            throw new IllegalArgumentException("Nivel de dificultad inválido. Use A1, A2, B1, B2, C1 o C2");
        }
        this.dificultad = dificultad;
    }

    public int getIdiomaId() {
        return idiomaId;
    }

    public void setIdiomaId(int idiomaId) {
        if (idiomaId <= 0) {
            throw new IllegalArgumentException("El ID del idioma debe ser positivo");
        }
        this.idiomaId = idiomaId;
    }

    // Métodos útiles
    @Override
    public String toString() {
        return "Curso{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", dificultad='" + dificultad + '\'' +
                ", idiomaId=" + idiomaId +
                '}';
    }

}
