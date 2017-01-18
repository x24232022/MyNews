package com.example.administrator.mynews.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.administrator.mynews.App;
import com.example.administrator.mynews.R;
import com.example.administrator.mynews.utils.Constant;
import com.wilddog.wilddogauth.core.Task;
import com.wilddog.wilddogauth.core.listener.OnCompleteListener;
import com.wilddog.wilddogauth.core.request.UserProfileChangeRequest;
import com.wilddog.wilddogauth.core.result.AuthResult;
import com.wilddog.wilddogauth.model.WilddogUser;

import java.io.ByteArrayOutputStream;

import butterknife.BindView;
import butterknife.ButterKnife;

public class InfoActivity extends AppCompatActivity {

    @BindView(R.id.et_email_activity_info)
    TextInputEditText etEmailActivityInfo;
    @BindView(R.id.et_email_Layout_activity_info)
    TextInputLayout etEmailLayoutActivityInfo;
    @BindView(R.id.et_password_activity_info)
    TextInputEditText etPasswordActivityInfo;
    @BindView(R.id.et_password_Layout_activity_info)
    TextInputLayout etPasswordLayoutActivityInfo;
    @BindView(R.id.btn_login_activity_info)
    Button btnLoginActivityInfo;
    @BindView(R.id.btn_register_activity_info)
    Button btnRegisterActivityInfo;
    private WilddogUser curUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        ButterKnife.bind(this);
        //添加账户文本变化监听器
        etTextChangedListener();
        //文本密码文本改变监听器,监听输入框中的文字内容变化
        etpasswordChangedListener();
    }

    /**
     * 文本密码文本改变监听器,监听输入框中的文字内容变化
     */
    private void etpasswordChangedListener() {
        etPasswordActivityInfo.addTextChangedListener(new TextWatcher() {
            //文字改变前
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            //文字改变中
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            //文字改变后
            @Override
            public void afterTextChanged(Editable editable) {

                if (editable.toString().trim().length() < 8) {
                    etPasswordLayoutActivityInfo.setError("邮箱不能少于8个字符");
                } else {
                    etPasswordLayoutActivityInfo.setErrorEnabled(false);
                }
            }
        });
    }

    /**
     * 添加账户文本变化监听器
     */
    private void etTextChangedListener() {
        etEmailActivityInfo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                //设置错误提示
                if (editable.toString().trim().length() < 8) {
                    etEmailLayoutActivityInfo.setError("邮箱不能少于8个字符");
                } else {
                    etEmailLayoutActivityInfo.setErrorEnabled(false);
                }
            }
        });
    }

    /**
     * 注册按钮
     * @param view
     */
    public void register(View view) {

        String email = etEmailActivityInfo.getText().toString().trim();
        String password = etPasswordActivityInfo.getText().toString().trim();
        btnRegisterActivityInfo.setEnabled(true);
        App.mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // 获取用户
                            curUser = task.getResult().getWilddogUser();
                            App.user = curUser;
                            Toast.makeText(InfoActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                        } else {
                            // 错误处理
                            Log.d("result", task.getException().toString());
                            Toast.makeText(InfoActivity.this, "注册失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


    }

    /**
     * 登录按钮
     * @param view
     */
    public void login(View view) {
        String email = etEmailActivityInfo.getText().toString().trim();
        String password = etPasswordActivityInfo.getText().toString().trim();
        btnRegisterActivityInfo.setEnabled(true);
        App.mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(Task<AuthResult> task) {
                        Log.d(InfoActivity.class.getName(), "signInWithEmail:onComplete:" + task
                                .isSuccessful());
                        if (!task.isSuccessful()) {
                            Log.w(InfoActivity.class.getName(), "signInWithEmail", task.getException());
                            Toast.makeText(InfoActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(InfoActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                            showDialog();
                    }
                    }
                });

    }

    /**
     * 头像设置提示框
     */
    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //设置对话框的标题
        builder.setTitle("请设置头像")
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

    /**
     * 本地图库选择头像
     */
    private void fromGallery() {

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, Constant.GET_IMAGE_FROM_CALLERY);

    }

    /**
     * 设置启动相机选择头像
     */
    private void fromCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, Constant.GET_IMAGE_FROM_CAMERA);
    }

    //接收图片信息
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //请求码              //结果码
        if (requestCode == Constant.GET_IMAGE_FROM_CAMERA && resultCode == RESULT_OK) {
            //长传图片
            uploadPictures(data);
        }
    }

    /**
     * 上传图片
     * @param data
     */
    private void uploadPictures(Intent data) {
        Bundle extras = data.getExtras();
        Bitmap bitmap = (Bitmap) extras.get("data");
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
        //进行Base64格式转化成可以进行网络传输的字符串
        final String imgStr = Base64.encodeToString(out.toByteArray(), Base64.DEFAULT);
        if (App.user != null) {
            App.ref.child(App.user.getUid()).setValue(imgStr);
            //同步图片Uid和账户
            synUid(imgStr);
        } else {
            Toast.makeText(this, "用户信息不存在", Toast.LENGTH_SHORT).show();
        }
        Toast.makeText(this, "上传成功", Toast.LENGTH_SHORT).show();
    }

    /**
     * 同步图片Uid和账户
     * @param imgStr
     */
    private void synUid(final String imgStr) {
        WilddogUser user = App.user;
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setPhotoUri(Uri.parse(curUser.getUid()))
                .build();
        user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(Task<Void> task) {
                        if (task.isSuccessful()) {
                            Intent intent = new Intent();
                            intent.putExtra("data", imgStr);
                            //发送图片字符串
                            setResult(RESULT_OK, intent);
                            finish();
                        } else {
                            // 发生错误
                        }
                    }
                });
    }
}
