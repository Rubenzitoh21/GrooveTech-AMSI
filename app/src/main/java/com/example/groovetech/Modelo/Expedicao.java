package com.example.groovetech.Modelo;

public class Expedicao {
    int id;
    String metodoExpedicao;

    public Expedicao(int id, String metodoExpedicao) {
        this.id = id;
        this.metodoExpedicao = metodoExpedicao;
    }

    public int getId() {
        return id;
    }

    public String getMetodoExpedicao() {
        return metodoExpedicao;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setMetodoExpedicao(String metodoExpedicao) {
        this.metodoExpedicao = metodoExpedicao;
    }

    @Override
    public String toString() {
        return metodoExpedicao; // Mostra o método de expedição no Spinner
    }
}
