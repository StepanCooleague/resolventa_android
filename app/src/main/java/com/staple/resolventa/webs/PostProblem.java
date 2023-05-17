package com.staple.resolventa.webs;

import com.staple.resolventa.prosol.Problem;
import com.staple.resolventa.prosol.Solution;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PostProblem {
    private final PostInterface postInterface;
    private final String baseUrl;

    public PostProblem(String baseUrl) {
        this.baseUrl = baseUrl;
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        postInterface = retrofit.create(PostInterface.class);
    }

    private void post(Problem problem, Callback<Solution> callback) {
        Call<Solution> call = postInterface.createPost(baseUrl, problem);
        call.enqueue(callback);
    }

    public void post_and_handle(Problem problem, ResponseHandler handler){
        post(problem, handler);
    }
}
