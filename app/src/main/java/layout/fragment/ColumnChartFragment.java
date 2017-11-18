package layout.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.baowuzhida.chishitang.R;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import Bean.UserHobbyBean;
import Link.HttpUtil;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.ColumnChartView;

import static android.graphics.Color.BLACK;

/**
 * Created by Baowuzhida on 2017/11/16.
 */

public class ColumnChartFragment extends Fragment {


    private View view;
    private static UserHobbyBean userHobbyBean = new UserHobbyBean();
    private static UserHobbyBean otherHobbyBean = new UserHobbyBean();
    ColumnChartView columnChartView=null;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_hobby_columnchart, container, false);

        columnChartView=(ColumnChartView)view.findViewById(R.id.column_content);
        columnChartView.setColumnChartData(null);


        return view;
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);



        generateDefaultData(userHobbyBean,otherHobbyBean);


    }

    private void generateDefaultData(UserHobbyBean userHobbyBean,UserHobbyBean otherHobbyBean){
        //定义有多少个柱子
        int numColumns = 5;
        //定义表格实现类
        ColumnChartData columnChartData;
        //Column 是下图中柱子的实现类
        List<Column> columns =new ArrayList<>();
        //SubcolumnValue 是下图中柱子中的小柱子的实现类，下面会解释我说的是什么

        //循环初始化每根柱子，
//        values.add(new SubcolumnValue(, ChartUtils.pickColor()));
        int[] uservalue = {userHobbyBean.getUb_grain(),userHobbyBean.getUb_beef(),userHobbyBean.getUb_vegetables(),userHobbyBean.getUb_beans(),userHobbyBean.getUb_fat()};
        int[] othervalue = {otherHobbyBean.getUb_grain(),otherHobbyBean.getUb_beef(),otherHobbyBean.getUb_vegetables(),otherHobbyBean.getUb_beans(),otherHobbyBean.getUb_fat(),4};
        String[] type = {"谷物","肉类","蔬果","豆制品","脂肪"};
        List<AxisValue> axisValuess=new ArrayList<>();
        for(int i=0;i<numColumns;i++){
            //每一根柱子中只有一根小柱子
            List<SubcolumnValue>  values=new ArrayList<>();
            values.add(new SubcolumnValue(0, ChartUtils.pickColor()).setTarget(uservalue[i]));
            values.add(new SubcolumnValue(0, ChartUtils.pickColor()).setTarget(othervalue[i]));
            axisValuess.add(new AxisValue(i).setLabel(type[i]));

            //初始化Column
            Column column = new Column(values);
            //给每一个柱子表上值
            column.setHasLabels(true);
            columns.add(column);

        }

        //给表格添加写好数据的柱子
        columnChartData = new ColumnChartData(columns);

        Axis axisBootom = new Axis();
        Axis axisLeft = new Axis();


        axisBootom.setValues(axisValuess);
        axisBootom.setName("数量(我的偏好/平均值）");
        axisBootom.setTextSize(10);
        axisBootom.setTextColor(BLACK);
        axisLeft.setName("类型");
        axisLeft.setTextSize(10);
        axisLeft.setTextColor(BLACK);

        axisBootom.setHasLines(true);
        axisLeft.setHasLines(true);

        columnChartData.setAxisXBottom(axisBootom);
        columnChartData.setAxisYLeft(axisLeft);


        columnChartView.setColumnChartData(columnChartData);
        //给画表格的View添加要画的表格
        columnChartView.startDataAnimation(2000);//动画

    }

    public void setinfo(UserHobbyBean userHobbyBean,UserHobbyBean otherHobbyBean){
        this.userHobbyBean = userHobbyBean;
        this.otherHobbyBean = otherHobbyBean;
    }
}
