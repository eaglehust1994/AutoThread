package com.viettel.framework.service.common;

import com.viettel.framework.client.databasse.DbClient;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author qlmvt_khiemvk
 */
public class Client extends DbClient {

    public synchronized ArrayList<Parameters> selectData(String query, Map columnMap, Timestamp startTime) throws Exception {
        ArrayList<Parameters> paramsList = new ArrayList<Parameters>();
        PreparedStatement pre_stmt = null;
        ResultSet rs = null;

        try {
            assureConnection();
            pre_stmt = connection.prepareStatement(query);
            pre_stmt.setTimestamp(1, startTime);
            rs = pre_stmt.executeQuery();

            while (rs.next()) {
                Parameters param = new Parameters();
                Set entries = columnMap.entrySet();
                Iterator it = entries.iterator();
                while (it.hasNext()) {
                    Map.Entry entry = (Map.Entry) it.next();
                    Object value = new Object();
                    try {   
                        value = rs.getObject(entry.getValue().toString());                        
                        param.add(entry.getKey().toString().toUpperCase(), value);                        
                    } catch (Exception e) {
//                        e.printStackTrace();
                    }
                }                
                paramsList.add(param);
            }            
        } catch (Exception ex) {
            throw ex;
        } finally {
            try{
                if (rs != null) {
                    rs.close();
                }
                if (pre_stmt != null) {
                    pre_stmt.close();
                }
            }catch(Exception ex){}
        }
        return paramsList;
    }
}
