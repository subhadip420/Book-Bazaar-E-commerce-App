package com.example.bookbazaar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.bookbazaar.Model.UserOrders;
import com.example.bookbazaar.Prevalent.Prevalent;
import com.example.bookbazaar.ViewHolder.UserOrdersViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class User_Order_Activity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private DatabaseReference orderListRef;

    // DatabaseReference
    private DatabaseReference adminCartView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_order);

        recyclerView = findViewById(R.id.user_order_item_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(false);

        orderListRef = FirebaseDatabase.getInstance().getReference().child("Orders");

        // for clear cart view admin
        adminCartView = FirebaseDatabase.getInstance().getReference().child("Cart List").child("Admin View");

    }///On create End ///////////////

    // onStart method
    @Override
    protected void onStart() {
        super.onStart();

        // FirebaseRecyclerOptions to configure the adapter
        FirebaseRecyclerOptions<UserOrders> options
                = new FirebaseRecyclerOptions.Builder<UserOrders>()
                .setQuery(orderListRef.orderByChild("orderId").equalTo(Prevalent.currentOnlineUser.getPhone()), UserOrders.class)
                .build();

        // FirebaseRecyclerAdapter to populate the RecyclerView
        FirebaseRecyclerAdapter<UserOrders, UserOrdersViewHolder> adapter
                = new FirebaseRecyclerAdapter<UserOrders, UserOrdersViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull UserOrdersViewHolder holder, final int position, @NonNull UserOrders model) {
                // Bind data to the ViewHolder
                holder.userOrderStatus.setText("Status = " + model.getState());
                holder.userAddress.setText("Address: " + model.getAddress());
                holder.userCity.setText("City: " + model.getCity());
                holder.userOrderDateTime.setText("Order on: " + model.getDate() + model.getTime());
                holder.userName.setText(model.getName());
                holder.userPhoneNumber.setText(model.getPhone());
                holder.userPin.setText("Pin: " + model.getPin());
                holder.userOrderTotalAmount.setText("Total Amount= ₹ " + model.getTotalAmount());

                // On click order items
                holder.ShowOrdersBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String uID = getRef(position).getKey();

                        Intent intent = new Intent(User_Order_Activity.this, User_Order_Items_List_Activity.class);
                        intent.putExtra("uid", uID);
                        startActivity(intent);
                    }
                });

                // Click listener for cancelling an order
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // AlertDialog to confirm cancellation
                        CharSequence options[] = new CharSequence[]
                                {
                                        "Yes", "No"
                                };

                        AlertDialog.Builder builder = new AlertDialog.Builder(User_Order_Activity.this);
                        builder.setTitle("Are you cancel this order ?");

                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (i == 0) {
                                    String uID = getRef(position).getKey();

                                    // create method and call
                                    userRemoverOrder(uID);
                                } else {
                                    //null
                                }
                            }
                        });

                        builder.show();
                    }
                });


            }

            @NonNull
            @Override
            public UserOrdersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                // Create and return a new UserOrdersViewHolder
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_orders_item_layout, parent, false);
                return new UserOrdersViewHolder(view);

            }
        };
        // Set the adapter and start listening for data changes
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    // Method to cancel an order
    private void userRemoverOrder(String uID) {
        orderListRef.child(uID).removeValue();

        // Clear cart view for admin
        adminCartView.child(uID).child("Products").removeValue();
        Toast.makeText(this, "Your Order Successfully Deleted", Toast.LENGTH_SHORT).show();
    }
}/// end public class