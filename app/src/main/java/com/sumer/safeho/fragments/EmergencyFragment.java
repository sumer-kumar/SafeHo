package com.sumer.safeho.fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.sumer.safeho.Constant;
import com.sumer.safeho.databinding.FragmentEmergencyBinding;
import com.sumer.safeho.modelclasses.Notification;
import com.sumer.safeho.modelclasses.User;
import com.sumer.safeho.sendNotificationPack.APIService;
import com.sumer.safeho.sendNotificationPack.Client;
import com.sumer.safeho.sendNotificationPack.Data;
import com.sumer.safeho.sendNotificationPack.MyResponse;
import com.sumer.safeho.sendNotificationPack.NotificationSender;
import com.sumer.safeho.sendNotificationPack.Token;

import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EmergencyFragment extends Fragment {
    private FragmentEmergencyBinding binding;
    FirebaseDatabase database;
    FirebaseAuth mAuth;
    User user;
    String longitude;
    String latitude;
    private APIService apiService;
    private FusedLocationProviderClient fusedLocationProviderClient;

    public EmergencyFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentEmergencyBinding.inflate(inflater, container, false);
        database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());
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
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    if (getContext().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                                        //get the location here
                                        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                                            @Override
                                            public void onSuccess(Location location) {
                                                if (location != null) {
                                                     latitude =""+location.getLatitude();
                                                     longitude = ""+location.getLongitude();
                                                     noti.setLongitude(longitude);
                                                     noti.setLatitude(latitude);
                                                     //yahan
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
                                                    database.getReference().child(Constant.TOKEN)
                                                            .child(mAuth.getCurrentUser().getPhoneNumber())
                                                            .child(Constant.TOKEN)
                                                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                                                @Override
                                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                    String usertoken=snapshot.getValue(String.class);
                                                                    sendNotifications(usertoken, noti.getName().toString().trim(),"Needs Your Help");
                                                                }

                                                                @Override
                                                                public void onCancelled(@NonNull DatabaseError error) {

                                                                }
                                                            });
                                                }
                                            }
                                        });
                                    } else {
                                        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                                    }
                                }

                                binding.btSafe.setVisibility(binding.btSafe.VISIBLE);
                                user.setSafe(false);
                                boolean t = true;
                                database.getReference().child(Constant.USER)
                                        .child(mAuth.getCurrentUser().getPhoneNumber()).child("safe").setValue(false);

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
                String num = "112";
                Uri number = Uri.parse("tel:"+num);
                Intent callIntent = new Intent(Intent.ACTION_DIAL, number);
                startActivity(callIntent);
            }
        });
        UpdateToken();
        return binding.getRoot();
    }
    private void UpdateToken(){
        FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        String refreshToken= FirebaseInstanceId.getInstance().getToken();
        Token token= new Token(refreshToken);
        FirebaseDatabase.getInstance().getReference()
                .child(Constant.TOKEN)
                .child(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber())
                .setValue(token);

    }

    public void sendNotifications(String usertoken, String title, String message) {
        Data data = new Data(title, message);
        NotificationSender sender = new NotificationSender(data, usertoken);
        apiService.sendNotifcation(sender).enqueue(new Callback<MyResponse>() {
            @Override
            public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                if (response.code() == 200) {
                    if (response.body().success != 1) {
                        Toast.makeText(getContext(), "Failed ", Toast.LENGTH_LONG);
                    }
                }
            }

            @Override
            public void onFailure(Call<MyResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Failed Successfully", Toast.LENGTH_SHORT).show();
            }
        });
}

}