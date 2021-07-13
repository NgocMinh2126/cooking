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
import com.example.trangchu.adapters.MonAn_CachLamAdapter;
import com.example.trangchu.models.NguyenLieu;
import com.example.trangchu.service.HttpUtils;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class MonAnChiTiet_CachLamFragment extends Fragment {
    MonAnChiTietActivity monAnChiTietActivity;
    String idmonan;
    MonAn_CachLamAdapter adapter;
    ListView cachlamlv;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.monan_chitiet_fragment_cachlam,container,false);
        monAnChiTietActivity=(MonAnChiTietActivity)getActivity();
        idmonan=monAnChiTietActivity.getIntent().getStringExtra("IDMonAn");
        cachlamlv=view.findViewById(R.id.cachlam_lv);
        setData(view,view.getContext());
        return view;
    }
    public void setData(View view, Context context){
        ArrayList<String> list = new ArrayList<String>();
        RequestParams rp = new RequestParams();
        String duongdan="monan/"+idmonan;
        HttpUtils.get(duongdan, rp, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    JSONObject mcl = response.getJSONObject("m");
                    if (mcl != null) {
                        JSONArray listCL = mcl.getJSONArray("CachLam");
                        for(int i=0;i<listCL.length();i++){
                            String clamserver = listCL.getString(i);
                            list.add(clamserver);
                        }
                        Log.i("chkchitietmonan","so buoc thuc hien: "+list.size()+"");
                        adapter=new MonAn_CachLamAdapter(context,list);
                        cachlamlv.setAdapter(adapter);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        return;
    }

}
