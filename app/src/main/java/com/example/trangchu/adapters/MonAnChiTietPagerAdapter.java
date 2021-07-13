package com.example.trangchu.adapters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.trangchu.fragments.MonAnChiTiet_CachLamFragment;
import com.example.trangchu.fragments.MonAnChiTiet_NLieuFragment;

public class MonAnChiTietPagerAdapter extends FragmentStatePagerAdapter {
    public MonAnChiTietPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new MonAnChiTiet_NLieuFragment();
            case 1:
                return new MonAnChiTiet_CachLamFragment();
            default:
                return new MonAnChiTiet_NLieuFragment();
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        String title="";
        switch (position){
            case 0:
                title="Nguyên liệu";
                break;
            case 1:
                title="Cách làm";
                break;
        }
        return title;
    }
}
