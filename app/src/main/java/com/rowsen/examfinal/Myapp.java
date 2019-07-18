package com.rowsen.examfinal;

import android.app.Activity;
import android.app.Application;

import com.miui.zeus.mimo.sdk.MimoSdk;

import java.util.ArrayList;


public class Myapp extends Application {
    private static Myapp instance;
    static ArrayList<JudgeBean> judgeList;
    static ArrayList<SelectionBean> selectionList;
    static ArrayList allList;
    public static ArrayList<Activity> activitys;
    static final String APP_ID = "2882303761517942601";
    static final String APP_KEY = "5121794213601";
    static final String APP_TOKEN = "Qa9OGvRbqTmDRy5aZENIEA==";
    //Mi测试参数
    /*static final String APP_ID = "2882303761517411490";
    static final String APP_TOKEN = "fake_app_token";
    static final String APP_KEY ="fake_app_key";*/

    //广点通APPID
    static final String GDT_APPID = "1108173909";

    //广点通测试APPID
    //static final String GTD_APPID = "1101152570";
    @Override
    public void onCreate() {
        super.onCreate();
        activitys = new ArrayList<>();
        judgeList = new ArrayList<>();
        selectionList = new ArrayList<>();
        allList = new ArrayList();
        instance = this;
        MimoSdk.setEnableUpdate(false);
        // MimoSdk.setDebugOn();
        // 正式上线时候务必关闭stage
        // MimoSdk.setStageOn();
        MimoSdk.init(this, APP_ID, APP_KEY, APP_TOKEN);
    }

    public static Myapp getInstance() {
        return instance;
    }
}
