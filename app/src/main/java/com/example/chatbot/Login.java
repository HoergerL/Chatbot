package com.example.chatbot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {
    EditText nameET;
    EditText emailET;
    EditText passwordET;
    TextView nameErrorTV;
    TextView emailErrorTV;
    TextView errorPasswordTV;
    Button skipLoginBTN;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        nameET = findViewById(R.id.nameET);
        passwordET = findViewById(R.id.passwortET);
        emailET = findViewById(R.id.emailET);
        nameErrorTV = findViewById(R.id.nameErrorTV);
        errorPasswordTV = findViewById(R.id.passwordErrorTV);
        emailErrorTV = findViewById(R.id.emailErrorTV);
        skipLoginBTN = findViewById(R.id.skipLoginBTN);

        // Wait for timer to run out then either open login dialogue or MainActivity
        new CountDownTimer(3000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {
                // Initialize Firebase Auth
                mAuth = FirebaseAuth.getInstance();

                getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources()
                        .getColor(R.color.dhbwGrey)));

                SpannableString content = new SpannableString("skip login");
                content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
                skipLoginBTN.setText(content);


                if (mAuth.getCurrentUser() != null) {
                    login();
                } else {
                    findViewById(R.id.constraint_login).setVisibility(View.VISIBLE);
                    findViewById(R.id.constraint_loading).setVisibility(View.GONE);
                }
            }
        }.start();
    }

    public void login() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        String userName = nameET.getText().toString();
        if (!userName.equals("")) {
            intent.putExtra("username", userName);
        } else {
            intent.putExtra("username", "Anonym");
        }

        startActivity(intent);
        finish();
    }

    public void skipLogin(View view) {
        new AlertDialog.Builder(this).setTitle("Bist du dir sicher?")
                .setMessage("Wenn du ohne Login fortfährst, kannst du deine Ergebnisse nicht " +
                        "speichern ")
                .setPositiveButton("Fortfahren", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        login();
                    }
                }).setNegativeButton("Abbrechen", null).show();
    }

    /**
     * Validate user Login Input
     *
     * @return true if correct
     */
    public boolean isValidLoginInput() {
        String password = passwordET.getText().toString();
        String name = nameET.getText().toString();
        String email = emailET.getText().toString();

        if (email.indexOf("@") > -1 && email.indexOf(".") > -1) { // TODO copy paste email validator
            emailErrorTV.setVisibility(View.INVISIBLE);
        } else {
            emailErrorTV.setVisibility(View.VISIBLE);
            return false;
        }

        if (password.length() >= 7) {
            errorPasswordTV.setVisibility(View.INVISIBLE);
        } else {
            errorPasswordTV.setVisibility(View.VISIBLE);
            return false;
        }

        if (name.indexOf(" ") > -1) {
            nameErrorTV.setVisibility(View.INVISIBLE);
        } else {
            nameErrorTV.setVisibility(View.VISIBLE);
            return false;
        }
        return true;
    }

    public void loginViaFirebase(View view) {
        if (isValidLoginInput()) {
            mAuth.signInWithEmailAndPassword(emailET.getText().toString(),
                    passwordET.getText().toString())
                    .addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                login();
                            } else {
                                // If sign in fails, display a message to the user.
                                Toast.makeText(getApplicationContext(), "Einloggen fehlgeschlagen",
                                        Toast.LENGTH_SHORT).show();

                                mAuth.createUserWithEmailAndPassword(emailET.getText().toString(),
                                        passwordET.getText().toString())
                                        .addOnCompleteListener(Login.this,
                                                new OnCompleteListener<AuthResult>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                                        if (task.isSuccessful()) {
                                                            login();
                                                        } else {
                                                            Toast.makeText(getApplicationContext(),
                                                                    "Registrieren + Einloggen " +
                                                                            "fehlgeschlagen",
                                                                    Toast.LENGTH_LONG);
                                                        }
                                                    }
                                                });
                            }
                        }
                    });
        }
    }
}
