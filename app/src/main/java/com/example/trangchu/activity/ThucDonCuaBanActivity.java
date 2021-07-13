package com.example.trangchu.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.trangchu.R;
import com.example.trangchu.adapters.ThucDonCuaBanAdapter;
import com.example.trangchu.models.ThucDon;
import com.example.trangchu.service.HttpUtils;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class ThucDonCuaBanActivity extends AppCompatActivity {
    private ListView lv_thucdoncuaban;
    private ThucDonCuaBanAdapter thucDonCuaBanAdapter;
    String userid;
    private Toolbar thucdoncuaban_toolbar;
    private LinearLayout thucdoncuaban_loi;
    private LinearLayout thucdoncuaban_ani;
    private RelativeLayout thucdoncuaban_data;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.thucdon_cuaban);
        userid = this.getIntent().getStringExtra("UserID");
        Log.i("chkthucdon","userid: "+userid);
        lv_thucdoncuaban = findViewById(R.id.thucdoncuaban_lv);
        setDataForThucDonCuaBan();
        thucdoncuaban_loi = findViewById(R.id.thucdoncuaban_loi);
        thucdoncuaban_ani = findViewById(R.id.thucdoncuaban_ani);
        thucdoncuaban_data = findViewById(R.id.thucdoncuaban_data);

        thucdoncuaban_toolbar = findViewById(R.id.thucdoncuaban_toolbar);
        setThucdoncuaban_toolbar();
        thucdoncuaban_toolbar.setTitle("Thực đơn của bạn");

        lv_thucdoncuaban.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ThucDon t = (ThucDon) thucDonCuaBanAdapter.getItem(position);
                Intent intent=new Intent(ThucDonCuaBanActivity.this,ThucDonCuaBan_ChiTietActivity.class);
                intent.putExtra("IDThucDon",t.getId());
                intent.putExtra("UserID",userid);
                startActivity(intent);
                finish();
            }
        });
        lv_thucdoncuaban.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                ThucDon t = (ThucDon) thucDonCuaBanAdapter.getItem(position);
                OpenXoaMonAnDialog(Gravity.CENTER, t.getId());
                return true;
            }
        });

    }

    public void setDataForThucDonCuaBan() {
        ArrayList<ThucDon> listThucDon = new ArrayList<ThucDon>();
        RequestParams rp = new RequestParams();
        String duongdan = "thucdon/" + userid;
        HttpUtils.get(duongdan, rp, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    JSONArray listThucDonServer = response.getJSONArray("thucdon");
                    if (listThucDonServer != null) {
                        for (int i = 0; i < listThucDonServer.length(); i++) {
                            JSONObject obj = listThucDonServer.getJSONObject(i);
                            listThucDon.add(new ThucDon(obj.getString("_id"),
                                    obj.getString("Ngay"), obj.getString("Buoi")));
                        }
                        Log.i("chkthucdon", "so luong thuc don: " + listThucDon.size());
                        thucDonCuaBanAdapter = new ThucDonCuaBanAdapter(listThucDon);
                        lv_thucdoncuaban.setAdapter(thucDonCuaBanAdapter);
                        thucdoncuaban_ani.setVisibility(View.GONE);
                        thucdoncuaban_data.setVisibility(View.VISIBLE);

                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                thucdoncuaban_ani.setVisibility(View.GONE);
                thucdoncuaban_loi.setVisibility(View.VISIBLE);
                lv_thucdoncuaban.setVisibility(View.GONE);
            }
        });

    }

    public void setThucdoncuaban_toolbar() {
        setSupportActionBar(thucdoncuaban_toolbar);
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

    public void OpenXoaMonAnDialog(int gravity, String idthucdon) {
        final Dialog xoadialog = new Dialog(this);
        xoadialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        xoadialog.setContentView(R.layout.thucdon_cuaban_xoathucdon);

        Window window = xoadialog.getWindow();
        if (window == null) {
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        //xac dinh vi tri
        WindowManager.LayoutParams windowattribute = window.getAttributes();
        windowattribute.gravity = gravity;
        window.setAttributes(windowattribute);

        if (Gravity.CENTER == gravity) {
            xoadialog.setCancelable(true);
        } else {
            xoadialog.setCancelable(false);
        }
        Button btn_huy = xoadialog.findViewById(R.id.xoathucdon_btn_huy);
        Button btn_ok = xoadialog.findViewById(R.id.xoathucdon_btn_ok);

        btn_huy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                xoadialog.dismiss();
            }
        });
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                XoaThucDon(idthucdon,xoadialog);
            }
        });
        xoadialog.show();
    }

    public void XoaThucDon(String idthucdon,Dialog xoa) {
        RequestParams rp = new RequestParams();
        String duongdan = "thucdon/" + idthucdon;
        HttpUtils.delete(duongdan, rp, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.i("chkthucdon", "xoa thanh cong");
                Toast.makeText(ThucDonCuaBanActivity.this, "Đã xóa thực đơn", Toast.LENGTH_SHORT).show();
                xoa.dismiss();
                finish();
                startActivity(getIntent());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.i("chkthucdon", "xoa that bai");
            }
        });


    }
}
