package com.example.groovetech.Modelo;

public class UtilizadorProfile {
    private int id;
    private String primeironome;
    private String apelido;
    private String codigopostal;
    private String rua;
    private String nif;
    private String dtanasc;
    private String dtaregisto;
    private String telefone;
    private String genero;
    private int user_id;

    public UtilizadorProfile(int id, String primeironome, String apelido, String codigopostal, String rua, String nif,
                             String dtanasc, String dtaregisto, String telefone, String genero, int user_id) {
        this.id = id;
        this.primeironome = primeironome;
        this.apelido = apelido;
        this.codigopostal = codigopostal;
        this.rua = rua;
        this.nif = nif;
        this.dtanasc = dtanasc;
        this.dtaregisto = dtaregisto;
        this.telefone = telefone;
        this.genero = genero;
        this.user_id = user_id;
    }

    public int getId() {
        return id;
    }

    public String getPrimeironome() {
        return primeironome;
    }

    public String getApelido() {
        return apelido;
    }

    public String getCodigopostal() {
        return codigopostal;
    }

    public String getRua() {
        return rua;
    }

    public String getNif() {
        return nif;
    }

    public String getDtanasc() {
        return dtanasc;
    }

    public String getDtaregisto() {
        return dtaregisto;
    }

    public String getTelefone() {
        return telefone;
    }

    public String getGenero() {
        return genero;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setPrimeironome(String primeironome) {
        this.primeironome = primeironome;
    }

    public void setApelido(String apelido) {
        this.apelido = apelido;
    }

    public void setCodigopostal(String codigopostal) {
        this.codigopostal = codigopostal;
    }

    public void setRua(String rua) {
        this.rua = rua;
    }

    public void setNif(String nif) {
        this.nif = nif;
    }

    public void setDtanasc(String dtanasc) {
        this.dtanasc = dtanasc;
    }

    public void setDtaregisto(String dtaregisto) {
        this.dtaregisto = dtaregisto;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }
}
