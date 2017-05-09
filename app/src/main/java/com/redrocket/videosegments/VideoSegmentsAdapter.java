package com.redrocket.videosegments;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class VideoSegmentsAdapter extends RecyclerView.Adapter<VideoSegmentsAdapter.VideoHolder>{
    private static final String TAG = "VideoSegmentsAdapter";

    private int mPxPerSecond = 5;
    private List<Integer> mVideos = new ArrayList<>();

    @Override
    public VideoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.video_item_view, parent,false);
        return new VideoHolder(view);
    }

    @Override
    public void onBindViewHolder(VideoHolder holder, int position) {
        holder.bind(mVideos.get(position),mPxPerSecond);
    }

    public void setPxPerSecond(final int pxPerSecond) {
        mPxPerSecond = pxPerSecond;
        notifyDataSetChanged();
    }

    public int getPxPerSecond() {
        return mPxPerSecond;
    }

    public int getDuration(int pos){
        return mVideos.get(pos);
    }

    public int getTime(int pos){
        int time = 0;
        for (int i = 0; i < pos; i++)
            time += mVideos.get(i);

        return time;
    }

    @Override
    public int getItemCount() {
        return mVideos.size();
    }

    public void setVideos(final List<Integer> videos) {
        mVideos = videos;
        notifyDataSetChanged();
    }

    public void join(int leftPos) {

        final int joinDuration = mVideos.get(leftPos) + mVideos.get(leftPos + 1);

        mVideos.remove(leftPos+1);
        mVideos.remove(leftPos);

        mVideos.add(leftPos, joinDuration);
        notifyItemRemoved(leftPos);
//        notifyItemRemoved(leftPos+1);
//        notifyItemInserted(leftPos);
        notifyItemRangeChanged(leftPos, getItemCount() - 1);

    }

    public void addVideo(int pos, int video) {
        mVideos.add(pos, video);
        notifyItemInserted(pos);
        if (pos == mVideos.size() - 1)
            notifyItemChanged(pos - 1);
    }

    public void removeVideo(int pos) {
        mVideos.remove(pos);

            notifyItemRemoved(pos);
        if (pos == getItemCount())
            notifyDataSetChanged();
    }


    public int getTotalDuration(){
        int totalDuration = 0;
        for (int duration : mVideos)
            totalDuration += duration;

        return totalDuration;
    }

    public void moveVideo(int from, int to) {
        if (from < to) {
            for (int i = from; i < to; i++) {
                Collections.swap(mVideos, i, i + 1);
            }
        } else {
            for (int i = from; i > to; i--) {
                Collections.swap(mVideos, i, i - 1);
            }
        }
        notifyItemMoved(from, to);
    }

    static class VideoHolder extends RecyclerView.ViewHolder{


        VideoHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i(TAG, "onClick: " + getAdapterPosition() + " " + getLayoutPosition());
                }
            });

        }

        void bind(final int duration, final int pxPerSecond) {
            final int needWidth = duration * pxPerSecond;
            if (itemView.getLayoutParams().width != needWidth) {
                itemView.getLayoutParams().width = needWidth;
                itemView.setLayoutParams(itemView.getLayoutParams());
            }

        }
    }
}
