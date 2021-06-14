package com.example.trangchu.adapters;

import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trangchu.ILoaiMonClickListener;
import com.example.trangchu.IRecycleViewClickListerner;
import com.example.trangchu.R;
import com.example.trangchu.models.LoaiMon;

import java.util.ArrayList;

public class Search_LoaiMon_Adapter extends RecyclerView.Adapter<Search_LoaiMon_Adapter.Search_LoaiMon_ViewHolder>{
    private ILoaiMonClickListener i;
    private ArrayList<LoaiMon> listLoaiMon;
    public void setData(ArrayList<LoaiMon>list,ILoaiMonClickListener i){
        this.listLoaiMon=list;
        this.i=i;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public Search_LoaiMon_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.search_loaimon_item,parent,false);
        return new Search_LoaiMon_ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Search_LoaiMon_ViewHolder holder, int position) {
        LoaiMon loaimon=listLoaiMon.get(position);
        if(loaimon==null){
            return;
        }
        holder.search_tenloaimon_item.setText(loaimon.getTenLoaiMon());
        holder.search_tenloaimon_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i.onItemClick(loaimon);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(listLoaiMon!=null){
            return listLoaiMon.size();
        }
        return 0;
    }

    public class Search_LoaiMon_ViewHolder extends RecyclerView.ViewHolder{
        private TextView search_tenloaimon_item;
        public Search_LoaiMon_ViewHolder(@NonNull View itemView) {
            super(itemView);
            search_tenloaimon_item=itemView.findViewById(R.id.search_ten_loaimon_item);
        }
    }
}
