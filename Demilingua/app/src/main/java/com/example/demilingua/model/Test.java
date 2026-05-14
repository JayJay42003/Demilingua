package com.example.demilingua.model;

/**
 * Clase que representa un test/examen en el sistema
 */
public class Test {
    private int id;
    private int cursoId;
    private String titulo;

    // Constructores
    public Test() {
        // Constructor vacío necesario para frameworks
    }

    public Test(int id, int cursoId, String titulo) {
        this.id = id;
        this.cursoId = cursoId;
        this.titulo = titulo;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCursoId() {
        return cursoId;
    }

    public void setCursoId(int cursoId) {
        if (cursoId <= 0) {
            throw new IllegalArgumentException("El ID del curso debe ser positivo");
        }
        this.cursoId = cursoId;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        if (titulo == null || titulo.trim().isEmpty()) {
            throw new IllegalArgumentException("El título del test no puede estar vacío");
        }
        if (titulo.length() > 255) {
            throw new IllegalArgumentException("El título no puede exceder los 255 caracteres");
        }
        this.titulo = titulo;
    }

    // Métodos útiles
    @Override
    public String toString() {
        return "Test{" +
                "id=" + id +
                ", cursoId=" + cursoId +
                ", titulo='" + titulo + '\'' +
                '}';
    }

    /**
     * Valida que los campos obligatorios estén correctos
     * @throws IllegalArgumentException si algún campo requerido es inválido
     */
    public void validar() {
        if (titulo == null || titulo.trim().isEmpty()) {
            throw new IllegalArgumentException("El título del test es requerido");
        }
        if (cursoId <= 0) {
            throw new IllegalArgumentException("ID de curso inválido");
        }
    }
}