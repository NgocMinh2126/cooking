package com.example.trangchu.models;

public class User {
    private String id;
    private String hoten;
    private String Anh;
    private String mail;
    private String ngaysinh;
    private String username;
    private String pass;

    public User(String id, String hoten, String anh, String mail, String ngaysinh) {
        this.id = id;
        this.hoten = hoten;
        Anh = anh;
        this.mail = mail;
        this.ngaysinh = ngaysinh;
    }

    public String getId() {
        return id;
    }

    public String getHoten() {
        return hoten;
    }

    public String getAnh() {
        return Anh;
    }

    public String getMail() {
        return mail;
    }

    public String getNgaysinh() {
        return ngaysinh;
    }
}
