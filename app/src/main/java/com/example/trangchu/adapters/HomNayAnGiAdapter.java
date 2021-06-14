package com.example.trangchu.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trangchu.IRecycleViewClickListerner;
import com.example.trangchu.R;
import com.example.trangchu.models.MonAn;

import java.util.ArrayList;

public class HomNayAnGiAdapter extends RecyclerView.Adapter<HomNayAnGiAdapter.HomNayAnGiViewHolder> {
    private ArrayList<MonAn> listMonAn;
    private IRecycleViewClickListerner irvclicklisterner;
    public void setData(ArrayList<MonAn>listMonAn,IRecycleViewClickListerner i){
        this.listMonAn=listMonAn;
        this.irvclicklisterner=i;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public HomNayAnGiViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.homnayangi_item,parent,false);
        return new HomNayAnGiViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomNayAnGiViewHolder holder, int position) {
        final MonAn monan=listMonAn.get(position);
        if(monan==null){
            return;
        }
        holder.img_monan_homnayangi.setImageResource(monan.getAnh());
        holder.tv_ten_monan_homnayangi.setText(monan.getTen());
        holder.homnayangi_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                irvclicklisterner.onItemClick(monan);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(listMonAn!=null){
            return listMonAn.size();
        }
        return 0;
    }

    public class HomNayAnGiViewHolder extends RecyclerView.ViewHolder{
        private TextView tv_ten_monan_homnayangi;
        private ImageView img_monan_homnayangi;
        private LinearLayout homnayangi_item;
        public HomNayAnGiViewHolder(@NonNull View itemView) {
            super(itemView);
            homnayangi_item=itemView.findViewById(R.id.homnayangi_item);
            tv_ten_monan_homnayangi=itemView.findViewById(R.id.tv_ten_monan_homnayangi);
            img_monan_homnayangi=itemView.findViewById(R.id.img_monan_homnayangi);
        }
    }
}
