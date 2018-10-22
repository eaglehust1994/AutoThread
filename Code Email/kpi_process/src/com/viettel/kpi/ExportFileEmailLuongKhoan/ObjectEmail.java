/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.kpi.ExportFileEmailLuongKhoan;

/**
 *
 * @author sonnh26
 */
public class ObjectEmail {
    
   private String senderEmailCode;
   private String content;
   private String email;
   private String subject;
   private String logServerId;
   private String path;
   private String fileName;
    private Integer isFile;

    public String getSenderEmailCode() {
        return senderEmailCode;
    }

    public void setSenderEmailCode(String senderEmailCode) {
        this.senderEmailCode = senderEmailCode;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getLogServerId() {
        return logServerId;
    }

    public void setLogServerId(String logServerId) {
        this.logServerId = logServerId;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Integer getIsFile() {
        return isFile;
    }

    public void setIsFile(Integer isFile) {
        this.isFile = isFile;
    }
   
   
    
}
