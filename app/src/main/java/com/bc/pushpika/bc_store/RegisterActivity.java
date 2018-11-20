package com.bc.pushpika.bc_store;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    EditText emailField, passwordField, verifyPasswordField;
    Vibrator vibrator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        emailField = findViewById(R.id.emailField);
        passwordField = (findViewById(R.id.passwordField));
        verifyPasswordField = findViewById(R.id.verifyPasswordField);

        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

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
        final String password = Utill.md5(passwordField.getText().toString());
        final String registerUrl = "http://192.168.1.104/bcApp-server/register.php";


        //Call our volley library
        StringRequest stringRequest = new StringRequest(Request.Method.POST,registerUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {

                            JSONObject obj = new JSONObject(response);
                            if (obj.getBoolean("error")) {
                                vibrator.vibrate(100);
                                Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                            } else {

                                //starting the login activity
                                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                            }

                        } catch (JSONException e) {
                            Toast.makeText(getApplicationContext(),"Registration Error", Toast.LENGTH_LONG).show();
                            vibrator.vibrate(100);
                            //e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(),"Connection Error"+error, Toast.LENGTH_LONG).show();
                        vibrator.vibrate(100);
                        //error.printStackTrace();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("emailAddress", emailAddress);
                params.put("password", password);

                return params;
            }
        };

        VolleySingleton.getInstance(RegisterActivity.this).addToRequestQueue(stringRequest);
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
