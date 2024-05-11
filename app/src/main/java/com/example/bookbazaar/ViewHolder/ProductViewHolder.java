package com.example.bookbazaar.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.bookbazaar.Interface.ItemClickListener;
import com.example.bookbazaar.R;

public class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    // TextViews and ImageView for displaying product details
    public TextView txtBookName, txtAuthorName, txtBookPrice;
    public ImageView imageView;

    // Interface instance for item click handling
    public ItemClickListener listner;

    // Constructor for initializing views
    public ProductViewHolder(View itemView) {
        super(itemView);

        // Linking views with their IDs
        imageView = (ImageView) itemView.findViewById(R.id.book_image);
        txtBookName = (TextView) itemView.findViewById(R.id.book_name);
        txtAuthorName = (TextView) itemView.findViewById(R.id.book_author_name);
        txtBookPrice = (TextView) itemView.findViewById(R.id.book_price);
    }

    // Method to set the item click listener
    public void setItemClickListner(ItemClickListener listner) {
        this.listner = listner;
    }

    // OnClickListener implementation
    @Override
    public void onClick(View view) {
        // Notify the listener when an item is clicked
        listner.onClick(view, getAdapterPosition(), false);
    }
}
