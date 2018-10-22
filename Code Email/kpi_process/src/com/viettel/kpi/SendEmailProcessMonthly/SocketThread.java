package com.viettel.kpi.SendEmailProcessMonthly;

import com.viettel.mmserver.base.ProcessThreadMX;
import com.viettel.passprotector.PassProtector;
import com.viettel.kpi.common.utils.DbTask;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Khiemvk
 */
public class SocketThread extends ProcessThreadMX {

    private DbTask db;
    private Socket socket;
    // BufferedReader in;
    private ObjectInputStream in;
    private String message;
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    private List<MscServer> lstMsc = new ArrayList<MscServer>();

    public SocketThread(int threadCount) {
        super("SOCKET_THREAD_" + threadCount);

    }

    public void init(Socket socket) {
        this.socket = socket;
    }

    @Override
    protected void prepareStart() {
        super.prepareStart();
        try {
            in = new ObjectInputStream(socket.getInputStream());
        } catch (Exception ex) {
            logger.error("prepare Start: " + ex.getMessage());
        }
    }

    @Override
    public void process() {
        logger.info("\r\n\r\n***** Begin Process *****");
        String senderEmailCode = "";
        try {
            // strCommand = in.readLine();
            setMessage((String) getIn().readObject());
            senderEmailCode = getMessage();

            logger.info("Incoming Message from " + socket.getInetAddress() + " :" + senderEmailCode);
            if ((senderEmailCode != null) && (senderEmailCode.length() > 0)) {
                //Lenh: Module|OSS1;OSS2;..|Date(dd/MM/yyyy)
                //Giai ma
                senderEmailCode = PassProtector.decrypt(senderEmailCode, "KPI");
                //PassTranformer.decrypt(strCommand);
                logger.info("strCommand: " + senderEmailCode);

                //Khoi tao cac thread
                List<Thread> lstThread = new ArrayList<Thread>();

                ProccessThreadService job = new ProccessThreadService(senderEmailCode);
                Thread threadJob = new Thread(job);
                threadJob.setName(senderEmailCode);
                threadJob.start();
                Thread.sleep(5000);
                lstThread.add(threadJob);

                //Comment Code :
                //Comment Code :Ket thuc phan test
                //Check thread ket thuc
                //logger.info("Begin check thread job stop");
                while (true) {
                    boolean bFinish = true;
                    for (Thread thread : lstThread) {
                        if (thread.getState() != Thread.State.TERMINATED) {
                            bFinish = false;
                        }
                    }

                    if (bFinish) {
                        break;
                    }
                    Thread.sleep(10000);
                }

                for (Thread thread : lstThread) {
                    try {
//                            thread.stop();
                    } catch (Exception ex) {
                    }
                }

                logger.info("All thread job stop");

            }
        } catch (Exception ex) {
            logger.error("process: " + ex.getMessage());
        } finally {
            releaseConnection();
            this.stop();
        }
    }

    @Override
    protected void prepareStop() {
        super.prepareStop();

        try {
            if (in != null) {
                in.close();
            }
            if ((socket != null) && (!socket.isClosed())) {
                socket.close();
            }
        } catch (IOException ex) {
            logger.error("prepareStop:" + ex.getMessage());
        }
        logger.info("releaseThread");
        SocketServer.threadPool.releaseThread(this);
    }

    public void releaseConnection() {
        try {
            if (db != null) {
                db.close();
                db = null;
            }
        } catch (Exception ex) {
        }
    }

    public String getMessage() {
        return message;
    }

    /**
     * @param message the message to set
     */
    public void setMessage(String message) {
        this.message = message;
    }

    public ObjectInputStream getIn() {
        return in;
    }

    /**
     * @param in the in to set
     */
    public void setIn(ObjectInputStream in) {
        this.in = in;
    }
}
