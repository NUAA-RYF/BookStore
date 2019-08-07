package com.thundersoft.bookstore.activity;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import com.flyco.tablayout.SlidingTabLayout;
import com.thundersoft.bookstore.R;
import com.thundersoft.bookstore.adapter.PagerAdapter;
import com.thundersoft.bookstore.fragment.BookListFragment;
import com.thundersoft.bookstore.fragment.BookRecommendFragment;
import com.thundersoft.bookstore.fragment.ManagerFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    List<Fragment> fragments;
    List<String> titles;

    @BindView(R.id.viewpager_bottom)
    ViewPager viewPager;
    @BindView(R.id.tab_bottom)
    SlidingTabLayout tabLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initData();
        initPagerAdapter();

        tabLayout.setViewPager(viewPager);
    }

    //初始化数据
    private void initData() {

        Log.i(TAG, "initData: 进入初始化数据");
        fragments = new ArrayList<>();
        titles = new ArrayList<>();
        fragments.add(BookRecommendFragment.newInstance("BookRecommend"));
        fragments.add(BookListFragment.newInstance("BookList"));
        fragments.add(ManagerFragment.newInstance("Manager"));
        titles.add("首页");
        titles.add("书籍");
        titles.add("我");
        viewPager.setOffscreenPageLimit(1);

    }

    private void initPagerAdapter() {
        FragmentManager fm = getSupportFragmentManager();
        PagerAdapter pagerAdapter = new PagerAdapter(fm, fragments, titles);
        viewPager.setAdapter(pagerAdapter);
    }

}
