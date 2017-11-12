package Link;

import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import static java.sql.DriverManager.println;


/**
 * Created by hasee on 2017/8/2.
 */

public class HttpUtil {

    private String adress;
    private String contents;
    private Handler handler;
    private static String cookie;
    private static SharedPrefsCookieUtil scookie;

    public HttpUtil(){}
    public HttpUtil(SharedPrefsCookieUtil scookie){
        this.scookie=scookie;
        cookie=scookie.getValue("cookie",null);
    }

    public void GetURL(String adress, Handler handler) {
        this.adress=adress;
        this.handler=handler;
        Thread thread = new Thread(runnableGet);
        thread.start();
    }

    public void PostURL(String adress, String contents, Handler handler) {
        this.adress=adress;
        this.contents=contents;
        this.handler=handler;
        Thread thread = new Thread(runnablePost);
        thread.start();
    }

    private Runnable runnableGet=new Runnable() {
        @Override
        public void run() {
            Message message=handler.obtainMessage();
            StringBuffer content=new StringBuffer();
            HttpURLConnection conn=null;
            try {
                URL url = new URL(adress);
                conn = (HttpURLConnection) url.openConnection();
                if (cookie != null) {
                    //发送cookie信息上去，以表明自己的身份，否则会被认为没有权限
                    conn.setRequestProperty("Cookie", cookie);
                }
                // 设置连接超时为3秒
                conn.setConnectTimeout(3000);
                // 设置请求类型为Get类型
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Content-Type","application/x-www-form-urlencoded;charset=UTF-8");
                // 判断请求Url是否成功
                if (conn.getResponseCode() != 200) {
                    throw new RuntimeException("请求url失败");
                }

                InputStream is = conn.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(is,"UTF-8"));
                String responseCookie = conn.getHeaderField("Set-Cookie");// 取到所用的Cookie
                if (responseCookie != null) {
                    //设置Cookie；
                    cookie = responseCookie.substring(0, responseCookie.indexOf(";"));
                    scookie.putValue("cookie",cookie);
                }
                String line = null;
                while ((line = br.readLine()) != null) {
                    content.append(line);
                }
                br.close();
                message.obj=content.toString();
            } catch (Exception e) {
                e.printStackTrace();
            }
            finally {
                conn.disconnect();
                handler.sendMessage(message);
            }
        }
    };

    public static String getCookie(){
        return cookie;
    }

    private Runnable runnablePost=new Runnable() {
        @Override
        public void run() {
            Message message=handler.obtainMessage();
            HttpURLConnection conn=null;
            StringBuffer content=new StringBuffer();
            try {
                URL url=new URL(adress);
                conn=(HttpURLConnection) url.openConnection();
                if (cookie != null) {
                    //发送cookie信息上去，以表明自己的身份，否则会被认为没有权限
                    conn.setRequestProperty("Cookie", cookie);
                }
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type","application/x-www-form-urlencoded;charset=UTF-8");//发送数据转码
                conn.setDoOutput(true);
                conn.setDoInput(true);
                conn.setUseCaches(false);
                conn.setConnectTimeout(3000);
                conn.setReadTimeout(3000);
                //向Post请求中添加数据
                OutputStream out = conn.getOutputStream();
                out.write(contents.getBytes());
                out.flush();

                InputStream is = conn.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(is,"UTF-8"));//接收数据转码
                String responseCookie = conn.getHeaderField("Set-Cookie");// 取到所用的Cookie
                if (responseCookie != null) {
                    //设置Cookie；
                    cookie = responseCookie.substring(0, responseCookie.indexOf(";"));
                    scookie.putValue("cookie",cookie);
                }
                String line = null;
                while ((line = br.readLine()) != null) {
                    content.append(line);
                }
                br.close();
                message.obj=content.toString();
            } catch (Exception e) {
                e.printStackTrace();
            }
            finally {
                conn.disconnect();
                handler.sendMessage(message);
            }
        }
    };
}
