package layout.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.baowuzhida.chishitang.ProductAdapter;
import com.example.baowuzhida.chishitang.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.LinkedList;

import Bean.ProductBean;
import Link.HttpUtil;

import static android.content.Context.MODE_PRIVATE;

public class ProductFragment extends android.support.v4.app.Fragment {


    private View view;
    private ListView product_listview;
    private HttpUtil httpUtil = new HttpUtil();

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        view=inflater.inflate(R.layout.fragment_product, container, false);
        return view;
    }
    //测试fragment中按钮用处
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        productlist();
        final SwipeRefreshLayout productswipe = (SwipeRefreshLayout)view.findViewById(R.id.product_swipe) ;
        productswipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                productlist();
                productswipe.setRefreshing(false);
            }
        });

    }

    public void cleanShare(){ //清除SharedPreferences中LoginManager数据

        SharedPreferences sharedPre = getActivity().getSharedPreferences("LoginManager", MODE_PRIVATE);;
        SharedPreferences.Editor editor=sharedPre.edit();
        editor.clear();
        editor.apply();

        ListView order_listview = (ListView)getActivity().findViewById(R.id.order_listview);
        order_listview.setAdapter(null);
        ArrayAdapter adapter = (ArrayAdapter) order_listview.getAdapter();// 获取当前listview的adapter
            if(adapter!=null){
        int count = adapter.getCount();// listview多少个组件
        if (count > 0) {
            order_listview.setAdapter(new ArrayAdapter<String>(getContext(),
                    android.R.layout.simple_list_item_1));
        }
        }

        Handler logouthander = new Handler(){
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                String type=(String)msg.obj;
                if(type.equals("ok")){
                    Toast.makeText(getActivity(), "已登出", Toast.LENGTH_LONG).show();
                }
                else
                    Toast.makeText(getActivity(), "服务端登出失败", Toast.LENGTH_LONG).show();
            }

        };
        httpUtil.PostURL("http://119.23.205.112:8080/eatCanteen_war/LoginServlet","type=logout",logouthander);
    }

    public void productlist(){
        product_listview = (ListView)view.findViewById(R.id.product_listview);
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

                ProductAdapter ordersAdapter=new ProductAdapter((LinkedList<ProductBean>)linkedList,getActivity());
                product_listview.setAdapter(ordersAdapter);

            }
        };
        httpUtil.PostURL("http://119.23.205.112:8080/eatCanteen_war/ProductServlet","type=list"+"&address=1",productlisthandler);
    }
}
