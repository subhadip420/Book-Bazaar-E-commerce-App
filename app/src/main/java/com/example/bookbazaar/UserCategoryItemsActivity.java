package com.example.bookbazaar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.bookbazaar.Model.Products;
import com.example.bookbazaar.ViewHolder.ProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

public class UserCategoryItemsActivity extends AppCompatActivity {

    //to get book category
    private String bookCategory = "";

    TextView categoryTextView;

    private RecyclerView singleCategoryList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_category_items);

        // Initialize views
        categoryTextView = (TextView) findViewById(R.id.textView_single_category);
        singleCategoryList = findViewById(R.id.single_category_item_recycler_view);
        singleCategoryList.setHasFixedSize(false);
        singleCategoryList.setLayoutManager(new GridLayoutManager(this, 2));

        // Get the selected category from the intent
        bookCategory = getIntent().getStringExtra("categoryName");

        // Set the category text
        categoryTextView.setText(bookCategory);

    }/// end on create

    // create on start
    @Override
    protected void onStart() {
        super.onStart();

        // Database reference
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Products");

        // Query to fetch products of the selected category
        FirebaseRecyclerOptions<Products> options
                = new FirebaseRecyclerOptions.Builder<Products>()
                .setQuery(reference.orderByChild("category").startAt(bookCategory), Products.class)
                .build();

        // Firebase Recycler Adapter to populate the RecyclerView with products
        FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter
                = new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ProductViewHolder holder, int position, @NonNull Products model) {
                // Set product details to the views
                holder.txtBookName.setText(model.getBookName());
                holder.txtAuthorName.setText("by " + model.getAuthorName());
                holder.txtBookPrice.setText("₹ " + model.getPrice());

                // For get image from firebase
                Picasso.get().load(model.getImage()).into(holder.imageView);


                // Handle click event to view product details
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(UserCategoryItemsActivity.this, UserProductDetailActivity.class);
                        intent.putExtra("bookId", model.getBookId());
                        startActivity(intent);
                    }
                });
            }

            @NonNull
            @Override
            public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                // Inflate the product item layout
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_items_layout, parent, false);
                ProductViewHolder holder = new ProductViewHolder(view);
                return holder;
            }
        };

        // Set adapter to the RecyclerView and start listening for changes
        singleCategoryList.setAdapter(adapter);
        adapter.startListening();
    }

}/// end public class