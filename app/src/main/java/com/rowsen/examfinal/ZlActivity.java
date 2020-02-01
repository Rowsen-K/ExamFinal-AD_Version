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
import com.rowsen.SqliteTools.SQLFunction;
import com.rowsen.mytools.BaseActivity;
import com.rowsen.mytools.Tools;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;

import static com.rowsen.examfinal.Myapp.exam_table;

public class ZlActivity extends BaseActivity {
    DachshundTabLayout tab;
    ViewPager vp;
    ArrayList<MyFragment> list;
    EditText content;
    ArrayList<Bean> pos, all, selList, judList;
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
    boolean show_result = false;

    @Override
    public void onBackPressed() {
        if (show_result) {
            searchResult.setVisibility(View.GONE);
            show_result = false;
        } else
            super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        tab = null;
        vp = null;
        list = null;
        all = null;
        content = null;
        pos = null;
        lastText = null;
        one = null;
        two = null;
        searchResult = null;
        searchList = null;
        selList = null;
        judList = null;
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

        list = new ArrayList<>();
        selList = SQLFunction.queryType(this, exam_table, 1);
        judList = SQLFunction.queryType(this, exam_table, 2);
        one = new MyFragment("选择题", 0, selList);
        two = new MyFragment("判断题", 1, judList);
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
        for (Bean b : selList)
            b.flag = false;
        for (Bean b : judList)
            b.flag = false;
        if (one.ba != null) one.ba.notifyDataSetChanged();
        if (two.ba != null) two.ba.notifyDataSetChanged();
        String s = content.getText().toString().trim();
        if (!TextUtils.isEmpty(s)) {
            if (lastText == null || !s.equals(lastText)) {
                if (lastText == null) pos = new ArrayList<>();
                else pos.clear();
                all = SQLFunction.queryAll(this, exam_table);
                for (int n = 0; n < all.size(); n++) {
                    Bean o = all.get(n);
                    //o.flag = false;
                    if (o.question.contains(s))
                        pos.add(o);
                }
                lastText = s;
                if (pos.size() == 0) {
                    Toasty.warning(ZlActivity.this, "题库中没有找到相关的题目!", Toast.LENGTH_SHORT).show();
                    YoYo.with(Techniques.Shake)
                            .duration(300)
                            .repeat(2)
                            .playOn(content);
                } else if (pos.size() == 1) {
                    if (pos.get(0).type == 1) {
                        vp.setCurrentItem(0);
                        one.lv.smoothScrollToPosition(Integer.valueOf(pos.get(0).No) - 1);
                        selList.get(selList.indexOf(pos.get(0))).flag = true;
                    } else {
                        vp.setCurrentItem(1);
                        two.lv.smoothScrollToPosition(Integer.valueOf(pos.get(0).No) - selList.size() - 1);
                        judList.get(judList.indexOf(pos.get(0))).flag = true;
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
                        public int getViewTypeCount() {
                            return 2;
                        }

                        @Override
                        public int getItemViewType(int position) {
                            if (TextUtils.isEmpty(pos.get(position).img))
                                return 0;
                            else
                                return 1;
                        }

                        @Override
                        public View getView(int position, View convertView, ViewGroup parent) {
                            ViewHolder_selection selection = null;
                            Bean sb = pos.get(position);
                            switch (getItemViewType(position)) {
                                case 0:
                                    if (convertView == null) {
                                        convertView = View.inflate(ZlActivity.this, R.layout.selection_item_layout, null);
                                        selection = new ViewHolder_selection();
                                        selection.root = convertView.findViewById(R.id.root);
                                        selection.ques = convertView.findViewById(R.id.tv_question);
                                        selection.ans1 = convertView.findViewById(R.id.tv_answer1);
                                        convertView.setTag(selection);
                                    } else selection = (ViewHolder_selection) convertView.getTag();
                                    break;
                                case 1:
                                    if (convertView == null) {
                                        convertView = View.inflate(ZlActivity.this, R.layout.selection_img_item_layout, null);
                                        selection = new ViewHolder_selection_img();
                                        selection.root = convertView.findViewById(R.id.root);
                                        selection.ques = convertView.findViewById(R.id.tv_question);
                                        selection.ans1 = convertView.findViewById(R.id.tv_answer1);
                                        ((ViewHolder_selection_img) selection).img = convertView.findViewById(R.id.question_img);
                                        convertView.setTag(selection);
                                    } else
                                        selection = (ViewHolder_selection_img) convertView.getTag();
                                    ((ViewHolder_selection_img) selection).img.setImageBitmap(Tools.base64ToBitmap(sb.img.split("=")[1]));
                                    break;
                            }
                            if (sb.flag)
                                selection.root.setBackgroundColor(getResources().getColor(R.color.bg1));
                            else
                                selection.root.setBackgroundColor(getResources().getColor(R.color.zlyellow));
                            if (sb.type == 1) {
                                selection.ques.setText(sb.No + "、" + sb.question);
                                switch (sb.corAns) {
                                    case "A":
                                        selection.ans1.setText(sb.answer1);
                                        break;
                                    case "B":
                                        selection.ans1.setText(sb.answer2);
                                        break;
                                    case "C":
                                        selection.ans1.setText(sb.answer3);
                                        break;
                                    case "D":
                                        selection.ans1.setText(sb.answer4);
                                        break;
                                }
                            } else {
                                int n = Integer.valueOf(sb.No) - selList.size();
                                selection.ques.setText(n + "、" + sb.question);
                                selection.ans1.setText(sb.corAns);
                            }
                            return convertView;
                        }
                    };
                    searchList.setAdapter(a);
                    searchResult.setVisibility(View.VISIBLE);
                    show_result = true;
                }
            } else {
                if (pos.size() == 0) {
                    Toasty.warning(ZlActivity.this, "题库中没有找到相关的题目!", Toast.LENGTH_SHORT).show();
                    YoYo.with(Techniques.StandUp)
                            .duration(300)
                            .playOn(content);
                } else if (pos.size() == 1) {
                    if (pos.get(0).type == 1) {
                        vp.setCurrentItem(0);
                        one.lv.smoothScrollToPosition(Integer.valueOf(pos.get(0).No) - 1);
                        selList.get(selList.indexOf(pos.get(0))).flag = true;
                    } else {
                        vp.setCurrentItem(1);
                        two.lv.smoothScrollToPosition(Integer.valueOf(pos.get(0).No) - selList.size() - 1);
                        judList.get(judList.indexOf(pos.get(0))).flag = true;
                    }
                    one.ba.notifyDataSetChanged();
                    two.ba.notifyDataSetChanged();
                } else {
                    searchResult.setVisibility(View.VISIBLE);
                    searchList.smoothScrollToPosition(0);
                    show_result = true;
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

    public class ViewHolder_selection {
        ViewGroup root;
        TextView ques, ans1;
    }

    public class ViewHolder_selection_img extends ViewHolder_selection {
        ImageView img;
    }
}
