package com.example.baowuzhida.chishitang;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import Adapter.ShoppingCartAdapter;
import Bean.BaseActivity;
import Bean.OrdersDetailsBean;
import Bean.ShoppingCartBean;
import Link.CartDao;
import Link.HttpUtil;

/**
 * Created by AYD on 2016/11/21.
 * 购物车页面
 */
public class ShoppingCartActivity extends BaseActivity implements View.OnClickListener
        , ShoppingCartAdapter.CheckInterface, ShoppingCartAdapter.ModifyCountInterface {

    private CartDao cartDao = new CartDao(this);
    private HttpUtil httpUtil = new HttpUtil();
    public TextView tv_show_price;
    private Button tv_settlement;
    private CheckBox ck_all;
    private ListView list_shopping_cart;
    private ShoppingCartAdapter shoppingCartAdapter;
    private TextView tv_edit;
    private boolean flag = false;
    private List<ShoppingCartBean> shoppingCartBeanList = new ArrayList<>();
    private double totalPrice = 0.00;// 购买的商品总价
    private int totalCount = 0;// 购买的商品总数量
    /**
     * 批量模式下，用来记录当前选中状态
     */
    private SparseArray<Boolean> mSelectState = new SparseArray<Boolean>();


    @Override
    protected int getLayout() {
        return R.layout.activity_shoppingcar;
    }

    @Override
    protected void initView() {
        list_shopping_cart = bindView(R.id.list_shopping_cart);
        ck_all = bindView(R.id.ck_all);
        ck_all.setOnClickListener(this);
        tv_show_price = bindView(R.id.tv_show_price);
        tv_settlement = bindView(R.id.tv_settlement);
        tv_settlement.setOnClickListener(this);
        tv_edit = bindView(R.id.tv_edit);
        tv_edit.setOnClickListener(this);
        shoppingCartAdapter = new ShoppingCartAdapter(this);
        shoppingCartAdapter.setCheckInterface(this);
        shoppingCartAdapter.setModifyCountInterface(this);
        list_shopping_cart.setAdapter(shoppingCartAdapter);
        shoppingCartAdapter.setShoppingCartBeanList(shoppingCartBeanList);

    }

    @Override
    protected void initData() {
        ArrayList<ShoppingCartBean> cartBeanArrayList =null;
        cartBeanArrayList = cartDao.dbQueryAll();
        if(cartBeanArrayList.size()==0) {
            Toast.makeText(ShoppingCartActivity.this,"购物车空空如也~", Toast.LENGTH_SHORT).show();
        }else{
            for (int i = 0; i < cartBeanArrayList.size(); i++) {
                ShoppingCartBean shoppingCartBean = new ShoppingCartBean();
                shoppingCartBean.setProduct_id(cartBeanArrayList.get(i).getProduct_id());
                shoppingCartBean.setShoppingName(cartBeanArrayList.get(i).getShoppingName());
                shoppingCartBean.setImageUrl(cartBeanArrayList.get(i).getImageUrl());
                shoppingCartBean.setDetail(cartBeanArrayList.get(i).getDetail());
                shoppingCartBean.setAddress(cartBeanArrayList.get(i).getAddress());
                shoppingCartBean.setPrice(cartBeanArrayList.get(i).getPrice());
                shoppingCartBean.setCount(cartBeanArrayList.get(i).getCount());
                shoppingCartBeanList.add(shoppingCartBean);
            }
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //全选按钮
            case R.id.ck_all:
                if (shoppingCartBeanList.size() != 0) {
                    if (ck_all.isChecked()) {
                        for (int i = 0; i < shoppingCartBeanList.size(); i++) {
                            shoppingCartBeanList.get(i).setChoosed(true);
                        }
                        shoppingCartAdapter.notifyDataSetChanged();
                    } else {
                        for (int i = 0; i < shoppingCartBeanList.size(); i++) {
                            shoppingCartBeanList.get(i).setChoosed(false);
                        }
                        shoppingCartAdapter.notifyDataSetChanged();
                    }
                }
                statistics();
                break;
            case R.id.tv_edit:
                flag = !flag;
                if (flag) {
                    tv_edit.setText("完成");
                    shoppingCartAdapter.isShow(false);
                } else {
                    tv_edit.setText("编辑");
                    shoppingCartAdapter.isShow(true);
                }
                break;
            case R.id.tv_settlement:
                tv_settlement = (Button)findViewById(R.id.tv_settlement);
                tv_settlement.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final ArrayList<OrdersDetailsBean> a = new ArrayList<>();
                        int x = 0;
                        for (int i = 0; i < shoppingCartBeanList.size(); i++) {
                            ShoppingCartBean shoppingCartBean = shoppingCartBeanList.get(i);
                            if (shoppingCartBean.isChoosed()) {
                                OrdersDetailsBean ordersDetailsBean = new OrdersDetailsBean();
                                ordersDetailsBean.getProductBean().setProduct_id(shoppingCartBean.getProduct_id());
                                ordersDetailsBean.setProduct_amount(shoppingCartBean.getCount());
                                a.add(ordersDetailsBean);
                                x++;
                            }
                        }
                        if(x==0){
                            Toast.makeText(getApplicationContext(), "您还未选择商品", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        final JSONArray jsonArray = new JSONArray();
                        try {
                            for (int i = 0; i < a.size(); i++) {
                                JSONObject jsonObject = new JSONObject();
                                jsonObject.put("orders_detail_id", a.get(i).getOrders_detail_id());
                                jsonObject.put("orders_id", a.get(i).getOrders_id());
                                jsonObject.put("product_amount", a.get(i).getProduct_amount());
                                JSONObject json = new JSONObject();
                                json.put("product_id",a.get(i).getProductBean().getProduct_id());
                                json.put("product_address",a.get(i).getProductBean().getProduct_address());
                                json.put("product_details",a.get(i).getProductBean().getProduct_details());
                                json.put("product_image",a.get(i).getProductBean().getProduct_image());
                                json.put("product_name",a.get(i).getProductBean().getProduct_name());
                                json.put("product_type",a.get(i).getProductBean().getProduct_type());
                                json.put("product_price",a.get(i).getProductBean().getProduct_price());
                                jsonObject.put("productBean",json);
                                jsonArray.put(jsonObject);
                            }
                        }
                        catch(Exception ignored)
                        {

                        }

                        new AlertDialog.Builder(ShoppingCartActivity.this)
                                .setTitle("结算！")
                                .setMessage("确定吗？")
                                .setPositiveButton("是", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {


                                        Handler orderhandler = new Handler(){
                                            public void handleMessage(Message msg){
                                                super.handleMessage(msg);
                                                String message= (String)msg.obj;
                                                if(msg.obj==null){
                                                    Toast.makeText(getApplicationContext(), "服务端连接失败", Toast.LENGTH_SHORT).show();
                                                }
                                                else if(message.equals("ok")){
                                                    Handler positionhandler = new Handler(){
                                                        public void handleMessage(Message msg) {
                                                            super.handleMessage(msg);
                                                            Integer position = (Integer) msg.obj;
                                                            CheckOut(a, position);
                                                            Toast.makeText(ShoppingCartActivity.this, "已结账", Toast.LENGTH_SHORT).show();
                                                        }
                                                    };
                                                    shoppingCartAdapter.PostPosition(positionhandler);
                                                }

                                            }
                                        };
                                        httpUtil.PostURL("http://119.23.205.112:8080/eatCanteen_war/OrdersServlet"
                                                ,"ArrayListOrderDetail="+jsonArray.toString()+"&totalPrice="+totalPrice+"&type=add",orderhandler);



                                    }
                                })
                                .setNegativeButton("否", null)
                                .show();
                    }
                });
        }
    }



    /**
     * 单选
     *
     * @param position  组元素位置
     * @param isChecked 组元素选中与否
     */
    @Override
    public void checkGroup(int position, boolean isChecked) {

        shoppingCartBeanList.get(position).setChoosed(isChecked);

        if (isAllCheck())
            ck_all.setChecked(true);
        else
            ck_all.setChecked(false);

        shoppingCartAdapter.notifyDataSetChanged();
        statistics();
    }


    /**
     * 遍历list集合
     *
     * @return
     */
    private boolean isAllCheck() {

        for (ShoppingCartBean group : shoppingCartBeanList) {
            if (!group.isChoosed())
                return false;
        }
        return true;
    }

    /**
     * 统计操作
     * 1.先清空全局计数器<br>
     * 2.遍历所有子元素，只要是被选中状态的，就进行相关的计算操作
     * 3.给底部的textView进行数据填充
     */
    public void statistics() {
        totalCount = 0;
        totalPrice = 0.00;
        for (int i = 0; i < shoppingCartBeanList.size(); i++) {
            ShoppingCartBean shoppingCartBean = shoppingCartBeanList.get(i);
            if (shoppingCartBean.isChoosed()) {
                totalCount++;
                totalPrice += shoppingCartBean.getPrice() * shoppingCartBean.getCount();
            }
        }
        tv_show_price.setText("合计:" + totalPrice);
        tv_settlement.setText("结算(" + totalCount + ")");
    }

    /**
     * 增加
     *
     * @param position      组元素位置
     * @param showCountView 用于展示变化后数量的View
     * @param isChecked     子元素选中与否
     */
    @Override
    public void doIncrease(int position, View showCountView, boolean isChecked) {
        ShoppingCartBean shoppingCartBean = shoppingCartBeanList.get(position);
        int currentCount = shoppingCartBean.getCount();
        currentCount++;
        shoppingCartBean.setCount(currentCount);
        ((TextView) showCountView).setText(currentCount + "");
        shoppingCartAdapter.notifyDataSetChanged();
        statistics();
    }

    /**
     * 删减
     *
     * @param position      组元素位置
     * @param showCountView 用于展示变化后数量的View
     * @param isChecked     子元素选中与否
     */
    @Override
    public void doDecrease(int position, View showCountView, boolean isChecked) {
        ShoppingCartBean shoppingCartBean = shoppingCartBeanList.get(position);
        int currentCount = shoppingCartBean.getCount();
        if (currentCount == 1) {
            return;
        }
        currentCount--;
        shoppingCartBean.setCount(currentCount);
        ((TextView) showCountView).setText(currentCount + "");
        shoppingCartAdapter.notifyDataSetChanged();
        statistics();
    }

    /**
     * 删除
     *
     * @param position
     */
    @Override
    public void childDelete(int position) {
        shoppingCartBeanList.remove(position);
        shoppingCartAdapter.notifyDataSetChanged();
        statistics();
    }
    /**
     * 删除结算后的
     *
     * @param position
     */
    public void CheckOut(ArrayList a,int position){
        for(int i=0;i<a.size();i++) {
            OrdersDetailsBean ordersDetailsBean = (OrdersDetailsBean)a.get(i);
            int productid = ordersDetailsBean.getProductBean().getProduct_id();
            shoppingCartBeanList.remove(position);
            cartDao.dbDeleteCart(productid);
        }
        shoppingCartAdapter.notifyDataSetChanged();
        statistics();
    }


}

