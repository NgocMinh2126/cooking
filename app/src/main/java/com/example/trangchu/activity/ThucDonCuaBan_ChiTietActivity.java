package com.example.trangchu.activity;

import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trangchu.IRecycleViewClickListerner;
import com.example.trangchu.R;
import com.example.trangchu.adapters.ThucDonCuaBanAdapter;
import com.example.trangchu.adapters.ThucDonCuaBan_ChiTiet_Adapter;
import com.example.trangchu.models.MonAn;
import com.example.trangchu.models.ThucDon;
import com.example.trangchu.service.HttpUtils;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class ThucDonCuaBan_ChiTietActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private RecyclerView rv_data;
    String idthucdon;
    ThucDonCuaBan_ChiTiet_Adapter adapter;
    private LinearLayout ani;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.thucdon_cuaban_chitiet);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        idthucdon=this.getIntent().getStringExtra("IDThucDon");
        Log.i("chkthucdon","id"+idthucdon);
        ani=findViewById(R.id.thucdoncuaban_chitiet_ani);

        toolbar=findViewById(R.id.thucdoncuaban_chitiet_toolbar);
        toolbar.setTitle("Chi tiết thực đơn");
        setToolbar();

        rv_data=findViewById(R.id.thucdoncuaban_chitiet_rv);
        GridLayoutManager gridLayoutManager=new GridLayoutManager(this,2);
        rv_data.setLayoutManager(gridLayoutManager);
        setDataForChiTietThucDon();
    }
    public void setToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    public void setDataForChiTietThucDon(){
        ArrayList<MonAn> listMonAn = new ArrayList<MonAn>();
        RequestParams rp = new RequestParams();
        String duongdan = "thucdon/id/"+idthucdon ;
        HttpUtils.get(duongdan, rp, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    JSONObject thucdon = response.getJSONObject("t");
                    if (thucdon != null) {
                        JSONArray listMonAnServer=thucdon.getJSONArray("DSMonAn");
                        if(listMonAnServer!=null){
                            for(int i=0;i<listMonAnServer.length();i++){
                                JSONObject obj=listMonAnServer.getJSONObject(i);
                                listMonAn.add(new MonAn(obj.getString("_id"),
                                        obj.getString("Ten"),obj.getString("Anh")));
                            }
                            adapter=new ThucDonCuaBan_ChiTiet_Adapter();
                            adapter.setData(listMonAn, new IRecycleViewClickListerner() {
                                @Override
                                public void onItemClick(MonAn monan) {

                                }
                            });
                            rv_data.setAdapter(adapter);
                            rv_data.setVisibility(View.VISIBLE);
                            ani.setVisibility(View.GONE);
                        }
                        Log.i("chkthucdon", "so luong thuc don: " + listMonAn.size());
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

        });
    }
}
