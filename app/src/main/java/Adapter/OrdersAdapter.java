package Adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.baowuzhida.chishitang.OrderDetailActivity;
import com.example.baowuzhida.chishitang.R;

import java.text.DecimalFormat;
import java.util.LinkedList;

import Bean.OrdersBean;
import Link.ZxingUtils;

/**
 * Created by hasee on 2017/8/5.
 */

public class OrdersAdapter extends BaseAdapter{

    private LinkedList<OrdersBean> mData;
    private Context mContext;
    private int itemCount = 3;

    public OrdersAdapter(LinkedList<OrdersBean> mData, Context mContext) {
        this.mData = mData;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        // 这里是关键
        // 如果数据数量大于3，只显示3条数据。这里数量自己定义。
        // 否则，显示全部数量。
        if (mData.size() > 3)
        {
            return itemCount;
        }else
        {
            return mData.size();
        }
    }

    public void addItemNum(int number)
    {
        itemCount = number;
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
            holder.textCode=(Button)convertView.findViewById(R.id.orderlist_orders_code);
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

        holder.textCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater factory = LayoutInflater.from(mContext);
                // 把activity_login中的控件定义在View中
                final View view = factory.inflate(R.layout.dialog_orders_code, null);
                // 将LoginActivity中的控件显示在对话框中
                final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setView(view);
                ImageView imagecode = (ImageView)view.findViewById(R.id.orders_code);
                AlertDialog dialog = builder.create();
                Bitmap bitmap = ZxingUtils.createBitmap(String.valueOf(id));
                imagecode.setImageBitmap(bitmap);
                Window window = dialog.getWindow();
                assert window != null;
                window.setGravity(Gravity.CENTER); // 此处可以设置dialog显示的位置
                dialog.show();
            }
        });
        return convertView;
    }

    private static class ViewHolder{
        TextView textOrdersID;
        TextView textUserID;
        TextView textPrice;
        TextView textTime;
        Button textLook,textCode;
    }

}
