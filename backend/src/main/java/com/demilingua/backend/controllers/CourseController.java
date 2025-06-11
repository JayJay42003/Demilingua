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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Joel
 */
@RestController
@RequestMapping("/api")
public class CourseController {

    private final String DB_URL = "jdbc:mysql://localhost:3306/demilingua";
    private final String DB_USER = "user";
    private final String DB_PASS = "";

    @GetMapping(value = "/courses", produces = "application/json")
    public List<Map<String, String>> getCursos(@RequestParam("idiomaId") int idiomaId) {

        List<Map<String, String>> cursos = new ArrayList<>();

        String sql = "SELECT id, nombre, descripcion, dificultad, idioma_id FROM curso WHERE idioma_id = ?";
        try (Connection c = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS); PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, idiomaId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Map<String, String> row = new HashMap<>();
                row.put("id", String.valueOf(rs.getInt("id")));
                row.put("nombre", rs.getString("nombre"));
                row.put("descripcion", rs.getString("descripcion"));
                row.put("dificultad", rs.getString("dificultad"));
                row.put("idioma_id", String.valueOf(rs.getInt("idioma_id")));
                cursos.add(row);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return cursos;
    }

}
