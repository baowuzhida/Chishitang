package com.example.baowuzhida.chishitang;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSS;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedList;

import Adapter.DynamicItemAdapter;
import Bean.DynamicBean;
import Bean.UserBean;
import Link.HttpUtil;
import Link.InitOssClient;
import es.dmoral.toasty.Toasty;

/**
 * Created by hasee on 2017/11/14.
 */

public class DateMealActivity extends AppCompatActivity implements View.OnClickListener {
    ListView mlistview;
    ImageButton mbutton;
    private Toolbar toolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datemeal);
        mlistview=(ListView)findViewById(R.id.dynamic_list);
        mbutton=(ImageButton)findViewById(R.id.add_dynamic);
        mbutton.setOnClickListener(this);
        toolbar=(Toolbar)findViewById(R.id.dynamic_toolbar);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.
                SOFT_INPUT_ADJUST_PAN);
        toolbar.setNavigationIcon(R.mipmap.ic_back);
        toolbar.setTitle("约餐");
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mlistview.setFocusable(false);//让listview失去焦点，解决第一时间显示的是listview视图

        ViewTrends();
    }

    public void ViewTrends(){
        Handler dynamichandler = new Handler() {
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                String type = (String) msg.obj;
                if (msg.obj == null) {
                    type = "connfail";
                }
                else if(type.equals("no"))
                        Toasty.error(getApplicationContext(), "请登录", Toast.LENGTH_SHORT, true).show();
                else if(type.equals("connfail"))
                        Toasty.error(getApplicationContext(), "连接超时", Toast.LENGTH_SHORT, true).show();
                else
                {
                    ArrayList list=new ArrayList();
                    String ss=(String)msg.obj;
                    String[] Data = ss.split("##");
                    try{
                        JSONArray jsonArray=new JSONArray((String)msg.obj);
                        for (int i=0;i<jsonArray.length();i++)
                        {
                            JSONObject jsonObject=(JSONObject)jsonArray.get(i);
                            DynamicBean dynamicBean=new DynamicBean();
                            dynamicBean.setD_id(jsonObject.getInt("d_id"));
                            dynamicBean.setD_content(jsonObject.getString("d_content"));
                            dynamicBean.setD_image(jsonObject.getString("d_image"));
                            dynamicBean.setD_url(jsonObject.getString("d_url"));
                            dynamicBean.setD_time(jsonObject.getString("d_time"));
                            JSONObject jsonObject1=jsonObject.getJSONObject("userBean");

                            final InitOssClient initOssClient = new InitOssClient();
                            String url= null;
                            OSS oss = initOssClient.getOss(getApplicationContext(),Data[Data.length-3],Data[Data.length-2],Data[Data.length-1]);
                            String obk = jsonObject1.getString("user_headimage");
                            try {
                                url = oss.presignConstrainedObjectURL("chishitang",obk,1000);
                            } catch (ClientException e) {
                                e.printStackTrace();
                            }

                            dynamicBean.getUserBean().setUser_headimage(url);
                            dynamicBean.getUserBean().setUser_name(jsonObject1.getString("user_name"));
                            dynamicBean.getUserBean().setUser_id(jsonObject1.getInt("user_id"));



                            list.add(dynamicBean);
                        }
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                    DynamicItemAdapter dynamicItemAdapter=new DynamicItemAdapter(DateMealActivity.this,list);
                    mlistview.setAdapter(dynamicItemAdapter);
                    setListViewHeightBasedOnChildren(mlistview);
                }
            }
        };
        HttpUtil httpUtil = new HttpUtil();
        httpUtil.PostURL("http://119.23.205.112:8080/eatCanteen_war/DynamicServlect",
                "type=list",dynamichandler);
    }


    public void setListViewHeightBasedOnChildren(ListView listView) {
        // 获取ListView对应的Adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) {
            // listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, listView);
            // 计算子项View 的宽高
            listItem.measure(0, 0);
            // 统计所有子项的总高度
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        // listView.getDividerHeight()获取子项间分隔符占用的高度
        // params.height最后得到整个ListView完整显示需要的高度
        listView.setLayoutParams(params);
    }
    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.add_dynamic)
        {
            Intent intent = new Intent(DateMealActivity.this, DynamicJoinActivity.class);
            startActivity(intent);
        }
    }
}
