package com.example.ktgk;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private Button btnManageBooks, btnManageLoans;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnManageBooks = findViewById(R.id.btnManageBooks);
        btnManageLoans = findViewById(R.id.btnManageLoans);

        btnManageBooks.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, BookActivity.class);
            startActivity(intent);
        });

        btnManageLoans.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, LoanActivity.class);
            startActivity(intent);
        });
    }
}
