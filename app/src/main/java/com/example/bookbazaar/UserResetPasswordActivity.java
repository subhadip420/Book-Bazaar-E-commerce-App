package com.example.bookbazaar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.example.bookbazaar.Prevalent.Prevalent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class UserResetPasswordActivity extends AppCompatActivity {
    private String check = "";

    private TextView pageTitle, titleQuestions;
    private EditText phoneNumber, question1, question2;
    private Button verifyButton;
    private TextInputLayout viewNumberLayout;

    private LottieAnimationView setQuestionImage, forgetPasswordImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_reset_password);

        check = getIntent().getStringExtra("check");

        // Initialize views
        pageTitle = (TextView) findViewById(R.id.textView_rst_pass);
        titleQuestions = (TextView) findViewById(R.id.txtView_answer_questions);
        viewNumberLayout = (TextInputLayout) findViewById(R.id.view_find_phone_number);
        phoneNumber = (EditText) findViewById(R.id.find_phone_number);
        question1 = (EditText) findViewById(R.id.question_1);
        question2 = (EditText) findViewById(R.id.question_2);

        setQuestionImage = (LottieAnimationView) findViewById(R.id.set_question_anim);
        forgetPasswordImage = (LottieAnimationView) findViewById(R.id.forget_password_anim);

        verifyButton = (Button) findViewById(R.id.verify_btn);


    }/// end on create ////

    // create on start methode
    @Override
    protected void onStart() {
        super.onStart();

        // Hide phone number input by default
        phoneNumber.setVisibility(View.GONE);
        viewNumberLayout.setVisibility(View.GONE);

        //if user go from setting
        if (check.equals("settings")) {
            // User is setting security questions
            forgetPasswordImage.setVisibility(View.GONE);
            setQuestionImage.setVisibility(View.VISIBLE);

            pageTitle.setText("Set Question's Answers");
            titleQuestions.setText("Please set answer for the following security questions");

            verifyButton.setText("Set Answers");

            // Display previous answers
            displayPreviousAnswers();

            //form setting on click verify
            verifyButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Set new answers
                    setAnswers();
                }
            });
        }
        //if user go from login page, forget password
        else if (check.equals("login")) {
            // User is resetting password from login page
            phoneNumber.setVisibility(View.VISIBLE);
            viewNumberLayout.setVisibility(View.VISIBLE);

            setQuestionImage.setVisibility(View.GONE);
            forgetPasswordImage.setVisibility(View.VISIBLE);

            // on click verify button
            verifyButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Verify user and reset password
                    verifyUser();
                }
            });
        }
    }


    // Set new security answers
    private void setAnswers() {
        String answer1 = question1.getText().toString().toLowerCase();
        String answer2 = question2.getText().toString().toLowerCase();

        //check if all are fill
        if (answer1.equals("")) {
            Toast.makeText(UserResetPasswordActivity.this, "Pls answer 1st question", Toast.LENGTH_SHORT).show();
        } else if (answer2.equals("")) {
            Toast.makeText(UserResetPasswordActivity.this, "Pls answer 2nd question", Toast.LENGTH_SHORT).show();
        } else {
            DatabaseReference ref = FirebaseDatabase.getInstance()
                    .getReference()
                    .child("Users")
                    .child(Prevalent.currentOnlineUser.getPhone());

            HashMap<String, Object> userdataMap = new HashMap<>();
            userdataMap.put("answer1", answer1);
            userdataMap.put("answer2", answer2);

            ref.child("Security Questions").updateChildren(userdataMap)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(UserResetPasswordActivity.this, "Your security answer is set successfully", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(UserResetPasswordActivity.this, User_Setting_Activity.class);
                                startActivity(intent);
                            }
                        }
                    });
        }
    }

    // Display previous security answers
    private void displayPreviousAnswers() {
        DatabaseReference ref = FirebaseDatabase.getInstance()
                .getReference()
                .child("Users")
                .child(Prevalent.currentOnlineUser.getPhone());

        ref.child("Security Questions").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String ans1 = snapshot.child("answer1").getValue().toString();
                    String ans2 = snapshot.child("answer2").getValue().toString();

                    question1.setText(ans1);
                    question2.setText(ans2);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    // Verify user and reset password
    private void verifyUser() {
        final String phone = phoneNumber.getText().toString();
        final String answer1 = question1.getText().toString().toLowerCase();
        final String answer2 = question2.getText().toString().toLowerCase();

        //check if all are fill
        if (phone.equals("")) {
            Toast.makeText(UserResetPasswordActivity.this, "Pls enter phone number", Toast.LENGTH_SHORT).show();
        } else if (answer1.equals("")) {
            Toast.makeText(UserResetPasswordActivity.this, "Pls answer 1st question", Toast.LENGTH_SHORT).show();
        } else if (answer2.equals("")) {
            Toast.makeText(UserResetPasswordActivity.this, "Pls answer 2nd question", Toast.LENGTH_SHORT).show();
        } else {

            final DatabaseReference ref = FirebaseDatabase.getInstance()
                    .getReference()
                    .child("Users")
                    .child(phone);

            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        if (snapshot.hasChild("Security Questions")) {
                            String ans1 = snapshot.child("Security Questions").child("answer1").getValue().toString().toLowerCase();
                            String ans2 = snapshot.child("Security Questions").child("answer2").getValue().toString().toLowerCase();

                            if (!ans1.equals(answer1)) {
                                Toast.makeText(UserResetPasswordActivity.this, "Your 1st answer is wrong", Toast.LENGTH_SHORT).show();
                            } else if (!ans2.equals(answer2)) {
                                Toast.makeText(UserResetPasswordActivity.this, "Your 2nd answer is wrong", Toast.LENGTH_SHORT).show();
                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(UserResetPasswordActivity.this);
                                builder.setTitle("Set New password");

                                final EditText newPassword = new EditText(UserResetPasswordActivity.this);
                                newPassword.setHint("Write Password Here...");

                                builder.setView(newPassword);

                                //on click positive button
                                builder.setPositiveButton("Change", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        if (!newPassword.getText().toString().equals("")) {
                                            ref.child("password")
                                                    .setValue(newPassword.getText().toString())
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            Toast.makeText(UserResetPasswordActivity.this, "Password Change Successfully", Toast.LENGTH_SHORT).show();
                                                            Intent intent = new Intent(UserResetPasswordActivity.this, UserLoginActivity.class);
                                                            startActivity(intent);
                                                        }
                                                    });
                                        } else {
                                            Toast.makeText(UserResetPasswordActivity.this, "Please write password", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });

                                //on click negative button
                                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.cancel();
                                    }
                                });
                                //for show dialog box
                                builder.show();
                            }
                        } else {
                            Toast.makeText(UserResetPasswordActivity.this, "You had not set your security questions", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(UserResetPasswordActivity.this, "This phone number not exists", Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        }
    }

}/// end public class////