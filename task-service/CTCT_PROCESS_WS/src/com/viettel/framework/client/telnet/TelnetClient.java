/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.framework.client.telnet;

import com.jscape.inet.telnet.DoOptionEvent;
import com.jscape.inet.telnet.DoSubOptionEvent;
import com.jscape.inet.telnet.DontOptionEvent;
import com.jscape.inet.telnet.TelnetConnectedEvent;
import com.jscape.inet.telnet.TelnetDataReceivedEvent;
import com.jscape.inet.telnet.TelnetDisconnectedEvent;
import com.jscape.inet.telnet.TelnetException;
import com.jscape.inet.telnet.TelnetListener;
import com.jscape.inet.telnet.WillOptionEvent;
import com.jscape.inet.telnet.WontOptionEvent;
import com.viettel.framework.service.common.LogServer;
import org.apache.log4j.Logger;

/**
 *
 * @author qlmvt_dungnt32
 */
public class TelnetClient implements TelnetListener {

    private Client telnet;
    private ObjectLogin loginInfo;
    private LogServer logServer;
    private Logger logger;

    public String sendCommand(String cmdString, String endPromt) {
        return telnet.sendWait(cmdString, endPromt);
    }

    public TelnetClient(LogServer logServer, String loginPath, Logger logger) {
        LoginConfig config = new LoginConfig(loginPath);
        this.loginInfo = config.readConfig();
        this.logServer = logServer;
        this.logger = logger;
    }

    public void connect() throws TelnetException {
        try {
            telnet = new Client(logServer.getIp(), logServer.getPort(), logServer.getUsername(), logServer.getPassword(), logger);
            if (loginInfo.getShellPromt().equalsIgnoreCase("?")) {
                telnet.setShellPromt("<");
            } else {
                telnet.setShellPromt(loginInfo.getShellPromt());
            }
            telnet.login(loginInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void disConnect() {
        if (loginInfo.getCmdExit() != null) {
            telnet.disConnect(loginInfo.getCmdExit());
        } else {
            telnet.disConnect(null);
        }

    }

    public void connected(TelnetConnectedEvent tce) {
        System.out.println("connected");
    }

    public void disconnected(TelnetDisconnectedEvent tde) {
    }

    public void doOption(DoOptionEvent doe) {
    }

    public void dontOption(DontOptionEvent doe) {
    }

    public void willOption(WillOptionEvent woe) {
    }

    public void wontOption(WontOptionEvent woe) {
    }

    public void doSubOption(DoSubOptionEvent dsoe) {
    }

    public void dataReceived(TelnetDataReceivedEvent tdre) {
        System.out.print("" + tdre.getData());
    }
}
