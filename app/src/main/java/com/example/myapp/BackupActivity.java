package com.example.myapp;

import android.app.backup.BackupManager;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapp.db.DBOpenHelper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class BackupActivity extends AppCompatActivity {
    DBOpenHelper dbOpenHelper;
    BackupManager backupManager;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_back);
//        backupManager = new BackupManager(this);
//        dbOpenHelper = new DBOpenHelper(this); // 初始化DBOpenHelper
        // 备份按钮点击事件
//        findViewById(R.id.main_back_btn_backup).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                backupManager.dataChanged();
//                Toast.makeText(BackupActivity.this, "数据已备份", Toast.LENGTH_SHORT).show();
//            }
//        });
    }

    // 恢复数据库
    public void onRestoreClick(View view) {
        try {
            // 请求用户授权访问文件
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("application/octet-stream");
            startActivityForResult(intent, 123);

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "请求文件访问授权失败", Toast.LENGTH_SHORT).show();
        }
    }
    // 在活动返回结果时恢复数据库
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 123 && resultCode == RESULT_OK && data != null) {
            // 获取用户选择的文件的URI
            Uri uri = data.getData();

            try {
                // 执行数据库恢复
                ParcelFileDescriptor parcelFileDescriptor = getContentResolver().openFileDescriptor(uri, "r");
                FileInputStream inputStream = new FileInputStream(parcelFileDescriptor.getFileDescriptor());
//                FileOutputStream outputStream = new FileOutputStream("backup.db");
//                byte[] buffer = new byte[1024];
//                int length;
//                while ((length = inputStream.read(buffer)) > 0) {
//                    outputStream.write(buffer, 0, length);
//                }
//                outputStream.flush();
//                outputStream.close();
                // 检查数据库助手对象是否初始化，如果未初始化，则初始化该对象
                if (dbOpenHelper == null) {
                    dbOpenHelper = new DBOpenHelper(this);
                }

                // 从备份文件恢复数据库和表数据
                SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
                db.beginTransaction();
                try {
                    db.execSQL("delete from " + DBOpenHelper.DATABASE_NAME);
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = inputStream.read(buffer)) > 0) {
                        db.execSQL(new String(buffer, 0, length));
                    }
                    db.setTransactionSuccessful();

                } catch (SQLException | IOException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "呦，😔失败了", Toast.LENGTH_SHORT).show();
                } finally {
                    db.endTransaction();
                }

                //关闭输入流
                inputStream.close();

                // 通知BackupManager数据已经更改
                backupManager.dataChanged();

                Toast.makeText(this, "恢复数据成功了呦，😏", Toast.LENGTH_SHORT).show();

            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "呦，😔失败了", Toast.LENGTH_SHORT).show();
            }
        }
    }



    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.about_iv_back:
                finish();
                break;
            case R.id.main_back_btn_backup:
                backupDatabase();
                break;
        }
    }
    public void backupDatabase() {
        try {
            // 打开数据源
            SQLiteDatabase db = SQLiteDatabase.openDatabase("/data/data/"+ getPackageName() +"/databases/myapp.db", null, SQLiteDatabase.OPEN_READWRITE);
            // 获取备份文件路径
            String backupFileName = "backup.db";

            File folder = new File("/storage/emulated/0/Download/SanXiaoXing");
            if (!folder.exists()) {
                folder.mkdirs();
            }
            File backupFile = new File(folder, backupFileName);
            // 备份
            // 打开或创建目标数据库
            SQLiteDatabase newdb = openOrCreateDatabase("backup", MODE_PRIVATE, null);
            // 创建名为“backup”的表
            newdb.execSQL("CREATE TABLE IF NOT EXISTS backup (id INTEGER PRIMARY KEY AUTOINCREMENT,typename varchar(10),sImageId integer,remarks varchar(80),money float," +
                    "time varchar(60),year integer,month integer,day integer,kind integer);");


            PrintWriter writer = new PrintWriter(new OutputStreamWriter(new FileOutputStream(backupFile), "UTF-8"));
            Cursor cursor = db.rawQuery("SELECT * FROM accounttb", null);
            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                String typename = cursor.getString(cursor.getColumnIndexOrThrow("typename"));
                String remarks = cursor.getString(cursor.getColumnIndexOrThrow("remarks"));
                String time = cursor.getString(cursor.getColumnIndexOrThrow("time"));
                int sImageId = cursor.getInt(cursor.getColumnIndexOrThrow("sImageId"));
                int kind = cursor.getInt(cursor.getColumnIndexOrThrow("kind"));
                float money = cursor.getFloat(cursor.getColumnIndexOrThrow("money"));
                writer.println("INSERT INTO backup (id, typename, remarks, time, sImageId, kind, money) VALUES (" + id + ", '" + typename + "', '"
                        + remarks + "', '" + time + "', " + sImageId + ", '" + kind + "', " + money + ");");
            }
            cursor.close();
            writer.close();
            // 关闭数据源
            db.close();
            Toast.makeText(this, "备份成功了👉呦，😊", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "呦，😔失败了", Toast.LENGTH_LONG).show();
        }
    }
//    private void restoreDatabase() {
//        try {
//            // 获取备份文件路径
//            String backupFileName = "backup.db";
////            File backupFile = new File(getExternalFilesDir(null), backupFileName);
////            File backupFile = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsoluteFile();
//
//            File folder = new File("/storage/emulated/0/Download/SanXiaoXing");
//            File backupFile = new File(folder, backupFileName);
//            if (!backupFile.exists()) {
//                Toast.makeText(this, "没有文件啊😏", Toast.LENGTH_LONG).show();
//                return;
//            }
//
//            // 打开目标数据库
//            SQLiteDatabase db = openOrCreateDatabase("backup", MODE_PRIVATE, null);
//
//            // 恢复
//            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(backupFile), "UTF-8"));
//            String line;
//            db.beginTransaction();
//            while ((line = reader.readLine()) != null) {
//                db.execSQL(line);
//            }
//            db.setTransactionSuccessful();
//            db.endTransaction();
//            reader.close();
//
//            // 关闭目标数据库
//            db.close();
//
//            Toast.makeText(this, "恢复数据成功了呦，😊", Toast.LENGTH_LONG).show();
//        } catch (IOException e) {
//            e.printStackTrace();
//            Toast.makeText(this, "呦，😔又没成功恢复", Toast.LENGTH_LONG).show();
//        }
//    }



}
