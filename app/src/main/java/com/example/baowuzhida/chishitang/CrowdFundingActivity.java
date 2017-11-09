package com.example.baowuzhida.chishitang;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;

//import com.spark.submitbutton.SubmitButton;

/**
 * Created by Baowuzhida on 2017/11/6.
 */

public class CrowdFundingActivity extends AppCompatActivity implements View.OnClickListener {
//    private SubmitButton clowfunding_join,clowfunding_support;
    private Toolbar toolbar;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crowdfunding);
        initView();

        toolbar.setNavigationIcon(R.mipmap.ic_back);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

//        clowfunding_join.setOnClickListener(this);
//        clowfunding_support.setOnClickListener(this);
    }

    public void initView(){
        toolbar=(Toolbar)findViewById(R.id.clowfunding_toolbar);
//        clowfunding_join=(SubmitButton)findViewById(R.id.clowfunding_join);
//        clowfunding_support=(SubmitButton)findViewById(R.id.clowfunding_support);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch(v.getId()){
            case R.id.clowfunding_join:
                intent = new Intent(CrowdFundingActivity.this, CrowdFundJoinActivity.class);
                startActivity(intent);
                break;
            case R.id.clowfunding_support:
                intent = new Intent(CrowdFundingActivity.this, CrowdFundSupportActivity.class);
                startActivity(intent);
                break;
        }
    }
}

