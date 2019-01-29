package com.zundi.perawatindonesia.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zundi.inc.R;
import com.zundi.perawatindonesia.model.Menu;

import java.util.ArrayList;

/**
 * Created by zindx on 05/08/2017.
 */

public class MenuBaseAdapter extends BaseAdapter {

    private Activity activity;
    private LayoutInflater inflater;
    private ArrayList<Menu> menuList;

    public MenuBaseAdapter(Activity activity, ArrayList<Menu> menuList) {
        this.activity = activity;
        this.menuList = menuList;
        this.inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return menuList.size();
    }

    @Override
    public Object getItem(int i) {
        return menuList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        final int position = i;

        View vi = convertView;
        ViewHolder holder;

        if(vi == null) {
            vi = inflater.inflate(R.layout.item_grid_menu, parent, false);
            holder = new ViewHolder(vi);
            vi.setTag(holder);
        } else {
            holder = (ViewHolder) vi.getTag();
        }

        final Menu item = menuList.get(position);

        holder.itemMenuLabel.setText(item.iconName);
        holder.itemMenuIcon.setImageResource(item.iconResources);

        return vi;
    }

    private static class ViewHolder {
        LinearLayout itemMenuContainer;
        ImageView itemMenuIcon;
        TextView itemMenuLabel;

        ViewHolder(View v) {
            itemMenuContainer = (LinearLayout) v.findViewById(R.id.item_grid_menu_container);
            itemMenuIcon = (ImageView) v.findViewById(R.id.item_grid_menu_icon);
            itemMenuLabel = (TextView) v.findViewById(R.id.item_grid_menu_label);
        }

    }

}
