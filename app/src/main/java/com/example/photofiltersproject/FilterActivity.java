package com.example.photofiltersproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;

public class FilterActivity extends AppCompatActivity {

    private static final int CAMERA_REQUEST = 1888;
    private static final int WRITE_EXTERNAL_STORAGE = 2888;
    private ImageView imageView;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    private static final int MY_EXTERNAL_PERMISSION_CODE = 200;
    int PERMISSION_ALL = 1;
    String[] PERMISSIONS = {
            android.Manifest.permission.READ_CONTACTS,
            android.Manifest.permission.WRITE_CONTACTS,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.READ_SMS,
            android.Manifest.permission.CAMERA
    };
    private int filterIndex = 0;
    private int exampleInt = 1;
    private boolean isRegular = true;
    private boolean isCamera = false;
    private String username;
    private boolean isUser;
    Date currentTime;
    Dal dal;
    Button saveBtn, cancelBtn, backBtn, resetBtn;
    ImageButton cameraBtn, rightBtn, leftBtn;
    TextView ex1Tv,ex2Tv,filterTv;
    Switch filterSw, examplesSw;
    Bitmap bitmap, source, tmpBitmap;
    Toast exampleT, premiumT, alert;
    Intent intent;
    String[] regularFiltersStrArray = new String[]{"None", "UpsideDown", "Right2Left", "BlackNWhite", "B/W but Red", "B/W but Green", "B/W but Blue"};
    String[] premiumFiltersStrArray = new String[]{"None", "Shades of Red", "Shades of Green", "Shades of Blue", "Red2White", "Red2Black", "Red2Gray", "Green2White", "Green2Black", "Green2Gray", "blue2White", "Blue2Black", "Blue2Gray", "Negative"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        dal = new Dal(this);
        intent = this.getIntent();
        isUser = intent.getBooleanExtra("isUser",false);
        if(isUser)
        {
            username = intent.getStringExtra("username");
        }
        exampleT  =  Toast.makeText(getApplicationContext(), "Error: Cannot Change Photo To Example While Camera Is On, Press Cancel To Enable Examples", Toast.LENGTH_SHORT);
        premiumT =  Toast.makeText(getApplicationContext(), "Error: Only Logged Users Can Use Premium Filters", Toast.LENGTH_SHORT);
        alert = Toast.makeText(getApplicationContext(), "Photo Saved Successfully", Toast.LENGTH_SHORT);


        this.imageView = (ImageView)findViewById(R.id.filter_iv_img);
        this.ex1Tv = (TextView)findViewById(R.id.filter_tv_e1);
        this.ex2Tv = (TextView)findViewById(R.id.filter_tv_e2);
        this.filterTv = (TextView)findViewById(R.id.filter_tv_filter);

        this.cameraBtn = (ImageButton)findViewById(R.id.filter_ibtn_camera);
        this.rightBtn = (ImageButton)findViewById(R.id.filter_ibtn_right);
        this.leftBtn = (ImageButton)findViewById(R.id.filter_ibtn_left);

        this.examplesSw = (Switch)findViewById(R.id.filter_sw_examples);
        this.filterSw = (Switch)findViewById(R.id.filter_sw_filter);

        this.cancelBtn = (Button)findViewById(R.id.filter_btn_cancel);
        this.resetBtn = (Button)findViewById(R.id.filter_btn_reset);
        this.backBtn = (Button)findViewById(R.id.filter_btn_back);
        this.saveBtn = (Button)findViewById(R.id.filter_btn_save);


        isRegular = true;
        isCamera = false;
        filterIndex = 0;
        exampleInt = 1;
        filterSw.setChecked(false);
        examplesSw.setChecked(false);
        setFiltersTextView();
        setNewBitmapSource(true);
        setNewBitmapTmp();

        filterSw.setTextOn("Premium");
        filterSw.setTextOff("Regular");
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
                    MediaStore.Images.Media.insertImage(getContentResolver(), tmpBitmap, filename , "PhotoFiltersProject");
                }
                if(isUser)
                {
                    //dal.photos_add_photo(username,isRegular?0:1,filterTv.getText().toString(),Photo.getBitmapToByte(tmpBitmap),dal.users_get_open_by_username(username)?1:0);
                    Toast.makeText(getApplicationContext(),"Save To DB: Coming soon, note - the photo was saved to gallery", Toast.LENGTH_SHORT).show();
                }

            }
        });
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isRegular = true;
                isCamera = false;
                filterIndex = 0;
                exampleInt = 1;
                filterSw.setChecked(false);
                examplesSw.setChecked(false);
                setFiltersTextView();
                setNewBitmapSource(true);
                setNewBitmapTmp();
            }
        });
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isCamera = false;
                setNewBitmapSource(true);
                setNewBitmapTmp();
            }
        });

        examplesSw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!isCamera)
                {
                    exampleInt = isChecked?2:1;
                    setNewBitmapSource(true);
                    setNewBitmapTmp();
                }
                else
                {
                    examplesSw.setChecked(false);
                    exampleT.show();
                }
            }
        });
        filterSw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if((!isUser) && isChecked)
                {
                    filterSw.setChecked(false);
                    premiumT.show();
                }
                else
                {
                    filterIndex = 0;
                    filterSw.setText(isChecked?"Premium":"Regular");
                    isRegular = !isChecked;
                    setFiltersTextView();
                    setNewBitmapTmp();
                }

            }
        });

        rightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(filterIndex+1 < (isRegular?regularFiltersStrArray.length:premiumFiltersStrArray.length))
                {
                    filterIndex++;
                }
                else
                {
                    filterIndex = 0;
                }
                setFiltersTextView();
                setNewBitmapTmp();
            }
        });
        leftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(filterIndex > 0)
                {
                    filterIndex--;
                }
                else
                {
                    filterIndex = isRegular?regularFiltersStrArray.length-1:premiumFiltersStrArray.length-1;
                }
                setFiltersTextView();
                setNewBitmapTmp();
            }
        });
        cameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isCamera = true;
                if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
                {
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
                }
                else
                {
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, CAMERA_REQUEST);
                }
            }
        });
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////
    private void setFiltersTextView()
    {
        this.filterTv.setText(isRegular?this.regularFiltersStrArray[filterIndex]:this.premiumFiltersStrArray[filterIndex]);
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////
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
    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            this.bitmap = (Bitmap) data.getExtras().get("data");
            //imageView.setImageBitmap(bitmap);
            setNewBitmapSource(false);
            setNewBitmapTmp();
        }
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////
    private void setNewBitmapSource(boolean isExample)
    {
        if(isExample)
        {
            //set by examples
            AssetManager manager = getAssets();
            InputStream stream;
            try {
                stream = manager.open("examples/example"+exampleInt+".png");
                source = BitmapFactory.decodeStream(stream);
            }catch (Exception e)
            {
                return;
            }
            if(exampleInt == 1)
            {
                ex1Tv.setVisibility(View.VISIBLE);
                ex2Tv.setVisibility(View.GONE);
            }
            else
            {
                ex1Tv.setVisibility(View.GONE);
                ex2Tv.setVisibility(View.VISIBLE);
            }
        }
        else
        {
            //set by bitmap
            source = Bitmap.createBitmap(bitmap);
            ex1Tv.setVisibility(View.GONE);
            ex2Tv.setVisibility(View.GONE);
        }
    }

    private void setNewBitmapTmp()
    {
        if(this.filterIndex != 0)
        {
            BitmapProcess();
        }
        else
        {
            this.tmpBitmap = Bitmap.createBitmap(source);
        }
        imageView.setImageBitmap(tmpBitmap);

    }
    ////////////////////////////////////////////////////////////////////////////////////////////////
    private int[] PremiumColorEffects(int r,int g,int b)
    {
        int[] ret = new int[]{r,g,b};
        int avrg = 0;
        switch (filterIndex){
            case 1:
                avrg = (r+b+g)/3;
                ret = new int[]{avrg,0,0};
                break;
            case 2:
                avrg = (r+b+g)/3;
                ret = new int[]{0,avrg,0};
                break;
            case 3:
                avrg = (r+g+b)/3;
                ret = new int[]{0,0,avrg};
                break;
            case 4:
                if(redDetection(r,g,b) || color(r,g,b))
                {
                    ret = new int[]{255,255,255};
                }
                else
                {
                    ret = new int[]{r,g,b};
                }
                break;
            case 5:
                if(redDetection(r,g,b) || color(r,g,b))
                {
                    ret = new int[]{0,0,0};
                }
                else
                {
                    ret = new int[]{r,g,b};
                }
                break;
            case 6:
                if(redDetection(r,g,b) || color(r,g,b))
                {
                    avrg = (r+g+b)/3;
                    ret = new int[]{avrg,avrg,avrg};
                }
                else
                {
                    ret = new int[]{r,g,b};
                }
                break;
            case 7:
                if(greenDetection(r,g,b))
                {
                    ret = new int[]{255,255,255};
                }
                else
                {
                    ret = new int[]{r,g,b};
                }
                break;
            case 8:
                if(greenDetection(r,g,b))
                {
                    ret = new int[]{0,0,0};
                }
                else
                {
                    ret = new int[]{r,g,b};
                }
                break;
            case 9:
                if(greenDetection(r,g,b))
                {
                    avrg = (r+g+b)/3;
                    ret = new int[]{avrg,avrg,avrg};
                }
                else
                {
                    ret = new int[]{r,g,b};
                }
                break;
            case 10:
                if(blueDetection(r,g,b))
                {
                    ret = new int[]{255,255,255};
                }
                else
                {
                    ret = new int[]{r,g,b};
                }
                break;
            case 11:
                if(blueDetection(r,g,b))
                {
                    ret = new int[]{0,0,0};
                }
                else
                {
                    ret = new int[]{r,g,b};
                }
                break;
            case 12:
                if(blueDetection(r,g,b))
                {
                    avrg = (r+g+b)/3;
                    ret = new int[]{avrg,avrg,avrg};
                }
                else
                {
                    ret = new int[]{r,g,b};
                }
                break;
            case 13:
                ret = new int[]{255-r,255-g,255-b};
            default:
                break;
        }
        return ret;
    }
    private int[] RegularColorEffects(int r,int g,int b)
    {
        int[] ret = new int[]{r,g,b};
        int avrg = 0;
        switch (filterIndex){
            case 3:
                avrg = (r+g+b)/3;
                ret = new int[]{avrg,avrg,avrg};
                break;
            case 4:
                if(redDetection(r,g,b) || color(r,g,b))
                {
                    avrg = (g+b)/3;
                    ret = new int[]{r,avrg,avrg};
                }
                else
                {
                    avrg = (r+g+b)/3;
                    ret = new int[]{avrg,avrg,avrg};
                }
                break;
            case 5:
                if(greenDetection(r,g,b))
                {
                    avrg = (b+r)/3;
                    ret = new int[]{avrg,g,avrg};
                }
                else
                {
                    avrg = (r+g+b)/3;
                    ret = new int[]{avrg,avrg,avrg};
                }
                break;
            case 6:
                if(blueDetection(r,g,b))
                {
                    avrg = (g+r)/3;
                    ret = new int[]{avrg,avrg,b};
                }
                else
                {
                    avrg = (r+g+b)/3;
                    ret = new int[]{avrg,avrg,avrg};
                }
                break;
            default:
                break;
        }
        return ret;
    }
    private boolean redDetection(int r, int g, int b)
    {
        return r >= 200?(b <=45&& g <=45):(r>=150?(b<=30&&g<=30): r >= 100 && (b <= 20 && g <= 20));
    }
    private boolean blueDetection(int r, int g, int b)
    {
        return (b >= 200?(r<75):(b>=100 && r<=30 && 2*g <= b));
    }
    private boolean greenDetection(int r,int g, int b)
    {
        return(g>=100 && 2*b <= g && 2*r <= g);
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////
    private void BitmapProcess()
    {
        int w = source.getWidth();
        int h = source.getHeight();
        int[] pixles = new int[w*h];
        int[] pix = new int[w*h];
        int[] rgb = new int[3];
        int index;
        source.getPixels(pixles,0,w,0,0,w,h);
        source.getPixels(pix,0,w,0,0,w,h);

        int r,g,b;
        int i;
        if(isRegular && filterIndex <3 &&filterIndex!= 0)
        {
            for (int y = 0; y < h; y++) {
                for (int x = 0; x < w; x++) {
                    index = y * w + x;
                    i = filterIndex==1?(h-1-y)*w+x:y*w+(w-x-1);
                    pixles[index] = pix[i];
                }
            }
        }
        else if(filterIndex!= 0){
            for (int y = 0; y < h; y++) {
                for (int x = 0; x < w; x++) {
                    index = y * w + x;
                    r = Color.red(pixles[index]);
                    b = Color.blue(pixles[index]);
                    g = Color.green(pixles[index]);
                    rgb = isRegular?RegularColorEffects(r,g,b):PremiumColorEffects(r,g,b);
                    pixles[index] = Color.rgb(rgb[0],rgb[1],rgb[2]);
                }
            }
        }
        else
        {
            tmpBitmap = Bitmap.createBitmap(source);
            return;
        }
        tmpBitmap = Bitmap.createBitmap(w,h,source.getConfig());
        tmpBitmap.setPixels(pixles,0,w,0,0,w,h);
        pixles = null;
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////

     private boolean color(int a, int b, int c)
     {
     return (a-b) > c && (a-c) > b;
     }

    ////////////////////////////////////////////////////////////////////////////////////////////////
}
