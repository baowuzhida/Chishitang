package layout.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.baowuzhida.chishitang.R;

import static android.content.Context.MODE_PRIVATE;

public class Product2Fragment extends android.support.v4.app.Fragment {
    private SwipeRefreshLayout mSwipeLayout;
    private TextView specialview;
    private View view;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        view=inflater.inflate(R.layout.fragment_product2, container, false);
        mSwipeLayout = (SwipeRefreshLayout)view.findViewById(R.id.fragment2swipe);
        mSwipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                SharedPreferences sharedPre=getActivity().getSharedPreferences("LoginManager", MODE_PRIVATE);
                String username=sharedPre.getString("username", "");
                specialview = (TextView)view.findViewById(R.id.specialview);
                if(username.equals("")){
                    specialview.setText("未登录");
                    mSwipeLayout.setRefreshing(false);
                }
                else{
                    specialview.setText(username+"已登录");
                    mSwipeLayout.setRefreshing(false);
                }
                Toast.makeText(getActivity(), "已刷新", Toast.LENGTH_LONG).show();
            }
        });
        iflogin();
        return view;
    }

    public void iflogin(){  //判断是否已经登录 如果登录则直接显示用户信息 没有则跳转Login
        SharedPreferences sharedPre=getActivity().getSharedPreferences("LoginManager", MODE_PRIVATE);
        String username=sharedPre.getString("username", "");
        specialview = (TextView)view.findViewById(R.id.specialview);
        if(username.equals("")){
            specialview.setText("未登录");
        }
        else
            specialview.setText("已登录");
    }

}
