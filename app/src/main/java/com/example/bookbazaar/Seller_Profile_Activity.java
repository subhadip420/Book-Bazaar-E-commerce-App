package com.example.bookbazaar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class Seller_Profile_Activity extends AppCompatActivity {

    // Declare variables
    private TextView viewSellerId;
    private EditText viewSellerName, viewSellerPhone, viewSellerEmail, viewSellerAddress, viewSellerPassword;
    private Button sellerUpdateProfile, sellerLogoutBtn;

    private Button whatsappBtn, emailBtn, callBtn;

    // Variables for seller details
    private String sName, sAddress, sPhone, sEmail, sID, sPassword;

    private DatabaseReference sellerRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_profile);

        // Initialize UI elements
        viewSellerId = (TextView) findViewById(R.id.textView_SellerId);

        viewSellerName = (EditText) findViewById(R.id.seller_profile_name_view);
        viewSellerPhone = (EditText) findViewById(R.id.seller_profile_phone_view);
        viewSellerEmail = (EditText) findViewById(R.id.seller_profile_email_view);
        viewSellerAddress = (EditText) findViewById(R.id.seller_profile_ads_view);
        viewSellerPassword = (EditText) findViewById(R.id.seller_profile_password_view);

        sellerUpdateProfile = (Button) findViewById(R.id.seller_update_profile_btn);
        sellerLogoutBtn = (Button) findViewById(R.id.seller_logout_button);

        whatsappBtn = (Button) findViewById(R.id.seller_whatsapp_btn);
        emailBtn = (Button) findViewById(R.id.seller_email_btn);
        callBtn = (Button) findViewById(R.id.seller_call_btn);

        // Firebase reference for seller details
        sellerRef = FirebaseDatabase.getInstance().getReference().child("Sellers");

        // Retrieve current online seller details
        sellerRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            sID = snapshot.child("sid").getValue().toString();
                            sEmail = snapshot.child("email").getValue().toString();
                            sName = snapshot.child("name").getValue().toString();
                            sAddress = snapshot.child("address").getValue().toString();
                            sPhone = snapshot.child("phone").getValue().toString();
                            sPassword = snapshot.child("password").getValue().toString();

                            // Display seller details
                            viewSellerDetails();

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Handle database error
                        Toast.makeText(Seller_Profile_Activity.this, "Error retrieving seller details", Toast.LENGTH_SHORT).show();
                    }
                });

    // Button click listeners
        sellerUpdateProfile.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(Seller_Profile_Activity.this);
            builder.setTitle("Are you sure you want to changes your details ?");

            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    applyChanges();
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            builder.show();
        });

        sellerLogoutBtn.setOnClickListener(view -> {
            
            logout();

        });

        whatsappBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                whatsappFunction();
            }
        });

        emailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                emailFunction();
            }
        });

        callBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callFunction();
            }
        });

    }/// end On create //////////

    // Method to logout seller
    private void logout() {
        // Display confirmation dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(Seller_Profile_Activity.this);
        builder.setTitle("Are you sure you want to logout?");
        builder.setPositiveButton("Yes", (dialogInterface, i) -> {
            FirebaseAuth.getInstance().signOut();
            Toast.makeText(Seller_Profile_Activity.this, "Logout Successful", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Seller_Profile_Activity.this, Login_Signin_Page.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
        builder.setNegativeButton("No", (dialogInterface, i) -> {});
        builder.show();
    }

    // Method to apply changes to seller profile
    private void applyChanges() {
        // Retrieve new values
        String newSellerName = viewSellerName.getText().toString();
        String newSellerPhone = viewSellerPhone.getText().toString();
        String newSellerAddress = viewSellerAddress.getText().toString();

        // Validate input fields
        if (TextUtils.isEmpty(newSellerName)) {
            Toast.makeText(this, "Enter your name", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(newSellerPhone)) {
            Toast.makeText(this, "Enter your phone no", Toast.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(newSellerAddress)) {
            Toast.makeText(this, "Enter your Address", Toast.LENGTH_SHORT).show();
        } else {
            // Update seller details in Firebase
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Updating Details");
            progressDialog.setMessage("Please wait, while we are updating");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            HashMap<String, Object> sellerMap = new HashMap<>();

            sellerMap.put("name", newSellerName);
            sellerMap.put("phone", newSellerPhone);
            sellerMap.put("address", newSellerAddress);

            sellerRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .updateChildren(sellerMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful()) {
                                progressDialog.dismiss();

                                Toast.makeText(Seller_Profile_Activity.this, "Changes Apply Successfully", Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(Seller_Profile_Activity.this, Seller_Home_Activity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();
                            } else {
                                progressDialog.dismiss();

                                String message = task.getException().toString();

                                //display the error
                                Toast.makeText(Seller_Profile_Activity.this, "Error:" + message, Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
        }
    }

    // Method to display seller details
    private void viewSellerDetails() {
        
        viewSellerId.setText("Seller Id: " + sID);
        viewSellerName.setText(sName);
        viewSellerPhone.setText(sPhone);
        viewSellerEmail.setText(sEmail);
        viewSellerAddress.setText(sAddress);
        viewSellerPassword.setText(sPassword);

    }

    // Method to open WhatsApp
    private void whatsappFunction() {

        String whatsappPhone = "+91 6296580849";  //use your number with country code for customer service help
        String whatsappMessage = "Hello";

        boolean installed = isAppInstalled("com.whatsapp");

        if (installed) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("http://api.whatsapp.com/send?phone=" + whatsappPhone + "&text=" + whatsappMessage));
            startActivity(intent);
        } else {
            Toast.makeText(Seller_Profile_Activity.this, "Whatsapp is not installed or failed to connect", Toast.LENGTH_LONG).show();
        }
    }

    // Method to check if an app is installed
    private boolean isAppInstalled(String s) {
        PackageManager packageManager = getPackageManager();
        boolean is_installed;

        try {
            packageManager.getPackageInfo(s, PackageManager.GET_ACTIVITIES);
            is_installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            is_installed = false;
            e.printStackTrace();
        }
        return is_installed;
    }

    // Method to compose an email
    private void emailFunction() {

        String recipientEmail = "subhadippramanik02@gmail.com"; // Replace with the desired email
        String emailSubject = "Write your subject.";
        String emailMessage = "Explain how can we help you...";
        String emailClient = "Choose an Email client:";


        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setData(Uri.parse("mailto:")); // Only email apps should handle this
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{recipientEmail});
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, emailSubject);
        emailIntent.putExtra(Intent.EXTRA_TEXT, emailMessage);
        startActivity(Intent.createChooser(emailIntent, emailClient));

    }

    // Method to initiate a phone call
    private void callFunction() {
        String phoneNumber = "tel:6296580849"; // Replace with the desired phone number
        Intent callIntent = new Intent(Intent.ACTION_DIAL, Uri.parse(phoneNumber));
        startActivity(callIntent);
    }

}///end public class