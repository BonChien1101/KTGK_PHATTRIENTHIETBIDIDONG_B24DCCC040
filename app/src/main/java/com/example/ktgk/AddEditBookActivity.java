package com.example.ktgk;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ktgk.database.DatabaseHelper;
import com.example.ktgk.models.Book;

public class AddEditBookActivity extends AppCompatActivity {

    private TextView tvTitle;
    private EditText edtMaSach, edtTenSach, edtTacGia, edtTheLoai;
    private Button btnSaveBook, btnCancelBook;
    private ImageButton btnBack;
    private DatabaseHelper databaseHelper;
    private Book currentBook = null;
    private boolean isEditMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_book);

        databaseHelper = new DatabaseHelper(this);

        tvTitle = findViewById(R.id.tvTitle);
        edtMaSach = findViewById(R.id.edtMaSach);
        edtTenSach = findViewById(R.id.edtTenSach);
        edtTacGia = findViewById(R.id.edtTacGia);
        edtTheLoai = findViewById(R.id.edtTheLoai);
        btnSaveBook = findViewById(R.id.btnSaveBook);
        btnCancelBook = findViewById(R.id.btnCancelBook);
        btnBack = findViewById(R.id.btnBack);

        btnBack.setOnClickListener(v -> finish());

        if (getIntent().hasExtra("book")) {
            currentBook = (Book) getIntent().getSerializableExtra("book");
            if (currentBook != null) {
                isEditMode = true;
                tvTitle.setText("CHỈNH SỬA SÁCH");
                edtMaSach.setText(currentBook.getMaSach());
                edtMaSach.setEnabled(false);
                edtTenSach.setText(currentBook.getTenSach());
                edtTacGia.setText(currentBook.getTacGia());
                edtTheLoai.setText(currentBook.getTheLoai());
            }
        }

        btnCancelBook.setOnClickListener(v -> finish());

        btnSaveBook.setOnClickListener(v -> saveBook());
    }

    private void saveBook() {
        String maSach = edtMaSach.getText().toString().trim();
        String tenSach = edtTenSach.getText().toString().trim();
        String tacGia = edtTacGia.getText().toString().trim();
        String theLoai = edtTheLoai.getText().toString().trim();

        boolean hasError = false;

        if (theLoai.isEmpty()) {
            edtTheLoai.setError("Vui lòng nhập thể loại sách!");
            edtTheLoai.requestFocus();
            hasError = true;
        }

        if (tacGia.isEmpty()) {
            edtTacGia.setError("Vui lòng nhập tên tác giả!");
            edtTacGia.requestFocus();
            hasError = true;
        }

        if (tenSach.isEmpty()) {
            edtTenSach.setError("Vui lòng nhập tên sách!");
            edtTenSach.requestFocus();
            hasError = true;
        }

        if (maSach.isEmpty()) {
            edtMaSach.setError("Vui lòng nhập mã sách!");
            edtMaSach.requestFocus();
            hasError = true;
        }

        if (hasError) {
            return;
        }

        if (isEditMode) {
            currentBook.setTenSach(tenSach);
            currentBook.setTacGia(tacGia);
            currentBook.setTheLoai(theLoai);
            boolean success = databaseHelper.updateBook(currentBook);
            if (success) {
                Toast.makeText(this, "Cập nhật sách thành công!", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Cập nhật thất bại!", Toast.LENGTH_SHORT).show();
            }
        } else {
            if (databaseHelper.checkBookExists(maSach)) {
                edtMaSach.setError("Mã sách này đã tồn tại, vui lòng nhập mã khác!");
                edtMaSach.requestFocus();
                return;
            }

            Book newBook = new Book(maSach, tenSach, tacGia, theLoai, 0);
            boolean success = databaseHelper.insertBook(newBook);
            if (success) {
                Toast.makeText(this, "Thêm sách mới thành công!", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Thêm sách thất bại!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
