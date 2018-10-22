package com.viettel.kpi.common.utils;

import com.jolbox.bonecp.BoneCP;
import com.jolbox.bonecp.BoneCPConfig;
import com.jolbox.bonecp.ConnectionHandle;
import com.viettel.security.PassTranformer;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import org.apache.log4j.Logger;

/**
 * @author qlmvt_minhht1
 * @version 1.0
 * @created 30-May-2012 10:04:21 AM
 */
public class ConnectionPool {

    private BoneCP pool;
    private static HashMap<String, BoneCP> mapPool;
    private static ConnectionPool instance;
    private static ConnectionPool instanceError;
    //  private  ConnectionPool instance;
    private Logger logger = Logger.getLogger(ConnectionPool.class);

    public static synchronized ConnectionPool getInstance(String userName, String password) throws Exception {
        ConnectionPool instance = new ConnectionPool(userName, password);
        return instance;
    }

    public static synchronized ConnectionPool getInstancefix() throws Exception {
        if (instance == null) {
            instance = new ConnectionPool();
        }
        return instance;
    }

    public static synchronized ConnectionPool getInstance() throws Exception {
        if (instance == null) {
            instance = new ConnectionPool();
        }
        return instance;
    }

    public static synchronized ConnectionPool getInstanceError() throws Exception {
        if (instanceError == null) {
            instanceError = new ConnectionPool(true);
        }
        return instanceError;
    }

//sonnh26 them
    private ConnectionPool(boolean check) throws Exception {
        String configFile = "../etc/bonecp-config-error.xml";
        // 1. Setup the connection pool
        try {
            Class.forName("oracle.jdbc.OracleDriver");
            FileInputStream fis = new FileInputStream(configFile);
            BoneCPConfig config = new BoneCPConfig(fis, "default");
            try {
                String password = config.getPassword();
                //password = PassProtector.decrypt(password, "mediation");
                password = PassTranformer.decrypt(password);
                config.setPassword(password);
            } catch (Exception e) {
                e.printStackTrace();
            }
            fis.close();

            pool = new BoneCP(config);
        } catch (Exception e) {
            logger.error("Create connectionPool fail");
            e.printStackTrace();
            throw e;
        }
    }

    public ConnectionPool() throws Exception {
        String configFile = "../etc/bonecp-config.xml";
        // 1. Setup the connection pool
        try {
            Class.forName("oracle.jdbc.OracleDriver");
            FileInputStream fis = new FileInputStream(configFile);
            BoneCPConfig config = new BoneCPConfig(fis, "default");
            try {
                String password = config.getPassword();
                password = PassTranformer.decrypt(password);
                config.setPassword(password);
            } catch (Exception e) {
                e.printStackTrace();
            }
            fis.close();
            pool = new BoneCP(config);
        } catch (Exception e) {
            logger.error("Create connectionPool fail");
            e.printStackTrace();
            throw e;
        }
    }
    public ConnectionPool(String configFile) throws Exception {
        // 1. Setup the connection pool
        try {
            Class.forName("oracle.jdbc.OracleDriver");
            FileInputStream fis = new FileInputStream(configFile);
            BoneCPConfig config = new BoneCPConfig(fis, "default");
            try {
                String password = config.getPassword();
                password = PassTranformer.decrypt(password);
                config.setPassword(password);
            } catch (Exception e) {
                e.printStackTrace();
            }
            fis.close();
            pool = new BoneCP(config);
        } catch (Exception e) {
            logger.error("Create connectionPool fail");
            e.printStackTrace();
            throw e;
        }
    }

    public ConnectionPool(String userName, String password) throws Exception {
        String configFile = "../etc/bonecp-config.xml";
        // 1. Setup the connection pool
        BoneCPConfig config = null;
        try {
            if (mapPool == null) {
                mapPool = new HashMap<String, BoneCP>();
            }
            if (!mapPool.containsKey(userName)) {
                Class.forName("oracle.jdbc.OracleDriver");
                FileInputStream fis = new FileInputStream(configFile);
                config = new BoneCPConfig(fis, userName.toUpperCase());
                config.setPassword(password);
                config.setUsername(userName);
                BoneCP poolCp = new BoneCP(config);
                mapPool.put(userName, poolCp);
                fis.close();
            }
        } catch (Exception e) {
            logger.error("Create connectionPool fail");
            e.printStackTrace();
            throw e;
        }
    }

    public Connection getConnection(String userName) throws Exception {
        try {
            BoneCP poolCP = mapPool.get(userName);
            logger.debug("Poolsize(free/Total): " + poolCP.getTotalFree() + "/" + poolCP.getTotalCreatedConnections());
            return poolCP.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public Connection getConnection() throws Exception {
        try {
            logger.info("Poolsize(free/Total): " + pool.getTotalFree() + "/" + pool.getTotalCreatedConnections());
            return pool.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public synchronized boolean isConnectionHandleAlive(Connection connection) {
        return pool.isConnectionHandleAlive((ConnectionHandle) connection);
    }

    public int getTotalCreatedConnections() {
        return pool.getTotalCreatedConnections();
    }

    public int getTotalLeased() {
        return pool.getTotalLeased();
    }

    public int getTotalFree(String userName) {
        return pool.getTotalFree();


    }

    public static void closeResource(ResultSet rs) {
        try {
            if (rs != null) {
                rs.close();
                rs = null;


            }
        } catch (SQLException e) {
            e.printStackTrace();


        }
    }

    public static void closeResource(PreparedStatement preStmt) {
        try {
            if (preStmt != null) {
                preStmt.close();
                preStmt = null;
            }
        } catch (SQLException e) {
            e.printStackTrace();


        }
    }

    public static void closeResource(Connection connection) {
        try {
            if (connection != null) {
                connection.close();
                connection = null;


            }
        } catch (SQLException e) {
            e.printStackTrace();


        }
    }

    public void destroy(String userName) {
        try {
            //          if (mapPool.containsKey(userName)) {
//                pool = mapPool.get(userName);
//                mapPool.remove(pool);
            if (pool != null) {
                pool.shutdown();
                pool = null;

                //  instance = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void destroy() {
        try {
            if (pool != null) {
                pool.shutdown();
                pool = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Connection refreshConnection(ConnectionHandle conHdl) throws Exception {
        try {
            if (conHdl == null || conHdl.isClosed() || !pool.isConnectionHandleAlive(conHdl)) {
                logger.warn("connection has die, reset pool");
                BoneCPConfig config = pool.getConfig();
                destroy();
                pool = new BoneCP(config);
                return pool.getConnection();
            }
            return conHdl;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
}
