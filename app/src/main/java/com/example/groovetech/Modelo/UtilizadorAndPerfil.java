package com.example.groovetech.Modelo;

public class UtilizadorAndPerfil {
    private Utilizador utilizador;
    private Perfil perfil;

    public UtilizadorAndPerfil(Utilizador utilizador, Perfil perfil) {
        this.utilizador = utilizador;
        this.perfil = perfil;
    }
    public Utilizador getUtilizador() {
        return utilizador;
    }
    public Perfil getPerfil() {
        return perfil;
    }
}

