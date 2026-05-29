package com.example.demilingua.model;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Clase que representa el contenido específico de un ejercicio
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ObjetoEjercicio {
    private int id;
    private int ejercicioId;
    private String contenido;
    private String respuestaCorrecta;
    private List<String> opciones; // Para ejercicios de opción múltiple

    private static final ObjectMapper objectMapper = new ObjectMapper();

    // Constructores
    public ObjetoEjercicio() {
        // Constructor vacío necesario para frameworks
    }

    public ObjetoEjercicio(int id, int ejercicioId, String contenido, String respuestaCorrecta, List<String> opciones) {
        this.id = id;
        this.ejercicioId = ejercicioId;
        this.contenido = contenido;
        this.respuestaCorrecta = respuestaCorrecta;
        this.opciones = opciones;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getEjercicioId() {
        return ejercicioId;
    }

    public void setEjercicioId(int ejercicioId) {
        if (ejercicioId <= 0) {
            throw new IllegalArgumentException("El ID de ejercicio debe ser positivo");
        }
        this.ejercicioId = ejercicioId;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        if (contenido == null || contenido.trim().isEmpty()) {
            throw new IllegalArgumentException("El contenido no puede estar vacío");
        }
        this.contenido = contenido;
    }

    public String getRespuestaCorrecta() {
        return respuestaCorrecta;
    }

    public void setRespuestaCorrecta(String respuestaCorrecta) {
        this.respuestaCorrecta = respuestaCorrecta;
    }

    public List<String> getOpciones() {
        return opciones;
    }

    public void setOpciones(List<String> opciones) {
        this.opciones = opciones;
    }

    // Métodos para manejar opciones como JSON
    public String getOpcionesJson() {
        try {
            return opciones != null ? objectMapper.writeValueAsString(opciones) : null;
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error al serializar opciones", e);
        }
    }

    public void setOpcionesJson(String opcionesJson) {
        try {
            this.opciones = opcionesJson != null ?
                    objectMapper.readValue(opcionesJson, new TypeReference<List<String>>() {}) :
                    null;
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error al deserializar opciones", e);
        }
    }

    // Métodos útiles
    @Override
    public String toString() {
        return "ObjetoEjercicio{" +
                "id=" + id +
                ", ejercicioId=" + ejercicioId +
                ", contenido='" + contenido + '\'' +
                ", respuestaCorrecta='" + respuestaCorrecta + '\'' +
                ", opciones=" + opciones +
                '}';
    }

    /**
     * Valida que los campos obligatorios estén correctos
     * @throws IllegalArgumentException si algún campo requerido es inválido
     */
    public void validar() {
        if (contenido == null || contenido.trim().isEmpty()) {
            throw new IllegalArgumentException("El contenido del ejercicio es requerido");
        }
        if (ejercicioId <= 0) {
            throw new IllegalArgumentException("ID de ejercicio inválido");
        }
    }

    /**
     * Verifica si una respuesta dada es correcta
     * @param respuestaUsuario La respuesta a verificar
     * @return true si la respuesta es correcta (ignorando mayúsculas y espacios)
     */
    public boolean esRespuestaCorrecta(String respuestaUsuario) {
        if (respuestaCorrecta == null) return false;
        return respuestaCorrecta.trim().equalsIgnoreCase(respuestaUsuario.trim());
    }
}