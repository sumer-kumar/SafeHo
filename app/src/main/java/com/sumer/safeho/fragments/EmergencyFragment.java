package com.sumer.safeho.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sumer.safeho.Constant;
import com.sumer.safeho.R;
import com.sumer.safeho.databinding.FragmentEmergencyBinding;
import com.sumer.safeho.modelclasses.Notification;
import com.sumer.safeho.modelclasses.User;

import java.util.ArrayList;
import java.util.Date;

public class EmergencyFragment extends Fragment {
    private FragmentEmergencyBinding binding;
    FirebaseDatabase database;
    FirebaseAuth mAuth;
    User user;
    String longitude;
    String latitude;
//    ArrayList<Notification> notiList=new ArrayList<>();
    public EmergencyFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentEmergencyBinding.inflate(inflater, container, false);
        database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();

        database.getReference(Constant.USER)
                .child(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        user = snapshot.getValue(User.class);
                        if(user.isSafe())
                        {
                            binding.btSafe.setVisibility(binding.btSafe.INVISIBLE);
//                            binding.btAlert.setEnabled(true);

                        }
                        else
                        {
                            binding.btSafe.setVisibility(binding.btSafe.VISIBLE);
//                            binding.btAlert.setEnabled(false);
                        }
                        Notification noti = new Notification();
                        noti.setPhoneNumber(user.getPhoneNumber());
                        noti.setTimeStamp(new Date().getTime());
                        noti.setSafe(false);
                        noti.setAge(user.getAge());
                        noti.setGender(user.getGender());
                        noti.setName(user.getName());
                        noti.setProfilepic(user.getProfilepic());
                        noti.setUid(user.getUid());


                        binding.btAlert.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                binding.btSafe.setVisibility(binding.btSafe.VISIBLE);
                                user.setSafe(false);
                                boolean t = true;
                                database.getReference().child(Constant.USER)
                                        .child(mAuth.getCurrentUser().getPhoneNumber()).child("safe").setValue(false);

                                database.getReference().child(Constant.EMERGENCY_PHONE_LIST)
                                        .child(mAuth.getCurrentUser().getPhoneNumber())
                                        .addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                                for (DataSnapshot snaps : snapshot.getChildren()) {
                                                    String ph = snaps.getValue(String.class);
                                                    database.getReference().child(Constant.NOTIFICATION).child(ph).push().setValue(noti);
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {
                                            }
                                        });

                            }
                        });
                        binding.btSafe.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                binding.btSafe.setVisibility(binding.btSafe.INVISIBLE);
                                noti.setSafe(true);
                                user.setSafe(true);
                                boolean t = true;
                                database.getReference().child(Constant.USER)
                                        .child(mAuth.getCurrentUser().getPhoneNumber()).child("safe").setValue(true);
                                database.getReference().child(Constant.EMERGENCY_PHONE_LIST)
                                        .child(mAuth.getCurrentUser().getPhoneNumber())
                                        .addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                                for (DataSnapshot snaps : snapshot.getChildren()) {
                                                    String ph = snaps.getValue(String.class);
                                                    database.getReference().child(Constant.NOTIFICATION).child(ph).push().setValue(noti);
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {
                                            }
                                        });
                            }
                        });

                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                });
        //safe button

        binding.btPolice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String num = "7530885187";
                Uri number = Uri.parse("tel:"+num);
                Intent callIntent = new Intent(Intent.ACTION_DIAL, number);
                startActivity(callIntent);
            }
        });


        return binding.getRoot();
    }
}