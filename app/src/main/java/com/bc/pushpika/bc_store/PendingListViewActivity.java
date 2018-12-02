package com.bc.pushpika.bc_store;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

public class PendingListViewActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private Animation animationUp, animationDown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_list_view);

        recyclerView = findViewById(R.id.recycler_view);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        animationUp = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_up);
        animationDown = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_down);

        PendingRecyclerAdapter pendingRecyclerAdapter = new PendingRecyclerAdapter(this, animationUp, animationDown);
        recyclerView.setAdapter(pendingRecyclerAdapter);
    }

}

