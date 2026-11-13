package com.example.ktgk.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.ktgk.models.Book;
import com.example.ktgk.models.Loan;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "LibraryManager.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_BOOKS = "Books";
    public static final String COLUMN_BOOK_ID = "ma_sach";
    public static final String COLUMN_BOOK_TITLE = "ten_sach";
    public static final String COLUMN_BOOK_AUTHOR = "tac_gia";
    public static final String COLUMN_BOOK_CATEGORY = "the_loai";
    public static final String COLUMN_BOOK_STATUS = "trang_thai";

    public static final String TABLE_LOANS = "Loans";
    public static final String COLUMN_LOAN_ID = "ma_phieu";
    public static final String COLUMN_LOAN_BOOK_ID = "ma_sach";
    public static final String COLUMN_LOAN_BORROWER_NAME = "ten_nguoi_muon";
    public static final String COLUMN_LOAN_PHONE = "so_dien_thoai";
    public static final String COLUMN_LOAN_DATE_BORROW = "ngay_muon";
    public static final String COLUMN_LOAN_DATE_RETURN = "ngay_tra";
    public static final String COLUMN_LOAN_STATUS = "trang_thai_phieu";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableBooks = "CREATE TABLE " + TABLE_BOOKS + " (" +
                COLUMN_BOOK_ID + " TEXT PRIMARY KEY, " +
                COLUMN_BOOK_TITLE + " TEXT NOT NULL, " +
                COLUMN_BOOK_AUTHOR + " TEXT NOT NULL, " +
                COLUMN_BOOK_CATEGORY + " TEXT NOT NULL, " +
                COLUMN_BOOK_STATUS + " INTEGER DEFAULT 0);";

        String createTableLoans = "CREATE TABLE " + TABLE_LOANS + " (" +
                COLUMN_LOAN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_LOAN_BOOK_ID + " TEXT NOT NULL, " +
                COLUMN_LOAN_BORROWER_NAME + " TEXT NOT NULL, " +
                COLUMN_LOAN_PHONE + " TEXT NOT NULL, " +
                COLUMN_LOAN_DATE_BORROW + " TEXT NOT NULL, " +
                COLUMN_LOAN_DATE_RETURN + " TEXT NOT NULL, " +
                COLUMN_LOAN_STATUS + " INTEGER DEFAULT 0, " +
                "FOREIGN KEY(" + COLUMN_LOAN_BOOK_ID + ") REFERENCES " + TABLE_BOOKS + "(" + COLUMN_BOOK_ID + ") ON DELETE CASCADE);";

        db.execSQL(createTableBooks);
        db.execSQL(createTableLoans);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOANS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BOOKS);
        onCreate(db);
    }

    public boolean insertBook(Book book) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_BOOK_ID, book.getMaSach());
        values.put(COLUMN_BOOK_TITLE, book.getTenSach());
        values.put(COLUMN_BOOK_AUTHOR, book.getTacGia());
        values.put(COLUMN_BOOK_CATEGORY, book.getTheLoai());
        values.put(COLUMN_BOOK_STATUS, book.getTrangThai());

        long result = db.insert(TABLE_BOOKS, null, values);
        return result != -1;
    }

    public boolean updateBook(Book book) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_BOOK_TITLE, book.getTenSach());
        values.put(COLUMN_BOOK_AUTHOR, book.getTacGia());
        values.put(COLUMN_BOOK_CATEGORY, book.getTheLoai());

        int result = db.update(TABLE_BOOKS, values, COLUMN_BOOK_ID + " = ?", new String[]{book.getMaSach()});
        return result > 0;
    }

    public boolean deleteBook(String maSach) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TABLE_BOOKS, COLUMN_BOOK_ID + " = ?", new String[]{maSach});
        return result > 0;
    }

    public boolean checkBookExists(String maSach) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_BOOKS, new String[]{COLUMN_BOOK_ID},
                COLUMN_BOOK_ID + " = ?", new String[]{maSach}, null, null, null);
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        return exists;
    }

    public List<Book> getAllBooks() {
        List<Book> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_BOOKS, null);
        if (cursor.moveToFirst()) {
            do {
                Book book = new Book(
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BOOK_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BOOK_TITLE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BOOK_AUTHOR)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BOOK_CATEGORY)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_BOOK_STATUS))
                );
                list.add(book);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    public List<Book> searchBooks(String query) {
        List<Book> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + TABLE_BOOKS + " WHERE " +
                COLUMN_BOOK_TITLE + " LIKE ? OR " + COLUMN_BOOK_AUTHOR + " LIKE ?";
        String selectionArg = "%" + query + "%";
        Cursor cursor = db.rawQuery(sql, new String[]{selectionArg, selectionArg});
        if (cursor.moveToFirst()) {
            do {
                Book book = new Book(
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BOOK_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BOOK_TITLE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BOOK_AUTHOR)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BOOK_CATEGORY)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_BOOK_STATUS))
                );
                list.add(book);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    public List<Book> getAvailableBooks() {
        List<Book> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_BOOKS, null, COLUMN_BOOK_STATUS + " = 0", null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                Book book = new Book(
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BOOK_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BOOK_TITLE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BOOK_AUTHOR)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BOOK_CATEGORY)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_BOOK_STATUS))
                );
                list.add(book);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    public boolean insertLoan(Loan loan) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(COLUMN_LOAN_BOOK_ID, loan.getMaSach());
            values.put(COLUMN_LOAN_BORROWER_NAME, loan.getTenNguoiMuon());
            values.put(COLUMN_LOAN_PHONE, loan.getSoDienThoai());
            values.put(COLUMN_LOAN_DATE_BORROW, loan.getNgayMuon());
            values.put(COLUMN_LOAN_DATE_RETURN, loan.getNgayTra());
            values.put(COLUMN_LOAN_STATUS, 0);

            long loanId = db.insert(TABLE_LOANS, null, values);
            if (loanId == -1) {
                return false;
            }

            ContentValues bookValues = new ContentValues();
            bookValues.put(COLUMN_BOOK_STATUS, 1);
            int updatedRows = db.update(TABLE_BOOKS, bookValues, COLUMN_BOOK_ID + " = ?", new String[]{loan.getMaSach()});

            if (updatedRows > 0) {
                db.setTransactionSuccessful();
                return true;
            } else {
                return false;
            }
        } finally {
            db.endTransaction();
        }
    }

    public boolean returnLoan(int maPhieu, String maSach) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            ContentValues loanValues = new ContentValues();
            loanValues.put(COLUMN_LOAN_STATUS, 1);
            int updatedLoans = db.update(TABLE_LOANS, loanValues, COLUMN_LOAN_ID + " = ?", new String[]{String.valueOf(maPhieu)});

            ContentValues bookValues = new ContentValues();
            bookValues.put(COLUMN_BOOK_STATUS, 0);
            int updatedBooks = db.update(TABLE_BOOKS, bookValues, COLUMN_BOOK_ID + " = ?", new String[]{maSach});

            if (updatedLoans > 0 && updatedBooks > 0) {
                db.setTransactionSuccessful();
                return true;
            } else {
                return false;
            }
        } finally {
            db.endTransaction();
        }
    }

    public List<Loan> getActiveLoans() {
        List<Loan> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_LOANS, null, COLUMN_LOAN_STATUS + " = 0", null, null, null, COLUMN_LOAN_ID + " DESC");
        if (cursor.moveToFirst()) {
            do {
                Loan loan = new Loan(
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_LOAN_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LOAN_BOOK_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LOAN_BORROWER_NAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LOAN_PHONE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LOAN_DATE_BORROW)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LOAN_DATE_RETURN)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_LOAN_STATUS))
                );
                list.add(loan);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }
}
