package com.staple.resolventa.webs;

import android.content.Context;

import androidx.annotation.NonNull;

import com.staple.resolventa.R;
import com.staple.resolventa.controllers.Controller;
import com.staple.resolventa.execruns.FileToCache;
import com.staple.resolventa.execruns.PdfToBitmap;
import com.staple.resolventa.prosol.ErrorProSolTypeException;
import com.staple.resolventa.prosol.ProSolTyper;
import com.staple.resolventa.prosol.Solution;

import java.io.File;
import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResponseHandler implements Callback<Solution> {
    private final Context context;
    private final Controller controller;

    public ResponseHandler(Controller controller) {
        this.controller = controller;
        this.context = controller.getActivity();
    }

    @Override
    public void onResponse(@NonNull Call<Solution> call, @NonNull Response<Solution> response) {
        if (response.isSuccessful()) {
            Solution result = response.body();
            try {
                if(result == null) return;
                ProSolTyper.check_prosol_type(context, result);
                String file_path = FileToCache.save(context, result.solution_content, context.getString(R.string.pdf_path));
                controller.setPdf_path(file_path);
                controller.show_result(PdfToBitmap.render(new File(file_path), 0));
                controller.set_sharing(true);
            } catch (IOException | ErrorProSolTypeException e) {
                controller.display_exception(e);
            }
        } else {
            try {
                if (response.errorBody() != null)
                    controller.display_exception(new Exception(response.errorBody().string()));
            } catch (IOException e) {
                controller.display_exception(e);
            }
        }
    }

    @Override
    public void onFailure(@NonNull Call<Solution> call, @NonNull Throwable t) {
        controller.display_exception(new Exception(t));
    }
}

