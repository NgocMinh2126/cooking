package com.example.trangchu.models;

public class ThucDon {
    private String id;
    private String ngay;
    private String buoi;

    public ThucDon(String id, String ngay, String buoi) {
        this.id = id;
        this.ngay = ngay;
        this.buoi = buoi;
    }

    public String getId() {
        return id;
    }

    public String getNgay() {
        return ngay;
    }

    public String getBuoi() {
        return buoi;
    }
}
