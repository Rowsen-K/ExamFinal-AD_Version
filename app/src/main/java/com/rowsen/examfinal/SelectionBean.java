package com.rowsen.examfinal;

import android.os.Parcel;
import android.os.Parcelable;

public class SelectionBean implements Parcelable {
    public String No;
    public String question;
    public String answer1;
    public String answer2;
    public String answer3;
    public String corAns;
    public boolean flag = false;

    public SelectionBean(Parcel in) {
        No = in.readString();
        question = in.readString();
        answer1 = in.readString();
        answer2 = in.readString();
        answer3 = in.readString();
        corAns = in.readString();
    }

    public SelectionBean() {

    }

    public static final Creator<SelectionBean> CREATOR = new Creator<SelectionBean>() {
        @Override
        public SelectionBean createFromParcel(Parcel in) {
            return new SelectionBean(in);
        }

        @Override
        public SelectionBean[] newArray(int size) {
            return new SelectionBean[size];
        }
    };

    @Override
    public String toString() {
        System.out.println(No + "---" + question);
        System.out.println(answer1);
        System.out.println(answer2);
        System.out.println(answer3);
        System.out.println(corAns);
        return super.toString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(No);
        parcel.writeString(question);
        parcel.writeString(answer1);
        parcel.writeString(answer2);
        parcel.writeString(answer3);
        parcel.writeString(corAns);
    }
}
