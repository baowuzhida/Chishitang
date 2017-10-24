package com.example.baowuzhida.chishitang;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import Bean.ShoppingCartBean;
import Link.CartDao;
import com.example.baowuzhida.chishitang.ShoppingCartActivity;


/**
 * Created by AYD on 2016/11/21.
 * <p/>
 * 购物车Adapter
 */
public class ShoppingCartAdapter extends BaseAdapter {

    private boolean isShow = true;//是否显示编辑/完成
//    private View activity_shoppingcar;
    private List<ShoppingCartBean> shoppingCartBeanList;
    private CheckInterface checkInterface;
    private ModifyCountInterface modifyCountInterface;
    private Context context;
    private ImageView iv_show_pic;
    private int position;
//    private TextView tv_settlement;

    public ShoppingCartAdapter(Context context) {
        this.context = context;
    }

    private CartDao cartDao;

    public void setShoppingCartBeanList(List<ShoppingCartBean> shoppingCartBeanList) {
        this.shoppingCartBeanList = shoppingCartBeanList;
        notifyDataSetChanged();
    }

    /**
     * 单选接口
     *
     * @param checkInterface
     */
    public void setCheckInterface(CheckInterface checkInterface) {
        this.checkInterface = checkInterface;
    }

    /**
     * 改变商品数量接口
     *
     * @param modifyCountInterface
     */
    public void setModifyCountInterface(ModifyCountInterface modifyCountInterface) {
        this.modifyCountInterface = modifyCountInterface;
    }

    @Override
    public int getCount() {
        return shoppingCartBeanList == null ? 0 : shoppingCartBeanList.size();
    }

    @Override
    public Object getItem(int position) {
        return shoppingCartBeanList.get(position);
    }

    @Override
    public long getItemId(int position) {
        this.position = position;
        return position;
    }
    public void PostPosition(Handler handler){
        Message message=handler.obtainMessage();
        message.obj = position;
        handler.sendMessage(message);
    }


    /**
     * 是否显示可编辑
     *
     * @param flag
     */
    public void isShow(boolean flag) {
        isShow = flag;
        notifyDataSetChanged();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        cartDao = new CartDao(context);
        final ViewHolder holder;


//        if(activity_shoppingcar==null){
//            activity_shoppingcar=LayoutInflater.from(context).inflate(R.layout.activity_shoppingcar,parent,false);
//        }

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.shoppingcar_list, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final ShoppingCartBean shoppingCartBean = shoppingCartBeanList.get(position);

//        tv_settlement = (TextView)activity_shoppingcar.findViewById(R.id.tv_settlement);
//        tv_settlement.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                final ArrayList<Integer> a = new ArrayList<>();
//                for (int i = 0; i < shoppingCartBeanList.size(); i++) {
//                    ShoppingCartBean shoppingCartBean = shoppingCartBeanList.get(i);
//                    if (shoppingCartBean.isChoosed()) {
//                        a.add(shoppingCartBean.getProduct_id());
//                    }
//                }
//                new AlertDialog.Builder(context)
//                        .setTitle("结算！")
//                        .setMessage("确定吗？")
//                        .setPositiveButton("是", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                HttpUtil httpUtil = new HttpUtil();
////                                httpUtil.PostURL("http://119.23.205.112:8080/eatCanteen_war/LoginServlet","accountNumber="+name+"&password="+psd+"&type=login",loginhandle);
//                                Toast.makeText(context, a+"已结账", Toast.LENGTH_SHORT).show();
//                                modifyCountInterface.CheckOut(a,position);
//                            }
//                        })
//                        .setNegativeButton("否", null)
//                        .show();
//            }
//        });

        iv_show_pic = (ImageView)convertView.findViewById(R.id.iv_show_pic);
        String url = shoppingCartBean.getImageUrl();

        Glide.with(context)
                .load(url)
                .placeholder(R.drawable.eat)
                .crossFade()
                .into(iv_show_pic);

        holder.tv_commodity_name.setText(shoppingCartBean.getShoppingName());
        holder.shoppingcart_detail.setText("详情:" + shoppingCartBean.getDetail());
        holder.tv_address.setText("食堂地址:" + shoppingCartBean.getAddress()+"食堂");
        holder.tv_price.setText("￥:" + shoppingCartBean.getPrice());
        holder.ck_chose.setChecked(shoppingCartBean.isChoosed());
        holder.tv_show_num.setText(shoppingCartBean.getCount() + "");
        holder.tv_num.setText("X" + shoppingCartBean.getCount());



        //单选框按钮
        holder.ck_chose.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        shoppingCartBean.setChoosed(((CheckBox) v).isChecked());
                        checkInterface.checkGroup(position, ((CheckBox) v).isChecked());//向外暴露接口
                    }
                }
        );

        //增加按钮
        holder.iv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int productid = shoppingCartBeanList.get(position).getProduct_id();
                ShoppingCartBean shoppingCartBean = cartDao.dbQueryOneByPID(productid);
                int count = shoppingCartBean.getCount();
                cartDao.dbUpdateCart(productid,count+1);
                modifyCountInterface.doIncrease(position, holder.tv_show_num, holder.ck_chose.isChecked());//暴露增加接口
            }
        });

        //删减按钮
        holder.iv_sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int productid = shoppingCartBeanList.get(position).getProduct_id();
                ShoppingCartBean shoppingCartBean = cartDao.dbQueryOneByPID(productid);
                int count = shoppingCartBean.getCount();
                if(count>1) {
                    cartDao.dbUpdateCart(productid, count - 1);
                    modifyCountInterface.doDecrease(position, holder.tv_show_num, holder.ck_chose.isChecked());//暴露删减接口
                }else{
                    Toast.makeText(context,"最少为一件", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //删除弹窗
        holder.tv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog alert = new AlertDialog.Builder(context).create();
                alert.setTitle("操作提示");
                alert.setMessage("您确定要将这些商品从购物车中移除吗？");
                alert.setButton(DialogInterface.BUTTON_NEGATIVE, "取消",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                return;
                            }
                        });
                alert.setButton(DialogInterface.BUTTON_POSITIVE, "确定",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                int productid = shoppingCartBeanList.get(position).getProduct_id();
                                cartDao.dbDeleteCart(productid);
                                Toast.makeText(context,"删除成功", Toast.LENGTH_SHORT).show();
                                modifyCountInterface.childDelete(position);//删除 目前只是从item中移除

                            }
                        });
                alert.show();
            }
        });

        //判断是否在编辑状态下
        if (isShow) {
            holder.tv_commodity_name.setVisibility(View.VISIBLE);
            holder.shoppingcart_detail.setVisibility(View.VISIBLE);
            holder.rl_edit.setVisibility(View.GONE);
            holder.tv_delete.setVisibility(View.GONE);
        } else {
            holder.tv_commodity_name.setVisibility(View.GONE);
            holder.shoppingcart_detail.setVisibility(View.GONE);
            holder.rl_edit.setVisibility(View.VISIBLE);
            holder.tv_delete.setVisibility(View.VISIBLE);
        }

        return convertView;
    }


    //初始化控件
    class ViewHolder {
        ImageView iv_chose;
        ImageView iv_show_pic, iv_sub, iv_add;
        TextView tv_commodity_name, shoppingcart_detail, tv_address, tv_price, tv_num, tv_delete, tv_show_num;
        CheckBox ck_chose;
        RelativeLayout rl_edit;

        public ViewHolder(View itemView) {
            ck_chose = (CheckBox) itemView.findViewById(R.id.ck_chose);
            iv_show_pic = (ImageView) itemView.findViewById(R.id.iv_show_pic);
            iv_sub = (ImageView) itemView.findViewById(R.id.iv_sub);
            iv_add = (ImageView) itemView.findViewById(R.id.iv_add);
            tv_commodity_name = (TextView) itemView.findViewById(R.id.tv_commodity_name);
            shoppingcart_detail = (TextView) itemView.findViewById(R.id.shoppingcart_detail);
            tv_address = (TextView) itemView.findViewById(R.id.tv_address);
            tv_price = (TextView) itemView.findViewById(R.id.tv_price);
            tv_num = (TextView) itemView.findViewById(R.id.tv_num);
            tv_delete = (TextView) itemView.findViewById(R.id.tv_delete);
            tv_show_num = (TextView) itemView.findViewById(R.id.tv_show_num);
            rl_edit = (RelativeLayout) itemView.findViewById(R.id.rl_edit);

        }

    }

    /**
     * 复选框接口
     */
    public interface CheckInterface {
        /**
         * 组选框状态改变触发的事件
         *
         * @param position  元素位置
         * @param isChecked 元素选中与否
         */
        void checkGroup(int position, boolean isChecked);
    }


    /**
     * 改变数量的接口
     */
    public interface ModifyCountInterface {
        /**
         * 增加操作
         *
         * @param position      组元素位置
         * @param showCountView 用于展示变化后数量的View
         * @param isChecked     子元素选中与否
         */
        void doIncrease(int position, View showCountView, boolean isChecked);

        /**
         * 删减操作
         *
         * @param position      组元素位置
         * @param showCountView 用于展示变化后数量的View
         * @param isChecked     子元素选中与否
         */
        void doDecrease(int position, View showCountView, boolean isChecked);

        /**
         * 删除子item
         *
         * @param position
         */
        void childDelete(int position);

        /**
         * 删除结账的item
         *
         * @param position
         */
        void CheckOut(ArrayList a,int position);
    }


}
