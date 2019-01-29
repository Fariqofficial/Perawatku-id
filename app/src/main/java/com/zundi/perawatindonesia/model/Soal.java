package com.zundi.perawatindonesia.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by zindx on 05/09/2017.
 */

public class Soal implements Parcelable {

    public int idSoal;
    public int idMateri;
    public String soal;
    public String A;
    public String B;
    public String C;
    public String D;
    public String E;
    public String jawabanBenar;
    public String jawabanUser;
    public String materi;

    public Soal() {}


    protected Soal(Parcel in) {
        idSoal = in.readInt();
        idMateri = in.readInt();
        soal = in.readString();
        A = in.readString();
        B = in.readString();
        C = in.readString();
        D = in.readString();
        E = in.readString();
        jawabanBenar = in.readString();
        jawabanUser = in.readString();
        materi = in.readString();
    }

    public static final Creator<Soal> CREATOR = new Creator<Soal>() {
        @Override
        public Soal createFromParcel(Parcel in) {
            return new Soal(in);
        }

        @Override
        public Soal[] newArray(int size) {
            return new Soal[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(idSoal);
        dest.writeInt(idMateri);
        dest.writeString(soal);
        dest.writeString(A);
        dest.writeString(B);
        dest.writeString(C);
        dest.writeString(D);
        dest.writeString(E);
        dest.writeString(jawabanBenar);
        dest.writeString(jawabanUser);
        dest.writeString(materi);
    }
}
