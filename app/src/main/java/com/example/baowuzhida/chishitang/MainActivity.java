package com.example.baowuzhida.chishitang;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.view.ScrollingView;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.ScrollerCompat;
import android.support.v4.widget.SwipeRefreshLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import Bean.OrdersBean;
import Link.HttpUtil;
import Link.SharedPrefsCookieUtil;
import layout.fragment.ProductFragment;
import layout.fragment.Product2Fragment;
import layout.fragment.Product3Fragment;


public class MainActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private ImageView RoundedImage;
    //    private LinearLayout LRLayout;
//    private LinearLayout LInfLayout;
    private View orderView,personalcenterView,specialView;
    private TextView IfLogin,show_login,Point;
    private ScrollView scr_personal_center;
    private SwipeRefreshLayout orderswipe;
    private ListView order_listview;
    private Button btn_login,btn_register,btn_logout;
    private Toolbar toolbar;
    private FloatingActionButton fbbtn_shoppingcar,fbbtn_question;
    private HttpUtil httpUtil;
    private FloatingActionMenu fab;


    private TabLayout.Tab one,two,three;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            viewPager = (ViewPager)findViewById(R.id.ViewPager);

            switch (item.getItemId()) {
                case R.id.navigation_home:
//                    viewPager.setCurrentItem(0);
                    fab.setVisibility(View.VISIBLE);
                    personalcenterView.setVisibility(View.GONE);
                    orderView.setVisibility(View.GONE);
                    specialView.setVisibility(View.GONE);
                    viewPager.setVisibility(View.VISIBLE);
                    return true;
                case R.id.navigation_dashboard:
                    contralOrder();
                    fab.setVisibility(View.GONE);
                    personalcenterView.setVisibility(View.GONE);
                    viewPager.setVisibility(View.GONE);
                    specialView.setVisibility(View.GONE);
                    orderView.setVisibility(View.VISIBLE);
                    return true;
                case R.id.navigation_special:
                    contralSpecial();
                    fab.setVisibility(View.GONE);
                    orderView.setVisibility(View.GONE);
                    viewPager.setVisibility(View.GONE);
                    personalcenterView.setVisibility(View.GONE);
                    specialView.setVisibility(View.VISIBLE);
                    return true;
                case R.id.navigation_notifications:
                    contralPersonalcenter();
                    fab.setVisibility(View.GONE);
                    orderView.setVisibility(View.GONE);
                    viewPager.setVisibility(View.GONE);
                    specialView.setVisibility(View.GONE);
                    personalcenterView.setVisibility(View.VISIBLE);
                    return true;
            }
            return false;
        }
    };

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPrefsCookieUtil scookie=new SharedPrefsCookieUtil(this);
        httpUtil = new HttpUtil(scookie);

        toolbar = (Toolbar)findViewById(R.id.Toolbar);
        orderView=findViewById(R.id.main_order);
        personalcenterView=findViewById(R.id.main_personalcenter);
        specialView=findViewById(R.id.main_special);
        show_login = (TextView)findViewById(R.id.order_show_login);
        orderswipe = (SwipeRefreshLayout)findViewById(R.id.order_swipe);

        contralToolbar();
        loadingViewPager();
//        LoadingTitle();
        relogin();//判断本地是否登录 如果登陆向服务端发送一次请求
        contralFab();
        //
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }
    //Fab信息
    public void contralFab(){
        fab = (FloatingActionMenu) findViewById(R.id.fab);
        fab.setClosedOnTouchOutside(true);

        fbbtn_shoppingcar= (FloatingActionButton)findViewById(R.id.fab_shoppingcar);
        fbbtn_question= (FloatingActionButton)findViewById(R.id.fab_question);
        fbbtn_shoppingcar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentcart = new Intent(MainActivity.this, ShoppingCartActivity.class);
                startActivity(intentcart);
                Toast.makeText(MainActivity.this, "进入购物车", Toast.LENGTH_SHORT).show();
            }
        });
    }
    //Toolbar信息
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }
    //Toolbar设置
    public void contralToolbar(){

        toolbar.setTitle("吃食堂");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setSubtitle("就爱吃食堂");
        toolbar.setSubtitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        toolbar.inflateMenu(R.menu.toolbar_menu);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override public boolean onMenuItemClick(MenuItem item)
            { switch (item.getItemId()) {
                case R.id.toolbar_menu:
                    Intent intent = new Intent(MainActivity.this, UpdateActivity.class);
                    startActivity(intent);
                    Toast.makeText(MainActivity.this, "进入app信息", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.toolbar_search:
                    Intent intentsearch = new Intent(MainActivity.this, SearchActivity.class);
                    startActivity(intentsearch);
                    Toast.makeText(MainActivity.this, "进入搜索页面", Toast.LENGTH_SHORT).show();
                    break;
            } return true;
            }
        });
    }
    //个人中心信息
    public void contralPersonalcenter(){
        scr_personal_center =(ScrollView)findViewById(R.id.person_detail_center_scroll);

        RoundedImage = (ImageView)findViewById(R.id.personalcenter_roundedmage);
        IfLogin = (TextView)findViewById(R.id.personalcenter_iflogin);
        Point = (TextView)findViewById(R.id.personalcenter_point);

//        btn_login=(Button)findViewById(R.id.personalcenter_login);
//        btn_register=(Button)findViewById(R.id.personalcenter_register);
        btn_logout=(Button)findViewById(R.id.personalcenter_logout);

//        LRLayout=(LinearLayout)findViewById(R.id.LRLayout);
//        LInfLayout=(LinearLayout)findViewById(R.id.LInfLayout);

        SharedPreferences sharedPre=getSharedPreferences("LoginManager", MODE_PRIVATE);
        String username=sharedPre.getString("username", "");
        String userimage=sharedPre.getString("userimage", "");
        if(username.equals("")){
            IfLogin.setText("未登录");
            btn_logout.setVisibility(View.GONE);
//            scr_personal_center.setVisibility(View.GONE);
//            LRLayout.setVisibility(View.VISIBLE);
//            LInfLayout.setVisibility(View.GONE);
        }
        else {
            IfLogin.setText(" 欢迎用户 "+username+" 登录");

            Glide.with(this)
                    .load(userimage)
                    .placeholder(R.drawable.eat)
                    .crossFade()
                    .into(RoundedImage);
            btn_logout.setVisibility(View.VISIBLE);
//            LRLayout.setVisibility(View.GONE);
//            LInfLayout.setVisibility(View.VISIBLE);
//            scr_personal_center.setVisibility(View.VISIBLE);

        }
        IfLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPre = getSharedPreferences("LoginManager", MODE_PRIVATE);
                final String username = sharedPre.getString("username", "");
                String password = sharedPre.getString("password", "");
                if(!username.equals("")) {
                    Toast.makeText(getApplicationContext(), "用户详情界面", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getApplicationContext(), "尚未登陆", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            }
        });
//        btn_login.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
//                startActivity(intent);
//            }
//        });
//        btn_register.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
//                startActivity(intent);
//            }
//        });
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("登出")
                        .setMessage("确定吗？")
                        .setPositiveButton("是", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                cleanShare();
                            }
                        })
                        .setNegativeButton("否", null)
                        .show();
            }
        });

    }
    //订单信息
    public void contralOrder() {
        orderRefresh(orderswipe);
        orderswipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(){
                orderRefresh(orderswipe);
            }
        });
    }
    //特色功能信息
    public void contralSpecial() {
        int[] icon = { R.mipmap.im_likeeat, R.mipmap.im_foodvote,
                R.mipmap.im_datemeal,R.mipmap.im_clowdfunding, R.mipmap.im_restaurantseat};
        String[] iconName = { "大家都爱吃", "喜爱美食投票", "约餐", "特色美食众筹", "食堂座位分布" };

        GridView gridview = (GridView) findViewById(R.id.special_gridview);

        //生成动态数组，并且转入数据
        ArrayList<HashMap<String, Object>> lstImageItem = new ArrayList<HashMap<String, Object>>();
        for (int i = 0; i < icon.length; i++) {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("ItemImage", icon[i]);//添加图像资源的ID
            map.put("ItemText", iconName[i]);//按序号做ItemText
            lstImageItem.add(map);
        }
        //生成适配器的ImageItem <====> 动态数组的元素，两者一一对应
        SimpleAdapter saImageItems = new SimpleAdapter(this, //没什么解释
                lstImageItem,//数据来源
                R.layout.special_list_item,//night_item的XML实现
                //动态数组与ImageItem对应的子项
                new String[]{"ItemImage", "ItemText"},
                //ImageItem的XML文件里面的一个ImageView,两个TextView ID
                new int[]{R.id.special_list_image, R.id.special_list_text});
        //添加并且显示
        gridview.setAdapter(saImageItems);
        //添加消息处理
        gridview.setOnItemClickListener(new ItemClickListener());

    }
    //判断本地是否登录 如果登陆向服务端发送一次请求
    public void relogin() {

        SharedPreferences sharedPre = getSharedPreferences("LoginManager", MODE_PRIVATE);
        final String username = sharedPre.getString("username", "");
        String password = sharedPre.getString("password", "");
        Handler ifloginhandle = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                String type = (String) msg.obj;
                if (msg.obj == null) {
                    type = "connfail";
                }
                switch (type) {
                    case "no":
                        Toast.makeText(getApplicationContext(), "账号或密码错误", Toast.LENGTH_SHORT).show();
                        break;
                    case "connfail":
                        Toast.makeText(getApplicationContext(), "连接超时", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        Toast.makeText(getApplicationContext(), "欢迎回来 " + username, Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        };
        if (!(username.equals("") || password.equals(""))) {
            httpUtil.PostURL("http://119.23.205.112:8080/eatCanteen_war/LoginServlet", "accountNumber=" + username + "&password=" + password + "&type=login", ifloginhandle);
        }

//        SharedPreferences sharedPre=getSharedPreferences("LoginManager", MODE_PRIVATE);
//        String username = sharedPre.getString("username", "");
//        String password = sharedPre.getString("password","");
//        return !(username.equals("") || password.equals(""));
    }
    //退出时清除本地保存的用户信息
    public void cleanShare(){ //清除SharedPreferences中LoginManager数据

        ListView order_listview = (ListView)findViewById(R.id.order_listview);
        order_listview.setAdapter(null);
        ArrayAdapter adapter = (ArrayAdapter) order_listview.getAdapter();// 获取当前listview的adapter
        if(adapter!=null){
            int count = adapter.getCount();// listview多少个组件
            if (count > 0) {
                order_listview.setAdapter(new ArrayAdapter<String>(this,
                        android.R.layout.simple_list_item_1));
            }
        }

        Handler logouthander = new Handler(){
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                String type=(String)msg.obj;

                if(msg.obj==null){
                    Toast.makeText(getApplicationContext(), "服务端连接失败", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(type.equals("ok")){
                    IfLogin.setText("未登录");

                    btn_logout.setVisibility(View.GONE);
//                    LRLayout.setVisibility(View.VISIBLE);
//                    scr_personal_center.setVisibility(View.GONE);
//                    LInfLayout.setVisibility(View.GONE);
                    RoundedImage.setImageDrawable(getResources().getDrawable(R.drawable.eat));
                    SharedPreferences sharedPre = getSharedPreferences("LoginManager", MODE_PRIVATE);;
                    SharedPreferences.Editor editor=sharedPre.edit();
                    editor.clear();
                    editor.apply();

                    show_login.setVisibility(View.GONE);
                    orderswipe.setVisibility(View.VISIBLE);

                    Toast.makeText(getApplicationContext(), "已登出", Toast.LENGTH_LONG).show();
                }
                else
                    Toast.makeText(getApplicationContext(), "服务端登出失败", Toast.LENGTH_LONG).show();
            }

        };
        HttpUtil httpUtil = new HttpUtil();
        httpUtil.PostURL("http://119.23.205.112:8080/eatCanteen_war/LoginServlet","type=logout",logouthander);
    }
    //加载顶部导航
    private void loadingViewPager() {

        //使用适配器将ViewPager与Fragment绑定在一起
        ViewPager vp = (ViewPager)findViewById(R.id.ViewPager);
        List<Fragment> fragmentList = new ArrayList<Fragment>();
        fragmentList.add(new ProductFragment());
        fragmentList.add(new Product2Fragment());
        fragmentList.add(new Product3Fragment());
        List<String> mTitles = new ArrayList<>();
        mTitles.add("菜品详情");
        mTitles.add("特色菜品");
        mTitles.add("当季推荐");
        vp.setAdapter(new myPagerAdapter(getSupportFragmentManager(), fragmentList, mTitles));

        //将TabLayout与ViewPager绑定在一起
        TabLayout mTabLayout = (TabLayout) findViewById(R.id.tabLayout);
        mTabLayout.setupWithViewPager(vp);

        //指定Tab的位置
        one = mTabLayout.getTabAt(0);
        two = mTabLayout.getTabAt(1);
        three = mTabLayout.getTabAt(2);

    }
    //刷新订单
    private void orderRefresh(final SwipeRefreshLayout orderswipe){
        SharedPreferences sharedPre=getSharedPreferences("LoginManager", MODE_PRIVATE);
        final String username=sharedPre.getString("username", "");
        if(username.equals("")){
            Toast.makeText(getApplicationContext(), "您还未登录呢！", Toast.LENGTH_SHORT).show();
            orderswipe.setRefreshing(false);
            return;
        }else{
            show_login.setVisibility(View.GONE);
            orderswipe.setVisibility(View.VISIBLE);
        }

        order_listview = (ListView)findViewById(R.id.order_listview);
        Handler orderlisthandler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                LinkedList<OrdersBean> linkedList=new LinkedList<>();
                JSONArray jsonArray;
                try {
                    jsonArray = new JSONArray((String) msg.obj);
                    for(int i = 0;i < jsonArray.length();i++) {
                        JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                        OrdersBean ordersBean=new OrdersBean();
                        ordersBean.setOrders_id(jsonObject.getInt("orders_id"));
                        ordersBean.setUser_id(jsonObject.getInt("user_id"));
                        ordersBean.setOrders_price(jsonObject.getDouble("orders_price"));
                        ordersBean.setOrders_time(jsonObject.getString("orders_time"));
                        linkedList.add(ordersBean);
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                OrdersAdapter ordersAdapter=new OrdersAdapter(linkedList,MainActivity.this);
                order_listview.setAdapter(ordersAdapter);
                orderswipe.setRefreshing(false);
            }
        };
        HttpUtil httpUtil = new HttpUtil();
        httpUtil.PostURL("http://119.23.205.112:8080/eatCanteen_war/OrdersServlet","type=list",orderlisthandler);
    }
    //特色菜单栏点击监听
    private class ItemClickListener implements AdapterView.OnItemClickListener {
        public void onItemClick(AdapterView<?> arg0,//The AdapterView where the click happened
                                View v,//The view within the AdapterView that was clicked
                                int position,//The position of the view in the adapter
                                long arg3//The row id of the item that was clicked
        ) {
            //在本例中arg2=arg3
            HashMap<String, Object> item = (HashMap<String, Object>) arg0.getItemAtPosition(position);
            //显示所选Item的ItemText
            switch (position){
                case 0:
                    break;
                case 1:
                    Intent intent = new Intent(MainActivity.this, VoteActivity.class);
                    startActivity(intent);
                    break;
                case 2:
                    break;
                case 3:
                    break;
                case 4:
                    break;
            }
            Toast.makeText(getApplicationContext(), "点击"+item.get("ItemText"), Toast.LENGTH_SHORT).show();
        }
    }
}

