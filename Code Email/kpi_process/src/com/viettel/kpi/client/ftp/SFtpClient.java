package com.viettel.kpi.client.ftp;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.viettel.kpi.getDataSql.common.CommonLogServer;
import com.viettel.passprotector.PassProtector;
import java.io.*;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import org.apache.log4j.Logger;

/**
 *
 * @author qlmvt_KhiemVK
 */
public class SFtpClient {

    String SFTPHOST = "";
    int SFTPPORT = 22;
    String SFTPUSER = "";
    String SFTPPASS = "";
    String SFTPWORKINGDIR = "";
    boolean checkFoder = true;
    boolean checkFile = true;
    boolean checkFileName = true;
    Session session = null;
    ChannelSftp sftpChannel = null;

    public ChannelSftp getSftpChannel() {
        return sftpChannel;
    }

    public void Connect(CommonLogServer logServer) throws Exception {
        try {
            JSch jsch = new JSch();
            SFTPUSER = logServer.getUserName();
            SFTPHOST = logServer.getIp();
            String password = PassProtector.decrypt(logServer.getPassword(), logServer.getIp());
            SFTPPASS = password;
            session = jsch.getSession(SFTPUSER, SFTPHOST, SFTPPORT);
            {
                session.setPassword(SFTPPASS);

            }
            java.util.Properties config = new java.util.Properties();
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);
            try {
                session.connect();
            } catch (Exception e) {
                checkFoder = false;
            }

            if (!checkFoder) {
                String err = "Fail to login ftp server: " + logServer.getIp();
                throw new Exception(err);
            } else if (checkFoder) {
                Channel channel = session.openChannel("sftp");
                channel.connect();
                sftpChannel = (ChannelSftp) channel;
            }


        } catch (Exception ex) {
            throw ex;
        }
    }

    public void Disconnect() {
        if (sftpChannel != null && session != null) {

            try {
                sftpChannel.exit();
                session.disconnect();
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }
    }

    public List<Map<String, InputStream>> getFileStream(String folder, String fileName, Logger logger,
            String local, Timestamp timestamp, String localPathFormat) throws Exception {

        Vector<ChannelSftp.LsEntry> files = new Vector<ChannelSftp.LsEntry>();
        List<Map<String, InputStream>> lstInputStream = new ArrayList<Map<String, InputStream>>();
        Map<String, InputStream> mapInputStream = new HashMap<String, InputStream>();
        
        if (folder != null && folder.length() > 0) {
            SFTPWORKINGDIR = folder;
            try {
                sftpChannel.cd(SFTPWORKINGDIR);

            } catch (Exception e) {
                checkFile = false;
                e.printStackTrace();
            }


            if (!checkFile) {
                logger.info("Duong dan bi sai : ");
                return null;
            } else {
                try {
                    //  inputStream = sftpChannel.get(fileName);
                    files = sftpChannel.ls(fileName);


                } catch (Exception e) {
                    checkFileName = false;
                    logger.info("Khong lay duoc file : " + fileName);
                    return null;
                }
                if (checkFileName && files.size() > 0) {
                    for (ChannelSftp.LsEntry file : files) {
			mapInputStream = new HashMap<String, InputStream>();
                        InputStream inputStream = sftpChannel.get(file.getFilename());
                        if (local != null && !local.equals("")) {
                            if (localPathFormat != null && !"".equals(localPathFormat.trim())) {
                                local = local.replace(localPathFormat, date2String(new Date(timestamp.getTime()), localPathFormat));
                            } else {
                                local = local.replace("yyyyMMdd", date2String(new Date(timestamp.getTime()), "yyyyMMdd"));
                            }
                            inputStream = writeFileLocal(local, file.getFilename(), inputStream);

                            logger.info("Da upload file : " + fileName + " len : " + local);
                        }
                         mapInputStream.put(file.getFilename(), inputStream);
                        lstInputStream.add(mapInputStream);
                        logger.info("Lay duoc fileName: " + fileName);
                    }

                    return lstInputStream;
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
}
