package com.sumer.safeho;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.sumer.safeho.R;
import com.sumer.safeho.databinding.ActivityLogin1Binding;
import com.sumer.safeho.modelclasses.User;

public class LoginActivity1 extends AppCompatActivity {

    private ActivityLogin1Binding binding;
    private String phoneNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLogin1Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();

        binding.btVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phoneNo = binding.edPhone.getText().toString();
                if(User.isValidPhoneNumber(phoneNo))
                {
                    Intent intent = new Intent(getApplicationContext(), LoginActivity2.class);
                    intent.putExtra(Constant.PHONE_NUMBER,phoneNo);
                    startActivity(intent);

                }
                else
                {
                    Toast.makeText(LoginActivity1.this, "Entered a wrong number ", Toast.LENGTH_SHORT).show();
                }
            }
        });



    }
}