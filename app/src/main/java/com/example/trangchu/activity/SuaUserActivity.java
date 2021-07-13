package com.example.trangchu.activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.trangchu.R;
import com.example.trangchu.service.HttpUtils;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.protocol.HTTP;
import de.hdodenhof.circleimageview.CircleImageView;

public class SuaUserActivity extends AppCompatActivity {
    EditText useredit_hoten, useredit_mail;
    Button btnDate;
    Calendar calendar;
    ImageView hienanh_fullscreen;
    ImageButton imgbtn_themanh, imgbtn_huy, imgbtn_luu;
    int date, month, year;
    CircleImageView avatar;
    public static int REQUEST_IMAGE_CAPTURE = 1;
    public static int REQUEST_GALLERY_IMAGE = 2;

    String userid;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.suathongtin_user);
        userid=this.getIntent().getStringExtra("UserID");

        avatar = findViewById(R.id.avatar);
        imgbtn_themanh = findViewById(R.id.imgbtn_themanh);
        imgbtn_themanh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayAlertDialog();
            }
        });

        useredit_hoten = findViewById(R.id.edituser_hoten);
        useredit_mail = findViewById(R.id.edituser_mail);

        hienanh_fullscreen = findViewById(R.id.hienanh_fullscreen);
        hienanh_fullscreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hienanh_fullscreen.setVisibility(v.GONE);
            }
        });

        btnDate = findViewById(R.id.edituser_ngaysinh);
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        date = calendar.get(Calendar.DAY_OF_MONTH);
        //showDate(year, month + 1, date);
        loadThongTin();
        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(1);
            }
        });
        imgbtn_huy = findViewById(R.id.imgbtn_huy);
        imgbtn_huy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                Toast.makeText(SuaUserActivity.this, "Nút hủy sửa thông tin", Toast.LENGTH_LONG).show();
            }
        });

        useredit_mail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                useredit_mail.setBackgroundResource(R.drawable.bg_corner_dnhap);
                useredit_mail.setHintTextColor(getResources().getColor(R.color.black));
                useredit_mail.setHint("Mail");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        useredit_hoten.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                useredit_hoten.setBackgroundResource(R.drawable.bg_corner_dnhap);
                useredit_hoten.setHintTextColor(getResources().getColor(R.color.black));
                useredit_hoten.setHint("Họ tên");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        imgbtn_luu = findViewById(R.id.imgbtn_luu);
        imgbtn_luu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ngaysinh=btnDate.getText().toString();
                String hoten=useredit_hoten.getText().toString();
                String mail=useredit_mail.getText().toString();
                if(hoten.length()==0||mail.length()==0){
                    if(hoten.length()==0){
                        useredit_hoten.setBackgroundResource(R.drawable.bg_warning);
                        useredit_hoten.setHintTextColor(getResources().getColor(R.color.red));
                        useredit_hoten.setHint("Điền tên của bạn");
                    }
                    if(mail.length()==0){
                        useredit_mail.setBackgroundResource(R.drawable.bg_warning);
                        useredit_mail.setHintTextColor(getResources().getColor(R.color.red));
                        useredit_mail.setHint("Điền mail của bạn");
                    }
                }else{
                    editThongTin(hoten,ngaysinh,mail,v.getContext());
                }
            }
        });

    }
    public void loadThongTin(){
        RequestParams rp = new RequestParams();
        String duongdan="user/"+userid;
        HttpUtils.get(duongdan, rp, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // If the response is JSONObject instead of expected JSONArray
                try {
                    JSONArray lstuser = response.getJSONArray("user");
                    if(lstuser!=null){
                        JSONObject user=lstuser.getJSONObject(0);
                        try {
                            URL url=new URL(user.getString("Anh"));
                            Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                            avatar.setImageBitmap(bmp);
                            hienanh_fullscreen.setImageBitmap(bmp);
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        useredit_hoten.setText(user.getString("HoTen"));
                        String sn=user.getString("NgaySinh");
                        int date=Integer.parseInt(sn.substring(0,2));
                        Log.i("chk","date: "+date);
                        int month=Integer.parseInt(sn.substring(3,5));
                        Log.i("chk","mon: "+month);
                        int year=Integer.parseInt(sn.substring(6,10));
                        Log.i("chk","date: "+year);
                        showDate(year,month,date);
                        useredit_mail.setText(user.getString("Mail"));
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }});
    }
    public void editThongTin(String hoten, String ngaysinh, String mail, Context context){
        JSONObject jsonParams = new JSONObject();
        try {
            String duongdan="user/"+userid;
            jsonParams.put("HoTen",hoten);
            jsonParams.put("NgaySinh",ngaysinh);
            jsonParams.put("Mail",mail);
            jsonParams.put("Anh","https://i.ibb.co/sj8SQyM/pic-macdinh.jpg");
            StringEntity entity = new StringEntity(jsonParams.toString(), HTTP.UTF_8);
            HttpUtils.post(context,duongdan, entity, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    Log.i("chk","update thanh cong");
                    Toast.makeText(context,"Đã cập nhật thông tin",Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(SuaUserActivity.this,ThongTinUserActivity.class);
                    intent.putExtra("UserID",userid);
                    startActivity(intent);
                    finish();
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    Log.i("chk",statusCode+"");
                    Log.i("chk","update that bai");
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void displayAlertDialog() {
        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.dialog_edituser, null);
        final Button xemanh = alertLayout.findViewById(R.id.btn_xemanh);
        final Button chupanh = alertLayout.findViewById(R.id.btn_chupanh);
        final Button chonanh = alertLayout.findViewById(R.id.btn_chonanh);
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setView(alertLayout);
        AlertDialog dialog = alert.create();
        dialog.show();
//nút xem ảnh
        xemanh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hienanh_fullscreen.setVisibility(v.VISIBLE);
                dialog.cancel();
            }
        });
        chupanh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takePicterIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                try {
                    startActivityForResult(takePicterIntent, REQUEST_IMAGE_CAPTURE);
                    dialog.cancel();
                } catch (ActivityNotFoundException e) {
                }
            }
        });
//nút chọn ảnh từ bộ sưu tập
        chonanh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent getPicterIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                try {
                    startActivityForResult(getPicterIntent, REQUEST_GALLERY_IMAGE);
                    dialog.cancel();
                } catch (ActivityNotFoundException e) {
                }
            }
        });
    }
    //hiện ảnh avatar
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        hiện ảnh từ chụp camera
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            avatar.setImageBitmap(imageBitmap);
            hienanh_fullscreen.setImageBitmap(imageBitmap);
        }
//        hiện ảnh từ chọn bộ sưu tập
        else if (requestCode == REQUEST_GALLERY_IMAGE && resultCode == RESULT_OK) {
            try {
                Uri uri = data.getData();
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                avatar.setImageBitmap(bitmap);
                hienanh_fullscreen.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == 1) {
            DatePickerDialog datePickerDialog = new DatePickerDialog(this, dateSetListener, year, month, date);
            DatePicker datePicker = datePickerDialog.getDatePicker();
            datePicker.setMaxDate((System.currentTimeMillis() - 1000));
            return datePickerDialog;
        }
        return null;
    }

    DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            showDate(year, month + 1, dayOfMonth);
        }
    };

    private void showDate(int year, int month, int date) {
        String ngay;
        String thang;
        if (date < 10) {
            ngay = "0" + date;
        } else {
            ngay = date + "";
        }
        if (month < 10) {
            thang = "0" + month;
        } else {
            thang = month + "";
        }
        btnDate.setText(ngay + "/" + thang + "/" + year);
    }
}
