package com.example.baowuzhida.chishitang;


import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.jude.rollviewpager.adapter.StaticPagerAdapter;

/**
 * Created by Baowuzhida on 2017/10/25.
 */

public class RollpagerviewAdapter extends StaticPagerAdapter {

    private int[] imgs = {
            R.drawable.background,
            R.drawable.background_blue,
            R.drawable.update,
    };


    @Override
    public View getView(ViewGroup container, int position) {
        ImageView view = new ImageView(container.getContext());
        view.setImageResource(imgs[position]);
        view.setScaleType(ImageView.ScaleType.CENTER_CROP);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        return view;
    }


    @Override
    public int getCount() {
        return imgs.length;
    }
}
