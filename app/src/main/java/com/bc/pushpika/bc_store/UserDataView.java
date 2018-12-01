package com.bc.pushpika.bc_store;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bc.pushpika.bc_store.data_structures.EducationalDetail;
import com.bc.pushpika.bc_store.data_structures.OccupationalDetail;
import com.bc.pushpika.bc_store.data_structures.PersonalDetail;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;
import java.util.Calendar;

import static com.bc.pushpika.bc_store.LoginActivity.userId;

public class UserDataView extends AppCompatActivity {

    EditText personalNameField,personalAddressField,personalDOBField,personalIDNumberField;
    EditText personalEmailField,personalMobileField,personalHomeField,personalNumberOfChildren;

    EditText educationalStartDateField, educationalIndexNumberField;
    EditText educationalEndDateField,educationalHigherStudiesField;

    EditText occupationalCompanyNameField,occupationalCompanyAddressField;
    EditText occupationalPhoneField,occupationalStartDateField;

    Spinner educationalStreamField,occupationalJobTitleField;
    TextInputLayout personalNumberOfChildrenView;

    Button submitButton;

    Switch personalMarriedField;

    Vibrator vibrator;

    private ProgressDialog progressDialog;

  @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_data_view);

        personalNameField = findViewById(R.id.personalNameField);
        disableField(personalNameField);
        personalAddressField = findViewById(R.id.personalAddressField);
        disableField(personalAddressField);
        personalDOBField = findViewById(R.id.personalDOBField);
        disableField(personalDOBField);
        personalIDNumberField = findViewById(R.id.personalIDNumberField);
        disableField(personalIDNumberField);
        personalEmailField = findViewById(R.id.personalEmailField);
        disableField(personalEmailField);
        personalMobileField = findViewById(R.id.personalMobileField);
        disableField(personalMobileField);
        personalHomeField = findViewById(R.id.personalHomeField);
        disableField(personalHomeField);
        personalMarriedField = findViewById(R.id.personalMarriedField);
      //  disableField(personalMarriedField);
        personalNumberOfChildren = findViewById(R.id.personalNumberOfChildren);
        disableField(personalNumberOfChildren);
        personalNumberOfChildrenView = findViewById(R.id.personalNumberOfChildrenView);
      //  disableField(personalNumberOfChildrenView);

        educationalStreamField = findViewById(R.id.educationalStreamField);
       // disableField(educationalStreamField);
        educationalIndexNumberField = findViewById(R.id.educationalIndexNumberField);
        disableField(educationalIndexNumberField);
        educationalStartDateField = findViewById(R.id.educationalStartDateField);
        disableField(educationalStartDateField);
        educationalEndDateField = findViewById(R.id.educationalEndDateField);
        disableField(educationalEndDateField);
        educationalHigherStudiesField = findViewById(R.id.educationalHigherStudiesField);
        disableField(educationalHigherStudiesField);

        occupationalCompanyNameField = findViewById(R.id.occupationalCompanyNameField);
        disableField(occupationalCompanyNameField);
        occupationalCompanyAddressField = findViewById(R.id.occupationalCompanyAddressField);
        disableField(occupationalCompanyAddressField);
        occupationalJobTitleField = findViewById(R.id.occupationalJobTitleField);
        //disableField(occupationalJobTitleField);
        occupationalPhoneField = findViewById(R.id.occupationalPhoneField);
        disableField(occupationalPhoneField);
        occupationalStartDateField = findViewById(R.id.occupationalStartDateField);
        disableField(occupationalStartDateField);

        submitButton = findViewById(R.id.submitButton);

        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        progressDialog = new ProgressDialog(this);

        setDataPickerListener(personalDOBField);

        personalEmailField.setText(LoginActivity.userEmail);
        personalEmailField.setEnabled(false);
        personalEmailField.setFocusable(false);

        personalNumberOfChildrenView.setVisibility(View.GONE);

        personalMarriedField.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
               if(isChecked){
                   personalNumberOfChildrenView.setVisibility(View.VISIBLE);
               }
               else{
                   personalNumberOfChildrenView.setVisibility(View.GONE);
               }
            }
        });

      personalMarriedField.setEnabled(false);
      occupationalJobTitleField.setEnabled(false);
      educationalStreamField.setEnabled(false);


      submitButton.setVisibility(View.GONE);
      submitButton.setEnabled(false);

      getDataFromDB();

    }

    private void disableField(EditText field) {
        field.setEnabled(false);
        field.setFocusable(false);
    }

    private void getDataFromDB() {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("UserData");

        myRef.child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                progressDialog.setMessage("Data loading......");
                progressDialog.setCancelable(false);
                progressDialog.show();

                PersonalDetail personalDetail = dataSnapshot.child("personalDetails").getValue(PersonalDetail.class);
                EducationalDetail educationalDetail = dataSnapshot.child("educationalDetails").getValue(EducationalDetail.class);
                OccupationalDetail occupationalDetail = dataSnapshot.child("occupationalDetails").getValue(OccupationalDetail.class);

                setPersonalDetails(personalDetail);
                setEducationalDetails(educationalDetail);
                setOccupationalDetails(occupationalDetail);

                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });
    }

    private void setOccupationalDetails(OccupationalDetail occupationalDetail) {

        occupationalCompanyNameField.setText(occupationalDetail.getCompanyName());
        occupationalCompanyAddressField.setText(occupationalDetail.getCompanyAddress());
        occupationalPhoneField.setText(occupationalDetail.getPhone());
        occupationalStartDateField.setText(occupationalDetail.getStartYear());

        int spinnerIndex = Arrays.asList(getResources().getStringArray(R.array.jobTitle_fields))
                .indexOf(occupationalDetail.getJobTitle());

        occupationalJobTitleField.setSelection( spinnerIndex < 0 ? 0:spinnerIndex);


    }

    private void setEducationalDetails(EducationalDetail educationalDetail) {

        educationalStartDateField.setText(educationalDetail.getStartYear());
        educationalIndexNumberField.setText(educationalDetail.getIndexNumber());
        educationalEndDateField.setText(educationalDetail.getEndYear());
        educationalHigherStudiesField.setText(educationalDetail.getHigherStudies());

        int spinnerIndex = Arrays.asList(getResources().getStringArray(R.array.stream_fields))
                .indexOf(educationalDetail.getStream());

        educationalStreamField.setSelection( spinnerIndex < 0 ? 0:spinnerIndex);

    }

    private void setPersonalDetails(PersonalDetail personalDetail) {

        personalNameField.setText(personalDetail.getName());
        personalAddressField.setText(personalDetail.getAddress());
        personalDOBField.setText(personalDetail.getdOB());
        personalIDNumberField.setText(personalDetail.getIdNumber());
        personalMobileField.setText(personalDetail.getMobile());
        personalHomeField.setText(personalDetail.getHome());
        personalMarriedField.setChecked(personalDetail.getMarried().equalsIgnoreCase("yes"));
        personalNumberOfChildren.setText(personalDetail.getNumberOfChildren());

    }


    private void setDataPickerListener(final EditText editText){

        final Calendar calendar = Calendar.getInstance();

        editText.setOnClickListener(new View.OnClickListener() {
            int selectedYear = calendar.get(Calendar.YEAR);
            int selectedMonth = calendar.get(Calendar.MONTH);
            int selectedDay = calendar.get(Calendar.DAY_OF_MONTH);

            @Override
            public void onClick(View v) {

                personalDOBField.setError(null);

                DatePickerDialog datePickerDialog = new DatePickerDialog(UserDataView.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                selectedDay = day;
                                selectedMonth = month;
                                selectedYear = year;
                                String fullDate = selectedDay + "/" + (selectedMonth+1) + "/" + selectedYear;
                                editText.setText(fullDate);
                            }
                        }, selectedYear, selectedMonth, selectedDay);

                datePickerDialog.show();
            }
        });

    }

    public void submitData(View view){

        progressDialog.setMessage("Data submitting ......");
        progressDialog.setCancelable(false);
        progressDialog.show();

        if(!validateData()){
            progressDialog.dismiss();
            return;
        }

        submitUserData();
    }


    private void submitUserData() {

        //first getting the values
        SharedPreferences.Editor editor = getSharedPreferences("MY_PREF", MODE_PRIVATE).edit();
        SharedPreferences prefs = getSharedPreferences("MY_PREF", MODE_PRIVATE);

        PersonalDetail personalDetail = getCurrentPersonalDetails();
        EducationalDetail educationalDetail = getCurrentEducationalDetails();
        OccupationalDetail occupationalDetail = getCurrentOccupationalDetails();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("UserData");

        String uId = prefs.getString("userID", "def");

        DatabaseReference ref = myRef.child(uId);

        ref.child("personalDetails").setValue(personalDetail);
        ref.child("educationalDetails").setValue(educationalDetail);
        ref.child("occupationalDetails").setValue(occupationalDetail);

        //set this only for first time prevent from updates
        if(!prefs.getBoolean("isDataSubmitted",false)){
            ref.child("isUserVerified").setValue(false);
        }

        editor.putBoolean("isDataSubmitted",true);
        editor.apply();

        FirebaseDatabase db = FirebaseDatabase.getInstance();
        database.getReference("UserStatus").child(uId).child("isDataSubmitted").setValue("true");

        progressDialog.dismiss();

        FirebaseAuth.getInstance().signOut();
        Toast.makeText(getApplicationContext(),"Please,log in again..",Toast.LENGTH_SHORT).show();
        finish();
        startActivity(new Intent(getApplicationContext(),LoginActivity.class));

    }

    private OccupationalDetail getCurrentOccupationalDetails() {

        OccupationalDetail occupationalDetail = new OccupationalDetail();

        occupationalDetail.setCompanyName(occupationalCompanyNameField.getText().toString().trim());
        occupationalDetail.setCompanyAddress(occupationalCompanyAddressField.getText().toString().trim());
        occupationalDetail.setJobTitle(occupationalJobTitleField.getSelectedItemId()==0? "":occupationalJobTitleField.getSelectedItem().toString());
        occupationalDetail.setPhone(occupationalPhoneField.getText().toString().trim());
        occupationalDetail.setStartYear(occupationalStartDateField.getText().toString().trim());

        return occupationalDetail;
    }

    private EducationalDetail getCurrentEducationalDetails() {

        EducationalDetail educationalDetail = new EducationalDetail();

        educationalDetail.setStream(educationalStreamField.getSelectedItem().toString().trim());
        educationalDetail.setIndexNumber(educationalIndexNumberField.getText().toString().trim());
        educationalDetail.setStartYear(educationalStartDateField.getText().toString().trim());
        educationalDetail.setEndYear(educationalEndDateField.getText().toString().trim());
        educationalDetail.setHigherStudies(educationalHigherStudiesField.getText().toString().trim());

        return educationalDetail;
    }


    private PersonalDetail getCurrentPersonalDetails() {

        PersonalDetail personalDetail = new PersonalDetail();
        personalDetail.setName(personalNameField.getText().toString().trim());
        personalDetail.setAddress(personalAddressField.getText().toString().trim());
        personalDetail.setdOB(personalDOBField.getText().toString().trim());
        personalDetail.setIdNumber(personalIDNumberField.getText().toString().trim());
        personalDetail.setEmailAddress(personalEmailField.getText().toString().trim());
        personalDetail.setMobile(personalMobileField.getText().toString().trim());
        personalDetail.setHome(personalHomeField.getText().toString().trim());
        personalDetail.setMarried(personalMarriedField.isChecked()? "yes":"No");
        personalDetail.setNumberOfChildren(personalNumberOfChildren.getText().toString().trim());

        return personalDetail;
    }


    private boolean validateData() {

        //optional field
        //educationalIndexNumberField,personalHomeField,occupationalJobTitleField
        //occupationalPhoneField,educationalHigherStudiesField

        //add spinner validation
        if(!isNotValidStateSelected(educationalStreamField)){
            return false;
        }

        if(!isNotEmptyField(personalNameField) || !isNotEmptyField(personalAddressField) ||
                !isNotEmptyField(personalDOBField)){
            return false;
        }

        if(!isNotEmptyField(occupationalCompanyNameField) || !isNotEmptyField(occupationalCompanyAddressField)){
            return false;
        }

        if(!isNotEmptyField(personalNameField) || !isNotEmptyField(personalAddressField) ||
                !isNotEmptyField(personalDOBField) ||!isNotEmptyField(personalEmailField)){
            return false;
        }

        if(!isValidYear(educationalStartDateField) || !isValidYear(occupationalStartDateField) ||
                !isValidYear(educationalEndDateField)){
            return false;
        }

        if(personalIDNumberField.getText().toString().trim().length() != 10){
            vibrator.vibrate(100);
            personalIDNumberField.setError("should have 10 character");
            personalIDNumberField.requestFocus();
            return false;
        }

        if(personalMobileField.getText().toString().trim().length() != 10){
            vibrator.vibrate(100);
            personalMobileField.setError("should be 10 character number");
            personalMobileField.requestFocus();
            return false;
        }

        if(!personalHomeField.getText().toString().trim().isEmpty()
                && personalHomeField.getText().toString().trim().length() != 10){
            vibrator.vibrate(100);
            personalHomeField.setError("should be 10 character number");
            personalHomeField.requestFocus();
            return false;
        }

        if(!occupationalPhoneField.getText().toString().trim().isEmpty()
                && occupationalPhoneField.getText().toString().trim().length() != 10){
            vibrator.vibrate(100);
            occupationalPhoneField.setError("should be 10 character number");
            occupationalPhoneField.requestFocus();
            return false;
        }

//       if(alYearField.getText().toString().trim().length() != 4){
//           vibrator.vibrate(100);
//           alYearField.setError("should be valid year");
//           return false;
//       }

        return true;
    }

    private boolean isValidYear(EditText field){
        if(field.getText().toString().trim().length()== 4){
            return true;
        }

        field.setError("Invalid Year");
        field.requestFocus();
        vibrator.vibrate(100);
        return false;
    }

    private boolean isNotValidStateSelected(Spinner spinner){
        if(spinner.getSelectedItemId() == 0){
            ((TextView)spinner.getSelectedView()).setError("set valid item");
            spinner.requestFocus();
            vibrator.vibrate(100);
            return false;
        }
        return true;
    }

    private boolean isNotEmptyField(EditText field){
        if(!field.getText().toString().trim().isEmpty()){
            return true;
        }

        field.setError("Invalid input");
        field.requestFocus();
        vibrator.vibrate(100);
        return false;
    }


}
