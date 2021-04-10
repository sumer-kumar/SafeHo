package com.sumer.safeho.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sumer.safeho.Constant;
import com.sumer.safeho.R;
import com.sumer.safeho.adapters.NotificationAdapter;
import com.sumer.safeho.databinding.FragmentNotificationBinding;
import com.sumer.safeho.modelclasses.Notification;
import com.sumer.safeho.modelclasses.User;

import java.util.ArrayList;
import java.util.logging.ConsoleHandler;

public class NotificationFragment extends Fragment {
    private FragmentNotificationBinding binding;
     ArrayList<Notification> notiList = new ArrayList<>();
    FirebaseDatabase database ;
    FirebaseAuth mAuth;
    public NotificationFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentNotificationBinding.inflate(inflater, container, false);

        NotificationAdapter adapter = new NotificationAdapter(notiList,getContext());
        binding.rvNotifications.setAdapter(adapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        binding.rvNotifications.setLayoutManager(layoutManager);

        database = FirebaseDatabase.getInstance();

        database.getReference().child(Constant.NOTIFICATION)
                .child(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        notiList.clear();
                        for(DataSnapshot snaps : snapshot.getChildren())
                        {
                            Notification noti = snaps.getValue(Notification.class);
                            notiList.add(noti);
                        }
                        adapter.notifyDataSetChanged();
//                        Toast.makeText(getContext(), ""+notiList.size(), Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
        return binding.getRoot();
    }
}