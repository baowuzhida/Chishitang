package Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.baowuzhida.chishitang.ImagePagerActivity;
import com.example.baowuzhida.chishitang.R;

import java.util.ArrayList;

import Bean.DynamicBean;
import Interpolator_extends.NoScrollGridView;

import static com.mob.MobSDK.getContext;

/**
 * Created by hasee on 2017/11/13.
 */

public class DynamicItemAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<DynamicBean> items;

    public DynamicItemAdapter(Context ctx, ArrayList<DynamicBean> items) {
        this.mContext = ctx;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items == null ? 0 : items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(mContext, R.layout.dynamic_item_list, null);
            holder.user_Image = (ImageView) convertView
                    .findViewById(R.id.user_Image);
            holder.user_name = (TextView) convertView
                    .findViewById(R.id.user_name);
            holder.dynamic_content = (TextView) convertView
                    .findViewById(R.id.dynamic_content);
            holder.gridview = (NoScrollGridView) convertView
                    .findViewById(R.id.gridview);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        DynamicBean itemEntity = items.get(position);
        holder.user_name.setText(itemEntity.getUserBean().getUser_name());
        holder.dynamic_content.setText(itemEntity.getD_content());
        // 使用ImageLoader加载网络图片
        Glide.with(mContext)
                .load(itemEntity.getUserBean().getUser_headimage())
                .placeholder(R.drawable.eat)
                .crossFade()
                .into(holder.user_Image);
        String[] url=itemEntity.getD_image().split("##");
        final ArrayList<String> imageUrls = new ArrayList<>();
        for (int i=0;i<url.length;i++)
        {
            Log.e("url",url[i]);
            imageUrls.add(url[i]);
        }
        if (imageUrls == null || imageUrls.size() == 0||imageUrls.get(0).equals("null")) { // 没有图片资源就隐藏GridView
            holder.gridview.setVisibility(View.GONE);
        } else {
            holder.gridview.setAdapter(new NoScrollGridAdapter(mContext, imageUrls));
        }

        // 点击回帖九宫格，查看大图
        holder.gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                imageBrower(position, imageUrls);
            }
        });
        return convertView;
    }

    /**
     * 打开图片查看器
     *
     * @param position
     * @param urls2
     */
    protected void imageBrower(int position, ArrayList<String> urls2) {
        Intent intent = new Intent(mContext, ImagePagerActivity.class);
        // 图片url,为了演示这里使用常量，一般从数据库中或网络中获取
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, urls2);
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
        mContext.startActivity(intent);
    }

    /**
     * listview组件复用，防止“卡顿”
     *
     * @author Administrator
     *
     */
    class ViewHolder {
        private ImageView user_Image;
        private TextView user_name;
        private TextView dynamic_content;
        private NoScrollGridView gridview;
    }
}
