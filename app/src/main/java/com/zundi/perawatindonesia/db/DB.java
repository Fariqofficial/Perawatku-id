package com.zundi.perawatindonesia.db;

import android.content.Context;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

/**
 * Created by zindx on 05/09/2017.
 */

public class DB extends SQLiteAssetHelper {

    public static final String DATABASE_NAME = "data";
    private static final int DATABASE_VERSION = 1;

    public enum Jenis {
        REF_DIAGNOSA,
        REF_PENYAKIT,
        REF_LAB,
        DIAGNOSA,
        PENYAKIT,
        LAB,
        SOP,
        UJIKOM
    }

    public DB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

}
