package com.example.bookbazaar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class User_Login_Success_Anim extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login_success_anim);

        final Handler handler = new Handler();  // Create a handler to delay the transition

        // Post a delayed action to start the next activity after 3000 milliseconds (3 seconds)
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Create and start an intent to navigate to User_Home_Content_Activity
                startActivity(new Intent(User_Login_Success_Anim.this, User_Home_Content_Activity.class));
                finish(); // Finish the current activity so it's not in the back stack
            }
        }, 3000); // Delay in milliseconds
    }
}