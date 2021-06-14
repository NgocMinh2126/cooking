package com.example.trangchu.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trangchu.IRecycleViewClickListerner;
import com.example.trangchu.R;
import com.example.trangchu.models.MonAn;

import java.util.ArrayList;

public class Search_Monan_Adapter extends RecyclerView.Adapter<Search_Monan_Adapter.Search_Monan_ViewHolder> implements Filterable {
    private IRecycleViewClickListerner i;
    private ArrayList<MonAn>listMonAn;
    private ArrayList<MonAn>listMonAn_old;
    private ArrayList<MonAn>listMonAn_short;
    public void setData(IRecycleViewClickListerner i,ArrayList<MonAn>list){
        this.i=i;
        this.listMonAn_old=list;
        if(list.size()<=15){
            this.listMonAn_short=list;
            Log.i("chkdata",listMonAn_short.size()+"");
        }else {
            this.listMonAn_short=new ArrayList<MonAn>();
            for(int j=0;j<15;j++){
                listMonAn_short.add(list.get(j));
            }
            Log.i("chkdata",listMonAn_short.size()+"helo");
        }
        this.listMonAn=listMonAn_short;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public Search_Monan_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.search_monan_item,parent,false);
        return new Search_Monan_ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Search_Monan_ViewHolder holder, int position) {
        MonAn monan=listMonAn.get(position);
        if(monan==null){
            return;
        }
        holder.search_tenmon_item.setText(monan.getTen());
        holder.search_img_monan_item.setImageResource(monan.getAnh());
        holder.search_ln_monan_item.setOnClickListener(new View.OnClickListener() {
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

    public class Search_Monan_ViewHolder  extends RecyclerView.ViewHolder{
        private TextView search_tenmon_item;
        private ImageView search_img_monan_item;
        private LinearLayout search_ln_monan_item;
        public Search_Monan_ViewHolder(@NonNull View itemView) {
            super(itemView);
            search_img_monan_item=itemView.findViewById(R.id.search_img_monan_item);
            search_tenmon_item=itemView.findViewById(R.id.search_tenmonan_item);
            search_ln_monan_item=itemView.findViewById(R.id.search_ln_monan_item);
        }
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String keySearch=constraint.toString(); //keyword nhập vào
                if(keySearch.isEmpty()){
                    listMonAn=listMonAn_short;
                }else{
                    ArrayList<MonAn>list=new ArrayList<MonAn>();
                    for(MonAn m:listMonAn_old){
                        if(m.getTen().toLowerCase().contains(keySearch.toLowerCase())){
                            list.add(m);
                        }
                    }
                    listMonAn=list;
                }
                FilterResults filterResults=new FilterResults();
                filterResults.values=listMonAn;
                return filterResults;
            }
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                listMonAn= (ArrayList<MonAn>) results.values;
                notifyDataSetChanged();
            }
        };
    }

}
