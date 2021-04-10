package com.sumer.safeho;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.sumer.safeho.databinding.ActivityDetailBinding;
import com.sumer.safeho.modelclasses.User;

import java.util.HashMap;

public class DetailActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private ActivityDetailBinding binding;
    private FirebaseAuth mAuth;
    private FirebaseStorage storage;
    private FirebaseDatabase database;
    private String gender;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();
        //firebase things
        storage = FirebaseStorage.getInstance();
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        binding.spGender.setOnItemSelectedListener(this);
        ArrayAdapter ad = new ArrayAdapter(this, android.R.layout.simple_spinner_item,Constant.GENDER_ARRAY);
        ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spGender.setAdapter(ad);

        binding.ivPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,Constant.REQUEST_CODE);
            }
        });
        //when "next" button pressend
        binding.bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = binding.edFullname.getText().toString();
                String ag = binding.edAge.getText().toString();
                if(name.trim().equals(""))
                {
                    Toast.makeText(DetailActivity.this, "Invalid name", Toast.LENGTH_SHORT).show();
                    return ;
                }
                else if(!verifyAge(ag.trim()))
                {
                    Toast.makeText(DetailActivity.this, "Invalid Age", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    String phoneNumber = mAuth.getCurrentUser().getPhoneNumber();
                    HashMap map = new HashMap();
                    map.put(Constant.NAME,name);
                    map.put(Constant.AGE,Integer.parseInt(ag));
                    map.put(Constant.GENDER,gender);
                    map.put(Constant.IS_DETAILS_GIVEN,true);

                    database.getReference().child(Constant.USER).child(phoneNumber).updateChildren(map);
                    Toast.makeText(DetailActivity.this, "Successfully added "+name, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(),EmergencyNumberActivity.class);
                    intent.putExtra(Constant.PHONE_NUMBER,phoneNumber);
                    startActivity(intent);
                }

            }
        });
    }
    private boolean verifyAge(String ag)
    {
        if(ag.length()>3) return false;
        int age;
        try {
            age = Integer.parseInt(ag);
        }catch(Exception e)
        {
            return false;
        }
        if(age<1) return false;
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data.getData()!=null)
        {
            Uri sFile = data.getData();
            binding.ivProfilePic.setImageURI(sFile);

            final StorageReference reference = storage.getReference().child(Constant.PROFILE_PICTURE).child(mAuth.getCurrentUser().getPhoneNumber());

            reference.putFile(sFile).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(DetailActivity.this, "Uploaded Successfully", Toast.LENGTH_SHORT).show();
                    reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                                database.getReference().child(Constant.USER).child(mAuth.getCurrentUser().getPhoneNumber())
                                        .child("profilepic").setValue(uri.toString());
                        }
                    });

                }
            });
        }

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//        Toast.makeText(getApplicationContext(), Constant.GENDER[position], Toast.LENGTH_LONG).show();
          gender = Constant.GENDER_ARRAY[position];
 }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        Toast.makeText(this, "Nothing Selected", Toast.LENGTH_SHORT).show();
    }
}