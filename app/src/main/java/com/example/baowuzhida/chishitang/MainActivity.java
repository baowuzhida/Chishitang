package com.example.baowuzhida.chishitang;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
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
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import Adapter.OrdersAdapter;
import Adapter.ProductMainAdapter;
import Bean.ProductBean;
import com.bumptech.glide.Glide;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.jude.rollviewpager.RollPagerView;
import com.zqg.dialogviewpager.StepDialog;
//import com.zqg.dialogviewpager.StepDialog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;

import Bean.OrdersBean;
import Link.HttpUtil;
import Link.SharedPrefsCookieUtil;
import es.dmoral.toasty.Toasty;


public class MainActivity extends AppCompatActivity {

    private ListView product_listview;
    private ImageView RoundedImage;
    private View orderView,personalcenterView,specialView,productView;
    private TextView IfLogin,show_login,Point;
    private ScrollView scr_personal_center;
    private SwipeRefreshLayout orderswipe,productswipe;
    private ListView order_listview;
    private Button btn_logout;
    private Toolbar toolbar;
    private FloatingActionButton fbbtn_shoppingcar,fbbtn_question;
    private HttpUtil httpUtil;
    private FloatingActionMenu fab;




    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    controlProduct();
                    fab.setVisibility(View.VISIBLE);
                    productView.setVisibility(View.VISIBLE);
                    personalcenterView.setVisibility(View.GONE);
                    orderView.setVisibility(View.GONE);
                    specialView.setVisibility(View.GONE);
                    return true;
                case R.id.navigation_dashboard:
                    contralOrder();
                    fab.setVisibility(View.GONE);
                    personalcenterView.setVisibility(View.GONE);
                    productView.setVisibility(View.GONE);
                    specialView.setVisibility(View.GONE);
                    orderView.setVisibility(View.VISIBLE);
                    return true;
                case R.id.navigation_special:
                    contralSpecial();
                    fab.setVisibility(View.GONE);
                    orderView.setVisibility(View.GONE);
                    productView.setVisibility(View.GONE);
                    personalcenterView.setVisibility(View.GONE);
                    specialView.setVisibility(View.VISIBLE);
                    return true;
                case R.id.navigation_notifications:
                    contralPersonalcenter();
                    fab.setVisibility(View.GONE);
                    orderView.setVisibility(View.GONE);
                    productView.setVisibility(View.GONE);
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

        SharedPreferences pref = this.getSharedPreferences("ifFirstUse" , MODE_PRIVATE);
        if(pref.getString("iffirstuse",null) == null){
            //id为空，用户是首次使用应用
            StepDialog.getInstance()
                    .setImages(new int[]{R.drawable.welcome, R.drawable.background, R.drawable.background_blue, R.drawable.update})
                    .show(getFragmentManager());
            SharedPreferences.Editor mEditor = pref.edit();
            mEditor.putString("iffirstuse", "used");
            //存入id
            mEditor.apply();
        }


        SharedPrefsCookieUtil scookie=new SharedPrefsCookieUtil(this);
        httpUtil = new HttpUtil(scookie);

        toolbar = (Toolbar)findViewById(R.id.Toolbar);
        orderView=findViewById(R.id.main_order);
        productView=findViewById(R.id.main_products);
        personalcenterView=findViewById(R.id.main_personalcenter);
        specialView=findViewById(R.id.main_special);
        show_login = (TextView)findViewById(R.id.order_show_login);
        orderswipe = (SwipeRefreshLayout)findViewById(R.id.order_swipe);
        productswipe = (SwipeRefreshLayout)findViewById(R.id.product_swipe);

        contralToolbar();
        relogin();//判断本地是否登录 如果登陆向服务端发送一次请求
        contralFab();
        productRefresh(productswipe);
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
        fbbtn_question.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new  AlertDialog.Builder(MainActivity.this)
                        .setTitle("打开小助手" )
                        .setMessage("确定吗？" )
                        .setPositiveButton("是", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(MainActivity.this,HelperActivity.class);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("否" , null)
                        .show();
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
                    break;
                case R.id.toolbar_search:
                    Intent intentsearch = new Intent(MainActivity.this, SearchActivity.class);
                    startActivity(intentsearch);
                    break;
            } return true;
            }
        });
    }
    //产品首页信息
    public void controlProduct(){
        productRefresh(productswipe);
        productswipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                productRefresh(productswipe);
            }
        });
    }
    //个人中心信息
    public void contralPersonalcenter(){

        scr_personal_center =(ScrollView)findViewById(R.id.person_detail_center_scroll);
        RoundedImage = (ImageView)findViewById(R.id.personalcenter_roundedmage);
        IfLogin = (TextView)findViewById(R.id.personalcenter_iflogin);
        Point = (TextView)findViewById(R.id.personalcenter_point);

        btn_logout=(Button)findViewById(R.id.personalcenter_logout);


        SharedPreferences sharedPre=getSharedPreferences("LoginManager", MODE_PRIVATE);
        String username=sharedPre.getString("username", "");
        String userimage=sharedPre.getString("userimage", "");
        if(username.equals("")){
            IfLogin.setText("未登录");
            btn_logout.setVisibility(View.GONE);
        }
        else {
            IfLogin.setText(" 欢迎用户 "+username+" 登录");

            Glide.with(this)
                    .load(userimage)
                    .placeholder(R.drawable.eat)
                    .crossFade()
                    .into(RoundedImage);
            btn_logout.setVisibility(View.VISIBLE);

        }
        IfLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPre = getSharedPreferences("LoginManager", MODE_PRIVATE);
                final String username = sharedPre.getString("username", "");
                String password = sharedPre.getString("password", "");
                if(!username.equals("")) {
                    Intent intent = new Intent(MainActivity.this, PersonDetailActivity.class);
                    startActivity(intent);
                }else{
                    Toasty.warning(getApplicationContext(), "尚未登陆", Toast.LENGTH_SHORT, true).show();
//                    Toast.makeText(getApplicationContext(), "尚未登陆", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            }
        });
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
                        Toasty.error(getApplicationContext(), "账号或密码错误", Toast.LENGTH_SHORT, true).show();
//                        Toast.makeText(getApplicationContext(), "账号或密码错误", Toast.LENGTH_SHORT).show();
                        break;
                    case "connfail":
                        Toasty.error(getApplicationContext(), "连接超时", Toast.LENGTH_SHORT, true).show();
//                        Toast.makeText(getApplicationContext(), "连接超时", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        Toasty.success(getApplicationContext(), "欢迎回来 "+ username, Toast.LENGTH_SHORT, true).show();
//                        Toast.makeText(getApplicationContext(), "欢迎回来 " + username, Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        };
        if (!(username.equals("") || password.equals(""))) {
            httpUtil.PostURL("http://119.23.205.112:8080/eatCanteen_war/LoginServlet", "accountNumber=" + username + "&password=" + password + "&type=login", ifloginhandle);
        }
    }
    //退出时清除本地保存的用户信息
    public void cleanShare(){ //清除SharedPreferences中LoginManager数据

        ListView order_listview = (ListView)findViewById(R.id.order_listview);
        TextView order_show_login=(TextView)findViewById(R.id.order_show_login);
        order_listview.setVisibility(View.GONE);
        order_show_login.setText("尚未登陆请登录");
//        order_listview.setAdapter(null);
//        ArrayAdapter adapter = (ArrayAdapter) order_listview.getAdapter();// 获取当前listview的adapter
//        if(adapter!=null){
//            int count = adapter.getCount();// listview多少个组件
//            if (count > 0) {
//                order_listview.setAdapter(new ArrayAdapter<String>(this,
//                        android.R.layout.simple_list_item_1));
//            }
//        }

        Handler logouthander = new Handler(){
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                String type=(String)msg.obj;

                if(msg.obj==null){
                    Toasty.error(getApplicationContext(), "服务端连接失败", Toast.LENGTH_SHORT, true).show();
//                    Toast.makeText(getApplicationContext(), "服务端连接失败", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(type.equals("ok")){
                    IfLogin.setText("未登录");

                    btn_logout.setVisibility(View.GONE);
                    RoundedImage.setImageDrawable(getResources().getDrawable(R.drawable.eat));
                    SharedPreferences sharedPre = getSharedPreferences("LoginManager", MODE_PRIVATE);;
                    SharedPreferences.Editor editor=sharedPre.edit();
                    editor.clear();
                    editor.apply();

                    show_login.setVisibility(View.GONE);
                    orderswipe.setVisibility(View.VISIBLE);
                    Toasty.info(getApplicationContext(), "已登出", Toast.LENGTH_SHORT, true).show();
//                    Toast.makeText(getApplicationContext(), "已登出", Toast.LENGTH_LONG).show();
                }
                else
                    Toasty.error(getApplicationContext(), "服务端登出失败", Toast.LENGTH_SHORT, true).show();
//                    Toast.makeText(getApplicationContext(), "服务端登出失败", Toast.LENGTH_LONG).show();
            }

        };
        HttpUtil httpUtil = new HttpUtil();
        httpUtil.PostURL("http://119.23.205.112:8080/eatCanteen_war/LoginServlet","type=logout",logouthander);
    }
    //刷新订单基于contralOrder
    private void orderRefresh(final SwipeRefreshLayout orderswipe){
        final ImageView ic_down;
        final View footView;

        footView = getLayoutInflater().inflate(R.layout.item_view_foot, null);
        ic_down = (ImageView) footView.findViewById(R.id.ic_down);

        SharedPreferences sharedPre=getSharedPreferences("LoginManager", MODE_PRIVATE);
        final String username=sharedPre.getString("username", "");
        if(username.equals("")){
            Toasty.warning(getApplicationContext(), "您还未登录呢!", Toast.LENGTH_SHORT, true).show();
//            Toast.makeText(getApplicationContext(), "您还未登录呢！", Toast.LENGTH_SHORT).show();
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
                final LinkedList<OrdersBean> linkedList=new LinkedList<>();
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
                Collections.reverse(linkedList);//订单倒序使得最新下单最先展示

                final OrdersAdapter ordersAdapter=new OrdersAdapter(linkedList,MainActivity.this);

                if(order_listview.getFooterViewsCount() > 0) {
                    order_listview.removeFooterView(footView);
                }else if(order_listview.getFooterViewsCount() == 0) {
                    order_listview.addFooterView(footView);
                    ordersAdapter.notifyDataSetChanged();
                }


                order_listview.setAdapter(ordersAdapter);
                orderswipe.setRefreshing(false);

                ic_down.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // 判断getCount()数据的数量，如果等于3点击后就设置getCount()为全部数量，设置修改标识，刷新。
                        // 否则，相反。
                        if (ordersAdapter.getCount() == 3) {
                            ordersAdapter.addItemNum(linkedList.size());
                            order_listview.removeFooterView(footView);
                            ordersAdapter.notifyDataSetChanged();
                        } else {
                            ordersAdapter.addItemNum(3);
                            ic_down.setImageDrawable(getResources().getDrawable(
                                    R.mipmap.ic_down));
                            ordersAdapter.notifyDataSetChanged();
                        }
                    }
                });
            }
        };
        HttpUtil httpUtil = new HttpUtil();
        httpUtil.PostURL("http://119.23.205.112:8080/eatCanteen_war/OrdersServlet","type=list",orderlisthandler);
    }
    //刷新产品基于controlProduct
    private void productRefresh(final SwipeRefreshLayout productswipe){

        product_listview = (ListView)findViewById(R.id.product_listview);
        Handler productlisthandler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                LinkedList linkedList=new LinkedList();
                JSONArray jsonArray;
                try {
                    jsonArray = new JSONArray((String) msg.obj);
                    for(int i = 0;i < jsonArray.length();i++) {
                        JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                        ProductBean productBean=new ProductBean();
                        productBean.setProduct_id(jsonObject.getInt("product_id"));
                        productBean.setProduct_name(jsonObject.getString("product_name"));
                        productBean.setProduct_details(jsonObject.getString("product_details"));
                        productBean.setProduct_address(jsonObject.getString("product_address"));
                        productBean.setProduct_image(jsonObject.getString("product_image"));
                        productBean.setProduct_type(jsonObject.getInt("product_type"));
                        productBean.setProduct_price(jsonObject.getDouble("product_price"));
                        linkedList.add(productBean);
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }

                ProductMainAdapter ordersAdapter=new ProductMainAdapter((LinkedList<ProductBean>)linkedList,MainActivity.this);
                product_listview.setAdapter(ordersAdapter);
                productswipe.setRefreshing(false);

            }
        };
        HttpUtil httpUtil = new HttpUtil();
        httpUtil.PostURL("http://119.23.205.112:8080/eatCanteen_war/ProductServlet","type=list"+"&address=1",productlisthandler);
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
            Intent intent;
            switch (position){
                case 0:
                    break;
                case 1:
                    intent = new Intent(MainActivity.this, VoteActivity.class);
                    startActivity(intent);
                    break;
                case 2:
                    break;
                case 3:
                    break;
                case 4:
                    intent = new Intent(MainActivity.this, SeatActivity.class);
                    startActivity(intent);
                    break;
            }
            Toast.makeText(getApplicationContext(), "点击"+item.get("ItemText"), Toast.LENGTH_SHORT).show();
        }
    }
    //首页轮播
//    public void rollpagerview(){
//        product_pager = (RollPagerView) findViewById(R.id.roll_view_pager);
//        //设置播放时间间隔
//        product_pager.setPlayDelay(2000);
//        //设置透明度
//        product_pager.setAnimationDurtion(500);
//        //设置适配器
//        product_pager.setAdapter(new RollpagerviewAdapter());
//
//        //设置指示器（顺序依次）
//        //自定义指示器图片
//        //设置圆点指示器颜色
//        //设置文字指示器
//        //隐藏指示器
//        //mRollViewPager.setHintView(new IconHintView(this, R.drawable.point_focus, R.drawable.point_normal));
//        product_pager.setHintView(new ColorPointHintView(this, Color.YELLOW,Color.WHITE));
//        //mRollViewPager.setHintView(new TextHintView(this));
//        //mRollViewPager.setHintView(null);
//    }
}

