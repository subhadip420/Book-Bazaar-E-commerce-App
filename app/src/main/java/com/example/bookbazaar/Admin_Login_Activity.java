package com.example.bookbazaar;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Admin_Login_Activity extends AppCompatActivity {

    private EditText InputAdminPhoneNumber, InputAdminPassword;
    private Button AdminLoginButton;

    private String parentDbName = "Admins";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);

        // Initialize views
        AdminLoginButton = (Button) findViewById(R.id.admin_login_btn);
        InputAdminPhoneNumber = (EditText) findViewById(R.id.admin_login_phoneno_input);
        InputAdminPassword = (EditText) findViewById(R.id.admin_login_passsword_input);

        // Set onClickListener for login button
        AdminLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginAdmin();
            }
        });
    }///end on create ////

    // Method to handle admin login
    private void LoginAdmin() {
        String phone = InputAdminPhoneNumber.getText().toString();
        String password = InputAdminPassword.getText().toString();

        // Validate input fields
        if (TextUtils.isEmpty(phone)) {
            Toast.makeText(this, "Please Enter Your Phone Number", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please Enter Your Password", Toast.LENGTH_SHORT).show();
        } else {
            AllowAccessToAdminAccount(phone, password);
        }
    }

    // Method to check admin credentials
    private void AllowAccessToAdminAccount(String phone, String password) {
        // Show progress dialog
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Checking Details");
        progressDialog.setMessage("Please wait, while we are checking your information");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        // Get Firebase database reference
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        // Listen for data changes in Firebase database
        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                // Check if admin account exists
                if (snapshot.child(parentDbName).child(phone).exists()) {
                    final String getPassword = snapshot.child(parentDbName).child(phone).child("password").getValue(String.class);

                    // Check if entered password matches the one stored in the database
                    if (getPassword.equals(password)) {
                        progressDialog.dismiss();

                        // Display login success message
                        Toast.makeText(Admin_Login_Activity.this, "Login Successful", Toast.LENGTH_SHORT).show();

                        // Navigate to admin home activity
                        Intent intent = new Intent(Admin_Login_Activity.this, AdminHomeActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        progressDialog.dismiss();

                        // Display incorrect password message
                        Toast.makeText(Admin_Login_Activity.this, "Please Enter Correct Password", Toast.LENGTH_SHORT).show();
                    }


                } else {
                    progressDialog.dismiss();

                    // Display account not exist message
                    Toast.makeText(Admin_Login_Activity.this, "Account not exist.", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Display database error message
                Toast.makeText(Admin_Login_Activity.this, "Database Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}