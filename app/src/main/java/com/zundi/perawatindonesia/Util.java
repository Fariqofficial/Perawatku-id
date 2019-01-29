package com.zundi.perawatindonesia;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.widget.EditText;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.lang.ref.WeakReference;
import java.util.List;


/**
 * Created by Teguh on 17/07/2014.
 */
public class Util {

    private static WeakReference<Bitmap> sBitmap = null;

    public static void setBitmap(Bitmap image) {
        Util.sBitmap = image != null ? new WeakReference<>(image) : null;
    }

    public static void clearBitmap() {
        setBitmap(null);
    }

    public static Bitmap getBitmap() {
        return sBitmap != null ? sBitmap.get() : null;
    }

    public static void showToast(Context context, String pesan) {
        showToast(context, pesan, Toast.LENGTH_SHORT);
    }

    public static void showToast(Context context, String pesan, int time) {
        Toast.makeText(context, pesan, time).show();
    }

    public static ProgressDialog ShowProgDialog(Activity activity, boolean cancelable) {
        ProgressDialog mDialog = new ProgressDialog(activity);
        mDialog.setIndeterminate(false);
        mDialog.setCancelable(cancelable);
        return mDialog;
    }

    public static boolean isEmpty(EditText... view) {
        boolean result = false;

        for(EditText item : view) {
            if(item.getText().length() == 0) {
                item.setError("Tidak Boleh Kosong");
                result = true;
            }
        }

        return result;
    }

    public static void clearErrors(EditText... view) {
        for(EditText item : view) {
            item.setError(null);
        }
    }

    public static void clearVal(EditText... view) {
        for(EditText item : view) {
            item.setText("");
        }
    }

    public static String val(EditText e) {
        return e.getText().toString();
    }

    public static byte[] createByteFromBitmap(Bitmap bmp) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        return stream.toByteArray();
    }

    public static boolean isAppIsInBackground(Context context) {
        boolean isInBackground = true;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    for (String activeProcess : processInfo.pkgList) {
                        if (activeProcess.equals(context.getPackageName())) {
                            isInBackground = false;
                        }
                    }
                }
            }
        } else {
            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
            ComponentName componentInfo = taskInfo.get(0).topActivity;
            if (componentInfo.getPackageName().equals(context.getPackageName())) {
                isInBackground = false;
            }
        }

        return isInBackground;
    }

}
