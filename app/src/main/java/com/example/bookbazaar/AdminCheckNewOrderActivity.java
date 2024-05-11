package com.example.bookbazaar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.bookbazaar.Model.AdminCheckOrders;
import com.example.bookbazaar.ViewHolder.AdminCheckOrdersViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AdminCheckNewOrderActivity extends AppCompatActivity {
    private RecyclerView ordersList;
    private DatabaseReference ordersRef;

    // DatabaseReference for clearing cart view
    private DatabaseReference adminCartView;

    // Variables to store order details temporarily
    private String orderBy, orderPhone, orderDate, orderTime, orderStatus, orderName, orderCity, orderPin, orderAddress, orderTotalAmount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_check_new_order);


        // Initialize DatabaseReference for orders
        ordersRef = FirebaseDatabase.getInstance().getReference().child("Orders");

        // Initialize DatabaseReference for clearing cart view
        adminCartView = FirebaseDatabase.getInstance().getReference().child("Cart List").child("Admin View");

        // Initialize RecyclerView for displaying orders
        ordersList = findViewById(R.id.seller_order_item_recycler_menu);
        ordersList.setLayoutManager(new LinearLayoutManager(this));


    }/// end on create //////

    // create on start
    @Override
    protected void onStart() {
        super.onStart();

        // FirebaseRecyclerOptions to configure adapter
        FirebaseRecyclerOptions<AdminCheckOrders> options
                = new FirebaseRecyclerOptions.Builder<AdminCheckOrders>()
                .setQuery(ordersRef, AdminCheckOrders.class)
                .build();

        // FirebaseRecyclerAdapter to populate RecyclerView with orders
        FirebaseRecyclerAdapter<AdminCheckOrders, AdminCheckOrdersViewHolder> adapter
                = new FirebaseRecyclerAdapter<AdminCheckOrders, AdminCheckOrdersViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull AdminCheckOrdersViewHolder holder, final int position, @NonNull AdminCheckOrders model) {

                // Populate ViewHolder with order details
                holder.userOrderBy.setText("Order by: " + model.getOrderId());
                holder.userOrderStatus.setText("Status = " + model.getState());
                holder.userAddress.setText("Address: " + model.getAddress());
                holder.userCity.setText("City: " + model.getCity());
                holder.userOrderDateTime.setText("Order at: " + model.getDate() + " " + model.getTime());
                holder.userName.setText("Name: " + model.getName());
                holder.userPhoneNumber.setText("Phone: " + model.getPhone());
                holder.userPin.setText("Pin No: " + model.getPin());
                holder.userOrderTotalAmount.setText("Total Amount= ₹ " + model.getTotalAmount());

                //for message order details
                orderBy = model.getOrderId();
                orderPhone = model.getPhone();
                orderDate = model.getDate();
                orderTime = model.getTime();
                orderStatus = model.getState();
                orderName = model.getName();
                orderCity = model.getCity();
                orderPin = model.getPin();
                orderAddress = model.getAddress();
                orderTotalAmount = model.getTotalAmount();

                // on click on phone
                holder.orderPhone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        CharSequence options[] = new CharSequence[]
                                {
                                        "Customer", "Receiver"
                                };

                        AlertDialog.Builder builder = new AlertDialog.Builder(AdminCheckNewOrderActivity.this);
                        builder.setTitle("Select to call ?");

                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (i == 0) {
                                    //to call customer
                                    orderCustomerPhone();
                                } else {
                                    //to call receiver
                                    orderReceiverPhone();
                                }
                            }
                        });

                        // for show the dialog box
                        builder.show();
                    }
                });

                // on click on whatsapp
                holder.orderWhatsapp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        CharSequence options[] = new CharSequence[]
                                {
                                        "Customer", "Receiver"
                                };

                        AlertDialog.Builder builder = new AlertDialog.Builder(AdminCheckNewOrderActivity.this);
                        builder.setTitle("Select to message?");

                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (i == 0) {
                                    //to message customer
                                    orderCustomerWhatsapp();
                                } else {
                                    //to message receiver
                                    orderReceiverWhatsapp();
                                }
                            }
                        });

                        // for show the dialog box
                        builder.show();
                    }
                });

                // on click on view order items
                holder.ShowOrdersBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String uID = getRef(position).getKey();

                        Intent intent = new Intent(AdminCheckNewOrderActivity.this, AdminCheckOrderItemsActivity.class);
                        intent.putExtra("uid", uID);
                        startActivity(intent);
                    }
                });

                //seller click on any item
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        CharSequence options[] = new CharSequence[]
                                {
                                        "Yes", "No"
                                };

                        AlertDialog.Builder builder = new AlertDialog.Builder(AdminCheckNewOrderActivity.this);
                        builder.setTitle("Have you shipped this order products?");

                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (i == 0) {
                                    String uID = getRef(position).getKey();

                                    RemoverOrder(uID);
                                } else {
                                    //null
                                }
                            }
                        });

                        // for show the dialog box
                        builder.show();
                    }
                });

            }

            @NonNull
            @Override
            public AdminCheckOrdersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                // Inflate layout for individual order item view
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.seller_orders_item_layout, parent, false);
                return new AdminCheckOrdersViewHolder(view);
            }
        };

        // Set adapter to RecyclerView
        ordersList.setAdapter(adapter);

        // Start listening for changes in Firebase database
        adapter.startListening();

    }


    // Method to message customer via WhatsApp
    private void orderCustomerWhatsapp() {


        //customer phone number
        String whatsappPhone = orderBy;

        String whatsappMessage = "Order By: " + orderBy + "\n"
                + "Order on: " + orderDate + " " + orderTime + "\n"
                + "Order State: " + orderStatus + "\n"
                + "Name: " + orderName + "\n"
                + "Phone: " + orderPhone + "\n"
                + "City: " + orderCity + "\n"
                + "Pin: " + orderPin + "\n"
                + "Delivery Address: " + orderAddress + "\n"
                + "Total Amount: " + orderTotalAmount + "\n";


        boolean installed = isAppInstalled("com.whatsapp");

        if (installed) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("http://api.whatsapp.com/send?phone=" + whatsappPhone + "&text=" + whatsappMessage));
            startActivity(intent);
        } else {
            Toast.makeText(AdminCheckNewOrderActivity.this, "Whatsapp is not installed or failed to connect", Toast.LENGTH_LONG).show();
        }
    }


    // Method to message receiver via WhatsApp
    private void orderReceiverWhatsapp() {
        //receiver phone number
        String whatsappPhone = orderPhone;

        String whatsappMessage = "Order By: " + orderBy + "\n"
                + "Order on: " + orderDate + " " + orderTime + "\n"
                + "Order State: " + orderStatus + "\n"
                + "Name: " + orderName + "\n"
                + "Phone: " + orderPhone + "\n"
                + "City: " + orderCity + "\n"
                + "Pin: " + orderPin + "\n"
                + "Delivery Address: " + orderAddress + "\n"
                + "Total Amount: " + orderTotalAmount + "\n";


        boolean installed = isAppInstalled("com.whatsapp");

        if (installed) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("http://api.whatsapp.com/send?phone=" + whatsappPhone + "&text=" + whatsappMessage));
            startActivity(intent);
        } else {
            Toast.makeText(AdminCheckNewOrderActivity.this, "Whatsapp is not installed or failed to connect", Toast.LENGTH_LONG).show();
        }
    }

    // Method to check if WhatsApp is installed
    private boolean isAppInstalled(String s) {
        PackageManager packageManager = getPackageManager();
        boolean is_installed;

        try {
            packageManager.getPackageInfo(s, PackageManager.GET_ACTIVITIES);
            is_installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            is_installed = false;
            e.printStackTrace();
        }
        return is_installed;
    }

    // Method to call customer
    private void orderCustomerPhone() {

        // customer phone number
        String phoneNumber = "tel:" + orderBy;

        Intent callIntent = new Intent(Intent.ACTION_DIAL, Uri.parse(phoneNumber));
        startActivity(callIntent);
    }

    // Method to call receiver
    private void orderReceiverPhone() {
        // receiver phone number
        String phoneNumber = "tel:" + orderPhone;

        Intent callIntent = new Intent(Intent.ACTION_DIAL, Uri.parse(phoneNumber));
        startActivity(callIntent);
    }

    // Method to remove order and clear cart view
    private void RemoverOrder(String uID) {
        //for clear order view in user
        ordersRef.child(uID).removeValue();

        // for clear cart view admin
        adminCartView.child(uID).child("Products").removeValue();
        Toast.makeText(this, "Order Items Shipped Successfully", Toast.LENGTH_SHORT).show();

    }

}/// end public class/////