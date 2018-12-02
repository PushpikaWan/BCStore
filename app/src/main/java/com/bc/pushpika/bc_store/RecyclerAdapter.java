package com.bc.pushpika.bc_store;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.alespero.expandablecardview.ExpandableCardView;
import com.bc.pushpika.bc_store.data_structures.EducationalDetail;
import com.bc.pushpika.bc_store.data_structures.FullDetail;
import com.bc.pushpika.bc_store.data_structures.OccupationalDetail;
import com.bc.pushpika.bc_store.data_structures.PersonalDetail;
import com.google.firebase.FirebaseError;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ReyclerViewHolder> {
    private LayoutInflater layoutInflater;
    private Animation animationUp, animationDown;
    private List<FullDetail> itemList,searchedList;
    private Context context;
    private final int COUNTDOWN_RUNNING_TIME = 500;
    private boolean isSearching =false;
    private ProgressDialog progressDialog;
    private Spinner spinner;
    private EditText searchTextField;

    public RecyclerAdapter(Context context, Animation animationUp, Animation animationDown,
                           final EditText searchTextField, ImageButton searchButton, final Spinner spinner) {

        itemList = new ArrayList();
        searchedList = new ArrayList();
        getDataFromDB();

        this.layoutInflater = LayoutInflater.from(context);
        this.animationDown = animationDown;
        this.animationUp = animationUp;
        this.context = context;
        this.searchTextField = searchTextField;
        this.spinner = spinner;

        progressDialog = new ProgressDialog(this.context);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setMessage("Searching Data......");
                progressDialog.setCancelable(false);
                progressDialog.show();
                isSearching = true;
                searchData(spinner.getSelectedItem().toString(),searchTextField.getText().toString());
            }
        });
    }

    private void searchData(String field, String text) {

        searchedList.clear();

        for (FullDetail element : itemList) {
           if(field.equals("Name") && element.getPersonalDetail().getName().toLowerCase().contains(text.toLowerCase())){
               searchedList.add(element);
           }
           else if (field.equals("Address") && element.getPersonalDetail().getAddress().toLowerCase().contains(text.toLowerCase())){
                searchedList.add(element);
            }
           else if (field.equals("A/L Stream") && element.getEducationalDetail().getStream().toLowerCase().contains(text.toLowerCase())){
               searchedList.add(element);
           }
           else if (field.equals("Company Name") && element.getOccupationalDetail().getCompanyName().toLowerCase().contains(text.toLowerCase())){
               searchedList.add(element);
           }
           //performance issue... else part go with first time
//           else{
//               searchedList.add(element);
//           }
        }

        RecyclerAdapter.super.notifyDataSetChanged();

        isSearching = false;

        progressDialog.dismiss();
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
                    Log.e("RecycleListenerfirebase","fired and removed");
                }

                if(isSearching){
                    return;
                }
//                progressDialog.setMessage("Searching data......");
//                progressDialog.setCancelable(false);
                Log.e("RecycleListenerfirebase","fired");
                itemList.clear();
                for (DataSnapshot messageSnapshot: dataSnapshot.getChildren()) {

                    PersonalDetail personalDetail = messageSnapshot.child("personalDetails").getValue(PersonalDetail.class);
                    EducationalDetail educationalDetail = messageSnapshot.child("educationalDetails").getValue(EducationalDetail.class);
                    OccupationalDetail occupationalDetail = messageSnapshot.child("occupationalDetails").getValue(OccupationalDetail.class);

                    if(messageSnapshot.child("isUserVerified").getValue()!= null
                            && messageSnapshot.child("isUserVerified").getValue().toString().equals("true")){
                        itemList.add(new FullDetail(personalDetail,occupationalDetail,educationalDetail,
                                "true",dataSnapshot.getKey()));
                    }

                }

//refresh occur when search clicked
//                RecyclerAdapter.super.notifyDataSetChanged();
                searchData(spinner.getSelectedItem().toString(),searchTextField.getText().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });
    }

    @Override
    public ReyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View item = layoutInflater.inflate(R.layout.item_recycler_view, parent, false);
        return new ReyclerViewHolder(item);
    }

    @Override
    public void onBindViewHolder(final ReyclerViewHolder holder, int position) {

        //set relevant holder data
        if(position >= searchedList.size()) return;

        holder.title.setText(searchedList.get(position).getPersonalDetail().getName());
        holder.id.setText(searchedList.get(position).getUserID());

        holder.personalDataCard.setText(Html.fromHtml((searchedList.get(position)).getPersonalDetail().AllPersonalDataText()));
        holder.educationalDataCard.setText(Html.fromHtml((searchedList.get(position)).getEducationalDetail().AllEducationalDataText()));
        holder.occupationalDataCard.setText(Html.fromHtml((searchedList.get(position)).getOccupationalDetail().AllOccupationalDataText()));


    }

    private String getContentText(FullDetail fullDetail) {
     return fullDetail.getPersonalDetail().AllPersonalDataText()+
     fullDetail.getEducationalDetail().AllEducationalDataText()+
     fullDetail.getOccupationalDetail().AllOccupationalDataText();
    }

    @Override
    public int getItemCount() {
        return searchedList.size();
    }

    class ReyclerViewHolder extends RecyclerView.ViewHolder {
        private TextView id; //hidden field to get id of displayed user data
        private TextView title;
        private TextView personalDataCard;
        private TextView educationalDataCard;
        private TextView occupationalDataCard;
        private ExpandableCardView expandableCardView;
        private ExpandableCardView personalDataView,educationalDataView,occupationalDataView;

        private ReyclerViewHolder(final View v) {
            super(v);

            //        image = (ImageView) v.findViewById(R.id.image);
            expandableCardView = v.findViewById(R.id.profile);
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