package com.example.ktgk;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ktgk.adapters.BookAdapter;
import com.example.ktgk.database.DatabaseHelper;
import com.example.ktgk.models.Book;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class BookActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper;
    private RecyclerView recyclerViewBooks;
    private BookAdapter bookAdapter;
    private SearchView searchView;
    private FloatingActionButton fabAddBook;
    private ImageButton btnBack;
    private List<Book> bookList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);

        databaseHelper = new DatabaseHelper(this);
        recyclerViewBooks = findViewById(R.id.recyclerViewBooks);
        searchView = findViewById(R.id.searchView);
        fabAddBook = findViewById(R.id.fabAddBook);
        btnBack = findViewById(R.id.btnBack);

        btnBack.setOnClickListener(v -> finish());

        recyclerViewBooks.setLayoutManager(new LinearLayoutManager(this));

        fabAddBook.setOnClickListener(v -> {
            Intent intent = new Intent(BookActivity.this, AddEditBookActivity.class);
            startActivity(intent);
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterBooks(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterBooks(newText);
                return true;
            }
        });

        loadBooks();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadBooks();
    }

    private void loadBooks() {
        bookList = databaseHelper.getAllBooks();
        if (bookAdapter == null) {
            bookAdapter = new BookAdapter(this, bookList, new BookAdapter.OnBookActionListener() {
                @Override
                public void onEdit(Book book) {
                    Intent intent = new Intent(BookActivity.this, AddEditBookActivity.class);
                    intent.putExtra("book", book);
                    startActivity(intent);
                }

                @Override
                public void onDelete(Book book) {
                    showDeleteDialog(book);
                }
            });
            recyclerViewBooks.setAdapter(bookAdapter);
        } else {
            bookAdapter.updateList(bookList);
        }
    }

    private void filterBooks(String query) {
        if (query == null || query.trim().isEmpty()) {
            bookList = databaseHelper.getAllBooks();
        } else {
            bookList = databaseHelper.searchBooks(query.trim());
        }
        if (bookAdapter != null) {
            bookAdapter.updateList(bookList);
        }
    }

    private void showDeleteDialog(Book book) {
        if (book.getTrangThai() == 1) {
            Toast.makeText(this, "Không thể xóa sách đang được mượn!", Toast.LENGTH_SHORT).show();
            return;
        }

        new AlertDialog.Builder(this)
                .setTitle("Xác nhận xóa")
                .setMessage("Bạn có chắc chắn muốn xóa sách '" + book.getTenSach() + "'?")
                .setPositiveButton("Xóa", (dialog, which) -> {
                    boolean success = databaseHelper.deleteBook(book.getMaSach());
                    if (success) {
                        Toast.makeText(this, "Xóa sách thành công", Toast.LENGTH_SHORT).show();
                        loadBooks();
                    } else {
                        Toast.makeText(this, "Xóa sách thất bại", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Hủy", null)
                .show();
    }
}
