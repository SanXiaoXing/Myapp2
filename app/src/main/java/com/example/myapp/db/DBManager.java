package com.example.myapp.db;

//管理数据库的类

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.myapp.utils.FloatUtils;

import java.util.ArrayList;
import java.util.List;

public class DBManager {

    private static SQLiteDatabase db;
    //初始化数据库
    public static void initDB(Context context){
        DBOpenHelper helper = new DBOpenHelper(context);//得到帮助类对象
        db = helper.getWritableDatabase();//得到数据库对象

    }
    /**
     * 读取数据库当中的数据，写入内存集合
     * kind：表示收入或支出
     */
    public static List<TypeBean>getTypeList(int kind){
        List<TypeBean>list = new ArrayList<>();
        //读取typetb 数据
        String sql = "select * from typetb where kind = " +kind;
        Cursor cursor = db.rawQuery(sql, null);
        //循环读取游标内容，存储到对象中
        while (cursor.moveToNext()){
            String typename = cursor.getString(cursor.getColumnIndexOrThrow("typename"));
            int imageId = cursor.getInt(cursor.getColumnIndexOrThrow("imageId"));
            int sImageId = cursor.getInt(cursor.getColumnIndexOrThrow("sImageId"));
            int kind1 = cursor.getInt(cursor.getColumnIndexOrThrow("kind"));
            int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
            TypeBean typeBean = new TypeBean(id, typename, imageId, sImageId, kind);
            list.add(typeBean);
        }
        return list;
    }

    //向记账表插入一条元素
    public static void insertItemToAccounttb(AccountBean bean){
        ContentValues values = new ContentValues();
        values.put("typename",bean.getTypename());
        values.put("sImageId",bean.getsImageId());
        values.put("remarks",bean.getRemarks());
        values.put("money",bean.getMoney());
        values.put("time",bean.getTime());
        values.put("year",bean.getYear());
        values.put("month",bean.getMonth());
        values.put("day",bean.getDay());
        values.put("kind",bean.getKind());
        db.insert("accounttb",null,values);
    }

    /*
     * 获取记账表当中某一天的所有支出或者收入情况
     * */
    public static List<AccountBean>getAccountListOneDayFromAccounttb(int year,int month,int day){
        List<AccountBean>list = new ArrayList<>();
        String sql = "select * from accounttb where year=? and month=? and day=? order by id desc";
        Cursor cursor = db.rawQuery(sql, new String[]{year + "", month + "", day + ""});
        //遍历符合要求的每一行数据
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
            String typename = cursor.getString(cursor.getColumnIndexOrThrow("typename"));
            String remarks = cursor.getString(cursor.getColumnIndexOrThrow("remarks"));
            String time = cursor.getString(cursor.getColumnIndexOrThrow("time"));
            int sImageId = cursor.getInt(cursor.getColumnIndexOrThrow("sImageId"));
            int kind = cursor.getInt(cursor.getColumnIndexOrThrow("kind"));
            float money = cursor.getFloat(cursor.getColumnIndexOrThrow("money"));
            AccountBean accountBean = new AccountBean(id, typename, sImageId, remarks, money, time, year, month, day, kind);
            list.add(accountBean);
        }
        return list;
    }

    /*
     * 获取记账表当中某一月的所有支出或者收入情况
     * */
    public static List<AccountBean>getAccountListOneMonthFromAccounttb(int year,int month){
        List<AccountBean>list = new ArrayList<>();
        String sql = "select * from accounttb where year=? and month=? order by id desc";
        Cursor cursor = db.rawQuery(sql, new String[]{year + "", month + ""});
        //遍历符合要求的每一行数据
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
            String typename = cursor.getString(cursor.getColumnIndexOrThrow("typename"));
            String remarks = cursor.getString(cursor.getColumnIndexOrThrow("remarks"));
            String time = cursor.getString(cursor.getColumnIndexOrThrow("time"));
            int sImageId = cursor.getInt(cursor.getColumnIndexOrThrow("sImageId"));
            int kind = cursor.getInt(cursor.getColumnIndexOrThrow("kind"));
            float money = cursor.getFloat(cursor.getColumnIndexOrThrow("money"));
            int day = cursor.getInt(cursor.getColumnIndexOrThrow("day"));
            AccountBean accountBean = new AccountBean(id, typename, sImageId, remarks, money, time, year, month, day, kind);
            list.add(accountBean);
        }
        return list;
    }

    /**
    * @Auther: SanXiaoXing
    * @Date: on 2023-03
    * @Description: 获取某一天的支出或者收入的总金额   kind：支出——0    收入——1
    */
    public static float getSumMoneyOneDay(int year,int month,int day,int kind){
        float total = 0.0f;
        String sql = "select sum(money) from accounttb where year=? and month=? and day=? and kind=?";
        Cursor cursor = db.rawQuery(sql, new String[]{year + "", month + "", day + "", kind + ""});
        // 遍历
        if (cursor.moveToFirst()) {
            float money = cursor.getFloat(cursor.getColumnIndexOrThrow("sum(money)"));
            total = money;
        }
        return total;
    }
    /**
    * @Auther: SanXiaoXing
    * @Date: on 2023-03
    * @Description: 获取某个月的支出或者收入的总金额   kind：支出——0   收入——1
    */
    public static float getSumMoneyOneMonth(int year,int month,int kind){
        float total = 0.0f;
        String sql = "select sum(money) from accounttb where year=? and month=? and kind=?";
        Cursor cursor = db.rawQuery(sql, new String[]{year + "", month + "", kind + ""});
        // 遍历
        if (cursor.moveToFirst()) {
            float money = cursor.getFloat(cursor.getColumnIndexOrThrow("sum(money)"));
            total = money;
        }
        return total;
    }

    /**
    * @Auther: SanXiaoXing
    * @Date: on 2023-03
    * @Description: 统计某月份支出或者收入情况有多少条  收入-1   支出-0
    */
    public static int getCountItemOneMonth(int year,int month,int kind){
        int total = 0;
        String sql = "select count(money) from accounttb where year=? and month=? and kind=?";
        Cursor cursor = db.rawQuery(sql, new String[]{year + "", month + "", kind + ""});
        if (cursor.moveToFirst()) {
            int count = cursor.getInt(cursor.getColumnIndexOrThrow("count(money)"));
            total = count;
        }
        return total;
    }
    /**
    * @Auther: SanXiaoXing
    * @Date: on 2023-03
    * @Description: 获取某一年的支出或者收入的总金额   kind：支出——0    收入——1
    */
    public static float getSumMoneyOneYear(int year,int kind){
        float total = 0.0f;
        String sql = "select sum(money) from accounttb where year=? and kind=?";
        Cursor cursor = db.rawQuery(sql, new String[]{year + "", kind + ""});
        // 遍历
        if (cursor.moveToFirst()) {
            float money = cursor.getFloat(cursor.getColumnIndexOrThrow("sum(money)"));
            total = money;
        }
        return total;
    }
    
    /**
    * @Auther: SanXiaoXing
    * @Date: on 2023-03
    * @Description: 根据传入的id，删除accounttb表当中的一条数据
    */
    public static int deleteItemFromAccounttbById(int id) {
        int i = db.delete("accounttb", "id=?", new String[]{id + ""});
        return i;
    }

    /**
    * @Auther: SanXiaoXing
    * @Date: on 2023-03
    * @Description: 根据备注搜索收入或者支出的情况列表
    */
    public static List<AccountBean>getAccountListByRemarkFromAccounttb(String remarks){
        List<AccountBean>list = new ArrayList<>();
        String sql = "select * from accounttb where remarks like '%"+remarks+"%'";
        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
            String typename = cursor.getString(cursor.getColumnIndexOrThrow("typename"));
            String bz = cursor.getString(cursor.getColumnIndexOrThrow("remarks"));
            String time = cursor.getString(cursor.getColumnIndexOrThrow("time"));
            int sImageId = cursor.getInt(cursor.getColumnIndexOrThrow("sImageId"));
            int kind = cursor.getInt(cursor.getColumnIndexOrThrow("kind"));
            float money = cursor.getFloat(cursor.getColumnIndexOrThrow("money"));
            int year = cursor.getInt(cursor.getColumnIndexOrThrow("year"));
            int month = cursor.getInt(cursor.getColumnIndexOrThrow("month"));
            int day = cursor.getInt(cursor.getColumnIndexOrThrow("day"));
            AccountBean accountBean = new AccountBean(id, typename, sImageId, bz, money, time, year, month, day, kind);
            list.add(accountBean);
        }
        return list;
    }
    /**
    * @Auther: SanXiaoXing
    * @Date: on 2023-03
    * @Description: 查询记账又几个年份的信息
    */
    public static List<Integer>getYearListFromAccounttb(){
        List<Integer>list = new ArrayList<>();
        String sql = "select distinct(year) from accounttb order by year asc";
        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            int year = cursor.getInt(cursor.getColumnIndexOrThrow("year"));
            list.add(year);
        }
        return list;
    }

    /**
    * @Auther: SanXiaoXing
    * @Date: on 2023-03
    * @Description: 删除accounttb表格当中的所有数据
    */
    public static void deleteAllAccount(){
        String sql = "delete from accounttb";
        db.execSQL(sql);
    }

    /**
    * @Auther: SanXiaoXing
    * @Date: on 2023-03
    * @Description: 查询指定年份和月份的收入或者支出每一种类型的总钱数
    */
    public static List<ChartItemBean>getChartListFromAccounttb(int year,int month,int kind){
        List<ChartItemBean>list = new ArrayList<>();
        float sumMoneyOneMonth = getSumMoneyOneMonth(year, month, kind);  //求出支出或者收入总钱数
        String sql = "select typename,sImageId,sum(money)as total from accounttb where year=? and month=? and kind=? group by typename " +
                "order by total desc";
        Cursor cursor = db.rawQuery(sql, new String[]{year + "", month + "", kind + ""});
        while (cursor.moveToNext()) {
            int sImageId = cursor.getInt(cursor.getColumnIndexOrThrow("sImageId"));
            String typename = cursor.getString(cursor.getColumnIndexOrThrow("typename"));
            float total = cursor.getFloat(cursor.getColumnIndexOrThrow("total"));

            //计算所占百分比  total /sumMonth
            float ratio = FloatUtils.div(total,sumMoneyOneMonth);
            ChartItemBean bean = new ChartItemBean(sImageId, typename, ratio, total);
            list.add(bean);
        }
        return list;

        }

    public static float getMaxMoneyOneDayInMonth(int year,int month,int kind){
        String sql = "select sum(money) from accounttb where year=? and month=? and kind=? group by day order by sum(money) desc";
        Cursor cursor = db.rawQuery(sql, new String[]{year + "", month + "", kind + ""});
        if (cursor.moveToFirst()) {
            float money = cursor.getFloat(cursor.getColumnIndexOrThrow("sum(money)"));
            return money;
        }
        return 0;
    }
    /** 根据指定月份每一日收入或者支出的总钱数的集合*/
    public static List<BarChartItemBean>getSumMoneyOneDayInMonth(int year,int month,int kind){
        String sql = "select day,sum(money) from accounttb where year=? and month=? and kind=? group by day";
        Cursor cursor = db.rawQuery(sql, new String[]{year + "", month + "", kind + ""});
        List<BarChartItemBean>list = new ArrayList<>();
        while (cursor.moveToNext()) {
            int day = cursor.getInt(cursor.getColumnIndexOrThrow("day"));
            float smoney = cursor.getFloat(cursor.getColumnIndexOrThrow("sum(money)"));
            BarChartItemBean itemBean = new BarChartItemBean(year, month, day, smoney);
            list.add(itemBean);
        }
        return list;
    }
}


