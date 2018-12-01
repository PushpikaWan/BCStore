package com.bc.pushpika.bc_store;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bc.pushpika.bc_store.data_structures.EducationalDetail;
import com.bc.pushpika.bc_store.data_structures.OccupationalDetail;
import com.bc.pushpika.bc_store.data_structures.PersonalDetail;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class UserDataActivity extends AppCompatActivity {

    EditText personalNameField,personalAddressField,personalDOBField,personalIDNumberField;
    EditText personalEmailField,personalMobileField,personalHomeField;

    EditText educationalStartDateField, educationalIndexNumberField;
    EditText educationalEndDateField,educationalHigherStudiesField;

    EditText occupationalCompanyNameField,occupationalCompanyAddressField;
    EditText occupationalPhoneField,occupationalStartDateField;

    Spinner educationalStreamField,personalMarriedField,occupationalJobTitleField;

    Vibrator vibrator;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_data);

        personalNameField = findViewById(R.id.personalNameField);
        personalAddressField = findViewById(R.id.personalAddressField);
        personalDOBField = findViewById(R.id.personalDOBField);
        personalIDNumberField = findViewById(R.id.personalIDNumberField);
        personalEmailField = findViewById(R.id.personalEmailField);
        personalMobileField = findViewById(R.id.personalMobileField);
        personalHomeField = findViewById(R.id.personalHomeField);
        personalMarriedField = findViewById(R.id.personalMarriedField);

        educationalStreamField = findViewById(R.id.educationalStreamField);
        educationalIndexNumberField = findViewById(R.id.educationalIndexNumberField);
        educationalStartDateField = findViewById(R.id.educationalStartDateField);
        educationalEndDateField = findViewById(R.id.educationalEndDateField);
        educationalHigherStudiesField = findViewById(R.id.educationalHigherStudiesField);

        occupationalCompanyNameField = findViewById(R.id.occupationalCompanyNameField);
        occupationalCompanyAddressField = findViewById(R.id.occupationalCompanyAddressField);
        occupationalJobTitleField = findViewById(R.id.occupationalJobTitleField);
        occupationalPhoneField = findViewById(R.id.occupationalPhoneField);
        occupationalStartDateField = findViewById(R.id.occupationalStartDateField);

        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        progressDialog = new ProgressDialog(this);

        setDataPickerListener(personalDOBField);

        personalEmailField.setText(LoginActivity.userEmail);
        personalEmailField.setEnabled(false);
        personalEmailField.setFocusable(false);
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

                DatePickerDialog datePickerDialog = new DatePickerDialog(UserDataActivity.this,
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
        personalDetail.setMarried(personalMarriedField.getSelectedItem().toString().trim());

        return personalDetail;
    }


    private boolean validateData() {

        //optional field
        //educationalIndexNumberField,personalHomeField,occupationalJobTitleField
        //occupationalPhoneField,educationalHigherStudiesField

        //add spinner validation
        if(!isNotValidStateSelected(personalMarriedField) ||
                !isNotValidStateSelected(educationalStreamField)){
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
