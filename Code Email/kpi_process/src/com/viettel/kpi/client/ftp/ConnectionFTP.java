/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.kpi.client.ftp;

import com.jscape.inet.ftp.Ftp;
import com.jscape.inet.scp.Scp;
import java.io.File;

/**
 *
 * @author sonnh26
 */
public class ConnectionFTP {

    Scp scp = null;

    public static File getInputStream(String ip, int port, String username,
            String password, String path, File filenameUpLoad) {
        File file = null;
        Ftp ftp = new Ftp(ip, username, password);
        try {
            ftp.connect();
            ftp.setDir(path);
            ftp.upload(filenameUpLoad);
            ftp.disconnect();
        } catch (Exception e) {
            System.out.println(e);
        }
        return file;
    }

    public File getInputStream(String ip, int port, String username,
            String password, String path, String filenameDownload, String dir) {
        File file = null;
        Ftp ftp = new Ftp(ip, username, password);
        try {
            ftp.connect();
            ftp.setDir(path);
            ftp.setLocalDir(new File(dir));
            file = ftp.download(filenameDownload);
            ftp.disconnect();
        } catch (Exception e) {
            System.out.println(e);
        }
        return file;
    }

    public void disconnect() {
        if (scp != null) {
            scp.disconnect();
        }
    }
}
