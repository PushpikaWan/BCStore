package com.bc.pushpika.bc_store;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    Button approveUserButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        approveUserButton = findViewById(R.id.button3);
        if(LoginActivity.isAdmin){
            approveUserButton.setVisibility(View.VISIBLE);
        }

    }

    public void approveUsers(View view){
        startActivity(new Intent(getApplicationContext(),PendingListViewActivity.class));
    }
    public void logout(View view){

        if( FirebaseAuth.getInstance()!= null){
            FirebaseAuth.getInstance().signOut();
            finish();
            startActivity(new Intent(getApplicationContext(),LoginActivity.class));
        }

    }
}
