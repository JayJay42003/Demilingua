/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.demilingua.backend.controllers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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

    @GetMapping("/courses")
    public Map<String, String> getCursos(@RequestBody int id) {
        Map<String, String> respuesta = new HashMap<>();

        // 1. Cadena SQL para recuperar el curso con ese id
        String sql = "SELECT id, nombre, descripcion, dificultad, idioma_id FROM curso WHERE id = ?";

        try (
            // 2. Abrir conexión
            Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
            // 3. Preparar statement con parámetro
            PreparedStatement ps = conn.prepareStatement(sql)
        ){
            ps.setInt(1, id);
            // 4. Ejecutar consulta
            ResultSet rs = ps.executeQuery();

            // 5. Si existe, rellenamos el Map con campo -> valor
            if (rs.next()) {
                respuesta.put("status", "ok");
                respuesta.put("id",            String.valueOf(rs.getInt("id")));
                respuesta.put("nombre",        rs.getString("nombre"));
                respuesta.put("descripcion",   rs.getString("descripcion"));
                respuesta.put("dificultad",    rs.getString("dificultad"));
                respuesta.put("idioma_id",     String.valueOf(rs.getInt("idioma_id")));
            }
            rs.close();
        } catch (Exception e) {
            respuesta.put("message", "Error en el servidor");
            respuesta.put("status", "error");
        }

        return respuesta;
    }
}
