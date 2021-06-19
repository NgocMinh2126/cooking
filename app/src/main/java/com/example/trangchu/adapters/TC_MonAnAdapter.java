package com.example.trangchu.adapters;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import com.example.trangchu.models.MonAn;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
//mon an noi bat trang chu
public class TC_MonAnAdapter extends RecyclerView.Adapter<TC_MonAnAdapter.TrangChuMonAnViewHolder>
{ private ArrayList<MonAn> listMonAn;
    private IRecycleViewClickListerner i;
    public void setData(ArrayList<MonAn> list,IRecycleViewClickListerner i){
        this.listMonAn=list;
        this.i=i;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public TrangChuMonAnViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.monan_trangchu_item,parent,false);
        return new TrangChuMonAnViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TrangChuMonAnViewHolder holder, int position) {
        MonAn monan=listMonAn.get(position);
        if(monan==null){
            return;
        }
        if(position%2==0){
            holder.trangchu_tv_ten_monan_item.setBackgroundResource(R.drawable.bg_monan_trangchu_item);
        }else{
            holder.trangchu_tv_ten_monan_item.setBackgroundResource(R.drawable.bg_monan_trangchu_item_2);
        }
        try {
            URL url=new URL(monan.getAnh());
            Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            holder.trangchu_img_monan_item.setImageBitmap(bmp);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        holder.trangchu_tv_ten_monan_item.setText(monan.getTenMonAn());
        holder.monannoibat_item.setOnClickListener(new View.OnClickListener() {
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

    public class TrangChuMonAnViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout monannoibat_item;
        private ImageView trangchu_img_monan_item;
        private TextView trangchu_tv_ten_monan_item;
        public TrangChuMonAnViewHolder(@NonNull View itemView) {
            super(itemView);
            monannoibat_item=itemView.findViewById(R.id.monannoibat_item);
            trangchu_img_monan_item=itemView.findViewById(R.id.trangchu_item_img_monan);
            trangchu_tv_ten_monan_item=itemView.findViewById(R.id.trangchu_item_ten_monan);
        }

    }

}
