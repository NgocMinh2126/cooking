package com.example.trangchu.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trangchu.ITipsOnItemClick;
import com.example.trangchu.R;
import com.example.trangchu.activity.MainActivity;
import com.example.trangchu.activity.SearchActivity;
import com.example.trangchu.activity.Tip_ChiTietActivity;
import com.example.trangchu.adapters.TipsAdapter;
import com.example.trangchu.models.Tips;

import java.util.ArrayList;
import java.util.List;

public class TipsFragment extends Fragment {
    RecyclerView rv_tip;
    TipsAdapter tipsAdapter;
    List<Tips>listTip;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_tips,container,false);
        rv_tip=view.findViewById(R.id.tips_rv);
        LinearLayoutManager layoutManager=new LinearLayoutManager(view.getContext(),LinearLayoutManager.VERTICAL,false);
        rv_tip.setLayoutManager(layoutManager);
        listTip=new ArrayList<Tips>();
        listTip=setTipData();
        tipsAdapter=new TipsAdapter();
        tipsAdapter.setData(listTip, new ITipsOnItemClick() {
            @Override
            public void onItemClick(Tips tip) {
                Intent intent=new Intent(view.getContext(), Tip_ChiTietActivity.class);
                intent.putExtra("id",tip.getId()+"");
                startActivity(intent);
            }
        });
        rv_tip.setAdapter(tipsAdapter);
        return view;
    }
    public List<Tips>setTipData(){
        List<Tips>list=new ArrayList<Tips>();
        list.add(new Tips(1,"B?? quy???t n??m n???m",R.drawable.tip_img_01));
        list.add(new Tips(2,"B?? quy???t n???u canh",R.drawable.tip_img_02));
        list.add(new Tips(3,"M???o n???u ch??o",R.drawable.tip03));
        list.add(new Tips(4,"B?? quy???t ch???n th???c ph???m",R.drawable.tip04));
        list.add(new Tips(5,"Tr??? b???ng r??t khi c???t ???t",R.drawable.tip05));
        list.add(new Tips(6,"C??ch b???o qu???n hoa qu???",R.drawable.tip06));
        return list;
    }

}
