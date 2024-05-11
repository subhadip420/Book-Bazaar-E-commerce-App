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
import android.widget.ImageView;
import android.widget.Toast;

import com.example.bookbazaar.Model.UploadPDF;
import com.example.bookbazaar.ViewHolder.PDFViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rey.material.widget.EditText;

import java.util.List;

public class User_Pdf_Activity extends AppCompatActivity {
    private Button pdfToHomeBtn;

    private RecyclerView recyclerView;
    private DatabaseReference databaseReference;

    //    //for search
    private Button searchButton;

    private ImageView searchBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_pdf);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("PDFs");

        // Initialize views and database reference
        pdfToHomeBtn = (Button) findViewById(R.id.pdf_to_bookBazaar_btn);
        searchButton = (Button) findViewById(R.id.search_button);
        searchBar = (ImageView) findViewById(R.id.pdf_search_bar_image);


        recyclerView = findViewById(R.id.pdfRecyclerView);
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Search button click listener to navigate to search activity
        searchBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(User_Pdf_Activity.this, UserPdfSearchActivity.class);
                startActivity(intent);
            }
        });

        // Button click listener to navigate back to the home activity
        pdfToHomeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(User_Pdf_Activity.this, "Book Bazaar", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(User_Pdf_Activity.this, User_Home_Content_Activity.class);
                startActivity(intent);
            }
        });

    }/// on create end ///////////


    @Override
    protected void onStart() {
        super.onStart();

        // Configure options for FirebaseRecyclerAdapter
        FirebaseRecyclerOptions<UploadPDF> options =
                new FirebaseRecyclerOptions.Builder<UploadPDF>()
                        .setQuery(databaseReference, UploadPDF.class)
                        .build();

        // Create and set FirebaseRecyclerAdapter
        FirebaseRecyclerAdapter<UploadPDF, PDFViewHolder> adapter =
                new FirebaseRecyclerAdapter<UploadPDF, PDFViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull PDFViewHolder holder, int position, @NonNull UploadPDF model) {
                        // Bind data to ViewHolder
                        holder.textViewTitle.setText(model.getPdfTitle());
                        holder.textViewAuthor.setText("by " + model.getPdfAuthor());
                        holder.textViewCategory.setText("Category: " + model.getPdfCategory());
                        holder.textViewDescription.setText("Description: " + model.getPdfDescription());

                        // Click listener to open the PDF
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
                        // Inflate the layout for each item in the RecyclerView
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pdf_item_layout, parent, false);
                        PDFViewHolder holder = new PDFViewHolder(view);
                        return holder;
                    }
                };
        // Set adapter and start listening for changes
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }
}///end public class