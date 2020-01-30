package com.rowsen.examfinal;

import android.app.Activity;
import android.app.Application;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import com.miui.zeus.mimo.sdk.MimoSdk;
import com.tencent.android.tpush.XGIOperateCallback;
import com.tencent.android.tpush.XGPushConfig;
import com.tencent.android.tpush.XGPushManager;
import com.tencent.stat.StatService;

import net.sqlcipher.database.SQLiteDatabase;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;


public class Myapp extends Application {
    public static int version = 1;
    public static boolean hw_Version = false;
    private static Myapp instance;
    static ArrayList<Bean> list;
    public static String exam_table;
    public static ArrayList<Activity> activitys;
    static final String APP_ID = "2882303761517942601";
    static final String APP_KEY = "5121794213601";
    static final String APP_TOKEN = "Qa9OGvRbqTmDRy5aZENIEA==";
    //Mi测试参数
    /*static final String APP_ID = "2882303761517411490";
    static final String APP_TOKEN = "fake_app_token";
    static final String APP_KEY ="fake_app_key";*/

    //广点通APPID
    public static final String GDT_APPID = "1108173909";

    //广点通测试APPID
    //static final String GTD_APPID = "1101152570";

    //穿山甲
/*    static final String TT_APPID = "5035909";
    public TTAdManager ttAdManager;*/

    //穿山甲测试ID
    //static final String TT_APPID = "5001121";


    @Override
    public void onCreate() {
        super.onCreate();
        SQLiteDatabase.loadLibs(this);
        try {
            version = getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        // 腾讯移动分析---打开Logcat输出，上线时，一定要关闭
        //StatConfig.setDebugEnable(true);
        // 注册activity生命周期，统计时长
        StatService.registerActivityLifecycleCallbacks(this);

        MimoSdk.setEnableUpdate(false);
        //MimoSdk.setDebug(true);
        // 正式上线时候务必关闭stage
        //MimoSdk.setStageOn();
        MimoSdk.init(this, APP_ID, APP_KEY, APP_TOKEN, null);

        disableAPIDialog();
        activitys = new ArrayList<>();
        list = new ArrayList<>();
        instance = this;

        XGPushConfig.enableOtherPush(this, true);
        XGPushConfig.setMiPushAppId(this, "2882303761517942601");
        XGPushConfig.setMiPushAppKey(this, "5121794213601");
        XGPushManager.registerPush(this, new XGIOperateCallback() {
            @Override
            public void onSuccess(Object data, int flag) {
                //token在设备卸载重装的时候有可能会变
                Log.d("TPush", "注册成功，设备token为：" + data);
            }

            @Override
            public void onFail(Object data, int errCode, String msg) {
                Log.d("TPush", "注册失败，错误码：" + errCode + ",错误信息：" + msg);
            }
        });


        //穿山甲初始化
        /*ttAdManager = TTAdSdk.init(this,
                new TTAdConfig.Builder()
                        .appId(TT_APPID)
                        .useTextureView(false) //使用TextureView控件播放视频,默认为SurfaceView,当有SurfaceView冲突的场景，可以使用TextureView
                        .appName("电工复审考试")
                        .titleBarTheme(TTAdConstant.TITLE_BAR_THEME_DARK)
                        .allowShowNotify(true) //是否允许sdk展示通知栏提示
                        .allowShowPageWhenScreenLock(true) //是否在锁屏场景支持展示广告落地页
                        .debug(true) //测试阶段打开，可以通过日志排查问题，上线时去除该调用
                        .directDownloadNetworkType(TTAdConstant.NETWORK_STATE_WIFI, TTAdConstant.NETWORK_STATE_3G) //允许直接下载的网络状态集合
                        .supportMultiProcess(false) //是否支持多进程，true支持
                        //.httpStack(new MyOkStack3())//自定义网络库，demo中给出了okhttp3版本的样例，其余请自行开发或者咨询工作人员。
                        .build());*/

    }

    public static Myapp getInstance() {
        return instance;
    }

    /**
     * 反射 禁止弹窗
     */
    private void disableAPIDialog() {
        if (Build.VERSION.SDK_INT < 28) return;
        try {
            Class clazz = Class.forName("android.app.ActivityThread");
            Method currentActivityThread = clazz.getDeclaredMethod("currentActivityThread");
            currentActivityThread.setAccessible(true);
            Object activityThread = currentActivityThread.invoke(null);
            Field mHiddenApiWarningShown = clazz.getDeclaredField("mHiddenApiWarningShown");
            mHiddenApiWarningShown.setAccessible(true);
            mHiddenApiWarningShown.setBoolean(activityThread, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
