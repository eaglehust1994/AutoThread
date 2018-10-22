package com.viettel.kpi.reloadData.raw;

import com.viettel.kpi.common.utils.DateTimeUtils;
import com.viettel.kpi.getDataSql.common.CommonMapQueryLogServer;
import com.viettel.kpi.getDataSql.config.GetDataFtpJob;
import com.viettel.kpi.getDataSql.config.GetDataJob;
import com.viettel.mmserver.base.ProcessThreadMX;
import com.viettel.security.PassTranformer;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 *
 * @author Khiemvk
 */
public class SocketThread extends ProcessThreadMX {

    private Socket socket;
    private String dbFile;
    private String dbcomon;
    BufferedReader in;
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    public static final String ENCRYPT = "@salt";

    public SocketThread(int threadCount) {

        super("SOCKET_THREAD_" + threadCount);
        this.dbcomon = Start.dbFileCommon;
    }

    public void init(Socket socket) {
        this.socket = socket;
    }

    @Override
    protected void prepareStart() {
        super.prepareStart();
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (Exception ex) {
            logger.error("prepare Start: " + ex.getMessage());
        }
    }

    @Override
    public void process() {
        logger.info("\r\n\r\n***** Begin Process *****");
        try {
            ParserString pares = new ParserString();
            String strCommand = null;
            String time = "";
            MyDbtask db = new MyDbtask();
            db.init(dbcomon);
            strCommand = in.readLine();
            logger.info("Incoming Message from " + socket.getInetAddress() + " :" + strCommand);
            logger.info("" + strCommand);
            if ((strCommand != null) && (strCommand.length() > 0)) {
                //Lenh: Module|OSS1;OSS2;..|Date(dd/MM/yyyy)

                //Giai ma
                logger.info("Bat dau ham giai ma");
                strCommand = PassTranformer.decrypt(strCommand);
                // strCommand = PassProtector.decrypt(strCommand, "@salt");
                logger.info("Ket thuc ham giai ma");
                logger.info("Chuoi web tra ve:" + strCommand);
                strCommand = strCommand.replace(ENCRYPT, "").trim();
                // phan tich chuoi tra ve de gọi lop hop ly.
                if (strCommand.contains(":")) {
                    time = strCommand.substring(0, strCommand.indexOf(":")).trim();
                }
                Date dTime = DateTimeUtils.convertStringToDateStandard(time);
                logger.info("Time:" + time);
                Date dEnTime = DateTimeUtils.convertStringToDateStandard(time);
                dEnTime.setDate(dEnTime.getDate() + 1);

                String endtime = DateTimeUtils.convertDateToString(dEnTime);
                Date DendTime = DateTimeUtils.convertStringToDateStandard(endtime);
                DendTime.setHours(0);
                DendTime.setSeconds(0);
                DendTime.setMinutes(0);


                Map<Long, List<Long>> lstQueryLog = pares.LstMap(strCommand);

                // Lay danh sach trong bang COMMON_MAP_QUERY_LOG_SERVER.

                for (Entry<Long, List<Long>> lstMap : lstQueryLog.entrySet()) {

                    Long logServerId = lstMap.getKey();
                    List<Long> lstQuery = lstMap.getValue();
                    for (Long lst : lstQuery) {

                        logger.info("***Bắt đầu lấy query list****");
                        logger.info("logServerId:" + logServerId);
                        logger.info("QueryId:" + lst);
                        List<CommonMapQueryLogServer> lstqueryLogServers =
                                db.getQueryList(logServerId, lst);
                        logger.info("***lstqueryLogServers.size :="
                                + lstqueryLogServers.size());
                        for (CommonMapQueryLogServer queryLogServer : lstqueryLogServers) {
                            int module_id = queryLogServer.getModuleId();
                            logger.info("module_id:" + module_id);
                            if (module_id == -2) {
                                dbFile = Start.dbFileAccess2G;
                                logger.info("Module 2G");
                            } else if (module_id == -3) {
                                dbFile = Start.dbFileAccess3G;
                                logger.info("Module 3G");
                            } else if (module_id == -4) {
                                dbFile = Start.dbFileVasIn;
                                logger.info("Module VASIN");
                            } else if (module_id == -6) {
                                dbFile = Start.dbFileRoaming;
                                logger.info("Module Roaming");
                            } else if (module_id == -7) {
                                dbFile = Start.dbFileIsp;
                                logger.info("Module ISP");
                            } else if (module_id == -8) {
                                dbFile = Start.dbFileTrans;
                                logger.info("Module Transmission");
                            } else if (module_id == -9) {
                                dbFile = Start.dbFileInoc;
                                logger.info("Module Inoc");
                            } else if (module_id == -1) {
//                                dbFile = Start.dbFileNss;
//                                logger.info("Module NSS");
                                dbFile = Start.dbFileCommon;
                                logger.info("Module KPI");
                            }
                            logger.info("dbFile:" + dbFile);
                            db.close();
                            while (true) {
                                logger.info("Used socket :" + Start.threadSizeInUsed + "/" + Start.threadMax);
                                if (Start.threadSizeInUsed < Start.threadMax) {
                                    if (queryLogServer.getType() != null) {
                                        if (queryLogServer.getType().equals("1")) {
                                            logger.info("startTime:" + new java.sql.Timestamp(dTime.getTime()));
                                            logger.info("endTime:" + new java.sql.Timestamp(DendTime.getTime()));
                                            GetDataJob getDataJob =
                                                    new GetDataJob(queryLogServer, dbFile, dbcomon, new java.sql.Timestamp(dTime.getTime()),
                                                    new java.sql.Timestamp(DendTime.getTime()), logger, Start.maxBatchSize);
                                            getDataJob.run();
                                            Start.increaseThreadSizeUsed();
                                            break;
                                        } else if (queryLogServer.getType().equals("2")) {
                                            logger.info("startTime:" + new java.sql.Timestamp(dTime.getTime()));
                                            logger.info("endTime:" + new java.sql.Timestamp(DendTime.getTime()));
                                            GetDataFtpJob getDataFtpJob =
                                                    new GetDataFtpJob(queryLogServer,
                                                    dbFile, dbcomon, new java.sql.Timestamp(dTime.getTime()),
                                                    new java.sql.Timestamp(DendTime.getTime()), logger);
                                            logger.info("" + getDataFtpJob);
                                            getDataFtpJob.run();

                                            Start.increaseThreadSizeUsed();
                                            break;
                                        }
                                    }
                                } else {
                                    logger.info("Sleep:" + 10 * 1000 + "s");
                                    Thread.sleep(10 * 1000);
                                }
                            }
                        }
                    }
                }

            }
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("process: " + ex.getMessage());
        } finally {
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
        // SocketServer.threadPool.releaseThread(this);
    }
}
