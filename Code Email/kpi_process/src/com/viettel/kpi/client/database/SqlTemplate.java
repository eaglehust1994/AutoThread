/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.kpi.client.database;

import com.viettel.kpi.client.database.Constants.CH;
import org.apache.commons.lang.StringUtils;
import java.util.List;

/**
 *
 * @author Pham Manh Hung
 */
public class SqlTemplate {

    /**
     *
     * @param tableName
     * @param Column
     * @return insert into TableName(column(1), column(2), column(3),...,column(n)) Values (?,?,?,...,?)
     */
    public static String insertTemplate(String tableName, List<String> Column) {
        if (StringUtils.isEmpty(tableName)) {
            return CH.EMPTY;
        }
        if (null == Column || 0 == Column.size()) {
            return CH.EMPTY;
        }
        StringBuilder insertTemplate = new StringBuilder();
        StringBuilder column = new StringBuilder();
        StringBuilder values = new StringBuilder();
        for (int i = 0; i < Column.size(); i++) {
            column.append(Column.get(i)).append(CH.COMMA);
            values.append(CH.QUESTION).append(CH.COMMA);
        }
        String col = null;
        String val = null;
        if (column.length() > 1) {
            col = column.substring(0, column.length() - 1);
        }
        if (values.length() > 1) {
            val = values.substring(0, values.length() - 1);
        }
        insertTemplate.append("INSERT INTO ").append(tableName).append("(").append(col).append(")").append(" VALUES").append("(").append(val).append(")");
        return insertTemplate.toString();
    }

    /**
     *
     * @param tableName
     * @param column
     * @param Condition
     *         condition la mot list danh sach cac dieu kieu gom kieu dieu kien va dieu kien
     *          kieu dieu kien gom kieu sap xep orderby va condition
     * @return Select column(1), column(2), ..., column(n) from tableName where condition(1)=? and condition(1)=? and ... condition(m)=? order by condtion(x)
     */
    public static String selectTemplate(String tableName, List<String> column, List<SqlParam> Condition) {

        if (StringUtils.isEmpty(tableName)) {
            return CH.EMPTY;
        }

        if (null == column || 0 == column.size()) {
            return CH.EMPTY;
        }

        StringBuilder selectTemplate = new StringBuilder();
        StringBuilder tmpField = new StringBuilder();
        if (!StringUtils.equals(column.get(0), "*")) {
            for (int i = 0; i < column.size(); i++) {
                tmpField.append(column.get(i)).append(CH.COMMA);
            }

            String field = null;
            if (tmpField.length() > 1) {
                field = tmpField.substring(0, tmpField.length() - 1);
            }
            selectTemplate.append("SELECT ").append(field).append(CH.SPACE);
        } else {
            selectTemplate.append("SELECT *").append(CH.SPACE);
        }


        selectTemplate.append(CH.FROM).append(CH.SPACE).append(tableName);
        if (null != Condition && !Condition.isEmpty()) {
            StringBuilder tmpCondition = new StringBuilder();
            StringBuilder tmpOrder = new StringBuilder();
            for (int i = 0; i < Condition.size(); i++) {
                SqlParam condition = Condition.get(i);

                if (StringUtils.equals(condition.getName(), CH.ORDER)) {

                    tmpOrder.append(CH.SPACE).append(CH.ORDER).append(CH.SPACE).append(condition.getObject());

                } else {
                    tmpCondition.append(CH.SPACE).append(condition.getName()).append(" = ").append(CH.QUESTION).append(CH.AND);
                }
            }

            if (tmpCondition.length() > 3) {
                selectTemplate.append(CH.SPACE).append(CH.WHERE).append(tmpCondition.substring(0, tmpCondition.length() - 3));
            }
            if (tmpOrder.length() > 0) {
                selectTemplate.append(tmpOrder.toString());
            }
        }

        return selectTemplate.toString();
    }

    /**
     * 
     * @param tableName
     * @param column
     * @param Condition
     * @return update tableName set column(1)=?, column(2)=?, column(3)=?,...,column(n)=? Where condition(1)=?, condition(2)=?,...,condition(m)=?
     */
    public static String updateTemplate(String tableName, List<String> column, List<String> Condition) {
        if (StringUtils.isEmpty(tableName)) {
            return CH.EMPTY;
        }

        if (null == column || 0 == column.size()) {
            return CH.EMPTY;
        }

        StringBuilder Column = new StringBuilder();
        for (int i = 0; i < column.size(); i++) {
            Column.append(column.get(i)).append("=").append(CH.QUESTION).append(CH.COMMA);
        }
        String col = null;
        if (Column.length() > 1) {
            col = Column.substring(0, Column.length() - 1);
        }

        String con = null;
        if (null != Condition || !Condition.isEmpty()) {
            StringBuilder condition = new StringBuilder();
            for (int i = 0; i < Condition.size(); i++) {
                condition.append(CH.SPACE).append(Condition.get(i)).append("=").append(CH.QUESTION).append(CH.SPACE).append(CH.AND);
            }
            if (condition.length() > 3) {
                con = condition.substring(0, condition.length() - 3);
            }
        }
        StringBuilder updateTemplate = new StringBuilder();

        updateTemplate.append("UPDATE ").append(tableName).append(CH.SPACE).append("SET ").append(col);

        if (!StringUtils.isEmpty(con)) {
            updateTemplate.append(CH.SPACE).append(CH.WHERE).append(con);
        }
        return updateTemplate.toString();
    }

    /**
     * 
     * @param tableName
     * @param Condition
     * @return
     */
    public static String deleteTemplate(String tableName, List<String> Condition) {
        if (StringUtils.isEmpty(tableName)) {
            return CH.EMPTY;
        }

        String con = null;
        if (null != Condition || !Condition.isEmpty()) {
            StringBuilder condition = new StringBuilder();
            for (int i = 0; i < Condition.size(); i++) {
                condition.append(CH.SPACE).append(Condition.get(i)).append("=").append(CH.QUESTION).append(CH.SPACE).append(CH.AND);
            }
            if (condition.length() > 3) {
                con = condition.substring(0, condition.length() - 3);
            }
        }

        StringBuilder deleteTemplate = new StringBuilder();

        deleteTemplate.append("DELETE FROM ").append(tableName);
        if (con != null && !StringUtils.isEmpty(con)) {
            deleteTemplate.append(CH.SPACE).append("WHERE").append(con);
        }
        return deleteTemplate.toString();
    }

    /**
     * 
     * @param tableName
     * @return
     */
    public static String deleteTemplate(String tableName) {
        return deleteTemplate(tableName, null);
    }
}
