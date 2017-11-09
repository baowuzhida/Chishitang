package com.example.baowuzhida.chishitang;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import Adapter.CrowdFundSupportAdapter;
import Bean.CrowdFundBean;
import Interpolator_extends.HorizontalListView;
import Link.HttpUtil;


/**
 * Created by Baowuzhida on 2017/11/6.
 */

public class CrowdFundSupportActivity extends AppCompatActivity {

    private HorizontalListView horizontalListView;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crowdfund_support);

        initView();
        horizontalListView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1,getData()));
//        setContentView(horizontalListView);
//        getList();
//        clowFundSupportAdapter = new ClowFundSupportAdapter(linkedList,ClowFundSupportActivity.this);
//        horizontalListView.setAdapter(clowFundSupportAdapter);

    }

    private List<String> getData(){

        List<String> data = new ArrayList<String>();
        data.add("测试数据1");
        data.add("测试数据2");
        data.add("测试数据3");
        data.add("测试数据4");

        return data;
    }

    public void initView(){
        horizontalListView = (HorizontalListView)findViewById(R.id.horizon_listview);
    }

    public void getList() {

        Handler clowlisthandler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                final LinkedList<CrowdFundBean> linkedList=new LinkedList<>();
                JSONArray jsonArray;
                try {
                    jsonArray = new JSONArray((String) msg.obj);
                    for(int i = 0;i < jsonArray.length();i++) {
                        JSONObject jsonObject = (JSONObject) jsonArray.get(i);
//                        OrdersBean ordersBean=new OrdersBean();
//                        ordersBean.setOrders_id(jsonObject.getInt("orders_id"));
//                        ordersBean.setUser_id(jsonObject.getInt("user_id"));
//                        ordersBean.setOrders_price(jsonObject.getDouble("orders_price"));
//                        ordersBean.setOrders_time(jsonObject.getString("orders_time"));
//                        linkedList.add(ordersBean);
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                Collections.reverse(linkedList);//订单倒序使得最新下单最先展示

                CrowdFundSupportAdapter crowdFundSupportAdapter=new CrowdFundSupportAdapter(linkedList,CrowdFundSupportActivity.this);

                horizontalListView.setAdapter(crowdFundSupportAdapter);
            }
        };
        HttpUtil httpUtil = new HttpUtil();
        httpUtil.PostURL("http://119.23.205.112:8080/eatCanteen_war/ClowFundServlet","type=list",clowlisthandler);
//        httpUtil.PostURL("http://119.23.205.112:8080/eatCanteen_war/ClowFundServlet","clowname="+name+"clowimage="+image+"clowdetail="+detail+"clowdeclaration="+declaration+"&type=add",clowlisthandler);
    }
}
