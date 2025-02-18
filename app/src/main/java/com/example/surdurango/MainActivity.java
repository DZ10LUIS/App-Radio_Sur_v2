package com.example.surdurango;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    ImageView img_play, img_pause;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        img_play = findViewById(R.id.play);
        img_pause = findViewById(R.id.pause);

        img_play.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startService(new Intent(MainActivity.this, ForegroundService.class));
                Toast.makeText(MainActivity.this, "Reproducción iniciada", Toast.LENGTH_SHORT).show();
            }
        });

        img_pause.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                stopService(new Intent(MainActivity.this, ForegroundService.class));
                Toast.makeText(MainActivity.this, "Reproducción pausada", Toast.LENGTH_SHORT).show();
            }
        });
    }
}


