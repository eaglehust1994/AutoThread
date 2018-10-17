/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.ctct.inventory;

import java.sql.Date;



/**
 *
 * @author pm1_os38
 */
public class StockDailyImExObject {
    String stockDailyImportExportId;
    Date ieDate;
    String stockTransType;
    String stockId;
    String stockCode;
    String stockName;
    String goodsType;
    String goodsTypeName;
    String goodsId;
    String goodsCode;
    String goodsName;
    String goodsIsSerial;
    String goodsState;
    String goodsStateName;
    String goodsUnitName;
    String stockTransUserName;
    String stockTransDescription;
    String goodsUnitId;
    String amountTotal;
    String totalPrice;
    String totalMoney;

    public String getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(String totalMoney) {
        this.totalMoney = totalMoney;
    }
    Date createDateTime;

    public String getStockDailyImportExportId() {
        return stockDailyImportExportId;
    }

    public void setStockDailyImportExportId(String stockDailyImportExportId) {
        this.stockDailyImportExportId = stockDailyImportExportId;
    }

    public Date getIeDate() {
        return ieDate;
    }

    public void setIeDate(Date ieDate) {
        this.ieDate = ieDate;
    }

    public String getStockTransType() {
        return stockTransType;
    }

    public void setStockTransType(String stockTransType) {
        this.stockTransType = stockTransType;
    }

    public String getStockId() {
        return stockId;
    }

    public void setStockId(String stockId) {
        this.stockId = stockId;
    }

    public String getStockCode() {
        return stockCode;
    }

    public void setStockCode(String stockCode) {
        this.stockCode = stockCode;
    }

    public String getStockName() {
        return stockName;
    }

    public void setStockName(String stockName) {
        this.stockName = stockName;
    }

    public String getGoodsType() {
        return goodsType;
    }

    public void setGoodsType(String goodsType) {
        this.goodsType = goodsType;
    }

    public String getGoodsTypeName() {
        return goodsTypeName;
    }

    public void setGoodsTypeName(String goodsTypeName) {
        this.goodsTypeName = goodsTypeName;
    }

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
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

    public String getGoodsIsSerial() {
        return goodsIsSerial;
    }

    public void setGoodsIsSerial(String goodsIsSerial) {
        this.goodsIsSerial = goodsIsSerial;
    }

    public String getGoodsState() {
        return goodsState;
    }

    public void setGoodsState(String goodsState) {
        this.goodsState = goodsState;
    }

    public String getGoodsStateName() {
        return goodsStateName;
    }

    public void setGoodsStateName(String goodsStateName) {
        this.goodsStateName = goodsStateName;
    }

    public String getGoodsUnitName() {
        return goodsUnitName;
    }

    public void setGoodsUnitName(String goodsUnitName) {
        this.goodsUnitName = goodsUnitName;
    }

    public String getStockTransUserName() {
        return stockTransUserName;
    }

    public void setStockTransUserName(String stockTransUserName) {
        this.stockTransUserName = stockTransUserName;
    }

    public String getStockTransDescription() {
        return stockTransDescription;
    }

    public void setStockTransDescription(String stockTransDescription) {
        this.stockTransDescription = stockTransDescription;
    }

    public String getGoodsUnitId() {
        return goodsUnitId;
    }

    public void setGoodsUnitId(String goodsUnitId) {
        this.goodsUnitId = goodsUnitId;
    }

    public String getAmountTotal() {
        return amountTotal;
    }

    public void setAmountTotal(String amountTotal) {
        this.amountTotal = amountTotal;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Date getCreateDateTime() {
        return createDateTime;
    }

    public void setCreateDateTime(Date createDateTime) {
        this.createDateTime = createDateTime;
    }
}
