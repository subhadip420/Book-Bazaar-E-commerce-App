package com.example.bookbazaar.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookbazaar.Interface.ItemClickListener;
import com.example.bookbazaar.R;

public class SellerItemsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    // TextViews and ImageView for displaying seller item details
    public TextView sellerBookName, sellerBookAuthorName, sellerBookPrice, sellerBookState;
    public ImageView sellerBookImage;

    // Interface instance for item click handling
    public ItemClickListener listner;

    // Constructor for initializing views
    public SellerItemsViewHolder(@NonNull View itemView) {
        super(itemView);

        // Linking views with their IDs
        sellerBookImage = (ImageView) itemView.findViewById(R.id.view_seller_book_image);
        sellerBookName = (TextView) itemView.findViewById(R.id.view_seller_book_name);
        sellerBookAuthorName = (TextView) itemView.findViewById(R.id.view_seller_book_author_name);
        sellerBookPrice = (TextView) itemView.findViewById(R.id.view_seller_book_price);
        sellerBookState = (TextView) itemView.findViewById(R.id.view_seller_book_state);

    }

    // Method to set the item click listener
    public void setItemClickListner(ItemClickListener listner) {
        this.listner = listner;
    }

    // OnClickListener implementation
    @Override
    public void onClick(View view) {
        // Notify the listener when an item is clicked
        listner.onClick(view, getAbsoluteAdapterPosition(), false);
    }
}
