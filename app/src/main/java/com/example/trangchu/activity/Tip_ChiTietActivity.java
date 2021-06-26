package com.example.trangchu.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.trangchu.R;
import com.example.trangchu.fragments.Tip01Fragment;
import com.example.trangchu.fragments.Tip02Fragment;
import com.example.trangchu.fragments.Tip03Fragment;
import com.example.trangchu.fragments.Tip04Fragment;
import com.example.trangchu.fragments.Tip05Fragment;
import com.example.trangchu.fragments.Tip06Fragment;
import com.example.trangchu.models.Tips;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;

public class Tip_ChiTietActivity extends AppCompatActivity {
    private AppBarLayout tip_appbarlayout;
    private CollapsingToolbarLayout tip_collapsing;
    private Toolbar tip_toolbar;
    private TextView tv_noidung;
    private Tips tip;
    private ImageView img_tip_item;
    private FrameLayout rl_tip_item;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_tip_chitiet);
        tip_appbarlayout=findViewById(R.id.tip_item_appbar);
        tip_collapsing=findViewById(R.id.collapsing_tip);
        tip_toolbar=findViewById(R.id.tip_item_toolbar);
        rl_tip_item=findViewById(R.id.rl_tip_item);
        img_tip_item=findViewById(R.id.img_tip_item);
        //set data
        tip=setTipdata(this.getIntent());

        toolbar_animation();
        setTip_toolbar();
        img_tip_item.setImageResource(tip.getAnh());
    }
    public void setTip_toolbar(){
        setSupportActionBar(tip_toolbar);
        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }
    public Tips setTipdata(Intent intent){
        String str = intent.getStringExtra("id");
        Tips t=new Tips();
        if(str.equals("1")){
            t.setData("Bí quyết nêm nếm",R.drawable.tip_img_01);
            replaceFragment(new Tip01Fragment());
        }else if(str.equals("2")){
            t.setData("Bí quyết nấu canh",R.drawable.tip_img_02);
            replaceFragment(new Tip02Fragment());
        }else if(str.equals("3")){
            t.setData("Mẹo nấu cháo",R.drawable.tip03);
            replaceFragment(new Tip03Fragment());
        }else if(str.equals("4")){
            t.setData("Bí quyết chọn thực phẩm",R.drawable.tip04);
            replaceFragment(new Tip04Fragment());
        }else if(str.equals("5")){
            t.setData("Trị bỏng rát khi cắt ớt",R.drawable.tip05);
            replaceFragment(new Tip05Fragment());
        }else if(str.equals("6")){
            t.setData("Cách bảo quản hoa quả",R.drawable.tip06);
            replaceFragment(new Tip06Fragment());
        }
        return t;
    }


    public void toolbar_animation(){
        tip_collapsing.setTitle(tip.getTen());
        Bitmap bitmap= BitmapFactory.decodeResource(getResources(),tip.getAnh());
        tip_collapsing.setStatusBarScrimColor(getResources().getColor(R.color.color_basic));
        tip_collapsing.setContentScrimColor(getResources().getColor(R.color.color_basic));
        tip_collapsing.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);
        tip_collapsing.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void replaceFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.rl_tip_item,fragment);
        fragmentTransaction.commit();
    }
}
