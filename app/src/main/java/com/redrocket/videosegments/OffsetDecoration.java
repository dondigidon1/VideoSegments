package com.redrocket.videosegments;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

public class OffsetDecoration extends RecyclerView.ItemDecoration {

    private static final String TAG = "OffsetDecoration";

    private final int mOffset;

    OffsetDecoration(final int offset){
        mOffset = offset;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        final int pos = parent.getChildAdapterPosition(view);
        Log.i(TAG, "getItemOffsets: pos " + pos);
        if (pos == 0)
            outRect.left = mOffset;
        else if (pos == parent.getAdapter().getItemCount() - 1)
            outRect.right = mOffset;
    }
}
