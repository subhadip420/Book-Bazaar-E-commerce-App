package com.example.bookbazaar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bookbazaar.Model.Products;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class AdminCheckNewBooksDetailsActivity extends AppCompatActivity {
    // Declare UI elements
    private ImageView sellerBookImage;
    private TextView bookId, bookName, bookAuthorName, bookPrice, bookCategory, bookDescription;
    private TextView sellerId, sellerName, sellerPhone, sellerEmail, sellerAddress;
    private TextView bookUploadTime, bookState;
    private Button approveBookBtn, rejectBookBtn;
    private Button emailBtn, callBtn, whatsappBtn;

    // Store the product ID
    private String productID = "";

    // Store the visibility state of buttons
    private String buttonView = "";

    // Firebase Database reference
    private DatabaseReference productsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_check_new_books_details);

        // Retrieve product ID and button visibility state from the intent
        productID = getIntent().getStringExtra("bookId");
        buttonView = getIntent().getStringExtra("buttonVisibility");

        // Initialize Firebase Database reference
        productsRef = FirebaseDatabase.getInstance().getReference().child("Products");

        // Initialize UI elements
        sellerBookImage = (ImageView) findViewById(R.id.seller_book_image_details);

        bookId = (TextView) findViewById(R.id.seller_BookId_details);
        bookName = (TextView) findViewById(R.id.seller_BookName_details);
        bookAuthorName = (TextView) findViewById(R.id.seller_authorName_details);
        bookPrice = (TextView) findViewById(R.id.seller_bookPrice_details);
        bookCategory = (TextView) findViewById(R.id.seller_bookCategory_details);
        bookDescription = (TextView) findViewById(R.id.seller_bookDescription_details);

        sellerId = (TextView) findViewById(R.id.seller_id_details);
        sellerName = (TextView) findViewById(R.id.seller_name_details);
        sellerPhone = (TextView) findViewById(R.id.seller_phone_details);
        sellerEmail = (TextView) findViewById(R.id.seller_email_details);
        sellerAddress = (TextView) findViewById(R.id.seller_address_details);

        bookUploadTime = (TextView) findViewById(R.id.seller_book_upload_time_details);
        bookState = (TextView) findViewById(R.id.seller_book_state_details);

        approveBookBtn = (Button) findViewById(R.id.admin_approve_book_btn);
        rejectBookBtn = (Button) findViewById(R.id.admin_reject_book_btn);

        whatsappBtn = (Button) findViewById(R.id.seller_whatsapp_link_btn);
        callBtn = (Button) findViewById(R.id.seller_call_link_btn);
        emailBtn = (Button) findViewById(R.id.seller_email_link_btn);

        // Load product details
        getProductDetails(productID);

        // Handle button visibility based on the received state
        if (buttonView.equals("hideButtons")) {
            approveBookBtn.setVisibility(View.GONE);
            rejectBookBtn.setVisibility(View.GONE);
        } else if (buttonView.equals("viewButtons")) {
            approveBookBtn.setVisibility(View.VISIBLE);
            rejectBookBtn.setVisibility(View.VISIBLE);
        }

        // Set onClickListeners for buttons
        approveBookBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showApproveDialog();

            }
        });

        rejectBookBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showRejectDialog();

            }
        });

        emailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(AdminCheckNewBooksDetailsActivity.this, "email", Toast.LENGTH_SHORT).show();
                contactSellerMail();
            }
        });

        callBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(AdminCheckNewBooksDetailsActivity.this, "call", Toast.LENGTH_SHORT).show();
                contactSellerCall();
            }
        });

        whatsappBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(AdminCheckNewBooksDetailsActivity.this, "whatsapp", Toast.LENGTH_SHORT).show();
                contactSellerWhatsapp();
            }
        });

    }///end on create *****



    // Display dialog to confirm approval
    private void showApproveDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(AdminCheckNewBooksDetailsActivity.this);
        builder.setTitle("Approve Book");
        builder.setMessage("Are you sure you want to approve this book?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                approveProductState(productID);
            }
        });
        builder.setNegativeButton("No", null);
        builder.show();
    }

    // Display dialog to confirm rejection
    private void showRejectDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(AdminCheckNewBooksDetailsActivity.this);
        builder.setTitle("Reject Book");
        builder.setMessage("Are you sure you want to reject this book?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                rejectProductState(productID);
            }
        });
        builder.setNegativeButton("No", null);
        builder.show();
    }


    // Contact seller via call
    private void contactSellerCall() {
        productsRef.child(productID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Products products = snapshot.getValue(Products.class);

                    String phoneNumber = "tel:" + (products.getSellerPhone()); // get seller phone to call seller

                    Intent callIntent = new Intent(Intent.ACTION_DIAL, Uri.parse(phoneNumber));
                    startActivity(callIntent);


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    // Contact seller via WhatsApp
    private void contactSellerWhatsapp() {

        productsRef.child(productID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Products products = snapshot.getValue(Products.class);

                    String whatsappPhone = (products.getSellerPhone());

                    String whatsappMessage = "Update on: " + (products.getDate()) + " " + (products.getTime()) + "\n"
                            + "Book State: " + (products.getProductState()) + "\n"
                            + "Book Id: " + (products.getBookId()) + "\n"
                            + "Book Name: " + (products.getBookName()) + "\n"
                            + "Author Name: " + (products.getAuthorName()) + "\n"
                            + "Book Price: " + (products.getPrice()) + "\n"
                            + "Book Category: " + (products.getCategory()) + "\n"
                            + "Details: " + (products.getDescription()) + "\n" + "\n"
                            + "Seller Id: " + (products.getSid()) + "\n"
                            + "Seller Name: " + (products.getSellerName()) + "\n"
                            + "Seller Phone: " + (products.getSellerPhone()) + "\n"
                            + "Seller Email: " + (products.getSellerEmail()) + "\n"
                            + "Seller Address: " + (products.getSellerAddress());


                    boolean installed = isAppInstalled("com.whatsapp");

                    if (installed) {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse("http://api.whatsapp.com/send?phone=" + whatsappPhone + "&text=" + whatsappMessage));
                        startActivity(intent);
                    } else {
                        Toast.makeText(AdminCheckNewBooksDetailsActivity.this, "Whatsapp is not installed or failed to connect", Toast.LENGTH_LONG).show();
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    //check whatsapp installed or not
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


    // Contact seller via email
    private void contactSellerMail() {
        productsRef.child(productID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Products products = snapshot.getValue(Products.class);

                    String recipientEmail = (products.getSellerEmail());
                    String emailSubject = "Write your subject.";
                    String emailMessage = "Update on: " + (products.getDate()) + " " + (products.getTime()) + "\n"
                            + "Book State: " + (products.getProductState()) + "\n"
                            + "Book Id: " + (products.getBookId()) + "\n"
                            + "Book Name: " + (products.getBookName()) + "\n"
                            + "Author Name: " + (products.getAuthorName()) + "\n"
                            + "Book Price: " + (products.getPrice()) + "\n"
                            + "Book Category: " + (products.getCategory()) + "\n"
                            + "Details: " + (products.getDescription()) + "\n" + "\n"
                            + "Seller Id: " + (products.getSid()) + "\n"
                            + "Seller Name: " + (products.getSellerName()) + "\n"
                            + "Seller Phone: " + (products.getSellerPhone()) + "\n"
                            + "Seller Email: " + (products.getSellerEmail()) + "\n"
                            + "Seller Address: " + (products.getSellerAddress());

                    String emailClient = "Choose an Email client:";


                    Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                    emailIntent.setData(Uri.parse("mailto:")); // Only email apps should handle this
                    emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{recipientEmail});
                    emailIntent.putExtra(Intent.EXTRA_SUBJECT, emailSubject);
                    emailIntent.putExtra(Intent.EXTRA_TEXT, emailMessage);
                    startActivity(Intent.createChooser(emailIntent, emailClient));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    // Approve the product and change its state to "Approved"
    private void approveProductState(String productID) {

        productsRef.child(productID).child("productState").setValue("Approved")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(AdminCheckNewBooksDetailsActivity.this, "The book has been approved and available for user", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(AdminCheckNewBooksDetailsActivity.this, AdminCheckNewBooksActivity.class);
                        startActivity(intent);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    }
                });
    }

    // Reject the product and change its state to "Rejected"
    private void rejectProductState(String productID) {

        productsRef.child(productID).child("productState").setValue("Rejected")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(AdminCheckNewBooksDetailsActivity.this, "The book has been rejected", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(AdminCheckNewBooksDetailsActivity.this, AdminCheckNewBooksActivity.class);
                        startActivity(intent);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    }
                });
    }

    // Retrieve and display product details
    private void getProductDetails(String productID) {
        //DatabaseReference productsRef = FirebaseDatabase.getInstance().getReference().child("Products");

        productsRef.child(productID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Products products = snapshot.getValue(Products.class);

                    Picasso.get().load(products.getImage()).into(sellerBookImage);

                    bookId.setText("Id: " + (products.getBookId()));
                    bookName.setText("Name: " + (products.getBookName()));
                    bookAuthorName.setText("Author Name: " + (products.getAuthorName()));
                    bookPrice.setText((products.getPrice()));
                    bookCategory.setText("Category:- " + (products.getCategory()));
                    bookDescription.setText("Details: " + (products.getDescription()));

                    sellerId.setText("Id: " + (products.getSid()));
                    sellerName.setText("Name: " + (products.getSellerName()));
                    sellerPhone.setText("Phone: " + (products.getSellerPhone()));
                    sellerEmail.setText("Email: " + (products.getSellerEmail()));
                    sellerAddress.setText("Address: " + (products.getSellerAddress()));

                    bookUploadTime.setText("Update on: " + (products.getDate()) + " " + (products.getTime()));
                    bookState.setText("State: " + (products.getProductState()));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}//end public class *****