package com.example.bookbazaar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class AdminAddPdfActivity extends AppCompatActivity {
    // Array for dropdown category list
    String[] cat = {
            "Arts", "Astronomy", "Biography", "Biology", "Chemistry", "Comics and Cartoon", "Computer and Technology", "Dictionary",
            "Drama", "Drawing", "Engineering", "General Knowledge", "Geography", "History", "Horror Special", "Kids and Funny",
            "Mathematics", "Medical", "Motivation and Self-help", "Physics", "Religion and Spirituality", "Romantic", "Story", "Others"
    };

    // Views
    // for dropdown category list
    private AutoCompleteTextView autoCompleteTextView;
    private ArrayAdapter<String> adapterItems;

    // Variables to hold PDF data
    private String pdfBName, pdfBAuthorName, pdfCategory, pdfBDescription;

    // Request code for selecting a PDF file
    private static final int PICK_PDF_REQUEST = 1;

    private EditText editTextBookName, editTextAuthorName, editTextBookDescription;
    private TextView textViewSelectedFile;
    private Button buttonChooseFile, buttonUpload;

    // URI to hold the selected PDF file
    private Uri pdfUri;

    // Firebase references for storage and database
    private StorageReference storageReference;
    private DatabaseReference databaseReference;

    // Current date and time
    private String saveCurrentDate, saveCurrentTime;

    // Unique key for each PDF
    private String pdfRandomKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_pdf);

        // Initialize Firebase references
        storageReference = FirebaseStorage.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("PDFs");

        // Initialize views
        editTextBookName = (EditText) findViewById(R.id.pdf_book_name);
        editTextAuthorName = (EditText) findViewById(R.id.pdf_book_author_name);
        autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.admin_pdf_category);
        editTextBookDescription = (EditText) findViewById(R.id.admin_pdf_book_description);
        textViewSelectedFile = findViewById(R.id.textViewSelectedFile);
        buttonChooseFile = findViewById(R.id.admin_choose_pdf_btn);
        buttonUpload = findViewById(R.id.admin_upload_pdf_btn);


        // Setting up the dropdown category list
        adapterItems = new ArrayAdapter<String>(this, R.layout.admin_pdf_category_item, cat);
        autoCompleteTextView.setAdapter(adapterItems);

        // Handle category selection
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String item = adapterView.getItemAtPosition(i).toString();
                Toast.makeText(AdminAddPdfActivity.this, "Category: " + item, Toast.LENGTH_SHORT).show();
            }
        });


        // Choose file button click listener
        buttonChooseFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseFile();
            }
        });

        // Upload button click listener
        buttonUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //for validate book
                //create methode and call
                ValidateBookData();
            }
        });


    }// on create end ****

    // Method to choose a PDF file from device storage
    private void chooseFile() {
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select PDF"), PICK_PDF_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Handle the result of choosing a PDF file
        if (requestCode == PICK_PDF_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            pdfUri = data.getData();
            textViewSelectedFile.setText("File Selected: " + data.getData().getLastPathSegment());
        }
    }

    // Method to validate the entered PDF data
    private void ValidateBookData() {
        //convert all inputs to String
        pdfBName = editTextBookName.getText().toString();
        pdfBAuthorName = editTextAuthorName.getText().toString();
        pdfBDescription = editTextBookDescription.getText().toString();
        pdfCategory = autoCompleteTextView.getText().toString();

        if (pdfUri == null) {
            Toast.makeText(this, "Select pdf file.", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(pdfBName)) {
            Toast.makeText(this, "Enter book name", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(pdfBAuthorName)) {
            Toast.makeText(this, "Enter book's author name", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(pdfCategory)) {
            Toast.makeText(this, "Select book's category", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(pdfBDescription)) {
            Toast.makeText(this, "Enter book's description", Toast.LENGTH_SHORT).show();
        } else {

            // All data is valid, proceed with uploading the PDF file
            //calling methode
            StoreBookInformation();
        }
    }

    // Method to store book information in Firebase
    private void StoreBookInformation() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading File");
        progressDialog.setMessage("Please wait, while we are uploading");
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

        //for unique random key for every product date and time
        pdfRandomKey = saveCurrentDate + saveCurrentTime;

        // Create a reference for the PDF file in Firebase Storage
        StorageReference fileReference = storageReference.child("PDFs/" + pdfRandomKey + ".pdf");

        // Upload the PDF file to Firebase Storage
        fileReference.putFile(pdfUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // If upload is successful, get the download URL of the PDF file
                        fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                //convert all inputs to String
                                pdfBName = editTextBookName.getText().toString();
                                pdfBAuthorName = editTextAuthorName.getText().toString();
                                pdfCategory = autoCompleteTextView.getText().toString();
                                pdfBDescription = editTextBookDescription.getText().toString();
                                String downloadUrl = uri.toString();

                                // Upload the book information along with the download URL to Firebase Database
                                uploadPdfInfo(pdfBName, pdfBAuthorName, pdfCategory, pdfBDescription, downloadUrl);

                                progressDialog.dismiss();
                            }
                        });

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // If upload fails, display an error message
                        Toast.makeText(AdminAddPdfActivity.this, "Upload failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();

                        progressDialog.dismiss();
                    }
                });

    }

    // Method to upload PDF information to Firebase Database
    private void uploadPdfInfo(String pdfBName, String pdfBAuthorName, String pdfCategory, String pdfBDescription, String downloadUrl) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading File");
        progressDialog.setMessage("Please wait, while we are uploading");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        // Create a HashMap to store PDF information
        Map<String, Object> pdfInfo = new HashMap<>();

        pdfInfo.put("pdfId", pdfRandomKey);
        pdfInfo.put("date", saveCurrentDate);
        pdfInfo.put("time", saveCurrentTime);
        pdfInfo.put("pdfTitle", pdfBName);
        pdfInfo.put("pdfAuthor", pdfBAuthorName);
        pdfInfo.put("pdfCategory", pdfCategory);
        pdfInfo.put("pdfDescription", pdfBDescription);
        pdfInfo.put("pdfUrl", downloadUrl);

        // Upload the PDF information to Firebase Database
        databaseReference.child(pdfRandomKey).updateChildren(pdfInfo)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            progressDialog.dismiss();
                            Toast.makeText(AdminAddPdfActivity.this, "PDF uploaded successfully", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(AdminAddPdfActivity.this, AdminHomeActivity.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(i);
                            finish();
                        } else //toast if upload Unsuccessful
                        {
                            progressDialog.dismiss();

                            // If upload fails, display an error message
                            String message = task.getException().toString();

                            //display the error
                            Toast.makeText(AdminAddPdfActivity.this, "Error:" + message, Toast.LENGTH_SHORT).show();

                        }
                    }
                });


    }//end upload pdf info *******


}// end public class *****