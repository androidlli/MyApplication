package com.cango.test;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.media.ExifInterface;
import android.os.Environment;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by cango on 2017/3/9.
 */

public class CameraPreView extends SurfaceView implements SurfaceHolder.Callback {

    private String TAG = "CameraPreView";
    private Camera mCamera;
    private SurfaceHolder mHolder;
    private Context context;
    private int mScreenWidth, mScreenHeight;

    public CameraPreView(Context context) {
        super(context);
        this.context = context;
    }

    public CameraPreView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public CameraPreView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }


    public void setmCamera(Camera camera) {
        WindowManager WM = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        WM.getDefaultDisplay().getMetrics(outMetrics);
        mScreenWidth = outMetrics.widthPixels;
        mScreenHeight = outMetrics.heightPixels;
        mCamera = camera;
//        setCameraParams(mCamera,mScreenWidth,mScreenHeight);
        mHolder = getHolder();
        mHolder.addCallback(this);
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    private void setCameraParamters() {
        if (mCamera == null) return;
        Camera.Parameters parameters = mCamera.getParameters();
        List<String> supportedFlashModes = parameters.getSupportedFlashModes();
        if (supportedFlashModes != null) {
            if (supportedFlashModes.contains(Camera.Parameters.FLASH_MODE_AUTO)) {
                parameters.setFlashMode(Camera.Parameters.FLASH_MODE_AUTO);
                Log.d(TAG, "setCameraParamters: 开启自动闪关灯");
            }
        }
        List<String> supportedFocusModes = parameters.getSupportedFocusModes();
        if (supportedFocusModes != null) {
            if (supportedFocusModes.contains(Camera.Parameters.FOCUS_MODE_AUTO)) {
                parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
                Log.d(TAG, "setCameraParamters: 开启自动聚焦");
            }
        }
        if (parameters.getSupportedFocusModes().contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);// 连续对焦模式
        }

        parameters.setJpegQuality(100); // 设置照片质量
        mCamera.cancelAutoFocus();//自动对焦。

        setCameraDisplayOrientation((Activity) context,0,mCamera);
        setPreViewAndPictrueAndSurSize(parameters);

        mCamera.setParameters(parameters);
    }

    private void setPreViewAndPictrueAndSurSize(Camera.Parameters parameters) {
        List<Camera.Size> supportedPictureSizes = parameters.getSupportedPictureSizes();
        for (Camera.Size size:supportedPictureSizes){
            Log.d(TAG, "SupportedPicture WidthSize : "+size.width+" HeightSize :"+size.height);
        }
        Camera.Size pictureSize = getProoperSize(supportedPictureSizes, (float) mScreenHeight / mScreenWidth);
        Log.d(TAG, "pictureWidthSize :"+pictureSize.width+"pictureHeightSize :"+pictureSize.height);
        parameters.setPictureSize(pictureSize.width,pictureSize.height);

        List<Camera.Size> supportedPreviewSizes = parameters.getSupportedPreviewSizes();
        for (Camera.Size size:supportedPreviewSizes){
            Log.d(TAG, "supportedPreviewSizes WidthSize : "+size.width+" HeightSize :"+size.height);
        }
        Camera.Size previewSize = getProoperSize(supportedPreviewSizes, (float) mScreenHeight / mScreenWidth);
        Log.d(TAG, "previewWidthSize :"+previewSize.width+"previewHeightSize :"+previewSize.height);
        parameters.setPreviewSize(previewSize.width,previewSize.height);

        float w = pictureSize.width;
        float h = pictureSize.height;
        this.setLayoutParams(new FrameLayout.LayoutParams((int) (mScreenHeight*(h/w)), mScreenHeight));
    }

    /**
     * 从列表中选取合适的分辨率
     * 默认w:h = 4:3
     * <p>注意：这里的w对应屏幕的height
     *            h对应屏幕的width<p/>
     */
    private Camera.Size getProoperSize(List<Camera.Size> sizeList ,float screenRatio){
        Camera.Size result=null;
        for (Camera.Size size :sizeList){
            float currentRatio = ((float)size.width) / size.height;
            if (currentRatio==screenRatio){
                result=size;
                break;
            }
        }
        if (result==null){
            for (Camera.Size size :sizeList){
                float currentRatio = ((float)size.width) / size.height;
                if (currentRatio==4f/3){
                    result=size;
                    break;
                }
            }
        }
        return result;
    }

    private void startCameraPreView() {
        if (mCamera == null)
            throw new IllegalStateException("mCamera may be null");
        try {
            setCameraParamters();
            mCamera.setPreviewDisplay(mHolder);
            mCamera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void stopCameraPreView() {
        if (mCamera != null) {
            mCamera.stopPreview();
        }
    }

    public void stopCameraPreViewAndFreeCamera() {
        stopCameraPreView();
        if (mCamera != null)
            mCamera.release();
        mCamera = null;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        startCameraPreView();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        stopCameraPreView();
    }

    public void takePicture() {
        //设置参数,并拍照
//        setCameraParams(mCamera, mScreenWidth, mScreenHeight);
        // 当调用camera.takePiture方法后，camera关闭了预览，这时需要调用startPreview()来重新开启预览
        mCamera.takePicture(null, null, jpeg);
    }

    //创建jpeg图片回调数据对象
    private Camera.PictureCallback jpeg = new Camera.PictureCallback() {

        @Override
        public void onPictureTaken(byte[] data, Camera Camera) {
            BufferedOutputStream bos = null;
            Bitmap bm = null,bitmap=null;
            File image = null;
            try {
                // 获得图片
                bm = BitmapFactory.decodeByteArray(data, 0, data.length);
                bitmap = setTakePicktrueOrientation(0, bm);
                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                    String imageFileName = "JPEG_" + timeStamp + "_";
                    File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                    image = File.createTempFile(
                            imageFileName,  /* prefix */
                            ".jpg",         /* suffix */
                            storageDir      /* directory */
                    );
                    bos = new BufferedOutputStream(new FileOutputStream(image));
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);//将图片压缩到流中

                } else {
                    Toast.makeText(context, "没有检测到内存卡", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    bos.flush();//输出
                    bos.close();//关闭
                    bm.recycle();// 回收bitmap空间
                    bitmap.recycle();;
                    mCamera.stopPreview();// 关闭预览
                    mCamera.startPreview();// 开启预览
                    Log.d(TAG, "onPictureTaken: Degree is "+getBitmapDegree(image.getAbsolutePath()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    };

    /**
     * 设置预览方向（手机方向和摄像头方向不同）
     * @param activity
     * @param cameraIo
     * @param camera
     */
    public void setCameraDisplayOrientation(Activity activity, int cameraIo, Camera camera){
        Camera.CameraInfo info=new Camera.CameraInfo();
        Camera.getCameraInfo(cameraIo,info);
        int rotation=activity.getWindowManager().getDefaultDisplay().getRotation();
        Log.d(TAG, "setCameraDisplayOrientation: rotation is "+rotation);
        int degress=0;
        switch(rotation){
            case Surface.ROTATION_0:
                degress=0;
                break;
            case Surface.ROTATION_90:
                degress=90;
                break;
            case Surface.ROTATION_180:
                degress=180;
                break;
            case Surface.ROTATION_270:
                degress=270;
                break;
        }
        int result;
        Log.d(TAG, "setCameraDisplayOrientation: info.orientation is "+info.orientation);
        if(info.facing==Camera.CameraInfo.CAMERA_FACING_FRONT){
            result = (info.orientation + degress) % 360;
                    result = (360 - result) % 360;
        } else { // back-facing
            result = (info.orientation - degress + 360) % 360;
        }
        Log.d(TAG, "setCameraDisplayOrientation: result is "+result);
        camera.setDisplayOrientation(result);
    }

    /**
     * 获取图片的旋转角度
     *
     * @param path 图片绝对路径
     * @return 图片的旋转角度
     */
    public static int getBitmapDegree(String path) {
        int degree = 0;
        try {
            // 从指定路径下读取图片，并获取其EXIF信息
            ExifInterface exifInterface = new ExifInterface(path);
            // 获取图片的旋转信息
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    public Bitmap setTakePicktrueOrientation(int id, Bitmap bitmap) {
        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(id, info);
        bitmap = rotaingImageView(id, info.orientation, bitmap);
        return bitmap;
    }

    /**
     * 把相机拍照返回照片转正
     *
     * @param angle 旋转角度
     * @return bitmap 图片
     */
    public  Bitmap rotaingImageView(int id, int angle, Bitmap bitmap) {
        //旋转图片 动作
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        //加入翻转 把相机拍照返回照片转正
        if (id == 1) {
            matrix.postScale(-1, 1);
        }
        // 创建新的图片
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
                bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return resizedBitmap;
    }
}
