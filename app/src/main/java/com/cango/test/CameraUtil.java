package com.cango.test;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;

/**
 * Created by cango on 2017/3/9.
 */

public class CameraUtil {
    /**
     * 检查是否存在照相机硬件
     * @return true or false
     */
    public static boolean checkCameraHardware(Context context){
        return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA);
    }

    public static Camera getCameraInstance(){
        Camera camera=null;
        try {
            camera=Camera.open();
        }catch (Exception e){
        }
        return camera;
    }
    public static Camera getCameraInstance(int id){
        Camera camera=null;
        try {
            camera=Camera.open(id);
        }catch (Exception e){
        }
        return camera;
    }
}
