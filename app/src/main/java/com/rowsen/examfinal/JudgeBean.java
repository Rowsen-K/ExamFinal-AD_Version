package com.rowsen.examfinal;

import android.os.Parcel;
import android.os.Parcelable;

public class JudgeBean implements Parcelable {
    public String No;
    public String question;
    public String answer;
    public boolean flag = false;

    protected JudgeBean(Parcel in) {
        No = in.readString();
        question = in.readString();
        answer = in.readString();
    }

    public JudgeBean() {

    }

    public static final Creator<JudgeBean> CREATOR = new Creator<JudgeBean>() {
        @Override
        public JudgeBean createFromParcel(Parcel in) {
            return new JudgeBean(in);
        }

        @Override
        public JudgeBean[] newArray(int size) {
            return new JudgeBean[size];
        }
    };

    public String toString() {
        return No + "---" + question + "\\r\\n" + answer;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(No);
        parcel.writeString(question);
        parcel.writeString(answer);
    }
}
