package com.example.bookbazaar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.bookbazaar.Model.Products;
import com.example.bookbazaar.ViewHolder.ProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rey.material.widget.EditText;
import com.squareup.picasso.Picasso;

public class User_Search_Activity extends AppCompatActivity {

    //for top toolbar option menu
    Toolbar toolbar;

    //for bottom toolbar
    BottomNavigationView bottomNavigationView;

    //for search
    private Button searchButton;
    private EditText inputText;
    private RecyclerView searchList;

    private String SearchInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_search);

        // Initialize views

        //for top toolbar
        toolbar = findViewById(R.id.user_top_search_toolbar);
        //for bottom nav bar
        bottomNavigationView = findViewById(R.id.user_bottom_search_nav_bar);

        //for searching
        searchButton = (Button) findViewById(R.id.search_button);
        inputText = (EditText) findViewById(R.id.search_input_text);
        searchList = findViewById(R.id.search_item_recycler_view);

        searchList.setHasFixedSize(false);
        searchList.setLayoutManager(new GridLayoutManager(this, 2));

        // Handle bottom navigation item clicks
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                int btmItemId = item.getItemId();

                if (btmItemId == R.id.user_search_bottom_nav_home) {
                    Intent intent = new Intent(User_Search_Activity.this, User_Home_Content_Activity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                } else if (btmItemId == R.id.user_search_bottom_nav_categories) {
                    Intent intent = new Intent(User_Search_Activity.this, User_Category_List_Activity.class);
                    startActivity(intent);
                } else if (btmItemId == R.id.user_search_bottom_nav_cart) {
                    Intent intent = new Intent(User_Search_Activity.this, User_Cart_Activity.class);
                    startActivity(intent);
                } else if (btmItemId == R.id.user_search_bottom_nav_search) {
                    Toast.makeText(User_Search_Activity.this, "Search Here", Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });

        // Handle search button click
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SearchInput = inputText.getText().toString();
                onStart(); // Refresh search results
            }
        });
    }///end on create//////////

    // create on start
    @Override
    protected void onStart() {
        super.onStart();

        // Query the database for products matching the search input
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Products");

        FirebaseRecyclerOptions<Products> options
                = new FirebaseRecyclerOptions.Builder<Products>()
                .setQuery(reference.orderByChild("bookName").startAt(SearchInput), Products.class)
                .build();

        // Set up the RecyclerView adapter to display search results
        FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter
                = new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ProductViewHolder holder, int position, @NonNull Products model) {
                // Bind product data to ViewHolder
                holder.txtBookName.setText(model.getBookName());
                holder.txtAuthorName.setText("by " + model.getAuthorName());
                holder.txtBookPrice.setText("₹ " + model.getPrice());

                // for get image from firebase
                Picasso.get().load(model.getImage()).into(holder.imageView);


                // Click listener to view product details
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(User_Search_Activity.this, UserProductDetailActivity.class);
                        intent.putExtra("bookId", model.getBookId());
                        startActivity(intent);

                        //Toast.makeText(Home_Content_Activity.this, "clicked", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @NonNull
            @Override
            public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                // Inflate layout for individual product items
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_items_layout, parent, false);
                ProductViewHolder holder = new ProductViewHolder(view);
                return holder;
            }
        };
        // Set adapter for the RecyclerView
        searchList.setAdapter(adapter);
        adapter.startListening();
    }
}//end public class