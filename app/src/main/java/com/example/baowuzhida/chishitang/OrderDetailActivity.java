package com.example.baowuzhida.chishitang;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSS;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.LinkedList;

import Adapter.OrderDetailAdapter;
import Bean.OrdersDetailsBean;
import Bean.ProductBean;
import Link.HttpUtil;
import Link.InitOssClient;


/**
 * Created by Baowuzhida on 2017/9/4.
 */

public class OrderDetailActivity extends AppCompatActivity{

    private ListView order_detail_listview;
    private Toolbar toolbar;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        toolbar = (Toolbar)findViewById(R.id.order_details_toolbar);
        toolbar.setNavigationIcon(R.mipmap.ic_back);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        Intent intent = getIntent();
        final int id = intent.getIntExtra("ID", -1);

        getOrderDetail(id);

        final SwipeRefreshLayout orderdetailswipe = (SwipeRefreshLayout)findViewById(R.id.order_detail_swipe);
        orderdetailswipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getOrderDetail(id);
            }
        });
    }
    private void getOrderDetail(int id){
        Handler orderdetailhandler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                LinkedList linkedList=new LinkedList();
                JSONArray jsonArray;
                String ss=(String)msg.obj;
                String[] Data = ss.split("##");
                try {
                    jsonArray = new JSONArray(Data[0]);
                    for(int i = 0;i < jsonArray.length();i++) {
                        JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                        OrdersDetailsBean ordersDetailsBean=new OrdersDetailsBean();
                        ordersDetailsBean.setOrders_detail_id(jsonObject.getInt("orders_detail_id"));
                        ordersDetailsBean.setOrders_id(jsonObject.getInt("orders_id"));
                        ordersDetailsBean.setProduct_amount(jsonObject.getInt("product_amount"));
                        JSONObject json=jsonObject.getJSONObject("productBean");
                        ProductBean productBean=new ProductBean();
                        productBean.setProduct_id(json.getInt("product_id"));
                        productBean.setProduct_name(json.getString("product_name"));
                        productBean.setProduct_details(json.getString("product_details"));

                        final InitOssClient initOssClient = new InitOssClient();
                        String url= null;
                        OSS oss = initOssClient.getOss(getApplicationContext(),Data[1],Data[2],Data[3]);
                        String obk = json.getString("product_image");
                        try {
                            url = oss.presignConstrainedObjectURL("chishitang",obk,1000);
                        } catch (ClientException e) {
                            e.printStackTrace();
                        }

                        productBean.setProduct_image(url);
                        productBean.setProduct_price(json.getDouble("product_price"));
                        productBean.setProduct_type(json.getInt("product_type"));
                        productBean.setProduct_address(json.getString("product_address"));
                        ordersDetailsBean.setProductBean(productBean);

                        linkedList.add(ordersDetailsBean);
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                order_detail_listview = (ListView)findViewById(R.id.order_detail_listview);
                SwipeRefreshLayout orderswipe = (SwipeRefreshLayout)findViewById(R.id.order_detail_swipe);
                OrderDetailAdapter orderDetailAdapter = new OrderDetailAdapter(linkedList, OrderDetailActivity.this);
                order_detail_listview.setAdapter(orderDetailAdapter);
                orderswipe.setRefreshing(false);
            }
        };
        HttpUtil httpUtil = new HttpUtil();
        httpUtil.PostURL("http://119.23.205.112:8080/eatCanteen_war/OrdersServlet","type=details&id="+id,orderdetailhandler);
    }
}
