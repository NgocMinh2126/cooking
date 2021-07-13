package com.example.trangchu.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.trangchu.R;
import com.example.trangchu.models.NguyenLieu;

import java.util.ArrayList;

public class MonAn_NgLieuAdapter extends BaseAdapter {
    private ArrayList<NguyenLieu> arrayList;
    Context context;
    public MonAn_NgLieuAdapter(){}
    public MonAn_NgLieuAdapter(Context context, ArrayList<NguyenLieu> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }
    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView ==  null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.fragment_nglieu_item, parent, false);
        }

        //set cac thuoc tinh
        TextView tv_name=convertView.findViewById(R.id.nguyenlieu_item_ten);
        String ten=arrayList.get(position).getTen();
        tv_name.setText(ten);
        TextView tv_slg_dv = convertView.findViewById(R.id.nguyenlieu_item_soluong);
        int soluong = arrayList.get(position).getSoluong();
        String donvi = arrayList.get(position).getDonvi();
        if(soluong == 0){
            tv_slg_dv.setText(" ");
        }else{
            tv_slg_dv.setText(soluong + " "+ donvi);
        }
        return convertView;
    }
}
