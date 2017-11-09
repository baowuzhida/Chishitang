package Link;

import java.io.File;


import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;



public class UploadFileTask extends AsyncTask<String, Void, String> {
    public static final String requestURL = "http://119.23.205.112:8080/eatCanteen_war/ImageServlet";
    /**
     * 可变长的输入参数，与AsyncTask.exucute()对应
     */
    private ProgressDialog pdialog;
    private AppCompatActivity context = null;

    public UploadFileTask(AppCompatActivity ctx) {
        this.context = ctx;
        pdialog = ProgressDialog.show(context, "正在加载...", "系统正在处理您的请求");
    }

    @Override
    protected void onPostExecute(String result) {
        // 返回HTML页面的内容
        pdialog.dismiss();
        if (UploadUtil.SUCCESS.equalsIgnoreCase(result)) {
            Toast.makeText(context, "上传成功!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "上传失败!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onPreExecute() {
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
    }

    @Override
    protected String doInBackground(String... params) {
        File file = new File(params[0]);
        return UploadUtil.uploadFile(file, requestURL);
    }

    @Override
    protected void onProgressUpdate(Void... values) {
    }

}