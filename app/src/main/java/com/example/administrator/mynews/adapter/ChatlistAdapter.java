package com.example.administrator.mynews.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.administrator.mynews.R;
import com.example.administrator.mynews.entity.MyMessage;

import java.util.List;

/**
 * Created by Administrator on 2016/12/23.
 */

public class ChatlistAdapter extends BaseAdapter {
    private List<MyMessage> datas;
    private Context context;

    public ChatlistAdapter(List<MyMessage> datas, Context context) {
        this.datas = datas;
        this.context = context;
    }

    public List<MyMessage> getDatas() {
        return datas;
    }

    public void setDatas(List<MyMessage> datas) {

        this.datas = datas;
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View converView, ViewGroup parent) {
        ChatViewHolder holder = null;
        if (getItemViewType(position) == 0) {

            if (converView == null) {
                holder = new ChatViewHolder();
                converView = LayoutInflater.from(context).inflate(R.layout
                        .item_from_message_chat_fragment_activity_main, parent,false);
                holder.tv_text = (TextView) converView.findViewById(R.id.tv_chatfrom_chat_fragment_activity_main);
                converView.setTag(holder);

            } else {
                holder = (ChatViewHolder) converView.getTag();
            }
            holder.tv_text.setText(datas.get(position).getText());

        } else if (getItemViewType(position) == 1) {

            if (converView == null) {
                holder = new ChatViewHolder();
                converView = LayoutInflater.from(context).inflate(R.layout
                        .item_to_message_chat_fragment_activity_main, parent,false);
                holder.tv_text = (TextView) converView.findViewById(R.id
                        .tv_chatto_chat_fragment_activity_main);
                converView.setTag(holder);
            } else {
                holder = (ChatViewHolder) converView.getTag();
            }
            holder.tv_text.setText(datas.get(position).getText());

        }
        return converView;
    }

    @Override
    public int getItemViewType(int position) {
        int type = 0;
        switch (datas.get(position).getType()) {
            case 0:
                return 0;
            case 1:
                return 1;
        }
        return 0;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    class ChatViewHolder {
        TextView tv_text;
    }
}
