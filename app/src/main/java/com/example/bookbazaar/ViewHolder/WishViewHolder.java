package com.example.bookbazaar.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookbazaar.Interface.ItemClickListener;
import com.example.bookbazaar.R;

public class WishViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    // TextViews for displaying wishlist item details
    public TextView txtBookName, txtAuthorName, txtBookPrice;

    // Interface instance for item click handling
    private ItemClickListener itemClickListner;

    // Constructor for initializing views
    public WishViewHolder(@NonNull View itemView) {
        super(itemView);
        // Linking views with their IDs
        txtBookName = itemView.findViewById(R.id.wishlist_book_name_details);
        txtAuthorName = itemView.findViewById(R.id.wishlist_book_author_name_details);
        txtBookPrice = itemView.findViewById(R.id.wishlist_book_price_details);
    }

    // OnClickListener implementation
    @Override
    public void onClick(View view) {
        // Notify the listener when an item is clicked
        itemClickListner.onClick(view, getAbsoluteAdapterPosition(), false);
    }

    // Method to set the item click listener
    public void setItemClickListner(ItemClickListener itemClickListner) {
        this.itemClickListner = itemClickListner;
    }
}
