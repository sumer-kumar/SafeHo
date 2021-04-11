package com.sumer.safeho;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
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
    AlertDialog.Builder builder;

//    String [] numbers;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEmergencyNumberBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        phoneNumber = getIntent().getStringExtra(Constant.PHONE_NUMBER);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        //we need to update this ArrayList from already existing database
        final ArrayList<String> ListElementsArrayList = new ArrayList<>();

        final ArrayAdapter<String> adapter = new ArrayAdapter<>
                (EmergencyNumberActivity.this, android.R.layout.simple_list_item_1, ListElementsArrayList);
        binding.lvEmrNumber.setAdapter(adapter);

        database.getReference().child(Constant.EMERGENCY_PHONE_LIST).child(mAuth.getCurrentUser().getPhoneNumber())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        ListElementsArrayList.clear();
                        for (DataSnapshot snaps : snapshot.getChildren()) {
                            String ph = snaps.getValue(String.class);
                            ListElementsArrayList.add(ph);

                        }
                        adapter.notifyDataSetChanged();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        builder = new AlertDialog.Builder(this);
        String dialog_title = "Note";

        builder.setMessage("Try to add those contacts which have installed the application or tell them to install the application otherwise they won't able to help ").setTitle(dialog_title);
        //Setting message manually and performing action on button click
        builder.setCancelable(true)
                .setNeutralButton("Okay", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //do nothing
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
        binding.btAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = binding.edNumber.getText().toString();
                if (User.isValidPhoneNumber(phone.trim())) {
                    if (!ListElementsArrayList.contains(phone)) {
                        ListElementsArrayList.add("+91" + phone);
                        adapter.notifyDataSetChanged();


                        database.getReference().child(Constant.EMERGENCY_PHONE_LIST)
                                .child(mAuth.getCurrentUser().getPhoneNumber()).setValue(ListElementsArrayList);
                        binding.edNumber.setText("");
                        binding.edNumber.requestFocus();

                    } else {
                        Toast.makeText(EmergencyNumberActivity.this, "Already present", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(EmergencyNumberActivity.this, "Invalid Number", Toast.LENGTH_SHORT).show();
                }
            }
        });

        binding.btDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ListElementsArrayList.size() <= 2) {
                    Toast.makeText(EmergencyNumberActivity.this, "Add at least " + (3 - ListElementsArrayList.size()) + " more contacts", Toast.LENGTH_SHORT).show();

                } else {
                    HashMap map = new HashMap();
                    map.put(Constant.IS_EMERGENCY_NO_GIVEN, true);
                    database.getReference().child(Constant.USER).child(mAuth.getCurrentUser().getPhoneNumber()).updateChildren(map);
                    Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                    startActivity(intent);
                    EmergencyNumberActivity.this.finish();
                }
            }
        });


        //long click listner
        binding.lvEmrNumber.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(EmergencyNumberActivity.this, "Deleted "+ListElementsArrayList.get(position), Toast.LENGTH_SHORT).show();
                ListElementsArrayList.remove(position);
                adapter.notifyDataSetChanged();
                database.getReference().child(Constant.EMERGENCY_PHONE_LIST)
                        .child(mAuth.getCurrentUser().getPhoneNumber()).setValue(ListElementsArrayList);
                return false;
            }
        });
    }

}