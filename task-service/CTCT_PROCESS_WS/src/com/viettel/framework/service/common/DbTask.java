/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.framework.service.common;

import com.jolbox.bonecp.ConnectionHandle;
//import com.viettel.pnms.isp.performance.CataLinkObject;
//import com.viettel.pnms.isp.performance.CataNodeObject;
//import com.viettel.pnms.isp.performance.NodeBeginObject;
import com.viettel.security.PassTranformer;
import java.sql.PreparedStatement;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import utils.ConnectionException;
//R0000_Edit_TienBV2_28122012_Start
//import utils.MultiDataStore;
//R0000_Edit_TienBV2_28122012_End
import java.io.FileReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.InetAddress;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import oracle.jdbc.OracleTypes;
import utils.MultiDataStore;

/**
 *
 * @author qlmvt_minhht1
 */
public class DbTask {

    protected Connection connection;
    protected CallableStatement stmt;
    ////sonnh26_Edit_r3554_8/03/2013_start
    //  protected MultiDataStore mtDs;
    //sonnh26_Edit_r3554_8/03/2013_End

    public void init(String dbFile) throws Exception {
//        FileReader r = null;
//        try {
//            r = new FileReader(dbFile);
//            Properties properties = new Properties();
//            properties.load(r);
//            String dbdriver = PassTranformer.decrypt(properties.getProperty("driver"));
//            String dbconnStr = PassTranformer.decrypt(properties.getProperty("connection"));
//            String dbusername = PassTranformer.decrypt(properties.getProperty("username"));
//            String dbpassword = PassTranformer.decrypt(properties.getProperty("password"));
//            String dbsqlcheck = PassTranformer.decrypt(properties.getProperty("sqlcheck", "select 1 from dual"));
//
//            mtDs = new MultiDataStore(dbdriver, dbconnStr, dbusername, dbpassword, dbsqlcheck);
//            connection = getConnection();
//        } finally {
//            if (r != null) {
//                r.close();
//            }
//        }
//         connection = ConnectionPool.getInstance().getConnection();
    }

    public void close() throws Exception {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException ex) {
            throw ex;
        }
    }
    //Sonnh26_new_R3680_10/05/2013_start

    public void close(Connection con) throws Exception {
        try {
            if (con != null && !con.isClosed()) {
                con.close();
            }
        } catch (SQLException ex) {
            throw ex;
        }
    }
    //Sonnh26_new_R3680_10/05/2013_End

    public synchronized Connection getConnection() throws SQLException, ConnectionException, Exception {
        Connection conn = null;
        try {
            conn = ConnectionPool.getInstance().getConnection();
//           if(!ConnectionPool.getInstance().isConnectionHandleAlive(conn)) {
            conn = ConnectionPool.getInstance().refreshConnection((ConnectionHandle) conn);
//            }

        } catch (SQLException ex) {
            throw ex;
        } catch (ConnectionException ex) {
            throw ex;
        } catch (Exception ex) {
            throw ex;
        }

        return conn;
    }
    //sonnh26_Edit_r3554_8/03/2013_End

    public synchronized Connection getConnectionCurrent() {
        return connection;
    }
//sonnh26_Edit_r3554_8/03/2013_start

    public synchronized boolean checkConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                return true;
            } else {
                return false;
            }
        } catch (Exception ex) {
            return false;
        }
    }
//sonnh26_Edit_25/03/2013_start

    public synchronized Connection assureConnection() throws SQLException, ConnectionException, Exception {
        try {

//            connection = getConnection();
            return getConnection();

        } catch (SQLException ex) {
            throw ex;
        } catch (ConnectionException ex) {
            throw ex;
        } catch (Exception ex) {
            throw ex;
        }
    }
//sonnh26_Edit_25/03/2013_End
//    public synchronized Connection assureConnection(Connection connection) throws SQLException, ConnectionException, Exception {
//        try {
//
////            connection = getConnection();
//            return getConnection(connection);
//
//        } catch (SQLException ex) {
//            throw ex;
//        } catch (ConnectionException ex) {
//            throw ex;
//        } catch (Exception ex) {
//            throw ex;
//        }
//    }
    //sonnh26_Edit_r3554_8/03/2013_End

    public synchronized void commit() throws SQLException, ConnectionException {
        if (null != connection && !connection.isClosed()) {
            connection.commit();
        }
    }
    //sonnh26_R3680_New_Start

    public synchronized void commit(Connection con) throws SQLException, ConnectionException {
        con.commit();
    }
    //sonnh26_R3680_New_End

    public void setAutoCommit(Connection con, boolean b) {
        try {
            con.setAutoCommit(b);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public synchronized ArrayList<LogServer> getLogServerList(String vendorName, int server_type_id) throws Exception {
        ResultSet rs = null;
        ArrayList<LogServer> serverList = new ArrayList<LogServer>();
        LogServer server = null;
        Connection con = null;
        try {
            //con = assureConnection();
            con = getConnection();
            stmt = con.prepareCall("{call " + Constants.PK_ISP + ".proc_GetLogServerList(?,?,?)}");
            stmt.setString(1, vendorName);
            stmt.setInt(2, server_type_id);
            stmt.registerOutParameter(3, OracleTypes.CURSOR);
            stmt.execute();
            rs = (ResultSet) stmt.getObject(3);
            while (rs.next()) {
                String logServerId = rs.getString("log_server_id").trim();
                String name = rs.getString("name");
                int serverTypeId = rs.getInt("server_type_id");
                String url = rs.getString("url");
                String ip = rs.getString("ip");
                int port = rs.getInt("port");
                String username = rs.getString("username");
                String password = rs.getString("password");
                try {
                    password = com.viettel.passprotector.PassProtector.decrypt(password, ip);
                } catch (Exception ex) {
                }
                String vendor = rs.getString("vendor");
                String protocol = rs.getString("protocol");
                String driver = rs.getString("driver");
                String module = rs.getString("module");
                String areaCode = rs.getString("area_code");
                int enable = rs.getInt("enable");

                server = new LogServer(logServerId, name, serverTypeId, url, ip, port, username, password, vendor, protocol, driver, enable, areaCode, module);
                serverList.add(server);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println(ex.getMessage());
            throw ex;
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }

            } catch (Exception e) {
            }

            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            //r3454_sonnh26_new_8/03/2013_start
            close(con);
            //r3454_sonnh26_new_8/03/2013_End

        }


        return serverList;
    }

    //dungdv15_New_25/03/2013_start
    public synchronized ArrayList<LogServer> getLogServerList1(String vendorName, int server_type_id) throws Exception {
        ResultSet rs = null;
        ArrayList<LogServer> serverList = new ArrayList<LogServer>();
        LogServer server = null;
        Connection con = null;
        try {
            con = getConnection();
            stmt = con.prepareCall("{call " + Constants.PK_ISP + ".proc_GetLogServerList(?,?,?)}");
            stmt.setString(1, vendorName);
            stmt.setInt(2, server_type_id);
            stmt.registerOutParameter(3, OracleTypes.CURSOR);
            stmt.execute();
            rs = (ResultSet) stmt.getObject(3);
            while (rs.next()) {
                String logServerId = rs.getString("log_server_id").trim();
                String name = rs.getString("name");
                int serverTypeId = rs.getInt("server_type_id");
                String url = rs.getString("url");
                String ip = rs.getString("ip");
                int port = rs.getInt("port");
                String username = rs.getString("username");
                String password = rs.getString("password");
                try {
                    password = com.viettel.passprotector.PassProtector.decrypt(password, ip);
                } catch (Exception ex) {
                }
                String vendor = rs.getString("vendor");
                String protocol = rs.getString("protocol");
                String driver = rs.getString("driver");
                int enable = rs.getInt("enable");
                String areaCode = rs.getString("area_code");
                String module = rs.getString("module");
                server = new LogServer(logServerId, name, serverTypeId, url, ip, port, username, password, vendor, protocol, driver, enable, areaCode, module);
                serverList.add(server);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println(ex.getMessage());
            throw ex;
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }

            } catch (Exception e) {
            }

            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            //r3454_sonnh26_new_8/03/2013_start
            close(con);
            //r3454_sonnh26_new_8/03/2013_End
        }


        return serverList;
    }
    //dungdv15_New_25/03/2013_End
//Sonnh26_edit_25/03/2013_start

    /**
     * Thực hiện store với tham số truyền vào là 1 ngày
     *
     * @param storeName tên store
     * @param timestamp ngày truyền vào
     * @throws Exception
     */
    public synchronized void executeStored(String storedName, Timestamp timestamp) throws Exception {
        Connection con = null;
        try {
            //Sonnh26_edit_25/03/2013_start
            // assureConnection();
            //Sonnh26_edit_25/03/2013_End
            con = getConnection();
            //Sonnh26_edit_25/03/2013_start
            //  stmt = connection.prepareCall("{call " + storedName + "(?)}");
            //Sonnh26_edit_25/03/2013_start
            stmt = con.prepareCall("{call " + storedName + "(?)}");
            stmt.setObject(1, timestamp);
            stmt.execute();
        } catch (Exception ex) {
            throw ex;
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (Exception e) {
            }

            //r3454_sonnh26_new_8/03/2013_start
            close(con);
            //r3454_sonnh26_new_8/03/2013_End
        }
    }

    /**
     * Thực hiện store với tham số truyền vào là danh sách ngày
     *
     * @param storeName tên store
     * @param dateList danh sách ngày truyền vào
     * @throws Exception
     */
    public synchronized void executeStored(String storedName, ArrayList<Timestamp> timestampList) throws Exception {
        for (Timestamp timestamp : timestampList) {
            executeStored(storedName, timestamp);
        }
    }

    /**
     * Thực hiện danh sách các store với tham số truyền vào là danh sách ngày
     *
     * @param storeNameList danh sách tên store
     * @param dateList danh sách ngày truyền vào
     * @throws Exception
     */
    public synchronized void executeStoredList(ArrayList<String> storedNameList, ArrayList<Timestamp> timestampList) throws Exception {
        for (String storedName : storedNameList) {
            executeStored(storedName, timestampList);
        }
    }

    public synchronized ArrayList<String> getStoredList(int serviceID) throws SQLException, Exception {
        ArrayList<String> storedList = new ArrayList<String>();
        ResultSet rs = null;
        Connection con = null;

        try {
            con = getConnection();
            stmt = con.prepareCall("{call PK_NSS.proc_GetStoredList(?,?)}");
            stmt.setInt(1, serviceID);
            stmt.registerOutParameter(2, OracleTypes.CURSOR);
            stmt.execute();
            rs = (ResultSet) stmt.getObject(2);
            while (rs.next()) {
                String storedName = rs.getString("store_name");
                storedList.add(storedName);
            }
            stmt.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
            }
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (Exception e) {
            }

            //r3454_sonnh26_new_8/03/2013_start
            close(con);
            //r3454_sonnh26_new_8/03/2013_End
        }

        return storedList;
    }

    public synchronized void setDateLossData(Timestamp maxTime) throws SQLException, Exception {
        Connection con = null;
        try {
            con = getConnection();
            stmt = con.prepareCall("{call PK_NSS.proc_setDateLostData(?)}");
            stmt.setTimestamp(1, maxTime);
            stmt.execute();
            stmt.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (Exception e) {
            }

            //r3454_sonnh26_new_8/03/2013_start
            close(con);
            //r3454_sonnh26_new_8/03/2013_End
        }
    }

    /**
     * Cập nhật thời gian có dữ liệu access
     *
     * @param updateTime: thời gian dữ liệu
     * @param table_name: bảng thô
     * @param log_server_id
     * @param time_Type: Daily/Hourly ...
     * @param truncTime: dd, hh24, mi ...
     */
    public synchronized void setTimeDataRaw_Accesss(Timestamp updateTime, String table_name,
            String log_server_id, String time_Type, String truncTime) throws SQLException, Exception {
        Connection con = null;

        try {
            con = getConnection();
            stmt = con.prepareCall("{call PK_KPI_BUILD_TIME.sp_setTimeDataRaw(?,?,?,?,?)}");
            stmt.setTimestamp(1, updateTime);
            stmt.setString(2, table_name);
            stmt.setString(3, log_server_id);
            stmt.setString(4, time_Type);
            stmt.setString(5, truncTime);
            stmt.execute();
            stmt.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (Exception e) {
            }

            //r3454_sonnh26_new_8/03/2013_start
            close(con);
            //r3454_sonnh26_new_8/03/2013_End
        }
    }
//
//    }
    //khangnt Start 30/01/2012

    public List<String> getGroupNodeCodeBegin() throws Exception {
        List<String> listResult = new ArrayList<String>();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Connection con = null;

        String sql = " SELECT DISTINCT SRC_NODE_CODE FROM ISP.CATA_LINK WHERE SRC_NODE_CODE IS NOT NULL";
        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                listResult.add(rs.getString("SRC_NODE_CODE"));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
            }
            try {
                if (pstmt != null) {
                    pstmt.close();
                }
            } catch (Exception e) {
            }
            //r3454_sonnh26_new_8/03/2013_start
            close(con);
            //r3454_sonnh26_new_8/03/2013_End
        }
        return listResult;
    }

   

    //Sonnh26_edit_25/03/2013_End

    //sonnh26_R5236_07032014_start
    public synchronized Connection getConnectionError() throws SQLException, ConnectionException, Exception {
        Connection connection = null;
        try {
//            if (!checkConnection()) {
            connection = ConnectionPool.getInstanceError().getConnection();
            connection = ConnectionPool.getInstanceError().refreshConnection((ConnectionHandle) connection);
//            }
        } catch (SQLException ex) {
            throw ex;
        } catch (ConnectionException ex) {
            throw ex;
        } catch (Exception ex) {
            throw ex;
        }
        return connection;
    }

//    public void close(Connection con) throws Exception {
//        try {
//            if (con != null) {
//                con.close();
//            }
//        } catch (SQLException ex) {
//            throw ex;
//        }
//    }
//
//    public synchronized void commit(Connection con) throws SQLException, ConnectionException {
//        con.commit();
//    }
    public void insertDataError(CommonLogServer commonLogServer, LogServer logServer, MscServerCommon mscServer, int type, String errorDetail, String path) throws Exception {
        AlarmErrorServer alarmErrorServer = new AlarmErrorServer();
        List<AlarmErrorServer> lstAlarmErrorServer = new ArrayList<AlarmErrorServer>();

//        Connection conn = null;
//        PreparedStatement pstmt = null;
//        conn = getConnection();
        if (type == 1) {
            // thuc hien lay thogn tin tu bang common.common_sv_log_servers
            try {
                if (logServer != null) {
                    alarmErrorServer.setName(logServer.getLogServerId());
                    alarmErrorServer.setIp(logServer.getIp());
                    alarmErrorServer.setPort(logServer.getPort());
                    alarmErrorServer.setModule(logServer.getModule());
                    alarmErrorServer.setVendor(logServer.getVendor());
                    alarmErrorServer.setAreaCode(logServer.getAreaCode());
                    alarmErrorServer.setErrorDetail(errorDetail);
                    alarmErrorServer.setPath_error(path);
                    lstAlarmErrorServer.add(alarmErrorServer);
                    insertAlamrServerNode(lstAlarmErrorServer);
                }

            } catch (Exception e) {
            }

        } else if (type == 2) {
            // thuc hien lay thogn tin tu bang common.common_log_server
            if (commonLogServer != null) {
                lstAlarmErrorServer = getCommonLogServer(commonLogServer.getLogServerId(), errorDetail, path);
                insertAlamrServerNode(lstAlarmErrorServer);
            }
        } else if (type == 3) {
            // thuc hien lay thogn tin tu bang nss.cata_msc
            lstAlarmErrorServer = getCataMsc(mscServer.getMscCode(), mscServer.getType(), errorDetail, path);
            insertAlamrServerNode(lstAlarmErrorServer);
        }

    }

    public void insertAlamrServerNode(List<AlarmErrorServer> alarmErrorServer) {
        try {
            int count = 0;
            Connection conn = null;
            PreparedStatement pstmt = null;
            conn = getConnectionError();

            String sql = " INSERT INTO  common.common_alarm_node_server "
                    + " ( id, name, ip, port, module, vendor, area_code,"
                    + " vesion, type, update_time, insert_time, error_detail,path_error,"
                    + " flag_sms, flag_email)"
                    + " VALUES (common.common_alarm_node_server_sq.nextval,?,?,?,?,?,?,?,?,TRUNC(sysdate),sysdate,?,?,0,0)";
            pstmt = conn.prepareStatement(sql);
            for (AlarmErrorServer alarmErrorServerBo : alarmErrorServer) {
                pstmt.setString(1, alarmErrorServerBo.getName() != null ? alarmErrorServerBo.getName() : "");
                pstmt.setString(2, alarmErrorServerBo.getIp() != null ? alarmErrorServerBo.getIp() : "");
                if (alarmErrorServerBo.getPort() != null) {
                    pstmt.setInt(3, alarmErrorServerBo.getPort());
                } else {
                    pstmt.setNull(3, Types.INTEGER);
                }
                pstmt.setString(4, alarmErrorServerBo.getModule() != null ? alarmErrorServerBo.getModule() : "");
                pstmt.setString(5, alarmErrorServerBo.getVendor() != null ? alarmErrorServerBo.getVendor() : "");
                pstmt.setString(6, alarmErrorServerBo.getAreaCode() != null ? alarmErrorServerBo.getAreaCode() : "");
                if (alarmErrorServerBo.getVesion() != null) {
                    pstmt.setInt(7, alarmErrorServerBo.getVesion());
                } else {
                    pstmt.setNull(7, Types.INTEGER);
                }
                pstmt.setString(8, alarmErrorServerBo.getType() != null ? alarmErrorServerBo.getType() : "");
                pstmt.setString(9, alarmErrorServerBo.getErrorDetail());
                pstmt.setString(10, alarmErrorServerBo.getPath_error());
                pstmt.addBatch();
                count++;
                if (count >= 500) {
                    pstmt.executeBatch();
                    pstmt.clearBatch();
                    count = 0;
                }
            }
            if (count > 0) {
                pstmt.executeBatch();
                pstmt.clearBatch();

            }
            commit(conn);
            try {
                if (pstmt != null) {
                    pstmt.close();
                }
            } catch (Exception e) {
            }
            close(conn);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<AlarmErrorServer> getCommonLogServer(Long logServerId, String errorDeatail, String path) throws Exception {
        List<AlarmErrorServer> list = new ArrayList<AlarmErrorServer>();
        PreparedStatement pstmt = null;
        Connection con = null;
        ResultSet result = null;
        AlarmErrorServer bo = null;
        try {
            con = getConnectionError();
            String sql = " select e.module_name,d.* from (select c.module_id, b.* from "
                    + " (select a.* from common.common_log_server a where a.log_server_id = ?)b "
                    + " left JOIN common.common_map_query_log_server c on b.log_server_id = c.log_server_id)d"
                    + " left join pnms.sv_kpi_module e on e.module_id = d.module_id  order by d.log_server_id ";
            pstmt = con.prepareStatement(sql);
            pstmt.setLong(1, logServerId);
            result = pstmt.executeQuery();

            while (result.next()) {
                try {
                    bo = new AlarmErrorServer();

                    bo.setModule(DBUtil.getString(result.getObject("module_name")));
                    bo.setName(DBUtil.getString(result.getObject("log_server_name")));
                    bo.setErrorDetail(errorDeatail);
                    bo.setIp(DBUtil.getString(result.getObject("ip")));
                    bo.setPath_error(path);
                    bo.setPort(DBUtil.getInteger(result.getObject("port")));
                    bo.setVendor(DBUtil.getString(result.getObject("vendor")));
                    list.add(bo);

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        } finally {

            try {
                if (result != null) {
                    result.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            try {
                if (pstmt != null) {
                    pstmt.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            close(con);
        }
        return list;

    }

    public List<AlarmErrorServer> getCataMsc(String mscCode, String type, String errorDeatail, String path) throws Exception {
        List<AlarmErrorServer> list = new ArrayList<AlarmErrorServer>();
        PreparedStatement pstmt = null;
        Connection con = null;
        ResultSet result = null;
        AlarmErrorServer bo = null;
        try {
            con = getConnectionError();
            String sql = " select b.area_code,a.* from nss.cata_msc a join nss.nss_cata_node b on a.msc_code=b.node_code where a.msc_code=? and a.type=? ";

            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, mscCode);
            pstmt.setString(2, type);
            result = pstmt.executeQuery();

            while (result.next()) {
                try {
                    bo = new AlarmErrorServer();

                    bo.setModule("NSS");
                    bo.setName(DBUtil.getString(result.getObject("msc_code")));
                    bo.setErrorDetail(errorDeatail);
                    bo.setIp(DBUtil.getString(result.getObject("ip")));
                    bo.setPath_error(path);
                    bo.setPort(DBUtil.getInteger(result.getObject("port")));
                    bo.setVendor(DBUtil.getString(result.getObject("vendor")));
                    bo.setVesion(DBUtil.getInteger(result.getObject("version")));
                    bo.setType(DBUtil.getString(result.getObject("type")));
                    bo.setAreaCode(DBUtil.getString(result.getObject("area_code")));
                    list.add(bo);

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        } finally {

            try {
                if (result != null) {
                    result.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            try {
                if (pstmt != null) {
                    pstmt.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            close(con);
        }
        return list;

    }

    public String getExceptionContent(Exception ex) {
        StringBuilder result = new StringBuilder();

        try {
            if (ex.getMessage() != null) {
                result = new StringBuilder(ex.getMessage());
                StringWriter sw = new StringWriter();
                new Throwable("").printStackTrace(new PrintWriter(sw));
                result.append(System.getProperty("line.separator"));
                if (sw.toString().length() <= 900) {
                    result.append(sw.toString());
                } else {
                    result.append(sw.toString().subSequence(0, 900));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result.toString();
    }

    public String getServerInfo() {
        String info = "";
        try {
            InetAddress thisIp = InetAddress.getLocalHost();
            info = "IP: " + thisIp.toString() + System.getProperty("line.separator") + " URL: " + System.getProperty("user.dir");
            if (info.length() > 450) {
                info = info.substring(0, 450);
            }
        } catch (Exception ex) {
//            ex.printStackTrace();
            return "N/A";
        }

        return info;
    }
}
