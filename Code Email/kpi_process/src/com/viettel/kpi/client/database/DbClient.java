/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.kpi.client.database;

import com.viettel.kpi.getDataSql.common.CommonLogServer;
import com.viettel.kpi.service.common.LogServer;
import com.viettel.passprotector.PassProtector;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.apache.log4j.Logger;
//import java.util.logging.Level;
//import java.util.logging.Logger;
import utils.ConnectionException;
import utils.MultiDataStore;

/**
 *
 * @author qlmvt_minhht1
 */
public class DbClient {

    protected static final String ORACLE = "ORACLE";
    protected static final String SQL_SERVER = "SQL_SERVER";
    protected static final String SYBASE = "SYBASE";
    protected static final String MYSQL = "MYSQL";
    protected Connection connection;
    protected CallableStatement stmt;
    protected MultiDataStore mtDs;
    protected String dbType; // oracle/SQL server/sybase
    private static final Logger logger = Logger.getLogger(DbClient.class);

    public DbClient() {
        dbType = null;
    }
// làm theo cấu hình

    public void connectOracle(CommonLogServer logServer) throws Exception {
        String dbDriver = logServer.getDriver();
        String dbUrl = logServer.getUrl();
        String dbUsername = logServer.getUserName();
        String dbPassword = PassProtector.decrypt(logServer.getPassword(), logServer.getIp());
        String dbSQLcheck = "select 1 from dual";

        logger.info("dbDriver: " + dbDriver);
        logger.info("dbUrl: " + dbUrl);
        logger.info("dbUsername: " + dbUsername);
        logger.info("dbPassword: " + logServer.getPassword());
        logger.info("dbSQLcheck: " + dbSQLcheck);

        mtDs = new MultiDataStore(dbDriver, dbUrl, dbUsername, dbPassword, dbSQLcheck);
        connection = getConnection();
        logger.info("connection: " + connection);
        dbType = ORACLE;
    }

    public void connectSQLServer(CommonLogServer logServer) throws Exception {
        String dbDriver = logServer.getDriver();
        String dbUrl = logServer.getUrl();
        String dbUsername = logServer.getUserName();
        String dbPassword = PassProtector.decrypt(logServer.getPassword(), logServer.getIp());
        String dbSQLcheck = "select 1";

        mtDs = new MultiDataStore(dbDriver, dbUrl, dbUsername, dbPassword, dbSQLcheck);
        connection = getConnection();
        dbType = SQL_SERVER;
    }

    public void connectPostgreSQL(CommonLogServer logServer) throws Exception {
        String dbDriver = logServer.getDriver();
        String dbUrl = logServer.getUrl();
        String dbUsername = logServer.getUserName();
        String dbPassword = PassProtector.decrypt(logServer.getPassword(), logServer.getIp());
        String dbSQLcheck = "select 1";

        mtDs = new MultiDataStore(dbDriver, dbUrl, dbUsername, dbPassword, dbSQLcheck);
        connection = getConnection();
        dbType = SQL_SERVER;
    }

    public void connectMySQL(CommonLogServer logServer) throws Exception {
        String dbDriver = logServer.getDriver();
        String dbUrl = logServer.getUrl();
        String dbUsername = logServer.getUserName();
        String dbPassword = PassProtector.decrypt(logServer.getPassword(), logServer.getIp());

        Class.forName(dbDriver).newInstance();
        connection = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
        dbType = MYSQL;
    }

    public void connectSybase(CommonLogServer logServer) throws Exception {
        String dbDriver = logServer.getDriver();
        String dbUrl = logServer.getUrl();
        String dbUsername = logServer.getUserName();
        String dbPassword = PassProtector.decrypt(logServer.getPassword(), logServer.getIp());
        String dbSQLcheck = "select 1";

        Class.forName(dbDriver).newInstance();
        connection = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
        dbType = SYBASE;
    }
    /*
     * Giữ nguyên cái cũ:
     */

    public void connectOracle(LogServer logServer) throws Exception {
        String dbDriver = logServer.getDriver();
        String dbUrl = logServer.getUrl();
        String dbUsername = logServer.getUsername();
        String dbPassword = PassProtector.decrypt(logServer.getPassword(), logServer.getIp());
        String dbSQLcheck = "select 1 from dual";

        mtDs = new MultiDataStore(dbDriver, dbUrl, dbUsername, dbPassword, dbSQLcheck);
        connection = getConnection();
        dbType = ORACLE;
    }

    public void connectSQLServer(LogServer logServer) throws Exception {
        String dbDriver = logServer.getDriver();
        String dbUrl = logServer.getUrl();
        String dbUsername = logServer.getUsername();
        String dbPassword = PassProtector.decrypt(logServer.getPassword(), logServer.getIp());
        String dbSQLcheck = "select 1";

        mtDs = new MultiDataStore(dbDriver, dbUrl, dbUsername, dbPassword, dbSQLcheck);
        connection = getConnection();
        dbType = SQL_SERVER;
    }

    public void connectMySQL(LogServer logServer) throws Exception {
        String dbDriver = logServer.getDriver();
        String dbUrl = logServer.getUrl();
        String dbUsername = logServer.getUsername();
        String dbPassword = PassProtector.decrypt(logServer.getPassword(), logServer.getIp());

        Class.forName(dbDriver).newInstance();
        connection = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
        dbType = MYSQL;
    }

    public void connectSybase(LogServer logServer) throws Exception {
        String dbDriver = logServer.getDriver();
        String dbUrl = logServer.getUrl();
        String dbUsername = logServer.getUsername();
        String dbPassword = PassProtector.decrypt(logServer.getPassword(), logServer.getIp());
        String dbSQLcheck = "select 1";

        Class.forName(dbDriver).newInstance();
        connection = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
        dbType = SYBASE;
    }

    public void close() throws Exception {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }

    //dungvv8_17022016
    public void close(Connection con) throws Exception {
        try {
            if (con != null && !con.isClosed()) {
                con.close();
            }
        } catch (SQLException ex) {
            throw ex;
        }
    }
    
    public synchronized Connection getConnection() throws SQLException, ConnectionException {
        Connection conn=null;
        try {
         conn = mtDs.getConnection();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        return conn;
    }

    public synchronized boolean checkConnection() {
        return mtDs.check(connection);
    }

    public synchronized void assureConnection() throws SQLException, ConnectionException {
        if (!checkConnection()) {
            connection = getConnection();
        }
    }

    public synchronized void commit() throws SQLException, ConnectionException {
        connection.commit();
    }

    public void setAutoCommit(boolean b) {
        try {
            connection.setAutoCommit(b);
        } catch (SQLException ex) {
            logger.error(ex.getMessage(), ex);
        }
    }
}
