package com.example.groovetech.Modelo;

public class Pagamento {
    int id, faturaID;
    String data, metodoPagamento;

    float valor;

    public Pagamento(int id, int faturaID, String data, String metodoPagamento, float valor) {
        this.id = id;
        this.faturaID = faturaID;
        this.data = data;
        this.metodoPagamento = metodoPagamento;
        this.valor = valor;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFaturaID() {
        return faturaID;
    }

    public void setFaturaID(int faturaID) {
        this.faturaID = faturaID;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
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
        return "Pagamento{" +
                "id=" + id +
                ", faturaID=" + faturaID +
                ", data='" + data + '\'' +
                ", metodoPagamento='" + metodoPagamento + '\'' +
                ", valor=" + valor +
                '}';
    }
}
