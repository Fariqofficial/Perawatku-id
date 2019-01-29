package com.zundi.perawatindonesia.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by zindx on 05/09/2017.
 */

public class KeyValue implements Parcelable {
    private int key;
    private String keyString;
    private String value;

    public KeyValue(int key, String value) {
        this.key = key;
        this.value = value;
    }

    public KeyValue(String keyString, String value) {
        this.keyString = keyString;
        this.value = value;
    }

    private KeyValue(Parcel in) {
        key = in.readInt();
        keyString = in.readString();
        value = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(key);
        dest.writeString(keyString);
        dest.writeString(value);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<KeyValue> CREATOR = new Creator<KeyValue>() {
        @Override
        public KeyValue createFromParcel(Parcel in) {
            return new KeyValue(in);
        }

        @Override
        public KeyValue[] newArray(int size) {
            return new KeyValue[size];
        }
    };

    public void setKey(int key) {
        this.key = key;
    }

    public int getKey() {
        return key;
    }

    public void setKeyString(String keyString) {
        this.keyString = keyString;
    }

    public String getKeyString() {
        return keyString;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
