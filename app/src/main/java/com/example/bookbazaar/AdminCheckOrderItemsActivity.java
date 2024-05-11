package com.example.bookbazaar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bookbazaar.Model.Cart;
import com.example.bookbazaar.ViewHolder.CartViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AdminCheckOrderItemsActivity extends AppCompatActivity {
    private RecyclerView productsList;
    RecyclerView.LayoutManager layoutManager;
    private DatabaseReference cartListRef;

    private String userID = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_check_order_items);

        // Get user ID from intent
        userID = getIntent().getStringExtra("uid");

        // Initialize RecyclerView and LayoutManager
        productsList = findViewById(R.id.seller_user_order_item_recycler_view);
        productsList.setHasFixedSize(false);
        layoutManager = new LinearLayoutManager(this);
        productsList.setLayoutManager(layoutManager);

        // DatabaseReference for fetching cart items
        cartListRef = FirebaseDatabase.getInstance().getReference()
                .child("Cart List").child("Admin View").child(userID).child("Products");

    }// end on create *******

    // Method executed when activity starts
    @Override
    protected void onStart() {
        super.onStart();

        // Configure FirebaseRecyclerOptions
        FirebaseRecyclerOptions<Cart> options
                = new FirebaseRecyclerOptions.Builder<Cart>()
                .setQuery(cartListRef, Cart.class)
                .build();

        // Initialize FirebaseRecyclerAdapter
        FirebaseRecyclerAdapter<Cart, CartViewHolder> adapter = new FirebaseRecyclerAdapter<Cart, CartViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CartViewHolder holder, int position, @NonNull Cart model) {
                // Populate ViewHolder with cart item details
                holder.txtBookName.setText(model.getBookName());
                holder.txtAuthorName.setText(model.getBookAuthorName());
                holder.txtBookPrice.setText(model.getPrice());
                holder.txtBookQuantity.setText("Quantity = " + model.getQuantity());

                // Set click listener for each item to view details
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Redirect to details activity with book ID and hideButtons flag
                        Intent intent = new Intent(AdminCheckOrderItemsActivity.this, AdminCheckNewBooksDetailsActivity.class);
                        intent.putExtra("bookId", model.getBookId());
                        intent.putExtra("buttonVisibility", "hideButtons");
                        startActivity(intent);

                    }
                });

            }

            @NonNull
            @Override
            public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                // Inflate layout for each item in RecyclerView
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_items_layout, parent, false);
                CartViewHolder holder = new CartViewHolder(view);
                return holder;
            }
        };

        // Set adapter to RecyclerView and start listening for changes
        productsList.setAdapter(adapter);
        adapter.startListening();
    }

}/// end public class ****