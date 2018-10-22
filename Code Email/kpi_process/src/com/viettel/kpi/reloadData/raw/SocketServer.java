package com.viettel.kpi.reloadData.raw;

import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import org.apache.log4j.Logger;

/**
 *
 * @author Khiemvk
 */
public class SocketServer {

    private static final Logger logger = Logger.getLogger(SocketServer.class.getName());
    private static ServerSocket serverSocket;
    public static int port;
    //Sonnh26_new_R3710_04/04/2013_Start
    public static String dbFile;
    //Sonnh26_new_R3710_04/04/2013_End
    public static int numberUserCallMax;// Số lần lớn nhất mà người dùng gọi
    public static long threadCount;
    private static boolean running;
    public static long reloadData_ID = 0;
    public static ThreadPool threadPool;

    public void run() {
        try {
            while (true) {
                try {
                    if (!running) {
                        port = Start.port;
                        dbFile = Start.dbFileCommon;
                        numberUserCallMax = Start.numberUserCallMax;
                        logger.info("Server Socket port: " + port);
                        serverSocket = new ServerSocket(port);
                        Thread socketNode = new Thread(new SocketNode());
                        socketNode.start();
                        running = true;
                    }
                } catch (BindException be) {
                    logger.info(be.getMessage());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Thread.sleep(5000);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void start() {
        new SocketServer().run();
    }

    public static void stop() {
        try {
            setRunning(false);
            serverSocket.close();
        } catch (Exception ex) {
            logger.error("Server Socket stop: " + ex.getMessage());
        }
    }

    class SocketNode implements Runnable {

        public void run() {
            try {
                threadCount = 0;
                while (programIsRunning()) {
                    logger.info("Server Socket running");
                    Socket socket = serverSocket.accept();
                    if (programIsRunning()) {
                        SocketThread thread = new SocketThread(numberUserCallMax);
                        if (thread != null) {
                            logger.info("Call thread proccess...");
                            thread.init(socket);
                            thread.start();
                        }
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public static synchronized boolean programIsRunning() {
        return running;
    }

    public static synchronized void setRunning(boolean status) {
        running = status;
    }

    public static void incThreadCount() {
        threadCount++;
    }
}
