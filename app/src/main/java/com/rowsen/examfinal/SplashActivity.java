package com.rowsen.examfinal;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Display;
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
import com.rowsen.mytools.BaseActivity;
import com.rowsen.mytools.PermissionListener;
import com.rowsen.mytools.Tools;
import com.xiaomi.ad.common.pojo.AdType;

import java.io.IOException;
import java.util.List;

import es.dmoral.toasty.Toasty;

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
    //Mi开屏广告ID
    private static final String POSITION_ID = "a897ffc9980907b94239cc601e160ce7";
    //Mi测试参数
    //private static final String POSITION_ID ="b373ee903da0c6fc9c9da202df95a500";

    //广点通开屏ID
    String GTD_SplashID = "3020360826493817";
    //广点通开屏测试ID
    //String GTD_SplashID = "8863364436303842593";
    SplashAD splashAD;

    @Override
    public void onBackPressed() {
        // super.onBackPressed();
        matchTextView = null;
        matchTextView2 = null;
        handler = null;
        splashAD = null;
        finish();
    }

    @Override
    protected void onDestroy() {
        try {
            if (mWorker != null)
                mWorker.recycle();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        logo_pic = findViewById(R.id.logo_pic);
        mi_container = findViewById(R.id.mi_container);
        GDT_container = findViewById(R.id.GDT_container);
        logo = findViewById(R.id.logo);
        matchTextView = findViewById(R.id.match);
        matchTextView2 = findViewById(R.id.match2);
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 2:
                        matchTextView.hide();
                        matchTextView2.hide();
                        sendEmptyMessageDelayed(3, 1080);
                        break;
                    case 3:
                        jump();
                        break;
                    case 4:
                        Toasty.error(SplashActivity.this, "题库读取失败,请重开或重新安装App!").show();
                        break;
                }
            }
        };
        readXml();
        // 如果api >= 23 需要显式申请权限
        permissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_PHONE_STATE};
        if (Build.VERSION.SDK_INT >= 23)
            grant(permissions);
            //小于23的版本直接干活
        else show_ad();
    }


    //读取题库文件
    void readXml() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    Tools.file2list(getAssets().open("selection.txt"), Myapp.selectionList);
                    Tools.file2list(getAssets().open("judge.txt"), Myapp.judgeList);
                    Myapp.allList.addAll(Myapp.selectionList);
                    Myapp.allList.addAll(Myapp.judgeList);
                    //Tools.list2xml(Myapp.allList, new File(getFilesDir().getPath(), "Exam.xml"));
                } catch (IOException e) {
                    e.printStackTrace();
                    handler.sendEmptyMessage(4);
                }
            }
        }.start();
    }

    //授权失败或异常递归授权
    void grant(final String[] permissions) {
        requestRunTimePermission(permissions, new PermissionListener() {
            //授权成功
            @Override
            public void onGranted() {
                show_ad();
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
    void show_ad() {
/*        new Thread() {
            @Override
            public void run() {
                super.run();
                while (!MimoSdk.isSdkReady()) ;*/
        try {
            mWorker = AdWorkerFactory.getAdWorker(SplashActivity.this, mi_container, new MimoAdListener() {
                @Override
                public void onAdPresent() {
                    // 开屏广告展示
                    //Log.d("展示", "onAdPresent");
                    logo_pic.setVisibility(View.GONE);
                }

                @Override
                public void onAdClick() {
                    //用户点击了开屏广告
                    //Log.d("点击", "onAdClick");
                }

                @Override
                public void onAdDismissed() {
                    //这个方法被调用时，表示从开屏广告消失。
                    //Log.d("消失", "onAdDismissed");
                    jump();
                }

                @Override
                public void onAdFailed(String s) {
                    Log.e("失败", "ad fail message : " + s);
                    fetchSplashAD();
                }

                @Override
                public void onAdLoaded(int size) {
                    logo_pic.setVisibility(View.GONE);
                }

                @Override
                public void onStimulateSuccess() {
                }
            }, AdType.AD_SPLASH);
            mWorker.loadAndShow(POSITION_ID);
        } catch (Exception e) {
            e.printStackTrace();
            fetchSplashAD();
        }
/*            }
        }.start();*/
    }

    //GTD广告
    /*
     * 拉取开屏广告，开屏广告的构造方法有3种，详细说明请参考开发者文档。
     *
     * @param activity        展示广告的 activity
     * @param adContainer     展示广告的大容器
     * @param skipContainer   自定义的跳过按钮：传入该 view 给 SDK 后，SDK 会自动给它绑定点击跳过事件。SkipView 的样式可以由开发者自由定制，其尺寸限制请参考 activity_splash.xml 或下面的注意事项。
     * @param appId           应用 ID
     * @param posId           广告位 ID
     * @param adListener      广告状态监听器
     * @param fetchDelay      拉取广告的超时时长：即开屏广告从请求到展示所花的最大时长（并不是指广告曝光时长）取值范围[3000, 5000]，设为0表示使用广点通 SDK 默认的超时时长。
     */
    private void fetchSplashAD() {
        //GDT_container.setVisibility(View.VISIBLE);
        splashAD = new SplashAD(this, GDT_container, null, GDT_APPID, GTD_SplashID, new SplashADListener() {
            @Override
            public void onADDismissed() {
                jump();
            }

            @Override
            public void onNoAD(AdError adError) {
                logo_show();
            }

            @Override
            public void onADPresent() {
                logo_pic.setVisibility(GONE);
            }

            @Override
            public void onADClicked() {
                jump();
            }

            @Override
            public void onADTick(long l) {

            }

            @Override
            public void onADExposure() {

            }
        }, 3000);
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
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setAttributes(lp);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        startActivity(new Intent(SplashActivity.this, MainActivity.class));
        finish();
    }

}