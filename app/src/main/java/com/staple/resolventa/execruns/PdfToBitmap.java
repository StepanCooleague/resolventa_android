package com.staple.resolventa.execruns;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.pdf.PdfRenderer;
import android.os.ParcelFileDescriptor;

import java.io.File;
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
}
