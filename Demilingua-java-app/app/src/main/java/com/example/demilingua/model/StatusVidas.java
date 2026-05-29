package com.example.demilingua.model;

import com.google.gson.annotations.SerializedName;

public class StatusVidas {
    @SerializedName("vidas")
    private String vidas;
    
    @SerializedName("racha")
    private String racha;

    @SerializedName("division_id")
    private String divisionId;

    public String getVidas() { return vidas; }
    public void setVidas(String vidas) { this.vidas = vidas; }
    public String getRacha() { return racha; }
    public void setRacha(String racha) { this.racha = racha; }
    public String getDivisionId() { return divisionId; }
    public void setDivisionId(String divisionId) { this.divisionId = divisionId; }
}