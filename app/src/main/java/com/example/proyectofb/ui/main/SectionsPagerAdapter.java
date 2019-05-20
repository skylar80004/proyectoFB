package com.example.proyectofb.ui.main;

import android.content.Context;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.proyectofb.FriendFragment;
import com.example.proyectofb.NotifyFragment;
import com.example.proyectofb.ProfileFragment;
import com.example.proyectofb.R;
import com.example.proyectofb.TimeLiFragment;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.tabTimeLineText, R.string.tabNotificationsLineText, R.string.tabFriendsLineText, R.string.tabProfileText};
    private final Context mContext;

    public SectionsPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {


        Fragment frag = ProfileFragment.newInstance("hola", "mundo");
        switch (position){
            case 3:
                frag = ProfileFragment.newInstance("hola", "mundo");
                break;
            case 2:
                frag = FriendFragment.newInstance("friend","friend");
                break;
            case 1:
                frag = NotifyFragment.newInstance("noti","noti");
                break;
            case 0:
                frag =  TimeLiFragment.newInstance("new","new");
                break;
        }
        return frag;
      //  return PlaceholderFragment.newInstance(position + 1);





    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        // Show 2 total pages.
        return 4;
    }
}