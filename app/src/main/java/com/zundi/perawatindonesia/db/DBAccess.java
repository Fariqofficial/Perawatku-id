package com.zundi.perawatindonesia.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.zundi.perawatindonesia.model.KeyValue;
import com.zundi.perawatindonesia.model.Soal;
import com.zundi.perawatindonesia.model.User;

import java.util.ArrayList;

/**
 * Created by zindx on 05/09/2017.
 */

public class DBAccess {

    private static final String TAG = DB.class.getSimpleName();
    private SQLiteOpenHelper openHelper;
    private SQLiteDatabase database;
    private static DBAccess instance;

    private DBAccess(Context context) {
        this.openHelper = new DB(context);
    }

    public static DBAccess getInstance(Context context) {
        if (instance == null) {
            instance = new DBAccess(context);
        }
        return instance;
    }

    public void open() {
        this.database = openHelper.getWritableDatabase();
    }

    public void close() {
        if (database != null) {
            this.database.close();
        }
    }

    public ArrayList<KeyValue> getList(Enum jns, int id) {
        ArrayList<KeyValue> list = new ArrayList<>();
        String sqlString = null;
        if (jns.equals(DB.Jenis.REF_DIAGNOSA)) {
            sqlString = "SELECT a.*,count(ref_diagnosa_id) AS jumlah FROM ref_diagnosa AS a LEFT JOIN tabel_diagnosa AS b USING (ref_diagnosa_id) GROUP BY ref_diagnosa_id";
        } else if (jns.equals(DB.Jenis.REF_PENYAKIT)) {
            sqlString = "SELECT a.*,count(ref_penyakit_id) AS jumlah FROM ref_penyakit AS a LEFT JOIN tabel_penyakit AS b USING (ref_penyakit_id) GROUP BY ref_penyakit_id";
        } else if (jns.equals(DB.Jenis.REF_LAB)) {
            sqlString = "SELECT a.*,count(ref_lab_id) AS jumlah FROM ref_lab AS a LEFT JOIN tabel_lab AS b USING (ref_lab_id) GROUP BY ref_lab_id";
        } else if(jns.equals(DB.Jenis.DIAGNOSA)) {
            sqlString = "SELECT diagnosa_id, diagnosa_sub, diagnosa_nic FROM tabel_diagnosa WHERE ref_diagnosa_id = '"+ id +"'";
        } else if(jns.equals(DB.Jenis.PENYAKIT)) {
            sqlString = "SELECT penyakit_id, penyakit_sub, penyakit_desc from tabel_penyakit WHERE ref_penyakit_id = '"+ id +"'";
        } else if (jns.equals(DB.Jenis.LAB)) {
            sqlString = "select id_lab, sub_lab, isi_lab from tabel_lab where ref_lab_id = '"+ id +"'";
        } else if (jns.equals(DB.Jenis.SOP)) {
            sqlString = "SELECT * FROM tabel_sop";
        }

        Cursor c = database.rawQuery(sqlString, null);
        try {
            if (c.moveToFirst()) {
                do {
                    KeyValue item = new KeyValue(c.getInt(0), c.getString(1));
                    item.setKeyString(c.getString(2));
                    list.add(item);
                } while(c.moveToNext());
            }
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to get posts from database");
        } finally {
            if (c != null && !c.isClosed()) {
                c.close();
            }
        }
        return list;
    }

    public ArrayList<Soal> getListTest() {
        ArrayList<Soal> list = new ArrayList<>();
        Cursor c = database.rawQuery("SELECT * FROM tabel_test_soal INNER JOIN ref_materi USING(id_materi) ORDER BY RANDOM()", null);

        try {
            if (c.moveToFirst()) {
                do {
                    Soal item = new Soal();
                    item.idSoal = c.getInt(0);
                    item.idMateri = c.getInt(1);
                    item.soal = c.getString(2);
                    item.A = c.getString(3);
                    item.B = c.getString(4);
                    item.C = c.getString(5);
                    item.D = c.getString(6);
                    item.E = c.getString(7);
                    item.jawabanBenar = c.getString(8);
                    item.materi = c.getString(9);

                    list.add(item);
                } while(c.moveToNext());
            }
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to get posts from database");
        } finally {
            if (c != null && !c.isClosed()) {
                c.close();
            }
        }
        return list;
    }

    public long saveUser(User u, boolean editable) {
        long result = -1;

        database.beginTransaction();
        try {
            ContentValues cv = new ContentValues();
            cv.put("user_name", u.userName);
            cv.put("user_id", u.userID);

            if(editable) {
                result = database.update("user", cv, "user_id = ?", new String[] {u.userID});
            } else {
                result = database.insert("user", null, cv);
            }
            database.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to add or update user");
        } finally {
            database.endTransaction();
        }

        return result;
    }

    public long deleteUser() {
        int result = 0;
        database.beginTransaction();
        try {
            result = database.delete("user", null, null);
            database.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            database.endTransaction();
        }
        return result;
    }

    public User getUser() {
        User user = null;
        Cursor c = database.rawQuery("SELECT * FROM user ORDER BY user_id LIMIT 1", null);

        try {
            if(c.moveToFirst()) {
                user = new User();
                user.userID = c.getString(0);
                user.userName = c.getString(1);
                user.userPhoto = c.getString(2);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(c != null && !c.isClosed()) {
                c.close();
            }
        }
        return user;
    }

}
