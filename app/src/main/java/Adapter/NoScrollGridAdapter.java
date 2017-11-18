package Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.baowuzhida.chishitang.R;

import java.util.ArrayList;



/**
 * Created by hasee on 2017/11/13.
 */

public class NoScrollGridAdapter extends BaseAdapter {

    private ArrayList<String> mData;
    private Context mContext;

    public NoScrollGridAdapter(Context mContext,ArrayList<String> mData){
        this.mContext=mContext;
        this.mData=mData;
    }

    @Override
    public int getCount() {
        return mData == null ? 0 : mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView == null) {
            //根据自定义的Item布局加载布局
            convertView = LayoutInflater.from(mContext).inflate(R.layout.dynamic_image_list, parent, false);
            holder = new ViewHolder();
            holder.imageView=(ImageView)convertView.findViewById(R.id.dynamic_image);
            convertView.setTag(holder);   //将Holder存储到convertView中
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }
        // 使用ImageLoader加载网络图片
        Glide.with(mContext)
                .load(mData.get(position))
                .placeholder(R.drawable.eat)
                .crossFade()
                .into(holder.imageView);
        return convertView;
    }
    private static class ViewHolder {
        ImageView imageView;
    }
}
