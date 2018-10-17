package com.viettel.framework.client.ftp;

import java.io.IOException;
import org.apache.commons.net.ftp.FTPClient;
import com.viettel.framework.service.common.LogServer;
import com.viettel.framework.service.utils.Day;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Timestamp;
import org.apache.commons.net.ftp.FTPFile;

/**
 *
 * @author qlmvt_KhiemVK
 */
public class FtpClient {
    private FTPClient ftpClient = null;

    public void Connect(LogServer logServer) throws Exception{
        try {
            ftpClient = new FTPClient();
            ftpClient.connect(logServer.getIp());
            boolean bStatus = ftpClient.login(logServer.getUsername(), logServer.getPassword());
            if (!bStatus) {
                String err = "Fail to login ftp server: " + logServer.getIp();                
                throw new Exception(err);
            } 
        } catch (Exception ex) {
            throw ex;
        }
    }

    public void Disconnect(){
        if(ftpClient!=null)
            if(ftpClient.isConnected()){
                try {
                    ftpClient.logout();
                    ftpClient.disconnect();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
    }

    public InputStream getFileStream(String folder, String fileName) throws Exception{
        FTPFile[] files = null;
        if (folder != null && folder.length() > 0) {
            files = ftpClient.listFiles(folder);
        } else {
            files = ftpClient.listFiles();
        }
        if (null == files || files.length < 1)
            throw new Exception("File not found");
        else 
            for (FTPFile file : files)
                if(file.isFile())
                    if(file.getName().equalsIgnoreCase(fileName)){
                        InputStream input = ftpClient.retrieveFileStream((folder != null && folder.length() > 0)? folder + "/" + fileName:fileName);
                        return input;
                    }
        return null;
    }

    public String makeDirectoryTreeTime(LogServer logServer, Timestamp startTime) throws Exception{
        String directory = new Day(startTime).format("yyyy/MM/dd");
        if(logServer.getUrl()!=null)
            ftpClient.changeWorkingDirectory("/" + logServer.getUrl());
        ftpClient.makeDirectory(directory);
        return "/" + logServer.getUrl() + "/" + directory;
    }

    public void makeFullDirectory(LogServer logServer, String directory) throws Exception{
        if(logServer.getUrl()!=null)
            ftpClient.changeWorkingDirectory("/" + logServer.getUrl());
        if(directory!=null){
            String[] paths = directory.split("\\/");
            for(String folder : paths){
                if(folder.length()>0){
                    ftpClient.makeDirectory(folder);
                    ftpClient.changeWorkingDirectory(folder);
                }
            }
        }
    }

    public void uploadFile(File f, String directory) throws Exception{
        if(directory!=null)
            ftpClient.changeWorkingDirectory(directory);
        if(f.exists() && f.isFile()){
            ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);            
            InputStream inputStream = new FileInputStream(f);
            ftpClient.storeFile(f.getName(), inputStream);            
            inputStream.close();
        }
    }

    public FTPClient getFtpClient() {
        return ftpClient;
    }
}
