package com.rowsen.SqliteTools;

import android.content.Context;
import android.util.Log;

import com.rowsen.examfinal.Bean;

import net.sqlcipher.Cursor;
import net.sqlcipher.database.SQLiteDatabase;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import es.dmoral.toasty.Toasty;


public class SQLFunction {
    static DBHelper helper;
    public static SQLiteDatabase sqLiteDatabase;

    /**
     * 将assets下的资源复制到应用程序的databases目录下
     *
     * @param context  上下文
     * @param fileName assets下的资源的文件名
     */
    public static void copyAssetsToDB(Context context, String fileName, int version) throws IOException {
        //数据库的存储路径,该路径在：data/data/包名/databases目录下，
        String destPath = "data/data/" + context.getPackageName() + "/databases";
        //Log.i("tag","path---->"+destPath);
        File file = new File(destPath);
        if (!file.exists()) {
            file.mkdirs();  //创建目录
        } else {
            if (Arrays.asList(file.list()).contains(fileName)) {
                SQLiteDatabase db = SQLiteDatabase.openDatabase(destPath + "/" + fileName, "Rowsen0608", null, SQLiteDatabase.OPEN_READONLY);
                int ver = db.getVersion();
                db.close();
                Log.e("更新前数据库版本", ver + "");
                if (ver == version) return;
            }
        }
        //打开assest文件，获得输入流
        InputStream is = context.getAssets().open(fileName);
        BufferedInputStream bis = new BufferedInputStream(is);

        //获得写入文件的输出流
        FileOutputStream fos = new FileOutputStream(destPath + File.separator + fileName);
        BufferedOutputStream bos = new BufferedOutputStream(fos);

        byte[] data = new byte[2 * 1024];
        int len;
        while ((len = bis.read(data)) != -1) {
            bos.write(data, 0, len);
        }

        bos.flush();
        bis.close();
        bos.close();

        SQLiteDatabase db = SQLiteDatabase.openDatabase(destPath + "/" + fileName, "Rowsen0608", null, SQLiteDatabase.OPEN_READONLY);
        int ver = db.getVersion();
        db.close();
        Log.e("更新后数据库版本", ver + "");
    }

    public static void initTable(Context context) {
        if (helper == null)
            helper = new DBHelper(context);
        //如果表不存在就先创建表
        //Log.i("TAG:", "创建数据库表！");
        sqLiteDatabase = helper.getReadableDatabase("Rowsen0608");
        //电工表初始化
/*        if (D)
            sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS '" + tableName + "'(_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    " Question VARCHAR,Question_img VARCHAR,A TEXT,B TEXT,C TEXT,D TEXT,Answer TEXT,Type INT)");
        else
            sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS '" + tableName + "'(_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    " Question VARCHAR,Question_img VARCHAR,A TEXT,B TEXT,C TEXT,Answer TEXT,Type INT)");*/
        //Marine.db初始化
/*        if (D)
            sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS " + tableName + "(_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    " Part INT,PartName TEXT,Chapter INT,ChapterName TEXT,Question VARCHAR,Question_img VARCHAR,A TEXT,B TEXT,C TEXT,D TEXT,Answer TEXT,Type INT)");
        else
            sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS " + tableName + "(_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    " Part INT,PartName TEXT,Chapter INT,ChapterName TEXT,Question VARCHAR,Question_img VARCHAR,A TEXT,B TEXT,C TEXT,Answer TEXT,Type INT)");*/
    }

    //查询所有
    public static ArrayList queryAll(Context context, String tableName) {
        initTable(context);
        String sql = "select * from " + tableName;
        /**Cursor是结果集游标，使用Cursou.moveToNext()方法可以从当前行移动到下一行**/
        Cursor cursor = sqLiteDatabase.rawQuery(sql, null);
        int clos_len = cursor.getColumnCount();                 //获取数据所有列数
        Log.i("TAG:", "querySQLite()方法中获得总列数clos_len：" + clos_len);
        ArrayList<Bean> list = new ArrayList<>();
        if ("上岗证".equals(tableName))
            addAll(cursor, list, false);
        else
            addAll(cursor, list, true);
        Log.i("TAG:", "查询完毕，返回数据：" + list.size());
        return list;
    }

    /**
     * 【模糊查询】指定分部
     **/
    public static ArrayList queryType(Context context, String tableName, int type) {
        initTable(context);
        ArrayList list;
        String sql = "select * from " + tableName + " where Type = ?";
        if (type != 1 && type != 2) {
            Toasty.error(context, "获取考题类型失败，请退出重启软件！").show();
            return null;
        } else {
            /**Cursor是结果集游标，使用Cursou.moveToNext()方法可以从当前行移动到下一行**/
            net.sqlcipher.Cursor cursor = sqLiteDatabase.rawQuery(sql, new String[]{String.valueOf(type)});
            int clos_len = cursor.getColumnCount();                 //获取数据所有列数
            Log.i("TAG:", "querySQLite()方法中获得总列数clos_len：" + clos_len);
            list = new ArrayList<Bean>();
            if ("上岗证".equals(tableName))
                addAll(cursor, list, false);
            else
                addAll(cursor, list, true);
        }
        Log.i("TAG:", "查询完毕，返回数据：" + list.size());

        return list;
    }


    /**
     * 【插入数据】
     **/
    public static boolean insert(String tableName, Object[] data, boolean D) {

        Log.i("TAG:", "插入数据到数据库表：" + tableName + "中:" + data.toString());
        String sql = "";
        if (D)
            sql = "insert into " + tableName + " ( Question,Question_img,A,B,C,D,Answer,Type ) values (?,?,?,?,?,?,?,?)";
        else
            sql = "insert into " + tableName + " ( Question,Question_img,A,B,C,Answer,Type ) values (?,?,?,?,?,?,?)";
        boolean isSuccess = false;
        try {
            sqLiteDatabase.execSQL(sql, data);
            isSuccess = true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            /*if (sqLiteDatabase != null) {
                sqLiteDatabase.close();
            }*/
            Log.i("TAG:", "数据插入数据库中状态：" + isSuccess);
        }
        return isSuccess;
    }

    public static void addAll(Cursor cursor, List<Bean> list, boolean D) {
        if (cursor != null && cursor.moveToFirst()) {
            do {
                Bean sb = new Bean();
                if (D) {
                    sb.No = cursor.getString(0);
                    sb.question = cursor.getString(1);
                    //sb.img = cursor.getString(cursor.getColumnIndex(cursor.getColumnName(6)));
                    sb.answer1 = cursor.getString(3);
                    sb.answer2 = cursor.getString(4);
                    sb.answer3 = cursor.getString(5);
                    sb.answer4 = cursor.getString(6);
                    sb.corAns = cursor.getString(7);
                    sb.type = cursor.getInt(8);
                } else {
                    sb.No = cursor.getString(0);
                    sb.question = cursor.getString(1);
                    sb.img = cursor.getString(2);
                    sb.answer1 = cursor.getString(3);
                    sb.answer2 = cursor.getString(4);
                    sb.answer3 = cursor.getString(5);
                    sb.corAns = cursor.getString(6);
                    sb.type = cursor.getInt(7);
                }
                list.add(sb);
                //sb.toString();
            } while (cursor.moveToNext());
        }
        cursor.close();
    }
}
