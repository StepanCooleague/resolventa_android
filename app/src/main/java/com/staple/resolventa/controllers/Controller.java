package com.staple.resolventa.controllers;

import android.graphics.Bitmap;

public interface Controller {
    void display_exception(Exception e);
    void on_click_submit();
    void show_result(Bitmap bitmap);
}
