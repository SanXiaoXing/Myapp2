package com.example.myapp.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.myapp.R;

public class DBOpenHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "myapp.db";
    public DBOpenHelper(@Nullable Context context) {
        super(context,DATABASE_NAME, null, 02);
    }

    //创建数据库的方法，只有项目第一次运行时会被调用
    @Override
    public void onCreate(SQLiteDatabase db) {
        //创建表示类型的表
        String sql = "create table typetb(id integer primary key autoincrement,typename varchar(10),imageId integer,sImageId integer,kind integer)";
        db.execSQL(sql);
        insertType(db);
        //创建记账表
        sql = "create table accounttb(id integer primary key autoincrement,typename varchar(10),sImageId integer,remarks varchar(80),money float,"+
                "time varchar(60),year integer,month integer,day integer,kind integer)";
        db.execSQL(sql);

    }

    private void insertType(SQLiteDatabase db) {
        //向typetb表中插入元素
        String sql = "insert into typetb (typename,imageId,sImageId,kind) values(?,?,?,?)";
        db.execSQL(sql,new Object[]{"其他", R.mipmap.more1,R.mipmap.more,0});
        db.execSQL(sql,new Object[]{"餐饮", R.mipmap.eat,R.mipmap.eat1,0});
        db.execSQL(sql,new Object[]{"交通", R.mipmap.bus,R.mipmap.bus1,0});
        db.execSQL(sql,new Object[]{"购物", R.mipmap.shopping,R.mipmap.shopping1,0});
        db.execSQL(sql,new Object[]{"服饰", R.mipmap.cloth,R.mipmap.cloth1,0});
        db.execSQL(sql,new Object[]{"日用品", R.mipmap.dayuse,R.mipmap.dayuse1,0});
        db.execSQL(sql,new Object[]{"娱乐", R.mipmap.enjoy,R.mipmap.enjoy1,0});
        db.execSQL(sql,new Object[]{"零食", R.mipmap.snack,R.mipmap.snack1,0});
        db.execSQL(sql,new Object[]{"饮料", R.mipmap.drink,R.mipmap.drink1,0});
        db.execSQL(sql,new Object[]{"学习", R.mipmap.study,R.mipmap.study1,0});
        db.execSQL(sql,new Object[]{"医疗", R.mipmap.sick,R.mipmap.sick1,0});
        db.execSQL(sql,new Object[]{"房租", R.mipmap.home,R.mipmap.home1,0});
        db.execSQL(sql,new Object[]{"水电", R.mipmap.water,R.mipmap.water1,0});
        db.execSQL(sql,new Object[]{"通讯", R.mipmap.phone,R.mipmap.phone1,0});
        db.execSQL(sql,new Object[]{"多余可省", R.mipmap.litter,R.mipmap.litter1,0});


        db.execSQL(sql,new Object[]{"其他", R.mipmap.qita,R.mipmap.qita1,1});
        db.execSQL(sql,new Object[]{"薪资", R.mipmap.money,R.mipmap.money1,1});
        db.execSQL(sql,new Object[]{"奖金", R.mipmap.jiangjin,R.mipmap.jiangjin1,1});
        db.execSQL(sql,new Object[]{"借入", R.mipmap.dkw,R.mipmap.dkw1,1});
        db.execSQL(sql,new Object[]{"收债", R.mipmap.zhai,R.mipmap.zhai1,1});
        db.execSQL(sql,new Object[]{"利息", R.mipmap.lixi,R.mipmap.lixi1,1});
        db.execSQL(sql,new Object[]{"投资", R.mipmap.touzi,R.mipmap.touzi1,1});

    }

    //数据库版本更新时发生改变，会调用方法
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
