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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
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
public class RankingController {

    private final String DB_URL = "jdbc:mysql://localhost:3306/demilingua";
    private final String DB_USER = "user";
    private final String DB_PASS = "";

    
    @GetMapping(value = "/ranking", produces = "application/json")
    public List<Map<String, String>> getRanking() {

        String sql = "SELECT u.nombre AS usuario, i.nombre AS idioma, ui.puntos AS puntos "
           + "FROM usuario_idioma ui "
           + "JOIN usuario u ON u.id = ui.usuario_id "
           + "JOIN idioma  i ON i.id = ui.idioma_id "
           + "ORDER BY i.nombre  ASC,"
           + "ui.puntos DESC";


        List<Map<String, String>> lista = new ArrayList<>();

        try (Connection c = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS); PreparedStatement ps = c.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Map<String, String> row = new HashMap<>();
                row.put("usuario", rs.getString("usuario"));
                row.put("idioma", rs.getString("idioma"));
                row.put("puntos", String.valueOf(rs.getInt("puntos")));
                lista.add(row);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return lista;
    }

    @RestController
    @RequestMapping("/api")
    public class PuntosController {

        private final String DB_URL = "jdbc:mysql://localhost:3306/demilingua";
        private final String DB_USER = "user";
        private final String DB_PASS = "";

        
        @PostMapping(value = "/puntos", produces = "application/json")
        public Map<String, String> guardarPuntos(@RequestBody Map<String, Integer> body) {

            int usuarioId = body.getOrDefault("usuarioId", 0);
            int idiomaId = body.getOrDefault("idiomaId", 0);
            int puntos = body.getOrDefault("puntos", 0);

            String sql = "INSERT INTO usuario_idioma (usuario_id, idioma_id, puntos) VALUES (?,?,?) "
                    + "ON DUPLICATE KEY UPDATE puntos = puntos + VALUES(puntos)";

            try (Connection c = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS); PreparedStatement ps = c.prepareStatement(sql)) {

                ps.setInt(1, usuarioId);
                ps.setInt(2, idiomaId);
                ps.setInt(3, puntos);
                ps.executeUpdate();

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            return Map.of("status", "ok");
        }
    }

}
