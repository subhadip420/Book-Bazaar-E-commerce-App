package com.example.bookbazaar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bookbazaar.Prevalent.Prevalent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class UserConfirmFinalOrderActivity extends AppCompatActivity {

    private EditText nameEditText, phoneEditText, pinEditText, cityEditText, addressEditText;
    private Button confirmOrderBtn;

    private TextView totalPriseView;

    //to get total price
    private String totalAmount = "";

    //for notifications
//    private  static final String CHANNEL_ID = "Alert Channel" ;
//    private  static  final int NOTIFICATION_ID = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_confirm_final_order);

        // Initialize views
        confirmOrderBtn = (Button) findViewById(R.id.confirm_order_btn);

        nameEditText = (EditText) findViewById(R.id.shipment_full_name);
        phoneEditText = (EditText) findViewById(R.id.shipment_phone_number);
        pinEditText = (EditText) findViewById(R.id.shipment_pin_number);
        cityEditText = (EditText) findViewById(R.id.shipment_city);
        addressEditText = (EditText) findViewById(R.id.shipment_address);

        totalPriseView = (TextView) findViewById(R.id.confirm_view_total_price);

        // Get total price from the intent
        totalAmount = getIntent().getStringExtra("Total Price");
        totalPriseView.setText("Total Price = ₹ " + String.valueOf(totalAmount));

        // Set onclick listener for confirm order button
        confirmOrderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Check if all fields are filled
                checkFields();
            }
        });


//        //for notification test
//        Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.order_confirm, null);
//        BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
//        Bitmap largeIcon = bitmapDrawable.getBitmap();
//
//        NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//        Notification orderConfirm;
//
//        // checking if android version is greater than oreo(API 26) or not
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//
//            orderConfirm = new Notification.Builder(this)
//                    .setLargeIcon(largeIcon)
//                    .setSmallIcon(R.drawable.app_logo_new)
//                    .setContentText("Your order successfully placed, soon it will be delivered")
//                    .setSubText("Book Bazaar Order")
//                    .setChannelId(CHANNEL_ID)
//                    .build();
//            nm.createNotificationChannel(new NotificationChannel(CHANNEL_ID, "alert", NotificationManager.IMPORTANCE_HIGH));
//        }
//        else
//        {
//            orderConfirm = new Notification.Builder(this)
//                    .setLargeIcon(largeIcon)
//                    .setSmallIcon(R.drawable.app_logo_new)
//                    .setContentText("Your order successfully placed, soon it will be delivered")
//                    .setSubText("Book Bazaar Order")
//                    .build();
//        }
//        nm.notify(NOTIFICATION_ID, orderConfirm);


    }/// end On Create /////


    // Check if all fields are filled
    private void checkFields() {
        if (TextUtils.isEmpty(nameEditText.getText().toString())) {
            Toast.makeText(this, "Please provide your full name", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(phoneEditText.getText().toString())) {
            Toast.makeText(this, "Please provide your phone number", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(pinEditText.getText().toString())) {
            Toast.makeText(this, "Please provide your pin number", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(cityEditText.getText().toString())) {
            Toast.makeText(this, "Please provide your city name", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(addressEditText.getText().toString())) {
            Toast.makeText(this, "Please provide your full address", Toast.LENGTH_SHORT).show();
        } else {
            // If all fields are filled, confirm the order
            ConfirmOrder();
        }
    }

    // Confirm the order
    private void ConfirmOrder() {
        // Show progress dialog
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Updating Order Details");
        progressDialog.setMessage("Please wait, while we are updating");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        // Get current date and time
        final String saveCurrentTime, saveCurrentDate;
        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy ");
        saveCurrentDate = currentDate.format(calForDate.getTime());
        SimpleDateFormat currentTime = new SimpleDateFormat(" HH:MM:ss a");
        saveCurrentTime = currentTime.format(calForDate.getTime());

        // Reference to the Orders node in the database
        final DatabaseReference ordersRef = FirebaseDatabase.getInstance().getReference()
                .child("Orders")
                .child(Prevalent.currentOnlineUser.getPhone());

        // Create a HashMap to store order details
        HashMap<String, Object> ordersMap = new HashMap<>();

        ordersMap.put("orderId", Prevalent.currentOnlineUser.getPhone());
        ordersMap.put("totalAmount", totalAmount);
        ordersMap.put("name", nameEditText.getText().toString());
        ordersMap.put("phone", phoneEditText.getText().toString());
        ordersMap.put("pin", pinEditText.getText().toString());
        ordersMap.put("city", cityEditText.getText().toString());
        ordersMap.put("address", addressEditText.getText().toString());
        ordersMap.put("date", saveCurrentDate);
        ordersMap.put("time", saveCurrentTime);
        ordersMap.put("State", "not shipped");

        // Update order details in the database
        ordersRef.updateChildren(ordersMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    // Clear the user's cart after placing the order
                    FirebaseDatabase.getInstance().getReference().child("Cart List")
                            .child("User View")
                            .child(Prevalent.currentOnlineUser.getPhone())
                            .removeValue()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    //for create notification
                                    // notification();

                                    progressDialog.dismiss();

                                    Toast.makeText(UserConfirmFinalOrderActivity.this, "Your final has been placed successfully", Toast.LENGTH_SHORT).show();
                                    // Navigate to order successful animation activity
                                    Intent intent = new Intent(UserConfirmFinalOrderActivity.this, UserOrderSuccessfulAnimActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                    finish();
                                }
                            });
                }
            }
        });

    }

//    // create notification if order confirm
//    private void notification()
//    {
//        Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.order_confirm, null);
//        BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
//        Bitmap largeIcon = bitmapDrawable.getBitmap();
//
//        NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//        Notification orderConfirm;
//
//        // checking if android version is greater than oreo(API 26) or not
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//
//            orderConfirm = new Notification.Builder(this)
//                    .setLargeIcon(largeIcon)
//                    .setSmallIcon(R.drawable.app_logo_new)
//                    .setContentText("Book Bazaar Order")
//                    .setSubText("Your order successfully placed, soon it will be delivered")
//                    .setChannelId(CHANNEL_ID)
//                    .build();
//            nm.createNotificationChannel(new NotificationChannel(CHANNEL_ID, "alert", NotificationManager.IMPORTANCE_HIGH));
//        }
//        else
//        {
//            orderConfirm = new Notification.Builder(this)
//                    .setLargeIcon(largeIcon)
//                    .setSmallIcon(R.drawable.app_logo_new)
//                    .setContentText("Book Bazaar Order")
//                    .setSubText("Your order successfully placed, soon it will be delivered")
//                    .build();
//        }
//
//        nm.notify(NOTIFICATION_ID, orderConfirm);
//
//    }


}///end public class