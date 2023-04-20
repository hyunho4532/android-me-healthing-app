package com.example.workingapp.Fragment.viewPager.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.workingapp.Fragment.viewPager.ViewPagerFrame1Fragment;
import com.example.workingapp.Fragment.viewPager.ViewPagerFrame2Fragment;
import com.example.workingapp.Fragment.viewPager.ViewPagerFrame3Fragment;

public class SettingAdapter extends FragmentStateAdapter {

    public int mCount;

    public SettingAdapter(@NonNull FragmentActivity fragmentActivity, int count) {
        super(fragmentActivity);
        this.mCount = count;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        int index = getRealPosition(position);

        if (index == 0) return new ViewPagerFrame1Fragment();
        else if (index == 1) return new ViewPagerFrame2Fragment();

        else return new ViewPagerFrame3Fragment();
    }

    private int getRealPosition(int position) {
        return position % mCount;
    }

    @Override
    public int getItemCount() {
        return 2000;
    }
}
