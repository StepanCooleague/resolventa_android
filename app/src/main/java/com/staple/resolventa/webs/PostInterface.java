package com.staple.resolventa.webs;

import com.staple.resolventa.prosol.Problem;
import com.staple.resolventa.prosol.Solution;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface PostInterface {
    @POST
    Call<Solution> createPost(@Url String url, @Body Problem body);
}
