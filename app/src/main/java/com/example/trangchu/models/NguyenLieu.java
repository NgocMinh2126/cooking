package com.example.trangchu.models;

public class NguyenLieu {
    private String Ten;
    private int Soluong;
    private String Donvi;
    private String id;

    public NguyenLieu(String ten, int soluong, String donvi) {
        Ten = ten;
        Soluong = soluong;
        Donvi = donvi;
    }

    public NguyenLieu(String ten, String id) {
        Ten = ten;
        this.id = id;
    }

    public NguyenLieu(String ten) {
        Ten = ten;
    }

    public String getTen() {
        return Ten;
    }

    public int getSoluong() {
        return Soluong;
    }

    public String getDonvi() {
        return Donvi;
    }

    public String getId() {
        return id;
    }

    public void setTen(String ten) {
        Ten = ten;
    }

    public void setSoluong(int soluong) {
        Soluong = soluong;
    }

    public void setDonvi(String donvi) {
        Donvi = donvi;
    }

    public void setId(String id) {
        this.id = id;
    }
}
