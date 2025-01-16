package com.example.groovetech.Modelo;

public class Fatura {
    int id, userID, pagamentosID, expedicoesID;
    String data, status;
    float valorTotal;

    public Fatura(int id, String data, float valorTotal, String status, int userID, int pagamentosID, int expedicoesID) {
        this.id = id;
        this.data = data;
        this.valorTotal = valorTotal;
        this.status = status;
        this.userID = userID;
        this.pagamentosID = pagamentosID;
        this.expedicoesID = expedicoesID;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public float getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(float valorTotal) {
        this.valorTotal = valorTotal;
    }

    @Override
    public String toString() {
        return "Fatura{" +
                "id=" + id +
                ", userID=" + userID +
                ", data='" + data + '\'' +
                ", status='" + status + '\'' +
                ", valorTotal=" + valorTotal +
                '}';
    }
}
