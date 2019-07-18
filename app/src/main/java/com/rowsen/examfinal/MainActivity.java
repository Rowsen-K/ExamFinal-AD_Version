package com.rowsen.examfinal;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.View;

import com.dreamlive.hotimglibrary.entity.HotArea;
import com.dreamlive.hotimglibrary.utils.FileUtils;
import com.dreamlive.hotimglibrary.view.HotClickView;
import com.github.glomadrian.grav.GravView;
import com.rowsen.mytools.BaseActivity;

import java.io.InputStream;

public class MainActivity extends BaseActivity {
    HotClickView mHotView;
    String code;
    GravView grav;
    Myapp app = Myapp.getInstance();

    @Override
    public void onBackPressed() {
        super.onBackPressed();
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
        initall();
        initDatas();
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
                        selectionIntent.putParcelableArrayListExtra("selectionList", Myapp.selectionList);
                        //  startActivity(selectionIntent, ActivityOptionsCompat.makeScaleUpAnimation(mHotView, (pos[0] + pos[2]) / 2, (pos[1] + pos[5]) / 2, pos[2] - pos[0], pos[5] - pos[1]).toBundle());
                        startActivity(selectionIntent);
                        break;
                    case "PD":
                        Intent judgeIntent = new Intent(MainActivity.this, JudgeActivity.class);
                        judgeIntent.putParcelableArrayListExtra("judgeList", Myapp.judgeList);
                        // startActivity(judgeIntent, ActivityOptionsCompat.makeScaleUpAnimation(mHotView, (pos[0] + pos[2]) / 2, (pos[1] + pos[5]) / 2, pos[2] - pos[0], pos[5] - pos[1]).toBundle());
                        startActivity(judgeIntent);
                        break;
                    case "about":
                        Intent aboutIntent = new Intent(MainActivity.this, AboutActivity.class);
                        aboutIntent.putExtra("code", code);
                        //  aboutIntent.putExtra("num",num);
                        //startActivity(aboutIntent, ActivityOptionsCompat.makeScaleUpAnimation(mHotView, (pos[0] + pos[2]) / 2, (pos[1] + pos[5]) / 2, pos[2] - pos[0], pos[5] - pos[1]).toBundle());
                        startActivity(aboutIntent);
                        break;
                    case "ZL":
                        Intent zlIntent = new Intent(MainActivity.this, ZlActivity.class);
                        // zlIntent.putParcelableArrayListExtra("selectionList", selectionList);
                        //  zlIntent.putParcelableArrayListExtra("judgeList", judgeList);
                        //  zlIntent.putParcelableArrayListExtra("all", allList);
                        //  startActivity(zlIntent, ActivityOptionsCompat.makeScaleUpAnimation(mHotView, (pos[0] + pos[2]) / 2, (pos[1] + pos[5]) / 2, pos[2] - pos[0], pos[5] - pos[1]).toBundle());
                        startActivity(zlIntent);
                        break;
                    case "KS":
                        Intent ExamIntent = new Intent(MainActivity.this, ExamActivity.class);
                        ExamIntent.putExtra("code", code);
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

}
