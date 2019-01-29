package com.zundi.perawatindonesia.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.IdRes;
import android.support.v4.view.PagerAdapter;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.zundi.inc.R;
import com.zundi.perawatindonesia.model.Soal;

import java.util.ArrayList;

/**
 * Created by zindx on 13/05/2017.
 */

public class TestPagerAdapter extends PagerAdapter {

    private Activity mActivity;
    private ArrayList<Soal> mList;
    private LayoutInflater inflater;

    public TestPagerAdapter(Activity activity, ArrayList<Soal> dataset) {
        this.mActivity = activity;
        this.mList = dataset;
        this.inflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return this.mList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        final Soal item = mList.get(position);
        View v = inflater.inflate(R.layout.item_pager_test, container, false);

        TextView itemSoal = (TextView) v.findViewById(R.id.item_soal);
        TextView itemMateri = (TextView) v.findViewById(R.id.item_materi);
        RadioGroup itemChoice = (RadioGroup) v.findViewById(R.id.item_choice);
        RadioButton A = (RadioButton) v.findViewById(R.id.item_choice_a);
        RadioButton B = (RadioButton) v.findViewById(R.id.item_choice_b);
        RadioButton C = (RadioButton) v.findViewById(R.id.item_choice_c);
        RadioButton D = (RadioButton) v.findViewById(R.id.item_choice_d);
        RadioButton E = (RadioButton) v.findViewById(R.id.item_choice_e);

        itemMateri.setText(item.materi);
        itemSoal.setText(item.soal);
        itemChoice.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                String ch = "";
                switch (checkedId) {
                    case R.id.item_choice_a : ch = "a"; break;
                    case R.id.item_choice_b : ch = "b"; break;
                    case R.id.item_choice_c : ch = "c"; break;
                    case R.id.item_choice_d : ch = "d"; break;
                    case R.id.item_choice_e : ch = "e"; break;
                }
                item.jawabanUser = ch;
            }
        });

        if(!TextUtils.isEmpty(item.jawabanUser)) {
            A.setChecked(item.jawabanUser.equals("a"));
            B.setChecked(item.jawabanUser.equals("b"));
            C.setChecked(item.jawabanUser.equals("c"));
            D.setChecked(item.jawabanUser.equals("d"));
            E.setChecked(item.jawabanUser.equals("e"));
        }

        A.setText(item.A);
        B.setText(item.B);
        C.setText(item.C);
        D.setText(item.D);
        E.setText(item.E);

        container.addView(v, 0);
        return v;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((ScrollView) object);
    }
}