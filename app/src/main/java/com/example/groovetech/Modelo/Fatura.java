package com.example.groovetech.Modelo;

public class Fatura {
    int id, userID;
    String data, status;
    float valorTotal;

    public Fatura(int id, int userID, String data, String status, float valorTotal) {
        this.id = id;
        this.userID = userID;
        this.data = data;
        this.status = status;
        this.valorTotal = valorTotal;
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
