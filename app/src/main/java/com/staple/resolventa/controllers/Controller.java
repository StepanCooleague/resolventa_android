package com.staple.resolventa.controllers;

import android.graphics.Bitmap;

import com.staple.resolventa.activities.MainActivity;

public interface Controller {
    String getCur_type();
    MainActivity getActivity();
    void setPdf_path(String pdf_path);
    void display_exception(Exception e);
    void on_click_submit();
    void show_result(Bitmap bitmap);
    void set_sharing(boolean value);
    void share_pdf();
}
