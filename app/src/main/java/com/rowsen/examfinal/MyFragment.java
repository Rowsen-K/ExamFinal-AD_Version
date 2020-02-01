package com.rowsen.examfinal;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.rowsen.mytools.Tools;

import java.util.ArrayList;

public class MyFragment extends Fragment {
    public String title;
    ListView lv;
    View v;
    int mode;
    BaseAdapter ba;
    ArrayList<Bean> list;

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
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
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
                        if(TextUtils.isEmpty(list.get(position).img))
                            return 0;
                        else
                            return 1;
                    }

                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {
                        ViewHolder_selection selection = null;
                        Bean sb = list.get(position);
                        switch (getItemViewType(position)){
                            case 0:
                                if (convertView == null) {
                                    convertView = View.inflate(getContext(), R.layout.selection_item_layout, null);
                                    selection = new ViewHolder_selection();
                                    selection.root = convertView.findViewById(R.id.root);
                                    selection.ques = convertView.findViewById(R.id.tv_question);
                                    selection.ans1 = convertView.findViewById(R.id.tv_answer1);
                                    convertView.setTag(selection);
                                } else selection = (ViewHolder_selection) convertView.getTag();
                                break;
                            case 1:
                                if (convertView == null) {
                                    convertView = View.inflate(getContext(), R.layout.selection_img_item_layout, null);
                                    selection = new ViewHolder_selection_img();
                                    selection.root = convertView.findViewById(R.id.root);
                                    selection.ques = convertView.findViewById(R.id.tv_question);
                                    selection.ans1 = convertView.findViewById(R.id.tv_answer1);
                                    ((ViewHolder_selection_img) selection).img = convertView.findViewById(R.id.question_img);
                                    convertView.setTag(selection);
                                }
                                else selection = (ViewHolder_selection_img) convertView.getTag();
                                ((ViewHolder_selection_img) selection).img.setImageBitmap(Tools.base64ToBitmap(sb.img.split("=")[1]));
                                break;
                        }
                        if (sb.flag) selection.root.setBackgroundColor(getResources().getColor(R.color.bg1));
                        else selection.root.setBackgroundColor(getResources().getColor(R.color.zlyellow));
                        selection.ques.setText(sb.No + "、" + sb.question);
                        //Log.e("答案",sb.corAns);
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
                        return convertView;
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
                        Bean jb = list.get(position);
                        TextView jques;
                        TextView jans;
                        jques = v.findViewById(R.id.judge_item_qus);
                        jans = v.findViewById(R.id.judge_item_ans);
                        if (jb.flag) v.setBackgroundColor(getResources().getColor(R.color.bg1));
                        else v.setBackgroundColor(getResources().getColor(R.color.zlyellow));
                        jques.setText(position + 1 + "、" + jb.question);
                        jans.setText(jb.corAns);
                        if ("✔".equals(jb.corAns))
                            jans.setTextColor(getResources().getColor(R.color.green));
                        else
                            jans.setTextColor(getResources().getColor(R.color.colorAccent));
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

    public class ViewHolder_selection{
        ViewGroup root;
        TextView ques,ans1;
    }
    public class ViewHolder_selection_img extends ViewHolder_selection{
        ImageView img;
    }
}
