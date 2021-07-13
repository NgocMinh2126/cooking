package com.example.trangchu.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.trangchu.R;

import java.util.ArrayList;

public class MonAn_CachLamAdapter extends BaseAdapter {
    Context context;
    ArrayList<String> arrayList;
    ListView lvCachlam;
    public MonAn_CachLamAdapter(Context context,ArrayList<String> arrayList){
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
            convertView = LayoutInflater.from(context).inflate(R.layout.fragment_cachlam_item, parent, false);
        }
        TextView tv_text = convertView.findViewById(R.id.cachlam_item_buoc);
        String text = arrayList.get(position);
        tv_text.setText(text);
        return convertView;
    }
}
