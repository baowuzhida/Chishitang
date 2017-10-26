package com.example.baowuzhida.chishitang;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.jude.rollviewpager.RollPagerView;
import com.jude.rollviewpager.hintview.ColorPointHintView;

import java.text.DecimalFormat;
import java.util.LinkedList;

import Bean.ProductBean;
import Bean.ShoppingCartBean;
import Link.CartDao;

/**
 * Created by hasee on 2017/8/5.
 */

public class ProductMainAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private LinkedList<ProductBean> mData;
    private Context mContext;
    private ImageView textProductImage;
    // 数据库操作类
    private CartDao cartDao;


    public ProductMainAdapter(LinkedList<ProductBean> mData, Context mContext) {
        this.mData = mData;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return mData.size()+1;
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

        inflater = LayoutInflater.from(mContext);
        cartDao = new CartDao(mContext);
        ViewHolder holder = null;
        if(position==0){
            if(convertView==null){
                convertView = inflater.inflate(R.layout.item_product_top, parent, false);
                holder = new ViewHolder();
                holder.product_pager = (RollPagerView)convertView.findViewById(R.id.roll_view_pager);
                holder.all_product = (TextView)convertView.findViewById(R.id.all_product);
                convertView.setTag(holder);
            }else{
                holder = (ViewHolder) convertView.getTag();
            }
            RollPagerView product_pager = (RollPagerView)convertView.findViewById(R.id.roll_view_pager);
            //设置播放时间间隔
            product_pager.setPlayDelay(2000);
            //设置透明度
            product_pager.setAnimationDurtion(500);
            //设置适配器
            product_pager.setAdapter(new RollpagerviewAdapter());
            //设置指示器（顺序依次）
            //自定义指示器图片
            //设置圆点指示器颜色
            //设置文字指示器
            //隐藏指示器
            //mRollViewPager.setHintView(new IconHintView(this, R.drawable.point_focus, R.drawable.point_normal));
            product_pager.setHintView(new ColorPointHintView(mContext, Color.YELLOW,Color.WHITE));

            holder.all_product.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.putExtra("page", 0);
                    intent.setClass(mContext, ProductAllActivity.class);
                    mContext.startActivity(intent);
                    Toast.makeText(mContext, "查看所有菜品 ", Toast.LENGTH_SHORT).show();
                }
            });
        }
        else {
            if (convertView == null) {
                //根据自定义的Item布局加载布局
                convertView = inflater.inflate(R.layout.product_list_item, parent, false);
                holder = new ViewHolder();

                holder.textProductImage = (ImageButton) convertView.findViewById(R.id.productlist_product_imageview);
                holder.textProductName = (TextView) convertView.findViewById(R.id.productlist_product_name);
                holder.textProductPrice = (TextView) convertView.findViewById(R.id.productlist_product_price);
                holder.textProductDetails = (TextView) convertView.findViewById(R.id.productlist_product_details);
                holder.textAdd = (Button) convertView.findViewById(R.id.productlist_product_add);
                convertView.setTag(holder);   //将Holder存储到convertView中
            } else {
                holder = (ViewHolder) convertView.getTag();
            }


            final String url = mData.get(position-1).getProduct_image();
            final String name = mData.get(position-1).getProduct_name();
            final Double price = mData.get(position-1).getProduct_price();
            final String details = mData.get(position-1).getProduct_details();
            final String address = mData.get(position-1).getProduct_address();
            final Integer productid = mData.get(position-1).getProduct_id();

            textProductImage = (ImageButton) convertView.findViewById(R.id.productlist_product_imageview);
            Glide.with(mContext)
                    .load(url)
                    .placeholder(R.drawable.eat)
                    .crossFade()
                    .into(textProductImage);

            holder.textProductName.setText("商品名称：" + mData.get(position-1).getProduct_name());
            holder.textProductPrice.setText("商品价格：¥" + new DecimalFormat("0.00").format(mData.get(position-1).getProduct_price()));
            holder.textProductDetails.setText("商品详情: " + mData.get(position-1).getProduct_details());
            holder.textAdd.setTag(position-1);
            //给Button添加单击事件  添加Button之后ListView将失去焦点  需要的直接把Button的焦点去掉
            holder.textAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ShoppingCartBean shoppingCartBean = cartDao.dbQueryOneByPID(productid);
                    if (shoppingCartBean != null) {
                        int count = shoppingCartBean.getCount();
                        cartDao.dbUpdateCart(productid, count + 1);
                    } else {
                        cartDao.dbInsert(productid, url, name, details, address, price, 1);
                    }
                    Toast.makeText(mContext, "添加菜品 " + name, Toast.LENGTH_SHORT).show();

                }
            });
            holder.textProductImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, ProductdetailActivity.class);
                    intent.putExtra("product_id", productid);
                    intent.putExtra("product_image", url);
                    intent.putExtra("product_name", name);
                    intent.putExtra("product_details", details);
                    intent.putExtra("product_address", address);
                    intent.putExtra("product_price", price);
                    mContext.startActivity(intent);
                    Toast.makeText(mContext, "查看菜品 " + name + "信息", Toast.LENGTH_SHORT).show();
                }
            });
        }
        return convertView;
    }


    private static class ViewHolder {
        ImageView textProductImage;
        TextView textProductName;
        TextView textProductPrice;
        TextView textProductDetails;
        Button textAdd;

        RollPagerView product_pager;
        TextView all_product;
    }

    public int getItemViewType(int position) {
        // TODO Auto-generated method stub
        return position>0?0:1;
    }
    @Override
    public int getViewTypeCount() {
        // TODO Auto-generated method stub
        return 2;
    }

    public void updatedata(LinkedList<ProductBean> mData){
        this.mData = mData;
        notifyDataSetChanged();
    }

}
