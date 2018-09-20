package com.example.karol.weatherassistant;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class SectionsStatePagerAdapter extends FragmentStatePagerAdapter
{
    private final List<Fragment> _fragmentList = new ArrayList<>();
    private final List<String> _fragmentTitleList = new ArrayList<>();

    public SectionsStatePagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public void addFragment(Fragment fragment, String title)
    {
        _fragmentList.add(fragment);
        _fragmentTitleList.add(title);
    }

    @Override
    public Fragment getItem(int position) {
        return _fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return _fragmentList.size();
    }
}
