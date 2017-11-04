package com.example.baowuzhida.chishitang;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import Interpolator_extends.RoundProgressBar;

import java.io.File;
import java.text.DecimalFormat;

import Link.UpdateManager;

/**
 * Created by hasee on 2017/10/24.
 */

public class UpdateActivity extends AppCompatActivity {

    public static int version,serverVersion;
    public static String versionName,serverVersionName,downloadResult;
    public static receiveVersionHandler handler;
    private UpdateManager manager = UpdateManager.getInstance();
    private RoundProgressBar proBar;

    Button  button;
    TextView textView1;
    TextView textView2;
    TextView textView3;
    TextView tv;
    TextView tv_jindu;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        final Context c = this;
        version = manager.getVersion(c);
        versionName = manager.getVersionName(c);
        proBar=(RoundProgressBar)findViewById(R.id.progressBar_id);

        button=(Button)findViewById(R.id.updata_button);
        textView1=(TextView)findViewById(R.id.update_text1);
        textView2=(TextView)findViewById(R.id.update_text2);
        textView3=(TextView)findViewById(R.id.update_text3);
        tv_jindu=(TextView)findViewById(R.id.update_jindu);

        tv=(TextView)findViewById(R.id.update4);
        tv.setText("当前版本号:"+version+"\n"+"当前版本名:"+versionName);
        handler = new receiveVersionHandler();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= 23) {
                    int REQUEST_CODE_CONTACT = 101;
                    String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
                    //验证是否许可权限
                    for (String str : permissions) {
                        if (c.checkSelfPermission(str) != PackageManager.PERMISSION_GRANTED) {
                            //申请权限
                            ActivityCompat.requestPermissions(UpdateActivity.this,permissions ,1);
                            return;
                        }
                    }
                }
                manager.compareVersion(UpdateActivity.this);
            }
        });
    }

    public class receiveVersionHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            proBar.setProgress(msg.arg1);
            double len=msg.arg2*msg.arg1*1.0;
            DecimalFormat fnum   =   new   DecimalFormat("##0.00");
            String   dd=fnum.format(msg.arg2*1.0/1024/1024);
            String   ss=fnum.format(len/1024/1024/100);
            tv_jindu.setText(ss+"M/"+dd+"M");
            tv_jindu.setVisibility(View.VISIBLE);
            button.setVisibility(View.GONE);
            proBar.setVisibility(View.VISIBLE);
            if(msg.arg1 == 100){
                Intent intent = new Intent(Intent.ACTION_VIEW);
                String path = Environment.getExternalStorageDirectory()+"/eat.apk";
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    Uri contentUri = FileProvider.getUriForFile(UpdateActivity.this, "com.example.baowuzhida.chishitang.fileprovider", new File(path));
                    intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
                } else {
                    intent.setDataAndType(Uri.fromFile(new File(path)), "application/vnd.android.package-archive");
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                }
                startActivity(intent);
            }
            proBar.setVisibility(View.VISIBLE );
        }
    }
}
