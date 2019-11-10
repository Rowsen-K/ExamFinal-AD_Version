package com.rowsen.examfinal;

import android.annotation.SuppressLint;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.kekstudio.dachshundtablayout.DachshundTabLayout;
import com.kekstudio.dachshundtablayout.indicators.DachshundIndicator;
import com.rowsen.mytools.BaseActivity;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;

public class ZlActivity extends BaseActivity {
    DachshundTabLayout tab;
    ViewPager vp;
    ArrayList<MyFragment> list;
    EditText content;
    ArrayList<String> pos;
    String lastText;
    MyFragment one;
    MyFragment two;
    LinearLayout searchResult;
    ListView searchList;
    ImageView iv;
    AnimatedVectorDrawable searchToBar;
    AnimatedVectorDrawable barToSearch;
    Interpolator interp;
    float offset;
    boolean expanded = true;

    Myapp app = Myapp.getInstance();

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        tab = null;
        vp = null;
        list = null;
        // search = null;
        //  all = null;
        content = null;
        pos = null;
        lastText = null;
        one = null;
        two = null;
        searchResult = null;
        searchList = null;
        // selList = null;
        // judList = null;
        app = null;
    }

    @SuppressLint({"ResourceAsColor", "NewApi"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_zl);

        tab = findViewById(R.id.tab);
        vp = findViewById(R.id.id_viewpager);
        // search =  findViewById(R.id.search);
        content = findViewById(R.id.content);
        searchResult = findViewById(R.id.search_result);
        searchList = findViewById(R.id.search_lv);

        // touch_outside = findViewById(R.id.touch_outsidee);
        searchToBar = (AnimatedVectorDrawable) getResources().getDrawable(R.drawable.anim_search_to_bar);
        barToSearch = (AnimatedVectorDrawable) getResources().getDrawable(R.drawable.anim_bar_to_search);
        interp = AnimationUtils.loadInterpolator(this, android.R.interpolator.linear_out_slow_in);
        // iv is sized to hold the search+bar so when only showing the search icon, translate the
        // whole view left by half the difference to keep it centered
        iv = findViewById(R.id.search);
        offset = -146f * (int) getResources().getDisplayMetrics().scaledDensity;
        // offset = getWindowManager().getDefaultDisplay().getWidth()/2;
        // System.out.println("=========="+offset);
        iv.setTranslationX(offset);

        //  all = getIntent().getParcelableArrayListExtra("all");
        //   selList = getIntent().getParcelableArrayListExtra("selectionList");
        //   judList = getIntent().getParcelableArrayListExtra("judgeList");
        list = new ArrayList<>();
        one = new MyFragment("选择题", 0, Myapp.selectionList);
        two = new MyFragment("判断题", 1, Myapp.judgeList);
        list.add(one);
        list.add(two);
        vp.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(), list));
        tab.setAnimatedIndicator(new DachshundIndicator(tab));
        tab.setSelectedTabIndicatorColor(R.color.dark_blue);
        tab.setupWithViewPager(vp);
        animate(iv);
        // search.setOnClickListener(new View.OnClickListener() {
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  ex.explode(v);
                animate(v);
            }
        });
    }

    void search() {
        String s = content.getText().toString().trim();
        if (TextUtils.isEmpty(s)) {
            //Toast.makeText(ZlActivity.this, "请先输入要搜索的题目内容,再按搜索键!", Toast.LENGTH_SHORT).show();
          /*Toasty.error(app, "请先输入要搜索的题目内容,再按搜索键!", Toast.LENGTH_SHORT).show();
           YoYo.with(Techniques.Shake)
                    .duration(200)
                    .repeat(2)
                    .playOn(content);
                    */
        } else {
            if (lastText == null || !s.equals(lastText)) {
                if (lastText == null) pos = new ArrayList<>();
                else pos.clear();
                for (int n = 0; n < Myapp.allList.size(); n++) {
                    Object o = Myapp.allList.get(n);
                    if (o instanceof SelectionBean) {
                        Myapp.selectionList.get(Integer.valueOf(((SelectionBean) o).No) - 1).flag = false;
                        if (((SelectionBean) o).question.contains(s))
                            pos.add("0&" + ((SelectionBean) o).No + "&" + n);
                    } else {
                        Myapp.judgeList.get(Integer.valueOf(((JudgeBean) o).No) - 1).flag = false;
                        if (((JudgeBean) o).question.contains(s))
                            pos.add("1&" + ((JudgeBean) o).No + "&" + n);
                    }
                }
                lastText = s;
                if (pos.size() == 0) {
                    //Toast.makeText(ZlActivity.this, "题库中没有找到相关的题目!", Toast.LENGTH_SHORT).show();
                    Toasty.warning(app, "题库中没有找到相关的题目!", Toast.LENGTH_SHORT).show();
                    YoYo.with(Techniques.Shake)
                            .duration(300)
                            .repeat(2)
                            .playOn(content);
                } else if (pos.size() == 1) {
                    final String[] temp = pos.get(0).split("&");
                    if (temp[0].equals("0")) {
                        vp.setCurrentItem(0);
                        one.lv.smoothScrollToPosition(Integer.valueOf(temp[1]) - 1);
                        Myapp.selectionList.get(Integer.valueOf(temp[1]) - 1).flag = true;
                    } else {
                        vp.setCurrentItem(1);
                        two.lv.smoothScrollToPosition(Integer.valueOf(temp[1]) - 1);
                        Myapp.judgeList.get(Integer.valueOf(temp[1]) - 1).flag = true;
                    }
                    one.ba.notifyDataSetChanged();
                    two.ba.notifyDataSetChanged();
                } else {
                    BaseAdapter a = new BaseAdapter() {
                        @Override
                        public int getCount() {
                            return pos.size();
                        }

                        @Override
                        public Object getItem(int position) {
                            return position;
                        }

                        @Override
                        public long getItemId(int position) {
                            return position;
                        }

                        @Override
                        public View getView(int position, View convertView, ViewGroup parent) {
                            View v;
                            if (convertView == null)
                                v = View.inflate(app, R.layout.selection_item_layout, null);
                            else v = convertView;
                            TextView qus = v.findViewById(R.id.tv_question);
                            TextView ans = v.findViewById(R.id.tv_answer1);
                            Object o = Myapp.allList.get(Integer.valueOf(pos.get(position).split("&")[2]));
                            if (o instanceof SelectionBean) {
                                qus.setText(((SelectionBean) o).No + "、" + ((SelectionBean) o).question);
                                switch (((SelectionBean) o).corAns) {
                                    case "A":
                                        ans.setText(((SelectionBean) o).answer1);
                                        break;
                                    case "B":
                                        ans.setText(((SelectionBean) o).answer2);
                                        break;
                                    case "C":
                                        ans.setText(((SelectionBean) o).answer3);
                                        break;
                                }
                            } else {
                                qus.setText(((JudgeBean) o).No + "、" + ((JudgeBean) o).question);
                                ans.setText(((JudgeBean) o).answer);
                            }
                            return v;
                        }
                    };
                    searchList.setAdapter(a);
                    searchResult.setVisibility(View.VISIBLE);
                }
            }
            //还有一种情况就是当前的输入和上一次是一样的,多搜索结果的逻辑现在是回不到搜索界面的,所以只要处理2种情况1,没有匹配结果和一个结果
            else {
                if (pos.size() == 0) {
                    //Toast.makeText(ZlActivity.this, "题库中没有找到相关的题目!", Toast.LENGTH_SHORT).show();
                    Toasty.warning(app, "题库中没有找到相关的题目!", Toast.LENGTH_SHORT).show();
                    YoYo.with(Techniques.StandUp)
                            .duration(300)
                            .playOn(content);
                } else if (pos.size() == 1) {
                    final String[] temp = pos.get(0).split("&");
                    if (temp[0].equals("0")) {
                        vp.setCurrentItem(0);
                        one.lv.smoothScrollToPosition(Integer.valueOf(temp[1]) - 1);
                        Myapp.selectionList.get(Integer.valueOf(temp[1]) - 1).flag = true;
                    } else {
                        vp.setCurrentItem(1);
                        two.lv.smoothScrollToPosition(Integer.valueOf(temp[1]) - 1);
                        Myapp.judgeList.get(Integer.valueOf(temp[1]) - 1).flag = true;
                    }
                    one.ba.notifyDataSetChanged();
                    two.ba.notifyDataSetChanged();
                }
            }
        }
    }

    @SuppressLint("NewApi")
    public void animate(View view) {
        if (!expanded) {
            iv.setImageDrawable(searchToBar);
            searchToBar.start();
            iv.animate().translationX(0f).setDuration(500).setInterpolator(interp);
            iv.setImageResource(R.drawable.search_icon);
            content.setVisibility(View.VISIBLE);
            content.animate().alpha(1f).setStartDelay(500 - 100).setDuration(100).setInterpolator(interp);
        } else {
            iv.setImageResource(R.drawable.search_bar);
            iv.setImageDrawable(barToSearch);
            barToSearch.start();
            iv.animate().translationX(offset).setDuration(500).setInterpolator(interp);
            content.setAlpha(0f);
            content.setVisibility(View.GONE);
        }
        if (expanded) search();
        expanded = !expanded;
    }
}
