package com.staple.resolventa.execruns;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.pdf.PdfRenderer;
import android.os.ParcelFileDescriptor;

import com.staple.resolventa.R;
import com.staple.resolventa.controllers.Controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class PdfToBitmap {
    public static Bitmap render(File file, int pageNumber) throws IOException {
        PdfRenderer mPdfRenderer;
        PdfRenderer.Page mPdfPage;

        ParcelFileDescriptor fileDescriptor = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY);
        mPdfRenderer = new PdfRenderer(fileDescriptor);
        mPdfPage = mPdfRenderer.openPage(pageNumber);

        Bitmap bitmap = Bitmap.createBitmap(mPdfPage.getWidth(),
                mPdfPage.getHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(Color.WHITE);

        mPdfPage.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);

        mPdfPage.close();
        mPdfRenderer.close();

        return bitmap;
    }

    public static String save_bitmap_to_cache(Bitmap bitmap, Controller controller) {
        try {
            String fileName = controller.getActivity().getString(R.string.temp_img);
            FileOutputStream fileOutputStream = controller.getActivity().openFileOutput(fileName, Context.MODE_PRIVATE);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
            fileOutputStream.close();
            return fileName;
        } catch (IOException e) {
            controller.display_exception(e);
        }
        return null;
    }

    public static Bitmap load_image_from_cache(String fileName, Controller controller) {
        try {
            if (fileName != null) {
                FileInputStream fileInputStream = controller.getActivity().openFileInput(fileName);
                Bitmap bitmap = BitmapFactory.decodeStream(fileInputStream);
                fileInputStream.close();
                return bitmap;
            }
        } catch (IOException e) {
            controller.display_exception(e);
        }
        return null;
    }

}
