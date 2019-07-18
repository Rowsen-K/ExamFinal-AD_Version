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

public class JudgeActivity extends BaseActivity {
    ListView lv;
    ArrayList<JudgeBean> list;
    HashMap<Integer, String> change;
    TextView count;
    int right;
    int wrong;
    HashMap<Integer, Integer> mark;

    RippleView rip1;
    RippleView rip2;
    Myapp app = Myapp.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_judge);


        change = new HashMap<>();


        lv = findViewById(R.id.judge_lv);
        count = findViewById(R.id.count);
        right = 0;
        wrong = 0;
        list = new ArrayList<>();
        mark = new HashMap<>();
        ArrayList source = getIntent().getParcelableArrayListExtra("judgeList");
        Tools.get2question(source, list, 0, Myapp.judgeList.size());
        BaseAdapter bad = new BaseAdapter() {
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
                    //在Note3 5.0的系统调试时一旦进入考试模块再返回进入判断模块就挂,debug信息大致是oom异常,有点不理解,因为进选择模式基本内容一致却不挂
                    //挂b问题已解决,就是在context时使用了this引用导致activity资源无法释放.可以使用getApplicationContext()解决,这里使用的是自定义application
                    v = View.inflate(app, R.layout.item_judge, null);
                else v = convertView;
                TextView index = v.findViewById(R.id.tv_question_index);
                TextView ques = v.findViewById(R.id.tv_question);
                index.setText("第" + (position + 1) + "题");
                ques.setText(list.get(position).question);
                final PercentRelativeLayout item1 = v.findViewById(R.id.rl_answer1);
                final PercentRelativeLayout item2 = v.findViewById(R.id.rl_answer2);

                rip1 = v.findViewById(R.id.rip1);
                rip2 = v.findViewById(R.id.rip2);

                final ImageView img = v.findViewById(R.id.iv_result);
                if (change.containsKey(position)) {
                    if (list.get(position).answer.equals(change.get(position))) {
                        switch (change.get(position)) {
                            case "✔":
                                item1.setBackground(getResources().getDrawable(R.drawable.bg_answerright));
                                item2.setBackground(getResources().getDrawable(R.drawable.bg_answernormal));
                                break;
                            case "✘":
                                item1.setBackground(getResources().getDrawable(R.drawable.bg_answernormal));
                                item2.setBackground(getResources().getDrawable(R.drawable.bg_answerright));
                                break;
                        }
                    } else {
                        switch (change.get(position)) {
                            case "✔":
                                item1.setBackground(getResources().getDrawable(R.drawable.bg_answerwrong));
                                item2.setBackground(getResources().getDrawable(R.drawable.bg_answernormal));
                                break;
                            case "✘":
                                item1.setBackground(getResources().getDrawable(R.drawable.bg_answernormal));
                                item2.setBackground(getResources().getDrawable(R.drawable.bg_answerwrong));
                                break;
                        }
                    }
                    if (mark.get(position) == 0) {
                        img.setImageResource(R.drawable.icon_gameright);
                    } else img.setImageResource(R.drawable.icon_gamewrong);
                    img.setVisibility(View.VISIBLE);
                } else {
                    item1.setBackground(getResources().getDrawable(R.drawable.bg_answernormal));
                    item2.setBackground(getResources().getDrawable(R.drawable.bg_answernormal));
                    img.setVisibility(View.INVISIBLE);
                }
                // item1.setOnClickListener(new View.OnClickListener() {
                rip1.setOnClickListener(new View.OnClickListener() {
                    @Override//✘
                    public void onClick(View v) {
                        if ("✔".equals(list.get(position).answer)) {
                            item1.setBackground(getResources().getDrawable(R.drawable.bg_answerright));
                            if (!change.containsKey(position)) {
                                img.setImageResource(R.drawable.icon_gameright);
                                right++;
                                count.setText("正确：" + right + "    错误：" + wrong);
                                mark.put(position, 0);
                            }
                        } else {
                            item1.setBackground(getResources().getDrawable(R.drawable.bg_answerwrong));
                            if (!change.containsKey(position)) {
                                img.setImageResource(R.drawable.icon_gamewrong);
                                wrong++;
                                count.setText("正确：" + right + "    错误：" + wrong);
                                mark.put(position, 1);
                            }
                        }
                        item2.setBackground(getResources().getDrawable(R.drawable.bg_answernormal));
                        img.setVisibility(View.VISIBLE);
                        change.put(position, "✔");
                    }
                });
                // item2.setOnClickListener(new View.OnClickListener() {
                rip2.setOnClickListener(new View.OnClickListener() {
                    @Override//✘
                    public void onClick(View v) {
                        if ("✘".equals(list.get(position).answer)) {
                            item2.setBackground(getResources().getDrawable(R.drawable.bg_answerright));
                            if (!change.containsKey(position)) {
                                img.setImageResource(R.drawable.icon_gameright);
                                right++;
                                count.setText("正确：" + right + "    错误：" + wrong);
                                mark.put(position, 0);
                            }
                        } else {
                            item2.setBackground(getResources().getDrawable(R.drawable.bg_answerwrong));
                            if (!change.containsKey(position)) {
                                img.setImageResource(R.drawable.icon_gamewrong);
                                wrong++;
                                count.setText("正确：" + right + "    错误：" + wrong);
                                mark.put(position, 1);
                            }
                        }
                        item1.setBackground(getResources().getDrawable(R.drawable.bg_answernormal));
                        img.setVisibility(View.VISIBLE);
                        change.put(position, "✘");
                    }
                });
                return v;
            }
        };
        lv.setAdapter(bad);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        lv = null;
        count = null;
        list = null;
        change = null;
        mark = null;
        rip1 = null;
        rip2 = null;
        finish();
    }
}
