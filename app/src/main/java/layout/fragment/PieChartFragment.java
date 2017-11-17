package layout.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.baowuzhida.chishitang.MainActivity;
import com.example.baowuzhida.chishitang.R;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import Bean.UserHobbyBean;
import Link.HttpUtil;
import lecho.lib.hellocharts.listener.PieChartOnValueSelectListener;
import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.ColumnChartView;
import lecho.lib.hellocharts.view.PieChartView;

import static android.graphics.Color.CYAN;

/**
 * Created by Baowuzhida on 2017/11/16.
 */

public class PieChartFragment extends Fragment {

    private Handler handler;
    private UserHobbyBean userHobbyBean =new UserHobbyBean();
    private View view;

    private PieChartView pieChart;
    private PieChartData pieChardata;
    List<SliceValue> values = new ArrayList<SliceValue>();
    private String[] stateChar = {"谷物","肉类","蔬果","豆制品","脂肪"};






    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_hobby_piechart, container, false);

        return view;
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        pieChart =(PieChartView)view.findViewById(R.id.pie_content);
        pieChart.setOnValueTouchListener(selectListener);//设置点击事件监听
//        generateDefaultData();
        getData();

    }



    private void getData(){

        handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
//                if(msg.obj==null)
//                    return;
//                final LinkedList<UserHobbyBean> linkedList=new LinkedList<>();
//                JSONArray jsonArray;
//                try {
//                    jsonArray = new JSONArray((String) msg.obj);
//                    for(int i = 0;i < jsonArray.length();i++) {
//                        JSONObject jsonObject = (JSONObject) jsonArray.get(i);
//
//                        userHobbyBean.setUb_id(jsonObject.getInt("ub_id"));
//                        userHobbyBean.setUser_id(jsonObject.getInt("user_id"));
//                        userHobbyBean.setUb_grain(jsonObject.getInt("ub_grain"));
//                        userHobbyBean.setUb_beef(jsonObject.getInt("ub_beef"));
//                        userHobbyBean.setUb_vegetables(jsonObject.getInt("ub_vegetables"));
//                        userHobbyBean.setUb_beans(jsonObject.getInt("ub_beans"));
//                        userHobbyBean.setUb_fat(jsonObject.getInt("ub_fat"));
//
//                        linkedList.add(userHobbyBean);
//                    }
//                }
//                catch (Exception e)
//                {
//                    e.printStackTrace();
//                }
                LinkedList<UserHobbyBean> linkedList=new LinkedList<>();
                userHobbyBean.setUb_id(1);
                userHobbyBean.setUser_id(1);
                userHobbyBean.setUb_grain(7);
                userHobbyBean.setUb_beef(3);
                userHobbyBean.setUb_vegetables(5);
                userHobbyBean.setUb_beans(2);
                userHobbyBean.setUb_fat(9);
                linkedList.add(userHobbyBean);

                int[] data = {userHobbyBean.getUb_grain(),userHobbyBean.getUb_beef(),userHobbyBean.getUb_vegetables(),userHobbyBean.getUb_beans(),userHobbyBean.getUb_fat()};

                setPieChartData(data);
                initPieChart();
//                ListView listView = (ListView)view.findViewById(R.id.barChart_list);
//                BarChartAdapter barChartAdapter = new BarChartAdapter(linkedList,getContext());
//                listView.setAdapter(barChartAdapter);

//                linkedList.add(userHobbyBean);
//                initData(userHobbyBean);
            }
        };
        HttpUtil httpUtil = new HttpUtil();
        httpUtil.PostURL("http://119.23.205.112:8080/eatCanteen_war/LoginServlet","type=list",handler);
    }




    /**
     * 获取数据
     */
    private void setPieChartData(int[] data){


        for (int i = 0; i < data.length; ++i) {
            SliceValue sliceValue = new SliceValue((float) data[i], ChartUtils.pickColor());//这里的颜色是我写了一个工具类 是随机选择颜色的
            values.add(sliceValue);
        }
    }


    /**
     * 初始化
     */
    private void initPieChart() {
        pieChardata = new PieChartData();
        pieChardata.setHasLabels(true);//显示表情
        pieChardata.setHasLabelsOnlyForSelected(false);//不用点击显示占的百分比
        pieChardata.setHasLabelsOutside(false);//占的百分比是否显示在饼图外面
        pieChardata.setHasCenterCircle(true);//是否是环形显示
        pieChardata.setValues(values);//填充数据
        pieChardata.setCenterCircleColor(Color.WHITE);//设置环形中间的颜色
        pieChardata.setCenterCircleScale(0.5f);//设置环形的大小级别
        pieChardata.setCenterText1("用户偏好");//环形中间的文字1
        pieChardata.setCenterText1Color(Color.BLACK);//文字颜色
        pieChardata.setCenterText1FontSize(14);//文字大小

        /**这里也可以自定义你的字体   Roboto-Italic.ttf这个就是你的字体库*/
//		Typeface tf = Typeface.createFromAsset(this.getAssets(), "Roboto-Italic.ttf");
//		data.setCenterText1Typeface(tf);

        pieChart.setPieChartData(pieChardata);
        pieChart.setValueSelectionEnabled(true);//选择饼图某一块变大
        pieChart.setChartRotationEnabled(true);//设置饼图是否可以手动旋转
        pieChart.setAlpha(0.9f);//设置透明度
        pieChart.setCircleFillRatio(1f);//设置饼图大小

    }




    /**
     * 监听事件
     */
    private PieChartOnValueSelectListener selectListener = new PieChartOnValueSelectListener() {

        @Override
        public void onValueDeselected() {
            // TODO Auto-generated method stub

        }

        @Override
        public void onValueSelected(int arg0, SliceValue value) {
            //选择对应图形后，在中间部分显示相应信息
            pieChardata.setCenterText1(stateChar[arg0]);
            pieChardata.setCenterText1Color(Color.BLACK);
            pieChardata.setCenterText1FontSize(10);
            pieChardata.setCenterText2(value.getValue() + "（" + value.getValue() + ")");
            pieChardata.setCenterText2Color(Color.BLACK);
            pieChardata.setCenterText2FontSize(12);
//            Toast.makeText(getActivity(), stateChar[arg0] + ":" + value.getValue(), Toast.LENGTH_SHORT).show();
        }
    };


}
