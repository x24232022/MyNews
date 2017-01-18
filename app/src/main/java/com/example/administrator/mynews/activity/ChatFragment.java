package com.example.administrator.mynews.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.JsonReader;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.example.administrator.mynews.R;
import com.example.administrator.mynews.adapter.ChatlistAdapter;
import com.example.administrator.mynews.entity.ChatBean;
import com.example.administrator.mynews.entity.CollectNewBean;
import com.example.administrator.mynews.entity.MyMessage;
import com.example.administrator.mynews.utils.Constant;
import com.example.administrator.mynews.utils.NoHttpInstences;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.rest.OnResponseListener;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.Response;

import java.util.ArrayList;
import java.util.List;

import static com.alibaba.fastjson.JSON.*;

/**
 * Created by Administrator on 2016/12/14.
 */
public class ChatFragment extends Fragment {
    private Button mBtn_submit;
    private EditText et_text;
    private List<MyMessage> datas;
    private ChatlistAdapter adapter;
    private ListView lv;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.layout_chat_fragment_activity_main, container, false);
        lv = (ListView) view.findViewById(R.id.lv_chat_fragment_activity_main);
        mBtn_submit = (Button) view.findViewById(R.id.btn_chat_fragment_activity);
        et_text = (EditText) view.findViewById(R.id.ed_text_chat_fragment_activity);
        datas = new ArrayList<>();
        adapter = new ChatlistAdapter(datas, getContext());
        lv.setAdapter(adapter);
        //设置到集合底部
        lv.setSelection(datas.size());

        lv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                InputMethodManager imm= (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                boolean b = imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                return b;
            }
        });
        lv.setTranscriptMode(ListView.TRANSCRIPT_MODE_NORMAL);


        return view;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mBtn_submit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String text = et_text.getText().toString();
                MyMessage myMessage = new MyMessage(1, text);
                datas.add(myMessage);
                adapter.setDatas(datas);
                adapter.notifyDataSetChanged();
                lv.setSelection(datas.size());
                et_text.setText("");
                final Request<String> request = NoHttp.createStringRequest(Constant.CHAT_URL + text + Constant.QUERY);
                NoHttpInstences.getInstance().add(0, request, new OnResponseListener<String>() {
                    @Override
                    public void onStart(int what) {

                    }

                    @Override
                    public void onSucceed(int what, Response<String> response) {
                        String result = response.get();
                        if (request != null) {

                            ChatBean chatBean = JSON.parseObject(result, ChatBean
                                    .class);
                            if (chatBean.getResult() != null) {
                                String text1 = chatBean.getResult().getText();
                                MyMessage myMessage1 = new MyMessage(0, text1);
                                datas.add(myMessage1);
                                adapter.setDatas(datas);
                                adapter.notifyDataSetChanged();
                                lv.setSelection(datas.size());
                            }

                        }
                    }

                    @Override
                    public void onFailed(int what, Response<String> response) {

                    }

                    @Override
                    public void onFinish(int what) {

                    }
                });

            }
        });
    }
}
