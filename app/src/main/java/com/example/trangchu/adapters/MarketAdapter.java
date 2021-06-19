package com.example.trangchu.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trangchu.R;
import com.example.trangchu.models.NguyenLieu;

import java.util.List;


public class MarketAdapter extends RecyclerView.Adapter<MarketAdapter.MarketViewHolder> {
    private List<NguyenLieu> listNguyenlieu;
    public void setData(List<NguyenLieu>list){
        this.listNguyenlieu=list;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public MarketViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_market_item,parent,false);
        return new MarketViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MarketViewHolder holder, int position) {
        NguyenLieu nl=listNguyenlieu.get(position);
        if(nl==null){
            return;
        }
        holder.tv_tennglieu.setText(nl.getTen());
        if(nl.getSoluong()!=0){
            String str=nl.getSoluong()+" "+nl.getDonvi();
            holder.tv_slnglieu.setText(str);
        }else{
            holder.tv_slnglieu.setText("Tùy lượng dùng");
        }

    }

    @Override
    public int getItemCount() {
        if(listNguyenlieu!=null){
            return listNguyenlieu.size();
        }
        return 0;
    }

    public class MarketViewHolder extends RecyclerView.ViewHolder{
        private TextView tv_tennglieu;
        private TextView tv_slnglieu;
        public MarketViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_slnglieu=itemView.findViewById(R.id.market_item_sl);
            tv_tennglieu=itemView.findViewById(R.id.market_item_ten_nglieu);
        }
    }
}
