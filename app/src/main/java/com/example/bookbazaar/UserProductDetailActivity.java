package com.example.bookbazaar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.bookbazaar.Model.Products;
import com.example.bookbazaar.Prevalent.Prevalent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class UserProductDetailActivity extends AppCompatActivity {

    private ImageView bookImage;
    private TextView bookName, bookAuthorName, bookPrice, bookCategory, bookDescription;
    private ElegantNumberButton numberButton;
    private Button addToCartBtn;
    private ImageView addToWishlistBtn;


    // Variables to get product ID and order state
    private String productID = "", state = "Normal";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_product_detail);

        // Get product ID from intent
        productID = getIntent().getStringExtra("bookId");

        // Initialize views
        bookImage = (ImageView) findViewById(R.id.product_image_details);
        bookName = (TextView) findViewById(R.id.product_BookName_details);
        bookAuthorName = (TextView) findViewById(R.id.product_authorName_details);
        bookPrice = (TextView) findViewById(R.id.product_bookPrice_details);
        bookCategory = (TextView) findViewById(R.id.product_bookCategory_details);
        bookDescription = (TextView) findViewById(R.id.product_bookDescription_details);
        numberButton = (ElegantNumberButton) findViewById(R.id.product_elegantNumberButton);
        addToCartBtn = (Button) findViewById(R.id.product_addToCart_btn);
        addToWishlistBtn = (ImageView) findViewById(R.id.product_addTo_wishlist_btn);

        // Load product details
        getProductDetails(productID);

        // Add to cart button click listener
        addToCartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Check order state
                if (state.equals("Order Placed") || state.equals("Order Shipped")) {
                    Toast.makeText(UserProductDetailActivity.this, "You can add to cart more products, when your previous order is shipped or confirmed", Toast.LENGTH_LONG).show();
                } else {
                    // Add to cart
                    addingToCartList();
                }
            }
        });

        // Add to wishlist button click listener
        addToWishlistBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Add to wishlist
                addingToWishlist();
            }
        });

    }///end on create ///

    //create on start methode
    @Override
    protected void onStart() {
        super.onStart();
        // Check order state
        CheckOrderState();
    }

    // Method to add product to wishlist
    private void addingToWishlist() {
        // Get current date and time
        String saveCurrentTime, saveCurrentDate;

        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy ");
        saveCurrentDate = currentDate.format(calForDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat(" HH:MM:ss a");
        saveCurrentTime = currentTime.format(calForDate.getTime());

        // Create wishlist reference
        final DatabaseReference wishListRef = FirebaseDatabase.getInstance().getReference().child("Wish List");

        final HashMap<String, Object> wishMap = new HashMap<>();
        wishMap.put("bookId", productID);
        wishMap.put("bookName", bookName.getText().toString());
        wishMap.put("bookAuthorName", bookAuthorName.getText().toString());
        wishMap.put("price", bookPrice.getText().toString());
        wishMap.put("date", saveCurrentDate);
        wishMap.put("time", saveCurrentTime);


        // Add to wishlist in database
        wishListRef.child("User View").child(Prevalent.currentOnlineUser.getPhone())
                .child("Products").child(productID)
                .updateChildren(wishMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()) {
                            Toast.makeText(UserProductDetailActivity.this, "Added to Wist List", Toast.LENGTH_SHORT).show();

                            // Change wishlist button image
                            addToWishlistBtn.setImageResource(R.drawable.ic_favorite_red);
                        }
                    }
                });
    }

    // Method to add product to cart
    private void addingToCartList() {
        // Get current date and time
        String saveCurrentTime, saveCurrentDate;

        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy ");
        saveCurrentDate = currentDate.format(calForDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat(" HH:MM:ss a");
        saveCurrentTime = currentTime.format(calForDate.getTime());

        // Create cart reference
        final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("Cart List");

        final HashMap<String, Object> cartMap = new HashMap<>();
        cartMap.put("bookId", productID);
        cartMap.put("bookName", bookName.getText().toString());
        cartMap.put("bookAuthorName", bookAuthorName.getText().toString());
        cartMap.put("price", bookPrice.getText().toString());
        cartMap.put("date", saveCurrentDate);
        cartMap.put("time", saveCurrentTime);
        cartMap.put("quantity", numberButton.getNumber());
        cartMap.put("discount", "");

        // Add to cart in database
        cartListRef.child("User View").child(Prevalent.currentOnlineUser.getPhone())
                .child("Products").child(productID)
                .updateChildren(cartMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // Add to admin view for user cart list
                            cartListRef.child("Admin View").child(Prevalent.currentOnlineUser.getPhone())
                                    .child("Products").child(productID)
                                    .updateChildren(cartMap)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(UserProductDetailActivity.this, "Added to Cart List", Toast.LENGTH_SHORT).show();
                                                // Start animation activity
                                                Intent intent = new Intent(UserProductDetailActivity.this, UserAddedToCartAnimActivity.class);
                                                startActivity(intent);
                                            }
                                        }
                                    });
                        }
                    }
                });
    }

    // Method to display product details
    private void getProductDetails(String productID) {
        DatabaseReference productsRef = FirebaseDatabase.getInstance().getReference().child("Products");

        productsRef.child(productID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // Get product details
                    Products products = snapshot.getValue(Products.class);
                    // Set product details to views
                    bookName.setText((products.getBookName()));
                    bookAuthorName.setText("by " + (products.getAuthorName()));
                    bookPrice.setText((products.getPrice()));
                    bookCategory.setText("Category:- " + (products.getCategory()));
                    bookDescription.setText("Details: " + (products.getDescription()));
                    // Load product image using Picasso library
                    Picasso.get().load(products.getImage()).into(bookImage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle database error
            }
        });
    }

    // Method to check order state
    private void CheckOrderState() {
        DatabaseReference ordersRef;
        ordersRef = FirebaseDatabase.getInstance().getReference().child("Orders").child(Prevalent.currentOnlineUser.getPhone());
        ordersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String shippingState = snapshot.child("State").getValue().toString();

                    if (shippingState.equals("shipped")) {
                        state = "Order Shipped";
                    } else if (shippingState.equals("not shipped")) {
                        state = "Order Placed";
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle database error
            }
        });
    }
}