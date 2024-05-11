package com.example.bookbazaar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.bookbazaar.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Seller_Login_Activity extends AppCompatActivity {

    // Declare variables
    private Button SellerLoginBtn;
    private EditText sellerLoginEmailInput, sellerLoginPasswordInput;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_login);

        // Initialize Firebase Authentication
        mAuth = FirebaseAuth.getInstance();

        // Initialize UI elements
        SellerLoginBtn = (Button) findViewById(R.id.seller_login_btn);
        sellerLoginEmailInput = (EditText) findViewById(R.id.seller_login_email_input);
        sellerLoginPasswordInput = (EditText) findViewById(R.id.seller_login_password_input);

        // Set OnClickListener for the login button
        SellerLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get email and password from EditText fields
                String email, password;
                email = String.valueOf(sellerLoginEmailInput.getText());
                password = String.valueOf(sellerLoginPasswordInput.getText());

                // Check if email field is empty
                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(Seller_Login_Activity.this, "Enter a valid email", Toast.LENGTH_SHORT).show();
                    return;
                }
                // Check if password field is empty
                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(Seller_Login_Activity.this, "Enter a password", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Sign in with email and password using FirebaseAuth
                mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // If login is successful, show a toast message and navigate to the Seller Home Activity
                            Toast.makeText(Seller_Login_Activity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), Seller_Home_Activity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        } else {
                            // If login fails, show a toast message
                            Toast.makeText(Seller_Login_Activity.this, "Authentication failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}
