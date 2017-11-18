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
import android.widget.TextView;

import com.example.baowuzhida.chishitang.R;

import java.util.ArrayList;
import java.util.List;

import Bean.UserHobbyBean;
import lecho.lib.hellocharts.listener.PieChartOnValueSelectListener;
import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.PieChartView;


/**
 * Created by Baowuzhida on 2017/11/16.
 */

public class PieChartFragment extends Fragment {

    private static UserHobbyBean userHobbyBean = new UserHobbyBean();
    private View view;

    private TextView info_hobby_text;
    private PieChartView pieChart;
    private PieChartData pieChardata;
    List<SliceValue> values = new ArrayList<SliceValue>();
    private String[] stateChar = {"谷物","肉类","蔬果","豆制品","脂肪"};
    private int i;
    private int j;






    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_hobby_piechart, container, false);
        pieChart =(PieChartView)view.findViewById(R.id.pie_content);
        info_hobby_text = (TextView)view.findViewById(R.id.info_hobby_text);
//        pieChart.setPieChartData(null);
        pieChart.setOnValueTouchListener(selectListener);//设置点击事件监听

        int[] data = {userHobbyBean.getUb_grain(),userHobbyBean.getUb_beef(),userHobbyBean.getUb_vegetables(),userHobbyBean.getUb_beans(),userHobbyBean.getUb_fat()};
        StringBuilder buffer = new StringBuilder();

        int max = getMax(data);
        buffer.append("您吃").append(stateChar[j]).append("比较多，最近吃了:").append(max).append("次;");
        int min = getMin(data);
        buffer.append("您吃").append(stateChar[j]).append("比较少，最近吃了:").append(min).append("次.");
        info_hobby_text.setText(buffer.toString());
        setPieChartData(data);
        initPieChart();
        return view;
    }

    public  int getMax(int[] arr) {
        int max = Integer.MIN_VALUE;
        for( i = 0; i < arr.length; i++) {
            if(arr[i] > max) {
                max = arr[i];
                j=i;
            }
        }

        return max;
    }

    public  int getMin(int[] arr) {
        int min = Integer.MAX_VALUE;
        for( i = 0; i < arr.length; i++) {
            if(arr[i] < min) {
                min = arr[i];
                j=i;
            }
        }

        return min;
    }

    /**
     * 获取数据
     */
    private void setPieChartData(int[] data){

        values.clear();
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

    public void setinfo(UserHobbyBean userHobbyBean){
        this.userHobbyBean = userHobbyBean;
    }

}
