package com.example.ktgk.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ktgk.R;
import com.example.ktgk.models.Book;

import java.util.List;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookViewHolder> {

    public interface OnBookActionListener {
        void onEdit(Book book);
        void onDelete(Book book);
    }

    private Context context;
    private List<Book> bookList;
    private OnBookActionListener listener;

    public BookAdapter(Context context, List<Book> bookList, OnBookActionListener listener) {
        this.context = context;
        this.bookList = bookList;
        this.listener = listener;
    }

    public void updateList(List<Book> newList) {
        this.bookList = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_book, parent, false);
        return new BookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
        Book book = bookList.get(position);
        holder.tvMaSach.setText("Mã sách: " + book.getMaSach());
        holder.tvTenSach.setText(book.getTenSach());
        holder.tvTacGia.setText("Tác giả: " + book.getTacGia());
        holder.tvTheLoai.setText("Thể loại: " + book.getTheLoai());

        if (book.getTrangThai() == 0) {
            holder.tvTrangThai.setText("Có sẵn");
            holder.tvTrangThai.setTextColor(Color.parseColor("#2E7D32"));
        } else {
            holder.tvTrangThai.setText("Đang được mượn");
            holder.tvTrangThai.setTextColor(Color.parseColor("#C62828"));
        }

        holder.btnEdit.setOnClickListener(v -> {
            if (listener != null) {
                listener.onEdit(book);
            }
        });

        holder.btnDelete.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDelete(book);
            }
        });
    }

    @Override
    public int getItemCount() {
        return bookList != null ? bookList.size() : 0;
    }

    public static class BookViewHolder extends RecyclerView.ViewHolder {
        TextView tvMaSach, tvTenSach, tvTacGia, tvTheLoai, tvTrangThai;
        ImageButton btnEdit, btnDelete;

        public BookViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMaSach = itemView.findViewById(R.id.tvMaSach);
            tvTenSach = itemView.findViewById(R.id.tvTenSach);
            tvTacGia = itemView.findViewById(R.id.tvTacGia);
            tvTheLoai = itemView.findViewById(R.id.tvTheLoai);
            tvTrangThai = itemView.findViewById(R.id.tvTrangThai);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
