package com.example.trangchu.models;

public class MonAn {
    private String Ten;
    private int Anh;
    public MonAn(String ten, int anh) {
        this.Ten = ten;
        this.Anh = anh;
    }
    public String getTen() {
        return Ten;
    }
    public int getAnh() {
        return Anh;
    }
}
