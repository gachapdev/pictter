package com.elzup.pictter.pictter.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.elzup.pictter.pictter.R;
import com.elzup.pictter.pictter.model.pojo.PictureStatus;
import com.elzup.pictter.pictter.view.activity.ShowImageActivity;

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
        holder.setPictureStatus(data);
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder implements GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener {
        PictureStatus pictureStatus;
        ImageView image;
        View rootView;
        private GestureDetector gestureDetector;

        public ViewHolder(View v) {
            super(v);
            rootView = v;
            image = (ImageView) v.findViewById(R.id.image);
            gestureDetector = new GestureDetector(v.getContext(), this);
            v.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    boolean result = gestureDetector.onTouchEvent(event);
                    return result;
                }
            });
        }

        public void setPictureStatus(PictureStatus pictureStatus) {
            this.pictureStatus = pictureStatus;
            this.image.setImageBitmap(pictureStatus.getImage());
            syncBackground();
        }

        private void syncBackground() {
            int back_id = R.drawable.border;
            if (pictureStatus.isSelected()) {
                back_id = R.drawable.border_on;
            }
            rootView.setBackground(rootView.getResources().getDrawable(back_id));
        }

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public void onShowPress(MotionEvent e) {

        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            return false;
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            pictureStatus.toggleSelected();
            syncBackground();
            return true;
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            return false;
        }

        @Override
        public boolean onDoubleTapEvent(MotionEvent e) {
            return false;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            return false;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            Intent intent = new Intent(rootView.getContext(), ShowImageActivity.class);
            intent.putExtra(ShowImageActivity.EXTRA_STRING_URL, pictureStatus.getImageUrl());
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            rootView.getContext().startActivity(intent);
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            return false;
        }
    }

    public ArrayList<PictureStatus> getSelectedPictureStatus() {
        ArrayList<PictureStatus> selectedList = new ArrayList<>();
        for (PictureStatus item : mDataList) {
            if (item.isSelected()) {
                selectedList.add(item);
            }
        }
        return selectedList;
    }

    public void selectAll() {
        // 全てのis_selected を統一する
        // とりあえず一つ目と反対にする
        boolean to_selected = !mDataList.get(0).isSelected();
        for (PictureStatus item : mDataList) {
            item.setSelected(to_selected);
        }
        notifyDataSetChanged();
    }
}

