package com.rowsen.examfinal;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class MyFragment extends Fragment {
    public String title;
    ListView lv;
    View v;
    int mode;
    BaseAdapter ba;
    ArrayList list;

    public MyFragment() {
        super();
    }

    @SuppressLint("ValidFragment")
    public MyFragment(String title, int mode, ArrayList list) {
        this();
        this.title = title;
        this.mode = mode;
        this.list = list;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment, container, false);
        lv = v.findViewById(R.id.frg_lv);
        switch (mode) {
            case 0:
                ba = new BaseAdapter() {
                    @Override
                    public void notifyDataSetChanged() {
                        super.notifyDataSetChanged();
                    }

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
                    public View getView(int position, View convertView, ViewGroup parent) {
                        View v;
                        if (convertView == null) {
                            v = View.inflate(getContext(), R.layout.selection_item_layout, null);
                        } else v = convertView;
                        SelectionBean sb = ((SelectionBean) list.get(position));
                        TextView ques;
                        TextView ans1;
                        ques = v.findViewById(R.id.tv_question);
                        ans1 = v.findViewById(R.id.tv_answer1);
                        if (sb.flag) v.setBackgroundColor(getResources().getColor(R.color.bg1));
                        else v.setBackgroundColor(getResources().getColor(R.color.zlyellow));
                        ques.setText(sb.No + "、" + sb.question);
                        switch (sb.corAns) {
                            case "A":
                                ans1.setText(sb.answer1);
                                break;
                            case "B":
                                ans1.setText(sb.answer2);
                                break;
                            case "C":
                                ans1.setText(sb.answer3);
                                break;
                        }
                        return v;
                    }
                };
                break;
            case 1:
                ba = new BaseAdapter() {
                    View v;

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
                    public View getView(int position, View convertView, ViewGroup parent) {
                        if (convertView == null) {
                            v = View.inflate(getContext(), R.layout.judge_item_layout, null);
                        } else v = convertView;
                        JudgeBean jb = (JudgeBean) list.get(position);
                        TextView jques;
                        TextView jans;
                        jques = v.findViewById(R.id.judge_item_qus);
                        jans = v.findViewById(R.id.judge_item_ans);
                        if (jb.flag) v.setBackgroundColor(getResources().getColor(R.color.bg1));
                        else v.setBackgroundColor(getResources().getColor(R.color.zlyellow));
                        jques.setText(jb.No + "、" + jb.question);
                        jans.setText(jb.answer);
                        return v;
                    }
                };
                break;
        }
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        lv.setAdapter(ba);
    }
}
