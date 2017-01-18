package com.example.administrator.mynews.activity;

import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.administrator.mynews.App;
import com.example.administrator.mynews.R;
import com.example.administrator.mynews.utils.Constant;
import com.wilddog.client.DataSnapshot;
import com.wilddog.client.SyncError;
import com.wilddog.client.SyncReference;
import com.wilddog.client.ValueEventListener;
import com.wilddog.wilddogauth.common.Context;
import com.wilddog.wilddogauth.model.WilddogUser;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import cn.smssdk.gui.RegisterPage;
import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {


    @BindView(R.id.toolbar_activity_main)
    Toolbar toolbarActivityMain;
    @BindView(R.id.navigation)
    NavigationView navigation;
    @BindView(R.id.drawlayout_activity_main)
    DrawerLayout drawlayoutActivityMain;
    @BindView(R.id.fl_content_activity_main)
    FrameLayout flContentActivityMain;
    private CircleImageView icon;
    private FragmentManager fragmentManager;
    private ArrayList<Fragment> fragmentList;
    private Fragment currFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        //初始化Toobar
        initToobar();
        //初始化碎片
        initFragment();
        //初始化导航视图
        initNavigationview();
        //初始化野狗
        initWilddog();

    }

    /**
     * 初始化野狗
     */
    private void initWilddog() {
        WilddogUser user = App.user;
        icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //判断软件开启时用户的登录状态
                if (App.user != null) {
                    //更换头像界面
                    showDialog();
                } else {
                    //注册界面
                    register();
                }

            }
        });

        if (user != null) {
            final String uid = user.getUid();
            ValueEventListener postListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String imgStr = (String) dataSnapshot.child(uid).getValue();
                    //将String转化为bitmap
                    byte[] decode = Base64.decode(imgStr, Base64.DEFAULT);
                    //将byte数组转变成bitmap
                    Bitmap bitmap = BitmapFactory.decodeByteArray(decode, 0, decode.length);
                    icon.setImageBitmap(bitmap);
                }

                @Override
                public void onCancelled(SyncError syncError) {

                }
            };
            //设置数据库的数据改变监听
            App.ref.addValueEventListener(postListener);
        }
    }
    //注册方法
    private void register() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setItems(new String[]{"手机注册", "邮箱注册"}, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    //手机注册
                    case 0:
                        //打开注册页面
                        RegisterPage registerPage = new RegisterPage();
                        registerPage.setRegisterCallback(new EventHandler() {
                            public void afterEvent(int event, int result, Object data) {
                                // 解析注册结果
                                if (result == SMSSDK.RESULT_COMPLETE) {
                                    @SuppressWarnings("unchecked")
                                    HashMap<String, Object> phoneMap = (HashMap<String, Object>) data;
                                    String country = (String) phoneMap.get("country");
                                    String phone = (String) phoneMap.get("phone");

                                    // 提交用户信息（此方法可以不调用）
                                    registerUser(country, phone);
                                }
                            }
                        });
                        registerPage.show(MainActivity.this);

                        break;
                    case 1:
                        //邮箱注册
                        Intent intent = new Intent(MainActivity.this, InfoActivity.class);
                        startActivityForResult(intent, Constant.GET_IMAGE_FROM_SERVICE);
                        break;
                }
            }
        }).show();
    }
    //手机注册成功的Toast
    private void registerUser(String country, String phone) {
        Toast.makeText(this, country+"+"+phone, Toast.LENGTH_SHORT).show();

    }

    /**
     * 初始化导航视图
     */
    private void initNavigationview() {
        navigation.setCheckedItem(R.id.new_navgation);
        currFragment = fragmentList.get(0);
        fragmentManager.beginTransaction().add(R.id.fl_content_activity_main, currFragment).commit();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawlayoutActivityMain, toolbarActivityMain, R
                .string.open, R.string.close);
        toggle.syncState();
        navigation.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int index = 0;
                switch (item.getItemId()) {
                    case R.id.new_navgation:
                        index = 0;
                        toolbarActivityMain.setTitle("新闻");
                        break;
                    case R.id.image_navgation:

                        index = 1;
                        toolbarActivityMain.setTitle("图片");
                        break;
                    case R.id.like_navgation:
                        index = 2;
                        toolbarActivityMain.setTitle("收藏");

                        break;
                    case R.id.chat_navgation:
                        index = 3;
                        toolbarActivityMain.setTitle("对话");
                        break;
                }
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                Fragment nextFragment = fragmentList.get(index);

                //判断后续碎片不等于当前碎片
                if (nextFragment != currFragment) {
                    //判断碎片是是否已经加载过
                    if (!nextFragment.isAdded()) {
                        //严谨性判断
                        if (currFragment != null) {
                            //将上一个碎片隐藏
                            transaction.hide(currFragment);
                        }
                        //添加下一个碎片
                        transaction.add(R.id.fl_content_activity_main, nextFragment);
                    } else {
                        //防止碎片加载混乱
                        if (currFragment != null) {

                            transaction.hide(currFragment);
                        }
                        transaction.show(nextFragment);
                    }
                    //将新添加的碎片赋值为当前碎片
                    currFragment = nextFragment;
                }


                transaction.commit();
                drawlayoutActivityMain.closeDrawers();
                return true;
            }
        });
    }
    //初始化fragment
    private void initFragment() {
        fragmentManager = getSupportFragmentManager();
        fragmentList = new ArrayList<>();
        fragmentList.add(new NewsFragment());
        fragmentList.add(new ImageFragment());
        fragmentList.add(new LikeFragment());
        fragmentList.add(new ChatFragment());
    }

    private void initToobar() {
        icon = (CircleImageView) navigation.getHeaderView(0).findViewById(R.id.icon_image);
        toolbarActivityMain.setTitle("新闻");
        toolbarActivityMain.setLogo(R.mipmap.ic);
        toolbarActivityMain.setNavigationIcon(R.mipmap.ic_launcher);
        setSupportActionBar(toolbarActivityMain);
    }
    //显示修改头像的对话框
    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //设置对话框的标题
        builder.setTitle("请选择更改的头像")
                //设置对话框的条目
                .setItems(new String[]{"相机", "本地图库"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (i) {
                            case 0:
                                fromCamera();
                                break;
                            case 1:
                                fromGallery();
                                break;
                        }
                    }


                }).show();
    }
    //调用相机设置头像
    private void fromCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, Constant.GET_IMAGE_FROM_CAMERA);
    }
    //调用图库设置头像
    private void fromGallery() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, Constant.GET_IMAGE_FROM_CALLERY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //本地相机获取图片之后,从InfoActivity传过来bitmap的字符串,将字符转解码成图片设置成头像2
        if (requestCode == Constant.GET_IMAGE_FROM_SERVICE && resultCode == RESULT_OK) {
            String imgStr = data.getStringExtra("data");
            //将图片字符串解码为byte数组
            byte[] decode = Base64.decode(imgStr, Base64.DEFAULT);
            //将byte数组转变成bitmap
            Bitmap bitmap = BitmapFactory.decodeByteArray(decode, 0, decode.length);
            icon.setImageBitmap(bitmap);

        } else if (requestCode == Constant.GET_IMAGE_FROM_CAMERA && resultCode == RESULT_OK) {
            //开启软件,如果已经登录,点击默认开启头像选择提示框,相机拍完照片后,将新的照片设置成头像并进行上传0
            Bundle extras = data.getExtras();
            Bitmap bitmap = (Bitmap) extras.get("data");
            ByteArrayOutputStream baout = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baout);
            String imgStr = Base64.encodeToString(baout.toByteArray(), Base64.DEFAULT);
            App.ref.child(App.user.getUid()).setValue(imgStr);
            icon.setImageBitmap(bitmap);
        } else if (requestCode == Constant.GET_IMAGE_FROM_CALLERY && resultCode == RESULT_OK) {
            //将本地图库的图片设置成头像,上传到云端
            Uri imgUri = data.getData();
            ContentResolver contentResolver = getContentResolver();
            try {
                InputStream in = contentResolver.openInputStream(imgUri);
                //将头像框的大小设置成图片压缩的尺寸
                Rect rect = new Rect(0, 0, 120, 120);
                BitmapFactory.Options opts = new BitmapFactory.Options();
                //设置图片大小的压缩比例
                opts.inSampleSize = 20;
                Bitmap bitmap = BitmapFactory.decodeStream(in, rect, opts);
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                String imgStr = Base64.encodeToString(out.toByteArray(), Base64.DEFAULT);
                App.ref.child(App.user.getUid()).setValue(imgStr);
                icon.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
    public void onBackPressed() {
        if (drawlayoutActivityMain.isDrawerOpen(GravityCompat.START)) {
            drawlayoutActivityMain.closeDrawer(GravityCompat.START);
        }else {
            super.onBackPressed();
        }
    }

}
