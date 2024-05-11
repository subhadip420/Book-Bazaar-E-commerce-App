package com.example.bookbazaar.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookbazaar.Interface.ItemClickListener;
import com.example.bookbazaar.R;

public class PDFViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    // TextViews for displaying PDF book details
    public TextView textViewTitle, textViewAuthor, textViewCategory, textViewDescription;

    // Interface instance for item click handling
    public ItemClickListener clickListener;

    // Constructor for initializing views
    public PDFViewHolder(@NonNull View itemView) {
        super(itemView);

        // Initialize TextViews for PDF book details
        textViewTitle = itemView.findViewById(R.id.pdf_book_name);
        textViewAuthor = itemView.findViewById(R.id.pdf_book_author_name);
        textViewCategory = itemView.findViewById(R.id.pdf_book_category);
        textViewDescription = itemView.findViewById(R.id.pdf_book_description);
    }

    // Method to set the item click listener
    public void setItemClickListner(ItemClickListener listner) {
        this.clickListener = listner;
    }

    // OnClickListener implementation
    @Override
    public void onClick(View view) {
        clickListener.onClick(view, getAdapterPosition(), false);
    }

}
