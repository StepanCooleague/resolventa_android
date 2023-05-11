package com.staple.resolventa.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.pdf.PdfRenderer;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.staple.resolventa.R;
import com.staple.resolventa.controllers.MainActivityController;
import com.staple.resolventa.execruns.FileToCache;
import com.staple.resolventa.prosol.Problem;
import com.staple.resolventa.prosol.Solution;
import com.staple.resolventa.webs.PostClass;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    public ImageView img;
    public Button submit_btn;
    public FloatingActionButton share_btn;
    public EditText edit_text;
    public ConstraintLayout main_layout;

    private MainActivityController controller;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        controller = new MainActivityController(this);
        img = findViewById(R.id.imageView);
        submit_btn = findViewById(R.id.submit_button);
        share_btn = findViewById(R.id.share_button);
        edit_text = findViewById(R.id.editText);
        main_layout = findViewById(R.id.main_layout);
        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                controller.on_click_submit();
            }
        });
        share_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                controller.share_pdf();
            }
        });
        img.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View arg0, MotionEvent event) {
                return controller.on_touch(event);
            }
        });
        controller.restore_state(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        controller.save_state(outState);
    }

}
