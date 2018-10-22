package com.viettel.kpi.reloadData.raw;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Socket;


/**
 *
 * @author KhiemVK
 */
public class SocketClient {

    private String serverIP;
    private Socket socket;
    private int port;
    PrintStream out;
    BufferedReader in;

    public SocketClient(String serverIP, int port) throws IOException {
        this.serverIP = serverIP;
        this.socket = null;
        while (null == this.socket) {
            try {
                InetAddress address = InetAddress.getByName(serverIP);
                this.socket = new Socket(address, port);
            } catch (Exception ex) {
                System.out.println("Create socket error: " + ex.getMessage());
            }
        }
        this.port = port;
        creatConnection();
    }

    public void creatConnection() throws IOException {
        try {
            out = new PrintStream(socket.getOutputStream());
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException ex) {
            ex.printStackTrace();
            throw ex;
        }
    }

    public void assureConnection() throws IOException {
        if ((null != out) || (null != in)) {
            creatConnection();
        }
    }

    public void sendMsg(String msg) throws IOException {
        assureConnection();
        out.println(msg);
        out.flush();      
    }
}
