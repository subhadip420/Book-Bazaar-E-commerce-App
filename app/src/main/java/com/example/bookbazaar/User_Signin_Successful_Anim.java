package com.example.bookbazaar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class User_Signin_Successful_Anim extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_signin_successful_anim);

        // Initialize a handler to post a delayed action
        final Handler handler  = new Handler();

        // Post a delayed action to start the next activity after 3 seconds
        handler .postDelayed(new Runnable() {
            @Override
            public void run() {
                // Start the UserLoginActivity
                startActivity(new Intent(User_Signin_Successful_Anim.this, UserLoginActivity.class));
                finish();  // Finish this activity to prevent the user from going back to it
            }
        },3000);  // 3000 milliseconds = 3 seconds
    }
}