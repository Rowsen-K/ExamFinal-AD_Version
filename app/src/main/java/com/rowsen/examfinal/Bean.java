package com.rowsen.examfinal;

import android.os.Parcel;
import android.os.Parcelable;

public class Bean implements Parcelable {
    public String No;
    public String question;
    public String img;
    public String answer1;
    public String answer2;
    public String answer3;
    public String answer4;
    public String corAns;
    public int type;
    public boolean flag = false;

    public Bean(Parcel in) {
        No = in.readString();
        question = in.readString();
        img = in.readString();
        answer1 = in.readString();
        answer2 = in.readString();
        answer3 = in.readString();
        answer4 = in.readString();
        corAns = in.readString();
        type = in.readInt();
    }

    public Bean() {
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Bean) {
            if (this.question.equals(((Bean) obj).question) && this.answer1.equals(((Bean) obj).answer1) && this.answer2.equals(((Bean) obj).answer2) && this.corAns.equals(((Bean) obj).corAns))
                return true;
            else
                return false;
        } else
            return false;
    }

    public Bean(Bean sb) {
        this.No = sb.No;
        this.question = sb.question;
        this.img = sb.img;
        this.answer1 = sb.answer1;
        this.answer2 = sb.answer2;
        this.answer3 = sb.answer3;
        this.answer4 = sb.answer4;
        this.corAns = sb.corAns;
        this.type = sb.type;
        this.flag = sb.flag;
    }

    public static final Creator<Bean> CREATOR = new Creator<Bean>() {
        @Override
        public Bean createFromParcel(Parcel in) {
            return new Bean(in);
        }

        @Override
        public Bean[] newArray(int size) {
            return new Bean[size];
        }
    };

    @Override
    public String toString() {
        System.out.println(No + "---" + question);
        System.out.println(img);
        System.out.println(answer1);
        System.out.println(answer2);
        System.out.println(answer3);
        System.out.println(answer4);
        System.out.println(corAns);
        System.out.println(type);
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
        parcel.writeString(img);
        parcel.writeString(answer1);
        parcel.writeString(answer2);
        parcel.writeString(answer3);
        parcel.writeString(answer4);
        parcel.writeString(corAns);
        parcel.writeInt(type);
    }
}
