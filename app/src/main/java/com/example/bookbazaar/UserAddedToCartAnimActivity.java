package com.example.bookbazaar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class UserAddedToCartAnimActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_added_to_cart_anim);

        // Handler to delay navigation
        final Handler hand = new Handler();

        // Post delayed action to navigate after 2400 milliseconds
        hand.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Start cart activity
                startActivity(new Intent(UserAddedToCartAnimActivity.this, User_Cart_Activity.class));
                finish(); // Finish this activity
            }
        }, 2400);  // Delay in milliseconds
    }
}