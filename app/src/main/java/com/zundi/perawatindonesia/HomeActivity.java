package com.zundi.perawatindonesia;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.support.customtabs.CustomTabsIntent;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.google.gson.Gson;
import com.zundi.inc.R;
import com.zundi.perawatindonesia.adapter.MenuBaseAdapter;
import com.zundi.perawatindonesia.db.DB;
import com.zundi.perawatindonesia.model.Menu;
import com.zundi.perawatindonesia.model.Res;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    private BroadcastReceiver mBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar bar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(bar);

        ArrayList<Menu> arrMenu = new ArrayList<>();
        arrMenu.add(new Menu("DIAGNOSA", R.drawable.ic_diagnosa));
        arrMenu.add(new Menu("PENYAKIT", R.drawable.ic_penyakit));
        arrMenu.add(new Menu("SOP TINDAKAN", R.drawable.ic_sop));
        arrMenu.add(new Menu("LABORATORIUM", R.drawable.ic_lab));
        arrMenu.add(new Menu("UJIKOM", R.drawable.ic_ujikom));
        arrMenu.add(new Menu("VIDEO", R.drawable.ic_video_new));

        GridView menu = (GridView) findViewById(R.id.home_gridmenu);
        menu.setAdapter(new MenuBaseAdapter(this, arrMenu));
        menu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0 : CommonActivity.page(HomeActivity.this, "Diagnosa", DB.Jenis.REF_DIAGNOSA); break;
                    case 1 : CommonActivity.page(HomeActivity.this, "Penyakit", DB.Jenis.REF_PENYAKIT); break;
                    case 2 : CommonActivity.page(HomeActivity.this, "SOP Tindakan", DB.Jenis.SOP); break;
                    case 3 : CommonActivity.page(HomeActivity.this, "Laboratorium", DB.Jenis.REF_LAB); break;
                    case 4 :
                        startActivity(new Intent(HomeActivity.this, TestActivity.class));
                        break;
                    case 5 :
                        String url = "https://www.youtube.com/channel/UCgBpptVfH8M8Wld3axXR3-w";
                        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                        builder.setToolbarColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
                        CustomTabsIntent customTabsIntent = builder.build();
                        customTabsIntent.intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        customTabsIntent.launchUrl(getApplicationContext(), Uri.parse(url));
                        break;
           }
            }
        });

        mBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(App.PUSH_NOTIFICATION)) {
                    Res.getNotification item =
                            new Gson().fromJson(intent.getStringExtra("data"), Res.getNotification.class);
                    NotificationUtils nu = new NotificationUtils(getApplicationContext());
                    nu.showNotificationMessage(
                        item.title,
                        item.message,
                        item.timeStamp, null);
                    Log.d("data", intent.getStringExtra("data"));
                }
            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager
                .getInstance(this).registerReceiver(mBroadcastReceiver,
                new IntentFilter(App.PUSH_NOTIFICATION));
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager
                .getInstance(this).unregisterReceiver(mBroadcastReceiver);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        moveTaskToBack(true);
    }

    public void mnuClick(View v) {
        switch (v.getId()) {
            case R.id.mnu_chat : startActivity(new Intent(HomeActivity.this, ChatActivity.class)); break;
            case R.id.mnu_profile : startActivity(new Intent(HomeActivity.this, ProfileActivity.class)); break;
        }
    }
}
