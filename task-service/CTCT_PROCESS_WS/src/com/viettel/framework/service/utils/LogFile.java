/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.framework.service.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author qlmvt_minhht1
 */
public class LogFile {

    private String logServerID;
    private boolean useLogFile;
    private FileWriter output;

    public LogFile(String logServerID, boolean useLogFile) throws IOException {
        this.logServerID = logServerID;
        this.useLogFile = useLogFile;
    }

    public void setUseLogFile(boolean useLogFile) {
        this.useLogFile = useLogFile;
    }

    public boolean getUseLogFile() {
        return useLogFile;
    }

    public void print(String message) throws IOException {
        if (useLogFile) {
            try {
                DateFormat dfday = new SimpleDateFormat("ddMMyyyy");
                File file = new File("../data/" + logServerID + "_" + dfday.format(new Date()) + ".txt");
                output = new FileWriter(file, true);
                output.write(message);
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                output.flush();
                output.close();
            }
        }
    }
    public void print(String message, String fileName) throws IOException {
        if (useLogFile) {
            try {
                File file = new File(fileName);
                output = new FileWriter(file, true);
                output.write(message);
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                output.flush();
                output.close();
            }
        }
    }

    public void println(String message) throws IOException {
        if (useLogFile) {
            try {
                String endLine = System.getProperty("line.separator");
                DateFormat dfday = new SimpleDateFormat("ddMMyyyy");
                File file = new File("../data/" + logServerID + "_" + dfday.format(new Date()) + ".txt");
                output = new FileWriter(file, true);
                output.write(message);
                output.write(endLine);
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                output.flush();
                output.close();
            }
        }
    }

    public void println(String message, String fileName) throws IOException {
        if (useLogFile) {
            try {
                String endLine = System.getProperty("line.separator");
                File file = new File(fileName);
                output = new FileWriter(file, true);
                output.write(message);
                output.write(endLine);
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                output.flush();
                output.close();
            }
        }
    }


    public void printlnEndLine() throws IOException {
        if (useLogFile) {
            try {
                String endLine = System.getProperty("line.separator");
                DateFormat dfday = new SimpleDateFormat("ddMMyyyy");
                File file = new File("../data/" + logServerID + "_" + dfday.format(new Date()) + ".txt");
                output = new FileWriter(file, true);
                output.write(endLine);
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                output.flush();
                output.close();
            }
        }
    }

    public void printlnEndLine(String fileName) throws IOException {
        if (useLogFile) {
            try {
                String endLine = System.getProperty("line.separator");
                DateFormat dfday = new SimpleDateFormat("ddMMyyyy");
                File file = new File(fileName);
                output = new FileWriter(file, true);
                output.write(endLine);
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                output.flush();
                output.close();
            }
        }
    }
}
