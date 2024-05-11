package com.example.bookbazaar.ViewHolder;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookbazaar.Interface.ItemClickListener;
import com.example.bookbazaar.R;

public class UserOrdersViewHolder extends RecyclerView.ViewHolder {

    // TextViews for displaying user order details
    public TextView userAddress, userCity, userOrderDateTime, userName, userPhoneNumber;
    public TextView userPin, userOrderStatus, userOrderTotalAmount;

    // Button for showing all orders
    public Button ShowOrdersBtn;

    // Interface instance for item click handling
    private ItemClickListener itemClickListner;

    // Constructor for initializing views
    public UserOrdersViewHolder(@NonNull View itemView) {
        super(itemView);

        // Linking views with their IDs in user_order_item_layout
        userAddress = (TextView) itemView.findViewById(R.id.user_order_item_address_details);
        userCity = (TextView) itemView.findViewById(R.id.user_order_item_city_details);
        userName = (TextView) itemView.findViewById(R.id.user_order_item_name_details);
        userPhoneNumber = (TextView) itemView.findViewById(R.id.user_order_item_phone_details);
        userPin = (TextView) itemView.findViewById(R.id.user_order_item_pin_details);
        userOrderDateTime = (TextView) itemView.findViewById(R.id.user_order_item_date_time_details);
        userOrderStatus = (TextView) itemView.findViewById(R.id.user_order_item_state_details);
        userOrderTotalAmount = (TextView) itemView.findViewById(R.id.user_order_item_totalPrice_details);

        // Initializing Button for showing all orders
        ShowOrdersBtn = (Button) itemView.findViewById(R.id.user_order_show_all_Item_btn);
    }
}
