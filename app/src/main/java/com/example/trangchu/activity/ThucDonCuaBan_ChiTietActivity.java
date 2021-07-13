package com.example.trangchu.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

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

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.protocol.HTTP;

public class ThucDonCuaBan_ChiTietActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private RecyclerView rv_data;
    String idthucdon;
    ThucDonCuaBan_ChiTiet_Adapter adapter;
    private LinearLayout ani;
    String userid;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.thucdon_cuaban_chitiet);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        idthucdon=this.getIntent().getStringExtra("IDThucDon");
        userid=this.getIntent().getStringExtra("UserID");
        Log.i("chkthucdon","iduser: "+userid);
        Log.i("chkthucdon","id"+idthucdon);
        ani=findViewById(R.id.thucdoncuaban_chitiet_ani);

        toolbar=findViewById(R.id.thucdoncuaban_chitiet_toolbar);
        toolbar.setTitle("Chi tiết thực đơn");
        setToolbar();

        rv_data=findViewById(R.id.thucdoncuaban_chitiet_rv);
        GridLayoutManager gridLayoutManager=new GridLayoutManager(this,2);
        rv_data.setLayoutManager(gridLayoutManager);
        setDataForChiTietThucDon(this);
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
    public void setDataForChiTietThucDon(Context context){
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
                                    OpenXoaMonAnDialog(Gravity.CENTER,monan.getId(),context);
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
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Toast.makeText(ThucDonCuaBan_ChiTietActivity.this,"Thực đơn đã bị xóa do không còn món ăn nào",Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(ThucDonCuaBan_ChiTietActivity.this,ThucDonCuaBanActivity.class);
                intent.putExtra("UserID",userid);
                startActivity(intent);
                finish();
            }
        });
    }
    public void OpenXoaMonAnDialog(int gravity, String idmonan,Context context) {
        final Dialog xoadialog = new Dialog(this);
        xoadialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        xoadialog.setContentView(R.layout.thucdon_update);

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
        Button btn_huy = xoadialog.findViewById(R.id.updatethucdon_btn_huy);
        Button btn_ok = xoadialog.findViewById(R.id.updatethucdon_btn_ok);

        btn_huy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                xoadialog.dismiss();
            }
        });
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Update(idmonan,xoadialog,context);
            }
        });
        xoadialog.show();
    }
    public void Update(String idmonan, Dialog dialog, Context context){
        JSONObject jsonParams=new JSONObject();
        try {
            jsonParams.put("IDThucDon", idthucdon);
            jsonParams.put("IDMonAn",idmonan);
            StringEntity entity = new StringEntity(jsonParams.toString());
            HttpUtils.put(context,"thucdon", entity, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    Log.i("chk","da xoa");
                    Intent intent=new Intent(ThucDonCuaBan_ChiTietActivity.this,ThucDonCuaBan_ChiTietActivity.class);
                    dialog.dismiss();
                    intent.putExtra("IDThucDon",idthucdon);
                    intent.putExtra("UserID",userid);
                    startActivity(intent);
                    finish();
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    Log.i("chk","xoa that bai");
                }
            });
        } catch (JSONException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

}
