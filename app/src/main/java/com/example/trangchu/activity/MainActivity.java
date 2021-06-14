package com.example.trangchu.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;;

import com.example.trangchu.R;
import com.example.trangchu.adapters.MainViewPagerAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private BottomNavigationView nav_bottom;
    private ViewPager main_viewpager;
    private Toolbar toolbar;
    private DrawerLayout main_drawerlayout;
    private NavigationView main_menu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
                Log.i("menu","mon an thuong ngay");
                break;
            case R.id.chaosup:
                Log.i("menu","chao sup");
                break;
            case R.id.monnuong:
                Log.i("menu","mon nuong");
                break;
            case R.id.trangmieng:
                Log.i("menu","trang mieng");
                break;
            case R.id.anchay:
                Log.i("menu","an chay");
                break;
            case R.id.giamcan:
                Log.i("menu","giam can");
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