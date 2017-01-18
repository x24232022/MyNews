package com.example.administrator.mynews.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.example.administrator.mynews.R;
import com.example.administrator.mynews.adapter.MyRecyclerViewAdapter;
import com.example.administrator.mynews.entity.NewBean;
import com.example.administrator.mynews.utils.Constant;
import com.example.administrator.mynews.utils.NoHttpInstences;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.rest.OnResponseListener;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.Response;

/**
 * Created by Administrator on 2016/12/14.
 */
public class ImageFragment extends Fragment {
    private RecyclerView recyclerView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.layout_image_fragment_activity_main,
                container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.rv_image_fragment_activity_main);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        Request<String> stringRequest = NoHttp.createStringRequest(Constant.BASE_URL + Constant.SHISHANG_NEWS_QUERY_STRING);
        NoHttpInstences.getInstance().add(0, stringRequest, new OnResponseListener<String>() {
            @Override
            public void onStart(int what) {
            }
            @Override
            public void onSucceed(int what, Response<String> response) {
                String jsonStr = response.get();
                NewBean newBean = JSON.parseObject(jsonStr, NewBean.class);
                final MyRecyclerViewAdapter adapter = new MyRecyclerViewAdapter(newBean.getResult()
                        .getData(), getContext());
                adapter.setOnRecyclerviewItemClickListener(new MyRecyclerViewAdapter.OnRecyclerviewItemClickListener() {
                    @Override
                    public void onItemClick(View v, int position) {
                        Intent intent=new Intent(getContext(),WabActivity.class);
                        String url=adapter.getDatas().get(position).getUrl();
                        intent.putExtra("url",url);
                        String img_url=adapter.getDatas().get(position).getThumbnail_pic_s();
                        intent.putExtra("img_url",img_url);
                        startActivity(intent);
                    }
                });
                recyclerView.setAdapter(adapter);
            }
            @Override
            public void onFailed(int what, Response<String> response) {
            }
            @Override
            public void onFinish(int what) {
            }
        });
        return view;
    }
}
