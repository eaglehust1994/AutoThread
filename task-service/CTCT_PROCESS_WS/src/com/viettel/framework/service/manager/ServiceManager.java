/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.framework.service.manager;

import com.viettel.framework.service.common.LogServer;
import com.viettel.mmserver.agent.MmJMXServerSec;
import com.viettel.mmserver.base.ProcessThreadMX;
import java.util.ArrayList;
import org.apache.log4j.PropertyConfigurator;

/**
 *
 * @author qlmvt_minhht1
 */
public class ServiceManager {

//    static {
//        try {
//            PropertyConfigurator.configure("../etc/log4j.cfg");
//        } catch (Exception ex) {
//        }
//    }
    private static final String CONFIG_FILE = "../conf/program.conf";
    private static ArrayList<ProcessThreadMX> processList = new ArrayList<ProcessThreadMX>();
    private int numOfThread; // number of Thread
    private int threadSize;  // number of LogServer per a Thread

    /**
     * Hàm start tiến trình. Khởi tạo các Thread của tiến trình. Mỗi Thread xử lý với 1 LogServer.
     * @param serverList Danh sách LogServer
     * @param threadSize Số lượng LogServer tối đa mà 1 Thread xử lý
     * @param job Class mô tả công việc của tiến trình
     * @param interval chu kỳ lấy dữ liệu
     */
    public static void start(ArrayList<LogServer> serverList, Job job, long interval) {
        try {
            if (serverList != null && serverList.size() > 0) {
                for (LogServer logServer : serverList) {
                    ArrayList<LogServer> subServerList = new ArrayList<LogServer>();
                    subServerList.add(logServer);
                    ProcessThread process = new ProcessThread(subServerList, job, interval);
                    processList.add(process);
                    process.start();
                }
            }
        } catch (Exception ex) {
            System.out.println("[ServiceFrameWork] ERROR ProcessManager");
            ex.printStackTrace();
        }
    }

    /**
     * Hàm start tiến trình. Khởi tạo các Thread của tiến trình. Mỗi Thread xử lý với 1 list các LogServer.
     * @param serverList Danh sách LogServer
     * @param threadSize Số lượng LogServer tối đa mà 1 Thread xử lý
     * @param job Class mô tả công việc của tiến trình
     */
    public static void start(ArrayList<LogServer> serverList, int threadSize, Job job, long interval) {
        try {
            if (serverList != null && serverList.size() > 0 && threadSize > 0) {
                ArrayList<ArrayList<LogServer>> listOfServerList = splitServerList(serverList, threadSize);
                if (listOfServerList != null && listOfServerList.size() > 0) {
                    for (ArrayList<LogServer> subServerList : listOfServerList) {
                        ProcessThread process = new ProcessThread(subServerList, job, interval);
                        processList.add(process);
                        process.start();
                    }
                }
            }
        } catch (Exception ex) {
            System.out.println("[ServiceFrameWork] ERROR ProcessManager");
            ex.printStackTrace();
        }
    }

    /**
     * Hàm start tiến trình chạy theo giờ (interval = 1 hour). Khởi tạo các Thread của tiến trình. Mỗi Thread xử lý với 1 LogServer.
     * @param serverList Danh sách LogServer
     * @param threadSize Số lượng LogServer tối đa mà 1 Thread xử lý
     * @param job Class mô tả công việc của tiến trình
     * @param minute phút trong giờ mà tiến trình thực hiện lấy dữ liệu
     */
    public static void startHourly(ArrayList<LogServer> serverList, ArrayList<Job> jobList, long minute) {
        try {
            if (serverList != null && serverList.size() > 0) {
                for(int i = 0; i < serverList.size(); i++){
                    ProcessThreadHourly process = new ProcessThreadHourly(serverList.get(i), jobList.get(i), minute);
                    processList.add(process);
                    process.start();
                }
            }
        } catch (Exception ex) {
            System.out.println("[ServiceFrameWork] ERROR ProcessManager");
            ex.printStackTrace();
        }
    }

    /**
     * Hàm start tiến trình chạy theo ngày (interval = 1 day). Khởi tạo các Thread của tiến trình. Mỗi Thread xử lý với 1 LogServer.
     * @param serverList Danh sách LogServer
     * @param threadSize Số lượng LogServer tối đa mà 1 Thread xử lý
     * @param job Class mô tả công việc của tiến trình
     * @param hour giờ trong ngày mà tiến trình thực hiện lấy dữ liệu
     */
    public static void startDaily(ArrayList<LogServer> serverList, Job job, long hour) {
        try {
            if (serverList != null && serverList.size() > 0) {
                for (LogServer logServer : serverList) {
                    ProcessThreadDaily process = new ProcessThreadDaily(logServer, job, hour);
                    processList.add(process);
                    process.start();
                }
            }
        } catch (Exception ex) {
            System.out.println("[ServiceFrameWork] ERROR ProcessManager");
            ex.printStackTrace();
        }
    }

    /**
     * Hàm start tiến trình chạy theo ngày (interval = 1 day). Khởi tạo các Thread của tiến trình. Mỗi Thread xử lý với 1 LogServer.
     * @param serverList Danh sách LogServer
     * @param threadSize Số lượng LogServer tối đa mà 1 Thread xử lý
     * @param jobList Class mô tả công việc của từng tiến trình
     * @param hour giờ trong ngày mà tiến trình thực hiện lấy dữ liệu
     */
    public static void startDaily(ArrayList<LogServer> serverList, ArrayList<Job> jobList, long hour) {
        try {
            if (serverList != null && serverList.size() > 0) {
                for(int i = 0; i < serverList.size(); i++){
                    ProcessThreadDaily process = new ProcessThreadDaily(serverList.get(i), jobList.get(i), hour);
                    processList.add(process);
                    process.start();
                }
            }
        } catch (Exception ex) {
            System.out.println("[ServiceFrameWork] ERROR ProcessManager");
            ex.printStackTrace();
        }
    }

    /**
     * Hàm start tiến trình chạy theo ngày (interval = 1 day). Khởi tạo các Thread của tiến trình. Mỗi Thread xử lý với 1 LogServer.
     * @param serverList Danh sách LogServer
     * @param threadSize Số lượng LogServer tối đa mà 1 Thread xử lý
     * @param jobList Class mô tả công việc của từng tiến trình
     * @param hour giờ trong ngày mà tiến trình thực hiện lấy dữ liệu
     * @param minute phút tiến trình chạy trong giờ đó
     */
    public static void startDaily(ArrayList<LogServer> serverList, ArrayList<Job> jobList, long hour, long minute) {
        try {
            if (serverList != null && serverList.size() > 0) {
                for(int i = 0; i < serverList.size(); i++){
                    ProcessThreadDaily process = new ProcessThreadDaily(serverList.get(i), jobList.get(i), hour, minute);
                    processList.add(process);
                    process.start();
                }
            }
        } catch (Exception ex) {
            System.out.println("[ServiceFrameWork] ERROR ProcessManager");
            ex.printStackTrace();
        }
    }

    /**
     * Hàm start tiến trình chạy theo ngày (interval = 1 day). Khởi tạo các Thread của tiến trình. Mỗi Thread xử lý với 1 LogServer.
     * @param serverList Danh sách LogServer
     * @param threadSize Số lượng LogServer tối đa mà 1 Thread xử lý
     * @param job Class mô tả công việc của tiến trình
     * @param day ngày trong tuần mà tiến trình thực hiện lấy dữ liệu
     * @param hour giờ trong ngày mà tiến trình thực hiện lấy dữ liệu
     */
    public static void startWeekly(ArrayList<LogServer> serverList, Job job, long day, long hour) {
        try {
            if (serverList != null && serverList.size() > 0) {
                for (LogServer logServer : serverList) {
                    ProcessThreadWeekly process = new ProcessThreadWeekly(logServer, job, day, hour);
                    processList.add(process);
                    process.start();
                }
            }
        } catch (Exception ex) {
            System.out.println("[ServiceFrameWork] ERROR ProcessManager");
            ex.printStackTrace();
        }
    }

    /**
     * Hàm stop tiến trình. Giải phóng tất cả các Thread của tiến trình.
     */
    public static void stop() {
        try{
            //giai phong tien trinh trong truong hop su dung M&M server
            MmJMXServerSec.getInstance().stop();
        }catch(Exception ex){
        }

        if (processList != null && processList.size() > 0) {
            for (int i = 0; i
                    < processList.size(); i++) {
                ProcessThreadMX process = processList.get(i);
                process.stop();


            }
        }
    }

    public static ArrayList<ArrayList<LogServer>> splitServerList(ArrayList<LogServer> serverList, int threadSize) {
        ArrayList<ArrayList<LogServer>> resultList = new ArrayList<ArrayList<LogServer>>();
        while (!serverList.isEmpty()) {
            int size = (serverList.size() < threadSize) ? serverList.size() : threadSize;
            ArrayList<LogServer> singleList = new ArrayList<LogServer>();
            singleList.addAll(serverList.subList(0, size));
            resultList.add(singleList);
            serverList.subList(0, size).clear();
        }

        return resultList;
    }
}
