package com.staple.resolventa.execruns;

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
    public static Bitmap render(File file, int page_number) throws IOException {
        PdfRenderer pdf_renderer;
        PdfRenderer.Page pdf_page;

        ParcelFileDescriptor fd = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY);
        pdf_renderer = new PdfRenderer(fd);
        pdf_page = pdf_renderer.openPage(page_number);

        Bitmap bitmap = Bitmap.createBitmap(pdf_page.getWidth(),
                pdf_page.getHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(Color.WHITE);

        pdf_page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);

        pdf_page.close();
        pdf_renderer.close();

        return bitmap;
    }

    public static String save_bitmap_to_cache(Bitmap bitmap, Controller controller) {
        try {
            String file_name = controller.getActivity().getString(R.string.temp_img);
            File cache_dir = controller.getActivity().getCacheDir();
            File cache_file = new File(cache_dir, file_name);
            FileOutputStream file_output_stream = new FileOutputStream(cache_file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, file_output_stream);
            file_output_stream.close();
            return cache_file.getAbsolutePath();
        } catch (IOException e) {
            controller.display_exception(e);
        }
        return null;
    }

    public static Bitmap load_image_from_cache(String file_path, Controller controller) {
        try {
            if (file_path != null) {
                File cache_file = new File(file_path);
                FileInputStream file_input_stream = new FileInputStream(cache_file);
                Bitmap bitmap = BitmapFactory.decodeStream(file_input_stream);
                file_input_stream.close();
                return bitmap;
            }
        } catch (IOException e) {
            controller.display_exception(e);
        }
        return null;
    }

}
