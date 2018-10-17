/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.qll.task;





/**
 *
 * @author kh1_cntt
 */
public class TaskBO {
    
    private Long taskId;
    private java.lang.Long idTaskGroup;
    private java.lang.Long status;
    private java.util.Date endTime;
    private java.util.Date startTime;
    private java.lang.Long createTaskCycle;
    private java.lang.String noteTask;

    /**
     * @return the taskId
     */
    public Long getTaskId() {
        return taskId;
    }

    /**
     * @param taskId the taskId to set
     */
    public void setTaskId(Long taskId) {
        this.taskId = taskId;
       
    }

    /**
     * @return the idTaskGroup
     */
    public java.lang.Long getIdTaskGroup() {
        return idTaskGroup;
    }

    /**
     * @param idTaskGroup the idTaskGroup to set
     */
    public void setIdTaskGroup(java.lang.Long idTaskGroup) {    
        this.idTaskGroup = idTaskGroup;
      
    }

    /**
     * @return the status
     */
    public java.lang.Long getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(java.lang.Long status) {   
        this.status = status;
      
    }

    /**
     * @return the endTime
     */
    public java.util.Date getEndTime() {
        return endTime;
    }

    /**
     * @param endTime the endTime to set
     */
    public void setEndTime(java.util.Date endTime) { 
        this.endTime = endTime;
      
    }

    /**
     * @return the startTime
     */
    public java.util.Date getStartTime() {
        return startTime;
    }

    /**
     * @param startTime the startTime to set
     */
    public void setStartTime(java.util.Date startTime) {  
        this.startTime = startTime;
        
    }

    /**
     * @return the createTaskCycle
     */
    public java.lang.Long getCreateTaskCycle() {
        return createTaskCycle;
    }

    /**
     * @param createTaskCycle the createTaskCycle to set
     */
    public void setCreateTaskCycle(java.lang.Long createTaskCycle) {      
        this.createTaskCycle = createTaskCycle;
        
    }

    /**
     * @return the noteTask
     */
    public java.lang.String getNoteTask() {
        return noteTask;
    }

    /**
     * @param noteTask the noteTask to set
     */
    public void setNoteTask(java.lang.String noteTask) {
        this.noteTask = noteTask;
        
    }
   
    
    
}
