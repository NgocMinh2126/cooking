package com.example.trangchu.adapters;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trangchu.IRecycleViewClickListerner;
import com.example.trangchu.R;
import com.example.trangchu.models.MonAn;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class DSMonAnAdapter extends RecyclerView.Adapter<DSMonAnAdapter.DSMonAnViewHolder> {
    private ArrayList<MonAn>listMonAn;
    private IRecycleViewClickListerner i;
    public void setData(ArrayList<MonAn>list,IRecycleViewClickListerner i){
        this.listMonAn=list;
        this.i=i;
    }
    @NonNull
    @Override
    public DSMonAnViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.dsmonan_item,parent,false);
        return new DSMonAnViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DSMonAnViewHolder holder, int position) {
        MonAn monan=listMonAn.get(position);
        if(monan==null){
            return;
        }
        holder.dsmonan_btn_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(),"Bạn đã thích món " + monan.getTenMonAn(), Toast.LENGTH_SHORT).show();
            }
        });
        holder.dsmonan_tenmon_item.setText(monan.getTenMonAn());
        if(position%2==0){
            holder.dsmonan_item.setBackgroundResource(R.drawable.bg_dsmonan1);
            holder.dsmonan_img_item.setBackgroundResource(R.drawable.bg_imgfood);
        }else{
            holder.dsmonan_item.setBackgroundResource(R.drawable.bg_dsmonan2);
            holder.dsmonan_img_item.setBackgroundResource(R.drawable.bg_img02);
        }
        try {
            URL url=new URL(monan.getAnh());
            Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            holder.dsmonan_img_item.setImageBitmap(bmp);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        holder.dsmonan_item.setOnClickListener(new View.OnClickListener() {
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

    public class DSMonAnViewHolder extends RecyclerView.ViewHolder {
        private RelativeLayout dsmonan_item;
        private TextView dsmonan_tenmon_item;
        private CircleImageView dsmonan_img_item;
        private ToggleButton dsmonan_btn_like;
        public DSMonAnViewHolder(@NonNull View itemView) {
            super(itemView);
            dsmonan_btn_like=itemView.findViewById(R.id.dsmonan_btn_like_item);
            dsmonan_img_item=itemView.findViewById(R.id.dsmonan_img_item);
            dsmonan_tenmon_item=itemView.findViewById(R.id.dsmonan_tenmon_item);
            dsmonan_item=itemView.findViewById(R.id.dsmonan_item);
        }
    }
}
