package com.example.baowuzhida.chishitang;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import java.text.DecimalFormat;
import java.util.LinkedList;

import Bean.OrdersBean;

/**
 * Created by hasee on 2017/8/5.
 */

public class OrdersAdapter extends BaseAdapter{

    private LinkedList<OrdersBean> mData;
    private Context mContext;

    public OrdersAdapter(LinkedList<OrdersBean> mData, Context mContext) {
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView == null)
        {
            holder = new ViewHolder();
            //根据自定义的Item布局加载布局
            convertView = LayoutInflater.from(mContext).inflate(R.layout.order_list_item,parent,false);
            holder = new ViewHolder();
            holder.textOrdersID=(TextView)convertView.findViewById(R.id.orderlist_orders_id);
            holder.textUserID=(TextView)convertView.findViewById(R.id.orderlist_users_id);
            holder.textPrice=(TextView)convertView.findViewById(R.id.orderlist_orders_price);
            holder.textTime=(TextView)convertView.findViewById(R.id.orderlist_orders_time);
            holder.textLook=(Button)convertView.findViewById(R.id.orderlist_orders_detail);
            convertView.setTag(holder);   //将Holder存储到convertView中
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }

        final int id = mData.get(position).getOrders_id();

        holder.textOrdersID.setText("订单ID："+mData.get(position).getOrders_id());
        holder.textUserID.setText("用户ID："+mData.get(position).getUser_id());
        holder.textPrice.setText("订单金额：¥"+new DecimalFormat("0.00").format(mData.get(position).getOrders_price()));
        holder.textTime.setText("时间："+mData.get(position).getOrders_time());
        holder.textLook.setTag(position);
        //给Button添加单击事件  添加Button之后ListView将失去焦点  需要的直接把Button的焦点去掉
        holder.textLook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                intent.putExtra("ID", id);
                intent.setClass(mContext, OrderDetailActivity.class);
                mContext.startActivity(intent);

                Toast.makeText(mContext,"查看订单"+id, Toast.LENGTH_SHORT).show();
            }
        });
        return convertView;
    }

    private static class ViewHolder{
        TextView textOrdersID;
        TextView textUserID;
        TextView textPrice;
        TextView textTime;
        Button textLook;
    }
}
