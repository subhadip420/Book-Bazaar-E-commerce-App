package com.example.bookbazaar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.example.bookbazaar.Model.Cart;
import com.example.bookbazaar.Prevalent.Prevalent;
import com.example.bookbazaar.ViewHolder.CartViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class User_Cart_Activity extends AppCompatActivity {
    // for top toolbar option menu
    Toolbar toolbar;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private Button NextProcessBtn;
    private TextView totalPrice, txtmsg1;


    // for total price
    private int overTotalPrice = 0;

    //for bottom toolbar
    BottomNavigationView bottomNavigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_cart);


        // Initializing top toolbar
        toolbar = findViewById(R.id.user_top_cart_toolbar);
        // Initializing bottom navigation bar
        bottomNavigationView = findViewById(R.id.user_bottom_cart_nav_bar);

        recyclerView = findViewById(R.id.cart_item_recycler_menu);
        recyclerView.setHasFixedSize(false);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        NextProcessBtn = (Button) findViewById(R.id.cart_product_order_btn);
        totalPrice = (TextView) findViewById(R.id.cart_total_price);

        txtmsg1 = (TextView) findViewById(R.id.msg1);

        // Click listener for order now button
        NextProcessBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Navigate to the order confirmation page
                Intent intent = new Intent(User_Cart_Activity.this, UserConfirmFinalOrderActivity.class);
                intent.putExtra("Total Price", String.valueOf(overTotalPrice));
                startActivity(intent);
                finish();
            }
        });

        // Bottom navigation item selection listener
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                int btmItemId = item.getItemId();

                // Handling different navigation item selections
                if (btmItemId == R.id.user_cart_bottom_nav_home) {
                    Intent intent = new Intent(User_Cart_Activity.this, User_Home_Content_Activity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                } else if (btmItemId == R.id.user_cart_bottom_nav_categories) {
                    Intent intent = new Intent(User_Cart_Activity.this, User_Category_List_Activity.class);
                    startActivity(intent);
                } else if (btmItemId == R.id.user_cart_bottom_nav_cart) {
                    Toast.makeText(User_Cart_Activity.this, "Your Cart", Toast.LENGTH_SHORT).show();
                } else if (btmItemId == R.id.user_cart_bottom_nav_search) {

                    Toast.makeText(User_Cart_Activity.this, "Search Here", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(User_Cart_Activity.this, User_Search_Activity.class);
                    startActivity(intent);
                }

                return true;
            }
        });

    }//// end on create //////


    // to retreave book details
    @Override
    protected void onStart() {
        super.onStart();

        // Call methods to initialize and check cart state
        CheckOrderState();

        // Create method if cart list is empty then order btn is invisible
        CartState();

        // Firebase reference to fetch cart items
        final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("Cart List");

        FirebaseRecyclerOptions<Cart> options =
                new FirebaseRecyclerOptions.Builder<Cart>()
                        .setQuery(cartListRef.child("User View")
                                .child(Prevalent.currentOnlineUser.getPhone())
                                .child("Products"), Cart.class)
                        .build();

        FirebaseRecyclerAdapter<Cart, CartViewHolder> adapter
                = new FirebaseRecyclerAdapter<Cart, CartViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CartViewHolder holder, int position, @NonNull Cart model) {

                // Binding data to the RecyclerView item views
                holder.txtBookName.setText(model.getBookName());
                holder.txtAuthorName.setText(model.getBookAuthorName());
                holder.txtBookPrice.setText(model.getPrice());
                holder.txtBookQuantity.setText("Quantity = " + model.getQuantity());


                // Calculate total price
                //for one type product total price
                int oneTypeProductTPrice = ((Integer.valueOf(model.getPrice()))) * Integer.valueOf(model.getQuantity());
                overTotalPrice = overTotalPrice + oneTypeProductTPrice;

                //for display total price
                totalPrice.setText("Total Price = ₹" + String.valueOf(overTotalPrice));

                // OnClickListener for RecyclerView items
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        CharSequence options[] = new CharSequence[]
                                {
                                        "View",
                                        "Remove"
                                };
                        AlertDialog.Builder builder = new AlertDialog.Builder(User_Cart_Activity.this);
                        builder.setTitle("Cart Options:");
                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (i == 0) {
                                    Intent intent = new Intent(User_Cart_Activity.this, UserProductDetailActivity.class);
                                    intent.putExtra("bookId", model.getBookId());
                                    startActivity(intent);
                                }
                                if (i == 1) {
                                    // Removing item from the cart
                                    cartListRef.child("User View")
                                            .child(Prevalent.currentOnlineUser.getPhone())
                                            .child("Products")
                                            .child(model.getBookId())
                                            .removeValue()
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    cartListRef.child("Admin View")
                                                            .child(Prevalent.currentOnlineUser.getPhone())
                                                            .child("Products")
                                                            .child(model.getBookId())
                                                            .removeValue()
                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    if (task.isSuccessful()) {
                                                                        Toast.makeText(User_Cart_Activity.this, "Item Removed Successfully", Toast.LENGTH_SHORT).show();
                                                                    }
                                                                }
                                                            });

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
            public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                // Inflating layout for RecyclerView items
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_items_layout, parent, false);
                CartViewHolder holder = new CartViewHolder(view);
                return holder;
            }
        };

        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    // Method to check cart state and update UI
    private void CartState() {
        DatabaseReference cartRef;
        cartRef = FirebaseDatabase.getInstance().getReference()
                .child("Cart List")
                .child("User View")
                .child(Prevalent.currentOnlineUser.getPhone());

        cartRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()) {
                    NextProcessBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Toast.makeText(User_Cart_Activity.this, "Your Cart is Empty", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    // Method to check order state and update UI
    private void CheckOrderState() {
        DatabaseReference ordersRef;
        ordersRef = FirebaseDatabase.getInstance().getReference().child("Orders").child(Prevalent.currentOnlineUser.getPhone());

        ordersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String shippingState = snapshot.child("State").getValue().toString();
                    String userName = snapshot.child("name").getValue().toString();

                    if (shippingState.equals("shipped")) {
                        totalPrice.setText("Dear" + userName + "\n order is shipped successfully.");
                        recyclerView.setVisibility(View.GONE);

                        txtmsg1.setVisibility(View.VISIBLE);
                        NextProcessBtn.setVisibility(View.GONE);

                        txtmsg1.setText("Congratulations, your final order has been Shipped successfully. Soon will received your order at your address.");

                        Toast.makeText(User_Cart_Activity.this, "You can purchase more product, once you received your previous order", Toast.LENGTH_SHORT).show();
                    } else if (shippingState.equals("not shipped")) {
                        totalPrice.setText("Shipping State = Not Shipped");
                        recyclerView.setVisibility(View.GONE);

                        txtmsg1.setVisibility(View.VISIBLE);
                        NextProcessBtn.setVisibility(View.GONE);

                        Toast.makeText(User_Cart_Activity.this, "You can purchase more product, once you received your previous order", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}/// end public class ////////