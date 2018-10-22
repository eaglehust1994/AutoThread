/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.kpi.ExportFileEmailLuongKhoan;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Date;
//import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author qlmvt_dongnd3
 */
public class SocketClient {

    private String serverIP;
    private Socket socket;
    private int port;
    PrintStream out;
    BufferedReader in;

    public SocketClient(String serverIP, int port) throws IOException, Exception {
        this.serverIP = serverIP;
        this.socket = null;
        int count = 0;
        while (null == this.socket && count < 3) {
            try {
                InetAddress address = InetAddress.getByName(serverIP);
                this.socket = new Socket(address, port);
                this.socket.setSoTimeout(300000);// Timeout 5phut
            } catch (Exception ex) {
                count++;
                if(count==2){
                    throw ex;
                }
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

//    public String receiveFile(HttpServletRequest request) throws IOException {
//        assureConnection();
//        Date now = new Date();
//        String fileNamePatch = request.getRealPath("share") + "/temp/truyvandulieu" + now.getTime() + ".zip";
//        String fileName = "truyvandulieu" + now.getTime() + ".zip";
//        byte[] mybytearray = new byte[1024];
//        BufferedInputStream in =
//                new BufferedInputStream(socket.getInputStream());
//        File file = new File(fileNamePatch);
//        FileOutputStream fos = new FileOutputStream(file);
//        BufferedOutputStream bos = new BufferedOutputStream(fos);
////        while(is.read()){
////
////        }
//        // int bytesRead = is.read(mybytearray, 0, mybytearray.length);
//        int len = 0;
//        while ((len = in.read(mybytearray)) > 0) {
//            fos.write(mybytearray, 0, len);
//            System.out.print("#");
//        }
//    //    bos.write(mybytearray, 0, bytesRead);
//        bos.close();
//        return fileName;
//    }
}
