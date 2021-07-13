package com.example.trangchu.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.trangchu.R;
import com.example.trangchu.activity.MonAnChiTietActivity;
import com.example.trangchu.adapters.MonAn_NgLieuAdapter;
import com.example.trangchu.models.NguyenLieu;
import com.example.trangchu.service.HttpUtils;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class MonAnChiTiet_NLieuFragment extends Fragment {
    MonAnChiTietActivity monAnChiTietActivity;
    String idmonan;
    MonAn_NgLieuAdapter adapter;
    ListView nglieulv;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.monan_chitiet_fragment_nglieu,container,false);
        monAnChiTietActivity=(MonAnChiTietActivity)getActivity();
        idmonan=monAnChiTietActivity.getIntent().getStringExtra("IDMonAn");
        nglieulv=view.findViewById(R.id.nguyenlieu_lv);
        setNguyenLieu(view,view.getContext());
        return view;
    }
    private void setNguyenLieu(View view, Context context) {
        ArrayList<NguyenLieu> list = new ArrayList<NguyenLieu>();
        RequestParams rp = new RequestParams();
        String duongdan="monan/"+idmonan;
        HttpUtils.get(duongdan, rp, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    JSONObject m = response.getJSONObject("m");
                    if (m != null) {
                        JSONArray listNL = m.getJSONArray("NguyenLieu");
                        for(int i=0;i<listNL.length();i++){
                            JSONObject nglieuserver = listNL.getJSONObject(i);
                            if(nglieuserver.isNull("DonVi")){
                                list.add(new NguyenLieu(nglieuserver.getString("TenNgL")));
                            }else{
                                list.add(new NguyenLieu(nglieuserver.getString("TenNgL"),nglieuserver.getInt("SoLuong"),nglieuserver.getString("DonVi")));
                            }
                        }
                        Log.i("chkchitietmonan","sl nglieu: "+list.size());
                        adapter=new MonAn_NgLieuAdapter(context,list);
                        nglieulv.setAdapter(adapter);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        return;
    }
}
