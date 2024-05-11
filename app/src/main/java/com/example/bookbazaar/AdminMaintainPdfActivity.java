package com.example.bookbazaar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class AdminMaintainPdfActivity extends AppCompatActivity {
    // Array of categories for AutoCompleteTextView
    String[] cat = {
            "Arts", "Astronomy", "Biography", "Biology", "Chemistry", "Comics and Cartoon", "Computer and Technology", "Dictionary",
            "Drama", "Drawing", "Engineering", "General Knowledge", "Geography", "History", "Horror Special", "Kids and Funny",
            "Mathematics", "Medical", "Motivation and Self-help", "Physics", "Religion and Spirituality", "Romantic", "Story", "Others"
    };

    // UI elements
    private AutoCompleteTextView autoCompleteTextView;
    private ArrayAdapter<String> adapterItems;

    private EditText editTextBookName, editTextAuthorName, editTextBookDescription;
    private TextView textViewPdfId, textViewPdfDateTime;
    private Button updatePdfBtn, deletePdfBtn;

    // Current date and time
    private String saveCurrentDate, saveCurrentTime;

    // Product ID
    private String pdfID = "";

    // Database reference
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_maintain_pdf);

        // Get product ID from intent
        pdfID = getIntent().getStringExtra("pdfId");

        // Database reference
        databaseReference = FirebaseDatabase.getInstance().getReference().child("PDFs").child(pdfID);

        // Initialize UI elements
        textViewPdfId = findViewById(R.id.textViewPdfId);

        editTextBookName = (EditText) findViewById(R.id.updated_pdf_book_name);
        editTextAuthorName = (EditText) findViewById(R.id.updated_pdf_book_author_name);
        autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.updated_pdf_category);
        editTextBookDescription = (EditText) findViewById(R.id.updated_pdf_book_description);

        textViewPdfDateTime = findViewById(R.id.textViewPdfDateTime);

        updatePdfBtn = findViewById(R.id.admin_update_pdf_btn);
        deletePdfBtn = findViewById(R.id.admin_delete_pdf_btn);

        // Setup AutoCompleteTextView
        adapterItems = new ArrayAdapter<String>(this, R.layout.admin_pdf_category_item, cat);
        autoCompleteTextView.setAdapter(adapterItems);

        // Set item click listener for AutoCompleteTextView
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String item = adapterView.getItemAtPosition(i).toString();

                Toast.makeText(AdminMaintainPdfActivity.this, item, Toast.LENGTH_SHORT).show();
            }
        });

        // Display specific product info
        displaySpecificProductInfo();

        // Update PDF button click listener
        updatePdfBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Confirm update with a dialog
                CharSequence options[] = new CharSequence[]
                        {
                                "Yes",
                                "No"
                        };

                AlertDialog.Builder builder = new AlertDialog.Builder(AdminMaintainPdfActivity.this);
                builder.setTitle("Are you sure you want to edit pdf details ?");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (i == 0) {
                            // call this method
                            // Apply changes if confirmed
                            applyChanges();
                        } else {
                            // Do nothing
                        }
                    }
                });
                // for show the dialog box
                builder.show();
            }
        });

        // Delete PDF button click listener
        deletePdfBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Confirm deletion with a dialog
                deletePdf();
            }
        });

    }/////// end on create *****************


    // Method to delete PDF
    private void deletePdf() {

        AlertDialog.Builder builder = new AlertDialog.Builder(AdminMaintainPdfActivity.this);
        builder.setTitle("Are You sure you want to Delete this PDF ?");

        // Confirm deletion with user
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Remove PDF from database
                databaseReference.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // Notify user about successful deletion
                        Toast.makeText(AdminMaintainPdfActivity.this, "The PDF deleted successfully", Toast.LENGTH_SHORT).show();
                        // Redirect to PDF list activity
                        Intent intent = new Intent(AdminMaintainPdfActivity.this, AdminPdfListActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
            }
        });
        // Cancel deletion
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Do nothing
            }
        });
        builder.show();

    }

    // Method to apply changes to PDF details
    private void applyChanges() {
        // Get input values
        String pdfName = editTextBookName.getText().toString();
        String pdfAuthorName = editTextAuthorName.getText().toString();
        String pdfCategory = autoCompleteTextView.getText().toString();
        String pdfDescription = editTextBookDescription.getText().toString();

        // Validate inputs
        if (TextUtils.isEmpty(pdfName)) {
            Toast.makeText(this, "Please Enter PDF Name", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(pdfAuthorName)) {
            Toast.makeText(this, "Please Enter Author Name", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(pdfCategory)) {
            Toast.makeText(this, "Please Select Category", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(pdfDescription)) {
            Toast.makeText(this, "Please Enter PDF's Description'", Toast.LENGTH_SHORT).show();
        } else {
            // Show progress dialog
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Updating PDF");
            progressDialog.setMessage("Please wait, while we are updating");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            // Get current date and time
            Calendar calendar = Calendar.getInstance();

            //for current date
            SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd,yyyy");
            saveCurrentDate = currentDate.format(calendar.getTime());

            //for current time
            SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
            saveCurrentTime = currentTime.format(calendar.getTime());

            // Create map to store updated values
            Map<String, Object> pdfInfo = new HashMap<>();

            pdfInfo.put("pdfTitle", pdfName);
            pdfInfo.put("pdfAuthor", pdfAuthorName);
            pdfInfo.put("pdfCategory", pdfCategory);
            pdfInfo.put("pdfDescription", pdfDescription);
            pdfInfo.put("date", saveCurrentDate);
            pdfInfo.put("time", saveCurrentTime);

            // Update values in database
            databaseReference.updateChildren(pdfInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    // Dismiss progress dialog
                    progressDialog.dismiss();

                    if (task.isSuccessful()) {
                        // Notify user about successful update
                        Toast.makeText(AdminMaintainPdfActivity.this, "PDF Updated Successfully", Toast.LENGTH_SHORT).show();
                        // Redirect to PDF list activity
                        Intent intent = new Intent(AdminMaintainPdfActivity.this, AdminPdfListActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    } else {
                        // Notify user about error
                        String message = task.getException().toString();
                        Toast.makeText(AdminMaintainPdfActivity.this, "Error:" + message, Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }


    // Method to display specific PDF details
    private void displaySpecificProductInfo() {

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // Get PDF details from database
                    String pdfId = snapshot.child("pdfId").getValue().toString();
                    String pdfName = snapshot.child("pdfTitle").getValue().toString();
                    String pdfAuthorName = snapshot.child("pdfAuthor").getValue().toString();
                    String pdfCategory = snapshot.child("pdfCategory").getValue().toString();
                    String pdfDescription = snapshot.child("pdfDescription").getValue().toString();

                    String pdfDate = snapshot.child("date").getValue().toString();
                    String pdfTime = snapshot.child("time").getValue().toString();

                    // Display PDF details
                    textViewPdfId.setText("PDF Id: " + pdfId);
                    editTextBookName.setText(pdfName);
                    editTextAuthorName.setText(pdfAuthorName);
                    autoCompleteTextView.setHint(pdfCategory);
                    editTextBookDescription.setText(pdfDescription);
                    textViewPdfDateTime.setText("Last update on: " + pdfDate + " " + pdfTime);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle database error
            }
        });
    }

}//end public class