package com.example.bookbazaar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class SellerMaintainProductsActivity extends AppCompatActivity {
    // Declare variables
    private ImageView SellerChangeBookImage;
    private TextView SellerBookState, SellerBookId;
    private EditText SellerChangeBookName, SellerChangeBookAuthorName, SellerChangeBookDescription, SellerChangeBookPrice;
    private Button SellerApplyChangesBook, SellerDeleteBook, SellerOutOfStockBook;

    // Array for dropdown category list
    private String[] cat = {
            "Arts", "Astronomy", "Biography", "Biology", "Chemistry", "Comics and Cartoon", "Computer and Technology", "Dictionary",
            "Drama", "Drawing", "Engineering", "General Knowledge", "Geography", "History", "Horror Special", "Kids and Funny",
            "Mathematics", "Medical", "Motivation and Self-help", "Physics", "Religion and Spirituality", "Romantic", "Story", "Others"
    };

    // AutoCompleteTextView for category selection
    private AutoCompleteTextView autoCompleteTextView;
    private ArrayAdapter<String> adapterItems;

    // Variables for book ID
    private String productID = "";

    // Database references
    private DatabaseReference productsRef;
    private DatabaseReference sellerRef;

    // Variables for online seller details
    private String sName, sAddress, sPhone, sEmail, sID;

    // Variables for current date and time
    private String saveCurrentDate, saveCurrentTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_maintain_products);

        // Get book ID from intent
        productID = getIntent().getStringExtra("bookId");

        // Initialize database references
        productsRef = FirebaseDatabase.getInstance().getReference().child("Products").child(productID);

        // Initialize UI elements
        SellerChangeBookImage = (ImageView) findViewById(R.id.seller_book_image);

        SellerBookState = (TextView) findViewById(R.id.textView_seller_BookState);
        SellerBookId = (TextView) findViewById(R.id.textView_seller_BookId);

        SellerChangeBookName = (EditText) findViewById(R.id.seller_book_name);
        SellerChangeBookAuthorName = (EditText) findViewById(R.id.seller_book_author_name);
        SellerChangeBookDescription = (EditText) findViewById(R.id.seller_book_description_name);
        SellerChangeBookPrice = (EditText) findViewById(R.id.seller_book_price);

        SellerApplyChangesBook = (Button) findViewById(R.id.seller_apply_change_book_btn);
        SellerOutOfStockBook = (Button) findViewById(R.id.seller_out_of_stock_book_btn);
        SellerDeleteBook = (Button) findViewById(R.id.seller_delete_book_btn);

        autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.seller_maintain_book_category_name);

        // Setup category dropdown
        adapterItems = new ArrayAdapter<String>(this, R.layout.admin_pdf_category_item, cat);
        autoCompleteTextView.setAdapter(adapterItems);

        // Setup category selection listener
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String item = adapterView.getItemAtPosition(i).toString();

                Toast.makeText(SellerMaintainProductsActivity.this, "Category: " + item, Toast.LENGTH_SHORT).show();
            }
        });


        // Initialize database references
        sellerRef = FirebaseDatabase.getInstance().getReference().child("Sellers");

        // Get online seller details
        sellerRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            sName = snapshot.child("name").getValue().toString();
                            sAddress = snapshot.child("address").getValue().toString();
                            sPhone = snapshot.child("phone").getValue().toString();
                            sEmail = snapshot.child("email").getValue().toString();
                            sID = snapshot.child("sid").getValue().toString();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        // Setup button click listeners
        SellerApplyChangesBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                // create a dialog box
                CharSequence options[] = new CharSequence[]
                        {
                                "Yes",
                                "No"
                        };

                AlertDialog.Builder builder = new AlertDialog.Builder(SellerMaintainProductsActivity.this);
                builder.setTitle("Are you sure you want to change book details?");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (i == 0) {
                            // call this method
                            //for apply changes
                            applyChanges();

                        } else {
                            //dismiss
                        }
                    }
                });
                // for show the dialog box
                builder.show();
            }
        });

        SellerOutOfStockBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                outOfStock();
            }
        });

        SellerDeleteBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // create a dialog box
                CharSequence options[] = new CharSequence[]
                        {
                                "Yes",
                                "No"
                        };

                AlertDialog.Builder builder = new AlertDialog.Builder(SellerMaintainProductsActivity.this);
                builder.setTitle("Are you sure you want to delete this book ?");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (i == 0) {
                            // create this methode for delete books
                            deleteThisBook();
                        } else {
                            //dismiss
                        }
                    }
                });
                // for show the dialog box
                builder.show();
            }
        });

        // Display previous book details
        displaySpecificProductInfo();


    }///end on create ///

    // Method to mark a book as out of stock
    private void outOfStock() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Updating Book Details");
        progressDialog.setMessage("Please wait, while we are updating");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        productsRef.child("productState").setValue("Out of Stock")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        progressDialog.dismiss();

                        Toast.makeText(SellerMaintainProductsActivity.this, "The book is now Out of Stock", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(SellerMaintainProductsActivity.this, Seller_Home_Activity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    }
                });
    }

    // Method to delete a book
    private void deleteThisBook() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Updating Book Details");
        progressDialog.setMessage("Please wait, while we are updating");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        productsRef.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                progressDialog.dismiss();

                Toast.makeText(SellerMaintainProductsActivity.this, "This book deleted successfully", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(SellerMaintainProductsActivity.this, Seller_Home_Activity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });
    }

    // Method to apply changes to a book
    private void applyChanges() {
        String newBookName = SellerChangeBookName.getText().toString();
        String newBookAuthorName = SellerChangeBookAuthorName.getText().toString();
        String newBookCategory = autoCompleteTextView.getText().toString();
        String newBookDescription = SellerChangeBookDescription.getText().toString();
        String newBookPrise = SellerChangeBookPrice.getText().toString();

        // Get current date and time
        Calendar calendar = Calendar.getInstance();

        //for current date
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd,yyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());

        //for current time
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calendar.getTime());

        // Validate input fields
        if (TextUtils.isEmpty(newBookName)) {
            Toast.makeText(this, "Write book name", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(newBookAuthorName)) {
            Toast.makeText(this, "Write book's author name", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(newBookCategory)) {
            Toast.makeText(this, "Select Book Category", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(newBookDescription)) {
            Toast.makeText(this, "Write book description", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(newBookPrise)) {
            Toast.makeText(this, "Enter book price", Toast.LENGTH_SHORT).show();
        } else {

            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Updating Book Details");
            progressDialog.setMessage("Please wait, while we are updating");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            // Create HashMap to update product details
            HashMap<String, Object> productMap = new HashMap<>();

            productMap.put("date", saveCurrentDate);
            productMap.put("time", saveCurrentTime);
            productMap.put("bookName", newBookName);
            productMap.put("authorName", newBookAuthorName);
            productMap.put("category", newBookCategory);
            productMap.put("description", newBookDescription);
            productMap.put("price", newBookPrise);
            productMap.put("productState", "Pending");

            // Add seller details
            productMap.put("sid", sID);
            productMap.put("sellerName", sName);
            productMap.put("sellerPhone", sPhone);
            productMap.put("sellerEmail", sEmail);
            productMap.put("sellerAddress", sAddress);

            // Update product details in database
            productsRef.updateChildren(productMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        progressDialog.dismiss();
                        Toast.makeText(SellerMaintainProductsActivity.this, "Changes Apply Successfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(SellerMaintainProductsActivity.this, Seller_Home_Activity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    } else // if upload Unsuccessful
                    {
                        progressDialog.dismiss();
                        String message = task.getException().toString();
                        //display the error
                        Toast.makeText(SellerMaintainProductsActivity.this, "Error:" + message, Toast.LENGTH_SHORT).show();
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
                    // Get book details from database
                    String bName = snapshot.child("bookName").getValue().toString();
                    String bAuthorName = snapshot.child("authorName").getValue().toString();
                    String bCategory = snapshot.child("category").getValue().toString();
                    String bDescription = snapshot.child("description").getValue().toString();
                    String bPrice = snapshot.child("price").getValue().toString();
                    String bState = snapshot.child("productState").getValue().toString();
                    String bId = snapshot.child("bookId").getValue().toString();
                    String bImage = snapshot.child("image").getValue().toString();

                    // Display book details
                    SellerBookState.setText("State: " + bState);
                    SellerBookId.setText("Book Id: " + bId);
                    SellerChangeBookName.setText(bName);
                    SellerChangeBookAuthorName.setText(bAuthorName);
                    SellerChangeBookDescription.setText(bDescription);
                    SellerChangeBookPrice.setText(bPrice);

                    Picasso.get().load(bImage).into(SellerChangeBookImage);


                    autoCompleteTextView.setHint(bCategory);

                    // Change book state text color
                    if (bState.equals("Approved")) {
                        SellerBookState.setTextColor(Color.parseColor("#089C0F"));
                    } else if (bState.equals("Pending")) {
                        SellerBookState.setTextColor(Color.parseColor("#2370F4"));
                    } else if (bState.equals("Out of Stock")) {
                        SellerBookState.setTextColor(Color.parseColor("#F1724B"));
                    } else if (bState.equals("Edited by Admin")) {
                        SellerBookState.setTextColor(Color.parseColor("#EA45A7"));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle database error
            }
        });
    }

}/// end public class////