package com.example.photofiltersproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class UsergalleryActivity extends AppCompatActivity {
    Button saveBtn, backBtn;
    ImageView userIv;
    ImageButton rightIBtn, leftIBtn;
    TextView userTv;
    String username;
    ArrayList<Photo> photos;
    Dal dal;
    Intent intent;
    private static final int CAMERA_REQUEST = 1888;
    private static final int WRITE_EXTERNAL_STORAGE = 2888;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    private static final int MY_EXTERNAL_PERMISSION_CODE = 200;
    int pos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usergallery);
        dal = new Dal(this);
        intent = this.getIntent();
        username = intent.getStringExtra("username");
        photos = dal.photos_get_photos_by_username(username);
        rightIBtn = (ImageButton)findViewById(R.id.usergallery_ibtn_right);//
        leftIBtn = (ImageButton)findViewById(R.id.usergallery_ibtn_left);//
        userIv = (ImageView)findViewById(R.id.usergallery_iv_photos);//
        userTv = (TextView)findViewById(R.id.usergallery_tv_username);//
        saveBtn = (Button)findViewById(R.id.usergallery_btn_save);//
        backBtn = (Button)findViewById(R.id.usergallery_btn_back);//

        pos = 0;
        userTv.setText(username);
        setBitmapPhoto();

        rightIBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pos < photos.size() -1)
                {
                    pos++;
                }
                else
                {
                    pos = 0;
                }
                setBitmapPhoto();
            }
        });
        leftIBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pos > 0)
                {
                    pos--;
                }
                else
                {
                    pos = photos.size() -1;
                }
                setBitmapPhoto();
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String filename = "PhotoFiltersProject-"+System.currentTimeMillis()+".png";
                if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                {
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_EXTERNAL_PERMISSION_CODE);
                }
                else
                {
                    MediaStore.Images.Media.insertImage(getContentResolver(), photos.get(pos).getBitmapPhoto(), filename , "PhotoFiltersProject");
                }
            }
        });
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE)
        {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
            else
            {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
        if(requestCode == MY_EXTERNAL_PERMISSION_CODE)
        {
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(this, "external permission granted", Toast.LENGTH_LONG).show();
                MediaStore.Images.Media.insertImage(getContentResolver(), photos.get(pos).getBitmapPhoto(), "PhotoFiltersProject-"+System.currentTimeMillis()+".JPEG" , "PhotoFiltersProject");
            }
            else
            {
                Toast.makeText(this, "external permission denied", Toast.LENGTH_LONG).show();
            }
        }

    }
    private void setBitmapPhoto()
    {
        Bitmap tmp = Bitmap.createBitmap(photos.get(pos).getBitmapPhoto());
        userIv.setImageBitmap(tmp);
        //Toast.makeText(getApplicationContext(),"Test",Toast.LENGTH_SHORT).show();
    }
}