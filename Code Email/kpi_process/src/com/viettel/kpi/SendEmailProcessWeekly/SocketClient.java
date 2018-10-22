package com.viettel.kpi.SendEmailProcessWeekly;

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

    private static String serverIP;
    private static Socket socket;
    private static int port;
    static PrintStream out;
    static BufferedReader in;

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

    public static void creatConnection() throws IOException {
        try {
            out = new PrintStream(socket.getOutputStream());
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException ex) {
            ex.printStackTrace();
            throw ex;
        }
    }

    public static void assureConnection() throws IOException {
        if ((null != out) || (null != in)) {
            creatConnection();
        }
    }

    public static void sendMsg(String msg) throws IOException {
        assureConnection();
        out.println(msg);
        out.flush();
    }

    public static void main(String[] args) throws IOException {

        SocketClient socket = new SocketClient("192.168.208.164", 9997);
        sendMsg("jhjghjd");
        System.out.println("OK");
    }
}
