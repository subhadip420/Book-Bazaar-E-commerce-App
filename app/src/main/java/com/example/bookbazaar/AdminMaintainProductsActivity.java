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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import java.util.HashMap;

public class AdminMaintainProductsActivity extends AppCompatActivity {

    // UI elements
    private ImageView changeSellerBookImage;
    private TextView viewBookState, viewBookId;
    private EditText changeSellerBookName, changeSellerBookAuthorName, changeSellerBookDescription, changeSellerBookPrice;
    private TextView sellerId, sellerName, sellerPhone, sellerEmail, sellerAddress;
    private TextView bookUploadTime;
    private Button contactSellerCall, contactSellerEmail, contactSellerWhatsapp;
    private Button adminApplyChangesBook, adminRemoveBook;

    // Product ID
    private String productID = "";

    // Database reference
    private DatabaseReference productsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_maintain_products);


        // Get product ID from intent
        productID = getIntent().getStringExtra("bookId");

        // Database reference
        productsRef = FirebaseDatabase.getInstance().getReference().child("Products").child(productID);

        // Initialize UI elements
        changeSellerBookImage = (ImageView) findViewById(R.id.admin_book_image);

        viewBookState = (TextView) findViewById(R.id.View_seller_BookState);
        viewBookId = (TextView) findViewById(R.id.View_seller_BookId);

        changeSellerBookName = (EditText) findViewById(R.id.admin_book_name);
        changeSellerBookAuthorName = (EditText) findViewById(R.id.admin_book_author_name);
        changeSellerBookDescription = (EditText) findViewById(R.id.admin_book_description_name);
        changeSellerBookPrice = (EditText) findViewById(R.id.admin_book_price);

        bookUploadTime = (TextView) findViewById(R.id.seller_book_upload_time_details2);
        sellerId = (TextView) findViewById(R.id.seller_id_details2);
        sellerName = (TextView) findViewById(R.id.seller_name_details2);
        sellerPhone = (TextView) findViewById(R.id.seller_phone_details2);
        sellerEmail = (TextView) findViewById(R.id.seller_email_details2);
        sellerAddress = (TextView) findViewById(R.id.seller_address_details2);

        contactSellerCall = (Button) findViewById(R.id.contact_seller_call_link_btn);
        contactSellerEmail = (Button) findViewById(R.id.contact_seller_email_link_btn);
        contactSellerWhatsapp = (Button) findViewById(R.id.contact_seller_whatsapp_link_btn);

        adminApplyChangesBook = (Button) findViewById(R.id.admin_apply_change_book_btn);
        adminRemoveBook = (Button) findViewById(R.id.admin_remove_book_btn);


        // Display specific product details
        displaySpecificProductInfo();

        // Contact seller via email
        contactSellerEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(AdminMaintainProductsActivity.this, "email", Toast.LENGTH_SHORT).show();
                contactSellerMail();
            }
        });

        // Contact seller via call
        contactSellerCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(AdminMaintainProductsActivity.this, "call", Toast.LENGTH_SHORT).show();
                contactSellerCall();
            }
        });

        // Contact seller via WhatsApp
        contactSellerWhatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(AdminMaintainProductsActivity.this, "whatsapp", Toast.LENGTH_SHORT).show();
                contactSellerWhatsapp();
            }
        });

        // Apply changes button click listener
        adminApplyChangesBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showApplyChangesDialog();
            }
        });

        // Remove book button click listener
        adminRemoveBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showRemoveBookDialog();
            }
        });


    }/// end on create ////

    // Method to show a dialog for applying changes
    private void showApplyChangesDialog() {
        // create a dialog box
        CharSequence options[] = new CharSequence[]
                {
                        "Yes",
                        "No"
                };

        AlertDialog.Builder builder = new AlertDialog.Builder(AdminMaintainProductsActivity.this);
        builder.setTitle("Are you sure you want to edit book details ?");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (i == 0) {
                    // call this method
                    //for apply changes
                    applyChanges();
                } else {
                    //null
                }
            }
        });
        // for show the dialog box
        builder.show();
    }

    // Method to show a dialog for removing the book
    private void showRemoveBookDialog() {
        // create a dialog box
        CharSequence options[] = new CharSequence[]
                {
                        "Yes",
                        "No"
                };

        AlertDialog.Builder builder = new AlertDialog.Builder(AdminMaintainProductsActivity.this);
        builder.setTitle("Are you sure you want to remove this book ?");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (i == 0) {
                    // create this methode for delete books
                    removeThisBook();
                } else {

                }
            }
        });
        // for show the dialog box
        builder.show();
    }

    // Method to contact seller via call
    private void contactSellerCall() {

        productsRef.addValueEventListener(new ValueEventListener() {
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

    // Method to contact seller via WhatsApp
    private void contactSellerWhatsapp() {

        productsRef.addValueEventListener(new ValueEventListener() {
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
                        Toast.makeText(AdminMaintainProductsActivity.this, "Whatsapp is not installed or failed to connect", Toast.LENGTH_LONG).show();
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    // Method to check if WhatsApp is installed
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

    // Method to contact seller via email
    private void contactSellerMail() {
        productsRef.addValueEventListener(new ValueEventListener() {
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


    // Method to remove the book
    private void removeThisBook() {
        productsRef.child("productState").setValue("Removed")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(AdminMaintainProductsActivity.this, "This book removed successfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(AdminMaintainProductsActivity.this, AdminHomeActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
    }


    // Method to apply changes to the book
    private void applyChanges() {
        String newBookName = changeSellerBookName.getText().toString();
        String newBookAuthorName = changeSellerBookAuthorName.getText().toString();
        String newBookDescription = changeSellerBookDescription.getText().toString();
        String newBookPrise = changeSellerBookPrice.getText().toString();

        if (newBookName.equals("")) {
            Toast.makeText(this, "Write book name", Toast.LENGTH_SHORT).show();
        } else if (newBookAuthorName.equals("")) {
            Toast.makeText(this, "Write book's author name", Toast.LENGTH_SHORT).show();
        } else if (newBookDescription.equals("")) {
            Toast.makeText(this, "Write book description", Toast.LENGTH_SHORT).show();
        } else if (newBookPrise.equals("")) {
            Toast.makeText(this, "Write book price", Toast.LENGTH_SHORT).show();
        } else {

            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Updating Book");
            progressDialog.setMessage("Please wait, while we are updating");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            HashMap<String, Object> productMap = new HashMap<>();

            productMap.put("bookName", newBookName);
            productMap.put("authorName", newBookAuthorName);
            productMap.put("description", newBookDescription);
            productMap.put("price", newBookPrise);
            productMap.put("productState", "Edited by Admin");

            productsRef.updateChildren(productMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        progressDialog.dismiss();
                        Toast.makeText(AdminMaintainProductsActivity.this, "Changes Apply Successfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(AdminMaintainProductsActivity.this, AdminHomeActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        progressDialog.dismiss();
                        String message = task.getException().toString();
                        Toast.makeText(AdminMaintainProductsActivity.this, "Error:" + message, Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    // Method to display specific product details
    private void displaySpecificProductInfo() {
        productsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // get all details form database
                    String bImage = snapshot.child("image").getValue().toString();
                    String bState = snapshot.child("productState").getValue().toString();
                    String bId = snapshot.child("bookId").getValue().toString();
                    String bName = snapshot.child("bookName").getValue().toString();
                    String bAuthorName = snapshot.child("authorName").getValue().toString();
                    String bDescription = snapshot.child("description").getValue().toString();
                    String bPrice = snapshot.child("price").getValue().toString();

                    String uploadDate = snapshot.child("date").getValue().toString();
                    String uploadTime = snapshot.child("time").getValue().toString();
                    String sID = snapshot.child("sid").getValue().toString();
                    String sName = snapshot.child("sellerName").getValue().toString();
                    String sPhone = snapshot.child("sellerPhone").getValue().toString();
                    String sEmail = snapshot.child("sellerEmail").getValue().toString();
                    String sAddress = snapshot.child("sellerAddress").getValue().toString();


                    //display all details
                    Picasso.get().load(bImage).into(changeSellerBookImage);
                    viewBookState.setText("Book State: " + bState);
                    viewBookId.setText("Book Id: " + bId);
                    changeSellerBookName.setText(bName);
                    changeSellerBookAuthorName.setText(bAuthorName);
                    changeSellerBookDescription.setText(bDescription);
                    changeSellerBookPrice.setText(bPrice);

                    bookUploadTime.setText("Upload on: " + uploadDate + " " + uploadTime);
                    sellerId.setText("Id: " + sID);
                    sellerName.setText("Name: " + sName);
                    sellerPhone.setText("Phone: " + sPhone);
                    sellerEmail.setText("Email: " + sEmail);
                    sellerAddress.setText("Address: " + sAddress);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}////end public class///