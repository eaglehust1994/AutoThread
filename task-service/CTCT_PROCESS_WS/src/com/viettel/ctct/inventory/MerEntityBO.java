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
public class MerEntityBO {

    private java.lang.Long merEntityId;
    private java.lang.String serial;
    private java.lang.Long goodsId;
    private java.lang.String goodsCode;
    private java.lang.String goodsName;
    private java.lang.String state;
    private java.lang.String status;
    private java.lang.Double amount;
    private java.lang.Long catManufacturerId;
    private java.lang.Long catProducingCountryId;
    private java.lang.String stockId;
    private java.lang.Long cntContractId;
    private java.lang.Long sysGroupId;
    private java.lang.Long projectId;
    private java.lang.Long shipmentId;
    private java.lang.String partNumber;
    private java.lang.Double unitPrice;
    private java.lang.Double applyPrice;
    private java.lang.String manufacturerName;
    private java.lang.String producingCountryName;
    private java.lang.String catUnitName;
    private java.lang.Long catUnitId;
    private java.lang.Long orderId;
    private java.lang.String cntContractCode;
    private java.util.Date importDate;
    private java.util.Date updatedDate;
    private java.lang.Long stockCellId;
    private java.lang.String stockCellCode;
    private java.lang.Long parentMerEntityId;
    private java.lang.String shipmentCode;
    private java.lang.Long goodsType;
    private java.lang.Long manufaceturerId;
    private java.lang.Long producingCountryId;
    private java.lang.String projectCode;
    private String importStockTransId;

    public MerEntityBO() {
    }

    public MerEntityBO(Long merEntityId, String serial, Long goodsId, String goodsCode, String goodsName, String state, String status, Double amount, Long catManufacturerId, Long catProducingCountryId, String stockId, Long cntContractId, Long sysGroupId, Long projectId, Long shipmentId, String partNumber, Double unitPrice, Double applyPrice, String manufacturerName, String producingCountryName, String catUnitName, Long catUnitId, Long orderId, String cntContractCode, Date importDate, Date updatedDate, Long stockCellId, String stockCellCode, Long parentMerEntityId, String shipmentCode, Long goodsType, Long manufaceturerId, Long producingCountryId, String projectCode, String importStockTransId) {
        this.merEntityId = merEntityId;
        this.serial = serial;
        this.goodsId = goodsId;
        this.goodsCode = goodsCode;
        this.goodsName = goodsName;
        this.state = state;
        this.status = status;
        this.amount = amount;
        this.catManufacturerId = catManufacturerId;
        this.catProducingCountryId = catProducingCountryId;
        this.stockId = stockId;
        this.cntContractId = cntContractId;
        this.sysGroupId = sysGroupId;
        this.projectId = projectId;
        this.shipmentId = shipmentId;
        this.partNumber = partNumber;
        this.unitPrice = unitPrice;
        this.applyPrice = applyPrice;
        this.manufacturerName = manufacturerName;
        this.producingCountryName = producingCountryName;
        this.catUnitName = catUnitName;
        this.catUnitId = catUnitId;
        this.orderId = orderId;
        this.cntContractCode = cntContractCode;
        this.importDate = importDate;
        this.updatedDate = updatedDate;
        this.stockCellId = stockCellId;
        this.stockCellCode = stockCellCode;
        this.parentMerEntityId = parentMerEntityId;
        this.shipmentCode = shipmentCode;
        this.goodsType = goodsType;
        this.manufaceturerId = manufaceturerId;
        this.producingCountryId = producingCountryId;
        this.projectCode = projectCode;
        this.importStockTransId= importStockTransId;
    }

    public Long getMerEntityId() {
        return merEntityId;
    }

    public void setMerEntityId(Long merEntityId) {
        this.merEntityId = merEntityId;
    }

    public String getImportStockTransId() {
        return importStockTransId;
    }

    public void setImportStockTransId(String importStockTransId) {
        this.importStockTransId = importStockTransId;
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

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Long getCatManufacturerId() {
        return catManufacturerId;
    }

    public void setCatManufacturerId(Long catManufacturerId) {
        this.catManufacturerId = catManufacturerId;
    }

    public Long getCatProducingCountryId() {
        return catProducingCountryId;
    }

    public void setCatProducingCountryId(Long catProducingCountryId) {
        this.catProducingCountryId = catProducingCountryId;
    }

    public String getStockId() {
        return stockId;
    }

    public void setStockId(String stockId) {
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

    public Double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Double getApplyPrice() {
        return applyPrice;
    }

    public void setApplyPrice(Double applyPrice) {
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

    public String getCatUnitName() {
        return catUnitName;
    }

    public void setCatUnitName(String catUnitName) {
        this.catUnitName = catUnitName;
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

    public Long getParentMerEntityId() {
        return parentMerEntityId;
    }

    public void setParentMerEntityId(Long parentMerEntityId) {
        this.parentMerEntityId = parentMerEntityId;
    }

    public String getShipmentCode() {
        return shipmentCode;
    }

    public void setShipmentCode(String shipmentCode) {
        this.shipmentCode = shipmentCode;
    }

    public Long getGoodsType() {
        return goodsType;
    }

    public void setGoodsType(Long goodsType) {
        this.goodsType = goodsType;
    }

    public Long getManufaceturerId() {
        return manufaceturerId;
    }

    public void setManufaceturerId(Long manufaceturerId) {
        this.manufaceturerId = manufaceturerId;
    }

    public Long getProducingCountryId() {
        return producingCountryId;
    }

    public void setProducingCountryId(Long producingCountryId) {
        this.producingCountryId = producingCountryId;
    }

    public String getProjectCode() {
        return projectCode;
    }

    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }

   

}
