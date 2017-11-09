package com.example.baowuzhida.chishitang;

import android.app.TabActivity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import Adapter.EvaluateAdapter;
import Adapter.myPagerAdapter;
import Bean.ShoppingCartBean;
import Bean.UserBean;
import Bean.UserEvaluateBean;
import Link.CartDao;
import Link.HttpUtil;
import layout.fragment.Product2Fragment;

/**
 * Created by Baowuzhida on 2017/8/4.
 */

public class ProductdetailActivity extends AppCompatActivity implements View.OnClickListener{

    private LinearLayout Ldetails,Levaluates;
    private FloatingActionButton fab_shoppingcar;
    private Toolbar toolbar;
    private TextView show_details,show_evaluates,product_price,product_price_before,product_detail_detais;
    private ImageView product_detail_image;
    private Button add_into_shoppingcar;
    private ListView prouct_evaluate_list;

    private String url,name,details,address;
    private Double price ;
    private Integer productid ;
    private CartDao cartDao = new CartDao(ProductdetailActivity.this);

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        initView();

        toolbar = (Toolbar) findViewById(R.id.product_detail_toolbar);
        toolbar.setNavigationIcon(R.mipmap.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



        Glide.with(ProductdetailActivity.this)
                .load(url)
                .placeholder(R.drawable.eat)
                .crossFade()
                .into(product_detail_image);

        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing);
        collapsingToolbar.setTitle(name);
        collapsingToolbar.setExpandedTitleColor(Color.rgb(0, 0, 0));
        collapsingToolbar.setCollapsedTitleTextColor(Color.rgb(0, 0, 0));

        product_price.setText(String.valueOf("￥"+price));
        product_price_before.setText(String.valueOf("Y"+price*1.2));
        product_price_before.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        product_detail_detais.setText("商品详情："+details);

        add_into_shoppingcar.setOnClickListener(this);
        show_details.setOnClickListener(this);
        show_evaluates.setOnClickListener(this);
        fab_shoppingcar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentcart = new Intent(ProductdetailActivity.this, ShoppingCartActivity.class);
                startActivity(intentcart);
                finish();
            }
        });

        Handler product_detail_handler=new Handler(){
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
                    System.out.println(msg.obj+"                      AAA");
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

                EvaluateAdapter evaluateAdapter=new EvaluateAdapter(linkedList,ProductdetailActivity.this);
                prouct_evaluate_list.setAdapter(evaluateAdapter);
            }
        };

        HttpUtil httpUtil=new HttpUtil();
        httpUtil.PostURL("http://119.23.205.112:8080/eatCanteen_war/UserEvaluateServlect","type=Product_id&id="+productid,product_detail_handler);
    }

    private void initView(){

        Ldetails = (LinearLayout)findViewById(R.id.details);
        Levaluates = (LinearLayout)findViewById(R.id.evaluates);

        prouct_evaluate_list=(ListView)findViewById(R.id.prouct_evaluate_list);

        show_details=(TextView)findViewById(R.id.show_details);
        show_evaluates=(TextView)findViewById(R.id.show_evaluates);
        product_price=(TextView)findViewById(R.id.product_detail_price);
        product_price_before=(TextView)findViewById(R.id.product_detail_price_before);
        product_detail_detais=(TextView)findViewById(R.id.product_detail_details);

        product_detail_image = (ImageView)findViewById(R.id.product_detail_image);

        add_into_shoppingcar=(Button)findViewById(R.id.add_into_shoppingcar);

        fab_shoppingcar = (FloatingActionButton) findViewById(R.id.fab_shoppingcar);
        product_detail_image = (ImageView)findViewById(R.id.product_detail_image);

        productid = getIntent().getIntExtra("product_id",-1);
        url = getIntent().getStringExtra("product_image");
        name = getIntent().getStringExtra("product_name");
        details = getIntent().getStringExtra("product_details");
        address = getIntent().getStringExtra("product_address");
        price = getIntent().getDoubleExtra("product_price",-9.99);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.show_details:
                Ldetails.setVisibility(View.VISIBLE);
                Levaluates.setVisibility(View.GONE);
                break;
            case R.id.show_evaluates:
                Ldetails.setVisibility(View.GONE);
                Levaluates.setVisibility(View.VISIBLE);
                break;
            case R.id.add_into_shoppingcar:
                ShoppingCartBean shoppingCartBean = cartDao.dbQueryOneByPID(productid);
                if (shoppingCartBean != null) {
                    int count = shoppingCartBean.getCount();
                    cartDao.dbUpdateCart(productid, count + 1);
                } else {
                    cartDao.dbInsert(productid, url, name, details, address, price, 1);
                }
                Snackbar.make(v, "添加菜品 " + name, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                break;
        }
    }
}
