package layout.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.baowuzhida.chishitang.R;

import lecho.lib.hellocharts.view.ColumnChartView;

/**
 * Created by Baowuzhida on 2017/11/17.
 */

public class AdjuctFragment extends Fragment {

    private View view;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_hobby_adjust, container, false);



        return view;
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }
}
