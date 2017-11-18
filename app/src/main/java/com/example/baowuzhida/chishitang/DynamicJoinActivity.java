package com.example.baowuzhida.chishitang;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.yancy.gallerypick.config.GalleryConfig;
import com.yancy.gallerypick.config.GalleryPick;
import com.yancy.gallerypick.inter.IHandlerCallBack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import Link.GlideImage;
import Link.HttpUtil;
import Link.UploadFileTask;
import es.dmoral.toasty.Toasty;

/**
 * Created by hasee on 2017/11/15.
 */

public class DynamicJoinActivity extends AppCompatActivity implements View.OnClickListener {

    private Button submit,back;
    private GridView image_gridView;
    private Toolbar toolbar;
    private EditText dynamic_detail;

    private final int IMAGE_OPEN = 1;        //打开图片标记
    private static final int TAKE_PHOTO = 2;  //打开相机标记
    private String pathImage;                //选择图片路径
    private Bitmap bmp;                      //导入临时图片
    private ArrayList<HashMap<String, Object>> imageItem;
    private ArrayList<String> paths=new ArrayList<String>();//发送图片存储地址
    private SimpleAdapter simpleAdapter;     //适配器
    private Context context;
    private String[] mCustomItems=new String[]{"本地相册","相机拍照"};
    private static final int RESULT_CAMERA=200;//返回码，相机
    private Bitmap photo;
    private String TAG = "---Yancy---";
    private final int PERMISSIONS_REQUEST_READ_CONTACTS = 8;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dynamic_join);
        context=this;
        initView();
        chooseImage();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.
                SOFT_INPUT_ADJUST_PAN);
        toolbar.setNavigationIcon(R.mipmap.ic_back);
        toolbar.setTitle("发表动态");
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        submit.setOnClickListener(this);
        back.setOnClickListener(this);
    }

    public void initView(){
        toolbar=(Toolbar)findViewById(R.id.dynamicjoin_toolbar);

        submit = (Button)findViewById(R.id.btn_submit);
        back=(Button)findViewById(R.id.btn_back);
        image_gridView=(GridView)findViewById(R.id.dynamic_image_gridview);

        dynamic_detail=(EditText)findViewById(R.id.dynamic_detail);

    }

    public void chooseImage(){
        /*
         * 载入默认图片添加图片加号
         * 通过适配器实现
         * SimpleAdapter参数imageItem为数据源 R.layout.crowdfund_grid_item为布局
         */
        //获取资源图片加号
        bmp = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_camera);
        imageItem = new ArrayList<HashMap<String, Object>>();
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("itemImage", bmp);
        imageItem.add(map);
        simpleAdapter = new SimpleAdapter(this,
                imageItem, R.layout.crowdfund_grid_item,
                new String[] { "itemImage"}, new int[] { R.id.crowdfund_view});
        /*
         * HashMap载入bmp图片在GridView中不显示,但是如果载入资源ID能显示 如
         * map.put("itemImage", R.drawable.img);
         * 解决方法:
         *              1.自定义继承BaseAdapter实现
         *              2.ViewBinder()接口实现
         *  参考 http://blog.csdn.net/admin_/article/details/7257901
         */
        simpleAdapter.setViewBinder(new SimpleAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Object data,
                                        String textRepresentation) {
                // TODO Auto-generated method stub
                if(view instanceof ImageView && data instanceof Bitmap){
                    ImageView i = (ImageView)view;
                    i.setImageBitmap((Bitmap) data);
                    return true;
                }
                return false;
            }
        });
        image_gridView.setAdapter(simpleAdapter);

        /*
         * 监听GridView点击事件
         * 报错:该函数必须抽象方法 故需要手动导入import android.view.View;
         */
        image_gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id)
            {
                if(position == 0) { //点击图片位置为+ 0对应0张图片
                    if( imageItem.size() < 10) {
                        Toast.makeText(DynamicJoinActivity.this, "添加图片", Toast.LENGTH_SHORT).show();
                        //选择图片
                        //showDialogCustom();
                        Gallery_Photo();
                    }
                    else
                        Toast.makeText(DynamicJoinActivity.this, "图片数9张已满", Toast.LENGTH_SHORT).show();
                }
                else {
                    dialog(position);
                    //		Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    //选择图片
    private void Gallery_Photo(){
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG, "需要授权 ");
            if (ActivityCompat.shouldShowRequestPermissionRationale(DynamicJoinActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                Log.i(TAG, "拒绝过了");
                // 提示用户如果想要正常使用，要手动去设置中授权。
                Toast.makeText(context, "请在 设置-应用管理 中开启此应用的储存授权。", Toast.LENGTH_SHORT).show();
            } else {
                Log.i(TAG, "进行授权");
                ActivityCompat.requestPermissions(DynamicJoinActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSIONS_REQUEST_READ_CONTACTS);
            }
        } else {
            Log.i(TAG, "不需要授权 ");
            // 进行正常操作
        }
        IHandlerCallBack iHandlerCallBack = new IHandlerCallBack() {
            @Override
            public void onStart() {
                Log.i(TAG, "onStart: 开启");
            }

            @Override
            public void onSuccess(List<String> photoList) {
                Log.i(TAG, "onSuccess: 返回数据");
                for (String s : photoList) {
                    pathImage=s;
                    onResume();
                    Log.i(TAG, s);
                }
            }

            @Override
            public void onCancel() {
                Log.i(TAG, "onCancel: 取消");
            }

            @Override
            public void onFinish() {
                Log.i(TAG, "onFinish: 结束");
            }

            @Override
            public void onError() {
                Log.i(TAG, "onError: 出错");
            }
        };
        GalleryConfig galleryConfig = new GalleryConfig.Builder()
                .imageLoader(new GlideImage())    // ImageLoader 加载框架（必填）
                .iHandlerCallBack(iHandlerCallBack)     // 监听接口（必填）
                .provider("com.example.baowuzhida.chishitang.fileprovider")   // provider (必填)
                //.pathList(path)                         // 记录已选的图片
                .multiSelect(true, 9)                   // 配置是否多选的同时 配置多选数量   默认：false ， 9
                .crop(true, 1, 1, 500, 500)             // 配置裁剪功能的参数，   默认裁剪比例 1:1
                .isShowCamera(true)                     // 是否现实相机按钮  默认：false
                .filePath("/Gallery/Pictures")          // 图片存放路径
                .build();
        GalleryPick.getInstance().setGalleryConfig(galleryConfig).open(DynamicJoinActivity.this);
    }
    protected void dialog(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(DynamicJoinActivity.this);
        builder.setMessage("确认移除已添加图片吗？");
        builder.setTitle("提示");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                imageItem.remove(position);
                paths.remove(position-1);
                simpleAdapter.notifyDataSetChanged();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(imageItem.size() < 10) {
            if (!TextUtils.isEmpty(pathImage)) {
                Bitmap addbmp = BitmapFactory.decodeFile(pathImage);
                HashMap<String, Object> map = new HashMap<String, Object>();
                map.put("itemImage", addbmp);
                paths.add(pathImage);
                imageItem.add(map);
                simpleAdapter = new SimpleAdapter(this,
                        imageItem, R.layout.crowdfund_grid_item,
                        new String[]{"itemImage"}, new int[]{R.id.crowdfund_view});
                simpleAdapter.setViewBinder(new SimpleAdapter.ViewBinder() {
                    @Override
                    public boolean setViewValue(View view, Object data,
                                                String textRepresentation) {
                        // TODO Auto-generated method stub
                        if (view instanceof ImageView && data instanceof Bitmap) {
                            ImageView i = (ImageView) view;
                            i.setImageBitmap((Bitmap) data);
                            return true;
                        }
                        return false;
                    }
                });
                image_gridView.setAdapter(simpleAdapter);
                simpleAdapter.notifyDataSetChanged();
                //刷新后释放防止手机休眠后自动添加
                pathImage = null;
            }
        }
    }

    public boolean adjstedittext() {
        if (dynamic_detail.getText().toString().equals("")&&paths.size()==0) {
            Toasty.info(getApplicationContext(), "写点动态吧或发几张图片吧!", Toast.LENGTH_SHORT, true).show();
            return false;
        }
        else
            return true;
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.btn_submit)
        {
            if(adjstedittext()) {
                Handler dynamic_addhandler = new Handler() {
                    public void handleMessage(Message msg) {
                        super.handleMessage(msg);
                        String type = (String) msg.obj;
                        if (msg.obj == null) {
                            type = "connfail";
                        }
                        switch (type) {
                            case "no":
                                Toasty.error(getApplicationContext(), "请先登录", Toast.LENGTH_SHORT, true).show();
                                break;
                            case "connfail":
                                Toasty.error(getApplicationContext(), "连接超时", Toast.LENGTH_SHORT, true).show();
                                break;
                            default:
                                Toasty.success(getApplicationContext(), "上传成功", Toast.LENGTH_SHORT, true).show();
                                for (int i = 0; i < paths.size(); i++) {
                                    String pathImage = paths.get(i);
                                    System.out.println(pathImage);
                                    if (pathImage != null && pathImage.length() > 0) {
                                        UploadFileTask uploadFileTask = new UploadFileTask(DynamicJoinActivity.this,"dynamic");
                                        uploadFileTask.execute(pathImage);
                                    }
                                }
                                finish();
                                break;
                        }
                    }
                };
                HttpUtil httpUtil = new HttpUtil();
                httpUtil.PostURL("http://119.23.205.112:8080/eatCanteen_war/DynamicServlect",
                        "type=add&dynamic_message="+dynamic_detail.getText().toString(), dynamic_addhandler);
            }
        }
        else
        {
            finish();
        }
    }
}
