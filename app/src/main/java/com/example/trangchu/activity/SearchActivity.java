package com.example.trangchu.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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
import com.example.trangchu.service.HttpUtils;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class SearchActivity extends AppCompatActivity {
    RecyclerView search_monan_rv;
    ShimmerFrameLayout search_monan_shimmer;
    ArrayList<MonAn> listMonAn;  //luu tat ca cac mon an
    Search_Monan_Adapter search_monan_adapter;
    ImageButton btn_search;
    ImageButton btn_back;
    EditText tv_searchkey;
    String userid;
    RecyclerView search_loaimon_rv;
    ArrayList<LoaiMon> listLoaiMon;
    Search_LoaiMon_Adapter search_loaiMon_adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_layout);
        userid = this.getIntent().getStringExtra("UserID");
        if (userid != null) {
            Log.i("chksearch", userid + " id user");
        } else {
            Log.i("chksearch", " id user");
        }
        btn_back = findViewById(R.id.search_back_btn);
        search_monan_shimmer = findViewById(R.id.search_monan_shimmer);
        btn_search = findViewById(R.id.search_btn);
        tv_searchkey = findViewById(R.id.search_tv_keyword);
        search_monan_rv = findViewById(R.id.search_rv_monan);
        setDataMonAn(SearchActivity.this);
        GridLayoutManager search_monan_layout = new GridLayoutManager(this, 3);
        search_monan_rv.setLayoutManager(search_monan_layout);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        //xu ly loai mon
        search_loaimon_rv = findViewById(R.id.search_rv_loaimon);
        LinearLayoutManager search_loaimon_layout = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        search_loaimon_rv.setLayoutManager(search_loaimon_layout);
        listLoaiMon = setDataLoaiMon();
        search_loaiMon_adapter = new Search_LoaiMon_Adapter();
        search_loaiMon_adapter.setData(listLoaiMon, new ILoaiMonClickListener() {
            @Override
            public void onItemClick(LoaiMon loaimon) {
                Intent intent = new Intent(SearchActivity.this, DSMonAnActivity.class);
                intent.putExtra("TenLoaiMon", loaimon.getTenLoaiMon());
                Log.i("chk", loaimon.getTenLoaiMon() + "");
                intent.putExtra("UserID", userid+"");
                startActivity(intent);
            }
        });
        search_loaimon_rv.setAdapter(search_loaiMon_adapter);
    }

    public ArrayList<MonAn> setDataMonAn(Context context) {
        ArrayList<MonAn> list = new ArrayList<MonAn>();
        RequestParams rp = new RequestParams();
        HttpUtils.get("monan/", rp, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    JSONArray listMonAn = response.getJSONArray("dsmonan");
                    if (listMonAn != null) {
                        for (int i = 0; i < listMonAn.length(); i++) {
                            JSONObject obj = listMonAn.optJSONObject(i);
                            MonAn m = new MonAn(obj.getString("_id"), obj.getString("Ten"), obj.getString("Anh"));
                            list.add(m);
                        }
                        search_monan_adapter = new Search_Monan_Adapter();
                        search_monan_adapter.setData(list, new IRecycleViewClickListerner() {
                            @Override
                            public void onItemClick(MonAn monan) {
                                Toast.makeText(context, monan.getTenMonAn(), Toast.LENGTH_SHORT).show();
                            }
                        });
                        search_monan_rv.setAdapter(search_monan_adapter);
                        search_monan_shimmer.startShimmer();
                        search_monan_shimmer.setVisibility(View.GONE);
                        btn_search.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String keyword = tv_searchkey.getText().toString();
                                search_monan_adapter.getFilter().filter(keyword);
                                search_monan_rv.setAdapter(search_monan_adapter);
                            }
                        });
                        tv_searchkey.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                            @Override
                            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                                    String keyword = tv_searchkey.getText().toString();
                                    search_monan_adapter.getFilter().filter(keyword);
                                    search_monan_rv.setAdapter(search_monan_adapter);
                                }
                                return false;
                            }
                        });
                        Log.i("chksearch", "Số lượng" + list.size());
//                        tv_searchkey.addTextChangedListener(new TextWatcher() {
//                            @Override
//                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//                            }
//
//                            @Override
//                            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                                Log.i("chkSearch","huhu");
//                                String keyword=tv_searchkey.getText().toString();
//                                search_monan_adapter.getFilter().filter(keyword);
//                                search_monan_rv.setAdapter(search_monan_adapter);
//                            }
//
//                            @Override
//                            public void afterTextChanged(Editable s) {
//
//                            }
//                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        return list;
    }

    public ArrayList<LoaiMon> setDataLoaiMon() {
        ArrayList<LoaiMon> list = new ArrayList<LoaiMon>();
        list.add(new LoaiMon("Bữa ăn thường ngày", "60935f3e3212664344ac98bf"));
        list.add(new LoaiMon("Cháo súp", "60935f3e3212664344ac98c1"));
        list.add(new LoaiMon("Món nướng", "60935f3e3212664344ac98c0"));
        list.add(new LoaiMon("Món tráng miệng", "60935f3e3212664344ac98c2"));
        list.add(new LoaiMon("Các món ăn chay", "60935f3e3212664344ac98c3"));
        return list;
    }
}
