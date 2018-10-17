/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.framework.client.telnet;

import java.util.List;

/**
 *
 * @author qlmvt_dungnt32
 */
public class ObjectLogin {

    private List<Promt> PromtList;
    private String shellPromt;
    private String commandTerminator;
    private String msgLoginSuccess;
    private String cmdExit;

    public ObjectLogin() {
    }

    public List<Promt> getPromtList() {
        return PromtList;
    }

    public void setPromtList(List<Promt> fromtList) {
        this.PromtList = fromtList;
    }

    public String getCommandTerminator() {
        return commandTerminator;
    }

    public void setCommandTerminator(String commandTerminator) {
        this.commandTerminator = commandTerminator;
    }

    public String getShellPromt() {
        return shellPromt;
    }

    public void setShellPromt(String shellPromt) {
        this.shellPromt = shellPromt;
    }

    public String getMsgLoginSuccess() {
        return msgLoginSuccess;
    }

    public void setMsgLoginSuccess(String msgLoginSuccess) {
        this.msgLoginSuccess = msgLoginSuccess;
    }

    public String getCmdExit() {
        return cmdExit;
    }

    public void setCmdExit(String cmdExit) {
        this.cmdExit = cmdExit;
    }
}
