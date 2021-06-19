package com.example.trangchu.activity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trangchu.IRecycleViewClickListerner;
import com.example.trangchu.R;
import com.example.trangchu.adapters.DSMonAnAdapter;
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

public class DSMonAnActivity extends AppCompatActivity {
    private RecyclerView dsmonan_rv;
    private String str;
    private TextView dsmonan_tv_tenloaimon;
    private DSMonAnAdapter dsmonan_adapter;
    private ShimmerFrameLayout dsmonan_ani;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dsmonan);
        dsmonan_tv_tenloaimon=findViewById(R.id.dsmonan_ten_loaimon);
        dsmonan_rv=findViewById(R.id.dsmonan_rv);
        dsmonan_ani=findViewById(R.id.dsmonan_ani);

        str=this.getIntent().getStringExtra("TenLoaiMon");
        dsmonan_tv_tenloaimon.setText(str);
        setDataForDsMonAn(this);
        LinearLayoutManager dsmonan_layout=new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);
        dsmonan_rv.setLayoutManager(dsmonan_layout);
    }
    public void setDataForDsMonAn(Context context){
        ArrayList<MonAn>list=new ArrayList<MonAn>();
        String duongdan="";
        if(str.equals("Bữa ăn thường ngày")){
            duongdan="monan/thuongngay";
        }else if(str.equals("Cháo súp")){
            duongdan="monan/thuongngay";
        }else if(str.equals("Món nướng")){
            duongdan="monan/thuongngay";
        }else if(str.equals("Món tráng miệng")){
            duongdan="monan/trangmieng";
        }else if(str.equals("Các món ăn chay")){
            duongdan="monan/thuongngay";
        }else{
            duongdan="monan/thuongngay";
        }
        RequestParams rp = new RequestParams();
//        rp.add("id", "123");
        HttpUtils.get(duongdan+"", rp, new JsonHttpResponseHandler() {
            @Override
            //statusCode: ma loi tra tu service ve
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    JSONArray lstMonAn = response.getJSONArray("listMonAn");
                    if (lstMonAn != null) {
                        for (int i=0;i<lstMonAn.length();i++){
                            JSONObject obj = lstMonAn.optJSONObject(i);
                            list.add(new MonAn(obj.getString("_id"),obj.getString("Ten"),obj.getString("Anh")));
                        }
                    }
                    Log.i("chkDSMonAn","sl mon an lay duoc"+list.size());
                    dsmonan_adapter=new DSMonAnAdapter();
                    dsmonan_adapter.setData(list, new IRecycleViewClickListerner() {
                        @Override
                        public void onItemClick(MonAn monan) {
                            Toast.makeText(context,"Bạn đã thích món " + monan.getTenMonAn(), Toast.LENGTH_SHORT).show();
                        }
                    });
                    dsmonan_rv.setAdapter(dsmonan_adapter);
                    dsmonan_ani.startShimmer();
                    dsmonan_ani.setVisibility(View.GONE);
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }});
    }
}
