package com.example.surdurango;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    ImageView img_play, img_pause, facebookImageView, whatsappImageView, sitiowebIV, logo;
    private RecyclerView recyclerView;
    private CarouselAdapter adapter;
    private List<String> dataList = new ArrayList<>();
    private Handler handler = new Handler(Looper.getMainLooper());
    private int currentIndex = 0;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        logo = findViewById(R.id.logo);
        img_play = findViewById(R.id.play);
        img_pause = findViewById(R.id.pause);
        recyclerView = findViewById(R.id.recyclerView);
        facebookImageView = findViewById(R.id.facebook);
        whatsappImageView = findViewById(R.id.whatsapp);
        sitiowebIV = findViewById(R.id.sitioweb);

        // Iniciar el reproductor automáticamente
        startService(new Intent(MainActivity.this, ForegroundService.class));
        Toast.makeText(MainActivity.this, "Reproducción iniciada automáticamente", Toast.LENGTH_SHORT).show();

        img_play.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startService(new Intent(MainActivity.this, ForegroundService.class));
                img_play.setVisibility(View.GONE);
                img_pause.setVisibility(View.VISIBLE);
                Toast.makeText(MainActivity.this, "Reproducción iniciada", Toast.LENGTH_SHORT).show();
            }
        });

        img_pause.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                stopService(new Intent(MainActivity.this, ForegroundService.class));
                img_pause.setVisibility(View.GONE);
                img_play.setVisibility(View.VISIBLE);
                Toast.makeText(MainActivity.this, "Reproducción pausada", Toast.LENGTH_SHORT).show();
            }
        });

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
                    Toast.makeText(MainActivity.this, "URL de Facebook inválida", Toast.LENGTH_SHORT).show();
                    Log.e("MainActivity", "Error al abrir URL de Facebook: " + e.getMessage());
                }
            }
        });

        whatsappImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("MainActivity", "WhatsApp ImageView clicked");
                // Llama al método para obtener el número dinámico desde Firebase
                retrieveWhatsAppNumber();
            }
        });

        sitiowebIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("MainActivity", "Sitioweb ImageView clicked");
                Intent intent = new Intent(MainActivity.this, MainActivity2.class);
                startActivity(intent);
            }
        });
        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("MainActivity", "Logo ImageView clicked");
                Intent intent = new Intent(MainActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });


        setupRecyclerView();
        startAutoScroll();
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
                        Log.d("MainActivity", "Número recuperado como Long: " + whatsappNumber);
                        openWhatsApp(whatsappNumber);
                    } else if (value instanceof String) {
                        // Si el valor ya es String
                        String whatsappNumber = (String) value;
                        Log.d("MainActivity", "Número recuperado como String: " + whatsappNumber);
                        openWhatsApp(whatsappNumber);
                    } else {
                        Log.e("MainActivity", "El nodo contiene un tipo de dato inesperado");
                        Toast.makeText(MainActivity.this, "Número no disponible", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.e("MainActivity", "El nodo LinkWpp no existe en Firebase");
                    Toast.makeText(MainActivity.this, "Número no disponible", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("MainActivity", "Error al acceder a Firebase: " + error.getMessage());
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
            Toast.makeText(MainActivity.this, "Error al abrir WhatsApp", Toast.LENGTH_SHORT).show();
            Log.e("MainActivity", "Error al iniciar Intent de WhatsApp: " + e.getMessage());
        }
    }

    private void setupRecyclerView() {
        DatabaseReference database = FirebaseDatabase.getInstance().getReference().child("Anuncios");
        adapter = new CarouselAdapter(dataList);

        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(adapter);

        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dataList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String value = dataSnapshot.getKey();
                    dataList.add(value);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("MainActivity", "Error al obtener datos", error.toException());
            }
        });
    }

    private void startAutoScroll() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (currentIndex == dataList.size()) {
                    currentIndex = 0;
                }
                recyclerView.smoothScrollToPosition(currentIndex++);
                handler.postDelayed(this, 3000); // Cambia la velocidad de desplazamiento aquí (3000 ms = 3 segundos)
            }
        }, 3000);
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
        handler.removeCallbacksAndMessages(null);
    }
}


