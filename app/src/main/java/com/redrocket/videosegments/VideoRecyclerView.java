package com.redrocket.videosegments;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;

public class VideoRecyclerView extends RecyclerView implements TrackLinearLayoutManager.TrackListener {
    private static final String TAG = "VideoRecyclerView";

    private int mOffset;

    private int mCurrentTime;

    public VideoRecyclerView(Context context) {
        super(context);
        init();
    }

    public VideoRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public VideoRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        if(!isInEditMode()){
            getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    mOffset = getWidth() / 2;
                    addItemDecoration(new OffsetDecoration(mOffset));
                    getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
            });
        }
    }


    @Override
    public void setLayoutManager(LayoutManager layout) {
        if (!isInEditMode()) {
            if (!(layout instanceof TrackLinearLayoutManager))
                throw new IllegalArgumentException("Need TrackLinearLayoutManager");

            ((TrackLinearLayoutManager) layout).setTrackListener(this);
        }


        super.setLayoutManager(layout);
    }

    public void smoothSetTime(int time) {
        VideoSegmentsAdapter adapter = ((VideoSegmentsAdapter) getAdapter());

        int timeDiff = time - mCurrentTime;
        Log.i(TAG, "smoothSetTime: timeDiff " + timeDiff);
        smoothScrollBy(timeDiff * adapter.getPxPerSecond(), 0);
    }

    @Override
    public void onUpdateLayout() {

//        int firstPos = ((LinearLayoutManager)getLayoutManager()).findFirstVisibleItemPosition();
//        View firstView = ((LinearLayoutManager)getLayoutManager()).findViewByPosition(firstPos);
//        int lastPos = ((LinearLayoutManager)getLayoutManager()).findLastVisibleItemPosition();
//        View lastView = ((LinearLayoutManager)getLayoutManager()).findViewByPosition(lastPos);
//
//
//
////        Log.i(TAG, "onUpdateLayout: "+firstPos+" "+firstView.getLeft()+" "+firstView.getRight()+" | "
////                +lastPos+" "+lastView.getLeft()+" "+lastView.getRight());
//
////        Log.i(TAG, "onUpdateLayout: " + firstPos + " "
////                + getLayoutManager().getDecoratedLeft(firstView) + " " + firstView.getLeft());
//
        VideoSegmentsAdapter adapter = ((VideoSegmentsAdapter) getAdapter());
////        int time = (int) ((1-(firstView.getWidth() + firstView.getLeft() / (float) firstView.getWidth())) *
////                adapter.getDuration(firstPos)
////                + adapter.getTime(firstPos));
//        int time = (int) (adapter.getTime(firstPos) + adapter.getDuration(firstPos)
//                - ((firstView.getWidth() + firstView.getLeft()) / (float) firstView.getWidth()) * adapter.getDuration(firstPos) + mOffset * 1f / adapter.getPxPerSecond());

        mCurrentTime = computeTime();

        Log.i(TAG, "onUpdateLayout: time " + mCurrentTime + " / " + adapter.getTotalDuration());


    }

    @Override
    public void onLayoutChildren() {
        //setItemAnimator(new DefaultItemAnimator());
    }

    private int computeTime(){
        int firstPos = ((LinearLayoutManager)getLayoutManager()).findFirstVisibleItemPosition();
        View firstView = getLayoutManager().findViewByPosition(firstPos);

        VideoSegmentsAdapter adapter = ((VideoSegmentsAdapter) getAdapter());
        return (int) (adapter.getTime(firstPos) + adapter.getDuration(firstPos)
                - ((firstView.getWidth() + firstView.getLeft()) / (float) firstView.getWidth()) * adapter.getDuration(firstPos) + mOffset * 1f / adapter.getPxPerSecond());
    }

//    public void skipAnimation() {
//        setItemAnimator(null);
//    }

    public void setTime(int time) {
        Log.i(TAG, "setTime: time "+time);
        VideoSegmentsAdapter adapter = ((VideoSegmentsAdapter) getAdapter());

        final int leftTime = (int) (time - mOffset * (1f / adapter.getPxPerSecond()));
        Log.i(TAG, "setTime: leftTime "+leftTime);


        int pos = -1;
        int offsetTime = -1;

        if(leftTime>=mOffset * (1f / adapter.getPxPerSecond())){
            for (int i = 0; i < adapter.getItemCount(); i++) {
                final int posStartTime = adapter.getTime(i);
                if (posStartTime + adapter.getDuration(i) > leftTime){
                    pos = i;
                    offsetTime = leftTime - posStartTime;
                    break;
                }
            }


        }else{
            pos = 0;
            offsetTime = time;
        }

        if (pos < 0 || offsetTime < 0)
            throw new IllegalStateException("pos " + pos + " " + " offsetTime " + offsetTime);


        scrollToPositionWithOffset(pos, -offsetTime * adapter.getPxPerSecond());
    }

    private void scrollToPositionWithOffset(final int position, final int offsetPx){
        Log.i(TAG, "scrollToPositionWithOffset: position " + position + " offsetPx " + offsetPx);
        if (isLayoutFrozen()) {
            return;
        }
        stopScroll();
        if (getLayoutManager() == null) {
            Log.e(TAG, "Cannot scroll to position a LayoutManager set. " +
                    "Call setLayoutManager with a non-null argument.");
            return;
        }
        ((LinearLayoutManager)getLayoutManager()).scrollToPositionWithOffset(position,offsetPx);
    }

    public int getTime() {
        return mCurrentTime;
    }
}
