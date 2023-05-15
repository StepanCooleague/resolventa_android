package com.staple.resolventa.webs;

import android.content.Context;

import com.staple.resolventa.controllers.Controller;
import com.staple.resolventa.prosol.Problem;
import com.staple.resolventa.prosol.Solution;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PostProblem {
    private final PostInterface postInterface;
    private final String baseUrl;
    private final Controller controller;

    public PostProblem(String baseUrl, Controller controller) {
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

    public void post_and_handle(Context context, Problem problem){
        post(problem, new ResponseHandler(context, controller));
    }
}
