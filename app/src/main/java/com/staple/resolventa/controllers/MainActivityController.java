package com.staple.resolventa.controllers;

import android.graphics.Bitmap;
import android.widget.Toast;

import com.staple.resolventa.R;
import com.staple.resolventa.activities.MainActivity;
import com.staple.resolventa.prosol.Problem;
import com.staple.resolventa.webs.PostClass;

public class MainActivityController implements Controller {
    private String cur_type;
    private MainActivity activity;
    private PostClass model;

    public MainActivityController(MainActivity activity){
        this.activity = activity;
        model = new PostClass(activity.getString(R.string.base_url), this);
        cur_type = activity.getString(R.string.nst);
    }

    public String getCur_type() {
        return cur_type;
    }

    public void display_exception(Exception e){
        //TODO display exception
        Toast.makeText(activity, e.getMessage(), Toast.LENGTH_SHORT).show();
    }

    public void on_click_submit(){
        model.post_and_solve(activity, new Problem(cur_type, activity.getString(R.string.test_seq)));
    }

    public void show_result(Bitmap bitmap){
        activity.img.setImageBitmap(bitmap);
    }

}
