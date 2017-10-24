package Link;

/**
 * Created by hasee on 2017/8/21.
 */

import android.content.Context;
import android.content.SharedPreferences;

/**
 * SharedPreferences存储数据方式工具类
 * @author zuolongsnail
 */

public class SharedPrefsCookieUtil {
    private final static String SETTING = "Cookies";
    private SharedPreferences.Editor sp;
    private SharedPreferences gp;
    public SharedPrefsCookieUtil(Context context){
        sp =  context.getSharedPreferences(SETTING, Context.MODE_PRIVATE).edit();
        gp =  context.getSharedPreferences(SETTING, Context.MODE_PRIVATE);
    }
    public void putValue(String key, int value) {
        sp.putInt(key, value);
        sp.commit();
    }
    public void putValue(String key, boolean value) {
        sp.putBoolean(key, value);
        sp.commit();
    }
    public  void putValue(String key, String value) {
        sp.putString(key, value);
        sp.commit();
    }
    public  int getValue(String key, int defValue) {
        int value = gp.getInt(key, defValue);
        return value;
    }
    public  boolean getValue(String key, boolean defValue) {
        boolean value = gp.getBoolean(key, defValue);
        return value;
    }
    public  String getValue(String key, String defValue) {
        String value = gp.getString(key, defValue);
        return value;
    }

}
