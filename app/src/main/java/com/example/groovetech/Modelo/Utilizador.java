package com.example.groovetech.Modelo;

public class Utilizador {
    private int id;
    private String username;
    private String email;
    private String auth_key;
    private String password_hash;
    private String password_reset_token;
    private String status;
    private String created_at;
    private String updated_at;
    private String verification_token;

    public Utilizador(int id, String username, String email, String auth_key, String password_hash, String password_reset_token,
                      String status, String created_at, String updated_at, String verification_token) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.auth_key = auth_key;
        this.password_hash = password_hash;
        this.password_reset_token = password_reset_token;
        this.status = status;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.verification_token = verification_token;
    }

    public Utilizador(String username, String email) {
        this.username = username;
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getAuth_key() {
        return auth_key;
    }

    public String getPassword_hash() {
        return password_hash;
    }

    public String getPassword_reset_token() {
        return password_reset_token;
    }

    public String getStatus() {
        return status;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public String getVerification_token() {
        return verification_token;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAuth_key(String auth_key) {
        this.auth_key = auth_key;
    }

    public void setPassword_hash(String password_hash) {
        this.password_hash = password_hash;
    }

    public void setPassword_reset_token(String password_reset_token) {
        this.password_reset_token = password_reset_token;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public void setVerification_token(String verification_token) {
        this.verification_token = verification_token;
    }

    @Override
    public String toString() {
        return "Utilizador{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", auth_key='" + auth_key + '\'' +
                ", password_hash='" + password_hash + '\'' +
                ", password_reset_token='" + password_reset_token + '\'' +
                ", status='" + status + '\'' +
                ", created_at='" + created_at + '\'' +
                ", updated_at='" + updated_at + '\'' +
                ", verification_token='" + verification_token + '\'' +
                '}';
    }
}
