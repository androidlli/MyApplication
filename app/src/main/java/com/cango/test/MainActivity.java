package com.cango.test;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    final String TAG = MainActivity.this.getClass().getSimpleName();
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private TextView mTextMessage;
    private Button mBtnCamera;
    private ImageView ivShow;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mTextMessage.setText(R.string.title_home);
                    return true;
                case R.id.navigation_dashboard:
                    mTextMessage.setText(R.string.title_dashboard);
                    return true;
                case R.id.navigation_notifications:
                    mTextMessage.setText(R.string.title_notifications);
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        mBtnCamera = (Button) findViewById(R.id.btn_camera);
        ivShow = (ImageView) findViewById(R.id.iv_show);
        initListener();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            setPic();
        }
    }

    private void setPic() {
        int targetW = ivShow.getWidth();
        int targetH = ivShow.getHeight();
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mCurrentPhotoPath, options);
        int photoW = options.outWidth;
        int photoH = options.outHeight;
        Log.d(TAG, "photoW :"+photoW+" photoH :"+photoH);
        int min = Math.min(photoW / targetW, photoH / targetH);
        if (min<=0)
            min=1;
        options.inJustDecodeBounds=false;
        options.inSampleSize=min;
        Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, options);
        Log.d(TAG, "W "+bitmap.getWidth()+" H :"+bitmap.getHeight());
        ivShow.setImageBitmap(bitmap);
    }

    private void initListener() {
        mBtnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCamera();
            }

        });
    }

    private void openCamera() {
        boolean hasSystemFeature = getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA);
        if (hasSystemFeature) {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
//                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                File photoFile = null;
                try {
                    photoFile = createImageFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (photoFile != null) {
                    Uri photoURI = FileProvider.getUriForFile(MainActivity.this, "com.cango.test.fileprovider", photoFile);
                    Log.d(TAG, "photoURI: " + photoURI.toString());
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }
            }
        }
    }

    String mCurrentPhotoPath;

    private File createImageFile() throws IOException {
        // Create an image file name
        File externalFilesDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        Log.d(TAG, "externalFilesDir path : " + externalFilesDir.getAbsolutePath());
        File filesDir = getFilesDir();
        Log.d(TAG, "filesDir path : " + filesDir.getAbsolutePath());
        File externalStorageDirectory = Environment.getExternalStorageDirectory();
        Log.d(TAG, "externalStorageDirectory: " + externalStorageDirectory.getAbsolutePath());
        Log.d(TAG, "CacheDir: " + getCacheDir().getAbsolutePath());
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

}
