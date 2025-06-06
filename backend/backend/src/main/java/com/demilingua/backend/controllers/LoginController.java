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
public class LoginController {
    
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
                respuesta.put("token", generarToken(rs.getInt("id"), rs.getString("nombre")));
                respuesta.put("user_id", String.valueOf(rs.getInt("id")));
            } else {
                respuesta.put("status", "error");
                respuesta.put("message", "Usuario o contraseña incorrectos");
            }

        } catch (SQLException ex) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
            respuesta.put("status", "error");
            respuesta.put("message", "Error en el servidor");
        }

        return respuesta;
    }
    
    private String generarToken(int userId, String username) {
        // Implementación básica - en producción usa JWT
        return "tok-" + userId + "-" + username.hashCode();
    }

}
