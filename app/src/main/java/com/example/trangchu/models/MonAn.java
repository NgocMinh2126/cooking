package com.example.trangchu.models;

import java.util.List;

public class MonAn {
    private String id;
    private String TenMonAn;
    private List<NguyenLieu> listNgLieu;
    private List<String> CachLam;
    private String Anh;
    private int LuotThich;
    private int LuotChiaSe;
    private String clip;
    private int DaThich;
    public MonAn(String id,String tenMonAn, List<NguyenLieu> listNgLieu) {
        this.id=id;
        TenMonAn = tenMonAn;
        this.listNgLieu = listNgLieu;
    }

    public MonAn(String id, String tenMonAn, List<NguyenLieu> listNgLieu, List<String> cachLam, String anh, int luotThich, int luotChiaSe) {
        this.id = id;
        TenMonAn = tenMonAn;
        this.listNgLieu = listNgLieu;
        CachLam = cachLam;
        Anh = anh;
        LuotThich = luotThich;
        LuotChiaSe = luotChiaSe;
    }

    public MonAn(String id, String tenMonAn, String anh) {
        this.id = id;
        TenMonAn = tenMonAn;
        Anh = anh;
    }
    public MonAn(String id, String tenMonAn, String anh,int like) {
        this.id = id;
        TenMonAn = tenMonAn;
        Anh = anh;
        this.DaThich=like;
    }

    public void setDaThich(int daThich) {
        DaThich = daThich;
    }

    public int getDaThich() {
        return DaThich;
    }

    public MonAn(String id, String tenMonAn, List<NguyenLieu> listNgLieu, List<String> cachLam, String anh, int luotThich, int luotChiaSe, String clip) {
        this.id = id;
        TenMonAn = tenMonAn;
        this.listNgLieu = listNgLieu;
        CachLam = cachLam;
        Anh = anh;
        LuotThich = luotThich;
        LuotChiaSe = luotChiaSe;
        this.clip = clip;
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

    public String getAnh() {
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
