package com.example.trangchu.adapters;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trangchu.IRecycleViewClickListerner;
import com.example.trangchu.R;
import com.example.trangchu.models.MonAn;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class ThucDonCuaBan_ChiTiet_Adapter extends RecyclerView.Adapter<ThucDonCuaBan_ChiTiet_Adapter.TDCB_ChiTietViewHolder> {
    private ArrayList<MonAn> listMonAn;
    private IRecycleViewClickListerner i;
    public void setData(ArrayList<MonAn>list,IRecycleViewClickListerner i){
        this.listMonAn=list;
        this.i=i;
    }
    @NonNull
    @Override
    public TDCB_ChiTietViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.thucdoncuaban_chitiet_item,parent,false);
        return new TDCB_ChiTietViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TDCB_ChiTietViewHolder holder, int position) {
        MonAn monan=listMonAn.get(position);
        if(monan==null){
            return;
        }
        holder.tv_tenmon.setText(monan.getTenMonAn());
        try {
            URL url=new URL(monan.getAnh());
            Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            holder.img_anhmonan.setImageBitmap(bmp);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        holder.rl_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i.onItemClick(monan);
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

    public class TDCB_ChiTietViewHolder extends RecyclerView.ViewHolder{
        private TextView tv_tenmon;
        private ImageView img_anhmonan;
        private RelativeLayout rl_item;
        public TDCB_ChiTietViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_tenmon=itemView.findViewById(R.id.thucdoncuaban_chitiet_tenmon);
            img_anhmonan=itemView.findViewById(R.id.thucdoncuaban_chitiet_anh);
            rl_item=itemView.findViewById(R.id.thucdoncuaban_chitiet_item);
        }
    }
}
