package com.example.baowuzhida.chishitang;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import Adapter.myPagerAdapter;
import Link.CartDao;
import layout.fragment.Product2Fragment;

/**
 * Created by Baowuzhida on 2017/8/4.
 */

public class ProductdetailActivity extends AppCompatActivity implements View.OnClickListener{

    private LinearLayout Ldetails,Levaluates;
    private FloatingActionButton fab_shoppingcar;
    private Toolbar toolbar;
    private TextView show_details,show_evaluates,product_price,product_price_before;
    private ImageView product_detail_image;
    private String url ;
    private String name ;

    private Double price ;
    private String details ;
    private String address ;
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
                Toast.makeText(ProductdetailActivity.this, "!!!!!!!!!!!", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        ImageView product_detail_image = (ImageView)findViewById(R.id.product_detail_image);

        Glide.with(ProductdetailActivity.this)
                .load(url)
                .placeholder(R.drawable.eat)
                .crossFade()
                .into(product_detail_image);


        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing);
        collapsingToolbar.setTitle(name);
        collapsingToolbar.setExpandedTitleColor(Color.rgb(0, 0, 0));
        collapsingToolbar.setCollapsedTitleTextColor(Color.rgb(0, 0, 0));

        show_details.setOnClickListener(this);
        show_evaluates.setOnClickListener(this);
        fab_shoppingcar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentcart = new Intent(ProductdetailActivity.this, ShoppingCartActivity.class);
                startActivity(intentcart);
                finish();
//                ShoppingCartBean shoppingCartBean = cartDao.dbQueryOneByPID(productid);
//                if (shoppingCartBean != null) {
//                    int count = shoppingCartBean.getCount();
//                    cartDao.dbUpdateCart(productid, count + 1);
//                } else {
//                    cartDao.dbInsert(productid, url, name, details, address, price, 1);
//                }
//                Snackbar.make(view, "添加菜品 " + name, Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
            }
        });
    }

    private void initView(){

        Ldetails = (LinearLayout)findViewById(R.id.details);
        Levaluates = (LinearLayout)findViewById(R.id.evaluates);

        show_details=(TextView)findViewById(R.id.show_details);
        show_evaluates=(TextView)findViewById(R.id.show_evaluates);
        product_price=(TextView)findViewById(R.id.product_detail_price);
        product_price_before=(TextView)findViewById(R.id.product_detail_price_before);

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
        }
    }
}
