package com.outstarttech.kabir.property.activities;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.inventioncore.kabir.sopfia.R;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class CustomQRcodes extends AppCompatActivity {

    EditText text;
    Button gen_btn;
    ImageView image;
    String text2Qr;
    Button btndownload;
    Button btnshare;
    Dialog MyDialog;
    ImageView qrImage;
    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_qrcodes);

        text = (EditText) findViewById(R.id.text);
        gen_btn = (Button) findViewById(R.id.gen_btn);
        image = (ImageView) findViewById(R.id.image);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            ActionBar bar = getSupportActionBar();
            bar.setBackgroundDrawable(new ColorDrawable(Color.BLACK));


//        getSupportActionBar().setLogo(R.drawable.logoactionbar);
            getSupportActionBar().setTitle("Custom QR Codes");
        }
        gen_btn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
            @Override
            public void onClick(View view) {

                text2Qr = text.getText().toString().trim();
                if(text2Qr.isEmpty())
                {
                    text.requestFocus();
                    text.setError("Field Required");
                    return;
                }
                else
                {
                    MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
                    try{
                        BitMatrix bitMatrix = multiFormatWriter.encode(text2Qr, BarcodeFormat.QR_CODE,300,300);
                        BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                        bitmap = barcodeEncoder.createBitmap(bitMatrix);
                        image.setImageBitmap(bitmap);
                    }
                    catch (WriterException e){
                        e.printStackTrace();
                    }

                    MyCustomAlertDialog();
                }


            }
        });


    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if (item.getItemId() == android.R.id.home){
            finish();
        } else {
//            createInformationDialog();
        }
        return super.onOptionsItemSelected(item);
    }

    public void MyCustomAlertDialog(){
        MyDialog = new Dialog(CustomQRcodes.this);
        MyDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        MyDialog.setContentView(R.layout.custom_dialogue);
        MyDialog.setTitle("QR Code ");

        btndownload = (Button)MyDialog.findViewById(R.id.btndownload);
        btnshare = (Button)MyDialog.findViewById(R.id.btnshare);
        qrImage = (ImageView)MyDialog.findViewById(R.id.qrdialogue);

        btndownload.setEnabled(true);
        btnshare.setEnabled(true);
        qrImage.setImageBitmap(bitmap);

        btndownload.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                isStoragePermissionGranted();
            }
        });

        btnshare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//
//                if (mInterstitialAd.isLoaded()) {
//                    mInterstitialAd.show();
//                }

                Intent i = new Intent(Intent.ACTION_SEND);

                if (Build.VERSION.SDK_INT >= 23) {
                    if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            == PackageManager.PERMISSION_GRANTED) {
                        i.setType("image/*");
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        i.putExtra(Intent.EXTRA_TEXT, "Download & Create Your QR Code \n https://play.google.com/store/apps/details?id=com.outstarttech.kabir.qrbarcodescanner");
                        i.putExtra(Intent.EXTRA_STREAM, getImageUri(getApplicationContext(), bitmap));
                        try {
                            startActivity(Intent.createChooser(i, "Share Image Using ..."));
                        } catch (android.content.ActivityNotFoundException ex) {

                            ex.printStackTrace();
                        }

                    } else {
                        ActivityCompat.requestPermissions(CustomQRcodes.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);

                    }
                } else { //permission is automatically granted on sdk<23 upon installation
                    i.setType("image/*");
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    i.putExtra(Intent.EXTRA_TEXT, "Create Your Own QR Codes with s0pFia \n https://ethmny.net/");
                    i.putExtra(Intent.EXTRA_STREAM, getImageUri(getApplicationContext(), bitmap));
                    try {
                        startActivity(Intent.createChooser(i, "Share Image Using ..."));
                    } catch (android.content.ActivityNotFoundException ex) {

                        ex.printStackTrace();
                    }
                }



            }
        });

        MyDialog.show();
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);

        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "QRCode", null);
        return Uri.parse(path);
    }

    public  void isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                setfile();
                Toast.makeText(getApplicationContext(), "QR Code saved in Downloads Folder", Toast.LENGTH_LONG).show();

            } else {
                ActivityCompat.requestPermissions(CustomQRcodes.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);

            }
        } else { //permission is automatically granted on sdk<23 upon installation
            setfile();
            Toast.makeText(getApplicationContext(), "QR Code saved in Downloads Folder", Toast.LENGTH_LONG).show();
        }
    }


    public void setfile()
    {
        File path = Environment.getExternalStorageDirectory();

        File dir = new File(path + "/Download/");
        dir.mkdirs();

        File file = new File(dir, "qrcode_sopfia" + text.getText().toString() + ".jpeg");

        OutputStream out = null;

        try {
            out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,out);
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}
