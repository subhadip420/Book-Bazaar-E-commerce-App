package com.example.bookbazaar.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookbazaar.Interface.ItemClickListener;
import com.example.bookbazaar.R;

public class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    // TextViews for displaying book details
    public TextView txtBookName, txtAuthorName, txtBookPrice, txtBookQuantity;

    // Interface for item click handling
    private ItemClickListener itemClickListner;

    // Constructor for initializing views
    public CartViewHolder(@NonNull View itemView) {
        super(itemView);

        // Initializing TextViews for book details
        txtBookName = itemView.findViewById(R.id.cart_list_book_name_details);
        txtAuthorName = itemView.findViewById(R.id.cart_list_book_author_name_details);
        txtBookPrice = itemView.findViewById(R.id.cart_list_book_price_details);
        txtBookQuantity = itemView.findViewById(R.id.cart_list_book_quantity_details);
    }

    // OnClickListener implementation
    @Override
    public void onClick(View view) {
        itemClickListner.onClick(view, getAbsoluteAdapterPosition(), false);
    }

    // Method to set the item click listener
    public void setItemClickListner(ItemClickListener itemClickListner) {
        this.itemClickListner = itemClickListner;
    }
}
