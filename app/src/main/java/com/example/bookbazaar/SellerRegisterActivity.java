package com.example.bookbazaar;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bookbazaar.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class SellerRegisterActivity extends AppCompatActivity {

    //private Button sellerHaveAccBtn;
    private TextView sellerHaveAccBtn;
    private EditText sellerNameInput, sellerPhoneInput, sellerEmailInput, sellerAddressInput, sellerPasswordInput;
    private Button sellerRegisterBtn;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_register);

        sellerHaveAccBtn = (TextView) findViewById(R.id.seller_already_acc_btn);
        sellerRegisterBtn = (Button) findViewById(R.id.seller_register_btn);
        sellerNameInput = (EditText) findViewById(R.id.seller_reg_name_input);
        sellerPhoneInput = (EditText) findViewById(R.id.seller_reg_phone_input);
        sellerEmailInput = (EditText) findViewById(R.id.seller_reg_email_input);
        sellerAddressInput = (EditText) findViewById(R.id.seller_reg_ads_input);
        sellerPasswordInput = (EditText) findViewById(R.id.seller_reg_password_input);

        mAuth = FirebaseAuth.getInstance();

        sellerHaveAccBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(SellerRegisterActivity.this, Seller_Login_Activity.class);
                startActivity(intent);
            }
        });

        sellerRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerSeller();
            }
        });
    }

    private void registerSeller() {
        String name = sellerNameInput.getText().toString();
        String phone = sellerPhoneInput.getText().toString();
        String email = sellerEmailInput.getText().toString();
        String address = sellerAddressInput.getText().toString();
        String password = sellerPasswordInput.getText().toString();

        if (name.equals("") | phone.equals("") | email.equals("") | address.equals("") | password.equals("")) {
            Toast.makeText(this, "Please Complete all Fields", Toast.LENGTH_SHORT).show();
        } else if (phone.length() != 10) {
            Toast.makeText(this, "Phone should be 10 digits", Toast.LENGTH_SHORT).show();
        } else if (password.length() != 8) {
            Toast.makeText(this, "Password should be 8 digits", Toast.LENGTH_SHORT).show();
        } else {
            // set progress dialog
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Creating Account");
            progressDialog.setMessage("Please wait, while we are creating your account.");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        final DatabaseReference rootRef;
                        rootRef = FirebaseDatabase.getInstance().getReference();

                        String sid = mAuth.getCurrentUser().getUid();

                        HashMap<String, Object> sellerMap = new HashMap<>();
                        sellerMap.put("sid", sid);
                        sellerMap.put("phone", phone);
                        sellerMap.put("email", email);
                        sellerMap.put("address", address);
                        sellerMap.put("name", name);
                        sellerMap.put("password", password);


                        rootRef.child("Sellers").child(sid).updateChildren(sellerMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(Task<Void> task) {
                                progressDialog.dismiss();

                                Toast.makeText(SellerRegisterActivity.this, "You are Successfully Register as Seller", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(SellerRegisterActivity.this, Seller_Home_Activity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();
                            }
                        });

                    }
                }
            });
        }

    }
}