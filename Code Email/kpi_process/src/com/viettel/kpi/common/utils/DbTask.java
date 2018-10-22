/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.kpi.common.utils;

import com.jolbox.bonecp.ConnectionHandle;
import com.viettel.security.PassTranformer;
import java.io.FileReader;
import java.sql.*;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import utils.ConnectionException;

/**
 *
 * @author qlmvt_minhht1
 */
public abstract class DbTask {

//    protected Connection connection;
    protected CallableStatement stmt;
//    protected MultiDataStore mtDs;
    protected String dbFile;
    protected Connection connection;
    protected String userName;
    protected String password;

    public void init(String dbFile) throws Exception {
        FileReader r = null;
        try {
            r = new FileReader(dbFile);
            Properties properties = new Properties();
            properties.load(r);
            String dbdriver = PassTranformer.decrypt(properties.getProperty("driver"));
            String dbconnStr = PassTranformer.decrypt(properties.getProperty("connection"));
            String dbusername = PassTranformer.decrypt(properties.getProperty("username"));
            this.userName = dbusername;
//            System.out.println("dbusername: " + dbusername);
            String dbpassword = PassTranformer.decrypt(properties.getProperty("password"));
            this.password = dbpassword;
//            System.out.println("dbpassword: " + dbpassword);
            String dbsqlcheck = PassTranformer.decrypt(properties.getProperty("sqlcheck", "select 1 from dual"));
//            ConnectionPool connectionPool = new ConnectionPool(dbusername, dbpassword);
//            connection = connectionPool.getConnection(dbusername);
        } finally {
            if (r != null) {
                r.close();
            }
        }
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

    public void close(Connection con) throws Exception {
        try {
            if (con != null && !con.isClosed()) {
                con.close();
            }
        } catch (SQLException ex) {
            throw ex;
        }
    }

    public void initConnection() throws Exception {
        connection = ConnectionPool.getInstance(userName, password).getConnection(userName);
    }

    public synchronized Connection getConnection() throws SQLException, ConnectionException, Exception {
        ConnectionPool conPool = ConnectionPool.getInstance(userName, password);
        Connection con = conPool.getConnection(userName);
        return con;
    }

    public synchronized Connection getConnection(String user, String pass) throws SQLException, ConnectionException, Exception {
        ConnectionPool conPool = ConnectionPool.getInstance(user, pass);
        Connection con = conPool.getConnection(user);
        return con;
    }

    //sonnh26_start
    public synchronized Connection getConnectionPool() throws SQLException, ConnectionException, Exception {
        Connection con = null;
        ConnectionPool conPool = ConnectionPool.getInstance();
        try {
            con = conPool.getConnection();
        } catch (Exception ex) {
            throw ex;
        }
        con = ConnectionPool.getInstance().refreshConnection((ConnectionHandle) con);
        return con;
    }

    //sonnh26_end
    public synchronized void assureConnection() throws Exception {
        try {
            if (connection == null && connection.isClosed()) {
                init(dbFile);
            }
        } catch (Exception ex) {
            throw ex;
        }
    }

    public synchronized void commit() throws SQLException, ConnectionException {
        connection.commit();
    }

    public synchronized void commit(Connection con) throws SQLException, ConnectionException {
        con.commit();
    }

    public void setAutoCommit(boolean b) {
        try {
            connection.setAutoCommit(b);
        } catch (SQLException ex) {
            Logger.getLogger(DbTask.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

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
//            assureConnection();
            con = getConnection();
            stmt = con.prepareCall("{call " + storedName + "(?)}");
            stmt.setObject(1, timestamp);
            stmt.execute();
        } catch (Exception ex) {
            throw ex;
        } finally {
            stmt.close();
            close(con);
        }
    }

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
}
