package com.elzup.pictter.pictter;

import android.graphics.Bitmap;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;

public class DeviceUtils {

    public static void saveToFile(Bitmap bitmap) {
        if (!sdcardWriteReady()) {
            return ;
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static boolean sdcardWriteReady() {
        String state = Environment.getExternalStorageState();
        return (Environment.MEDIA_MOUNTED.equals(state));

    }
}
