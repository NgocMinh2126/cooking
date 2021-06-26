package com.example.trangchu.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.trangchu.IRecycleViewClickListerner;
import com.example.trangchu.R;
import com.example.trangchu.adapters.DSMonAnAdapter;
import com.example.trangchu.adapters.MonAnYeuThichAdapter;
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


public class MonAnYeuThichActivity extends AppCompatActivity {
    private RecyclerView dsmonanyeuthich_rv;
    private String userid;
    private MonAnYeuThichAdapter dsmonanyeuthich_adapter;
    private LinearLayout dsmonanyeuthich_ani;
    private LinearLayout dsmonanyeuthich_loi;
    private ImageButton dsmonanyeuthich_back_btn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.monanyeuthich);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        userid=this.getIntent().getStringExtra("UserID");
        dsmonanyeuthich_ani=findViewById(R.id.monanyeuthich_ani);
        dsmonanyeuthich_rv=findViewById(R.id.monanyeuthich_rv);
        dsmonanyeuthich_loi=findViewById(R.id.monanyeuthich_loi);
        dsmonanyeuthich_back_btn=findViewById(R.id.monanyeuthich_back_btn);

        LinearLayoutManager yeuthich_layoutmanager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        dsmonanyeuthich_rv.setLayoutManager(yeuthich_layoutmanager);
        setDataForMonAnYeuThich(this);

        dsmonanyeuthich_back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });
    }
    public void setDataForMonAnYeuThich(Context context){
        ArrayList<MonAn>listMonAn=new ArrayList<MonAn>();
        Log.i("chk",""+userid);
        String duongdan="monanyeuthich/"+userid;
        RequestParams rp = new RequestParams();
        HttpUtils.get(duongdan, rp, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    JSONObject dsyeuthich = response.getJSONObject("dsyeuthich");
                    if(dsyeuthich!=null){
                        JSONArray dsmonan=dsyeuthich.getJSONArray("dsmonan");
                        if(dsmonan!=null){
                            for(int i=0;i<dsmonan.length();i++){
                                JSONObject obj=dsmonan.getJSONObject(i);
                                listMonAn.add(new MonAn(obj.getString("_id"),
                                        obj.getString("Ten"),obj.getString("Anh")));
                            }
                        }
                    }
                    Log.i("chk","mon an yeu thich: "+listMonAn.size());
                    dsmonanyeuthich_adapter=new MonAnYeuThichAdapter();
                    dsmonanyeuthich_adapter.setData(listMonAn,userid, new IRecycleViewClickListerner() {
                        @Override
                        public void onItemClick(MonAn monan) {

                        }
                    });
                    dsmonanyeuthich_rv.setAdapter(dsmonanyeuthich_adapter);
                    dsmonanyeuthich_ani.setVisibility(View.GONE);

                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                dsmonanyeuthich_rv.setVisibility(View.GONE);
                dsmonanyeuthich_ani.setVisibility(View.GONE);
                dsmonanyeuthich_loi.setVisibility(View.VISIBLE);
            }
        });



    }



}
