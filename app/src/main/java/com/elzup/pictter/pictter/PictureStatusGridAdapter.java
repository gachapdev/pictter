package com.elzup.pictter.pictter;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;

public class PictureStatusGridAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private LayoutInflater mLayoutInflater;
    private ArrayList<PictureStatus> mDataList;

    public PictureStatusGridAdapter(Context context, ArrayList<PictureStatus> dataList) {
        super();
        mLayoutInflater = LayoutInflater.from(context);
        mDataList = dataList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = mLayoutInflater.inflate(R.layout.item_status_grid, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        this.onBindViewHolder((PictureStatusGridAdapter.ViewHolder) holder, position);
    }

    public void onBindViewHolder(PictureStatusGridAdapter.ViewHolder holder, int position) {
        PictureStatus data = mDataList.get(position);
        holder.image.setImageBitmap(data.getImage());
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView image;
        View rootView;

        public ViewHolder(View v) {
            super(v);
            rootView = v;
            image = (ImageView) v.findViewById(R.id.image);
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            rootView.setBackground(rootView.getResources().getDrawable(R.drawable.border_on));
        }
    }
}

