package com.example.administrator.mynews.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.mynews.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class LogoActivity extends AppCompatActivity {
    @BindView(R.id.iv_logo_activity_logo)
    ImageView ivLogoActivityLogo;
    @BindView(R.id.btn_start_activity_logo)
    Button btn_start;

    private int leftTime = 3;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //设置倒计时
            calcLeftTime(msg);
        }
    };

    /**
     * 设置倒计时方法
     * @param msg
     */
    private void calcLeftTime(Message msg) {
        switch (msg.what) {
            case 0:
                if (leftTime > 0) {
                    String newText = "跳过(" + leftTime-- + "s)";
                    btn_start.setText(newText);
                    handler.sendEmptyMessageDelayed(0, 1000);
                } else {
                    Intent intent = new Intent(LogoActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
                break;

        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logo);
        ButterKnife.bind(this);
        btn_start.setVisibility(View.INVISIBLE);
        initAnimation();
        initListener();
    }


    private void initListener() {
        btn_start.setVisibility(View.INVISIBLE);
        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(LogoActivity.this,MainActivity.class);
                startActivity(intent);
                handler.removeMessages(0);
                LogoActivity.this.finish();
            }
        });
    }

    private void initAnimation() {

        Animation loagAnimation = AnimationUtils.loadAnimation(LogoActivity.this, R.anim.animation);
        ivLogoActivityLogo.setAnimation(loagAnimation);
        loagAnimation.start();
        loagAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                btn_start.setVisibility(View.VISIBLE);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        handler.sendEmptyMessageDelayed(0, 1000);

    }

}
