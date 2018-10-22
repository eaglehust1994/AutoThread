package com.viettel.kpi.SendEmailProcessMonthly;

import com.viettel.kpi.common.utils.ThreadPoolMonthly;

import java.io.IOException;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;

import org.apache.log4j.Logger;

/**
 * @author Khiemvk
 */
public class SocketServer {

    private static final Logger logger = Logger.getLogger(SocketServer.class.getName());
    private static ServerSocket serverSocket;
    public static int port;
    public static int threadMax;
    public static long threadCount;
    public static ThreadPoolMonthly threadPool;
    private static boolean running;
    public static long reloadData_ID = 0;

    public void run() {
        try {
            while (true) {
                try {
                    if (!running) {
                        port = ProcessManager.port;
                        logger.info("Port hien thoi la " + port);
                        threadMax = ProcessManager.threadMax;
                        logger.info("Server Socket port: " + port);
                        threadPool = new ThreadPoolMonthly(threadMax);
                        serverSocket = new ServerSocket(port);
                        Thread socketNode = new Thread(new SocketNode());
                        socketNode.start();
                        running = true;
                    }
                } catch (BindException be) {
                    logger.warn(be.getMessage());
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
            threadPool.stop();
            serverSocket.close();
        } catch (Exception ex) {
            logger.error("Server Socket stop: " + ex.getMessage());
        }
    }

    class SocketNode implements Runnable {

        @Override
        public void run() {
            try {
                threadCount = 0;
                while (programIsRunning()) {
                    logger.info("Server Socket running");
                    Socket socket = serverSocket.accept();
                    if (programIsRunning()) {
                        SocketThread thread = threadPool.getThread();
                        if (thread != null) {
                            logger.info("Call thread proccess...");
                            thread.init(socket);
                            thread.start();
                        }
                    }
                }
            } catch (Exception ex) {
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
