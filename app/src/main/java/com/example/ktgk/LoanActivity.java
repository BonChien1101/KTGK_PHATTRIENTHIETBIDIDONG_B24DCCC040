package com.example.ktgk;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ktgk.adapters.LoanAdapter;
import com.example.ktgk.database.DatabaseHelper;
import com.example.ktgk.models.Loan;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class LoanActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper;
    private RecyclerView recyclerViewLoans;
    private LoanAdapter loanAdapter;
    private FloatingActionButton fabAddLoan;
    private ImageButton btnBack;
    private List<Loan> loanList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loan);

        databaseHelper = new DatabaseHelper(this);
        recyclerViewLoans = findViewById(R.id.recyclerViewLoans);
        fabAddLoan = findViewById(R.id.fabAddLoan);
        btnBack = findViewById(R.id.btnBack);

        btnBack.setOnClickListener(v -> finish());

        recyclerViewLoans.setLayoutManager(new LinearLayoutManager(this));

        fabAddLoan.setOnClickListener(v -> {
            Intent intent = new Intent(LoanActivity.this, AddLoanActivity.class);
            startActivity(intent);
        });

        loadLoans();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadLoans();
    }

    private void loadLoans() {
        loanList = databaseHelper.getActiveLoans();
        if (loanAdapter == null) {
            loanAdapter = new LoanAdapter(this, loanList, loan -> showReturnDialog(loan));
            recyclerViewLoans.setAdapter(loanAdapter);
        } else {
            loanAdapter.updateList(loanList);
        }
    }

    private void showReturnDialog(Loan loan) {
        new AlertDialog.Builder(this)
                .setTitle("Xác nhận trả sách")
                .setMessage("Bạn muốn xác nhận trả sách mã '" + loan.getMaSach() + "'?")
                .setPositiveButton("Trả sách", (dialog, which) -> {
                    boolean success = databaseHelper.returnLoan(loan.getMaPhieu(), loan.getMaSach());
                    if (success) {
                        Toast.makeText(this, "Trả sách thành công!", Toast.LENGTH_SHORT).show();
                        loadLoans();
                    } else {
                        Toast.makeText(this, "Trả sách thất bại!", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Hủy", null)
                .show();
    }
}
