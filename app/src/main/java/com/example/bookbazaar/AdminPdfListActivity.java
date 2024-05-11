package com.example.bookbazaar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.bookbazaar.Model.UploadPDF;
import com.example.bookbazaar.ViewHolder.PDFViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AdminPdfListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private DatabaseReference databaseReference;

    private Button addPDFButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_pdf_list);

        // Initialize UI elements
        addPDFButton = (Button) findViewById(R.id.admin_add_pdf_btn);

        // Initialize database reference
        databaseReference = FirebaseDatabase.getInstance().getReference().child("PDFs");

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.AdminPdfRecyclerView);
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Add click listener to add PDF button
        addPDFButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminPdfListActivity.this, AdminAddPdfActivity.class);
                startActivity(intent);
            }
        });

    }//end on create

    @Override
    protected void onStart() {
        super.onStart();

        // Configure FirebaseRecyclerAdapter
        FirebaseRecyclerOptions<UploadPDF> options =
                new FirebaseRecyclerOptions.Builder<UploadPDF>()
                        .setQuery(databaseReference, UploadPDF.class)
                        .build();

        FirebaseRecyclerAdapter<UploadPDF, PDFViewHolder> adapter =
                new FirebaseRecyclerAdapter<UploadPDF, PDFViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull PDFViewHolder holder, int position, @NonNull UploadPDF model) {
                        // Bind data to ViewHolder
                        holder.textViewTitle.setText(model.getPdfTitle());
                        holder.textViewAuthor.setText("by " + model.getPdfAuthor());
                        holder.textViewCategory.setText("Category: " + model.getPdfCategory());
                        holder.textViewDescription.setText("Description: " + model.getPdfDescription());


                        // Set click listener for each item in the RecyclerView
                        holder.itemView.setOnClickListener(view -> {

                            AlertDialog.Builder builder = new AlertDialog.Builder(AdminPdfListActivity.this);
                            builder.setTitle("Do you want to View or Edit this PDF ?");

                            builder.setPositiveButton("Edit", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Intent intent = new Intent(AdminPdfListActivity.this, AdminMaintainPdfActivity.class);
                                    intent.putExtra("pdfId", model.getPdfId());
                                    startActivity(intent);
                                }
                            });
                            builder.setNegativeButton("View", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Intent intent = new Intent(Intent.ACTION_VIEW);
                                    intent.setDataAndType(Uri.parse(model.getPdfUrl()), "application/pdf");
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                }
                            });
                            builder.show();
                        });
                    }

                    @NonNull
                    @Override
                    public PDFViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pdf_item_layout, parent, false);

                        PDFViewHolder holder = new PDFViewHolder(view);
                        return holder;
                    }
                };
        // Set adapter to the RecyclerView and start listening for changes in the data
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

}/// end public class