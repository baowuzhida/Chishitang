package com.example.baowuzhida.chishitang;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

/**
 * Created by Baowuzhida on 2017/11/5.
 */

@SuppressLint("Registered")
public class SeatActivity extends AppCompatActivity {

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
    }
}
