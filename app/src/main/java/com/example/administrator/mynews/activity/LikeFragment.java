package com.example.administrator.mynews.activity;

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
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.administrator.mynews.BuildConfig;
import com.example.administrator.mynews.R;
import com.example.administrator.mynews.adapter.LikeBenAdapter;
import com.example.administrator.mynews.adapter.NewBenAdapter;
import com.example.administrator.mynews.db.MyNewsOrmliteOpenHelper;
import com.example.administrator.mynews.entity.CollectNewBean;
import com.example.administrator.mynews.entity.NewBean;
import com.example.administrator.mynews.utils.Constant;
import com.example.administrator.mynews.utils.NoHttpInstences;
import com.example.administrator.mynews.utils.SpUtils;
import com.j256.ormlite.dao.Dao;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.rest.OnResponseListener;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.Response;

import org.xutils.DbManager;
import org.xutils.x;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/12/14.
 */
public class LikeFragment extends Fragment {

    private SwipeRefreshLayout layout;
    private LikeBenAdapter adapter;
    private ListView lv;
    private List<CollectNewBean> collectNewBeanList;
    private Dao<CollectNewBean, Long> dao;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        layout = (SwipeRefreshLayout) LayoutInflater.from(getContext()).inflate(R
                .layout
                .listview_newben_fragment_activity, null);
        lv = (ListView) layout.findViewById(R.id.listview_news_fragment_activity_main);

        try {
            dao = MyNewsOrmliteOpenHelper.getInstances(getContext
                    ()).getDao(CollectNewBean.class);
            collectNewBeanList = dao.queryForAll();
            adapter = new LikeBenAdapter(collectNewBeanList, getContext());
            adapter.notifyDataSetChanged();
            lv.setAdapter(adapter);

        } catch (SQLException e) {
            e.printStackTrace();
        }



    return layout;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onResume() {
        super.onResume();

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent intent = new Intent(getActivity(), WabActivity.class);
                String url = collectNewBeanList.get(position).getUrl();
                String title = collectNewBeanList.get(position).getTitle();
                String img_url = collectNewBeanList.get(position).getImg();
                intent.putExtra("url", url);
                intent.putExtra("title", title);
                intent.putExtra("img_url", img_url);
                startActivity(intent);
            }
        });
        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int position,
                                           long l) {
                AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
                builder.setTitle("是否删除");
                builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        try {
                            Dao<CollectNewBean, ?> dao = MyNewsOrmliteOpenHelper.getInstances(getContext
                                    ()).getDao(CollectNewBean.class);
                            dao.delete(collectNewBeanList.get(position));
                            collectNewBeanList.remove(position);
                            adapter.notifyDataSetChanged();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                });
                builder.setNegativeButton("否", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).show();


                return true;
            }
        });
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);


    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        //判断fragment显示和隐藏的状态,如果fragment为非隐藏的状态,执行刷新集合
        if (!hidden){

            try {
                dao = MyNewsOrmliteOpenHelper.getInstances(getContext
                        ()).getDao(CollectNewBean.class);
                collectNewBeanList = dao.queryForAll();
                //将数据库中最新的数据集合刷新赋值给适配器,实现界面的动态刷新;
                adapter.datas = collectNewBeanList;
                adapter.notifyDataSetChanged();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        Toast.makeText(getContext(), "hidden:" + hidden, Toast.LENGTH_SHORT).show();
        }
    }

}
