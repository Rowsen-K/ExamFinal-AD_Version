package com.rowsen.examfinal;

import android.os.Bundle;
import android.support.percent.PercentRelativeLayout;
import android.text.TextUtils;
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

public class PracticeActivity extends BaseActivity {
    ListView sl_lv;
    ArrayList<Bean> list;
    HashMap<Integer, String> change;
    int rightCount;
    int wrongCount;
    TextView record;
    HashMap<Integer, Integer> mark;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sl_lv = null;
        list = null;
        change = null;
        record = null;
        mark = null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        rightCount = 0;
        wrongCount = 0;
        setContentView(R.layout.activity_practice);


        sl_lv = findViewById(R.id.selction_lv);
        record = findViewById(R.id.count);
        list = new ArrayList<>();
        change = new HashMap();
        mark = new HashMap<>();
        record.setText(getIntent().getStringExtra("title"));
        Tools.get2question(Myapp.list, list, Myapp.list.size(), 0);
        BaseAdapter adp = new BaseAdapter() {
            @Override
            public int getCount() {
                return list.size();
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
            public int getItemViewType(int position) {
                Bean b = list.get(position);
                if (b.type == 2)
                    return 1;//VeiwHolder_judge
                else {
                    if (TextUtils.isEmpty(b.answer4)) {
                        if (TextUtils.isEmpty(b.img)) {
                            if (!b.answer1.contains("src=iVB"))
                                return 2;//VeiwHolder_select3
                            else
                                return 3;//ViewHolder_select_3img
                        } else
                            return 4;//ViewHolder_select3_img
                    } else {
                        if (TextUtils.isEmpty(b.img))
                            return 5;//ViewHolder_select4;
                        else
                            return 6;//ViewHolder_select4_img
                    }
                }
            }

            @Override
            public int getViewTypeCount() {
                return 6;
            }

            @Override
            public View getView(final int position, View convertView, ViewGroup parent) {
                final ViewHolder_judge judgeView;
                final ViewHolder_selection3 selectView;
                final Bean bean = list.get(position);
                final String value = change.get(position);
                switch (getItemViewType(position)) {
                    case 1:
                        if (convertView == null) {
                            convertView = View.inflate(PracticeActivity.this, R.layout.item_judge, null);
                            judgeView = new ViewHolder_judge();
                            judgeView.index = convertView.findViewById(R.id.tv_question_index);
                            judgeView.ques = convertView.findViewById(R.id.tv_question);

                            judgeView.item1 = convertView.findViewById(R.id.rl_answer1);
                            judgeView.item2 = convertView.findViewById(R.id.rl_answer2);

                            judgeView.rip1 = convertView.findViewById(R.id.rip1);
                            judgeView.rip2 = convertView.findViewById(R.id.rip2);

                            judgeView.img = convertView.findViewById(R.id.iv_result);
                            convertView.setTag(judgeView);
                        } else
                            judgeView = (ViewHolder_judge) convertView.getTag();
                        judgeView.index.setText("第" + (position + 1) + "题");
                        judgeView.ques.setText(bean.question);
                        if (change.containsKey(position)) {
                            if (bean.corAns.equals(change.get(position))) {
                                switch (change.get(position)) {
                                    case "✔":
                                        judgeView.item1.setBackground(getResources().getDrawable(R.drawable.bg_answerright));
                                        judgeView.item2.setBackground(getResources().getDrawable(R.drawable.bg_answernormal));
                                        break;
                                    case "✘":
                                        judgeView.item1.setBackground(getResources().getDrawable(R.drawable.bg_answernormal));
                                        judgeView.item2.setBackground(getResources().getDrawable(R.drawable.bg_answerright));
                                        break;
                                }
                            } else {
                                switch (change.get(position)) {
                                    case "✔":
                                        judgeView.item1.setBackground(getResources().getDrawable(R.drawable.bg_answerwrong));
                                        judgeView.item2.setBackground(getResources().getDrawable(R.drawable.bg_answernormal));
                                        break;
                                    case "✘":
                                        judgeView.item1.setBackground(getResources().getDrawable(R.drawable.bg_answernormal));
                                        judgeView.item2.setBackground(getResources().getDrawable(R.drawable.bg_answerwrong));
                                        break;
                                }
                            }
                            if (mark.get(position) == 0) {
                                judgeView.img.setImageResource(R.drawable.icon_gameright);
                            } else judgeView.img.setImageResource(R.drawable.icon_gamewrong);
                            judgeView.img.setVisibility(View.VISIBLE);
                        }
                        else {
                            judgeView.item1.setBackground(getResources().getDrawable(R.drawable.bg_answernormal));
                            judgeView.item2.setBackground(getResources().getDrawable(R.drawable.bg_answernormal));
                            judgeView.img.setVisibility(View.INVISIBLE);
                        }
                        judgeView.rip1.setOnClickListener(new View.OnClickListener() {
                            @Override//✘
                            public void onClick(View v) {
                                if ("✔".equals(bean.corAns)) {
                                    judgeView.item1.setBackground(getResources().getDrawable(R.drawable.bg_answerright));
                                    if (!change.containsKey(position)) {
                                        judgeView.img.setImageResource(R.drawable.icon_gameright);
                                        rightCount++;
                                        record.setText("正确：" + rightCount + "    错误：" + wrongCount);
                                        mark.put(position, 0);
                                    }
                                } else {
                                    judgeView.item1.setBackground(getResources().getDrawable(R.drawable.bg_answerwrong));
                                    if (!change.containsKey(position)) {
                                        judgeView.img.setImageResource(R.drawable.icon_gamewrong);
                                        wrongCount++;
                                        record.setText("正确：" + rightCount + "    错误：" + wrongCount);
                                        mark.put(position, 1);
                                    }
                                }
                                judgeView.item2.setBackground(getResources().getDrawable(R.drawable.bg_answernormal));
                                judgeView.img.setVisibility(View.VISIBLE);
                                change.put(position, "✔");
                            }
                        });
                        judgeView.rip2.setOnClickListener(new View.OnClickListener() {
                            @Override//✘
                            public void onClick(View v) {
                                if ("✘".equals(bean.corAns)) {
                                    judgeView.item2.setBackground(getResources().getDrawable(R.drawable.bg_answerright));
                                    if (!change.containsKey(position)) {
                                        judgeView.img.setImageResource(R.drawable.icon_gameright);
                                        rightCount++;
                                        record.setText("正确：" + rightCount + "    错误：" + wrongCount);
                                        mark.put(position, 0);
                                    }
                                } else {
                                    judgeView.item2.setBackground(getResources().getDrawable(R.drawable.bg_answerwrong));
                                    if (!change.containsKey(position)) {
                                        judgeView.img.setImageResource(R.drawable.icon_gamewrong);
                                        wrongCount++;
                                        record.setText("正确：" + rightCount + "    错误：" + wrongCount);
                                        mark.put(position, 1);
                                    }
                                }
                                judgeView.item1.setBackground(getResources().getDrawable(R.drawable.bg_answernormal));
                                judgeView.img.setVisibility(View.VISIBLE);
                                change.put(position, "✘");
                            }
                        });
                        break;
                    case 2:
                        if (convertView == null) {
                            convertView = View.inflate(PracticeActivity.this, R.layout.item_selection, null);
                            selectView = new ViewHolder_selection3();
                            findView(2, selectView, convertView);
                            convertView.setTag(selectView);
                        } else
                            selectView = (ViewHolder_selection3) convertView.getTag();
                        ini_selection(2, selectView, position, bean, value);
                        break;
                    case 3:
                        if (convertView == null) {
                            convertView = View.inflate(PracticeActivity.this, R.layout.item_selection_3img, null);
                            selectView = new ViewHolder_selection_3img();
                            findView(3, selectView, convertView);
                            convertView.setTag(selectView);
                        } else
                            selectView = (ViewHolder_selection3) convertView.getTag();
                        ini_selection(3, selectView, position, bean, value);
                        ((ViewHolder_selection_3img) selectView).select1.setImageBitmap(Tools.base64ToBitmap(bean.answer1.split("、src=")[1]));
                        ((ViewHolder_selection_3img) selectView).select2.setImageBitmap(Tools.base64ToBitmap(bean.answer2.split("、src=")[1]));
                        ((ViewHolder_selection_3img) selectView).select3.setImageBitmap(Tools.base64ToBitmap(bean.answer3.split("、src=")[1]));
                        break;
                    case 4:
                        if (convertView == null) {
                            convertView = View.inflate(PracticeActivity.this, R.layout.item_selection_img, null);
                            selectView = new ViewHolder_selection3_img();
                            findView(4, selectView, convertView);
                            convertView.setTag(selectView);
                        } else
                            selectView = (ViewHolder_selection3) convertView.getTag();
                        ini_selection(4, selectView, position, bean, value);
                        if (!TextUtils.isEmpty(bean.img) && ((ViewHolder_selection3_img) selectView).ques_img != null) {
                            ((ViewHolder_selection3_img) selectView).ques_img.setImageBitmap(Tools.base64ToBitmap(bean.img.split("=")[1]));
                        }
                        break;
                    case 5:
                        if (convertView == null) {
                            convertView = View.inflate(PracticeActivity.this, R.layout.item_selection2, null);
                            selectView = new ViewHolder_selection4();
                            findView(5, selectView, convertView);
                            convertView.setTag(selectView);
                        } else
                            selectView = (ViewHolder_selection3) convertView.getTag();
                        ini_selection(5, selectView, position, bean, value);
                        break;
                    case 6:
                        if (convertView == null) {
                            convertView = View.inflate(PracticeActivity.this, R.layout.item_selection2_img, null);
                            selectView = new ViewHolder_selection4_img();
                            findView(6, selectView, convertView);
                            convertView.setTag(selectView);
                        } else
                            selectView = (ViewHolder_selection3) convertView.getTag();
                        ini_selection(6, selectView, position, bean, value);
                        break;
                }
                return convertView;
            }

            /*@Override
            public View getView(final int position, View convertView, ViewGroup parent) {
                final Bean bean = list.get(position);
                if (bean.type == 1) {
                    ImageView img = null;
                    ImageView ques_img = null;
                    final TextView ans1;
                    final TextView ans2;
                    final TextView ans3;
                    final TextView ans4;
                    PercentRelativeLayout item1, item2, item3, item4 = null;
                    if (!TextUtils.isEmpty(bean.answer4)) {
                        if (TextUtils.isEmpty(bean.img))
                            convertView = View.inflate(PracticeActivity.this, R.layout.item_selection2, null);
                        else {
                            convertView = View.inflate(PracticeActivity.this, R.layout.item_selection2_img, null);
                            ques_img = convertView.findViewById(R.id.qustion_img);
                        }
                        ans4 = convertView.findViewById(R.id.tv_answer4);
                        ans4.setText(bean.answer4);
                        img = convertView.findViewById(R.id.iv_result);
                        item1 = convertView.findViewById(R.id.rl_answer1);
                        item2 = convertView.findViewById(R.id.rl_answer2);
                        item3 = convertView.findViewById(R.id.rl_answer3);
                        item4 = convertView.findViewById(R.id.rl_answer4);
                        rip4 = convertView.findViewById(R.id.rip4);
                        final PercentRelativeLayout finalItem1 = item4;
                        final PercentRelativeLayout finalItem2 = item3;
                        final PercentRelativeLayout finalItem3 = item2;
                        final PercentRelativeLayout finalItem4 = item1;
                        final ImageView img1 = img;
                        rip4.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if ("D".equals(bean.corAns)) {
                                    finalItem1.setBackground(getResources().getDrawable(R.drawable.bg_answerright));
                                    //    img.setVisibility(View.VISIBLE);
                                    if (!change.containsKey(position)) {
                                        img1.setImageResource(R.drawable.icon_gameright);
                                        rightCount++;
                                        record.setText("正确：" + rightCount + "    错误：" + wrongCount);
                                        mark.put(position, 0);
                                    }
                                } else {
                                    finalItem1.setBackground(getResources().getDrawable(R.drawable.bg_answerwrong));
                                    //   img.setVisibility(View.VISIBLE);
                                    if (!change.containsKey(position)) {
                                        img1.setImageResource(R.drawable.icon_gamewrong);
                                        wrongCount++;
                                        record.setText("正确：" + rightCount + "    错误：" + wrongCount);
                                        mark.put(position, 1);
                                    }
                                }
                                finalItem2.setBackground(getResources().getDrawable(R.drawable.bg_answernormal));
                                finalItem3.setBackground(getResources().getDrawable(R.drawable.bg_answernormal));
                                finalItem4.setBackground(getResources().getDrawable(R.drawable.bg_answernormal));
                                change.put(position, "D");
                                img1.setVisibility(View.VISIBLE);
                            }
                        });
                    } else {
                        if (TextUtils.isEmpty(bean.img))
                            convertView = View.inflate(PracticeActivity.this, R.layout.item_selection, null);
                        else {
                            convertView = View.inflate(PracticeActivity.this, R.layout.item_selection_img, null);
                            ques_img = convertView.findViewById(R.id.qustion_img);
                        }
                    }
                    if (img == null)
                        img = convertView.findViewById(R.id.iv_result);
                    TextView index = convertView.findViewById(R.id.tv_question_index);
                    TextView ques = convertView.findViewById(R.id.tv_question);
                    index.setText("第" + (position + 1) + "题");
                    ques.setText(bean.question);
                    if (!TextUtils.isEmpty(bean.img) && ques_img != null) {
                        Log.e("----------", index.getText().toString());
                        ques_img.setImageBitmap(Tools.base64ToBitmap(bean.img.split("=")[1]));
                    }
                    ans1 = convertView.findViewById(R.id.tv_answer1);
                    ans2 = convertView.findViewById(R.id.tv_answer2);
                    ans3 = convertView.findViewById(R.id.tv_answer3);
                    ans1.setText(bean.answer1);
                    ans2.setText(bean.answer2);
                    ans3.setText(bean.answer3);

                    item1 = convertView.findViewById(R.id.rl_answer1);
                    item2 = convertView.findViewById(R.id.rl_answer2);
                    item3 = convertView.findViewById(R.id.rl_answer3);
                    rip1 = convertView.findViewById(R.id.rip1);
                    rip2 = convertView.findViewById(R.id.rip2);
                    rip3 = convertView.findViewById(R.id.rip3);

                    final String value = change.get(position);
                    if (value == null) {
                        item1.setBackground(getResources().getDrawable(R.drawable.bg_answernormal));
                        item2.setBackground(getResources().getDrawable(R.drawable.bg_answernormal));
                        item3.setBackground(getResources().getDrawable(R.drawable.bg_answernormal));
                        if (!TextUtils.isEmpty(bean.answer4))
                            item4.setBackground(getResources().getDrawable(R.drawable.bg_answernormal));
                        img.setVisibility(View.INVISIBLE);
                    } else {
                        switch (value) {
                            case "A":
                                if ("A".equals(bean.corAns)) {
                                    item1.setBackground(getResources().getDrawable(R.drawable.bg_answerright));
                                } else {
                                    img.setImageResource(R.drawable.icon_gamewrong);
                                    img.setVisibility(View.VISIBLE);
                                    item1.setBackground(getResources().getDrawable(R.drawable.bg_answerwrong));
                                }
                                if (!TextUtils.isEmpty(bean.answer4))
                                    item4.setBackground(getResources().getDrawable(R.drawable.bg_answernormal));
                                item2.setBackground(getResources().getDrawable(R.drawable.bg_answernormal));
                                item3.setBackground(getResources().getDrawable(R.drawable.bg_answernormal));
                                break;
                            case "B":
                                if ("B".equals(bean.corAns)) {
                                    img.setImageResource(R.drawable.icon_gameright);
                                    img.setVisibility(View.VISIBLE);
                                    item2.setBackground(getResources().getDrawable(R.drawable.bg_answerright));
                                } else {
                                    img.setImageResource(R.drawable.icon_gamewrong);
                                    img.setVisibility(View.VISIBLE);
                                    item2.setBackground(getResources().getDrawable(R.drawable.bg_answerwrong));
                                }
                                if (!TextUtils.isEmpty(bean.answer4))
                                    item4.setBackground(getResources().getDrawable(R.drawable.bg_answernormal));
                                item1.setBackground(getResources().getDrawable(R.drawable.bg_answernormal));
                                item3.setBackground(getResources().getDrawable(R.drawable.bg_answernormal));
                                break;
                            case "C":
                                if ("C".equals(bean.corAns)) {
                                    img.setImageResource(R.drawable.icon_gameright);
                                    img.setVisibility(View.VISIBLE);
                                    item3.setBackground(getResources().getDrawable(R.drawable.bg_answerright));
                                } else {
                                    img.setImageResource(R.drawable.icon_gamewrong);
                                    img.setVisibility(View.VISIBLE);
                                    item3.setBackground(getResources().getDrawable(R.drawable.bg_answerwrong));
                                }
                                if (!TextUtils.isEmpty(bean.answer4))
                                    item4.setBackground(getResources().getDrawable(R.drawable.bg_answernormal));
                                item2.setBackground(getResources().getDrawable(R.drawable.bg_answernormal));
                                item1.setBackground(getResources().getDrawable(R.drawable.bg_answernormal));
                                break;
                            case "D":
                                if ("D".equals(bean.corAns)) {
                                    img.setImageResource(R.drawable.icon_gameright);
                                    img.setVisibility(View.VISIBLE);
                                    item4.setBackground(getResources().getDrawable(R.drawable.bg_answerright));
                                } else {
                                    img.setImageResource(R.drawable.icon_gamewrong);
                                    img.setVisibility(View.VISIBLE);
                                    item4.setBackground(getResources().getDrawable(R.drawable.bg_answerwrong));
                                }
                                item3.setBackground(getResources().getDrawable(R.drawable.bg_answernormal));
                                item2.setBackground(getResources().getDrawable(R.drawable.bg_answernormal));
                                item1.setBackground(getResources().getDrawable(R.drawable.bg_answernormal));
                                break;
                            default:
                                break;
                        }
                        if (mark.get(position) == 0)
                            img.setImageResource(R.drawable.icon_gameright);
                        else img.setImageResource(R.drawable.icon_gamewrong);
                        img.setVisibility(View.VISIBLE);
                    }

                    final PercentRelativeLayout finalItem = item1;
                    final PercentRelativeLayout finalItem5 = item2;
                    final PercentRelativeLayout finalItem6 = item3;
                    final ImageView img2 = img;
                    final PercentRelativeLayout finalItem7 = item4;
                    rip1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if ("A".equals(bean.corAns)) {
                                finalItem.setBackground(getResources().getDrawable(R.drawable.bg_answerright));
                                if (!change.containsKey(position)) {
                                    img2.setImageResource(R.drawable.icon_gameright);
                                    rightCount++;
                                    record.setText("正确：" + rightCount + "    错误：" + wrongCount);
                                    mark.put(position, 0);
                                }
                            } else {
                                finalItem.setBackground(getResources().getDrawable(R.drawable.bg_answerwrong));
                                // img.setVisibility(View.VISIBLE);
                                if (!change.containsKey(position)) {
                                    img2.setImageResource(R.drawable.icon_gamewrong);
                                    wrongCount++;
                                    record.setText("正确：" + rightCount + "    错误：" + wrongCount);
                                    mark.put(position, 1);
                                }
                            }
                            if (!TextUtils.isEmpty(bean.answer4))
                                finalItem7.setBackground(getResources().getDrawable(R.drawable.bg_answernormal));
                            finalItem5.setBackground(getResources().getDrawable(R.drawable.bg_answernormal));
                            finalItem6.setBackground(getResources().getDrawable(R.drawable.bg_answernormal));
                            img2.setVisibility(View.VISIBLE);
                            change.put(position, "A");
                        }
                    });

                    rip2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if ("B".equals(bean.corAns)) {
                                finalItem5.setBackground(getResources().getDrawable(R.drawable.bg_answerright));
                                //   img.setVisibility(View.VISIBLE);
                                if (!change.containsKey(position)) {
                                    img2.setImageResource(R.drawable.icon_gameright);
                                    rightCount++;
                                    record.setText("正确：" + rightCount + "    错误：" + wrongCount);
                                    mark.put(position, 0);
                                }
                            } else {
                                finalItem5.setBackground(getResources().getDrawable(R.drawable.bg_answerwrong));
                                //      img.setVisibility(View.VISIBLE);
                                if (!change.containsKey(position)) {
                                    img2.setImageResource(R.drawable.icon_gamewrong);
                                    wrongCount++;
                                    record.setText("正确：" + rightCount + "    错误：" + wrongCount);
                                    mark.put(position, 1);
                                }
                            }
                            if (!TextUtils.isEmpty(bean.answer4))
                                finalItem7.setBackground(getResources().getDrawable(R.drawable.bg_answernormal));
                            finalItem.setBackground(getResources().getDrawable(R.drawable.bg_answernormal));
                            finalItem6.setBackground(getResources().getDrawable(R.drawable.bg_answernormal));
                            change.put(position, "B");
                            img2.setVisibility(View.VISIBLE);
                        }
                    });

                    rip3.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if ("C".equals(bean.corAns)) {
                                finalItem6.setBackground(getResources().getDrawable(R.drawable.bg_answerright));
                                //    img.setVisibility(View.VISIBLE);
                                if (!change.containsKey(position)) {
                                    img2.setImageResource(R.drawable.icon_gameright);
                                    rightCount++;
                                    record.setText("正确：" + rightCount + "    错误：" + wrongCount);
                                    mark.put(position, 0);
                                }
                            } else {
                                finalItem6.setBackground(getResources().getDrawable(R.drawable.bg_answerwrong));
                                //   img.setVisibility(View.VISIBLE);
                                if (!change.containsKey(position)) {
                                    img2.setImageResource(R.drawable.icon_gamewrong);
                                    wrongCount++;
                                    record.setText("正确：" + rightCount + "    错误：" + wrongCount);
                                    mark.put(position, 1);
                                }
                            }
                            if (!TextUtils.isEmpty(bean.answer4))
                                finalItem7.setBackground(getResources().getDrawable(R.drawable.bg_answernormal));
                            finalItem5.setBackground(getResources().getDrawable(R.drawable.bg_answernormal));
                            finalItem.setBackground(getResources().getDrawable(R.drawable.bg_answernormal));
                            change.put(position, "C");
                            img2.setVisibility(View.VISIBLE);
                        }
                    });
                } else {
                    convertView = View.inflate(PracticeActivity.this, R.layout.item_judge, null);
                    TextView index = convertView.findViewById(R.id.tv_question_index);
                    TextView ques = convertView.findViewById(R.id.tv_question);
                    index.setText("第" + (position + 1) + "题");
                    ques.setText(bean.question);
                    final PercentRelativeLayout item1 = convertView.findViewById(R.id.rl_answer1);
                    final PercentRelativeLayout item2 = convertView.findViewById(R.id.rl_answer2);

                    rip1 = convertView.findViewById(R.id.rip1);
                    rip2 = convertView.findViewById(R.id.rip2);

                    final ImageView img = convertView.findViewById(R.id.iv_result);
                    if (change.containsKey(position)) {
                        if (bean.corAns.equals(change.get(position))) {
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
                    rip1.setOnClickListener(new View.OnClickListener() {
                        @Override//✘
                        public void onClick(View v) {
                            if ("✔".equals(bean.corAns)) {
                                item1.setBackground(getResources().getDrawable(R.drawable.bg_answerright));
                                if (!change.containsKey(position)) {
                                    img.setImageResource(R.drawable.icon_gameright);
                                    rightCount++;
                                    record.setText("正确：" + rightCount + "    错误：" + wrongCount);
                                    mark.put(position, 0);
                                }
                            } else {
                                item1.setBackground(getResources().getDrawable(R.drawable.bg_answerwrong));
                                if (!change.containsKey(position)) {
                                    img.setImageResource(R.drawable.icon_gamewrong);
                                    wrongCount++;
                                    record.setText("正确：" + rightCount + "    错误：" + wrongCount);
                                    mark.put(position, 1);
                                }
                            }
                            item2.setBackground(getResources().getDrawable(R.drawable.bg_answernormal));
                            img.setVisibility(View.VISIBLE);
                            change.put(position, "✔");
                        }
                    });
                    rip2.setOnClickListener(new View.OnClickListener() {
                        @Override//✘
                        public void onClick(View v) {
                            if ("✘".equals(bean.corAns)) {
                                item2.setBackground(getResources().getDrawable(R.drawable.bg_answerright));
                                if (!change.containsKey(position)) {
                                    img.setImageResource(R.drawable.icon_gameright);
                                    rightCount++;
                                    record.setText("正确：" + rightCount + "    错误：" + wrongCount);
                                    mark.put(position, 0);
                                }
                            } else {
                                item2.setBackground(getResources().getDrawable(R.drawable.bg_answerwrong));
                                if (!change.containsKey(position)) {
                                    img.setImageResource(R.drawable.icon_gamewrong);
                                    wrongCount++;
                                    record.setText("正确：" + rightCount + "    错误：" + wrongCount);
                                    mark.put(position, 1);
                                }
                            }
                            item1.setBackground(getResources().getDrawable(R.drawable.bg_answernormal));
                            img.setVisibility(View.VISIBLE);
                            change.put(position, "✘");
                        }
                    });
                }
                return convertView;
            }*/
        };
        sl_lv.setAdapter(adp);
    }

    public class ViewHolder_selection3 {
        TextView index, ques;
        ImageView img;
        TextView ans1, ans2, ans3;
        PercentRelativeLayout item1, item2, item3;
        RippleView rip1, rip2, rip3;
    }

    public class ViewHolder_selection4 extends ViewHolder_selection3 {
        TextView ans4;
        PercentRelativeLayout item4;
        RippleView rip4;
    }

    public class ViewHolder_selection3_img extends ViewHolder_selection3 {
        ImageView ques_img;
    }

    public class ViewHolder_selection4_img extends ViewHolder_selection3 {
        ImageView ques_img;
        TextView ans4;
        PercentRelativeLayout item4;
        RippleView rip4;
    }

    public class ViewHolder_selection_3img extends ViewHolder_selection3 {
        ImageView select1, select2, select3;
    }

    public class ViewHolder_judge {
        TextView index, ques;
        PercentRelativeLayout item1, item2;
        RippleView rip1, rip2;
        ImageView img;
    }

    public void findView(int type, ViewHolder_selection3 selectView, View convertView) {
        selectView.img = convertView.findViewById(R.id.iv_result);
        selectView.index = convertView.findViewById(R.id.tv_question_index);
        selectView.ques = convertView.findViewById(R.id.tv_question);
        selectView.item1 = convertView.findViewById(R.id.rl_answer1);
        selectView.item2 = convertView.findViewById(R.id.rl_answer2);
        selectView.item3 = convertView.findViewById(R.id.rl_answer3);
        selectView.ans1 = convertView.findViewById(R.id.tv_answer1);
        selectView.ans2 = convertView.findViewById(R.id.tv_answer2);
        selectView.ans3 = convertView.findViewById(R.id.tv_answer3);
        selectView.rip1 = convertView.findViewById(R.id.rip1);
        selectView.rip2 = convertView.findViewById(R.id.rip2);
        selectView.rip3 = convertView.findViewById(R.id.rip3);
        switch (type) {
            case 3:
                ((ViewHolder_selection_3img) selectView).select1 = convertView.findViewById(R.id.select1);
                ((ViewHolder_selection_3img) selectView).select2 = convertView.findViewById(R.id.select2);
                ((ViewHolder_selection_3img) selectView).select3 = convertView.findViewById(R.id.select3);
                break;
            case 4:
                ((ViewHolder_selection3_img) selectView).ques_img = convertView.findViewById(R.id.qustion_img);
                break;
            case 5:
                ((ViewHolder_selection4) selectView).ans4 = convertView.findViewById(R.id.tv_answer4);
                ((ViewHolder_selection4) selectView).item4 = convertView.findViewById(R.id.rl_answer4);
                ((ViewHolder_selection4) selectView).rip4 = convertView.findViewById(R.id.rip4);
                break;
            case 6:
                ((ViewHolder_selection4_img) selectView).ques_img = convertView.findViewById(R.id.qustion_img);
                ((ViewHolder_selection4_img) selectView).ans4 = convertView.findViewById(R.id.tv_answer4);
                ((ViewHolder_selection4_img) selectView).item4 = convertView.findViewById(R.id.rl_answer4);
                ((ViewHolder_selection4_img) selectView).rip4 = convertView.findViewById(R.id.rip4);
                break;
        }
    }

    public void ini_selection(final int type, final ViewHolder_selection3 selectView, final int position, final Bean bean, String value) {
        selectView.index.setText("第" + (position + 1) + "题");
        selectView.ques.setText(bean.question);
        if (bean.answer1.contains("src=iVB"))
            selectView.ans1.setText("A");
        else
            selectView.ans1.setText(bean.answer1);
        if (bean.answer2.contains("src=iVB"))
            selectView.ans2.setText("B");
        else
            selectView.ans2.setText(bean.answer2);
        if (bean.answer3.contains("src=iVB"))
            selectView.ans3.setText("C");
        else
            selectView.ans3.setText(bean.answer3);
        switch (type) {
            case 5:
                ((ViewHolder_selection4) selectView).ans4.setText(bean.answer4);
                ((ViewHolder_selection4) selectView).rip4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if ("D".equals(bean.corAns)) {
                            ((ViewHolder_selection4) selectView).item4.setBackground(getResources().getDrawable(R.drawable.bg_answerright));
                            if (!change.containsKey(position)) {
                                selectView.img.setImageResource(R.drawable.icon_gameright);
                                rightCount++;
                                record.setText("正确：" + rightCount + "    错误：" + wrongCount);
                                mark.put(position, 0);
                            }
                        } else {
                            ((ViewHolder_selection4) selectView).item4.setBackground(getResources().getDrawable(R.drawable.bg_answerwrong));
                            if (!change.containsKey(position)) {
                                selectView.img.setImageResource(R.drawable.icon_gamewrong);
                                wrongCount++;
                                record.setText("正确：" + rightCount + "    错误：" + wrongCount);
                                mark.put(position, 1);
                            }
                        }
                        selectView.item1.setBackground(getResources().getDrawable(R.drawable.bg_answernormal));
                        selectView.item2.setBackground(getResources().getDrawable(R.drawable.bg_answernormal));
                        selectView.item3.setBackground(getResources().getDrawable(R.drawable.bg_answernormal));
                        change.put(position, "D");
                        selectView.img.setVisibility(View.VISIBLE);
                    }
                });
                break;
            case 6:
                if (!TextUtils.isEmpty(bean.img) && ((ViewHolder_selection4_img) selectView).ques_img != null) {
                    ((ViewHolder_selection4_img) selectView).ques_img.setImageBitmap(Tools.base64ToBitmap(bean.img.split("=")[1]));
                }
                ((ViewHolder_selection4_img) selectView).ans4.setText(bean.answer4);
                ((ViewHolder_selection4_img) selectView).rip4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if ("D".equals(bean.corAns)) {
                            ((ViewHolder_selection4_img) selectView).item4.setBackground(getResources().getDrawable(R.drawable.bg_answerright));
                            if (!change.containsKey(position)) {
                                selectView.img.setImageResource(R.drawable.icon_gameright);
                                rightCount++;
                                record.setText("正确：" + rightCount + "    错误：" + wrongCount);
                                mark.put(position, 0);
                            }
                        } else {
                            ((ViewHolder_selection4_img) selectView).item4.setBackground(getResources().getDrawable(R.drawable.bg_answerwrong));
                            if (!change.containsKey(position)) {
                                selectView.img.setImageResource(R.drawable.icon_gamewrong);
                                wrongCount++;
                                record.setText("正确：" + rightCount + "    错误：" + wrongCount);
                                mark.put(position, 1);
                            }
                        }
                        selectView.item1.setBackground(getResources().getDrawable(R.drawable.bg_answernormal));
                        selectView.item2.setBackground(getResources().getDrawable(R.drawable.bg_answernormal));
                        selectView.item3.setBackground(getResources().getDrawable(R.drawable.bg_answernormal));
                        change.put(position, "D");
                        selectView.img.setVisibility(View.VISIBLE);
                    }
                });
                break;
        }

        if (value == null) {
            selectView.item1.setBackground(getResources().getDrawable(R.drawable.bg_answernormal));
            selectView.item2.setBackground(getResources().getDrawable(R.drawable.bg_answernormal));
            selectView.item3.setBackground(getResources().getDrawable(R.drawable.bg_answernormal));
            switch (type) {
                case 5:
                    ((ViewHolder_selection4) selectView).item4.setBackground(getResources().getDrawable(R.drawable.bg_answernormal));
                    break;
                case 6:
                    ((ViewHolder_selection4_img) selectView).item4.setBackground(getResources().getDrawable(R.drawable.bg_answernormal));
                    break;
            }
            selectView.img.setVisibility(View.INVISIBLE);
        } else {
            switch (value) {
                case "A":
                    if ("A".equals(bean.corAns)) {
                        selectView.item1.setBackground(getResources().getDrawable(R.drawable.bg_answerright));
                    } else {
                        //img.setImageResource(R.drawable.icon_gamewrong);
                        //img.setVisibility(View.VISIBLE);
                        selectView.item1.setBackground(getResources().getDrawable(R.drawable.bg_answerwrong));
                    }
                    selectView.item2.setBackground(getResources().getDrawable(R.drawable.bg_answernormal));
                    selectView.item3.setBackground(getResources().getDrawable(R.drawable.bg_answernormal));
                    switch (type) {
                        case 5:
                            ((ViewHolder_selection4) selectView).item4.setBackground(getResources().getDrawable(R.drawable.bg_answernormal));
                            break;
                        case 6:
                            ((ViewHolder_selection4_img) selectView).item4.setBackground(getResources().getDrawable(R.drawable.bg_answernormal));
                            break;
                    }
                    break;
                case "B":
                    if ("B".equals(bean.corAns)) {
                        //img.setImageResource(R.drawable.icon_gameright);
                        //img.setVisibility(View.VISIBLE);
                        selectView.item2.setBackground(getResources().getDrawable(R.drawable.bg_answerright));
                    } else {
                        //img.setImageResource(R.drawable.icon_gamewrong);
                        //img.setVisibility(View.VISIBLE);
                        selectView.item2.setBackground(getResources().getDrawable(R.drawable.bg_answerwrong));
                    }
                    selectView.item1.setBackground(getResources().getDrawable(R.drawable.bg_answernormal));
                    selectView.item3.setBackground(getResources().getDrawable(R.drawable.bg_answernormal));
                    switch (type) {
                        case 5:
                            ((ViewHolder_selection4) selectView).item4.setBackground(getResources().getDrawable(R.drawable.bg_answernormal));
                            break;
                        case 6:
                            ((ViewHolder_selection4_img) selectView).item4.setBackground(getResources().getDrawable(R.drawable.bg_answernormal));
                    }
                    break;
                case "C":
                    if ("C".equals(bean.corAns)) {
                        //img.setImageResource(R.drawable.icon_gameright);
                        //img.setVisibility(View.VISIBLE);
                        selectView.item3.setBackground(getResources().getDrawable(R.drawable.bg_answerright));
                    } else {
                        //img.setImageResource(R.drawable.icon_gamewrong);
                        //img.setVisibility(View.VISIBLE);
                        selectView.item3.setBackground(getResources().getDrawable(R.drawable.bg_answerwrong));
                    }
                    selectView.item2.setBackground(getResources().getDrawable(R.drawable.bg_answernormal));
                    selectView.item1.setBackground(getResources().getDrawable(R.drawable.bg_answernormal));
                    switch (type) {
                        case 5:
                            ((ViewHolder_selection4) selectView).item4.setBackground(getResources().getDrawable(R.drawable.bg_answernormal));
                            break;
                        case 6:
                            ((ViewHolder_selection4_img) selectView).item4.setBackground(getResources().getDrawable(R.drawable.bg_answernormal));
                    }
                    break;
                case "D":
                    if ("D".equals(bean.corAns)) {
                        //img.setImageResource(R.drawable.icon_gameright);
                        //img.setVisibility(View.VISIBLE);
                        switch (type) {
                            case 5:
                                ((ViewHolder_selection4) selectView).item4.setBackground(getResources().getDrawable(R.drawable.bg_answerright));
                                break;
                            case 6:
                                ((ViewHolder_selection4_img) selectView).item4.setBackground(getResources().getDrawable(R.drawable.bg_answerright));
                                break;
                        }
                    } else {
                        //img.setImageResource(R.drawable.icon_gamewrong);
                        //img.setVisibility(View.VISIBLE);
                        switch (type) {
                            case 5:
                                ((ViewHolder_selection4) selectView).item4.setBackground(getResources().getDrawable(R.drawable.bg_answerwrong));
                                break;
                            case 6:
                                ((ViewHolder_selection4_img) selectView).item4.setBackground(getResources().getDrawable(R.drawable.bg_answerwrong));
                                break;
                        }
                    }
                    selectView.item3.setBackground(getResources().getDrawable(R.drawable.bg_answernormal));
                    selectView.item2.setBackground(getResources().getDrawable(R.drawable.bg_answernormal));
                    selectView.item1.setBackground(getResources().getDrawable(R.drawable.bg_answernormal));
                    break;
            }
            if (mark.get(position) == 0)
                selectView.img.setImageResource(R.drawable.icon_gameright);
            else selectView.img.setImageResource(R.drawable.icon_gamewrong);
            selectView.img.setVisibility(View.VISIBLE);
        }
        selectView.rip1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("A".equals(bean.corAns)) {
                    selectView.item1.setBackground(getResources().getDrawable(R.drawable.bg_answerright));
                    if (!change.containsKey(position)) {
                        selectView.img.setImageResource(R.drawable.icon_gameright);
                        rightCount++;
                        record.setText("正确：" + rightCount + "    错误：" + wrongCount);
                        mark.put(position, 0);
                    }
                } else {
                    selectView.item1.setBackground(getResources().getDrawable(R.drawable.bg_answerwrong));
                    if (!change.containsKey(position)) {
                        selectView.img.setImageResource(R.drawable.icon_gamewrong);
                        wrongCount++;
                        record.setText("正确：" + rightCount + "    错误：" + wrongCount);
                        mark.put(position, 1);
                    }
                }
                selectView.item2.setBackground(getResources().getDrawable(R.drawable.bg_answernormal));
                selectView.item3.setBackground(getResources().getDrawable(R.drawable.bg_answernormal));
                switch (type) {
                    case 5:
                        ((ViewHolder_selection4) selectView).item4.setBackground(getResources().getDrawable(R.drawable.bg_answernormal));
                        break;
                    case 6:
                        ((ViewHolder_selection4_img) selectView).item4.setBackground(getResources().getDrawable(R.drawable.bg_answernormal));
                }
                selectView.img.setVisibility(View.VISIBLE);
                change.put(position, "A");
            }
        });

        selectView.rip2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("B".equals(bean.corAns)) {
                    selectView.item2.setBackground(getResources().getDrawable(R.drawable.bg_answerright));
                    if (!change.containsKey(position)) {
                        selectView.img.setImageResource(R.drawable.icon_gameright);
                        rightCount++;
                        record.setText("正确：" + rightCount + "    错误：" + wrongCount);
                        mark.put(position, 0);
                    }
                } else {
                    selectView.item2.setBackground(getResources().getDrawable(R.drawable.bg_answerwrong));
                    if (!change.containsKey(position)) {
                        selectView.img.setImageResource(R.drawable.icon_gamewrong);
                        wrongCount++;
                        record.setText("正确：" + rightCount + "    错误：" + wrongCount);
                        mark.put(position, 1);
                    }
                }
                selectView.item1.setBackground(getResources().getDrawable(R.drawable.bg_answernormal));
                selectView.item3.setBackground(getResources().getDrawable(R.drawable.bg_answernormal));
                switch (type) {
                    case 5:
                        ((ViewHolder_selection4) selectView).item4.setBackground(getResources().getDrawable(R.drawable.bg_answernormal));
                        break;
                    case 6:
                        ((ViewHolder_selection4_img) selectView).item4.setBackground(getResources().getDrawable(R.drawable.bg_answernormal));
                }
                change.put(position, "B");
                selectView.img.setVisibility(View.VISIBLE);
            }
        });

        selectView.rip3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("C".equals(bean.corAns)) {
                    selectView.item3.setBackground(getResources().getDrawable(R.drawable.bg_answerright));
                    if (!change.containsKey(position)) {
                        selectView.img.setImageResource(R.drawable.icon_gameright);
                        rightCount++;
                        record.setText("正确：" + rightCount + "    错误：" + wrongCount);
                        mark.put(position, 0);
                    }
                } else {
                    selectView.item3.setBackground(getResources().getDrawable(R.drawable.bg_answerwrong));
                    if (!change.containsKey(position)) {
                        selectView.img.setImageResource(R.drawable.icon_gamewrong);
                        wrongCount++;
                        record.setText("正确：" + rightCount + "    错误：" + wrongCount);
                        mark.put(position, 1);
                    }
                }
                selectView.item1.setBackground(getResources().getDrawable(R.drawable.bg_answernormal));
                selectView.item2.setBackground(getResources().getDrawable(R.drawable.bg_answernormal));
                switch (type) {
                    case 5:
                        ((ViewHolder_selection4) selectView).item4.setBackground(getResources().getDrawable(R.drawable.bg_answernormal));
                        break;
                    case 6:
                        ((ViewHolder_selection4_img) selectView).item4.setBackground(getResources().getDrawable(R.drawable.bg_answernormal));
                }
                change.put(position, "C");
                selectView.img.setVisibility(View.VISIBLE);
            }
        });
    }
}
