package com.example.baowuzhida.chishitang;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.app.AlertDialog;
import android.provider.MediaStore;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import Link.GlideImage;
import Link.HttpUtil;
import Link.UploadFileTask;
import es.dmoral.toasty.Toasty;


/**
 * Created by Baowuzhida on 2017/11/6.
 */

public class CrowdFundJoinActivity extends AppCompatActivity implements View.OnClickListener {

    private Button submit,back;
    private Spinner spinner_category;
    private String element;//选择的类别
    private GridView image_gridView;
    private Toolbar toolbar;
    private int pos;//类别对应的数字
    private EditText crowdfund_name,crowdfund_detail,crowdfund_money,crowdfund_declaration;
    private ProgressDialog pdialog;

    private final int IMAGE_OPEN = 1;        //打开图片标记
    public static final int TAKE_PHOTO = 2;  //打开相机标记
    private String pathImage;                //选择图片路径
    private Bitmap bmp;                      //导入临时图片
    private ArrayList<HashMap<String, Object>> imageItem;
    private ArrayList<String> paths=new ArrayList<String>();//发送图片存储地址
    private ArrayList<String> path=new ArrayList<String>();//多选图片存储地址
    private SimpleAdapter simpleAdapter;     //适配器
    private Context context;
    private String[] mCustomItems=new String[]{"本地相册","相机拍照"};
    private static final int RESULT_CAMERA=200;//返回码，相机
    private Bitmap photo;
    private String TAG = "---CST---";
    private final int PERMISSIONS_REQUEST_READ_CONTACTS = 8;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crowdfund_join);
        context=this;

        initView();

        chooseImage();

        getWindow().setSoftInputMode(WindowManager.LayoutParams.
                SOFT_INPUT_ADJUST_PAN);

        toolbar.setNavigationIcon(R.mipmap.ic_back);
        toolbar.setTitle("美食众筹");
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        List<String> dataset = new ArrayList<>(Arrays.asList("请选择类别","好吃的菜", "难吃的菜", "一般的菜", "还可以的菜", "不行的菜"));
        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,dataset);
        spinner_category.setAdapter(arrayAdapter);
        spinner_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                pos = position;
                element = parent.getItemAtPosition(position).toString();
                Toast.makeText(CrowdFundJoinActivity.this, "选择的元素是：" +
                        element+position,Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
//                Toast.makeText(CrowdFundJoinActivity.this, "记得选择类别",Toast.LENGTH_SHORT).show();
            }
        });
        submit.setOnClickListener(this);
        back.setOnClickListener(this);
    }

    public void initView(){
        toolbar=(Toolbar)findViewById(R.id.crowdfund_join_toolbar);

        spinner_category = (Spinner) findViewById(R.id.nice_spinner);
        submit = (Button)findViewById(R.id.btn_submit);
        back=(Button)findViewById(R.id.btn_back);
        image_gridView=(GridView)findViewById(R.id.image_gridview);

        crowdfund_name=(EditText)findViewById(R.id.crowdfund_name);
        crowdfund_detail=(EditText)findViewById(R.id.crowdfund_detail);
        crowdfund_declaration=(EditText)findViewById(R.id.crowdfund_declaration);
        crowdfund_money=(EditText)findViewById(R.id.crowdfund_money);

    }

    public void submitmessage() throws JSONException {


        JSONObject jsonObject = new JSONObject();
        jsonObject.put("crowdfund_name", crowdfund_name.getText().toString());
        jsonObject.put("crowdfund_detail", crowdfund_detail.getText().toString());
        jsonObject.put("crowdfund_aimcapital", crowdfund_money.getText().toString());
        jsonObject.put("crowdfund_declaration", crowdfund_declaration.getText().toString());
        jsonObject.put("crowdfund_type", pos+"");

        String crowdfund_message = jsonObject.toString();


        Handler crowdfundhandler = new Handler(){
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                String type = (String) msg.obj;
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (msg.obj == null) {
                    type = "connfail";
                }
                switch (type) {
                    case "no":
                        Toasty.error(getApplicationContext(), "账号或密码错误", Toast.LENGTH_SHORT, true).show();
                        break;
                    case "connfail":
                        Toasty.error(getApplicationContext(), "连接超时", Toast.LENGTH_SHORT, true).show();
                        break;
                    default:
                        pdialog = ProgressDialog.show(context, "正在加载...", "系统正在处理您的请求");
                        for (int i = 0; i < paths.size(); i++) {
                            String pathImage = paths.get(i);
                            System.out.println(pathImage);

                            if (pathImage != null && pathImage.length() > 0) {
                                UploadFileTask uploadFileTask = new UploadFileTask(CrowdFundJoinActivity.this,"clowdfund");
                                uploadFileTask.execute(pathImage);
                            }
                        }
                        System.out.println(pathImage);
                        break;
                }
            }
        };

        HttpUtil httpUtil = new HttpUtil();
        httpUtil.PostURL("http://119.23.205.112:8080/eatCanteen_war/CrowdfundServlet",
                "type=message&crowdfund_message="+crowdfund_message,crowdfundhandler);
    }

    public void onPause() {
        super.onPause();
        if ((null != pdialog) && pdialog.isShowing()) {
            pdialog.dismiss();
        }
    }

    public boolean adjstedittext(){

        if(crowdfund_name.getText().toString().equals("")){
            Toasty.info(getApplicationContext(), "您还没写名称呢!", Toast.LENGTH_SHORT, true).show();
            return false;
        }else if(crowdfund_detail.getText().toString().equals("")){
            Toasty.info(getApplicationContext(), "您还没写详情呢!", Toast.LENGTH_SHORT, true).show();
            return false;
        }else if(crowdfund_declaration.getText().toString().equals("")){
            Toasty.info(getApplicationContext(), "您还没写宣言呢!", Toast.LENGTH_SHORT, true).show();
            return false;
        }else if(crowdfund_money.getText().toString().equals("")){
            Toasty.info(getApplicationContext(), "您还没写目标资金呢!", Toast.LENGTH_SHORT, true).show();
            return false;
        }else if(pos==0){
            Toasty.info(getApplicationContext(), "记得选择类别~", Toast.LENGTH_SHORT, true).show();
            return false;
        }else
            return true;
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_submit:
                boolean adjust = adjstedittext();
                if(adjust){
                    AlertDialog.Builder alert = new AlertDialog.Builder(CrowdFundJoinActivity.this);
                    alert.setTitle("操作提示");
                    alert.setMessage("您可以保持沉默但您所说的每一句话都将成为呈堂证供？");
                    alert.setPositiveButton("确定",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //...To-do
                                    try {
                                        submitmessage();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                    alert.setNegativeButton("取消",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //...To-do
                                }
                            });
                    alert.show();
                }
                break;
            case R.id.btn_back:
                finish();
        }

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
                    if( imageItem.size() < 6) { //第一张为默认图片
                        Toast.makeText(CrowdFundJoinActivity.this, "添加图片", Toast.LENGTH_SHORT).show();
                        //选择图片
                        //showDialogCustom();
                        Gallery_Photo();
                    }
                    else
                        Toast.makeText(CrowdFundJoinActivity.this, "图片数5张已满", Toast.LENGTH_SHORT).show();
                }
                else {
                    dialog(position);
                    //Toast.makeText(MainActivity.this, "点击第"+(position + 1)+" 号图片",
                    //		Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //选择图片
    private void Gallery_Photo(){
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG, "需要授权 ");
            if (ActivityCompat.shouldShowRequestPermissionRationale(CrowdFundJoinActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                Log.i(TAG, "拒绝过了");
                // 提示用户如果想要正常使用，要手动去设置中授权。
                Toast.makeText(context, "请在 设置-应用管理 中开启此应用的储存授权。", Toast.LENGTH_SHORT).show();
            } else {
                Log.i(TAG, "进行授权");
                ActivityCompat.requestPermissions(CrowdFundJoinActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSIONS_REQUEST_READ_CONTACTS);
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
                .multiSelect(true, 5)                   // 配置是否多选的同时 配置多选数量   默认：false ， 9
                .crop(true, 1, 1, 500, 500)             // 配置裁剪功能的参数，   默认裁剪比例 1:1
                .isShowCamera(true)                     // 是否现实相机按钮  默认：false
                .filePath("/Gallery/Pictures")          // 图片存放路径
                .build();
        GalleryPick.getInstance().setGalleryConfig(galleryConfig).open(CrowdFundJoinActivity.this);
    }


    //选择相册或者相机
    private void showDialogCustom(){
        //创建对话框
        ProgressDialog.Builder builder=new AlertDialog.Builder(CrowdFundJoinActivity.this);
        builder.setTitle("请选择：");
        builder.setItems(mCustomItems, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(which==0) {
                    //相册
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, IMAGE_OPEN);

                }else if(which==1){
                    //照相机
                    if (Build.VERSION.SDK_INT >= 23) {
                        int checkCallPhonePermission = ContextCompat.checkSelfPermission(CrowdFundJoinActivity.this, Manifest.permission.CAMERA);
                        if(checkCallPhonePermission != PackageManager.PERMISSION_GRANTED){
                            ActivityCompat.requestPermissions(CrowdFundJoinActivity.this,new String[]{Manifest.permission.CAMERA},222);
                            return;
                        }else{
                            //启动相机程序
                            Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                            startActivityForResult(intent, TAKE_PHOTO);
                        }
                    } else {
                        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                        startActivityForResult(intent, TAKE_PHOTO);
                    }
                }
            }
        });
        builder.create().show();
    }
    //获取图片路径 响应startActivityForResult
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //打开图片
        if (data != null)
            if(resultCode==RESULT_OK && requestCode==IMAGE_OPEN) {
                Uri uri = data.getData();
                if (!TextUtils.isEmpty(uri.getAuthority())) {
                    //查询选择图片
                    Cursor cursor = getContentResolver().query(
                            uri,
                            new String[] { MediaStore.Images.Media.DATA },
                            null,
                            null,
                            null);
                    //返回 没找到选择图片
                    if (null == cursor) {
                        return;
                    }
                    //光标移动至开头 获取图片路径
                    cursor.moveToFirst();
                    pathImage = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                }
            }//end if 打开图片
            else if(requestCode==TAKE_PHOTO)
            {
                Uri uri = data.getData();
                if (uri != null) {
                    this.photo = BitmapFactory.decodeFile(uri.getPath()); // 拿到图片
                }
                Log.e("path","wrwerwerwerwerw"+photo);
                if (photo == null) {
                    Bundle bundle = data.getExtras();
                    if (bundle != null) {
                        photo = (Bitmap) bundle.get("data");
                        FileOutputStream fileOutputStream = null;
                        try {
// 获取 SD 卡根目录 生成图片并
                            String saveDir = Environment
                                    .getExternalStorageDirectory()
                                    + "/Eat_Photos";
// 新建目录
                            File dir = new File(saveDir);
                            if (!dir.exists())
                                dir.mkdir();
// 生成文件名
                            @SuppressLint("SimpleDateFormat") SimpleDateFormat t = new SimpleDateFormat(
                                    "yyyyMMddssSSS");
                            String filename = "MT" + (t.format(new Date()))
                                    + ".jpg";
// 新建文件
                            File file = new File(saveDir, filename);
// 打开文件输出流
                            fileOutputStream = new FileOutputStream(file);
// 生成图片文件
                            this.photo.compress(Bitmap.CompressFormat.JPEG,
                                    100, fileOutputStream);
// 相片的完整路径
                            pathImage = file.getPath();
                            Log.e("path",pathImage);
                            photo=null;
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            if (fileOutputStream != null) {
                                try {
                                    fileOutputStream.close();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        Toast.makeText(getApplicationContext(), "获取到了",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "找不到图片",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            }
    }
    //刷新图片
    @Override
    protected void onResume() {
        super.onResume();
        if(imageItem.size()<6) {
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
    /*
     * Dialog对话框提示用户删除操作
     * position为删除图片位置
     */
    protected void dialog(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(CrowdFundJoinActivity.this);
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
}
