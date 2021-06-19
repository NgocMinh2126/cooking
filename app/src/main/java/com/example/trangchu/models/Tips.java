package com.example.trangchu.models;

public class Tips {
    private String Ten;
    private int Anh;
    private int id;
    private String NoiDung;
    public Tips(){}
    public Tips(int id,String ten, int anh) {
        this.id=id;
        Ten = ten;
        Anh = anh;
    }

    public Tips(String ten, int anh, String noiDung) {
        Ten = ten;
        Anh = anh;
        NoiDung = noiDung;
    }

    public int getId() {
        return id;
    }

    public String getNoiDung() {
        return NoiDung;
    }
    public void setData(String ten, int anh){
        this.Anh=anh;
        this.Ten=ten;
    }
    public void setNoiDung(String noiDung) {
        NoiDung = noiDung;
    }

    public String getTen() {
        return Ten;
    }

    public int getAnh() {
        return Anh;
    }
}
