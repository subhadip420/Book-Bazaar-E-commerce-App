package com.example.bookbazaar;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bookbazaar.Model.Users;
import com.example.bookbazaar.Prevalent.Prevalent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rey.material.widget.CheckBox;

import io.paperdb.Paper;

public class UserLoginActivity extends AppCompatActivity {
    private EditText InputPhoneNumber, InputPassword;
    private Button LoginButton;
    private TextView AdminLink;
    private TextView SellerLink;
    private TextView ForgetPasswordLink;

    private String parentDbName = "Users";
    private CheckBox checkBoxRememberMe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);

        // Initialize views
        LoginButton = (Button) findViewById(R.id.login_btn);
        InputPhoneNumber = (EditText) findViewById(R.id.login_phoneno_input);
        InputPassword = (EditText) findViewById(R.id.login_passsword_input);
        checkBoxRememberMe = (CheckBox) findViewById(R.id.remember_me_checkbox);
        AdminLink = (TextView) findViewById(R.id.admin_link);
        SellerLink = (TextView) findViewById(R.id.seller_link);
        ForgetPasswordLink = (TextView) findViewById(R.id.forget_password_link);

        // Initialize Paper for storing user credentials
        Paper.init(this);

        // Handle click on "Forget Password" link
        ForgetPasswordLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserLoginActivity.this, UserResetPasswordActivity.class);
                intent.putExtra("check", "login");
                startActivity(intent);
            }
        });

        // Handle click on login button
        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // call loginUser methode
                LoginUser();
            }
        });

        // Handle click on "I am an Admin" link
        AdminLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserLoginActivity.this, Admin_Login_Activity.class);
                startActivity(intent);
            }
        });

        // Handle click on "I am a Seller" link
        SellerLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserLoginActivity.this, Seller_Login_Activity.class);
                startActivity(intent);
            }
        });

    }// end on create methode *********

    // Override onBackPressed to finish the activity
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }


    // Check if user entered all fields
    private void LoginUser() {
        String phone = InputPhoneNumber.getText().toString();
        String password = InputPassword.getText().toString();

        if (TextUtils.isEmpty(phone)) {
            Toast.makeText(this, "Please Enter Your Phone Number", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please Enter Your Password", Toast.LENGTH_SHORT).show();
        } else {
            // If all fields are filled, attempt to login
            AllowAccessToAccount(phone, password);
        }
    }

    // Attempt to login with entered credentials
    private void AllowAccessToAccount(String phone, String password) {

        // Show progress dialog
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Checking Details");
        progressDialog.setMessage("Please wait, while we are checking your information.");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        // If "Remember Me" checkbox is checked, store credentials using Paper library
        if (checkBoxRememberMe.isChecked()) {
            Paper.book().write(Prevalent.UserPhoneKey, phone);
            Paper.book().write(Prevalent.UserPasswordKey, password);
        }

        // Get database reference
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        // Check if the user exists in the database
        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                if (snapshot.child(parentDbName).child(phone).exists()) {
                    Users userData = snapshot.child(parentDbName).child(phone).getValue(Users.class);
                    // If phone number and password match, login successful
                    if (userData.getPhone().equals(phone)) {
                        if (userData.getPassword().equals(password)) {
                            progressDialog.dismiss();
                            Toast.makeText(UserLoginActivity.this, "Login Successfully", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(UserLoginActivity.this, User_Login_Success_Anim.class);
                            startActivity(intent);
                            finish();
                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(UserLoginActivity.this, "Please Enter Correct Password", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(UserLoginActivity.this, "Account not exist, Resister now.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(UserLoginActivity.this, Login_Signin_Page.class);
                    startActivity(intent);
                }
            }
            @Override
            public void onCancelled(DatabaseError error) {
            }
        });
    }//end allowAccessMethode

}// end public class