package com.example.photofiltersproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class GalleryActivity extends AppCompatActivity {

    ListView galleryLv;
    ArrayList<Photo> photos_al;
    Dal dal;

    boolean isUser;
    String username = "Guest";

    Intent intent;

    Bitmap tmpBitmap;
    Button backBtn;
    private static final String TAG = "GalleryActivity";

    private static final int CAMERA_REQUEST = 1888;
    private static final int WRITE_EXTERNAL_STORAGE = 2888;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    private static final int MY_EXTERNAL_PERMISSION_CODE = 200;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        intent = this.getIntent();
        isUser = intent.getBooleanExtra("is_user", false);
        if(isUser)
        {
            username = intent.getStringExtra("username");
        }
        dal = new Dal(this);
        photos_al = dal.photos_get_photos();

        backBtn = (Button)findViewById(R.id.gallery_btn_back);
        galleryLv = (ListView)findViewById(R.id.gallery_lv_photos);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
    class MyAdapter extends BaseAdapter
    {
        private Photo p;

        @Override
        public int getCount() {
            return 0;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup container) {
            LayoutInflater layoutInflater = (LayoutInflater)getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View photos = layoutInflater.inflate(R.layout.photos,container,false);
            ImageView img = photos.findViewById(R.id.lv_iv_photo);
            TextView usernameTv = photos.findViewById(R.id.lv_tv_username);
            final EditText codeEt = photos.findViewById(R.id.lv_et_code);
            Button saveBtn = photos.findViewById(R.id.lv_btn_save);
            Button shareBtn = photos.findViewById(R.id.lv_btn_share);
            final Button codeBtn = photos.findViewById(R.id.lv_btn_code);

            img.setImageBitmap(photos_al.get(position).getBitmapPhoto());
            usernameTv.setText(photos_al.get(position).getUsername());

            if((!photos_al.get(position).getOpen() ) || (username.compareTo(photos_al.get(position).getUsername()) != 0))
            {
                codeBtn.setVisibility(View.VISIBLE);
                codeEt.setVisibility(View.VISIBLE);

                codeBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(codeEt.getText().toString().compareTo("")!= 0)
                        {
                            if(dal.users_check_username_code_matches_code(photos_al.get(position).getUsername(),codeEt.getText().toString()))
                            {
                                codeBtn.setVisibility(View.GONE);
                                codeEt.setVisibility(View.GONE);
                                photos_al.get(position).setOpen(1);
                                Toast.makeText(getApplicationContext(),"SUCCESS - YOU CAN SHARE AND SAVE THIS PHOTO",Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(),"ERROR: INCORRECT CODE",Toast.LENGTH_SHORT).show();
                            }
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(),"ERROR: CODE MUST BE FILLED",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }


            saveBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(photos_al.get(position).getOpen())
                    {
                        String filename = "PhotoFiltersProject-Gallery-"+photos_al.get(position).getUsername()+":"+System.currentTimeMillis()+".JPEG";
                        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                        {
                            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_EXTERNAL_PERMISSION_CODE);
                        }
                        else
                        {
                            MediaStore.Images.Media.insertImage(getContentResolver(), photos_al.get(position).getBitmapPhoto(), filename , "PhotoFiltersProject");
                        }
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),"ERROR: PHOTO IS PRIVATE, ENTER THE CORRECT CODE TO SAVE AND SHARE IT",Toast.LENGTH_SHORT).show();
                    }
                }
            });

            shareBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(photos_al.get(position).getOpen())
                    {
                        if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED)
                        {
                            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},MY_EXTERNAL_PERMISSION_CODE);
                            String root = Environment.getExternalStorageDirectory().toString();
                            File myDir = new File(root + "/Pfp_2020_images");
                            myDir.mkdirs();
                            String fname = "Pfp_2020_Gallery_"+photos_al.get(position).getName()+"_"+photos_al.get(position).getUsername()+".JPEG";
                            File file = new File(myDir, fname);
                            Log.i(TAG, "" + file);
                            if (file.exists())
                                file.delete();
                            try {
                                FileOutputStream out = new FileOutputStream(file);
                                photos_al.get(position).getBitmapPhoto().compress(Bitmap.CompressFormat.JPEG, 100, out);
                                out.flush();
                                out.close();
                                Intent share = new Intent(Intent.ACTION_SEND);
                                share.setType("image/*");
                                Uri imgUri = Uri.parse(file.getAbsolutePath());
                                share.putExtra(Intent.EXTRA_STREAM, imgUri);
                                share.setPackage("com.whatsapp");//package name of the app
                                startActivity(Intent.createChooser(share, "Share Image"));

                            } catch (Exception e) {
                                e.printStackTrace();
                                Toast.makeText(getApplicationContext(),"ERROR: SOMETHING WENT WRONG",Toast.LENGTH_SHORT).show();

                            }

                        }
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),"ERROR: PHOTO IS PRIVATE, ENTER THE CORRECT CODE TO SAVE AND SHARE IT",Toast.LENGTH_SHORT).show();
                    }

                }
            });


            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.photos, container, false);
            }

            ((Button) convertView.findViewById(R.id.lv_btn_save))
                    .setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    });
            return convertView;
        }

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
                MediaStore.Images.Media.insertImage(getContentResolver(), tmpBitmap, "PhotoFiltersProject-"+System.currentTimeMillis()+".JPEG" , "PhotoFiltersProject");
            }
            else
            {
                Toast.makeText(this, "external permission denied", Toast.LENGTH_LONG).show();
            }
        }

    }

}