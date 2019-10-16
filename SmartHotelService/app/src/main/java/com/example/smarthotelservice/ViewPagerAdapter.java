package com.example.smarthotelservice;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    int pagecount;
    public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm);
        this.pagecount = behavior;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                RoomServiceFragment roomServiceFragment = new RoomServiceFragment();
                return roomServiceFragment;
            case 1:
                CartFragment cartFragment = new CartFragment();
                return cartFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return pagecount;
    }

    /*@Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }*/
}
