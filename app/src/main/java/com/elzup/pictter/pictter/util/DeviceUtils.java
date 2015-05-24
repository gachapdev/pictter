package com.elzup.pictter.pictter.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.os.Environment;

import com.elzup.pictter.pictter.R;

import java.io.File;
import java.io.FileOutputStream;

public class DeviceUtils {

    public static void saveToFile(Context context, Bitmap bitmap) {
        if (!sdcardWriteReady()) {
            return;
        }
        try {
            File file = new File(Environment.getExternalStorageDirectory()
                    .getPath() + "/" + Environment.DIRECTORY_PICTURES + context.getResources().getString(R.string.path_image_stroage));
            if (!file.exists()) {
                file.mkdir();
            }
            String AttachName = file.getAbsolutePath() + "/" + System.currentTimeMillis() + "." + context.getResources().getString(R.string.extension_save_image);
            FileOutputStream out = new FileOutputStream(AttachName);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
            mediaScan(context, new String[]{AttachName});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void mediaScan(Context context, String[] paths) {
        String[] mimeTypes = {"image/jpeg"};
        MediaScannerConnection.scanFile(context, paths, mimeTypes, null);
    }

    private static boolean sdcardWriteReady() {
        String state = Environment.getExternalStorageState();
        return (Environment.MEDIA_MOUNTED.equals(state));

    }
}
