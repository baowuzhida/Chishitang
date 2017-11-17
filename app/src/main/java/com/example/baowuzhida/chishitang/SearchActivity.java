package com.example.baowuzhida.chishitang;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSS;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import Adapter.ProductAllAdapter;
import Bean.ProductBean;
import Link.HttpUtil;
import Link.InitOssClient;
import es.dmoral.toasty.Toasty;

/**
 * Created by Baowuzhida on 2017/7/29.
 */

public class SearchActivity extends Activity {

    private ListView mListView;
    private ArrayAdapter searchadapter;
    private List<String> search = new ArrayList<>();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        SearchView mSearchView = (SearchView) findViewById(R.id.search_searchView);
        mListView = (ListView) findViewById(R.id.search_listView);
        TextView search_clearsearch = (TextView) findViewById(R.id.search_clearsearch);

//        search = new ArrayList<>();

        SharedPreferences sharedPre=getSharedPreferences("SearchHistroy", MODE_PRIVATE);
        String s=sharedPre.getString("search_history", "");
        String[] searchdetail = s.split(",");
        for(int i=0;i<searchdetail.length;i++)
        {
            search.add(searchdetail[i]);
        }

        searchadapter  =new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, search);
        mListView.setAdapter(searchadapter);
        mListView.setTextFilterEnabled(true);

        if(search.size()>0){
            search_clearsearch.setVisibility(View.VISIBLE);
        }
        search_clearsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPre=getSharedPreferences("SearchHistroy", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPre.edit();
                editor.clear();
                editor.apply();

                search.clear();
                String s=sharedPre.getString("search_history", "");
                String[] searchdetail = s.split(",");
                for(int i=0;i<searchdetail.length;i++)
                {
                    search.add(searchdetail[i]);
                }
                Toasty.success(getApplicationContext(),"已清除", Toast.LENGTH_SHORT, true).show();
//                Toast.makeText(getApplicationContext(),"已清除",Toast.LENGTH_SHORT).show();
                searchadapter.notifyDataSetChanged();
            }
        });
        // 设置搜索文本监听
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            // 当点击搜索按钮时触发该方法
            @Override
            public boolean onQueryTextSubmit(String query) {

//                Toast.makeText(getApplicationContext(),"搜索"+query,Toast.LENGTH_SHORT).show();

                //获取SharedPreferences对象
                SharedPreferences sharedPre=getSharedPreferences("SearchHistroy", MODE_PRIVATE);

                String oldText = sharedPre.getString("search_history", "");
                StringBuilder builder = new StringBuilder(query);
                builder.append(",").append(oldText);
                if (!TextUtils.isEmpty(query) && !oldText.contains(query + ",")) {
                    SharedPreferences.Editor myEditor = sharedPre.edit();
                    myEditor.putString("search_history", builder.toString());
                    myEditor.apply();
                }

                String s=sharedPre.getString("search_history", "");
                String[] searchdetail = s.split(",");
                for(int i=0;i<searchdetail.length;i++)
                {
                    search.add(searchdetail[i]);
                }
                searchadapter.notifyDataSetChanged();

                translateMessage(query);

                Handler handler=new Handler(){
                    @Override
                    public void handleMessage(Message msg) {
                        super.handleMessage(msg);
                        LinkedList linkedList=new LinkedList();
                        JSONArray jsonArray;
                        String ss=(String)msg.obj;
                        String[] Data = ss.split("##");
                        try {
                            jsonArray = new JSONArray(Data[0]);
                            for(int i = 0;i < jsonArray.length();i++) {
                                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                                ProductBean productBean=new ProductBean();
                                productBean.setProduct_id(jsonObject.getInt("product_id"));
                                productBean.setProduct_name(jsonObject.getString("product_name"));
                                productBean.setProduct_details(jsonObject.getString("product_details"));
                                productBean.setProduct_address(jsonObject.getString("product_address"));

                                final InitOssClient initOssClient = new InitOssClient();
                                String url= null;
                                OSS oss = initOssClient.getOss(getApplicationContext(),Data[1],Data[2],Data[3]);
                                String obk = jsonObject.getString("product_image");
                                try {
                                    url = oss.presignConstrainedObjectURL("chishitang",obk,1000);
                                } catch (ClientException e) {
                                    e.printStackTrace();
                                }

                                productBean.setProduct_image(url);
                                productBean.setProduct_type(jsonObject.getInt("product_type"));
                                productBean.setProduct_price(jsonObject.getDouble("product_price"));
                                linkedList.add(productBean);
                            }
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                        ProductAllAdapter productAllAdapter =new ProductAllAdapter((LinkedList<ProductBean>)linkedList,SearchActivity.this);
                        mListView.setAdapter(productAllAdapter);
                    }
                };
                HttpUtil httpUtil=new HttpUtil();
                httpUtil.PostURL("http://119.23.205.112:8080/eatCanteen_war/ProductServlet","type=name&name="+query,handler);
                return false;
            }
            // 当搜索内容改变时触发该方法
            @Override
            public boolean onQueryTextChange(String newText) {
                if (!TextUtils.isEmpty(newText)){
                    mListView.setFilterText(newText);
                }else{
                    mListView.clearTextFilter();
                }
                return false;
            }
        });
    }

    public void translateMessage(String message){
        //获取SharedPreferences对象
        SharedPreferences sharedPre=getSharedPreferences("SearchManager", MODE_PRIVATE);
        //获取Editor对象
        SharedPreferences.Editor editor=sharedPre.edit();
        //设置参数
        editor.putString("searchmessage", message);
        //提交
        editor.apply();
    }

}
