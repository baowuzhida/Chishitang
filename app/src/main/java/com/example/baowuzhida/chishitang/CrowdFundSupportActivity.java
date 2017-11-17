package com.example.baowuzhida.chishitang;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Toast;

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
import es.dmoral.toasty.Toasty;


/**
 * Created by Baowuzhida on 2017/11/6.
 */

public class CrowdFundSupportActivity extends AppCompatActivity {

    private HorizontalListView horizontalListView;
    private Toolbar toolbar;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crowdfund_support);

        initView();
        getList();

        getWindow().setSoftInputMode(WindowManager.LayoutParams.
                SOFT_INPUT_ADJUST_PAN);

        toolbar.setNavigationIcon(R.mipmap.ic_back);
        toolbar.setTitle("美食众筹");
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    public void initView(){
        toolbar = (Toolbar)findViewById(R.id.crowdfund_support_toolbar);
        horizontalListView = (HorizontalListView)findViewById(R.id.horizon_listview);
    }

    public void getList() {

        Handler crowdlisthandler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if(msg.obj==null)
                    return;
                final LinkedList<CrowdFundBean> linkedList=new LinkedList<>();
                JSONArray jsonArray;
                try {
                    jsonArray = new JSONArray((String) msg.obj);
                    for(int i = 0;i < jsonArray.length();i++) {
                        JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                        CrowdFundBean crowdFundBean=new CrowdFundBean();

                        crowdFundBean.setCrowdfund_id(jsonObject.getInt("crowdfund_id"));
                        crowdFundBean.setBuilder_id(jsonObject.getInt("builder_id"));
                        crowdFundBean.setCrowdfund_image(jsonObject.getString("crowdfund_image"));
                        crowdFundBean.setCrowdfund_name(jsonObject.getString("crowdfund_name"));
                        crowdFundBean.setCrowdfund_detail(jsonObject.getString("crowdfund_detail"));
                        crowdFundBean.setCrowdfund_declaration(jsonObject.getString("crowdfund_declaration"));
                        crowdFundBean.setState(jsonObject.getInt("state"));
                        crowdFundBean.setCrowdfund_capital(jsonObject.getDouble("crowdfund_capital"));
                        crowdFundBean.setCrowdfund_aimcapital(jsonObject.getDouble("crowdfund_aimcapital"));
                        crowdFundBean.setCrowdfund_supporters(jsonObject.getInt("crowdfund_supporters"));
                        crowdFundBean.setCrowdfund_type(jsonObject.getInt("crowdfund_type"));

                        linkedList.add(crowdFundBean);
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                if(linkedList.size()==0){
                    AlertDialog.Builder builder = new AlertDialog.Builder(CrowdFundSupportActivity.this);
                    builder.setTitle("Info");
                    builder.setMessage("暂无众筹信息");
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }

//                Collections.reverse(linkedList);//订单倒序使得最新下单最先展示
                CrowdFundSupportAdapter crowdFundSupportAdapter=new CrowdFundSupportAdapter(linkedList,CrowdFundSupportActivity.this);
                horizontalListView.setAdapter(crowdFundSupportAdapter);
            }
        };
        HttpUtil httpUtil = new HttpUtil();
        httpUtil.PostURL("http://119.23.205.112:8080/eatCanteen_war/CrowdfundServlet","type=list",crowdlisthandler);
//        httpUtil.PostURL("http://119.23.205.112:8080/eatCanteen_war/ClowFundServlet","clowname="+name+"clowimage="+image+"clowdetail="+detail+"clowdeclaration="+declaration+"&type=add",clowlisthandler);
    }
}
