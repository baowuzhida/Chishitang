package layout.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import Adapter.ProductAllAdapter;

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSS;
import com.example.baowuzhida.chishitang.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.LinkedList;

import Bean.ProductBean;
import Link.HttpUtil;
import Link.InitOssClient;


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

    public void productlist(){

        product_listview = (ListView)view.findViewById(R.id.product_listview);
        Handler productlisthandler=new Handler(){
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
                        OSS oss = initOssClient.getOss(getContext(),Data[1],Data[2],Data[3]);
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

                ProductAllAdapter productAllAdapter=new ProductAllAdapter((LinkedList<ProductBean>)linkedList,getActivity());
                product_listview.setAdapter(productAllAdapter);

            }
        };
        httpUtil.PostURL("http://119.23.205.112:8080/eatCanteen_war/ProductServlet","type=list"+"&address=1",productlisthandler);
    }
}
