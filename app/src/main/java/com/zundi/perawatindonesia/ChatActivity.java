package com.zundi.perawatindonesia;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.zundi.inc.R;
import com.zundi.perawatindonesia.adapter.ChatRecAdapter;
import com.zundi.perawatindonesia.db.DBAccess;
import com.zundi.perawatindonesia.model.Chat;
import com.zundi.perawatindonesia.model.Res;
import com.zundi.perawatindonesia.model.User;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatActivity extends AppCompatActivity {

    private EditText mChatMsg;
    private ImageView mBtnSend;
    private RecyclerView mChatRecview;
    private ProgressDialog mProgress;

    private ArrayList<Chat> mDataset;
    private DBAccess dba;
    private BroadcastReceiver mBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        mDataset = new ArrayList<>();
        dba = DBAccess.getInstance(this);

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mChatMsg = (EditText) findViewById(R.id.chat_message);
        mChatRecview = (RecyclerView) findViewById(R.id.chat_recview);
        mBtnSend = (ImageView) findViewById(R.id.btn_send);
        mProgress = Util.ShowProgDialog(this, false);

        mChatMsg.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mBtnSend.setEnabled(s.length() > 0);
                mBtnSend.setImageResource(s.length() > 0 ? R.drawable.ic_send_a : R.drawable.ic_send);
            }
        });

        mBtnSend.setEnabled(false);
        mBtnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendChat();
            }
        });

        mChatRecview.setHasFixedSize(true);
        mChatRecview.setLayoutManager(new LinearLayoutManager(this));
        mChatRecview.setAdapter(new ChatRecAdapter(this, mDataset, getUser().userID));

        mBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(App.PUSH_NOTIFICATION)) {
                    getChat(false);
                    new NotificationUtils(getApplicationContext()).playSound();
                }
            }
        };

        getChat(true);

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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private User getUser() {
        User u;
        dba.open();
        u = dba.getUser();
        dba.close();
        return u;
    }

    private void getChat(boolean loading) {
        if(loading) {
            mProgress.setMessage("Mengambil Data Obrolan..");
            mProgress.show();
        }

        Call<Res.getChat> serv = App.getInstance().getApi().getChat();
        serv.enqueue(new Callback<Res.getChat>() {
            @Override
            public void onResponse(Call<Res.getChat> call, Response<Res.getChat> response) {
                if(response.isSuccessful()) {
                    mDataset = response.body().rows;
                    ((ChatRecAdapter) mChatRecview.getAdapter()).swapItems(mDataset);
                    mChatRecview.getLayoutManager()
                            .scrollToPosition(mChatRecview.getAdapter().getItemCount() - 1);
                }
                mProgress.hide();
            }

            @Override
            public void onFailure(Call<Res.getChat> call, Throwable t) {
                mProgress.hide();
            }
        });
    }

    private void sendChat() {
        mProgress.setMessage("Mengirim Pesan");
        mProgress.show();

        Call<Res.sendChat> serv = App.getInstance().getApi()
                .sendChat(getUser().userID, Util.val(mChatMsg));
        serv.enqueue(new Callback<Res.sendChat>() {
            @Override
            public void onResponse(Call<Res.sendChat> call, Response<Res.sendChat> response) {
                if(response.isSuccessful()) {
                    getChat(false);
                    Util.clearVal(mChatMsg);
                }
                mProgress.hide();
            }

            @Override
            public void onFailure(Call<Res.sendChat> call, Throwable t) {
                mProgress.hide();
            }
        });
    }

}
