package com.ktay.DailySelfie.util;

import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CameraUtil {

    public static final int MEDIA_TYPE_IMAGE = 1;
    private static final String DIR_NAME = "DailySelfie";

    /** Create a file Uri for saving an image or video */
    public static Uri getOutputMediaFileUri(File file){
        return Uri.fromFile(file);
    }

    /** Create a File for saving an image or video */
    public static File getOutputMediaFile(int type){

        if (!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            Log.d(DIR_NAME, "media storage is not mounted");
            return null;
        }

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), DIR_NAME);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()){
            if (!mediaStorageDir.mkdirs()){
                Log.d(DIR_NAME, "failed to create directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE){
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "IMG_"+ timeStamp + ".jpg");
        } else {
            return null;
        }

        return mediaFile;
    }
}
