package com.example.trangchu.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trangchu.R;
import com.example.trangchu.activity.DangNhapActivity;
import com.example.trangchu.activity.MainActivity;
import com.example.trangchu.adapters.MarketAdapter;
import com.example.trangchu.models.NguyenLieu;
import com.example.trangchu.service.HttpUtils;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class MarketFragment extends Fragment {
    MainActivity mainActivity;
    LinearLayout nointernet;
    String userid;
    RecyclerView rv_market;
    List<NguyenLieu> listNgLieu;
    MarketAdapter marketAdapter;
    RelativeLayout ln_baoloi;
    TableLayout tb_nglieu;
    RelativeLayout maket_onlyforuser;
    Button btn_dnhap;
    RelativeLayout market_guest;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_market,container,false);

        mainActivity=(MainActivity) getActivity();
        Intent intent=mainActivity.getIntent();
        userid=intent.getStringExtra("UserID");

        nointernet=view.findViewById(R.id.market_nointernet);
        rv_market=view.findViewById(R.id.rv_market);
        ln_baoloi=view.findViewById(R.id.ln_baoloi);
        tb_nglieu=view.findViewById(R.id.tb_nglieu);
        market_guest=view.findViewById(R.id.fragment_market_guest);
        btn_dnhap=view.findViewById(R.id.fragment_market_dnhap);
        maket_onlyforuser=view.findViewById(R.id.fragment_market_foruser);
        checkInternetConnection();
        if(userid!=null){
            maket_onlyforuser.setVisibility(View.VISIBLE);
            setNgLieu();
            LinearLayoutManager layoutManager=new LinearLayoutManager(view.getContext(),LinearLayoutManager.VERTICAL,false);
            rv_market.setLayoutManager(layoutManager);
            market_guest.setVisibility(View.GONE);
        }else{
            btn_dnhap.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(getActivity(), DangNhapActivity.class);
                    startActivity(intent);
                }
            });
        }

        return view;
    }
    public void setNgLieu(){
        Log.i("chkMarket","helo");
        List<NguyenLieu>list=new ArrayList<NguyenLieu>();
        RequestParams rp = new RequestParams();
        String duongdan="thucdon/nglieu/"+userid;
        HttpUtils.get(duongdan, rp, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    JSONArray lstNguyenLieu = response.getJSONArray("nglieu");
                    if (lstNguyenLieu != null) {
                        for (int i=0;i<lstNguyenLieu.length();i++){
                            JSONObject obj = lstNguyenLieu.optJSONObject(i);
                            JSONObject nglieuobj=obj.getJSONObject("NguyenLieu");
                            try{
                                if(obj.getInt("SoLuong")>0){
                                    list.add(new NguyenLieu(nglieuobj.getString("Ten"),
                                            obj.getInt("SoLuong"),obj.getString("DonVi")));
                                }
                            }catch(JSONException e){
                                list.add(new NguyenLieu(nglieuobj.getString("Ten"),
                                        nglieuobj.getString("_id")));
                            }
                        }
                    }
                    Log.i("chkMarket",list.size()+"");
                    marketAdapter=new MarketAdapter();
                    marketAdapter.setData(list);
                    rv_market.setAdapter(marketAdapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                tb_nglieu.setVisibility(View.GONE);
                ln_baoloi.setVisibility(View.VISIBLE);
            }
        });
        return;
    }
    private boolean checkInternetConnection() {
        ConnectivityManager connManager =
                (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connManager.getActiveNetworkInfo();
        if (networkInfo == null) {
            market_guest.setVisibility(View.GONE);
            nointernet.setVisibility(View.VISIBLE);
            return false;
        }
        return true;
    }
}
