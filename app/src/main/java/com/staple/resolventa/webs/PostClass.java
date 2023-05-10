package com.staple.resolventa.webs;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.pdf.PdfRenderer;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.widget.ImageView;

import com.staple.resolventa.activities.MainActivity;
import com.staple.resolventa.controllers.Controller;
import com.staple.resolventa.controllers.MainActivityController;
import com.staple.resolventa.execruns.FileToCache;
import com.staple.resolventa.prosol.ErrorProSolTypeException;
import com.staple.resolventa.prosol.ProSolTyper;
import com.staple.resolventa.prosol.Problem;
import com.staple.resolventa.prosol.Solution;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PostClass {
    private PostInterface postInterface;
    private String baseUrl;
    private Controller controller;

    public PostClass(String baseUrl, Controller controller) {
        this.baseUrl = baseUrl;
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        postInterface = retrofit.create(PostInterface.class);
        this.controller = controller;
    }

    private void post(Problem problem, Callback<Solution> callback) {
        Call<Solution> call = postInterface.createPost(baseUrl, problem);
        call.enqueue(callback);
    }

    private Bitmap pdf_to_bitmap(File file, int pageNumber) throws IOException {
        PdfRenderer mPdfRenderer;
        PdfRenderer.Page mPdfPage;

        ParcelFileDescriptor fileDescriptor = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY);
        mPdfRenderer = new PdfRenderer(fileDescriptor);
        mPdfPage = mPdfRenderer.openPage(pageNumber);

        Bitmap bitmap = Bitmap.createBitmap(mPdfPage.getWidth(),
                mPdfPage.getHeight(),
                Bitmap.Config.ARGB_8888);
        mPdfPage.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);

        mPdfPage.close();
        mPdfRenderer.close();

        return bitmap;
    }

    public void post_and_solve(Context context, Problem problem){
        post(problem, new Callback<Solution>() {
            @Override
            public void onResponse(Call<Solution> call, Response<Solution> response) {
                if (response.isSuccessful()) {
                    Solution result = response.body();
                    try {
                        ProSolTyper.check_prosol_type(context, result);
                        String filePath = FileToCache.save(context, result.solution_content, "result.pdf");
                        controller.show_result(pdf_to_bitmap(new File(filePath), 0));
                    } catch (IOException | ErrorProSolTypeException e) {
                        controller.display_exception(e);
                    }

                } else {
                    try {
                        controller.display_exception(new Exception(response.errorBody().string()));
                    } catch (IOException e) {
                        controller.display_exception(e);
                    }
                }
            }

            @Override
            public void onFailure(Call<Solution> call, Throwable t) {
                controller.display_exception(new Exception(t));
            }
        });
    }
}
