package com.example.bookbazaar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

public class AdminHomeActivity extends AppCompatActivity {
    private Button maintainBookbtn, newBookbtn, newOrderbtn, addPDFbtn, logoutbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

        // Initialize buttons
        maintainBookbtn = (Button) findViewById(R.id.admin_maintain_book_btn);
        newBookbtn = (Button) findViewById(R.id.admin_check_new_book_btn);
        newOrderbtn = (Button) findViewById(R.id.admin_check_new_order_btn);
        addPDFbtn = (Button) findViewById(R.id.admin_add_new_pdf_btn);
        logoutbtn = (Button) findViewById(R.id.admin_logout_button);

        // Button click listeners

        // Redirect to User Home Content Activity
        maintainBookbtn.setOnClickListener(view -> {
            Intent intent = new Intent(AdminHomeActivity.this, User_Home_Content_Activity.class);
            intent.putExtra("Admin", "Admin");
            startActivity(intent);
        });

        // Redirect to Admin Check New Books Activity
        newBookbtn.setOnClickListener(view -> {
            Intent intent = new Intent(AdminHomeActivity.this, AdminCheckNewBooksActivity.class);
            startActivity(intent);
        });

        // Redirect to Admin Check New Order Activity
        newOrderbtn.setOnClickListener(view -> {
            Intent intent = new Intent(AdminHomeActivity.this, AdminCheckNewOrderActivity.class);
            startActivity(intent);
        });

        // Redirect to Admin PDF List Activity
        addPDFbtn.setOnClickListener(view -> {
            Intent intent = new Intent(AdminHomeActivity.this, AdminPdfListActivity.class);
            startActivity(intent);
        });

        // Logout button functionality
        logoutbtn.setOnClickListener(view -> {

            // Confirmation dialog for logout
            CharSequence options[] = new CharSequence[]
                    {
                            "Yes", "No"
                    };

            AlertDialog.Builder builder = new AlertDialog.Builder(AdminHomeActivity.this);
            builder.setTitle("Are you sure you want to Logout ?");

            builder.setItems(options, (dialogInterface, i) -> {
                if (i == 0) {
                    // Logout action
                    Toast.makeText(AdminHomeActivity.this, "Logout Successful", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(AdminHomeActivity.this, Login_Signin_Page.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    finish();
                    startActivity(intent);
                } else {
                    // Do nothing on cancel
                }
            });
            // Show the dialog box
            builder.show();
        });

    }////end on create //////

}/// end public class //////