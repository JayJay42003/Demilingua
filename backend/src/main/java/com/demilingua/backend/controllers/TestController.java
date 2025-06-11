package com.demilingua.backend.controllers;

import org.springframework.web.bind.annotation.*;

import java.sql.*;
import java.util.*;

@RestController
@RequestMapping("/api")
public class TestController {

    private final String DB_URL  = "jdbc:mysql://localhost:3306/demilingua";
    private final String DB_USER = "user";
    private final String DB_PASS = "";

    /* ───────── Test aleatorio ───────── */
    @GetMapping(value = "/test", produces = "application/json")
    public Map<String,String> getRandomTest(@RequestParam("cursoId") int cursoId) {

        String sql = "SELECT id, curso_id, titulo FROM test WHERE curso_id = ? ORDER BY RAND() LIMIT 1";

        try (Connection c = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, cursoId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Map<String,String> test = new HashMap<>();
                test.put("id"      , String.valueOf(rs.getInt("id")));
                test.put("curso_id", String.valueOf(rs.getInt("curso_id")));
                test.put("titulo"  , rs.getString("titulo"));
                return test;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Collections.emptyMap();
    }

    /* ───────── Lista de ejercicios del test ───────── */
    @GetMapping(value = "/exercises", produces = "application/json")
    public List<Map<String,String>> getEjercicios(@RequestParam("testId") int testId) {

        String sql = "SELECT e.id AS ejercicioId, e.tipo AS tipo, e.puntuacion AS puntos, "
                   + "o.contenido AS contenido, o.respuesta_correcta AS respuesta, o.opciones AS opciones "
                   + "FROM ejercicio e JOIN objeto_ejercicio o ON o.ejercicio_id = e.id "
                   + "WHERE e.test_id = ? ORDER BY e.id";

        List<Map<String,String>> ejercicios = new ArrayList<>();

        try (Connection c = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, testId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Map<String,String> row = new LinkedHashMap<>();
                row.put("ejercicioId", String.valueOf(rs.getInt("ejercicioId")));
                row.put("tipo"       , rs.getString("tipo"));
                row.put("puntos"     , String.valueOf(rs.getInt("puntos")));
                row.put("contenido"  , rs.getString("contenido"));
                row.put("respuesta"  , rs.getString("respuesta"));
                row.put("opciones"   , rs.getString("opciones"));
                ejercicios.add(row);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return ejercicios;
    }
}
