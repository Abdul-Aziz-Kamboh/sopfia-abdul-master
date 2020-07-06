package com.outstarttech.kabir.property.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.github.sumimakito.awesomeqr.AwesomeQRCode;
import com.github.sumimakito.awesomeqr.AwesomeQrRenderer;
import com.inventioncore.kabir.sopfia.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import yuku.ambilwarna.AmbilWarnaDialog;

public class CustomizeQrcodes extends AppCompatActivity {

    private final int BKG_IMAGE = 822;
    private final int LOGO_IMAGE = 379;
    private  float dotScl,logScl;//DOT AND LOGO SCALE points
    private ImageView qrCodeImageView;
    private EditText etColorLight, etColorDark, etContents;
    private Button btGenerate, btSelectBG,btBackColor,clearImage;
    private CheckBox ckbWhiteMargin;
    private Bitmap backgroundImage = null;
    private AlertDialog progressDialog;
    private boolean generating = false;
    private CheckBox ckbAutoColor;
    private Button color;
    private ScrollView scrollView;
    private SeekBar etDotScale,etMargin,etSize,etLogoMargin,etLogoCornerRadius,etLogoScale,etBinarizeThreshold;
    private int defaultColor, defaultColorBg,finalColor;
    private String asd;
    private CheckBox ckbBinarize;
    private CheckBox ckbRoundedDataDots;
    private Bitmap qrBitmap;
    private Button btOpen;
    RelativeLayout rL;
    private Button btRemoveLogoImage;
    private Button btSelectLogo;
    private Bitmap logoImage;
    private ViewGroup configViewContainer, resultViewContainer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customize_qrcodes);


        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);


        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            ActionBar bar = getSupportActionBar();
            bar.setBackgroundDrawable(new ColorDrawable(Color.BLACK));


//        getSupportActionBar().setLogo(R.drawable.logoactionbar);
            getSupportActionBar().setTitle("Customize QR Codes");
        }
        configViewContainer = (ViewGroup) findViewById(R.id.configViewContainer);
        resultViewContainer = (ViewGroup) findViewById(R.id.resultViewContainer);

        scrollView = (ScrollView) findViewById(R.id.scrollView);
        scrollView = (ScrollView) findViewById(R.id.scrollView);
        color = (Button) findViewById(R.id.clr);
        btBackColor = (Button) findViewById(R.id.backColor);
        clearImage = (Button) findViewById(R.id.clearImage);
        qrCodeImageView = (ImageView) findViewById(R.id.qrcode);
        etColorLight = (EditText) findViewById(R.id.colorLight);
        etColorDark = (EditText) findViewById(R.id.colorDark);
        etContents = (EditText) findViewById(R.id.contents);
        etSize = (SeekBar) findViewById(R.id.size);
        etMargin = (SeekBar) findViewById(R.id.margin);
        etDotScale = (SeekBar) findViewById(R.id.dotScale);
        btSelectBG = (Button) findViewById(R.id.backgroundImage);
        btSelectLogo = (Button) findViewById(R.id.logoImage);
        btRemoveLogoImage = (Button) findViewById(R.id.removeLogoImage);
        btGenerate = (Button) findViewById(R.id.generate);
        btOpen = (Button) findViewById(R.id.open);
        ckbWhiteMargin = (CheckBox) findViewById(R.id.whiteMargin);
        ckbAutoColor = (CheckBox) findViewById(R.id.autoColor);
        ckbBinarize = (CheckBox) findViewById(R.id.binarize);
        ckbRoundedDataDots = (CheckBox) findViewById(R.id.rounded);
        etBinarizeThreshold = (SeekBar) findViewById(R.id.binarizeThreshold);
        etLogoMargin = (SeekBar) findViewById(R.id.logoMargin);
        etLogoScale = (SeekBar) findViewById(R.id.logoScale);
        rL = (RelativeLayout) findViewById(R.id.layout);
        etLogoCornerRadius = (SeekBar) findViewById(R.id.logoRadius);
        //  etBinarizeThreshold = (EditText) findViewById(R.id.binarizeThreshold);

        //To Binerize
        etBinarizeThreshold.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Toast.makeText(seekBar.getContext(), "Value: "+progress, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        //logo scale seekbar
        etLogoScale.setMax(10);
        etLogoScale.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float currentProgress = progress * 0.1f;
                String yourprogress = String.format("%.1f", currentProgress);
                //  textView.setText( yourprogress + " km");// + seekBar.getMax());
                logScl=Float.parseFloat(yourprogress);
                Toast.makeText(CustomizeQrcodes.this, "Value "+yourprogress, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

//corner radius seekbar
        etLogoCornerRadius.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Toast.makeText(seekBar.getContext(), "Value: "+progress, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
//logo margin seekbar
        etLogoMargin.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Toast.makeText(seekBar.getContext(), "Value: "+progress, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        //size seekbar
        etSize.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Toast.makeText(seekBar.getContext(), "Value: "+progress, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
//margin seekbar

        etMargin.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Toast.makeText(seekBar.getContext(), "Value: "+progress, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        // Background color picker
        defaultColor = ContextCompat.getColor(CustomizeQrcodes.this, R.color.colorPrimary);
        color.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openColorPickerBg();
            }
        });

        ckbAutoColor.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                etColorLight.setEnabled(!isChecked);
                etColorDark.setEnabled(!isChecked);
            }
        });

        ckbBinarize.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                etBinarizeThreshold.setEnabled(isChecked);
            }
        });
//Dots Color of qr
        btBackColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openColorPicker();
            }
        });

        //select background pic

        btSelectBG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent;
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
                    intent = new Intent(Intent.ACTION_GET_CONTENT);
                } else {
                    intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                }
                intent.setType("image/*");
                startActivityForResult(intent, BKG_IMAGE);
            }
        });

        //Remove background image

        clearImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backgroundImage = null;
                Toast.makeText(CustomizeQrcodes.this, "Background image removed.", Toast.LENGTH_SHORT).show();
            }
        });

        btSelectLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
                    intent = new Intent(Intent.ACTION_GET_CONTENT);
                } else {
                    intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                }
                intent.setType("image/*");
                startActivityForResult(intent, LOGO_IMAGE);
            }
        });

        btOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (qrBitmap != null) saveBitmap(qrBitmap);
            }
        });


        //color picker


        btRemoveLogoImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoImage = null;
                Toast.makeText(CustomizeQrcodes.this, "Logo image removed.", Toast.LENGTH_SHORT).show();
            }
        });


        //converting dot scale seekbar to float
        etDotScale.setMax(10);
        etDotScale.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float currentProgress = progress * 0.1f;
                String yourprogress = String.format("%.1f", currentProgress);
                //  textView.setText( yourprogress + " km");// + seekBar.getMax());
                dotScl=Float.parseFloat(yourprogress);
                Toast.makeText(CustomizeQrcodes.this, "Value "+yourprogress, Toast.LENGTH_SHORT).show();
            }



            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
//final float dotS=etDotScale.getProgress();
//Log.d("logScale","value is "+dotScl);

        //to generate Qr code

        btGenerate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(CustomizeQrcodes.this, "Scroll Down To View It and Pick Dots Color", Toast.LENGTH_SHORT).show();
                try {
                    generate(etContents.getText().length() == 0 ? "Enter Text." : etContents.getText().toString(),
                            etSize.getProgress() == 0 ? 800 : etSize.getProgress(),
                            etMargin.getProgress() == 0 ? 50 : etMargin.getProgress(),
                            dotScl == 0 ? 0.3f : dotScl,
                            ckbAutoColor.isChecked() ? defaultColorBg : Color.parseColor(etColorDark.getText().toString()),
                            ckbAutoColor.isChecked() ? defaultColor : Color.parseColor(etColorLight.getText().toString()),
                            backgroundImage,
                            ckbWhiteMargin.isChecked(),
                            ckbAutoColor.isChecked(),
                            ckbBinarize.isChecked(),
                            etBinarizeThreshold.getProgress() == 0 ? 128 : etBinarizeThreshold.getProgress(),
                            ckbRoundedDataDots.isChecked(),
                            logoImage,
                            etLogoMargin.getProgress() == 0 ? 10 : etLogoMargin.getProgress(),
                            etLogoCornerRadius.getProgress() == 0 ? 100 : etLogoCornerRadius.getProgress(),
                            logScl == 0 ? 10 : logScl
                    );
                } catch (Exception e) {

                    //  Toast.makeText(MainActivity.this, "Error occurred, please Check the boxes above.", Toast.LENGTH_LONG).show();

                    if (ckbAutoColor.isChecked()) {
                        Toast.makeText(CustomizeQrcodes.this, "Generating", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(CustomizeQrcodes.this, "Error:Auto Color Checkbox status is :" + ckbAutoColor.isChecked(), Toast.LENGTH_SHORT).show();
                    }

                }

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        acquireStoragePermissions();
    }

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
//dot scale function
   /* public double getConvertedValue(int intVal){
        double floatVal = 0.0;
        floatVal = 0.5f * intVal;
        return floatVal;
    }*/

    private void acquireStoragePermissions() {
        int permission = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && data.getData() != null) {
            try {
                Uri imageUri = data.getData();
                if (requestCode == BKG_IMAGE) {
                    //  requestCode=defaultColor;
                    backgroundImage = BitmapFactory.decodeFile(ContentHelper.absolutePathFromUri(this, imageUri));

                    Toast.makeText(this, "Background Color added.", Toast.LENGTH_SHORT).show();
                } else if (requestCode == LOGO_IMAGE) {
                    logoImage = BitmapFactory.decodeFile(ContentHelper.absolutePathFromUri(this, imageUri));
                    Toast.makeText(this, "Logo image added.", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
                if (requestCode == BKG_IMAGE) {
                    Toast.makeText(this, "Failed to add the background image.", Toast.LENGTH_SHORT).show();
                } else if (requestCode == LOGO_IMAGE) {
                    Toast.makeText(this, "Failed to add the logo image.", Toast.LENGTH_SHORT).show();
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

//Qr code Generting function

    private void generate(final String contents, final int size, final int margin, final float dotScale,
                          final int colorDark, final int colorLight, final Bitmap background, final boolean whiteMargin,
                          final boolean autoColor, final boolean binarize, final int binarizeThreshold, final boolean roundedDD,
                          final Bitmap logoImage, final int logoMargin, final int logoCornerRadius, final float logoScale) {
        if (generating) return;
        generating = true;
        progressDialog = new ProgressDialog.Builder(this).setMessage("Generating...").setCancelable(false).create();
        progressDialog.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    qrBitmap = AwesomeQRCode.create(contents, size, margin, dotScale, colorDark,
                            colorLight, background, whiteMargin, autoColor, binarize, binarizeThreshold,
                            roundedDD, logoImage, logoMargin, logoCornerRadius, logoScale);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //  qrCodeImageView.setBackgroundColor(defaultColorBg);
                            qrCodeImageView.setImageBitmap(qrBitmap);
                            configViewContainer.setVisibility(View.VISIBLE);
                            resultViewContainer.setVisibility(View.VISIBLE);
                            if (progressDialog != null) progressDialog.dismiss();
                            generating = false;
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (progressDialog != null) progressDialog.dismiss();
                            configViewContainer.setVisibility(View.VISIBLE);
                            resultViewContainer.setVisibility(View.VISIBLE);
                            generating = false;
                        }
                    });
                }
            }
        }).start();
    }
    //Picture Sving
    private void saveBitmap(Bitmap bitmap) {
        FileOutputStream fos = null;
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream.toByteArray();
            File outputFile = new File(getPublicContainer(), System.currentTimeMillis() + ".png");
            fos = new FileOutputStream(outputFile);
            fos.write(byteArray);
            fos.close();
            Toast.makeText(this, "Image saved to " + outputFile.getAbsolutePath(), Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to save the image.", Toast.LENGTH_LONG).show();
        }
    }


    //color picker for background
    public void openColorPickerBg(){

        /*final ColorPicker cp = new ColorPicker(MainActivity.this);
        cp.show();

        cp.enableAutoClose(); // Enable auto-dismiss for the dialog

        // Set a new Listener called when user click "select"
        cp.setCallback(new ColorPickerCallback() {
            @Override
            public void onColorChosen(@ColorInt int colora) {
                // Do whatever you want
                // Examples
                defaultColor=colora;
               // int startColor=defaultColor;
                //int endColor=defaultColorBg;
                //finalColor=defaultColor+defaultColorBg;
                //GradientDrawable mDrawable=new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, new int[] {startColor,endColor});
                //      //  int finalColor=(int)mDrawable; defaultColor=colora;
              //qrCodeImageView.setBackground(mDrawable);
                //mDrawable.setColor(de);
                //defaultColor=mDrawable;
               // finalColor=mDrawable;
            }
        });*/


        AmbilWarnaDialog colorPicker=new AmbilWarnaDialog(this, defaultColorBg, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onCancel(AmbilWarnaDialog dialog) {

            }

            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {
                defaultColorBg=color;
                // BKG_IMAGE=defaultColorBg;
                //qrCodeImageView.setBackgroundColor(defaultColorBg);
                // resultViewContainer.setBackgroundColor(defaultColorBg);
                //  rL.setBackgroundColor(defaultColor);
            }
        });
        colorPicker.show();
    }

//Dots Color Picker

    public void openColorPicker(){
        AmbilWarnaDialog colorPicker=new AmbilWarnaDialog(this, defaultColor, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onCancel(AmbilWarnaDialog dialog) {

            }

            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {
                defaultColor=color;
                ///qrCodeImageView.setBackgroundColor(Color.RED);
                //resultViewContainer.setBackgroundColor(defaultColor);
                //  rL.setBackgroundColor(defaultColor);
            }
        });
        colorPicker.show();
    }



    public static File getPublicContainer() {
        File musicContainer = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File aqr = new File(musicContainer, "AwesomeQR");
        if (aqr.exists() && !aqr.isDirectory()) {
            aqr.delete();
        }
        aqr.mkdirs();
        return aqr;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (configViewContainer.getVisibility() != View.VISIBLE) {
                configViewContainer.setVisibility(View.VISIBLE);
                resultViewContainer.setVisibility(View.GONE);
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
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
}
