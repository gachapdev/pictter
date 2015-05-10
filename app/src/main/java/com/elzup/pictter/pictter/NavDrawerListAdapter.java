package com.elzup.pictter.pictter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

public class NavDrawerListAdapter extends ArrayAdapter<NavDrawerItem> {
    private Context context;
    private List<NavDrawerItem> navDrawerItems;
    private LayoutInflater layoutInflater_;
    private View.OnClickListener clickListener;

    public NavDrawerListAdapter(Context context, int textViewResourceId, List<NavDrawerItem> navDrawerItems) {
        super(context, textViewResourceId, navDrawerItems);
        this.context = context;
        // TODO: remove?
        layoutInflater_ = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.navDrawerItems = navDrawerItems;
    }

    @Override
    public int getCount() {
        return navDrawerItems.size();
    }

    @Override
    public NavDrawerItem getItem(int position) {
        return navDrawerItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater)
                    context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.item_nav_drawer, null);
        }

        final ToggleButton favButton = (ToggleButton) convertView.findViewById(R.id.favButton);
        TextView txtTitle = (TextView) convertView.findViewById(R.id.title);
        txtTitle.setOnClickListener(this.clickListener);

        final NavDrawerItem navDrawerItem = navDrawerItems.get(position);
        favButton.setChecked(navDrawerItem.isFavorite());
        txtTitle.setText(navDrawerItem.getName());

        favButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navDrawerItem.toggle();
            }
        });

        // displaying count
        // check whether it set visible or not
        return convertView;
    }

    public int getPosition(String keyword) {
        int l = navDrawerItems.size();
        for (int i = 0; i < l; i++) {
            if (navDrawerItems.get(i).getName() == keyword) {
                return i;
            }
        }
        return -1;
    }

    public NavDrawerItem get(String keyword) {
        for (NavDrawerItem item : navDrawerItems) {
            if (item.getName() == keyword) {
                return item;
            }
        }
        return null;
    }

    public void setClickListener(View.OnClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public void removeUncheckedItems() {
        List<NavDrawerItem> removes = new ArrayList<>();
        for (NavDrawerItem item : this.navDrawerItems) {
            if (!item.isFavorite()) {
                removes.add(item);
            }
        }
        this.navDrawerItems.removeAll(removes);
        this.notifyDataSetChanged();
    }
}
