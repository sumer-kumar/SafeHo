package com.sumer.safeho;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sumer.safeho.databinding.ActivityMainBinding;
import com.sumer.safeho.modelclasses.User;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private final int SPLASH_DISPLAY_LENGTH = 100;
    private FirebaseAuth mAuth; //for firebase authentication
    private FirebaseDatabase database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //to hide tool bar
        getSupportActionBar().hide();
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                //if user is signed in
                if(mAuth.getCurrentUser()!=null)
                {
                    String ph = mAuth.getCurrentUser().getPhoneNumber();
                    database.getReference().child(Constant.USER).child(ph).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            User user = snapshot.getValue(User.class);
                            Intent intent;
                            if(!user.isDetailsGiven())
                            {
                                //if details are not given
                                intent = new Intent(getApplicationContext(),DetailActivity.class);
                            }
                            else if(!user.isEmergencyNoGiven())
                            {
                                //if emergency numbers are not given
                                intent = new Intent(getApplicationContext(),EmergencyNumberActivity.class);
                            }
                            else
                            {
                                //if every detail is given
                                intent = new Intent(getApplicationContext(),HomeActivity.class);
                            }
                            startActivity(intent);
                            MainActivity.this.finish();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(MainActivity.this, "Something went wrong\n"+error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else{
                    /* Create an Intent that will start the Menu-Activity. */
                    Intent mainIntent = new Intent(MainActivity.this, LoginActivity1.class);
                    MainActivity.this.startActivity(mainIntent);
                    MainActivity.this.finish();
                }
            }
        }, SPLASH_DISPLAY_LENGTH);
    }
}