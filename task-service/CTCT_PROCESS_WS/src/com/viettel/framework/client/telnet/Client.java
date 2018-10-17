package com.viettel.framework.client.telnet;

/*
 * @(#)TelnetExample.java
 *
 * Copyright (c) JSCAPE LLC.
 *
 * This software is the confidential and proprietary information of
 * JSCAPE. ("Confidential Information").  You shall not disclose such
 * Confidential Information and shall use it only in accordance with
 * the terms of the license agreement you entered into with JSCAPE.
 */
import com.jscape.inet.telnet.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.log4j.Logger;

public class Client extends TelnetAdapter {

    private Telnet telnet = null;
    private OutputStream output = null;
    private boolean connected = false;
    private StringBuilder str = new StringBuilder();
    private boolean failed = false;
    private String user;
    private String pass;
    private Logger logger;
    private String shellPromt;
    private String commandTerminator;

    public boolean isConnected() {
        return connected;
    }

    public void setConnected(boolean connected) {
        this.connected = connected;
    }

    public String getShellPromt() {
        return shellPromt;
    }

    public void setShellPromt(String shellPromt) {
        this.shellPromt = shellPromt;
    }

    public String getCommandTerminator() {
        return commandTerminator;
    }

    public void setCommandTerminator(String commandTerminator) {
        this.commandTerminator = commandTerminator;
    }

    public Client(String hostname, int port, String user, String pass, Logger logger) throws IOException, TelnetException, InterruptedException {

        String input = null;

        this.logger = logger;

        // create new Telnet instance
        telnet = new Telnet(hostname, port);

        // register this class as TelnetListener
        telnet.addTelnetListener(this);

        // establish Telnet connection
        telnet.connect();


        // get output stream
        output = telnet.getOutputStream();

        this.user = user;

        this.pass = pass;


    }

    public void disConnect(String cmdExit) {
        connected = false;
        if (cmdExit != null) {
            sendWait(cmdExit);
        }
        output = null;
        str = null;
        telnet.disconnect();
        telnet = null;
    }

    public boolean login(ObjectLogin objectLogin) {

        String response = login(objectLogin.getPromtList());
        if (objectLogin.getMsgLoginSuccess() != null) {
            if (response.contains(objectLogin.getMsgLoginSuccess())) {
                logger.info("login sucess!");
                return true;
            } else {
                logger.info("login faile!");
                return false;
            }
        } else {
            logger.info("login sucess!");
            return true;
        }
    }

    public String login(List<Promt> listPromt) {
        String reSult = null;
        if (listPromt.size() == 1) {
            String loginString = listPromt.get(0).getValue();
            loginString = loginString.replace("username", user);
            loginString = loginString.replace("password", pass);
            reSult = sendWait(loginString, null);
        } else if (listPromt.size() == 2) {
            String tmp = waitFor(listPromt.get(0).getValue());
            String tmp1 = sendWait("Test", listPromt.get(1).getValue());
            reSult = sendWait("123456", ">");
        } else {
            for (int i = 0; i < listPromt.size(); i++) {
                String tmp = waitFor(listPromt.get(i).getValue());
                if (i + 1 >= listPromt.size()) {
                    reSult = sendWait(listPromt.get(i).getResponse(), null);
                } else {
                    if (listPromt.get(i).getResponse() != null && listPromt.get(i).getResponse().equalsIgnoreCase("username")) {
                        listPromt.get(i).setResponse(user);
                    }
                    if (listPromt.get(i).getResponse() != null && listPromt.get(i).getResponse().equalsIgnoreCase("password")) {
                        listPromt.get(i).setResponse(pass);
                    }
                    sendWait(listPromt.get(i).getResponse(), listPromt.get(i + 1).getValue());
                }
            }
        }
        return reSult;
    }

    /** Invoked when Telnet socked is connected.
     * @see com.jscape.inet.telnet.TelnetConnectedEvent
     * @see com.jscape.inet.telnet.Telnet#connect
     */
    public void connected(TelnetConnectedEvent event) {
        System.out.println("Connected");
    }

    /**
     * Invoked when Telnet socket is disconnected. Disconnect can
     * occur in many circumstances including IOException during socket read/write.
     * @see com.jscape.inet.telnet.TelnetDisconnectedEvent
     * @see com.jscape.inet.telnet.Telnet#disconnect
     */
    public void disconnected(TelnetDisconnectedEvent event) {
        connected = false;
        System.out.print("Disconnected.  Press enter key to quit.");
    }

    /**
     * Invoked when Telnet server requests that the Telnet client begin performing specified <code>TelnetOption</code>.
     * @param event a <code>DoOptionEvent</code>
     * @see com.jscape.inet.telnet.DoOptionEvent
     * @see com.jscape.inet.telnet.TelnetOption
     */
    public void doOption(DoOptionEvent event) {
        // refuse any options requested by Telnet server
        telnet.sendWontOption(event.getOption());
    }

    /**
     * Invoked when Telnet server offers to begin performing specified <code>TelnetOption</code>.
     * @param event a <code>WillOptionEvent</code>
     * @see com.jscape.inet.telnet.WillOptionEvent
     * @see com.jscape.inet.telnet.TelnetOption
     */
    public void willOption(WillOptionEvent event) {
        // refuse any options offered by Telnet server
        telnet.sendDontOption(event.getOption());
    }

    /**
     * Invoked when data is received from Telnet server.
     * @param event a <code>TelnetDataReceivedEvent</code>
     * @see com.jscape.inet.telnet.TelnetDataReceivedEvent
     */
    public void dataReceived(TelnetDataReceivedEvent event) {
        // print data recevied from Telnet server to console
        try {
//            System.out.print(event.getData());
            str.append(event.getData());

        } catch (Exception e) {
        }



    }

    public void sendCmd(String cmd) {

        try {
            str = new StringBuilder();
            if (cmd == null) {
                cmd = "";
            }
            System.out.println("Send: " + cmd);
            ((TelnetOutputStream) output).println(cmd + "\r\n");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public String sendWait(String cmd) {

        return sendWait(cmd, null);

    }

    public String sendWait(String cmd, String endPromt) {

        if (failed) {
            return "FAILED";
        }
        if (endPromt == null) {
            endPromt = shellPromt;
        }
        sendCmd(cmd);
        return waitFor(endPromt);

    }

    public String waitFor(String endPromt) {
        int lifetime = 0;
        while (!checkEndCmd(endPromt)) {
            try {
//                System.out.println("\n\n  CHECK WAIT FOR " + lifetime);
                if (lifetime == 11) {
                    failed = true;
                    break;
                }
                Thread.sleep(2000);
                lifetime++;
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
        return str.toString();
    }

    public boolean checkEndCmd(String endPromt) {

        String strs = str.toString();
//                System.out.println("checkEndCmd STR " + strs);
        Pattern pattern = Pattern.compile(endPromt);

        Matcher match = pattern.matcher(strs);

        if (match.find()) {
            return true;
        }

        return false;

    }
}
