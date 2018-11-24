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

import com.bc.pushpika.bc_store.data_structures.PersonalDetail;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    EditText emailField, passwordField;
    Vibrator vibrator;

    FirebaseAuth firebaseAuth;

    //move these two to shared prefereneces
    public static String userId;
    public static boolean isAdmin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userId = "";
        isAdmin = false;

        emailField = findViewById(R.id.emailField);
        passwordField = findViewById(R.id.passwordField);

        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        firebaseAuth = FirebaseAuth.getInstance();

        checkUserLoggedIn();

    }

    private void checkUserLoggedIn() {

        //FirebaseAuth.getInstance().signOut();

        if(firebaseAuth.getCurrentUser()!=null){

//            startActivity(new Intent(getApplicationContext(),UserDataActivity.class));
            checkUserStateAndStartActivity();
        }

    }

    private void checkAdminPermissions(){

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("AdminData");


        myRef.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        //Get map of users in datasnapshot
                        for (DataSnapshot messageSnapshot: dataSnapshot.getChildren()) {

                            if( messageSnapshot.getKey().equals(userId)){
                                isAdmin = true;
                            }
                        }
                        nevigateToNextPageAfterCheckAdmin();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        //handle databaseError
                    }
                });

    }


    //this method is duplicated in register and login due to safe delete prefs
    private void checkUserStateAndStartActivity() {
        FirebaseUser firebaseUser =  firebaseAuth.getCurrentUser();
        if( firebaseUser != null){
            SharedPreferences.Editor editor = getSharedPreferences("MY_PREF", MODE_PRIVATE).edit();

            editor.putString("userID", firebaseUser.getUid());
            editor.putString("emailAddress", firebaseUser.getEmail());

            userId = firebaseUser.getUid();

            editor.apply();
        }

        checkAdminPermissions();
    }

    private void nevigateToNextPageAfterCheckAdmin() {

        SharedPreferences prefs = getSharedPreferences("MY_PREF", MODE_PRIVATE);

        if(!prefs.getBoolean("isDataSubmitted", false)){
            finish();
            startActivity(new Intent(getApplicationContext(),UserDataActivity.class));
        }
        else{
            finish();
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
        }
    }

    public void goRegistration(View view){

        Intent intent = new Intent(this,RegisterActivity.class);
        startActivity(intent);

    }

    public void login(View view){

        if(!validateData()){
            return;
        }

        loginUser();

    }

    private void loginUser() {

        //first getting the values
        final String emailAddress = emailField.getText().toString();
       // final String password = Utill.md5(passwordField.getText().toString());
        final String password = passwordField.getText().toString();

        firebaseAuth.signInWithEmailAndPassword(emailAddress,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            checkUserLoggedIn();
//                            startActivity(new Intent(getApplicationContext(),UserDataActivity.class));
//                            finish();
                        }
                        else{
                            vibrator.vibrate(100);
                            Toast.makeText(getApplicationContext(),"E-mail or password is wrong"+task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                            Log.d("login error:",task.getException().getMessage());
                        }
                    }
                });

    }


    private boolean validateData() {

        //check validity of email address
        if(!isValidEmail(emailField.getText().toString())){
            emailField.setError("Invalid email address");
            emailField.requestFocus();
            vibrator.vibrate(100);
            return false;
        }

        return true;
    }

    public static boolean isValidEmail(String email) {
        return (!TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches());
    }


}
