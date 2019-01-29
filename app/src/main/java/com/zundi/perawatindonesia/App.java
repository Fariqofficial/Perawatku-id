package com.zundi.perawatindonesia;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;

import com.pixplicity.easyprefs.library.Prefs;
import com.zundi.perawatindonesia.db.DBAccess;
import com.zundi.perawatindonesia.model.User;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Zundi on 15/03/2017.
 */

public class App extends Application {

    private static App mInstance;
    private Retrofit retrofit = null;

    public static final String BASE_URL = "http://perawatindo.com/public/";
    //public static final String BASE_URL = "http://192.168.43.42/perawat-indonesia/public/";
    public static final String PUSH_NOTIFICATION = "pushNotification";
    public static final String TOPIC_GLOBAL = "zundi";
    public static final int NOTIFICATION_ID = 100;
    public static final int TIMEOUT_DEFAULT = 20;
    public static synchronized App getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        new Prefs.Builder()
            .setContext(getApplicationContext())
            .setMode(MODE_PRIVATE)
            .setPrefsName(getPackageName())
            .setUseDefaultSharedPreference(true)
            .build();
    }

    public Retrofit getClient(int timeout) {
        if(retrofit == null) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient client = new OkHttpClient.Builder()
                .readTimeout(timeout, TimeUnit.SECONDS)
                .writeTimeout(timeout, TimeUnit.SECONDS)
                .connectTimeout(timeout, TimeUnit.SECONDS)
                .addInterceptor(logging)
                .build();

            retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        }
        return retrofit;
    }

    public Retrofit getClient() {
        return getClient(TIMEOUT_DEFAULT);
    }

    public ApiInterface getApi() {
        return getClient().create(ApiInterface.class);
    }

    public User getUser() {
        User user;
        DBAccess dba = DBAccess.getInstance(this);
        dba.open();
        user = dba.getUser();
        dba.close();
        return user;

    }

    public void startSession(Activity activity) {
        if(getUser() != null) {
            Intent intent = new Intent(activity, HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
    }

    public void destroySession(Activity activity) {
        DBAccess dba = DBAccess.getInstance(this);
        dba.open();
        if(dba.deleteUser() != 0) {
            Intent intent = new Intent(activity, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
        dba.close();
    }
}
