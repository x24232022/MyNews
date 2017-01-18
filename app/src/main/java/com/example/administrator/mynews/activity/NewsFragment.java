package com.example.administrator.mynews.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.administrator.mynews.R;
import com.example.administrator.mynews.adapter.MyFragmentPagerAdapter;
import com.example.administrator.mynews.fragment.NewBenFragment;
import com.example.administrator.mynews.utils.Constant;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/12/14.
 */
public class NewsFragment extends Fragment {

    @BindView(R.id.tabs_news_fragment)
    TabLayout tabsNewsFragment;
    @BindView(R.id.vp_news_fragment)
    ViewPager vpNewsFragment;
    private List<Fragment> fragments;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.layout_news_fragment_activity_main,
                null);

        ButterKnife.bind(this, layout);
        tabsNewsFragment.setupWithViewPager(vpNewsFragment);
        tabsNewsFragment.setTabMode(TabLayout.MODE_SCROLLABLE);
        tabsNewsFragment.setBackgroundColor(Color.LTGRAY);
        tabsNewsFragment.setTabTextColors(Color.BLUE,Color.GREEN);
        return layout;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fragments = new ArrayList<>();

        fragments.add(new NewBenFragment(Constant.SHEHUI_NEWS_QUERY_STRING));
        fragments.add(new NewBenFragment(Constant.GUONEI_NEWS_QUERY_STRING));
        fragments.add(new NewBenFragment(Constant.GUOJI_NEWS_QUERY_STRING));
        fragments.add(new NewBenFragment(Constant.TIYU_NEWS_QUERY_STRING));
        fragments.add(new NewBenFragment(Constant.YULE_NEWS_QUERY_STRING));
        fragments.add(new NewBenFragment(Constant.JUNSHI_NEWS_QUERY_STRING));
        fragments.add(new NewBenFragment(Constant.KEJI_NEWS_QUERY_STRING));
        fragments.add(new NewBenFragment(Constant.CAIJING_NEWS_QUERY_STRING));
        fragments.add(new NewBenFragment(Constant.SHISHANG_NEWS_QUERY_STRING));
        MyFragmentPagerAdapter adapter = new MyFragmentPagerAdapter(getFragmentManager(), fragments);
        vpNewsFragment.setAdapter(adapter);


    }


}
