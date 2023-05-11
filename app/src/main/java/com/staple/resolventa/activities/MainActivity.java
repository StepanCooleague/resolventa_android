package com.staple.resolventa.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.staple.resolventa.R;
import com.staple.resolventa.controllers.MainActivityController;

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
        submit_btn.setOnClickListener(v -> controller.on_click_submit());
        share_btn.setOnClickListener(v -> controller.share_pdf());
        img.setOnTouchListener((arg0, event) -> controller.on_touch(event));
        controller.restore_state(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        controller.save_state(outState);
    }

}
