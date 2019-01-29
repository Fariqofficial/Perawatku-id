package com.zundi.perawatindonesia.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zundi.inc.R;
import com.zundi.perawatindonesia.model.Chat;

import java.util.ArrayList;

/**
 * Created by Teguh on 14/02/2016.
 */
public class ChatRecAdapter extends RecyclerView.Adapter<ChatRecAdapter.Holder> {

    private int SELF = 100;
    private String userID;
    private ArrayList<Chat> mDataset;
    private Activity mActivity;

    public ChatRecAdapter(Activity activity, ArrayList<Chat> mDataset, String userID) {
        this.mActivity = activity;
        this.mDataset = mDataset;
        this.userID = userID;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView =
                LayoutInflater.from(parent.getContext())
                        .inflate(viewType == SELF ?
                                R.layout.item_list_chat_self: R.layout.item_list_chat_other, parent, false);
        return new Holder(itemView);
    }

    @Override
    public int getItemViewType(int position) {
        Chat item = mDataset.get(position);
        if(item.userID.equals(userID)) {
            return SELF;
        }
        return super.getItemViewType(position);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        Chat item = mDataset.get(position);
        holder.itemAuthor.setText(item.userName);
        holder.itemText.setText(Html.fromHtml(item.chatMsg.replaceAll("(\\\\r\\\\n|\\\\n)", "<br />")));
        holder.itemDate.setText(item.chatDateIdn);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public void swapItems(ArrayList<Chat> data) {
        mDataset = data;
        notifyDataSetChanged();
    }

    class Holder extends RecyclerView.ViewHolder  {
        TextView itemAuthor, itemText, itemDate;

        Holder(View v) {
            super(v);
            itemAuthor = (TextView) v.findViewById(R.id.item_list_author);
            itemText = (TextView) v.findViewById(R.id.item_list_text);
            itemDate = (TextView) v.findViewById(R.id.item_list_date);
        }
    }
}
