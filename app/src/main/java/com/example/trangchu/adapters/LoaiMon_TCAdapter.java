package com.example.trangchu.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trangchu.IRecycleViewClickListerner;
import com.example.trangchu.R;
import com.example.trangchu.models.LoaiMon_MonAn;
import com.example.trangchu.models.MonAn;

import java.util.ArrayList;

public class LoaiMon_TCAdapter extends RecyclerView.Adapter<LoaiMon_TCAdapter.LoaiMon_TCViewHolder> {
    private Context context;
    private ArrayList<LoaiMon_MonAn> listLoaiMon;
    private IRecycleViewClickListerner i;
    public LoaiMon_TCAdapter(Context mcontext){
        this.context=mcontext;
    }
    public void setData(ArrayList<LoaiMon_MonAn> list,IRecycleViewClickListerner i){
        this.i=i;
        this.listLoaiMon=list;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public LoaiMon_TCViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.loaimon_trangchu_item,parent,false);
       return new LoaiMon_TCViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LoaiMon_TCViewHolder holder, int position) {
        LoaiMon_MonAn loaimon=listLoaiMon.get(position);
        if(loaimon==null){
            return;
        }
        holder.tv_ten_loaimon_trangchu_item.setText(loaimon.getTenLoaiMon());
        LoaiMon_MonAnAdapter loaiMon_monAnAdapter=new LoaiMon_MonAnAdapter();
        loaiMon_monAnAdapter.setData(loaimon.getListMonAn(),i);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        holder.rv_loaimon_trangchu_item.setLayoutManager(layoutManager);
        holder.rv_loaimon_trangchu_item.setAdapter(loaiMon_monAnAdapter);
    }

    @Override
    public int getItemCount() {
        if(listLoaiMon.size()!=0){
            return listLoaiMon.size();
        }
        return 0;
    }

    public class LoaiMon_TCViewHolder extends RecyclerView.ViewHolder{
        private LinearLayout trangchu_loaimon_item;
        private TextView tv_ten_loaimon_trangchu_item;
        private RecyclerView rv_loaimon_trangchu_item;
        private Button btn_tatca_loaimon_trangchu_item;
        public LoaiMon_TCViewHolder(@NonNull View itemView) {
            super(itemView);
            trangchu_loaimon_item=itemView.findViewById(R.id.loaimon_trangchu_item);
            tv_ten_loaimon_trangchu_item=itemView.findViewById(R.id.tv_ten_loaimon_trangchu_item);
            rv_loaimon_trangchu_item=itemView.findViewById(R.id.rv_trangchu_loaimon_item);
            btn_tatca_loaimon_trangchu_item=itemView.findViewById(R.id.btn_tatca_loaimon_trangchu_item);
        }
    }
}
