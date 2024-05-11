package com.example.bookbazaar.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookbazaar.R;

public class CategoryTextViewHolder extends RecyclerView.ViewHolder {

    // TextView for displaying category name
    public TextView categoryName;

    // CardView for the category item
    public CardView cardView;

    // Constructor for initializing views
    public CategoryTextViewHolder(@NonNull View itemView) {
        super(itemView);

        // Initialize TextView for category name
        categoryName = itemView.findViewById(R.id.category_name_text_details);

        // Initialize CardView for category item
        cardView = itemView.findViewById(R.id.category_item_details_card);
    }
}
