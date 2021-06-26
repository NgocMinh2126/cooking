package com.example.trangchu.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.trangchu.R;
import com.example.trangchu.service.HttpUtils;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import cz.msebera.android.httpclient.Header;

public class ThongTinUserActivity  extends AppCompatActivity {
    Button btn_monanyeuthich, btn_thucdon, btn_chinhsuathongtin, btn_doimk, btn_thongtinungdung, btn_donggopykien, btn_dangxuat;
    CollapsingToolbarLayout collapsingToolbarLayout;
    TextView tv_hoten,tv_ngaysinh,tv_mail;
    ImageView img_avatar, img_fullscreen;
    private Toolbar toolbar;
    String userid;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.thongtinuser);
        userid=this.getIntent().getStringExtra("UserID");

        toolbar=findViewById(R.id.toolbar);
        collapsingToolbarLayout=findViewById(R.id.userinfo_collapsingtoolbarlayout);

        btn_monanyeuthich=findViewById(R.id.userinfo_btn_monanyeuthich);
        btn_chinhsuathongtin=findViewById(R.id.userinfo_btn_chinhsuathongtin);
        btn_dangxuat=findViewById(R.id.userinfo_btn_dangxuat);
        btn_doimk=findViewById(R.id.userinfo_btn_doimk);
        btn_donggopykien=findViewById(R.id.userinfo_btn_donggopykien);
        btn_thongtinungdung=findViewById(R.id.userinfo_btn_thongtiungdung);
        btn_thucdon=findViewById(R.id.userinfo_btn_thucdon);

        img_avatar=findViewById(R.id.userinfo_img_avatar);
        img_fullscreen=findViewById(R.id.userinfo_img_fullscreen);
        tv_hoten=findViewById(R.id.userinfo_tv_hoten);
        tv_mail=findViewById(R.id.userinfo_tv_mail);
        tv_ngaysinh=findViewById(R.id.userinfo_tv_ngaysinh);

        img_avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                img_fullscreen.setVisibility(v.VISIBLE);
            }
        });
        img_fullscreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                img_fullscreen.setVisibility(v.GONE);
            }
        });

        setUserInfo_Toolbar();
        toolbar_animation();
        setDataForUserInfo();

        btn_dangxuat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(ThongTinUserActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        btn_doimk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ThongTinUserActivity.this,DoiMKActivity.class);
                intent.putExtra("UserID",userid);
                startActivity(intent);
            }
        });
        btn_donggopykien.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(ThongTinUserActivity.this,DongGopYKienActivity.class);
                startActivity(intent);
            }
        });
        btn_thongtinungdung.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(ThongTinUserActivity.this,GioiThieuActivity.class);
                startActivity(intent);
            }
        });
        btn_thucdon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent thucdon=new Intent(ThongTinUserActivity.this,ThucDonCuaBanActivity.class);
                thucdon.putExtra("UserID",userid);
                startActivity(thucdon);
            }
        });
        btn_monanyeuthich.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent monanyeuthich=new Intent(ThongTinUserActivity.this,MonAnYeuThichActivity.class);
                monanyeuthich.putExtra("UserID",""+userid);
                startActivity(monanyeuthich);
            }
        });
        btn_dangxuat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ThongTinUserActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        btn_chinhsuathongtin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ThongTinUserActivity.this,SuaUserActivity.class);
                intent.putExtra("UserID",userid);
                startActivity(intent);
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:break;
        }
        return super.onOptionsItemSelected(item);
    }
    public void setUserInfo_Toolbar(){
        setSupportActionBar(toolbar);
        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }
    public void toolbar_animation(){
        Bitmap bitmap= BitmapFactory.decodeResource(getResources(),R.drawable.tae_pic);
        collapsingToolbarLayout.setStatusBarScrimColor(getResources().getColor(R.color.color_basic));
        collapsingToolbarLayout.setContentScrimColor(getResources().getColor(R.color.color_basic));
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);
    }
    public void setDataForUserInfo(){
        RequestParams rp = new RequestParams();
        String duongdan="user/"+userid;
        HttpUtils.get(duongdan, rp, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    JSONArray listUser = response.getJSONArray("user");
                    if (listUser != null) {
                        for (int i=0;i<listUser.length();i++){
                            JSONObject obj = listUser.optJSONObject(i);
                            tv_hoten.setText(obj.getString("HoTen"));
                            tv_mail.setText(obj.getString("Mail"));
                            tv_ngaysinh.setText(obj.getString("NgaySinh"));
                            String title=obj.getString("Username");
                            collapsingToolbarLayout.setTitle(title);
                            try {
                                String anh=obj.getString("Anh");
                                URL url=new URL(anh);
                                Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                                img_avatar.setImageBitmap(bmp);
                                img_fullscreen.setImageBitmap(bmp);
                            } catch (MalformedURLException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }});
    }
}
