package com.example.baowuzhida.chishitang;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Link.HttpUtil;

/**
 * Created by Baowuzhida on 2017/7/25.
 */

public class RegisterActivity extends AppCompatActivity {
    private EditText register_addname;
    private EditText register_addpassword;
    private EditText register_addemail;
    private EditText register_addphone;
    private Button register_register;
    private Toolbar toolbar;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        toolbar = (Toolbar)findViewById(R.id.register_toolbar);
        toolbar.setNavigationIcon(R.mipmap.ic_back);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        register_addname = (EditText)findViewById(R.id.register_addname);
        register_addpassword = (EditText)findViewById(R.id.register_addpassword);
        register_addemail = (EditText)findViewById(R.id.register_addemail);
        register_addphone = (EditText)findViewById(R.id.register_addphone);
        register_register = (Button)findViewById(R.id.register_register);

        register_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(check()){
                    gotoregister();
                }
            }
        });
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
                    Toast.makeText(getApplicationContext(), "注册成功", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                    startActivity(intent);
                    RegisterActivity.this.finish();
                    break;
                case "nophone":
                    Toast.makeText(getApplicationContext(), "电话号码重复", Toast.LENGTH_SHORT).show();
                    break;
                case "noemail":
                    Toast.makeText(getApplicationContext(), "邮箱重复", Toast.LENGTH_SHORT).show();
                    break;
                case "connfail":
                    Toast.makeText(getApplicationContext(), "连接超时", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    Toast.makeText(getApplicationContext(), "注册失败", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    public void gotoregister(){
        String name = register_addname.getText().toString();
        String psd = register_addpassword.getText().toString();
        String email = register_addemail.getText().toString();
        String phone = register_addphone.getText().toString();

        HttpUtil httpUtil = new HttpUtil();
        httpUtil.PostURL("http://119.23.205.112:8080/eatCanteen_war/LoginServlet","accountNumber="+name+"&password="+psd+"&email="+email+"&phone="+phone+"&type=register",registerhandle);
    }
    public boolean check(){
        String name = register_addname.getText().toString();
        String psd = register_addpassword.getText().toString();
        String email = register_addemail.getText().toString();
        String phone = register_addphone.getText().toString();
        boolean isphone = isPhone(phone);
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(getApplicationContext(),"姓名不能为空",Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(psd)) {
            Toast.makeText(getApplicationContext(),"密码不能为空",Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(),"邮箱不能为空",Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(phone)) {
            Toast.makeText(getApplicationContext(),"电话号码不能为空",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(!isphone){
            Toast.makeText(getApplicationContext(),"请输入正确号码",Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
    private boolean isPhone(String str) {
        // 将给定的正则表达式编译并赋予给Pattern类
        Pattern pattern = Pattern.compile("1[0-9]{10}");
        // 对指定输入的字符串创建一个Matcher对象
        Matcher matcher = pattern.matcher(str);
        // 尝试对整个目标字符展开匹配检测,也就是只有整个目标字符串完全匹配时才返回真值.
        if (matcher.matches()) {
            return true;
        } else {
            return false;
        }
    }

}
