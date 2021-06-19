package com.example.trangchu.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;;

import com.example.trangchu.R;
import com.example.trangchu.adapters.MainViewPagerAdapter;
import com.example.trangchu.models.LoaiMon;
import com.example.trangchu.service.HttpUtils;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

//https://evening-tor-29508.herokuapp.com/
public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    List<LoaiMon>listloaimon;
    private BottomNavigationView nav_bottom;
    private ViewPager main_viewpager;
    private Toolbar toolbar;
    private DrawerLayout main_drawerlayout;
    private NavigationView main_menu;
    private long backpresstime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        setContentView(R.layout.activity_main);
        nav_bottom=findViewById(R.id.nav_bottom);
        main_viewpager=findViewById(R.id.main_viewpager);
        main_drawerlayout=findViewById(R.id.main_draw);
        toolbar=findViewById(R.id.main_toolbar);
        main_menu=findViewById(R.id.nav_main_view);

        setUpMainViewPager();
        setSupportActionBar(toolbar);
        main_menu.bringToFront();
        ActionBarDrawerToggle main_toggle=new ActionBarDrawerToggle(this,main_drawerlayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        main_drawerlayout.addDrawerListener(main_toggle);
        main_toggle.syncState();
        main_menu.setNavigationItemSelectedListener(this);

        //nav botton
        nav_bottom.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.action_home:
                        main_viewpager.setCurrentItem(0);
                        break;
                    case R.id.action_market:
                        main_viewpager.setCurrentItem(1);
                        break;
                    case R.id.action_tips:
                        main_viewpager.setCurrentItem(2);
                        break;
                }
                return true;
            }
        });

    }
    //xu ly nut back
    @Override
    public void onBackPressed() {
        if(main_drawerlayout.isDrawerOpen(GravityCompat.START)){
            //dang mo menu nhan back thi dong menu
            main_drawerlayout.closeDrawer(GravityCompat.START);
            return;
        }else if(backpresstime+2000>System.currentTimeMillis()){
            super.onBackPressed();
            return;
        }else{
            Toast.makeText(MainActivity.this,"Nhấn back lần nữa để thoát",Toast.LENGTH_SHORT).show();
        }
        backpresstime=System.currentTimeMillis();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_navbar_top,menu);
        return true;
    }
    //su kien cho top nav
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
        if(id==R.id.search_btn){
            Intent intent=new Intent(MainActivity.this,SearchActivity.class);
            startActivity(intent);
            Log.i("btnchk","search");
        }else if(id==R.id.profile_btn){
            Log.i("btnchk","profile");
        }
        return true;
    }

    public void setUpMainViewPager(){
        MainViewPagerAdapter mainViewPagerAdapter=new MainViewPagerAdapter(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT); //chi fragment hien tai moi nam trong state on resume
        main_viewpager.setAdapter(mainViewPagerAdapter);
        main_viewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }
            @Override
            public void onPageSelected(int position) {
                switch (position){
                    case 0:
                        nav_bottom.getMenu().findItem(R.id.action_home).setChecked(true);
                        break;
                    case 1:
                        nav_bottom.getMenu().findItem(R.id.action_market).setChecked(true);
                        break;
                    case 2:
                        nav_bottom.getMenu().findItem(R.id.action_tips).setChecked(true);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.monan_yeuthich:
                Log.i("menu","mon an yeu thich");
                break;
            case R.id.thucdon:
                Log.i("menu","thuc don");
                break;
            case R.id.monan_thuongngay:
                Intent intent1=new Intent(MainActivity.this,DSMonAnActivity.class);
                intent1.putExtra("TenLoaiMon","Bữa ăn thường ngày");
                startActivity(intent1);
                break;
            case R.id.chaosup:
                Intent intent2=new Intent(MainActivity.this,DSMonAnActivity.class);
                intent2.putExtra("TenLoaiMon","Cháo súp");
                startActivity(intent2);
                break;
            case R.id.monnuong:
                Intent intent3=new Intent(MainActivity.this,DSMonAnActivity.class);
                intent3.putExtra("TenLoaiMon","Món nướng");
                startActivity(intent3);
                break;
            case R.id.trangmieng:
                Intent intent4=new Intent(MainActivity.this,DSMonAnActivity.class);
                intent4.putExtra("TenLoaiMon","Món tráng miệng");
                startActivity(intent4);
                break;
            case R.id.anchay:
                Intent intent5=new Intent(MainActivity.this,DSMonAnActivity.class);
                intent5.putExtra("TenLoaiMon","Các món ăn chay");
                startActivity(intent5);
                break;
            case R.id.giamcan:
                Intent intent6=new Intent(MainActivity.this,DSMonAnActivity.class);
                intent6.putExtra("TenLoaiMon","Các món ăn giảm cân");
                startActivity(intent6);
                break;
            case R.id.lienhe:
                Log.i("menu","lien he");
                break;
            case R.id.chinhsach:
                Log.i("menu","chinh sach");
                break;
        }
        return true;
    }
}