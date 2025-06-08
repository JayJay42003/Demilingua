/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.demilingua.backend.controllers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Joel
 */
@RestController
@RequestMapping("/api")
public class UserController {
    
    private final String DB_URL = "jdbc:mysql://localhost:3306/demilingua";
    private final String DB_USER = "user";
    private final String DB_PASS = "";

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody Map<String, String> datos) {

        String usuario = datos.get("username");
        String password = datos.get("password");
        Map<String, String> respuesta = new HashMap<>();
        
        try (Connection conexion = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS); PreparedStatement sentencia = conexion.prepareStatement(
                "SELECT id, nombre FROM usuario WHERE correo = ? AND contrasena = ?")) {

            // Usamos PreparedStatement para evitar SQL injection
            sentencia.setString(1, usuario);
            sentencia.setString(2, password);

            ResultSet rs = sentencia.executeQuery();

            if (rs.next()) {
                // Usuario válido
                respuesta.put("status", "ok");
                respuesta.put("user_id", String.valueOf(rs.getInt("id")));
            } else {
                respuesta.put("status", "error");
                respuesta.put("message", "Usuario o contraseña incorrectos");
            }

        } catch (SQLException ex) {
            Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, null, ex);
            respuesta.put("status", "error");
            respuesta.put("message", "Error en el servidor");
        }

        return respuesta;
    }
    
    @PostMapping("/register")
    public Map<String, String> registrar(@RequestBody Map<String, String> datos) {
        String nombre = datos.get("nombre");
        String correo = datos.get("correo");
        String password = datos.get("contrasena");
        
        Map<String, String> respuesta = new HashMap<>();

        try (Connection conexion = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
            
            // 1. Verificar si el correo ya existe
            if(usuarioExiste(conexion, correo)) {
                respuesta.put("status", "error");
                respuesta.put("message", "El correo ya está registrado");
                return respuesta;
            }

            // 2. Registrar nuevo usuario
            try (PreparedStatement ps = conexion.prepareStatement(
                    "INSERT INTO usuario (nombre, correo, contrasena) VALUES (?, ?, ?)")) {
                
                ps.setString(1, nombre);
                ps.setString(2, correo);
                ps.setString(3, password); // En producción usa BCrypt
                
                int affectedRows = ps.executeUpdate();
                
                if(affectedRows > 0) {
                        respuesta.put("status", "ok");
                        respuesta.put("message", "Registro exitoso");
                } else {
                    respuesta.put("status", "error");
                    respuesta.put("message", "No se pudo completar el registro");
                }
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, null, ex);
            respuesta.put("status", "error");
            respuesta.put("message", "Error en el servidor");
        }

        return respuesta;
    }

    private boolean usuarioExiste(Connection conexion, String correo) throws SQLException {
        try (PreparedStatement ps = conexion.prepareStatement(
                "SELECT id FROM usuario WHERE correo = ?")) {
            ps.setString(1, correo);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next(); // Retorna true si encuentra un usuario
            }
        }
    }

}
