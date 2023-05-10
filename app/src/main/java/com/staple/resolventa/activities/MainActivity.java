package com.staple.resolventa.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.pdf.PdfRenderer;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

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
    public Button button;

    private MainActivityController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        controller = new MainActivityController(this);
        img = findViewById(R.id.imageView);
        button = findViewById(R.id.submitbtn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                controller.on_click_submit();
            }
        });
    }

}
