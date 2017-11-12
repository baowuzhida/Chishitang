
package Interpolator_extends;
/**
 * Created by Baowuzhida on 2017/11/12.
 */

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.baowuzhida.chishitang.R;
import com.makeramen.roundedimageview.RoundedImageView;


public class ImageTextButton extends RelativeLayout {

    private RoundedImageView imgView;
    private TextView  textView;

    public ImageTextButton(Context context) {
        super(context,null);
    }

    public ImageTextButton(Context context,AttributeSet attributeSet) {
        super(context, attributeSet);

        LayoutInflater.from(context).inflate(R.layout.btn_imagetext_btn, this,true);

        this.imgView = (RoundedImageView)findViewById(R.id.imgview);
        this.textView = (TextView)findViewById(R.id.textview);

        this.setClickable(true);
        this.setFocusable(true);
    }

    public void setImgResource(int resourceID) {
        this.imgView.setImageResource(resourceID);
    }

    public void setText(String text) {
        this.textView.setText(text);
    }

    public void setTextColor(int color) {
        this.textView.setTextColor(color);
    }

    public void setTextSize(float size) {
        this.textView.setTextSize(size);
    }

}
