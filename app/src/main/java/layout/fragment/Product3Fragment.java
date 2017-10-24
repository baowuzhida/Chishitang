package layout.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.baowuzhida.chishitang.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Product3Fragment extends Fragment {

    private static final int REFRESH_COMPLETE = 0X110;
    private SwipeRefreshLayout mSwipeLayout;
    private GridView textgirdview;
    private ArrayAdapter<String> mAdapter;
    private List<String> mDatas = new ArrayList<String>(Arrays.asList("Java", "Javascript", "C++", "Ruby", "Json", "HTML"));
    private Handler mHandler = new Handler()
    {
        public void handleMessage(android.os.Message msg)
        {
            switch (msg.what)
            {
                case REFRESH_COMPLETE:
                    mDatas.addAll(Arrays.asList("Lucene", "Canvas", "Bitmap"));

                    //每次刷新时执行的代码
                    mAdapter.notifyDataSetChanged();
                    mSwipeLayout.setRefreshing(false);
                    break;

            }
        };
    };

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View view=inflater.inflate(R.layout.fragment_product3, container, false);

        textgirdview = (GridView) view.findViewById(R.id.textgirdview);
        mSwipeLayout = (SwipeRefreshLayout)view.findViewById(R.id.fragment3swipe);
        mAdapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_list_item_1, mDatas);
        textgirdview.setAdapter(mAdapter);

        mSwipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mHandler.sendEmptyMessageDelayed(REFRESH_COMPLETE, 2000);
            }
        });

        return view;
    }
}
