/*
 * Copyright 2012 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.viettel.kpi.ExportFileEmailLuongKhoan;

import static ReportNTD.ExcelReportUtil.getValueFormat;
import com.viettel.kpi.common.utils.DateTimeUtils;
import ReportNTD.FileUtil;
import ReportNTD.*;
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
import java.text.DecimalFormat;
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

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

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
    public String pathFile;
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
    public ProccessEmailThreadService(Logger logger, String pathFile) throws Exception {
//        this.lst = lst;
        this.logger = logger;
        this.pathFile = pathFile;
    }

    @Override
    public void run() {
        lstDataHtml = new ArrayList<String>();
        lstFileName = new ArrayList<String>();
        client = new MyClient();
        checkData = true;
        String content = "";
        String subject = "";
        String fomatSubject = "";
        Date time = new Date();
        time.setDate(time.getDate() - 1);
        try {
            String serverIp = ProcessManager.ipReloadData;
            int serverPort = ProcessManager.portReloadData;
//            setRequestSocket(new Socket(serverIp, serverPort));
            setRequestSocket(new Socket("10.58.71.134", 8352));
            setOut(new ObjectOutputStream(getRequestSocket().getOutputStream()));
            getOut().flush();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        try {
            boolean checkExport = onExportLuongKhoan(client, pathFile);
            sendMessage("hoanm1_022018");
            if (checkExport) {
                if (lstFileName != null && !lstFileName.isEmpty()) {
                    String _bodyHtml = "";
                    if (!lstDataHtml.isEmpty()) {
                        for (String _html : lstDataHtml) {
                            _bodyHtml = _bodyHtml + _html + "<br/><br/>";
                        }
                    }
                    if (!_bodyHtml.equals("")) {
                        content = content + _bodyHtml;
                    }
                } else {
                    content = "Kính gửi đầu mối.<br/>Hiện tại hệ thống chưa có dữ liệu Export.<br/>Trân trọng!";
                }
                if (fomatSubject != null && !fomatSubject.equals("")) {
                    String _timeSubject = DateTimeUtils.convertDateTimeToString(time, fomatSubject);
                    subject = subject.replaceAll(fomatSubject, _timeSubject);
                }
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            logger.info("Xuat bao cao Fail");
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
//    hoanm1_20180228_start

    public boolean onExportLuongKhoan(MyClient client, String pathFile) {
        try {
            List<SalaryEmployeeForm> lst = client.getDataLuongKhoan();
            for (int i = 0; i < lst.size(); i++) {
//                String pathFile = "D:\\20180126\\Template_LuongKhoan.xlsx";
                ExcelWriterUtils excelUtil = new ExcelWriterUtils();
                Workbook workbook = excelUtil.readFileExcel(pathFile + "Template_LuongKhoan.xlsx");
                Sheet tmpSheet = workbook.getSheetAt(0);
                Cell cellString = ExcelWriterUtils.getCellOfSheet(8, 1, tmpSheet);
                CellStyle csString = cellString.getCellStyle();
                //Number Sum
                Cell cellNumberSum = ExcelWriterUtils.getCellOfSheet(19, 1, tmpSheet);
                CellStyle csNumberSum = cellNumberSum.getCellStyle();                
                //Number
                Cell cellNumber = ExcelWriterUtils.getCellOfSheet(20, 1, tmpSheet);
                CellStyle csNumber = cellNumber.getCellStyle();
                
                //Header
                Cell cellHeader = ExcelWriterUtils.getCellOfSheet(5, 0, tmpSheet);
                CellStyle csHeader = cellHeader.getCellStyle();

                Date timeDay = new Date();
                String updateTime = DateTimeUtils.convertDateTimeToString(timeDay, "dd/MM/yyyy");
                SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                Date dateTime = df.parse(updateTime);
                Calendar cal = Calendar.getInstance();
                cal.setTime(dateTime);
                int month = cal.get(Calendar.MONTH);
                int year = cal.get(Calendar.YEAR);
                String thang = +month + "_" + year;
                
                Calendar calMin = Calendar.getInstance();
                calMin.set(Calendar.MONTH, month - 1);
                calMin.set(Calendar.DAY_OF_MONTH, month - 1);
                calMin.set(Calendar.YEAR, year);
                int minDay = calMin.getActualMinimum(Calendar.DAY_OF_MONTH);

                Calendar calMax = Calendar.getInstance();
                calMax.set(Calendar.MONTH, month - 1);
                calMax.set(Calendar.DAY_OF_MONTH, month - 1);
                calMax.set(Calendar.YEAR, year);
                int maxDay = calMax.getActualMaximum(Calendar.DAY_OF_MONTH);
                
                DecimalFormat dfFormat= new DecimalFormat("#,###");

                excelUtil.createCell(tmpSheet, 0, 5, String.valueOf("Tháng " + month + " năm " + year)).setCellStyle(csHeader);
                excelUtil.createCell(tmpSheet, 0, 6, String.valueOf("Từ " + minDay + "/" + month + "/" + year + " đến " + maxDay + "/" + month + "/" + year)).setCellStyle(csHeader);

                excelUtil.createCell(tmpSheet, 1, 8, String.valueOf(lst.get(i).getFull_name())).setCellStyle(csString);
                excelUtil.createCell(tmpSheet, 4, 8, String.valueOf(lst.get(i).getStaff_code())).setCellStyle(csString);
                excelUtil.createCell(tmpSheet, 1, 9, String.valueOf("")).setCellStyle(csString);
                excelUtil.createCell(tmpSheet, 4, 9, String.valueOf(lst.get(i).getContract_type())).setCellStyle(csString);
                excelUtil.createCell(tmpSheet, 1, 10, String.valueOf(lst.get(i).getUnit_name())).setCellStyle(csString);
                excelUtil.createCellNumberLuong(tmpSheet, 4, 10, Long.parseLong(lst.get(i).getCong_che_do())).setCellStyle(csString);
                excelUtil.createCell(tmpSheet, 1, 11, String.valueOf(lst.get(i).getSo_tai_khoan())).setCellStyle(csString);
                excelUtil.createCell(tmpSheet, 4, 11, String.valueOf(lst.get(i).getNganhang())).setCellStyle(csString);
                excelUtil.createCell(tmpSheet, 1, 12, String.valueOf(lst.get(i).getEmail())).setCellStyle(csString);

                excelUtil.createCell(tmpSheet, 1, 14, String.valueOf("")).setCellStyle(csNumber);
                excelUtil.createCell(tmpSheet, 4, 14, String.valueOf(lst.get(i).getHSCD())).setCellStyle(csNumber);
                excelUtil.createCell(tmpSheet, 1, 15, String.valueOf(lst.get(i).getTham_nien())).setCellStyle(csNumber);
                excelUtil.createCell(tmpSheet, 4, 15, String.valueOf(lst.get(i).getKNL())).setCellStyle(csNumber);
                excelUtil.createCell(tmpSheet, 1, 16, String.valueOf(lst.get(i).getKI())).setCellStyle(csNumber);
                excelUtil.createCellNumeric(tmpSheet, 4, 16, String.valueOf(lst.get(i).getCong_tinh_luong())).setCellStyle(csNumber);
                excelUtil.createCell(tmpSheet, 1, 17, String.valueOf("")).setCellStyle(csNumber);
                excelUtil.createCell(tmpSheet, 4, 17, String.valueOf(lst.get(i).getHSQMTT())).setCellStyle(csNumber);
                excelUtil.createCell(tmpSheet, 1, 18, String.valueOf("")).setCellStyle(csNumber);

                excelUtil.createCell(tmpSheet, 1, 19, String.valueOf("")).setCellStyle(csNumberSum);
//                excelUtil.createCell(tmpSheet, 1, 20, String.valueOf(lst.get(i).getLuong_kcq_ttvhkt())).setCellStyle(csNumber);
                excelUtil.createCellNumberLuong(tmpSheet, 1, 20, Long.parseLong(lst.get(i).getLuong_kcq_ttvhkt())).setCellStyle(csNumber);
                excelUtil.createCellNumberLuong(tmpSheet, 4, 20, Long.parseLong(lst.get(i).getLuong_co_dong())).setCellStyle(csNumber);
                excelUtil.createCellNumberLuong(tmpSheet, 1, 21, Long.parseLong(lst.get(i).getLuong_giantiep_tinhhuyen())).setCellStyle(csNumber);
                Long luong = 0L;
                if (lst.get(i).getLuong_hotro_NAN() != null && lst.get(i).getLuong_hotro_KHA() != null) {
                    luong = Long.parseLong(lst.get(i).getLuong_hotro_NAN()) + Long.parseLong(lst.get(i).getLuong_hotro_KHA());
                }
                excelUtil.createCellNumberLuong(tmpSheet, 4, 21, Long.parseLong(luong.toString())).setCellStyle(csNumber);
                excelUtil.createCellNumberLuong(tmpSheet, 1, 22, Long.parseLong(lst.get(i).getLuong_nha_tram())).setCellStyle(csNumber);
                excelUtil.createCellNumberLuong(tmpSheet, 4, 22, Long.parseLong(lst.get(i).getLuong_thaisan_chohuu())).setCellStyle(csNumber);
                excelUtil.createCellNumberLuong(tmpSheet, 1, 23, Long.parseLong(lst.get(i).getLuong_day_may())).setCellStyle(csNumber);
                excelUtil.createCellNumberLuong(tmpSheet, 4, 23, Long.parseLong(lst.get(i).getLuong_hotro_thitruong())).setCellStyle(csNumber);
                excelUtil.createCellNumberLuong(tmpSheet, 1, 24, Long.parseLong(lst.get(i).getLuong_lai_xe())).setCellStyle(csNumber);
                excelUtil.createCell(tmpSheet, 4, 24, String.valueOf("")).setCellStyle(csNumber);
                Long luong_le = 0L;
                if (lst.get(i).getLuong_them_2212() != null && lst.get(i).getLuong_them_tet() != null) {
                    luong_le = Long.parseLong(lst.get(i).getLuong_them_2212()) + Long.parseLong(lst.get(i).getLuong_them_tet());
                }
                excelUtil.createCellNumberLuong(tmpSheet, 1, 25, Long.parseLong((luong_le.toString()))).setCellStyle(csNumber);
                excelUtil.createCellNumberLuong(tmpSheet, 4, 25, Long.parseLong(lst.get(i).getLuong_thu_lao_quyettoan())).setCellStyle(csNumber);

                excelUtil.createCell(tmpSheet, 1, 26, String.valueOf("")).setCellStyle(csNumberSum);
                excelUtil.createCellNumberLuong(tmpSheet, 1, 27, Long.parseLong(lst.get(i).getAn_ca_quyettoan())).setCellStyle(csNumber);
                excelUtil.createCellNumberLuong(tmpSheet, 4, 27, Long.parseLong(lst.get(i).getDoitruong_totruong_quyettoan())).setCellStyle(csNumber);
                excelUtil.createCellNumberLuong(tmpSheet, 1, 28, Long.parseLong(lst.get(i).getDien_thoai_quyettoan())).setCellStyle(csNumber);
                excelUtil.createCellNumberLuong(tmpSheet, 4, 28, Long.parseLong(lst.get(i).getDia_ban_quyettoan())).setCellStyle(csNumber);
                excelUtil.createCellNumberLuong(tmpSheet, 1, 29, Long.parseLong(lst.get(i).getXang_xe_quyettoan())).setCellStyle(csNumber);
                excelUtil.createCellNumberLuong(tmpSheet, 4, 29, Long.parseLong(lst.get(i).getPhu_cap_khac_quyettoan())).setCellStyle(csNumber);
                excelUtil.createCellNumberLuong(tmpSheet, 1, 30, Long.parseLong(lst.get(i).getThi_truong_quyettoan())).setCellStyle(csNumber);

                excelUtil.createCell(tmpSheet, 1, 31, String.valueOf("")).setCellStyle(csNumberSum);
                excelUtil.createCellNumberLuong(tmpSheet, 1, 32, Long.parseLong(lst.get(i).getLuong_bao_hiem_tamung())).setCellStyle(csNumber);
                excelUtil.createCell(tmpSheet, 4, 32, String.valueOf("")).setCellStyle(csNumber);
                excelUtil.createCellNumberLuong(tmpSheet, 1, 33, Long.parseLong(lst.get(i).getBao_hiem_tamung())).setCellStyle(csNumber);
                excelUtil.createCellNumberLuong(tmpSheet, 4, 33, Long.parseLong(lst.get(i).getDia_ban_tamung())).setCellStyle(csNumber);
                excelUtil.createCellNumberLuong(tmpSheet, 1, 34, Long.parseLong(lst.get(i).getAn_ca_tamung())).setCellStyle(csNumber);
                excelUtil.createCellNumberLuong(tmpSheet, 4, 34, Long.parseLong(lst.get(i).getPhu_cap_khac_tamung())).setCellStyle(csNumber);
                excelUtil.createCellNumberLuong(tmpSheet, 1, 35, Long.parseLong(lst.get(i).getDien_thoai_tamung())).setCellStyle(csNumber);
                excelUtil.createCellNumberLuong(tmpSheet, 4, 35, Long.parseLong(lst.get(i).getThi_truong_tamung())).setCellStyle(csNumber);
                excelUtil.createCellNumberLuong(tmpSheet, 1, 36, Long.parseLong(lst.get(i).getXang_xe_tamung())).setCellStyle(csNumber);
                excelUtil.createCell(tmpSheet, 4, 36, String.valueOf("")).setCellStyle(csNumber);
                excelUtil.createCell(tmpSheet, 1, 37, String.valueOf("")).setCellStyle(csNumber);

                excelUtil.createCell(tmpSheet, 1, 38, String.valueOf("")).setCellStyle(csNumberSum);
                excelUtil.createCellNumberLuong(tmpSheet, 1, 39, Long.parseLong(lst.get(i).getBao_hiem_connhan())).setCellStyle(csNumber);
                excelUtil.createCellNumberLuong(tmpSheet, 4, 39, Long.parseLong(lst.get(i).getTien_bep_an())).setCellStyle(csNumber);
                excelUtil.createCellNumberLuong(tmpSheet, 1, 40, Long.parseLong(lst.get(i).getThue_TNCN_connhan())).setCellStyle(csNumber);
                excelUtil.createCell(tmpSheet, 4, 40, String.valueOf("")).setCellStyle(csNumber);
                excelUtil.createCellNumberLuong(tmpSheet, 1, 41, Long.parseLong(lst.get(i).getDpcd())).setCellStyle(csNumber);
                excelUtil.createCellNumberLuong(tmpSheet, 1, 42, Long.parseLong(lst.get(i).getChuyen_khoan())).setCellStyle(csNumberSum);

                String exportFolder = ProcessManager.pathFile;
                FileUtil.forceFolderExist(exportFolder); //Tạo folder nếu folder chưa tồn tại
                String email = "";
                if (lst.get(i).getEmail() != null) {
                    String[] account = lst.get(i).getEmail().split("@");
                    email = account[0];
                }

                String pathOut = exportFolder + email + "_luong thang " + thang + ".xlsx";
                excelUtil.setSheetSelected(0);
                excelUtil.saveToFileExcel(pathOut);
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            return false;
        }
        return true;
    }
//    public boolean onExportLuongKhoanOld(MyClient client, String pathFile) {
//        try {
//            List<SalaryEmployeeForm> lst = client.getDataLuongKhoan();
//            for (int i = 0; i < lst.size(); i++) {
////                String pathFile = "D:\\20180126\\Template_LuongKhoan.xlsx";
//                ExcelWriterUtils excelUtil = new ExcelWriterUtils();
//                Workbook workbook = excelUtil.readFileExcel(pathFile + "Template_LuongKhoan.xlsx");
//                Sheet tmpSheet = workbook.getSheetAt(0);
//                Cell cellString = ExcelWriterUtils.getCellOfSheet(3, 1, tmpSheet);
//                CellStyle csString = cellString.getCellStyle();
//                //Number stt
//                Cell cellNumberStt = ExcelWriterUtils.getCellOfSheet(3, 0, tmpSheet);
//                CellStyle csNumberStt = cellNumberStt.getCellStyle();
//                //Number
//                Cell cellNumber = ExcelWriterUtils.getCellOfSheet(3, 6, tmpSheet);
//                CellStyle csNumber = cellNumber.getCellStyle();
//
//                int currentColunmData = 0;
//                int row = 3;
//                excelUtil.createCell(tmpSheet, currentColunmData, row, String.valueOf(row)).setCellStyle(csNumberStt);
//                currentColunmData++;
//                excelUtil.createCell(tmpSheet, currentColunmData, row, String.valueOf(lst.get(i).getStaff_code())).setCellStyle(csString);
//                currentColunmData++;
//                excelUtil.createCell(tmpSheet, currentColunmData, row, String.valueOf(lst.get(i).getFull_name())).setCellStyle(csString);
//                currentColunmData++;
//                excelUtil.createCell(tmpSheet, currentColunmData, row, String.valueOf(lst.get(i).getEmail())).setCellStyle(csString);
//                currentColunmData++;
//                excelUtil.createCell(tmpSheet, currentColunmData, row, String.valueOf(lst.get(i).getContract_type())).setCellStyle(csString);
//                currentColunmData++;
//                excelUtil.createCell(tmpSheet, currentColunmData, row, String.valueOf(lst.get(i).getUnit_name())).setCellStyle(csString);
//                currentColunmData++;
//                excelUtil.createCell(tmpSheet, currentColunmData, row, String.valueOf(lst.get(i).getHSCD())).setCellStyle(csNumber);
//                currentColunmData++;
//                excelUtil.createCell(tmpSheet, currentColunmData, row, String.valueOf(lst.get(i).getKNL())).setCellStyle(csNumber);
//                currentColunmData++;
//                excelUtil.createCell(tmpSheet, currentColunmData, row, String.valueOf(lst.get(i).getHSQMTT())).setCellStyle(csNumber);
//                currentColunmData++;
//                excelUtil.createCell(tmpSheet, currentColunmData, row, String.valueOf(lst.get(i).getTham_nien())).setCellStyle(csNumber);
//                currentColunmData++;
//                excelUtil.createCell(tmpSheet, currentColunmData, row, String.valueOf(lst.get(i).getCong_che_do())).setCellStyle(csNumber);
//                currentColunmData++;
//                excelUtil.createCell(tmpSheet, currentColunmData, row, String.valueOf(lst.get(i).getCong_tinh_luong())).setCellStyle(csNumber);
//                currentColunmData++;
//                excelUtil.createCell(tmpSheet, currentColunmData, row, String.valueOf(lst.get(i).getKI())).setCellStyle(csString);
//                currentColunmData++;
//                excelUtil.createCell(tmpSheet, currentColunmData, row, String.valueOf(lst.get(i).getLuong_thu_lao_quyettoan())).setCellStyle(csNumber);
//                currentColunmData++;
//                excelUtil.createCell(tmpSheet, currentColunmData, row, String.valueOf(lst.get(i).getBao_hiem_quyettoan())).setCellStyle(csNumber);
//                currentColunmData++;
//                excelUtil.createCell(tmpSheet, currentColunmData, row, String.valueOf(lst.get(i).getThue_TNCN_quyettoan())).setCellStyle(csNumber);
//                currentColunmData++;
//                excelUtil.createCell(tmpSheet, currentColunmData, row, String.valueOf(lst.get(i).getAn_ca_quyettoan())).setCellStyle(csNumber);
//                currentColunmData++;
//                excelUtil.createCell(tmpSheet, currentColunmData, row, String.valueOf(lst.get(i).getDien_thoai_quyettoan())).setCellStyle(csNumber);
//                currentColunmData++;
//                excelUtil.createCell(tmpSheet, currentColunmData, row, String.valueOf(lst.get(i).getXang_xe_quyettoan())).setCellStyle(csNumber);
//                currentColunmData++;
//                excelUtil.createCell(tmpSheet, currentColunmData, row, String.valueOf(lst.get(i).getDia_ban_quyettoan())).setCellStyle(csNumber);
//                currentColunmData++;
//                excelUtil.createCell(tmpSheet, currentColunmData, row, String.valueOf(lst.get(i).getThi_truong_quyettoan())).setCellStyle(csNumber);
//                currentColunmData++;
//                excelUtil.createCell(tmpSheet, currentColunmData, row, String.valueOf(lst.get(i).getDoitruong_totruong_quyettoan())).setCellStyle(csNumber);
//                currentColunmData++;
//                excelUtil.createCell(tmpSheet, currentColunmData, row, String.valueOf(lst.get(i).getPhu_cap_khac_quyettoan())).setCellStyle(csNumber);
//                currentColunmData++;
//                excelUtil.createCell(tmpSheet, currentColunmData, row, String.valueOf(lst.get(i).getLuong_bao_hiem_tamung())).setCellStyle(csNumber);
//                currentColunmData++;
//                excelUtil.createCell(tmpSheet, currentColunmData, row, String.valueOf(lst.get(i).getBao_hiem_tamung())).setCellStyle(csNumber);
//                currentColunmData++;
//                excelUtil.createCell(tmpSheet, currentColunmData, row, String.valueOf(lst.get(i).getAn_ca_tamung())).setCellStyle(csNumber);
//                currentColunmData++;
//                excelUtil.createCell(tmpSheet, currentColunmData, row, String.valueOf(lst.get(i).getDien_thoai_tamung())).setCellStyle(csNumber);
//                currentColunmData++;
//                excelUtil.createCell(tmpSheet, currentColunmData, row, String.valueOf(lst.get(i).getXang_xe_tamung())).setCellStyle(csNumber);
//                currentColunmData++;
//                excelUtil.createCell(tmpSheet, currentColunmData, row, String.valueOf(lst.get(i).getDia_ban_tamung())).setCellStyle(csNumber);
//                currentColunmData++;
//                excelUtil.createCell(tmpSheet, currentColunmData, row, String.valueOf(lst.get(i).getThi_truong_tamung())).setCellStyle(csNumber);
//                currentColunmData++;
//                excelUtil.createCell(tmpSheet, currentColunmData, row, String.valueOf(lst.get(i).getPhu_cap_khac_tamung())).setCellStyle(csNumber);
//                currentColunmData++;
//                excelUtil.createCell(tmpSheet, currentColunmData, row, String.valueOf(lst.get(i).getKhoan_nsld())).setCellStyle(csNumber);
//                currentColunmData++;
//                excelUtil.createCell(tmpSheet, currentColunmData, row, String.valueOf(lst.get(i).getBao_hiem_connhan())).setCellStyle(csNumber);
//                currentColunmData++;
//                excelUtil.createCell(tmpSheet, currentColunmData, row, String.valueOf(lst.get(i).getThue_TNCN_connhan())).setCellStyle(csNumber);
//                currentColunmData++;
//                excelUtil.createCell(tmpSheet, currentColunmData, row, String.valueOf(lst.get(i).getAn_ca_connhan())).setCellStyle(csNumber);
//                currentColunmData++;
//                excelUtil.createCell(tmpSheet, currentColunmData, row, String.valueOf(lst.get(i).getDien_thoai_connhan())).setCellStyle(csNumber);
//                currentColunmData++;
//                excelUtil.createCell(tmpSheet, currentColunmData, row, String.valueOf(lst.get(i).getXang_xe_connhan())).setCellStyle(csNumber);
//                currentColunmData++;
//                excelUtil.createCell(tmpSheet, currentColunmData, row, String.valueOf(lst.get(i).getDia_ban_connhan())).setCellStyle(csNumber);
//                currentColunmData++;
//                excelUtil.createCell(tmpSheet, currentColunmData, row, String.valueOf(lst.get(i).getThi_truong_connhan())).setCellStyle(csNumber);
//                currentColunmData++;
//                excelUtil.createCell(tmpSheet, currentColunmData, row, String.valueOf(lst.get(i).getDoitruong_totruong_connhan())).setCellStyle(csNumber);
//                currentColunmData++;
//                excelUtil.createCell(tmpSheet, currentColunmData, row, String.valueOf(lst.get(i).getPhu_cap_khac_connhan())).setCellStyle(csNumber);
//                currentColunmData++;
//                excelUtil.createCell(tmpSheet, currentColunmData, row, String.valueOf(lst.get(i).getCon_nhan())).setCellStyle(csNumber);
//                currentColunmData++;
//                excelUtil.createCell(tmpSheet, currentColunmData, row, String.valueOf(lst.get(i).getGhichu())).setCellStyle(csString);
//                currentColunmData++;
//                excelUtil.createCell(tmpSheet, currentColunmData, row, String.valueOf(lst.get(i).getTien_bep_an())).setCellStyle(csNumber);
//                currentColunmData++;
//                excelUtil.createCell(tmpSheet, currentColunmData, row, String.valueOf(lst.get(i).getDpcd())).setCellStyle(csNumber);
//                currentColunmData++;
//                excelUtil.createCell(tmpSheet, currentColunmData, row, String.valueOf(lst.get(i).getChuyen_khoan())).setCellStyle(csNumber);
//                currentColunmData++;
//                excelUtil.createCell(tmpSheet, currentColunmData, row, String.valueOf(" "));
//                currentColunmData++;
//                row++;
//                String exportFolder = ProcessManager.pathFile;
//                FileUtil.forceFolderExist(exportFolder); //Tạo folder nếu folder chưa tồn tại
//                String email = "";
//                if (lst.get(i).getEmail() != null) {
//                    String[] account = lst.get(i).getEmail().split("@");
//                    email = account[0];
//                }
//                Date timeDay = new Date();
//                String updateTime = DateTimeUtils.convertDateTimeToString(timeDay, "dd/MM/yyyy");
//                SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
//                Date dateTime = df.parse(updateTime);
//                Calendar cal = Calendar.getInstance();
//                cal.setTime(dateTime);
//                int month = cal.get(Calendar.MONTH);
//                int year = cal.get(Calendar.YEAR);
//                String thang = +month + "_" + year;
//                String pathOut = exportFolder + email + "_luong thang " + thang + ".xlsx";
//                excelUtil.setSheetSelected(0);
//                excelUtil.saveToFileExcel(pathOut);
//            }
//        } catch (Exception ex) {
//            logger.error(ex.getMessage(), ex);
//            return false;
//        }
//        return true;
//    }
    //    hoanm1_20180228_end
}
