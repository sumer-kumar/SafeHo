package com.sumer.safeho.adapters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.sumer.safeho.Constant;
import com.sumer.safeho.fragments.EmergencyFragment;
import com.sumer.safeho.fragments.NotificationFragment;

public class FragmentAdapter extends FragmentPagerAdapter {
    public FragmentAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    public FragmentAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch(position)
        {
            case 0 : return new EmergencyFragment();
            case 1 : return new NotificationFragment();
            default: return new EmergencyFragment();
        }
    }
    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position)
        {
            case 0 : return Constant.EMERGENCY;
            case 1 : return Constant.NOTIFICATION;
            default: return Constant.EMERGENCY;
        }
    }
}
