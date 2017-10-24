package Link;

/**
 * Created by Baowuzhida on 2017/8/16.
 */
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import Bean.ShoppingCartBean;

public class CartDao {
    private DBOpenHelper dbOpenHelper;// 创建DBOpenHelper对象
    private SQLiteDatabase sqliteDatabase;// 创建SQLiteDatabase对象

    public CartDao(Context context)// 定义构造函数
    {
        dbOpenHelper = new DBOpenHelper(context, null, null, 0);// 初始化DBOpenHelper对象
    }

    // 插入用户数据
    public void dbInsert(Integer product_id,String product_image, String product_name,String product_detail,String product_address,Double product_price,Integer product_count) {
        sqliteDatabase = dbOpenHelper.getWritableDatabase();// 以读写方法打开数据库，不仅仅是写，getReadableDatabase()是只读
        String sql = "insert into cart("+
                "product_id,"+
                "product_image," +
                "product_name," +
                "product_detail," +
                "product_address," +
                "product_price," +
                "product_count) " +
                "values (?,?,?,?,?,?,?)";
        // 传递过来的username与password分别按顺序替换上面sql语句的两个?，自动转换类型，下同，不再赘述
        Object bindArgs[] = new Object[] { product_id,product_image,product_name,product_detail,product_address,product_price,product_count };
        // 执行这条无返回值的sql语句
        sqliteDatabase.execSQL(sql, bindArgs);
        sqliteDatabase.close();
    }

    // 求出表中有多少条数据
    public int dbGetCartSize() {
        sqliteDatabase = dbOpenHelper.getWritableDatabase();
        String sql = "select count(*) from cart where isDel=0";
        Cursor cursor = sqliteDatabase.rawQuery(sql, null);
        if (cursor.moveToNext())// 判断Cursor中是否有数据
        {
            return cursor.getInt(0);// 返回总记录数
        }
        return 0;// 如果没有数据，则返回0
    }

    // 根据PID查找信息
    public ShoppingCartBean dbQueryOneByPID(Integer id) {
        sqliteDatabase = dbOpenHelper.getWritableDatabase();
        String sql = "select * from cart where product_id=?";
        String[] selectionArgs = new String[] {String.valueOf(id)};
        Cursor cursor = sqliteDatabase.rawQuery(sql, selectionArgs);
        if (cursor.moveToNext())// 判断Cursor中是否有数据
        {
            // 如果有用户，则把查到的值填充这个用户实体
            ShoppingCartBean cartBean = new ShoppingCartBean();
            cartBean.setProduct_id(cursor.getInt(cursor
                    .getColumnIndex("product_id")));
            cartBean.setCount(cursor.getInt(cursor
                    .getColumnIndex("product_count")));
            cartBean.setShoppingName(cursor.getString(cursor
                    .getColumnIndex("product_name")));
            cartBean.setImageUrl(cursor.getString(cursor
                    .getColumnIndex("product_image")));
            cartBean.setDetail(cursor.getString(cursor
                    .getColumnIndex("product_detail")));
            cartBean.setPrice(cursor.getDouble(cursor
                    .getColumnIndex("product_price")));
            cartBean.setAddress(cursor.getInt(cursor
                    .getColumnIndex("product_address")));
            sqliteDatabase.close();
            return cartBean;// 返回一个用户给前台
        }
        sqliteDatabase.close();
        return null;// 没有返回null
    }

    // 查询所有购物车
    public ArrayList<ShoppingCartBean> dbQueryAll() {
        ArrayList<ShoppingCartBean> cartArrayList = new ArrayList<ShoppingCartBean>();
        sqliteDatabase = dbOpenHelper.getWritableDatabase();
        String sql = "select * from cart";
        Cursor cursor = sqliteDatabase.rawQuery(sql, null);
        // 游标从头读到尾
        try{
        for (cursor.moveToFirst(); !(cursor.isAfterLast()); cursor.moveToNext()) {
            ShoppingCartBean cartBean = new ShoppingCartBean();
//            cartBean.setId(cursor.getInt(cursor
//                    .getColumnIndex("id")));
            cartBean.setProduct_id(cursor.getInt(cursor
                    .getColumnIndex("product_id")));
            cartBean.setCount(cursor.getInt(cursor
                    .getColumnIndex("product_count")));
            cartBean.setShoppingName(cursor.getString(cursor
                    .getColumnIndex("product_name")));
            cartBean.setImageUrl(cursor.getString(cursor
                    .getColumnIndex("product_image")));
            cartBean.setDetail(cursor.getString(cursor
                    .getColumnIndex("product_detail")));
            cartBean.setPrice(cursor.getDouble(cursor
                    .getColumnIndex("product_price")));
            cartBean.setAddress(cursor.getInt(cursor
                    .getColumnIndex("product_address")));
            cartArrayList.add(cartBean);
            sqliteDatabase.close();
        }
        }catch (SQLException e){
            e.printStackTrace();
        }
        sqliteDatabase.close();
        return cartArrayList;
    }

    public void dbUpdateCart(int id, int count) {
        sqliteDatabase = dbOpenHelper.getWritableDatabase();
        String sql = "update cart set product_count=? where product_id=?";
        Object bindArgs[] = new Object[] { count, id };
        sqliteDatabase.execSQL(sql, bindArgs);
        sqliteDatabase.close();
    }

    // 删除Cart，通过product id
    public void dbDeleteCart(int id) {
        sqliteDatabase = dbOpenHelper.getWritableDatabase();
        String sql = " delete from cart where product_id=?";
        Object bindArgs[] = new Object[] { id };
        sqliteDatabase.execSQL(sql, bindArgs);
        sqliteDatabase.close();
    }

    public void dbDeleteAll() {
        sqliteDatabase = dbOpenHelper.getWritableDatabase();
        String sql = " delete from cart";
        sqliteDatabase.execSQL(sql);
        sqliteDatabase.close();
    }

}
