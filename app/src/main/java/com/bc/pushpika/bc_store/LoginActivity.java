package com.bc.pushpika.bc_store;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
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

public class LoginActivity extends AppCompatActivity {

    EditText emailField, passwordField;
    Vibrator vibrator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailField = findViewById(R.id.emailField);
        passwordField = findViewById(R.id.passwordField);

        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

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
        final String email = emailField.getText().toString();
        final String password = Utill.md5(passwordField.getText().toString());
        final String loginURL = "http://192.168.1.104/bcApp-server/login.php";


        //Call our volley library
        StringRequest stringRequest = new StringRequest(Request.Method.POST,loginURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
//                            Toast.makeText(getApplicationContext(),response.toString(), Toast.LENGTH_SHORT).show();

                            JSONObject obj = new JSONObject(response);
                            if (obj.getBoolean("error")) {
                                Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                                vibrator.vibrate(100);
                            } else {

                                Log.d("response id",obj.getString("id"));
                                Log.d("response email",obj.getString("emailAddress"));
                              //  Toast.makeText(getApplicationContext(),id, Toast.LENGTH_SHORT).show();

                                //storing the user in shared preferences
                  ///////            //  SharedPref.getInstance(getApplicationContext()).storeUserName(Username);
                                //starting the profile activity
                                SharedPreferences.Editor editor = getSharedPreferences("MY_PREF", MODE_PRIVATE).edit();
                                editor.putString("userID", obj.getString("id"));
                                editor.putString("emailAddress", obj.getString("emailAddress"));
                                editor.apply();

                                finish();
                                startActivity(new Intent(getApplicationContext(), HomeActivity.class));

                            }



                        } catch (JSONException e) {
                            Toast.makeText(getApplicationContext(),"Login failed", Toast.LENGTH_SHORT).show();
                            vibrator.vibrate(100);
                            //e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(),"Connection Error"+error, Toast.LENGTH_SHORT).show();
                        vibrator.vibrate(100);
                       // error.printStackTrace();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("emailAddress", email);
                params.put("password", password);

                return params;
            }
        };
        VolleySingleton.getInstance(LoginActivity.this).addToRequestQueue(stringRequest);
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
