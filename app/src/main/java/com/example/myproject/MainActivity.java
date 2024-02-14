package com.example.myproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private EditText text;
    private ImageButton logoutbtn;
    private ImageButton searchtextbtn;
    private ImageButton searchvoicebtn;
    private ImageButton searchcamerabtn;
    private Toolbar toolbar;
    private FragmentManager fragmentManager;
    private BottomNavigationView bottomNavigationView;
    private static final int PICK_IMAGE_REQUEST = 1;
    private byte[] selectedImageBytes;

    private static final int SPEECH_REQUEST_CODE = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        logoutbtn = findViewById(R.id.btn_logout);
        text = findViewById(R.id.edt_search);
        searchtextbtn = findViewById(R.id.btn_text_search);
        searchvoicebtn = findViewById(R.id.btn_voice_search);
        searchcamerabtn = findViewById(R.id.btn_camera);
        toolbar = findViewById(R.id.toolbar_nav);
        bottomNavigationView = findViewById(R.id.bottom_nav);

        setSupportActionBar(toolbar);
        bottomNavigationView.setBackground(null);

        fragmentManager = getSupportFragmentManager();

        logoutbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences preferences = getSharedPreferences("checkbox", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("remember", "false");
                editor.apply();
                finish();
            }
        });

        searchtextbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchText = text.getText().toString();
                openFragment(new viewsearchFragment(searchText));
            }
        });

        searchvoicebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startVoiceRecognition();
            }
        });

        searchcamerabtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openBarcodeScanner();            }
        });

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.home) {
                    openFragment(new homamainFragment());
                    return true;
                } else if (itemId == R.id.category) {
                    openFragment(new catmainFragment());
                    return true;
                } else if (itemId == R.id.product) {
                    openFragment(new productmainFragment());
                    return true;
                } else if (itemId == R.id.shopping_cart) {
                    openFragment(new shoppingcartFragment());
                    return true;
                } else if (itemId == R.id.order) {
                    openFragment(new ordermainFragment());
                    return true;
                }
                return false;
            }
        });

        openFragment(new homamainFragment());
    }

    private void openFragment(Fragment fragment) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();
    }

    private void startVoiceRecognition() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak now...");

        try {
            startActivityForResult(intent, SPEECH_REQUEST_CODE);
        } catch (Exception e) {
            Toast.makeText(this, "Speech recognition not supported on this device", Toast.LENGTH_SHORT).show();
        }
    }

    private void openBarcodeScanner() {
        new IntentIntegrator(this).initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SPEECH_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            handleVoiceRecognitionResult(data);
        } else {
            handleBarcodeScanResult(requestCode, resultCode, data);
        }
    }

    private void handleVoiceRecognitionResult(Intent data) {
        ArrayList<String> results = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
        if (results != null && !results.isEmpty()) {
            String voiceSearchQuery = results.get(0);
            openFragment(new viewsearchFragment(voiceSearchQuery));
        } else {
            Toast.makeText(this, "No speech input", Toast.LENGTH_SHORT).show();
        }
    }
    private void handleBarcodeScanResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() != null) {
                String scannedBarcode = result.getContents();
                performBarcodeSearch(scannedBarcode);
            } else {
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
    private void performBarcodeSearch(String barcode) {
        DatabaseHelper databaseHelper3 = new DatabaseHelper(this);
        Product product = databaseHelper3.getProductByBarcode(barcode);

        if (product != null) {
            openFragment(new viewsearchcameraFragment(barcode));
        } else {
            Toast.makeText(this, "No barcode found", Toast.LENGTH_SHORT).show();

        }
    }

}