package com.example.bookbazaar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class User_Customer_Care_Activity extends AppCompatActivity {

    private Button whatsappBtn, emailBtn, callBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_customer_care);

        // Initialize buttons
        whatsappBtn = (Button) findViewById(R.id.admin_whatsapp_btn);
        emailBtn = (Button) findViewById(R.id.admin_email_btn);
        callBtn = (Button) findViewById(R.id.admin_call_btn);

        // Set click listeners for buttons
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


    }///end on create **************

    // Method to handle WhatsApp functionality
    private void whatsappFunction() {

        String whatsappPhone = "+91 6296580849";  //use your number with country code for customer service help
        String whatsappMessage = "Hello";

        // Check if WhatsApp is installed
        boolean installed = isAppInstalled("com.whatsapp");

        if (installed) {
            // If WhatsApp is installed, open it with the provided phone number and message
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("http://api.whatsapp.com/send?phone=" + whatsappPhone + "&text=" + whatsappMessage));
            startActivity(intent);
        } else {
            // If WhatsApp is not installed, display a message
            Toast.makeText(User_Customer_Care_Activity.this, "Whatsapp is not installed or failed to connect", Toast.LENGTH_LONG).show();
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

    // Method to handle email functionality
    private void emailFunction() {

        String recipientEmail = "subhadippramanik02@gmail.com"; // Replace with the desired email
        String emailSubject = "Write your subject.";
        String emailMessage = "Explain how can we help you...";
        String emailClient = "Choose an Email client:";

        // Create an intent to send email
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setData(Uri.parse("mailto:")); // Only email apps should handle this
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{recipientEmail});
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, emailSubject);
        emailIntent.putExtra(Intent.EXTRA_TEXT, emailMessage);
        startActivity(Intent.createChooser(emailIntent, emailClient));
    }

    // Method to handle phone call functionality
    private void callFunction() {
        String phoneNumber = "tel:6296580849"; // Replace with the desired phone number
        Intent callIntent = new Intent(Intent.ACTION_DIAL, Uri.parse(phoneNumber));
        startActivity(callIntent);
    }

}//end public class ////