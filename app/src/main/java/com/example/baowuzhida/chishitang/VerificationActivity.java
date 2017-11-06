package com.example.baowuzhida.chishitang;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.PermissionChecker;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import Link.HttpUtil;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import es.dmoral.toasty.Toasty;

/**
 * Created by Baowuzhida on 2017/10/31.
 */

public class VerificationActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "SmsYanzheng";
    String mEditTextPhoneNumber,phone,password,name,email;
    EditText mEditTextCode;
    Button mButtonGetCode;
    Button mButtonRegister;
    Toolbar toolbar;
    TextView showtelephone;

    EventHandler eventHandler;
//    String strPhoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);

        initView();


        toolbar.setNavigationIcon(R.mipmap.ic_back);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        SMSSDK.setAskPermisionOnReadContact(true);

        Intent intent = getIntent();
        //从Intent当中根据key取得value
        if (intent != null) {
//            mEditTextPhoneNumber = intent.getStringExtra("phone");
            phone = intent.getStringExtra("phone");
            email = intent.getStringExtra("email");
            password = intent.getStringExtra("password");
            name = intent.getStringExtra("name");
        }

        showtelephone.setText("向号码："+phone+" 发送一条验证短信");

        mButtonGetCode.setOnClickListener(this);
        mButtonRegister.setOnClickListener(this);

//        SMSSDK.initSDK(this, "app key", "app secret"); //使用你申请的key 和 secret

        eventHandler = new EventHandler() {

            /**
             * 在操作之后被触发
             *
             * @param event  参数1
             * @param result 参数2 SMSSDK.RESULT_COMPLETE表示操作成功，为SMSSDK.RESULT_ERROR表示操作失败
             * @param data   事件操作的结果
             */

            @Override
            public void afterEvent(int event, int result, Object data) {
                Message message = myHandler.obtainMessage(0x00);
                message.arg1 = event;
                message.arg2 = result;
                message.obj = data;
                myHandler.sendMessage(message);
            }
        };

        SMSSDK.registerEventHandler(eventHandler);
    }

    private void initView(){
        toolbar = (Toolbar)findViewById(R.id.verification_toolbar);

        showtelephone=(TextView)findViewById(R.id.telephone);

        mEditTextCode = (EditText) findViewById(R.id.register_inputcode);

        mButtonGetCode = (Button) findViewById(R.id.register_getcode);
        mButtonRegister = (Button) findViewById(R.id.register_register);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterEventHandler(eventHandler);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.register_register) {
            String strCode = mEditTextCode.getText().toString();
            if (null != strCode && strCode.length() == 4) {
                Log.d(TAG, mEditTextCode.getText().toString());
                SMSSDK.submitVerificationCode("86", phone, mEditTextCode.getText().toString());
            } else {
                Toasty.warning(getApplicationContext(), "密码长度不正确", Toast.LENGTH_SHORT, true).show();
//                Toast.makeText(this, "密码长度不正确", Toast.LENGTH_SHORT).show();
            }
        } else if (view.getId() == R.id.register_getcode) {
            String  strPhoneNumber = phone;
            if (null == strPhoneNumber || "".equals(strPhoneNumber) || strPhoneNumber.length() != 11) {
                Toasty.warning(getApplicationContext(), "电话号码输入有误", Toast.LENGTH_SHORT, true).show();
//                Toast.makeText(this, "电话号码输入有误", Toast.LENGTH_SHORT).show();
                return;
            }
            SMSSDK.getVerificationCode("86", strPhoneNumber);
            mButtonGetCode.setClickable(false);
            //开启线程去更新button的text
            new Thread() {
                @Override
                public void run() {
                    int totalTime = 60;
                    for (int i = 0; i < totalTime; i++) {
                        Message message = myHandler.obtainMessage(0x01);
                        message.arg1 = totalTime - i;
                        myHandler.sendMessage(message);
                        try {
                            sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    myHandler.sendEmptyMessage(0x02);
                }
            }.start();
        }
    }

    Handler registerhandle=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String type=(String)msg.obj;
            if(msg.obj==null){
                type = "connfail";
            }
            switch (type) {
                case "ok":
                    Toasty.success(getApplicationContext(), "注册成功", Toast.LENGTH_SHORT, true).show();
                    Intent intent = new Intent(VerificationActivity.this, MainActivity.class);
                    startActivity(intent);
                    VerificationActivity.this.finish();
                    break;
                case "nophone":
                    Toasty.warning(getApplicationContext(), "电话号码重复", Toast.LENGTH_SHORT, true).show();
                    break;
                case "noemail":
                    Toasty.warning(getApplicationContext(), "邮箱重复", Toast.LENGTH_SHORT, true).show();
                    break;
                case "connfail":
                    Toasty.warning(getApplicationContext(), "连接超时", Toast.LENGTH_SHORT, true).show();
                    break;
                default:
                    Toasty.error(getApplicationContext(), "注册失败", Toast.LENGTH_SHORT, true).show();
                    break;
            }
        }
    };

    @SuppressLint("HandlerLeak")
    Handler myHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0x00:
                    int event = msg.arg1;
                    int result = msg.arg2;
                    Object data = msg.obj;
                    Log.e(TAG, "result : " + result + ", event: " + event + ", data : " + data);
                    if (result == SMSSDK.RESULT_COMPLETE) { //回调  当返回的结果是complete
                        if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) { //获取验证码
                            Toasty.success(getApplicationContext(), "发送验证码成功", Toast.LENGTH_SHORT, true).show();
                            Log.d(TAG, "get verification code successful.");
                        } else if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) { //提交验证码
                            Log.d(TAG, "submit code successful");
                            Toasty.success(getApplicationContext(), "提交验证码成功", Toast.LENGTH_SHORT, true).show();

                            HttpUtil httpUtil = new HttpUtil();
                            httpUtil.PostURL("http://119.23.205.112:8080/eatCanteen_war/LoginServlet","accountNumber="+name+"&password="+password+"&email="+email+"&phone="+phone+"&type=register",registerhandle);

//                            Intent intent = new Intent(VerificationActivity.this, MainActivity.class);
//                            startActivity(intent);
//                            finish();
                        } else {
                            Log.d(TAG, data.toString());
                        }
                    } else { //进行操作出错，通过下面的信息区分析错误原因
                        try {
                            Throwable throwable = (Throwable) data;
                            throwable.printStackTrace();
                            JSONObject object = new JSONObject(throwable.getMessage());
                            String des = object.optString("detail");//错误描述
                            int status = object.optInt("status");//错误代码
                            //错误代码：  http://wiki.mob.com/android-api-%E9%94%99%E8%AF%AF%E7%A0%81%E5%8F%82%E8%80%83/
                            Log.e(TAG, "status: " + status + ", detail: " + des);
                            if (status > 0 && !TextUtils.isEmpty(des)) {
                                Toast.makeText(VerificationActivity.this, des, Toast.LENGTH_SHORT).show();
                                return;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case 0x01:
                    mButtonGetCode.setText("重新发送(" + msg.arg1 + ")");
                    break;
                case 0x02:
                    mButtonGetCode.setText("获取验证码");
                    mButtonGetCode.setClickable(true);
                    break;
            }
        }
    };
}
