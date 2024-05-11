package com.example.bookbazaar.ViewHolder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookbazaar.Interface.SelectListener;
import com.example.bookbazaar.Model.CategoryText;
import com.example.bookbazaar.R;
import com.example.bookbazaar.ViewHolder.CategoryTextViewHolder;

import java.util.List;

public class CategoryTextAdapter extends RecyclerView.Adapter<CategoryTextViewHolder> {
    private Context context;
    private List<CategoryText> list;

    // Interface instance for item selection handling
    private SelectListener listener;


    // Constructor to initialize the adapter with necessary data and listener
    public CategoryTextAdapter(Context context, List<CategoryText> list, SelectListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;

    }
    // Inflating the layout and creating ViewHolder
    @NonNull
    @Override
    public CategoryTextViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CategoryTextViewHolder(LayoutInflater.from(context).inflate(R.layout.category_item_layout, parent, false));

    }
    // Binding data to ViewHolder
    @Override
    public void onBindViewHolder(@NonNull CategoryTextViewHolder holder, int position) {
        // Set the category name
        holder.categoryName.setText(list.get(position).getCategory());

        // Set click listener for the card item
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Notify the listener when an item is clicked
                listener.onItemClicked(list.get(position));
            }
        });
    }
    // Return the total number of items in the list
    @Override
    public int getItemCount() {
        return list.size();
    }
}
