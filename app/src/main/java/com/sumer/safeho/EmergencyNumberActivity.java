package com.sumer.safeho;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.sumer.safeho.databinding.ActivityEmergencyNumberBinding;
import com.sumer.safeho.modelclasses.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class EmergencyNumberActivity extends AppCompatActivity {
    private ActivityEmergencyNumberBinding binding;
    private FirebaseAuth mAuth;
    private String phoneNumber;
    FirebaseDatabase database;
//    String [] numbers;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEmergencyNumberBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        phoneNumber = getIntent().getStringExtra(Constant.PHONE_NUMBER);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        final ArrayList<String> ListElementsArrayList = new ArrayList<>();
        final ArrayAdapter<String> adapter = new ArrayAdapter<>
                (EmergencyNumberActivity.this, android.R.layout.simple_list_item_1, ListElementsArrayList);
        binding.lvEmrNumber.setAdapter(adapter);

        binding.btAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = binding.edNumber.getText().toString();
                if(User.isValidPhoneNumber(phone))
                {
                    if(!ListElementsArrayList.contains(phone)) {
                        ListElementsArrayList.add(phone);
                        adapter.notifyDataSetChanged();
                        HashMap map = new HashMap();
                        database.getReference().child(Constant.USER).child(phoneNumber).child(Constant.EMERGENCY_PHONE_LIST).updateChildren();
                    }
                    else
                    {
                        Toast.makeText(EmergencyNumberActivity.this, "Already present", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(EmergencyNumberActivity.this, "Invalid Number", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}