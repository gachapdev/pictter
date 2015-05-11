package com.elzup.pictter.pictter;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;

public class DeviceUtils {

    public static void saveToFile(Context context, Bitmap bitmap) {
        if (!sdcardWriteReady()) {
            return;
        }
        try {
            File file = new File(Environment.getExternalStorageDirectory()
                    .getPath() + "/" + Environment.DIRECTORY_PICTURES + "/pictter/");
            if (!file.exists()) {
                file.mkdir();
            }
            String AttachName = file.getAbsolutePath() + "/" + System.currentTimeMillis() + ".jpg";
            FileOutputStream out = new FileOutputStream(AttachName);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
            // /storage/emulated/0/images/1430541663207.jpg
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
