/*
 * Copyright 2012 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.viettel.kpi.SendEmailLuongKhoan;

import static ReportNTD.ExcelReportUtil.getValueFormat;
import com.viettel.kpi.common.utils.DateTimeUtils;
import ReportNTD.FileUtil;
import ReportNTD.*;
import com.viettel.kpi.SendEmailLuongKhoan.EmailSendFrom;
import com.viettel.kpi.SendEmailLuongKhoan.MailUtil;
import com.viettel.kpi.SendEmailLuongKhoan.MyDbTask;
import com.viettel.kpi.common.utils.DBUtil;
import com.viettel.kpi.common.utils.ExcelWriterUtils;
import com.viettel.kpi.common.utils.StringUtils;
import com.viettel.passprotector.PassProtector;
import java.io.BufferedReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.Socket;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.util.CellRangeAddress;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

/**
 *
 * @author dungvv8
 */
public class ProccessEmailThreadService implements Runnable {

    MyClient client;
    public int hour;
    public int day;
    public long intervalSleep = 3600000L;
    private Socket requestSocket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private Boolean checkData;
    private List<InformationIsFileForm> lst;
    private static Logger logger = Logger.getLogger(ProccessEmailThreadService.class);
    List<String> lstFileName = new ArrayList<String>();
    List<String> lstDataHtml = new ArrayList<String>();
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    public Socket getRequestSocket() {
        return requestSocket;
    }

    public void setRequestSocket(Socket requestSocket) {
        this.requestSocket = requestSocket;
    }

    public ObjectOutputStream getOut() {
        return out;
    }

    public void setOut(ObjectOutputStream out) {
        this.out = out;
    }

    public ObjectInputStream getIn() {
        return in;
    }

    public void setIn(ObjectInputStream in) {
        this.in = in;
    }

//    public ProccessEmailThreadService(List<InformationIsFileForm> lst, Logger logger) throws Exception {
    public ProccessEmailThreadService(Logger logger) throws Exception {
//        this.lst = lst;
        this.logger = logger;
    }

    @Override
    public void run() {
        lstDataHtml = new ArrayList<String>();
        lstFileName = new ArrayList<String>();
        client = new MyClient();
        checkData = true;
        try {
            //hoanm1_20180228_start
            MyDbTask db = new MyDbTask();
            logger.info("==========Get Email Success=========");
            List<EmailSendFrom> emailLst = db.getEmployee();

            for (EmailSendFrom lst : emailLst) {
                logger.info("***** Bat dau gui mail *****");
                try {
                    MailUtil mailUtil = new MailUtil();
                    boolean checkSendmail = mailUtil.sendMail(lst, db);
                    if (checkSendmail) {
                        logger.info("***** Gui mail thanh cong *****");
                    } else {
                        logger.info("***** Khong gui duoc Email *****");
                    }
                } catch (Exception ex) {
                    logger.error(ex.getMessage(), ex);
                }
            }
            //hoanm1_20180228_end

        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        } finally {

            try {
                if (client != null) {
                    client.close();
                }
            } catch (Exception ex) {
                logger.error(ex.getMessage(), ex);
            }
        }

    }

    public void sendMessage(String msg) {
        try {
            getOut().writeObject(msg);
            getOut().flush();
            System.out.println("client>" + msg);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
    }

    public boolean onExport(MyClient client, List<InformationIsFileForm> lst) {
        try {
            Date time = new Date();
            time.setDate(time.getDate() - 1);
            ExcelWriterUtils excelUtil = new ExcelWriterUtils();
            SXSSFWorkbook wb = new SXSSFWorkbook();
//            ReportExcelInterface reportExcel = null;

            excelUtil.setSXSSFworkbook(wb);
//            int isFile = lst.get(0).getIsFile();
//            if (isFile == 1) {
            excelUtil = writeTempExport(excelUtil, wb, client, lst);

            if (lstFileName != null && !lstFileName.isEmpty()) {
                String path = lst.get(0).getPathFile();
                String fomatPath = lst.get(0).getFomatPathFile();
                if (fomatPath != null && !fomatPath.equals("")) {
                    String _timePath = DateTimeUtils.convertDateTimeToString(time, fomatPath);
                    path = path.replaceAll(fomatPath, _timePath);
                }
                //hoanm1_update_28042016
                //muc tuan
//                Date timeWeek = new Date() ;
//                String updateTime = DateTimeUtils.convertDateTimeToString(timeWeek, "dd/MM/yyyy");
//                SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
//                Date dateTime = df.parse(updateTime);
//                Calendar cal = Calendar.getInstance();
//                cal.setTime(dateTime);
//                int week = cal.get(Calendar.WEEK_OF_YEAR);
//                int year = cal.get(Calendar.YEAR);
//                String tuan = +week + "_" + year;
//                String reportName = lstFileName.get(0) + "_" + tuan + ".xlsx";
                //muc ngay
                Date timeWeek = new Date();
                Calendar c = Calendar.getInstance();
                c.setTime(timeWeek);
                c.add(Calendar.DATE, -1);
                timeWeek.setTime(c.getTime().getTime());
                String updateTime = DateTimeUtils.convertDateTimeToString(timeWeek, "ddMMyyyy");
                String reportName = lstFileName.get(0) + "_" + updateTime + ".xlsx";
                //hoanm1_update_28042016

                String exportFolder = ProcessManager.pathFile;
                FileUtil.forceFolderExist(exportFolder); //Tạo folder nếu folder chưa tồn tại
                String pathOut = exportFolder + reportName;

                excelUtil.setSheetSelectedSXSSF(0);
                excelUtil.saveToFileExcelSXSSF(pathOut);
            }
//            }
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            return false;
        }
        return true;
    }

    public ExcelWriterUtils writeTempExport(ExcelWriterUtils excelUtil,
            SXSSFWorkbook wb, MyClient client, List<InformationIsFileForm> lst) {
        try {
            Map<String, Sheet> mapSheet = new HashMap<String, Sheet>();
            Map<String, Integer> mapRowSheet = new HashMap<String, Integer>();
            Map<String, Integer> mapRowHeader = new HashMap<String, Integer>();
            List<MergTitleBO> listMergTitle = new ArrayList();

//            int rowSheet = 8;
            int rowSheet = 9;
//            int rowHeader = 7;
            int rowHeader2 = 6;
            int rowHeader1 = 7;
            int rowHeader0 = 8;

            for (InformationIsFileForm infomationIsFileForm : lst) {
                String functionCode = infomationIsFileForm.getFunctionCode();
                List<String> lstSheet = client.getSheetName(functionCode);

                for (String sheetName : lstSheet) {
                    StringBuilder rowData = new StringBuilder();
                    logger.info("sheetName: " + sheetName);
                    List<EmailConfigForm> lstFunction = client.getFunction(functionCode, sheetName);

                    //Đổ dữ liệu vào sheet0
                    //Style
                    String _sheetName = lstFunction.get(0).getSheetName();
                    if (_sheetName == null || _sheetName.equals("")) {
                        sheetName = "sheet";
                    } else {
                        sheetName = _sheetName;
                    }
                    Integer checkRow = mapRowSheet.get(sheetName);

                    int row = 0;
                    if (checkRow == null) {
                        row = rowSheet;
                    } else {
                        row = checkRow + 3;
                    }
                    Sheet exportSheet = wb.getSheet(sheetName);

                    //<editor-fold defaultstate="collapsed" desc="cellStyle">
                    CellStyle csHeader = excelUtil.getCsColHeader();
                    CellStyle csCenter = excelUtil.getCsCenterBoder();
                    CellStyle csDate = excelUtil.getCsCenterBoder();
                    CellStyle csLeft = excelUtil.getCsLeftBoder();
                    CellStyle csRight = excelUtil.getCsRightBoder();
//                    hoanm1_add_04072016
                    CellStyle csRightFloat = excelUtil.getCsRightBoderFloat();
//                    hoanm1_add_04072016
                    CellStyle csSubTitle = excelUtil.getCsSubTitle();
                    CellStyle csTitle = excelUtil.getCsTitle();
                    //</editor-fold>

                    //<editor-fold defaultstate="collapsed" desc="title">
                    Date time = new Date();
//                    time.setDate(time.getDate() - 1);
                    String title = lstFunction.get(0).getFunctionTitle().toUpperCase();
                    if (exportSheet == null) {
                        exportSheet = wb.createSheet(sheetName);

                        excelUtil.creatTemplate(exportSheet, title);
                        ExcelWriterUtils.mergeCells(exportSheet, 5, 5, 1, 6);
                        //muc tuan
//                        String updateTime = DateTimeUtils.convertDateTimeToString(time, "dd/MM/yyyy");
//                        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
//                        Date dateTime = df.parse(updateTime);
//                        Calendar cal = Calendar.getInstance();
//                        cal.setTime(dateTime);
//                        int week = cal.get(Calendar.WEEK_OF_YEAR);
//                        int year = cal.get(Calendar.YEAR);
//                        String tuan = +week + "-" + year;
//                        excelUtil.createCellObject(exportSheet, 1, 5, "Tuần báo cáo:" + tuan, csSubTitle);

                        //muc ngay
                        Date timeWeek = new Date();
                        Calendar c = Calendar.getInstance();
                        c.setTime(timeWeek);
                        c.add(Calendar.DATE, -1);
                        timeWeek.setTime(c.getTime().getTime());
                        String updateTime = DateTimeUtils.convertDateTimeToString(timeWeek, "dd/MM/yyyy");
                        excelUtil.createCellObject(exportSheet, 1, 5, "Ngày báo cáo:" + updateTime, csSubTitle);
                    } else {
                        exportSheet = mapSheet.get(sheetName);
                        ExcelWriterUtils.mergeCells(exportSheet, row - 3, row - 3, 1, 6);
                        excelUtil.setRowHeight(exportSheet, row - 3, 630);
                        excelUtil.createCellObject(exportSheet, 1, row - 3, title, csTitle);
//                        reportExcel.setListMergTitle(listMergeTitleBO);
//                        excelUtil.set
                    }
                    //</editor-fold>

                    //<editor-fold defaultstate="collapsed" desc="header">
                    List<Object[]> headerInfors = new ArrayList<Object[]>();
                    headerInfors.add(new Object[]{"STT", 2000});
                    boolean level1 = false;
                    boolean level2 = false;
                    List listOfListColumnTitleMerge = new ArrayList();
                    List<String> columnNameLevel1 = new ArrayList();
                    List<String> columnNameLevel2 = new ArrayList();
                    for (EmailConfigForm obj : lstFunction) {

                        if (obj.getColumnNameL1() == null || obj.getColumnNameL1().trim().equals("")) {
                            headerInfors.add(new Object[]{getFormatColumn(obj.getColumnName(), time, dateFormat, 1), 7000});
                        } else {
//                            headerInfors.add(new Object[]{getFormatColumn(obj.getColumnName() + ": " + obj.getColumnNameL1(), time, dateFormat, 1), 7000});
                            headerInfors.add(new Object[]{getFormatColumn(obj.getColumnName(), time, dateFormat, 1), 7000});
                        }
                        if (StringUtils.isNotNull(obj.getColumnNameL1())) {
                            level1 = true;
                            columnNameLevel1.add(obj.getColumnNameL1());
                        } else {
                            columnNameLevel1.add(" ");
                        }

                        if (StringUtils.isNotNull(obj.getColumnNameL2())) {
                            level2 = true;
                            columnNameLevel2.add(obj.getColumnNameL2());
                        } else {
                            columnNameLevel2.add(" ");
                        }

                    }
                    if (level2) {
                        listOfListColumnTitleMerge.add(columnNameLevel2);
                    }
                    if (level1) {
                        listOfListColumnTitleMerge.add(columnNameLevel1);
                    }
                    MergTitleBO mergeTitleBO = new MergTitleBO();
                    mergeTitleBO.setListOfListTitleMerge(listOfListColumnTitleMerge);
//                    mergeTitleBO.setSttRS(listRsData.size() - 1);
                    listMergTitle.add(mergeTitleBO);
                    int size = headerInfors.size();
                    int size1 = columnNameLevel1.size();
                    Integer checkRowHeader = mapRowHeader.get(sheetName);
                    int rowHeaderIndex = 0;
                    if (checkRowHeader == null) {
                        if (level1 && level2) {
                            rowHeaderIndex = rowHeader2;
                        } else if (level1 && !level2) {
                            rowHeaderIndex = rowHeader1;
                        } else if (!level1 && !level2) {
                            rowHeaderIndex = rowHeader0;
                        }
                    } else {
                        rowHeaderIndex = checkRowHeader + 1;
                    }

                    int row_row = 6;
                    for (int i = 0; i < size1; i++) {

                        //hoanm1_add_start
                        if (listMergTitle != null && listMergTitle.size() > 0) {
                            for (int abc = 0; abc < listMergTitle.size(); abc++) {
                                MergTitleBO mergTitleBO = listMergTitle.get(abc);
                                if (mergTitleBO.getSttRS() == i) {
                                    List listOfListTitleMerge = mergTitleBO.getListOfListTitleMerge();
                                    for (int a1 = 0; a1 < listOfListTitleMerge.size(); a1++) {
                                        List listTitleMerge = (List) listOfListTitleMerge.get(a1);
                                        String strOldTitle = "";
                                        int startColumn = 0;
                                        for (int a2 = 0; a2 < listTitleMerge.size(); a2++) {
                                            if (strOldTitle.equals("")) {
                                                strOldTitle = String.valueOf(listTitleMerge.get(a2));
                                            } else {
                                                if (!String.valueOf(listTitleMerge.get(a2)).equals(strOldTitle)) {
                                                    exportSheet.addMergedRegion(createCellReangeAddress(startColumn, rowHeaderIndex, a2, rowHeaderIndex, exportSheet, csHeader));
                                                    excelUtil.createCellObject(exportSheet, startColumn, rowHeaderIndex, strOldTitle, csHeader);
                                                    strOldTitle = String.valueOf(listTitleMerge.get(a2));
                                                    startColumn = a2 + 1;
                                                }
                                            }
                                        }
                                        exportSheet.addMergedRegion(createCellReangeAddress(startColumn, rowHeaderIndex, listTitleMerge.size(), rowHeaderIndex, exportSheet, csHeader));
                                        excelUtil.createCellObject(exportSheet, startColumn, rowHeaderIndex, strOldTitle, csHeader);
                                        //Lay lai lan nua
                                        rowHeaderIndex++;
                                    }
                                    break;
                                }
                            }
                        }
                        //hoanm1_add_end  

                    }
                    for (int i = 0; i < size; i++) {
                        Object[] infor = headerInfors.get(i);

                        String label = (String) infor[0];
                        Integer width = (Integer) infor[1];

                        excelUtil.createCell(exportSheet, i, rowHeaderIndex, label).setCellStyle(csHeader);
                        exportSheet.setColumnWidth(i, width.intValue());
                    }

                    excelUtil.setRowHeight(exportSheet, rowHeaderIndex, 600);
                    //</editor-fold>

                    //<editor-fold defaultstate="collapsed" desc="headerEmail">
                    StringBuilder _contentHtml = new StringBuilder();
                    String isBodyEmail = infomationIsFileForm.getIsBodyEmail();
                    if ("on".equalsIgnoreCase(isBodyEmail)) {
                        _contentHtml.append("<style>table {\n"
                                + "    border-collapse: collapse;\n"
                                + "}\n"
                                + "table, td, th {\n"
                                + "    border: 1px solid black;\n"
                                + "}\n"
                                + "th{\n"
                                + " background-color:lightgreen   \n"
                                + "}"
                                + "</style>");
                        _contentHtml.append("<br/>");
                        _contentHtml.append("<table  style=\"width: 800px;border-collapse: collapse;\">");
                        _contentHtml.append("<tr>");

                        _contentHtml.append("       <th colspan=\"" + (lstFunction.size() + 1) + "\"><b>" + title + "</b></th>");
                        _contentHtml.append("   </tr>");
                        _contentHtml.append("   <tr>");
                        _contentHtml.append("       <th>STT</th>");
                        for (EmailConfigForm obj : lstFunction) {
                            if (obj.getColumnNameL1() == null || obj.getColumnNameL1().trim().equals("")) {
                                _contentHtml.append("      <th>" + getFormatColumn(obj.getColumnName(), time, dateFormat, 1) + "</th>");
                            } else {

                                _contentHtml.append("      <th>" + getFormatColumn(obj.getColumnName() + ": " + obj.getColumnNameL1(), time, dateFormat, 1) + "</th>");
                            }
                        }
                        _contentHtml.append("   </tr>");

                    }
                    //</editor-fold>

                    //<editor-fold defaultstate="collapsed" desc="contentQuery">
                    StringBuilder str = new StringBuilder();
                    String string;
                    Clob clob = lstFunction.get(0).getQueryStringsql();

                    BufferedReader bufferRead;
                    try {
                        bufferRead = new BufferedReader(clob.getCharacterStream());
                        while ((string = bufferRead.readLine()) != null) {
                            str.append(string + " ");
                        }
                    } catch (SQLException ex) {
                    }
                    //</editor-fold>

                    String _sql = str.toString();
                    logger.info("_sql: " + _sql);
                    boolean checkData = false;
                    ResultSet rs = null;
                    System.out.println("===GetData===");
                    PreparedStatement pstmt = null;
                    Connection con = null;
                    try {
                        con = client.getConnectionPool();
                        pstmt = con.prepareStatement(_sql);
                        rs = pstmt.executeQuery();
                        int i = 1;

                        while (rs.next()) {
                            checkData = true;

                            //<editor-fold defaultstate="collapsed" desc="content">
                            int currCol = 0;
                            excelUtil.createCell(exportSheet, currCol, row, i, csCenter);
                            currCol++;
                            for (EmailConfigForm temp : lstFunction) {
                                if (rs.getString(temp.getColumnCode()) != null) {
                                    logger.info("getColumnCode " + temp.getColumnCode());
                                    logger.info("getColumnStyle " + temp.getColumnStyle());
                                    if (temp.getColumnStyle() == null || temp.getColumnStyle().equals("")) {
                                        excelUtil.createCell(exportSheet, currCol, row, rs.getString(temp.getColumnCode()), csLeft);
//                                        currCol++;
                                    } else if (temp.getColumnStyle().equalsIgnoreCase("Date")) {
                                        excelUtil.createCell(exportSheet, currCol, row,
                                                getValueFormat(new Date(rs.getTimestamp(temp.getColumnCode()).getTime()), "date"), csCenter);
//                                        currCol++;
                                    } else if (temp.getColumnStyle().equalsIgnoreCase("DateTime")) {
                                        excelUtil.createCell(exportSheet, currCol, row,
                                                getValueFormat(new Date(rs.getTimestamp(temp.getColumnCode()).getTime()), "datetime"), csCenter);
//                                        currCol++;
                                    } else if (temp.getColumnStyle().equalsIgnoreCase("Float")) {
                                        excelUtil.createCell(exportSheet, currCol, row,
                                                getValueFormat(rs.getBigDecimal(temp.getColumnCode()), "float"), csRight);
                                        //                    hoanm1_add_04072016
//                                        excelUtil.createCellFloat(exportSheet, currCol, row, Double.parseDouble(rs.getString(temp.getColumnCode()))).setCellStyle(csRightFloat);
                                        //                    hoanm1_add_04072016
//                                        currCol++;
                                    } else if (temp.getColumnStyle().equalsIgnoreCase("Integer") || temp.getColumnStyle().equalsIgnoreCase("Number")) {
//                                        excelUtil.createCell(exportSheet, currCol, row,
//                                                getValueFormat(rs.getBigDecimal(temp.getColumnCode()), "integer"), csRight);
                                        excelUtil.createCellFloat(exportSheet, currCol, row, Double.parseDouble(rs.getString(temp.getColumnCode()))).setCellStyle(csRightFloat);
//                                        currCol++;
                                    } else {
                                        excelUtil.createCell(exportSheet, currCol, row, rs.getString(temp.getColumnCode()), csLeft);
//                                        currCol++;
                                    }
//                                    else if (temp.getColumnStyle().equalsIgnoreCase("Number")) {
//                                        Double data = DBUtil.getDouble(rs.getObject(temp.getColumnCode()));
//                                        Double f = null;
//
//                                        if (data != null) {
//                                            String strData = data.toString();
//                                            if (strData.contains(".")) {
//                                                if (strData.substring(strData.indexOf(".") + 1, strData.length()).length() < 5) {
//                                                    f = data;
//                                                } else {
//                                                    BigDecimal fd = new BigDecimal(rs.getDouble(temp.getColumnCode()));
//                                                    BigDecimal cutted = fd.setScale(5, RoundingMode.DOWN);
//
//                                                    f = cutted.doubleValue();
//                                                }
//                                            } else {
//                                                f = data;
//
//                                            }
//
//                                        } else {
//                                            f = data;
//                                        }
//                                        excelUtil.createCellObject(exportSheet, currCol, row, f, csRight);
//                                        currCol++;
//                                    }
                                } else {
                                    excelUtil.createCell(exportSheet, currCol, row, rs.getString(temp.getColumnCode()), csLeft);
                                }
                                currCol++;
                            }
                            row = row + 1;

                            //</editor-fold>
                            //<editor-fold defaultstate="collapsed" desc="contentEmail">
                            rowData.append("<tr>");
                            if ("on".equalsIgnoreCase(isBodyEmail)) {

                                rowData.append("       <td>" + i + "</td>");
                                for (EmailConfigForm temp : lstFunction) {
                                    String columnStyle = temp.getColumnStyle();
                                    String columnCode = temp.getColumnCode();
                                    if (rs.getObject(columnCode) != null) {
                                        if (columnStyle == null || columnStyle.equals("")) {
                                            rowData.append("       <td>" + DBUtil.getString(rs.getObject(columnCode)) + "</td>");
                                        } else if (columnStyle.equalsIgnoreCase("DateTime")) {
                                            rowData.append("       <td>" + rs.getTimestamp(columnCode) + "</td>");
                                        } else if (columnStyle.equalsIgnoreCase("Date")) {
                                            rowData.append("       <td>" + DateTimeUtils.convertDateTimeToString(rs.getTimestamp(columnCode), "dd/MM/yyyy") + "</td>");
                                        } else if (columnStyle.equalsIgnoreCase("Float")) {
                                            rowData.append("       <td>" + DBUtil.getFloat(rs.getObject(columnCode)) + "</td>");
                                        } else if (columnStyle.equalsIgnoreCase("Integer")) {
                                            rowData.append("       <td>" + DBUtil.getInteger(rs.getObject(columnCode)) + "</td>");
                                        } else if (columnStyle.equalsIgnoreCase("Number")) {
                                            Float data = DBUtil.getFloat(rs.getObject(columnCode));
                                            Float f = null;
                                            if (data != null) {
                                                String strData = data.toString();
                                                if (strData.contains(".")) {
                                                    if (strData.substring(strData.indexOf(".") + 1, strData.length()).length() < 5) {
                                                        f = data;
                                                    } else {
                                                        BigDecimal fd = new BigDecimal(rs.getDouble(columnCode));
                                                        BigDecimal cutted = fd.setScale(5, RoundingMode.DOWN);
                                                        f = cutted.floatValue();
                                                    }
                                                } else {
                                                    f = data;
                                                }
                                            } else {
                                                f = data;
                                            }
                                            rowData.append("       <td>" + f + "</td>");
                                        }
                                    } else {
                                        rowData.append("       <td></td>");
                                    }

                                }
                            }
                            rowData.append("     </tr>");

                            //hoanm1_add_start
//                        if (listMergTitle != null && listMergTitle.size() > 0) {
//                            for (int abc = 0; abc < listMergTitle.size(); abc++) {
//                                MergTitleBO mergTitleBO = listMergTitle.get(abc);
//                                if (mergTitleBO.getSttRS() == i) {
//                                    List listOfListTitleMerge = mergTitleBO.getListOfListTitleMerge();
//                                    for (int a1 = 0; a1 < listOfListTitleMerge.size(); a1++) {
//                                        List listTitleMerge = (List) listOfListTitleMerge.get(a1);
//                                        String strOldTitle = "";
//                                        int startColumn = 0;
//                                        for (int a2 = 0; a2 < listTitleMerge.size(); a2++) {
//                                            if (strOldTitle.equals("")) {
//                                                strOldTitle = String.valueOf(listTitleMerge.get(a2));
//                                            } else {
//                                                if (!String.valueOf(listTitleMerge.get(a2)).equals(strOldTitle)) {
//                                                    exportSheet.addMergedRegion(createCellReangeAddress(startColumn, row, a2, row, exportSheet, csLeft));
//                                                    excelUtil.createCellObject(exportSheet, startColumn, row, strOldTitle, csLeft);
//                                                    strOldTitle = String.valueOf(listTitleMerge.get(a2));
//                                                    startColumn = a2 + 1;
//                                                }
//                                            }
//                                        }
//                                        exportSheet.addMergedRegion(createCellReangeAddress(startColumn, row, listTitleMerge.size(), row, exportSheet, csLeft));
//                                        excelUtil.createCellObject(exportSheet, startColumn, row, strOldTitle, csLeft);
//                                        //Lay lai lan nua
//                                        row++;
//                                    }
//                                    break;
//                                }
//                            }
//                        }
                            //hoanm1_add_end    
                            i++;
                        }

                        //<editor-fold defaultstate="collapsed" desc="endEmail">
                        if ("on".equalsIgnoreCase(isBodyEmail)) {
                            if (i > 1) {
                                String _contentTable = _contentHtml + rowData.toString() + "</table>";
                                lstDataHtml.add(_contentTable);
                            }
                        }
                        //</editor-fold>

                    } catch (Exception ex) {
                        logger.error(ex.getMessage(), ex);
                        logger.info("Loi functionCode: " + functionCode);
                        throw ex;
                    } finally {
                        if (rs != null) {
                            rs.close();
                        }
                        if (pstmt != null) {
                            try {
                                pstmt.close();
                            } catch (Exception ex) {
                                logger.error(ex.getMessage(), ex);
                            }
                        }
                        try {
                            if (con != null) {
                                con.close();
                            }
                        } catch (Exception ex) {
                            logger.error(ex.getMessage(), ex);
                        }
                        if (checkData) {
                            lstFileName.add(infomationIsFileForm.getFileName());
                        }
                    }

                    mapRowSheet.put(sheetName, rowSheet + row - 8 + 2);
                    if (level1 && level2) {
                        mapRowHeader.put(sheetName, rowHeader2 + row - 8 + 4);
                    } else if (level1 && !level2) {
                        mapRowHeader.put(sheetName, rowHeader1 + row - 8 + 4);
                    } else if (!level1 && !level2) {
                        mapRowHeader.put(sheetName, rowHeader0 + row - 8 + 4);
                    }
//                    mapRowHeader.put(sheetName, rowHeader + row - 8 + 4);
                    mapSheet.put(sheetName, exportSheet);
                }
            }

        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        return excelUtil;
    }

    public String getFormatColumn(String columnName, Date reportDate, SimpleDateFormat dateFormat, int type) {
        if (reportDate != null) {
            Date date = (Date) reportDate.clone();
            Date toDate = (Date) reportDate.clone();
            for (int j = 50; j >= 0; j--) {
                if (columnName.toUpperCase().contains("DAY" + j)) {
                    date.setDate(date.getDate() - j);
                    return columnName.toUpperCase().replace("DAY" + j, dateFormat.format(date));
                } else if (columnName.equalsIgnoreCase("null")) {
                    return " ";
                }
            }
            for (int j = 10; j >= 0; j--) {
                if (columnName.contains("W" + j)) {
                    date.setDate(date.getDate() - 7 * j);
                    toDate.setDate(toDate.getDate() - 7 * j - 6);
                    return columnName.replace("W" + j, "W" + j + " (" + dateFormat.format(toDate) + " - " + dateFormat.format(date) + ")");
                }
            }
            for (int j = 20; j >= 0; j--) {
                if (columnName.contains("MON" + j)) {
                    date.setMonth(date.getMonth() - j);
                    return columnName.replace("MON" + j, "T" + (date.getMonth() + 1) + "/" + (date.getYear() + 1900));
                }
            }
        }
        return columnName;
    }

    private static CellRangeAddress createCellReangeAddress(int col1, int row1, int col2, int row2, Sheet sheet, CellStyle cellStyle) {
        Row row = null;
        Cell cell = null;
        if (cellStyle != null) {
            for (int i = row1; i <= row2; i++) {
                for (int j = col1; j <= col2; j++) {
                    row = sheet.getRow(i);
                    if (row == null) {
                        row = sheet.createRow(i);
                    }
                    cell = row.getCell(j);
                    if (cell == null) {
                        cell = row.createCell(j);
                    }
                    cell.setCellStyle(cellStyle);
                }
            }
        }
        return new CellRangeAddress(row1, row2, col1, col2);
    }
}
