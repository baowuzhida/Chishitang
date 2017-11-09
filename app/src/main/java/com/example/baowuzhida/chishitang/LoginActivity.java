package com.example.baowuzhida.chishitang;


import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import Bean.UserBean;
import Interpolator_extends.JellyInterpolator;
import Link.HttpUtil;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import es.dmoral.toasty.Toasty;

/**
 * Created by Baowuzhida on 2017/7/20.
 */

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private Toolbar toolbar;
    private EditText login_input_username;
    private EditText login_input_password;
    private Button login_login;
    private TextView login_goto_register;
    private ImageButton login_delete_username;
    private static CheckBox login_remember_password;
    private float mWidth, mHeight;
    private View progress;
    private View mInputLayout;

    private void initView() {
        progress = findViewById(R.id.layout_progress);
        mInputLayout = findViewById(R.id.login_ll);
        login_goto_register = (TextView)findViewById(R.id.login_goto_register) ;
        login_login = (Button)findViewById(R.id.login_login);
        login_remember_password=(CheckBox)findViewById(R.id.login_remember_password);
        login_input_username = (EditText)findViewById(R.id.login_input_username) ;
        login_input_password = (EditText)findViewById(R.id.login_input_password) ;
        login_delete_username = (ImageButton)findViewById(R.id.login_delete_username);
        login_input_username.addTextChangedListener(textChange);


        login_goto_register.setOnClickListener(this);
        login_login.setOnClickListener(this);
        login_remember_password.setOnClickListener(this);
    }

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        toolbar = (Toolbar)findViewById(R.id.login_toolbar);
        toolbar.setNavigationIcon(R.mipmap.ic_back);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        initView();

        SharedPreferences remembersharePre=getSharedPreferences("RememberPassword", MODE_PRIVATE);
        boolean isRemember = remembersharePre.getBoolean("remember_password",false);
        if(isRemember){
            String username = remembersharePre.getString("username","");
            String password = remembersharePre.getString("password","");
            login_input_username.setText(username);
            login_input_password.setText(password);
            login_remember_password.setChecked(true);
        }

    }

    public void onClick(View v) {

        switch (v.getId()){
            case R.id.login_goto_register:
                Register();
                break;
            case R.id.login_login:
                name = login_input_username.getText().toString();
                psd = login_input_password.getText().toString();
                if (TextUtils.isEmpty(name)) {
                    Toasty.info(getApplicationContext(), "姓名不能为空", Toast.LENGTH_SHORT, true).show();
//                    Toast.makeText(getApplicationContext(),"姓名不能为空",Toast.LENGTH_SHORT).show();
                    break;
                }
                if (TextUtils.isEmpty(psd)) {
                    Toasty.info(getApplicationContext(), "密码不能为空", Toast.LENGTH_SHORT, true).show();
//                    Toast.makeText(getApplicationContext(),"密码不能为空",Toast.LENGTH_SHORT).show();
                    break;
                }
                mWidth = login_login.getMeasuredWidth();
                mHeight = login_login.getMeasuredHeight();
                // 隐藏输入框
                login_input_username.setVisibility(View.INVISIBLE);
                login_input_password.setVisibility(View.INVISIBLE);
                inputAnimator(mInputLayout, mWidth, mHeight);

                Login(name,psd);
                break;
        }
    }

    TextWatcher textChange = new TextWatcher(){
        @Override
        public void afterTextChanged(Editable s) {
            login_delete_username.setVisibility(View.VISIBLE);
            login_delete_username.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    delete_username();
                }
            });
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
        }
        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
        }};

    public void Register(){
        Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
        startActivity(intent);
        LoginActivity.this.finish();
    }

    String name;
    String psd;
    Handler loginhandle=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String type=(String)msg.obj;
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if(msg.obj==null){
                type = "connfail";
            }
            switch (type) {
                case "no":
                    Toasty.error(getApplicationContext(), "账号或密码错误", Toast.LENGTH_SHORT, true).show();
//                    Toast.makeText(getApplicationContext(), "账号或密码错误", Toast.LENGTH_SHORT).show();
                    progress.setVisibility(View.GONE);
                    mInputLayout.setVisibility(View.VISIBLE);
                    break;
                case "connfail":
                    Toasty.error(getApplicationContext(), "连接超时", Toast.LENGTH_SHORT, true).show();
//                    Toast.makeText(getApplicationContext(), "连接超时", Toast.LENGTH_SHORT).show();
                    progress.setVisibility(View.GONE);
                    mInputLayout.setVisibility(View.VISIBLE);
                    break;
                default:
                    UserBean userBean=new UserBean();
                    try {
                        JSONObject jsonObject = new JSONObject(type);
                        userBean.setUser_id(jsonObject.getInt("user_id"));
                        userBean.setUser_name(jsonObject.getString("user_name"));
                        userBean.setUser_headimage(jsonObject.getString("user_headimage"));
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                    saveLoginInfo(getApplicationContext(), name, psd,userBean.getUser_headimage());
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                    break;
            }
        }
    };

    public void Login(String name,String psd){

        HttpUtil httpUtil = new HttpUtil();
        httpUtil.PostURL("http://119.23.205.112:8080/eatCanteen_war/LoginServlet","accountNumber="+name+"&password="+psd+"&type=login",loginhandle);
    }

    public static void saveLoginInfo(Context context, String username, String password,String userimage){
        //获取SharedPreferences对象
        SharedPreferences loginsharePre=context.getSharedPreferences("LoginManager", MODE_PRIVATE);
        SharedPreferences remembersharePre=context.getSharedPreferences("RememberPassword", MODE_PRIVATE);
        //获取Editor对象
        SharedPreferences.Editor editor=loginsharePre.edit();
        SharedPreferences.Editor remembereditor=remembersharePre.edit();
        //设置参数
        if(login_remember_password.isChecked()){
            remembereditor.putBoolean("remember_password",true);
            remembereditor.putString("username", username);
            remembereditor.putString("password", password);
        }else{
            remembereditor.clear();
        }
        editor.putString("username", username);
        editor.putString("password", password);
        editor.putString("userimage", userimage);
        //提交
        editor.apply();
        remembereditor.apply();
    }
    //监听用户名文本输入情况 一旦输入则显示X按钮
    public void delete_username() {//X按钮清除用户名密码
        login_input_username.setText("");
        login_input_password.setText("");
        login_delete_username.setVisibility(View.GONE);
        login_input_username.setFocusable(true);
        login_input_username.setFocusableInTouchMode(true);
        login_input_username.requestFocus();
    }

    private void inputAnimator(final View view, float w, float h) {

        AnimatorSet set = new AnimatorSet();

        ValueAnimator animator = ValueAnimator.ofFloat(0, w);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (Float) animation.getAnimatedValue();
                ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) view
                        .getLayoutParams();
                params.leftMargin = (int) value;
                params.rightMargin = (int) value;
                view.setLayoutParams(params);
            }
        });

        ObjectAnimator animator2 = ObjectAnimator.ofFloat(mInputLayout,
                "scaleX", 1f, 0.5f);
        set.setDuration(300);
        set.setInterpolator(new AccelerateDecelerateInterpolator());
        set.playTogether(animator, animator2);
        set.start();
        set.addListener(new Animator.AnimatorListener() {

            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                /**
                 * 动画结束后，先显示加载的动画，然后再隐藏输入框
                 */
                progress.setVisibility(View.VISIBLE);
                progressAnimator(progress);
                mInputLayout.setVisibility(View.GONE);

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }
        });

    }

    /**
     * 出现进度动画
     *
     * @param view
     */
    private void progressAnimator(final View view) {
        PropertyValuesHolder animator = PropertyValuesHolder.ofFloat("scaleX",
                0.5f, 1f);
        PropertyValuesHolder animator2 = PropertyValuesHolder.ofFloat("scaleY",
                0.5f, 1f);
        ObjectAnimator animator3 = ObjectAnimator.ofPropertyValuesHolder(view,
                animator, animator2);
        animator3.setDuration(1000);
        animator3.setInterpolator(new JellyInterpolator());
        animator3.start();


    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {//点击的是返回键
            if (event.getAction() == KeyEvent.ACTION_DOWN && event.getRepeatCount() == 0) {//按键的按下事件
//                Toast.makeText(getApplicationContext(), "dispatchKeyEvent--Down", Toast.LENGTH_SHORT).show();
//               return false;
            } else if (event.getAction() == KeyEvent.ACTION_UP && event.getRepeatCount() == 0) {//按键的抬起事件
//                Toast.makeText(getApplicationContext(), "dispatchKeyEvent--UP", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
//               return false;
            }
        }
        return super.dispatchKeyEvent(event);
    }
}
