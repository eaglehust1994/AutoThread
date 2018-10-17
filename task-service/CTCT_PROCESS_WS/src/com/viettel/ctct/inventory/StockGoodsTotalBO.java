/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.ctct.inventory;

import java.util.Date;

/**
 *
 * @author kh1_cntt
 */
public class StockGoodsTotalBO {

	private java.lang.Long stockGoodsTotalId;
	private java.lang.Long stockId;
	private java.lang.Long goodsId;
	private java.lang.String goodsState;
	private java.lang.String goodsStateName;
	private java.lang.String goodsCode;
	private java.lang.String goodsName;
	private java.lang.Long goodsType;
	private java.lang.String goodsIsSerial;
	private java.lang.Long goodsUnitId;
	private java.lang.String goodsUnitName;
	private java.lang.Double amountRemain;
        private java.lang.Double amountKpi;
        private java.lang.Double amountQuota;
	private java.util.Date changeDate;
	private java.lang.String goodsTypeName;
	private java.lang.Double amountIssue;
	private java.lang.String stockCode;
	private java.lang.String stockName;

    public Long getStockGoodsTotalId() {
        return stockGoodsTotalId;
    }

    public void setStockGoodsTotalId(Long stockGoodsTotalId) {
        this.stockGoodsTotalId = stockGoodsTotalId;
    }

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

    public Long getGoodsType() {
        return goodsType;
    }

    public void setGoodsType(Long goodsType) {
        this.goodsType = goodsType;
    }

    public String getGoodsIsSerial() {
        return goodsIsSerial;
    }

    public void setGoodsIsSerial(String goodsIsSerial) {
        this.goodsIsSerial = goodsIsSerial;
    }

    public Long getGoodsUnitId() {
        return goodsUnitId;
    }

    public void setGoodsUnitId(Long goodsUnitId) {
        this.goodsUnitId = goodsUnitId;
    }

    public String getGoodsUnitName() {
        return goodsUnitName;
    }

    public void setGoodsUnitName(String goodsUnitName) {
        this.goodsUnitName = goodsUnitName;
    }

    public Double getAmountRemain() {
        return amountRemain;
    }

    public void setAmountRemain(Double amountRemain) {
        this.amountRemain = amountRemain;
    }

    public Double getAmountKpi() {
        return amountKpi;
    }

    public void setAmountKpi(Double amountKpi) {
        this.amountKpi = amountKpi;
    }

    public Double getAmountQuota() {
        return amountQuota;
    }

    public void setAmountQuota(Double amountQuota) {
        this.amountQuota = amountQuota;
    }

    public Date getChangeDate() {
        return changeDate;
    }

    public void setChangeDate(Date changeDate) {
        this.changeDate = changeDate;
    }

    public String getGoodsTypeName() {
        return goodsTypeName;
    }

    public void setGoodsTypeName(String goodsTypeName) {
        this.goodsTypeName = goodsTypeName;
    }

    public Double getAmountIssue() {
        return amountIssue;
    }

    public void setAmountIssue(Double amountIssue) {
        this.amountIssue = amountIssue;
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

    public StockGoodsTotalBO() {
    }

    public StockGoodsTotalBO(Long stockGoodsTotalId, Long stockId, Long goodsId, String goodsState, String goodsStateName, String goodsCode, String goodsName, Long goodsType, String goodsIsSerial, Long goodsUnitId, String goodsUnitName, Double amountRemain, Double amountKpi, Double amountQuota, Date changeDate, String goodsTypeName, Double amountIssue, String stockCode, String stockName) {
        this.stockGoodsTotalId = stockGoodsTotalId;
        this.stockId = stockId;
        this.goodsId = goodsId;
        this.goodsState = goodsState;
        this.goodsStateName = goodsStateName;
        this.goodsCode = goodsCode;
        this.goodsName = goodsName;
        this.goodsType = goodsType;
        this.goodsIsSerial = goodsIsSerial;
        this.goodsUnitId = goodsUnitId;
        this.goodsUnitName = goodsUnitName;
        this.amountRemain = amountRemain;
        this.amountKpi = amountKpi;
        this.amountQuota = amountQuota;
        this.changeDate = changeDate;
        this.goodsTypeName = goodsTypeName;
        this.amountIssue = amountIssue;
        this.stockCode = stockCode;
        this.stockName = stockName;
    }

  
        
}
