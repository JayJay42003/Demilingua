package com.example.demilingua.model;

import java.time.LocalDateTime;

/**
 * Clase que representa un mensaje en el chat
 */
    public class MensajeChat {
    private int id;
    private int remitenteId;
    private int destinatarioId;
    private String mensaje;
    private LocalDateTime fechaEnvio;
    private boolean leido;

    // Constructores
    public MensajeChat() {
        this.fechaEnvio = LocalDateTime.now();
        this.leido = false;
    }

    public MensajeChat(int remitenteId, int destinatarioId, String mensaje) {
        this();
        this.remitenteId = remitenteId;
        this.destinatarioId = destinatarioId;
        this.mensaje = mensaje;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRemitenteId() {
        return remitenteId;
    }

    public void setRemitenteId(int remitenteId) {
        if (remitenteId <= 0) {
            throw new IllegalArgumentException("ID de remitente inválido");
        }
        this.remitenteId = remitenteId;
    }

    public int getDestinatarioId() {
        return destinatarioId;
    }

    public void setDestinatarioId(int destinatarioId) {
        if (destinatarioId <= 0) {
            throw new IllegalArgumentException("ID de destinatario inválido");
        }
        this.destinatarioId = destinatarioId;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        if (mensaje == null || mensaje.trim().isEmpty()) {
            throw new IllegalArgumentException("El mensaje no puede estar vacío");
        }
        if (mensaje.length() > 1000) {
            throw new IllegalArgumentException("El mensaje no puede exceder 1000 caracteres");
        }
        this.mensaje = mensaje;
    }

    public LocalDateTime getFechaEnvio() {
        return fechaEnvio;
    }

    public void setFechaEnvio(LocalDateTime fechaEnvio) {
        if (fechaEnvio == null) {
            throw new IllegalArgumentException("La fecha de envío no puede ser nula");
        }
        this.fechaEnvio = fechaEnvio;
    }

    public boolean isLeido() {
        return leido;
    }

    public void setLeido(boolean leido) {
        this.leido = leido;
    }

    // Métodos útiles
    @Override
    public String toString() {
        return "MensajeChat{" +
                "id=" + id +
                ", remitenteId=" + remitenteId +
                ", destinatarioId=" + destinatarioId +
                ", mensaje='" + mensaje + '\'' +
                ", fechaEnvio=" + fechaEnvio +
                ", leido=" + leido +
                '}';
    }

    /**
     * Marca el mensaje como leído
     */
    public void marcarComoLeido() {
        this.leido = true;
    }

    /**
     * Valida que los campos obligatorios estén correctos
     * @throws IllegalArgumentException si algún campo requerido es inválido
     */
    public void validar() {
        if (remitenteId <= 0 || destinatarioId <= 0) {
            throw new IllegalArgumentException("IDs de usuario inválidos");
        }
        if (mensaje == null || mensaje.trim().isEmpty()) {
            throw new IllegalArgumentException("El contenido del mensaje es requerido");
        }
        if (fechaEnvio == null) {
            throw new IllegalArgumentException("La fecha de envío es requerida");
        }
    }

    /**
     * Verifica si el mensaje fue enviado por un usuario específico
     * @param usuarioId El ID del usuario a verificar
     * @return true si el usuario es el remitente
     */
    public boolean esRemitente(int usuarioId) {
        return this.remitenteId == usuarioId;
    }

    /**
     * Verifica si el mensaje fue recibido por un usuario específico
     * @param usuarioId El ID del usuario a verificar
     * @return true si el usuario es el destinatario
     */
    public boolean esDestinatario(int usuarioId) {
        return this.destinatarioId == usuarioId;
    }
}