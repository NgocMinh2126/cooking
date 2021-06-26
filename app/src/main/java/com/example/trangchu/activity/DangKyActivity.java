package com.example.trangchu.activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;
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

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Calendar;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;
import de.hdodenhof.circleimageview.CircleImageView;

public class DangKyActivity extends AppCompatActivity {
    TextInputLayout layout_username, layout_name, layout_mail, layout_password, layout_confirmpass;
    TextInputEditText dangky_username, dangky_hoten, dangky_email, dangky_pass, dangky_confirmpass;
    Button btn_dob, btn_dangki;
    CheckBox chk_dkhoan;
    ImageButton imgbtn_themanh, imgbtn_trove;
    Calendar calendar;
    TextView tv_chk_dkhoan;
    int date, month, year;
    CircleImageView avatar;
    public static int REQUEST_IMAGE_CAPTURE = 1;
    public static int REQUEST_GALLERY_IMAGE = 2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dangky);
//        Nút quay trở về trang đăng nhập
        imgbtn_trove = findViewById(R.id.imgbtn_trove);
        imgbtn_trove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        //    Thêm Ảnh Avatar
        avatar = findViewById(R.id.avatar);
        imgbtn_themanh = findViewById(R.id.imgbtn_themanh);
        imgbtn_themanh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayAlertDialog();
            }
        });
        chk_dkhoan = findViewById(R.id.dangky_chk);
        layout_username = findViewById(R.id.layout_username);
        layout_name = findViewById(R.id.layout_name);
        layout_mail = findViewById(R.id.layout_mail);
        layout_password = findViewById(R.id.layout_password);
        layout_confirmpass = findViewById(R.id.layout_confirmpass);
        dangky_username = findViewById(R.id.dangky_username);
        dangky_hoten = findViewById(R.id.dangky_hoten);
        dangky_email = findViewById(R.id.dangky_email);
        dangky_pass = findViewById(R.id.dangky_pass);
        dangky_confirmpass = findViewById(R.id.dangky_comfirmpass);
        tv_chk_dkhoan = findViewById(R.id.dangky_chk_dkhoan);
        //Cick chọn ngày sinh
        btn_dob = findViewById(R.id.btn_dob);
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        date = calendar.get(Calendar.DAY_OF_MONTH);
        showDate(year, month + 1, date);
        btn_dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(1);
            }
        });
        //su kien thay doi edit text

        dangky_username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                layout_username.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        dangky_hoten.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                layout_name.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        dangky_email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                layout_mail.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        dangky_pass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                layout_password.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        dangky_confirmpass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                layout_confirmpass.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        //     Click đăng kí
        btn_dangki = findViewById(R.id.btn_dangki);
        btn_dangki.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = dangky_username.getText().toString();
                String hoten = dangky_hoten.getText().toString();
                String ngaysinh = btn_dob.getText().toString();
                Log.i("chkdangky", ngaysinh + "");
                String pass = dangky_pass.getText().toString();
                String comfirm_pass = dangky_confirmpass.getText().toString();
                String email = dangky_email.getText().toString();
                if (chk_dkhoan.isChecked() == false) {
                    Toast.makeText(v.getContext(), "Vui lòng đồng ý với điều khoản để tiếp tục", Toast.LENGTH_LONG).show();
                    tv_chk_dkhoan.setTextColor(getResources().getColor(R.color.red));
                }
                if (username.length() == 0 || email.length() == 0 || hoten.length() == 0 || pass.length() == 0 || comfirm_pass.length() == 0) {
                    if (username.length() == 0) {
                        layout_username.setError("Điền username");
                    }
                    if (email.length() == 0) {
                        layout_mail.setError("Điền email");
                    }
                    if (hoten.length() == 0) {
                        layout_name.setError("Điền họ tên");
                    }
                    if (pass.length() == 0) {
                        layout_password.setError("Điền mật khẩu");
                    }
                    if (comfirm_pass.length() == 0) {
                        layout_confirmpass.setError("Điền lại mật khẩu");
                    }
                } else if (pass.equals(comfirm_pass) == false) {
                    layout_confirmpass.setError("Mật khẩu không khớp");
                } else {
                    dky(username,email,hoten,pass,ngaysinh,v.getContext());
                }
            }
        });
    }

    ;

    //Dialog set ảnh avatar
    public void displayAlertDialog() {
        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.dialog_dangky, null);
        final Button chupanh = alertLayout.findViewById(R.id.btn_chupanh);
        final Button chonanh = alertLayout.findViewById(R.id.btn_chonanh);
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setView(alertLayout);
        AlertDialog dialog = alert.create();
        dialog.show();
//        chọn camera để chụp ảnh
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
//        chọn ảnh từ bộ sưu tập
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
//        hiện ảnh từ camera
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            avatar.setImageBitmap(imageBitmap);
        }
//        hiện ảnh từ bộ sưu tập
        else if (requestCode == REQUEST_GALLERY_IMAGE && resultCode == RESULT_OK) {
            try {
                Uri uri = data.getData();
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                avatar.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //    Chọn ngày sinh từ DatePicker
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
        Log.i("chkdangky", "nam: " + year);
        Log.i("chkdangky", "thang: " + month);
        Log.i("chkdangky", "ngay: " + date);
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
        btn_dob.setText(ngay + "/" + thang + "/" + year);
    }

    public void dky(String username, String mail, String hoten, String pass, String ngaysinh, Context context){
        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("Anh","https://i.ibb.co/sj8SQyM/pic-macdinh.jpg");
            jsonParams.put("Username", username);
            jsonParams.put("HoTen",hoten);
            jsonParams.put("NgaySinh",ngaysinh);
            jsonParams.put("Password",pass);
            jsonParams.put("Mail",mail);
            jsonParams.put("PhanQuyen",1);
            StringEntity entity = new StringEntity(jsonParams.toString());
            HttpUtils.post(context,"user/", entity, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    Toast.makeText(context,"Đăng ký thành công. Vui lòng đăng nhập lại để tiếp tục",Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(DangKyActivity.this,DangNhapActivity.class);
                    startActivity(intent);
                    finish();
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    layout_username.setError("Username này đã có người dùng");
                }
            });
        } catch (JSONException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
