package com.example.ktgk.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ktgk.R;
import com.example.ktgk.models.Loan;

import java.util.List;

public class LoanAdapter extends RecyclerView.Adapter<LoanAdapter.LoanViewHolder> {

    public interface OnReturnClickListener {
        void onReturn(Loan loan);
    }

    private Context context;
    private List<Loan> loanList;
    private OnReturnClickListener listener;

    public LoanAdapter(Context context, List<Loan> loanList, OnReturnClickListener listener) {
        this.context = context;
        this.loanList = loanList;
        this.listener = listener;
    }

    public void updateList(List<Loan> newList) {
        this.loanList = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public LoanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_loan, parent, false);
        return new LoanViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LoanViewHolder holder, int position) {
        Loan loan = loanList.get(position);
        holder.tvMaPhieu.setText("Mã phiếu: #" + loan.getMaPhieu());
        holder.tvMaSach.setText("Mã sách: " + loan.getMaSach());
        holder.tvNguoiMuon.setText("Người mượn: " + loan.getTenNguoiMuon());
        holder.tvSdt.setText("SĐT: " + loan.getSoDienThoai());
        holder.tvNgayMuon.setText("Ngày mượn: " + loan.getNgayMuon());
        holder.tvNgayTra.setText("Ngày trả: " + loan.getNgayTra());

        holder.btnTraSach.setOnClickListener(v -> {
            if (listener != null) {
                listener.onReturn(loan);
            }
        });
    }

    @Override
    public int getItemCount() {
        return loanList != null ? loanList.size() : 0;
    }

    public static class LoanViewHolder extends RecyclerView.ViewHolder {
        TextView tvMaPhieu, tvMaSach, tvNguoiMuon, tvSdt, tvNgayMuon, tvNgayTra;
        Button btnTraSach;

        public LoanViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMaPhieu = itemView.findViewById(R.id.tvMaPhieu);
            tvMaSach = itemView.findViewById(R.id.tvMaSach);
            tvNguoiMuon = itemView.findViewById(R.id.tvNguoiMuon);
            tvSdt = itemView.findViewById(R.id.tvSdt);
            tvNgayMuon = itemView.findViewById(R.id.tvNgayMuon);
            tvNgayTra = itemView.findViewById(R.id.tvNgayTra);
            btnTraSach = itemView.findViewById(R.id.btnTraSach);
        }
    }
}
