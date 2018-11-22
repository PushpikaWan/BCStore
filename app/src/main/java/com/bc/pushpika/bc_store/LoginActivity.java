package com.bc.pushpika.bc_store;

import android.content.Context;
import android.content.Intent;
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


public class LoginActivity extends AppCompatActivity {

    EditText emailField, passwordField;
    Vibrator vibrator;

    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailField = findViewById(R.id.emailField);
        passwordField = findViewById(R.id.passwordField);

        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        firebaseAuth = FirebaseAuth.getInstance();


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
                            startActivity(new Intent(getApplicationContext(),UserDataActivity.class));
                            finish();
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
