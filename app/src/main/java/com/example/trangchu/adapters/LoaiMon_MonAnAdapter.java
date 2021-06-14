package com.example.trangchu.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trangchu.IRecycleViewClickListerner;
import com.example.trangchu.R;
import com.example.trangchu.models.LoaiMon_MonAn;
import com.example.trangchu.models.MonAn;

import java.util.ArrayList;

public class LoaiMon_MonAnAdapter extends RecyclerView.Adapter<LoaiMon_MonAnAdapter.LoaiMon_MonAnViewHolder> {
    private ArrayList<MonAn> list;
    private IRecycleViewClickListerner i;
    public void setData(ArrayList<MonAn> list,IRecycleViewClickListerner i){
        this.list=list;
        this.i=i;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public LoaiMon_MonAnViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.loaimon_monan_item,parent,false);
        return new LoaiMon_MonAnViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LoaiMon_MonAnViewHolder holder, int position) {
        MonAn monan=list.get(position);
        if(monan==null){
            return;
        }
        if(position%2==0){
            holder.tv_ten_monan_loaimon_item.setBackgroundResource(R.drawable.bg_monan_trangchu_item);
        }else{
            holder.tv_ten_monan_loaimon_item.setBackgroundResource(R.drawable.bg_monan_trangchu_item_2);
        }
        holder.tv_ten_monan_loaimon_item.setText(monan.getTen());
        holder.img_monan_loaimon_item.setImageResource(monan.getAnh());
        holder.loaimon_monan_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i.onItemClick(monan);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(list.size()!=0){
            return list.size();
        }
        return 0;
    }

    public class LoaiMon_MonAnViewHolder extends RecyclerView.ViewHolder{
        private TextView tv_ten_monan_loaimon_item;
        private ImageView img_monan_loaimon_item;
        private LinearLayout loaimon_monan_item;
        public LoaiMon_MonAnViewHolder(@NonNull View itemView) {
            super(itemView);
            loaimon_monan_item=itemView.findViewById(R.id.loaimon_monan_item);
            tv_ten_monan_loaimon_item=itemView.findViewById(R.id.tv_ten_loaimon_monan_item);
            img_monan_loaimon_item=itemView.findViewById(R.id.img_loaimon_monan_item);
        }
    }
}
