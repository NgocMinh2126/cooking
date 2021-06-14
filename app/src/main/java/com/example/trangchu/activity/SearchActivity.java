package com.example.trangchu.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trangchu.ILoaiMonClickListener;
import com.example.trangchu.IRecycleViewClickListerner;
import com.example.trangchu.R;
import com.example.trangchu.adapters.Search_LoaiMon_Adapter;
import com.example.trangchu.adapters.Search_Monan_Adapter;
import com.example.trangchu.models.LoaiMon;
import com.example.trangchu.models.MonAn;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {
    RecyclerView search_monan_rv;
    ArrayList<MonAn> listMonAn;  //luu tat ca cac mon an
    Search_Monan_Adapter search_monan_adapter;
    ImageButton btn_search;
    ImageButton btn_back;
    EditText tv_searchkey;

    RecyclerView search_loaimon_rv;
    ArrayList<LoaiMon>listLoaiMon;
    Search_LoaiMon_Adapter search_loaiMon_adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_layout);

        btn_back=findViewById(R.id.search_back_btn);
        btn_search=findViewById(R.id.search_btn);
        tv_searchkey=findViewById(R.id.search_tv_keyword);
        search_monan_rv=findViewById(R.id.search_rv_monan);
        GridLayoutManager search_monan_layout=new GridLayoutManager(this,3);
        search_monan_rv.setLayoutManager(search_monan_layout);
        listMonAn=new ArrayList<MonAn>();
        listMonAn=setDataMonAn();
        search_monan_adapter=new Search_Monan_Adapter();
        //set data
        search_monan_adapter.setData(new IRecycleViewClickListerner() {
            @Override
            public void onItemClick(MonAn monan) {
                Toast.makeText(SearchActivity.this,monan.getTen(),Toast.LENGTH_SHORT).show();
            }
        },listMonAn);
        search_monan_rv.setAdapter(search_monan_adapter);
        //search khi nhan nut search
        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String keyword=tv_searchkey.getText().toString();
                search_monan_adapter.getFilter().filter(keyword);
                search_monan_rv.setAdapter(search_monan_adapter);
            }
        });
        //search khi thay doi text nhap vao
        tv_searchkey.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String keyword=tv_searchkey.getText().toString();
                search_monan_adapter.getFilter().filter(keyword);
                search_monan_rv.setAdapter(search_monan_adapter);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        //search khi nhan nut tren ban phim
        tv_searchkey.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId==EditorInfo.IME_ACTION_SEARCH){
                    String keyword=tv_searchkey.getText().toString();
                    search_monan_adapter.getFilter().filter(keyword);
                    search_monan_rv.setAdapter(search_monan_adapter);
                }
                return false;
            }
        });
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SearchActivity.this, MainActivity.class);
                //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });

        //xu ly loai mon
        search_loaimon_rv=findViewById(R.id.search_rv_loaimon);
        LinearLayoutManager search_loaimon_layout=new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        search_loaimon_rv.setLayoutManager(search_loaimon_layout);
        listLoaiMon=new ArrayList<LoaiMon>();
        listLoaiMon=setDataLoaiMon();
        search_loaiMon_adapter =new Search_LoaiMon_Adapter();
        search_loaiMon_adapter.setData(listLoaiMon, new ILoaiMonClickListener() {
            @Override
            public void onItemClick(LoaiMon loaimon) {
                Toast.makeText(SearchActivity.this,loaimon.getTenLoaiMon(),Toast.LENGTH_SHORT).show();
            }
        });
        search_loaimon_rv.setAdapter(search_loaiMon_adapter);
    }
    public ArrayList<MonAn>setDataMonAn(){
        ArrayList<MonAn>list=new ArrayList<MonAn>();
        list.add(new MonAn("Chân giò kho tiêu",R.drawable.chan_gio_kho_tieu));
        list.add(new MonAn("Gà hấp hành",R.drawable.tom_rang_muoi));
        list.add(new MonAn("Canh khoai từ",R.drawable.chan_gio_kho_tieu));
        list.add(new MonAn("Khổ qua nhồi thịt",R.drawable.chan_gio_kho_tieu));
        list.add(new MonAn("Kem chuối",R.drawable.chan_gio_kho_tieu));
        list.add(new MonAn("Chân giò kho tiêu",R.drawable.chan_gio_kho_tieu));
        list.add(new MonAn("Gà hấp hành",R.drawable.tom_rang_muoi));
        list.add(new MonAn("Canh khoai từ",R.drawable.chan_gio_kho_tieu));
        list.add(new MonAn("Khổ qua nhồi thịt",R.drawable.chan_gio_kho_tieu));
        list.add(new MonAn("Kem chuối",R.drawable.chan_gio_kho_tieu));
        list.add(new MonAn("Chân giò kho tiêu",R.drawable.chan_gio_kho_tieu));
        list.add(new MonAn("Gà hấp hành",R.drawable.tom_rang_muoi));
        list.add(new MonAn("Canh khoai từ",R.drawable.chan_gio_kho_tieu));
        list.add(new MonAn("Khổ qua nhồi thịt",R.drawable.chan_gio_kho_tieu));
        list.add(new MonAn("Kem chuối",R.drawable.chan_gio_kho_tieu));
        list.add(new MonAn("Chân giò kho tiêu",R.drawable.chan_gio_kho_tieu));
        list.add(new MonAn("Gà hấp hành",R.drawable.tom_rang_muoi));
        list.add(new MonAn("Canh khoai từ",R.drawable.chan_gio_kho_tieu));
        list.add(new MonAn("Khổ qua nhồi thịt",R.drawable.chan_gio_kho_tieu));
        list.add(new MonAn("Kem chuối",R.drawable.chan_gio_kho_tieu));
        return list;
    }
    public ArrayList<LoaiMon>setDataLoaiMon(){
        ArrayList<LoaiMon> list=new ArrayList<LoaiMon>();
        LoaiMon loaimon1=new LoaiMon("Loại 1");
        LoaiMon loaimon2=new LoaiMon("Món ăn thường ngày");
        LoaiMon loaimon3=new LoaiMon("Chè các loai");
        LoaiMon loaimon4=new LoaiMon("Món nướng");
        LoaiMon loaimon5=new LoaiMon("Món ăn chay");
        list.add(loaimon1);
        list.add(loaimon2);
        list.add(loaimon3);
        list.add(loaimon4);
        list.add(loaimon5);
        return list;
    }
}
