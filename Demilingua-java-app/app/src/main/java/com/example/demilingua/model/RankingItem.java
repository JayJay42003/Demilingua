package com.example.demilingua.model;

public class RankingItem {
    private final String nombre;
    private final String racha;
    private final String division;

    public RankingItem(String nombre, String racha, String division) {
        this.nombre = nombre;
        this.racha  = racha;
        this.division  = division;
    }
    public String getNombre(){ return nombre; }
    public String getRacha() { return racha; }
    public String getDivision() { return division; }
}
