package com.example.trangchu.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trangchu.IRecycleViewClickListerner;
import com.example.trangchu.R;
import com.example.trangchu.adapters.HomNayAnGiAdapter;
import com.example.trangchu.adapters.LoaiMon_TCAdapter;
import com.example.trangchu.adapters.TC_MonAnAdapter;
import com.example.trangchu.models.LoaiMon;
import com.example.trangchu.models.MonAn;
import com.example.trangchu.service.HttpUtils;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class TrangChufragment extends Fragment {
    ShimmerFrameLayout homnayangi_ani;
    ShimmerFrameLayout noibat_ani;
    ShimmerFrameLayout loaimon_ani;

    static ArrayList<MonAn>listMonAnNoiBat = new ArrayList<MonAn>();
    static TC_MonAnAdapter tc_monAnAdapter;
    RecyclerView rv_noibat;

    static ArrayList<LoaiMon>listLoaiMon=new ArrayList<LoaiMon>();
    static LoaiMon_TCAdapter trangchu_loaimon;
    RecyclerView rv_trangchu_loaimon;

    RecyclerView rv_trangchu_homnayangi;
    static ArrayList<MonAn> listMonAnforHomnayangi = new ArrayList<MonAn>();
    static HomNayAnGiAdapter homayangiAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_trangchu,container,false);
        //Trang chu noi bat
        noibat_ani=view.findViewById(R.id.noibat_ani);
        rv_noibat=view.findViewById(R.id.rv_noibat);
        LinearLayoutManager noibat_layoutmanager
                = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
        rv_noibat.setLayoutManager(noibat_layoutmanager);
        setDataforlistMonAnNoiBat(view);

        //Trang chu Hom nay an gi
        homnayangi_ani=view.findViewById(R.id.homnayangi_ani);
        rv_trangchu_homnayangi=view.findViewById(R.id.rv_trangchu_homnayangi);
        LinearLayoutManager Homnayangi_layoutmanager
                = new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false);
        rv_trangchu_homnayangi.setLayoutManager(Homnayangi_layoutmanager);
            setDataForlistHomnayangi(view);
        //Trang chu_ Loai mon
        loaimon_ani=view.findViewById(R.id.loaimon_ani);
        rv_trangchu_loaimon=view.findViewById(R.id.rv_trangchu_loaimon);
        LinearLayoutManager layoutforloaimon
                = new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false);
        rv_trangchu_loaimon.setLayoutManager(layoutforloaimon);
        setDataforlistLoaiMon(view);
        return view;
    }

    public void setDataforlistMonAnNoiBat(View view){
        if(listMonAnNoiBat.size() == 0){
            RequestParams rp = new RequestParams();
            HttpUtils.get("monan/", rp, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    try {
                        JSONArray listMonAn = response.getJSONArray("dsmonan");
                        if (listMonAn != null) {
                            for (int i=0;i<listMonAn.length();i++){
                                JSONObject obj = listMonAn.optJSONObject(i);
                                if(obj.getInt("LuotThich")>0){
                                    MonAn m=new MonAn(obj.getString("_id"),obj.getString("Ten"),obj.getString("Anh"));
                                    listMonAnNoiBat.add(m);
                                }
                            }
                            tc_monAnAdapter = new TC_MonAnAdapter();
                            tc_monAnAdapter.setData(listMonAnNoiBat, new IRecycleViewClickListerner() {
                                @Override
                                public void onItemClick(MonAn monan) {
                                    Toast.makeText(view.getContext(),monan.getTenMonAn(),Toast.LENGTH_SHORT).show();
                                }
                            });
                            rv_noibat.setAdapter(tc_monAnAdapter);
                            noibat_ani.startShimmer();
                            noibat_ani.setVisibility(View.GONE);
                            Log.i("chkTrangChuFragment","Số lượng món ăn lấy được noi bat"+listMonAnNoiBat.size());
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }else{
            noibat_ani.setVisibility(View.GONE);
            rv_noibat.setAdapter(tc_monAnAdapter);
        }
        return;
    }

    public void setDataForlistHomnayangi(View view){
        Calendar c = Calendar.getInstance();
        Date yourDate=Calendar.getInstance().getTime();
        c.setTime(yourDate);
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
        if(listMonAnforHomnayangi.size()==0){
            Log.i("chkTrangChuFragment","ngay trong tuan:"+dayOfWeek);
            RequestParams rp = new RequestParams();
            HttpUtils.get("monan/", rp, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    try {
                        JSONArray lstMonAnServer = response.getJSONArray("dsmonan");
                        if (lstMonAnServer != null) {
                            for (int i=0;i<lstMonAnServer.length();i++){
                                JSONObject obj = lstMonAnServer.optJSONObject(i);
                                listMonAnforHomnayangi.add(new MonAn(obj.getString("_id"),obj.getString("Ten"),obj.getString("Anh")));
                            }
                            Log.i("chkTrangChuFragment","sl mon an lay dc"+listMonAnforHomnayangi.size());
                            ArrayList<MonAn>lstForAdapter=new ArrayList<MonAn>();
                            int slmonan=listMonAnforHomnayangi.size();
                            int start,end,n;
                            switch (dayOfWeek){
                                case 1:
                                    n=0;
                                    start=slmonan-(5*n+1);
                                    end=slmonan-(5*n+5);
                                    for(int i=start;i>=end;i--){
                                        lstForAdapter.add(listMonAnforHomnayangi.get(i));
                                    }
                                    break;
                                case 2:
                                    n=1;
                                    start=slmonan-(5*n+1);
                                    end=slmonan-(5*n+5);
                                    for(int i=start;i>=end;i--){
                                        lstForAdapter.add(listMonAnforHomnayangi.get(i));
                                    }
                                    break;
                                case 3:
                                    n=2;
                                    start=slmonan-(5*n+1);
                                    end=slmonan-(5*n+5);
                                    for(int i=start;i>=end;i--){
                                        lstForAdapter.add(listMonAnforHomnayangi.get(i));
                                    }
                                    break;
                                case 4:
                                    n=3;
                                    start=slmonan-(5*n+1);
                                    end=slmonan-(5*n+5);
                                    for(int i=start;i>=end;i--){
                                        lstForAdapter.add(listMonAnforHomnayangi.get(i));
                                    }
                                    break;
                                case 5:
                                    n=4;
                                    start=slmonan-(5*n+1);
                                    end=slmonan-(5*n+5);
                                    for(int i=start;i>=end;i--){
                                        lstForAdapter.add(listMonAnforHomnayangi.get(i));
                                    }
                                    break;
                                case 6:
                                    n=5;
                                    start=slmonan-(5*n+1);
                                    end=slmonan-(5*n+5);
                                    for(int i=start;i>=end;i--){
                                        lstForAdapter.add(listMonAnforHomnayangi.get(i));
                                    }
                                    break;
                                case 7:
                                    n=6;
                                    start=slmonan-(5*n+1);
                                    end=slmonan-(5*n+5);
                                    for(int i=start;i>=end;i--){
                                        lstForAdapter.add(listMonAnforHomnayangi.get(i));
                                    }
                                    break;
                            }
                            homayangiAdapter=new HomNayAnGiAdapter();
                            homayangiAdapter.setData(lstForAdapter, new IRecycleViewClickListerner() {
                                @Override
                                public void onItemClick(MonAn monan) {
                                    Toast.makeText(view.getContext(),monan.getTenMonAn(),Toast.LENGTH_SHORT).show();
                                }
                            });
                            rv_trangchu_homnayangi.setAdapter(homayangiAdapter);
                            homnayangi_ani.startShimmer();
                            homnayangi_ani.setVisibility(View.GONE);
                            rv_trangchu_homnayangi.setVisibility(View.VISIBLE);
                        }
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }});

        }else{
            rv_trangchu_homnayangi.setAdapter(homayangiAdapter);
            homnayangi_ani.startShimmer();
            homnayangi_ani.setVisibility(View.GONE);
            rv_trangchu_homnayangi.setVisibility(View.VISIBLE);
        }

    }


    public void setDataforlistLoaiMon(View view){
        if(listLoaiMon.size() == 0){
            Log.i("chkTrangChu","tao list loai mon"+ listLoaiMon.size());
            RequestParams rp = new RequestParams();
            HttpUtils.get("loaimon/monansort", rp, new JsonHttpResponseHandler() {
                @Override
                //statusCode: ma loi tra tu service ve
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    // If the response is JSONObject instead of expected JSONArray
                    try {
                        JSONArray listLoaiMonserver = response.getJSONArray("dsMonAnTheoLoai");
                        if (listLoaiMonserver != null) {
                            for (int i=0;i<2;i++){
                                JSONObject obj = listLoaiMonserver.optJSONObject(i);
                                JSONArray arr=obj.getJSONArray("dsMonAn");
                                ArrayList <MonAn>dsmonan=new ArrayList<MonAn>();
                                for(int j=0;j<arr.length();j++){
                                    JSONObject monanobj=arr.optJSONObject(j);
                                    dsmonan.add(new MonAn(monanobj.getString("_id")
                                            ,monanobj.getString("Ten"),monanobj.getString("Anh")));
                                }
                                Log.i("chkTrangchuLoaiMon","ten loai mon: "+obj.getString("TenLoaiMon"));
                                Log.i("chkTrangchuLoaiMon","So luong monan tim duoc: "+dsmonan.size());
                                if(dsmonan.size()!=0){
                                    listLoaiMon.add(new LoaiMon(obj.getString("TenLoaiMon"),obj.getString("IDLoaiMon"),dsmonan));
                                }
                            }
                            Log.i("chkTrangchuLoaiMon","So luong loai mon tim duoc: "+listLoaiMon.size());
                            trangchu_loaimon=new LoaiMon_TCAdapter(view.getContext());
                            trangchu_loaimon.setData(listLoaiMon, new IRecycleViewClickListerner() {
                                @Override
                                public void onItemClick(MonAn monan) {
                                    Toast.makeText(view.getContext(),monan.getTenMonAn(),Toast.LENGTH_SHORT).show();
                                }
                            });
                            rv_trangchu_loaimon.setAdapter(trangchu_loaimon);
                            loaimon_ani.startShimmer();
                            loaimon_ani.setVisibility(View.GONE);
                            rv_trangchu_loaimon.setVisibility(View.VISIBLE);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }});
        }else{
            loaimon_ani.setVisibility(View.GONE);
            rv_trangchu_loaimon.setAdapter(trangchu_loaimon);
        }
    }
}
