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
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

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
            holder.imageView = (RoundedImageView) convertView.findViewById(R.id.crowdfund_roundedimage);
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

        String[] url = urlall.split("##");//以"##"为分隔符，截取上面的字符串。

        for (String anUrl : url) {
            System.out.println(anUrl);//循环输出结果
        }

        Glide.with(mContext)
                .load(url[0])
                .placeholder(R.drawable.eat)
                .crossFade()
                .into(holder.imageView);

        holder.name.setText(mData.get(position).getCrowdfund_name());
        holder.declaration.setText(mData.get(position).getCrowdfund_declaration());
        holder.money.setText("目前资金：" + mData.get(position).getCrowdfund_capital() + "/" + mData.get(position).getCrowdfund_aimcapital());
        return convertView;
    }

    private static class ViewHolder {
        RoundedImageView imageView;
        TextView name;
        TextView declaration;
        TextView money;
        TextView text1;
        Button btn_detail;
        ImageView btn_share,btn_message,btn_admire;
    }
}
