package com.example.trangchu.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
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
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Hashtable;

import cz.msebera.android.httpclient.Header;

public class DSMonAnActivity extends AppCompatActivity {
    private RecyclerView dsmonan_rv;
    private String str;
    private TextView dsmonan_tv_tenloaimon;
    private DSMonAnAdapter dsmonan_adapter;
    private LinearLayout dsmonan_ani, nointernet;
    private ImageButton dsmonan_back_btn;
    String userid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dsmonan);
        dsmonan_tv_tenloaimon = findViewById(R.id.dsmonan_ten_loaimon);
        dsmonan_rv = findViewById(R.id.dsmonan_rv);

        dsmonan_ani = findViewById(R.id.dsmonan_ani);

        dsmonan_back_btn = findViewById(R.id.dsmonan_back_btn);

        nointernet = findViewById(R.id.dsmonan_nointernet);
        checkInternetConnection();
        str = this.getIntent().getStringExtra("TenLoaiMon");
        if (str.equals("null") == false) {
            Log.i("chk", str + " loaimon");
        } else {
            Log.i("chk", " loai mon");
        }
        userid = this.getIntent().getStringExtra("UserID");
        if (userid.equals("null") == false) {
            Log.i("chk", userid + " id user");
        } else {
            Log.i("chk", " id user");
        }
        dsmonan_tv_tenloaimon.setText(str);
        setDataForDsMonAn(this);
        LinearLayoutManager dsmonan_layout = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        dsmonan_rv.setLayoutManager(dsmonan_layout);
        dsmonan_back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    public void setDataForDsMonAn(Context context) {
        ArrayList<MonAn> list = new ArrayList<MonAn>();
        String duongdan = "";
        if (str.equals("Bữa ăn thường ngày")) {
            duongdan = "monan/thuongngay";
        } else if (str.equals("Món cháo, súp")) {
            duongdan = "monan/loaimon/60935f3e3212664344ac98cb";
        } else if (str.equals("Các món nướng")) {
            duongdan = "monan/loaimon/60935f3e3212664344ac98c4";
        } else if (str.equals("Món tráng miệng")) {
            duongdan = "monan/trangmieng";
        } else if (str.equals("Món ăn chay")) {
            duongdan = "monan/loaimon/60935f3e3212664344ac98c6";
        } else if (str.equals("Món xào")) {
            duongdan = "monan/loaimon/60935f3e3212664344ac98c1";
        } else {
            duongdan = "monan/loaimon/60935f3e3212664344ac98c7";
        }
        Log.i("chkDSMonAn", "" + duongdan);
        RequestParams rp = new RequestParams();
        Hashtable<String, String> headers = new Hashtable<>();
        if (userid.equals("null") == false) {
            Log.i("chk", userid + " id user");
            Log.i("chk", "co id user");
            headers.put("iduser", userid);
            HttpUtils.get(duongdan, rp, headers, new JsonHttpResponseHandler() {
                @Override
                //statusCode: ma loi tra tu service ve
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    try {
                        JSONArray lstMonAn = response.getJSONArray("listMonAn");
                        if (lstMonAn != null) {
                            for (int i = 0; i < lstMonAn.length(); i++) {
                                JSONObject obj = lstMonAn.optJSONObject(i);
                                if (obj.has("Thich")) {
                                    list.add(new MonAn(obj.getString("_id"), obj.getString("Ten"), obj.getString("Anh"), obj.getInt("Thich")));
                                } else {
                                    list.add(new MonAn(obj.getString("_id"), obj.getString("Ten"), obj.getString("Anh")));
                                }
                            }
                            Log.i("chk", list.size() + " monan");
                        }
                        dsmonan_adapter = new DSMonAnAdapter();
                        Activity activity = (Activity) context;
                        dsmonan_adapter.setData2(list, activity, userid, new IRecycleViewClickListerner() {
                            @Override
                            public void onItemClick(MonAn monan) {
                                Intent intent=new Intent(DSMonAnActivity.this, MonAnChiTietActivity.class);
                                intent.putExtra("UserID",userid+"");
                                intent.putExtra("IDMonAn",monan.getId());
                                startActivity(intent);
                            }
                        });
                        dsmonan_rv.setAdapter(dsmonan_adapter);
                        dsmonan_ani.setVisibility(View.GONE);
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            });
        } else {
            Log.i("chk", "ko co id user");
            RequestParams rp2 = new RequestParams();
            HttpUtils.get(duongdan, rp2, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    try {
                        JSONArray lstMonAn = response.getJSONArray("listMonAn");
                        if (lstMonAn != null) {
                            for (int i = 0; i < lstMonAn.length(); i++) {
                                JSONObject obj = lstMonAn.optJSONObject(i);
                                list.add(new MonAn(obj.getString("_id"),
                                        obj.getString("Ten"), obj.getString("Anh")));
                            }
                        }
                        dsmonan_adapter = new DSMonAnAdapter();
                        dsmonan_adapter.setData(list, new IRecycleViewClickListerner() {
                            @Override
                            public void onItemClick(MonAn monan) {
                                Intent intent=new Intent(DSMonAnActivity.this, MonAnChiTietActivity.class);
                                intent.putExtra("UserID",userid+"");
                                intent.putExtra("IDMonAn",monan.getId());
                                startActivity(intent);
                            }
                        });
                        dsmonan_rv.setAdapter(dsmonan_adapter);
                        dsmonan_ani.setVisibility(View.GONE);
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    private boolean checkInternetConnection() {
        ConnectivityManager connManager =
                (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connManager.getActiveNetworkInfo();

        if (networkInfo == null) {
            dsmonan_ani.setVisibility(View.GONE);
            dsmonan_rv.setVisibility(View.GONE);
            nointernet.setVisibility(View.VISIBLE);
            return false;
        }
        return true;
    }
}
