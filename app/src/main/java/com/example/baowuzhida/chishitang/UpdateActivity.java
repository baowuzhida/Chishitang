package com.example.baowuzhida.chishitang;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by hasee on 2017/10/24.
 */

public class UpdateActivity extends AppCompatActivity {

    private Button  button;
    private TextView textView1;
    private TextView textView2;
    private TextView textView3;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        button=(Button)findViewById(R.id.updata_button);
        textView1=(TextView)findViewById(R.id.update_text1);
        textView2=(TextView)findViewById(R.id.update_text2);
        textView3=(TextView)findViewById(R.id.update_text3);

    }
}
