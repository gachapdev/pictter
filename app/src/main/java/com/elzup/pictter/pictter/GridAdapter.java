package com.elzup.pictter.pictter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by mike on 15/04/26.
 */
public class GridAdapter extends ArrayAdapter<GridData> {
    private LayoutInflater layoutInflater_;

    public GridAdapter(Context context, int textViewResourceId, List<GridData> objects) {
        super(context, textViewResourceId, objects);
        layoutInflater_ = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // 特定の行(position)のデータを得る
        GridData item = (GridData) getItem(position);

        // convertViewは使い回しされている可能性があるのでnullの時だけ新しく作る
        if (null == convertView) {
            convertView = layoutInflater_.inflate(R.layout.grid_layout, null);
        }

        CheckBox check;
        check = (CheckBox) convertView.findViewById(R.id.checkbox);
        check.setChecked(item.getCheck());

        // CustomDataのデータをViewの各Widgetにセットする
        ImageView imageView;
        imageView = (ImageView) convertView.findViewById(R.id.imageView2);
        imageView.setImageBitmap(item.getImageData());

        return convertView;
    }
}
