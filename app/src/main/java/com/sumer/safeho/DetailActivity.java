package com.sumer.safeho;

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
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.sumer.safeho.databinding.ActivityDetailBinding;

public class DetailActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private ActivityDetailBinding binding;
    private FirebaseAuth mAuth;
    private FirebaseStorage storage;
    private FirebaseDatabase database;
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
        ArrayAdapter ad = new ArrayAdapter(this, android.R.layout.simple_spinner_item,Constant.GENDER);
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
        binding.btNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = binding.edFullname.getText().toString();
                if(name.trim().equals(""))
                {
                    Toast.makeText(DetailActivity.this, "Invalid name", Toast.LENGTH_SHORT).show();
                    return ;
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
                }
            });
        }

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(getApplicationContext(), Constant.GENDER[position], Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        Toast.makeText(this, "Nothing Selected", Toast.LENGTH_SHORT).show();
    }
}