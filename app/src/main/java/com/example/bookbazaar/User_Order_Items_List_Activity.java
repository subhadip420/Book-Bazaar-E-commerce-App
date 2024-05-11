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

public class User_Order_Items_List_Activity extends AppCompatActivity {
    private RecyclerView productsList;
    RecyclerView.LayoutManager layoutManager;
    private DatabaseReference cartListRef;

    private String userID = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_order_items_list);

        // Get user ID from intent
        userID = getIntent().getStringExtra("uid");

        // Initialize RecyclerView and LayoutManager
        productsList = findViewById(R.id.user_purchase_item_recycler_view);
        productsList.setHasFixedSize(false);
        layoutManager = new LinearLayoutManager(this);
        productsList.setLayoutManager(layoutManager);

        // Reference to user's cart in the database
        cartListRef = FirebaseDatabase.getInstance().getReference()
                .child("Cart List").child("Admin View").child(userID).child("Products");


    }/// end on create ////

    // create on start method
    @Override
    protected void onStart() {
        super.onStart();

        // Configure options for FirebaseRecyclerAdapter
        FirebaseRecyclerOptions<Cart> options
                = new FirebaseRecyclerOptions.Builder<Cart>()
                .setQuery(cartListRef, Cart.class)
                .build();

        // Create and set FirebaseRecyclerAdapter
        FirebaseRecyclerAdapter<Cart, CartViewHolder> adapter = new FirebaseRecyclerAdapter<Cart, CartViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CartViewHolder holder, int position, @NonNull Cart model) {
                // Bind data to ViewHolder
                holder.txtBookName.setText(model.getBookName());
                holder.txtAuthorName.setText(model.getBookAuthorName());
                holder.txtBookPrice.setText(model.getPrice());
                holder.txtBookQuantity.setText("Quantity = " + model.getQuantity());

                // Click listener to view product details
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(User_Order_Items_List_Activity.this, UserProductDetailActivity.class);
                        intent.putExtra("bookId", model.getBookId());
                        startActivity(intent);
                    }
                });
            }

            @NonNull
            @Override
            public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                // Navigate to UserProductDetailActivity to view product details
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_items_layout, parent, false);
                CartViewHolder holder = new CartViewHolder(view);
                return holder;
            }
        };
        // Set adapter and start listening for changes
        productsList.setAdapter(adapter);
        adapter.startListening();
    }

}/// end public class /////////