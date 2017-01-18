package com.example.administrator.mynews.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.administrator.mynews.BuildConfig;
import com.example.administrator.mynews.R;
import com.example.administrator.mynews.activity.WabActivity;
import com.example.administrator.mynews.adapter.NewBenAdapter;
import com.example.administrator.mynews.db.MyNewsOrmliteOpenHelper;
import com.example.administrator.mynews.entity.CollectNewBean;
import com.example.administrator.mynews.entity.NewBean;
import com.example.administrator.mynews.utils.Constant;
import com.example.administrator.mynews.utils.GlideImageLoader;
import com.example.administrator.mynews.utils.NoHttpInstences;

import com.example.administrator.mynews.utils.SpUtils;
import com.j256.ormlite.dao.Dao;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.rest.OnResponseListener;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.Response;
import com.youth.banner.Banner;
import com.youth.banner.Transformer;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import cn.sharesdk.onekeyshare.OnekeyShare;

/**
 * Created by Administrator on 2016/12/14.
 */

public class NewBenFragment extends Fragment {

    private SwipeRefreshLayout layout;
    private ListView listview;
    private List<NewBean.ResultBean.DataBean> datas;
    private String url;
    private NewBenAdapter adapter;
    private Dao<CollectNewBean, Long> dao;


    public NewBenFragment(String url) {
        this.url = url;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        layout = (SwipeRefreshLayout) inflater.inflate(R.layout.listview_newben_fragment_activity,
                null);
        listview = (ListView) layout.findViewById(R.id.listview_news_fragment_activity_main);

        layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        SystemClock.sleep(2000);
                        adapter.datas.remove(0);
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                adapter.notifyDataSetChanged();
                                layout.setRefreshing(false);
                            }
                        });
                    }
                }).start();
            }
        });
        Banner banner = new Banner(getContext());
        AbsListView.LayoutParams params = new AbsListView.LayoutParams(AbsListView.LayoutParams
                .MATCH_PARENT, 500);
        banner.setLayoutParams(params);
        banner.setImageLoader(new GlideImageLoader());
        banner.setBannerAnimation(Transformer.Accordion);
        List<String> images = new ArrayList<>();
        images.add("http://img3.duitang.com/uploads/item/201601/25/20160125154601_xFTZy.jpeg");
        images.add("http://h9.86.cc/walls/20141126/1440x900_c9a82e869685481.jpg");
        images.add("http://easyread.ph.126.net/WzUXr7fjp3hydZY-CcXxNQ==/7917059864082487443.jpg");
        images.add("http://www.005.tv/uploads/allimg/161116/1511331918-8.jpg");
        images.add("http://www.pp3.cn/uploads/201604/20160401004.jpg");
        banner.setImages(images);
        listview.addHeaderView(banner);
        banner.start();
        return layout;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Request<String> stringRequest = NoHttp.createStringRequest(Constant.BASE_URL + url);
        NoHttpInstences.getInstance().add(Constant.WHAT_NENS_REQUEST, stringRequest, new OnResponseListener<String>() {

            @Override
            public void onStart(int what) {

            }

            @Override
            public void onSucceed(int what, Response<String> response) {
                String result = response.get();
                NewBean newBean = JSON.parseObject(result, NewBean.class);
                datas = newBean.getResult().getData();
                adapter = new NewBenAdapter(datas, getContext());
                listview.setAdapter(adapter);

            }

            @Override
            public void onFailed(int what, Response<String> response) {
                if (BuildConfig.DEBUG)
                    Log.d("NewBenFragment", "错误:" + "response.getException():" + response
                            .getException());
            }

            @Override
            public void onFinish(int what) {

            }

        });
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent intent = new Intent(getActivity(), WabActivity.class);
                String url = datas.get(position - 1).getUrl();
                String title = datas.get(position - 1).getTitle();
                String img_url = datas.get(position - 1).getThumbnail_pic_s();
                intent.putExtra("url", url);
                intent.putExtra("title", title);
                intent.putExtra("img_url", img_url);


                String readedUrl = SpUtils.getStringData(getContext(), SpUtils.GEADED);
                if (!readedUrl.contains(datas.get(position).getUrl())) {
                    readedUrl = readedUrl + url + ",";
                }

                SpUtils.putStringData(getContext(), SpUtils.GEADED, readedUrl);
                adapter.notifyDataSetChanged();
                startActivity(intent);
            }
        });
        listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int position,
                                           long l) {

                final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("是否收藏本条新闻");
                String[] item=new String[]{"分享","收藏"};
                builder.setItems(item, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:

                                OnekeyShare oks = new OnekeyShare();
                                //关闭sso授权
                                oks.disableSSOWhenAuthorize();
                                // title标题，印象笔记、邮箱、信息、微信、人人网、QQ和QQ空间使用
                                oks.setTitle(datas.get(position-1).getTitle());
                                // titleUrl是标题的网络链接，仅在Linked-in,QQ和QQ空间使用
                                oks.setTitleUrl(datas.get(position-1).getUrl());
                                // text是分享文本，所有平台都需要这个字段
                                oks.setText("我是分享文本");
                                //分享网络图片，新浪微博分享网络图片需要通过审核后申请高级写入接口，否则请注释掉测试新浪微博
                                oks.setImageUrl(datas.get(position-1).getThumbnail_pic_s());
                                // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
                                //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
                                // url仅在微信（包括好友和朋友圈）中使用
                                oks.setUrl(datas.get(position-1).getUrl());
                                // comment是我对这条分享的评论，仅在人人网和QQ空间使用
                                oks.setComment("我是测试评论文本");
                                // site是分享此内容的网站名称，仅在QQ空间使用
                                oks.setSite("ShareSDK");
                                // siteUrl是分享此内容的网站地址，仅在QQ空间使用
                                oks.setSiteUrl(datas.get(position-1).getUrl());// 启动分享GUI
                                oks.show(getActivity());

                                break;
                            case 1:
                                NewBean.ResultBean.DataBean dataBean = adapter.datas.get(position - 1);

                                String url = dataBean.getUrl();
                                int indexStart = url.indexOf("mobile/");
                                int indexEnd = url.indexOf(".html");
                                String newIdStr = url.substring(indexStart + 7, indexEnd);
                                long newId = Long.parseLong(newIdStr);

                                CollectNewBean news = new CollectNewBean(newId, dataBean.getThumbnail_pic_s(),
                                        dataBean.getTitle(), dataBean.getDate(), dataBean.getUrl());
                                try {
                                    dao = MyNewsOrmliteOpenHelper.getInstances(getContext
                                            ()).getDao(CollectNewBean.class);
                                    dao.createIfNotExists(news);

                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }

                                break;
                        }
                    }
                });
                builder.show();





                return true;
            }

        });
    }


    @Override
    public void onResume() {
        super.onResume();

    }
}
