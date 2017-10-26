package com.example.baowuzhida.chishitang;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import layout.fragment.Product2Fragment;
import layout.fragment.Product3Fragment;
import layout.fragment.ProductFragment;

/**
 * Created by Baowuzhida on 2017/10/26.
 */

public class ProductAllActivity extends AppCompatActivity {

    private TabLayout.Tab one,two,three;
    private ViewPager vp;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_all);

        vp = (ViewPager)findViewById(R.id.product_all_pager);
        Intent intent = getIntent();
        int page = intent.getIntExtra("page", -1);
        loadingViewPager();
        vp.setCurrentItem(page);
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
        mTitles.add("当季推荐");
        vp.setAdapter(new myPagerAdapter(getSupportFragmentManager(), fragmentList, mTitles));

        //将TabLayout与ViewPager绑定在一起
        TabLayout mTabLayout = (TabLayout) findViewById(R.id.product_all_tab);
        mTabLayout.setupWithViewPager(vp);

        //指定Tab的位置
        one = mTabLayout.getTabAt(0);
        two = mTabLayout.getTabAt(1);
        three = mTabLayout.getTabAt(2);

    }
}
