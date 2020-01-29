package com.rowsen.mytools;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;

import com.rowsen.examfinal.Bean;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Random;

/*
    目标：制作一个常用的工具类集合
    1、构造时传入一个上下文，获取程序的环境，再传入一个文件名，拿到raw目录的该文件（txt），
    最好是拿到文件对象的方式，但是现在没有找到方法，只能拿到inputStream流
    2、考试app的文件处理方法file2list，需要提供2个参数，一个是要处理的文件流，第二个是处理后需要保存到的集合对象
    3、list集合对象序列化到xml文件,2个参数，一个是需要序列化的list对象，一个是最终保存的文件对象
    4、抽题工具，随机获取一组题，选择题40个，判断题60个，形成一个list集合
    5、截图保存功能
 */
public class Tools {

    public static void get2question(List<Bean> resource, List<Bean> target, int num, int offset) {
        Random r = new Random();
        for (int n = 1; n <= num; ) {
            Bean ob = resource.get(r.nextInt(resource.size()));
            //Log.e(ob.No,ob.question+"\r\nA、"+ob.answer1+"\r\nB、"+ob.answer2+"\r\nC、"+ob.answer3+"\r\nD、"+ob.answer4+"\r\n答案："+ob.corAns);
            if (!target.contains(ob)) {
                Bean sb = new Bean(ob);
                sb.No = offset + n + "";
                target.add(sb);
                //Log.e(n + "", ob.question + "\r\nA、" + ob.answer1 + "\r\nB、" + ob.answer2 + "\r\nC、" + ob.answer3 + "\r\nD、" + ob.answer4 + "\r\n答案：" + ob.corAns);
                n++;
            }
        }
    }

    public static String dif2time(long dif) {
        long hour = dif / (1000 * 60 * 60);
        long minute = dif / (1000 * 60) - 60 * hour;
        return hour + ":" + minute;
    }

    // 获取指定Activity的截屏，保存到png文件
    public static Bitmap takeScreenShot(Activity activity) {
        // View是你需要截图的View
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap b1 = view.getDrawingCache();
        // 获取状态栏高度
        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;
        Log.i("TAG", "" + statusBarHeight);
        // 获取屏幕长和高
        int width = activity.getWindowManager().getDefaultDisplay().getWidth();
        int height = activity.getWindowManager().getDefaultDisplay()
                .getHeight();
        // 去掉状态栏，如果需要的话
        // Bitmap b = Bitmap.createBitmap(b1, 0, 25, 320, 455);
        Bitmap b = Bitmap.createBitmap(b1, 0, statusBarHeight, width, height
                - statusBarHeight);
        view.destroyDrawingCache();
        return b;
    }

    // 保存到sdcard
    private static void savePic(Bitmap b, String strFileName) {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(strFileName);
            if (null != fos) {
                b.compress(Bitmap.CompressFormat.PNG, 90, fos);
                fos.flush();
                fos.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 程序入口，外界直接调用此方法即可
    public static void shoot(Activity a) {
        Tools.savePic(Tools.takeScreenShot(a), Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath() + "/Rowsen.png");
    }

    /**
     * 将图片转换成Base64编码的字符串
     */
    public static String imageToBase64(String path) {
        if (TextUtils.isEmpty(path)) {
            return null;
        }
        InputStream is = null;
        byte[] data = null;
        String result = null;
        try {
            is = new FileInputStream(path);
            //创建一个字符流大小的数组。
            data = new byte[is.available()];
            //写入数组
            is.read(data);
            //用默认的编码格式进行编码
            result = Base64.encodeToString(data, Base64.NO_WRAP);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != is) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    public static String imageToBase64(InputStream is) {
        byte[] data = null;
        String result = null;
        try {
            //创建一个字符流大小的数组。
            data = new byte[is.available()];
            //写入数组
            is.read(data);
            //用默认的编码格式进行编码
            result = Base64.encodeToString(data, Base64.NO_WRAP);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != is) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    /**
     * 将Base64编码转换为图片
     *
     * @param base64Str
     * @param path
     * @return true
     */
    public static boolean base64ToFile(String base64Str, String path) {
        byte[] data = Base64.decode(base64Str, Base64.NO_WRAP);
        for (int i = 0; i < data.length; i++) {
            if (data[i] < 0) {
                //调整异常数据
                data[i] += 256;
            }
        }
        OutputStream os = null;
        try {
            os = new FileOutputStream(path);
            os.write(data);
            os.flush();
            os.close();
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static Bitmap base64ToBitmap(String base64) {
        byte[] data = Base64.decode(base64, Base64.NO_WRAP);
        for (int i = 0; i < data.length; i++) {
            if (data[i] < 0) {
                //调整异常数据
                data[i] += 256;
            }
        }
        return BitmapFactory.decodeByteArray(data, 0, data.length);
    }
}
