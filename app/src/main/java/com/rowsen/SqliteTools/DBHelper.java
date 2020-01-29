package com.rowsen.SqliteTools;

import android.content.Context;
import android.util.Log;

import com.rowsen.examfinal.Myapp;

import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "ElecExam.db";  //数据库名字
    public static int DATABASE_VERSION = Myapp.version;         //数据库版本号

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    /**
     * 创建数据库表：person
     * _id为主键，自增
     **/
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
/*        Log.i("TAG:", "创建数据库表！");
        //从assets复制到数据库app目录
        try {
            copyAssetsToDB(context);
        } catch (IOException e) {
            e.printStackTrace();
        }

         //Marine
         sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS "+tableName+"(_id INTEGER PRIMARY KEY AUTOINCREMENT," +
         "Part INT,PartName TEXT,Chapter INT,ChapterName TEXT,Question VARCHAR,Question_img VARCHAR,A TEXT,B TEXT,C TEXT,Answer TEXT,Type INT)");*/
        /**
         //焊接作业
         sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS "+tableName+"(_id INTEGER PRIMARY KEY AUTOINCREMENT," +
         "Question VARCHAR,A TEXT,B TEXT,C TEXT,Answer TEXT,Type INT)");
         //电工作业
         sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS "+tableName+"(_id INTEGER PRIMARY KEY AUTOINCREMENT," +
         "Question VARCHAR,A TEXT,B TEXT,C TEXT,Answer TEXT,Type INT)");
         **/
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        Log.i("TAG:", "升级数据库表！");
        //sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS " + tableName + "(_id INTEGER PRIMARY KEY AUTOINCREMENT," +
        // " Part INT,PartName TEXT,Chapter INT,ChapterName TEXT,Question VARCHAR,Question_img VARCHAR,A TEXT,B TEXT,C TEXT,Answer TEXT,Type INT)");
    }

    @Override
    public void onOpen(SQLiteDatabase sqLiteDatabase) {
        super.onOpen(sqLiteDatabase);
    }

}
