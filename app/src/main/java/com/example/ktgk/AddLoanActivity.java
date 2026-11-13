package com.example.ktgk;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ktgk.database.DatabaseHelper;
import com.example.ktgk.models.Book;
import com.example.ktgk.models.Loan;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class AddLoanActivity extends AppCompatActivity {

    private Spinner spinnerBooks;
    private EditText edtNguoiMuon, edtSdt, edtNgayMuon, edtNgayTra;
    private Button btnSaveLoan, btnCancelLoan;
    private ImageButton btnBack;
    private DatabaseHelper databaseHelper;
    private List<Book> availableBookList;
    private Calendar calendarBorrow, calendarReturn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_loan);

        databaseHelper = new DatabaseHelper(this);

        spinnerBooks = findViewById(R.id.spinnerBooks);
        edtNguoiMuon = findViewById(R.id.edtNguoiMuon);
        edtSdt = findViewById(R.id.edtSdt);
        edtNgayMuon = findViewById(R.id.edtNgayMuon);
        edtNgayTra = findViewById(R.id.edtNgayTra);
        btnSaveLoan = findViewById(R.id.btnSaveLoan);
        btnCancelLoan = findViewById(R.id.btnCancelLoan);
        btnBack = findViewById(R.id.btnBack);

        btnBack.setOnClickListener(v -> finish());

        calendarBorrow = Calendar.getInstance();
        calendarReturn = Calendar.getInstance();
        calendarReturn.add(Calendar.DAY_OF_MONTH, 7);

        updateDateLabel(edtNgayMuon, calendarBorrow);
        updateDateLabel(edtNgayTra, calendarReturn);

        setupBookSpinner();
        setupDatePickers();

        btnCancelLoan.setOnClickListener(v -> finish());
        btnSaveLoan.setOnClickListener(v -> saveLoan());
    }

    private void setupBookSpinner() {
        availableBookList = databaseHelper.getAvailableBooks();
        List<String> displayList = new ArrayList<>();

        if (availableBookList.isEmpty()) {
            displayList.add("Không có sách nào có sẵn");
        } else {
            for (Book book : availableBookList) {
                displayList.add(book.getMaSach() + " - " + book.getTenSach());
            }
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, displayList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerBooks.setAdapter(adapter);
    }

    private void setupDatePickers() {
        edtNgayMuon.setOnClickListener(v -> showDatePicker(edtNgayMuon, calendarBorrow));
        edtNgayTra.setOnClickListener(v -> showDatePicker(edtNgayTra, calendarReturn));
    }

    private void showDatePicker(EditText editText, Calendar calendar) {
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, year1, month1, dayOfMonth) -> {
            calendar.set(Calendar.YEAR, year1);
            calendar.set(Calendar.MONTH, month1);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateDateLabel(editText, calendar);
        }, year, month, day);
        datePickerDialog.show();
    }

    private void updateDateLabel(EditText editText, Calendar calendar) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        editText.setText(sdf.format(calendar.getTime()));
    }

    private void saveLoan() {
        if (availableBookList == null || availableBookList.isEmpty()) {
            Toast.makeText(this, "Không có sách nào có sẵn để mượn!", Toast.LENGTH_SHORT).show();
            return;
        }

        int selectedPosition = spinnerBooks.getSelectedItemPosition();
        if (selectedPosition < 0 || selectedPosition >= availableBookList.size()) {
            Toast.makeText(this, "Vui lòng chọn một cuốn sách!", Toast.LENGTH_SHORT).show();
            return;
        }

        Book selectedBook = availableBookList.get(selectedPosition);
        String nguoiMuon = edtNguoiMuon.getText().toString().trim();
        String sdt = edtSdt.getText().toString().trim();
        String ngayMuon = edtNgayMuon.getText().toString().trim();
        String ngayTra = edtNgayTra.getText().toString().trim();

        boolean hasError = false;

        if (ngayTra.isEmpty()) {
            edtNgayTra.setError("Vui lòng chọn ngày dự kiến trả!");
            edtNgayTra.requestFocus();
            hasError = true;
        }

        if (ngayMuon.isEmpty()) {
            edtNgayMuon.setError("Vui lòng chọn ngày mượn!");
            edtNgayMuon.requestFocus();
            hasError = true;
        }

        if (sdt.isEmpty()) {
            edtSdt.setError("Vui lòng nhập số điện thoại!");
            edtSdt.requestFocus();
            hasError = true;
        }

        if (nguoiMuon.isEmpty()) {
            edtNguoiMuon.setError("Vui lòng nhập họ tên người mượn!");
            edtNguoiMuon.requestFocus();
            hasError = true;
        }

        if (hasError) {
            return;
        }

        Loan loan = new Loan(selectedBook.getMaSach(), nguoiMuon, sdt, ngayMuon, ngayTra, 0);
        boolean success = databaseHelper.insertLoan(loan);

        if (success) {
            Toast.makeText(this, "Tạo phiếu mượn thành công!", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Tạo phiếu mượn thất bại!", Toast.LENGTH_SHORT).show();
        }
    }
}
