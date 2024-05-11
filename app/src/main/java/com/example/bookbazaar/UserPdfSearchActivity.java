package com.example.bookbazaar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.bookbazaar.Model.UploadPDF;
import com.example.bookbazaar.ViewHolder.PDFViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rey.material.widget.EditText;

public class UserPdfSearchActivity extends AppCompatActivity {
    // Search components
    private Button searchButton;
    private EditText inputText;
    private String SearchInput;

    // RecyclerView to display search results
    private RecyclerView searchList;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_pdf_search);

        // Initialize Firebase database reference
        databaseReference = FirebaseDatabase.getInstance().getReference().child("PDFs");

        // Initialize search components
        searchButton = (Button) findViewById(R.id.search_button);
        inputText = (EditText) findViewById(R.id.search_input_text);

        // Initialize RecyclerView
        searchList = findViewById(R.id.pdfSearchRecyclerView);
        searchList.setHasFixedSize(false);
        searchList.setLayoutManager(new LinearLayoutManager(this));

        // Search button click listener
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get the search input text
                SearchInput = inputText.getText().toString();
                // Start searching
                onStart();
            }
        });

    }//end on create *********

    @Override
    protected void onStart() {
        super.onStart();

        // Configure FirebaseRecyclerAdapter to display search results
        FirebaseRecyclerOptions<UploadPDF> options =
                new FirebaseRecyclerOptions.Builder<UploadPDF>()
                        .setQuery(databaseReference.orderByChild("pdfTitle").startAt(SearchInput), UploadPDF.class)
                        .build();

        // Set firebase recycler adapter
        FirebaseRecyclerAdapter<UploadPDF, PDFViewHolder> adapter =
                new FirebaseRecyclerAdapter<UploadPDF, PDFViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull PDFViewHolder holder, int position, @NonNull UploadPDF model) {
                        // Bind PDF data to ViewHolder
                        holder.textViewTitle.setText(model.getPdfTitle());
                        holder.textViewAuthor.setText("by " + model.getPdfAuthor());
                        holder.textViewCategory.setText("Category: " + model.getPdfCategory());
                        holder.textViewDescription.setText("Description: " + model.getPdfDescription());

                        // Click listener to open PDF link
                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(Intent.ACTION_VIEW);
                                intent.setDataAndType(Uri.parse(model.getPdfUrl()), "application/pdf");
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                            }
                        });
                    }

                    @NonNull
                    @Override
                    public PDFViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        // Inflate layout for PDF item
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pdf_item_layout, parent, false);
                        // Create ViewHolder
                        PDFViewHolder holder = new PDFViewHolder(view);
                        return holder;
                    }
                };
        // Set adapter to RecyclerView
        searchList.setAdapter(adapter);
        // Start listening for changes
        adapter.startListening();
    }

}//end public class