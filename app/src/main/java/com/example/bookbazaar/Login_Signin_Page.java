package com.example.bookbazaar;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bookbazaar.Model.Users;
import com.example.bookbazaar.Prevalent.Prevalent;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import io.paperdb.Paper;

public class Login_Signin_Page extends AppCompatActivity {

    private Button signin, login;
    private TextView SellerRegLink;

    //for test activity
    private Button testButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_signin_page);

        // Initialize UI elements
        signin = (Button) findViewById(R.id.newuser_signin_btn);
        login = (Button) findViewById(R.id.login_now_btn);

        SellerRegLink = (TextView) findViewById(R.id.seller_reg_link1);

        // Click listener for Seller registration link
        SellerRegLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login_Signin_Page.this, SellerRegisterActivity.class);
                startActivity(intent);
            }
        });

        // Initialize Paper for data storage
        Paper.init(this);

        // Click listener for login button
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login_Signin_Page.this, UserLoginActivity.class);
                startActivity(intent);
            }
        });

        // Click listener for sign up button
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login_Signin_Page.this, UserRegisterActivity.class);
                startActivity(intent);
            }
        });

        // Check if user is already logged in using Paper library
        String UserPhoneKey = Paper.book().read(Prevalent.UserPhoneKey);
        String UserPasswordKey = Paper.book().read(Prevalent.UserPasswordKey);
        if (UserPhoneKey != "" && UserPasswordKey != "") {
            if (!TextUtils.isEmpty(UserPhoneKey) && !TextUtils.isEmpty(UserPasswordKey)) {

                //call this methode
                AllowAccess(UserPhoneKey, UserPasswordKey);
            }
        }

    }//on create end ********

    @Override
    protected void onStart() {
        super.onStart();

        // Check if user is already logged in using Firebase Authentication
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if (firebaseUser != null) {
            Intent intent = new Intent(Login_Signin_Page.this, Seller_Home_Activity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }
    }

    // Method to allow access to the app
    private void AllowAccess(final String phone, final String password) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Welcome to BookBazaar");
        progressDialog.setMessage("Please wait, while we are fetching your details");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NotNull DataSnapshot snapshot) {

                if (snapshot.child("Users").child(phone).exists()) {
                    Users userData = snapshot.child("Users").child(phone).getValue(Users.class);

                    if (userData.getPhone().equals(phone)) {
                        if (userData.getPassword().equals(password)) {
                            progressDialog.dismiss();

                            Toast.makeText(Login_Signin_Page.this, "Already Logged in", Toast.LENGTH_LONG).show();

                            Intent intent = new Intent(Login_Signin_Page.this, User_Home_Content_Activity.class);
                            // Store current user's data in Prevalent
                            Prevalent.currentOnlineUser = userData;
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(Login_Signin_Page.this, "Please Enter Correct Password", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
    }


}//end public class