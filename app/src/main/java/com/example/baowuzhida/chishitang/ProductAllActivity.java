package com.example.baowuzhida.chishitang;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import java.util.ArrayList;
import java.util.List;

import Adapter.myPagerAdapter;
import layout.fragment.Product2Fragment;
import layout.fragment.Product3Fragment;
import layout.fragment.ProductFragment;

/**
 * Created by Baowuzhida on 2017/10/26.
 */

public class ProductAllActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TabLayout.Tab one,two,three;
    private ViewPager vp;
    private FloatingActionMenu fab;
    private FloatingActionButton fbbtn_shoppingcar,fbbtn_question;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_all);

        initView();
        toolbar.setNavigationIcon(R.mipmap.ic_back);
//        toolbar.setSubtitle("美食");
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        contralFab();
        loadingViewPager();
        Intent intent = getIntent();
        int page = intent.getIntExtra("page", -1);
        vp.setCurrentItem(page);
    }
    public void initView(){
        toolbar = (Toolbar)findViewById(R.id.product_all_toolbar);
        vp = (ViewPager)findViewById(R.id.product_all_pager);
    }

    //加载顶部导航
    private void loadingViewPager() {

        //使用适配器将ViewPager与Fragment绑定在一起

        List<Fragment> fragmentList = new ArrayList<Fragment>();
        fragmentList.add(new ProductFragment());
        fragmentList.add(new Product2Fragment());
        fragmentList.add(new Product3Fragment());
        List<String> mTitles = new ArrayList<>();
        mTitles.add("所有美食");
        mTitles.add("特色菜品");
        mTitles.add("众筹菜品");
        vp.setAdapter(new myPagerAdapter(getSupportFragmentManager(), fragmentList, mTitles));

        //将TabLayout与ViewPager绑定在一起
        TabLayout mTabLayout = (TabLayout) findViewById(R.id.product_all_tab);
        mTabLayout.setupWithViewPager(vp);

        //指定Tab的位置
        one = mTabLayout.getTabAt(0);
        two = mTabLayout.getTabAt(1);
        three = mTabLayout.getTabAt(2);

    }

    public void contralFab(){
        fab = (FloatingActionMenu) findViewById(R.id.fab);
        fab.setClosedOnTouchOutside(true);

        fbbtn_shoppingcar= (FloatingActionButton)findViewById(R.id.fab_shoppingcar);
        fbbtn_question= (FloatingActionButton)findViewById(R.id.fab_question);
        fbbtn_shoppingcar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentcart = new Intent(ProductAllActivity.this, ShoppingCartActivity.class);
                startActivity(intentcart);
            }
        });
        fbbtn_question.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new  AlertDialog.Builder(ProductAllActivity.this)
                        .setTitle("打开小助手" )
                        .setMessage("确定吗？" )
                        .setPositiveButton("是" ,  null )
                        .setNegativeButton("否" , null)
                        .show();
            }
        });
    }
}
