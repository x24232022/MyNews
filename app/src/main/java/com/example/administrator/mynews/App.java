package com.example.administrator.mynews;

import android.app.Application;

import com.wilddog.client.SyncReference;
import com.wilddog.client.WilddogSync;
import com.wilddog.wilddogauth.WilddogAuth;
import com.wilddog.wilddogauth.model.WilddogUser;
import com.wilddog.wilddogcore.WilddogApp;
import com.wilddog.wilddogcore.WilddogOptions;
import com.yolanda.nohttp.NoHttp;

import cn.jpush.android.api.JPushInterface;
import cn.sharesdk.framework.ShareSDK;
import cn.smssdk.SMSSDK;

/**
 * Created by Administrator on 2016/12/13.
 */

public class App extends Application {

    public static WilddogAuth mAuth;
    public static SyncReference ref;
    public static WilddogUser user;
    @Override
    public void onCreate() {
        super.onCreate();
        NoHttp.initialize(this);
        WilddogOptions options = new WilddogOptions.Builder().setSyncUrl("https://newsapp1234" +
                ".wilddogio.com").build();
        WilddogApp.initializeApp(this, options);
        mAuth = WilddogAuth.getInstance();
        ref = WilddogSync.getInstance().getReference("users");
        //拿到当前用户
        WilddogUser currentUser = mAuth.getCurrentUser();
        if(currentUser!=null){
            user=currentUser;
        }

        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);

        ShareSDK.initSDK(this);

        SMSSDK.initSDK(this, "1a472dedf1a3c", "e83a4331070009bd2f37de78388f7687");
    }
}
