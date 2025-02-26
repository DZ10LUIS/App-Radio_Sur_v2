package com.example.surdurango;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity2 extends AppCompatActivity {

    ImageView facebookImageView, whatsappImageView, home;
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        facebookImageView = findViewById(R.id.facebook);
        whatsappImageView = findViewById(R.id.whatsapp);
        home = findViewById(R.id.home);
        webView = findViewById(R.id.webview);

        // Configuración de EdgeToEdge, si es necesaria
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Configurar el WebView
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl("https://zeno.fm/radio/radio-sur-durango"); // Reemplaza con la URL que deseas mostrar

        facebookImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("MainActivity", "Facebook ImageView clicked");
                String facebookUrl = "https://www.facebook.com/profile.php?id=61559943816593";
                try {
                    Uri uri = Uri.parse(facebookUrl);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                } catch (Exception e) {
                    Toast.makeText(MainActivity2.this, "URL de Facebook inválida", Toast.LENGTH_SHORT).show();
                    Log.e("MainActivity", "Error al abrir URL de Facebook: " + e.getMessage());
                }
            }
        });

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("MainActivity2", "Home ImageView clicked");
                Intent intent = new Intent(MainActivity2.this, MainActivity.class);
                startActivity(intent);
            }
        });

        whatsappImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("MainActivity", "WhatsApp ImageView clicked");
                String whatsappUrl = "https://wa.me/5216751087260";
                try {
                    Uri uri = Uri.parse(whatsappUrl);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                } catch (Exception e) {
                    Toast.makeText(MainActivity2.this, "URL de WhatsApp inválida", Toast.LENGTH_SHORT).show();
                    Log.e("MainActivity", "Error al abrir URL de WhatsApp: " + e.getMessage());
                }
            }
        });
    }
}
