package com.example.demilingua.model;

/**
 * Clase que representa un ejercicio en el sistema
 */
public class Ejercicio {
    private int id;
    private Integer puntos; // Usamos Integer para permitir null
    private int test_id;
    private String tipo;

    // Constructores
    public Ejercicio() {
        // Constructor vacío necesario para frameworks
    }

    public Ejercicio(int id, Integer puntos, int test_id, String tipo) {
        this.id = id;
        this.puntos = puntos;
        this.test_id = test_id;
        this.tipo = tipo;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getPuntos() {
        return puntos;
    }

    public void setPuntos(Integer puntos) {
        // Validamos que si no es null, esté en un rango razonable
        if (puntos != null && (puntos < 0 || puntos > 100)) {
            throw new IllegalArgumentException("La puntuación debe estar entre 0 y 100");
        }
        this.puntos = puntos;
    }

    public int getTest_id() {
        return test_id;
    }

    public void setTest_id(int test_id) {
        if (test_id <= 0) {
            throw new IllegalArgumentException("El ID del test debe ser positivo");
        }
        this.test_id = test_id;
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
                ", puntos=" + puntos +
                ", test_id=" + test_id +
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
        if (test_id <= 0) {
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
        if (puntos == null) {
            throw new IllegalStateException("El ejercicio no tiene puntuación asignada");
        }
        return puntos >= puntuacionMinima;
    }
}