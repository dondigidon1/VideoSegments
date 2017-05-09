package com.redrocket.videosegments;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;

public class TrackLinearLayoutManager extends LinearLayoutManager {
    private static final String TAG = "TrackLinearLayoutManage";

    private TrackListener mListener;

    public TrackLinearLayoutManager(Context context) {
        super(context);
    }

    public TrackLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    public TrackLinearLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void setTrackListener(final TrackListener listener){
        mListener = listener;
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        super.onLayoutChildren(recycler, state);
        Log.i(TAG, "onLayoutChildren: ");

        if (!state.isPreLayout() && mListener != null){
            mListener.onUpdateLayout();
            mListener.onLayoutChildren();
        }

    }

    @Override
    public int scrollHorizontallyBy(int dx, RecyclerView.Recycler recycler, RecyclerView.State state) {
        final int px = super.scrollHorizontallyBy(dx, recycler, state);

        if (mListener != null)
            mListener.onUpdateLayout();

        return px;
    }

    public interface TrackListener {
        void onUpdateLayout();

        void onLayoutChildren();
    }
}
