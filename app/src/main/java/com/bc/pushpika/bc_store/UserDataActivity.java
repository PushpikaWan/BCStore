package com.bc.pushpika.bc_store;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import com.bc.pushpika.bc_store.data_structures.EducationalDetail;
import com.bc.pushpika.bc_store.data_structures.FullDetail;
import com.bc.pushpika.bc_store.data_structures.OccupationalDetail;
import com.bc.pushpika.bc_store.data_structures.PersonalDetail;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class UserDataActivity extends AppCompatActivity {

    EditText personalNameField,personalAddressField,personalDOBField;
    EditText personalEmailField,personalMobileField,personalHomeField;

    EditText educationalStreamField,educationalStartDateField;
    EditText educationalEndDateField,educationalHigherStudiesField;

    EditText occupationalCompanyNameField,occupationalCompanyAddressField;
    EditText occupationalJobTitleField,occupationalPhoneField;
    EditText occupationalStartDateField;

    Vibrator vibrator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_data);

        personalNameField = findViewById(R.id.personalNameField);
        personalAddressField = findViewById(R.id.personalAddressField);
        personalDOBField = findViewById(R.id.personalDOBField);
        personalEmailField = findViewById(R.id.personalEmailField);
        personalMobileField = findViewById(R.id.personalMobileField);
        personalHomeField = findViewById(R.id.personalHomeField);

        educationalStreamField = findViewById(R.id.educationalStreamField);
        educationalStartDateField = findViewById(R.id.educationalStartDateField);
        educationalEndDateField = findViewById(R.id.educationalEndDateField);
        educationalHigherStudiesField = findViewById(R.id.educationalHigherStudiesField);

        occupationalCompanyNameField = findViewById(R.id.occupationalCompanyNameField);
        occupationalCompanyAddressField = findViewById(R.id.occupationalCompanyAddressField);
        occupationalJobTitleField = findViewById(R.id.occupationalJobTitleField);
        occupationalPhoneField = findViewById(R.id.occupationalPhoneField);
        occupationalStartDateField = findViewById(R.id.occupationalStartDateField);

        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

    }

    public void submitData(View view){
        if(!validateData()){
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

        editor.putBoolean("isDataSubmitted",true);
        editor.apply();

        startActivity(new Intent(getApplicationContext(),MainActivity.class));
        finish();

    }

    private OccupationalDetail getCurrentOccupationalDetails() {

        OccupationalDetail occupationalDetail = new OccupationalDetail();

        occupationalDetail.setCompanyName(occupationalCompanyNameField.getText().toString().trim());
        occupationalDetail.setCompanyAddress(occupationalCompanyAddressField.getText().toString().trim());
        occupationalDetail.setJobTitle(occupationalJobTitleField.getText().toString().trim());
        occupationalDetail.setPhone(occupationalPhoneField.getText().toString().trim());
        occupationalDetail.setStartDate(occupationalStartDateField.getText().toString().trim());

        return occupationalDetail;
    }

    private EducationalDetail getCurrentEducationalDetails() {

        EducationalDetail educationalDetail = new EducationalDetail();

        educationalDetail.setStream(educationalStreamField.getText().toString().trim());
        educationalDetail.setStartDate(educationalStartDateField.getText().toString().trim());
        educationalDetail.setEndDate(educationalEndDateField.getText().toString().trim());
        educationalDetail.setHigherStudies(educationalHigherStudiesField.getText().toString().trim());

        return educationalDetail;
    }


    private PersonalDetail getCurrentPersonalDetails() {

        PersonalDetail personalDetail = new PersonalDetail();
        personalDetail.setName(personalNameField.getText().toString().trim());
        personalDetail.setAddress(personalAddressField.getText().toString().trim());
        personalDetail.setdOB(personalDOBField.getText().toString().trim());
        personalDetail.setEmailAddress(personalEmailField.getText().toString().trim());
        personalDetail.setMobile(personalMobileField.getText().toString().trim());
        personalDetail.setHome(personalHomeField.getText().toString().trim());

        return personalDetail;
    }


    private boolean validateData() {

        if(!isNotEmptyField(personalNameField) || !isNotEmptyField(personalAddressField) ||
                !isNotEmptyField(personalDOBField) ||!isNotEmptyField(personalEmailField)){
            return false;
        }

        if(!isNotEmptyField(educationalStreamField) || !isNotEmptyField(educationalStartDateField) ||
                !isNotEmptyField(educationalEndDateField) ||!isNotEmptyField(educationalHigherStudiesField)){
            return false;
        }

        if(!isNotEmptyField(occupationalCompanyNameField) || !isNotEmptyField(occupationalCompanyAddressField) ||
                !isNotEmptyField(occupationalJobTitleField) ||!isNotEmptyField(occupationalStartDateField)){
            return false;
        }

        if(!isNotEmptyField(personalNameField) || !isNotEmptyField(personalAddressField) ||
                !isNotEmptyField(personalDOBField) ||!isNotEmptyField(personalEmailField)){
            return false;
        }

       if(personalMobileField.getText().toString().trim().length() != 10){
           vibrator.vibrate(100);
           personalMobileField.setError("should be 10 character number");
           return false;
       }

       if(personalHomeField.getText().toString().trim().length() != 10){
            vibrator.vibrate(100);
            personalHomeField.setError("should be 10 character number");
            return false;
        }

        if(occupationalPhoneField.getText().toString().trim().length() != 10){
            vibrator.vibrate(100);
            occupationalPhoneField.setError("should be 10 character number");
            return false;
        }

//       if(alYearField.getText().toString().trim().length() != 4){
//           vibrator.vibrate(100);
//           alYearField.setError("should be valid year");
//           return false;
//       }

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
