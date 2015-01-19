package com.chrisgcasey.glimpse;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;




/**
 * Created by Chris on 1/6/2015.
 */
public class TabPagerAdapter extends FragmentPagerAdapter {

    protected Context mContext;
    String[] tabNames = new String[]{"Inbox", "Friends"};




    public TabPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position){
            case 0:
                return new InboxFragment();
            case 1:
                return new FriendsFragment();
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 2;
    }
    @Override
    public CharSequence getPageTitle(int position){
        switch (position){
            case 0:
                return tabNames[position];
            case 1:
                return tabNames[position];
        }
        return null;
    }
}