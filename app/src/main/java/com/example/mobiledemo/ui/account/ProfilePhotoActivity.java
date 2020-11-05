package com.example.mobiledemo.ui.account;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.example.mobiledemo.R;

public class ProfilePhotoActivity extends AppCompatActivity {
    private ProfilePhotoViewModel ProfilePhotoViewModel;
    Context context = this;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
        ProfilePhotoViewModel = ViewModelProviders.of(this).get(ProfilePhotoViewModel.class);
        final Button backButton = findViewById(R.id.account_back2);
        final ImageButton image1 =findViewById(R.id.imageButton1);


        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfilePhotoActivity.this, AccountActivity.class);
                startActivity(intent);
            }
        });
        image1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDefaultPhoto("3");
            }
        });
    }

    private void setDefaultPhoto(String index){



        Log.d("TAG-goaccont","gotoaccont");
        Intent intent = new Intent(ProfilePhotoActivity.this, AccountActivity.class);
        intent.putExtra("photoIndex",index);
        startActivity(intent);

        Log.d("TAG-reach","reach");
        try {
            Thread.sleep(1000);
        } catch(InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
        AccountActivity changephoto = new AccountActivity();
        changephoto.setphoto(index);
    }
}