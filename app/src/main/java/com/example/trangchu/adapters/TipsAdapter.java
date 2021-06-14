package com.example.trangchu.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trangchu.ITipsOnItemClick;
import com.example.trangchu.R;
import com.example.trangchu.models.Tips;

import java.util.List;

public class TipsAdapter extends RecyclerView.Adapter<TipsAdapter.TipsViewHolder> {
    private List<Tips>listTip;
    private ITipsOnItemClick i;
    public void setData(List<Tips>list,ITipsOnItemClick i){
        this.i=i;
        this.listTip=list;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public TipsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_tips_item,parent,false);
        return new TipsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TipsViewHolder holder, int position) {
        Tips tip=listTip.get(position);
        if(tip==null){
            return;
        }
        if(position%2==0){
            holder.tv_ten_tip_item.setBackgroundResource(R.drawable.bg_monan_trangchu_item);
        }else{
            holder.tv_ten_tip_item.setBackgroundResource(R.drawable.bg_monan_trangchu_item_2);
        }
        holder.img_tip_item.setImageResource(tip.getAnh());
        holder.tv_ten_tip_item.setText(tip.getTen());
        holder.ln_tip_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i.onItemClick(tip);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(listTip!=null){
            return listTip.size();
        }
        return 0;
    }

    public class TipsViewHolder extends RecyclerView.ViewHolder{
        LinearLayout ln_tip_item;
        private TextView tv_ten_tip_item;
        private ImageView img_tip_item;
        public TipsViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_ten_tip_item=itemView.findViewById(R.id.tips_tv_ten);
            img_tip_item=itemView.findViewById(R.id.tips_img);
            ln_tip_item=itemView.findViewById(R.id.tips_ln_item);
        }
    }
}
