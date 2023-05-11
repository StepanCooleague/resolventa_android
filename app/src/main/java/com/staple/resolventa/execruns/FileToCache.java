package com.staple.resolventa.execruns;

import android.content.Context;
import android.util.Base64;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileToCache {
    public static String save(Context context, String base64File, String fileName) throws IOException {
        byte[] decoded_file = Base64.decode(base64File, Base64.DEFAULT);
        File file = new File(context.getCacheDir(), fileName);
        FileOutputStream output_stream = new FileOutputStream(file);
        output_stream.write(decoded_file);
        output_stream.close();
        return file.getAbsolutePath();
    }

}
