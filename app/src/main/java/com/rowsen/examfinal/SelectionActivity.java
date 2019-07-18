package com.rowsen.examfinal;

import android.os.Bundle;
import android.support.percent.PercentRelativeLayout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.andexert.library.RippleView;
import com.rowsen.mytools.BaseActivity;
import com.rowsen.mytools.Tools;

import java.util.ArrayList;
import java.util.HashMap;

public class SelectionActivity extends BaseActivity {
    ListView sl_lv;
    ArrayList<SelectionBean> list;
    HashMap<Integer, String> change;
    int rightCount;
    int wrongCount;
    TextView record;
    HashMap<Integer, Integer> mark;

    RippleView rip1;
    RippleView rip2;
    RippleView rip3;
    Myapp app = Myapp.getInstance();

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        sl_lv = null;
        list = null;
        change = null;
        record = null;
        mark = null;
        rip1 = null;
        rip2 = null;
        rip3 = null;
        app = null;
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //  requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        rightCount = 0;
        wrongCount = 0;
        setContentView(R.layout.activity_selection);


        sl_lv = findViewById(R.id.selction_lv);
        record = findViewById(R.id.count);
        list = new ArrayList<>();
        change = new HashMap();
        mark = new HashMap<>();
        // File xml = new File(getFilesDir().getPath(), "Exam.xml");
        // Tools.xml2list(xml, list, 1);
        ArrayList source = getIntent().getParcelableArrayListExtra("selectionList");

        Tools.get2question(source, list, Myapp.selectionList.size(), 0);
        //System.out.println("=============后"+Myapp.selectionList);
        // temp = null;
        BaseAdapter adp = new BaseAdapter() {
            @Override
            public int getCount() {
                return list.size();
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
                if (convertView == null)
                    v = View.inflate(app, R.layout.item_selection, null);
                else
                    v = convertView;
                TextView index = v.findViewById(R.id.tv_question_index);
                TextView ques = v.findViewById(R.id.tv_question);
                index.setText("第" + (position + 1) + "题");
                ques.setText(list.get(position).question);
                final TextView ans1;
                final TextView ans2;
                final TextView ans3;
                ans1 = v.findViewById(R.id.tv_answer1);
                ans2 = v.findViewById(R.id.tv_answer2);
                ans3 = v.findViewById(R.id.tv_answer3);
                ans1.setText(list.get(position).answer1);
                ans2.setText(list.get(position).answer2);
                ans3.setText(list.get(position).answer3);
                final PercentRelativeLayout item1 = v.findViewById(R.id.rl_answer1);
                final PercentRelativeLayout item2 = v.findViewById(R.id.rl_answer2);
                final PercentRelativeLayout item3 = v.findViewById(R.id.rl_answer3);
                rip1 = v.findViewById(R.id.rip1);
                rip2 = v.findViewById(R.id.rip2);
                rip3 = v.findViewById(R.id.rip3);
                final ImageView img = v.findViewById(R.id.iv_result);
                final String value = change.get(position);
                if (value == null) {
                    item1.setBackground(getResources().getDrawable(R.drawable.bg_answernormal));
                    item2.setBackground(getResources().getDrawable(R.drawable.bg_answernormal));
                    item3.setBackground(getResources().getDrawable(R.drawable.bg_answernormal));
                    img.setVisibility(View.INVISIBLE);
                } else {
                    switch (value) {
                        case "A":
                            if ("A".equals(list.get(position).corAns)) {
                                item1.setBackground(getResources().getDrawable(R.drawable.bg_answerright));
                            } else {
                                img.setImageResource(R.drawable.icon_gamewrong);
                                img.setVisibility(View.VISIBLE);
                                item1.setBackground(getResources().getDrawable(R.drawable.bg_answerwrong));
                            }
                            item2.setBackground(getResources().getDrawable(R.drawable.bg_answernormal));
                            item3.setBackground(getResources().getDrawable(R.drawable.bg_answernormal));
                            break;
                        case "B":
                            if ("B".equals(list.get(position).corAns)) {
                                img.setImageResource(R.drawable.icon_gameright);
                                img.setVisibility(View.VISIBLE);
                                item2.setBackground(getResources().getDrawable(R.drawable.bg_answerright));
                            } else {
                                img.setImageResource(R.drawable.icon_gamewrong);
                                img.setVisibility(View.VISIBLE);
                                item2.setBackground(getResources().getDrawable(R.drawable.bg_answerwrong));
                            }
                            item1.setBackground(getResources().getDrawable(R.drawable.bg_answernormal));
                            item3.setBackground(getResources().getDrawable(R.drawable.bg_answernormal));
                            break;
                        case "C":
                            if ("C".equals(list.get(position).corAns)) {
                                img.setImageResource(R.drawable.icon_gameright);
                                img.setVisibility(View.VISIBLE);
                                item3.setBackground(getResources().getDrawable(R.drawable.bg_answerright));
                            } else {
                                img.setImageResource(R.drawable.icon_gamewrong);
                                img.setVisibility(View.VISIBLE);
                                item3.setBackground(getResources().getDrawable(R.drawable.bg_answerwrong));
                            }
                            item2.setBackground(getResources().getDrawable(R.drawable.bg_answernormal));
                            item1.setBackground(getResources().getDrawable(R.drawable.bg_answernormal));
                            break;
                        default:
                            break;
                    }
                    if (mark.get(position) == 0) img.setImageResource(R.drawable.icon_gameright);
                    else img.setImageResource(R.drawable.icon_gamewrong);
                    img.setVisibility(View.VISIBLE);
                }
                //item1.setOnClickListener(new View.OnClickListener() {
                rip1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if ("A".equals(list.get(position).corAns)) {
                            item1.setBackground(getResources().getDrawable(R.drawable.bg_answerright));
                            if (!change.containsKey(position)) {
                                img.setImageResource(R.drawable.icon_gameright);
                                rightCount++;
                                record.setText("正确：" + rightCount + "    错误：" + wrongCount);
                                mark.put(position, 0);
                            }
                        } else {
                            item1.setBackground(getResources().getDrawable(R.drawable.bg_answerwrong));
                            // img.setVisibility(View.VISIBLE);
                            if (!change.containsKey(position)) {
                                img.setImageResource(R.drawable.icon_gamewrong);
                                wrongCount++;
                                record.setText("正确：" + rightCount + "    错误：" + wrongCount);
                                mark.put(position, 1);
                            }
                        }
                        item2.setBackground(getResources().getDrawable(R.drawable.bg_answernormal));
                        item3.setBackground(getResources().getDrawable(R.drawable.bg_answernormal));
                        img.setVisibility(View.VISIBLE);
                        change.put(position, "A");
                    }
                });
                // item2.setOnClickListener(new View.OnClickListener() {
                rip2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if ("B".equals(list.get(position).corAns)) {
                            item2.setBackground(getResources().getDrawable(R.drawable.bg_answerright));
                            //   img.setVisibility(View.VISIBLE);
                            if (!change.containsKey(position)) {
                                img.setImageResource(R.drawable.icon_gameright);
                                rightCount++;
                                record.setText("正确：" + rightCount + "    错误：" + wrongCount);
                                mark.put(position, 0);
                            }
                        } else {
                            item2.setBackground(getResources().getDrawable(R.drawable.bg_answerwrong));
                            //      img.setVisibility(View.VISIBLE);
                            if (!change.containsKey(position)) {
                                img.setImageResource(R.drawable.icon_gamewrong);
                                wrongCount++;
                                record.setText("正确：" + rightCount + "    错误：" + wrongCount);
                                mark.put(position, 1);
                            }
                        }
                        item1.setBackground(getResources().getDrawable(R.drawable.bg_answernormal));
                        item3.setBackground(getResources().getDrawable(R.drawable.bg_answernormal));
                        change.put(position, "B");
                        img.setVisibility(View.VISIBLE);
                    }
                });
                //  ans3.setOnClickListener(new View.OnClickListener() {
                rip3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if ("C".equals(list.get(position).corAns)) {
                            item3.setBackground(getResources().getDrawable(R.drawable.bg_answerright));
                            //    img.setVisibility(View.VISIBLE);
                            if (!change.containsKey(position)) {
                                img.setImageResource(R.drawable.icon_gameright);
                                rightCount++;
                                record.setText("正确：" + rightCount + "    错误：" + wrongCount);
                                mark.put(position, 0);
                            }
                        } else {
                            item3.setBackground(getResources().getDrawable(R.drawable.bg_answerwrong));
                            //   img.setVisibility(View.VISIBLE);
                            if (!change.containsKey(position)) {
                                img.setImageResource(R.drawable.icon_gamewrong);
                                wrongCount++;
                                record.setText("正确：" + rightCount + "    错误：" + wrongCount);
                                mark.put(position, 1);
                            }
                        }
                        item2.setBackground(getResources().getDrawable(R.drawable.bg_answernormal));
                        item1.setBackground(getResources().getDrawable(R.drawable.bg_answernormal));
                        change.put(position, "C");
                        img.setVisibility(View.VISIBLE);
                    }
                });
                //System.out.println(change);
                return v;
            }
        };
        sl_lv.setAdapter(adp);
    }
}
