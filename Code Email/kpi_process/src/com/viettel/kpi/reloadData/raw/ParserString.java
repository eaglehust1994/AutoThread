/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.kpi.reloadData.raw;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author os_sonnh
 */
public class ParserString {

    public ParserString() {
    }

    public Map<Long, List<Long>> LstMap(String content) {

        Map<Long, List<Long>> mapLst = new HashMap<Long, List<Long>>();
        if (content != null && !content.equals("")) {
            List<Long> lstQueryId = new ArrayList<Long>();
            content = content.substring(content.indexOf(":") + 1).trim();
            content = content.replace("||", "sonnh26");
            //  String cut = "\\||";
            String[] lstLog = content.split("sonnh26");
            Long logserverId = null;
            String query = null;

            for (String lst : lstLog) {
                lstQueryId = new ArrayList<Long>();
                lst = lst.replace("|", "sonnh26");
                String[] lstLogQuery = lst.split("sonnh26");
                if (checkLong(lstLogQuery[0].trim())) {
                    logserverId = Long.parseLong(lstLogQuery[0].trim());
                }
                query = lstLogQuery[1];
                String[] lstQuery = query.split(",");
                for (String lstQ : lstQuery) {
                    if (checkLong(lstQ.trim())) {
                        Long lquery = Long.parseLong(lstQ.trim());
                        lstQueryId.add(lquery);
                    }
                }
                mapLst.put(logserverId, lstQueryId);
            }

        }
        return mapLst;


    }

    public boolean checkLong(String str) {
        boolean checkL = false;
        try {
            if (str != null && !str.equals("")) {
                Long bt = Long.parseLong(str);
                checkL = true;

            }
        } catch (Exception e) {
            e.printStackTrace();
            checkL = false;
        }
        return checkL;

    }
}
