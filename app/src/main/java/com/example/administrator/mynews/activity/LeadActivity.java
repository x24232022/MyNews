package com.example.administrator.mynews.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.administrator.mynews.R;

import com.example.administrator.mynews.utils.SpUtils;


import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/12/12.
 */

public class LeadActivity extends AppCompatActivity {

    @BindView(R.id.vp_activity_lead)
    ViewPager mVp;
    @BindView(R.id.btn_enter)
    Button btnEnter;
    @BindView(R.id.iv_icon_activity_lead_1)
    ImageView ivIconActivityLead1;
    @BindView(R.id.iv_icon_activity_lead_2)
    ImageView ivIconActivityLead2;
    @BindView(R.id.iv_icon_activity_lead_3)
    ImageView ivIconActivityLead3;

    private ImageView[] icons=new ImageView[3];


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leadactivity);
        ButterKnife.bind(this);
        //给角标添加图片
        setCornerMark();
        //判断第一次进入的方法
        judgeFirstEntrance();
        //按钮点击跳转
        btnStart();
        //给viewpager设置滑动监听
        viewPagerListener();
    }

    /**
     * 给角标添加图片
     */
    private void setCornerMark() {
        icons[0] = ivIconActivityLead1;
        icons[1] = ivIconActivityLead2;
        icons[2] = ivIconActivityLead3;
    }

    /**
     * 给viewpager设置滑动监听
     */
    private void viewPagerListener() {
        mVp.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                //设置角标显隐
                setCornerMarkShow(position);
            }
            @Override
            public void onPageScrollStateChanged(int state) {

            }
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

        });
    }

    /**
     * 按钮点击跳转
     */
    private void btnStart() {
        btnEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SpUtils.putBooleanData(LeadActivity.this,"is_first_run",false);
                Intent intent=new Intent(LeadActivity.this,LogoActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * 设置角标显隐
     * @param position
     */
    private void setCornerMarkShow(int position) {
        if(position==2){
            btnEnter.setVisibility(View.VISIBLE);
        }else {
            btnEnter.setVisibility(View.INVISIBLE);
        }
        for (int i = 0; i <icons.length ; i++) {
            icons[i].setBackgroundResource(R.mipmap.adware_style_default);
        }
        icons[position].setBackgroundResource(R.mipmap.adware_style_selected);
    }

    /**
     * 判断第一次进入的方法
     */
    private void judgeFirstEntrance() {
        if (SpUtils.getBooleanData(LeadActivity.this, "is_first_run", true)) {
            icons[0].setBackgroundResource(R.mipmap.adware_style_selected);
            btnEnter.setVisibility(View.INVISIBLE);


            ArrayList<ImageView> pages = new ArrayList<ImageView>();
            //给viewpager添加图片
            AddPagerData(pages);

            MyPagerAdapter adapter = new MyPagerAdapter(pages);
            mVp.setAdapter(adapter);

        }else {
            Intent intet=new Intent(LeadActivity.this,LogoActivity.class);
            startActivity(intet);
            LeadActivity.this.finish();
        }
    }

    private void AddPagerData(ArrayList<ImageView> pages) {
        ImageView iv1 = new ImageView(LeadActivity.this);
        iv1.setBackgroundResource(R.mipmap.a);
        pages.add(iv1);

        ImageView iv2 = new ImageView(LeadActivity.this);
        iv2.setBackgroundResource(R.mipmap.b);
        pages.add(iv2);

        ImageView iv3 = new ImageView(LeadActivity.this);
        iv3.setBackgroundResource(R.mipmap.c);
        pages.add(iv3);
    }


}
