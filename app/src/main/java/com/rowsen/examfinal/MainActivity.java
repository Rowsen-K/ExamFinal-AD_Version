package com.rowsen.examfinal;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.dreamlive.hotimglibrary.entity.HotArea;
import com.dreamlive.hotimglibrary.utils.FileUtils;
import com.dreamlive.hotimglibrary.view.HotClickView;
import com.github.glomadrian.grav.GravView;
import com.qq.e.ads.banner.ADSize;
import com.qq.e.ads.banner.AbstractBannerADListener;
import com.qq.e.ads.banner.BannerView;
import com.qq.e.comm.util.AdError;
import com.rowsen.mytools.BaseActivity;
import com.rowsen.mytools.Tools;

import java.io.IOException;
import java.io.InputStream;

import es.dmoral.toasty.Toasty;

import static com.rowsen.examfinal.Myapp.GDT_APPID;

public class MainActivity extends BaseActivity {
    HotClickView mHotView;
    String exam_type;
    GravView grav;
    FrameLayout type_select;
    ViewGroup GDT_banner;
    Myapp app = Myapp.getInstance();
    //gdt横幅广告ID
    String posId = "3080059597263454";
    BannerView bv;

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
        GDT_banner = findViewById(R.id.type_GDT_banner);
        initall();
        initDatas();
        banner_GDT();
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
                    Tools.file2list(getAssets().open(exam_type + "selection.txt"), Myapp.selectionList,mode);
                    Tools.file2list(getAssets().open(exam_type + "judge.txt"), Myapp.judgeList,mode);
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
            readXml(exam_type,false);
        else readXml(exam_type,true);
        type_select.setVisibility(View.GONE);
        Toasty.success(this,exam_type).show();
    }

    //显示GDT横幅广告
    void banner_GDT() {
        /*mi_banner.setVisibility(View.GONE);
        tmall.setVisibility(View.GONE);
        note.setVisibility(View.VISIBLE);
        GDT_banner.setVisibility(View.VISIBLE);*/
        bv = new BannerView(this, ADSize.BANNER, GDT_APPID, posId);
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
                GDT_banner.setVisibility(View.VISIBLE);
                Log.i("AD_DEMO", "ONBannerReceive");
            }
        });
        GDT_banner.addView(bv);
        bv.loadAD();
    }
}
