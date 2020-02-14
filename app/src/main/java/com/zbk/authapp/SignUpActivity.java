package com.zbk.authapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUpActivity extends AppCompatActivity {

    private TextInputEditText editEmail;
    private TextInputEditText editUsername;
    private TextInputEditText editPassword;
    private TextInputEditText editCpassword;
    private FirebaseAuth mAuth;
    private ProgressDialog statusDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        editEmail = findViewById(R.id.editEmail);
        editUsername = findViewById(R.id.editUsername);
        editPassword = findViewById(R.id.editPassword);
        editCpassword = findViewById(R.id.editCpassword);
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = editEmail.getText().toString();
                String username = editUsername.getText().toString();
                String password = editPassword.getText().toString();
                String cpassword = editCpassword.getText().toString();
                if (cpassword.equals(password)) {
                    if (!email.isEmpty() && !username.isEmpty()) {
                        statusDialog = showDialog("registering user to cloud");
                        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    updateUI(user);
                                } else {
                                    // If sign in fails, display a message to the user.
                                    updateUI(null);
                                }

                                // ...
                            }
                        });
                    }
                }
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    public ProgressDialog showDialog(String msg) {
        Context context;
        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setTitle(msg);
        dialog.setCancelable(false);
        dialog.show();
        return dialog;
    }

    public void hideDialog(ProgressDialog dialog) {
        if (dialog != null) {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }
    }
    private void updateUI(FirebaseUser currentUser) {
        hideDialog(statusDialog);
        if (currentUser != null) {
            // link to home
            startActivity(new Intent(this,HomeActivity.class));
            finish();
        } else {
            Snackbar.make(editEmail,"user is not registered",Snackbar.LENGTH_LONG).show();
        }
    }
}