package com.example.bookbazaar;

import androidx.annotation.NonNull;
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
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class Seller_Add_New_Product_Activity extends AppCompatActivity {

    // Dropdown category list
    private String[] cat = {
            "Arts", "Astronomy", "Biography", "Biology", "Chemistry", "Comics and Cartoon", "Computer and Technology", "Dictionary",
            "Drama", "Drawing", "Engineering", "General Knowledge", "Geography", "History", "Horror Special", "Kids and Funny",
            "Mathematics", "Medical", "Motivation and Self-help", "Physics", "Religion and Spirituality", "Romantic", "Story", "Others"
    };

    // for dropdown category list
    private AutoCompleteTextView autoCompleteTextView;
    private ArrayAdapter<String> adapterItems;

    //for book categories
    private String CategoryName;

    // Book details
    private String BName, BAuthorName, BCategory, BDescription, BPrice;
    private ImageView InputSellerBookImage;
    private EditText InputSellerBookName, InputSellerBookAuthorName, InputSellerBookDescription, InputSellerBookPrice;
    private Button SellerAddNewBook;

    // Gallery
    private static final int GalleryPick = 1;
    private Uri ImageUri;

    // Date and time
    private String saveCurrentDate, saveCurrentTime;

    //for unique random key for every product date and time
    private String productRandomKey;

    //for store images in the firebase
    private StorageReference ProductImagesRef;

    //for link image Url to firebase
    private String downloadImageUrl;

    //for create a node in firebase as as product(books)
    private DatabaseReference ProductRef;

    // for online seller
    private String sName, sAddress, sPhone, sEmail, sID;

    // firebase for get online seller details
    private DatabaseReference sellerRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_add_new_product);

        //for store images in the firebase
        ProductImagesRef = FirebaseStorage.getInstance().getReference().child("Product Images");

        //for create a node in firebase as as product(books)
        ProductRef = FirebaseDatabase.getInstance().getReference().child("Products");

        // for get online seller details
        sellerRef = FirebaseDatabase.getInstance().getReference().child("Sellers");

        // Get online seller details
        sellerRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            sID = snapshot.child("sid").getValue().toString();
                            sName = snapshot.child("name").getValue().toString();
                            sPhone = snapshot.child("phone").getValue().toString();
                            sEmail = snapshot.child("email").getValue().toString();
                            sAddress = snapshot.child("address").getValue().toString();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        // Initialize views
        InputSellerBookImage = (ImageView) findViewById(R.id.seller_book_image);
        InputSellerBookName = (EditText) findViewById(R.id.seller_book_name);
        InputSellerBookAuthorName = (EditText) findViewById(R.id.seller_book_author_name);
        InputSellerBookDescription = (EditText) findViewById(R.id.seller_book_description_name);
        InputSellerBookPrice = (EditText) findViewById(R.id.seller_book_price);
        SellerAddNewBook = (Button) findViewById(R.id.seller_add_book_btn);

        // Dropdown category list
        autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.seller_book_category_name);
        adapterItems = new ArrayAdapter<String>(this, R.layout.admin_pdf_category_item, cat);
        autoCompleteTextView.setAdapter(adapterItems);

        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String item = adapterView.getItemAtPosition(i).toString();

                Toast.makeText(Seller_Add_New_Product_Activity.this, "Category: " + item, Toast.LENGTH_SHORT).show();
            }
        });

        // On click listener for adding book image
        InputSellerBookImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //open seller gallery to upload images
                //create methode and call
                OpenGallery();
            }
        });


        // On click listener for adding a new book
        SellerAddNewBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //for validate book
                //create methode and call
                ValidateBookData();
            }
        });

    }//close on create/////

    // Open gallery to select an image
    private void OpenGallery() {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, GalleryPick);
    }

    // After selecting image from gallery
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GalleryPick && resultCode == RESULT_OK && data != null) {
            ImageUri = data.getData();
            InputSellerBookImage.setImageURI(ImageUri);
        }
    }

    // Validate book data before adding
    private void ValidateBookData() {
        //convert all inputs to String
        BName = InputSellerBookName.getText().toString();
        BAuthorName = InputSellerBookAuthorName.getText().toString();
        BCategory = autoCompleteTextView.getText().toString();
        BDescription = InputSellerBookDescription.getText().toString();
        BPrice = InputSellerBookPrice.getText().toString();

        if (ImageUri == null) {
            Toast.makeText(this, "Book Image is Mandatory...", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(BName)) {
            Toast.makeText(this, "Book Name is Mandatory...", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(BAuthorName)) {
            Toast.makeText(this, "Author Name is Mandatory...", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(BCategory)) {
            Toast.makeText(this, "Category is Mandatory...", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(BDescription)) {
            Toast.makeText(this, "Book Description is Mandatory...", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(BPrice)) {
            Toast.makeText(this, "Book Price is Mandatory...", Toast.LENGTH_SHORT).show();
        } else {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Adding Book");
            progressDialog.setMessage("Please wait, while we are updating");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            //create methode for store book information into firebase
            //calling methode
            StoreBookInformation();
        }

    }//end validate book details

    // Store book information in the database
    private void StoreBookInformation() {
        //for store current date and time when book is added
        Calendar calendar = Calendar.getInstance();

        //for current date
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd,yyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());

        //for current time
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calendar.getTime());

        //for unique random key for every product date and time
        productRandomKey = saveCurrentDate + saveCurrentTime;

        //for store images in the firebase
        StorageReference filePath = ProductImagesRef.child(ImageUri.getLastPathSegment() + productRandomKey);

        //for upload image
        final UploadTask uploadTask = filePath.putFile(ImageUri);

        //for failure upload
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                //message which exception occure
                String message = e.toString();

                //display which error occure
                Toast.makeText(Seller_Add_New_Product_Activity.this, "Error:" + message, Toast.LENGTH_SHORT).show();

            } // on successful upload
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(Seller_Add_New_Product_Activity.this, "Book Image Uploaded Successfully", Toast.LENGTH_SHORT).show();

                //link image to firebase
                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }

                        downloadImageUrl = filePath.getDownloadUrl().toString();
                        return filePath.getDownloadUrl();
                    }

                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(Task<Uri> task) {
                        if (task.isSuccessful()) {
                            downloadImageUrl = task.getResult().toString();
                            Toast.makeText(Seller_Add_New_Product_Activity.this, "Got Book Image Url Successfully", Toast.LENGTH_SHORT).show();

                            // After get book Image we save book details to data base
                            //create a methode
                            SaveBookInfoToDatabase();
                        }
                    }
                });
            }
        });
    }

    // Save book information to the database
    private void SaveBookInfoToDatabase() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Adding Book");
        progressDialog.setMessage("Please wait, while we are updating");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        HashMap<String, Object> productMap = new HashMap<>();
        productMap.put("bookId", productRandomKey);
        productMap.put("date", saveCurrentDate);
        productMap.put("time", saveCurrentTime);
        productMap.put("image", downloadImageUrl);
        productMap.put("bookName", BName);
        productMap.put("authorName", BAuthorName);
        productMap.put("category", BCategory);
        productMap.put("description", BDescription);
        productMap.put("price", BPrice);
        productMap.put("productState", "Pending");

        //for seller details
        productMap.put("sellerName", sName);
        productMap.put("sellerAddress", sAddress);
        productMap.put("sellerPhone", sPhone);
        productMap.put("sellerEmail", sEmail);
        productMap.put("sid", sID);

        ProductRef.child(productRandomKey).updateChildren(productMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(Task<Void> task) {
                        progressDialog.dismiss();

                        if (task.isSuccessful()) {
                            // go back to seller home
                            Toast.makeText(Seller_Add_New_Product_Activity.this, "Book is Added Successfully...", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(Seller_Add_New_Product_Activity.this, Seller_Home_Activity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();

                        } else //toast if upload Unsuccessful
                        {
                            String message = task.getException().toString();

                            //display the error
                            Toast.makeText(Seller_Add_New_Product_Activity.this, "Error:" + message, Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }

}// close public class