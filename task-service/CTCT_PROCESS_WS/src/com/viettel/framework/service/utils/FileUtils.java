/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.framework.service.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 *
 * @author qlmvt_minhht1
 */
public class FileUtils {

    public static String getContent(File f) throws FileNotFoundException, IOException {
        StringBuilder contents = new StringBuilder();
        BufferedReader input = new BufferedReader(new FileReader(f));
        String line = "";
        while ((line = input.readLine()) != null) {
            contents.append(line);
            contents.append(System.getProperty("line.separator"));
        }
        input.close();
        return contents.toString();
    }

    public static String getContent(String fileName) throws FileNotFoundException, IOException {
        File f = new File(fileName);
        StringBuilder contents = new StringBuilder();
        BufferedReader input = new BufferedReader(new FileReader(f));
        String line = "";
        while ((line = input.readLine()) != null) {
            contents.append(line);
            contents.append(System.getProperty("line.separator"));
        }
        input.close();
        return contents.toString();
    }

    public static boolean deleteDir(File dir) throws Exception {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }

        // The directory is now empty so delete it
        return dir.delete();
    }

    public static boolean deleteDir(String dirName) throws Exception {
        File dir = new File(dirName);

        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }

        // The directory is now empty so delete it
        return dir.delete();
    }

    public static boolean createDir(String dirName) throws Exception {
        File f = new File(dirName);
        return f.mkdirs();
    }

    public static void deleteFilesOlderThanNdays(int daysBack, String dirWay) {

        File directory = new File(dirWay);
        if (directory.exists()) {

            File[] listFiles = directory.listFiles();
            long purgeTime = System.currentTimeMillis() - (daysBack * 24 * 60 * 60 * 1000);
            for (File listFile : listFiles) {
                if (listFile.lastModified() < purgeTime) {
                    if (!listFile.delete()) {
                        System.err.println("Unable to delete file: " + listFile);
                    }
                }
            }
        } else {
            System.out.println("Files were not deleted, directory " + dirWay + " does'nt exist!");
        }
    }
}
