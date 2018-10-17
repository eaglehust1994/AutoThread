/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.ctct.quantityByDate;

//import com.viettel.coms.bo.ConstructionTaskBO;
//import com.viettel.erp.utils.JsonDateDeserializer;
//import com.viettel.erp.utils.JsonDateSerializerDate;
//import com.viettel.service.base.dto.BaseFWDTOImpl;
//import com.viettel.service.base.utils.StringUtils;

import javax.xml.bind.annotation.XmlRootElement;

//import org.codehaus.jackson.annotate.JsonIgnoreProperties;
//import org.codehaus.jackson.map.annotate.JsonDeserialize;
//import org.codehaus.jackson.map.annotate.JsonSerialize;

/**
 *
 * @author thuannht
 */
@XmlRootElement(name = "CONSTRUCTION_TASKBO")
//@JsonIgnoreProperties(ignoreUnknown = true)
public class ConstructionTaskDTO {

private java.lang.Double completePercent;
private java.lang.String description;
private java.lang.String status;
private java.lang.String sourceType;
private java.lang.String deployType;
private java.lang.Double vat;
private java.lang.String type;
private java.lang.Long detailMonthPlanId;
private java.util.Date createdDate;
private java.lang.Long createdUserId;
private java.lang.Long createdGroupId;
private java.util.Date updatedDate;
private java.lang.Long updatedUserId;
private java.lang.Long updatedGroupId;
private java.lang.String completeState;
private java.lang.Long performerWorkItemId;
private java.lang.Double supervisorId;
private java.lang.Double directorId;
private java.lang.Long performerId;
private java.lang.Double quantity;
private java.lang.Long constructionTaskId;
private java.lang.Long sysGroupId;
private java.lang.Long month;
private java.lang.Long year;
private java.lang.String taskName;
private java.lang.String reasonStop;
private java.lang.String constructionCode;
private java.lang.String workItemName;
private java.lang.String constructionName;
private java.lang.String performerName;
private java.lang.String quantityByDate;
//hoanm1_20180628_start
private java.lang.Double quantityRevenue;
//phucvx_28/06
private Double amount;
private Double price;
public Double getAmount() {
	return amount;
}
public void setAmount(Double amount) {
	this.amount = amount;
}

public Double getPrice() {
	return price;
}
public void setPrice(Double price) {
	this.price = price;
}
//phuc_end
public java.lang.Double getQuantityRevenue() {
	return quantityRevenue;
}
public void setQuantityRevenue(java.lang.Double quantityRevenue) {
	this.quantityRevenue = quantityRevenue;
}
//hoanm1_20180628_end
//chinhpxn20180621
private String taskOrder;

public String getTaskOrder() {
	return taskOrder;
}
public void setTaskOrder(String taskOrder) {
	this.taskOrder = taskOrder;
}
//chinhpxn20180621

public java.lang.String getConstructionCode() {
	return constructionCode;
}

public void setConstructionCode(java.lang.String constructionCode) {
	this.constructionCode = constructionCode;
}

public java.lang.String getWorkItemName() {
	return workItemName;
}

public void setWorkItemName(java.lang.String workItemName) {
	this.workItemName = workItemName;
}

//@JsonSerialize(using = JsonDateSerializerDate.class)
//@JsonDeserialize(using = JsonDateDeserializer.class)
private java.util.Date startDate;
//@JsonSerialize(using = JsonDateSerializerDate.class)
//@JsonDeserialize(using = JsonDateDeserializer.class)
private java.util.Date endDate;
private java.util.Date baselineStartDate;
private java.util.Date baselineEndDate;
private java.lang.Long constructionId;
private java.lang.Long workItemId;
private java.lang.Long catTaskId;
private java.lang.Long levelId;
private java.lang.Long parentId;
private java.lang.String path;

    public java.lang.Double getCompletePercent(){
    return completePercent;
    }
    public void setCompletePercent(java.lang.Double completePercent)
    {
    this.completePercent = completePercent;
    }
    
    public java.lang.String getDescription(){
    return description;
    }
    public void setDescription(java.lang.String description)
    {
    this.description = description;
    }
    
    public java.lang.String getStatus(){
    return status;
    }
    public void setStatus(java.lang.String status)
    {
    this.status = status;
    }
    
    public java.lang.String getSourceType(){
    return sourceType;
    }
    public void setSourceType(java.lang.String sourceType)
    {
    this.sourceType = sourceType;
    }
    
    public java.lang.String getDeployType(){
    return deployType;
    }
    public void setDeployType(java.lang.String deployType)
    {
    this.deployType = deployType;
    }
    
    public java.lang.String getType(){
    return type;
    }
    public void setType(java.lang.String type)
    {
    this.type = type;
    }
    
    public java.lang.Double getVat(){
    return vat!=null?vat/1000000:0;
    }
    public void setVat(java.lang.Double vat)
    {
    this.vat = vat;
    }
    
    public java.lang.Long getDetailMonthPlanId(){
    return detailMonthPlanId;
    }
    public void setDetailMonthPlanId(java.lang.Long detailMonthPlanId)
    {
    this.detailMonthPlanId = detailMonthPlanId;
    }
    
    public java.util.Date getCreatedDate(){
    return createdDate;
    }
    public void setCreatedDate(java.util.Date createdDate)
    {
    this.createdDate = createdDate;
    }
    
    public java.lang.Long getCreatedUserId(){
    return createdUserId;
    }
    public void setCreatedUserId(java.lang.Long createdUserId)
    {
    this.createdUserId = createdUserId;
    }
    
    public java.lang.Long getCreatedGroupId(){
    return createdGroupId;
    }
    public void setCreatedGroupId(java.lang.Long createdGroupId)
    {
    this.createdGroupId = createdGroupId;
    }
    
    public java.util.Date getUpdatedDate(){
    return updatedDate;
    }
    public void setUpdatedDate(java.util.Date updatedDate)
    {
    this.updatedDate = updatedDate;
    }
    
    public java.lang.Long getUpdatedUserId(){
    return updatedUserId;
    }
    public void setUpdatedUserId(java.lang.Long updatedUserId)
    {
    this.updatedUserId = updatedUserId;
    }
    
    public java.lang.Long getUpdatedGroupId(){
    return updatedGroupId;
    }
    public void setUpdatedGroupId(java.lang.Long updatedGroupId)
    {
    this.updatedGroupId = updatedGroupId;
    }
    
    public java.lang.String getCompleteState(){
    return completeState;
    }
    public void setCompleteState(java.lang.String completeState)
    {
    this.completeState = completeState;
    }
    
    public java.lang.Long getPerformerWorkItemId(){
    return performerWorkItemId;
    }
    public void setPerformerWorkItemId(java.lang.Long performerWorkItemId)
    {
    this.performerWorkItemId = performerWorkItemId;
    }
    
    public java.lang.Double getSupervisorId(){
    return supervisorId;
    }
    public void setSupervisorId(java.lang.Double supervisorId)
    {
    this.supervisorId = supervisorId;
    }
    
    public java.lang.Double getDirectorId(){
    return directorId;
    }
    public void setDirectorId(java.lang.Double directorId)
    {
    this.directorId = directorId;
    }
    
    public java.lang.Long getPerformerId(){
    return performerId;
    }
    public void setPerformerId(java.lang.Long performerId)
    {
    this.performerId = performerId;
    }
    
    
    public java.lang.Double getQuantity(){
    return quantity!=null?quantity/1000000:0;
    }
    
    public void setQuantity(java.lang.Double quantity)
    {
    this.quantity = quantity;
    }
    
    public java.lang.Long getConstructionTaskId(){
    return constructionTaskId;
    }
    public void setConstructionTaskId(java.lang.Long constructionTaskId)
    {
    this.constructionTaskId = constructionTaskId;
    }
    
    public java.lang.Long getSysGroupId(){
    return sysGroupId;
    }
    public void setSysGroupId(java.lang.Long sysGroupId)
    {
    this.sysGroupId = sysGroupId;
    }
    
    public java.lang.Long getMonth(){
    return month;
    }
    public void setMonth(java.lang.Long month)
    {
    this.month = month;
    }
    
    public java.lang.Long getYear(){
    return year;
    }
    public void setYear(java.lang.Long year)
    {
    this.year = year;
    }
    
    public java.lang.String getTaskName(){
    return taskName;
    }
    public void setTaskName(java.lang.String taskName)
    {
    this.taskName = taskName;
    }
    
    public java.util.Date getStartDate(){
    return startDate;
    }
    public void setStartDate(java.util.Date startDate)
    {
    this.startDate = startDate;
    }
    
    public java.util.Date getEndDate(){
    return endDate;
    }
    public void setEndDate(java.util.Date endDate)
    {
    this.endDate = endDate;
    }
    
    public java.util.Date getBaselineStartDate(){
    return baselineStartDate;
    }
    public void setBaselineStartDate(java.util.Date baselineStartDate)
    {
    this.baselineStartDate = baselineStartDate;
    }
    
    public java.util.Date getBaselineEndDate(){
    return baselineEndDate;
    }
    public void setBaselineEndDate(java.util.Date baselineEndDate)
    {
    this.baselineEndDate = baselineEndDate;
    }
    
    public java.lang.Long getConstructionId(){
    return constructionId;
    }
    public void setConstructionId(java.lang.Long constructionId)
    {
    this.constructionId = constructionId;
    }
    
    public java.lang.Long getWorkItemId(){
    return workItemId;
    }
    public void setWorkItemId(java.lang.Long workItemId)
    {
    this.workItemId = workItemId;
    }
    
    public java.lang.Long getCatTaskId(){
    return catTaskId;
    }
    public void setCatTaskId(java.lang.Long catTaskId)
    {
    this.catTaskId = catTaskId;
    }

	public java.lang.Long getLevelId() {
		return levelId;
	}

	public void setLevelId(java.lang.Long levelId) {
		this.levelId = levelId;
	}

	public java.lang.Long getParentId() {
		return parentId;
	}

	public void setParentId(java.lang.Long parentId) {
		this.parentId = parentId;
	}

	public java.lang.String getPath() {
		return path;
	}

	public void setPath(java.lang.String path) {
		this.path = path;
	}
	
	public java.lang.String getReasonStop() {
		return reasonStop;
	}

	public void setReasonStop(java.lang.String reasonStop) {
		this.reasonStop = reasonStop;
	}

	public java.lang.String getConstructionName() {
		return constructionName;
	}

	public void setConstructionName(java.lang.String constructionName) {
		this.constructionName = constructionName;
	}

	public java.lang.String getPerformerName() {
		return performerName;
	}

	public void setPerformerName(java.lang.String performerName) {
		this.performerName = performerName;
	}
	public java.lang.String getQuantityByDate() {
		return quantityByDate;
	}
	public void setQuantityByDate(java.lang.String quantityByDate) {
		this.quantityByDate = quantityByDate;
	}
	
    
   
}
