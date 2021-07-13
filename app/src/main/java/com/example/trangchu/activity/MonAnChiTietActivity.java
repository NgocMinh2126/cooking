package com.example.trangchu.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.trangchu.IRecycleViewClickListerner;
import com.example.trangchu.R;
import com.example.trangchu.adapters.DSMonAnAdapter;
import com.example.trangchu.adapters.MonAnChiTietPagerAdapter;
import com.example.trangchu.models.MonAn;
import com.example.trangchu.service.HttpUtils;
import com.google.android.material.tabs.TabLayout;
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
import java.util.Hashtable;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.protocol.HTTP;

public class MonAnChiTietActivity extends AppCompatActivity {
    private ImageView img_anhmonan;
    private TextView tenmonan;
    private ToggleButton btn_like;
    private ImageButton btn_back, btn_share;
    private LinearLayout chitiet_data, chitiet_ani;
    private TabLayout tablayout;
    private ViewPager viewpager;
    private Button btnthem;
    String userid;
    String idmonan;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.monan_chitiet);
        userid = this.getIntent().getStringExtra("UserID");
        idmonan = this.getIntent().getStringExtra("IDMonAn");
        img_anhmonan = findViewById(R.id.monan_chitiet_anh);
        tenmonan = findViewById(R.id.monan_chitiet_tenmon);
        btn_like = findViewById(R.id.monan_chitiet_like_btn);
        btn_back = findViewById(R.id.monan_chitiet_back_btn);
        chitiet_data = findViewById(R.id.monan_chitiet_data);
        chitiet_ani = findViewById(R.id.monan_chitiet_ani);
        tablayout = findViewById(R.id.monan_chitiet_tablayout);
        viewpager = findViewById(R.id.monan_chitiet_viewpager);
        btn_share = findViewById(R.id.monan_chitiet_share_btn);
        btnthem = (Button) findViewById(R.id.btnthem);


        setDataForMonAn(userid, idmonan);

        //set fragment
        MonAnChiTietPagerAdapter monAnChiTietPagerAdapter = new MonAnChiTietPagerAdapter(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        viewpager.setAdapter(monAnChiTietPagerAdapter);
        tablayout.setupWithViewPager(viewpager);
        btn_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                String ten = tenmonan.getText()+"";
                String url = "https://evening-tor-29508.herokuapp.com/monan/"+idmonan;
                intent.putExtra(Intent.EXTRA_TEXT,"Tên món ăn: "+ ten + "\n" + "Đường link: " + url);
                startActivity(Intent.createChooser(intent,"Share mon an"));
            }
        });
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        if (userid.equals("null") == true) {
            btnthem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(v.getContext(),"Vui lòng đăng nhập để thêm món ăn vào thực đơn",Toast.LENGTH_SHORT).show();

                }
            });
            btn_like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    btn_like.setChecked(false);
                    Toast.makeText(v.getContext(), "Vui lòng đăng nhập để thêm món ăn yêu thích", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            btnthem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i("chkbtn","click ");
                    openDialogThemThucDon(Gravity.CENTER,userid,idmonan);
                }
            });
            btn_like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean checked = ((ToggleButton) v).isChecked();
                    if (checked == true) {
                        ThemMonAnYeuThich(userid, idmonan, v);
                        Log.i("chk", "da thich");
                    } else {
                        OpenXoaMonAnDialog(Gravity.CENTER, idmonan, userid, v.getContext(), btn_like);
                        Log.i("chk", "bo");
                    }
                }
            });
        }
    }

    public void setDataForMonAn(String userid, String idmonan) {
        if (userid.equals("null") == true) {
            String duongdan = "monan/" + idmonan;
            RequestParams rp = new RequestParams();
            HttpUtils.get(duongdan, rp, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    try {
                        JSONObject chitiet = response.getJSONObject("m");
                        MonAn m = new MonAn(chitiet.getString("_id"), chitiet.getString("TenMon"), chitiet.getString("Anh"));
                        tenmonan.setText(m.getTenMonAn());
                        URL url = null;
                        try {
                            url = new URL(m.getAnh());
                            Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                            img_anhmonan.setImageBitmap(bmp);
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                            Log.i("chk", "Ko load duoc anh");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        chitiet_ani.setVisibility(View.GONE);
                        chitiet_data.setVisibility(View.VISIBLE);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
            return;
        } else {
            String duongdan = "monan/" + idmonan;
            RequestParams rp = new RequestParams();
            Hashtable<String, String> headers = new Hashtable<>();
            headers.put("iduser", userid);
            HttpUtils.get(duongdan, rp, headers, new JsonHttpResponseHandler() {
                @Override
                //statusCode: ma loi tra tu service ve
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    try {
                        JSONObject chitiet = response.getJSONObject("m");
                        MonAn m = new MonAn(chitiet.getString("_id"), chitiet.getString("TenMon"), chitiet.getString("Anh"), chitiet.getInt("Thich"));
                        tenmonan.setText(m.getTenMonAn());
                        URL url = null;
                        try {
                            url = new URL(m.getAnh());
                            Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                            img_anhmonan.setImageBitmap(bmp);
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                            Log.i("chk", "Ko load duoc anh");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        if (m.getDaThich() == 1) {
                            btn_like.setChecked(true);
                        }
                        chitiet_ani.setVisibility(View.GONE);
                        chitiet_data.setVisibility(View.VISIBLE);
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    public void OpenXoaMonAnDialog(int gravity, String monan, String userid, Context context, ToggleButton btn) {
        final Dialog xoadialog = new Dialog(context);
        xoadialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        xoadialog.setContentView(R.layout.monanyeuthich_xoadialog);
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
            btn.setChecked(true);
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
                btn.setChecked(true);
            }
        });
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                XoaMonAnYeuThich(userid, monan, v);
                xoadialog.dismiss();
                btn.setChecked(false);
            }
        });
        xoadialog.show();
    }

    public void XoaMonAnYeuThich(String userid, String monan, View v) {
        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("IDUser", userid);
            jsonParams.put("IDMonAn", monan);
            StringEntity entity = new StringEntity(jsonParams.toString());
            HttpUtils.post(v.getContext(), "monanyeuthich/", entity, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    Log.i("chk", "xoa ra khoi dsach yeu thich");
                    Toast.makeText(v.getContext(), "Đã xóa khỏi danh sách yêu thích", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    Log.i("chk", "ko the xoa ra khoi dsach yeu thich");
                }
            });
        } catch (JSONException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public void ThemMonAnYeuThich(String userid, String monan, View v) {
        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("IDUser", userid);
            jsonParams.put("IDMonAn", monan);
            Log.i("chk", "userid: " + userid);
            StringEntity entity = new StringEntity(jsonParams.toString());
            HttpUtils.post(v.getContext(), "monanyeuthich/", entity, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    Toast.makeText(v.getContext(), "Đã thêm vào danh sách yêu thích", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    Log.i("chk", "ko the xoa ra khoi dsach yeu thich");
                }
            });
        } catch (JSONException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private void openDialogThemThucDon(int gravity, String userid, String idmonan) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.themthucdon_dialog);
        Window window = dialog.getWindow();
        if (window == null) {
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams win = window.getAttributes();
        win.gravity = gravity;
        window.setAttributes(win);

        Button btn1 = (Button) dialog.findViewById(R.id.btn1);
        Button btn2 = (Button) dialog.findViewById(R.id.btn2);
        Button btn3 = (Button) dialog.findViewById(R.id.btn3);
        Button btn4 = (Button) dialog.findViewById(R.id.btn4);
        Button btn5 = (Button) dialog.findViewById(R.id.btn5);
        Button btn6 = (Button) dialog.findViewById(R.id.btn6);
        Button btn7 = (Button) dialog.findViewById(R.id.btn7);

        final int[] co1 = {0};
        final int[] co2 = {0};
        final int[] co3 = {0};
        final int[] co4 = {0};
        final int[] co5 = {0};
        final int[] co6 = {0};
        final int[] co7 = {0};

        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH) + 1;
        int date = c.get(Calendar.DATE);
        int date1, month1;
        switch (month) {
            case 2: {
                if (year % 400 == 0 || (year % 4 == 0 && year % 100 != 0)) {
                    {
                        if (date < 10 && (date + 6) < 10) {
                            btn1.setText("0" + month + "/" + "0" + date);
                            btn2.setText("0" + month + "/" + "0" + (date + 1));
                            btn3.setText("0" + month + "/" + "0" + (date + 2));
                            btn4.setText("0" + month + "/" + "0" + (date + 3));
                            btn5.setText("0" + month + "/" + "0" + (date + 4));
                            btn6.setText("0" + month + "/" + "0" + (date + 5));
                            btn7.setText("0" + month + "/" + "0" + (date + 6));
                            break;
                        } else if (date < 10 && (date + 6) == 10) {
                            btn1.setText("0" + month + "/" + "0" + date);
                            btn2.setText("0" + month + "/" + "0" + (date + 1));
                            btn3.setText("0" + month + "/" + "0" + (date + 2));
                            btn4.setText("0" + month + "/" + "0" + (date + 3));
                            btn5.setText("0" + month + "/" + "0" + (date + 4));
                            btn6.setText("0" + month + "/" + "0" + (date + 5));
                            btn7.setText("0" + month + "/" + (date + 6));
                            break;
                        } else if (date < 10 && (date + 5) == 10) {
                            btn1.setText("0" + month + "/" + "0" + date);
                            btn2.setText("0" + month + "/" + "0" + (date + 1));
                            btn3.setText("0" + month + "/" + "0" + (date + 2));
                            btn4.setText("0" + month + "/" + "0" + (date + 3));
                            btn5.setText("0" + month + "/" + "0" + (date + 4));
                            btn6.setText("0" + month + "/" + (date + 5));
                            btn7.setText("0" + month + "/" + (date + 6));
                            break;
                        } else if (date < 10 && (date + 4) == 10) {
                            btn1.setText("0" + month + "/" + "0" + date);
                            btn2.setText("0" + month + "/" + "0" + (date + 1));
                            btn3.setText("0" + month + "/" + "0" + (date + 2));
                            btn4.setText("0" + month + "/" + "0" + (date + 3));
                            btn5.setText("0" + month + "/" + (date + 4));
                            btn6.setText("0" + month + "/" + (date + 5));
                            btn7.setText("0" + month + "/" + (date + 6));
                            break;
                        } else if (date < 10 && (date + 3) == 10) {
                            btn1.setText("0" + month + "/" + "0" + date);
                            btn2.setText("0" + month + "/" + "0" + (date + 1));
                            btn3.setText("0" + month + "/" + "0" + (date + 2));
                            btn4.setText("0" + month + "/" + (date + 3));
                            btn5.setText("0" + month + "/" + (date + 4));
                            btn6.setText("0" + month + "/" + (date + 5));
                            btn7.setText("0" + month + "/" + (date + 6));
                            break;
                        } else if (date < 10 && (date + 2) == 10) {
                            btn1.setText("0" + month + "/" + "0" + date);
                            btn2.setText("0" + month + "/" + "0" + (date + 1));
                            btn3.setText("0" + month + "/" + (date + 2));
                            btn4.setText("0" + month + "/" + (date + 3));
                            btn5.setText("0" + month + "/" + (date + 4));
                            btn6.setText("0" + month + "/" + (date + 5));
                            btn7.setText("0" + month + "/" + (date + 6));
                            break;
                        } else if (date < 10 && (date + 1) == 10) {
                            btn1.setText("0" + month + "/" + "0" + date);
                            btn2.setText("0" + month + "/" + (date + 1));
                            btn3.setText("0" + month + "/" + (date + 2));
                            btn4.setText("0" + month + "/" + (date + 3));
                            btn5.setText("0" + month + "/" + (date + 4));
                            btn6.setText("0" + month + "/" + (date + 5));
                            btn7.setText("0" + month + "/" + (date + 6));
                            break;
                        } else if (date + 1 > 29) {
                            date1 = 1;
                            month1 = month + 1;
                            btn1.setText("0" + month + "/" + date);
                            btn2.setText("0" + month1 + "/" + "0" + date1);
                            btn3.setText("0" + month1 + "/" + "0" + (date1 + 1));
                            btn4.setText("0" + month1 + "/" + "0" + (date1 + 2));
                            btn5.setText("0" + month1 + "/" + "0" + (date1 + 3));
                            btn6.setText("0" + month1 + "/" + "0" + (date1 + 4));
                            btn7.setText("0" + month1 + "/" + "0" + (date1 + 5));
                            break;
                        } else if (date + 2 > 29) {
                            date1 = 1;
                            month1 = month + 1;
                            btn1.setText("0" + month + "/" + date);
                            btn2.setText("0" + month + "/" + (date + 1));
                            btn3.setText("0" + month1 + "/" + "0" + (date1));
                            btn4.setText("0" + month1 + "/" + "0" + (date1 + 1));
                            btn5.setText("0" + month1 + "/" + "0" + (date1 + 2));
                            btn6.setText("0" + month1 + "/" + "0" + (date1 + 3));
                            btn7.setText("0" + month1 + "/" + "0" + (date1 + 4));
                            break;
                        } else if (date + 3 > 29) {
                            date1 = 1;
                            month1 = month + 1;
                            btn1.setText("0" + month + "/" + date);
                            btn2.setText("0" + month + "/" + (date + 1));
                            btn3.setText("0" + month + "/" + (date + 2));
                            btn4.setText("0" + month1 + "/" + "0" + (date1));
                            btn5.setText("0" + month1 + "/" + "0" + (date1 + 1));
                            btn6.setText("0" + month1 + "/" + "0" + (date1 + 2));
                            btn7.setText("0" + month1 + "/" + "0" + (date1 + 3));
                            break;
                        } else if (date + 4 > 29) {
                            date1 = 1;
                            month1 = month + 1;
                            btn1.setText("0" + month + "/" + date);
                            btn2.setText("0" + month + "/" + (date + 1));
                            btn3.setText("0" + month + "/" + (date + 2));
                            btn4.setText("0" + month + "/" + (date + 3));
                            btn5.setText("0" + month1 + "/" + "0" + (date1));
                            btn6.setText("0" + month1 + "/" + "0" + (date1 + 1));
                            btn7.setText("0" + month1 + "/" + "0" + (date1 + 2));
                            break;
                        } else if (date + 5 > 29) {
                            date1 = 1;
                            month1 = month + 1;
                            btn1.setText("0" + month + "/" + date);
                            btn2.setText("0" + month + "/" + (date + 1));
                            btn3.setText("0" + month + "/" + (date + 2));
                            btn4.setText("0" + month + "/" + (date + 3));
                            btn5.setText("0" + month + "/" + (date + 4));
                            btn6.setText("0" + month1 + "/" + "0" + (date1));
                            btn7.setText("0" + month1 + "/" + "0" + (date1 + 1));
                            break;
                        } else if (date + 6 > 29) {
                            date1 = 1;
                            month1 = month + 1;
                            btn1.setText("0" + month + "/" + date);
                            btn2.setText("0" + month + "/" + (date + 1));
                            btn3.setText("0" + month + "/" + (date + 2));
                            btn4.setText("0" + month + "/" + (date + 3));
                            btn5.setText("0" + month + "/" + (date + 4));
                            btn6.setText("0" + month + "/" + (date + 5));
                            btn7.setText("0" + month1 + "/" + "0" + (date1));
                            break;
                        } else {
                            btn1.setText("0" + month + "/" + date);
                            btn2.setText("0" + month + "/" + (date + 1));
                            btn3.setText("0" + month + "/" + (date + 2));
                            btn4.setText("0" + month + "/" + (date + 3));
                            btn5.setText("0" + month + "/" + (date + 4));
                            btn6.setText("0" + month + "/" + (date + 5));
                            btn7.setText("0" + month + "/" + (date + 6));
                            break;
                        }
                    }
                } else {
                    if (date < 10 && (date + 6) < 10) {
                        btn1.setText("0" + month + "/" + "0" + date);
                        btn2.setText("0" + month + "/" + "0" + (date + 1));
                        btn3.setText("0" + month + "/" + "0" + (date + 2));
                        btn4.setText("0" + month + "/" + "0" + (date + 3));
                        btn5.setText("0" + month + "/" + "0" + (date + 4));
                        btn6.setText("0" + month + "/" + "0" + (date + 5));
                        btn7.setText("0" + month + "/" + "0" + (date + 6));
                        break;
                    } else if (date < 10 && (date + 6) == 10) {
                        btn1.setText("0" + month + "/" + "0" + date);
                        btn2.setText("0" + month + "/" + "0" + (date + 1));
                        btn3.setText("0" + month + "/" + "0" + (date + 2));
                        btn4.setText("0" + month + "/" + "0" + (date + 3));
                        btn5.setText("0" + month + "/" + "0" + (date + 4));
                        btn6.setText("0" + month + "/" + "0" + (date + 5));
                        btn7.setText("0" + month + "/" + (date + 6));
                        break;
                    } else if (date < 10 && (date + 5) == 10) {
                        btn1.setText("0" + month + "/" + "0" + date);
                        btn2.setText("0" + month + "/" + "0" + (date + 1));
                        btn3.setText("0" + month + "/" + "0" + (date + 2));
                        btn4.setText("0" + month + "/" + "0" + (date + 3));
                        btn5.setText("0" + month + "/" + "0" + (date + 4));
                        btn6.setText("0" + month + "/" + (date + 5));
                        btn7.setText("0" + month + "/" + (date + 6));
                        break;
                    } else if (date < 10 && (date + 4) == 10) {
                        btn1.setText("0" + month + "/" + "0" + date);
                        btn2.setText("0" + month + "/" + "0" + (date + 1));
                        btn3.setText("0" + month + "/" + "0" + (date + 2));
                        btn4.setText("0" + month + "/" + "0" + (date + 3));
                        btn5.setText("0" + month + "/" + (date + 4));
                        btn6.setText("0" + month + "/" + (date + 5));
                        btn7.setText("0" + month + "/" + (date + 6));
                        break;
                    } else if (date < 10 && (date + 3) == 10) {
                        btn1.setText("0" + month + "/" + "0" + date);
                        btn2.setText("0" + month + "/" + "0" + (date + 1));
                        btn3.setText("0" + month + "/" + "0" + (date + 2));
                        btn4.setText("0" + month + "/" + (date + 3));
                        btn5.setText("0" + month + "/" + (date + 4));
                        btn6.setText("0" + month + "/" + (date + 5));
                        btn7.setText("0" + month + "/" + (date + 6));
                        break;
                    } else if (date < 10 && (date + 2) == 10) {
                        btn1.setText("0" + month + "/" + "0" + date);
                        btn2.setText("0" + month + "/" + "0" + (date + 1));
                        btn3.setText("0" + month + "/" + (date + 2));
                        btn4.setText("0" + month + "/" + (date + 3));
                        btn5.setText("0" + month + "/" + (date + 4));
                        btn6.setText("0" + month + "/" + (date + 5));
                        btn7.setText("0" + month + "/" + (date + 6));
                        break;
                    } else if (date < 10 && (date + 1) == 10) {
                        btn1.setText("0" + month + "/" + "0" + date);
                        btn2.setText("0" + month + "/" + (date + 1));
                        btn3.setText("0" + month + "/" + (date + 2));
                        btn4.setText("0" + month + "/" + (date + 3));
                        btn5.setText("0" + month + "/" + (date + 4));
                        btn6.setText("0" + month + "/" + (date + 5));
                        btn7.setText("0" + month + "/" + (date + 6));
                        break;
                    } else if (date + 1 > 28) {
                        date1 = 1;
                        month1 = month + 1;
                        btn1.setText("0" + month + "/" + date);
                        btn2.setText("0" + month1 + "/" + "0" + date1);
                        btn3.setText("0" + month1 + "/" + "0" + (date1 + 1));
                        btn4.setText("0" + month1 + "/" + "0" + (date1 + 2));
                        btn5.setText("0" + month1 + "/" + "0" + (date1 + 3));
                        btn6.setText("0" + month1 + "/" + "0" + (date1 + 4));
                        btn7.setText("0" + month1 + "/" + "0" + (date1 + 5));
                        break;
                    } else if (date + 2 > 28) {
                        date1 = 1;
                        month1 = month + 1;
                        btn1.setText("0" + month + "/" + date);
                        btn2.setText("0" + month + "/" + (date + 1));
                        btn3.setText("0" + month1 + "/" + "0" + (date1));
                        btn4.setText("0" + month1 + "/" + "0" + (date1 + 1));
                        btn5.setText("0" + month1 + "/" + "0" + (date1 + 2));
                        btn6.setText("0" + month1 + "/" + "0" + (date1 + 3));
                        btn7.setText("0" + month1 + "/" + "0" + (date1 + 4));
                        break;
                    } else if (date + 3 > 28) {
                        date1 = 1;
                        month1 = month + 1;
                        btn1.setText("0" + month + "/" + date);
                        btn2.setText("0" + month + "/" + (date + 1));
                        btn3.setText("0" + month + "/" + (date + 2));
                        btn4.setText("0" + month1 + "/" + "0" + (date1));
                        btn5.setText("0" + month1 + "/" + "0" + (date1 + 1));
                        btn6.setText("0" + month1 + "/" + "0" + (date1 + 2));
                        btn7.setText("0" + month1 + "/" + "0" + (date1 + 3));
                        break;
                    } else if (date + 4 > 28) {
                        date1 = 1;
                        month1 = month + 1;
                        btn1.setText("0" + month + "/" + date);
                        btn2.setText("0" + month + "/" + (date + 1));
                        btn3.setText("0" + month + "/" + (date + 2));
                        btn4.setText("0" + month + "/" + (date + 3));
                        btn5.setText("0" + month1 + "/" + "0" + (date1));
                        btn6.setText("0" + month1 + "/" + "0" + (date1 + 1));
                        btn7.setText("0" + month1 + "/" + "0" + (date1 + 2));
                        break;
                    } else if (date + 5 > 28) {
                        date1 = 1;
                        month1 = month + 1;
                        btn1.setText("0" + month + "/" + date);
                        btn2.setText("0" + month + "/" + (date + 1));
                        btn3.setText("0" + month + "/" + (date + 2));
                        btn4.setText("0" + month + "/" + (date + 3));
                        btn5.setText("0" + month + "/" + (date + 4));
                        btn6.setText("0" + month1 + "/" + "0" + (date1));
                        btn7.setText("0" + month1 + "/" + "0" + (date1 + 1));
                        break;
                    } else if (date + 6 > 28) {
                        date1 = 1;
                        month1 = month + 1;
                        btn1.setText("0" + month + "/" + date);
                        btn2.setText("0" + month + "/" + (date + 1));
                        btn3.setText("0" + month + "/" + (date + 2));
                        btn4.setText("0" + month + "/" + (date + 3));
                        btn5.setText("0" + month + "/" + (date + 4));
                        btn6.setText("0" + month + "/" + (date + 5));
                        btn7.setText("0" + month1 + "/" + "0" + (date1));
                        break;
                    } else {
                        btn1.setText("0" + month + "/" + date);
                        btn2.setText("0" + month + "/" + (date + 1));
                        btn3.setText("0" + month + "/" + (date + 2));
                        btn4.setText("0" + month + "/" + (date + 3));
                        btn5.setText("0" + month + "/" + (date + 4));
                        btn6.setText("0" + month + "/" + (date + 5));
                        btn7.setText("0" + month + "/" + (date + 6));
                        break;
                    }
                }
            }
            case 1:
            case 3:
            case 5:
            case 7:
            case 8: {
                if (date < 10 && (date + 6) < 10) {
                    btn1.setText("0" + month + "/" + "0" + date);
                    btn2.setText("0" + month + "/" + "0" + (date + 1));
                    btn3.setText("0" + month + "/" + "0" + (date + 2));
                    btn4.setText("0" + month + "/" + "0" + (date + 3));
                    btn5.setText("0" + month + "/" + "0" + (date + 4));
                    btn6.setText("0" + month + "/" + "0" + (date + 5));
                    btn7.setText("0" + month + "/" + "0" + (date + 6));
                    break;
                } else if (date < 10 && (date + 6) == 10) {
                    btn1.setText("0" + month + "/" + "0" + date);
                    btn2.setText("0" + month + "/" + "0" + (date + 1));
                    btn3.setText("0" + month + "/" + "0" + (date + 2));
                    btn4.setText("0" + month + "/" + "0" + (date + 3));
                    btn5.setText("0" + month + "/" + "0" + (date + 4));
                    btn6.setText("0" + month + "/" + "0" + (date + 5));
                    btn7.setText("0" + month + "/" + (date + 6));
                    break;
                } else if (date < 10 && (date + 5) == 10) {
                    btn1.setText("0" + month + "/" + "0" + date);
                    btn2.setText("0" + month + "/" + "0" + (date + 1));
                    btn3.setText("0" + month + "/" + "0" + (date + 2));
                    btn4.setText("0" + month + "/" + "0" + (date + 3));
                    btn5.setText("0" + month + "/" + "0" + (date + 4));
                    btn6.setText("0" + month + "/" + (date + 5));
                    btn7.setText("0" + month + "/" + (date + 6));
                    break;
                } else if (date < 10 && (date + 4) == 10) {
                    btn1.setText("0" + month + "/" + "0" + date);
                    btn2.setText("0" + month + "/" + "0" + (date + 1));
                    btn3.setText("0" + month + "/" + "0" + (date + 2));
                    btn4.setText("0" + month + "/" + "0" + (date + 3));
                    btn5.setText("0" + month + "/" + (date + 4));
                    btn6.setText("0" + month + "/" + (date + 5));
                    btn7.setText("0" + month + "/" + (date + 6));
                    break;
                } else if (date < 10 && (date + 3) == 10) {
                    btn1.setText("0" + month + "/" + "0" + date);
                    btn2.setText("0" + month + "/" + "0" + (date + 1));
                    btn3.setText("0" + month + "/" + "0" + (date + 2));
                    btn4.setText("0" + month + "/" + (date + 3));
                    btn5.setText("0" + month + "/" + (date + 4));
                    btn6.setText("0" + month + "/" + (date + 5));
                    btn7.setText("0" + month + "/" + (date + 6));
                    break;
                } else if (date < 10 && (date + 2) == 10) {
                    btn1.setText("0" + month + "/" + "0" + date);
                    btn2.setText("0" + month + "/" + "0" + (date + 1));
                    btn3.setText("0" + month + "/" + (date + 2));
                    btn4.setText("0" + month + "/" + (date + 3));
                    btn5.setText("0" + month + "/" + (date + 4));
                    btn6.setText("0" + month + "/" + (date + 5));
                    btn7.setText("0" + month + "/" + (date + 6));
                    break;
                } else if (date < 10 && (date + 1) == 10) {
                    btn1.setText("0" + month + "/" + "0" + date);
                    btn2.setText("0" + month + "/" + (date + 1));
                    btn3.setText("0" + month + "/" + (date + 2));
                    btn4.setText("0" + month + "/" + (date + 3));
                    btn5.setText("0" + month + "/" + (date + 4));
                    btn6.setText("0" + month + "/" + (date + 5));
                    btn7.setText("0" + month + "/" + (date + 6));
                    break;
                } else if (date + 1 > 31) {
                    date1 = 1;
                    month1 = month + 1;
                    btn1.setText("0" + month + "/" + date);
                    btn2.setText("0" + month1 + "/" + "0" + date1);
                    btn3.setText("0" + month1 + "/" + "0" + (date1 + 1));
                    btn4.setText("0" + month1 + "/" + "0" + (date1 + 2));
                    btn5.setText("0" + month1 + "/" + "0" + (date1 + 3));
                    btn6.setText("0" + month1 + "/" + "0" + (date1 + 4));
                    btn7.setText("0" + month1 + "/" + "0" + (date1 + 5));
                    break;
                } else if (date + 2 > 31) {
                    date1 = 1;
                    month1 = month + 1;
                    btn1.setText("0" + month + "/" + date);
                    btn2.setText("0" + month + "/" + (date + 1));
                    btn3.setText("0" + month1 + "/" + "0" + (date1));
                    btn4.setText("0" + month1 + "/" + "0" + (date1 + 1));
                    btn5.setText("0" + month1 + "/" + "0" + (date1 + 2));
                    btn6.setText("0" + month1 + "/" + "0" + (date1 + 3));
                    btn7.setText("0" + month1 + "/" + "0" + (date1 + 4));
                    break;
                } else if (date + 3 > 31) {
                    date1 = 1;
                    month1 = month + 1;
                    btn1.setText("0" + month + "/" + date);
                    btn2.setText("0" + month + "/" + (date + 1));
                    btn3.setText("0" + month + "/" + (date + 2));
                    btn4.setText("0" + month1 + "/" + "0" + (date1));
                    btn5.setText("0" + month1 + "/" + "0" + (date1 + 1));
                    btn6.setText("0" + month1 + "/" + "0" + (date1 + 2));
                    btn7.setText("0" + month1 + "/" + "0" + (date1 + 3));
                    break;
                } else if (date + 4 > 31) {
                    date1 = 1;
                    month1 = month + 1;
                    btn1.setText("0" + month + "/" + date);
                    btn2.setText("0" + month + "/" + (date + 1));
                    btn3.setText("0" + month + "/" + (date + 2));
                    btn4.setText("0" + month + "/" + (date + 3));
                    btn5.setText("0" + month1 + "/" + "0" + (date1));
                    btn6.setText("0" + month1 + "/" + "0" + (date1 + 1));
                    btn7.setText("0" + month1 + "/" + "0" + (date1 + 2));
                    break;
                } else if (date + 5 > 31) {
                    date1 = 1;
                    month1 = month + 1;
                    btn1.setText("0" + month + "/" + date);
                    btn2.setText("0" + month + "/" + (date + 1));
                    btn3.setText("0" + month + "/" + (date + 2));
                    btn4.setText("0" + month + "/" + (date + 3));
                    btn5.setText("0" + month + "/" + (date + 4));
                    btn6.setText("0" + month1 + "/" + "0" + (date1));
                    btn7.setText("0" + month1 + "/" + "0" + (date1 + 1));
                    break;
                } else if (date + 6 > 31) {
                    date1 = 1;
                    month1 = month + 1;
                    btn1.setText("0" + month + "/" + date);
                    btn2.setText("0" + month + "/" + (date + 1));
                    btn3.setText("0" + month + "/" + (date + 2));
                    btn4.setText("0" + month + "/" + (date + 3));
                    btn5.setText("0" + month + "/" + (date + 4));
                    btn6.setText("0" + month + "/" + (date + 5));
                    btn7.setText("0" + month1 + "/" + "0" + (date1));
                    break;
                } else {
                    btn1.setText("0" + month + "/" + date);
                    btn2.setText("0" + month + "/" + (date + 1));
                    btn3.setText("0" + month + "/" + (date + 2));
                    btn4.setText("0" + month + "/" + (date + 3));
                    btn5.setText("0" + month + "/" + (date + 4));
                    btn6.setText("0" + month + "/" + (date + 5));
                    btn7.setText("0" + month + "/" + (date + 6));
                    break;
                }
            }
            case 10: {
                if (date < 10 && (date + 6) < 10) {
                    btn1.setText(month + "/" + "0" + date);
                    btn2.setText(month + "/" + "0" + (date + 1));
                    btn3.setText(month + "/" + "0" + (date + 2));
                    btn4.setText(month + "/" + "0" + (date + 3));
                    btn5.setText(month + "/" + "0" + (date + 4));
                    btn6.setText(month + "/" + "0" + (date + 5));
                    btn7.setText(month + "/" + "0" + (date + 6));
                    break;
                } else if (date < 10 && (date + 6) == 10) {
                    btn1.setText(month + "/" + "0" + date);
                    btn2.setText(month + "/" + "0" + (date + 1));
                    btn3.setText(month + "/" + "0" + (date + 2));
                    btn4.setText(month + "/" + "0" + (date + 3));
                    btn5.setText(month + "/" + "0" + (date + 4));
                    btn6.setText(month + "/" + "0" + (date + 5));
                    btn7.setText(month + "/" + (date + 6));
                    break;
                } else if (date < 10 && (date + 5) == 10) {
                    btn1.setText(month + "/" + "0" + date);
                    btn2.setText(month + "/" + "0" + (date + 1));
                    btn3.setText(month + "/" + "0" + (date + 2));
                    btn4.setText(month + "/" + "0" + (date + 3));
                    btn5.setText(month + "/" + "0" + (date + 4));
                    btn6.setText(month + "/" + (date + 5));
                    btn7.setText(month + "/" + (date + 6));
                    break;
                } else if (date < 10 && (date + 4) == 10) {
                    btn1.setText(month + "/" + "0" + date);
                    btn2.setText(month + "/" + "0" + (date + 1));
                    btn3.setText(month + "/" + "0" + (date + 2));
                    btn4.setText(month + "/" + "0" + (date + 3));
                    btn5.setText(month + "/" + (date + 4));
                    btn6.setText(month + "/" + (date + 5));
                    btn7.setText(month + "/" + (date + 6));
                    break;
                } else if (date < 10 && (date + 3) == 10) {
                    btn1.setText(month + "/" + "0" + date);
                    btn2.setText(month + "/" + "0" + (date + 1));
                    btn3.setText(month + "/" + "0" + (date + 2));
                    btn4.setText(month + "/" + (date + 3));
                    btn5.setText(month + "/" + (date + 4));
                    btn6.setText(month + "/" + (date + 5));
                    btn7.setText(month + "/" + (date + 6));
                    break;
                } else if (date < 10 && (date + 2) == 10) {
                    btn1.setText(month + "/" + "0" + date);
                    btn2.setText(month + "/" + "0" + (date + 1));
                    btn3.setText(month + "/" + (date + 2));
                    btn4.setText(month + "/" + (date + 3));
                    btn5.setText(month + "/" + (date + 4));
                    btn6.setText(month + "/" + (date + 5));
                    btn7.setText(month + "/" + (date + 6));
                    break;
                } else if (date < 10 && (date + 1) == 10) {
                    btn1.setText(month + "/" + "0" + date);
                    btn2.setText(month + "/" + (date + 1));
                    btn3.setText(month + "/" + (date + 2));
                    btn4.setText(month + "/" + (date + 3));
                    btn5.setText(month + "/" + (date + 4));
                    btn6.setText(month + "/" + (date + 5));
                    btn7.setText(month + "/" + (date + 6));
                    break;
                } else if (date + 1 > 31) {
                    date1 = 1;
                    month1 = month + 1;
                    btn1.setText(month + "/" + date);
                    btn2.setText(month1 + "/" + "0" + date1);
                    btn3.setText(month1 + "/" + "0" + (date1 + 1));
                    btn4.setText(month1 + "/" + "0" + (date1 + 2));
                    btn5.setText(month1 + "/" + "0" + (date1 + 3));
                    btn6.setText(month1 + "/" + "0" + (date1 + 4));
                    btn7.setText(month1 + "/" + "0" + (date1 + 5));
                    break;
                } else if (date + 2 > 31) {
                    date1 = 1;
                    month1 = month + 1;
                    btn1.setText(month + "/" + date);
                    btn2.setText(month + "/" + (date + 1));
                    btn3.setText(month1 + "/" + "0" + (date1));
                    btn4.setText(month1 + "/" + "0" + (date1 + 1));
                    btn5.setText(month1 + "/" + "0" + (date1 + 2));
                    btn6.setText(month1 + "/" + "0" + (date1 + 3));
                    btn7.setText(month1 + "/" + "0" + (date1 + 4));
                    break;
                } else if (date + 3 > 31) {
                    date1 = 1;
                    month1 = month + 1;
                    btn1.setText(month + "/" + date);
                    btn2.setText(month + "/" + (date + 1));
                    btn3.setText(month + "/" + (date + 2));
                    btn4.setText(month1 + "/" + "0" + (date1));
                    btn5.setText(month1 + "/" + "0" + (date1 + 1));
                    btn6.setText(month1 + "/" + "0" + (date1 + 2));
                    btn7.setText(month1 + "/" + "0" + (date1 + 3));
                    break;
                } else if (date + 4 > 31) {
                    date1 = 1;
                    month1 = month + 1;
                    btn1.setText(month + "/" + date);
                    btn2.setText(month + "/" + (date + 1));
                    btn3.setText(month + "/" + (date + 2));
                    btn4.setText(month + "/" + (date + 3));
                    btn5.setText(month1 + "/" + "0" + (date1));
                    btn6.setText(month1 + "/" + "0" + (date1 + 1));
                    btn7.setText(month1 + "/" + "0" + (date1 + 2));
                    break;
                } else if (date + 5 > 31) {
                    date1 = 1;
                    month1 = month + 1;
                    btn1.setText(month + "/" + date);
                    btn2.setText(month + "/" + (date + 1));
                    btn3.setText(month + "/" + (date + 2));
                    btn4.setText(month + "/" + (date + 3));
                    btn5.setText(month + "/" + (date + 4));
                    btn6.setText(month1 + "/" + "0" + (date1));
                    btn7.setText(month1 + "/" + "0" + (date1 + 1));
                    break;
                } else if (date + 6 > 31) {
                    date1 = 1;
                    month1 = month + 1;
                    btn1.setText(month + "/" + date);
                    btn2.setText(month + "/" + (date + 1));
                    btn3.setText(month + "/" + (date + 2));
                    btn4.setText(month + "/" + (date + 3));
                    btn5.setText(month + "/" + (date + 4));
                    btn6.setText(month + "/" + (date + 5));
                    btn7.setText(month1 + "/" + "0" + (date1));
                    break;
                } else {
                    btn1.setText(month + "/" + date);
                    btn2.setText(month + "/" + (date + 1));
                    btn3.setText(month + "/" + (date + 2));
                    btn4.setText(month + "/" + (date + 3));
                    btn5.setText(month + "/" + (date + 4));
                    btn6.setText(month + "/" + (date + 5));
                    btn7.setText(month + "/" + (date + 6));
                    break;
                }
            }
            case 12: {
                if (date < 10 && (date + 6) < 10) {
                    btn1.setText(month + "/" + "0" + date);
                    btn2.setText(month + "/" + "0" + (date + 1));
                    btn3.setText(month + "/" + "0" + (date + 2));
                    btn4.setText(month + "/" + "0" + (date + 3));
                    btn5.setText(month + "/" + "0" + (date + 4));
                    btn6.setText(month + "/" + "0" + (date + 5));
                    btn7.setText(month + "/" + "0" + (date + 6));
                    break;
                } else if (date < 10 && (date + 6) == 10) {
                    btn1.setText(month + "/" + "0" + date);
                    btn2.setText(month + "/" + "0" + (date + 1));
                    btn3.setText(month + "/" + "0" + (date + 2));
                    btn4.setText(month + "/" + "0" + (date + 3));
                    btn5.setText(month + "/" + "0" + (date + 4));
                    btn6.setText(month + "/" + "0" + (date + 5));
                    btn7.setText(month + "/" + (date + 6));
                    break;
                } else if (date < 10 && (date + 5) == 10) {
                    btn1.setText(month + "/" + "0" + date);
                    btn2.setText(month + "/" + "0" + (date + 1));
                    btn3.setText(month + "/" + "0" + (date + 2));
                    btn4.setText(month + "/" + "0" + (date + 3));
                    btn5.setText(month + "/" + "0" + (date + 4));
                    btn6.setText(month + "/" + (date + 5));
                    btn7.setText(month + "/" + (date + 6));
                    break;
                } else if (date < 10 && (date + 4) == 10) {
                    btn1.setText(month + "/" + "0" + date);
                    btn2.setText(month + "/" + "0" + (date + 1));
                    btn3.setText(month + "/" + "0" + (date + 2));
                    btn4.setText(month + "/" + "0" + (date + 3));
                    btn5.setText(month + "/" + (date + 4));
                    btn6.setText(month + "/" + (date + 5));
                    btn7.setText(month + "/" + (date + 6));
                    break;
                } else if (date < 10 && (date + 3) == 10) {
                    btn1.setText(month + "/" + "0" + date);
                    btn2.setText(month + "/" + "0" + (date + 1));
                    btn3.setText(month + "/" + "0" + (date + 2));
                    btn4.setText(month + "/" + (date + 3));
                    btn5.setText(month + "/" + (date + 4));
                    btn6.setText(month + "/" + (date + 5));
                    btn7.setText(month + "/" + (date + 6));
                    break;
                } else if (date < 10 && (date + 2) == 10) {
                    btn1.setText(month + "/" + "0" + date);
                    btn2.setText(month + "/" + "0" + (date + 1));
                    btn3.setText(month + "/" + (date + 2));
                    btn4.setText(month + "/" + (date + 3));
                    btn5.setText(month + "/" + (date + 4));
                    btn6.setText(month + "/" + (date + 5));
                    btn7.setText(month + "/" + (date + 6));
                    break;
                } else if (date < 10 && (date + 1) == 10) {
                    btn1.setText(month + "/" + "0" + date);
                    btn2.setText(month + "/" + (date + 1));
                    btn3.setText(month + "/" + (date + 2));
                    btn4.setText(month + "/" + (date + 3));
                    btn5.setText(month + "/" + (date + 4));
                    btn6.setText(month + "/" + (date + 5));
                    btn7.setText(month + "/" + (date + 6));
                    break;
                } else if (date + 1 > 31) {
                    date1 = 1;
                    month1 = 1;
                    year++;
                    btn1.setText(month + "/" + date);
                    btn2.setText("0" + month1 + "/" + "0" + date1);
                    btn3.setText("0" + month1 + "/" + "0" + (date1 + 1));
                    btn4.setText("0" + month1 + "/" + "0" + (date1 + 2));
                    btn5.setText("0" + month1 + "/" + "0" + (date1 + 3));
                    btn6.setText("0" + month1 + "/" + "0" + (date1 + 4));
                    btn7.setText("0" + month1 + "/" + "0" + (date1 + 5));
                    break;
                } else if (date + 2 > 31) {
                    date1 = 1;
                    month1 = 1;
                    year++;
                    btn1.setText(month + "/" + date);
                    btn2.setText(month + "/" + (date + 1));
                    btn3.setText("0" + month1 + "/" + "0" + (date1));
                    btn4.setText("0" + month1 + "/" + "0" + (date1 + 1));
                    btn5.setText("0" + month1 + "/" + "0" + (date1 + 2));
                    btn6.setText("0" + month1 + "/" + "0" + (date1 + 3));
                    btn7.setText("0" + month1 + "/" + "0" + (date1 + 4));
                    break;
                } else if (date + 3 > 31) {
                    date1 = 1;
                    month1 = 1;
                    year++;
                    btn1.setText(month + "/" + date);
                    btn2.setText(month + "/" + (date + 1));
                    btn3.setText(month + "/" + (date + 2));
                    btn4.setText("0" + month1 + "/" + "0" + (date1));
                    btn5.setText("0" + month1 + "/" + "0" + (date1 + 1));
                    btn6.setText("0" + month1 + "/" + "0" + (date1 + 2));
                    btn7.setText("0" + month1 + "/" + "0" + (date1 + 3));
                    break;
                } else if (date + 4 > 31) {
                    date1 = 1;
                    month1 = 1;
                    year++;
                    btn1.setText(month + "/" + date);
                    btn2.setText(month + "/" + (date + 1));
                    btn3.setText(month + "/" + (date + 2));
                    btn4.setText(month + "/" + (date + 3));
                    btn5.setText("0" + month1 + "/" + "0" + (date1));
                    btn6.setText("0" + month1 + "/" + "0" + (date1 + 1));
                    btn7.setText("0" + month1 + "/" + "0" + (date1 + 2));
                    break;
                } else if (date + 5 > 31) {
                    date1 = 1;
                    month1 = 1;
                    year++;
                    btn1.setText(month + "/" + date);
                    btn2.setText(month + "/" + (date + 1));
                    btn3.setText(month + "/" + (date + 2));
                    btn4.setText(month + "/" + (date + 3));
                    btn5.setText(month + "/" + (date + 4));
                    btn6.setText("0" + month1 + "/" + "0" + (date1));
                    btn7.setText("0" + month1 + "/" + "0" + (date1 + 1));
                    break;
                } else if (date + 6 > 31) {
                    date1 = 1;
                    month1 = 1;
                    year++;
                    btn1.setText(month + "/" + date);
                    btn2.setText(month + "/" + (date + 1));
                    btn3.setText(month + "/" + (date + 2));
                    btn4.setText(month + "/" + (date + 3));
                    btn5.setText(month + "/" + (date + 4));
                    btn6.setText(month + "/" + (date + 5));
                    btn7.setText("0" + month1 + "/" + "0" + (date1));
                    break;
                } else {
                    btn1.setText(month + "/" + date);
                    btn2.setText(month + "/" + (date + 1));
                    btn3.setText(month + "/" + (date + 2));
                    btn4.setText(month + "/" + (date + 3));
                    btn5.setText(month + "/" + (date + 4));
                    btn6.setText(month + "/" + (date + 5));
                    btn7.setText(month + "/" + (date + 6));
                    break;
                }
            }
            case 4:
            case 6: {
                if (date < 10 && (date + 6) < 10) {
                    btn1.setText("0" + month + "/" + "0" + date);
                    btn2.setText("0" + month + "/" + "0" + (date + 1));
                    btn3.setText("0" + month + "/" + "0" + (date + 2));
                    btn4.setText("0" + month + "/" + "0" + (date + 3));
                    btn5.setText("0" + month + "/" + "0" + (date + 4));
                    btn6.setText("0" + month + "/" + "0" + (date + 5));
                    btn7.setText("0" + month + "/" + "0" + (date + 6));
                    break;
                } else if (date < 10 && (date + 6) == 10) {
                    btn1.setText("0" + month + "/" + "0" + date);
                    btn2.setText("0" + month + "/" + "0" + (date + 1));
                    btn3.setText("0" + month + "/" + "0" + (date + 2));
                    btn4.setText("0" + month + "/" + "0" + (date + 3));
                    btn5.setText("0" + month + "/" + "0" + (date + 4));
                    btn6.setText("0" + month + "/" + "0" + (date + 5));
                    btn7.setText("0" + month + "/" + (date + 6));
                    break;
                } else if (date < 10 && (date + 5) == 10) {
                    btn1.setText("0" + month + "/" + "0" + date);
                    btn2.setText("0" + month + "/" + "0" + (date + 1));
                    btn3.setText("0" + month + "/" + "0" + (date + 2));
                    btn4.setText("0" + month + "/" + "0" + (date + 3));
                    btn5.setText("0" + month + "/" + "0" + (date + 4));
                    btn6.setText("0" + month + "/" + (date + 5));
                    btn7.setText("0" + month + "/" + (date + 6));
                    break;
                } else if (date < 10 && (date + 4) == 10) {
                    btn1.setText("0" + month + "/" + "0" + date);
                    btn2.setText("0" + month + "/" + "0" + (date + 1));
                    btn3.setText("0" + month + "/" + "0" + (date + 2));
                    btn4.setText("0" + month + "/" + "0" + (date + 3));
                    btn5.setText("0" + month + "/" + (date + 4));
                    btn6.setText("0" + month + "/" + (date + 5));
                    btn7.setText("0" + month + "/" + (date + 6));
                    break;
                } else if (date < 10 && (date + 3) == 10) {
                    btn1.setText("0" + month + "/" + "0" + date);
                    btn2.setText("0" + month + "/" + "0" + (date + 1));
                    btn3.setText("0" + month + "/" + "0" + (date + 2));
                    btn4.setText("0" + month + "/" + (date + 3));
                    btn5.setText("0" + month + "/" + (date + 4));
                    btn6.setText("0" + month + "/" + (date + 5));
                    btn7.setText("0" + month + "/" + (date + 6));
                    break;
                } else if (date < 10 && (date + 2) == 10) {
                    btn1.setText("0" + month + "/" + "0" + date);
                    btn2.setText("0" + month + "/" + "0" + (date + 1));
                    btn3.setText("0" + month + "/" + (date + 2));
                    btn4.setText("0" + month + "/" + (date + 3));
                    btn5.setText("0" + month + "/" + (date + 4));
                    btn6.setText("0" + month + "/" + (date + 5));
                    btn7.setText("0" + month + "/" + (date + 6));
                    break;
                } else if (date < 10 && (date + 1) == 10) {
                    btn1.setText("0" + month + "/" + "0" + date);
                    btn2.setText("0" + month + "/" + (date + 1));
                    btn3.setText("0" + month + "/" + (date + 2));
                    btn4.setText("0" + month + "/" + (date + 3));
                    btn5.setText("0" + month + "/" + (date + 4));
                    btn6.setText("0" + month + "/" + (date + 5));
                    btn7.setText("0" + month + "/" + (date + 6));
                    break;
                } else if (date + 1 > 30) {
                    date1 = 1;
                    month1 = month + 1;
                    btn1.setText("0" + month + "/" + date);
                    btn2.setText("0" + month1 + "/" + "0" + date1);
                    btn3.setText("0" + month1 + "/" + "0" + (date1 + 1));
                    btn4.setText("0" + month1 + "/" + "0" + (date1 + 2));
                    btn5.setText("0" + month1 + "/" + "0" + (date1 + 3));
                    btn6.setText("0" + month1 + "/" + "0" + (date1 + 4));
                    btn7.setText("0" + month1 + "/" + "0" + (date1 + 5));
                    break;
                } else if (date + 2 > 30) {
                    date1 = 1;
                    month1 = month + 1;
                    btn1.setText("0" + month + "/" + date);
                    btn2.setText("0" + month + "/" + (date + 1));
                    btn3.setText("0" + month1 + "/" + "0" + (date1));
                    btn4.setText("0" + month1 + "/" + "0" + (date1 + 1));
                    btn5.setText("0" + month1 + "/" + "0" + (date1 + 2));
                    btn6.setText("0" + month1 + "/" + "0" + (date1 + 3));
                    btn7.setText("0" + month1 + "/" + "0" + (date1 + 4));
                    break;
                } else if (date + 3 > 30) {
                    date1 = 1;
                    month1 = month + 1;
                    btn1.setText("0" + month + "/" + date);
                    btn2.setText("0" + month + "/" + (date + 1));
                    btn3.setText("0" + month + "/" + (date + 2));
                    btn4.setText("0" + month1 + "/" + "0" + (date1));
                    btn5.setText("0" + month1 + "/" + "0" + (date1 + 1));
                    btn6.setText("0" + month1 + "/" + "0" + (date1 + 2));
                    btn7.setText("0" + month1 + "/" + "0" + (date1 + 3));
                    break;
                } else if (date + 4 > 30) {
                    date1 = 1;
                    month1 = month + 1;
                    btn1.setText("0" + month + "/" + date);
                    btn2.setText("0" + month + "/" + (date + 1));
                    btn3.setText("0" + month + "/" + (date + 2));
                    btn4.setText("0" + month + "/" + (date + 3));
                    btn5.setText("0" + month1 + "/" + "0" + (date1));
                    btn6.setText("0" + month1 + "/" + "0" + (date1 + 1));
                    btn7.setText("0" + month1 + "/" + "0" + (date1 + 2));
                    break;
                } else if (date + 5 > 30) {
                    date1 = 1;
                    month1 = month + 1;
                    btn1.setText("0" + month + "/" + date);
                    btn2.setText("0" + month + "/" + (date + 1));
                    btn3.setText("0" + month + "/" + (date + 2));
                    btn4.setText("0" + month + "/" + (date + 3));
                    btn5.setText("0" + month + "/" + (date + 4));
                    btn6.setText("0" + month1 + "/" + "0" + (date1));
                    btn7.setText("0" + month1 + "/" + "0" + (date1 + 1));
                    break;
                } else if (date + 6 > 30) {
                    date1 = 1;
                    month1 = month + 1;
                    btn1.setText("0" + month + "/" + date);
                    btn2.setText("0" + month + "/" + (date + 1));
                    btn3.setText("0" + month + "/" + (date + 2));
                    btn4.setText("0" + month + "/" + (date + 3));
                    btn5.setText("0" + month + "/" + (date + 4));
                    btn6.setText("0" + month + "/" + (date + 5));
                    btn7.setText("0" + month1 + "/" + "0" + (date1));
                    break;
                } else {
                    btn1.setText("0" + month + "/" + date);
                    btn2.setText("0" + month + "/" + (date + 1));
                    btn3.setText("0" + month + "/" + (date + 2));
                    btn4.setText("0" + month + "/" + (date + 3));
                    btn5.setText("0" + month + "/" + (date + 4));
                    btn6.setText("0" + month + "/" + (date + 5));
                    btn7.setText("0" + month + "/" + (date + 6));
                    break;
                }
            }
            case 9: {
                if (date < 10 && (date + 6) < 10) {
                    btn1.setText("0" + month + "/" + "0" + date);
                    btn2.setText("0" + month + "/" + "0" + (date + 1));
                    btn3.setText("0" + month + "/" + "0" + (date + 2));
                    btn4.setText("0" + month + "/" + "0" + (date + 3));
                    btn5.setText("0" + month + "/" + "0" + (date + 4));
                    btn6.setText("0" + month + "/" + "0" + (date + 5));
                    btn7.setText("0" + month + "/" + "0" + (date + 6));
                    break;
                } else if (date < 10 && (date + 6) == 10) {
                    btn1.setText("0" + month + "/" + "0" + date);
                    btn2.setText("0" + month + "/" + "0" + (date + 1));
                    btn3.setText("0" + month + "/" + "0" + (date + 2));
                    btn4.setText("0" + month + "/" + "0" + (date + 3));
                    btn5.setText("0" + month + "/" + "0" + (date + 4));
                    btn6.setText("0" + month + "/" + "0" + (date + 5));
                    btn7.setText("0" + month + "/" + (date + 6));
                    break;
                } else if (date < 10 && (date + 5) == 10) {
                    btn1.setText("0" + month + "/" + "0" + date);
                    btn2.setText("0" + month + "/" + "0" + (date + 1));
                    btn3.setText("0" + month + "/" + "0" + (date + 2));
                    btn4.setText("0" + month + "/" + "0" + (date + 3));
                    btn5.setText("0" + month + "/" + "0" + (date + 4));
                    btn6.setText("0" + month + "/" + (date + 5));
                    btn7.setText("0" + month + "/" + (date + 6));
                    break;
                } else if (date < 10 && (date + 4) == 10) {
                    btn1.setText("0" + month + "/" + "0" + date);
                    btn2.setText("0" + month + "/" + "0" + (date + 1));
                    btn3.setText("0" + month + "/" + "0" + (date + 2));
                    btn4.setText("0" + month + "/" + "0" + (date + 3));
                    btn5.setText("0" + month + "/" + (date + 4));
                    btn6.setText("0" + month + "/" + (date + 5));
                    btn7.setText("0" + month + "/" + (date + 6));
                    break;
                } else if (date < 10 && (date + 3) == 10) {
                    btn1.setText("0" + month + "/" + "0" + date);
                    btn2.setText("0" + month + "/" + "0" + (date + 1));
                    btn3.setText("0" + month + "/" + "0" + (date + 2));
                    btn4.setText("0" + month + "/" + (date + 3));
                    btn5.setText("0" + month + "/" + (date + 4));
                    btn6.setText("0" + month + "/" + (date + 5));
                    btn7.setText("0" + month + "/" + (date + 6));
                    break;
                } else if (date < 10 && (date + 2) == 10) {
                    btn1.setText("0" + month + "/" + "0" + date);
                    btn2.setText("0" + month + "/" + "0" + (date + 1));
                    btn3.setText("0" + month + "/" + (date + 2));
                    btn4.setText("0" + month + "/" + (date + 3));
                    btn5.setText("0" + month + "/" + (date + 4));
                    btn6.setText("0" + month + "/" + (date + 5));
                    btn7.setText("0" + month + "/" + (date + 6));
                    break;
                } else if (date < 10 && (date + 1) == 10) {
                    btn1.setText("0" + month + "/" + "0" + date);
                    btn2.setText("0" + month + "/" + (date + 1));
                    btn3.setText("0" + month + "/" + (date + 2));
                    btn4.setText("0" + month + "/" + (date + 3));
                    btn5.setText("0" + month + "/" + (date + 4));
                    btn6.setText("0" + month + "/" + (date + 5));
                    btn7.setText("0" + month + "/" + (date + 6));
                    break;
                } else if (date + 1 > 30) {
                    date1 = 1;
                    month1 = month + 1;
                    btn1.setText("0" + month + "/" + date);
                    btn2.setText(month1 + "/" + "0" + date1);
                    btn3.setText(month1 + "/" + "0" + (date1 + 1));
                    btn4.setText(month1 + "/" + "0" + (date1 + 2));
                    btn5.setText(month1 + "/" + "0" + (date1 + 3));
                    btn6.setText(month1 + "/" + "0" + (date1 + 4));
                    btn7.setText(month1 + "/" + "0" + (date1 + 5));
                    break;
                } else if (date + 2 > 30) {
                    date1 = 1;
                    month1 = month + 1;
                    btn1.setText("0" + month + "/" + date);
                    btn2.setText("0" + month + "/" + (date + 1));
                    btn3.setText(month1 + "/" + "0" + (date1));
                    btn4.setText(month1 + "/" + "0" + (date1 + 1));
                    btn5.setText(month1 + "/" + "0" + (date1 + 2));
                    btn6.setText(month1 + "/" + "0" + (date1 + 3));
                    btn7.setText(month1 + "/" + "0" + (date1 + 4));
                    break;
                } else if (date + 3 > 30) {
                    date1 = 1;
                    month1 = month + 1;
                    btn1.setText("0" + month + "/" + date);
                    btn2.setText("0" + month + "/" + (date + 1));
                    btn3.setText("0" + month + "/" + (date + 2));
                    btn4.setText(month1 + "/" + "0" + (date1));
                    btn5.setText(month1 + "/" + "0" + (date1 + 1));
                    btn6.setText(month1 + "/" + "0" + (date1 + 2));
                    btn7.setText(month1 + "/" + "0" + (date1 + 3));
                    break;
                } else if (date + 4 > 30) {
                    date1 = 1;
                    month1 = month + 1;
                    btn1.setText("0" + month + "/" + date);
                    btn2.setText("0" + month + "/" + (date + 1));
                    btn3.setText("0" + month + "/" + (date + 2));
                    btn4.setText("0" + month + "/" + (date + 3));
                    btn5.setText(month1 + "/" + "0" + (date1));
                    btn6.setText(month1 + "/" + "0" + (date1 + 1));
                    btn7.setText(month1 + "/" + "0" + (date1 + 2));
                    break;
                } else if (date + 5 > 30) {
                    date1 = 1;
                    month1 = month + 1;
                    btn1.setText("0" + month + "/" + date);
                    btn2.setText("0" + month + "/" + (date + 1));
                    btn3.setText("0" + month + "/" + (date + 2));
                    btn4.setText("0" + month + "/" + (date + 3));
                    btn5.setText("0" + month + "/" + (date + 4));
                    btn6.setText(month1 + "/" + "0" + (date1));
                    btn7.setText(month1 + "/" + "0" + (date1 + 1));
                    break;
                } else if (date + 6 > 30) {
                    date1 = 1;
                    month1 = month + 1;
                    btn1.setText("0" + month + "/" + date);
                    btn2.setText("0" + month + "/" + (date + 1));
                    btn3.setText("0" + month + "/" + (date + 2));
                    btn4.setText("0" + month + "/" + (date + 3));
                    btn5.setText("0" + month + "/" + (date + 4));
                    btn6.setText("0" + month + "/" + (date + 5));
                    btn7.setText(month1 + "/" + "0" + (date1));
                    break;
                } else {
                    btn1.setText("0" + month + "/" + date);
                    btn2.setText("0" + month + "/" + (date + 1));
                    btn3.setText("0" + month + "/" + (date + 2));
                    btn4.setText("0" + month + "/" + (date + 3));
                    btn5.setText("0" + month + "/" + (date + 4));
                    btn6.setText("0" + month + "/" + (date + 5));
                    btn7.setText("0" + month + "/" + (date + 6));
                    break;
                }
            }
            case 11: {
                if (date < 10 && (date + 6) < 10) {
                    btn1.setText(month + "/" + "0" + date);
                    btn2.setText(month + "/" + "0" + (date + 1));
                    btn3.setText(month + "/" + "0" + (date + 2));
                    btn4.setText(month + "/" + "0" + (date + 3));
                    btn5.setText(month + "/" + "0" + (date + 4));
                    btn6.setText(month + "/" + "0" + (date + 5));
                    btn7.setText(month + "/" + "0" + (date + 6));
                    break;
                } else if (date < 10 && (date + 6) == 10) {
                    btn1.setText(month + "/" + "0" + date);
                    btn2.setText(month + "/" + "0" + (date + 1));
                    btn3.setText(month + "/" + "0" + (date + 2));
                    btn4.setText(month + "/" + "0" + (date + 3));
                    btn5.setText(month + "/" + "0" + (date + 4));
                    btn6.setText(month + "/" + "0" + (date + 5));
                    btn7.setText(month + "/" + (date + 6));
                    break;
                } else if (date < 10 && (date + 5) == 10) {
                    btn1.setText(month + "/" + "0" + date);
                    btn2.setText(month + "/" + "0" + (date + 1));
                    btn3.setText(month + "/" + "0" + (date + 2));
                    btn4.setText(month + "/" + "0" + (date + 3));
                    btn5.setText(month + "/" + "0" + (date + 4));
                    btn6.setText(month + "/" + (date + 5));
                    btn7.setText(month + "/" + (date + 6));
                    break;
                } else if (date < 10 && (date + 4) == 10) {
                    btn1.setText(month + "/" + "0" + date);
                    btn2.setText(month + "/" + "0" + (date + 1));
                    btn3.setText(month + "/" + "0" + (date + 2));
                    btn4.setText(month + "/" + "0" + (date + 3));
                    btn5.setText(month + "/" + (date + 4));
                    btn6.setText(month + "/" + (date + 5));
                    btn7.setText(month + "/" + (date + 6));
                    break;
                } else if (date < 10 && (date + 3) == 10) {
                    btn1.setText(month + "/" + "0" + date);
                    btn2.setText(month + "/" + "0" + (date + 1));
                    btn3.setText(month + "/" + "0" + (date + 2));
                    btn4.setText(month + "/" + (date + 3));
                    btn5.setText(month + "/" + (date + 4));
                    btn6.setText(month + "/" + (date + 5));
                    btn7.setText(month + "/" + (date + 6));
                    break;
                } else if (date < 10 && (date + 2) == 10) {
                    btn1.setText(month + "/" + "0" + date);
                    btn2.setText(month + "/" + "0" + (date + 1));
                    btn3.setText(month + "/" + (date + 2));
                    btn4.setText(month + "/" + (date + 3));
                    btn5.setText(month + "/" + (date + 4));
                    btn6.setText(month + "/" + (date + 5));
                    btn7.setText(month + "/" + (date + 6));
                    break;
                } else if (date < 10 && (date + 1) == 10) {
                    btn1.setText(month + "/" + "0" + date);
                    btn2.setText(month + "/" + (date + 1));
                    btn3.setText(month + "/" + (date + 2));
                    btn4.setText(month + "/" + (date + 3));
                    btn5.setText(month + "/" + (date + 4));
                    btn6.setText(month + "/" + (date + 5));
                    btn7.setText(month + "/" + (date + 6));
                    break;
                } else if (date + 1 > 30) {
                    date1 = 1;
                    month1 = month + 1;
                    btn1.setText(month + "/" + date);
                    btn2.setText(month1 + "/" + "0" + date1);
                    btn3.setText(month1 + "/" + "0" + (date1 + 1));
                    btn4.setText(month1 + "/" + "0" + (date1 + 2));
                    btn5.setText(month1 + "/" + "0" + (date1 + 3));
                    btn6.setText(month1 + "/" + "0" + (date1 + 4));
                    btn7.setText(month1 + "/" + "0" + (date1 + 5));
                    break;
                } else if (date + 2 > 30) {
                    date1 = 1;
                    month1 = month + 1;
                    btn1.setText(month + "/" + date);
                    btn2.setText(month + "/" + (date + 1));
                    btn3.setText(month1 + "/" + "0" + (date1));
                    btn4.setText(month1 + "/" + "0" + (date1 + 1));
                    btn5.setText(month1 + "/" + "0" + (date1 + 2));
                    btn6.setText(month1 + "/" + "0" + (date1 + 3));
                    btn7.setText(month1 + "/" + "0" + (date1 + 4));
                    break;
                } else if (date + 3 > 30) {
                    date1 = 1;
                    month1 = month + 1;
                    btn1.setText(month + "/" + date);
                    btn2.setText(month + "/" + (date + 1));
                    btn3.setText(month + "/" + (date + 2));
                    btn4.setText(month1 + "/" + "0" + (date1));
                    btn5.setText(month1 + "/" + "0" + (date1 + 1));
                    btn6.setText(month1 + "/" + "0" + (date1 + 2));
                    btn7.setText(month1 + "/" + "0" + (date1 + 3));
                    break;
                } else if (date + 4 > 30) {
                    date1 = 1;
                    month1 = month + 1;
                    btn1.setText(month + "/" + date);
                    btn2.setText(month + "/" + (date + 1));
                    btn3.setText(month + "/" + (date + 2));
                    btn4.setText(month + "/" + (date + 3));
                    btn5.setText(month1 + "/" + "0" + (date1));
                    btn6.setText(month1 + "/" + "0" + (date1 + 1));
                    btn7.setText(month1 + "/" + "0" + (date1 + 2));
                    break;
                } else if (date + 5 > 30) {
                    date1 = 1;
                    month1 = month + 1;
                    btn1.setText(month + "/" + date);
                    btn2.setText(month + "/" + (date + 1));
                    btn3.setText(month + "/" + (date + 2));
                    btn4.setText(month + "/" + (date + 3));
                    btn5.setText(month + "/" + (date + 4));
                    btn6.setText(month1 + "/" + "0" + (date1));
                    btn7.setText(month1 + "/" + "0" + (date1 + 1));
                    break;
                } else if (date + 6 > 30) {
                    date1 = 1;
                    month1 = month + 1;
                    btn1.setText(month + "/" + date);
                    btn2.setText(month + "/" + (date + 1));
                    btn3.setText(month + "/" + (date + 2));
                    btn4.setText(month + "/" + (date + 3));
                    btn5.setText(month + "/" + (date + 4));
                    btn6.setText(month + "/" + (date + 5));
                    btn7.setText(month1 + "/" + "0" + (date1));
                    break;
                } else {
                    btn1.setText(month + "/" + date);
                    btn2.setText(month + "/" + (date + 1));
                    btn3.setText(month + "/" + (date + 2));
                    btn4.setText(month + "/" + (date + 3));
                    btn5.setText(month + "/" + (date + 4));
                    btn6.setText(month + "/" + (date + 5));
                    btn7.setText(month + "/" + (date + 6));
                    break;
                }
            }
        }

        Button btnb1 = (Button) dialog.findViewById(R.id.btn_b1);
        Button btnb2 = (Button) dialog.findViewById(R.id.btn_b2);
        Button btnb3 = (Button) dialog.findViewById(R.id.btn_b3);
        Button btnb4 = (Button) dialog.findViewById(R.id.btn_b4);

        final int[] cob1 = {0};
        final int[] cob2 = {0};
        final int[] cob3 = {0};
        final int[] cob4 = {0};

        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        int t = hour * 3600 + minute * 60;
        //trua 11h-5h, toi 5h -9h
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (co1[0] == 0) {
                    btnb1.setBackgroundResource(R.drawable.bg_button_coner10);
                    btnb1.setTextColor(getResources().getColor(R.color.black));
                    btnb2.setBackgroundResource(R.drawable.bg_button_coner10);
                    btnb2.setTextColor(getResources().getColor(R.color.black));
                    btnb3.setBackgroundResource(R.drawable.bg_button_coner10);
                    btnb3.setTextColor(getResources().getColor(R.color.black));
                    btnb4.setBackgroundResource(R.drawable.bg_button_coner10);
                    btnb4.setTextColor(getResources().getColor(R.color.black));
                    if (t >= 39600 && t < 61200) {
                        btnb1.setEnabled(false);
                    } else if (t >= 61200 && t < 7560) {
                        btnb1.setEnabled(false);
                        btnb2.setEnabled(false);
                    } else if (t >= 75600) {
                        btnb1.setEnabled(false);
                        btnb2.setEnabled(false);
                        btnb3.setEnabled(false);
                    }
                    btn1.setBackgroundResource(R.drawable.bg_button_doimau);
                    btn1.setTextColor(getResources().getColor(R.color.white));
                    co1[0] = 1;
                    btn2.setBackgroundResource(R.drawable.bg_button_coner10);
                    btn2.setTextColor(getResources().getColor(R.color.black));
                    co2[0] = 0;
                    btn3.setBackgroundResource(R.drawable.bg_button_coner10);
                    btn3.setTextColor(getResources().getColor(R.color.black));
                    co3[0] = 0;
                    btn4.setBackgroundResource(R.drawable.bg_button_coner10);
                    btn4.setTextColor(getResources().getColor(R.color.black));
                    co4[0] = 0;
                    btn5.setBackgroundResource(R.drawable.bg_button_coner10);
                    btn5.setTextColor(getResources().getColor(R.color.black));
                    co5[0] = 0;
                    btn6.setBackgroundResource(R.drawable.bg_button_coner10);
                    btn6.setTextColor(getResources().getColor(R.color.black));
                    co6[0] = 0;
                    btn7.setBackgroundResource(R.drawable.bg_button_coner10);
                    btn7.setTextColor(getResources().getColor(R.color.black));
                    co7[0] = 0;
                    cob1[0] = 0;
                    cob2[0] = 0;
                    cob3[0] = 0;
                    cob4[0] = 0;
                } else {
                    btn1.setBackgroundResource(R.drawable.bg_button_coner10);
                    btn1.setTextColor(getResources().getColor(R.color.black));
                    btnb1.setEnabled(true);
                    btnb2.setEnabled(true);
                    btnb3.setEnabled(true);
                    co1[0] = 0;
                }
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (co2[0] == 0) {
                    btn2.setBackgroundResource(R.drawable.bg_button_doimau);
                    btn2.setTextColor(getResources().getColor(R.color.white));
                    co2[0] = 1;
                    btn1.setBackgroundResource(R.drawable.bg_button_coner10);
                    btn1.setTextColor(getResources().getColor(R.color.black));
                    co1[0] = 0;
                    btn3.setBackgroundResource(R.drawable.bg_button_coner10);
                    btn3.setTextColor(getResources().getColor(R.color.black));
                    co3[0] = 0;
                    btn4.setBackgroundResource(R.drawable.bg_button_coner10);
                    btn4.setTextColor(getResources().getColor(R.color.black));
                    co4[0] = 0;
                    btn5.setBackgroundResource(R.drawable.bg_button_coner10);
                    btn5.setTextColor(getResources().getColor(R.color.black));
                    co5[0] = 0;
                    btn6.setBackgroundResource(R.drawable.bg_button_coner10);
                    btn6.setTextColor(getResources().getColor(R.color.black));
                    co6[0] = 0;
                    btn7.setBackgroundResource(R.drawable.bg_button_coner10);
                    btn7.setTextColor(getResources().getColor(R.color.black));
                    co7[0] = 0;
                    btnb1.setEnabled(true);
                    btnb2.setEnabled(true);
                    btnb3.setEnabled(true);
                    cob1[0] = 0;
                    cob2[0] = 0;
                    cob3[0] = 0;
                    cob4[0] = 0;
                    btnb1.setBackgroundResource(R.drawable.bg_button_coner10);
                    btnb1.setTextColor(getResources().getColor(R.color.black));
                    btnb2.setBackgroundResource(R.drawable.bg_button_coner10);
                    btnb2.setTextColor(getResources().getColor(R.color.black));
                    btnb3.setBackgroundResource(R.drawable.bg_button_coner10);
                    btnb3.setTextColor(getResources().getColor(R.color.black));
                    btnb4.setBackgroundResource(R.drawable.bg_button_coner10);
                    btnb4.setTextColor(getResources().getColor(R.color.black));
                } else {
                    btn2.setBackgroundResource(R.drawable.bg_button_coner10);
                    btn2.setTextColor(getResources().getColor(R.color.black));
                    co2[0] = 0;
                }
            }
        });

        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (co3[0] == 0) {
                    btn3.setBackgroundResource(R.drawable.bg_button_doimau);
                    btn3.setTextColor(getResources().getColor(R.color.white));
                    co3[0] = 1;
                    btn1.setBackgroundResource(R.drawable.bg_button_coner10);
                    btn1.setTextColor(getResources().getColor(R.color.black));
                    co1[0] = 0;
                    btn2.setBackgroundResource(R.drawable.bg_button_coner10);
                    btn2.setTextColor(getResources().getColor(R.color.black));
                    co2[0] = 0;
                    btn4.setBackgroundResource(R.drawable.bg_button_coner10);
                    btn4.setTextColor(getResources().getColor(R.color.black));
                    co4[0] = 0;
                    btn5.setBackgroundResource(R.drawable.bg_button_coner10);
                    btn5.setTextColor(getResources().getColor(R.color.black));
                    co5[0] = 0;
                    btn6.setBackgroundResource(R.drawable.bg_button_coner10);
                    btn6.setTextColor(getResources().getColor(R.color.black));
                    co6[0] = 0;
                    btn7.setBackgroundResource(R.drawable.bg_button_coner10);
                    btn7.setTextColor(getResources().getColor(R.color.black));
                    co7[0] = 0;
                    btnb1.setEnabled(true);
                    btnb2.setEnabled(true);
                    btnb3.setEnabled(true);
                    cob1[0] = 0;
                    cob2[0] = 0;
                    cob3[0] = 0;
                    cob4[0] = 0;
                    btnb1.setBackgroundResource(R.drawable.bg_button_coner10);
                    btnb1.setTextColor(getResources().getColor(R.color.black));
                    btnb2.setBackgroundResource(R.drawable.bg_button_coner10);
                    btnb2.setTextColor(getResources().getColor(R.color.black));
                    btnb3.setBackgroundResource(R.drawable.bg_button_coner10);
                    btnb3.setTextColor(getResources().getColor(R.color.black));
                    btnb4.setBackgroundResource(R.drawable.bg_button_coner10);
                    btnb4.setTextColor(getResources().getColor(R.color.black));
                } else {
                    btn3.setBackgroundResource(R.drawable.bg_button_coner10);
                    btn3.setTextColor(getResources().getColor(R.color.black));
                    co3[0] = 0;
                }
            }
        });

        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (co4[0] == 0) {
                    btn4.setBackgroundResource(R.drawable.bg_button_doimau);
                    btn4.setTextColor(getResources().getColor(R.color.white));
                    co4[0] = 1;
                    btn1.setBackgroundResource(R.drawable.bg_button_coner10);
                    btn1.setTextColor(getResources().getColor(R.color.black));
                    co1[0] = 0;
                    btn2.setBackgroundResource(R.drawable.bg_button_coner10);
                    btn2.setTextColor(getResources().getColor(R.color.black));
                    co2[0] = 0;
                    btn3.setBackgroundResource(R.drawable.bg_button_coner10);
                    btn3.setTextColor(getResources().getColor(R.color.black));
                    co3[0] = 0;
                    btn5.setBackgroundResource(R.drawable.bg_button_coner10);
                    btn5.setTextColor(getResources().getColor(R.color.black));
                    co5[0] = 0;
                    btn6.setBackgroundResource(R.drawable.bg_button_coner10);
                    btn6.setTextColor(getResources().getColor(R.color.black));
                    co6[0] = 0;
                    btn7.setBackgroundResource(R.drawable.bg_button_coner10);
                    btn7.setTextColor(getResources().getColor(R.color.black));
                    co7[0] = 0;
                    btnb1.setEnabled(true);
                    btnb2.setEnabled(true);
                    btnb3.setEnabled(true);
                    cob1[0] = 0;
                    cob2[0] = 0;
                    cob3[0] = 0;
                    cob4[0] = 0;
                    btnb1.setBackgroundResource(R.drawable.bg_button_coner10);
                    btnb1.setTextColor(getResources().getColor(R.color.black));
                    btnb2.setBackgroundResource(R.drawable.bg_button_coner10);
                    btnb2.setTextColor(getResources().getColor(R.color.black));
                    btnb3.setBackgroundResource(R.drawable.bg_button_coner10);
                    btnb3.setTextColor(getResources().getColor(R.color.black));
                    btnb4.setBackgroundResource(R.drawable.bg_button_coner10);
                    btnb4.setTextColor(getResources().getColor(R.color.black));
                } else {
                    btn4.setBackgroundResource(R.drawable.bg_button_coner10);
                    btn4.setTextColor(getResources().getColor(R.color.black));
                    co4[0] = 0;
                }
            }
        });

        btn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (co5[0] == 0) {
                    btn5.setBackgroundResource(R.drawable.bg_button_doimau);
                    btn5.setTextColor(getResources().getColor(R.color.white));
                    co5[0] = 1;
                    btn1.setBackgroundResource(R.drawable.bg_button_coner10);
                    btn1.setTextColor(getResources().getColor(R.color.black));
                    co1[0] = 0;
                    btn2.setBackgroundResource(R.drawable.bg_button_coner10);
                    btn2.setTextColor(getResources().getColor(R.color.black));
                    co2[0] = 0;
                    btn3.setBackgroundResource(R.drawable.bg_button_coner10);
                    btn3.setTextColor(getResources().getColor(R.color.black));
                    co3[0] = 0;
                    btn4.setBackgroundResource(R.drawable.bg_button_coner10);
                    btn4.setTextColor(getResources().getColor(R.color.black));
                    co4[0] = 0;
                    btn6.setBackgroundResource(R.drawable.bg_button_coner10);
                    btn6.setTextColor(getResources().getColor(R.color.black));
                    co6[0] = 0;
                    btn7.setBackgroundResource(R.drawable.bg_button_coner10);
                    btn7.setTextColor(getResources().getColor(R.color.black));
                    co7[0] = 0;
                    btnb1.setEnabled(true);
                    btnb2.setEnabled(true);
                    btnb3.setEnabled(true);
                    cob1[0] = 0;
                    cob2[0] = 0;
                    cob3[0] = 0;
                    cob4[0] = 0;
                    btnb1.setBackgroundResource(R.drawable.bg_button_coner10);
                    btnb1.setTextColor(getResources().getColor(R.color.black));
                    btnb2.setBackgroundResource(R.drawable.bg_button_coner10);
                    btnb2.setTextColor(getResources().getColor(R.color.black));
                    btnb3.setBackgroundResource(R.drawable.bg_button_coner10);
                    btnb3.setTextColor(getResources().getColor(R.color.black));
                    btnb4.setBackgroundResource(R.drawable.bg_button_coner10);
                    btnb4.setTextColor(getResources().getColor(R.color.black));
                } else {
                    btn5.setBackgroundResource(R.drawable.bg_button_coner10);
                    btn5.setTextColor(getResources().getColor(R.color.black));
                    co5[0] = 0;
                }
            }
        });

        btn6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (co6[0] == 0) {
                    btn6.setBackgroundResource(R.drawable.bg_button_doimau);
                    btn6.setTextColor(getResources().getColor(R.color.white));
                    co6[0] = 1;
                    btn1.setBackgroundResource(R.drawable.bg_button_coner10);
                    btn1.setTextColor(getResources().getColor(R.color.black));
                    co1[0] = 0;
                    btn2.setBackgroundResource(R.drawable.bg_button_coner10);
                    btn2.setTextColor(getResources().getColor(R.color.black));
                    co2[0] = 0;
                    btn3.setBackgroundResource(R.drawable.bg_button_coner10);
                    btn3.setTextColor(getResources().getColor(R.color.black));
                    co3[0] = 0;
                    btn4.setBackgroundResource(R.drawable.bg_button_coner10);
                    btn4.setTextColor(getResources().getColor(R.color.black));
                    co4[0] = 0;
                    btn5.setBackgroundResource(R.drawable.bg_button_coner10);
                    btn5.setTextColor(getResources().getColor(R.color.black));
                    co5[0] = 0;
                    btn7.setBackgroundResource(R.drawable.bg_button_coner10);
                    btn7.setTextColor(getResources().getColor(R.color.black));
                    co7[0] = 0;
                    btnb1.setEnabled(true);
                    btnb2.setEnabled(true);
                    btnb3.setEnabled(true);
                    cob1[0] = 0;
                    cob2[0] = 0;
                    cob3[0] = 0;
                    cob4[0] = 0;
                    btnb1.setBackgroundResource(R.drawable.bg_button_coner10);
                    btnb1.setTextColor(getResources().getColor(R.color.black));
                    btnb2.setBackgroundResource(R.drawable.bg_button_coner10);
                    btnb2.setTextColor(getResources().getColor(R.color.black));
                    btnb3.setBackgroundResource(R.drawable.bg_button_coner10);
                    btnb3.setTextColor(getResources().getColor(R.color.black));
                    btnb4.setBackgroundResource(R.drawable.bg_button_coner10);
                    btnb4.setTextColor(getResources().getColor(R.color.black));
                } else {
                    btn6.setBackgroundResource(R.drawable.bg_button_coner10);
                    btn6.setTextColor(getResources().getColor(R.color.black));
                    co6[0] = 0;
                }
            }
        });

        btn7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (co7[0] == 0) {
                    btn7.setBackgroundResource(R.drawable.bg_button_doimau);
                    btn7.setTextColor(getResources().getColor(R.color.white));
                    co7[0] = 1;
                    btn1.setBackgroundResource(R.drawable.bg_button_coner10);
                    btn1.setTextColor(getResources().getColor(R.color.black));
                    co1[0] = 0;
                    btn2.setBackgroundResource(R.drawable.bg_button_coner10);
                    btn2.setTextColor(getResources().getColor(R.color.black));
                    co2[0] = 0;
                    btn3.setBackgroundResource(R.drawable.bg_button_coner10);
                    btn3.setTextColor(getResources().getColor(R.color.black));
                    co3[0] = 0;
                    btn4.setBackgroundResource(R.drawable.bg_button_coner10);
                    btn4.setTextColor(getResources().getColor(R.color.black));
                    co4[0] = 0;
                    btn5.setBackgroundResource(R.drawable.bg_button_coner10);
                    btn5.setTextColor(getResources().getColor(R.color.black));
                    co5[0] = 0;
                    btn6.setBackgroundResource(R.drawable.bg_button_coner10);
                    btn6.setTextColor(getResources().getColor(R.color.black));
                    co6[0] = 0;
                    btnb1.setEnabled(true);
                    btnb2.setEnabled(true);
                    btnb3.setEnabled(true);
                    cob1[0] = 0;
                    cob2[0] = 0;
                    cob3[0] = 0;
                    cob4[0] = 0;
                    btnb1.setBackgroundResource(R.drawable.bg_button_coner10);
                    btnb1.setTextColor(getResources().getColor(R.color.black));
                    btnb2.setBackgroundResource(R.drawable.bg_button_coner10);
                    btnb2.setTextColor(getResources().getColor(R.color.black));
                    btnb3.setBackgroundResource(R.drawable.bg_button_coner10);
                    btnb3.setTextColor(getResources().getColor(R.color.black));
                    btnb4.setBackgroundResource(R.drawable.bg_button_coner10);
                    btnb4.setTextColor(getResources().getColor(R.color.black));
                } else {
                    btn7.setBackgroundResource(R.drawable.bg_button_coner10);
                    btn7.setTextColor(getResources().getColor(R.color.black));
                    co7[0] = 0;
                }
            }
        });


        btnb1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cob1[0] == 0) {
                    btnb1.setBackgroundResource(R.drawable.bg_button_doimau);
                    btnb1.setTextColor(getResources().getColor(R.color.white));
                    cob1[0] = 1;
                    btnb2.setBackgroundResource(R.drawable.bg_button_coner10);
                    btnb2.setTextColor(getResources().getColor(R.color.black));
                    cob2[0] = 0;
                    btnb3.setBackgroundResource(R.drawable.bg_button_coner10);
                    btnb3.setTextColor(getResources().getColor(R.color.black));
                    cob3[0] = 0;
                    btnb4.setBackgroundResource(R.drawable.bg_button_coner10);
                    btnb4.setTextColor(getResources().getColor(R.color.black));
                    cob4[0] = 0;
                } else {
                    btnb1.setBackgroundResource(R.drawable.bg_button_coner10);
                    btnb1.setTextColor(getResources().getColor(R.color.black));
                    cob1[0] = 0;
                }
            }
        });

        btnb2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cob2[0] == 0) {
                    btnb2.setBackgroundResource(R.drawable.bg_button_doimau);
                    btnb2.setTextColor(getResources().getColor(R.color.white));
                    cob2[0] = 1;
                    btnb1.setBackgroundResource(R.drawable.bg_button_coner10);
                    btnb1.setTextColor(getResources().getColor(R.color.black));
                    cob1[0] = 0;
                    btnb3.setBackgroundResource(R.drawable.bg_button_coner10);
                    btnb3.setTextColor(getResources().getColor(R.color.black));
                    cob3[0] = 0;
                    btnb4.setBackgroundResource(R.drawable.bg_button_coner10);
                    btnb4.setTextColor(getResources().getColor(R.color.black));
                    cob4[0] = 0;
                } else {
                    btnb2.setBackgroundResource(R.drawable.bg_button_coner10);
                    btnb2.setTextColor(getResources().getColor(R.color.black));
                    cob2[0] = 0;
                }
            }
        });

        btnb3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cob3[0] == 0) {
                    btnb3.setBackgroundResource(R.drawable.bg_button_doimau);
                    btnb3.setTextColor(getResources().getColor(R.color.white));
                    cob3[0] = 1;
                    btnb1.setBackgroundResource(R.drawable.bg_button_coner10);
                    btnb1.setTextColor(getResources().getColor(R.color.black));
                    cob1[0] = 0;
                    btnb2.setBackgroundResource(R.drawable.bg_button_coner10);
                    btnb2.setTextColor(getResources().getColor(R.color.black));
                    cob2[0] = 0;
                    btnb4.setBackgroundResource(R.drawable.bg_button_coner10);
                    btnb4.setTextColor(getResources().getColor(R.color.black));
                    cob4[0] = 0;

                } else {
                    btnb3.setBackgroundResource(R.drawable.bg_button_coner10);
                    btnb3.setTextColor(getResources().getColor(R.color.black));
                    cob3[0] = 0;
                }
            }
        });

        btnb4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cob4[0] == 0) {
                    btnb4.setBackgroundResource(R.drawable.bg_button_doimau);
                    btnb4.setTextColor(getResources().getColor(R.color.white));
                    cob4[0] = 1;
                    btnb1.setBackgroundResource(R.drawable.bg_button_coner10);
                    btnb1.setTextColor(getResources().getColor(R.color.black));
                    cob1[0] = 0;
                    btnb3.setBackgroundResource(R.drawable.bg_button_coner10);
                    btnb3.setTextColor(getResources().getColor(R.color.black));
                    cob3[0] = 0;
                    btnb2.setBackgroundResource(R.drawable.bg_button_coner10);
                    btnb2.setTextColor(getResources().getColor(R.color.black));
                    cob2[0] = 0;
                } else {
                    btnb4.setBackgroundResource(R.drawable.bg_button_coner10);
                    btnb4.setTextColor(getResources().getColor(R.color.black));
                    cob4[0] = 0;
                }
            }
        });

        Button btnThem = (Button) dialog.findViewById(R.id.btn_them_dialog);
        int Year = year;
        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String n = null, b = null;
                int[] mang = new int[]{co1[0], co2[0], co3[0], co4[0], co5[0], co6[0], co7[0]};
                if (co1[0] == 0 && co2[0] == 0 && co3[0] == 0 && co4[0] == 0 && co5[0] == 0 && co6[0] == 0 && co7[0] == 0) {
                    if (cob1[0] == 0 && cob2[0] == 0 && cob3[0] == 0 && cob4[0] == 0) {
                        Toast.makeText(MonAnChiTietActivity.this, "Vui lòng chọn ngày và bữa", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MonAnChiTietActivity.this, "Vui lòng chọn ngày", Toast.LENGTH_SHORT).show();
                    }
                } else if (cob1[0] == 0 && cob2[0] == 0 && cob3[0] == 0 && cob4[0] == 0) {
                    Toast.makeText(MonAnChiTietActivity.this, "Vui lòng chọn bữa", Toast.LENGTH_SHORT).show();
                } else {
                    //ngay
                    if (co1[0] == 1) {
                        n = btn1.getText() + "";
                    } else if (co2[0] == 1) {
                        n = btn2.getText() + "";
                    } else if (co3[0] == 1) {
                        n = btn3.getText() + "";
                    } else if (co4[0] == 1) {
                        n = btn4.getText() + "";
                    } else if (co5[0] == 1) {
                        n = btn5.getText() + "";
                    } else if (co6[0] == 1) {
                        n = btn6.getText() + "";
                    } else if (co7[0] == 1) {
                        n = btn7.getText() + "";
                    }
                    //buoi
                    if (cob1[0] == 1) {
                        b = btnb1.getText() + "";
                    } else if (cob2[0] == 1) {
                        b = btnb2.getText() + "";
                    } else if (cob3[0] == 1) {
                        b = btnb3.getText() + "";
                    } else if (cob4[0] == 1) {
                        b = btnb4.getText() + "";
                    }
                    String y = Year + "/" + n;
                    postData(userid, idmonan, y + "", b + "", v.getContext(),dialog);
                }
            }
        });

        Button btn_huy = (Button) dialog.findViewById(R.id.btn_cancel);
        btn_huy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void postData(String userID, String dsmonan, String ngay, String buoi, Context context,Dialog dialog) {
        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("IDUser", userID);
            jsonParams.put("DSMonAn", dsmonan);
            jsonParams.put("Ngay", ngay);
            jsonParams.put("Buoi", buoi);
            //dich ra chuoi
            StringEntity entity = new StringEntity(jsonParams.toString(), HTTP.UTF_8);
            HttpUtils.post(context, "thucdon/", entity, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    Toast.makeText(context, "Thêm thành công!", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    Toast.makeText(context,"Món ăn đã có trong thực đơn rồi",Toast.LENGTH_SHORT).show();
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
