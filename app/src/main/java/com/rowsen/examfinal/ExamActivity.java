package com.rowsen.examfinal;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.percent.PercentRelativeLayout;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.andexert.library.RippleView;
import com.miui.zeus.mimo.sdk.ad.AdWorkerFactory;
import com.miui.zeus.mimo.sdk.ad.IAdWorker;
import com.miui.zeus.mimo.sdk.listener.MimoAdListener;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.qq.e.ads.banner.ADSize;
import com.qq.e.ads.banner.AbstractBannerADListener;
import com.qq.e.ads.banner.BannerView;
import com.qq.e.ads.banner2.UnifiedBannerADListener;
import com.qq.e.ads.banner2.UnifiedBannerView;
import com.qq.e.comm.util.AdError;
import com.rowsen.mytools.BaseActivity;
import com.rowsen.mytools.SnapUpCountDownTimerView;
import com.rowsen.mytools.Tools;
import com.xiaomi.ad.common.pojo.AdType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.dmoral.toasty.Toasty;

import static android.view.View.GONE;
import static com.rowsen.examfinal.Myapp.GDT_APPID;

public class ExamActivity extends BaseActivity {
    //Mi横幅广告ID
    static final String Mi_posId = "19684d52bf8ab255e13f387b3dff4f41";//802e356f1726f9ff39c69308bfd6f06a";
    ListView exam_listView;
    ListView wrong_listView;
    SnapUpCountDownTimerView clock;
    ArrayList exam;
    Map<String, String> ansMap;
    ArrayList wrongList;
    Button get;
    Dialog dialog;
    long startTime;
    int selectionNum;
    int judgeNum;
    Handler handler;
    TextView title;
    List source;
    BaseAdapter exam_list_adapter;

    ImageView tmall;
    ViewGroup mi_banner;
    ViewGroup GDT_banner;
    TextView note;
    //老版GDT
    BannerView bv;
    //GDT -banner 2.0
    UnifiedBannerView banner;
    boolean flag = true;//线程结束标记,用来防止oom
    Myapp app = Myapp.getInstance();
    //Mi
    IAdWorker mBannerAd;
    //浏览错题状态:浏览错题true
    boolean check_wrong_state = false;
    //广告点击状态
    boolean click_success_state = false;
    //gdt横幅广告ID
    //String GDT_posId = "3080059597263454";
    //gdt横幅2.0广告ID
    String GDT_posId = "4050869827503147";

    //广点通测试bannerID
    //String BannerPosID = "9079537218417626401";

    //穿山甲广告
/*    ViewGroup TT_banner;
    String TT_posId = "935909785";
    TTAdNative mTTAdNative;
    TTNativeExpressAd mTTAd;*/

    boolean exam_mode;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_exam);
        Toasty.error(this, "点击广告获得完整的考试体验！").show();
        //TT_banner = findViewById(R.id.exam_TT_banner);
        GDT_banner = findViewById(R.id.GDT_banner);
        mi_banner = findViewById(R.id.mi_banner);
        tmall = findViewById(R.id.tmall);
        if ("上岗证".equals(getIntent().getStringExtra("exam_type")))
            exam_mode = false;
        else exam_mode = true;
        tmall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                click_success();
                /*Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                intent.setData(Uri.parse("https://s.click.taobao.com/t?e=m%3D2%26s%3DhuuEKODW7pYcQipKwQzePCperVdZeJviK7Vc7tFgwiFRAdhuF14FMVLXV5QSu4A%2BJ1gyddu7kN9nRUqImSSjf9IAswdBIv%2F6RSzO4qoxmkB7VlEYTFHDeQBXgSuv7Sv7KtseCpInTy%2FkXkdea8kgYymBZ5ZWikN7RIhXpwzXCM5Pf2BYFhAHdMw9hjotyhJY8HTPyqWu%2F6Ov7%2BIzlFsQSV7tq5jQc9hazOVMRxaE72YKaG3sZf8f2Q%3D%3D"));
                startActivity(intent);*/
            }
        });
        exam_listView = findViewById(R.id.lv);
        wrong_listView = findViewById(R.id.lv2);
        note = findViewById(R.id.note);
        selectionNum = 5;
        judgeNum = 5;

        //显示横幅广告
        //banner_TT();
        banner_GDT2();
        title = findViewById(R.id.title);
        clock = findViewById(R.id.countDown);
        clock.setTime(1, 30, 0);

        exam = new ArrayList();
        ansMap = new HashMap<>();
        source = new ArrayList();
        source.addAll(Myapp.selectionList);
        source.addAll(Myapp.judgeList);
        Tools.get2question(source, exam, selectionNum, judgeNum);
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == 0) get.setVisibility(View.VISIBLE);
            }
        };

        exam_list_adapter = new BaseAdapter() {

            @Override
            public int getCount() {
                return exam.size();
            }

            @Override
            public Object getItem(int position) {
                return null;
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(final int position, View convertView, ViewGroup parent) {
                View v;
                Object object = exam.get(position);
                String uAns = ansMap.get((position + 1) + "");
                if (position < selectionNum) {
                    if (exam_mode)
                        v = View.inflate(getApplicationContext(), R.layout.item_selection2, null);
                    else v = View.inflate(getApplicationContext(), R.layout.item_selection, null);
                    TextView ques = v.findViewById(R.id.tv_question);
                    TextView index = v.findViewById(R.id.tv_question_index);
                    ques.setText((position + 1) + "、" + ((SelectionBean) object).question);
                    index.setText("第" + (position + 1) + "题");
                    final TextView ans1;
                    final TextView ans2;
                    final TextView ans3;
                    final TextView ans4;
                    PercentRelativeLayout item4 = null;
                    RippleView rip4 = null;
                    ans1 = v.findViewById(R.id.tv_answer1);
                    ans2 = v.findViewById(R.id.tv_answer2);
                    ans3 = v.findViewById(R.id.tv_answer3);
                    ans1.setText(((SelectionBean) object).answer1);
                    ans2.setText(((SelectionBean) object).answer2);
                    ans3.setText(((SelectionBean) object).answer3);
                    final PercentRelativeLayout item1;
                    final PercentRelativeLayout item2;
                    final PercentRelativeLayout item3;
                    item1 = v.findViewById(R.id.rl_answer1);
                    item2 = v.findViewById(R.id.rl_answer2);
                    item3 = v.findViewById(R.id.rl_answer3);

                    RippleView rip1 = v.findViewById(R.id.rip1);
                    RippleView rip2 = v.findViewById(R.id.rip2);
                    RippleView rip3 = v.findViewById(R.id.rip3);
                    if (exam_mode) {
                        ans4 = v.findViewById(R.id.tv_answer4);
                        ans4.setText(((SelectionBean) object).answer4);
                        item4 = v.findViewById(R.id.rl_answer4);
                        rip4 = v.findViewById(R.id.rip4);
                    }
                    if (uAns == null) {
                        item1.setBackground(getResources().getDrawable(R.drawable.bg_answernormal));
                        item2.setBackground(getResources().getDrawable(R.drawable.bg_answernormal));
                        item3.setBackground(getResources().getDrawable(R.drawable.bg_answernormal));
                        if (exam_mode)
                            item4.setBackground(getResources().getDrawable(R.drawable.bg_answernormal));
                    } else {
                        switch (uAns) {
                            case "A":
                                item1.setBackground(getResources().getDrawable(R.drawable.bg_answerright));
                                item2.setBackground(getResources().getDrawable(R.drawable.bg_answernormal));
                                item3.setBackground(getResources().getDrawable(R.drawable.bg_answernormal));
                                if (exam_mode)
                                    item4.setBackground(getResources().getDrawable(R.drawable.bg_answernormal));
                                break;
                            case "B":
                                item2.setBackground(getResources().getDrawable(R.drawable.bg_answerright));
                                item1.setBackground(getResources().getDrawable(R.drawable.bg_answernormal));
                                item3.setBackground(getResources().getDrawable(R.drawable.bg_answernormal));
                                if (exam_mode)
                                    item4.setBackground(getResources().getDrawable(R.drawable.bg_answernormal));
                                break;
                            case "C":
                                item3.setBackground(getResources().getDrawable(R.drawable.bg_answerright));
                                item2.setBackground(getResources().getDrawable(R.drawable.bg_answernormal));
                                item1.setBackground(getResources().getDrawable(R.drawable.bg_answernormal));
                                if (exam_mode)
                                    item4.setBackground(getResources().getDrawable(R.drawable.bg_answernormal));
                                break;
                            case "D":
                                item4.setBackground(getResources().getDrawable(R.drawable.bg_answerright));
                                item3.setBackground(getResources().getDrawable(R.drawable.bg_answernormal));
                                item2.setBackground(getResources().getDrawable(R.drawable.bg_answernormal));
                                item1.setBackground(getResources().getDrawable(R.drawable.bg_answernormal));
                                break;
                            default:
                                break;
                        }
                    }
                    //item1.setOnClickListener(new View.OnClickListener() {
                    final PercentRelativeLayout finalItem = item4;
                    rip1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ansMap.put((position + 1) + "", "A");
                            item1.setBackground(getResources().getDrawable(R.drawable.bg_answerright));
                            item2.setBackground(getResources().getDrawable(R.drawable.bg_answernormal));
                            item3.setBackground(getResources().getDrawable(R.drawable.bg_answernormal));
                            if (exam_mode)
                                finalItem.setBackground(getResources().getDrawable(R.drawable.bg_answernormal));
                        }
                    });
                    // item2.setOnClickListener(new View.OnClickListener() {
                    rip2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ansMap.put((position + 1) + "", "B");
                            item2.setBackground(getResources().getDrawable(R.drawable.bg_answerright));
                            item1.setBackground(getResources().getDrawable(R.drawable.bg_answernormal));
                            item3.setBackground(getResources().getDrawable(R.drawable.bg_answernormal));
                            if (exam_mode)
                                finalItem.setBackground(getResources().getDrawable(R.drawable.bg_answernormal));
                        }
                    });
                    // item3.setOnClickListener(new View.OnClickListener() {
                    rip3.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ansMap.put((position + 1) + "", "C");
                            item3.setBackground(getResources().getDrawable(R.drawable.bg_answerright));
                            item2.setBackground(getResources().getDrawable(R.drawable.bg_answernormal));
                            item1.setBackground(getResources().getDrawable(R.drawable.bg_answernormal));
                            if (exam_mode)
                                finalItem.setBackground(getResources().getDrawable(R.drawable.bg_answernormal));
                        }
                    });
                    if (exam_mode) {
                        rip4.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ansMap.put((position + 1) + "", "D");
                                finalItem.setBackground(getResources().getDrawable(R.drawable.bg_answerright));
                                item1.setBackground(getResources().getDrawable(R.drawable.bg_answernormal));
                                item2.setBackground(getResources().getDrawable(R.drawable.bg_answernormal));
                                item3.setBackground(getResources().getDrawable(R.drawable.bg_answernormal));
                            }
                        });
                    }
                } else {
                    v = View.inflate(getApplicationContext(), R.layout.item_judge, null);
                    TextView ques = v.findViewById(R.id.tv_question);
                    TextView index = v.findViewById(R.id.tv_question_index);
                    final PercentRelativeLayout item4;
                    final PercentRelativeLayout item5;
                    item4 = v.findViewById(R.id.rl_answer1);
                    item5 = v.findViewById(R.id.rl_answer2);

                    RippleView rip4 = v.findViewById(R.id.rip1);
                    RippleView rip5 = v.findViewById(R.id.rip2);

                    ques.setText((position + 1) + "、" + ((JudgeBean) object).question);
                    index.setText("第" + (position + 1) + "题");
                    if (uAns != null) {
                        switch (uAns) {
                            case "✔":
                                item4.setBackground(getResources().getDrawable(R.drawable.bg_answerright));
                                item5.setBackground(getResources().getDrawable(R.drawable.bg_answernormal));
                                break;
                            case "✘":
                                item5.setBackground(getResources().getDrawable(R.drawable.bg_answerright));
                                item4.setBackground(getResources().getDrawable(R.drawable.bg_answernormal));
                                break;
                            default:
                                break;
                        }
                    } else {
                        item4.setBackground(getResources().getDrawable(R.drawable.bg_answernormal));
                        item5.setBackground(getResources().getDrawable(R.drawable.bg_answernormal));
                    }
                    //  item4.setOnClickListener(new View.OnClickListener() {
                    rip4.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ansMap.put((position + 1) + "", "✔");
                            item4.setBackground(getResources().getDrawable(R.drawable.bg_answerright));
                            item5.setBackground(getResources().getDrawable(R.drawable.bg_answernormal));
                        }
                    });
                    // item5.setOnClickListener(new View.OnClickListener() {
                    rip5.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ansMap.put((position + 1) + "", "✘");
                            item5.setBackground(getResources().getDrawable(R.drawable.bg_answerright));
                            item4.setBackground(getResources().getDrawable(R.drawable.bg_answernormal));
                        }
                    });
                }
                return v;
            }
        };
        final BaseAdapter ba = new BaseAdapter() {
            @Override
            public int getCount() {
                return wrongList.size();
            }

            @Override
            public Object getItem(int i) {
                return wrongList.get(i);
            }

            @Override
            public long getItemId(int i) {
                return i;
            }

            @Override
            public View getView(int i, View view, ViewGroup viewGroup) {
                View v;
                Object ob = wrongList.get(i);
                if (ob instanceof SelectionBean) {
                    if (exam_mode)
                        v = View.inflate(ExamActivity.this, R.layout.item_selection2, null);
                    else v = View.inflate(ExamActivity.this, R.layout.item_selection, null);
                    TextView index = v.findViewById(R.id.tv_question_index);
                    TextView ques = v.findViewById(R.id.tv_question);
                    TextView ans1 = v.findViewById(R.id.tv_answer1);
                    TextView ans2 = v.findViewById(R.id.tv_answer2);
                    TextView ans3 = v.findViewById(R.id.tv_answer3);
                    TextView ans4;
                    PercentRelativeLayout item4 = null;
                    PercentRelativeLayout item1 = v.findViewById(R.id.rl_answer1);
                    PercentRelativeLayout item2 = v.findViewById(R.id.rl_answer2);
                    PercentRelativeLayout item3 = v.findViewById(R.id.rl_answer3);
                    String uAns = ansMap.get(((SelectionBean) ob).No);
                    index.setText("第" + ((SelectionBean) ob).No + "题");
                    ques.setText(((SelectionBean) ob).No + "、" + ((SelectionBean) ob).question);
                    ans1.setText(((SelectionBean) ob).answer1);
                    ans2.setText(((SelectionBean) ob).answer2);
                    ans3.setText(((SelectionBean) ob).answer3);
                    if (exam_mode) {
                        ans4 = v.findViewById(R.id.tv_answer4);
                        item4 = v.findViewById(R.id.rl_answer4);
                        ans4.setText(((SelectionBean) ob).answer4);
                    }
                    switch (uAns) {
                        case "A":
                            item1.setBackground(getResources().getDrawable(R.drawable.bg_answerwrong));
                            break;
                        case "B":
                            item2.setBackground(getResources().getDrawable(R.drawable.bg_answerwrong));
                            break;
                        case "C":
                            item3.setBackground(getResources().getDrawable(R.drawable.bg_answerwrong));
                            break;
                        case "D":
                            item4.setBackground(getResources().getDrawable(R.drawable.bg_answerwrong));
                            break;
                        default:
                            break;
                    }
                    switch (((SelectionBean) ob).corAns) {
                        case "A":
                            item1.setBackground(getResources().getDrawable(R.drawable.bg_answerright));
                            break;
                        case "B":
                            item2.setBackground(getResources().getDrawable(R.drawable.bg_answerright));
                            break;
                        case "C":
                            item3.setBackground(getResources().getDrawable(R.drawable.bg_answerright));
                            break;
                        case "D":
                            item4.setBackground(getResources().getDrawable(R.drawable.bg_answerright));
                            break;
                        default:
                            break;
                    }
                } else {
                    v = View.inflate(ExamActivity.this, R.layout.item_judge, null);
                    TextView ques = v.findViewById(R.id.tv_question);
                    TextView index = v.findViewById(R.id.tv_question_index);
                    PercentRelativeLayout item1 = v.findViewById(R.id.rl_answer1);
                    PercentRelativeLayout item2 = v.findViewById(R.id.rl_answer2);
                    String uAns = ansMap.get(((JudgeBean) ob).No);
                    index.setText("第" + ((JudgeBean) ob).No + "题");
                    ques.setText(((JudgeBean) ob).No + "、" + ((JudgeBean) ob).question);
                    switch (uAns) {
                        case "✔":
                            item1.setBackground(getResources().getDrawable(R.drawable.bg_answerwrong));
                            break;
                        case "✘":
                            item2.setBackground(getResources().getDrawable(R.drawable.bg_answerwrong));
                            break;
                    }
                }
                return v;
            }
        };
        exam_listView.setAdapter(exam_list_adapter);
        startTime = System.currentTimeMillis();
        clock.start();
        get = findViewById(R.id.get);
        //检测答题开始后显示交卷按钮
        new Thread() {
            @Override
            public void run() {
                while (ansMap == null || ansMap.size() == 0 & flag) {
                }
                if (flag) handler.sendEmptyMessage(0);
            }
        }.start();
        get.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                clock.stop();
                long useTime = System.currentTimeMillis() - startTime;
                AlertDialog.Builder builder = new AlertDialog.Builder(ExamActivity.this);
                int score = 0;
                wrongList = new ArrayList();
                for (Object j : exam) {
                    if (j instanceof SelectionBean) {
                        if (ansMap.containsKey(((SelectionBean) j).No)) {
                            if (ansMap.get(((SelectionBean) j).No).equals(((SelectionBean) j).corAns))
                                score++;
                            else wrongList.add(j);
                        }
                    } else {
                        if (ansMap.containsKey(((JudgeBean) j).No)) {
                            if (ansMap.get(((JudgeBean) j).No).equals(((JudgeBean) j).answer))
                                score++;
                            else wrongList.add(j);
                        }
                    }
                }
                View v = View.inflate(app, R.layout.dialog_score, null);
                TextView right = v.findViewById(R.id.right_count);
                TextView ans_count = v.findViewById(R.id.ans_count);
                TextView time = v.findViewById(R.id.time);
                TextView tv_score = v.findViewById(R.id.score);
                TextView confirm = v.findViewById(R.id.tv_dialog_confirm);
                TextView cancel = v.findViewById(R.id.tv_dialog_cancel);
                ImageView rmark = v.findViewById(R.id.right);
                ImageView vs = v.findViewById(R.id.vs);
                ImageLoaderConfiguration config = ImageLoaderConfiguration.createDefault(ExamActivity.this);
                ImageLoader.getInstance().init(config);
                DisplayImageOptions opt = DisplayImageOptions.createSimple();
                ImageLoader.getInstance().displayImage("drawable://" + R.drawable.right, rmark, opt);
                ImageLoader.getInstance().displayImage("drawable://" + R.drawable.vs, vs, opt);
                right.setText(score + "");
                right.setTextColor(getResources().getColor(R.color.green));
                ans_count.setTextColor(getResources().getColor(R.color.colorAccent));
                ans_count.setText(ansMap.size() + "");
                if (score >= 80) {
                    tv_score.setText(score + "");
                    tv_score.setTextColor(getResources().getColor(R.color.green));
                } else {
                    tv_score.setTextColor(getResources().getColor(R.color.colorAccent));
                    tv_score.setText(score + "（不合格）");
                }
                time.setText(Tools.dif2time(useTime));
                confirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        Intent intent = new Intent(ExamActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                });
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        if (wrongList.size() > 0) {
                            exam_listView.setVisibility(View.GONE);
                            clock.setVisibility(View.GONE);
                            get.setVisibility(View.GONE);
                            title.setVisibility(View.VISIBLE);
                            wrong_listView.setAdapter(ba);
                            wrong_listView.setVisibility(View.VISIBLE);
                            check_wrong_state = true;
                        } else {
                            //Toast.makeText(ExamActivity.this, "全部正确哦,您是个天才!", Toast.LENGTH_LONG).show();
                            Toasty.success(ExamActivity.this, "全部正确哦,您是个天才!", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(ExamActivity.this, MainActivity.class));
                        }
                    }
                });
                builder.setView(v);
                dialog = builder.create();
                dialog.show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (click_success_state)
            clearAD();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            mBannerAd.recycle();
        } catch (Exception e) {
            e.printStackTrace();
        }
        exam_listView = null;
        wrong_listView = null;
        clock = null;
        get = null;
        exam = null;
        ansMap = null;
        wrongList = null;
        dialog = null;
        title = null;
        source = null;
        exam_list_adapter = null;
        handler = null;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        clock.stop();
        flag = false;
    }

    //穿山甲横幅
/*
    private void banner_TT() {
        mTTAdNative = app.ttAdManager.createAdNative(this);
        TT_banner.removeAllViews();
        float expressViewWidth = 350;
        float expressViewHeight = 350;
        try {
            expressViewWidth = getWindowManager().getDefaultDisplay().getWidth();
            expressViewHeight = 60;
        } catch (Exception e) {
            expressViewHeight = 0; //高度设置为0,则高度会自适应
        }
        //step4:创建广告请求参数AdSlot,具体参数含义参考文档
        AdSlot adSlot = new AdSlot.Builder()
                .setCodeId(TT_posId) //广告位id
                .setSupportDeepLink(true)
                .setAdCount(3) //请求广告数量为1到3条
                .setExpressViewAcceptedSize(expressViewWidth, expressViewHeight) //期望模板广告view的size,单位dp
                .setImageAcceptedSize(640, 320)//这个参数设置即可，不影响模板广告的size
                .build();
        //step5:请求广告，对请求回调的广告作渲染处理
        mTTAdNative.loadBannerExpressAd(adSlot, new TTAdNative.NativeExpressAdListener() {
            @Override
            public void onError(int code, String message) {
                Log.e("load error : ", code + ", " + message);
                TT_banner.removeAllViews();
                banner_GDT();
            }

            @Override
            public void onNativeExpressAdLoad(List<TTNativeExpressAd> ads) {
                if (ads == null || ads.size() == 0) {
                    return;
                }
                mTTAd = ads.get(0);
                mTTAd.setSlideIntervalTime(30 * 1000);
                bindAdListener(mTTAd);
                mTTAd.render();
            }
        });
    }

    private void bindAdListener(TTNativeExpressAd ad) {
        ad.setExpressInteractionListener(new TTNativeExpressAd.ExpressAdInteractionListener() {
            @Override
            public void onAdClicked(View view, int type) {
                click_success();
            }

            @Override
            public void onAdShow(View view, int type) {
*/
/*                tmall.setVisibility(View.GONE);
                mi_banner.setVisibility(View.GONE);
                GDT_banner.setVisibility(View.GONE);
                TT_banner.setVisibility(View.VISIBLE);
                note.setVisibility(View.VISIBLE);*//*

            }

            @Override
            public void onRenderFail(View view, String msg, int code) {
                Log.e("ExpressView", "render fail:");
                TT_banner.removeAllViews();
                banner_GDT();
            }

            @Override
            public void onRenderSuccess(View view, float width, float height) {
                //Log.e("ExpressView","render suc:");
                TT_banner.removeAllViews();
                TT_banner.addView(view);
                TT_banner.setVisibility(View.VISIBLE);
            }
        });
    }
*/

    //显示横幅广告
    void banner_Mi() {
        try {
            mBannerAd = AdWorkerFactory.getAdWorker(ExamActivity.this, mi_banner, new MimoAdListener() {
                @Override
                public void onAdPresent() {
                    //Log.e("展示", "onAdPresent");
                }

                @Override
                public void onAdClick() {
                    //Log.e("点击", "onAdClick");
                    click_success();
                }

                @Override
                public void onAdDismissed() {
                    banner_GDT2();
                }

                @Override
                public void onAdFailed(String s) {
                    //Log.e("失败", s);
                    show_ali();
                }

                @Override
                public void onAdLoaded(int size) {
                    note.setVisibility(View.VISIBLE);
                    mi_banner.setVisibility(View.VISIBLE);
                    //TT_banner.setVisibility(View.GONE);
                    tmall.setVisibility(View.GONE);
                    GDT_banner.setVisibility(View.GONE);
                }

                @Override
                public void onStimulateSuccess() {
                }
            }, AdType.AD_BANNER);
            mBannerAd.loadAndShow(Mi_posId);
        } catch (Exception e) {
            e.printStackTrace();
            show_ali();
        }
    }

    //显示GDT横幅广告
    void banner_GDT() {
        bv = new BannerView(this, ADSize.BANNER, GDT_APPID, GDT_posId);
        // 注意：如果开发者的banner不是始终展示在屏幕中的话，请关闭自动刷新，否则将导致曝光率过低。
        // 并且应该自行处理：当banner广告区域出现在屏幕后，再手动loadAD。
        bv.setRefresh(30);
        bv.setADListener(new AbstractBannerADListener() {

            @Override
            public void onADClicked() {
                super.onADClicked();
                click_success();
            }

            @Override
            public void onADClosed() {
                super.onADClosed();
                banner_Mi();
            }

            @Override
            public void onNoAD(AdError error) {
                Log.i(
                        "AD_DEMO",
                        String.format("Banner onNoAD，eCode = %d, eMsg = %s", error.getErrorCode(),
                                error.getErrorMsg()));
                banner_Mi();
            }

            @Override
            public void onADReceiv() {
                Log.i("AD_DEMO", "ONBannerReceive");
                if (!click_success_state) {
                    note.setVisibility(View.VISIBLE);
                    //TT_banner.setVisibility(View.GONE);
                    mi_banner.setVisibility(View.GONE);
                    tmall.setVisibility(View.GONE);
                    GDT_banner.setVisibility(View.VISIBLE);
                }
            }
        });
        GDT_banner.addView(bv);
        bv.loadAD();
    }

    public void banner_GDT2() {
        banner = new UnifiedBannerView(this, Myapp.GDT_APPID, GDT_posId, new UnifiedBannerADListener() {
            @Override
            public void onNoAD(AdError adError) {
                Log.i("AD_DEMO", "BannerNoAD，eCode=" + adError.getErrorCode());
                banner_Mi();
            }

            @Override
            public void onADReceive() {
                Log.i("AD_DEMO", "ONBannerReceive");
                if (!click_success_state) {
                    tmall.setVisibility(GONE);
                    mi_banner.setVisibility(GONE);
                    note.setVisibility(View.VISIBLE);
                    GDT_banner.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onADExposure() {

            }

            @Override
            public void onADClosed() {
                banner_Mi();
            }

            @Override
            public void onADClicked() {
                click_success();
            }

            @Override
            public void onADLeftApplication() {

            }

            @Override
            public void onADOpenOverlay() {

            }

            @Override
            public void onADCloseOverlay() {

            }
        });
        //设置广告轮播时间，为0或30~120之间的数字，单位为s,0标识不自动轮播
        banner.setRefresh(30);

        GDT_banner.addView(banner, getUnifiedBannerLayoutParams());
        /* 发起广告请求，收到广告数据后会展示数据     */
        banner.loadAD();
    }

    private FrameLayout.LayoutParams getUnifiedBannerLayoutParams() {
        int width = getWindowManager().getDefaultDisplay().getWidth();
        return new FrameLayout.LayoutParams(width, Math.round(width / 6.4F));
    }

    //备份阿里妈妈推广
    void show_ali() {
        //TT_banner.setVisibility(View.GONE);
        mi_banner.setVisibility(View.GONE);
        GDT_banner.setVisibility(View.GONE);
        note.setVisibility(View.VISIBLE);
        tmall.setVisibility(View.VISIBLE);
    }

    //点击广告成功后
    void click_success() {
        if (check_wrong_state)
            Toasty.warning(this, "浏览错题状态，无法激活考试功能，请先返回考试页面！", 3000).show();
        else {
            click_success_state = true;
            clearAD();
            selectionNum = 30;
            judgeNum = 70;
            //清除可能出现的全屏状态
            WindowManager.LayoutParams lp = getWindow().getAttributes();
            lp.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getWindow().setAttributes(lp);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

            exam.clear();
            ansMap.clear();
            Tools.get2question(source, exam, selectionNum, judgeNum);
            exam_listView.setAdapter(exam_list_adapter);
            //刷新时同时要刷新时间
            clock.setTime(1, 30, 0);
            clock.start();
        }
    }

    //清除所有广告栏
    void clearAD() {
        tmall.setVisibility(View.GONE);
        //TT_banner.setVisibility(View.GONE);
        mi_banner.setVisibility(View.GONE);
        GDT_banner.setVisibility(View.GONE);
        note.setVisibility(View.GONE);
    }
}
