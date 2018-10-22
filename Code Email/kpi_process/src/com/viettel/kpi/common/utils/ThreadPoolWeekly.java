
package com.viettel.kpi.common.utils;

//import com.viettel.kpi.SendEmailProcess.SocketThread;
import com.viettel.kpi.SendEmailProcessWeekly.SocketThread;
import com.viettel.mmserver.agent.MmJMXServerSec;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author Khiemvk
 */
public class ThreadPoolWeekly {
    
    private static final Logger logger = Logger.getLogger(ThreadPoolWeekly.class.getName());
    List<SocketThread> threadList = new ArrayList<SocketThread>();
    List<Boolean> threadIsUseList = new ArrayList<Boolean>();
    int threadMin = 1;
    int threadMax;

    public ThreadPoolWeekly(int threadMax) {       
        this.threadMax = threadMax;
        // init threads
        for (int i = 0; i < this.threadMin; i++) {
            System.out.println("Vao socket pool");
            SocketThread socketThread;
            socketThread = new SocketThread(i);
            threadList.add(socketThread);
            threadIsUseList.add(false);
        }
    }

    public SocketThread getThread() {
        for (int i = 0; i < threadList.size(); i++) {
            if (!threadIsUseList.get(i)) {
                threadIsUseList.set(i, true);
                return threadList.get(i);
            }
        }
        
        if (threadList.size() < threadMax) {
            // create a new thread
            SocketThread socketThread;
            socketThread = new SocketThread(threadList.size());
            threadList.add(socketThread);
            threadIsUseList.add(true);
            return socketThread;
        }

        // return null if threadPool is limit
        return null;
    }

    public void releaseThread(SocketThread socketThread) {
        int index = threadList.indexOf(socketThread);
        logger.info("Release Thread:"+ index);
        threadIsUseList.set(index, false);
    }

    public void stop() {
        try{
            MmJMXServerSec.getInstance().stop();
        }catch(Exception ex){
        }
        for (SocketThread socketThread : threadList) {
            socketThread.stop();
        }
    }
}
