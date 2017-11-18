package com.example.baowuzhida.chishitang;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Baowuzhida on 2017/11/5.
 */

@SuppressLint("Registered")
public class SeatActivity extends AppCompatActivity {

    private List<String> list = new ArrayList<String>();//创建一个String类型的数组列表。
    private TextView myTextView;
    private Spinner mySpinner;
    private ImageView restaurant_seatview;
    private ArrayAdapter<String> adapter;//创建一个数组适配器

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seatview);

        Toolbar toolbar = (Toolbar)findViewById(R.id.seat_toolbar);
        toolbar.setNavigationIcon(R.mipmap.ic_back);
        toolbar.setTitle("食堂座位分布");
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



        // 添加一个下拉列表项的list，这里添加的项就是下拉列表的菜单项，即数据源
        // list.add("选择食堂");
        list.add("一食堂");
        list.add("二食堂");
        list.add("齐大厨");
        restaurant_seatview=(ImageView)findViewById(R.id.restaurant_seatview);
        myTextView = (TextView) findViewById(R.id.textView);//作用在创建点击事件时的文本说明。
        mySpinner = (Spinner) findViewById(R.id.spinner1);
        //1.为下拉列表定义一个数组适配器，这个数组适配器就用到里前面定义的list。装的都是list所添加的内容
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, list);//样式为原安卓里面有的android.R.layout.simple_spinner_item，让这个数组适配器装list内容。
        //2.为适配器设置下拉菜单样式。adapter.setDropDownViewResource
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //3.以上声明完毕后，建立适配器,有关于sipnner这个控件的建立。用到myspinner
        mySpinner.setAdapter(adapter);
        //4.为下拉列表设置各种点击事件，以响应菜单中的文本item被选中了，用setOnItemSelectedListener
        mySpinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {//选择item的选择点击监听事件
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                // TODO Auto-generated method stub
                // 将所选mySpinner 的值带入myTextView 中
                // myTextView.setText("您选择的是：" + adapter.getItem(arg2));//文本说明
                if(arg2==1){
                    restaurant_seatview.setBackgroundResource(R.drawable.fir_res);
//                    myTextView.setText("食堂：" + adapter.getItem(arg2));
//                    restaurant_seatview.setBackground();
                }
                if(arg2==2){
                    restaurant_seatview.setBackgroundResource(R.drawable.sec_res);
//                    myTextView.setText("食堂：" + adapter.getItem(arg2));
                }
                if(arg2==0){
                    restaurant_seatview.setBackgroundResource(R.drawable.xi_res);
//                    myTextView.setText("食堂：" + adapter.getItem(arg2));

                }
            }

            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
                myTextView.setText("Nothing");
            }
        });

    }
}
