package com.staple.resolventa.controllers;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.staple.resolventa.R;
import com.staple.resolventa.activities.MainActivity;
import com.staple.resolventa.execruns.PdfToBitmap;
import com.staple.resolventa.prosol.Problem;
import com.staple.resolventa.webs.PostClass;

public class MainActivityController implements Controller {
    private static final String KEY_EDIT_TEXT = "key_edit_text";
    private static final String KEY_IMAGE_URI = "key_image_uri";

    private String cur_type;
    private MainActivity activity;
    private PostClass model;
    private float mx, my;

    public MainActivityController(MainActivity activity){
        this.activity = activity;
        model = new PostClass(activity.getString(R.string.base_url), this);
        cur_type = activity.getString(R.string.nst);
    }

    public String getCur_type() {
        return cur_type;
    }

    public MainActivity getActivity() {
        return activity;
    }

    public void display_exception(Exception e){
        //TODO display exception
        Toast.makeText(activity, e.getMessage(), Toast.LENGTH_SHORT).show();
    }

    public void on_click_submit(){
        model.post_and_solve(activity, new Problem(cur_type, activity.edit_text.getText().toString()));
    }

    public void show_result(Bitmap bitmap){
        activity.img.setImageBitmap(bitmap);
    }

    public void save_state(Bundle out_state){
        out_state.putString(KEY_EDIT_TEXT, activity.edit_text.getText().toString());

        Drawable drawable = activity.img.getDrawable();
        if (drawable instanceof BitmapDrawable) {
            Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
            String image_uri = PdfToBitmap.save_bitmap_to_cache(bitmap, this);
            out_state.putString(KEY_IMAGE_URI, image_uri);
        }
    }

    public void restore_state(Bundle saved_state){
        if (saved_state != null && saved_state.containsKey(KEY_EDIT_TEXT)) {
            String edit_text_content = saved_state.getString(KEY_EDIT_TEXT);
            if(edit_text_content != null)
                activity.edit_text.setText(edit_text_content);
        }

        if (saved_state != null && saved_state.containsKey(KEY_IMAGE_URI)) {
            String image_uri = saved_state.getString(KEY_IMAGE_URI);
            Bitmap bitmap = null;
            if(image_uri != null)
                bitmap = PdfToBitmap.load_image_from_cache(image_uri, this);
            if(bitmap != null)
                show_result(bitmap);
        }
    }

    public boolean on_touch(MotionEvent event){
        float curX, curY;

        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                mx = event.getX();
                my = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                curX = event.getX();
                curY = event.getY();
                activity.img.scrollBy((int) (mx - curX), (int) (my - curY));
                mx = curX;
                my = curY;
                break;
            case MotionEvent.ACTION_UP:
                curX = event.getX();
                curY = event.getY();
                activity.img.scrollBy((int) (mx - curX), (int) (my - curY));
                break;
        }
        return true;
    }


}
