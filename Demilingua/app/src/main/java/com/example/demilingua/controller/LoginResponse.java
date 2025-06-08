package com.example.demilingua.controller;

public class LoginResponse {
    private String status;
    private String message;
    private int userId;
    private String nombre;

    // Constructor vacío necesario para Retrofit
    public LoginResponse() {}

    // Getters y Setters
    public boolean isSuccess() {
        return "ok".equals(status);
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}