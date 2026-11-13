package com.example.ktgk.models;

import java.io.Serializable;

public class Loan implements Serializable {
    private int maPhieu;
    private String maSach;
    private String tenNguoiMuon;
    private String soDienThoai;
    private String ngayMuon;
    private String ngayTra;
    private int trangThaiPhieu;

    public Loan() {
    }

    public Loan(int maPhieu, String maSach, String tenNguoiMuon, String soDienThoai, String ngayMuon, String ngayTra, int trangThaiPhieu) {
        this.maPhieu = maPhieu;
        this.maSach = maSach;
        this.tenNguoiMuon = tenNguoiMuon;
        this.soDienThoai = soDienThoai;
        this.ngayMuon = ngayMuon;
        this.ngayTra = ngayTra;
        this.trangThaiPhieu = trangThaiPhieu;
    }

    public Loan(String maSach, String tenNguoiMuon, String soDienThoai, String ngayMuon, String ngayTra, int trangThaiPhieu) {
        this.maSach = maSach;
        this.tenNguoiMuon = tenNguoiMuon;
        this.soDienThoai = soDienThoai;
        this.ngayMuon = ngayMuon;
        this.ngayTra = ngayTra;
        this.trangThaiPhieu = trangThaiPhieu;
    }

    public int getMaPhieu() {
        return maPhieu;
    }

    public void setMaPhieu(int maPhieu) {
        this.maPhieu = maPhieu;
    }

    public String getMaSach() {
        return maSach;
    }

    public void setMaSach(String maSach) {
        this.maSach = maSach;
    }

    public String getTenNguoiMuon() {
        return tenNguoiMuon;
    }

    public void setTenNguoiMuon(String tenNguoiMuon) {
        this.tenNguoiMuon = tenNguoiMuon;
    }

    public String getSoDienThoai() {
        return soDienThoai;
    }

    public void setSoDienThoai(String soDienThoai) {
        this.soDienThoai = soDienThoai;
    }

    public String getNgayMuon() {
        return ngayMuon;
    }

    public void setNgayMuon(String ngayMuon) {
        this.ngayMuon = ngayMuon;
    }

    public String getNgayTra() {
        return ngayTra;
    }

    public void setNgayTra(String ngayTra) {
        this.ngayTra = ngayTra;
    }

    public int getTrangThaiPhieu() {
        return trangThaiPhieu;
    }

    public void setTrangThaiPhieu(int trangThaiPhieu) {
        this.trangThaiPhieu = trangThaiPhieu;
    }
}
