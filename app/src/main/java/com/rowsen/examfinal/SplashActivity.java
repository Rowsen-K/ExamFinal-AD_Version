package com.rowsen.examfinal;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.miui.zeus.mimo.sdk.ad.AdWorkerFactory;
import com.miui.zeus.mimo.sdk.ad.IAdWorker;
import com.miui.zeus.mimo.sdk.listener.MimoAdListener;
import com.qq.e.ads.splash.SplashAD;
import com.qq.e.ads.splash.SplashADListener;
import com.qq.e.comm.util.AdError;
import com.roger.match.library.MatchTextView;
import com.rowsen.SqliteTools.SQLFunction;
import com.rowsen.mytools.BaseActivity;
import com.rowsen.mytools.PermissionListener;
import com.xiaomi.ad.common.pojo.AdType;

import java.io.IOException;
import java.util.List;

import static android.view.View.GONE;
import static com.rowsen.examfinal.Myapp.GDT_APPID;


public class SplashActivity extends BaseActivity {
    MatchTextView matchTextView;
    MatchTextView matchTextView2;
    Handler handler;
    ImageView logo_pic;
    ViewGroup mi_container;
    ViewGroup GDT_container;
    ViewGroup logo;
    IAdWorker mWorker;
    public String[] permissions = null;
    String POSITION_ID;
    //Mi测试参数
    //private static final String POSITION_ID ="b373ee903da0c6fc9c9da202df95a500";
    String GTD_SplashID;
    //广点通开屏测试ID
    //String GTD_SplashID = "8863364436303842593";
    SplashAD splashAD;

    //跳转状态
    boolean jump_state = false;

    //穿山甲广告平台
/*
    String TT_SplashID = "835909200";
    private TTAdNative mTTAdNative;
    private FrameLayout tt_container;
*/

    /**
     * 开屏页一定要禁止用户对返回按钮的控制，否则将可能导致用户手动退出了App而广告无法正常曝光和计费
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_HOME) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        try {
            if (mWorker != null)
                mWorker.recycle();
        } catch (Exception e) {
            e.printStackTrace();
        }
        matchTextView = null;
        matchTextView2 = null;
        handler = null;
        splashAD = null;
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if (getSupportActionBar() != null)
            getSupportActionBar().hide();
        setContentView(R.layout.activity_splash);
        if (Myapp.hw_Version) {
            //广点通开屏ID---华为版本
            GTD_SplashID = "2010394840539862";
            //Mi开屏广告ID---华为版本
            POSITION_ID = "983a690f3f742be56e2a107e6cd1363c";
        } else {
            //广点通开屏ID---通用版本
            GTD_SplashID = "3020360826493817";
            //Mi开屏广告ID---通用版本
            POSITION_ID = "a897ffc9980907b94239cc601e160ce7";
        }

        logo_pic = findViewById(R.id.logo_pic);
        mi_container = findViewById(R.id.mi_container);
        GDT_container = findViewById(R.id.GDT_container);
        //tt_container = findViewById(R.id.tt_container);
        logo = findViewById(R.id.logo);
        matchTextView = findViewById(R.id.match);
        matchTextView2 = findViewById(R.id.match2);
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 1:
                        logo_show();
                        break;
                    case 2:
                        if (matchTextView != null) matchTextView.hide();
                        if (matchTextView2 != null) matchTextView2.hide();
                        sendEmptyMessageDelayed(3, 1080);
                        break;
                    case 3:
                        if (!jump_state)
                            jump();
                        break;
                }
            }
        };

        // 如果api >= 23 需要显式申请权限
        permissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_PHONE_STATE};
        if (Build.VERSION.SDK_INT >= 23)
            grant(permissions);
            //小于23的版本直接干活
        else {
            //miSplashAD();
            // ttSplashAD();
            copyDB("ElecExam.db");
            gdtSplashAD();
        }
    }


    //授权失败或异常递归授权
    void grant(final String[] permissions) {
        requestRunTimePermission(permissions, new PermissionListener() {
            //授权成功
            @Override
            public void onGranted() {
                //ttSplashAD();
                copyDB("ElecExam.db");
                gdtSplashAD();
                //miSplashAD();
            }

            //授权失败
            @Override
            public void onGranted(List<String> grantedPermission) {

            }

            @Override
            public void onDenied(List<String> deniedPermission) {
                String[] permission = new String[deniedPermission.size()];
                grant(deniedPermission.toArray(permission));
            }
        });
    }

    //显示广告
    void miSplashAD() {
        try {
            mWorker = AdWorkerFactory.getAdWorker(SplashActivity.this, mi_container, new MimoAdListener() {
                @Override
                public void onAdPresent() {
                    // 开屏广告展示
                    //Log.d("展示", "onAdPresent");
                    logo_pic.setVisibility(View.GONE);
                    //tt_container.setVisibility(GONE);
                    GDT_container.setVisibility(GONE);
                }

                @Override
                public void onAdClick() {
                    //用户点击了开屏广告
                    //Log.d("点击", "onAdClick");
                    if (!jump_state) jump();
                }

                @Override
                public void onAdDismissed() {
                    //这个方法被调用时，表示从开屏广告消失。
                    //Log.d("消失", "onAdDismissed");
                    if (!jump_state) jump();
                }

                @Override
                public void onAdFailed(String s) {
                    Log.e("失败", "ad fail message : " + s);
                    //gdtSplashAD();
                    logo_show();
                }

                @Override
                public void onAdLoaded(int size) {
                    logo_pic.setVisibility(View.GONE);
                    //tt_container.setVisibility(GONE);
                    GDT_container.setVisibility(GONE);
                }

                @Override
                public void onStimulateSuccess() {
                }
            }, AdType.AD_SPLASH);
            mWorker.loadAndShow(POSITION_ID);
        } catch (Exception e) {
            e.printStackTrace();
            //gdtSplashAD();
            handler.sendEmptyMessage(1);
        }
    }

    //GTD广告
    /*
     * 拉取开屏广告，开屏广告的构造方法有3种，详细说明请参考开发者文档。
     *
     * @param activity        展示广告的 activity
     * @param adContainer     展示广告的大容器
     * @param skipContainer   自定义的跳过按钮：传入该 view 给 SDK 后，SDK 会自动给它绑定点击跳过事件。SkipView 的样式可以由开发者自由定制，其尺寸限制请参考 activity_splash.xml 或下面的注意事项。
     * @param appId           应用 ID
     * @param GDT_posId           广告位 ID
     * @param adListener      广告状态监听器
     * @param fetchDelay      拉取广告的超时时长：即开屏广告从请求到展示所花的最大时长（并不是指广告曝光时长）取值范围[3000, 5000]，设为0表示使用广点通 SDK 默认的超时时长。
     */
    private void gdtSplashAD() {
        //GDT_container.setVisibility(View.VISIBLE);
        splashAD = new SplashAD(this, null, GDT_APPID, GTD_SplashID, new SplashADListener() {
            @Override
            public void onADDismissed() {
                if (!jump_state) jump();
            }

            @Override
            public void onNoAD(AdError adError) {
                //logo_show();
                miSplashAD();
            }

            @Override
            public void onADPresent() {
                logo_pic.setVisibility(GONE);
                //tt_container.setVisibility(GONE);
            }

            @Override
            public void onADClicked() {
                if (!jump_state)
                    jump();
            }

            @Override
            public void onADTick(long l) {

            }

            @Override
            public void onADExposure() {

            }
        }, 3000, null);
        splashAD.fetchAndShowIn(GDT_container);
    }

    //广告拉取失败或异常时显示的界面
    void logo_show() {
        WindowManager wm = getWindowManager();
        Display d = wm.getDefaultDisplay();
        logo.getLayoutParams().height = d.getHeight();
        logo.getLayoutParams().width = d.getWidth();
        logo_pic.setVisibility(View.GONE);

        matchTextView.setTextSize(100);
        matchTextView.setTextColor(getResources().getColor(R.color.white));
        matchTextView.setText("Design By");
        matchTextView.setProgress(0.5F);

        matchTextView2.setTextSize(100);
        matchTextView2.setTextColor(getResources().getColor(R.color.white));
        matchTextView2.setText("Rowsen");
        matchTextView2.setProgress(0.5F);
        handler.sendEmptyMessageDelayed(2, 3000);
    }

    //先清除全屏状态跳转主页,否则会导致主页动画失效
    void jump() {
        jump_state = true;
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setAttributes(lp);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        startActivity(new Intent(SplashActivity.this, MainActivity.class));
        finish();
    }

    //copy数据库
    void copyDB(final String fileName) {
        //Log.e("开干","ing");
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    SQLFunction.copyAssetsToDB(SplashActivity.this, fileName, Myapp.version);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    //加载只穿山甲广告
   /* void ttSplashAD() {
        mTTAdNative = app.ttAdManager.createAdNative(this);
        //step3:创建开屏广告请求参数AdSlot,具体参数含义参考文档
        AdSlot adSlot = new AdSlot.Builder()
                .setCodeId(TT_SplashID)
                .setSupportDeepLink(true)
                .setImageAcceptedSize(1080, 1920)
                .build();
        //step4:请求广告，调用开屏广告异步请求接口，对请求回调的广告作渲染处理
        mTTAdNative.loadSplashAd(adSlot, new TTAdNative.SplashAdListener() {
            @Override
            @MainThread
            public void onError(int code, String message) {
                Log.e("error", message);
                gdtSplashAD();
                *//*mHasLoaded = true;
                showToast(message);
                goToMainActivity();*//*
            }

            @Override
            @MainThread
            public void onTimeout() {
                Log.e("error", "开屏广告加载超时");
                gdtSplashAD();
               *//* mHasLoaded = true;
                showToast("开屏广告加载超时");
                goToMainActivity();*//*
            }

            @Override
            @MainThread
            public void onSplashAdLoad(TTSplashAd ad) {
                Log.d("error", "开屏广告请求成功");
                logo_pic.setVisibility(View.GONE);
               *//* mHasLoaded = true;
                mHandler.removeCallbacksAndMessages(null);*//*
                if (ad == null) {
                    return;
                }
                //获取SplashView
                View view = ad.getSplashView();
                if (view != null) {
                    tt_container.removeAllViews();
                    //把SplashView 添加到ViewGroup中,注意开屏广告view：width >=70%屏幕宽；height >=50%屏幕高
                    tt_container.addView(view);
                    //设置不开启开屏广告倒计时功能以及不显示跳过按钮,如果这么设置，您需要自定义倒计时逻辑
                    //ad.setNotAllowSdkCountdown();
                } else {
                    *//*goToMainActivity();*//*
                }

                //设置SplashView的交互监听器
                ad.setSplashInteractionListener(new TTSplashAd.AdInteractionListener() {
                    @Override
                    public void onAdClicked(View view, int type) {
                        Log.d("message", "onAdClicked");
                        if (!jump_state) jump();
                    }

                    @Override
                    public void onAdShow(View view, int type) {
                        Log.d("message", "onAdShow");
                        logo_pic.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAdSkip() {
                        Log.d("message", "onAdSkip");
                        if (!jump_state) jump();
                       *//* showToast("开屏广告跳过");
                        goToMainActivity();*//*

                    }

                    @Override
                    public void onAdTimeOver() {
                        Log.d("message", "onAdTimeOver");
                        if (!jump_state) jump();
                        *//*showToast("开屏广告倒计时结束");
                        goToMainActivity();*//*
                    }
                });
                if (ad.getInteractionType() == TTAdConstant.INTERACTION_TYPE_DOWNLOAD) {
                    ad.setDownloadListener(new TTAppDownloadListener() {
                        boolean hasShow = false;

                        @Override
                        public void onIdle() {

                        }

                        @Override
                        public void onDownloadActive(long totalBytes, long currBytes, String fileName, String appName) {
                            if (!hasShow) {
                                //showToast("下载中...");
                                hasShow = true;
                            }
                        }

                        @Override
                        public void onDownloadPaused(long totalBytes, long currBytes, String fileName, String appName) {
                            //showToast("下载暂停...");

                        }

                        @Override
                        public void onDownloadFailed(long totalBytes, long currBytes, String fileName, String appName) {
                            //showToast("下载失败...");

                        }

                        @Override
                        public void onDownloadFinished(long totalBytes, String fileName, String appName) {

                        }

                        @Override
                        public void onInstalled(String fileName, String appName) {

                        }
                    });
                }
            }
        }, 5000);

    }*/

}