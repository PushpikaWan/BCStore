package com.bc.pushpika.bc_store;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgetPasswordActivity extends AppCompatActivity {

    EditText emailField;
    Vibrator vibrator;
    FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    private Button resetButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        emailField = findViewById(R.id.emailField);
        resetButton = findViewById(R.id.resetButton);

        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
    }

    public void sendResetEmail(View view){

        progressDialog.setMessage("Password reset in progress ......");
        progressDialog.setCancelable(false);
        progressDialog.show();

        if(!validateData()){
            progressDialog.dismiss();
            return;
        }

        sendMail();
    }

    private boolean validateData() {
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

    private void sendMail() {
        FirebaseAuth.getInstance().sendPasswordResetEmail(emailField.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("Password reset", "Email sent.");
                            changeButtonToEmailSent();
                        }
                        else{
                            Toast.makeText(getApplicationContext(),
                                    "Password reset failed"+task.getException().getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }

                        vibrator.vibrate(100);

                        progressDialog.dismiss();
                    }
                });
    }

    private void changeButtonToEmailSent() {
        resetButton.setEnabled(false);
        resetButton.setClickable(false);
        resetButton.setBackgroundColor(getResources().getColor(R.color.white));
        resetButton.setTextColor(getResources().getColor(R.color.colorAccentSecond));
        resetButton.setText("Password reset email sent");
    }

    @Override
    protected void onStop() {
        dismissProgressBarIfShowing();
        super.onStop();
    }

    private void dismissProgressBarIfShowing() {
        if(progressDialog.isShowing()){
            progressDialog.dismiss();
        }
    }

    @Override
    protected void onDestroy() {
        dismissProgressBarIfShowing();
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        dismissProgressBarIfShowing();
        super.onPause();
    }

}
