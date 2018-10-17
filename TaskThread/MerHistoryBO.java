/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.ctct.merHistory;

import java.util.Date;

/**
 *
 * @author hoangnh38
 */
public class MerHistoryBO {
    private java.lang.Long merHistoryId;
    private java.lang.Long merEntityId;
    private java.lang.String serial;
    private java.lang.Long goodsId;
    private java.lang.String goodsCode;
    private java.lang.String goodsName;
    private java.lang.String state;
    private java.lang.String status;
    private java.lang.String amount;
    private java.lang.Long catManufacturerId;
    private java.lang.Long stockId;
    private java.lang.Long cntContractId;
    private java.lang.Long sysGroupId;
    private java.lang.Long projectId;
    private java.lang.Long shipmentId;
    private java.lang.String partNumber;
    private java.lang.String unitPrice;
    private java.lang.String applyPrice;
    private java.lang.String manufacturerName;
    private java.lang.String producingCountryName;
    private java.lang.Long catUnitId;
    private java.lang.Long orderId;
    private java.lang.String cntContractCode;
    private Date importDate;
    private Date updatedDate;
    private java.lang.Long stockCellId;
    private java.lang.String stockCellCode;
    private java.lang.Long catProducingCountryId;
    private java.lang.Long parentMerEntityId;
    private java.lang.String catUnitName;
    private Date exportDate;
    private java.lang.Long importStockTransId;
    private Date createdDate;

    public Long getMerHistoryId() {
        return merHistoryId;
    }

    public void setMerHistoryId(Long merHistoryId) {
        this.merHistoryId = merHistoryId;
    }

    public Long getMerEntityId() {
        return merEntityId;
    }

    public void setMerEntityId(Long merEntityId) {
        this.merEntityId = merEntityId;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public Long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Long goodsId) {
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

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public Long getCatManufacturerId() {
        return catManufacturerId;
    }

    public void setCatManufacturerId(Long catManufacturerId) {
        this.catManufacturerId = catManufacturerId;
    }

    public Long getStockId() {
        return stockId;
    }

    public void setStockId(Long stockId) {
        this.stockId = stockId;
    }


    public Long getCntContractId() {
        return cntContractId;
    }

    public void setCntContractId(Long cntContractId) {
        this.cntContractId = cntContractId;
    }

    public Long getSysGroupId() {
        return sysGroupId;
    }

    public void setSysGroupId(Long sysGroupId) {
        this.sysGroupId = sysGroupId;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public Long getShipmentId() {
        return shipmentId;
    }

    public void setShipmentId(Long shipmentId) {
        this.shipmentId = shipmentId;
    }

    public String getPartNumber() {
        return partNumber;
    }

    public void setPartNumber(String partNumber) {
        this.partNumber = partNumber;
    }

    public String getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(String unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getApplyPrice() {
        return applyPrice;
    }

    public void setApplyPrice(String applyPrice) {
        this.applyPrice = applyPrice;
    }

    public String getManufacturerName() {
        return manufacturerName;
    }

    public void setManufacturerName(String manufacturerName) {
        this.manufacturerName = manufacturerName;
    }

    public String getProducingCountryName() {
        return producingCountryName;
    }

    public void setProducingCountryName(String producingCountryName) {
        this.producingCountryName = producingCountryName;
    }

    public Long getCatUnitId() {
        return catUnitId;
    }

    public void setCatUnitId(Long catUnitId) {
        this.catUnitId = catUnitId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getCntContractCode() {
        return cntContractCode;
    }

    public void setCntContractCode(String cntContractCode) {
        this.cntContractCode = cntContractCode;
    }

    public Date getImportDate() {
        return importDate;
    }

    public void setImportDate(Date importDate) {
        this.importDate = importDate;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    public Long getStockCellId() {
        return stockCellId;
    }

    public void setStockCellId(Long stockCellId) {
        this.stockCellId = stockCellId;
    }

    public String getStockCellCode() {
        return stockCellCode;
    }

    public void setStockCellCode(String stockCellCode) {
        this.stockCellCode = stockCellCode;
    }

    public Long getCatProducingCountryId() {
        return catProducingCountryId;
    }

    public void setCatProducingCountryId(Long catProducingCountryId) {
        this.catProducingCountryId = catProducingCountryId;
    }

    public Long getParentMerEntityId() {
        return parentMerEntityId;
    }

    public void setParentMerEntityId(Long parentMerEntityId) {
        this.parentMerEntityId = parentMerEntityId;
    }

    public String getCatUnitName() {
        return catUnitName;
    }

    public void setCatUnitName(String catUnitName) {
        this.catUnitName = catUnitName;
    }

    public Date getExportDate() {
        return exportDate;
    }

    public void setExportDate(Date exportDate) {
        this.exportDate = exportDate;
    }

    public Long getImportStockTransId() {
        return importStockTransId;
    }

    public void setImportStockTransId(Long importStockTransId) {
        this.importStockTransId = importStockTransId;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }
   
    
}
