package Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.baowuzhida.chishitang.R;

import java.util.LinkedList;


import Bean.UserEvaluateBean;

/**
 * Created by hasee on 2017/9/24.
 */

public class EvaluateAdapter extends BaseAdapter {

    private LinkedList<UserEvaluateBean> mData;
    private Context mContext;


    public EvaluateAdapter(LinkedList<UserEvaluateBean> mData, Context mContext) {
        this.mData = mData;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        if (convertView == null) {
//            holder = new ViewHolder();
            //根据自定义的Item布局加载布局
            convertView = LayoutInflater.from(mContext).inflate(R.layout.evaluate_list_item, parent, false);
            holder = new ViewHolder();
            holder.imageView = (ImageButton) convertView.findViewById(R.id.Ib_evaluate);
            holder.name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.evaluate = (TextView) convertView.findViewById(R.id.tv_evaluate);
            convertView.setTag(holder);   //将Holder存储到convertView中
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final String url = mData.get(position).getUserBean().getUser_headimage();
        Glide.with(mContext)
                .load(url)
                .placeholder(R.drawable.eat)
                .crossFade()
                .into(holder.imageView);
        holder.name.setText(mData.get(position).getUserBean().getUser_name());
        holder.evaluate.setText(mData.get(position).getUser_evaluate());
        return convertView;
    }

    private static class ViewHolder {
        ImageView imageView;
        TextView name;
        TextView evaluate;
    }
}
