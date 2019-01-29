package com.zundi.perawatindonesia;

import android.content.Intent;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.zundi.inc.R;
import com.zundi.perawatindonesia.adapter.TestPagerAdapter;
import com.zundi.perawatindonesia.db.DBAccess;
import com.zundi.perawatindonesia.model.Soal;
import com.zundi.perawatindonesia.util.ViewPagerLocked;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class TestNextActivity extends AppCompatActivity {
    
    private TextView mCounter, mBtnNext, mBtnPrev, mPage, mTestSalahLabel, mTestSalahLabel2;
    private ViewPagerLocked mTestPager;
    private ProgressBar mProgress;
    private LinearLayout mPageBenar, mPageSalah;
    private ArrayList<Soal> mDataset;
    private int totalPage, i, time = 10800000;
    private DBAccess dba;
    private PageResult pr;
    private CountDownTimer mCountDownTimer;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_next);

        dba = DBAccess.getInstance(this);
        mDataset = new ArrayList<>();

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        mCounter = (TextView) findViewById(R.id.test_count);
        mProgress = (ProgressBar) findViewById(R.id.test_progressbar);
        mBtnNext = (TextView) findViewById(R.id.btn_next);
        mBtnPrev = (TextView) findViewById(R.id.btn_prev);
        mPage = (TextView) findViewById(R.id.test_page);
        mTestPager = (ViewPagerLocked) findViewById(R.id.test_pager);
        mPageBenar = (LinearLayout) findViewById(R.id.test_benar);
        mPageSalah = (LinearLayout) findViewById(R.id.test_salah);
        mTestSalahLabel = (TextView) findViewById(R.id.test_salah_user);
        mTestSalahLabel2 = (TextView) findViewById(R.id.test_salah_benar);


        mBtnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            countResult();
            }
        });

        mBtnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentItem = mTestPager.getCurrentItem();

                Soal item = mDataset.get(currentItem);
                if(TextUtils.isEmpty(item.jawabanUser)) {
                    Util.showToast(getApplication(), "Anda belum menjawab soal ini.");
                    return;
                }

                if(item.jawabanUser.equals(item.jawabanBenar)) {
                    pr.openTrue();
                } else {
                    pr.openFalse(item);
                }

                mTestPager.setCurrentItem(currentItem + 1);
                mPage.setText((mTestPager.getCurrentItem() + 1)  + "/" + totalPage);

                if((currentItem + 1) == mDataset.size()) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            countResult();
                        }
                    }, (item.jawabanUser.equals(item.jawabanBenar) ? pr.defaultTimer : pr.defaultTimer + 1000));
                }
            }
        });

        mTestPager.setPagingEnabled(false);

        fillData();
        startTimer();
        mCountDownTimer.start();
        pr = new PageResult();
        mPage.setText("1/" + totalPage);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 17) {
            if(resultCode == RESULT_OK) {
                finish();
            } else {
                setResult(RESULT_OK);
                finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        Util.showToast(getApplicationContext(), "Sedang ujian tidak bisa kembali");
    }

    private void fillData() {
        dba.open();
        mDataset = dba.getListTest();
        dba.close();

        mTestPager.setAdapter(new TestPagerAdapter(this, mDataset));
        totalPage = mDataset.size();
    }

    private void startTimer() {
        i = 0;
        mCountDownTimer = new CountDownTimer(time, 1000) {
            @Override
            public void onTick(long millis) {
                i++;
                String hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millis),
                        TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
                        TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
                mCounter.setText(hms);
                mProgress.setProgress(i*100/(time/1000));
            }

            @Override
            public void onFinish() {
                mProgress.setProgress(100);
                countResult();
            }
        };
    }

    private void countResult() {
        int benar = 0;
        float persentase;

        for(Soal item : mDataset) {
            Log.d("hasilx", item.jawabanBenar +" = "+ item.jawabanUser);
            if(!TextUtils.isEmpty(item.jawabanUser)) {
                if(item.jawabanUser.equalsIgnoreCase(item.jawabanBenar)) {
                    benar += 1;
                }
            }
        }

        persentase = (benar * 100) / 180 ;
        Intent intent = new Intent(TestNextActivity.this, TestResultActivity.class);
        intent.putExtra("benar", benar);
        intent.putExtra("persentase", persentase);
        startActivityForResult(intent, 17);
    }

    private class PageResult {
        private int defaultTimer = 1500;

        PageResult() {
            this.hideAll();
        }

        void openTrue() {
            mPageBenar.setVisibility(View.VISIBLE);
            mPageSalah.setVisibility(View.GONE);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    hideAll();
                }
            }, this.defaultTimer);
        }

        void openFalse(Soal item) {
            mPageSalah.setVisibility(View.VISIBLE);
            mPageBenar.setVisibility(View.GONE);
            mTestSalahLabel.setText(item.jawabanUser);
            mTestSalahLabel2.setText(item.jawabanBenar);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    hideAll();
                }
            }, this.defaultTimer + 1000);
        }

        void hideAll() {
            mPageBenar.setVisibility(View.GONE);
            mPageSalah.setVisibility(View.GONE);
        }
    }


}
