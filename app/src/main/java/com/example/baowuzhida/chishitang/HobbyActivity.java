package com.example.baowuzhida.chishitang;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PointF;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;


import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import Adapter.myPagerAdapter;
import Bean.UserHobbyBean;
import Link.HttpUtil;
import layout.fragment.ColumnChartFragment;
import layout.fragment.PieChartFragment;
import layout.fragment.Product3Fragment;


/**
 * Created by Baowuzhida on 2017/11/14.
 */

public class HobbyActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ViewPager vp;
    private TabLayout.Tab one,two,three;
    private UserHobbyBean userHobbyBean = new UserHobbyBean();


    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hobby);

        initView();
        toolbar.setNavigationIcon(R.mipmap.ic_back);
        toolbar.setTitle("用户喜好");
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


//        getData();

        loadingViewPager();
        Intent intent = getIntent();
        int page = intent.getIntExtra("page", -1);
        vp.setCurrentItem(page);


    }

    private void  initView(){
        vp = (ViewPager)findViewById(R.id.chart_pager);
        toolbar = (Toolbar)findViewById(R.id.alllike_toolbar);
    }

    private void loadingViewPager() {

        //使用适配器将ViewPager与Fragment绑定在一起

        List<Fragment> fragmentList = new ArrayList<Fragment>();
        fragmentList.add(new PieChartFragment());
        fragmentList.add(new ColumnChartFragment());
        fragmentList.add(new Product3Fragment());
        List<String> mTitles = new ArrayList<>();
        mTitles.add("饼状图");
        mTitles.add("组合图");
        mTitles.add("推荐");
        vp.setAdapter(new myPagerAdapter(getSupportFragmentManager(), fragmentList, mTitles));

        //将TabLayout与ViewPager绑定在一起
        TabLayout mTabLayout = (TabLayout) findViewById(R.id.chart_tab);
        mTabLayout.setupWithViewPager(vp);

        //指定Tab的位置
        one = mTabLayout.getTabAt(0);
        two = mTabLayout.getTabAt(1);
        three = mTabLayout.getTabAt(2);

    }




    private void getData(){

        Handler handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
//                if(msg.obj==null)
//                    return;
                final LinkedList<UserHobbyBean> linkedList=new LinkedList<>();
                JSONArray jsonArray;
//                try {
//                    jsonArray = new JSONArray((String) msg.obj);
//                    for(int i = 0;i < jsonArray.length();i++) {
//                        JSONObject jsonObject = (JSONObject) jsonArray.get(i);
//
//                        userHobbyBean.setUb_id(jsonObject.getInt("ub_id"));
//                        userHobbyBean.setUser_id(jsonObject.getInt("user_id"));
//                        userHobbyBean.setUb_grain(jsonObject.getInt("ub_grain"));
//                        userHobbyBean.setUb_beef(jsonObject.getInt("ub_beef"));
//                        userHobbyBean.setUb_vegetables(jsonObject.getInt("ub_vegetables"));
//                        userHobbyBean.setUb_beans(jsonObject.getInt("ub_beans"));
//                        userHobbyBean.setUb_fat(jsonObject.getInt("ub_fat"));
//
//                        linkedList.add(userHobbyBean);
//                    }
//                }
//                catch (Exception e)
//                {
//                    e.printStackTrace();
//                }

                userHobbyBean.setUb_id(1);
                userHobbyBean.setUser_id(1);
                userHobbyBean.setUb_grain(2);
                userHobbyBean.setUb_beef(3);
                userHobbyBean.setUb_vegetables(5);
                userHobbyBean.setUb_beans(2);
                userHobbyBean.setUb_fat(9);


//                BarChart barChart = (BarChart) view.findViewById(R.id.barChart);
//                int[] type = {userHobbyBean.getUb_grain(),userHobbyBean.getUb_beef(),userHobbyBean.getUb_vegetables(),userHobbyBean.getUb_beans(),userHobbyBean.getUb_fat()};
//
//                int[] t = {12,32,44,19,24};
//                for (int i = 0; i < 5; i++) {
//                    mPointFList.add(new PointF(i+1, t[i]));
//                }
//                mBarData.setValue(mPointFList);
//                mBarData.setColor(Color.CYAN);
//                mBarData.setPaintWidth(pxTodp(5));
//                mBarData.setTextSize(pxTodp(10));
//                mDataList.add(mBarData);
//




//                barChart.setDataList(mDataList);
//                barChart.setXAxisUnit("食物种类");
//                barChart.setYAxisUnit("点单次数");

//                linkedList.add(userHobbyBean);
//                initData(userHobbyBean);
            }
        };
        HttpUtil httpUtil = new HttpUtil();
        httpUtil.PostURL("http://119.23.205.112:8080/eatCanteen_war/LoginServlet","type=list",handler);
    }



}
