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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class UserRegisterActivity extends AppCompatActivity {

    private Button RegisterBtn;
    private EditText InputName, InputPhoneNumber, InputPassword;
    private TextView SellerRegLink;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_register);

        // Initialize views
        RegisterBtn = (Button) findViewById(R.id.register_btn);
        InputName = (EditText) findViewById(R.id.signin_username_input);
        InputPhoneNumber = (EditText) findViewById(R.id.signin_phoneno_input);
        InputPassword = (EditText) findViewById(R.id.signin_passsword_input);
        SellerRegLink = (TextView) findViewById(R.id.seller_reg_link);

        // Register button click listener
        RegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call method to create account
                CreateAccount();
            }
        });

        // Seller registration link click listener
        SellerRegLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Navigate to seller registration activity
                Intent intent = new Intent(UserRegisterActivity.this, SellerRegisterActivity.class);
                startActivity(intent);
            }
        });

    }//end oncreate

    // Method to create a new user account
    private void CreateAccount() {
        String name = InputName.getText().toString();
        String phone = InputPhoneNumber.getText().toString();
        String password = InputPassword.getText().toString();

        // Check if all fields are filled
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(this, "Please Enter Your Name", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(phone)) {
            Toast.makeText(this, "Please Enter Your Phone Number", Toast.LENGTH_SHORT).show();
        } else if (phone.length() != 10) {
            Toast.makeText(this, "Phone Number must be Ten Digits", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please Enter Your Password", Toast.LENGTH_SHORT).show();
        } else if (password.length() < 5) {
            Toast.makeText(this, "Password must be Five Digits", Toast.LENGTH_SHORT).show();
        } else {
            // If all entries are correct, call this method to store data in Firebase
            ValidatePhoneNumber(name, phone, password);
        }

    }

    // Method to validate phone number and store user data in Firebase
    private void ValidatePhoneNumber(String name, String phone, String password) {
        // Show progress dialog
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Creating Account");
        progressDialog.setMessage("Please wait, while we are creating your account.");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NotNull DataSnapshot snapshot) {


                if (!(snapshot.child("Users").child(phone).exists())) {
                    // If phone number does not exist, proceed with registration
                    HashMap<String, Object> userdataMap = new HashMap<>();
                    userdataMap.put("phone", phone);
                    userdataMap.put("password", password);
                    userdataMap.put("name", name);

                    RootRef.child("Users").child(phone).updateChildren(userdataMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(Task<Void> task) {

                            if (task.isSuccessful()) {
                                // Registration successful, dismiss progress dialog and navigate to success activity
                                progressDialog.dismiss();
                                Toast.makeText(UserRegisterActivity.this, "Congratulations, Your Account has been Created", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(UserRegisterActivity.this, User_Signin_Successful_Anim.class);
                                startActivity(intent);
                                finish();
                            } else {
                                // Registration failed, dismiss progress dialog and show error message
                                progressDialog.dismiss();
                                Toast.makeText(UserRegisterActivity.this, "Network error! Pls Try Again", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                } else // if enter number already exists
                {
                    // If phone number already exists, show error message
                    progressDialog.dismiss();
                    Toast.makeText(UserRegisterActivity.this, "This " + phone + " Already Exists.", Toast.LENGTH_SHORT).show();
                    Toast.makeText(UserRegisterActivity.this, "Pls try Again with another Phone Number", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NotNull DatabaseError error) {
                // Handle database error
            }
        });

    }
}