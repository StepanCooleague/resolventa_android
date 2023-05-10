package com.staple.resolventa.execruns;

import android.content.Context;
import android.os.Environment;
import android.util.Base64;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FileToCache {
    public static String save(Context context, String base64File, String fileName) throws IOException {
        byte[] decodedFile = Base64.decode(base64File, Base64.DEFAULT);
        File file = new File(context.getCacheDir(), fileName);
        FileOutputStream outputStream = new FileOutputStream(file);
        outputStream.write(decodedFile);
        return file.getAbsolutePath();
    }

}
