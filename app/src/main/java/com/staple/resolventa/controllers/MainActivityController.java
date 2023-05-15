package com.staple.resolventa.controllers;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.core.content.FileProvider;

import com.google.android.material.snackbar.Snackbar;
import com.staple.resolventa.R;
import com.staple.resolventa.activities.MainActivity;
import com.staple.resolventa.execruns.PdfToBitmap;
import com.staple.resolventa.prosol.Problem;
import com.staple.resolventa.webs.PostProblem;

import java.io.File;
import java.util.Objects;

public class MainActivityController implements Controller {
    private final String KEY_EDIT_TEXT;
    private final String KEY_IMAGE_URI;
    private final String KEY_ENABLED_SHARING;
    private final String KEY_PDF_PATH;

    private final String cur_type;
    private String pdf_path;
    private final MainActivity activity;
    private final PostProblem postman;
    private final Animation fade_in;
    private float mx, my;

    public MainActivityController(MainActivity activity){
        this.activity = activity;
        postman = new PostProblem(activity.getString(R.string.base_url), this);
        cur_type = activity.getString(R.string.nst);
        KEY_EDIT_TEXT = activity.getString(R.string.key_edit_text);
        KEY_IMAGE_URI = activity.getString(R.string.key_image_uri);
        KEY_ENABLED_SHARING = activity.getString(R.string.key_enabled_sharing);
        KEY_PDF_PATH = activity.getString(R.string.key_pdf_path);
        fade_in = AnimationUtils.loadAnimation(activity, R.anim.crossfade);
    }

    public void setPdf_path(String pdf_path) {
        this.pdf_path = pdf_path;
    }

    public String getCur_type() {
        return cur_type;
    }

    public MainActivity getActivity() {
        return activity;
    }

    public void display_exception(Exception e){
        Snackbar snackbar = Snackbar.make(activity.main_layout, Objects.requireNonNull(e.getMessage()), Snackbar.LENGTH_LONG);
        snackbar.setTextColor(0xFFFFFFFF);
        snackbar.setBackgroundTint(0xFFB71C1C);
        snackbar.show();
    }

    public void on_click_submit(){
        postman.post_and_handle(activity, new Problem(cur_type, activity.edit_text.getText().toString()));
    }

    public void show_result(Bitmap bitmap){
        activity.img.setImageBitmap(bitmap);
        activity.img.startAnimation(fade_in);
    }

    public void set_sharing(boolean value){
        activity.share_btn.setEnabled(value);
    }

    public void save_state(Bundle out_state){
        out_state.putString(KEY_EDIT_TEXT, activity.edit_text.getText().toString());
        out_state.putString(KEY_PDF_PATH, pdf_path);
        out_state.putBoolean(KEY_ENABLED_SHARING, activity.share_btn.isEnabled());

        Drawable drawable = activity.img.getDrawable();
        if (drawable instanceof BitmapDrawable) {
            Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
            String image_uri = PdfToBitmap.save_bitmap_to_cache(bitmap, this);
            out_state.putString(KEY_IMAGE_URI, image_uri);
        }
    }

    public void restore_state(Bundle saved_state){
        if(saved_state == null)
            return;

        if (saved_state.containsKey(KEY_EDIT_TEXT)) {
            String edit_text_content = saved_state.getString(KEY_EDIT_TEXT);
            if(edit_text_content != null)
                activity.edit_text.setText(edit_text_content);
        }

        if (saved_state.containsKey(KEY_PDF_PATH)) {
            String loaded_pdf_path = saved_state.getString(KEY_PDF_PATH);
            if(loaded_pdf_path != null)
                pdf_path = loaded_pdf_path;
        }

        if (saved_state.containsKey(KEY_ENABLED_SHARING)) {
            set_sharing(saved_state.getBoolean(KEY_ENABLED_SHARING));
        }

        if (saved_state.containsKey(KEY_IMAGE_URI)) { //using uri for checking null pointer
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

    public void share_pdf() {
        File fileToShare = new File(pdf_path);
        Uri contentUri = FileProvider.getUriForFile(activity, activity.getApplicationContext().getPackageName() + ".fileprovider", fileToShare);

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("application/pdf");
        shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri);
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        activity.startActivity(Intent.createChooser(shareIntent, "Share PDF file"));
    }


}
