package com.staple.resolventa.webs;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.pdf.PdfRenderer;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.widget.ImageView;

import com.staple.resolventa.activities.MainActivity;
import com.staple.resolventa.controllers.Controller;
import com.staple.resolventa.controllers.MainActivityController;
import com.staple.resolventa.execruns.FileToCache;
import com.staple.resolventa.execruns.PdfToBitmap;
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

    public void post_and_solve(Context context, Problem problem){
        post(problem, new Callback<Solution>() {
            @Override
            public void onResponse(Call<Solution> call, Response<Solution> response) {
                if (response.isSuccessful()) {
                    Solution result = response.body();
                    try {
                        ProSolTyper.check_prosol_type(context, result);
                        String filePath = FileToCache.save(context, result.solution_content, "result.pdf");
                        controller.show_result(PdfToBitmap.render(new File(filePath), 0));
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
