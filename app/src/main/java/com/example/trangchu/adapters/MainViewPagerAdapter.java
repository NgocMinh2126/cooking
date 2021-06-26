package com.example.trangchu.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.trangchu.fragments.MarketFragment;
import com.example.trangchu.fragments.TipsFragment;
import com.example.trangchu.fragments.TrangChufragment;
import com.example.trangchu.models.Tips;

public class MainViewPagerAdapter extends FragmentStatePagerAdapter {
//    static TrangChufragment trangchu = new TrangChufragment();
//    static MarketFragment market = new MarketFragment();
//    static TipsFragment tip = new TipsFragment();

    public MainViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new TrangChufragment();
            case 1:
                return new MarketFragment();
            case 2:
                return new TipsFragment();
            default:
                return new TrangChufragment();
        }
    }

    @Override
    public int getCount() {
        return 3; //so luong tab
    }
}
