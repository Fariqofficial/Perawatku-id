package com.zundi.perawatindonesia.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zundi.inc.R;
import com.zundi.perawatindonesia.CommonActivity;
import com.zundi.perawatindonesia.CommonPreviewActivity;
import com.zundi.perawatindonesia.TestActivity;
import com.zundi.perawatindonesia.db.DB;
import com.zundi.perawatindonesia.model.KeyValue;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by zindx on 05/09/2017.
 */

public class CommonRecAdapter extends RecyclerView.Adapter<CommonRecAdapter.Holder> {

    private ArrayList<KeyValue> mDataset;
    private Activity mActivity;
    private Enum mJenis;
    private DB.Jenis[] mRef = { DB.Jenis.REF_LAB, DB.Jenis.REF_DIAGNOSA, DB.Jenis.REF_PENYAKIT };

    public CommonRecAdapter(Activity activity, ArrayList<KeyValue> dataset, Enum jenis) {
        mActivity = activity;
        mDataset = dataset;
        mJenis = jenis;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        if(Arrays.asList(mRef).contains(mJenis)) {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_common_1, parent, false);
        } else {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_common_2, parent, false);
        }
        return new Holder(v);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        KeyValue item = mDataset.get(position);
        holder.itemTitle.setText(item.getValue());

        if(Arrays.asList(mRef).contains(mJenis)) {
            holder.itemSubTitle.setText(item.getKeyString() + " Jenis");
        }
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    class Holder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView itemTitle, itemSubTitle;

        Holder(View v) {
            super(v);
            v.setOnClickListener(this);
            v.setClickable(true);

            itemTitle = (TextView) v.findViewById(R.id.item_title);
            itemSubTitle = (TextView) v.findViewById(R.id.item_subtitle);
        }

        @Override
        public void onClick(View v) {
            KeyValue current = mDataset.get(getAdapterPosition());
            if(mJenis.equals(DB.Jenis.REF_DIAGNOSA)) {
                CommonActivity.page(mActivity, current.getValue() , DB.Jenis.DIAGNOSA, current.getKey());
            } else if (mJenis.equals(DB.Jenis.REF_PENYAKIT)) {
                CommonActivity.page(mActivity, current.getValue() , DB.Jenis.PENYAKIT, current.getKey());
            } else if (mJenis.equals(DB.Jenis.REF_LAB)) {
                CommonActivity.page(mActivity, current.getValue() , DB.Jenis.LAB, current.getKey());
            } else if(mJenis.equals(DB.Jenis.UJIKOM)) {
                Intent intent = new Intent(mActivity, TestActivity.class);
                intent.putExtra("materi", current.getKey());
                mActivity.startActivity(intent);
            } else {
                Intent intent = new Intent(mActivity, CommonPreviewActivity.class);
                intent.putExtra("title", current.getValue());
                intent.putExtra("html", current.getKeyString());
                mActivity.startActivity(intent);
            }
        }
    }

}
