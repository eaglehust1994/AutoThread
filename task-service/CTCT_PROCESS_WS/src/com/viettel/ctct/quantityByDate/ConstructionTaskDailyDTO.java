/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.ctct.quantityByDate;

import java.util.Date;

/**
 *
 * @author hungnx
 */
public class ConstructionTaskDailyDTO {
    private java.lang.Long constructionTaskDailyId;
	private java.lang.Long sysGroupId;
	private java.lang.Double amount;
	private java.lang.String type;
	private java.lang.String confirm;
	private java.lang.Long createdUserId;
	private java.lang.Long createdGroupId;
//	@JsonDeserialize(using = CustomJsonDateDeserializer.class)
//	@JsonSerialize(using = CustomJsonDateSerializer.class)
	private java.util.Date updatedDate;
	private java.lang.Long updatedUserId;
	private java.lang.Long updatedGroupId;
	private java.lang.Long constructionTaskId;
	private String workItemName;
	private Long workItemId;
    // hungnx 20180627 start
//	@JsonDeserialize(using = CustomJsonDateDeserializer.class)
//	@JsonSerialize(using = CustomJsonDateSerializer.class)
	private java.util.Date createdDate;
	private Double quantity;
//	@JsonDeserialize(using = CustomJsonDateDeserializer.class)
//	@JsonSerialize(using = CustomJsonDateSerializer.class)
	private Date approveDate;
	private Long approveUserId;
	private Double price;
	private String constructionTypeName;
	private Long constructionId;
	private String statusConstructionTask;
	private Double amountConstruction;
        private Long catTaskId;
        private String path;
	// hungnx 20180627 end

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public Date getApproveDate() {
        return approveDate;
    }

    public void setApproveDate(Date approveDate) {
        this.approveDate = approveDate;
    }

    public Long getApproveUserId() {
        return approveUserId;
    }

    public void setApproveUserId(Long approveUserId) {
        this.approveUserId = approveUserId;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getConstructionTypeName() {
        return constructionTypeName;
    }

    public void setConstructionTypeName(String constructionTypeName) {
        this.constructionTypeName = constructionTypeName;
    }

    public Long getConstructionId() {
        return constructionId;
    }

    public void setConstructionId(Long constructionId) {
        this.constructionId = constructionId;
    }

    public String getStatusConstructionTask() {
        return statusConstructionTask;
    }

    public void setStatusConstructionTask(String statusConstructionTask) {
        this.statusConstructionTask = statusConstructionTask;
    }

    public Double getAmountConstruction() {
        return amountConstruction;
    }

    public void setAmountConstruction(Double amountConstruction) {
        this.amountConstruction = amountConstruction;
    }

    public Long getConstructionTaskDailyId() {
        return constructionTaskDailyId;
    }

    public void setConstructionTaskDailyId(Long constructionTaskDailyId) {
        this.constructionTaskDailyId = constructionTaskDailyId;
    }

    public Long getSysGroupId() {
        return sysGroupId;
    }

    public void setSysGroupId(Long sysGroupId) {
        this.sysGroupId = sysGroupId;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getConfirm() {
        return confirm;
    }

    public void setConfirm(String confirm) {
        this.confirm = confirm;
    }

    public Long getCreatedUserId() {
        return createdUserId;
    }

    public void setCreatedUserId(Long createdUserId) {
        this.createdUserId = createdUserId;
    }

    public Long getCreatedGroupId() {
        return createdGroupId;
    }

    public void setCreatedGroupId(Long createdGroupId) {
        this.createdGroupId = createdGroupId;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    public Long getUpdatedUserId() {
        return updatedUserId;
    }

    public void setUpdatedUserId(Long updatedUserId) {
        this.updatedUserId = updatedUserId;
    }

    public Long getUpdatedGroupId() {
        return updatedGroupId;
    }

    public void setUpdatedGroupId(Long updatedGroupId) {
        this.updatedGroupId = updatedGroupId;
    }

    public Long getConstructionTaskId() {
        return constructionTaskId;
    }

    public void setConstructionTaskId(Long constructionTaskId) {
        this.constructionTaskId = constructionTaskId;
    }

    public String getWorkItemName() {
        return workItemName;
    }

    public void setWorkItemName(String workItemName) {
        this.workItemName = workItemName;
    }

    public Long getWorkItemId() {
        return workItemId;
    }

    public void setWorkItemId(Long workItemId) {
        this.workItemId = workItemId;
    }

    public Long getCatTaskId() {
        return catTaskId;
    }

    public void setCatTaskId(Long catTaskId) {
        this.catTaskId = catTaskId;
    }    

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
    
}
