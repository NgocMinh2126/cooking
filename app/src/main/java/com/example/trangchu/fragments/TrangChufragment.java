package com.example.trangchu.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trangchu.IRecycleViewClickListerner;
import com.example.trangchu.R;
import com.example.trangchu.adapters.HomNayAnGiAdapter;
import com.example.trangchu.adapters.LoaiMon_MonAnAdapter;
import com.example.trangchu.adapters.LoaiMon_TCAdapter;
import com.example.trangchu.adapters.TC_MonAnAdapter;
import com.example.trangchu.models.LoaiMon_MonAn;
import com.example.trangchu.models.MonAn;

import java.util.ArrayList;

public class TrangChufragment extends Fragment {
    ArrayList<MonAn>listMonAn01;
    TC_MonAnAdapter tc_monAnAdapter;
    RecyclerView rv_noibat;
    ArrayList<LoaiMon_MonAn>listLoaiMon;
    LoaiMon_TCAdapter trangchu_loaimon;
    RecyclerView rv_trangchu_loaimon;

    RecyclerView rv_trangchu_homnayangi;
    ArrayList<MonAn> listMonAnforHomnayangi;
    HomNayAnGiAdapter homayangiAdapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_trangchu,container,false);
        tc_monAnAdapter=new TC_MonAnAdapter();
        //Trang chu noi bat
        rv_noibat=view.findViewById(R.id.rv_noibat);
        listMonAn01=new ArrayList<MonAn>();
        listMonAn01=setDataforlistMonAn01();
        LinearLayoutManager noibat_layoutmanager
                = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
        tc_monAnAdapter.setData(listMonAn01, new IRecycleViewClickListerner() {
            @Override
            public void onItemClick(MonAn monan) {
                Toast.makeText(view.getContext(),monan.getTen(),Toast.LENGTH_SHORT).show();
            }
        });
        rv_noibat.setAdapter(tc_monAnAdapter);
        rv_noibat.setLayoutManager(noibat_layoutmanager);

        //Trang chu Hom nay an gi
        rv_trangchu_homnayangi=view.findViewById(R.id.rv_trangchu_homnayangi);
        listMonAnforHomnayangi=setDataforlistMonAn01();
        LinearLayoutManager Homnayangi_layoutmanager
                = new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false);
        rv_trangchu_homnayangi.setLayoutManager(Homnayangi_layoutmanager);
        homayangiAdapter=new HomNayAnGiAdapter();
        homayangiAdapter.setData(listMonAnforHomnayangi, new IRecycleViewClickListerner() {
            @Override
            public void onItemClick(MonAn monan) {
                Toast.makeText(view.getContext(),monan.getTen(),Toast.LENGTH_SHORT).show();
            }
        });
        rv_trangchu_homnayangi.setAdapter(homayangiAdapter);
        //Trang chu_ Loai mon
        listLoaiMon=new ArrayList<LoaiMon_MonAn>();
        rv_trangchu_loaimon=view.findViewById(R.id.rv_trangchu_loaimon);
        trangchu_loaimon=new LoaiMon_TCAdapter(view.getContext());
        listLoaiMon=setDataforlistLoaiMon();
        trangchu_loaimon.setData(listLoaiMon, new IRecycleViewClickListerner() {
            @Override
            public void onItemClick(MonAn monan) {
                Toast.makeText(view.getContext(),monan.getTen(),Toast.LENGTH_SHORT).show();
            }
        });
        LinearLayoutManager layoutforloaimon
                = new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false);
        rv_trangchu_loaimon.setLayoutManager(layoutforloaimon);
        rv_trangchu_loaimon.setAdapter(trangchu_loaimon);
        return view;
    }
    public ArrayList<MonAn> setDataforlistMonAn01(){
        ArrayList<MonAn>list=new ArrayList<MonAn>();
        MonAn m1=new MonAn("Chân giò kho tiêu",R.drawable.chan_gio_kho_tieu);
        MonAn m2=new MonAn("Gà hấp hành",R.drawable.tom_rang_muoi);
        MonAn m3=new MonAn("Canh khoai từ",R.drawable.chan_gio_kho_tieu);
        MonAn m4=new MonAn("Khổ qua nhồi thịt",R.drawable.chan_gio_kho_tieu);
        MonAn m5=new MonAn("Kem chuối",R.drawable.chan_gio_kho_tieu);
        list.add(m1);
        list.add(m2);
        list.add(m3);
        list.add(m4);
        list.add(m5);
        return list;
    }
    public ArrayList<LoaiMon_MonAn> setDataforlistLoaiMon(){
        ArrayList <LoaiMon_MonAn> list=new ArrayList<LoaiMon_MonAn>();
        ArrayList <MonAn>listmonan01=new ArrayList<MonAn>();
        ArrayList <MonAn>listmonan02=new ArrayList<MonAn>();
        ArrayList <MonAn>listmonan03=new ArrayList<MonAn>();
        MonAn m1=new MonAn("Chân giò kho tiêu",R.drawable.chan_gio_kho_tieu);
        MonAn m2=new MonAn("Gà hấp hành",R.drawable.tom_rang_muoi);
        MonAn m3=new MonAn("Canh khoai từ",R.drawable.chan_gio_kho_tieu);
        MonAn m4=new MonAn("Khổ qua nhồi thịt",R.drawable.chan_gio_kho_tieu);
        MonAn m5=new MonAn("Kem chuối",R.drawable.chan_gio_kho_tieu);
        listmonan01.add(m1);
        listmonan01.add(m2);
        listmonan01.add(m3);
        listmonan01.add(m4);
        listmonan01.add(m5);
        LoaiMon_MonAn loaimon1=new LoaiMon_MonAn("Loại 1",listmonan01);
        list.add(loaimon1);
        MonAn m6=new MonAn("Chân giò kho tiêu",R.drawable.chan_gio_kho_tieu);
        MonAn m7=new MonAn("Gà hấp hành",R.drawable.tom_rang_muoi);
        MonAn m8=new MonAn("Canh khoai từ",R.drawable.chan_gio_kho_tieu);
        MonAn m9=new MonAn("Khổ qua nhồi thịt",R.drawable.chan_gio_kho_tieu);
        MonAn m10=new MonAn("Kem chuối",R.drawable.chan_gio_kho_tieu);
        listmonan02.add(m6);
        listmonan02.add(m7);
        listmonan02.add(m8);
        listmonan02.add(m9);
        listmonan02.add(m10);
        LoaiMon_MonAn loaimon2=new LoaiMon_MonAn("Loại thứ 2",listmonan02);
        list.add(loaimon2);
        MonAn m11=new MonAn("Chân giò kho tiêu",R.drawable.chan_gio_kho_tieu);
        MonAn m12=new MonAn("Gà hấp hành",R.drawable.tom_rang_muoi);
        MonAn m13=new MonAn("Canh khoai từ",R.drawable.chan_gio_kho_tieu);
        MonAn m14=new MonAn("Khổ qua nhồi thịt",R.drawable.chan_gio_kho_tieu);
        MonAn m15=new MonAn("Kem chuối",R.drawable.chan_gio_kho_tieu);
        listmonan03.add(m11);
        listmonan03.add(m12);
        listmonan03.add(m13);
        listmonan03.add(m14);
        listmonan03.add(m15);
        LoaiMon_MonAn loaimon3=new LoaiMon_MonAn("Loại thứ 3",listmonan03);
        list.add(loaimon3);
        return list;
    }
}
