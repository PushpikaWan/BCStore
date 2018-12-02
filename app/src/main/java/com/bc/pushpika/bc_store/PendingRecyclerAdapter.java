package com.bc.pushpika.bc_store;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.alespero.expandablecardview.ExpandableCardView;
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
    private ProgressDialog progressDialog;

    public PendingRecyclerAdapter(Context context, Animation animationUp, Animation animationDown) {

        itemList = new ArrayList();
        getDataFromDB();

        this.layoutInflater = LayoutInflater.from(context);
        this.animationDown = animationDown;
        this.animationUp = animationUp;
        this.context = context;

        progressDialog = new ProgressDialog(this.context);
    }

    private void getDataFromDB() {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference("UserData");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(((Activity) context).isFinishing())
                {
                    myRef.removeEventListener(this);
                    Log.e("PendinRecyclefirebase","fired and removed");
                }

//                progressDialog.setMessage("Data loading......");
//                progressDialog.setCancelable(false);
//                progressDialog.show();

                Log.e("PendinRecycle Listener","fired");
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
                                isVerified,messageSnapshot.getKey()));
                    }

                }

                PendingRecyclerAdapter.super.notifyDataSetChanged();
                progressDialog.dismiss();
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

        final String requesterId = itemList.get(position).getUserID();

        holder.title.setText(itemList.get(position).getPersonalDetail().getName());
        holder.id.setText(requesterId);

        holder.personalDataCard.setText(Html.fromHtml((itemList.get(position)).getPersonalDetail().AllPersonalDataText()));
        holder.educationalDataCard.setText(Html.fromHtml((itemList.get(position)).getEducationalDetail().AllEducationalDataText()));
        holder.occupationalDataCard.setText(Html.fromHtml((itemList.get(position)).getOccupationalDetail().AllOccupationalDataText()));

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

                                        DatabaseReference ref1 = myRef1.child(requesterId).child("isUserVerified");
                                        DatabaseReference ref2 = myRef2.child(requesterId).child("isUserVerified");
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
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    class PendingReyclerViewHolder extends RecyclerView.ViewHolder {
        //      private ImageView image;
        private TextView id; //hidden field to get id of displayed user data
        private TextView title;
        private TextView personalDataCard;
        private TextView educationalDataCard;
        private TextView occupationalDataCard;
        private Button approveButton;
        private ExpandableCardView expandableCardView;
        private ExpandableCardView personalDataView,educationalDataView,occupationalDataView;

        private PendingReyclerViewHolder(final View v) {
            super(v);

            //        image = (ImageView) v.findViewById(R.id.image);
            expandableCardView = v.findViewById(R.id.profile);
            approveButton = v.findViewById(R.id.approve_btn);
            id = v.findViewById(R.id.id_hidden);
            title = v.findViewById(R.id.title);
            personalDataCard = v.findViewById(R.id.personalDataCard);
            educationalDataCard = v.findViewById(R.id.educationalDataCard);
            occupationalDataCard = v.findViewById(R.id.occupationalDataCard);

            personalDataView = v.findViewById(R.id.personalDataView);
            educationalDataView = v.findViewById(R.id.educationalDataView);
            occupationalDataView = v.findViewById(R.id.occupationalDataView);

            personalDataView.setOnExpandedListener(new ExpandableCardView.OnExpandedListener() {
                @Override
                public void onExpandChanged(View v, boolean isExpanded) {
                    expandableCardView.expand();
                }
            });

            educationalDataView.setOnExpandedListener(new ExpandableCardView.OnExpandedListener() {
                @Override
                public void onExpandChanged(View v, boolean isExpanded) {
                    expandableCardView.expand();
                }
            });

            occupationalDataView.setOnExpandedListener(new ExpandableCardView.OnExpandedListener() {
                @Override
                public void onExpandChanged(View v, boolean isExpanded) {
                    expandableCardView.expand();
                }
            });
        }
    }
}