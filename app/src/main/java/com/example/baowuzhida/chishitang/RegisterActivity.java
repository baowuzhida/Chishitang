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
import es.dmoral.toasty.Toasty;

/**
 * Created by Baowuzhida on 2017/7/25.
 */

public class RegisterActivity extends AppCompatActivity {
    private EditText register_addname;
    private EditText register_addpassword;
    private EditText register_addemail;
    private EditText register_addphone;
    private Button register_next;
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
        register_next = (Button)findViewById(R.id.register_next);

        register_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(check()){
                    gotoregister();
                }
            }
        });
    }



    public void gotoregister(){
        String name = register_addname.getText().toString();
        String psd = register_addpassword.getText().toString();
        String email = register_addemail.getText().toString();
        String phone = register_addphone.getText().toString();

        Intent intent = new Intent(RegisterActivity.this,VerificationActivity.class);
        intent.putExtra("phone",phone);
        intent.putExtra("name",name);
        intent.putExtra("password",psd);
        intent.putExtra("email",email);
        startActivity(intent);
    }

    public boolean check(){
        String name = register_addname.getText().toString();
        String psd = register_addpassword.getText().toString();
        String email = register_addemail.getText().toString();
        String phone = register_addphone.getText().toString();
        boolean isphone = isPhone(phone);
        if (TextUtils.isEmpty(name)) {
            Toasty.warning(getApplicationContext(), "姓名不能为空", Toast.LENGTH_SHORT, true).show();
//            Toast.makeText(getApplicationContext(),"姓名不能为空",Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(psd)) {
            Toasty.warning(getApplicationContext(), "密码不能为空", Toast.LENGTH_SHORT, true).show();
//            Toast.makeText(getApplicationContext(),"密码不能为空",Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(email)) {
            Toasty.warning(getApplicationContext(), "邮箱不能为空", Toast.LENGTH_SHORT, true).show();
//            Toast.makeText(getApplicationContext(),"邮箱不能为空",Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(phone)) {
            Toasty.warning(getApplicationContext(), "电话号码不能为空", Toast.LENGTH_SHORT, true).show();
//            Toast.makeText(getApplicationContext(),"电话号码不能为空",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(!isphone){
            Toasty.warning(getApplicationContext(), "请输入正确号码", Toast.LENGTH_SHORT, true).show();
//            Toast.makeText(getApplicationContext(),"请输入正确号码",Toast.LENGTH_SHORT).show();
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
