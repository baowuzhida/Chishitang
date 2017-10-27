package Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.baowuzhida.chishitang.R;
import com.example.baowuzhida.chishitang.VoteActivity;
import com.example.baowuzhida.chishitang.VoteDetailActivity;

import java.util.LinkedList;

import Bean.FavoriteProduct;
import Link.HttpUtil;

/**
 * Created by hasee on 2017/9/13.
 */

public class VoteAdapter extends BaseAdapter {

    private LinkedList<FavoriteProduct> mData;
    private Context mContext;
    private VoteActivity voteActivity = new VoteActivity();


    public VoteAdapter(LinkedList<FavoriteProduct> mData, Context mContext) {
        this.mData = mData;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;

        if (convertView == null) {
            holder = new ViewHolder();
            //根据自定义的Item布局加载布局
            convertView = LayoutInflater.from(mContext).inflate(R.layout.vote_list_item, parent, false);
            holder = new ViewHolder();
            holder.ProductImage = (ImageButton) convertView.findViewById(R.id.votelist_image);
            holder.ProductName = (TextView) convertView.findViewById(R.id.votelist_name);
            holder.voteAdd = (Button) convertView.findViewById(R.id.votelist_add);
            convertView.setTag(holder);   //将Holder存储到convertView中
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
//        gridView=(GridView)convertView.findViewById(R.id.gv_1);
        final String url = mData.get(position).getProduct().getProduct_image();

        Glide.with(mContext)
                .load(url)
                .placeholder(R.drawable.eat)
                .crossFade()
                .into(holder.ProductImage);

        holder.ProductName.setText("商品名称：" + mData.get(position).getProduct().getProduct_name());
        holder.voteAdd.setTag(position);
        final FavoriteProduct favoriteProduct=mData.get(position);
        //给Button添加单击事件  添加Button之后ListView将失去焦点  需要的直接把Button的焦点去掉
        holder.voteAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Handler handler=new Handler(){
                    @Override
                    public void handleMessage(Message msg) {
                        super.handleMessage(msg);
                        String type=(String)msg.obj;
                        if(type.equals("ok")){

                            Toast.makeText(mContext,"投票成功",Toast.LENGTH_SHORT).show();
                            VoteActivity.getObj().refreshList();
                        }
                        else
                            Toast.makeText(mContext,"投票失败",Toast.LENGTH_SHORT).show();
                    }
                };
                HttpUtil httpUtil=new HttpUtil();
                httpUtil.PostURL("http://119.23.205.112:8080/eatCanteen_war/FavoriteProductServlet","type=add&id="+favoriteProduct.getFproduct_id(),handler);

            }
        });
        holder.ProductImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext,VoteDetailActivity.class);
                intent.putExtra("favourite_product_id",mData.get(position).getFproduct_id());
                intent.putExtra("vote_image",url);
                intent.putExtra("vote_name",mData.get(position).getProduct().getProduct_name());
                intent.putExtra("vote_number",mData.get(position).getNumber_votes());
                intent.putExtra("vote_details",mData.get(position).getProduct().getProduct_details());
                mContext.startActivity(intent);
            }
        });
        return convertView;
    }

    private static class ViewHolder {
        ImageView ProductImage;
        TextView ProductName;
        Button voteAdd;
    }
}
