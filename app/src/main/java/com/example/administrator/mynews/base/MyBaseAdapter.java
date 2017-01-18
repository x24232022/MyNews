package com.example.administrator.mynews.base;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * Created by Administrator on 2016/12/14.
 */

public class MyBaseAdapter<DataType> extends BaseAdapter{

    private Context context;
    private List<DataType> datas;
    private LayoutInflater inflater;

    public List<DataType> getDatas() {
        return datas;
    }

    public void setDatas(List<DataType> datas) {
        if(datas!=null){
            this.datas.clear();
            this.datas.addAll(datas);
        }else {
            this.datas.clear();
        }
        this.datas = datas;
    }

    public MyBaseAdapter(Context context, List<DataType> datas) {
        this.context = context;
        this.datas = datas;
        this.inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return (datas==null)?0:datas.size();
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
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        return null;
    }
}
