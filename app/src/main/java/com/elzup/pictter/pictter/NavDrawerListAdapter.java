package com.elzup.pictter.pictter;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.List;

public class NavDrawerListAdapter extends ArrayAdapter<NavDrawerItem> {
    private Context context;
    private List<NavDrawerItem> navDrawerItems;
    private View.OnClickListener clickListener;
    private View.OnClickListener toggleListener;

    public NavDrawerListAdapter(Context context, int textViewResourceId, List<NavDrawerItem> navDrawerItems) {
        super(context, textViewResourceId, navDrawerItems);
        this.context = context;
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

        TextView nameTitle = (TextView) convertView.findViewById(R.id.title);
        nameTitle.setOnClickListener(this.clickListener);

        final ToggleButton favButton = (ToggleButton) convertView.findViewById(R.id.favButton);
        final NavDrawerItem navDrawerItem = navDrawerItems.get(position);
        final Drawable iconStarOn = convertView.getResources().getDrawable(android.R.drawable.btn_star_big_on);
        final Drawable iconStarOff = convertView.getResources().getDrawable(android.R.drawable.btn_star_big_off);

        favButton.setChecked(navDrawerItem.isFavorite());
        if (navDrawerItem.isFavorite()) {
            favButton.setBackgroundDrawable(iconStarOn);
        } else {
            favButton.setBackgroundDrawable(iconStarOff);
        }

        nameTitle.setText(navDrawerItem.getName());

        favButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navDrawerItem.toggle();
                if (navDrawerItem.isFavorite()) {
                    favButton.setBackgroundDrawable(iconStarOn);
                } else {
                    favButton.setBackgroundDrawable(iconStarOff);
                }
                toggleListener.onClick(v);
            }
        });

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
            if (item.getName().equals(keyword)) {
                return item;
            }
        }
        return null;
    }

    public void setClickListener(View.OnClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public void setToggleListener(View.OnClickListener toggleListener) {
        this.toggleListener = toggleListener;
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

    public List<NavDrawerItem> getFavoriteItems() {
        List<NavDrawerItem> items = new ArrayList<>();
        for (NavDrawerItem item : this.navDrawerItems) {
            if (item.isFavorite()) {
                items.add(item);
            }
        }
        return items;
    }
}
