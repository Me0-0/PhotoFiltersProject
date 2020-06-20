package com.example.photofiltersproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MenuActivity extends AppCompatActivity {

    Intent intent;
    Dal dal;
    boolean isUser;
    String username = "Guest";

    Button galleryBtn, usergalleryBtn,filterBtn, settingsBtn, exitBtn;
    TextView userTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        dal = new Dal(this);
        intent = this.getIntent();
        isUser = intent.getBooleanExtra("isUser",false);
        if(isUser)
        {
            username = intent.getStringExtra("username");
        }

        galleryBtn = (Button)findViewById(R.id.menu_btn_gallery);//
        usergalleryBtn = (Button)findViewById(R.id.menu_btn_usergallery);//
        filterBtn = (Button)findViewById(R.id.menu_btn_filter);//
        settingsBtn = (Button)findViewById(R.id.menu_btn_settings);//
        exitBtn = (Button)findViewById(R.id.menu_btn_exit);//
        userTv = (TextView)findViewById(R.id.menu_tv_titel);//

        userTv.setText("Welcome, "+ username);

        usergalleryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isUser)
                {
                    if(true)
                    {
                        Toast.makeText(getApplicationContext(),"Coming soon!", Toast.LENGTH_SHORT).show();
                    }
                    else if(dal.photos_check_existence_username(username)) {
                        intent = new Intent(MenuActivity.this, UsergalleryActivity.class);
                        intent.putExtra("username", username);
                        startActivity(intent);
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),"You have no photos save from PhotoFilterProject!",Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"ERROR: ONLY LOGGED USER CAN USE THIS OPTION",Toast.LENGTH_SHORT).show();
                }
            }
        });

        settingsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isUser)
                {

                    intent = new Intent(MenuActivity.this, SettingsActivity.class);
                    intent.putExtra("username", username);
                    startActivity(intent);
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"ERROR: ONLY LOGGED USER CAN USE THIS OPTION",Toast.LENGTH_SHORT).show();
                }
            }
        });

        exitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        galleryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (true) {
                    Toast.makeText(getApplicationContext(), "Coming soon!", Toast.LENGTH_SHORT).show();
                } else {
                    if (dal.photos_check_existence()) {
                        intent = new Intent(MenuActivity.this, GalleryActivity.class);
                        intent.putExtra("is_user", isUser);
                        if (isUser) {
                            intent.putExtra("username", username);
                        }
                        startActivity(intent);
                    } else {
                        Toast.makeText(getApplicationContext(), "There are no Photos on the App shared Gallery", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        filterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(MenuActivity.this, FilterActivity.class);
                intent.putExtra("isUser",isUser);
                if(isUser)
                {
                    intent.putExtra("username", username);
                }
                startActivity(intent);
            }
        });
    }
}