package com.example.baowuzhida.chishitang;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.BounceInterpolator;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import java.text.DecimalFormat;
import java.util.LinkedList;

import Bean.OrdersDetailsBean;
import Bean.ProductBean;
import Link.HttpUtil;

/**
 * Created by hasee on 2017/8/13.
 */

public class OrderDetailAdapter extends BaseAdapter {

    private LinkedList<OrdersDetailsBean> mData;
    private Context mContext;
    private String url;
    private Handler handler;
    private ImageView product_image;

    public OrderDetailAdapter(LinkedList<OrdersDetailsBean> mData, Context mContext) {
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
            //根据自定义的Item布局加载布局
            convertView = LayoutInflater.from(mContext).inflate(R.layout.order_detail_list,parent,false);
            holder = new ViewHolder();
            holder.product_name=(TextView)convertView.findViewById(R.id.product_name);
            holder.product_price=(TextView)convertView.findViewById(R.id.product_price);
//            holder.product_detail=(TextView)convertView.findViewById(R.id.product_details);
            holder.product_amount=(TextView)convertView.findViewById(R.id.product_amount);
            holder.product_ourprice=(TextView)convertView.findViewById(R.id.product_ourprice);
            holder.evaluate_product=(Button)convertView.findViewById(R.id.order_hint_order_evaluate);
            convertView.setTag(holder);   //将Holder存储到convertView中
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }

        url = mData.get(position).getProductBean().getProduct_image();

        product_image = (ImageView) convertView.findViewById(R.id.product_imageview);
        Glide.with(mContext)
                .load(url)
                .placeholder(R.drawable.eat)
                .crossFade()
                .into(product_image);

        final int id = mData.get(position).getOrders_id();
        final ProductBean productBean=mData.get(position).getProductBean();
        holder.product_name.setText("商品名称："+productBean.getProduct_name());
        holder.product_price.setText("商品价格：¥"+new DecimalFormat(".00").format(productBean.getProduct_price()));
//        holder.product_detail.setText("商品详情："+productBean.getProduct_details());
        holder.product_amount.setText("商品总数：x"+mData.get(position).getProduct_amount());
        holder.product_ourprice.setText("商品总价：¥"+new DecimalFormat(".00").format(mData.get(position).getProduct_amount()*productBean.getProduct_price()));
        holder.evaluate_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "评价菜品"+productBean.getProduct_name(), Toast.LENGTH_SHORT).show();

                LayoutInflater factory = LayoutInflater.from(mContext);
                // 把activity_login中的控件定义在View中
                final View view = factory.inflate(R.layout.windows_evaluate_input, null);
                // 将LoginActivity中的控件显示在对话框中
                final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setView(view);
                final EditText text_evaluare = (EditText)view.findViewById(R.id.text_evaluate);
                Button btn_back = (Button)view.findViewById(R.id.btn_back) ;
                Button btn_save_evaluate=(Button)view.findViewById(R.id.btn_save_evaluate);
                AlertDialog dialog = builder.create();
                final AlertDialog finalDialog = dialog;



                btn_back.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finalDialog.dismiss();
                    }
                });
                btn_save_evaluate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        android.app.AlertDialog alert = new android.app.AlertDialog.Builder(mContext).create();
                        alert.setTitle("操作提示");
                        alert.setMessage("您确定要这么评价吗？");
                        alert.setButton(DialogInterface.BUTTON_NEGATIVE, "取消",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        return;
                                    }
                                });
                        alert.setButton(DialogInterface.BUTTON_POSITIVE, "确定",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        System.out.println("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa +                          "+text_evaluare.getText());
                                        HttpUtil httpUtil = new HttpUtil();
                                        httpUtil.PostURL("http://119.23.205.112:8080/eatCanteen_war/UserEvaluateServlect","evaluate="+text_evaluare.getText()+"&product_id="+productBean.getProduct_id()+"&type=add",evaluareHandler);
                                        finalDialog.dismiss();

                                    }
                                });
                        alert.show();
                        //保存
                    }
                });

                Window window = dialog.getWindow();
                assert window != null;
                window.setGravity(Gravity.CENTER); // 此处可以设置dialog显示的位置
                dialog.show();
            }
        });
        return convertView;
    }

    private static class ViewHolder{
        Button evaluate_product;
        TextView product_name;
        TextView product_price;
        TextView product_detail;
        TextView product_amount;
        TextView product_ourprice;

    }
    Handler evaluareHandler = new Handler(){
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String type=(String)msg.obj;
            if(msg.obj==null){
                type = "connfail";
            }
            switch (type) {
                case "error":
                    Toast.makeText(mContext, "评价失败", Toast.LENGTH_SHORT).show();
                    break;
                case "connfail":
                    Toast.makeText(mContext, "连接超时", Toast.LENGTH_SHORT).show();
                    break;
                case "ok":
                    Toast.makeText(mContext, "评价成功", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

}


