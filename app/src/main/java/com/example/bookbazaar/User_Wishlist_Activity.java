package com.example.bookbazaar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.bookbazaar.Model.Wish;
import com.example.bookbazaar.Prevalent.Prevalent;
import com.example.bookbazaar.ViewHolder.WishViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class User_Wishlist_Activity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_wishlist);

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.wishlist_item_recycler_menu);
        recyclerView.setHasFixedSize(false);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);


    }/// end on create///

    // to retrive book details
    @Override
    protected void onStart() {
        super.onStart();

        final DatabaseReference wishListRef = FirebaseDatabase.getInstance().getReference().child("Wish List");

        // Configure FirebaseRecyclerAdapter to fetch wish list items
        FirebaseRecyclerOptions<Wish> options =
                new FirebaseRecyclerOptions.Builder<Wish>()
                        .setQuery(wishListRef.child("User View")
                                .child(Prevalent.currentOnlineUser.getPhone()).child("Products"), Wish.class)
                        .build();

        // Create FirebaseRecyclerAdapter
        FirebaseRecyclerAdapter<Wish, WishViewHolder> adapter
                = new FirebaseRecyclerAdapter<Wish, WishViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull WishViewHolder holder, int position, @NonNull Wish model) {
                // Bind wish list item data to ViewHolder
                holder.txtBookName.setText(model.getBookName());
                holder.txtAuthorName.setText(model.getBookAuthorName());
                holder.txtBookPrice.setText(model.getPrice());

                // Handle item click events
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Show options when item is clicked
                        CharSequence options[] = new CharSequence[]
                                {
                                        "View",
                                        "Remove"
                                };
                        AlertDialog.Builder builder = new AlertDialog.Builder(User_Wishlist_Activity.this);
                        builder.setTitle("Wishlist Options:");
                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (i == 0) {
                                    // View item details
                                    Intent intent = new Intent(User_Wishlist_Activity.this, UserProductDetailActivity.class);
                                    intent.putExtra("bookId", model.getBookId());
                                    startActivity(intent);
                                }
                                if (i == 1) {
                                    // Remove item from wishlist
                                    wishListRef.child("User View")
                                            .child(Prevalent.currentOnlineUser.getPhone())
                                            .child("Products")
                                            .child(model.getBookId())
                                            .removeValue()
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Toast.makeText(User_Wishlist_Activity.this, "Item Removed Successfully", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                }
                            }
                        });
                        // for shoe dialog box
                        builder.show();
                    }
                });
            }

            @NonNull
            @Override
            public WishViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                // Inflate layout for wish list item
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.wishlist_items_layout, parent, false);
                WishViewHolder holder = new WishViewHolder(view);
                return holder;
            }
        };
        // Set adapter for RecyclerView
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }//end on start

}/// end public class/////////