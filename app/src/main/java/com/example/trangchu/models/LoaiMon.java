package com.example.trangchu.models;

import java.util.ArrayList;

public class LoaiMon {
    private String TenLoaiMon;
    private String id;
    private ArrayList<MonAn> listMonAn;
    public LoaiMon(String tenLoaiMon,String id) {
        TenLoaiMon = tenLoaiMon;
        this.id=id;
    }

    public LoaiMon(String tenLoaiMon, String id, ArrayList<MonAn> listMonAn) {
        TenLoaiMon = tenLoaiMon;
        this.id = id;
        this.listMonAn = listMonAn;
    }

    public ArrayList<MonAn> getListMonAn() {
        return listMonAn;
    }

    public String getTenLoaiMon() {
        return TenLoaiMon;
    }

    public String getId() {
        return id;
    }
}
