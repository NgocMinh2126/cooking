package com.example.trangchu.adapters;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.trangchu.R;
import com.example.trangchu.models.ThucDon;

import java.util.ArrayList;

public class ThucDonCuaBanAdapter extends BaseAdapter {
    ArrayList<ThucDon> listThucDon;
    public ThucDonCuaBanAdapter(ArrayList<ThucDon> list){
        this.listThucDon=list;
    }
    @Override
    public int getCount() {
        return listThucDon.size();
    }

    @Override
    public Object getItem(int position) {
        return listThucDon.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        if (convertView == null) {
            view = View.inflate(parent.getContext(), R.layout.thucdon_cuaban_item, null);
        }
        else view = convertView;
        ThucDon thucdon= listThucDon.get(position);
        if(thucdon!=null){
            ((TextView) view.findViewById(R.id.thucdoncuaban_ngay_item)).setText(thucdon.getNgay());
            ((TextView) view.findViewById(R.id.thucdoncuaban_buoi_item)).setText(thucdon.getBuoi());
        }
        return view;
    }
}
