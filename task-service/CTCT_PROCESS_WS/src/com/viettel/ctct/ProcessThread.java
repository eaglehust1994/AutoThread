/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.ctct;

import com.viettel.framework.service.utils.DateTimeUtils;
import com.viettel.mmserver.base.ProcessThreadMX;
import com.viettel.qldtktts.webservice.CatMerchandiseBO;
import com.viettel.qldtktts.webservice.CatStationBO;
import com.viettel.qldtktts.webservice.CatStationHouseBO;
import com.viettel.qldtktts.webservice.CntContractBO;
import com.viettel.qldtktts.webservice.ConstrConstructionsBO;
import com.viettel.qldtktts.webservice.KTTSSynchronizedQLCTWS;
import com.viettel.qldtktts.webservice.KTTSSynchronizedQLCTWS_Service;
import com.viettel.qldtktts.webservice.Kttsbo;
import com.viettel.qldtktts.webservice.LogicticsSyncWS;
import com.viettel.qldtktts.webservice.LogicticsSyncWS_Service;
import com.viettel.qldtktts.webservice.MerInExpNoteDTO;
import com.viettel.qldtktts.webservice.ProductCompanyBO;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.namespace.QName;
import java.sql.Connection;

//  @author hoanm1
public class ProcessThread extends ProcessThreadMX {

    MyDbTask db;
    private long interval;
    private long maxBatchSize;
    private boolean useFixedHour;
    private long minuteRun;
    private long hour;
    String urlStr = "";
    int port;
    String namespace = "";
    String localPart = "";
    String username = "";
    String password = "";
    String database = "";

    /**
     *
     * @throws Exception
     */
    public ProcessThread(LogServerObject objWs, long interval, long maxBatchSize, long hour, long minute) throws Exception {
        super("Synchronous from pmth_ktts to pmxl_ctct");
        this.interval = interval;
        this.maxBatchSize = maxBatchSize;
        urlStr = objWs.getUrl();
        namespace = objWs.getNamespace();
        localPart = objWs.getLocalPart();
        username = objWs.getUsername();
        password = objWs.getPassword();
        this.hour = hour;
        this.database = ProcessManager.database;
        String MbeanName = "process:name=SynTempFromTKTUToBikt";
        this.useFixedHour = (0 <= hour && hour <= 23) ? true : false;
        this.minuteRun = (0 <= minute && minute <= 59) ? minute : 0;
        registerAgent(MbeanName);
    }

    @Override
    protected void process() {
        //to do
        logger.info("=======================================================");
        logger.info("*****BEGIN GET SYNCHRONOUS FROM KTTS*****");
        logger.info("=======================================================");
        MyDbTask myDbTask = new MyDbTask();
//        buStartTime = new Date();
        long startTime = System.currentTimeMillis();
        Calendar calendar = Calendar.getInstance();
        int hours = calendar.get(Calendar.HOUR_OF_DAY);
        String startDate = DateTimeUtils.format(DateTimeUtils.add(new Date(), ProcessManager.backDay), "dd/MM/yyyy");
        String endDate = DateTimeUtils.format(DateTimeUtils.add(new Date(), 0), "dd/MM/yyyy");
        String type = ProcessManager.typePartner;
        boolean bRunning = false;
        try {
            if (!useFixedHour) {
                getDataFromKTTS(myDbTask, startDate, endDate, type);
//                getDataFromKTTSOld(myDbTask, startDate, endDate);
                bRunning = true;
            } else {
                if (getCurrentHour() == hour) {
                    //Kiểm tra phút
                    long currentMinute = getCurrentMinute();
                    if (currentMinute >= minuteRun) {
                        getDataFromKTTS(myDbTask, startDate, endDate, type);
//                        getDataFromKTTSOld(myDbTask, startDate, endDate);
                        bRunning = true;
                    } else {
                        //Nghỉ đến phút chạy
                        long sleepTime = (minuteRun - currentMinute) * 60 * 1000;
                        logger.info("Thread " + this.threadName + " sleep " + sleepTime / 1000 + " seconds");
                        Thread.sleep(sleepTime);
                    }
                } else {
                    long currentMinute = getCurrentMinute();
                    //Nghỉ hết giờ hiện tại
                    long sleepTime = (60 - currentMinute) * 60 * 1000;
                    logger.info("Thread " + this.threadName + " sleep " + sleepTime / 1000 + " seconds");
                    Thread.sleep(sleepTime);
                }
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        } finally {
            if (bRunning) {
                try {
                    long endTime = System.currentTimeMillis();
                    long processTime = endTime - startTime;
                    if (interval != 0) {
                        processTime %= interval;
                    }
                    logger.info("Thread syn from WS TKTU sleep " + this.threadName + " sleep " + (interval - processTime) / 1000 + " seconds");
                    Thread.sleep(interval - processTime);
                } catch (InterruptedException ex) {
                    logger.error(ex.getMessage(), ex);
                    Logger.getLogger(ProcessThread.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    private void getDataFromKTTS(MyDbTask db, String startDate, String endDate, String type) throws Exception {
        logger.info("Start Connect to WebService KTTS...");
        int countReply = 1;
        boolean checkWs = true;
        KTTSSynchronizedQLCTWS port = null;
        URL url = null;
        URL baseUrl;

        List<CatStationBO> lstDataStationTram = new ArrayList();
        List<CatStationBO> lstDataStationTuyen = new ArrayList();
        List<CatStationHouseBO> lstDataStationHouse = new ArrayList();
        List<CntContractBO> lstDataContract = new ArrayList();
        List<ProductCompanyBO> lstDataProductCompany = new ArrayList();
        List<MerInExpNoteDTO> lstDataWareExpNote = new ArrayList();
        List<MerInExpNoteDTO> lstDataWareImpNote = new ArrayList();

        Kttsbo kttsBoStationTram = new Kttsbo();
        Kttsbo kttsBoStationTuyen = new Kttsbo();
        Kttsbo kttsBoStationHouse = new Kttsbo();
        Kttsbo kttsBoContract = new Kttsbo();
        Kttsbo kttsBoProductCompany = new Kttsbo();
        Kttsbo kttsBoWareExpNote = new Kttsbo();
        Kttsbo kttsBoWareImpNote = new Kttsbo();

        Kttsbo px = new Kttsbo();
        while ((countReply <= 3 && !checkWs) || countReply == 1) {
            try {
                baseUrl = KTTSSynchronizedQLCTWS_Service.class.getResource(".");
                try {
                    url = new URL(baseUrl, urlStr);
                } catch (MalformedURLException ex) {
                    ex.printStackTrace();
                    logger.error(ex);
                    Logger.getLogger(ProcessThread.class.getName()).log(Level.SEVERE, null, ex);
                }
                KTTSSynchronizedQLCTWS_Service nocService = new KTTSSynchronizedQLCTWS_Service(url, new QName(namespace, localPart));
                port = nocService.getKTTSSynchronizedQLCTWSPort();
                countReply = countReply + 1;
                checkWs = true;
                if (checkWs && port != null) {
                    logger.info("Bat dau lay du lieu tu WS KTTS...");

                    //********************************
                    //START
                    //********************************
                    kttsBoStationTram = port.getListCatStationByCodeForTram("", startDate, endDate, username, password);
                    lstDataStationTram = kttsBoStationTram.getLstCatStationBO();
                    if (lstDataStationTram != null && !lstDataStationTram.isEmpty()) {
                        logger.info("CAT_STATION_TRAM: Co du lieu tra ve tu WS KTTS:" + lstDataStationTram.size());
                        db.updateCatStation(lstDataStationTram, logger, maxBatchSize, 1);
                    } else {
                        logger.info("CAT_STATION_TRAM: Ngay lay khong co du lieu KTTS...");
                    }
                    kttsBoStationTuyen = port.getListCatStationByCodeForTuyen("", startDate, endDate, username, password);
                    lstDataStationTuyen = kttsBoStationTuyen.getLstCatStationBO();
                    if (lstDataStationTuyen != null && !lstDataStationTuyen.isEmpty()) {
                        logger.info("StationTuyen: Co du lieu tra ve tu WS KTTS:" + lstDataStationTuyen.size());
                        db.updateCatStation(lstDataStationTuyen, logger, maxBatchSize, 2);
                    } else {
                        logger.info("StationTuyen: Ngay lay khong co du lieu KTTS...");
                    }
                    kttsBoStationHouse = port.getListCatStationHouseByCode("", startDate, endDate, username, password);
                    lstDataStationHouse = kttsBoStationHouse.getLstCatStationHouseBO();
                    if (lstDataStationHouse != null && !lstDataStationHouse.isEmpty()) {
                        logger.info("StationHouse : Co du lieu tra ve tu WS KTTS:" + lstDataStationHouse.size());
                        db.updateCatStationHouse(lstDataStationHouse, logger, maxBatchSize);
                    } else {
                        logger.info("StationHouse: Ngay lay khong co du lieu KTTS...");
                    }

//                    //GET TYPE PARTNER LIST START
                    String typePartner;
                    List<String> typePartnerLst = new ArrayList<String>();
                    try {
                        Connection con = db.getConnection();
                        String sql = "SELECT DISTINCT KTTS_PARTNER_CODE"
                                + " FROM CTCT_CAT_OWNER.CAT_SYS_GROUP_MAP";
                        PreparedStatement stmt = con.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                        ResultSet rs = stmt.executeQuery();
                        while (rs.next()) {
                            typePartner = rs.getString("KTTS_PARTNER_CODE");
                            typePartnerLst.add(typePartner);
                        }
                        stmt.close();
                        rs.close();
                        con.close();
                    } catch (Exception e) {
                        logger.info("GET TYPE PARTNER FAIL: " + e.toString());
                    }
//                    //GET TYPE PARTNER LIST END

                    kttsBoContract = port.getListContractByCode("", startDate, endDate, username, password, typePartnerLst);
                    lstDataContract = kttsBoContract.getListContract();
                    if (lstDataContract != null && !lstDataContract.isEmpty()) {
                        logger.info("Contract: Co du lieu tra ve tu WS KTTS:" + lstDataContract.size());
                        db.updateCntContract(lstDataContract, logger, maxBatchSize);
                    } else {
                        logger.info("Contract: Ngay lay khong co du lieu KTTS...");
                    }
                    kttsBoProductCompany = port.getListProductCompanyByCode("", startDate, endDate, username, password);
                    lstDataProductCompany = kttsBoProductCompany.getLstProductCompanyBO();
                    if (lstDataProductCompany != null && !lstDataProductCompany.isEmpty()) {
                        logger.info("CAT_MANUFACTURER: Co du lieu tra ve tu WS KTTS:" + lstDataProductCompany.size());
                        db.updateProductCompany(lstDataProductCompany, logger, maxBatchSize);
                    } else {
                        logger.info("CAT_MANUFACTURER: Ngay lay khong co du lieu KTTS...");
                    }
                    
                    
                    //GET KTTS_PARTNER_CODE LIST START
                    String partnerCode;
                    List<String> lstPartnerCodeCTCT = new ArrayList<String>();
                    try {
                        Connection con = db.getConnection();
                        String sql = "SELECT DISTINCT KTTS_PARTNER_CODE"
                                + " FROM CTCT_CAT_OWNER.CAT_SYS_GROUP_MAP";
                        PreparedStatement stmt = con.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                        ResultSet rs = stmt.executeQuery();
                        while (rs.next()) {
                            partnerCode = rs.getString("KTTS_PARTNER_CODE");
                            lstPartnerCodeCTCT.add(partnerCode);
                        }
                        stmt.close();
                        rs.close();
                        con.close();
                    } catch (Exception e) {
                        logger.info("GET TYPE PARTNER FAIL: " + e.toString());
                    }
                    //GET TYPE PARTNER LIST END
                    
                    kttsBoWareExpNote = port.getListWareExpNoteByCode("", startDate, endDate, username, password, lstPartnerCodeCTCT);
                    lstDataWareExpNote = kttsBoWareExpNote.getLstExpNote();
                    if (lstDataWareExpNote != null && !lstDataWareExpNote.isEmpty()) {
                        logger.info("ExpNote: Co du lieu tra ve tu WS KTTS:" + lstDataWareExpNote.size());
                        db.updateWareExpNote(lstDataWareExpNote, logger, maxBatchSize, 2);
                    } else {
                        logger.info("ExpNote: Ngay lay khong co du lieu KTTS...");
                    }

                    kttsBoWareImpNote = port.getListWareImpNoteByCode("", startDate, endDate, username, password);
                    lstDataWareImpNote = kttsBoWareImpNote.getLstMerInImpNote();
                    if (lstDataWareImpNote != null && !lstDataWareImpNote.isEmpty()) {
                        logger.info("ImpNote: Co du lieu tra ve tu WS KTTS:" + lstDataWareImpNote.size());
                        db.updateWareImpNote(lstDataWareImpNote, logger, maxBatchSize, 1);
                    } else {
                        logger.info("ImpNote: Ngay lay khong co du lieu KTTS...");
                    }

                    //****************************
                    //END
                    //****************************
                    logger.info("Ket thuc lay du lieu tu WS KTTS...");
                    logger.info("=======================================================");
                    logger.info("*****END GET SYNCHRONOUS FROM KTTS*****");
                    logger.info("=======================================================");

                }
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                countReply = countReply + 1;
                checkWs = false;
                logger.info("Sleep 10s to try connect WS KTTS....");
                Thread.sleep(10000);
            }
        }
    }

    private void getDataFromKTTSOld(MyDbTask db, String startDate, String endDate) throws Exception {
        logger.info("Start Connect to WebService KTTS...");
        int countReply = 1;
        boolean checkWs = true;
        LogicticsSyncWS port = null;
        URL url = null;
        URL baseUrl;

        List<CatMerchandiseBO> lstDataGoods = new ArrayList();
        List<ConstrConstructionsBO> lstDataConstruction = new ArrayList();

        while ((countReply <= 3 && !checkWs) || countReply == 1) {
            try {
                baseUrl = LogicticsSyncWS_Service.class.getResource(".");
                try {
                    url = new URL(baseUrl, urlStr);
                } catch (MalformedURLException ex) {
                    ex.printStackTrace();
                    logger.error(ex);
                    Logger.getLogger(ProcessThread.class.getName()).log(Level.SEVERE, null, ex);
                }
                LogicticsSyncWS_Service nocService = new LogicticsSyncWS_Service(url, new QName(namespace, localPart));

                port = nocService.getLogicticsSyncWSPort();
                countReply = countReply + 1;
                checkWs = true;
                if (checkWs && port != null) {
                    logger.info("Bat dau lay du lieu tu WS KTTS...");
                    lstDataGoods = port.getListGoods(null, "", startDate, endDate);
                    if (lstDataGoods != null && !lstDataGoods.isEmpty()) {
                        logger.info("GOODS: Co du lieu tra ve tu WS KTTS:" + lstDataGoods.size());
                        db.updateGoods(lstDataGoods, logger, maxBatchSize);
                    } else {
                        logger.info("GOODS: Ngay lay khong co du lieu KTTS...");
                    }

                    lstDataConstruction = port.getListConstr(null, "", startDate, endDate);
                    if (lstDataConstruction != null && !lstDataConstruction.isEmpty()) {
                        logger.info("CONSTRUCTION: Co du lieu tra ve tu WS KTTS:" + lstDataConstruction.size());
                        db.updateConstruction(lstDataConstruction, logger, maxBatchSize);
                    } else {
                        logger.info("CONSTRUCTION: Ngay lay khong co du lieu KTTS...");
                    }

                    //end of the world
                    logger.info("Ket thuc lay du lieu tu WS KTTS...");
                    logger.info("=======================================================");
                    logger.info("*****END GET SYNCHRONOUS FROM KTTS*****");
                    logger.info("=======================================================");
                }
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                countReply = countReply + 1;
                checkWs = false;
                logger.info("Sleep 10s to try connect WS KTTS....");
                Thread.sleep(10000);
            }
        }
    }

    public int getCurrentHour() {
        int currentHour = 0;
        try {
            Calendar now = Calendar.getInstance();
            currentHour = now.get(Calendar.HOUR_OF_DAY);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return currentHour;
    }

    public int getCurrentMinute() {
        int currentMinute = 0;
        try {
            Calendar now = Calendar.getInstance();
            currentMinute = now.get(Calendar.MINUTE);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return currentMinute;
    }
}
