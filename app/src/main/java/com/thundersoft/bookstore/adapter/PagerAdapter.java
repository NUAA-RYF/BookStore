package com.thundersoft.bookstore.adapter;

import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import java.util.List;

public class PagerAdapter extends FragmentStatePagerAdapter {

    private List<String> title;

    private List<Fragment> mList;

    public PagerAdapter(FragmentManager fm, List<Fragment> fragments, List<String> title) {
        super(fm);
        this.mList = fragments;
        this.title = title;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        super.destroyItem(container, position, object);
    }

    @Override
    public Fragment getItem(int position) {
        return mList.get(position);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return title.get(position % mList.size());
    }

    @Override
    public int getCount() {
        return mList.size();
    }
}
