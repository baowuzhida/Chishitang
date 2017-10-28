package com.example.baowuzhida.chishitang;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.LinkedList;

import Adapter.EvaluateAdapter;
import Bean.UserBean;
import Bean.UserEvaluateBean;
import Link.HttpUtil;

/**
 * Created by Baowuzhida on 2017/9/15.
 */

public class VoteDetailActivity extends AppCompatActivity {

    private int favourite_product_id,vote_number;
    private String vote_url,vote_details,vote_name;
    private Toolbar toolbar;

    private ImageView vote_details_image;
    private TextView vote_details_name,vote_details_details,vote_details_votes;
    private Button vote_details_add;
    private ListView vote_evaluate_list;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vote_details);

        initView();

        toolbar = (Toolbar)findViewById(R.id.vote_details_toolbar);
        toolbar.setNavigationIcon(R.mipmap.ic_back);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPause();
                finish();
            }
        });

        vote_details_name.setText("商品名称："+vote_name);
        vote_details_votes.setText("票数："+vote_number);
        vote_details_details.setText("商品详情"+vote_details);

        Glide.with(VoteDetailActivity.this)
                .load(vote_url)
                .placeholder(R.drawable.eat)
                .crossFade()
                .into(vote_details_image);

        Handler handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg == null) {
                    return;
                }
                LinkedList<UserEvaluateBean> linkedList=new LinkedList<>();
                JSONArray jsonArray;
                try {
                    jsonArray = new JSONArray((String) msg.obj);
                    for(int i = 0;i < jsonArray.length();i++) {
                        JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                        UserEvaluateBean userEvaluate=new UserEvaluateBean();
                        userEvaluate.setProduct_id(jsonObject.getInt("product_id"));
                        userEvaluate.setUser_evaluate(jsonObject.getString("user_evaluate"));
                        userEvaluate.setUser_evaluate_id(jsonObject.getInt("user_evaluate_id"));
                        userEvaluate.setUser_id(jsonObject.getInt("user_id"));

                        UserBean userBean=new UserBean();
                        JSONObject jsonObject1=jsonObject.getJSONObject("userBean");
                        userBean.setUser_id(jsonObject1.getInt("user_id"));
                        userBean.setUser_name(jsonObject1.getString("user_name"));
                        userBean.setUser_headimage(jsonObject1.getString("user_headimage"));
                        userEvaluate.setUserBean(userBean);
                        linkedList.add(userEvaluate);
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                EvaluateAdapter evaluateAdapter=new EvaluateAdapter(linkedList,VoteDetailActivity.this);
                vote_evaluate_list.setAdapter(evaluateAdapter);
            }
        };

        HttpUtil httpUtil=new HttpUtil();
        httpUtil.PostURL("http://119.23.205.112:8080/eatCanteen_war/UserEvaluateServlect","type=Product_id&id="+favourite_product_id,handler);

        vote_details_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Handler vote_details_handler=new Handler(){
                    @Override
                    public void handleMessage(Message msg) {
                        super.handleMessage(msg);
                        String type=(String)msg.obj;
                        if(type.equals("ok")){
                            vote_number++;
                            vote_details_votes.setText("票数："+vote_number);
                            Toast.makeText(getApplicationContext(),"投票成功",Toast.LENGTH_SHORT).show();
                        }
                        else
                            Toast.makeText(getApplicationContext(),"投票失败",Toast.LENGTH_SHORT).show();
                    }
                };
                HttpUtil httpUtil=new HttpUtil();
                httpUtil.PostURL("http://119.23.205.112:8080/eatCanteen_war/FavoriteProductServlet","type=add&id="+favourite_product_id,vote_details_handler);
            }
        });
    }

    private void initView(){

        favourite_product_id = getIntent().getIntExtra("favourite_product_id",-1);
        vote_number = getIntent().getIntExtra("vote_number",-1);
        vote_url = getIntent().getStringExtra("vote_image");
        vote_details = getIntent().getStringExtra("vote_details");
        vote_name = getIntent().getStringExtra("vote_name");



        vote_details_image=(ImageView)findViewById(R.id.vote_details_image);
        vote_details_name=(TextView)findViewById(R.id.vote_details_name);
        vote_details_votes=(TextView)findViewById(R.id.vote_details_votes);
        vote_details_details=(TextView)findViewById(R.id.vote_details_details);
        vote_details_add=(Button)findViewById(R.id.vote_details_add);
        vote_evaluate_list=(ListView)findViewById(R.id.vote_evaluate_list);
    }

    protected void onPause(){
        super.onPause();
        VoteActivity.getObj().refreshList();
    }
}
