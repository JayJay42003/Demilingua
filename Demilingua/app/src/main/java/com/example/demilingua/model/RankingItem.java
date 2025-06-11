package com.example.demilingua.model;

public class RankingItem {
    private final String usuario;
    private final String idioma;
    private final int puntos;

    public RankingItem(String usuario, String idioma, int puntos) {
        this.usuario = usuario;
        this.idioma  = idioma;
        this.puntos  = puntos;
    }
    public String getUsuario(){ return usuario; }
    public String getIdioma() { return idioma; }
    public int    getPuntos() { return puntos; }
}
