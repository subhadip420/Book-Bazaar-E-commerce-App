package com.example.bookbazaar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.bookbazaar.Model.Products;
import com.example.bookbazaar.ViewHolder.ProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class AdminCheckNewBooksActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    private DatabaseReference unverifiedProductsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_check_new_books);

        // Initialize database reference
        unverifiedProductsRef = FirebaseDatabase.getInstance().getReference().child("Products");
        // Initialize RecyclerView
        recyclerView = findViewById(R.id.admin_check_new_book_recycler_view);
        recyclerView.setHasFixedSize(false);
        layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);


    }///end on create///


    // create on start method
    @Override
    protected void onStart() {
        super.onStart();

        // Define FirebaseRecyclerOptions
        FirebaseRecyclerOptions<Products> options
                = new FirebaseRecyclerOptions.Builder<Products>()
                .setQuery(unverifiedProductsRef.orderByChild("productState").equalTo("Pending"), Products.class)
                .build();

        // Initialize FirebaseRecyclerAdapter
        FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter
                = new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ProductViewHolder holder, int position, @NonNull Products model) {
                // Bind product data to ViewHolder
                holder.txtBookName.setText(model.getBookName());
                holder.txtAuthorName.setText("by " + model.getAuthorName());
                holder.txtBookPrice.setText("₹ " + model.getPrice());

                // Load product image using Picasso
                Picasso.get().load(model.getImage()).into(holder.imageView);

                // Set OnClickListener for viewing product details
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Pass product ID to the details activity
                        Intent intent = new Intent(AdminCheckNewBooksActivity.this, AdminCheckNewBooksDetailsActivity.class);
                        intent.putExtra("bookId", model.getBookId());
                        intent.putExtra("buttonVisibility", "viewButtons");
                        startActivity(intent);

                    }
                });
            }

            @NonNull
            @Override
            public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                // Inflate product item layout
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_items_layout, parent, false);

                ProductViewHolder holder = new ProductViewHolder(view);
                return holder;
            }
        };

        // Set adapter to RecyclerView and start listening for data changes
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }


}///end public class///