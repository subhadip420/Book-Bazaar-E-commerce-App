package com.example.bookbazaar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import com.example.bookbazaar.Model.SellerProducts;
import com.example.bookbazaar.ViewHolder.SellerItemsViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class Seller_Home_Activity extends AppCompatActivity {

    // Declaring variables
    private BottomNavigationView sellerBottomNavigationView;

    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    private DatabaseReference sellerProductsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_home);

        // Initialize Firebase database reference
        sellerProductsRef = FirebaseDatabase.getInstance().getReference().child("Products");

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.seller_order_item_recycler_menu);
        recyclerView.setHasFixedSize(false);
        layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);

        // Initialize bottom navigation view
        sellerBottomNavigationView = findViewById(R.id.seller_bottom_nav_bar);

        // Handle navigation item selection
        sellerBottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                int btmItemId = item.getItemId();

                if (btmItemId == R.id.seller_nav_home) {
                    Toast.makeText(Seller_Home_Activity.this, "This is Dashboard", Toast.LENGTH_SHORT).show();
                } else if (btmItemId == R.id.seller_nav_add_items) {
                    Intent intent = new Intent(Seller_Home_Activity.this, Seller_Add_New_Product_Activity.class);
                    startActivity(intent);
                } else if (btmItemId == R.id.seller_nav_profile) {
                    Intent intent = new Intent(Seller_Home_Activity.this, Seller_Profile_Activity.class);
                    startActivity(intent);
                }

                return true;
            }
        });

    } ///  end on create  /////

    @Override
    protected void onStart() {
        super.onStart();

        // Set up FirebaseRecyclerAdapter to populate the RecyclerView with seller's products
        FirebaseRecyclerOptions<SellerProducts> options
                = new FirebaseRecyclerOptions.Builder<SellerProducts>()
                .setQuery(sellerProductsRef.orderByChild("sid").equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid()), SellerProducts.class)
                .build();

        FirebaseRecyclerAdapter<SellerProducts, SellerItemsViewHolder> adapter
                = new FirebaseRecyclerAdapter<SellerProducts, SellerItemsViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull SellerItemsViewHolder holder, int position, @NonNull SellerProducts model) {
                // Bind product data to the ViewHolder
                holder.sellerBookName.setText(model.getBookName());
                holder.sellerBookAuthorName.setText("by " + model.getAuthorName());
                holder.sellerBookPrice.setText("₹ " + model.getPrice());
                holder.sellerBookState.setText("State- " + model.getProductState());

                // Load product image using Picasso library
                Picasso.get().load(model.getImage()).into(holder.sellerBookImage);

                // Change text color based on product state
                if (model.getProductState().equals("Approved")) {
                    holder.sellerBookState.setTextColor(Color.parseColor("#089C0F"));
                } else if (model.getProductState().equals("Pending")) {
                    holder.sellerBookState.setTextColor(Color.parseColor("#2370F4"));
                } else if (model.getProductState().equals("Out of Stock")) {
                    holder.sellerBookState.setTextColor(Color.parseColor("#F1724B"));
                } else if (model.getProductState().equals("Edited by Admin")) {
                    holder.sellerBookState.setTextColor(Color.parseColor("#EA45A7"));
                }

                // Handle item click to navigate to product details
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(Seller_Home_Activity.this, SellerMaintainProductsActivity.class);
                        intent.putExtra("bookId", model.getBookId());
                        startActivity(intent);
                    }
                });
            }

            @NonNull
            @Override
            public SellerItemsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                // Create ViewHolder for each item in the RecyclerView
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.seller_items_layout, parent, false);
                SellerItemsViewHolder holder = new SellerItemsViewHolder(view);
                return holder;
            }
        };

        // Set the adapter to the RecyclerView and start listening for data changes
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

}////  end public class /////////