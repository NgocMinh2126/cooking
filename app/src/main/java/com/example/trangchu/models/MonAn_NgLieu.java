package com.example.trangchu.models;

import java.util.List;

public class MonAn_NgLieu {
    private String TenMonAn;
    private List<NguyenLieu> listNgLieu;
    private String id;
    private List<String> CachLam;
    private int Anh;
    private int LuotThich;
    private int LuotChiaSe;
    private String clip;
    public MonAn_NgLieu(String tenMonAn, List<NguyenLieu> listNgLieu) {
        TenMonAn = tenMonAn;
        this.listNgLieu = listNgLieu;
    }

    public String getTenMonAn() {
        return TenMonAn;
    }

    public List<NguyenLieu> getListNgLieu() {
        return listNgLieu;
    }

    public String getId() {
        return id;
    }

    public List<String> getCachLam() {
        return CachLam;
    }

    public int getAnh() {
        return Anh;
    }

    public int getLuotThich() {
        return LuotThich;
    }

    public int getLuotChiaSe() {
        return LuotChiaSe;
    }

    public String getClip() {
        return clip;
    }
}
