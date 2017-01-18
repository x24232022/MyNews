package com.example.administrator.mynews.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.example.administrator.mynews.R;
import com.example.administrator.mynews.utils.NoHttpInstences;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.rest.OnResponseListener;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.Response;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WabActivity extends AppCompatActivity {

    @BindView(R.id.webView_activity_web)
    WebView webViewActivityWeb;
    @BindView(R.id.toobar_activity_web)
    Toolbar toobarActivityWeb;
    @BindView(R.id.prohressBar_activity_web)
    ProgressBar prohressBarActivityWeb;
    @BindView(R.id.iv_toobar_activity_web)
    ImageView ivToobarActivityWeb;
    @BindView(R.id.collapsing_toobar_activity_web)
    CollapsingToolbarLayout collapsingToobarActivityWeb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wab);
        ButterKnife.bind(this);
        //通过意图传递数据,点击时穿入图片地址和新闻标题
        Intent intent = getIntent();
        String url = intent.getStringExtra("url");
        String title = intent.getStringExtra("title");
        String img_url = intent.getStringExtra("img_url");
        initcollapseToobar(img_url);

        //将toobar设置成actionbar
        setSupportActionBar(toobarActivityWeb);
        //设置actionbar的返回按钮
        setActionbarReturn(url, title);

    }

    /**
     * 设置actionbar的返回按钮
     * @param url
     * @param title
     */
    private void setActionbarReturn(String url, String title) {
        ActionBar bar = getSupportActionBar();

        if (bar != null) {
            //显示返回按钮
            bar.setDisplayShowHomeEnabled(true);
            //配置返回按钮
            bar.setDisplayHomeAsUpEnabled(true);
            //设置标题
            bar.setTitle(title);
        }
        //设置网页属性
        setWebview(url);
    }


    /**
     * 设置网页属性
     * @param url
     */
    private void setWebview(String url) {
        ///设置网页图片的加载属性
        WebSettings settings = webViewActivityWeb.getSettings();
        settings.setSupportZoom(true);//支持缩放
        settings.setJavaScriptEnabled(true);//支持js脚本语言

        settings.setDisplayZoomControls(true);//显示缩放控件
        //解析网络图片
        webViewActivityWeb.loadUrl(url);
        //设置web浏览器客户端
        webViewActivityWeb.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                collapsingToobarActivityWeb.setTitle(title);
            }
        });
        //这种web视图客户端
        webViewActivityWeb.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);

            }
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                //设置progressbar的隐藏属性,加载完成后自动消失
                prohressBarActivityWeb.setVisibility(View.GONE);
            }
        });
    }

    /**
     * 设置可折叠toobar内容
     * @param img_url
     */
    private void initcollapseToobar(String img_url) {
        Glide.with(this).load(img_url).into(ivToobarActivityWeb);
        Request<Bitmap> imageRequest = NoHttp.createImageRequest(img_url);
        NoHttpInstences.getInstance().add(0, imageRequest, new OnResponseListener<Bitmap>() {
            @Override
            public void onStart(int what) {

            }

            @Override
            public void onSucceed(int what, Response<Bitmap> response) {
                //设置actionbar的主色调为图片的主色调,如果提取色调失败,默认设置成系统色调
                int vibrantColor = Palette.from(response.get()).generate().getVibrantColor(getResources().getColor(R
                        .color.colorPrimary));
                collapsingToobarActivityWeb.setContentScrimColor(vibrantColor);
            }

            @Override
            public void onFailed(int what, Response<Bitmap> response) {

            }

            @Override
            public void onFinish(int what) {

            }
        });
    }


    //    @Override
//    public boolean onSupportNavigateUp() {
//        WabActivity.this.finish();
//        return super.onSupportNavigateUp();
//    }
    //设置actionbar左上角的返回按钮
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
            break;
        }


        return super.onOptionsItemSelected(item);
    }
}
