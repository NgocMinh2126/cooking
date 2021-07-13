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
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trangchu.IRecycleViewClickListerner;
import com.example.trangchu.R;
import com.example.trangchu.activity.MainActivity;
import com.example.trangchu.activity.MonAnChiTietActivity;
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

    MainActivity mainActivity;

    TC_MonAnAdapter tc_monAnAdapter;
    RecyclerView rv_noibat;


    LoaiMon_TCAdapter trangchu_loaimon;
    RecyclerView rv_trangchu_loaimon;

    RecyclerView rv_trangchu_homnayangi;
    HomNayAnGiAdapter homayangiAdapter;
    LinearLayout nointernet;
    LinearLayout tc_mainlinear;

    String userid;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trangchu, container, false);
        mainActivity=(MainActivity) getActivity();
        Intent intent=mainActivity.getIntent();
        userid=intent.getStringExtra("UserID");

        Log.i("chkmain",""+userid);
        nointernet=view.findViewById(R.id.nointernet);
        tc_mainlinear=view.findViewById(R.id.trangchu_mainlinear);
        checkInternetConnection();
        //Trang chu noi bat
        noibat_ani = view.findViewById(R.id.noibat_ani);
        rv_noibat = view.findViewById(R.id.rv_noibat);
        LinearLayoutManager noibat_layoutmanager
                = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
        rv_noibat.setLayoutManager(noibat_layoutmanager);


        //Trang chu Hom nay an gi
        homnayangi_ani = view.findViewById(R.id.homnayangi_ani);
        rv_trangchu_homnayangi = view.findViewById(R.id.rv_trangchu_homnayangi);
        LinearLayoutManager Homnayangi_layoutmanager
                = new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false);
        rv_trangchu_homnayangi.setLayoutManager(Homnayangi_layoutmanager);
        //Trang chu_ Loai mon
        loaimon_ani = view.findViewById(R.id.loaimon_ani);
        rv_trangchu_loaimon = view.findViewById(R.id.rv_trangchu_loaimon);
        LinearLayoutManager layoutforloaimon
                = new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false);
        rv_trangchu_loaimon.setLayoutManager(layoutforloaimon);
        setDataforlistMonAnNoiBat(view);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.i("chkTCF", "destroyview");
    }

    public void setDataforlistMonAnNoiBat(View view) {
        ArrayList<MonAn> listMonAnNoiBat = new ArrayList<MonAn>();
        RequestParams rp = new RequestParams();
        HttpUtils.get("home/", rp, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    {
                        JSONArray listMonAn = response.getJSONArray("dsmonan");
                        if (listMonAn != null) {
                            for (int i = 0; i < listMonAn.length(); i++) {
                                JSONObject obj = listMonAn.optJSONObject(i);
                                if (obj.getInt("LuotThich") > 0) {
                                    MonAn m = new MonAn(obj.getString("_id"), obj.getString("Ten"), obj.getString("Anh"));
                                    listMonAnNoiBat.add(m);
                                }
                            }
                            tc_monAnAdapter = new TC_MonAnAdapter();
                            tc_monAnAdapter.setData(listMonAnNoiBat, new IRecycleViewClickListerner() {
                                @Override
                                public void onItemClick(MonAn monan) {
                                    Intent intent=new Intent(getActivity(), MonAnChiTietActivity.class);
                                    intent.putExtra("UserID",userid+"");
                                    intent.putExtra("IDMonAn",monan.getId());
                                    startActivity(intent);
                                }
                            });
                            rv_noibat.setAdapter(tc_monAnAdapter);
                            noibat_ani.startShimmer();
                            noibat_ani.setVisibility(View.GONE);
                            Log.i("chktrangchu", "Số lượng món ăn lấy được noi bat" + listMonAnNoiBat.size());
                        }
                    }
                    {
                        ArrayList<MonAn> listMonAnforHomnayangi = new ArrayList<MonAn>();
                        Calendar c = Calendar.getInstance();
                        Date yourDate = Calendar.getInstance().getTime();
                        c.setTime(yourDate);
                        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
                        JSONArray lstMonAnServer = response.getJSONArray("dsmonan");
                        if (lstMonAnServer != null) {
                            for (int i = 0; i < lstMonAnServer.length(); i++) {
                                JSONObject obj = lstMonAnServer.optJSONObject(i);
                                listMonAnforHomnayangi.add(new MonAn(obj.getString("_id"), obj.getString("Ten"), obj.getString("Anh")));
                            }
                            Log.i("chktrangchu", "sl mon an lay dc" + listMonAnforHomnayangi.size());
                            ArrayList<MonAn> lstForAdapter = new ArrayList<MonAn>();
                            int slmonan = listMonAnforHomnayangi.size();
                            int start, end, n;
                            switch (dayOfWeek) {
                                case 1:
                                    n = 0;
                                    start = slmonan - (5 * n + 1);
                                    end = slmonan - (5 * n + 5);
                                    for (int i = start; i >= end; i--) {
                                        Log.i("chktrangchu",i+"");
                                        lstForAdapter.add(listMonAnforHomnayangi.get(i));
                                    }
                                    break;
                                case 2:
                                    n = 1;
                                    start = slmonan - (5 * n + 1);
                                    end = slmonan - (5 * n + 5);
                                    for (int i = start; i >= end; i--) {
                                        Log.i("chktrangchu",i+"");
                                        lstForAdapter.add(listMonAnforHomnayangi.get(i));
                                    }
                                    break;
                                case 3:
                                    n = 2;
                                    start = slmonan - (5 * n + 1);
                                    end = slmonan - (5 * n + 5);
                                    for (int i = start; i >= end; i--) {
                                        Log.i("chktrangchu",i+"");
                                        lstForAdapter.add(listMonAnforHomnayangi.get(i));
                                    }
                                    break;
                                case 4:
                                    n = 3;
                                    start = slmonan - (5 * n + 1);
                                    end = slmonan - (5 * n + 5);
                                    for (int i = start; i >= end; i--) {
                                        Log.i("chktrangchu",i+"");
                                        lstForAdapter.add(listMonAnforHomnayangi.get(i));
                                    }
                                    break;
                                case 5:
                                    n = 4;
                                    start = slmonan - (5 * n + 1);
                                    end = slmonan - (5 * n + 5);
                                    for (int i = start; i >= end; i--) {
                                        Log.i("chktrangchu",i+" thu: "+n);
                                        lstForAdapter.add(listMonAnforHomnayangi.get(i));
                                    }
                                    break;
                                case 6:
                                    n = 5;
                                    start = slmonan - (5 * n + 1);
                                    end = slmonan - (5 * n + 5);
                                    for (int i = start; i >= end; i--) {
                                        Log.i("chktrangchu",i+"");
                                        lstForAdapter.add(listMonAnforHomnayangi.get(i));
                                    }
                                    break;
                                case 7:
                                    n = 6;
                                    start = slmonan - (5 * n + 1);
                                    end = slmonan - (5 * n + 5);
                                    for (int i = start; i >= end; i--) {
                                        Log.i("chktrangchu",i+"");
                                        lstForAdapter.add(listMonAnforHomnayangi.get(i));
                                    }
                                    break;
                            }
                            homayangiAdapter = new HomNayAnGiAdapter();
                            homayangiAdapter.setData(lstForAdapter, new IRecycleViewClickListerner() {
                                @Override
                                public void onItemClick(MonAn monan) {
                                    Intent intent=new Intent(getActivity(), MonAnChiTietActivity.class);
                                    intent.putExtra("UserID",userid+"");
                                    intent.putExtra("IDMonAn",monan.getId());
                                    startActivity(intent);                                }
                            });
                            rv_trangchu_homnayangi.setAdapter(homayangiAdapter);
                            homnayangi_ani.startShimmer();
                            homnayangi_ani.setVisibility(View.GONE);
                            rv_trangchu_homnayangi.setVisibility(View.VISIBLE);
                        }

                    }
                    {
                        ArrayList<LoaiMon> listLoaiMon = new ArrayList<LoaiMon>();
                        JSONArray listLoaiMonserver = response.getJSONArray("dsMonAnTheoLoai");
                        if (listLoaiMonserver != null) {
                            for (int i = 0; i < listLoaiMonserver.length(); i++) {
                                JSONObject obj = listLoaiMonserver.optJSONObject(i);
                                JSONArray arr = obj.getJSONArray("dsMonAn");
                                ArrayList<MonAn> dsmonan = new ArrayList<MonAn>();
                                for (int j = 0; j < arr.length(); j++) {
                                    JSONObject monanobj = arr.optJSONObject(j);
                                    dsmonan.add(new MonAn(monanobj.getString("_id")
                                            , monanobj.getString("Ten"), monanobj.getString("Anh")));
                                }
                                if (dsmonan.size() != 0) {
                                    listLoaiMon.add(new LoaiMon(obj.getString("TenLoaiMon"), obj.getString("IDLoaiMon"), dsmonan));
                                }
                            }
                            if(userid!=null){
                                Log.i("chk","chk loai mon: "+userid);
                                trangchu_loaimon = new LoaiMon_TCAdapter(view.getContext());
                                trangchu_loaimon.setData2(listLoaiMon,userid,getActivity(), new IRecycleViewClickListerner() {
                                    @Override
                                    public void onItemClick(MonAn monan) {
                                        Intent intent=new Intent(getActivity(), MonAnChiTietActivity.class);
                                        intent.putExtra("UserID",userid+"");
                                        intent.putExtra("IDMonAn",monan.getId());
                                        startActivity(intent);                                    }
                                });
                            }else{
                                trangchu_loaimon = new LoaiMon_TCAdapter(view.getContext());
                                trangchu_loaimon.setData(listLoaiMon,getActivity(), new IRecycleViewClickListerner() {
                                    @Override
                                    public void onItemClick(MonAn monan) {
                                        Intent intent=new Intent(getActivity(), MonAnChiTietActivity.class);
                                        intent.putExtra("UserID",userid+"");
                                        intent.putExtra("IDMonAn",monan.getId());
                                        startActivity(intent);                                    }
                                });
                            }
                            rv_trangchu_loaimon.setAdapter(trangchu_loaimon);
                            loaimon_ani.startShimmer();
                            loaimon_ani.setVisibility(View.GONE);
                            rv_trangchu_loaimon.setVisibility(View.VISIBLE);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            };
            @Override
            public void onFailure(int code, Header[] headers, String res, Throwable throwable){
                Log.i("error", code + res);
            }
        });
        return;
    }

    private boolean checkInternetConnection() {
        ConnectivityManager connManager =
                (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connManager.getActiveNetworkInfo();
        if (networkInfo == null) {
            tc_mainlinear.setVisibility(View.GONE);
            nointernet.setVisibility(View.VISIBLE);
            return false;
        }
        return true;
    }
}
