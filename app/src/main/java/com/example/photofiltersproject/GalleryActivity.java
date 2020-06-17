package com.example.photofiltersproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

public class GalleryActivity extends AppCompatActivity {

    ListView galleryLv;
    ArrayList<Button> saveBtn;
    ArrayList<Button> shareBtn;
    ArrayList<Button> codeBtn;
    ArrayList<EditText> codeEt;
    ArrayList<Photo> photos;

    Button backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

    }
}