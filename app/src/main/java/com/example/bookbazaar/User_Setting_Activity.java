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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bookbazaar.Prevalent.Prevalent;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class User_Setting_Activity extends AppCompatActivity {

    // Views and variables
    private CircleImageView userProfileImageView;
    private EditText userNameEditText, userPhoneEditText, userAddressEditText;
    private TextView profileImageChangeTxtBtn, closeSettingTxtBtn, updateSettingTxtBtn;
    private Button securityQuestionBtn;
    private TextView phoneNumber;

    // For storing profile picture in Firebase Storage
    private Uri imageUri;
    private String myUrl = "";
    private StorageTask uploadTask;
    private StorageReference storageProfilePictureRef;
    private String checker = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_setting);

        // Initialize Firebase Storage reference
        storageProfilePictureRef = FirebaseStorage.getInstance().getReference().child("Profile Pictures");

        // Initialize views
        userProfileImageView = (CircleImageView) findViewById(R.id.user_settings_profile_image);
        userNameEditText = (EditText) findViewById(R.id.user_setting_full_name);
        userAddressEditText = (EditText) findViewById(R.id.user_setting_address);

        profileImageChangeTxtBtn = findViewById(R.id.user_profile_image_change_btn);
        closeSettingTxtBtn = findViewById(R.id.user_settings_close_btn);
        updateSettingTxtBtn = findViewById(R.id.user_settings_update_btn);
        securityQuestionBtn = findViewById(R.id.security_question_btn);
        phoneNumber = (TextView) findViewById(R.id.textView_User_phone);


        // Display user information
        userInfoDisplay(userProfileImageView, userNameEditText, userPhoneEditText, userAddressEditText);

        // Set security question button click listener
        securityQuestionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(User_Setting_Activity.this, UserResetPasswordActivity.class);
                intent.putExtra("check", "settings");
                startActivity(intent);
            }
        });

        // Close settings activity
        closeSettingTxtBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // Update user information
        updateSettingTxtBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checker.equals("clicked")) {
                    // call this methode
                    userInfoSaved();
                } else {
                    // call this methode
                    updateOnlyUserInfo();
                }
            }
        });

        // Change profile image
        profileImageChangeTxtBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checker = "clicked";

                // crop image features from gallery
                // start cropping activity for pre-acquired image saved on the device
                CropImage.activity(imageUri)
                        .setAspectRatio(1, 1)
                        .start(User_Setting_Activity.this);
            }
        });


    }////////// on create end //////////////////

    // Handle result after opening gallery for image selection
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            imageUri = result.getUri();
            userProfileImageView.setImageURI(imageUri);
        }

        //if image not selected
        else {
            Toast.makeText(this, "Error, Try Again", Toast.LENGTH_SHORT).show();

            startActivity(new Intent(User_Setting_Activity.this, User_Setting_Activity.class));
            finish();
        }
    }

    // Update user information in Firebase Realtime Database
    private void updateOnlyUserInfo() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users");

        HashMap<String, Object> userMap = new HashMap<>();
        userMap.put("name", userNameEditText.getText().toString());
        userMap.put("address", userAddressEditText.getText().toString());

        ref.child(Prevalent.currentOnlineUser.getPhone()).updateChildren(userMap);
        startActivity(new Intent(User_Setting_Activity.this, User_Home_Content_Activity.class));

        Toast.makeText(User_Setting_Activity.this, "Profile info Updated", Toast.LENGTH_SHORT).show();
        finish();
    }

    // Save user information and upload profile image to Firebase Storage
    private void userInfoSaved() {
        if (TextUtils.isEmpty(userNameEditText.getText().toString())) {
            Toast.makeText(this, "Name is Mandatory", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(userAddressEditText.getText().toString())) {
            Toast.makeText(this, "Address is Mandatory", Toast.LENGTH_SHORT).show();
        } else if (checker.equals("clicked")) {
            // call this methode
            uploadImage();
        }
    }

    // Upload profile image to Firebase Storage
    private void uploadImage() {
        // set progress dialog
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Update Profile");
        progressDialog.setMessage("Please wait, while we are updating your information");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        if (imageUri != null) {
            final StorageReference fileRef = storageProfilePictureRef
                    .child(Prevalent.currentOnlineUser.getPhone() + ".jpg");

            uploadTask = fileRef.putFile(imageUri);

            uploadTask.continueWithTask(new Continuation() {
                        @Override
                        public Object then(@NonNull Task task) throws Exception {
                            if (!task.isSuccessful()) {
                                throw task.getException();
                            }
                            return fileRef.getDownloadUrl();
                        }
                    })
                    .addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {
                                Uri downloadUrl = task.getResult();

                                myUrl = downloadUrl.toString();

                                DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users");

                                HashMap<String, Object> userMap = new HashMap<>();
                                userMap.put("name", userNameEditText.getText().toString());
                                userMap.put("address", userAddressEditText.getText().toString());
                                userMap.put("image", myUrl);
                                ref.child(Prevalent.currentOnlineUser.getPhone()).updateChildren(userMap);

                                progressDialog.dismiss();

                                startActivity(new Intent(User_Setting_Activity.this, User_Home_Content_Activity.class));

                                Toast.makeText(User_Setting_Activity.this, "Profile info Updated", Toast.LENGTH_SHORT).show();

                                finish();
                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(User_Setting_Activity.this, "Error!!!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } else {
            Toast.makeText(this, "Images Not Selected", Toast.LENGTH_SHORT).show();
        }
    }

    // Display user information
    private void userInfoDisplay(CircleImageView userProfileImageView, EditText userNameEditText, EditText userPhoneEditText, EditText userAddressEditText) {
        DatabaseReference UserRef = FirebaseDatabase.getInstance().getReference().child("Users").child(Prevalent.currentOnlineUser.getPhone());

        UserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // if above data exits
                if (snapshot.exists()) {
                    // if user have image
                    if (snapshot.child("image").exists()) {
                        String image = snapshot.child("image").getValue().toString();
                        String name = snapshot.child("name").getValue().toString();
                        String phone = snapshot.child("phone").getValue().toString();
                        String address = snapshot.child("address").getValue().toString();

                        Picasso.get().load(image).into(userProfileImageView);
                        userNameEditText.setText(name);
                        phoneNumber.setText(phone);
                        userAddressEditText.setText(address);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}