package com.example.auth_microservice.entity;

import java.util.Objects;

public class RegisterRequest {

    private String username;
    private String password;
    private String address;
    private int age;

    public RegisterRequest() {
    }

    public RegisterRequest(String username, String password, String address, int age) {
        this.username = username;
        this.password = password;
        this.address = address;
        this.age = age;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RegisterRequest request = (RegisterRequest) o;
        return Objects.equals(username, request.username) && Objects.equals(password, request.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, password);
    }
}