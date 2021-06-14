package com.example.trangchu.models;

import java.util.ArrayList;

public class LoaiMon_MonAn {
    private String TenLoaiMon;
    private ArrayList<MonAn> listMonAn;

    public LoaiMon_MonAn(String tenLoaiMon, ArrayList<MonAn> listMonAn) {
        this.TenLoaiMon = tenLoaiMon;
        this.listMonAn = listMonAn;
    }

    public String getTenLoaiMon() {
        return TenLoaiMon;
    }

    public ArrayList<MonAn> getListMonAn() {
        return listMonAn;
    }
}
