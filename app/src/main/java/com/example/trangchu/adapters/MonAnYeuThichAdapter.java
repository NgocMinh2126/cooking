package com.example.trangchu.adapters;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trangchu.IRecycleViewClickListerner;
import com.example.trangchu.R;
import com.example.trangchu.activity.MonAnYeuThichActivity;
import com.example.trangchu.models.MonAn;
import com.example.trangchu.service.HttpUtils;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;
import de.hdodenhof.circleimageview.CircleImageView;

public class MonAnYeuThichAdapter extends RecyclerView.Adapter<MonAnYeuThichAdapter.MonAnYeuThichViewHolder> {
    private ArrayList<MonAn>listMonAn;
    private IRecycleViewClickListerner i;
    private String userid;
    private MonAnYeuThichAdapter adapter;
    public void setData(ArrayList<MonAn>list,String userid,IRecycleViewClickListerner i){
        this.listMonAn=list;
        this.i=i;
        this.userid=userid;
        this.adapter=this;
    }
    @NonNull
    @Override
    public MonAnYeuThichViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.dsmonan_item,parent,false);
        return new MonAnYeuThichViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MonAnYeuThichViewHolder holder, int position) {
        MonAn monan=listMonAn.get(position);
        if(monan==null){
            return;
        }
        holder.dsmonan_btn_like.setChecked(true);
        holder.dsmonan_btn_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenXoaMonAnDialog(Gravity.CENTER,userid,monan,v.getContext(),holder.dsmonan_btn_like);
            }
        });
        holder.dsmonan_tenmon_item.setText(monan.getTenMonAn());
        if(position%2==0){
            holder.dsmonan_item.setBackgroundResource(R.drawable.bg_dsmonan1);
            holder.dsmonan_img_item.setBackgroundResource(R.drawable.bg_imgfood);
        }else{
            holder.dsmonan_item.setBackgroundResource(R.drawable.bg_dsmonan2);
            holder.dsmonan_img_item.setBackgroundResource(R.drawable.bg_img02);
        }
        try {
            URL url=new URL(monan.getAnh());
            Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            holder.dsmonan_img_item.setImageBitmap(bmp);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        holder.dsmonan_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i.onItemClick(monan);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(listMonAn!=null){
            return listMonAn.size();
        }
        return 0;
    }

    public class MonAnYeuThichViewHolder extends RecyclerView.ViewHolder {
        private RelativeLayout dsmonan_item;
        private TextView dsmonan_tenmon_item;
        private ImageView dsmonan_img_item;
        private ToggleButton dsmonan_btn_like;
        public MonAnYeuThichViewHolder(@NonNull View itemView) {
            super(itemView);
            dsmonan_btn_like=itemView.findViewById(R.id.dsmonan_btn_like_item);
            dsmonan_img_item=itemView.findViewById(R.id.dsmonan_img_item);
            dsmonan_tenmon_item=itemView.findViewById(R.id.dsmonan_tenmon_item);
            dsmonan_item=itemView.findViewById(R.id.dsmonan_item);
        }
    }
    public void OpenXoaMonAnDialog(int gravity,String userid,MonAn m, Context context,ToggleButton btn) {
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
            xoadialog.setCancelable(true);
            btn.setChecked(true);
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
                XoaMonAnYeuThich(userid,m,v);
                xoadialog.dismiss();
                btn.setChecked(false);
            }
        });
        xoadialog.show();
    }
    public void XoaMonAnYeuThich(String userid,MonAn m,View v){
        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("IDUser", userid);
            jsonParams.put("IDMonAn",m.getId());
            StringEntity entity = new StringEntity(jsonParams.toString());
            HttpUtils.post(v.getContext(),"monanyeuthich/", entity, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    Log.i("chk","xoa ra khoi dsach yeu thich");
                    Toast.makeText(v.getContext(),"Đã xóa món ăn yêu thích",Toast.LENGTH_SHORT).show();
                    listMonAn.remove(m);
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    Log.i("chk","ko the xoa ra khoi dsach yeu thich");
                }
            });
        } catch (JSONException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
