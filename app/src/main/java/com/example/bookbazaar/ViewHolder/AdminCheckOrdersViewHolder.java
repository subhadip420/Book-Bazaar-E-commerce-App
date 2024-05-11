package com.example.bookbazaar.ViewHolder;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookbazaar.R;

public class AdminCheckOrdersViewHolder extends RecyclerView.ViewHolder {

    // Views for displaying order details
    public TextView userOrderBy, userAddress, userCity, userOrderDateTime, userName, userPhoneNumber;
    public TextView userPin, userOrderStatus, userOrderTotalAmount;

    // Buttons for actions related to orders
    public Button orderPhone, orderWhatsapp;
    public Button ShowOrdersBtn;

    // Constructor for initializing views
    public AdminCheckOrdersViewHolder(@NonNull View itemView) {
        super(itemView);

        // Initializing TextViews for order details
        userOrderBy = (TextView) itemView.findViewById(R.id.order_by_details);
        userAddress = (TextView) itemView.findViewById(R.id.order_item_address_details);
        userCity = (TextView) itemView.findViewById(R.id.order_item_city_details);
        userName = (TextView) itemView.findViewById(R.id.order_item_name_details);
        userPhoneNumber = (TextView) itemView.findViewById(R.id.order_item_phone_details);
        userPin = (TextView) itemView.findViewById(R.id.order_item_pin_details);
        userOrderDateTime = (TextView) itemView.findViewById(R.id.order_item_date_time_details);
        userOrderStatus = (TextView) itemView.findViewById(R.id.order_item_state_details);
        userOrderTotalAmount = (TextView) itemView.findViewById(R.id.order_item_totalPrice_details);

        // Initializing Buttons for order actions
        ShowOrdersBtn = (Button) itemView.findViewById(R.id.order_show_all_Item_btn);
        orderPhone = (Button) itemView.findViewById(R.id.user_order_phone_btn);
        orderWhatsapp = (Button) itemView.findViewById(R.id.user_order_whatsapp_btn);

    }
}
