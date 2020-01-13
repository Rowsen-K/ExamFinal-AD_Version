package com.rowsen.examfinal;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.dreamlive.hotimglibrary.entity.HotArea;
import com.dreamlive.hotimglibrary.utils.FileUtils;
import com.dreamlive.hotimglibrary.view.HotClickView;
import com.github.glomadrian.grav.GravView;
import com.rowsen.mytools.BannerAD;
import com.rowsen.mytools.BaseActivity;
import com.rowsen.mytools.Tools;

import java.io.IOException;
import java.io.InputStream;

import es.dmoral.toasty.Toasty;

public class MainActivity extends BaseActivity {
    HotClickView mHotView;
    String exam_type;
    GravView grav;
    FrameLayout type_select;
    ViewGroup GDT_banner;
    ViewGroup Mi_banner;
    Myapp app = Myapp.getInstance();
    //穿山甲广告
/*    ViewGroup TT_banner;
    String TT_posId = "935909648";*/
    /*    TTAdNative mTTAdNative;
        TTNativeExpressAd mTTAd;*/
    //gdt横幅广告ID
    String GDT_posId = "2050594035277672";
    //BannerView bv;
    //Mi横幅ID
    String Mi_posId = "9b45afa5ea4decaaecb0aaad06de100c";
    //static final String Mi_posId = "19684d52bf8ab255e13f387b3dff4f41";
    BannerAD bannerAD;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        grav = null;
        app = null;
        mHotView = null;
        bannerAD = null;
        for (Activity act : Myapp.activitys) {
            if (act != null)
                act.finish();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //  requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_main);
        grav = findViewById(R.id.grav);
        type_select = findViewById(R.id.type_select);
        //TT_banner = findViewById(R.id.tt_banner);
        GDT_banner = findViewById(R.id.type_GDT_banner);
        Mi_banner = findViewById(R.id.type_mi_banner);
        initall();
        initDatas();
        //banner_GDT();
        //banner_TT(TT_posId);
        //bannerAD = new BannerAD();
        bannerAD = new BannerAD(this, GDT_posId, GDT_banner, Mi_posId, Mi_banner);
        bannerAD.banner_GDT2(this, GDT_banner);
        //bannerAD.banner_Mi(Mi_posId);
        // 4、 设置监听事件
        mHotView.setOnClickListener(new HotClickView.OnClickListener() {
            @Override
            public void OnClick(View view, HotArea hotArea) {
                //Toast.makeText(MainActivity.this, "现在进入" + hotArea.getDesc(), Toast.LENGTH_SHORT).show();
                //  Toasty.success(MainActivity.this, "现在进入" + hotArea.getDesc(), Toast.LENGTH_SHORT).show();
                //grav.setVisibility(View.INVISIBLE);
                // int[] pos = hotArea.getPts();
                switch (hotArea.getAreaId()) {
                    case "XZ":
                        Intent selectionIntent = new Intent(MainActivity.this, SelectionActivity.class);
                        selectionIntent.putExtra("exam_type", exam_type);
                        //selectionIntent.putParcelableArrayListExtra("selectionList", Myapp.selectionList);
                        //  startActivity(selectionIntent, ActivityOptionsCompat.makeScaleUpAnimation(mHotView, (pos[0] + pos[2]) / 2, (pos[1] + pos[5]) / 2, pos[2] - pos[0], pos[5] - pos[1]).toBundle());
                        startActivity(selectionIntent);
                        break;
                    case "PD":
                        Intent judgeIntent = new Intent(MainActivity.this, JudgeActivity.class);
                        //judgeIntent.putParcelableArrayListExtra("judgeList", Myapp.judgeList);
                        // startActivity(judgeIntent, ActivityOptionsCompat.makeScaleUpAnimation(mHotView, (pos[0] + pos[2]) / 2, (pos[1] + pos[5]) / 2, pos[2] - pos[0], pos[5] - pos[1]).toBundle());
                        startActivity(judgeIntent);
                        break;
                    case "about":
                        Intent aboutIntent = new Intent(MainActivity.this, AboutActivity.class);
                        //aboutIntent.putExtra("code", code);
                        //  aboutIntent.putExtra("num",num);
                        //startActivity(aboutIntent, ActivityOptionsCompat.makeScaleUpAnimation(mHotView, (pos[0] + pos[2]) / 2, (pos[1] + pos[5]) / 2, pos[2] - pos[0], pos[5] - pos[1]).toBundle());
                        startActivity(aboutIntent);
                        break;
                    case "ZL":
                        Intent zlIntent = new Intent(MainActivity.this, ZlActivity.class);
                        zlIntent.putExtra("exam_type", exam_type);
                        // zlIntent.putParcelableArrayListExtra("selectionList", selectionList);
                        //  zlIntent.putParcelableArrayListExtra("judgeList", judgeList);
                        //  zlIntent.putParcelableArrayListExtra("all", allList);
                        //  startActivity(zlIntent, ActivityOptionsCompat.makeScaleUpAnimation(mHotView, (pos[0] + pos[2]) / 2, (pos[1] + pos[5]) / 2, pos[2] - pos[0], pos[5] - pos[1]).toBundle());
                        startActivity(zlIntent);
                        break;
                    case "KS":
                        Intent ExamIntent = new Intent(MainActivity.this, ExamActivity.class);
                        ExamIntent.putExtra("exam_type", exam_type);
                        //  ExamIntent.putParcelableArrayListExtra("selectionList", selectionList);
                        //  ExamIntent.putParcelableArrayListExtra("judgeList", judgeList);
                        //  startActivity(ExamIntent, ActivityOptionsCompat.makeScaleUpAnimation(mHotView, (pos[0] + pos[2]) / 2, (pos[1] + pos[5]) / 2, pos[2] - pos[0], pos[5] - pos[1]).toBundle());
                        startActivity(ExamIntent);
                        break;
                }
            }
        });
        //非广告版的激活码发送逻辑
        /*TelephonyManager manager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(app, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_PHONE_STATE}, 1);
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        final String imei = manager.getDeviceId();
        final String imsi = manager.getSubscriberId();
        num = manager.getLine1Number();
        final String brand = Build.BRAND;
        final String manufacturer = Build.MANUFACTURER;
        final String model = Build.MODEL;
        final String version = Build.VERSION.RELEASE;
        if (num != null && !num.equals(""))
            code = (Long.valueOf(num) + Long.valueOf(imei)) + "";
        else {
            num = imei;
            if (imsi != null && !imsi.equals(""))
                code = (Long.valueOf(imsi) + Long.valueOf(num)) + "";
            else code = (Long.valueOf(imei) + 608) + "";
        }
        Myapp.sp.edit().putString("num", num).commit();
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    Connection connection = Jsoup.connect("https://sc.ftqq.com/SCU32217T8041223572b586408b08c1ba3b5c03135b9b147839307.send?text=用户：" + num + "的设备信息：");
                    connection.method(Connection.Method.POST);
                    connection.data("desp", "品牌：" + brand + "\n\n型号：" + model + "\n\n生产厂家：" + manufacturer + "\n\n系统版本：" + version + "\n\nIMEI：" + imei + "\n\nIMSI：" + imsi);
                    connection.execute();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();*/
    }

    void initall() {
        mHotView = findViewById(R.id.a_main_hotview);
        //  2、 禁止缩放

        mHotView.setCanScale(false);
        // 3、 禁止滑动

        mHotView.setCanMove(false);

    }

    protected void initDatas() {
        AssetManager assetManager = getResources().getAssets();
        InputStream imgInputStream = null;
        InputStream fileInputStream = null;
        try {
            imgInputStream = assetManager.open("main_final.png");
            fileInputStream = assetManager.open("main.xml");
            mHotView.setImageBitmap(fileInputStream, imgInputStream, HotClickView.FIT_XY);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            FileUtils.closeInputStream(imgInputStream);
            FileUtils.closeInputStream(fileInputStream);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // grav.start();
        // grav.setVisibility(View.VISIBLE);
    }

    //读取题库文件
    void readXml(final String exam_type, final boolean mode) {
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    Myapp.selectionList.clear();
                    Myapp.judgeList.clear();
                    Myapp.allList.clear();
                    Tools.file2list(getAssets().open(exam_type + "selection.txt"), Myapp.selectionList, mode);
                    Tools.file2list(getAssets().open(exam_type + "judge.txt"), Myapp.judgeList, mode);
                    Myapp.allList.addAll(Myapp.selectionList);
                    Myapp.allList.addAll(Myapp.judgeList);
                    //Tools.list2xml(Myapp.allList, new File(getFilesDir().getPath(), "Exam.xml"));
                } catch (IOException e) {
                    e.printStackTrace();
                    //handler.sendEmptyMessage(4);
                }
            }
        }.start();
    }

    public void exam_select(View view) {
        exam_type = ((TextView) view).getText().toString();
        if ("上岗证".equals(exam_type))
            readXml(exam_type, false);
        else readXml(exam_type, true);
        type_select.setVisibility(View.GONE);
        Toasty.success(this, exam_type).show();
    }

    //显示GDT横幅广告
   /* void banner_GDT() {
        bv = new BannerView(this, ADSize.BANNER, GDT_APPID, GDT_posId);
        // 注意：如果开发者的banner不是始终展示在屏幕中的话，请关闭自动刷新，否则将导致曝光率过低。
        // 并且应该自行处理：当banner广告区域出现在屏幕后，再手动loadAD。
        bv.setRefresh(30);
        bv.setADListener(new AbstractBannerADListener() {

            @Override
            public void onADClicked() {
                super.onADClicked();
            }

            @Override
            public void onADClosed() {
                super.onADClosed();
            }

            @Override
            public void onNoAD(AdError error) {
                Log.i(
                        "AD_DEMO",
                        String.format("Banner onNoAD，eCode = %d, eMsg = %s", error.getErrorCode(),
                                error.getErrorMsg()));
            }

            @Override
            public void onADReceiv() {
                TT_banner.setVisibility(View.GONE);
                GDT_banner.setVisibility(View.VISIBLE);
                Log.i("AD_DEMO", "ONBannerReceive");
            }
        });
        GDT_banner.addView(bv);
        bv.loadAD();
    }*/

    //显示穿山甲广告
/*    private void banner_TT(String codeId) {
        mTTAdNative = app.ttAdManager.createAdNative(this);
        TT_banner.removeAllViews();
        float expressViewWidth = 350;
        float expressViewHeight = 350;
        try{
            expressViewWidth = getWindowManager().getDefaultDisplay().getWidth();
            expressViewHeight = 60;
        }catch (Exception e){
            expressViewHeight = 0; //高度设置为0,则高度会自适应
        }
        //step4:创建广告请求参数AdSlot,具体参数含义参考文档
        AdSlot adSlot = new AdSlot.Builder()
                .setCodeId(TT_posId) //广告位id
                .setSupportDeepLink(true)
                .setAdCount(3) //请求广告数量为1到3条
                .setExpressViewAcceptedSize(expressViewWidth,expressViewHeight) //期望模板广告view的size,单位dp
                .setImageAcceptedSize(640,320 )//这个参数设置即可，不影响模板广告的size
                .build();
        //step5:请求广告，对请求回调的广告作渲染处理
        mTTAdNative.loadBannerExpressAd(adSlot, new TTAdNative.NativeExpressAdListener() {
            @Override
            public void onError(int code, String message) {
                Log.e("load error : " , code + ", " + message);
                TT_banner.removeAllViews();
                banner_GDT();
            }

            @Override
            public void onNativeExpressAdLoad(List<TTNativeExpressAd> ads) {
                if (ads == null || ads.size() == 0){
                    return;
                }
                mTTAd = ads.get(0);
                mTTAd.setSlideIntervalTime(30*1000);
                bindAdListener(mTTAd);
                mTTAd.render();
            }
        });
    }

    private void bindAdListener(TTNativeExpressAd ad) {
        ad.setExpressInteractionListener(new TTNativeExpressAd.ExpressAdInteractionListener() {
            @Override
            public void onAdClicked(View view, int type) {

            }

            @Override
            public void onAdShow(View view, int type) {

            }

            @Override
            public void onRenderFail(View view, String msg, int code) {
                Log.e("ExpressView","render fail:");

            }

            @Override
            public void onRenderSuccess(View view, float width, float height) {
                //Log.e("ExpressView","render suc:");
                TT_banner.removeAllViews();
                TT_banner.addView(view);
            }
        });
    }*/
}
