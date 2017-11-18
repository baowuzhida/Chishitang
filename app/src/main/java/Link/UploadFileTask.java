package Link;

import java.io.File;


import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import es.dmoral.toasty.Toasty;


public class UploadFileTask extends AsyncTask<String, Void, String> {

    public static final String requestURL = "http://119.23.205.112:8080/eatCanteen_war/ImageServlet";
    public static String type;
    /**
     * 可变长的输入参数，与AsyncTask.exucute()对应
     */
    private AppCompatActivity context = null;

    public UploadFileTask(AppCompatActivity ctx,String type) {
        this.context = ctx;
        this.type=type;
    }

    @Override
    protected void onPostExecute(String result) {
        // 返回HTML页面的内容

        if (UploadUtil.SUCCESS.equalsIgnoreCase(result)) {
            Toasty.success(context, "上传成功", Toast.LENGTH_SHORT, true).show();
            context.finish();
        } else {
            Toasty.error(context, "上传失败", Toast.LENGTH_SHORT, true).show();
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
        return UploadUtil.uploadFile(file, requestURL,type);
    }

    @Override
    protected void onProgressUpdate(Void... values) {
    }

 }