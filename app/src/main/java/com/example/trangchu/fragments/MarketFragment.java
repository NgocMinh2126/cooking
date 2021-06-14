package com.example.trangchu.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trangchu.R;
import com.example.trangchu.adapters.MarketAdapter;
import com.example.trangchu.models.MonAn_NgLieu;
import com.example.trangchu.models.NguyenLieu;

import java.util.ArrayList;
import java.util.List;

public class MarketFragment extends Fragment {
    RecyclerView rv_market;
    List<NguyenLieu> listNgLieu;
    MarketAdapter marketAdapter;
    TableLayout tb_nglieu;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_market,container,false);
        rv_market=view.findViewById(R.id.rv_market);
        LinearLayoutManager layoutManager=new LinearLayoutManager(view.getContext(),LinearLayoutManager.VERTICAL,false);
        rv_market.setLayoutManager(layoutManager);
        listNgLieu=setNgLieu();
        marketAdapter=new MarketAdapter();
        marketAdapter.setData(listNgLieu);
        rv_market.setAdapter(marketAdapter);
        return view;
    }
    public List<NguyenLieu> setNgLieu(){
        List<NguyenLieu>list=new ArrayList<NguyenLieu>();
        list.add(new NguyenLieu("Nguyen lieu 1",1,"bó"));
        list.add(new NguyenLieu("Nguyen lieu 2",2,"muỗng cà phê"));
        list.add(new NguyenLieu("Nguyen lieu 3",3,"muỗng canh"));
        list.add(new NguyenLieu("Nguyen lieu 4",4,"kg"));
        list.add(new NguyenLieu("Nguyen lieu 5",5,"gram"));
        return list;
    }
    public List<MonAn_NgLieu> setMonAn(){
        List<MonAn_NgLieu> list=new ArrayList<MonAn_NgLieu>();
        List<NguyenLieu> listNgl=new ArrayList<NguyenLieu>();
        listNgl=setNgLieu();
        list.add(new MonAn_NgLieu("gà hấp xả",listNgl));
        list.add(new MonAn_NgLieu("tôm chiên giòn",listNgl));
        list.add(new MonAn_NgLieu("Bí đỏ hầm hạt sen",listNgl));
        list.add(new MonAn_NgLieu("Cháo vịt",listNgl));
        list.add(new MonAn_NgLieu("Cháo gà",listNgl));

        return list;
    }

}
