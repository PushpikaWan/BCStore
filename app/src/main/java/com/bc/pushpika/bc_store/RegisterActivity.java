package com.bc.pushpika.bc_store;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class RegisterActivity extends AppCompatActivity {

    EditText emailField, passwordField, verifyPasswordField;
    Vibrator vibrator;
    FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        emailField = findViewById(R.id.emailField);
        passwordField = (findViewById(R.id.passwordField));
        verifyPasswordField = findViewById(R.id.verifyPasswordField);

        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        firebaseAuth = FirebaseAuth.getInstance();
    }

    public void register(View view){
        if(!validateData()){
            return;
        }
         registerUser();
    }

    private void registerUser() {

        //first getting the values
        final String emailAddress = emailField.getText().toString();
        final String password = passwordField.getText().toString();

        firebaseAuth.createUserWithEmailAndPassword(emailAddress,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            doAfterRegistration();
                            startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                            finish();
                        }
                        else{
                            vibrator.vibrate(100);
                            Toast.makeText(getApplicationContext(),"Registration failed"+task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                            Log.d("login error:",task.getException().getMessage());
                        }
                    }
                });

    }

    private void doAfterRegistration() {

        FirebaseUser firebaseUser =  firebaseAuth.getCurrentUser();
        if( firebaseUser != null){
            SharedPreferences.Editor editor = getSharedPreferences("MY_PREF", MODE_PRIVATE).edit();
            editor.putString("userID", firebaseUser.getUid());
            editor.putString("emailAddress", firebaseUser.getEmail());
            editor.putBoolean("isDataSubmitted",false);
            editor.apply();

            addInitialDataToDB(firebaseUser.getUid());
        }
        else{
            vibrator.vibrate(100);
            Toast.makeText(getApplicationContext(),"Registration failed",Toast.LENGTH_SHORT).show();
        }

    }

    private void addInitialDataToDB(String uid) {
        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("UserStatus");

        DatabaseReference ref = myRef.child(uid).child("isUserVerified");
        ref.setValue(false);
    }

    private boolean validateData() {

        //check validity of email address
        if(!isValidEmail(emailField.getText().toString())){
            emailField.setError("Invalid email address");
            emailField.requestFocus();
            vibrator.vibrate(100);
            return false;
        }

        //match two passwords
        if(!passwordField.getText().toString().
                equals(verifyPasswordField.getText().toString())){
            verifyPasswordField.setError("Passwords mismatch");
            verifyPasswordField.requestFocus();
            vibrator.vibrate(100);
            return false;
        }

        //check length of password
        if(passwordField.getText().toString().length() < 6){
            passwordField.setError("Password must contain at least six characters");
            passwordField.requestFocus();
            vibrator.vibrate(100);
            return false;
        }
        return true;
    }

    public static boolean isValidEmail(String email) {
        return (!TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches());
    }

}
