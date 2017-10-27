package com.example.baowuzhida.chishitang;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.GridView;
import android.widget.SearchView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.LinkedList;

import Adapter.VoteAdapter;
import Bean.FavoriteProduct;
import Bean.ProductBean;
import Link.HttpUtil;

/**
 * Created by Baowuzhida on 2017/8/13.
 */

public class VoteActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private GridView mGridView;
    private SearchView mSearchView;
    private SwipeRefreshLayout vote_list_swipe;
    private LinkedList<FavoriteProduct> linkedList;
    private HttpUtil httpUtil=new HttpUtil();
    private static VoteActivity voteActivity ;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vote);
        mGridView=(GridView)findViewById(R.id.votelist_gridbview);
        mSearchView=(SearchView)findViewById(R.id.special_searchView);
        vote_list_swipe=(SwipeRefreshLayout)findViewById(R.id.vote_list_swipe);
        voteActivity=this;

        toolbar = (Toolbar)findViewById(R.id.vote_toolbar);
        toolbar.setNavigationIcon(R.mipmap.ic_back);
        toolbar.setTitle("喜爱食物投票");
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        refreshList();

        // 设置搜索文本监听
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            // 当点击搜索按钮时触发该方法
            @Override
            public boolean onQueryTextSubmit(final String query) {
                Handler vote_search_handler=new Handler(){
                    @Override
                    public void handleMessage(Message msg) {
                        super.handleMessage(msg);
                        String type=(String)msg.obj;
                        if(type.equals("no"))
                            Toast.makeText(getApplicationContext(),"未搜索到"+query,Toast.LENGTH_SHORT).show();
                        else
                        {
                            try {
                                JSONObject jsonObject = new JSONObject(type);
                                FavoriteProduct favoriteProduct=new FavoriteProduct();
                                favoriteProduct.setFproduct_id(jsonObject.getInt("fproduct_id"));
                                favoriteProduct.setNumber_votes(jsonObject.getInt("number_votes"));
                                favoriteProduct.setTime(jsonObject.getString("time"));

                                JSONObject json=jsonObject.getJSONObject("product");
                                ProductBean productBean=new ProductBean();
                                productBean.setProduct_id(json.getInt("product_id"));
                                productBean.setProduct_name(json.getString("product_name"));
                                productBean.setProduct_details(json.getString("product_details"));
                                productBean.setProduct_image(json.getString("product_image"));
                                productBean.setProduct_price(json.getDouble("product_price"));
                                productBean.setProduct_type(json.getInt("product_type"));
                                productBean.setProduct_address(json.getString("product_address"));
                                favoriteProduct.setProduct(productBean);

                                Intent intent = new Intent(VoteActivity.this,VoteDetailActivity.class);
                                intent.putExtra("favourite_product_id",favoriteProduct.getFproduct_id());
                                intent.putExtra("vote_image",favoriteProduct.getProduct().getProduct_image());
                                intent.putExtra("vote_name",favoriteProduct.getProduct().getProduct_name());
                                intent.putExtra("vote_number",favoriteProduct.getNumber_votes());
                                intent.putExtra("vote_details",favoriteProduct.getProduct().getProduct_details());
                                startActivity(intent);
                            }
                            catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                    }
                };
                httpUtil.PostURL("http://119.23.205.112:8080/eatCanteen_war/FavoriteProductServlet","type=search&name="+query,vote_search_handler);
                return false;
            }

            // 当搜索内容改变时触发该方法
            @Override
            public boolean onQueryTextChange(String newText) {
                if (!TextUtils.isEmpty(newText)){
                    mGridView.setFilterText(newText);
                }else{
                    mGridView.clearTextFilter();
                }
                return false;
            }
        });

        vote_list_swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshList();
                vote_list_swipe.setRefreshing(false);
            }
        });

    }

    public static VoteActivity  getObj(){
        return voteActivity ;
    }

    public void refreshList(){
        final Handler vote_list_handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if(msg ==null){
                    Toast.makeText(getApplicationContext(),"连接超时",Toast.LENGTH_SHORT).show();
                    return;
                }
                linkedList=new LinkedList<>();
                JSONArray jsonArray;
                try {
                    jsonArray = new JSONArray((String) msg.obj);
                    for(int i = 0;i < jsonArray.length();i++) {
                        JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                        FavoriteProduct favoriteProduct=new FavoriteProduct();
                        favoriteProduct.setFproduct_id(jsonObject.getInt("fproduct_id"));
                        favoriteProduct.setNumber_votes(jsonObject.getInt("number_votes"));
                        favoriteProduct.setTime(jsonObject.getString("time"));

                        JSONObject json=jsonObject.getJSONObject("product");
                        ProductBean productBean=new ProductBean();
                        productBean.setProduct_id(json.getInt("product_id"));
                        productBean.setProduct_name(json.getString("product_name"));
                        productBean.setProduct_details(json.getString("product_details"));
                        productBean.setProduct_image(json.getString("product_image"));
                        productBean.setProduct_price(json.getDouble("product_price"));
                        productBean.setProduct_type(json.getInt("product_type"));
                        productBean.setProduct_address(json.getString("product_address"));
                        favoriteProduct.setProduct(productBean);
                        linkedList.add(favoriteProduct);
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                VoteAdapter voteAdapter = new VoteAdapter(linkedList, VoteActivity.this);
                mGridView.setAdapter(voteAdapter);
            }
        };
        httpUtil.PostURL("http://119.23.205.112:8080/eatCanteen_war/FavoriteProductServlet","type=list",vote_list_handler);
    }
}
