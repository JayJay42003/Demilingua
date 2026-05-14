package com.example.demilingua.model;

/**
 * Clase que representa un ejercicio en el sistema
 */
public class Ejercicio {
    private int id;
    private Integer puntuacion; // Usamos Integer para permitir null
    private int testId;
    private String tipo;

    // Constructores
    public Ejercicio() {
        // Constructor vacío necesario para frameworks
    }

    public Ejercicio(int id, Integer puntuacion, int testId, String tipo) {
        this.id = id;
        this.puntuacion = puntuacion;
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

    public Integer getPuntuacion() {
        return puntuacion;
    }

    public void setPuntuacion(Integer puntuacion) {
        // Validamos que si no es null, esté en un rango razonable
        if (puntuacion != null && (puntuacion < 0 || puntuacion > 100)) {
            throw new IllegalArgumentException("La puntuación debe estar entre 0 y 100");
        }
        this.puntuacion = puntuacion;
    }

    public int getTestId() {
        return testId;
    }

    public void setTestId(int testId) {
        if (testId <= 0) {
            throw new IllegalArgumentException("El ID del test debe ser positivo");
        }
        this.testId = testId;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        if (tipo == null || tipo.trim().isEmpty()) {
            throw new IllegalArgumentException("El tipo de ejercicio no puede estar vacío");
        }
        this.tipo = tipo;
    }

    // Métodos útiles
    @Override
    public String toString() {
        return "Ejercicio{" +
                "id=" + id +
                ", puntuacion=" + puntuacion +
                ", testId=" + testId +
                ", tipo='" + tipo + '\'' +
                '}';
    }

    /**
     * Valida que los campos obligatorios estén correctos
     * @throws IllegalArgumentException si algún campo requerido es inválido
     */
    public void validar() {
        if (tipo == null || tipo.trim().isEmpty()) {
            throw new IllegalArgumentException("El tipo de ejercicio es requerido");
        }
        if (testId <= 0) {
            throw new IllegalArgumentException("ID de test inválido");
        }
    }

    /**
     * Calcula si el ejercicio está aprobado
     * @param puntuacionMinima La puntuación mínima para aprobar
     * @return true si está aprobado, false si no
     * @throws IllegalStateException Si la puntuación es null
     */
    public boolean estaAprobado(int puntuacionMinima) {
        if (puntuacion == null) {
            throw new IllegalStateException("El ejercicio no tiene puntuación asignada");
        }
        return puntuacion >= puntuacionMinima;
    }
}