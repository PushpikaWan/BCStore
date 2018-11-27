package com.bc.pushpika.bc_store;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    LinearLayout adminView, normalView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        adminView = findViewById(R.id.adminView);
        normalView = findViewById(R.id.normalView);

        if(LoginActivity.isAdmin){
            adminView.setVisibility(View.VISIBLE);
        }
        else{
            normalView.setVisibility(View.VISIBLE);
        }

    }

    public void userData(View view){
        startActivity(new Intent(getApplicationContext(),UserDataActivity.class));
    }

    public void searchUsers(View view){
        startActivity(new Intent(getApplicationContext(),ListViewActivity.class));
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
