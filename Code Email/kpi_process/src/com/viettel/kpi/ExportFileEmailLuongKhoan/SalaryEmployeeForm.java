/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.kpi.ExportFileEmailLuongKhoan;

import java.sql.Clob;

/**
 *
 * @author sonnh26
 */
public class SalaryEmployeeForm {

    private String salary_employee_id;
    private String staff_code;
    private String full_name;
    private String email;
    private String contract_type;
    private String unit_name;
    private String HSCD;
    private String KNL;
    private String HSQMTT;
    private String tham_nien;
    private String cong_che_do;
    private String cong_tinh_luong;
    private String KI;

    private String luong_kcq_ttvhkt;
    private String luong_giantiep_tinhhuyen;
    private String luong_nha_tram;
    private String luong_day_may;
    private String luong_lai_xe;
    private String luong_co_dong;
    private String luong_hotro_NAN;
    private String luong_hotro_KHA;
    private String luong_thaisan_chohuu;
    private String luong_hotro_thitruong;
    private String luong_them_2212;
    private String luong_them_tet;

    private String luong_thu_lao_quyettoan;
    private String bao_hiem_quyettoan;
    private String thue_TNCN_quyettoan;
    private String an_ca_quyettoan;
    private String dien_thoai_quyettoan;
    private String xang_xe_quyettoan;
    private String dia_ban_quyettoan;
    private String thi_truong_quyettoan;
    private String doitruong_totruong_quyettoan;
    private String phu_cap_khac_quyettoan;
    private String thucnhan_quyettoan;
    private String luong_bao_hiem_tamung;
    private String bao_hiem_tamung;
    private String an_ca_tamung;
    private String dien_thoai_tamung;
    private String xang_xe_tamung;
    private String dia_ban_tamung;
    private String thi_truong_tamung;
    private String phu_cap_khac_tamung;
    private String khoan_nsld;
    private String bao_hiem_connhan;
    private String thue_TNCN_connhan;
    private String an_ca_connhan;
    private String dien_thoai_connhan;
    private String xang_xe_connhan;
    private String dia_ban_connhan;
    private String thi_truong_connhan;
    private String doitruong_totruong_connhan;
    private String phu_cap_khac_connhan;
    private String con_nhan;
    private String ghichu;

    private String thue_ke_khai;
    private String thue_truy_thu;
    private String doi_tuong;

    private String tien_bep_an;
    private String dpcd;
    private String chuyen_khoan;

    private String so_tai_khoan;
    private String nganhang;
    private String chi_nhanh_nganhang;
    private String loai_nganhang;
    private String phu_cap_khac_qt;
    private String phu_cap_khac_tu;

    public String getThi_truong_connhan() {
        return thi_truong_connhan;
    }

    public void setThi_truong_connhan(String thi_truong_connhan) {
        this.thi_truong_connhan = thi_truong_connhan;
    }

    public String getSalary_employee_id() {
        return salary_employee_id;
    }

    public void setSalary_employee_id(String salary_employee_id) {
        this.salary_employee_id = salary_employee_id;
    }

    public String getStaff_code() {
        return staff_code;
    }

    public void setStaff_code(String staff_code) {
        this.staff_code = staff_code;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContract_type() {
        return contract_type;
    }

    public void setContract_type(String contract_type) {
        this.contract_type = contract_type;
    }

    public String getUnit_name() {
        return unit_name;
    }

    public void setUnit_name(String unit_name) {
        this.unit_name = unit_name;
    }

    public String getHSCD() {
        return HSCD;
    }

    public void setHSCD(String HSCD) {
        this.HSCD = HSCD;
    }

    public String getKNL() {
        return KNL;
    }

    public void setKNL(String KNL) {
        this.KNL = KNL;
    }

    public String getHSQMTT() {
        return HSQMTT;
    }

    public void setHSQMTT(String HSQMTT) {
        this.HSQMTT = HSQMTT;
    }

    public String getTham_nien() {
        return tham_nien;
    }

    public void setTham_nien(String tham_nien) {
        this.tham_nien = tham_nien;
    }

    public String getCong_che_do() {
        return cong_che_do;
    }

    public void setCong_che_do(String cong_che_do) {
        this.cong_che_do = cong_che_do;
    }

    public String getCong_tinh_luong() {
        return cong_tinh_luong;
    }

    public void setCong_tinh_luong(String cong_tinh_luong) {
        this.cong_tinh_luong = cong_tinh_luong;
    }

    public String getKI() {
        return KI;
    }

    public void setKI(String KI) {
        this.KI = KI;
    }

    public String getLuong_thu_lao_quyettoan() {
        return luong_thu_lao_quyettoan;
    }

    public void setLuong_thu_lao_quyettoan(String luong_thu_lao_quyettoan) {
        this.luong_thu_lao_quyettoan = luong_thu_lao_quyettoan;
    }

    public String getBao_hiem_quyettoan() {
        return bao_hiem_quyettoan;
    }

    public void setBao_hiem_quyettoan(String bao_hiem_quyettoan) {
        this.bao_hiem_quyettoan = bao_hiem_quyettoan;
    }

    public String getThue_TNCN_quyettoan() {
        return thue_TNCN_quyettoan;
    }

    public void setThue_TNCN_quyettoan(String thue_TNCN_quyettoan) {
        this.thue_TNCN_quyettoan = thue_TNCN_quyettoan;
    }

    public String getAn_ca_quyettoan() {
        return an_ca_quyettoan;
    }

    public void setAn_ca_quyettoan(String an_ca_quyettoan) {
        this.an_ca_quyettoan = an_ca_quyettoan;
    }

    public String getDien_thoai_quyettoan() {
        return dien_thoai_quyettoan;
    }

    public void setDien_thoai_quyettoan(String dien_thoai_quyettoan) {
        this.dien_thoai_quyettoan = dien_thoai_quyettoan;
    }

    public String getXang_xe_quyettoan() {
        return xang_xe_quyettoan;
    }

    public void setXang_xe_quyettoan(String xang_xe_quyettoan) {
        this.xang_xe_quyettoan = xang_xe_quyettoan;
    }

    public String getDia_ban_quyettoan() {
        return dia_ban_quyettoan;
    }

    public void setDia_ban_quyettoan(String dia_ban_quyettoan) {
        this.dia_ban_quyettoan = dia_ban_quyettoan;
    }

    public String getThi_truong_quyettoan() {
        return thi_truong_quyettoan;
    }

    public void setThi_truong_quyettoan(String thi_truong_quyettoan) {
        this.thi_truong_quyettoan = thi_truong_quyettoan;
    }

    public String getDoitruong_totruong_quyettoan() {
        return doitruong_totruong_quyettoan;
    }

    public void setDoitruong_totruong_quyettoan(String doitruong_totruong_quyettoan) {
        this.doitruong_totruong_quyettoan = doitruong_totruong_quyettoan;
    }

    public String getPhu_cap_khac_quyettoan() {
        return phu_cap_khac_quyettoan;
    }

    public void setPhu_cap_khac_quyettoan(String phu_cap_khac_quyettoan) {
        this.phu_cap_khac_quyettoan = phu_cap_khac_quyettoan;
    }

    public String getLuong_bao_hiem_tamung() {
        return luong_bao_hiem_tamung;
    }

    public void setLuong_bao_hiem_tamung(String luong_bao_hiem_tamung) {
        this.luong_bao_hiem_tamung = luong_bao_hiem_tamung;
    }

    public String getBao_hiem_tamung() {
        return bao_hiem_tamung;
    }

    public void setBao_hiem_tamung(String bao_hiem_tamung) {
        this.bao_hiem_tamung = bao_hiem_tamung;
    }

    public String getAn_ca_tamung() {
        return an_ca_tamung;
    }

    public void setAn_ca_tamung(String an_ca_tamung) {
        this.an_ca_tamung = an_ca_tamung;
    }

    public String getDien_thoai_tamung() {
        return dien_thoai_tamung;
    }

    public void setDien_thoai_tamung(String dien_thoai_tamung) {
        this.dien_thoai_tamung = dien_thoai_tamung;
    }

    public String getXang_xe_tamung() {
        return xang_xe_tamung;
    }

    public void setXang_xe_tamung(String xang_xe_tamung) {
        this.xang_xe_tamung = xang_xe_tamung;
    }

    public String getDia_ban_tamung() {
        return dia_ban_tamung;
    }

    public void setDia_ban_tamung(String dia_ban_tamung) {
        this.dia_ban_tamung = dia_ban_tamung;
    }

    public String getThi_truong_tamung() {
        return thi_truong_tamung;
    }

    public void setThi_truong_tamung(String thi_truong_tamung) {
        this.thi_truong_tamung = thi_truong_tamung;
    }

    public String getPhu_cap_khac_tamung() {
        return phu_cap_khac_tamung;
    }

    public void setPhu_cap_khac_tamung(String phu_cap_khac_tamung) {
        this.phu_cap_khac_tamung = phu_cap_khac_tamung;
    }

    public String getKhoan_nsld() {
        return khoan_nsld;
    }

    public void setKhoan_nsld(String khoan_nsld) {
        this.khoan_nsld = khoan_nsld;
    }

    public String getBao_hiem_connhan() {
        return bao_hiem_connhan;
    }

    public void setBao_hiem_connhan(String bao_hiem_connhan) {
        this.bao_hiem_connhan = bao_hiem_connhan;
    }

    public String getThue_TNCN_connhan() {
        return thue_TNCN_connhan;
    }

    public void setThue_TNCN_connhan(String thue_TNCN_connhan) {
        this.thue_TNCN_connhan = thue_TNCN_connhan;
    }

    public String getAn_ca_connhan() {
        return an_ca_connhan;
    }

    public void setAn_ca_connhan(String an_ca_connhan) {
        this.an_ca_connhan = an_ca_connhan;
    }

    public String getDien_thoai_connhan() {
        return dien_thoai_connhan;
    }

    public void setDien_thoai_connhan(String dien_thoai_connhan) {
        this.dien_thoai_connhan = dien_thoai_connhan;
    }

    public String getXang_xe_connhan() {
        return xang_xe_connhan;
    }

    public void setXang_xe_connhan(String xang_xe_connhan) {
        this.xang_xe_connhan = xang_xe_connhan;
    }

    public String getDia_ban_connhan() {
        return dia_ban_connhan;
    }

    public void setDia_ban_connhan(String dia_ban_connhan) {
        this.dia_ban_connhan = dia_ban_connhan;
    }

    public String getDoitruong_totruong_connhan() {
        return doitruong_totruong_connhan;
    }

    public void setDoitruong_totruong_connhan(String doitruong_totruong_connhan) {
        this.doitruong_totruong_connhan = doitruong_totruong_connhan;
    }

    public String getPhu_cap_khac_connhan() {
        return phu_cap_khac_connhan;
    }

    public void setPhu_cap_khac_connhan(String phu_cap_khac_connhan) {
        this.phu_cap_khac_connhan = phu_cap_khac_connhan;
    }

    public String getCon_nhan() {
        return con_nhan;
    }

    public void setCon_nhan(String con_nhan) {
        this.con_nhan = con_nhan;
    }

    public String getGhichu() {
        return ghichu;
    }

    public void setGhichu(String ghichu) {
        this.ghichu = ghichu;
    }

    public String getTien_bep_an() {
        return tien_bep_an;
    }

    public void setTien_bep_an(String tien_bep_an) {
        this.tien_bep_an = tien_bep_an;
    }

    public String getDpcd() {
        return dpcd;
    }

    public void setDpcd(String dpcd) {
        this.dpcd = dpcd;
    }

    public String getChuyen_khoan() {
        return chuyen_khoan;
    }

    public void setChuyen_khoan(String chuyen_khoan) {
        this.chuyen_khoan = chuyen_khoan;
    }

    public String getLuong_kcq_ttvhkt() {
        return luong_kcq_ttvhkt;
    }

    public void setLuong_kcq_ttvhkt(String luong_kcq_ttvhkt) {
        this.luong_kcq_ttvhkt = luong_kcq_ttvhkt;
    }

    public String getLuong_giantiep_tinhhuyen() {
        return luong_giantiep_tinhhuyen;
    }

    public void setLuong_giantiep_tinhhuyen(String luong_giantiep_tinhhuyen) {
        this.luong_giantiep_tinhhuyen = luong_giantiep_tinhhuyen;
    }

    public String getLuong_nha_tram() {
        return luong_nha_tram;
    }

    public void setLuong_nha_tram(String luong_nha_tram) {
        this.luong_nha_tram = luong_nha_tram;
    }

    public String getLuong_day_may() {
        return luong_day_may;
    }

    public void setLuong_day_may(String luong_day_may) {
        this.luong_day_may = luong_day_may;
    }

    public String getLuong_lai_xe() {
        return luong_lai_xe;
    }

    public void setLuong_lai_xe(String luong_lai_xe) {
        this.luong_lai_xe = luong_lai_xe;
    }

    public String getLuong_co_dong() {
        return luong_co_dong;
    }

    public void setLuong_co_dong(String luong_co_dong) {
        this.luong_co_dong = luong_co_dong;
    }

    public String getLuong_hotro_NAN() {
        return luong_hotro_NAN;
    }

    public void setLuong_hotro_NAN(String luong_hotro_NAN) {
        this.luong_hotro_NAN = luong_hotro_NAN;
    }

    public String getLuong_hotro_KHA() {
        return luong_hotro_KHA;
    }

    public void setLuong_hotro_KHA(String luong_hotro_KHA) {
        this.luong_hotro_KHA = luong_hotro_KHA;
    }

    public String getLuong_thaisan_chohuu() {
        return luong_thaisan_chohuu;
    }

    public void setLuong_thaisan_chohuu(String luong_thaisan_chohuu) {
        this.luong_thaisan_chohuu = luong_thaisan_chohuu;
    }

    public String getLuong_hotro_thitruong() {
        return luong_hotro_thitruong;
    }

    public void setLuong_hotro_thitruong(String luong_hotro_thitruong) {
        this.luong_hotro_thitruong = luong_hotro_thitruong;
    }

    public String getLuong_them_2212() {
        return luong_them_2212;
    }

    public void setLuong_them_2212(String luong_them_2212) {
        this.luong_them_2212 = luong_them_2212;
    }

    public String getLuong_them_tet() {
        return luong_them_tet;
    }

    public void setLuong_them_tet(String luong_them_tet) {
        this.luong_them_tet = luong_them_tet;
    }

    public String getThucnhan_quyettoan() {
        return thucnhan_quyettoan;
    }

    public void setThucnhan_quyettoan(String thucnhan_quyettoan) {
        this.thucnhan_quyettoan = thucnhan_quyettoan;
    }

    public String getThue_ke_khai() {
        return thue_ke_khai;
    }

    public void setThue_ke_khai(String thue_ke_khai) {
        this.thue_ke_khai = thue_ke_khai;
    }

    public String getThue_truy_thu() {
        return thue_truy_thu;
    }

    public void setThue_truy_thu(String thue_truy_thu) {
        this.thue_truy_thu = thue_truy_thu;
    }

    public String getDoi_tuong() {
        return doi_tuong;
    }

    public void setDoi_tuong(String doi_tuong) {
        this.doi_tuong = doi_tuong;
    }

    public String getSo_tai_khoan() {
        return so_tai_khoan;
    }

    public void setSo_tai_khoan(String so_tai_khoan) {
        this.so_tai_khoan = so_tai_khoan;
    }

    public String getNganhang() {
        return nganhang;
    }

    public void setNganhang(String nganhang) {
        this.nganhang = nganhang;
    }

    public String getChi_nhanh_nganhang() {
        return chi_nhanh_nganhang;
    }

    public void setChi_nhanh_nganhang(String chi_nhanh_nganhang) {
        this.chi_nhanh_nganhang = chi_nhanh_nganhang;
    }

    public String getLoai_nganhang() {
        return loai_nganhang;
    }

    public void setLoai_nganhang(String loai_nganhang) {
        this.loai_nganhang = loai_nganhang;
    }

    public String getPhu_cap_khac_qt() {
        return phu_cap_khac_qt;
    }

    public void setPhu_cap_khac_qt(String phu_cap_khac_qt) {
        this.phu_cap_khac_qt = phu_cap_khac_qt;
    }

    public String getPhu_cap_khac_tu() {
        return phu_cap_khac_tu;
    }

    public void setPhu_cap_khac_tu(String phu_cap_khac_tu) {
        this.phu_cap_khac_tu = phu_cap_khac_tu;
    }

}
