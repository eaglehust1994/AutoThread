/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.framework.client.databasse;

import com.viettel.framework.client.databasse.Constants.CH;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 *
 * @author Pham Manh Hung
 */
public class Query {

    public static Logger log = Logger.getLogger(Query.class.getName());
    private PreparedStatement stm;
    private Connection conn;
    private boolean isTransaction = false;

    public Connection connection() {
        return conn;
    }

    public Query() {
        this.stm = null;
        this.conn = null;
    }

    public Query(Connection conn) {
        this.conn = conn;
    }

    /**
     * 
     * @param conn
     */
    public void setConnection(Connection conn) {
        this.conn = conn;
    }

    public void beginTransaction() throws SQLException {
        conn.setAutoCommit(false);
        isTransaction = true;
    }

    public void commit() throws SQLException {
        conn.commit();
        isTransaction = false;
        conn.setAutoCommit(true);
    }

    public void rollBack() throws SQLException {
        conn.rollback();
    }

    /**
     * 
     * @param tableName
     * @param Column
     * @param Condition
     * @return
     * @throws SQLException
     *
     */
    private synchronized List<Map<String, Object>> select(String tableName, List<String> Column, List<SqlParam> Condition) throws SQLException {
        log.info("Begin Select");
        if (null == Column || Column.isEmpty()) {
            return null;
        }
        String sqlSelect = SqlTemplate.selectTemplate(tableName, Column, Condition);
//        log.info(sqlSelect);
        stm = conn.prepareStatement(sqlSelect);
        addSqlParam(stm, Condition);
        ResultSet result = stm.executeQuery();
        List<Map<String, Object>> resultMap = convertResultSetToMap(result);
        stm.close();
        return resultMap;
    }

    /**
     * 
     * @param tableName
     * @param column
     * @param Condition
     * @return
     * @throws SQLException
     *
     */
    private List<Map<String, Object>> select(String tableName, String column, List<SqlParam> Condition) throws SQLException {
        List<String> Column = new ArrayList<String>();
        Column.add(column);
        return select(tableName, Column, Condition);
    }

    /**
     * 
     * @param Table Name
     * @param Column List: <Column Name>
     * @param Condition List: <Column Name, Value>
     * @return List record :  <Column Name, Value>
     * @throws SQLException
     */
    public List<Map<String, Object>> select(String tableName, List<String> Column, Map<String, Object> Condition) throws SQLException {
        return select(tableName, Column, convertMapToSqlParam(Condition));
    }

    /**
     * 
     * @param Table Name
     * @param Column  Colum Name
     * @param Condition List: <Column Name, Value>
     * @return List record :  <Column Name, Value>
     * @throws SQLException
     */
    public List<Map<String, Object>> select(String tableName, String Column, Map<String, Object> Condition) throws SQLException {
        return select(tableName, Column, convertMapToSqlParam(Condition));
    }

    /**
     * 
     * @param Table Name
     * @param Column  Colum Name
     * @returnList record :  <Column Name, Value>
     * @throws SQLException
     *
     */
    public List<Map<String, Object>> select(String tableName, String column) throws SQLException {
        List<String> Column = new ArrayList<String>();
        Column.add(column);
        Map<String, Object> Condition = null;
        return select(tableName, Column, Condition);
    }

    /**
     *
     * @param Table Name
     * @param Column List  <Column Name, Value>
     * @throws SQLException
     */
    public synchronized void insert(String tableName, List<Map<String, Object>> Column) throws SQLException {
        log.info("Begin insert...");
        if (tableName == null || tableName.isEmpty()) {
            return;
        }
        if (null == Column || 0 == Column.size()) {
            return;
        }
        List<String> listColumn = new ArrayList<String>();
        ArrayList<SqlParam> param = convertMapToSqlParam(Column.get(0));
        for (int i = 0; i < param.size(); i++) {
            listColumn.add(param.get(i).getName());
        }
        String sqlInsert = SqlTemplate.insertTemplate(tableName, listColumn);
//        log.info(sqlInsert);
        stm = conn.prepareStatement(sqlInsert);
        if (!isTransaction) {
            conn.setAutoCommit(false);
        }
        for (int i = 0; i < Column.size(); i++) {
            ArrayList<SqlParam> paramlist = convertMapToSqlParam(Column.get(i));
            addSqlParam(stm, paramlist);
            stm.addBatch();
        }
        stm.executeBatch();
        if (!isTransaction) {
            conn.commit();
        }
        stm.close();
        log.info("Insert Done!");
    }

    /**
     * 
     * @param Table Name
     * @param Column <Column Name, Value>
     * @throws SQLException
     */
    public void insert(String tableName, Map<String, Object> column) throws SQLException {
        List<Map<String, Object>> Column = new ArrayList<Map<String, Object>>();
        Column.add(column);
        insert(tableName, Column);
    }

    /**
     * 
     * @param tableName
     * @param Column
     * @param Condition
     * @throws SQLException
     */
    private synchronized void update(String tableName, List<SqlParam> Column, List<SqlParam> Condition) throws SQLException {
        log.info("Begin Update...");
        if (null == Column || Column.isEmpty()) {
            return;
        }
        ArrayList<String> column = new ArrayList<String>();
        for (int i = 0; i < Column.size(); i++) {
            column.add(Column.get(i).getName());
        }
        ArrayList<String> cond = new ArrayList<String>();
        for (int i = 0; i < Condition.size(); i++) {
            cond.add(Condition.get(i).getName());
        }
        String sqlUpdate = SqlTemplate.updateTemplate(tableName, column, cond);
//        log.info(sqlUpdate);
        ArrayList<SqlParam> total = new ArrayList<SqlParam>();
        for (int i = 0; i < Column.size(); i++) {
            total.add(Column.get(i));
        }
        for (int i = 0; i < Condition.size(); i++) {
            total.add(Condition.get(i));
        }
        if (!isTransaction) {
            conn.setAutoCommit(false);
        }
        stm = conn.prepareStatement(sqlUpdate);
        addSqlParam(stm, total);
        stm.executeUpdate();
        stm.close();
        if (!isTransaction) {
            conn.commit();
        }
        log.info("Update Done!");
    }

    /**
     * update 
     * @param Table Name
     * @param Column <Column Name, Value>
     * @param Condition <Column Name, Value>
     * @throws SQLException
     */
    public void update(String tableName, Map<String, Object> Column, Map<String, Object> Condition) throws SQLException {
        update(tableName, convertMapToSqlParam(Column), convertMapToSqlParam(Condition));
    }

    /**
     *
     * @param tableName
     * @param condition
     * @throws SQLException
     */
    public void delete(String tableName, List<SqlParam> condition) throws SQLException {
        if (StringUtils.isEmpty(tableName)) {
            return;
        }
        ArrayList<String> cond = new ArrayList<String>();
        if (condition != null) {
            for (int i = 0; i < condition.size(); i++) {
                cond.add(condition.get(i).getName());
            }
        }
        String sqlUpdate = SqlTemplate.deleteTemplate(tableName, cond);
        if (!isTransaction) {
            conn.setAutoCommit(false);
        }
        stm = conn.prepareStatement(sqlUpdate);
        addSqlParam(stm, condition);
        stm.execute();
        if (!isTransaction) {
            conn.commit();
        }
        stm.close();
    }

    /**
     *
     * @param tableName
     * @param condition
     * @throws SQLException
     */
    public void delete(String tableName, Map<String, Object> condition) throws SQLException {
        delete(tableName, convertMapToSqlParam(condition));
    }

    /**
     * 
     * @param tableName
     * @throws SQLException
     */
    public void delete(String tableName) throws SQLException {
        delete(tableName, (List<SqlParam>) null);
    }

    /**
     * 
     * @param stm
     * @param condition
     * @throws SQLException
     */
    public void addSqlParam(PreparedStatement stm, List<SqlParam> condition) throws SQLException {
        if (null == condition || condition.isEmpty()) {
            return;
        }
        SqlParam sql = null;
        Object param;
        int count = 1;
        for (int i = 0; i < condition.size(); i++) {
            // get param
            sql = condition.get(i);
            if (!StringUtils.equals(sql.getName(), CH.ORDER)) {
                param = condition.get(i).getObject();
                // set param
                if (param instanceof String) {
                    stm.setString(count++, String.valueOf(param));
                } else if (param instanceof Integer) {
                    stm.setInt(count++, Integer.parseInt(String.valueOf(param)));
                } else if (param instanceof Timestamp) {
                    stm.setTimestamp(count++, Timestamp.valueOf(String.valueOf(param)));
                } else if (param instanceof java.util.Date) {
                    stm.setTimestamp(count++, new Timestamp(((java.util.Date) param).getTime()));
                } else if (param instanceof Double) {
                    stm.setDouble(count++, Double.parseDouble(String.valueOf(param)));
                } else if (param instanceof java.util.Date) {
                    stm.setDate(count++, convertDateToSqlDate((java.util.Date) param));
                } else if (param instanceof BigDecimal) {
                    stm.setBigDecimal(count++, (BigDecimal) param);
                } else if (param instanceof Float) {
                    stm.setDouble(count++, Float.parseFloat(String.valueOf(param)));
                } else if (param instanceof Long) {
                    stm.setDouble(count++, Float.parseFloat(String.valueOf(param)));
                } else {
                    stm.setString(count++, null);
                }
            }
        }
    }

    /**
     * 
     * @param _result
     * @return
     * @throws SQLException
     */
    protected ArrayList<Map<String, Object>> convertResultSetToMap(ResultSet _result) throws SQLException {
        log.info("START convert");
        ArrayList<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
        Map<String, Object> resultMap = null;
        ResultSetMetaData rsmd = null;
        while (_result.next()) {
            rsmd = _result.getMetaData();
            resultMap = new HashMap<String, Object>();
            for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                resultMap.put(rsmd.getColumnName(i), _result.getObject(i));
            }
            mapList.add(resultMap);
        }
        _result.close();
        log.info("END convert");
        return mapList;
    }

    /**
     * 
     * @param _map
     * @return
     */
    protected ArrayList<SqlParam> convertMapToSqlParam(Map<String, Object> _map) {
        ArrayList<SqlParam> paramList = new ArrayList<SqlParam>();
        SqlParam sqlParam = null;
        String key;
        if (null != _map) {
            Iterator<String> iterator = (_map.keySet()).iterator();
            while (iterator.hasNext()) {
                key = iterator.next().toString();
                sqlParam = new SqlParam();
                sqlParam.setName(key);
                sqlParam.setObject(_map.get(key));
                paramList.add(sqlParam);
            }
        }
        return paramList;
    }

    /**
     * 
     * @param SeqName
     * @return
     * @throws SQLException
     */
    public int seq(String SeqName) throws SQLException {
        stm = conn.prepareStatement("select " + SeqName + ".nextval from dual");
        ResultSet resultSet = stm.executeQuery();
        int id = 0;
        if (resultSet.next()) {
            id = resultSet.getInt(1);
        }
        resultSet.close();
        stm.close();
        return id;
    }

    /**
     * 
     * @return
     * @throws SQLException
     */
    public java.util.Date getServerTime() throws SQLException {
        stm = conn.prepareStatement("SELECT SYSTIMESTAMP FROM DUAL");
        ResultSet resultSet = stm.executeQuery();
        Date date = null;
        if (resultSet.next()) {
            date = resultSet.getDate(1);
        }
        resultSet.close();
        stm.close();
        return date;
    }

    /**
     * 
     * @param util0date
     * @return
     */
    protected java.sql.Date convertDateToSqlDate(java.util.Date utildate) {
        java.sql.Date sqlDate = new java.sql.Date(utildate.getTime());
        return sqlDate;
    }

    /**
     * 
     * @param tableName
     * @throws SQLException
     */
    public void truncate(String tableName) throws SQLException {
        stm = conn.prepareStatement("TRUNCATE TABLE " + tableName);
        stm.execute();
        stm.close();
    }

    /**
     *
     * @param sql
     * @throws SQLException
     */
    public void sql(String sql) throws SQLException {
        stm = conn.prepareStatement(sql);
    }

    /**
     *
     * @param index
     * @param value
     * @throws SQLException
     */
    public void setParameter(int index, Object value) throws SQLException {
        if (value instanceof String) {
            stm.setString(index, String.valueOf(value));
        } else if (value instanceof Integer) {
            stm.setInt(index, Integer.parseInt(String.valueOf(value)));
        } else if (value instanceof Timestamp) {
            stm.setTimestamp(index, Timestamp.valueOf(String.valueOf(value)));
        } else if (value instanceof java.util.Date) {
            stm.setTimestamp(index, new Timestamp(((java.util.Date) value).getTime()));
        } else if (value instanceof Double) {
            stm.setDouble(index, Double.parseDouble(String.valueOf(value)));
        } else if (value instanceof java.util.Date) {
            stm.setDate(index, convertDateToSqlDate((java.util.Date) value));
        } else if (value instanceof BigDecimal) {
            stm.setBigDecimal(index, (BigDecimal) value);
        } else if (value instanceof Float) {
            stm.setDouble(index, Float.parseFloat(String.valueOf(value)));
        } else if (value instanceof Long) {
            stm.setDouble(index, Float.parseFloat(String.valueOf(value)));
        } else {
            stm.setString(index, null);
        }
    }

    /**
     *
     * @throws SQLException
     */
    public void execute() throws SQLException {
        stm.execute();
        stm.close();
    }

    /**
     * 
     * @return
     * @throws SQLException
     */
    public List<Map<String, Object>> executeQuery() throws SQLException {
        ResultSet res = stm.executeQuery();
        List<Map<String, Object>> list = convertResultSetToMap(res);
        stm.close();
        return list;
    }
}
