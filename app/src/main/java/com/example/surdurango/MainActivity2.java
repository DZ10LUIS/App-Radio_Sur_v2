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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity2 extends AppCompatActivity {

    ImageView facebookImageView, whatsappImageView, home, logo;
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        logo = findViewById(R.id.logo);
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
                Log.d("MainActivity2", "Facebook ImageView clicked");
                String facebookUrl = "https://www.facebook.com/profile.php?id=61559943816593";
                try {
                    Uri uri = Uri.parse(facebookUrl);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                } catch (Exception e) {
                    Toast.makeText(MainActivity2.this, "URL de Facebook inválida", Toast.LENGTH_SHORT).show();
                    Log.e("MainActivity2", "Error al abrir URL de Facebook: " + e.getMessage());
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

        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("MainActivity2", "Logo ImageView clicked");
                Intent intent = new Intent(MainActivity2.this, MainActivity.class);
                startActivity(intent);
            }
        });

        // Configurar acción dinámica para WhatsApp
        whatsappImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("MainActivity2", "WhatsApp ImageView clicked");
                retrieveWhatsAppNumber(); // Llama al método para obtener el número dinámico desde Firebase
            }
        });
    }

    private void retrieveWhatsAppNumber() {
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        DatabaseReference whatsappRef = database.child("LinkWpp");

        whatsappRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Verifica si el nodo existe
                if (snapshot.exists()) {
                    Object value = snapshot.getValue();
                    if (value instanceof Long) {
                        // Si el valor es un Long, conviértelo a String
                        String whatsappNumber = String.valueOf(value);
                        Log.d("MainActivity2", "Número recuperado como Long: " + whatsappNumber);
                        openWhatsApp(whatsappNumber);
                    } else if (value instanceof String) {
                        // Si el valor ya es String
                        String whatsappNumber = (String) value;
                        Log.d("MainActivity2", "Número recuperado como String: " + whatsappNumber);
                        openWhatsApp(whatsappNumber);
                    } else {
                        Log.e("MainActivity2", "El nodo contiene un tipo de dato inesperado");
                        Toast.makeText(MainActivity2.this, "Número no disponible", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.e("MainActivity2", "El nodo LinkWpp no existe en Firebase");
                    Toast.makeText(MainActivity2.this, "Número no disponible", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("MainActivity2", "Error al acceder a Firebase: " + error.getMessage());
            }
        });
    }

    private void openWhatsApp(String number) {
        String whatsappUrl = "https://wa.me/" + number;
        try {
            Uri uri = Uri.parse(whatsappUrl);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(MainActivity2.this, "Error al abrir WhatsApp", Toast.LENGTH_SHORT).show();
            Log.e("MainActivity2", "Error al iniciar Intent de WhatsApp: " + e.getMessage());
        }
    }
}
