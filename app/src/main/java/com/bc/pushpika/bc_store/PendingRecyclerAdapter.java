package com.bc.pushpika.bc_store;

import android.content.Context;
import android.content.DialogInterface;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bc.pushpika.bc_store.data_structures.EducationalDetail;
import com.bc.pushpika.bc_store.data_structures.FullDetail;
import com.bc.pushpika.bc_store.data_structures.OccupationalDetail;
import com.bc.pushpika.bc_store.data_structures.PersonalDetail;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class PendingRecyclerAdapter extends RecyclerView.Adapter<PendingRecyclerAdapter.PendingReyclerViewHolder> {
    private LayoutInflater layoutInflater;
    private Animation animationUp, animationDown;
    private List<FullDetail> itemList;
    private Context context;
    private final int COUNTDOWN_RUNNING_TIME = 500;

    public PendingRecyclerAdapter(Context context, Animation animationUp, Animation animationDown) {

        itemList = new ArrayList();
        getDataFromDB();

        this.layoutInflater = LayoutInflater.from(context);
        this.animationDown = animationDown;
        this.animationUp = animationUp;
        this.context = context;
    }

    private void getDataFromDB() {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("UserData");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                itemList.clear();
                for (DataSnapshot messageSnapshot: dataSnapshot.getChildren()) {

                 String isVerified =
                            messageSnapshot.child("isUserVerified").getValue() != null ?
                                    messageSnapshot.child("isUserVerified").getValue().toString(): "false";

                    if(isVerified.equals("false")){

                        PersonalDetail personalDetail = messageSnapshot.child("personalDetails").getValue(PersonalDetail.class);
                        EducationalDetail educationalDetail = messageSnapshot.child("educationalDetails").getValue(EducationalDetail.class);
                        OccupationalDetail occupationalDetail = messageSnapshot.child("occupationalDetails").getValue(OccupationalDetail.class);

                        itemList.add(new FullDetail(personalDetail,occupationalDetail,educationalDetail,
                                isVerified,dataSnapshot.getKey()));
                    }

                }

                PendingRecyclerAdapter.super.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });
    }

    @Override
    public PendingReyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View item = layoutInflater.inflate(R.layout.pending_item_recycler_view, parent, false);
        return new PendingReyclerViewHolder(item);
    }

    @Override
    public void onBindViewHolder(final PendingReyclerViewHolder holder, int position) {

        //set relevant holder data
        if(position >= itemList.size()) return;

        holder.title.setText(itemList.get(position).getPersonalDetail().getName());
        holder.id.setText(itemList.get(position).getUserID());
        holder.contentLayout.setText(Html.fromHtml(getContentText(itemList.get(position))));

        holder.approveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder.setMessage("Are you sure, You want to approve this request");
                        alertDialogBuilder.setPositiveButton("yes",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface arg0, int arg1) {

                                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                                        DatabaseReference myRef1 = database.getReference("UserData");
                                        DatabaseReference myRef2 = database.getReference("UserStatus");

                                        DatabaseReference ref1 = myRef1.child(LoginActivity.userId).child("isUserVerified");
                                        DatabaseReference ref2 = myRef1.child(LoginActivity.userId).child("isUserVerified");
                                        ref1.setValue("true");
                                        ref2.setValue("true");
                                    }
                                });

                alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();

            }
        });

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
                    holder.showMore.setImageResource(R.drawable.arrow_up_black_24dp);
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

    class PendingReyclerViewHolder extends RecyclerView.ViewHolder {
        //      private ImageView image;
        private TextView id; //hidden field to get id of displayed user data
        private TextView title;
        private ImageButton showMore;
        private TextView contentLayout;
        private Button approveButton;

        private PendingReyclerViewHolder(final View v) {
            super(v);

            //        image = (ImageView) v.findViewById(R.id.image);
            approveButton = v.findViewById(R.id.approve_btn);
            id = v.findViewById(R.id.id_hidden);
            title = v.findViewById(R.id.title);
            contentLayout = v.findViewById(R.id.content);
            showMore = v.findViewById(R.id.show_more);
        }
    }
}