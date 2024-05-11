package com.example.bookbazaar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class UserOrderSuccessfulAnimActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_order_successful_anim);

        // Initialize a Handler to delay the redirection
        final Handler handler  = new Handler();

        // Set a delay of 3000 milliseconds (3 seconds)
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Create and start an intent to open the cart activity
                startActivity(new Intent(UserOrderSuccessfulAnimActivity.this, User_Cart_Activity.class));
                finish();   // Finish this activity so the user can't navigate back to it
            }
        }, 3000);   // Delay duration
    }
}