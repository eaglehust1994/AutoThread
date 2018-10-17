/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.framework.client.databasse;

import com.viettel.framework.service.common.LogServer;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
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

    public DbClient() {
        dbType = null;
    }

    public void connectOracle(LogServer logServer) throws Exception {
        String dbDriver = logServer.getDriver();
        String dbUrl = logServer.getUrl();
        String dbUsername = logServer.getUsername();
        String dbPassword = logServer.getPassword();
        String dbSQLcheck = "select 1 from dual";

        mtDs = new MultiDataStore(dbDriver, dbUrl, dbUsername, dbPassword, dbSQLcheck);
        connection = getConnection();
        dbType = ORACLE;
    }

    public void connectSQLServer(LogServer logServer) throws Exception {
        String dbDriver = logServer.getDriver();
        String dbUrl = logServer.getUrl();
        String dbUsername = logServer.getUsername();
        String dbPassword = logServer.getPassword();
        String dbSQLcheck = "select 1";

        mtDs = new MultiDataStore(dbDriver, dbUrl, dbUsername, dbPassword, dbSQLcheck);
        connection = getConnection();
        dbType = SQL_SERVER;
    }

    public void connectMySQL(LogServer logServer) throws Exception {
        String dbDriver = logServer.getDriver();
        String dbUrl = logServer.getUrl();
        String dbUsername = logServer.getUsername();
        String dbPassword = logServer.getPassword();        

        Class.forName(dbDriver).newInstance();
        connection = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
        dbType = MYSQL;
    }

    public void connectSybase(LogServer logServer) throws Exception {
        String dbDriver = logServer.getDriver();
        String dbUrl = logServer.getUrl();
        String dbUsername = logServer.getUsername();
        String dbPassword = logServer.getPassword();
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

    public synchronized Connection getConnection() throws SQLException, ConnectionException {
        Connection conn = mtDs.getConnection();
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
            Logger.getLogger(DbClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
