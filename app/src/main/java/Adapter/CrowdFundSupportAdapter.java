package Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.baowuzhida.chishitang.R;

import java.util.LinkedList;

import Bean.CrowdFundBean;

/**
 * Created by Baowuzhida on 2017/11/7.
 */

public class CrowdFundSupportAdapter extends BaseAdapter {

    private LinkedList<CrowdFundBean> mData;
    private Context mContext;

    public CrowdFundSupportAdapter(LinkedList<CrowdFundBean> mData, Context mContext) {
        this.mData = mData;
        this.mContext = mContext;
    }
    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CrowdFundSupportAdapter.ViewHolder holder;
        if (convertView == null) {
//            holder = new CrowdFundSupportAdapter.ViewHolder();
            //根据自定义的Item布局加载布局
            convertView = LayoutInflater.from(mContext).inflate(R.layout.crowdfund_list_item, parent, false);
            holder = new CrowdFundSupportAdapter.ViewHolder();
            holder.imageView = (ImageButton) convertView.findViewById(R.id.crowdfund_roundedmage);
            holder.name = (TextView) convertView.findViewById(R.id.crowdfund_name);
            holder.declaration = (TextView) convertView.findViewById(R.id.crowdfund_declaration);
            holder.money = (TextView) convertView.findViewById(R.id.crowdfund_money);
            holder.btn_detail = (Button) convertView.findViewById(R.id.btn_detail);
            holder.btn_share = (ImageView) convertView.findViewById(R.id.btn_share);
            holder.btn_message = (ImageView) convertView.findViewById(R.id.btn_message);
            holder.btn_admire = (ImageView) convertView.findViewById(R.id.btn_admire);
            convertView.setTag(holder);   //将Holder存储到convertView中
        } else {
            holder = (CrowdFundSupportAdapter.ViewHolder) convertView.getTag();
        }
        final String urlall = mData.get(position).getCrowdfund_image();

        String[] url=urlall.split("##");//以"##"为分隔符，截取上面的字符串。

        for(int i=0;i<url.length;i++){
            System.out.println(url[i]);//循环输出结果
        }

        Glide.with(mContext)
                .load(url[0])
                .placeholder(R.drawable.eat)
                .crossFade()
                .into(holder.imageView);

        return convertView;
    }

    private static class ViewHolder {
        ImageView imageView;
        TextView name;
        TextView declaration;
        TextView money;
        Button btn_detail;
        ImageView btn_share,btn_message,btn_admire;
    }
}
