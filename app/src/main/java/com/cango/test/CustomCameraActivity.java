package com.cango.test;

import android.Manifest;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

public class CustomCameraActivity extends AppCompatActivity {

    private final String TAG=getClass().getSimpleName();
    private static final int PERMISSIONS_REQUEST_CAMERA=1;
    private CameraPreView mPreView;
    private CoordinatorLayout mCoordinatorLayout;
    private FloatingActionButton fab;
    private Camera mCamera;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_camera);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCamera==null){
                    Snackbar.make(mCoordinatorLayout, "camera 是空的", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    return;
                }
                mPreView.takePicture();
            }
        });
        mCoordinatorLayout= (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        mPreView = (CameraPreView) findViewById(R.id.preView);
//        int numberOfCameras = Camera.getNumberOfCameras();

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onResume() {
        super.onResume();
        if (CameraUtil.checkCameraHardware(this)){
            //动态申请相机权限
            if (ContextCompat.checkSelfPermission(this,Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED){
                if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)){
                    ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA},PERMISSIONS_REQUEST_CAMERA);
                }else {
                    ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA},PERMISSIONS_REQUEST_CAMERA);
                }
            }else {
                mCamera=CameraUtil.getCameraInstance();
                if (mCamera!=null)
                    Log.d(TAG, "Camrea 开启成功");
                mPreView.setmCamera(mCamera);
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPreView.stopCameraPreViewAndFreeCamera();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case PERMISSIONS_REQUEST_CAMERA:
                if (grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    mCamera=CameraUtil.getCameraInstance();
                    if (mCamera!=null)
                        Log.d(TAG, "Camrea 开启成功");
                    mPreView.setmCamera(mCamera);
                }else{
                    Snackbar.make(mCoordinatorLayout, "自己去是设置里面开启相机权限吧", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
                break;
        }
    }

    private void safeCameraOpen(){

    }
}
