package com.example.trangchu.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.trangchu.R;
import com.example.trangchu.service.HttpUtils;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;

public class DoiMKActivity extends AppCompatActivity {
    TextInputLayout layout_oldpassword, layout_newpassword, layout_confirmpassword;
    TextInputEditText doimk_oldpass,doimk_newpass,doimk_repeat;
    Button doimk_back_btn,doimk_luu_btn;
    String userid;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doi_matkhau);
        userid=this.getIntent().getStringExtra("UserID");

        layout_oldpassword = findViewById(R.id.layout_oldpassword);
        layout_newpassword = findViewById(R.id.layout_newpassword);
        layout_confirmpassword = findViewById(R.id.layout_confirmpassword);
        doimk_newpass=findViewById(R.id.doimk_newpass);
        doimk_oldpass=findViewById(R.id.doimk_oldpass);
        doimk_repeat=findViewById(R.id.doimk_nhaplai);
        doimk_back_btn=findViewById(R.id.doimk_btn_huy);
        doimk_luu_btn=findViewById(R.id.doimk_btnluu);

        doimk_back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });

        doimk_luu_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mk_cu=doimk_oldpass.getText().toString();
                String mk_moi=doimk_newpass.getText().toString();
                String mk_nhaplai=doimk_repeat.getText().toString();

                if(mk_cu.length()==0||mk_moi.length()==0||mk_nhaplai.length()==0){
                    if(mk_cu.length()==0){
                        layout_oldpassword.setError("Nhập mật khẩu hiện tại");
                    }
                    if(mk_moi.length()==0){
                        layout_newpassword.setError("Nhập mật khẩu mới");
                    }
                    if(mk_nhaplai.length()==0){
                        layout_confirmpassword.setError("Nhập lại mật khẩu mới");
                    }
                }else if(mk_moi.equals(mk_nhaplai)==false){
                    layout_confirmpassword.setError("Mật khẩu không khớp");
                }else{
                    JSONObject jsonParams = new JSONObject();
                    try {
                        jsonParams.put("IDUser", userid);
                        jsonParams.put("OldPass",mk_cu);
                        jsonParams.put("Password",mk_moi);
                        StringEntity entity = new StringEntity(jsonParams.toString());
                        String duongdan="user/doipass/"+"6091fb1e9fee1f0d0459c67d";
                        HttpUtils.post(v.getContext(),duongdan, entity, new JsonHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                Toast.makeText(DoiMKActivity.this,"Đổi mật khẩu thành công",Toast.LENGTH_SHORT).show();
                                onBackPressed();
                                finish();
                            }
                            @Override
                            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                                Log.i("chkdoimk","sai pass word cũ");
                                layout_oldpassword.setError("Mật khẩu hiện tại sai");
                            }
                        });
                    } catch (JSONException | UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}
