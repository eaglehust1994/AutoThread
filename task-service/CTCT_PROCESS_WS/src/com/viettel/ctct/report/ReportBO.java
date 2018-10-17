/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.ctct.report;

import java.util.Date;

/**
 *
 * @author hoangnh38
 */
public class ReportBO {
    Date reportDate;
    Long stockId;
    Long goodsId;
    String goodsCode;
    String goodsName;
    String stockName;
    String goodsState;
    String goodsUnitName;
    String amount;
    String amountTotalImport;
    String amountTotalExport;
    String amountFinal;
    String totalMoney;
    String moneyTotalIm;
    String moneyTotalEx;
    String moneyFinal;
    String type;
    Date createdDate;

    public Long getStockId() {
        return stockId;
    }

    public void setStockId(Long stockId) {
        this.stockId = stockId;
    }

    public Long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getAmountTotalImport() {
        return amountTotalImport;
    }

    public void setAmountTotalImport(String amountTotalImport) {
        this.amountTotalImport = amountTotalImport;
    }

    public String getAmountTotalExport() {
        return amountTotalExport;
    }

    public void setAmountTotalExport(String amountTotalExport) {
        this.amountTotalExport = amountTotalExport;
    }

    public String getAmountFinal() {
        return amountFinal;
    }

    public void setAmountFinal(String amountFinal) {
        this.amountFinal = amountFinal;
    }

    public String getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(String totalMoney) {
        this.totalMoney = totalMoney;
    }

    public String getMoneyTotalIm() {
        return moneyTotalIm;
    }

    public void setMoneyTotalIm(String moneyTotalIm) {
        this.moneyTotalIm = moneyTotalIm;
    }

    public String getMoneyTotalEx() {
        return moneyTotalEx;
    }

    public void setMoneyTotalEx(String moneyTotalEx) {
        this.moneyTotalEx = moneyTotalEx;
    }

    public String getMoneyFinal() {
        return moneyFinal;
    }

    public void setMoneyFinal(String moneyFinal) {
        this.moneyFinal = moneyFinal;
    }
    
    
    public Date getReportDate() {
        return reportDate;
    }

    public void setReportDate(Date reportDate) {
        this.reportDate = reportDate;
    }


    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public String getStockName() {
        return stockName;
    }

    public void setStockName(String stockName) {
        this.stockName = stockName;
    }

    public String getGoodsState() {
        return goodsState;
    }

    public void setGoodsState(String goodsState) {
        this.goodsState = goodsState;
    }

    public String getGoodsUnitName() {
        return goodsUnitName;
    }

    public void setGoodsUnitName(String goodsUnitName) {
        this.goodsUnitName = goodsUnitName;
    }

    public String getGoodsCode() {
        return goodsCode;
    }

    public void setGoodsCode(String goodsCode) {
        this.goodsCode = goodsCode;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    
    
}
