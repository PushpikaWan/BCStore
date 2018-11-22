package com.bc.pushpika.bc_store;

import android.content.Context;
import android.os.CountDownTimer;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bc.pushpika.bc_store.data_structures.FullDetail;

import java.util.ArrayList;
import java.util.List;


public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ReyclerViewHolder> {
    private LayoutInflater layoutInflater;
    private Animation animationUp, animationDown;
    private List<FullDetail> itemList;
    private Context context;
    private final int COUNTDOWN_RUNNING_TIME = 500;

    public RecyclerAdapter(Context context, Animation animationUp, Animation animationDown) {
        this.layoutInflater = LayoutInflater.from(context);
        this.animationDown = animationDown;
        this.animationUp = animationUp;
        this.context = context;
        itemList = new ArrayList();

        getDataFromDB();
    }

    private void getDataFromDB() {
        itemList.add(new FullDetail());
        itemList.add(new FullDetail());
        itemList.add(new FullDetail());
        itemList.add(new FullDetail());
    }

    @Override
    public ReyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View item = layoutInflater.inflate(R.layout.item_recycler_view, parent, false);
        return new ReyclerViewHolder(item);
    }

    @Override
    public void onBindViewHolder(final ReyclerViewHolder holder, int position) {

        //set relevant holder data
        if(position >= itemList.size()) return;

        holder.title.setText(itemList.get(position).getPersonalDetail().getName());
        holder.id.setText(itemList.get(position).getUserID());
        holder.contentLayout.setText(getContentText(itemList.get(position)));


        holder.showMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.contentLayout.isShown()) {
                    holder.contentLayout.startAnimation(animationUp);

                    CountDownTimer countDownTimerStatic = new CountDownTimer(COUNTDOWN_RUNNING_TIME, 16) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                        }

                        @Override
                        public void onFinish() {
                            holder.contentLayout.setVisibility(View.GONE);
                        }
                    };
                    countDownTimerStatic.start();

                    holder.showMore.setImageResource(R.drawable.arrow_down);
                } else {
                    holder.contentLayout.setVisibility(View.VISIBLE);
                    holder.contentLayout.startAnimation(animationDown);

                    holder.showMore.setImageResource(R.drawable.ic_account_box_white_24dp);
                }
            }
        });
    }

    private String getContentText(FullDetail fullDetail) {
     return fullDetail.getPersonalDetail().getAllPersonalDataText()+
     fullDetail.getEducationalDetail().getAllEducationalDataText()+
     fullDetail.getOccupationalDetail().getAllOccupationalDataText();
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    class ReyclerViewHolder extends RecyclerView.ViewHolder {
  //      private ImageView image;
        private TextView id; //hidden field to get id of displayed user data
        private TextView title;
        private ImageButton showMore;
        private TextView contentLayout;

        private ReyclerViewHolder(final View v) {
            super(v);

    //        image = (ImageView) v.findViewById(R.id.image);
            id = v.findViewById(R.id.id_hidden);
            title = v.findViewById(R.id.title);
            contentLayout = v.findViewById(R.id.content);
            showMore = v.findViewById(R.id.show_more);
        }
    }
}