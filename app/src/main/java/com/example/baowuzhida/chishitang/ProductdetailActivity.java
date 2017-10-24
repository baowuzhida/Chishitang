package com.example.baowuzhida.chishitang;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import Bean.ShoppingCartBean;
import Link.CartDao;

/**
 * Created by Baowuzhida on 2017/8/4.
 */

public class ProductdetailActivity extends AppCompatActivity {

    private String url ;
    private String name ;
    private Double price ;
    private String details ;
    private String address ;
    private Integer productid ;
    private CartDao cartDao = new CartDao(ProductdetailActivity.this);

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productdetail);

        productid = getIntent().getIntExtra("product_id",-1);
        url = getIntent().getStringExtra("product_image");
        name = getIntent().getStringExtra("product_name");
        details = getIntent().getStringExtra("product_details");
        address = getIntent().getStringExtra("product_address");
        price = getIntent().getDoubleExtra("product_price",-9.99);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TextView product_detail_price = (TextView)findViewById(R.id.product_detail_price);
        TextView product_detail_details = (TextView)findViewById(R.id.product_detail_details);
        ImageView product_detail_image = (ImageView)findViewById(R.id.product_detail_image);

        Glide.with(ProductdetailActivity.this)
                .load(url)
                .placeholder(R.drawable.eat)
                .crossFade()
                .into(product_detail_image);

        product_detail_price.setText("菜品价格： "+price);
        product_detail_details.setText("详情介绍： "+details);

        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        collapsingToolbar.setTitle(name);
        collapsingToolbar.setExpandedTitleColor(Color.rgb(0, 0, 0));
        collapsingToolbar.setCollapsedTitleTextColor(Color.rgb(0, 0, 0));

        FloatingActionButton fab_add = (FloatingActionButton) findViewById(R.id.product_detail_fab_add);
        FloatingActionButton fab_back = (FloatingActionButton) findViewById(R.id.product_detail_fab_back);
        fab_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShoppingCartBean shoppingCartBean = cartDao.dbQueryOneByPID(productid);
                if (shoppingCartBean != null) {
                    int count = shoppingCartBean.getCount();
                    cartDao.dbUpdateCart(productid, count + 1);
                } else {
                    cartDao.dbInsert(productid, url, name, details, address, price, 1);
                }
                Snackbar.make(view, "添加菜品 " + name, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        fab_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
