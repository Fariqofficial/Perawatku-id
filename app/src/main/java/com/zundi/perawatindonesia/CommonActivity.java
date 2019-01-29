package com.zundi.perawatindonesia;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.zundi.inc.R;
import com.zundi.perawatindonesia.adapter.CommonRecAdapter;
import com.zundi.perawatindonesia.db.DB;
import com.zundi.perawatindonesia.db.DBAccess;
import com.zundi.perawatindonesia.model.KeyValue;

import java.util.ArrayList;

public class CommonActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common);

        Intent currentData = getIntent();

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(currentData.getStringExtra("title"));

        ArrayList<KeyValue> mDataset = currentData.getParcelableArrayListExtra("list");
        RecyclerView mRecview = (RecyclerView) findViewById(R.id.common_recview);
        mRecview.setAdapter(new CommonRecAdapter(this, mDataset, (Enum) currentData.getSerializableExtra("type")));
        mRecview.setLayoutManager(new LinearLayoutManager(this));
        mRecview.setHasFixedSize(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static void page(Activity activity, String title, DB.Jenis Jenis, int jenisID) {
        DBAccess db = DBAccess.getInstance(activity);
        db.open();
        Intent intent = new Intent(activity, CommonActivity.class);
        intent.putExtra("title", title);
        intent.putExtra("type", Jenis);
        intent.putParcelableArrayListExtra("list", db.getList(Jenis, jenisID));
        activity.startActivity(intent);
        db.close();
    }

    public static void page(Activity activity, String title, DB.Jenis Jenis) {
        page(activity, title, Jenis, 0);
    }
}
