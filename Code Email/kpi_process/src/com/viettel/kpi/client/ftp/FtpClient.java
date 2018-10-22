package com.viettel.kpi.client.ftp;

import com.viettel.kpi.getDataSql.common.CommonLogServer;
import com.viettel.kpi.service.common.Day;
import com.viettel.kpi.service.common.LogServer;
import com.viettel.passprotector.PassProtector;
import java.io.IOException;
import org.apache.commons.net.ftp.FTPClient;
import java.io.*;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.log4j.Logger;

/**
 *
 * @author qlmvt_KhiemVK
 */
public class FtpClient {

    private FTPClient ftpClient = null;

    public void Connect(LogServer logServer) throws Exception {
        try {
            ftpClient = new FTPClient();
            ftpClient.connect(logServer.getIp());
            String password = PassProtector.decrypt(logServer.getPassword(), logServer.getIp());
            boolean bStatus = ftpClient.login(logServer.getUsername(), password);
            if (!bStatus) {
                String err = "Fail to login ftp server: " + logServer.getIp();
                throw new Exception(err);
            }
        } catch (Exception ex) {
            throw ex;
        }
    }

    public void Connect(CommonLogServer logServer) throws Exception {
        try {
            ftpClient = new FTPClient();
            int port = 21;
            if(logServer.getPort() != null){
                port = logServer.getPort();
            }
            ftpClient.connect(logServer.getIp(), port);
            String password = PassProtector.decrypt(logServer.getPassword(), logServer.getIp());
            boolean bStatus = ftpClient.login(logServer.getUserName(), password);
            if (!bStatus) {
                String err = "Fail to login ftp server: " + logServer.getIp();
                throw new Exception(err);
            }
            //ftpClient.enterLocalPassiveMode();


        } catch (Exception ex) {
            throw ex;
        }
    }

    public void Disconnect() {
        if (ftpClient != null) {
            if (ftpClient.isConnected()) {
                try {
                    ftpClient.logout();
                    ftpClient.disconnect();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    public void DisconnectPool(FTPClient fTPClient) {
        if (fTPClient != null) {
            if (fTPClient.isConnected()) {
                try {
                    fTPClient.logout();
                    fTPClient.disconnect();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    public InputStream getFileStream(String folder, String fileName, Logger logger,
            String local, Timestamp timestamp, String localPathFormat) throws Exception {
        FTPFile[] files = null;
        ftpClient.enterLocalPassiveMode();
//        logger.info("File Path:" + folder + fileName);
        if (folder != null && folder.length() > 0) {
            try {
                files = ftpClient.listFiles(folder);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            files = ftpClient.listFiles();
        }
        if (null == files || files.length < 1) {
            logger.info("Khong lay duoc file : " + fileName);
            return null;
        } else {
//            logger.info("so luong file trong folder: "+files.length);
            for (FTPFile file : files) {
                if (file.isFile()) {
                    if (file.getName().toUpperCase().contains(fileName.toUpperCase())) {
                        InputStream inputStream = ftpClient.retrieveFileStream(folder + "/" + fileName);
                        if (local != null && !local.equals("")) {
////                            writeFileLocal(local,fileName,input);
                            if (localPathFormat != null && !"".equals(localPathFormat.trim())) {
                                local = local.replace(localPathFormat, date2String(new Date(timestamp.getTime()), localPathFormat));
                            } else {
                                local = local.replace("yyyyMMdd", date2String(new Date(timestamp.getTime()), "yyyyMMdd"));
                            }
                            inputStream = writeFileLocal(local, fileName, inputStream);
                            logger.info("Da upload file : " + file.getName() + " len : " + local);
                        }
                        logger.info("Lay duoc fileName: " + file.getName());
                        return inputStream;
                    }
                }
            }
        }
        logger.info("Khong lay duoc file : " + fileName);
        return null;
    }

    public String date2String(Date date, String format) {
        DateFormat df = new SimpleDateFormat(format);
        return df.format(date);
    }

    public InputStream getFileStream(String folder, String fileName, FTPClient client, Logger logger) throws Exception {
        FTPFile[] files = null;
        if (folder != null && folder.length() > 0) {
            logger.info(folder);
            files = client.listFiles(folder);
        } else {
            files = client.listFiles();
        }
        if (null == files || files.length < 1) {
            logger.info("no file");
            return null;
        } else {
            logger.info("so luong file trong folder: " + files.length);
            for (FTPFile file : files) {
                if (file.isFile()) {
                    logger.info("fileName: " + file.getName());
                    if (file.getName().toUpperCase().contains(fileName.toUpperCase())) {
                        InputStream input = client.retrieveFileStream(folder + "/" + fileName);
                        return input;
                    }
                }
            }
        }
        return null;
    }

    //sonnh26_R??_28/10/2013_Start
    public List<Map<String, InputStream>> getFileStream(String folder, String fileName, Logger logger,
            String local, Timestamp timestamp, String localPathFormat, boolean isMutilFile, CommonLogServer logServer) throws Exception {
        List<Map<String, InputStream>> lstInputStream = new ArrayList<Map<String, InputStream>>();

        FTPFile[] files = null;
        ftpClient.enterLocalPassiveMode();
        if (folder != null && folder.length() > 0) {
            try {
                files = ftpClient.listFiles(folder + "/" + fileName);
            } catch (Exception e) {
                // e.printStackTrace();
            }
        } else {
            files = ftpClient.listFiles();
        }


        if (null == files || files.length < 1) {
            logger.info("Khong co file trong thu muc : " + folder);            
            return null;
        } else {
            logger.info("so luong file trong folder: " + files.length);
            for (FTPFile file : files) {
                Map<String, InputStream> mapInputStream = new HashMap<String, InputStream>();
                InputStream inputStream = null;
                try {
                    if (file.isFile()) {
                        ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
//                        logger.info("vuongnd5 start get file: " + new Date() + " file: " + folder + "/" + cutFileName(file.getName()));
                        ftpClient.enterLocalPassiveMode();
//                        logger.info("vuongnd5 enter Local Passsive Mode");
                        InputStream ftpInputStream = null;
                        ftpInputStream = ftpClient.retrieveFileStream(folder + "/" + cutFileName(file.getName()));
//                        logger.info("Reply String: " + ftpClient.getReplyStrings());
//                        logger.info("Reply String: " + ftpClient.getReplyString());
//                        logger.info("vuongnd5 end get file: " + new Date() + " ftpInputStream:" + ftpInputStream);
                        try {
                            inputStream = copyInputStream(ftpInputStream);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        } finally {
                            if (ftpInputStream != null) {
                                ftpInputStream.close();
                                ftpClient.completePendingCommand();
                            }
                        }
                        // logger.info("Local" + local);
                        if (local != null && !local.equals("")) {
                            if (localPathFormat != null && !"".equals(localPathFormat.trim())) {
                                local = local.replace(localPathFormat, date2String(new Date(timestamp.getTime()), localPathFormat));
                            } else {
                                local = local.replace("yyyyMMdd", date2String(new Date(timestamp.getTime()), "yyyyMMdd"));
                            }
                            writeFileLocal(local, fileName, inputStream);
                            logger.info("Da upload file : " + file.getName() + " len : " + local);
                        }                        
                        if (inputStream != null) {
                            logger.info("Lay duoc fileName: " + file.getName());
                            // doi ve kieu map gom file name v� InputStrem
                            mapInputStream.put(cutFileName(file.getName()), inputStream);
                            lstInputStream.add(mapInputStream);
                        } else {
                            logger.info("loi khi lay file: " + file.getName());                            
                        }
                    }
                } catch (Exception e) {
                    // e.printStackTrace();
                }
            }
            if (lstInputStream.isEmpty()) {
                logger.info("Khong lay duoc file : " + fileName);                
                return null;
            } else {
                return lstInputStream;
            }
        }
    }
    //sonnh26_R??_28/10/2013_End

    public String makeDirectoryTreeTime(LogServer logServer, Timestamp startTime) throws Exception {
        String directory = new Day(startTime).format("yyyy/MM/dd");
        if (logServer.getUrl() != null) {
            ftpClient.changeWorkingDirectory("/" + logServer.getUrl());
        }
        ftpClient.makeDirectory(directory);
        return "/" + logServer.getUrl() + "/" + directory;
    }

    public void makeFullDirectory(LogServer logServer, String directory) throws Exception {
        if (logServer.getUrl() != null) {
            ftpClient.changeWorkingDirectory("/" + logServer.getUrl());
        }
        if (directory != null) {
            String[] paths = directory.split("\\/");
            for (String folder : paths) {
                if (folder.length() > 0) {
                    ftpClient.makeDirectory(folder);
                    ftpClient.changeWorkingDirectory(folder);
                }
            }
        }
    }

    public void uploadFile(File f, String directory) throws Exception {
        ftpClient.enterLocalPassiveMode();
        if (directory != null) {
            ftpClient.changeWorkingDirectory(directory);
        }
        if (f.exists() && f.isFile()) {
            ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
            InputStream inputStream = new FileInputStream(f);
            ftpClient.storeFile(f.getName(), inputStream);
            inputStream.close();
        }
    }

//    public void uploadFileFtp(File f, String directory) throws Exception {
//        if (directory != null) {
//            ftpClient.changeWorkingDirectory(directory);
//        }
//        if (f.exists() && f.isFile()) {
//            ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
//            InputStream inputStream = new FileInputStream(f);
//            ftpClient.storeFile(f.getName(), inputStream); 
//        }
//    }
    public void deleteFile(String directory, Integer timeDuration) throws IOException {
//        if (directory != null) {
//            ftpClient.changeWorkingDirectory(directory);
//        }
        FTPFile[] files = null;
        ftpClient.enterLocalPassiveMode();
        if (directory != null && directory.length() > 0) {
            files = ftpClient.listFiles(directory);
        } else {
            files = ftpClient.listFiles();
        }
        if (null != files || files.length >= 1) {
            for (FTPFile file : files) {
                if (file.isFile()) {
                    Calendar creatFile = file.getTimestamp();
                    Calendar now = Calendar.getInstance();
                    long timeDuaCreatFile = now.getTimeInMillis() - creatFile.getTimeInMillis();
                    if (timeDuaCreatFile > timeDuration * 24 * 60 * 60 * 1000L) {
                        try {
                            ftpClient.deleteFile(directory + "/" + file.getName());
                        } catch (Exception e) {
                            System.out.println("Khong xoa duoc file: " + file.getName());
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }
    
    public InputStream writeFileLocal(String fileLocal, String fileName, InputStream inputStream) throws Exception {
        try {
            createFolder(fileLocal);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len;
            while ((len = inputStream.read(buffer)) > -1) {
                baos.write(buffer, 0, len);
            }
            baos.flush();

            OutputStream out = new FileOutputStream(new File(fileLocal + "/" + fileName), false);
            InputStream ip = new ByteArrayInputStream(baos.toByteArray());
            int read = 0;
            byte[] bytes = new byte[1024];
            while ((read = ip.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }
            out.flush();
            out.close();
            ip.close();
            ip = new ByteArrayInputStream(baos.toByteArray());
            baos.close();
            return ip;
        } catch (Exception ex) {
            throw ex;
        }
    }

    public void createFolder(String folder) {
        File f = new File(folder);
        if (!f.exists()) {
            f.mkdir();
        }
    }

    public FTPClient getFtpClient() {
        return ftpClient;
    }

    public InputStream copyInputStream(InputStream input) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len;
        while ((len = input.read(buffer)) > -1) {
            baos.write(buffer, 0, len);
        }
        baos.flush();
        return new ByteArrayInputStream(baos.toByteArray());
    }
    
    
    public static String cutFileName(String fileName) {
        int index = 0;
        index = fileName.lastIndexOf('/');
        if(index <0) {
            return fileName;
        } else {
            return fileName.substring(index+1);
        }
    }        
}
