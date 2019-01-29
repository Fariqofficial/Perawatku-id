package com.zundi.perawatindonesia;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.zundi.inc.R;

public class TestResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_result);

        TextView mBenar = (TextView) findViewById(R.id.test_benar);
        TextView mPresentase = (TextView) findViewById(R.id.test_presentase);

        mBenar.setText(getIntent().getIntExtra("benar", 0) + " soal");
        mPresentase.setText(getIntent().getFloatExtra("persentase", 0) + " %");

        findViewById(R.id.btn_finish).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });

        findViewById(R.id.btn_retry).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_OK);
                finish();
            }
        });
    }


    @Override
    public void onBackPressed() {

    }
}
