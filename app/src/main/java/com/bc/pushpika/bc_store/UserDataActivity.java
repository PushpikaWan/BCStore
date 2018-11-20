package com.bc.pushpika.bc_store;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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

public class UserDataActivity extends AppCompatActivity {

    EditText nameField, addressField, bloodTypeField, mobileField, alYearField;
    Vibrator vibrator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_data);

        nameField = findViewById(R.id.nameField);
        addressField = findViewById(R.id.addressField);
        bloodTypeField = findViewById(R.id.bloodTypeField);
        mobileField = findViewById(R.id.mobileField);
        alYearField = findViewById(R.id.alYearField);

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
        SharedPreferences.Editor editor = getSharedPreferences("", MODE_PRIVATE).edit();
        SharedPreferences prefs = getSharedPreferences("MY_PREF", MODE_PRIVATE);

        final String userID = prefs.getString("userID", null);
        final String name = nameField.getText().toString().trim();
        final String address = addressField.getText().toString().trim();
        final String emailAddress = prefs.getString("emailAddress", null);
        final String bloodType = bloodTypeField.getText().toString().trim();
        final String mobileNumber = mobileField.getText().toString().trim();
        final String advancedLevelYear = alYearField.getText().toString().trim();

        Log.d("response id",userID);
        Log.d("response email",emailAddress);


        final String registerUrl = "http://192.168.1.104/bcApp-server/userData.php";

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
                            Toast.makeText(getApplicationContext(),"Data insertion Error", Toast.LENGTH_LONG).show();
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
                params.put("userID", userID);
                params.put("name", name);
                params.put("address",address);
                params.put("emailAddress", emailAddress);
                params.put("bloodType", bloodType);
                params.put("mobileNumber", mobileNumber);
                params.put("advancedLevelYear", advancedLevelYear);

                return params;
            }
        };

        VolleySingleton.getInstance(UserDataActivity.this).addToRequestQueue(stringRequest);
    }

    private boolean validateData() {
        if(!isNotEmptyField(nameField) || !isNotEmptyField(addressField) ||
                !isNotEmptyField(bloodTypeField)){
            return false;
        }

       if(mobileField.getText().toString().trim().length() != 10){
           vibrator.vibrate(100);
           mobileField.setError("should be 10 character number");
           return false;
       }

       if(alYearField.getText().toString().trim().length() != 4){
           vibrator.vibrate(100);
           alYearField.setError("should be valid year");
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
