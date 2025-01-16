package com.example.groovetech.Modelo;

public class Pagamento {
    int id;
    String metodoPagamento;

    float valor;

    public Pagamento(int id, String metodoPagamento) {
        this.id = id;
        this.metodoPagamento = metodoPagamento;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMetodoPagamento() {
        return metodoPagamento;
    }

    public void setMetodoPagamento(String metodoPagamento) {
        this.metodoPagamento = metodoPagamento;
    }

    public float getValor() {
        return valor;
    }

    public void setValor(float valor) {
        this.valor = valor;
    }

    @Override
    public String toString() {
        return metodoPagamento; // Mostra o método de pagamento no Spinner
    }
}
