package com.example.administrator.mynews.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.administrator.mynews.R;
import com.example.administrator.mynews.base.MyBaseAdapter;
import com.example.administrator.mynews.entity.NewBean;
import com.example.administrator.mynews.utils.SpUtils;

import java.util.List;

/**
 * Created by Administrator on 2016/12/14.
 */

public class NewBenAdapter extends BaseAdapter {

    public List<NewBean.ResultBean.DataBean> datas;
    private Context context;

    public NewBenAdapter(List<NewBean.ResultBean.DataBean> datas, Context context) {
        this.datas = datas;
        this.context = context;
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
    public View getView(int position, View convertView, ViewGroup viewGroup) {

        View view = null;
        ViewHolder vh = null;
        if (convertView == null) {
            vh = new ViewHolder();
            view = View.inflate(context, R.layout.item_newben_fragment_activity_main, null);
            vh.iv_photo = (ImageView) view.findViewById(R.id.iv_photo_item_newben_fragment_activity_main);
            vh.tv_title = (TextView) view.findViewById(R.id.tv_title_item_newben_fragment_activity_main);
            vh.tv_date = (TextView) view.findViewById(R.id.tv_date_item_newben_fragment_activity_main);
            view.setTag(vh);

        } else {
            view = convertView;
            vh = (ViewHolder) view.getTag();

        }
        String readedUrl = SpUtils.getStringData(context, SpUtils.GEADED);


        if (readedUrl.contains(datas.get(position).getUrl())) {
            vh.tv_title.setTextColor(Color.GRAY);

        }else {
            vh.tv_title.setTextColor(Color.BLACK);
        }


        Glide.with(context).load(datas.get(position).getThumbnail_pic_s()).into(vh.iv_photo);
        vh.tv_title.setText(datas.get(position).getTitle());
        vh.tv_date.setText(datas.get(position).getDate());

        return view;
    }

    class ViewHolder {
        TextView tv_title;
        TextView tv_date;
        ImageView iv_photo;
    }
}
