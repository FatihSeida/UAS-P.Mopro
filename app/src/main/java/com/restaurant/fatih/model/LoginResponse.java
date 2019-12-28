package com.restaurant.fatih.model;

import com.google.gson.annotations.Expose;

public class LoginResponse {
    @Expose
    private Login data;
    @Expose
    private String message;
    @Expose
    private String status;
    public Login getLogin() {
        return data;
    }
    public void setLogin(Login data) {
        this.data = data;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
}
