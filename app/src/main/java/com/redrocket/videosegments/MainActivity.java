package com.redrocket.videosegments;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.SeekBar;

import java.util.ArrayList;
import java.util.List;

import static com.redrocket.videosegments.R.id.recyclerView;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private VideoSegmentsAdapter mAdapter;
    private VideoRecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (VideoRecyclerView) findViewById(recyclerView);
        mRecyclerView.setHasFixedSize(true);

        TrackLinearLayoutManager layoutManager = new TrackLinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator(){
//            @Override
//            public long getRemoveDuration() {
//                return 6000;
//            }
//
//            @Override
//            public long getAddDuration() {
//                return 6000;
//            }
//
//            @Override
//            public long getMoveDuration() {
//                return 6000;
//            }
//
//
//            @Override
//            public long getChangeDuration() {
//                return 0;
//            }
        });

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                int dragFlags = ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
                int swipeFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
                return makeMovementFlags(dragFlags, swipeFlags);
            }

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                mAdapter.moveVideo(viewHolder.getAdapterPosition(),target.getAdapterPosition());
                return true;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                final int position = viewHolder.getAdapterPosition();

                final int nextTime = position == 0 ? 0 :
                        (mAdapter.getTime(position-1) + mAdapter.getDuration(position-1));

                mAdapter.removeVideo(position);

                mRecyclerView.setTime(nextTime);

            }

            @Override
            public boolean isLongPressDragEnabled() {
                return true;
            }

            @Override
            public boolean isItemViewSwipeEnabled() {
                return true;
            }
        });

        itemTouchHelper.attachToRecyclerView(mRecyclerView);

        mAdapter = new VideoSegmentsAdapter();
        List<Integer> videos = new ArrayList<>();
        videos.add(10);
        videos.add(20);
        videos.add(5);
        videos.add(7);
        videos.add(12);
        videos.add(45);
        mAdapter.setVideos(videos);


        //mRecyclerView.addItemDecoration(new OffsetDecoration());
        mRecyclerView.setAdapter(mAdapter);

        ((SeekBar)findViewById(R.id.seekBar)).setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mAdapter.setPxPerSecond(progress+1);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


    }

    public void onTest1Click(View v){



        mAdapter.addVideo(1, 20);
        mRecyclerView.setTime(10);


    }

    public void onTest2Click(View v){
        mAdapter.removeVideo(1);
    }

    public void onTest3Click(View v){


        mAdapter.addVideo(6, 60);

        mRecyclerView.setTime(mAdapter.getTime(6));

    }

    public void onTest4Click(View v){

        final int oldTime = mRecyclerView.getTime();

        List<Integer> videos = new ArrayList<>();
        videos.add(10);
        videos.add(20);
        videos.add(5);
        videos.add(7);
        videos.add(12);
        videos.add(45);

        mAdapter.setVideos(videos);

        mRecyclerView.setTime(oldTime);

        //mRecyclerView.smoothSetTime(30);
    }

    public void onTest5Click(View v){


        List<Integer> videos = new ArrayList<>();
        videos.add(10);
        videos.add(25);
        videos.add(7);
        videos.add(12);
        videos.add(45);
        mAdapter.setVideos(videos);



        mRecyclerView.setTime(30);

    }
}
