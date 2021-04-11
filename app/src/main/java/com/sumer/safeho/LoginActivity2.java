package com.sumer.safeho;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.FirebaseDatabase;
import com.sumer.safeho.R;
import com.sumer.safeho.databinding.ActivityLogin2Binding;
import com.sumer.safeho.modelclasses.User;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class LoginActivity2 extends AppCompatActivity {

    private ActivityLogin2Binding binding;
    private String phoneNumber;
    private FirebaseAuth mAuth;
    FirebaseDatabase database;

    private String verificationCodeBySystem;

    private final String TAG = "LoginActivity2";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLogin2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();
        phoneNumber = "+91"+getIntent().getStringExtra(Constant.PHONE_NUMBER);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        sendVerificationCode(phoneNumber);

        binding.bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String codie = binding.edOtp.getText().toString();
                VerifyCode(codie);
            }
        });

    }
    public void sendVerificationCode(String ph)
    {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phoneNumber)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(LoginActivity2.this)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }
    //variable mCallbacks

    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationCodeBySystem = s;

        }

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();
            if(code.trim()!=null)
            {
                VerifyCode(code);
            }
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Toast.makeText(LoginActivity2.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    };

    private void VerifyCode(String code){
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationCodeBySystem, code);
        signInWithPhoneAuthCredential(credential);
    }
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            String uid = task.getResult().getUser().getUid();
                            User user = new User(phoneNumber,uid,false,false);
//                            ArrayList<String> f = new ArrayList<>();
//                            user.setEmergencyPhoneList(f);
                            database.getReference().child(Constant.USER).child(phoneNumber).setValue(user);
                            Intent intent = new Intent(getApplicationContext(),DetailActivity.class);
//                            intent.putExtra(Constant.PHONE_NUMBER,phoneNumber);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);

                        } else {
                            // Sign in failed, display a message and update the UI
                            Toast.makeText(LoginActivity2.this, "Something went wrong "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                Toast.makeText(LoginActivity2.this, "The verification code entered was invalid", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }
}