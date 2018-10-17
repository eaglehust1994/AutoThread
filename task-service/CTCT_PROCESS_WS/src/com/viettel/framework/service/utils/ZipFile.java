package com.viettel.framework.service.utils;

import java.io.*;
import java.util.zip.*;

/**
 *
 * @author qlmvt_KhiemVK
 */
public class ZipFile {

    //zipFile.ZipFile("C:/aoesync.txt", "C:/outFile.zip");
    public boolean ZipFile(String fileLocal, String zipFile) {
        try {
            ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(zipFile)));
            out.setLevel(Deflater.DEFAULT_COMPRESSION);

            byte[] data = new byte[18024];
            File fIn = new File(fileLocal);
            BufferedInputStream in = new BufferedInputStream(new FileInputStream(fIn));
            int count;
            out.putNextEntry(new ZipEntry(fIn.getName()));
            while ((count = in.read(data)) != -1) {
                out.write(data, 0, count);
            }
            in.close();
            out.flush();
            out.close();
        } catch (Exception ex) {
            return false;
        }
        return true;
    }
}
