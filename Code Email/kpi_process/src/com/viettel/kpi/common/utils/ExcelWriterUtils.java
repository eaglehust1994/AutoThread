/*
 * Copyright 2011 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.viettel.kpi.common.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.DVConstraint;
import org.apache.poi.hssf.usermodel.HSSFDataValidation;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Name;
import org.apache.poi.ss.usermodel.Picture;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.util.*;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFFont;

/**
 * Class help to manipulate excel file
 *
 * @author ThuanNHT
 * @version 1.0
 * @since: 1.0
 */
public class ExcelWriterUtils {

    private Workbook workbook;
    private Logger logger = Logger.getLogger(ExcelWriterUtils.class);
    private FileOutputStream fileOut;
    private SXSSFWorkbook SXSSFworkbook;

    /**
     * Method to create a workbook to work with excel
     *
     * @param filePathName ThuanNHT
     */
    public void createWorkBook(String filePathName) {
        if (filePathName.endsWith(".xls") || filePathName.endsWith(".XLS")) {
            workbook = new HSSFWorkbook();
        } else if (filePathName.endsWith(".xlsx") || filePathName.endsWith(".XLSX")) {
            workbook = new XSSFWorkbook();
        }
    }

    public SXSSFWorkbook getSXSSFworkbook() {
        return SXSSFworkbook;
    }

    public void setSXSSFworkbook(SXSSFWorkbook SXSSFworkbook) {
        this.SXSSFworkbook = SXSSFworkbook;
    }

    /**
     * Method to create a new excel(xls,xlsx) file with file Name
     *
     * @param fileName ThuanNHT
     */
    public void saveToFileExcel(String filePathName) {
        try {
            fileOut = new FileOutputStream(filePathName);
            workbook.write(fileOut);
        } catch (Exception ex) {
            logger.error(ex);
        } finally {
            try {
                fileOut.close();
                workbook = null;
            } catch (IOException ex) {
                logger.error(ex);
            }
        }
    }

    /**
     * method to create a sheet
     *
     * @param sheetName ThuanNHT
     */
    public Sheet createSheet(String sheetName) {
        String temp = WorkbookUtil.createSafeSheetName(sheetName);
        return workbook.createSheet(temp);
    }

    /**
     * method t create a row
     *
     * @param r
     * @return ThuanNHT
     */
    public Row createRow(Sheet sheet, int r) {
        Row row = sheet.createRow(r);
        return row;
    }

    /**
     * method to create a cell with value
     *
     * @param cellValue ThuanNHT
     */
    public Cell createCell(Row row, int column, String cellValue) {
        // Create a cell and put a value in it.
        Cell cell = row.createCell(column);
        cell.setCellValue(cellValue);
        return cell;
    }

    /**
     * method to create a cell with value
     *
     * @param cellValue ThuanNHT
     */
    public Cell createCell(Sheet sheet, int c, int r, String cellValue) {
        Row row = sheet.getRow(r);
        if (row == null) {
            row = sheet.createRow(r);
        }
        // Create a cell and put a value in it.
        Cell cell = row.createCell(c);
        cell.setCellValue(cellValue);
        return cell;
    }
    //hoanm1_25-12-2013_start

    public Cell createCellNumeric(Sheet sheet, int c, int r, String cellValue) {
        Row row = sheet.getRow(r);
        if (row == null) {
            row = sheet.createRow(r);
        }
        Cell cell = row.createCell(c);
        cell.setCellType(Cell.CELL_TYPE_NUMERIC);
//        cell.setCellValue(Long.parseLong(cellValue));
        cell.setCellValue(cellValue);
        return cell;
    }

    public Cell createCellNumberLuong(Sheet sheet, int c, int r, Long cellValue) {
        Row row = sheet.getRow(r);
        if (row == null) {
            row = sheet.createRow(r);
        }
        Cell cell = row.createCell(c);
        try {
            if (cellValue != null) {
                DecimalFormat df = new DecimalFormat("#.###");
                cell.setCellType(Cell.CELL_TYPE_NUMERIC);
                String cellValueNew = df.format(Long.parseLong(cellValue.toString()));
                cell.setCellValue(Long.parseLong(cellValueNew));
            } else {
                cell.setCellValue("");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return cell;
    }

    public Cell createCellFloatLuong(Sheet sheet, int c, int r, Double cellValue) {
        Row row = sheet.getRow(r);
        if (row == null) {
            row = sheet.createRow(r);
        }
        Cell cell = row.createCell(c);
        try {
            if (cellValue != null) {
                DecimalFormat df = new DecimalFormat("#.##");
                cell.setCellType(Cell.CELL_TYPE_NUMERIC);
                String cellValueNew = df.format(Double.parseDouble(cellValue.toString()));
                cell.setCellValue(Double.parseDouble(cellValueNew));
            } else {
                cell.setCellValue("");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return cell;
    }

    public Cell createCellFloat(Sheet sheet, int c, int r, String cellValue) {
        Row row = sheet.getRow(r);
        if (row == null) {
            row = sheet.createRow(r);
        }
        Cell cell = row.createCell(c);
        cell.setCellType(Cell.CELL_TYPE_NUMERIC);
        cell.setCellValue(Double.parseDouble(cellValue));
        return cell;
    }

    public Cell createCellNumSTP(Sheet sheet, int c, int r, Double cellValue) {
        Row row = sheet.getRow(r);
        if (row == null) {
            row = sheet.createRow(r);
        }
        Cell cell = row.createCell(c);
        cell.setCellType(Cell.CELL_TYPE_NUMERIC);
        cell.setCellValue(cellValue);
        return cell;
    }
    //hoanm1_25-12-2013_end

    public Cell createCell1(Sheet sheet, int c, int r, double cellValue) {
        Row row = sheet.getRow(r);
        if (row == null) {
            row = sheet.createRow(r);
        }
        // Create a cell and put a value in it.
        Cell cell = row.createCell(c);
        cell.setCellValue(cellValue);
        return cell;
    }

    /**
     * method to create a cell with value with style
     *
     * @param cellValue ThuanNHT
     */
//    public Cell createCell(Sheet sheet, int c, int r, String cellValue, CellStyle style) {
//        Row row = sheet.getRow(r);
//        if (row == null) {
//            row = sheet.createRow(r);
//        }
//        // Create a cell and put a value in it.
//        Cell cell = row.createCell(c);
//        cell.setCellValue(cellValue);
//        cell.setCellStyle(style);
//        return cell;
//    }
    /**
     * Method get primitive content Of cell
     *
     * @param sheet
     * @param c
     * @param r
     * @return
     */
    public static Object getCellContent(Sheet sheet, int c, int r) {
        Cell cell = getCellOfSheet(r, c, sheet);
        if (cell == null) {
            return "";
        }
        switch (cell.getCellType()) {
            case Cell.CELL_TYPE_STRING:
                return cell.getRichStringCellValue().getString();
            case Cell.CELL_TYPE_NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue();
                } else {
                    return cell.getNumericCellValue();
                }
            case Cell.CELL_TYPE_BOOLEAN:
                return cell.getBooleanCellValue();
            case Cell.CELL_TYPE_FORMULA:
                return cell.getCellFormula();
            default:
                return "";

        }
    }

    /**
     * Method set sheet is selected when is opened
     *
     * @param posSheet
     */
    public void setSheetSelected(int posSheet) {
        try {
            workbook.setActiveSheet(posSheet);
        } catch (IllegalArgumentException ex) {
            workbook.setActiveSheet(0);
        }
    }

    /**
     * method to merge cell
     *
     * @param sheet
     * @param firstRow based 0
     * @param lastRow based 0
     * @param firstCol based 0
     * @param lastCol based 0
     */
    public static void mergeCells(Sheet sheet, int firstRow, int lastRow, int firstCol, int lastCol) {
        sheet.addMergedRegion(new CellRangeAddress(
                firstRow, //first row (0-based)
                lastRow, //last row  (0-based)
                firstCol, //first column (0-based)
                lastCol //last column  (0-based)
        ));
    }

    /**
     * method to fill color background for cell
     *
     * @param cell
     * @param colors:BLACK, WHITE, RED, BRIGHT_GREEN, BLUE, YELLOW, PINK,
     * TURQUOISE, DARK_RED, GREEN, DARK_BLUE, DARK_YELLOW, VIOLET, TEAL,
     * GREY_25_PERCENT, GREY_50_PERCENT, CORNFLOWER_BLUE, MAROON, LEMON_CHIFFON,
     * ORCHID, CORAL, ROYAL_BLUE, LIGHT_CORNFLOWER_BLUE, SKY_BLUE,
     * LIGHT_TURQUOISE, LIGHT_GREEN, LIGHT_YELLOW, PALE_BLUE, ROSE, LAVENDER,
     * TAN, LIGHT_BLUE, AQUA, LIME, GOLD, LIGHT_ORANGE, ORANGE, BLUE_GREY,
     * GREY_40_PERCENT, DARK_TEAL, SEA_GREEN, DARK_GREEN, OLIVE_GREEN, BROWN,
     * PLUM, INDIGO, GREY_80_PERCENT, AUTOMATIC;
     */
    public void fillAndColorCell(Cell cell, IndexedColors colors) {
        CellStyle style = workbook.createCellStyle();
        style.setFillBackgroundColor(colors.getIndex());
        cell.setCellStyle(style);
    }
    // datpk  lay object tu Row

    public static Object getCellContentRow(int c, Row row) {
        Cell cell = getCellOfSheetRow(c, row);
        if (cell == null) {
            return "";
        }
        switch (cell.getCellType()) {
            case Cell.CELL_TYPE_STRING:
                return cell.getRichStringCellValue().getString();
            case Cell.CELL_TYPE_NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue();
                } else {
                    return cell.getNumericCellValue();
                }
            case Cell.CELL_TYPE_BOOLEAN:
                return cell.getBooleanCellValue();
            case Cell.CELL_TYPE_FORMULA:
                return cell.getCellFormula();
            default:
                return "";

        }
    }

    /**
     * Method get text content Of cell
     *
     * @param sheet
     * @param c
     * @param r
     * @return
     */
    public static String getCellStrContent(Sheet sheet, int c, int r) {
        Cell cell = getCellOfSheet(r, c, sheet);
        if (cell == null) {
            return "";
        }
        String temp = getCellContent(sheet, c, r).toString().trim();
        if (temp.endsWith(".0")) {
            return temp.substring(0, temp.length() - 2);
        }
        return temp;
    }
    // datpk getStringconten tu Row

    public static String getCellStrContentRow(int c, Row row) {
        Cell cell = getCellOfSheetRow(c, row);
        if (cell == null) {
            return "";
        }
        String temp = getCellContentRow(c, row).toString().trim();
        if (temp.endsWith(".0")) {
            return temp.substring(0, temp.length() - 2);
        }
        return temp;
    }

    /**
     * method to create validation from array String.But String do not exceed
     * 255 characters
     *
     * @param arrValidate * ThuanNHT
     */
    public void createDropDownlistValidateFromArr(Sheet sheet, String[] arrValidate, int firstRow, int lastRow, int firstCol, int lastCol) {
        CellRangeAddressList addressList = new CellRangeAddressList(
                firstRow, lastRow, firstCol, lastCol);
        DVConstraint dvConstraint = DVConstraint.createExplicitListConstraint(arrValidate);
        HSSFDataValidation dataValidation = new HSSFDataValidation(addressList, dvConstraint);
        dataValidation.setSuppressDropDownArrow(false);
        HSSFSheet sh = (HSSFSheet) sheet;
        sh.addValidationData(dataValidation);
    }

    /**
     * Method to create validation from spread sheet via range
     *
     * @param range
     * @param firstRow
     * @param lastRow
     * @param firstCol
     * @param lastCol * ThuanNHT
     */
    public void createDropDownListValidateFromSpreadSheet(String range, int firstRow, int lastRow, int firstCol, int lastCol, Sheet shet) {
        Name namedRange = workbook.createName();
        Random rd = new Random();
        String refName = ("List" + rd.nextInt()).toString().replace("-", "");
        namedRange.setNameName(refName);
//        namedRange.setRefersToFormula("'Sheet1'!$A$1:$A$3");
        namedRange.setRefersToFormula(range);
        DVConstraint dvConstraint = DVConstraint.createFormulaListConstraint(refName);
        CellRangeAddressList addressList = new CellRangeAddressList(
                firstRow, lastRow, firstCol, lastCol);
        HSSFDataValidation dataValidation = new HSSFDataValidation(addressList, dvConstraint);
        dataValidation.setSuppressDropDownArrow(false);
        HSSFSheet sh = (HSSFSheet) shet;
        sh.addValidationData(dataValidation);
    }

    public void createDropDownListValidateFromSpreadSheet(String sheetName, String columnRangeName,
            int rowRangeStart, int rowRangeEnd, int firstRow, int lastRow, int firstCol, int lastCol, Sheet shet) {
        String range = "'" + sheetName + "'!$" + columnRangeName + "$" + rowRangeStart + ":" + "$" + columnRangeName + "$" + rowRangeEnd;
        Name namedRange = workbook.createName();
        Random rd = new Random();
        String refName = ("List" + rd.nextInt()).toString().replace("-", "");
        namedRange.setNameName(refName);
//        namedRange.setRefersToFormula("'Sheet1'!$A$1:$A$3");
        namedRange.setRefersToFormula(range);
        DVConstraint dvConstraint = DVConstraint.createFormulaListConstraint(refName);
        CellRangeAddressList addressList = new CellRangeAddressList(
                firstRow, lastRow, firstCol, lastCol);
        HSSFDataValidation dataValidation = new HSSFDataValidation(addressList, dvConstraint);
        dataValidation.setSuppressDropDownArrow(false);
        HSSFSheet sh = (HSSFSheet) shet;
        sh.addValidationData(dataValidation);
    }

    public Sheet getSheetAt(int pos) {
        return workbook.getSheetAt(pos);
    }

    public Sheet getSheet(String name) {
        return workbook.getSheet(name);
    }

    /**
     * Method to read an excel file
     *
     * @param filePathName
     * @return * ThuanNHT
     */
    public Workbook readFileExcel(String filePathName) {
        InputStream inp = null;
        try {
            inp = new FileInputStream(filePathName);
            workbook = WorkbookFactory.create(inp);
        } catch (FileNotFoundException ex) {
            logger.error(ex);
        } catch (Exception ex) {
            logger.error(ex);
        } finally {
            try {
                inp.close();
            } catch (IOException ex) {
                logger.error(ex);
            }
        }
        return workbook;
    }

    /**
     *  * ThuanNHT
     *
     * @param r
     * @param c
     * @param sheet
     * @return
     */
    public static Cell getCellOfSheet(int r, int c, Sheet sheet) {
        try {
            Row row = sheet.getRow(r);
            if (row == null) {
                return null;
            }
            return row.getCell(c);
        } catch (Exception ex) {
            return null;
        }
    }

    /**
     * set style for cell
     *
     * @param cell
     * @param halign
     * @param valign
     * @param border
     * @param borderColor
     */
    public void setCellStyle(Cell cell, short halign, short valign, short border, short borderColor, int fontHeight) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setFontHeightInPoints((short) fontHeight);
        font.setFontName("Times New Roman");
        style.setAlignment(halign);
        style.setVerticalAlignment(valign);
        style.setBorderBottom(border);
        style.setBottomBorderColor(borderColor);
        style.setBorderLeft(border);
        style.setLeftBorderColor(borderColor);
        style.setBorderRight(border);
        style.setRightBorderColor(borderColor);
        style.setBorderTop(border);
        style.setTopBorderColor(borderColor);
        style.setFont(font);
        cell.setCellStyle(style);
    }

    /**
     * Method to draw an image on excel file
     *
     * @param imgSrc
     * @param sheet
     * @param colCorner
     * @param rowCorner
     * @throws IOException
     */
    public void drawImageOnSheet(String imgSrc, Sheet sheet, int colCorner, int rowCorner) throws IOException {
        InputStream is = null;
        try {
            is = new FileInputStream(imgSrc);
            byte[] bytes = IOUtils.toByteArray(is);
            int pictureIdx = workbook.addPicture(bytes, Workbook.PICTURE_TYPE_JPEG);
            if (imgSrc.endsWith(".jpg") || imgSrc.endsWith(".JPG") || imgSrc.endsWith(".jpeg") || imgSrc.endsWith(".JPEG")) {
                pictureIdx = workbook.addPicture(bytes, Workbook.PICTURE_TYPE_JPEG);
            } else if (imgSrc.endsWith(".png") || imgSrc.endsWith(".PNG")) {
                pictureIdx = workbook.addPicture(bytes, Workbook.PICTURE_TYPE_PNG);
            }

            CreationHelper helper = workbook.getCreationHelper();
            // Create the drawing patriarch.  This is the top level container for all shapes.
            Drawing drawing = sheet.createDrawingPatriarch();
            //add a picture shape
            ClientAnchor anchor = helper.createClientAnchor();
            //set top-left corner of the picture,
            //subsequent call of Picture#resize() will operate relative to it
            anchor.setCol1(colCorner);
            anchor.setRow1(rowCorner);
            Picture pict = drawing.createPicture(anchor, pictureIdx);

            //auto-size picture relative to its top-left corner
            pict.resize();
        } catch (Exception ex) {
            logger.error(ex);
        } finally {
            if (is != null) {
                is.close();
            }
        }

    }

    public void setStandardCellStyle(Cell cell) {
        setCellStyle(cell, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, CellStyle.BORDER_THIN, IndexedColors.BLACK.getIndex(), 12);
    }

    // datpk: lay cell tu Row
    public static Cell getCellOfSheetRow(int c, Row row) {
        try {
            if (row == null) {
                return null;
            }
            return row.getCell(c);
        } catch (Exception ex) {
            return null;
        }
    }

    public static Boolean compareToLong(String str, Long t) {
        Boolean check = false;
        try {
            Double d = Double.valueOf(str);
            Long l = d.longValue();
            if (l.equals(t)) {
                check = true;
            }
        } catch (Exception ex) {
            check = false;
        }
        return check;
    }

    public static Boolean doubleIsLong(String str) {
        Boolean check = false;
        try {
            Double d = Double.valueOf(str);
            Long l = d.longValue();
            if (d.equals(Double.valueOf(l))) {
                check = true;
            }
        } catch (Exception ex) {
            check = false;
        }
        return check;
    }

    public static void main(String[] arg) {
        try {
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    //sonnh26_start
    public CellStyle getCsLeftBoder() {
        return cellStyle(CellStyle.ALIGN_LEFT, CellStyle.VERTICAL_BOTTOM, CellStyle.BORDER_THIN,
                IndexedColors.BLACK.getIndex(), -1, 11, -1, false);
    }

    public CellStyle getCsRightBoder() {
        return cellStyle(CellStyle.ALIGN_RIGHT, CellStyle.VERTICAL_BOTTOM, CellStyle.BORDER_THIN,
                IndexedColors.BLACK.getIndex(), -1, 11, -1, true);
    }

    public CellStyle getCsRightBoderFloat() {
        return cellStyleFloat(CellStyle.ALIGN_RIGHT, CellStyle.VERTICAL_BOTTOM, CellStyle.BORDER_THIN,
                IndexedColors.BLACK.getIndex(), -1, 11, -1, true);
    }

    public CellStyle getCsCenterBoder() {
        return cellStyle(CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_BOTTOM, CellStyle.BORDER_THIN,
                IndexedColors.BLACK.getIndex(), -1, 11, -1, true);
    }

    public CellStyle getCsCenterNoBoder() {
        return cellStyle(CellStyle.ALIGN_RIGHT, CellStyle.VERTICAL_BOTTOM, CellStyle.NO_FILL,
                IndexedColors.BLACK.getIndex(), -1, 11, -1, true);
    }

    public CellStyle getCsCenterNoboderBoldweight() {
        return cellStyle(CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_BOTTOM, CellStyle.NO_FILL,
                IndexedColors.BLACK.getIndex(), -1, 11, XSSFFont.BOLDWEIGHT_BOLD, false);
    }

    public CellStyle getCsColHeader() {
        return cellStyle(CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, CellStyle.BORDER_THIN,
                IndexedColors.BLACK.getIndex(), IndexedColors.SKY_BLUE.getIndex(),
                11, XSSFFont.BOLDWEIGHT_BOLD, true);
    }

    public CellStyle getCsTitle() {
        return cellStyle(CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_BOTTOM, CellStyle.NO_FILL,
                IndexedColors.BLACK.getIndex(), -1, 18, XSSFFont.BOLDWEIGHT_BOLD, true);
    }

    public CellStyle getCsSubTitle() {
        return cellStyle(CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_BOTTOM, CellStyle.NO_FILL,
                IndexedColors.BLACK.getIndex(), -1, 13, XSSFFont.BOLDWEIGHT_BOLD, true);
    }

    public CellStyle cellStyle(short halign, short valign, short border,
            short borderColor, int foregroundColor,
            int fontHeight, int fontWeight, boolean wraptext) {
        CellStyle style = null;
        Font font = null;
        if (workbook != null) {
            style = workbook.createCellStyle();
            font = workbook.createFont();

        } else {
            style = SXSSFworkbook.createCellStyle();
            font = SXSSFworkbook.createFont();
        }
        font.setFontHeightInPoints((short) fontHeight);
        font.setFontName("Times New Roman");
        if (fontWeight != -1) {
            font.setBoldweight((short) fontWeight);
        }
        style.setAlignment(halign);
        style.setVerticalAlignment(valign);
        style.setBorderBottom(border);
        style.setBottomBorderColor(borderColor);
        style.setBorderLeft(border);
        style.setLeftBorderColor(borderColor);
        style.setBorderRight(border);
        style.setRightBorderColor(borderColor);
        style.setBorderTop(border);
        style.setTopBorderColor(borderColor);
        style.setFont(font);
        if (foregroundColor != -1) {
            style.setFillForegroundColor((short) foregroundColor);
            style.setFillPattern(CellStyle.SOLID_FOREGROUND);
        }
        style.setWrapText(wraptext);
        return style;
    }

    public CellStyle cellStyleFloat(short halign, short valign, short border,
            short borderColor, int foregroundColor,
            int fontHeight, int fontWeight, boolean wraptext) {
        CellStyle style = null;
        Font font = null;
        DataFormat dataFormat = null;
        if (workbook != null) {
            style = workbook.createCellStyle();
            font = workbook.createFont();
            dataFormat = workbook.createDataFormat();
        } else {
            style = SXSSFworkbook.createCellStyle();
            font = SXSSFworkbook.createFont();
            dataFormat = SXSSFworkbook.createDataFormat();
        }
        style.setDataFormat(dataFormat.getFormat("#,##0"));
        font.setFontHeightInPoints((short) fontHeight);
        font.setFontName("Times New Roman");
        if (fontWeight != -1) {
            font.setBoldweight((short) fontWeight);
        }
        style.setAlignment(halign);
        style.setVerticalAlignment(valign);
        style.setBorderBottom(border);
        style.setBottomBorderColor(borderColor);
        style.setBorderLeft(border);
        style.setLeftBorderColor(borderColor);
        style.setBorderRight(border);
        style.setRightBorderColor(borderColor);
        style.setBorderTop(border);
        style.setTopBorderColor(borderColor);
        style.setFont(font);
        if (foregroundColor != -1) {
            style.setFillForegroundColor((short) foregroundColor);
            style.setFillPattern(CellStyle.SOLID_FOREGROUND);
        }
        style.setWrapText(wraptext);
        return style;
    }

    public void creatTemplate(Sheet sheet, String title) {
//        CellStyle cs1 = cellStyle(CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_BOTTOM, CellStyle.NO_FILL,
//                IndexedColors.BLACK.getIndex(), -1, 13, XSSFFont.BOLDWEIGHT_BOLD, true);
//
//        CellStyle cs2 = cellStyle(CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_BOTTOM, CellStyle.NO_FILL,
//                IndexedColors.BLACK.getIndex(), -1, 11, XSSFFont.BOLDWEIGHT_BOLD, true);
//
//        CellStyle cs3 = cellStyle(CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_BOTTOM, CellStyle.NO_FILL,
//                IndexedColors.BLACK.getIndex(), -1, 18, XSSFFont.BOLDWEIGHT_BOLD, true);

        String label1 = "TẬP ĐOÀN VIỄN THÔNG QUÂN ĐỘI";
        String label2 = "TỔNG CÔNG TY VIỄN THÔNG VIETTEL";
        String label3 = "CỘNG HOÀ XÃ HỘI CHỦ NGHĨA VIỆT NAM";
        String label4 = "Độc Lập - Tự Do - Hạnh Phúc";

        createCellObject(sheet, 0, 0, label1, getCsSubTitle());
        createCellObject(sheet, 0, 1, label2, getCsCenterNoboderBoldweight());
        createCellObject(sheet, 5, 0, label3, getCsSubTitle());
        createCellObject(sheet, 5, 1, label4, getCsCenterNoboderBoldweight());
        createCellObject(sheet, 1, 4, title, getCsTitle());

        ExcelWriterUtils.mergeCells(sheet, 0, 0, 0, 2);
        ExcelWriterUtils.mergeCells(sheet, 1, 1, 0, 2);
        ExcelWriterUtils.mergeCells(sheet, 0, 0, 5, 8);
        ExcelWriterUtils.mergeCells(sheet, 1, 1, 5, 8);
        ExcelWriterUtils.mergeCells(sheet, 4, 4, 1, 6);

        setRowHeight(sheet, 4, 630);
    }

    public void setRowHeight(Sheet sheet, int rowIndex, int rowHeight) {
        Row row = sheet.getRow(rowIndex);
        if (row == null) {
            row = sheet.createRow(rowIndex);
        }
        row.setHeight((short) rowHeight);
    }

    public Cell createCellObject(Sheet sheet, int c, int r, Object obj, CellStyle cs) {
        Row row = sheet.getRow(r);
        if (row == null) {
            row = sheet.createRow(r);
        }
        // Create a cell and put a value in it.
        Cell cell = row.createCell(c);
        if (obj != null) {
            if (obj instanceof String) {
                cell.setCellType(Cell.CELL_TYPE_STRING);
                cell.setCellValue((String) obj);
            } else if (obj instanceof Double) {
                cell.setCellType(Cell.CELL_TYPE_NUMERIC);
                cell.setCellValue((Double) obj);
            } else if (obj instanceof Float) {
                cell.setCellType(Cell.CELL_TYPE_NUMERIC);
//                obj = ExcelWriterUtils.getValueFormat(new BigDecimal(String.valueOf(obj)), "float");
//                cell.setCellValue(((BigDecimal) obj).doubleValue());
            } else if (obj instanceof Long) {
                cell.setCellType(Cell.CELL_TYPE_NUMERIC);
                cell.setCellValue((Long) obj);
            } else if (obj instanceof Integer) {
                cell.setCellType(Cell.CELL_TYPE_NUMERIC);
                cell.setCellValue((Integer) obj);
            } else if (obj instanceof Date) {
                cell.setCellType(Cell.CELL_TYPE_NUMERIC);
                cell.setCellValue((Date) obj);
            }
        }

        // cellStyle
        if (cs != null) {
            cell.setCellStyle(cs);
            if (obj instanceof Date) {
                cs.setDataFormat(sheet.getWorkbook().
                        createDataFormat().getFormat("dd/mm/yyyy hh:mm:ss"));
            }
//            if (obj instanceof Double) {
//                Double data = Double.valueOf(obj.toString());
//                if (data % 1 != 0) {
//                    cs.setDataFormat(sheet.getWorkbook().
//                            createDataFormat().getFormat("#.#####"));
//                } else {
//                    cs.setDataFormat(sheet.getWorkbook().
//                            createDataFormat().getFormat("#"));
//                }
//            }
        }
        return cell;
    }

    public Cell createCell(Sheet sheet, int col, int iRowIndex, Object value, CellStyle cellStyle) {
        Row row = null;
        Cell cell = null;
        try {
            row = sheet.getRow(iRowIndex);
            if (row == null) {
                row = sheet.createRow(iRowIndex);
            }
            cell = row.getCell(col);
            if (cell == null) {
                cell = row.createCell(col);
            }
            if (value instanceof BigDecimal) {
                cell.setCellValue(((BigDecimal) value).doubleValue());
            } else if ((value instanceof Float)) {
                cell.setCellValue(((Float) value));
            } else if ((value instanceof Double)) {
                cell.setCellValue(((Double) value));
            } else if ((value instanceof Integer)) {
                cell.setCellValue(((Integer) value));
            } else if ((value instanceof Long)) {
                cell.setCellValue(((Long) value));
            } else {
                cell.setCellValue(value == null ? "" : value.toString());
            }
            cell.setCellStyle(cellStyle);
        } catch (Exception ex) {
        }
        return cell;
    }

    public Cell createCellFloat(Sheet sheet, int c, int r, Double cellValue) {
        Row row = sheet.getRow(r);
        if (row == null) {
            row = sheet.createRow(r);
        }
        Cell cell = row.createCell(c);
        try {
            if (cellValue != null) {
                CellStyle cellStyle = sheet.getWorkbook().createCellStyle();
                DataFormat dataFormat = sheet.getWorkbook().createDataFormat();
                cellStyle.setDataFormat(dataFormat.getFormat("#,##0"));
                cell.setCellType(Cell.CELL_TYPE_NUMERIC);
                cell.setCellValue(cellValue);
                cell.setCellStyle(cellStyle);
            } else {
                cell.setCellValue("");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return cell;
    }

//    public static Object getValueFormat(Object obj, String format) {
//        if (obj == null) {
//            return "";
//        }
//        if (format == null) {
//            if ((obj instanceof String)) {
//                return (String) obj;
//            }
//            return obj.toString();
//        }
//        if ((format.equalsIgnoreCase("date"))
//                && ((obj instanceof Date))) {
//            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
//            return sdf.format((Date) obj);
//        }
//        if ((format.equalsIgnoreCase("datetime"))
//                && ((obj instanceof Date))) {
//            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
//            return sdf.format((Date) obj);
//        }
//        if ((format.equalsIgnoreCase("integer"))
//                && ((obj instanceof BigDecimal))) {
//            return ((BigDecimal) obj).setScale(0, 4);
//        }
//        if ((format.equalsIgnoreCase("float"))
//                && ((obj instanceof BigDecimal))) {
//            return ((BigDecimal) obj).setScale(2, 4);
//        }
//        if ((format.equalsIgnoreCase("number"))
//                && ((obj instanceof BigDecimal))) {
//            return ((BigDecimal) obj).setScale(5, 4);
//        }
//        return "";
//    }
    public void setSheetSelectedSXSSF(int posSheet) {
        try {
            SXSSFworkbook.setActiveSheet(posSheet);
        } catch (IllegalArgumentException ex) {
            SXSSFworkbook.setActiveSheet(0);
        }
    }

    public void saveToFileExcelSXSSF(String filePathName) {
        try {
            fileOut = new FileOutputStream(filePathName);
            SXSSFworkbook.write(fileOut);
        } catch (Exception ex) {
            logger.error(ex);
        } finally {
            try {
                fileOut.close();
                SXSSFworkbook = null;
            } catch (IOException ex) {
                logger.error(ex);
            }
        }
    }

    //sonnh26_End
    public Workbook getWorkbook() {
        return workbook;
    }

    public void setWorkbook(Workbook workbook) {
        this.workbook = workbook;
    }
}
