/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.ctct;

import com.viettel.framework.service.common.DbTask;
import com.viettel.qldtktts.webservice.CatMerchandiseBO;
import com.viettel.qldtktts.webservice.CatStationBO;
import com.viettel.qldtktts.webservice.CatStationHouseBO;
import com.viettel.qldtktts.webservice.CntContractBO;
import com.viettel.qldtktts.webservice.ConstrConstructionsBO;

import com.viettel.qldtktts.webservice.MerEntityBO;
import com.viettel.qldtktts.webservice.MerInExpNoteDTO;
import com.viettel.qldtktts.webservice.ProductCompanyBO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import org.apache.log4j.Logger;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import org.apache.commons.lang.ArrayUtils;

//  @author hoanm1
public class MyDbTask extends DbTask {

    public synchronized void updateCatStation(List<CatStationBO> lstCatStation, Logger logger, long maxBatchSize, long type) {
        logger.info("UPDATE CAT_STATION START!!!");
        if (lstCatStation != null && !lstCatStation.isEmpty()) {
            int[] updatedResultTotal = null;    //update result 1=updated,0=not updated
            StringBuilder sql = new StringBuilder(" UPDATE CAT_STATION ");
            sql.append(" SET CREATED_DATE = sysdate, TYPE = ? , CODE = ? , ADDRESS = ?, ");
            sql.append(" STATUS = ?, START_POINT_ID = ?, END_POINT_ID = ?,");
            sql.append(" SCOPE = ?, SCOPE_NAME = ?, START_POINT_TYPE = ?, END_POINT_TYPE = ?,");
            sql.append(" PARENT_ID = ?, LATITUDE = ?, LONGITUDE = ?,");
            sql.append(" AREA_LOCATION = ?, CAT_STATION_TYPE_ID = ? , CAT_PROVINCE_ID = ?, CAT_STATION_HOUSE_ID = ?,");
            sql.append(" LINE_TYPE_ID = ?, LINE_LENGTH = ?");
            sql.append(" WHERE CODE = ?");
            PreparedStatement pstmt = null;
            Connection con = null;
            try {
                con = getConnection();
                pstmt = con.prepareStatement(sql.toString());
                int count = 0;
                int size = lstCatStation.size();
                int group = 0;
                int totalAffectedRows = 0;

                for (CatStationBO obj : lstCatStation) {
                    int i = 1;
                    try {
                        group++;
                        count++;
                        pstmt.setString(i++, String.valueOf(type));
                        pstmt.setString(i++, obj.getStationCode());
                        pstmt.setString(i++, obj.getAddress());

                        if (obj.getIsActive() != null) {
                            pstmt.setString(i++, String.valueOf(obj.getIsActive()));
                        } else {
                            pstmt.setNull(i++, Types.NVARCHAR);
                        }

                        if (obj.getStartPointId() != null) {
                            pstmt.setLong(i++, obj.getStartPointId());
                        } else {
                            pstmt.setNull(i++, Types.INTEGER);
                        }
                        if (obj.getEndPointId() != null) {
                            pstmt.setLong(i++, obj.getEndPointId());
                        } else {
                            pstmt.setNull(i++, Types.INTEGER);
                        }
                        if (obj.getScope() != null) {
                            pstmt.setLong(i++, obj.getScope());
                        } else {
                            pstmt.setNull(i++, Types.INTEGER);
                        }
                        pstmt.setString(i++, obj.getScopeName());
                        if (obj.getStartPointType() != null) {
                            pstmt.setString(i++, String.valueOf(obj.getStartPointType()));
                        } else {
                            pstmt.setNull(i++, Types.NVARCHAR);
                        }
                        if (obj.getEndPointType() != null) {
                            pstmt.setString(i++, String.valueOf(obj.getEndPointType()));
                        } else {
                            pstmt.setNull(i++, Types.NVARCHAR);
                        }
                        if (obj.getParentId() != null) {
                            pstmt.setLong(i++, obj.getParentId());
                        } else {
                            pstmt.setNull(i++, Types.INTEGER);
                        }//10
                        if (obj.getLatitude() != null) {
                            pstmt.setLong(i++, obj.getLatitude());
                        } else {
                            pstmt.setNull(i++, Types.INTEGER);
                        }
                        if (obj.getLongitude() != null) {
                            pstmt.setLong(i++, obj.getLongitude());
                        } else {
                            pstmt.setNull(i++, Types.INTEGER);
                        }
                        pstmt.setString(i++, obj.getAreaLocation());
                        if (obj.getCatStationTypeId() != null) {
                            pstmt.setLong(i++, obj.getCatStationTypeId());
                        } else {
                            pstmt.setNull(i++, Types.INTEGER);
                        }
                        if (obj.getProvinceId() != null) {
                            pstmt.setLong(i++, obj.getProvinceId());
                        } else {
                            pstmt.setNull(i++, Types.INTEGER);
                        }
                        if (obj.getCatStationHouseId() != null) {
                            pstmt.setLong(i++, obj.getCatStationHouseId());
                        } else {
                            pstmt.setNull(i++, Types.INTEGER);
                        }

                        if (type == 2) {
                            if (obj.getLineTypeId() != null) {
                                pstmt.setLong(i++, obj.getLineTypeId());
                            } else {
                                pstmt.setNull(i++, Types.INTEGER);
                            }
                            if (obj.getLineLength() != null) {
                                pstmt.setLong(i++, obj.getLineLength());
                            } else {
                                pstmt.setNull(i++, Types.INTEGER);
                            }
                        } else {
                            pstmt.setNull(i++, Types.INTEGER);
                            pstmt.setNull(i++, Types.INTEGER);
                        }
                        pstmt.setString(i++, obj.getStationCode());

                        pstmt.addBatch();

                        int[] updatedResult;    //result of update query
                        int groupAffectedRows;  //number of rows affected per batch executed
                        if (group == maxBatchSize) {
                            logger.info("KTTS EXECUTE BATCH: " + count + "/" + size);
                            groupAffectedRows = 0;

                            updatedResult = pstmt.executeBatch();
                            for (int returnCode : updatedResult) {
                                if (returnCode > 0) {
                                    groupAffectedRows++;
                                }
                            }
                            updatedResultTotal = (int[]) ArrayUtils.addAll(updatedResultTotal, updatedResult);
                            totalAffectedRows += groupAffectedRows;
                            logger.info("ROWS AFFECTED: " + groupAffectedRows + "/" + group);
                            group = 0;
                            con.setAutoCommit(false);
                            this.commit(con);
                        }
                        if (group < maxBatchSize && count == size) {
                            logger.info("KTTS EXCECUTE BATCH: " + count + "/" + size);
                            updatedResult = pstmt.executeBatch();
                            for (int returnCode : updatedResult) {
                                if (returnCode > 0) {
                                    totalAffectedRows++;
                                }
                            }
                            updatedResultTotal = (int[]) ArrayUtils.addAll(updatedResultTotal, updatedResult);
                            logger.info("TOTAL ROWS AFFECTED: " + totalAffectedRows + "/" + size);
                            con.setAutoCommit(false);
                            this.commit(con);
                        }
                    } catch (Exception ex) {
                        logger.info("KTTS INSERT INTO CAT_STATION FAIL!");
                        logger.error(ex.getMessage(), ex);
                    }
                }
            } catch (Exception ex) {
                logger.info("KTTS UPDATE CAT_STATION FAIL!");
                logger.error(ex.getMessage(), ex);
            } finally {
                if (con != null) {
                    try {
                        close(con);
                    } catch (Exception e) {
                        logger.error(e.getMessage(), e);
                    }
                }
                if (pstmt != null) {
                    try {
                        pstmt.close();
                    } catch (Exception ex) {
                        logger.error(ex.getMessage(), ex);
                    }
                }
            }
            insertCatStation(lstCatStation, logger, maxBatchSize, type, updatedResultTotal);
        } else {
            logger.info("List KTTS UPDATE CAT_STATION is empty!");
            return;
        }
        logger.info("UPDATE CAT_STATION FINISH!");

    }

    public synchronized void insertCatStation(List<CatStationBO> lstCatStation, Logger logger, long maxBatchSize, long type, int[] updatedResultTotal) {
        logger.info("INSERT CAT_STATION START!");
        if (lstCatStation != null && !lstCatStation.isEmpty()) {
            StringBuilder sql = new StringBuilder(" INSERT INTO cat_station ");
            sql.append(" (CAT_STATION_ID, CREATED_DATE, TYPE, CODE, ADDRESS, ");
            sql.append(" STATUS, START_POINT_ID, END_POINT_ID,");
            sql.append(" SCOPE , SCOPE_NAME, START_POINT_TYPE, END_POINT_TYPE,");
            sql.append(" PARENT_ID, LATITUDE, LONGITUDE,");
            sql.append(" AREA_LOCATION, CAT_STATION_TYPE_ID, CAT_PROVINCE_ID, CAT_STATION_HOUSE_ID, ");
            sql.append(" LINE_TYPE_ID, LINE_LENGTH)");
            sql.append(" VALUES (");
            sql.append(" ?,SYSDATE,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?");
            sql.append(" )");
            PreparedStatement pstmt = null;
            Connection con = null;
            try {
                con = getConnection();
                pstmt = con.prepareStatement(sql.toString());
                int count = 0;
                int size = lstCatStation.size();
                int group = 0;

                int rowCount = 0;   //row index of updatedResultTotal
                for (CatStationBO obj : lstCatStation) {
                    int i = 1;

                    try {
                        if (updatedResultTotal[rowCount] == 1) {
                            rowCount++;
                            size--; //total number of obj to be inserted

                            if (size != count && group < 2000) {
                                continue;   // if this obj is updated then no need to insert
                            }
                        } else {
                            rowCount++;
                            group++;
                            count++;

                            pstmt.setLong(i++, obj.getId());
                            pstmt.setString(i++, String.valueOf(type));
                            pstmt.setString(i++, obj.getStationCode());
                            pstmt.setString(i++, obj.getAddress());

                            if (obj.getIsActive() != null) {
                                pstmt.setString(i++, String.valueOf(obj.getIsActive()));
                            } else {
                                pstmt.setNull(i++, Types.NVARCHAR);
                            }

                            if (obj.getStartPointId() != null) {
                                pstmt.setLong(i++, obj.getStartPointId());
                            } else {
                                pstmt.setNull(i++, Types.INTEGER);
                            }
                            if (obj.getEndPointId() != null) {
                                pstmt.setLong(i++, obj.getEndPointId());
                            } else {
                                pstmt.setNull(i++, Types.INTEGER);
                            }
                            if (obj.getScope() != null) {
                                pstmt.setLong(i++, obj.getScope());
                            } else {
                                pstmt.setNull(i++, Types.INTEGER);
                            }
                            pstmt.setString(i++, obj.getScopeName());
                            if (obj.getStartPointType() != null) {
                                pstmt.setString(i++, String.valueOf(obj.getStartPointType()));
                            } else {
                                pstmt.setNull(i++, Types.NVARCHAR);
                            }
                            if (obj.getEndPointType() != null) {
                                pstmt.setString(i++, String.valueOf(obj.getEndPointType()));
                            } else {
                                pstmt.setNull(i++, Types.NVARCHAR);
                            }
                            if (obj.getParentId() != null) {
                                pstmt.setLong(i++, obj.getParentId());
                            } else {
                                pstmt.setNull(i++, Types.INTEGER);
                            }//10
                            if (obj.getLatitude() != null) {
                                pstmt.setDouble(i++, obj.getLatitude());
                            } else {
                                pstmt.setNull(i++, Types.DOUBLE);
                            }
                            if (obj.getLongitude() != null) {
                                pstmt.setDouble(i++, obj.getLongitude());
                            } else {
                                pstmt.setNull(i++, Types.DOUBLE);
                            }
                            pstmt.setString(i++, obj.getAreaLocation());
                            if (obj.getCatStationTypeId() != null) {
                                pstmt.setLong(i++, obj.getCatStationTypeId());
                            } else {
                                pstmt.setNull(i++, Types.INTEGER);
                            }
                            if (obj.getProvinceId() != null) {
                                pstmt.setLong(i++, obj.getProvinceId());
                            } else {
                                pstmt.setNull(i++, Types.INTEGER);
                            }
                            if (obj.getCatStationHouseId() != null) {
                                pstmt.setLong(i++, obj.getCatStationHouseId());
                            } else {
                                pstmt.setNull(i++, Types.INTEGER);
                            }
                            //16
                            if (type == 2) {
                                if (obj.getLineTypeId() != null) {
                                    pstmt.setLong(i++, obj.getLineTypeId());
                                } else {
                                    pstmt.setNull(i++, Types.INTEGER);
                                }
                                if (obj.getLineLength() != null) {
                                    pstmt.setLong(i++, obj.getLineLength());
                                } else {
                                    pstmt.setNull(i++, Types.INTEGER);
                                }
                            } else {
                                pstmt.setNull(i++, Types.INTEGER);
                                pstmt.setNull(i++, Types.INTEGER);
                            }
                            pstmt.addBatch();
                        }

                        if (group == maxBatchSize) {
                            logger.info("KTTS INSERTED " + count + "/" + lstCatStation.size());
                            group = 0;
                            pstmt.executeBatch();
                            con.setAutoCommit(false);
                            this.commit(con);
                        }
                        if (group < maxBatchSize && count == size) {
                            logger.info("KTTS INSERTED " + count + "/" + lstCatStation.size());
                            pstmt.executeBatch();
                            con.setAutoCommit(false);
                            this.commit(con);
                        }
                    } catch (Exception ex) {
                        logger.info("KTTS INSERT INTO CAT_STATION FAIL!");
                        logger.error(ex.getMessage(), ex);
                    }
                }
            } catch (Exception ex) {
                logger.info("KTTS INSERT INTO CAT_STATION FAIL!");
                logger.error(ex.getMessage(), ex);
            } finally {
                if (con != null) {
                    try {
                        close(con);
                    } catch (Exception e) {
                        logger.error(e.getMessage(), e);
                    }
                }
                if (pstmt != null) {
                    try {
                        pstmt.close();
                    } catch (Exception ex) {
                        logger.error(ex.getMessage(), ex);
                    }
                }
            }
        } else {
            logger.info("List KTTS UPDATE CAT_STATION is empty!");
            return;
        }
        logger.info("INSERT CAT_STATION FINISH!");
    }

    public void deleteCatStation(List<CatStationBO> lstInfoDevice, Logger logger, long maxBatchSize, long type) {
        logger.info("DELETE START!");
        if (lstInfoDevice != null && !lstInfoDevice.isEmpty()) {
            StringBuilder sql = new StringBuilder(" DELETE FROM CAT_STATION WHERE CAT_STATION_ID = ? ");
            PreparedStatement pstmt = null;
            Connection con = null;
            try {
                con = getConnection();
                pstmt = con.prepareStatement(sql.toString());
                int group = 0;
                for (CatStationBO obj : lstInfoDevice) {
                    int i = 1;
                    try {
                        group++;
                        pstmt.setLong(i++, obj.getId());
                        pstmt.addBatch();
                        if (group >= maxBatchSize) {
                            group = 0;
                            pstmt.executeBatch();
                            pstmt.clearBatch();
                        }
                    } catch (Exception ex) {
                        logger.info("KTTS DELETE INTO CAT_STATION FAIL!");
                        logger.error(ex.getMessage(), ex);
                    }
                }
                pstmt.executeBatch();
                con.setAutoCommit(false);
                this.commit(con);
                logger.info("KTTS DELETE INTO CAT_STATION SUCCESS!");
            } catch (Exception ex) {
                logger.info("KTTS DELETE INTO CAT_STATION FAIL!");
                logger.error(ex.getMessage(), ex);
            } finally {
                if (con != null) {
                    try {
                        close(con);
                    } catch (Exception e) {
                        logger.error(e.getMessage(), e);
                    }
                }
                if (pstmt != null) {
                    try {
                        pstmt.close();
                    } catch (Exception ex) {
                        logger.error(ex.getMessage(), ex);
                    }
                }
            }
        } else {
            logger.info("List KTTS DELETE FROM CAT_STATION is empty!");
            return;
        }
        logger.info("DELETE CAT_STATION FINISH!");
    }

    public synchronized void insertCatStationHouse(List<CatStationHouseBO> lstCatStationHouse, Logger logger, long maxBatchSize, int[] updatedResultTotal) {
        logger.info("INSERT START!");
        if (lstCatStationHouse != null && !lstCatStationHouse.isEmpty()) {

            StringBuilder sql = new StringBuilder(" INSERT INTO CAT_STATION_HOUSE ");
            sql.append(" (CAT_STATION_HOUSE_ID, CREATED_DATE, CODE, ADDRESS, STATUS,");
            sql.append(" CAT_PROVINCE_ID, CAT_STATION_TYPE_HOUSE_ID, LONGTITUE, LATITUDE, AREA_LOCATION)");
            sql.append(" values (? ,SYSDATE, ? , ? , ? , ? , ?, ?, ?, ?)");
            PreparedStatement pstmt = null;
            Connection con = null;
            try {
                con = getConnection();
                pstmt = con.prepareStatement(sql.toString());
                int count = 0;
                int size = lstCatStationHouse.size();
                int group = 0;
                int rowCount = 0;
                for (CatStationHouseBO obj : lstCatStationHouse) {

                    int i = 1;
                    try {
                        if (updatedResultTotal[rowCount] == 1) {
                            rowCount++;
                            size--;
                            if (size != count && group < 2000) {
                                continue;
                            }
                        } else {
                            rowCount++;
                            group++;
                            count++;
                            pstmt.setLong(i++, obj.getCatStationHouseId());
                            pstmt.setString(i++, obj.getStationHouseCode());
                            pstmt.setString(i++, obj.getAddress());
                            if (obj.getIsActive() != null) {
                                pstmt.setString(i++, String.valueOf(obj.getIsActive()));
                            } else {
                                pstmt.setNull(i++, Types.NVARCHAR);
                            }

                            if (obj.getProvinceId() != null) {
                                pstmt.setLong(i++, obj.getProvinceId());
                            } else {
                                pstmt.setLong(i++, -1l);
                            }
                            if (obj.getCatStationHouseTypeId() != null) {
                                pstmt.setLong(i++, obj.getCatStationHouseTypeId());
                            } else {
                                pstmt.setNull(i++, Types.INTEGER);
                            }

                            if (obj.getLongitude() != null) {
                                pstmt.setDouble(i++, obj.getLongitude());
                            } else {
                                pstmt.setNull(i++, Types.DOUBLE);
                            }
                            if (obj.getLatitude() != null) {
                                pstmt.setDouble(i++, obj.getLatitude());
                            } else {
                                pstmt.setNull(i++, Types.DOUBLE);
                            }
                            if (obj.getAreaLocation() != null) {
                                pstmt.setLong(i++, obj.getAreaLocation());
                            } else {
                                pstmt.setNull(i++, Types.INTEGER);
                            }

                            pstmt.addBatch();
                        }

                        if (group == maxBatchSize) {
                            logger.info("KTTS INSERTED " + count + "/" + lstCatStationHouse.size());
                            group = 0;
                            pstmt.executeBatch();
                            con.setAutoCommit(false);
                            this.commit(con);
                        }
                        if (group < maxBatchSize && count == size) {
                            logger.info("KTTS INSERTED " + count + "/" + lstCatStationHouse.size());
                            pstmt.executeBatch();
                            con.setAutoCommit(false);
                            this.commit(con);
                        }
                    } catch (Exception ex) {
                        logger.info("KTTS INSERT INTO CAT_STATION_HOUSE FAIL!");
                        logger.error(ex.getMessage(), ex);
                    }
                }
                pstmt.executeBatch();
                logger.info("TOTAL: KTTS INSERTED " + count + "/" + size);
                con.setAutoCommit(false);
                this.commit(con);
            } catch (Exception ex) {
                logger.info("KTTS INSERT INTO CAT_STATION_HOUSE FAIL!");
                logger.error(ex.getMessage(), ex);
            } finally {
                if (con != null) {
                    try {
                        close(con);
                    } catch (Exception e) {
                        logger.error(e.getMessage(), e);
                    }
                }
                if (pstmt != null) {
                    try {
                        pstmt.close();
                    } catch (Exception ex) {
                        logger.error(ex.getMessage(), ex);
                    }
                }
            }
        } else {
            logger.info("List KTTS INSERT INTO CAT_STATION_HOUSE is empty!");
            return;
        }
        logger.info("INSERT CAT_STATION_HOUSE FINISH!");
    }

    public void deleteCatStationHouse(List<CatStationHouseBO> lstCatStationHouse, Logger logger, long maxBatchSize) {
        logger.info("DELETE START!");
        if (lstCatStationHouse != null && !lstCatStationHouse.isEmpty()) {
            StringBuilder sql = new StringBuilder(" DELETE FROM CAT_STATION_HOUSE WHERE CAT_STATION_HOUSE_ID = ? ");
            PreparedStatement pstmt = null;
            Connection con = null;
            try {
                con = getConnection();
                pstmt = con.prepareStatement(sql.toString());
                int group = 0;
                for (CatStationHouseBO obj : lstCatStationHouse) {
                    int i = 1;
                    try {
                        group++;
                        pstmt.setLong(i++, obj.getCatStationHouseId());
                        pstmt.addBatch();
                        if (group >= maxBatchSize) {
                            group = 0;
                            pstmt.executeBatch();
                            pstmt.clearBatch();
                        }
                    } catch (Exception ex) {
                        logger.info("KTTS DELETE FROM CAT_STATION_HOUSE FAIL!");
                        logger.error(ex.getMessage(), ex);
                    }
                }
                pstmt.executeBatch();
                con.setAutoCommit(false);
                this.commit(con);
                logger.info("KTTS DELETE FROM CAT_STATION_HOUSE SUCCESS!");
            } catch (Exception ex) {
                logger.info("KTTS DELETE FROM CAT_STATION_HOUSE FAIL!");
                logger.error(ex.getMessage(), ex);
            } finally {
                if (con != null) {
                    try {
                        close(con);
                    } catch (Exception e) {
                        logger.error(e.getMessage(), e);
                    }
                }
                if (pstmt != null) {
                    try {
                        pstmt.close();
                    } catch (Exception ex) {
                        logger.error(ex.getMessage(), ex);
                    }
                }
            }
        } else {
            logger.info("List KTTS DELETE FROM CAT_STATION_HOUSE is empty!");
            return;
        }
        logger.info("DELETE CAT_STATION_HOUSE FINISH!");
    }

    public synchronized void insertCntContract(List<CntContractBO> lstCntContract, Logger logger, long maxBatchSize, int[] updatedResultTotal) {
        logger.info("INSERT CNT_CONTRACT START!");

        if (lstCntContract != null && !lstCntContract.isEmpty()) {

            StringBuilder sql = new StringBuilder(" INSERT INTO CNT_CONTRACT");
            sql.append(" (CNT_CONTRACT_ID, CREATED_DATE, CODE, CONTRACT_CODE_KTTS, NAME, CONTENT, SIGN_DATE, START_TIME, END_TIME, PRICE,");
            sql.append(" NUM_STATION, APPENDIX_CONTRACT, BIDDING_PACKAGE_ID, CAT_PARTNER_ID, FORMAL, SYS_GROUP_ID, SYN_STATE, CONTRACT_TYPE, STATUS, NUM_DAY, MONEY_TYPE)");
            sql.append(" values (?, SYSDATE,");
            sql.append(" ? , ? , ? , ?,");
            sql.append(" TO_DATE(?,'YYYY-MM-DD') ,");
            sql.append(" TO_DATE(?,'YYYY-MM-DD') ,");
            sql.append(" TO_DATE(?,'YYYY-MM-DD') ,");
            sql.append(" ?, ?, ?, ?, ?, ?, ?, ?, 0, ?, ?, ? )");
            PreparedStatement pstmt = null;
            Connection con = null;
            try {
                con = getConnection();

                HashMap<String, Long> sysGroupMap = new HashMap<String, Long>();
                String stationSQL1 = "SELECT KTTS_PARTNER_CODE, SYS_GROUP_ID"
                        + " FROM CAT_SYS_GROUP_MAP";
                PreparedStatement stmt1 = con.prepareStatement(stationSQL1, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                ResultSet rs1 = stmt1.executeQuery();
                while (rs1.next()) {
                    Long sysGroupId = rs1.getLong("SYS_GROUP_ID");
                    String kttsPartnerCode = String.valueOf(rs1.getString("KTTS_PARTNER_CODE"));
                    sysGroupMap.put(kttsPartnerCode, sysGroupId);
                }

                stmt1.close();
                rs1.close();

                HashMap<String, Long> catPartnerMap = new HashMap<String, Long>();
                String stationSQL2 = "SELECT KTTS_GROUP_CODE, CAT_PARTNER_ID"
                        + " FROM CAT_PARTNER_MAP";
                PreparedStatement stmt2 = con.prepareStatement(stationSQL2, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                ResultSet rs2 = stmt2.executeQuery();
                while (rs2.next()) {
                    Long catPartnerId = rs2.getLong("CAT_PARTNER_ID");
                    String kttsGroupCode = String.valueOf(rs2.getString("KTTS_GROUP_CODE"));
                    catPartnerMap.put(kttsGroupCode, catPartnerId);
                }

                stmt2.close();
                rs2.close();

                pstmt = con.prepareStatement(sql.toString());
                int count = 0;
                int size = lstCntContract.size();
                int group = 0;
                int rowCount = 0;
                for (CntContractBO obj : lstCntContract) {

                    int i = 1;
                    try {
                        if (updatedResultTotal[rowCount] != 0) {
                            rowCount++;
                            size--;
                            if (size != count && group < 2000) {
                                continue;
                            }
                        } else {
                            rowCount++;
                            group++;
                            count++;
                            //CNT_CONTRACT_ID
                            if (obj.getContractId() != null) {
                                pstmt.setDouble(i++, obj.getContractId());
                            } else {
                                pstmt.setNull(i++, Types.INTEGER);
                            }
                            //CODE
                            pstmt.setString(i++, obj.getCode());
                            //CONTRACT_CODE_KTTS
                            pstmt.setString(i++, obj.getCode());
                            //NAME
                            pstmt.setString(i++, obj.getContractName());
                            //CONTENT
                            pstmt.setString(i++, obj.getContent());
                            //SIGN_DATE
                            if (obj.getSignedDate() != null) {
                                pstmt.setString(i++, String.valueOf(obj.getSignedDate()).substring(0, 10));
                            } else {
                                pstmt.setNull(i++, Types.DATE);
                            }
                            //START_TIME
                            if (obj.getGuaranteeDateFrom() != null) {
                                pstmt.setString(i++, String.valueOf(obj.getGuaranteeDateFrom()).substring(0, 10));
                            } else {
                                pstmt.setNull(i++, Types.DATE);
                            }
                            //END_TIME
                            if (obj.getGuaranteeDateTo() != null) {
                                pstmt.setString(i++, String.valueOf(obj.getGuaranteeDateTo()).substring(0, 10));
                            } else {
                                pstmt.setNull(i++, Types.DATE);
                            }
                            //PRICE
                            if (obj.getTotalValue() != null) {
                                pstmt.setDouble(i++, obj.getTotalValue());
                            } else {
                                pstmt.setNull(i++, Types.DOUBLE);
                            }
                            //NUM_STATION
                            pstmt.setNull(i++, Types.INTEGER);
                            //APPENDIX_CONTRACT
                            pstmt.setNull(i++, Types.INTEGER);
                            pstmt.setNull(i++, Types.INTEGER);
                            //CAT_PARTNER_ID
                            if (obj.getGroupCode() != null) {
                                String groupCodeTemp = obj.getGroupCode();
                                if (catPartnerMap.get(groupCodeTemp) != null) {
                                    pstmt.setLong(i++, catPartnerMap.get(groupCodeTemp));
                                } else {
                                    pstmt.setLong(i++, -1l);
                                }
                            } else {
                                pstmt.setLong(i++, -1l);
                            }
                            //FORMAL
                            if (obj.getContractStyle() != null) {
                                Long t = obj.getContractStyle();
                                Long formalTemp = -1l;
                                if (t == 1) {
                                    formalTemp = 0l;
                                } else if (t == 2) {
                                    formalTemp = 1l;
                                } else if (t == 3) {
                                    formalTemp = 2l;
                                } else if (t == 4) {
                                    formalTemp = 3l;
                                } else if (t == 5 || t == 6) {
                                    formalTemp = 4l;
                                }
                                pstmt.setLong(i++, formalTemp);
                            } else {
                                pstmt.setNull(i++, Types.INTEGER);
                            }

                            //SYS_GROUP_ID
                            if (obj.getPartnerCode() != null) {
                                String partnerCodeTemp = obj.getPartnerCode();
                                if (sysGroupMap.get(partnerCodeTemp) != null) {
                                    pstmt.setLong(i++, sysGroupMap.get(partnerCodeTemp));
                                } else {
                                    pstmt.setLong(i++, -1l);
                                }
                            } else {
                                pstmt.setLong(i++, -1l);
                            }

                            //SYN_STATE
                            pstmt.setLong(i++, 2);
                            //STATUS
                            if (obj.getStatusId() != null) {
                                pstmt.setLong(i++, obj.getStatusId());
                            } else {
                                pstmt.setNull(i++, Types.INTEGER);
                            }

                            //NUM_DAY
                            if (obj.getExecutionTime() != null) {
                                pstmt.setLong(i++, obj.getExecutionTime());
                            } else {
                                pstmt.setNull(i++, Types.INTEGER);
                            }

                            //MONEY_TYPE
                            String moneyTypeCode;
                            moneyTypeCode = obj.getPaymentCurrencyCode();
                            if (moneyTypeCode != null) {
                                if (moneyTypeCode.equalsIgnoreCase("VND")) {
                                    pstmt.setLong(i++, 1l);
                                } else if (moneyTypeCode.equalsIgnoreCase("USD")) {
                                    pstmt.setLong(i++, 2l);
                                } else {
                                    pstmt.setLong(i++, 1l);
                                }
                            } else {
                                pstmt.setLong(i++, 1l);
                            }

                            pstmt.addBatch();
                        }

                        if (group == maxBatchSize) {
                            logger.info("KTTS INSERTED " + count + "/" + lstCntContract.size());
                            group = 0;
                            pstmt.executeBatch();
                            con.setAutoCommit(false);
                            this.commit(con);
                        }
                        if (group < maxBatchSize && count == size) {
                            logger.info("KTTS INSERTED " + count + "/" + lstCntContract.size());
                            pstmt.executeBatch();
                            con.setAutoCommit(false);
                            this.commit(con);
                        }
                    } catch (Exception ex) {
                        logger.info("KTTS INSERT INTO CNT_CONTRACT FAIL!");
                        logger.error(ex.getMessage(), ex);
                    }
                }
                updateCntConstrWorkItemTask(lstCntContract, updatedResultTotal, logger, maxBatchSize, con);
            } catch (Exception ex) {
                logger.info("KTTS INSERT INTO CNT_CONTRACT FAIL!");
                logger.error(ex.getMessage(), ex);
            } finally {
                if (con != null) {
                    try {
                        close(con);
                    } catch (Exception e) {
                        logger.error(e.getMessage(), e);
                    }
                }
                if (pstmt != null) {
                    try {
                        pstmt.close();
                        logger.info("KTTS INSERT INTO CNT_CONTRACT FINISH!");
                    } catch (Exception ex) {
                        logger.error(ex.getMessage(), ex);
                    }
                }
            }
        } else {
            logger.info("List KTTS INSERT INTO CNT_CONTRACT is empty!");
            return;
        }
        logger.info("INSERT CNT_CONTRACT FINISH!");
    }

    public void deleteCntContract(List<CntContractBO> lstCntContract, Logger logger, long maxBatchSize) {
        logger.info("DELETE CNT_CONTRACT START!");
        if (lstCntContract != null && !lstCntContract.isEmpty()) {
            StringBuilder sql = new StringBuilder(" DELETE FROM CNT_CONTRACT WHERE CODE = ? AND SYN_STATE = 2");
            PreparedStatement pstmt = null;
            Connection con = null;
            try {
                con = getConnection();
                pstmt = con.prepareStatement(sql.toString());
                int group = 0;
                for (CntContractBO obj : lstCntContract) {
                    int i = 1;
                    try {
                        group++;
                        pstmt.setString(i++, obj.getCode());
                        pstmt.addBatch();
                        if (group >= maxBatchSize) {
                            group = 0;
                            pstmt.executeBatch();
                            pstmt.clearBatch();
                        }
                    } catch (Exception ex) {
                        logger.info("KTTS DELETE FROM CNT_CONTRACT FAIL!");
                        logger.error(ex.getMessage(), ex);
                    }
                }
                pstmt.executeBatch();
                con.setAutoCommit(false);
                this.commit(con);
                logger.info("KTTS DELETE FROM CNT_CONTRACT SUCCESS!");
            } catch (Exception ex) {
                logger.info("KTTS DELETE FROM CNT_CONTRACT FAIL!");
                logger.error(ex.getMessage(), ex);
            } finally {
                if (con != null) {
                    try {
                        close(con);
                    } catch (Exception e) {
                        logger.error(e.getMessage(), e);
                    }
                }
                if (pstmt != null) {
                    try {
                        pstmt.close();
                    } catch (Exception ex) {
                        logger.error(ex.getMessage(), ex);
                    }
                }
            }
            deleteCntConstrWorkItem(lstCntContract, logger, maxBatchSize);
        } else {
            logger.info("List KTTS DELETE FROM CNT_CONTRACT is empty!");
            return;
        }
        logger.info("DELETE CNT_CONTRACT FINISH!");
    }

    public void deleteCntConstrWorkItem(List<CntContractBO> lstCntContract, Logger logger, long maxBatchSize) {
        logger.info("DELETE CNT_CONSTR_WORK_ITEM_TASK START!");
        if (lstCntContract != null && !lstCntContract.isEmpty()) {
            StringBuilder sql = new StringBuilder(" DELETE FROM CNT_CONSTR_WORK_ITEM_TASK WHERE CNT_CONTRACT_ID = ?");
            PreparedStatement pstmt = null;
            Connection con = null;
            try {
                con = getConnection();
                pstmt = con.prepareStatement(sql.toString());
                int group = 0;
                for (CntContractBO obj : lstCntContract) {
                    int i = 1;
                    try {
                        group++;
                        pstmt.setLong(i++, obj.getContractId());
                        pstmt.addBatch();
                        if (group >= maxBatchSize) {
                            group = 0;
                            pstmt.executeBatch();
                            pstmt.clearBatch();
                        }
                    } catch (Exception ex) {
                        logger.info("KTTS DELETE FROM CNT_CONTRACT FAIL!");
                        logger.error(ex.getMessage(), ex);
                    }
                }
                pstmt.executeBatch();
                con.setAutoCommit(false);
                this.commit(con);
                logger.info("KTTS DELETE FROM CNT_CONSTR_WORK_ITEM_TASK SUCCESS!");
            } catch (Exception ex) {
                logger.info("KTTS DELETE FROM CNT_CONSTR_WORK_ITEM_TASK FAIL!");
                logger.error(ex.getMessage(), ex);
            } finally {
                if (con != null) {
                    try {
                        close(con);
                    } catch (Exception e) {
                        logger.error(e.getMessage(), e);
                    }
                }
                if (pstmt != null) {
                    try {
                        pstmt.close();
                    } catch (Exception ex) {
                        logger.error(ex.getMessage(), ex);
                    }
                }
            }

        } else {
            logger.info("List KTTS DELETE FROM CNT_CONSTR_WORK_ITEM_TASK is empty!");
            return;
        }
        logger.info("DELETE CNT_CONSTR_WORK_ITEM_TASK FINISH!");
    }

    public synchronized void updateProductCompany(List<ProductCompanyBO> lstProductCompany, Logger logger, long maxBatchSize) {
        logger.info("UPDATE CAT_MANUFACTURER START!");
        int[] updatedResultTotal = null;
        if (lstProductCompany != null && !lstProductCompany.isEmpty()) {
            StringBuilder sql = new StringBuilder(" UPDATE CAT_MANUFACTURER");
            sql.append(" SET CODE = ? , NAME = ? , STATUS = ? ");
            sql.append(" WHERE CAT_MANUFACTURER_ID = ?");
            PreparedStatement pstmt = null;
            Connection con = null;
            try {
                con = getConnection();
                pstmt = con.prepareStatement(sql.toString());
                int count = 0;
                int size = lstProductCompany.size();
                int group = 0;
                int totalAffectedRows = 0;
                for (ProductCompanyBO obj : lstProductCompany) {
                    int i = 1;
                    try {
                        group++;
                        count++;

                        pstmt.setString(i++, obj.getCode());
                        pstmt.setString(i++, obj.getName());

                        if (obj.getIsActive() != null) {
                            pstmt.setString(i++, String.valueOf(obj.getIsActive()));
                        } else {
                            pstmt.setNull(i++, Types.NVARCHAR);
                        }

                        if (obj.getCompanyId() != null) {
                            pstmt.setLong(i++, obj.getCompanyId());
                        } else {
                            pstmt.setNull(i++, Types.INTEGER);
                        }

                        pstmt.addBatch();
                        int[] updatedResult;
                        int groupAffectedRows;
                        if (group == maxBatchSize) {
                            logger.info("KTTS EXECUTE BATCH: " + count + "/" + size);
                            groupAffectedRows = 0;
                            updatedResult = pstmt.executeBatch();
                            for (int returnCode : updatedResult) {
                                if (returnCode > 0) {
                                    groupAffectedRows++;
                                }
                            }
                            totalAffectedRows += groupAffectedRows;
                            updatedResultTotal = (int[]) ArrayUtils.addAll(updatedResultTotal, updatedResult);
                            logger.info("ROWS AFFECTED: " + groupAffectedRows + "/" + group);
                            group = 0;
                            con.setAutoCommit(false);
                            this.commit(con);
                        }
                        if (group < maxBatchSize && count == size) {
                            logger.info("KTTS EXCECUTE BATCH: " + count + "/" + size);
                            updatedResult = pstmt.executeBatch();
                            for (int returnCode : updatedResult) {
                                if (returnCode > 0) {
                                    totalAffectedRows++;
                                }
                            }
                            updatedResultTotal = (int[]) ArrayUtils.addAll(updatedResultTotal, updatedResult);
                            logger.info("TOTAL ROWS AFFECTED: " + totalAffectedRows + "/" + size);
                            con.setAutoCommit(false);
                            this.commit(con);
                        }
                    } catch (Exception ex) {
                        logger.info("KTTS UPDATE CAT_MANUFACTURER FAIL!");
                        logger.error(ex.getMessage(), ex);
                    }
                }
            } catch (Exception ex) {
                logger.info("KTTS UPDATE CAT_MANUFACTURER FAIL!");
                logger.error(ex.getMessage(), ex);
            } finally {
                if (con != null) {
                    try {
                        close(con);
                    } catch (Exception e) {
                        logger.error(e.getMessage(), e);
                    }
                }
                if (pstmt != null) {
                    try {
                        pstmt.close();
                    } catch (Exception ex) {
                        logger.error(ex.getMessage(), ex);
                    }
                }
            }
        } else {
            logger.info("List KTTS UPDATE CAT_MANUFACTURER is empty!");
            return;
        }
        logger.info("UPDATE CAT_MANUFACTURER FINISH!");
        insertProductCompany(lstProductCompany, logger, maxBatchSize, updatedResultTotal);
    }

    public synchronized void insertProductCompany(List<ProductCompanyBO> lstProductCompany, Logger logger, long maxBatchSize, int[] updatedResultTotal) {
        logger.info("INSERT CAT_MANUFACTURER START!");
        if (lstProductCompany != null && !lstProductCompany.isEmpty()) {
            StringBuilder sql = new StringBuilder(" INSERT INTO CAT_MANUFACTURER");
            sql.append(" (CAT_MANUFACTURER_ID, CODE ,NAME, STATUS)");
            sql.append(" VALUES (?, ?, ?, ?)");
            PreparedStatement pstmt = null;
            Connection con = null;
            try {
                con = getConnection();
                pstmt = con.prepareStatement(sql.toString());
                int count = 0;
                int size = lstProductCompany.size();
                int group = 0;
                int rowCount = 0;
                for (ProductCompanyBO obj : lstProductCompany) {

                    int i = 1;
                    try {
                        if (updatedResultTotal[rowCount] == 1) {
                            rowCount++;
                            size--;
                            if (size != count && group < 2000) {
                                continue;
                            }
                        } else {
                            rowCount++;
                            group++;
                            count++;

                            if (obj.getCompanyId() != null) {
                                pstmt.setLong(i++, obj.getCompanyId());
                            } else {
                                pstmt.setNull(i++, Types.INTEGER);
                            }

                            pstmt.setString(i++, obj.getCode());
                            pstmt.setString(i++, obj.getName());

                            if (obj.getIsActive() != null) {
                                pstmt.setString(i++, String.valueOf(obj.getIsActive()));
                            } else {
                                pstmt.setNull(i++, Types.NVARCHAR);
                            }
                            pstmt.addBatch();
                        }

                        if (group == maxBatchSize) {
                            logger.info("KTTS INSERTED " + count + "/" + lstProductCompany.size());
                            group = 0;
                            pstmt.executeBatch();
                            con.setAutoCommit(false);
                            this.commit(con);
                        }
                        if (group < maxBatchSize && count == size) {
                            logger.info("KTTS INSERTED " + count + "/" + lstProductCompany.size());
                            pstmt.executeBatch();
                            con.setAutoCommit(false);
                            this.commit(con);
                        }

                    } catch (Exception ex) {
                        logger.info("KTTS INSERT CAT_MANUFACTURER FAIL!");
                        logger.error(ex.getMessage(), ex);
                    }
                }
            } catch (Exception ex) {
                logger.info("KTTS INSERT CAT_MANUFACTURER FAIL!");
                logger.error(ex.getMessage(), ex);
            } finally {
                if (con != null) {
                    try {
                        close(con);
                    } catch (Exception e) {
                        logger.error(e.getMessage(), e);
                    }
                }
                if (pstmt != null) {
                    try {
                        pstmt.close();
                    } catch (Exception ex) {
                        logger.error(ex.getMessage(), ex);
                    }
                }
            }
        } else {
            logger.info("List KTTS INSERT CAT_MANUFACTURER is empty!");
            return;
        }
        logger.info("INSERT CAT_MANUFACTURER FINISH!");
    }

    public void deleteProductCompany(List<ProductCompanyBO> lstProductCompany, Logger logger, long maxBatchSize) {
        logger.info("DELETE CAT_MANUFACTURER START!");
        if (lstProductCompany != null && !lstProductCompany.isEmpty()) {
            StringBuilder sql = new StringBuilder(" DELETE FROM CAT_MANUFACTURER WHERE CAT_MANUFACTURER_ID = ? ");
            PreparedStatement pstmt = null;
            Connection con = null;
            try {
                con = getConnection();
                pstmt = con.prepareStatement(sql.toString());
                int group = 0;
                for (ProductCompanyBO obj : lstProductCompany) {
                    int i = 1;
                    try {
                        group++;
                        pstmt.setLong(i++, obj.getCompanyId());
                        pstmt.addBatch();
                        if (group >= maxBatchSize) {
                            group = 0;
                            pstmt.executeBatch();
                            pstmt.clearBatch();
                        }
                    } catch (Exception ex) {
                        logger.info("KTTS DELETE FROM CAT_MANUFACTURER FAIL!");
                        logger.error(ex.getMessage(), ex);
                    }
                }
                pstmt.executeBatch();
                con.setAutoCommit(false);
                this.commit(con);
                logger.info("KTTS DELETE FROM CAT_MANUFACTURER SUCCESS!");
            } catch (Exception ex) {
                logger.info("KTTS DELETE FROM CAT_MANUFACTURER FAIL!");
                logger.error(ex.getMessage(), ex);
            } finally {
                if (con != null) {
                    try {
                        close(con);
                    } catch (Exception e) {
                        logger.error(e.getMessage(), e);
                    }
                }
                if (pstmt != null) {
                    try {
                        pstmt.close();
                    } catch (Exception ex) {
                        logger.error(ex.getMessage(), ex);
                    }
                }
            }
        } else {
            logger.info("List KTTS DELETE FROM CAT_MANUFACTURER is empty!");
            return;
        }
        logger.info("DELETE CAT_MANUFACTURER FINISH!");
    }

    public synchronized void insertGoods(List<CatMerchandiseBO> lstGoods, Logger logger, long maxBatchSize, int[] updatedResultTotal) {
        logger.info("INSERT GOODS START!");
        if (lstGoods != null && !lstGoods.isEmpty()) {
            StringBuilder sql = new StringBuilder(" INSERT INTO GOODS");
            sql.append(" (GOODS_ID, CREATED_DATE, UPDATED_DATE, CODE, NAME, GOODS_TYPE, UNIT_TYPE,");
            sql.append(" ORIGIN_SIZE, IS_SERIAL, ORIGIN_PRICE, DESCRIPTION, STATUS, CAT_PRODUCING_COUNTRY_ID,");
            sql.append(" PRODUCING_COUNTRY_NAME, CAT_MANUFACTURER_ID, MANUFACTURER_NAME, UNIT_TYPE_NAME)");
            sql.append(" values (?, SYSDATE,");
            sql.append("  TO_DATE(?,'YYYY-MM-DD'),");
            sql.append(" ? , ? , 4 , ? , ? , ? , ? , ? , ? , ? , ? , ? , ?, ?)");
//10
            PreparedStatement pstmt = null;
            Connection con = null;
            try {
                con = getConnection();
                pstmt = con.prepareStatement(sql.toString());
                int count = 0;
                int size = lstGoods.size();
                int group = 0;
                int rowCount = 0;
                for (CatMerchandiseBO obj : lstGoods) {

                    int i = 1;
                    try {
                        if (updatedResultTotal[rowCount] == 1) {
                            rowCount++;
                            size--;
                            if (size != count && group < 2000) {
                                continue;
                            }
                        } else {
                            rowCount++;
                            group++;
                            count++;

                            pstmt.setLong(i++, obj.getGoodsId());

                            if (obj.getUpdatedDate() != null) {
                                pstmt.setString(i++, String.valueOf(obj.getUpdatedDate()).substring(0, 10));
                            } else {
                                pstmt.setNull(i++, Types.DATE);
                            }
                            pstmt.setString(i++, obj.getGoodsCode());
                            pstmt.setString(i++, obj.getGoodsName());
                            
                            if (obj.getGoodsUnitId() != null) {
                                pstmt.setString(i++, String.valueOf(obj.getGoodsUnitId()));
                            } else {
                                pstmt.setNull(i++, Types.NVARCHAR);
                            }
                            pstmt.setString(i++, obj.getOriginalSize());
                            if (obj.getIsDevice() != null) {
                                pstmt.setString(i++, String.valueOf(obj.getIsDevice()));
                            } else {
                                pstmt.setString(i++, "0");
                            }
                            if (obj.getOriginalPrice() != null) {
                                pstmt.setDouble(i++, obj.getOriginalPrice());
                            } else {
                                pstmt.setNull(i++, Types.DOUBLE);
                            }
                            pstmt.setString(i++, obj.getDescription());
                            if (obj.getStatus() != null) {
                                pstmt.setString(i++, String.valueOf(obj.getStatus()));
                            } else {
                                pstmt.setString(i++, "0");
                            }
                            pstmt.setNull(i++, Types.NVARCHAR);
                            pstmt.setNull(i++, Types.NVARCHAR);
                            pstmt.setNull(i++, Types.NVARCHAR);
                            pstmt.setNull(i++, Types.NVARCHAR);

                            pstmt.setString(i++, obj.getGoodsUnitName());

                            pstmt.addBatch();
                        }

                        if (group == maxBatchSize) {
                            logger.info("KTTS INSERTED " + count + "/" + lstGoods.size());
                            group = 0;
                            pstmt.executeBatch();
                            con.setAutoCommit(false);
                            this.commit(con);
                        }
                        if (group < maxBatchSize && count == size) {
                            logger.info("KTTS INSERTED " + count + "/" + lstGoods.size());
                            pstmt.executeBatch();
                            con.setAutoCommit(false);
                            this.commit(con);
                        }
                    } catch (Exception ex) {
                        logger.info("KTTS INSERT INTO GOODS FAIL!");
                        logger.error(ex.getMessage(), ex);
                    }
                }

            } catch (Exception ex) {
                logger.info("KTTS INSERT INTO GOODS FAIL!");
                logger.error(ex.getMessage(), ex);
            } finally {
                if (con != null) {
                    try {
                        close(con);
                    } catch (Exception e) {
                        logger.error(e.getMessage(), e);
                    }
                }
                if (pstmt != null) {
                    try {
                        pstmt.close();
                    } catch (Exception ex) {
                        logger.error(ex.getMessage(), ex);
                    }
                }
            }
        } else {
            logger.info("List KTTS INSERT INTO GOODS is empty!");
            return;
        }
        logger.info("INSERT GOODS FINISH!");
    }

    public void deleteGoods(List<CatMerchandiseBO> lstGoods, Logger logger, long maxBatchSize) {
        logger.info("DELETE GOODS START!");
        if (lstGoods != null && !lstGoods.isEmpty()) {
            StringBuilder sql = new StringBuilder(" DELETE FROM GOODS WHERE GOODS_ID = ? ");
            PreparedStatement pstmt = null;
            Connection con = null;
            try {
                con = getConnection();
                pstmt = con.prepareStatement(sql.toString());
                int group = 0;
                for (CatMerchandiseBO obj : lstGoods) {
                    int i = 1;
                    try {
                        group++;
                        pstmt.setLong(i++, obj.getGoodsId());
                        pstmt.addBatch();
                        if (group >= maxBatchSize) {
                            group = 0;
                            pstmt.executeBatch();
                            pstmt.clearBatch();
                        }
                    } catch (Exception ex) {
                        logger.info("KTTS DELETE FROM GOODS FAIL!");
                        logger.error(ex.getMessage(), ex);
                    }
                }
                pstmt.executeBatch();
                con.setAutoCommit(false);
                this.commit(con);
                logger.info("KTTS DELETE FROM GOODS SUCCESS!");
            } catch (Exception ex) {
                logger.info("KTTS DELETE FROM GOODS FAIL!");
                logger.error(ex.getMessage(), ex);
            } finally {
                if (con != null) {
                    try {
                        close(con);
                    } catch (Exception e) {
                        logger.error(e.getMessage(), e);
                    }
                }
                if (pstmt != null) {
                    try {
                        pstmt.close();
                    } catch (Exception ex) {
                        logger.error(ex.getMessage(), ex);
                    }
                }
            }
        } else {
            logger.info("List KTTS DELETE FROM GOODS is empty!");
            return;
        }
        logger.info("DELETE GOODS FINISH!");
    }

    public synchronized void insertConstruction(List<ConstrConstructionsBO> lstConstruction, Logger logger, long maxBatchSize, int[] updatedResultTotal) {
        logger.info("INSERT CONSTRUCTION START!");
        int[] insertResultTotal = null;
        int rowInserted = 0;
        List<ConstrConstructionsBO> lstConstructionInsert = new ArrayList<ConstrConstructionsBO>();
        if (lstConstruction != null && !lstConstruction.isEmpty()) {
            StringBuilder sql = new StringBuilder(" INSERT INTO CONSTRUCTION");
            sql.append(" (CONSTRUCTION_ID, CODE, NAME, SYS_GROUP_ID, CAT_PARTNER_ID, BROADCASTING_DATE,");
            sql.append(" IS_RETURN, CAT_CONSTRUCTION_DEPLOY_ID,");
            sql.append(" HANDOVER_DATE, STARTING_DATE, ");
            sql.append(" STATUS, REGION, CAT_STATION_ID, CAT_CONSTRUCTION_TYPE_ID, EXCPECTED_STARTING_DATE, DEPLOY_TYPE,created_date)");
            sql.append(" values (?,");
            sql.append("  ? , ? , ? , ? , TO_DATE(?,'YYYY-MM-DD') , ? , ? , TO_DATE(?,'YYYY-MM-DD') , ");
            sql.append(" TO_DATE(?,'YYYY-MM-DD'), ");
            sql.append(" ?, ?, ?, ?,  TO_DATE(?,'YYYY-MM-DD'), ?, sysdate)");
//10

            PreparedStatement pstmt = null;
            Connection con = null;
            try {
                con = getConnection();

                HashMap<String, String> catStationMap = new HashMap<String, String>();
                HashMap<String, Long> catStationMap2 = new HashMap<String, Long>();
                String stationSQL = "SELECT CODE, CAT_STATION_ID, CAT_PROVINCE_ID"
                        + " FROM CAT_STATION";
                PreparedStatement stmt = con.prepareStatement(stationSQL, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    String catStationId = String.valueOf(rs.getLong("CAT_STATION_ID"));
                    String catStationCode = String.valueOf(rs.getString("CODE"));
                    Long catProvinceId = rs.getLong("CAT_PROVINCE_ID");
                    catStationMap.put(catStationCode, catStationId);
                    catStationMap2.put(catStationId, catProvinceId);
                }
                stmt.close();
                rs.close();

                HashMap<Long, Long> sysGroupMap = new HashMap<Long, Long>();
                String configGroupProvinceSQL = "SELECT CAT_PROVINCE_ID, SYS_GROUP_ID"
                        + " FROM CONFIG_GROUP_PROVINCE";
                PreparedStatement stmt2 = con.prepareStatement(configGroupProvinceSQL, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                ResultSet rs2 = stmt2.executeQuery();
                while (rs2.next()) {
                    Long catProvinceId = rs2.getLong("CAT_PROVINCE_ID");
                    Long sysGroupId = rs2.getLong("SYS_GROUP_ID");
                    sysGroupMap.put(catProvinceId, sysGroupId);
                }
                stmt2.close();
                rs2.close();

                pstmt = con.prepareStatement(sql.toString());
                int count = 0;
                int size = lstConstruction.size();
                int group = 0;
                int rowCount = 0;
                for (ConstrConstructionsBO obj : lstConstruction) {

                    int i = 1;
                    try {
                        if (updatedResultTotal[rowCount] == 1) {
                            rowCount++;
                            size--;
                            if (size != count && group < 2000) {
                                continue;
                            }
                        } else {
                            lstConstructionInsert.add(obj);
                            rowCount++;
                            group++;
                            count++;
                            //CONSTRUCTION_ID
                            pstmt.setLong(i++, obj.getConstructId());
                            //CODE
                            pstmt.setString(i++, obj.getConstrtCode());
                            //NAME
                            pstmt.setString(i++, obj.getConstrtName());
                            //GET CONSTR_TYPE ***

                            Long constrTypeTemp = -1l;
                            // CAT_CONSTRUCTION_TYPE_ID
                            if (obj.getCatConstrTypesId() != null) {
                                Long t = obj.getCatConstrTypesId();
                                if (t == 82) {
                                    constrTypeTemp = 1l;
                                } else if (t == 390) {
                                    constrTypeTemp = 2l;
                                } else if (t == 393 || t == 394 || t == 3576 || t == 3556) {
                                    constrTypeTemp = 3l;
                                } else {
                                    constrTypeTemp = 4l;
                                }
                                obj.setCatConstrTypesId(constrTypeTemp);
                            }

                            String catStationId = null;
                            Long catProvinceId = -1l;
                            Long sysGroupIdTemp = -1l;
                            if (obj.getStationCode() != null) {
                                catStationId = catStationMap.get(obj.getStationCode());
                                if (catStationId != null) {
                                    catProvinceId = catStationMap2.get(catStationId);
                                    if (catProvinceId != null) {
                                        sysGroupIdTemp = sysGroupMap.get(catProvinceId);
                                        if (sysGroupIdTemp != null) {
                                            pstmt.setLong(i++, sysGroupIdTemp);
                                        } else {
                                            pstmt.setLong(i++, -1l);
                                        }
                                    } else {
                                        pstmt.setLong(i++, -1l);
                                    }
                                } else {
                                    pstmt.setLong(i++, -1l);
                                }
                            } else {
                                pstmt.setLong(i++, -1l);
                            }
                            obj.setSysGroupId(sysGroupIdTemp);
                            //CAT_PARTNER_ID
                            pstmt.setNull(i++, Types.INTEGER);

                            //BROADCASTING_DATE
                            if (obj.getEmissionDate() != null) {
                                pstmt.setString(i++, String.valueOf(obj.getEmissionDate()).substring(0, 10));
                            } else {
                                pstmt.setNull(i++, Types.DATE);
                            }
                            //IS_RETURN
                            pstmt.setNull(i++, Types.VARCHAR);
                            //CAT_CONSTRUCTION_DEPLOY_ID
                            if (obj.getConstrtForm() != null) {
                                pstmt.setLong(i++, obj.getConstrtForm());
                            } else {
                                pstmt.setNull(i++, Types.INTEGER);
                            }
                            //HANDOVER_DATE
                            if (obj.getHandoverDate() != null) {
                                pstmt.setString(i++, String.valueOf(obj.getHandoverDate()).substring(0, 10));
                            } else {
                                pstmt.setNull(i++, Types.DATE);
                            }
                            //STARTING_DATE
                            if (obj.getStartingDate() != null) {
                                pstmt.setString(i++, String.valueOf(obj.getStartingDate()).substring(0, 10));
                            } else {
                                pstmt.setNull(i++, Types.DATE);
                            }

                            //STATUS
                            if (obj.getStatusId() != null) {
                                Long t = obj.getStatusId();
                                int statusId;
                                if (t == 392 || t == 393) {
                                    statusId = 1;
                                } else if (t == 391 || t == 263) {
                                    statusId = 2;
                                } else if (t == 394 || t == 395 || t == 102 || t == 103) {
                                    statusId = 3;
                                } else if (t == 396 || t == 104 || t == 403 || t == 266) {
                                    statusId = 5;
                                } else if (t == 399 || t == 400 || t == 407 || t == 408 || t == 409 || t == 397 || t == 2450 || t == 105 || t == 406) {
                                    statusId = 6;
                                } else if (t == 410 || t == 412) {
                                    statusId = 8;
                                } else if (t == 405) {
                                    statusId = 0;
                                } else {
                                    statusId = -1;
                                }
                                pstmt.setString(i++, String.valueOf(statusId));
                            } else {
                                pstmt.setNull(i++, Types.NVARCHAR);
                            }
                            //REGION
                            if (obj.getDistrictId() != null) {
                                Long t = obj.getDistrictId();
                                if (t == 123) {
                                    pstmt.setLong(i++, 1l);
                                } else if (t == 181) {
                                    pstmt.setLong(i++, 2l);
                                } else if (t == 182) {
                                    pstmt.setLong(i++, 3l);
                                } else {
                                    pstmt.setLong(i++, -1l);
                                }
                            } else {
                                pstmt.setNull(i++, Types.INTEGER);
                            }

                            //CAT_STATION_ID
                            if (obj.getStationCode() != null) {
                                if (catStationMap.get(obj.getStationCode()) != null) {
                                    pstmt.setLong(i++, Long.parseLong(catStationMap.get(obj.getStationCode())));
                                } else {
                                    if (obj.getStationId() != null) {
                                        pstmt.setLong(i++, obj.getStationId());
                                    } else {
                                        pstmt.setNull(i++, Types.INTEGER);
                                    }
                                }
                            } else {
                                if (obj.getStationId() != null) {
                                    pstmt.setLong(i++, obj.getStationId());
                                } else {
                                    pstmt.setNull(i++, Types.INTEGER);
                                }
                            }

                            // CAT_CONSTRUCTION_TYPE_ID
                            if (obj.getCatConstrTypesId() != null) {
                                Long t = obj.getCatConstrTypesId();

                                if (t == 82) {
                                    constrTypeTemp = 1l;
                                } else if (t == 390) {
                                    constrTypeTemp = 2l;
                                } else if (t == 393 || t == 394 || t == 3576 || t == 3556) {
                                    constrTypeTemp = 3l;
                                } else {
                                    constrTypeTemp = 4l;
                                }
                                pstmt.setLong(i++, constrTypeTemp);
                            } else {
                                pstmt.setNull(i++, Types.INTEGER);
                            }

                            //  EXCPECTED_STARTING_DATE
                            if (obj.getPreStartingDate() != null) {
                                pstmt.setString(i++, String.valueOf(obj.getPreStartingDate()).substring(0, 10));
                            } else {
                                pstmt.setNull(i++, Types.DATE);
                            }

                            //DEPLOY_TYPE   
                            if (obj.getConstrType() != null && (constrTypeTemp == 2l || constrTypeTemp == 3l)) {
                                Long t = obj.getConstrType();
                                Long deployTypeTemp = -1l;
                                if (constrTypeTemp == 2l && t == 1) {
                                    deployTypeTemp = 1l;
                                } else if (constrTypeTemp == 2l && t == 2) {
                                    deployTypeTemp = 2l;
                                } else if (constrTypeTemp == 2l && t == 3) {
                                    deployTypeTemp = 3l;
                                } else if (constrTypeTemp == 2l && t == 5) {
                                    deployTypeTemp = 4l;
                                } else if (constrTypeTemp == 3l && t == 393) {
                                    deployTypeTemp = 3l;
                                } else if (constrTypeTemp == 3l && t == 394) {
                                    deployTypeTemp = 4l;
                                } else if (constrTypeTemp == 3l && t == 3576) {
                                    deployTypeTemp = 2l;
                                } else if (constrTypeTemp == 3l && t == 3556) {
                                    deployTypeTemp = 1l;
                                }
                                pstmt.setLong(i++, deployTypeTemp);
                            } else {
                                pstmt.setNull(i++, Types.INTEGER);
                            }

                            pstmt.addBatch();
                            int[] insertResult;
                        
                            insertResult = pstmt.executeBatch();
                            insertResultTotal = (int[]) ArrayUtils.addAll(insertResultTotal, insertResult);
                            if (insertResult[0] > 0) {
                                rowInserted++;
                            }
                            con.setAutoCommit(false);
                            this.commit(con);
                        }
                            
                            
//                        if (group == maxBatchSize) {
//                            logger.info("KTTS INSERTED " + count + "/" + lstConstruction.size());
//                            group = 0;
//                            insertResult = pstmt.executeBatch();
//                            insertResultTotal = (int[]) ArrayUtils.addAll(insertResultTotal, insertResult);
//                            con.setAutoCommit(false);
//                            this.commit(con);
//                        }
//                        if (group < maxBatchSize && count == size) {
//                            logger.info("KTTS INSERTED " + count + "/" + lstConstruction.size());
//                            insertResult = pstmt.executeBatch();
//                            insertResultTotal = (int[]) ArrayUtils.addAll(insertResultTotal, insertResult);
//                            con.setAutoCommit(false);
//                            this.commit(con);
//                        }
                    } catch (Exception ex) {
                        insertResultTotal = (int[]) ArrayUtils.add(insertResultTotal, 0);
                        logger.info("KTTS INSERT INTO CONSTRUCTION FAIL!");
                        logger.info("OBJECT INDEX: " + lstConstruction.indexOf(obj));
                        logger.info(obj.getConstrtCode());
                        logger.error(ex.getMessage(), ex);
                    }
                    if(lstConstruction.indexOf(obj) == lstConstruction.size()-1) {
                        logger.info("==TOTAL ROW INSERTED: " + rowInserted);
                    }
                }

            } catch (Exception ex) {
                logger.info("KTTS INSERT INTO CONSTRUCTION FAIL!");
                logger.error(ex.getMessage(), ex);
            } finally {
                if (con != null) {
                    try {
                        close(con);
                    } catch (Exception e) {
                        logger.error(e.getMessage(), e);
                    }
                }
                if (pstmt != null) {
                    try {
                        pstmt.close();
                    } catch (Exception ex) {
                        logger.error(ex.getMessage(), ex);
                    }
                }
            }
        } else {
            logger.info("List KTTS INSERT INTO CONSTRUCTION is empty!");
            return;
        }

        insertWorkItem(lstConstructionInsert, insertResultTotal, logger, maxBatchSize);
        logger.info("INSERT CONSTRUCTION FINISH!");
    }

    public void deleteConstruction(List<ConstrConstructionsBO> lstConstruction, Logger logger, long maxBatchSize) {
        logger.info("DELETE CONSTRUCTION START!");
        if (lstConstruction != null && !lstConstruction.isEmpty()) {
            StringBuilder sql = new StringBuilder(" DELETE FROM CONSTRUCTION WHERE CODE = ? ");
            PreparedStatement pstmt = null;
            Connection con = null;
            try {
                con = getConnection();
                pstmt = con.prepareStatement(sql.toString());
                int group = 0;
                for (ConstrConstructionsBO obj : lstConstruction) {
                    int i = 1;
                    try {
                        group++;
                        pstmt.setString(i++, obj.getConstrtCode());
                        pstmt.addBatch();
                        if (group >= maxBatchSize) {
                            group = 0;
                            pstmt.executeBatch();
                            pstmt.clearBatch();
                        }
                    } catch (Exception ex) {
                        logger.info("KTTS DELETE FROM CONSTRUCTION FAIL!");
                        logger.error(ex.getMessage(), ex);
                    }
                }
                pstmt.executeBatch();
                con.setAutoCommit(false);
                this.commit(con);
                logger.info("KTTS DELETE FROM CONSTRUCTION SUCCESS!");
            } catch (Exception ex) {
                logger.info("KTTS DELETE FROM CONSTRUCTION FAIL!");
                logger.error(ex.getMessage(), ex);
            } finally {
                if (con != null) {
                    try {
                        close(con);
                    } catch (Exception e) {
                        logger.error(e.getMessage(), e);
                    }
                }
                if (pstmt != null) {
                    try {
                        pstmt.close();
                    } catch (Exception ex) {
                        logger.error(ex.getMessage(), ex);
                    }
                }
            }
        } else {
            logger.info("List KTTS DELETE FROM CONSTRUCTION is empty!");
            return;
        }
        logger.info("DELETE CONSTRUCTION FINISH!");
    }

    public synchronized void updateWareExpNote(List<MerInExpNoteDTO> lstWareExpNote, Logger logger, long maxBatchSize, int type) {
        logger.info("UPDATE SYN_STOCK_TRANS START!");
        int[] updatedResultTotal = null;
        HashMap<String, String> configUserProvinceMap = null;
        HashMap<String, String> synDetailMap = null;
        if (lstWareExpNote != null && !lstWareExpNote.isEmpty()) {
            StringBuilder sqlStockTrans
                    = new StringBuilder("UPDATE SYN_STOCK_TRANS ");
            sqlStockTrans.append(" SET STOCK_ID             = ?, ");
            sqlStockTrans.append("   CODE                   = ?, ");
            sqlStockTrans.append("   TYPE                   = ?, ");
            sqlStockTrans.append("   BUSSINESS_TYPE         = ?, ");
            sqlStockTrans.append("   BUSSINESS_TYPE_NAME    = ?, ");
            sqlStockTrans.append("   STATUS                 = ?, ");
            sqlStockTrans.append("   CREATED_BY             = ?, ");
            sqlStockTrans.append("   CREATED_BY_NAME        = ?, ");
            sqlStockTrans.append("   CREATED_DATE           = TO_DATE(?,'YYYY-MM-DD'), ");
            sqlStockTrans.append("   CREATED_DEPT_ID        = ?, ");
            sqlStockTrans.append("   CREATED_DEPT_NAME      = ?, ");
            sqlStockTrans.append("   REAL_IE_TRANS_DATE     = TO_DATE(?,'YYYY-MM-DD'), ");
            sqlStockTrans.append("   REAL_IE_USER_NAME      = ?, ");
            sqlStockTrans.append("   SHIPPER_ID             = ?, ");
            sqlStockTrans.append("   LAST_SHIPPER_ID        = ?, ");
            sqlStockTrans.append("   SHIPPER_NAME           = ?, ");
            sqlStockTrans.append("   CANCEL_DATE            = TO_DATE(?,'YYYY-MM-DD'), ");
            sqlStockTrans.append("   CANCEL_BY              = ?, ");
            sqlStockTrans.append("   CANCEL_BY_NAME         = ?, ");
            sqlStockTrans.append("   CANCEL_REASON_NAME     = ?, ");
            sqlStockTrans.append("   ORDER_CODE             = ?, ");
            sqlStockTrans.append("   ORDER_ID               = ?, ");
            sqlStockTrans.append("   CONSTRUCTION_ID        = ?, ");
            sqlStockTrans.append("   DESCRIPTION            = ?, ");
            sqlStockTrans.append("   IN_ROAL                = ?, ");
            sqlStockTrans.append("   CONSTRUCTION_CODE      = ?, ");
            sqlStockTrans.append("   SYN_TRANS_TYPE         = ?, ");
            sqlStockTrans.append("   SHIPMENT_CODE          = ?, ");
            sqlStockTrans.append("   STOCK_CODE             = ? ");
            sqlStockTrans.append(" WHERE SYN_STOCK_TRANS_ID = ? ");
            //27

            PreparedStatement pstmtStockTrans = null;

            Connection con = null;
            try {
                con = getConnection();

                configUserProvinceMap = new HashMap<String, String>();
                String sql3 = "SELECT CAT_PROVINCE_ID, SYS_GROUP_ID, SYS_USER_ID"
                        + " FROM CONFIG_USER_PROVINCE";
                PreparedStatement stmt3 = con.prepareStatement(sql3, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                ResultSet rs3 = stmt3.executeQuery();
                while (rs3.next()) {
                    String catProvinceId = String.valueOf(rs3.getLong("CAT_PROVINCE_ID"));
                    String sysGroupId = String.valueOf(rs3.getLong("SYS_GROUP_ID"));
                    String sysUserId = String.valueOf(rs3.getLong("SYS_USER_ID"));
                    configUserProvinceMap.put(sysGroupId + "|" + catProvinceId, sysUserId);
                }
                stmt3.close();
                rs3.close();

                //get synStockTransId and merEntityId to get synStockTransDetailId later.
                synDetailMap = new HashMap<String, String>();
                String getSynStockTransDetailIdSQL
                        = " SELECT SYN_STOCK_TRANS_DETAIL_ID, SYN_STOCK_TRANS_ID,"
                        + " MER_ENTITY_ID "
                        + " FROM SYN_STOCK_TRANS_DETAIL_SERIAL";
                PreparedStatement pstmt = con.prepareStatement(getSynStockTransDetailIdSQL);
                ResultSet rs4 = pstmt.executeQuery();
                while (rs4.next()) {
                    String synStockTransDetailIdTemp = String.valueOf(rs4.getLong("SYN_STOCK_TRANS_DETAIL_ID"));
                    String synStockTransIdTemp = String.valueOf(rs4.getLong("SYN_STOCK_TRANS_ID"));
                    String merEntityIdTemp = String.valueOf(rs4.getLong("MER_ENTITY_ID"));
                    synDetailMap.put(synStockTransIdTemp + "|" + merEntityIdTemp, synStockTransDetailIdTemp);
                }
                pstmt.close();
                rs4.close();
                //***********

                pstmtStockTrans = con.prepareStatement(sqlStockTrans.toString());

                int count = 0;
                int totalAffectedRows = 0;
                int size = lstWareExpNote.size();
                int group = 0;
                for (MerInExpNoteDTO obj : lstWareExpNote) {
                    int i = 1;
                    try {
                        group++;
                        count++;

                        String constructionId = null;
                        String sysGroupId = null;
                        String catStationId = null;
                        String sql = "SELECT CONSTRUCTION_ID, CODE, SYS_GROUP_ID, CAT_STATION_ID"
                                + " FROM CONSTRUCTION WHERE UPPER(CODE) = UPPER(?)";
                        PreparedStatement stmt = con.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                        stmt.setString(1, obj.getConstrtCode());
                        ResultSet rs = stmt.executeQuery();
                        while (rs.next()) {
                            constructionId = String.valueOf(rs.getLong("CONSTRUCTION_ID"));
                            sysGroupId = String.valueOf(rs.getLong("SYS_GROUP_ID"));
                            catStationId = String.valueOf(rs.getLong("CAT_STATION_ID"));
                        }
                        stmt.close();
                        rs.close();

                        //STOCK_ID
                        pstmtStockTrans.setLong(i++, obj.getSourceWhId());
                        //CODE
                        pstmtStockTrans.setString(i++, obj.getExpNoteCode());
                        //TYPE
                        pstmtStockTrans.setLong(i++, 2);
                        //BUSSINESS_TYPE
                        pstmtStockTrans.setLong(i++, 2);
                        //BUSSINESS_TYPE_NAME
                        pstmtStockTrans.setString(i++, "Xuất ra công trình");
                        //STATUS
                        pstmtStockTrans.setLong(i++, 2l);
                        //CREATED_BY
                        if (obj.getCreatorId() != null) {
                            pstmtStockTrans.setLong(i++, obj.getCreatorId());
                        } else {
                            pstmtStockTrans.setNull(i++, Types.INTEGER);
                        }
                        //CREATED_BY_NAME
                        if (obj.getFullName() != null) {
                            pstmtStockTrans.setString(i++, obj.getFullName());
                        } else {
                            pstmtStockTrans.setString(i++, "");
                        }

                        //CREATED_DATE
                        if (obj.getCreatedDate() != null) {
                            pstmtStockTrans.setString(i++, String.valueOf(obj.getCreatedDate()).substring(0, 10));
                        } else {
                            pstmtStockTrans.setNull(i++, Types.DATE);
                        }
                        //CREATED_DEPT_ID
                        if (obj.getCreNoteGroupId() != null) {
                            pstmtStockTrans.setLong(i++, obj.getCreNoteGroupId());
                        } else {
                            pstmtStockTrans.setNull(i++, Types.INTEGER);
                        }
                        //CREATED_DEPT_NAME
                        pstmtStockTrans.setString(i++, obj.getCreatedSysGroupName());
                        //REAL_IE_TRANS_DATE
                        if (obj.getActualExpDate() != null) {
                            pstmtStockTrans.setString(i++, String.valueOf(obj.getActualExpDate()).substring(0, 10));
                        } else {
                            pstmtStockTrans.setNull(i++, Types.DATE);
                        }
                        //REAL_IE_USER_NAME
                        if (obj.getActualExpBy() != null) {
                            pstmtStockTrans.setLong(i++, obj.getActualExpBy());
                        } else {
                            pstmtStockTrans.setNull(i++, Types.INTEGER);
                        }

                        //SHIPPER_ID
                        String catProvinceId = null;
                        String sql2 = "SELECT CAT_PROVINCE_ID"
                                + " FROM CAT_STATION WHERE CAT_STATION_ID = ?";
                        PreparedStatement stmt2 = con.prepareStatement(sql2, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                        stmt2.setLong(1, Long.parseLong(catStationId));
                        ResultSet rs2 = stmt2.executeQuery();
                        while (rs2.next()) {
                            catProvinceId = String.valueOf(rs2.getLong("CAT_PROVINCE_ID"));
                        }
                        stmt2.close();
                        rs2.close();
                        String sysUserIdTemp = configUserProvinceMap.get(sysGroupId + "|" + catProvinceId);
                        if (sysUserIdTemp == null) {
                            pstmtStockTrans.setLong(i++, -1l);
                        } else {
                            pstmtStockTrans.setLong(i++, Long.parseLong(sysUserIdTemp));
                        }

                        //LAST_SHIPPER_ID
                        if (sysUserIdTemp == null) {
                            pstmtStockTrans.setLong(i++, -1l);
                        } else {
                            pstmtStockTrans.setLong(i++, Long.parseLong(sysUserIdTemp));
                        }

                        //SHIPPER_NAME
                        pstmtStockTrans.setString(i++, obj.getReceiverName());
                        //CANCEL_DATE
                        if (obj.getDelConfirmDate() != null) {
                            pstmtStockTrans.setString(i++, String.valueOf(obj.getDelConfirmDate()).substring(0, 10));
                        } else {
                            pstmtStockTrans.setNull(i++, Types.DATE);
                        }
                        //CANCEL_BY
                        if (obj.getUserIdDel() != null) {
                            pstmtStockTrans.setLong(i++, obj.getUserIdDel());
                        } else {
                            pstmtStockTrans.setNull(i++, Types.INTEGER);
                        }
                        //CANCEL_BY_NAME
                        pstmtStockTrans.setString(i++, obj.getCancelByName());
                        //CANCEL_REASON_NAME
                        pstmtStockTrans.setString(i++, obj.getDeleteDisapproveNoteCause());
                        //ORDER_CODE
                        pstmtStockTrans.setString(i++, obj.getExpReqCode());
                        //ORDER_ID
                        if (obj.getExpReqId() != null) {
                            pstmtStockTrans.setLong(i++, obj.getExpReqId());
                        } else {
                            pstmtStockTrans.setNull(i++, Types.INTEGER);
                        }

                        //CONSTRUCTION_ID
                        if (obj.getConstrtCode() != null) {
                            if (constructionId != null) {
                                pstmtStockTrans.setLong(i++, Long.parseLong(constructionId));
                            } else {
                                if (obj.getConstructionId() != null) {
                                    pstmtStockTrans.setLong(i++, obj.getConstructionId());
                                } else {
                                    pstmtStockTrans.setNull(i++, Types.INTEGER);
                                }
                            }
                        } else {
                            if (obj.getConstructionId() != null) {
                                pstmtStockTrans.setLong(i++, obj.getConstructionId());
                            } else {
                                pstmtStockTrans.setNull(i++, Types.INTEGER);
                            }
                        }

                        //DESCRIPTION
                        pstmtStockTrans.setString(i++, obj.getDescription());
                        //IN_ROAL
                        if (obj.getReceiveConfirm() != null) {
                            pstmtStockTrans.setString(i++, String.valueOf(obj.getReceiveConfirm()));
                        } else {
                            pstmtStockTrans.setNull(i++, Types.NVARCHAR);
                        }
                        //CONSTRUCTION_CODE
                        pstmtStockTrans.setString(i++, obj.getConstrtCode());
                        //SYN_TRANS_TYPE
                        pstmtStockTrans.setString(i++, "1");
                        //SHIPMENT_CODE
                        pstmtStockTrans.setString(i++, obj.getShipmentNo());
                        //STOCK_CODE
                        pstmtStockTrans.setString(i++, obj.getSourceWhCode());
                        //SYN_STOCK_TRANS_ID
                        pstmtStockTrans.setLong(i++, obj.getDeliveryNoteId());
                        pstmtStockTrans.addBatch();
                        int[] updatedResult;
                        int groupAffectedRows;
                        if (group == maxBatchSize) {
                            logger.info("KTTS EXECUTE BATCH: " + count + "/" + size);
                            groupAffectedRows = 0;
                            updatedResult = pstmtStockTrans.executeBatch();
                            for (int returnCode : updatedResult) {
                                if (returnCode > 0) {
                                    groupAffectedRows++;
                                }
                            }
                            totalAffectedRows += groupAffectedRows;
                            updatedResultTotal = (int[]) ArrayUtils.addAll(updatedResultTotal, updatedResult);
                            logger.info("ROWS AFFECTED: " + groupAffectedRows + "/" + group);
                            group = 0;
                            con.setAutoCommit(false);
                            this.commit(con);
                        }
                        if (group < maxBatchSize && count == size) {
                            logger.info("KTTS EXCECUTE BATCH: " + count + "/" + size);
                            updatedResult = pstmtStockTrans.executeBatch();
                            for (int returnCode : updatedResult) {
                                if (returnCode > 0) {
                                    totalAffectedRows++;
                                }
                            }
                            updatedResultTotal = (int[]) ArrayUtils.addAll(updatedResultTotal, updatedResult);
                            logger.info("TOTAL ROWS AFFECTED: " + totalAffectedRows + "/" + size);
                            con.setAutoCommit(false);
                            this.commit(con);
                        }
                    } catch (Exception ex) {
                        logger.info("KTTS UPDATE SYN_STOCK_TRANS FAIL!");
                        logger.error(ex.getMessage(), ex);
                    }
                }

                updateSynStockTransDetailSerial(lstWareExpNote, 0l, logger, maxBatchSize, type, synDetailMap, con);
                updateSynStockTransDetail(lstWareExpNote, logger, maxBatchSize, type, con);
                updatedDetailId(logger, maxBatchSize, con);

            } catch (Exception ex) {
                logger.info("KTTS UPDATE SYN_STOCK_TRANS FAIL!");
                logger.error(ex.getMessage(), ex);
            } finally {
                if (con != null) {
                    try {
                        close(con);
                    } catch (Exception e) {
                        logger.error(e.getMessage(), e);
                    }
                }
                if (pstmtStockTrans != null) {
                    try {
                        pstmtStockTrans.close();
                    } catch (Exception ex) {
                        logger.error(ex.getMessage(), ex);
                    }
                }
            }

        } else {
            logger.info("List KTTS UPDATE SYN_STOCK_TRANS is empty!");
            return;
        }
        logger.info("UPDATE SYN_STOCK_TRANS FINISH!");
        insertWareExpNote(lstWareExpNote, logger, maxBatchSize, updatedResultTotal, type, synDetailMap, configUserProvinceMap);

    }

    public void deleteWareExpNote(List<MerInExpNoteDTO> lstWareExpNote, Logger logger, long maxBatchSize, int type) {
        logger.info("DELETE SYN_STOCK_TRANS START!");
        if (lstWareExpNote != null && !lstWareExpNote.isEmpty()) {
            StringBuilder sql = new StringBuilder(" DELETE FROM SYN_STOCK_TRANS WHERE SYN_STOCK_TRANS_ID = ? ");
            PreparedStatement pstmt = null;
            Connection con = null;
            try {
                con = getConnection();
                pstmt = con.prepareStatement(sql.toString());
                int group = 0;
                for (MerInExpNoteDTO obj : lstWareExpNote) {
                    int i = 1;
                    try {
                        group++;
                        if (type == 1) {
                            pstmt.setLong(i++, obj.getReceiptNoteId());
                        } else {
                            pstmt.setLong(i++, obj.getDeliveryNoteId());
                        }

                        pstmt.addBatch();
                        if (group >= maxBatchSize) {
                            group = 0;
                            pstmt.executeBatch();
                            pstmt.clearBatch();
                        }
                    } catch (Exception ex) {
                        logger.info("KTTS DELETE FROM SYN_STOCK_TRANS FAIL!");
                        logger.error(ex.getMessage(), ex);
                    }
                }
                pstmt.executeBatch();
                con.setAutoCommit(false);
                this.commit(con);
                logger.info("KTTS DELETE FROM SYN_STOCK_TRANS SUCCESS!");
            } catch (Exception ex) {
                logger.info("KTTS DELETE FROM SYN_STOCK_TRANS FAIL!");
                logger.error(ex.getMessage(), ex);
            } finally {
                if (con != null) {
                    try {
                        close(con);
                    } catch (Exception e) {
                        logger.error(e.getMessage(), e);
                    }
                }
                if (pstmt != null) {
                    try {
                        pstmt.close();
                    } catch (Exception ex) {
                        logger.error(ex.getMessage(), ex);
                    }
                }
            }

            deleteSynStockTransDetailSerial(lstWareExpNote, logger, maxBatchSize, type);
            deleteSynStockTransDetail(lstWareExpNote, logger, maxBatchSize, type);
        } else {
            logger.info("List KTTS DELETE FROM SYN_STOCK_TRANS is empty!");
            return;
        }
        logger.info("DELETE SYN_STOCK_TRANS FINISH!");
    }

    public void deleteSynStockTransDetailSerial(List<MerInExpNoteDTO> lstWareExpNote, Logger logger, long maxBatchSize, int type) {
        logger.info("DELETE SYN_STOCK_TRANS_DETAIL_SERIAL START!");
        if (lstWareExpNote != null && !lstWareExpNote.isEmpty()) {
            StringBuilder sql = new StringBuilder(" DELETE FROM SYN_STOCK_TRANS_DETAIL_SERIAL WHERE SYN_STOCK_TRANS_ID = ? ");
            PreparedStatement pstmt = null;
            Connection con = null;
            try {
                con = getConnection();
                pstmt = con.prepareStatement(sql.toString());
                int group = 0;
                for (MerInExpNoteDTO obj : lstWareExpNote) {
                    int i = 1;
                    try {
                        group++;
                        if (type == 2) {
                            pstmt.setLong(i++, obj.getDeliveryNoteId());
                        } else {
                            pstmt.setLong(i++, obj.getReceiptNoteId());
                        }
                        pstmt.addBatch();
                        if (group >= maxBatchSize) {
                            group = 0;
                            pstmt.executeBatch();
                            pstmt.clearBatch();
                        }
                    } catch (Exception ex) {
                        logger.info("KTTS DELETE FROM SYN_STOCK_TRANS_DETAIL_SERIAL FAIL!");
                        logger.error(ex.getMessage(), ex);
                    }
                }
                pstmt.executeBatch();
                con.setAutoCommit(false);
                this.commit(con);
                logger.info("KTTS DELETE FROM SYN_STOCK_TRANS_DETAIL_SERIAL SUCCESS!");
            } catch (Exception ex) {
                logger.info("KTTS DELETE FROM SYN_STOCK_TRANS_DETAIL_SERIAL FAIL!");
                logger.error(ex.getMessage(), ex);
            } finally {
                if (con != null) {
                    try {
                        close(con);
                    } catch (Exception e) {
                        logger.error(e.getMessage(), e);
                    }
                }
                if (pstmt != null) {
                    try {
                        pstmt.close();
                    } catch (Exception ex) {
                        logger.error(ex.getMessage(), ex);
                    }
                }
            }
        } else {
            logger.info("List KTTS DELETE FROM SYN_STOCK_TRANS_DETAIL_SERIAL is empty!");
            return;
        }
        logger.info("DELETE SYN_STOCK_TRANS_DETAIL_SERIAL FINISH!");
    }

    public void deleteSynStockTransDetail(List<MerInExpNoteDTO> lstWareExpNote, Logger logger, long maxBatchSize, int type) {
        logger.info("DELETE SYN_STOCK_TRANS_DETAIL START!");
        if (lstWareExpNote != null && !lstWareExpNote.isEmpty()) {
            StringBuilder sql = new StringBuilder(" DELETE FROM SYN_STOCK_TRANS_DETAIL WHERE SYN_STOCK_TRANS_ID = ? ");
            PreparedStatement pstmt = null;
            Connection con = null;
            try {
                con = getConnection();
                pstmt = con.prepareStatement(sql.toString());
                int group = 0;
                for (MerInExpNoteDTO obj : lstWareExpNote) {
                    int i = 1;
                    try {
                        group++;
                        if (type == 2) {
                            pstmt.setLong(i++, obj.getDeliveryNoteId());
                        } else {
                            pstmt.setLong(i++, obj.getReceiptNoteId());
                        }
                        pstmt.addBatch();
                        if (group >= maxBatchSize) {
                            group = 0;
                            pstmt.executeBatch();
                            pstmt.clearBatch();
                        }
                    } catch (Exception ex) {
                        logger.info("KTTS DELETE FROM SYN_STOCK_TRANS_DETAIL_SERIAL FAIL!");
                        logger.error(ex.getMessage(), ex);
                    }
                }
                pstmt.executeBatch();
                con.setAutoCommit(false);
                this.commit(con);
                logger.info("KTTS DELETE FROM SYN_STOCK_TRANS_DETAIL_SERIAL SUCCESS!");
            } catch (Exception ex) {
                logger.info("KTTS DELETE FROM SYN_STOCK_TRANS_DETAIL_SERIAL FAIL!");
                logger.error(ex.getMessage(), ex);
            } finally {
                if (con != null) {
                    try {
                        close(con);
                    } catch (Exception e) {
                        logger.error(e.getMessage(), e);
                    }
                }
                if (pstmt != null) {
                    try {
                        pstmt.close();
                    } catch (Exception ex) {
                        logger.error(ex.getMessage(), ex);
                    }
                }
            }
        } else {
            logger.info("List KTTS DELETE FROM SYN_STOCK_TRANS_DETAIL_SERIAL is empty!");
            return;
        }
        logger.info("DELETE SYN_STOCK_TRANS_DETAIL_SERIAL FINISH!");
    }

    public synchronized void updateCatStationHouse(List<CatStationHouseBO> lstCatStationHouse, Logger logger, long maxBatchSize) {
        logger.info("UPDATE CAT_STATION_HOUSE START!");
        int[] updatedResultTotal = null;
        if (lstCatStationHouse != null && !lstCatStationHouse.isEmpty()) {

            StringBuilder sql = new StringBuilder(" UPDATE CAT_STATION_HOUSE ");
            sql.append(" SET CREATED_DATE = SYSDATE, CODE = ?, ADDRESS = ?, STATUS = ?, CAT_STATION_TYPE_ID = ?,");
            sql.append(" CAT_PROVINCE_ID = ?, CAT_STATION_TYPE_HOUSE_ID = ?, LONGTITUE = ?, LATITUDE = ?, AREA_LOCATION = ? ");
            sql.append(" WHERE CAT_STATION_HOUSE_ID = ? ");
            PreparedStatement pstmt = null;
            Connection con = null;
            try {
                con = getConnection();
                pstmt = con.prepareStatement(sql.toString());
                int count = 0;
                int size = lstCatStationHouse.size();
                int group = 0;
                int totalAffectedRows = 0;
                for (CatStationHouseBO obj : lstCatStationHouse) {
                    int i = 1;
                    try {
                        group++;
                        count++;
                        pstmt.setString(i++, obj.getStationHouseCode());
                        pstmt.setString(i++, obj.getAddress());
                        if (obj.getIsActive() != null) {
                            pstmt.setString(i++, String.valueOf(obj.getIsActive()));
                        } else {
                            pstmt.setNull(i++, Types.NVARCHAR);
                        }

                        pstmt.setLong(i++, 84);
                        if (obj.getProvinceId() != null) {
                            pstmt.setLong(i++, obj.getProvinceId());
                        } else {
                            pstmt.setLong(i++, -1l);
                        }
                        if (obj.getCatStationHouseTypeId() != null) {
                            pstmt.setLong(i++, obj.getCatStationHouseTypeId());
                        } else {
                            pstmt.setNull(i++, Types.INTEGER);
                        }
                        if (obj.getLongitude() != null) {
                            pstmt.setDouble(i++, obj.getLongitude());
                        } else {
                            pstmt.setNull(i++, Types.DOUBLE);
                        }
                        if (obj.getLatitude() != null) {
                            pstmt.setDouble(i++, obj.getLatitude());
                        } else {
                            pstmt.setNull(i++, Types.DOUBLE);
                        }

                        if (obj.getAreaLocation() != null) {
                            pstmt.setLong(i++, obj.getAreaLocation());
                        } else {
                            pstmt.setNull(i++, Types.INTEGER);
                        }

                        pstmt.setLong(i++, obj.getCatStationHouseId());

                        pstmt.addBatch();
                        int[] updatedResult;
                        int groupAffectedRows;
                        if (group == maxBatchSize) {
                            logger.info("KTTS EXECUTE BATCH: " + count + "/" + size);
                            groupAffectedRows = 0;
                            updatedResult = pstmt.executeBatch();
                            for (int returnCode : updatedResult) {
                                if (returnCode > 0) {
                                    groupAffectedRows++;
                                }
                            }
                            totalAffectedRows += groupAffectedRows;
                            updatedResultTotal = (int[]) ArrayUtils.addAll(updatedResultTotal, updatedResult);
                            logger.info("ROWS AFFECTED: " + groupAffectedRows + "/" + group);
                            group = 0;
                            con.setAutoCommit(false);
                            this.commit(con);
                        }
                        if (group < maxBatchSize && count == size) {
                            logger.info("KTTS EXCECUTE BATCH: " + count + "/" + size);
                            updatedResult = pstmt.executeBatch();
                            for (int returnCode : updatedResult) {
                                if (returnCode > 0) {
                                    totalAffectedRows++;
                                }
                            }
                            updatedResultTotal = (int[]) ArrayUtils.addAll(updatedResultTotal, updatedResult);
                            logger.info("TOTAL ROWS AFFECTED: " + totalAffectedRows + "/" + size);
                            con.setAutoCommit(false);
                            this.commit(con);
                        }
                    } catch (Exception ex) {
                        logger.info("KTTS UPDATE CAT_STATION_HOUSE FAIL!");
                        logger.error(ex.getMessage(), ex);
                    }
                }

            } catch (Exception ex) {
                logger.info("KTTS UPDATE CAT_STATION_HOUSE FAIL!");
                logger.error(ex.getMessage(), ex);
            } finally {
                if (con != null) {
                    try {
                        close(con);
                    } catch (Exception e) {
                        logger.error(e.getMessage(), e);
                    }
                }
                if (pstmt != null) {
                    try {
                        pstmt.close();
                    } catch (Exception ex) {
                        logger.error(ex.getMessage(), ex);
                    }
                }
            }
        } else {
            logger.info("List KTTS UPDATE CAT_STATION_HOUSE is empty!");
            return;
        }
        insertCatStationHouse(lstCatStationHouse, logger, maxBatchSize, updatedResultTotal);
        logger.info("UPDATE CAT_STATION_HOUSE FINISH!");
    }

    public synchronized void updateCntContract(List<CntContractBO> lstCntContract, Logger logger, long maxBatchSize) {
        logger.info("UPDATE CNT_CONTRACT START!");
        int[] updatedResultTotal = null;
        if (lstCntContract != null && !lstCntContract.isEmpty()) {

            StringBuilder sql = new StringBuilder(" UPDATE CNT_CONTRACT");
            sql.append(" SET CREATED_DATE = SYSDATE, NAME = ?, CONTRACT_CODE_KTTS = ?,");
            sql.append(" START_TIME = TO_DATE(?,'YYYY-MM-DD') , END_TIME = TO_DATE(?,'YYYY-MM-DD') , PRICE = ?,");
            sql.append(" NUM_STATION = ?, APPENDIX_CONTRACT = ?, BIDDING_PACKAGE_ID = ?,");
            sql.append(" CAT_PARTNER_ID = ?, FORMAL = ?, SYS_GROUP_ID = ?, SYN_STATE = 2, CONTRACT_TYPE = 0,");
            sql.append(" NUM_DAY = ?, MONEY_TYPE = ? ");
            sql.append(" WHERE CODE = ? AND STATUS != 0");
            PreparedStatement pstmt = null;
            Connection con = null;
            try {
                con = getConnection();

                HashMap<String, Long> catPartnerMap = new HashMap<String, Long>();
                String stationSQL2 = "SELECT KTTS_GROUP_CODE, CAT_PARTNER_ID"
                        + " FROM CAT_PARTNER_MAP";
                PreparedStatement stmt2 = con.prepareStatement(stationSQL2, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                ResultSet rs2 = stmt2.executeQuery();
                while (rs2.next()) {
                    Long catPartnerId = rs2.getLong("CAT_PARTNER_ID");
                    String kttsGroupCode = String.valueOf(rs2.getString("KTTS_GROUP_CODE"));
                    catPartnerMap.put(kttsGroupCode, catPartnerId);
                }

                stmt2.close();
                rs2.close();

                HashMap<String, Long> sysGroupMap = new HashMap<String, Long>();
                String stationSQL1 = "SELECT KTTS_PARTNER_CODE, SYS_GROUP_ID"
                        + " FROM CAT_SYS_GROUP_MAP";
                PreparedStatement stmt1 = con.prepareStatement(stationSQL1, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                ResultSet rs1 = stmt1.executeQuery();
                while (rs1.next()) {
                    Long sysGroupId = rs1.getLong("SYS_GROUP_ID");
                    String sysGroupCode = String.valueOf(rs1.getString("KTTS_PARTNER_CODE"));
                    sysGroupMap.put(sysGroupCode, sysGroupId);
                }

                stmt1.close();
                rs1.close();

                pstmt = con.prepareStatement(sql.toString());
                int count = 0;
                int size = lstCntContract.size();
                int group = 0;
                int totalAffectedRows = 0;
                for (CntContractBO obj : lstCntContract) {
                    int i = 1;
                    try {
                        group++;
                        count++;
                        //NAME
                        pstmt.setString(i++, obj.getContractName());
                        //CONTRACT_CODE_KTTS
                        pstmt.setString(i++, obj.getCode());

                        //START_TIME
                        if (obj.getExpectStartDate() != null) {
                            pstmt.setString(i++, String.valueOf(obj.getExpectStartDate()).substring(0, 10));
                        } else {
                            pstmt.setNull(i++, Types.DATE);
                        }
                        //END_TIME
                        if (obj.getGuaranteeDateTo() != null) {
                            pstmt.setString(i++, String.valueOf(obj.getGuaranteeDateTo()).substring(0, 10));
                        } else {
                            pstmt.setNull(i++, Types.DATE);
                        }
                        //PRICE
                        if (obj.getTotalValue() != null) {
                            pstmt.setDouble(i++, obj.getTotalValue());
                        } else {
                            pstmt.setNull(i++, Types.DOUBLE);
                        }
                        //NUM_STATION
                        pstmt.setNull(i++, Types.INTEGER);
                        //APPENDIX_CONTRACT
                        pstmt.setNull(i++, Types.INTEGER);
                        //BIDDING_PACKAGE_ID
                        pstmt.setNull(i++, Types.INTEGER);
                        //CAT_PARTNER_ID
                        if (obj.getGroupCode() != null) {
                            String groupCodeTemp = obj.getGroupCode();
                            if (catPartnerMap.get(groupCodeTemp) != null) {
                                pstmt.setLong(i++, catPartnerMap.get(groupCodeTemp));
                            } else {
                                pstmt.setLong(i++, -1l);
                            }
                        } else {
                            pstmt.setLong(i++, -1l);
                        }
                        //FORMAL
                        if (obj.getContractStyle() != null) {
                            Long t = obj.getContractStyle();
                            Long formalTemp = -1l;
                            if (t == 1) {
                                formalTemp = 0l;
                            } else if (t == 2) {
                                formalTemp = 1l;
                            } else if (t == 3) {
                                formalTemp = 2l;
                            } else if (t == 4) {
                                formalTemp = 3l;
                            } else if (t == 5 || t == 6) {
                                formalTemp = 4l;
                            }
                            pstmt.setLong(i++, formalTemp);
                        } else {
                            pstmt.setNull(i++, Types.INTEGER);
                        }
                        //SYS_GROUP_ID
                        if (obj.getPartnerCode() != null) {
                            String signGroupCodeTemp = obj.getPartnerCode();
                            if (sysGroupMap.get(signGroupCodeTemp) != null) {
                                pstmt.setLong(i++, sysGroupMap.get(signGroupCodeTemp));
                            } else {
                                pstmt.setLong(i++, -1l);
                            }
                        } else {
                            pstmt.setLong(i++, -1l);
                        }

                        //NUM_DAY
                        if (obj.getExecutionTime() != null) {
                            pstmt.setLong(i++, obj.getExecutionTime());
                        } else {
                            pstmt.setNull(i++, Types.INTEGER);
                        }

                        //MONEY_TYPE
                        String moneyTypeCode;
                        moneyTypeCode = obj.getPaymentCurrencyCode();
                        if (moneyTypeCode != null) {
                            if (moneyTypeCode.equalsIgnoreCase("VND")) {
                                pstmt.setLong(i++, 1l);
                            } else if (moneyTypeCode.equalsIgnoreCase("USD")) {
                                pstmt.setLong(i++, 2l);
                            } else {
                                pstmt.setLong(i++, 1l);
                            }
                        } else {
                            pstmt.setLong(i++, 1l);
                        }

                        //CODE
                        pstmt.setString(i++, obj.getCode());

                        pstmt.addBatch();
                        int[] updatedResult = null;
                        int groupAffectedRows;
                        if (group == maxBatchSize) {
                            logger.info("KTTS EXECUTE BATCH: " + count + "/" + size);
                            groupAffectedRows = 0;
                            updatedResult = pstmt.executeBatch();
                            for (int returnCode : updatedResult) {
                                if (returnCode > 0) {
                                    groupAffectedRows++;
                                }
                            }
                            totalAffectedRows += groupAffectedRows;
                            updatedResultTotal = (int[]) ArrayUtils.addAll(updatedResultTotal, updatedResult);
                            logger.info("ROWS AFFECTED: " + groupAffectedRows + "/" + group);
                            group = 0;
                            con.setAutoCommit(false);
                            this.commit(con);
                        }
                        if (group < maxBatchSize && count == size) {
                            logger.info("KTTS EXCECUTE BATCH: " + count + "/" + size);
                            updatedResult = pstmt.executeBatch();
                            for (int returnCode : updatedResult) {
                                if (returnCode > 0) {
                                    totalAffectedRows++;
                                }
                            }
                            updatedResultTotal = (int[]) ArrayUtils.addAll(updatedResultTotal, updatedResult);
                            logger.info("TOTAL ROWS AFFECTED: " + totalAffectedRows + "/" + size);
                            con.setAutoCommit(false);
                            this.commit(con);
                        }
                    } catch (Exception ex) {
                        logger.info("KTTS UPDATE CNT_CONTRACT FAIL!");
                        logger.error(ex.getMessage(), ex);
                    }
                }
                updateCntConstrWorkItemTask(lstCntContract, updatedResultTotal, logger, maxBatchSize, con);

            } catch (Exception ex) {
                logger.info("KTTS UPDATE CNT_CONTRACT FAIL!");
                logger.error(ex.getMessage(), ex);
            } finally {
                if (con != null) {
                    try {
                        close(con);
                    } catch (Exception e) {
                        logger.error(e.getMessage(), e);
                    }
                }
                if (pstmt != null) {
                    try {
                        pstmt.close();
                    } catch (Exception ex) {
                        logger.error(ex.getMessage(), ex);
                    }
                }
            }
        } else {
            logger.info("List KTTS UPDATE CNT_CONTRACT is empty!");
            return;
        }
        logger.info("UPDATE CNT_CONTRACT FINISH!");
        insertCntContract(lstCntContract, logger, maxBatchSize, updatedResultTotal);
    }

    public synchronized void updateConstruction(List<ConstrConstructionsBO> lstConstruction, Logger logger, long maxBatchSize) {
        logger.info("UPDATE CONSTRUCTION START!");
        int[] updatedResultTotal = null;
        HashMap<String, String> catStationMap = null;
        if (lstConstruction != null && !lstConstruction.isEmpty()) {
            StringBuilder sql = new StringBuilder(" UPDATE CONSTRUCTION");
            sql.append(" SET CODE = ?, NAME = ?, CAT_PARTNER_ID = ?, BROADCASTING_DATE = TO_DATE(?,'YYYY-MM-DD'),");
            sql.append(" IS_RETURN = ?, CAT_CONSTRUCTION_DEPLOY_ID = ?,");
            sql.append(" HANDOVER_DATE = TO_DATE(?,'YYYY-MM-DD'), STARTING_DATE = TO_DATE(?,'YYYY-MM-DD'), ");
            sql.append(" REGION = ?, CAT_STATION_ID = ?, CAT_CONSTRUCTION_TYPE_ID = ?, EXCPECTED_STARTING_DATE = TO_DATE(?,'YYYY-MM-DD'), DEPLOY_TYPE = ?,updated_date = sysdate "); // MOI + REGION
            sql.append(" WHERE CONSTRUCTION_ID = ?");
            PreparedStatement pstmt = null;
            Connection con = null;
            try {
                con = getConnection();

                catStationMap = new HashMap<String, String>();
                String stationSQL = "SELECT CODE, CAT_STATION_ID"
                        + " FROM CAT_STATION";
                PreparedStatement stmt = con.prepareStatement(stationSQL, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    String catStationId = String.valueOf(rs.getLong("CAT_STATION_ID"));
                    String catStationCode = String.valueOf(rs.getString("CODE"));
                    catStationMap.put(catStationCode, catStationId);
                }
                stmt.close();
                rs.close();

                pstmt = con.prepareStatement(sql.toString());
                int size = lstConstruction.size();
                int totalAffectedRows = 0;
                for (ConstrConstructionsBO obj : lstConstruction) {
                    int i = 1;
                    try {
                        //CODE
                        pstmt.setString(i++, obj.getConstrtCode());
                        //NAME
                        pstmt.setString(i++, obj.getConstrtName());

                        //GET CONSTR_TYPE ***
                        Long constrTypeTemp = -1l;
                        // CAT_CONSTRUCTION_TYPE_ID
                        if (obj.getCatConstrTypesId() != null) {
                            Long t = obj.getCatConstrTypesId();
                            if (t == 82) {
                                constrTypeTemp = 1l;
                            } else if (t == 390) {
                                constrTypeTemp = 2l;
                            } else if (t == 393 || t == 394 || t == 3576 || t == 3556) {
                                constrTypeTemp = 3l;
                            } else {
                                constrTypeTemp = 4l;
                            }
                        }

                        //CAT_PARTNER_ID
                        pstmt.setNull(i++, Types.CHAR);

                        //BROADCASTING_DATE
                        if (obj.getEmissionDate() != null) {
                            pstmt.setString(i++, String.valueOf(obj.getEmissionDate()).substring(0, 10));
                        } else {
                            pstmt.setNull(i++, Types.DATE);
                        }
                        //IS_RETURN
                        pstmt.setNull(i++, Types.VARCHAR);
                        //CAT_CONSTRUCTION_DEPLOY_ID
                        if (obj.getConstrtForm() != null) {
                            pstmt.setLong(i++, obj.getConstrtForm());
                        } else {
                            pstmt.setNull(i++, Types.INTEGER);
                        }
                        //HANDOVER_DATE
                        if (obj.getHandoverDate() != null) {
                            pstmt.setString(i++, String.valueOf(obj.getHandoverDate()).substring(0, 10));
                        } else {
                            pstmt.setNull(i++, Types.DATE);
                        }
                        //STARTING_DATE
                        if (obj.getStartingDate() != null) {
                            pstmt.setString(i++, String.valueOf(obj.getStartingDate()).substring(0, 10));
                        } else {
                            pstmt.setNull(i++, Types.DATE);
                        }
                        //REGION
                        if (obj.getDistrictId() != null) {
                            Long t = obj.getDistrictId();
                            if (t == 123) {
                                pstmt.setLong(i++, 1l);
                            } else if (t == 181) {
                                pstmt.setLong(i++, 2l);
                            } else if (t == 182) {
                                pstmt.setLong(i++, 3l);
                            } else {
                                pstmt.setLong(i++, -1l);
                            }
                        } else {
                            pstmt.setNull(i++, Types.INTEGER);
                        }

                        //CAT_STATION_ID
                        if (obj.getStationCode() != null) {
                            if (catStationMap.get(obj.getStationCode()) != null) {
                                pstmt.setLong(i++, Long.parseLong(catStationMap.get(obj.getStationCode())));
                            } else {
                                if (obj.getStationId() != null) {
                                    pstmt.setLong(i++, obj.getStationId());
                                } else {
                                    pstmt.setNull(i++, Types.INTEGER);
                                }
                            }
                        } else {
                            if (obj.getStationId() != null) {
                                pstmt.setLong(i++, obj.getStationId());
                            } else {
                                pstmt.setNull(i++, Types.INTEGER);
                            }
                        }

                        // CAT_CONSTRUCTION_TYPE_ID
                        if (obj.getCatConstrTypesId() != null) {
                            Long t = obj.getCatConstrTypesId();
                            if (t == 82) {
                                constrTypeTemp = 1l;
                            } else if (t == 390) {
                                constrTypeTemp = 2l;
                            } else if (t == 393 || t == 394 || t == 3576 || t == 3556) {
                                constrTypeTemp = 3l;
                            } else {
                                constrTypeTemp = 4l;
                            }
                            pstmt.setLong(i++, constrTypeTemp);
                        } else {
                            pstmt.setNull(i++, Types.INTEGER);
                        }

                        //  EXCPECTED_STARTING_DATE
                        if (obj.getPreStartingDate() != null) {
                            pstmt.setString(i++, String.valueOf(obj.getPreStartingDate()).substring(0, 10));
                        } else {
                            pstmt.setNull(i++, Types.DATE);
                        }

                        //DEPLOY_TYPE   
                        if (obj.getConstrType() != null && (constrTypeTemp == 2l || constrTypeTemp == 3l)) {
                            Long t = obj.getConstrType();
                            Long deployTypeTemp = -1l;
                            if (constrTypeTemp == 2l && t == 1) {
                                deployTypeTemp = 1l;
                            } else if (constrTypeTemp == 2l && t == 2) {
                                deployTypeTemp = 2l;
                            } else if (constrTypeTemp == 2l && t == 3) {
                                deployTypeTemp = 3l;
                            } else if (constrTypeTemp == 2l && t == 5) {
                                deployTypeTemp = 4l;
                            } else if (constrTypeTemp == 3l && t == 393) {
                                deployTypeTemp = 3l;
                            } else if (constrTypeTemp == 3l && t == 394) {
                                deployTypeTemp = 4l;
                            } else if (constrTypeTemp == 3l && t == 3576) {
                                deployTypeTemp = 2l;
                            } else if (constrTypeTemp == 3l && t == 3556) {
                                deployTypeTemp = 1l;
                            }
                            pstmt.setLong(i++, deployTypeTemp);
                        } else {
                            pstmt.setNull(i++, Types.INTEGER);
                        }
                        //CONSTRUCTION_ID
                        pstmt.setLong(i++, obj.getConstructId());

                        pstmt.addBatch();
                        int[] updatedResult;
                        updatedResult = pstmt.executeBatch();
                        con.setAutoCommit(false);
                        this.commit(con);
                        for (int returnCode : updatedResult) {
                            if (returnCode > 0) {
                                totalAffectedRows++;
                            }
                        }
                        updatedResultTotal = (int[]) ArrayUtils.addAll(updatedResultTotal, updatedResult);
                        if( lstConstruction.indexOf(obj) == lstConstruction.size() - 1) {
                            logger.info("TOTAL ROWS AFFECTED: " + totalAffectedRows + "/" + size);
                        }
                    } catch (Exception ex) {
                        logger.info("KTTS UPDATE CONSTRUCTION FAIL!");
                        updatedResultTotal = (int[]) ArrayUtils.add(updatedResultTotal, 0);
                        logger.error(ex.getMessage(), ex);
                    }
                }

            } catch (Exception ex) {
                logger.info("KTTS UPDATE CONSTRUCTION FAIL!");
                logger.error(ex.getMessage(), ex);
            } finally {
                if (con != null) {
                    try {
                        close(con);
                    } catch (Exception e) {
                        logger.error(e.getMessage(), e);
                    }
                }
                if (pstmt != null) {
                    try {
                        pstmt.close();
                    } catch (Exception ex) {
                        logger.error(ex.getMessage(), ex);
                    }
                }
            }
        } else {
            logger.info("List KTTS UPDATE CONSTRUCTION is empty!");
            return;
        }

        updateConstructionByCode(lstConstruction, logger, maxBatchSize, updatedResultTotal, catStationMap);
        logger.info("UPDATE CONSTRUCTION FINISH!");
    }

    public synchronized void updateConstructionByCode(List<ConstrConstructionsBO> lstConstruction, Logger logger, long maxBatchSize,
            int[] updatedResultTotal, HashMap<String, String> catStationMap) {
        int[] updatedByCodeResultTotal = null;
        int[] updatedResult;

        logger.info("UPDATE CONSTRUCTION BY CODE START!");
        if (lstConstruction != null && !lstConstruction.isEmpty()) {
            StringBuilder sql = new StringBuilder(" UPDATE CONSTRUCTION");
            sql.append(" SET CODE = ?, NAME = ?, CAT_PARTNER_ID = ?, BROADCASTING_DATE = TO_DATE(?,'YYYY-MM-DD'),");
            sql.append(" IS_RETURN = ?, CAT_CONSTRUCTION_DEPLOY_ID = ?,");
            sql.append(" HANDOVER_DATE = TO_DATE(?,'YYYY-MM-DD'), STARTING_DATE = TO_DATE(?,'YYYY-MM-DD'), ");
            sql.append(" REGION = ?, CAT_STATION_ID = ?, CAT_CONSTRUCTION_TYPE_ID = ?, EXCPECTED_STARTING_DATE = TO_DATE(?,'YYYY-MM-DD'), DEPLOY_TYPE = ?,updated_date = sysdate "); // MOI + REGION
            sql.append(" WHERE upper(CODE) = ?");
//10
            PreparedStatement pstmt = null;
            Connection con = null;
            try {
                con = getConnection();

                pstmt = con.prepareStatement(sql.toString());
                int size = lstConstruction.size();
                int rowCount = 0;
                int totalAffectedRows = 0;
                for (ConstrConstructionsBO obj : lstConstruction) {
                    int i = 1;
                    try {
                        if (updatedResultTotal[rowCount] == 1) {
                            updatedByCodeResultTotal = (int[]) ArrayUtils.add(updatedByCodeResultTotal, 1);
                            rowCount++;
                        } else {
                            rowCount++;
                            //CODE
                            pstmt.setString(i++, obj.getConstrtCode());
                            //NAME
                            pstmt.setString(i++, obj.getConstrtName());

                            //GET CONSTR_TYPE ***
                            Long constrTypeTemp = -1l;
                            // CAT_CONSTRUCTION_TYPE_ID
                            if (obj.getCatConstrTypesId() != null) {
                                Long t = obj.getCatConstrTypesId();
                                if (t == 82) {
                                    constrTypeTemp = 1l;
                                } else if (t == 390) {
                                    constrTypeTemp = 2l;
                                } else if (t == 393 || t == 394 || t == 3576 || t == 3556) {
                                    constrTypeTemp = 3l;
                                } else {
                                    constrTypeTemp = 4l;
                                }
                            }

                            //CAT_PARTNER_ID
                            pstmt.setNull(i++, Types.CHAR);

                            //BROADCASTING_DATE
                            if (obj.getEmissionDate() != null) {
                                pstmt.setString(i++, String.valueOf(obj.getEmissionDate()).substring(0, 10));
                            } else {
                                pstmt.setNull(i++, Types.DATE);
                            }
                            //IS_RETURN
                            pstmt.setNull(i++, Types.VARCHAR);
                            //CAT_CONSTRUCTION_DEPLOY_ID
                            if (obj.getConstrtForm() != null) {
                                pstmt.setLong(i++, obj.getConstrtForm());
                            } else {
                                pstmt.setNull(i++, Types.INTEGER);
                            }
                            //HANDOVER_DATE
                            if (obj.getHandoverDate() != null) {
                                pstmt.setString(i++, String.valueOf(obj.getHandoverDate()).substring(0, 10));
                            } else {
                                pstmt.setNull(i++, Types.DATE);
                            }
                            //STARTING_DATE
                            if (obj.getStartingDate() != null) {
                                pstmt.setString(i++, String.valueOf(obj.getStartingDate()).substring(0, 10));
                            } else {
                                pstmt.setNull(i++, Types.DATE);
                            }
                            //REGION
                            if (obj.getDistrictId() != null) {
                                Long t = obj.getDistrictId();
                                if (t == 123) {
                                    pstmt.setLong(i++, 1l);
                                } else if (t == 181) {
                                    pstmt.setLong(i++, 2l);
                                } else if (t == 182) {
                                    pstmt.setLong(i++, 3l);
                                } else {
                                    pstmt.setLong(i++, -1l);
                                }
                            } else {
                                pstmt.setNull(i++, Types.INTEGER);
                            }

                            //CAT_STATION_ID
                            if (obj.getStationCode() != null) {
                                if (catStationMap.get(obj.getStationCode()) != null) {
                                    pstmt.setLong(i++, Long.parseLong(catStationMap.get(obj.getStationCode())));
                                } else {
                                    if (obj.getStationId() != null) {
                                        pstmt.setLong(i++, obj.getStationId());
                                    } else {
                                        pstmt.setNull(i++, Types.INTEGER);
                                    }
                                }
                            } else {
                                if (obj.getStationId() != null) {
                                    pstmt.setLong(i++, obj.getStationId());
                                } else {
                                    pstmt.setNull(i++, Types.INTEGER);
                                }
                            }

                            // CAT_CONSTRUCTION_TYPE_ID
                            if (obj.getCatConstrTypesId() != null) {
                                Long t = obj.getCatConstrTypesId();
                                if (t == 82) {
                                    constrTypeTemp = 1l;
                                } else if (t == 390) {
                                    constrTypeTemp = 2l;
                                } else if (t == 393 || t == 394 || t == 3576 || t == 3556) {
                                    constrTypeTemp = 3l;
                                } else {
                                    constrTypeTemp = 4l;
                                }
                                pstmt.setLong(i++, constrTypeTemp);
                            } else {
                                pstmt.setNull(i++, Types.INTEGER);
                            }

                            //  EXCPECTED_STARTING_DATE
                            if (obj.getPreStartingDate() != null) {
                                pstmt.setString(i++, String.valueOf(obj.getPreStartingDate()).substring(0, 10));
                            } else {
                                pstmt.setNull(i++, Types.DATE);
                            }

                            //DEPLOY_TYPE   
                            if (obj.getConstrType() != null && (constrTypeTemp == 2l || constrTypeTemp == 3l)) {
                                Long t = obj.getConstrType();
                                Long deployTypeTemp = -1l;
                                if (constrTypeTemp == 2l && t == 1) {
                                    deployTypeTemp = 1l;
                                } else if (constrTypeTemp == 2l && t == 2) {
                                    deployTypeTemp = 2l;
                                } else if (constrTypeTemp == 2l && t == 3) {
                                    deployTypeTemp = 3l;
                                } else if (constrTypeTemp == 2l && t == 5) {
                                    deployTypeTemp = 4l;
                                } else if (constrTypeTemp == 3l && t == 393) {
                                    deployTypeTemp = 3l;
                                } else if (constrTypeTemp == 3l && t == 394) {
                                    deployTypeTemp = 4l;
                                } else if (constrTypeTemp == 3l && t == 3576) {
                                    deployTypeTemp = 2l;
                                } else if (constrTypeTemp == 3l && t == 3556) {
                                    deployTypeTemp = 1l;
                                }
                                pstmt.setLong(i++, deployTypeTemp);
                            } else {
                                pstmt.setNull(i++, Types.INTEGER);
                            }
                            //CODE
                            pstmt.setString(i++, String.valueOf(obj.getConstrtCode()).toUpperCase());

                            pstmt.addBatch();
                            updatedResult = pstmt.executeBatch();
                            con.setAutoCommit(false);
                            this.commit(con);
                            for (int returnCode : updatedResult) {
                                if (returnCode > 0) {
                                    totalAffectedRows++;
                                }
                            }
                            updatedByCodeResultTotal = (int[]) ArrayUtils.addAll(updatedByCodeResultTotal, updatedResult);
                        }
                           
                        if (lstConstruction.indexOf(obj) == lstConstruction.size() - 1) {
                            logger.info("TOTAL ROWS AFFECTED: " + totalAffectedRows + "/" + size);
                        }
                    } catch (Exception ex) {
                        logger.info("KTTS UPDATE CONSTRUCTION BY CODE FAIL!");
                        logger.error(ex.getMessage(), ex);
                    }
                }

            } catch (Exception ex) {
                logger.info("KTTS UPDATE CONSTRUCTION BY CODE FAIL!");
                logger.error(ex.getMessage(), ex);
            } finally {
                if (con != null) {
                    try {
                        close(con);
                    } catch (Exception e) {
                        logger.error(e.getMessage(), e);
                    }
                }
                if (pstmt != null) {
                    try {
                        pstmt.close();
                    } catch (Exception ex) {
                        logger.error(ex.getMessage(), ex);
                    }
                }
            }
        } else {
            logger.info("List KTTS UPDATE CONSTRUCTION BY CODE is empty!");
            return;
        }
        insertConstruction(lstConstruction, logger, maxBatchSize, updatedByCodeResultTotal);
        logger.info("UPDATE CONSTRUCTION FINISH!");
    }

    public synchronized void updateGoods(List<CatMerchandiseBO> lstGoods, Logger logger, long maxBatchSize) {
        logger.info("UPDATE GOODS START!");
        int[] updatedResultTotal = null;
        if (lstGoods != null && !lstGoods.isEmpty()) {
            StringBuilder sql = new StringBuilder(" UPDATE GOODS");
            sql.append(" SET CREATED_DATE = SYSDATE, UPDATED_DATE = TO_DATE(?,'YYYY-MM-DD'), CODE = ?,");
            sql.append(" NAME = ?, GOODS_TYPE = 4, UNIT_TYPE = ?, UNIT_TYPE_NAME = ?, ");
            sql.append(" ORIGIN_SIZE = ?, IS_SERIAL = ?, ORIGIN_PRICE = ?, DESCRIPTION = ?,");
            sql.append(" STATUS = ?, CAT_PRODUCING_COUNTRY_ID = ?,");
            sql.append(" PRODUCING_COUNTRY_NAME = ?, CAT_MANUFACTURER_ID = ?, MANUFACTURER_NAME = ?");
            sql.append(" WHERE CODE = ? ");
//10
            PreparedStatement pstmt = null;
            Connection con = null;
            try {
                con = getConnection();
                pstmt = con.prepareStatement(sql.toString());
                int count = 0;
                int size = lstGoods.size();
                int group = 0;
                int totalAffectedRows = 0;
                for (CatMerchandiseBO obj : lstGoods) {
                    int i = 1;
                    try {
                        group++;
                        count++;

                        if (obj.getUpdatedDate() != null) {
                            pstmt.setString(i++, String.valueOf(obj.getUpdatedDate()).substring(0, 10));
                        } else {
                            pstmt.setNull(i++, Types.DATE);
                        }
                        pstmt.setString(i++, obj.getGoodsCode());
                        pstmt.setString(i++, obj.getGoodsName());
                        
                        if (obj.getGoodsUnitId() != null) {
                            pstmt.setString(i++, String.valueOf(obj.getGoodsUnitId()));
                        } else {
                            pstmt.setNull(i++, Types.NVARCHAR);
                        }

                        pstmt.setString(i++, obj.getGoodsUnitName());
                        pstmt.setString(i++, obj.getOriginalSize());
                        if (obj.getIsDevice() != null) {
                            pstmt.setString(i++, String.valueOf(obj.getIsDevice()));
                        } else {
                            pstmt.setString(i++, "0");
                        }
                        if (obj.getOriginalPrice() != null) {
                            pstmt.setDouble(i++, obj.getOriginalPrice());
                        } else {
                            pstmt.setNull(i++, Types.DOUBLE);
                        }
                        pstmt.setString(i++, obj.getDescription());
                        if (obj.getStatus() != null) {
                            pstmt.setString(i++, String.valueOf(obj.getStatus()));
                        } else {
                            pstmt.setString(i++, "0");
                        }
                        pstmt.setNull(i++, Types.NVARCHAR);
                        pstmt.setNull(i++, Types.NVARCHAR);
                        pstmt.setNull(i++, Types.NVARCHAR);
                        pstmt.setNull(i++, Types.NVARCHAR);

                        pstmt.setString(i++, obj.getGoodsCode());

                        pstmt.addBatch();
                        int[] updatedResult;
                        int groupAffectedRows;
                        if (group == maxBatchSize) {
                            logger.info("KTTS EXECUTE BATCH: " + count + "/" + size);
                            groupAffectedRows = 0;
                            updatedResult = pstmt.executeBatch();
                            for (int returnCode : updatedResult) {
                                if (returnCode > 0) {
                                    groupAffectedRows++;
                                }
                            }
                            totalAffectedRows += groupAffectedRows;
                            updatedResultTotal = (int[]) ArrayUtils.addAll(updatedResultTotal, updatedResult);
                            logger.info("ROWS AFFECTED: " + groupAffectedRows + "/" + group);
                            group = 0;
                            con.setAutoCommit(false);
                            this.commit(con);
                        }
                        if (group < maxBatchSize && count == size) {
                            logger.info("KTTS EXCECUTE BATCH: " + count + "/" + size);
                            updatedResult = pstmt.executeBatch();
                            for (int returnCode : updatedResult) {
                                if (returnCode > 0) {
                                    totalAffectedRows++;
                                }
                            }
                            updatedResultTotal = (int[]) ArrayUtils.addAll(updatedResultTotal, updatedResult);
                            logger.info("TOTAL ROWS AFFECTED: " + totalAffectedRows + "/" + size);
                            con.setAutoCommit(false);
                            this.commit(con);
                        }
                    } catch (Exception ex) {
                        logger.info("KTTS UPDATE GOODS FAIL!");
                        logger.error(ex.getMessage(), ex);
                    }
                }

            } catch (Exception ex) {
                logger.info("KTTS UPDATE GOODS FAIL!");
                logger.error(ex.getMessage(), ex);
            } finally {
                if (con != null) {
                    try {
                        close(con);
                    } catch (Exception e) {
                        logger.error(e.getMessage(), e);
                    }
                }
                if (pstmt != null) {
                    try {
                        pstmt.close();
                    } catch (Exception ex) {
                        logger.error(ex.getMessage(), ex);
                    }
                }
            }
        } else {
            logger.info("List KTTS UPDATE GOODS is empty!");
            return;
        }
        insertGoods(lstGoods, logger, maxBatchSize, updatedResultTotal);
        logger.info("UPDATE GOODS FINISH!");
    }

    public synchronized void insertWareExpNote(List<MerInExpNoteDTO> lstWareExpNote, Logger logger, long maxBatchSize, int[] updatedResultTotal, int type,
            HashMap<String, String> synDetailMap, HashMap<String, String> configUserProvinceMap) {
        logger.info("INSERT SYN_STOCK_TRANS START!");
        if (lstWareExpNote != null && !lstWareExpNote.isEmpty()) {
            StringBuilder sqlStockTrans
                    = new StringBuilder("INSERT INTO SYN_STOCK_TRANS");
            sqlStockTrans.append("  ( ");
            sqlStockTrans.append("    SYN_STOCK_TRANS_ID, ");
            sqlStockTrans.append("    STOCK_ID, ");
            sqlStockTrans.append("    CODE, ");
            sqlStockTrans.append("    TYPE, ");
            sqlStockTrans.append("    BUSSINESS_TYPE, ");
            sqlStockTrans.append("    BUSSINESS_TYPE_NAME, ");
            sqlStockTrans.append("    STATUS, ");
            sqlStockTrans.append("    CREATED_BY, ");
            sqlStockTrans.append("    CREATED_BY_NAME, ");
            sqlStockTrans.append("    CREATED_DATE, ");
            sqlStockTrans.append("    CREATED_DEPT_ID, ");
            sqlStockTrans.append("    CREATED_DEPT_NAME, ");
            sqlStockTrans.append("    REAL_IE_TRANS_DATE, ");
            sqlStockTrans.append("    REAL_IE_USER_NAME, ");
            sqlStockTrans.append("    SHIPPER_ID, ");
            sqlStockTrans.append("    SHIPPER_NAME, ");
            sqlStockTrans.append("    CANCEL_DATE, ");
            sqlStockTrans.append("    CANCEL_BY, ");
            sqlStockTrans.append("    CANCEL_BY_NAME, ");
            sqlStockTrans.append("    CANCEL_REASON_NAME, ");
            sqlStockTrans.append("    ORDER_CODE, ");
            sqlStockTrans.append("    ORDER_ID, ");
            sqlStockTrans.append("    CONSTRUCTION_ID, ");
            sqlStockTrans.append("    DESCRIPTION, ");
            sqlStockTrans.append("    CONFIRM, ");
            sqlStockTrans.append("    IN_ROAL, ");
            sqlStockTrans.append("    CONSTRUCTION_CODE, ");
            sqlStockTrans.append("    SYN_TRANS_TYPE, ");
            sqlStockTrans.append("    SHIPMENT_CODE, ");
            sqlStockTrans.append("    STOCK_CODE, ");
            sqlStockTrans.append("    LAST_SHIPPER_ID ");
            sqlStockTrans.append("  ) ");
            sqlStockTrans.append("VALUES ");
            sqlStockTrans.append("( ");
            sqlStockTrans.append("  ?, ?, ?, ?, ?, ?, ?, ?, ?, TO_DATE(?,'YYYY-MM-DD'), ?, ?, TO_DATE(?,'YYYY-MM-DD'), ?, ?, ?, TO_DATE(?,'YYYY-MM-DD'), ?, ?, ?, ?, ?, ?, ?, 0, ?, ?, ?, ?, ?, ? ");
            sqlStockTrans.append(") ");
            //28
            PreparedStatement pstmtStockTrans = null;
            Connection con = null;
            try {
                con = getConnection();

                pstmtStockTrans = con.prepareStatement(sqlStockTrans.toString());
                int count = 0;
                int size = lstWareExpNote.size();
                int group = 0;
                int rowCount = 0;
                for (MerInExpNoteDTO obj : lstWareExpNote) {

                    int i = 1;
                    try {
                        if (updatedResultTotal[rowCount] == 1) {
                            rowCount++;
                            size--;
                            if (size != count && group < 2000) {
                                continue;
                            }
                        } else {
                            rowCount++;
                            group++;
                            count++;

                            String constructionId = null;
                            String sysGroupId = null;
                            String catStationId = null;
                            String sql = "SELECT CONSTRUCTION_ID, CODE, SYS_GROUP_ID, CAT_STATION_ID"
                                    + " FROM CONSTRUCTION WHERE UPPER(CODE) = UPPER(?)";
                            PreparedStatement stmt = con.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                            stmt.setString(1, obj.getConstrtCode());
                            ResultSet rs = stmt.executeQuery();
                            while (rs.next()) {
                                constructionId = String.valueOf(rs.getLong("CONSTRUCTION_ID"));
                                sysGroupId = String.valueOf(rs.getLong("SYS_GROUP_ID"));
                                catStationId = String.valueOf(rs.getLong("CAT_STATION_ID"));
                            }
                            stmt.close();
                            rs.close();

                            //SYN_STOCK_TRANS_ID
                            pstmtStockTrans.setLong(i++, obj.getDeliveryNoteId());
                            //STOCK_ID
                            pstmtStockTrans.setLong(i++, obj.getSourceWhId());
                            //CODE
                            pstmtStockTrans.setString(i++, obj.getExpNoteCode());
                            //TYPE
                            pstmtStockTrans.setLong(i++, 2);
                            //BUSSINESS_TYPE
                            pstmtStockTrans.setLong(i++, 2);
                            //BUSSINESS_TYPE_NAME 
                            pstmtStockTrans.setString(i++, "Xuất ra công trình");
                            //STATUS
                            pstmtStockTrans.setLong(i++, 2l);
                            //CREATED_BY 
                            if (obj.getCreatorId() != null) {
                                pstmtStockTrans.setLong(i++, obj.getCreatorId());
                            } else {
                                pstmtStockTrans.setNull(i++, Types.INTEGER);
                            }
                            //CREATED_BY_NAME
                            if (obj.getFullName() != null) {
                                pstmtStockTrans.setString(i++, obj.getFullName());
                            } else {
                                pstmtStockTrans.setString(i++, "");
                            }
                            //CREATED_DATE
                            if (obj.getCreatedDate() != null) {
                                pstmtStockTrans.setString(i++, String.valueOf(obj.getCreatedDate()).substring(0, 10));
                            } else {
                                pstmtStockTrans.setNull(i++, Types.DATE);
                            }
                            //CREATED_DEPT_ID
                            if (obj.getCreNoteGroupId() != null) {
                                pstmtStockTrans.setLong(i++, obj.getCreNoteGroupId());
                            } else {
                                pstmtStockTrans.setNull(i++, Types.INTEGER);
                            }
                            //CREATED_DEPT_NAME
                            pstmtStockTrans.setString(i++, obj.getCreatedSysGroupName());
                            //REAL_IE_TRANS_DATE
                            if (obj.getActualExpDate() != null) {
                                pstmtStockTrans.setString(i++, String.valueOf(obj.getActualExpDate()).substring(0, 10));
                            } else {
                                pstmtStockTrans.setNull(i++, Types.DATE);
                            }
                            //REAL_IE_USER_NAME
                            if (obj.getActualExpBy() != null) {
                                pstmtStockTrans.setLong(i++, obj.getActualExpBy());
                            } else {
                                pstmtStockTrans.setNull(i++, Types.INTEGER);
                            }
                            //SHIPPER_ID
                            String catProvinceId = null;
                            String sql2 = "SELECT CAT_PROVINCE_ID"
                                    + " FROM CAT_STATION WHERE CAT_STATION_ID = ?";
                            PreparedStatement stmt2 = con.prepareStatement(sql2, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                            stmt2.setLong(1, Long.parseLong(catStationId));
                            ResultSet rs2 = stmt2.executeQuery();
                            while (rs2.next()) {
                                catProvinceId = String.valueOf(rs2.getLong("CAT_PROVINCE_ID"));
                            }
                            stmt2.close();
                            rs2.close();
                            String sysUserIdTemp = configUserProvinceMap.get(sysGroupId + "|" + catProvinceId);
                            if (sysUserIdTemp == null) {
                                pstmtStockTrans.setLong(i++, -1l);
                            } else {
                                pstmtStockTrans.setLong(i++, Long.parseLong(sysUserIdTemp));
                            }

                            //SHIPPER_NAME
                            pstmtStockTrans.setString(i++, obj.getReceiverName());
                            //CANCEL_DATE
                            if (obj.getDelConfirmDate() != null) {
                                pstmtStockTrans.setString(i++, String.valueOf(obj.getDelConfirmDate()).substring(0, 10));
                            } else {
                                pstmtStockTrans.setNull(i++, Types.DATE);
                            }
                            //CANCEL_BY
                            if (obj.getUserIdDel() != null) {
                                pstmtStockTrans.setLong(i++, obj.getUserIdDel());
                            } else {
                                pstmtStockTrans.setNull(i++, Types.INTEGER);
                            }
                            //CANCEL_BY_NAME
                            pstmtStockTrans.setString(i++, obj.getCancelByName());
                            //CANCEL_REASON_NAME
                            pstmtStockTrans.setString(i++, obj.getDeleteDisapproveNoteCause());
                            //ORDER_CODE
                            pstmtStockTrans.setString(i++, obj.getExpReqCode());
                            //ORDER_ID
                            if (obj.getExpReqId() != null) {
                                pstmtStockTrans.setLong(i++, obj.getExpReqId());
                            } else {
                                pstmtStockTrans.setNull(i++, Types.INTEGER);
                            }

                            //CONSTRUCTION_ID
                            if (obj.getConstrtCode() != null) {
                                if (constructionId != null) {
                                    pstmtStockTrans.setLong(i++, Long.parseLong(constructionId));
                                } else {
                                    if (obj.getConstructionId() != null) {
                                        pstmtStockTrans.setLong(i++, obj.getConstructionId());
                                    } else {
                                        pstmtStockTrans.setNull(i++, Types.INTEGER);
                                    }
                                }
                            } else {
                                if (obj.getConstructionId() != null) {
                                    pstmtStockTrans.setLong(i++, obj.getConstructionId());
                                } else {
                                    pstmtStockTrans.setNull(i++, Types.INTEGER);
                                }
                            }
                            //DESCRIPTION
                            pstmtStockTrans.setString(i++, obj.getDescription());
                            //IN_ROAL
                            if (obj.getReceiveConfirm() != null) {
                                pstmtStockTrans.setString(i++, String.valueOf(obj.getReceiveConfirm()));
                            } else {
                                pstmtStockTrans.setNull(i++, Types.NVARCHAR);
                            }
                            //CONSTRUCTION_CODE
                            pstmtStockTrans.setString(i++, obj.getConstrtCode());
                            //SYN_TRANS_TYPE
                            pstmtStockTrans.setString(i++, "1");
                            //SHIPMENT_CODE
                            pstmtStockTrans.setString(i++, obj.getShipmentNo());
                            //STOCK_CODE
                            pstmtStockTrans.setString(i++, obj.getSourceWhCode());
//                            pstmtStockTrans.setString(i++, "Ma_kho_xuat");
                            //LAST_SHIPPER_ID
                            if (sysUserIdTemp == null) {
                                //                            pstmtStockTrans.setNull(i++, Types.INTEGER);
                                pstmtStockTrans.setLong(i++, -1l);
                            } else {
                                pstmtStockTrans.setLong(i++, Long.parseLong(sysUserIdTemp));
                            }

                            pstmtStockTrans.addBatch();
                        }

                        if (group == maxBatchSize) {
                            logger.info("KTTS INSERTED " + count + "/" + size);
                            group = 0;
                            pstmtStockTrans.executeBatch();
                            con.setAutoCommit(false);
                            this.commit(con);
                        }
                        if (group < maxBatchSize && count == size) {
                            logger.info("KTTS INSERTED " + count + "/" + size);
                            pstmtStockTrans.executeBatch();
                            con.setAutoCommit(false);
                            this.commit(con);
                        }
                    } catch (Exception ex) {
                        logger.info("KTTS INSERT INTO SYN_STOCK_TRANS FAIL!");
                        logger.error(ex.getMessage(), ex);
                    }
                }

                updateSynStockTransDetailSerial(lstWareExpNote, -1l, logger, maxBatchSize, type, synDetailMap, con);
                updateSynStockTransDetail(lstWareExpNote, logger, maxBatchSize, type, con);
                updatedDetailId(logger, maxBatchSize, con);

            } catch (Exception ex) {
                logger.info("KTTS INSERT INTO SYN_STOCK_TRANS FAIL!");
                logger.error(ex.getMessage(), ex);
            } finally {
                if (con != null) {
                    try {
                        close(con);
                    } catch (Exception e) {
                        logger.error(e.getMessage(), e);
                    }
                }
                if (pstmtStockTrans != null) {
                    try {
                        pstmtStockTrans.close();
                    } catch (Exception ex) {
                        logger.error(ex.getMessage(), ex);
                    }
                }
            }
        } else {
            logger.info("List KTTS INSERT INTO SYN_STOCK_TRANS is empty!");
            return;
        }
        logger.info("INSERT SYN_STOCK_TRANS FINISH!");
    }

    public synchronized void insertSynStockTransDetail(List<MerInExpNoteDTO> lstWareExpNote, Logger logger, long maxBatchSize, int[] updatedResultTotal, int type,
            Connection con) {
        logger.info("INSERT SYN_STOCK_TRANS_DETAIL START!");
        int size = 0;
        StringBuilder sqlStockTransDetail = new StringBuilder("INSERT INTO SYN_STOCK_TRANS_DETAIL ");
        sqlStockTransDetail.append("(SYN_STOCK_TRANS_DETAIL_ID, SYN_STOCK_TRANS_ID, GOODS_ID,");
        sqlStockTransDetail.append(" GOODS_CODE, GOODS_NAME, GOODS_STATE, GOODS_STATE_NAME, ");
        sqlStockTransDetail.append(" AMOUNT_ORDER, AMOUNT_REAL, GOODS_UNIT_ID, GOODS_UNIT_NAME)");
        sqlStockTransDetail.append(" VALUES(");
        sqlStockTransDetail.append(" SYN_STOCK_TRANS_DETAIL_SEQ.NEXTVAL,?,?,?,?,?,?,?,?,?,?");
        sqlStockTransDetail.append(" )");

        PreparedStatement pstmtStockTransDetail = null;
        try {
            int rowCount = 0;
            pstmtStockTransDetail = con.prepareStatement(sqlStockTransDetail.toString());
            int count = 0;
            int group = 0;

            String sql = "SELECT SYN_STOCK_TRANS_ID, GOODS_ID, GOODS_STATE, SUM(AMOUNT) AS AMOUNT, "
                    + " GOODS_CODE, GOODS_NAME, GOODS_STATE_NAME, GOODS_UNIT_ID, GOODS_UNIT_NAME"
                    + " FROM SYN_STOCK_TRANS_DETAIL_SERIAL"
                    + " GROUP BY GOODS_ID, GOODS_STATE, SYN_STOCK_TRANS_ID, GOODS_CODE, GOODS_NAME,"
                    + " GOODS_STATE_NAME, GOODS_UNIT_ID, GOODS_UNIT_NAME";
            PreparedStatement stmt = con.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ResultSet rs = stmt.executeQuery();

            if (rs.last()) {
                size = rs.getRow();
                rs.beforeFirst();
            }
            while (rs.next()) {

                Long goods_id = rs.getLong("GOODS_ID");
                String goods_state = rs.getString("GOODS_STATE");
                Double amount = rs.getDouble("AMOUNT");
                String goods_code = rs.getString("GOODS_CODE");
                String goods_name = rs.getString("GOODS_NAME");
                String goods_state_name = rs.getString("GOODS_STATE_NAME");
                Long syn_stock_trans_id = rs.getLong("SYN_STOCK_TRANS_ID");
                Long goods_unit_id = rs.getLong("GOODS_UNIT_ID");
                String goods_unit_name = rs.getString("GOODS_UNIT_NAME");
                int i = 1;
                try {
                    if (updatedResultTotal[rowCount] != 0) {
                        rowCount++;
                        size--;
                        if (size != count && group < 2000) {
                            continue;
                        }
                    } else {
                        rowCount++;
                        group++;
                        count++;
                        //SYN_STOCK_TRANS_ID
                        pstmtStockTransDetail.setLong(i++, syn_stock_trans_id);
                        //GOODS_ID
                        pstmtStockTransDetail.setLong(i++, goods_id);
                        //GOODS_CODE
                        pstmtStockTransDetail.setString(i++, goods_code);
                        //GOODS_NAME
                        pstmtStockTransDetail.setString(i++, goods_name);
                        //GOODS_STATE
                        pstmtStockTransDetail.setString(i++, goods_state);
                        //GOODS_STATE_NAME
                        pstmtStockTransDetail.setString(i++, goods_state_name);
                        //AMOUNT_ORDER
                        pstmtStockTransDetail.setDouble(i++, amount);
                        //AMOUNT_REAL
                        pstmtStockTransDetail.setDouble(i++, amount);
                        //GOODS_UNIT_ID
                        pstmtStockTransDetail.setLong(i++, goods_unit_id);
                        //GOODS_UNIT_NAME
                        pstmtStockTransDetail.setString(i++, goods_unit_name);
                        pstmtStockTransDetail.addBatch();
                    }

                    if (group == maxBatchSize) {
                        logger.info("KTTS INSERTED " + count + "/" + size);
                        group = 0;
                        pstmtStockTransDetail.executeBatch();
                        con.setAutoCommit(false);
                        this.commit(con);
                    }
                    if (group < maxBatchSize && count == size) {
                        logger.info("KTTS INSERTED " + count + "/" + size);
                        pstmtStockTransDetail.executeBatch();
                        con.setAutoCommit(false);
                        this.commit(con);
                    }
                } catch (Exception ex) {
                    logger.info("KTTS INSERT INTO SYN_STOCK_TRANS_DETAIL FAIL!");
                    logger.error(ex.getMessage(), ex);
                }
            }

            stmt.close();
            rs.close();

        } catch (Exception ex) {
            logger.info("KTTS INSERT INTO SYN_STOCK_TRANS_DETAIL FAIL!");
            logger.error(ex.getMessage(), ex);
        } finally {
            //ko dc close connection o day
            if (pstmtStockTransDetail != null) {
                try {
                    pstmtStockTransDetail.close();
                } catch (Exception ex) {
                    logger.error(ex.getMessage(), ex);
                }
            }
        }

        StringBuilder updateWSSQL = new StringBuilder("UPDATE SYN_STOCK_TRANS_DETAIL ");
        updateWSSQL.append(" SET GOODS_IS_SERIAL = ?, ORDER_ID = ?");
        updateWSSQL.append(" WHERE SYN_STOCK_TRANS_ID = ? AND GOODS_ID = ? AND GOODS_STATE = ?");

        try {
            logger.info("INSERT SYN_STOCK_TRANS_DETAIL WS START!");
            pstmtStockTransDetail = con.prepareStatement(updateWSSQL.toString());

            size = 0;
            for (MerInExpNoteDTO obj : lstWareExpNote) {
                if (obj.getLstMer() != null) {
                    size += obj.getLstMer().size();
                }
            }
            int count = 0;
            int group = 0;
            for (MerInExpNoteDTO obj : lstWareExpNote) {

                Long synStockTransId;
                if (type == 2) {
                    synStockTransId = obj.getDeliveryNoteId();
                } else {
                    synStockTransId = obj.getReceiptNoteId();
                }
                int totalAffectedRows = 0;

                if (obj.getLstMer() != null) {

                    for (MerEntityBO mer : obj.getLstMer()) {
                        count++;
                        group++;
                        pstmtStockTransDetail.setLong(1, mer.getIsDevice());
                        if (type == 2) {
                            pstmtStockTransDetail.setLong(2, mer.getExpReqId());
                        } else {
                            pstmtStockTransDetail.setLong(2, mer.getImpReqId());
                        }

                        pstmtStockTransDetail.setLong(3, synStockTransId);
                        pstmtStockTransDetail.setLong(4, mer.getMerchandiseId());
                        pstmtStockTransDetail.setString(5, mer.getMerIsActive().toString());

                        pstmtStockTransDetail.addBatch();

                        int[] updatedResult;
                        int groupAffectedRows;
                        if (group == maxBatchSize) {
                            logger.info("KTTS EXECUTE BATCH: " + count + "/" + size);
                            groupAffectedRows = 0;
                            updatedResult = pstmtStockTransDetail.executeBatch();
                            for (int returnCode : updatedResult) {
                                if (returnCode > 0) {
                                    groupAffectedRows++;
                                }
                            }
                            totalAffectedRows += groupAffectedRows;
                            logger.info("ROWS AFFECTED: " + groupAffectedRows + "/" + group);
                            group = 0;
                            con.setAutoCommit(false);
                            this.commit(con);
                        }
                        if (group < maxBatchSize && count == size) {
                            logger.info("KTTS EXCECUTE BATCH: " + count + "/" + size);
                            updatedResult = pstmtStockTransDetail.executeBatch();
                            for (int returnCode : updatedResult) {
                                if (returnCode > 0) {
                                    totalAffectedRows++;
                                }
                            }
                            logger.info("TOTAL ROWS AFFECTED: " + totalAffectedRows + "/" + size);
                            con.setAutoCommit(false);
                            this.commit(con);
                        }
                    }
                }
            }

        } catch (Exception ex) {
            logger.info("KTTS UPDATE SYN_STOCK_TRANS_DETAIL WS FAIL!");
            logger.error(ex.getMessage(), ex);
        } finally {
            logger.info("update SYN_STOCK_TRANS_DETAIL WS FINISH!");
            if (pstmtStockTransDetail != null) {
                try {
                    pstmtStockTransDetail.close();
                } catch (Exception ex) {
                    logger.error(ex.getMessage(), ex);
                }
            }
        }
        logger.info("INSERT INTO SYN_STOCK_TRANS_DETAIL FINISH!");
    }

    public synchronized void updateSynStockTransDetail(List<MerInExpNoteDTO> lstWareExpNote, Logger logger, long maxBatchSize, int type, Connection con) {
        logger.info("UPDATE SYN_STOCK_TRANS_DETAIL START!");
        int[] updatedResultTotal = null;
        int size = 0;
        StringBuilder updateDBSQL = new StringBuilder("UPDATE SYN_STOCK_TRANS_DETAIL ");
        updateDBSQL.append(" SET GOODS_CODE           = ?, ");
        updateDBSQL.append("   GOODS_NAME             = ?, ");
        updateDBSQL.append("   GOODS_STATE_NAME       = ?, ");
        updateDBSQL.append("   AMOUNT_ORDER           = ?, ");
        updateDBSQL.append("   AMOUNT_REAL            = ?, ");
        updateDBSQL.append("   GOODS_UNIT_ID          = ?, ");
        updateDBSQL.append("   GOODS_UNIT_NAME        = ? ");
        updateDBSQL.append(" WHERE SYN_STOCK_TRANS_ID = ? ");
        updateDBSQL.append(" AND GOODS_ID             = ? ");
        updateDBSQL.append(" AND GOODS_STATE          = ? ");

        PreparedStatement pstmtStockTransDetail = null;
        try {
            pstmtStockTransDetail = con.prepareStatement(updateDBSQL.toString());
            int count = 0;
            int group = 0;

            String sql = "SELECT SYN_STOCK_TRANS_ID,"
                    + "  GOODS_ID,"
                    + "  GOODS_STATE,"
                    + "  SUM(AMOUNT) AS AMOUNT,"
                    + "  GOODS_CODE,"
                    + "  GOODS_NAME,"
                    + "  GOODS_STATE_NAME,"
                    + "  GOODS_UNIT_ID,"
                    + "  GOODS_UNIT_NAME "
                    + "FROM SYN_STOCK_TRANS_DETAIL_SERIAL "
                    + "GROUP BY GOODS_ID,"
                    + "  GOODS_STATE,"
                    + "  SYN_STOCK_TRANS_ID,"
                    + "  GOODS_CODE,"
                    + "  GOODS_NAME,"
                    + "  GOODS_STATE_NAME,"
                    + "  GOODS_UNIT_ID,"
                    + "  GOODS_UNIT_NAME";
            PreparedStatement stmt = con.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ResultSet rs = stmt.executeQuery();

            int totalAffectedRows = 0;
            if (rs.last()) {
                size = rs.getRow();
                rs.beforeFirst();
            }
            while (rs.next()) {
                Long goods_id = rs.getLong("GOODS_ID");
                String goods_state = rs.getString("GOODS_STATE");
                Double amount = rs.getDouble("AMOUNT");
                String goods_code = rs.getString("GOODS_CODE");
                String goods_name = rs.getString("GOODS_NAME");
                String goods_state_name = rs.getString("GOODS_STATE_NAME");
                Long syn_stock_trans_id = rs.getLong("SYN_STOCK_TRANS_ID");
                Long goods_unit_id = rs.getLong("GOODS_UNIT_ID");
                String goods_unit_name = rs.getString("GOODS_UNIT_NAME");
                int i = 1;
                try {
                    group++;
                    count++;
                    //GOODS_CODE
                    pstmtStockTransDetail.setString(i++, goods_code);
                    //GOODS_NAME
                    pstmtStockTransDetail.setString(i++, goods_name);
                    //GOODS_STATE_NAME
                    pstmtStockTransDetail.setString(i++, goods_state_name);
                    //AMOUNT_ORDER
                    pstmtStockTransDetail.setDouble(i++, amount);
                    //AMOUNT_REAL
                    pstmtStockTransDetail.setDouble(i++, amount);
                    //GOODS_UNIT_ID
                    pstmtStockTransDetail.setLong(i++, goods_unit_id);
                    //GOODS_UNIT_NAME
                    pstmtStockTransDetail.setString(i++, goods_unit_name);
                    //SYN_STOCK_TRANS_ID
                    pstmtStockTransDetail.setLong(i++, syn_stock_trans_id);
                    //GOODS_ID
                    pstmtStockTransDetail.setLong(i++, goods_id);
                    //GOODS_STATE
                    pstmtStockTransDetail.setString(i++, goods_state);

                    pstmtStockTransDetail.addBatch();

                    int[] updatedResult;
                    int groupAffectedRows;
                    if (group == maxBatchSize) {
                        logger.info("KTTS EXECUTE BATCH: " + count + "/" + size);
                        groupAffectedRows = 0;
                        updatedResult = pstmtStockTransDetail.executeBatch();
                        for (int returnCode : updatedResult) {
                            if (returnCode > 0) {
                                groupAffectedRows++;
                            }
                        }
                        totalAffectedRows += groupAffectedRows;
                        updatedResultTotal = (int[]) ArrayUtils.addAll(updatedResultTotal, updatedResult);
                        logger.info("ROWS AFFECTED: " + groupAffectedRows + "/" + group);
                        group = 0;
                        con.setAutoCommit(false);
                        this.commit(con);
                    }
                    if (group < maxBatchSize && count == size) {
                        logger.info("KTTS EXCECUTE BATCH: " + count + "/" + size);
                        updatedResult = pstmtStockTransDetail.executeBatch();
                        for (int returnCode : updatedResult) {
                            if (returnCode > 0) {
                                totalAffectedRows++;
                            }
                        }
                        updatedResultTotal = (int[]) ArrayUtils.addAll(updatedResultTotal, updatedResult);
                        logger.info("TOTAL ROWS AFFECTED: " + totalAffectedRows + "/" + size);
                        con.setAutoCommit(false);
                        this.commit(con);
                    }
                } catch (Exception ex) {
                    logger.info("KTTS UPDATE SYN_STOCK_TRANS_DETAIL FAIL!");
                    logger.error(ex.getMessage(), ex);
                }
            }

            stmt.close();
            rs.close();
        } catch (Exception ex) {
            logger.info("KTTS UPDATE SYN_STOCK_TRANS_DETAIL FAIL!");
            logger.error(ex.getMessage(), ex);
        } finally {
            //ko dc close Connection o day!
            if (pstmtStockTransDetail != null) {
                try {
                    pstmtStockTransDetail.close();
                } catch (Exception ex) {
                    logger.error(ex.getMessage(), ex);
                }
            }
        }
        StringBuilder updateWSSQL = new StringBuilder("UPDATE SYN_STOCK_TRANS_DETAIL ");
        updateWSSQL.append(" SET GOODS_TYPE = ?, GOODS_IS_SERIAL = ?, ORDER_ID = ?");
        updateWSSQL.append(" WHERE SYN_STOCK_TRANS_ID = ? AND GOODS_ID = ? AND GOODS_STATE = ?");
        try {
            logger.info("UPDATE SYN_STOCK_TRANS_DETAIL WS START!");
            pstmtStockTransDetail = con.prepareStatement(updateWSSQL.toString());
            size = 0;
            for (MerInExpNoteDTO obj : lstWareExpNote) {
                if (obj.getLstMer() != null) {
                    size += obj.getLstMer().size();
                }
            }
            int count = 0;
            int group = 0;
            for (MerInExpNoteDTO obj : lstWareExpNote) {
                int totalAffectedRows = 0;
                Long synStockTransId;
                if (type == 2) {
                    synStockTransId = obj.getDeliveryNoteId();
                } else {
                    synStockTransId = obj.getReceiptNoteId();
                }
                if (obj.getLstMer() != null) {

                    for (MerEntityBO mer : obj.getLstMer()) {
                        count++;
                        group++;
                        pstmtStockTransDetail.setLong(1, mer.getIsDevice());
                        pstmtStockTransDetail.setLong(2, mer.getIsDevice());
                        if (type == 2) {
                            pstmtStockTransDetail.setLong(3, mer.getExpReqId());
                        } else {
                            pstmtStockTransDetail.setLong(3, mer.getImpReqId());
                        }
                        pstmtStockTransDetail.setLong(4, synStockTransId);
                        pstmtStockTransDetail.setLong(5, mer.getMerchandiseId());
                        pstmtStockTransDetail.setString(6, mer.getMerIsActive().toString());

                        pstmtStockTransDetail.addBatch();

                        int[] updatedResult;
                        int groupAffectedRows;
                        if (group == maxBatchSize) {
                            logger.info("KTTS EXECUTE BATCH : " + count + "/" + size);
                            groupAffectedRows = 0;
                            updatedResult = pstmtStockTransDetail.executeBatch();
                            for (int returnCode : updatedResult) {
                                if (returnCode > 0) {
                                    groupAffectedRows++;
                                }
                            }
                            totalAffectedRows += groupAffectedRows;
                            logger.info("ROWS AFFECTED: " + groupAffectedRows + "/" + group);
                            group = 0;
                            con.setAutoCommit(false);
                            this.commit(con);
                        }
                        if (group < maxBatchSize && count == size) {
                            logger.info("KTTS EXCECUTE BATCH: " + count + "/" + size);
                            updatedResult = pstmtStockTransDetail.executeBatch();
                            for (int returnCode : updatedResult) {
                                if (returnCode > 0) {
                                    totalAffectedRows++;
                                }
                            }
                            logger.info("TOTAL ROWS AFFECTED: " + totalAffectedRows + "/" + size);
                            con.setAutoCommit(false);
                            this.commit(con);
                        }
                    }
                }
            }

        } catch (Exception ex) {
            logger.info("KTTS UPDATE SYN_STOCK_TRANS_DETAIL WS FAIL!");
            logger.error(ex.getMessage(), ex);
        } finally {
            logger.info("KTTS UPDATE SYN_STOCK_TRANS_DETAIL WS FINISH!");
            if (pstmtStockTransDetail != null) {
                try {
                    pstmtStockTransDetail.close();
                } catch (Exception ex) {
                    logger.error(ex.getMessage(), ex);
                }
            }
        }

        insertSynStockTransDetail(lstWareExpNote, logger, maxBatchSize, updatedResultTotal, type, con);

        logger.info("UPDATE SYN_STOCK_TRANS_DETAIL FINISH!");
    }

    public synchronized void updatedDetailId(Logger logger, long maxBatchSize, Connection con) {
        logger.info("UPDATE DETAIL_ID START!");
        int[] updatedResultTotal = null;

        StringBuilder sqlStockTransDetail = new StringBuilder("UPDATE SYN_STOCK_TRANS_DETAIL_SERIAL ");
        sqlStockTransDetail.append(" SET");
        sqlStockTransDetail.append(" SYN_STOCK_TRANS_DETAIL_ID = ?");
        sqlStockTransDetail.append(" WHERE SYN_STOCK_TRANS_ID = ? AND GOODS_ID = ? AND GOODS_STATE = ?");

        PreparedStatement pstmtStockTransDetail = null;
        try {
            pstmtStockTransDetail = con.prepareStatement(sqlStockTransDetail.toString());
            int count = 0;

            int group = 0;

            String sql = "SELECT DISTINCT SYN_STOCK_TRANS_DETAIL_ID,SYN_STOCK_TRANS_ID, GOODS_ID, GOODS_STATE"
                    + " FROM SYN_STOCK_TRANS_DETAIL";
            PreparedStatement stmt = con.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ResultSet rs = stmt.executeQuery();
            int size = 0;
            int totalAffectedRows = 0;
            if (rs.last()) {
                size = rs.getRow();
                rs.beforeFirst();
            }
            while (rs.next()) {
                Long goods_id = rs.getLong("GOODS_ID");
                String goods_state = rs.getString("GOODS_STATE");
                Long syn_stock_trans_id = rs.getLong("SYN_STOCK_TRANS_ID");
                Long syn_stock_trans_detail_id = rs.getLong("SYN_STOCK_TRANS_DETAIL_ID");
                int i = 1;
                try {
                    group++;
                    count++;

                    pstmtStockTransDetail.setLong(i++, syn_stock_trans_detail_id);
                    pstmtStockTransDetail.setLong(i++, syn_stock_trans_id);
                    pstmtStockTransDetail.setLong(i++, goods_id);
                    pstmtStockTransDetail.setString(i++, goods_state);

                    pstmtStockTransDetail.addBatch();

                    int[] updatedResult;
                    int groupAffectedRows;
                    if (group == maxBatchSize) {
                        logger.info("KTTS EXECUTE BATCH: " + count + "/" + size);
                        groupAffectedRows = 0;
                        updatedResult = pstmtStockTransDetail.executeBatch();
                        for (int returnCode : updatedResult) {
                            if (returnCode > 0) {
                                groupAffectedRows++;
                            }
                        }
                        totalAffectedRows += groupAffectedRows;
                        updatedResultTotal = (int[]) ArrayUtils.addAll(updatedResultTotal, updatedResult);
                        logger.info("ROWS AFFECTED: " + groupAffectedRows + "/" + group);
                        group = 0;
                        con.setAutoCommit(false);
                        this.commit(con);
                    }
                    if (group < maxBatchSize && count == size) {
                        logger.info("KTTS EXCECUTE BATCH: " + count + "/" + size);
                        updatedResult = pstmtStockTransDetail.executeBatch();
                        for (int returnCode : updatedResult) {
                            if (returnCode > 0) {
                                totalAffectedRows++;
                            }
                        }
                        updatedResultTotal = (int[]) ArrayUtils.addAll(updatedResultTotal, updatedResult);
                        logger.info("TOTAL ROWS AFFECTED: " + totalAffectedRows + "/" + size);
                        con.setAutoCommit(false);
                        this.commit(con);
                    }
                } catch (Exception ex) {
                    logger.info("KTTS UPDATE DETAIL_ID FAIL!");
                    logger.error(ex.getMessage(), ex);
                }
            }

            stmt.close();
            rs.close();
        } catch (Exception ex) {
            logger.info("KTTS UPDATE DETAIL_ID FAIL!");
            logger.error(ex.getMessage(), ex);
        } finally {
            if (pstmtStockTransDetail != null) {
                try {
                    pstmtStockTransDetail.close();
                } catch (Exception ex) {
                    logger.error(ex.getMessage(), ex);
                }
            }
        }
        logger.info("UPDATE DETAIL_ID FINISH!");
    }

    public synchronized void insertSynStockTransDetailSerial(List<MerInExpNoteDTO> lstWareExpNote, Long synStockTransDetailId, Logger logger,
            long maxBatchSize, int[] updatedResultTotal, int type, Connection con) {
        logger.info("INSERT SYN_STOCK_TRANS_DETAIL_SERIAL START!");
        if (lstWareExpNote != null && !lstWareExpNote.isEmpty()) {
            StringBuilder sqlStockTransDetail = new StringBuilder("INSERT INTO SYN_STOCK_TRANS_DETAIL_SERIAL ");
            sqlStockTransDetail.append("(SYN_STOCK_TRANS_DETAIL_SER_ID, SYN_STOCK_TRANS_DETAIL_ID, SYN_STOCK_TRANS_ID,");
            sqlStockTransDetail.append(" MER_ENTITY_ID, SERIAL, UNIT_PRICE, CAT_MANUFACTURER_ID, CAT_PRODUCING_COUNTRY_ID, ");
            sqlStockTransDetail.append(" AMOUNT, GOODS_CODE, GOODS_NAME, GOODS_ID, GOODS_STATE, GOODS_STATE_NAME,");
            sqlStockTransDetail.append(" CONSTRUCTION_CODE, GOODS_UNIT_ID, GOODS_UNIT_NAME)");
            sqlStockTransDetail.append(" VALUES(");
            sqlStockTransDetail.append(" SYN_STOCK_TRANS_DETAIL_SER_SEQ.NEXTVAL,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?");
            sqlStockTransDetail.append(" )");
            PreparedStatement pstmtStockTransDetailSerial = null;
            try {
                pstmtStockTransDetailSerial = con.prepareStatement(sqlStockTransDetail.toString());
                int size = updatedResultTotal.length;
                int count = 0;
                int rowCount = 0;
                for (MerInExpNoteDTO merInExpNoteDTO : lstWareExpNote) {

                    int group = 0;
                    for (MerEntityBO obj : merInExpNoteDTO.getLstMer()) {

                        int i = 1;
                        try {
                            if (updatedResultTotal[rowCount] != 0) {
                                rowCount++;
                                size--;
                                if (size != count && group < 2000) {
                                    continue;
                                }
                            } else {
                                rowCount++;
                                group++;
                                count++;
                                //SYN_STOCK_TRANS_DETAIL_ID
                                pstmtStockTransDetailSerial.setLong(i++, synStockTransDetailId);
                                //SYN_STOCK_TRANS_ID
                                if (type == 2) {
                                    pstmtStockTransDetailSerial.setLong(i++, merInExpNoteDTO.getDeliveryNoteId());
                                } else {
                                    pstmtStockTransDetailSerial.setLong(i++, merInExpNoteDTO.getReceiptNoteId());
                                }
                                //MER_ENTITY_ID
                                if (obj.getMerEntityId() != null) {
                                    pstmtStockTransDetailSerial.setLong(i++, obj.getMerEntityId());
                                } else {
                                    pstmtStockTransDetailSerial.setNull(i++, Types.INTEGER);
                                }
                                //SERIAL
                                if (obj.getSerialNumber() != null) {
                                    pstmtStockTransDetailSerial.setString(i++, obj.getSerialNumber());
                                } else {
                                    pstmtStockTransDetailSerial.setNull(i++, Types.NVARCHAR);
                                }
                                //UNIT_PRICE
                                if (obj.getOriginalPrice() != null) {
                                    pstmtStockTransDetailSerial.setDouble(i++, obj.getOriginalPrice());
                                } else {
                                    pstmtStockTransDetailSerial.setNull(i++, Types.DOUBLE);
                                }
                                //CAT_MANUFACTURER_ID
                                if (obj.getCompanyId() != null) {
                                    pstmtStockTransDetailSerial.setLong(i++, obj.getCompanyId());
                                } else {
                                    pstmtStockTransDetailSerial.setNull(i++, Types.NVARCHAR);
                                }
                                //CAT_PRODUCING_COUNTRY_NAME
                                if (obj.getNationalId() != null) {
                                    pstmtStockTransDetailSerial.setLong(i++, obj.getNationalId());
                                } else {
                                    pstmtStockTransDetailSerial.setNull(i++, Types.NVARCHAR);
                                }
                                //AMOUNT
                                if (obj.getCount() != null) {
                                    pstmtStockTransDetailSerial.setDouble(i++, obj.getCount());
                                } else {
                                    pstmtStockTransDetailSerial.setNull(i++, Types.DOUBLE);
                                }
                                //GOODS_CODE
                                pstmtStockTransDetailSerial.setString(i++, obj.getMerCode());
                                //GOODS_NAME
                                pstmtStockTransDetailSerial.setString(i++, obj.getMerName());
                                //GOODS_ID
                                if (obj.getMerchandiseId() != null) {
                                    pstmtStockTransDetailSerial.setLong(i++, obj.getMerchandiseId());
                                } else {
                                    pstmtStockTransDetailSerial.setNull(i++, Types.INTEGER);
                                }
                                //GOODS_STATE

                                pstmtStockTransDetailSerial.setString(i++, "1");

                                //GOODS_STATE_NAME
                                pstmtStockTransDetailSerial.setString(i++, "Bình thường");
                                //CONSTRUCTION_CODE
                                pstmtStockTransDetailSerial.setNull(i++, Types.NVARCHAR);
                                //GOODS_UNIT_ID
                                if (obj.getUnitId() != null) {
                                    pstmtStockTransDetailSerial.setLong(i++, obj.getUnitId());
                                } else {
                                    pstmtStockTransDetailSerial.setNull(i++, Types.INTEGER);
                                }
                                //GOODS_UNIT_NAME
                                pstmtStockTransDetailSerial.setString(i++, obj.getGoodsUnitName());

                                pstmtStockTransDetailSerial.addBatch();
                            }

                            if (group == maxBatchSize) {
                                logger.info("KTTS SYN_STOCK_TRANS_DETAIL_SERIAL INSERTED " + count + "/" + size);
                                group = 0;
                                pstmtStockTransDetailSerial.executeBatch();
                                con.setAutoCommit(false);
                                this.commit(con);
                            }
                            if (group < maxBatchSize && count == size) {
                                logger.info("KTTS SYN_STOCK_TRANS_DETAIL_SERIAL INSERTED " + count + "/" + size);
                                pstmtStockTransDetailSerial.executeBatch();
                                con.setAutoCommit(false);
                                this.commit(con);
                            }
                        } catch (Exception ex) {
                            logger.info("KTTS INSERT INTO SYN_STOCK_TRANS_DETAIL_SERIAL FAIL!");

                            logger.error(ex.getMessage(), ex);
                        }
                    }
                }

            } catch (Exception ex) {
                logger.info("KTTS INSERT INTO SYN_STOCK_TRANS_DETAIL_SERIAL FAIL!");
                logger.error(ex.getMessage(), ex);
            } finally {
                if (pstmtStockTransDetailSerial != null) {
                    try {
                        pstmtStockTransDetailSerial.close();
                    } catch (Exception ex) {
                        logger.error(ex.getMessage(), ex);
                    }
                }
            }
        } else {
            logger.info("List KTTS INSERT INTO SYN_STOCK_TRANS_DETAIL_SERIAL is empty!");
            return;
        }
        logger.info("INSERT INTO SYN_STOCK_TRANS_DETAIL_SERIAL FINISH!");
    }

    public synchronized void updateSynStockTransDetailSerial(List<MerInExpNoteDTO> lstWareExpNote, Long synStockTransDetailId, Logger logger, long maxBatchSize, int type,
            HashMap<String, String> synDetailMap, Connection con) {
        logger.info("UPDATE SYN_STOCK_TRANS_DETAIL_SERIAL START!");
        int[] updatedResultTotal = null;
        if (lstWareExpNote != null && !lstWareExpNote.isEmpty()) {
            StringBuilder sqlStockTransDetail = new StringBuilder("UPDATE SYN_STOCK_TRANS_DETAIL_SERIAL ");
            sqlStockTransDetail.append(" SET SYN_STOCK_TRANS_DETAIL_ID = ?, ");
            sqlStockTransDetail.append("   UNIT_PRICE                  = ?, ");
            sqlStockTransDetail.append("   CAT_MANUFACTURER_ID         = ?, ");
            sqlStockTransDetail.append("   CAT_PRODUCING_COUNTRY_ID    = ?, ");
            sqlStockTransDetail.append("   AMOUNT                      = ?, ");
            sqlStockTransDetail.append("   GOODS_CODE                  = ?, ");
            sqlStockTransDetail.append("   GOODS_NAME                  = ?, ");
            sqlStockTransDetail.append("   GOODS_STATE_NAME            = ?, ");
            sqlStockTransDetail.append("   CONSTRUCTION_CODE           = ?, ");
            sqlStockTransDetail.append("   SERIAL                      = ?, ");
            sqlStockTransDetail.append("   GOODS_STATE                 = ?, ");
            sqlStockTransDetail.append("   GOODS_UNIT_ID               = ?, ");
            sqlStockTransDetail.append("   GOODS_UNIT_NAME             = ? ");
            sqlStockTransDetail.append(" WHERE SYN_STOCK_TRANS_ID      = ? ");
            sqlStockTransDetail.append(" AND MER_ENTITY_ID             = ? ");
            PreparedStatement pstmtStockTransDetailSerial = null;
            try {
                pstmtStockTransDetailSerial = con.prepareStatement(sqlStockTransDetail.toString());
                int size = 0;
                for (MerInExpNoteDTO obj : lstWareExpNote) {
                    if (obj.getLstMer() != null) {
                        size += obj.getLstMer().size();
                    }
                }
                int count = 0;
                int totalAffectedRows = 0;

                int group = 0;
                for (MerInExpNoteDTO merInExpNoteDTO : lstWareExpNote) {
                    for (MerEntityBO obj : merInExpNoteDTO.getLstMer()) {
                        int i = 1;
                        try {
                            group++;
                            count++;
                            //SYN_STOCK_TRANS_DETAIL_ID
                            if (synStockTransDetailId == -1) {
                                pstmtStockTransDetailSerial.setLong(i++, synStockTransDetailId);
                            } else {
                                // get synStockTransDetailId
                                Long synStockTransDetailIdDB = -1l;
                                Long synStockTransIdWS = -1l;
                                if (type == 2) {
                                    synStockTransIdWS = merInExpNoteDTO.getDeliveryNoteId();
                                } else {
                                    synStockTransIdWS = merInExpNoteDTO.getReceiptNoteId();
                                }
                                String synStockTransDetailIdDBStr = synDetailMap.get(synStockTransIdWS + "|" + obj.getMerEntityId());
                                if (synStockTransDetailIdDBStr != null) {
                                    synStockTransDetailIdDB = Long.parseLong(synStockTransDetailIdDBStr);
                                }
                                //----
                                pstmtStockTransDetailSerial.setLong(i++, synStockTransDetailIdDB);
                            }
//                            /UNIT_PRICE
                            if (obj.getOriginalPrice() != null) {
                                pstmtStockTransDetailSerial.setDouble(i++, obj.getOriginalPrice());
                            } else {
                                pstmtStockTransDetailSerial.setNull(i++, Types.DOUBLE);
                            }
                            //CAT_MANUFACTURER_ID
                            if (obj.getCompanyId() != null) {
                                pstmtStockTransDetailSerial.setLong(i++, obj.getCompanyId());
                            } else {
                                pstmtStockTransDetailSerial.setNull(i++, Types.NVARCHAR);
                            }
                            //CAT_PRODUCING_COUNTRY_ID
                            if (obj.getNationalId() != null) {
                                pstmtStockTransDetailSerial.setLong(i++, obj.getNationalId());
                            } else {
                                pstmtStockTransDetailSerial.setNull(i++, Types.NVARCHAR);
                            }
                            //AMOUNT
                            if (obj.getCount() != null) {
                                pstmtStockTransDetailSerial.setDouble(i++, obj.getCount());
                            } else {
                                pstmtStockTransDetailSerial.setNull(i++, Types.DOUBLE);
                            }
                            //GOODS_CODE
                            pstmtStockTransDetailSerial.setString(i++, obj.getMerCode());
                            //GOODS_NAME
                            pstmtStockTransDetailSerial.setString(i++, obj.getMerName());
                            //GOODS_STATE_NAME
                            pstmtStockTransDetailSerial.setString(i++, "Bình thường");
                            //CONSTRUCTION_CODE
                            pstmtStockTransDetailSerial.setNull(i++, Types.NVARCHAR);
                            //SERIAL
                            if (obj.getSerialNumber() != null) {
                                pstmtStockTransDetailSerial.setString(i++, obj.getSerialNumber());
                            } else {
                                pstmtStockTransDetailSerial.setNull(i++, Types.NVARCHAR);
                            }
                            //GOODS_STATE

                            pstmtStockTransDetailSerial.setString(i++, "1");
                            //GOODS_UNIT_ID
                            if (obj.getUnitId() != null) {
                                pstmtStockTransDetailSerial.setLong(i++, obj.getUnitId());
                            } else {
                                pstmtStockTransDetailSerial.setNull(i++, Types.INTEGER);
                            }
                            //GOODS_UNIT_NAME
                            pstmtStockTransDetailSerial.setString(i++, obj.getGoodsUnitName());

                            //SYN_STOCK_TRANS_ID
                            if (type == 2) {
                                pstmtStockTransDetailSerial.setLong(i++, merInExpNoteDTO.getDeliveryNoteId());
                            } else {
                                pstmtStockTransDetailSerial.setLong(i++, merInExpNoteDTO.getReceiptNoteId());
                            }
                            //MER_ENTITY_ID
                            if (obj.getMerEntityId() != null) {
                                pstmtStockTransDetailSerial.setLong(i++, obj.getMerEntityId());
                            } else {
                                pstmtStockTransDetailSerial.setNull(i++, Types.INTEGER);
                            }

                            pstmtStockTransDetailSerial.addBatch();
                            int[] updatedResult;
                            int groupAffectedRows;
                            if (group == maxBatchSize) {
                                logger.info("KTTS EXECUTE BATCH: " + count + "/" + size);
                                groupAffectedRows = 0;
                                updatedResult = pstmtStockTransDetailSerial.executeBatch();
                                for (int returnCode : updatedResult) {
                                    if (returnCode > 0) {
                                        groupAffectedRows++;
                                    }
                                }
                                totalAffectedRows += groupAffectedRows;
                                updatedResultTotal = (int[]) ArrayUtils.addAll(updatedResultTotal, updatedResult);
                                logger.info("ROWS AFFECTED: " + groupAffectedRows + "/" + group);
                                group = 0;
                                con.setAutoCommit(false);
                                this.commit(con);
                            }
                            if (group < maxBatchSize && count == size) {
                                logger.info("KTTS EXCECUTE BATCH: " + count + "/" + size);
                                updatedResult = pstmtStockTransDetailSerial.executeBatch();
                                for (int returnCode : updatedResult) {
                                    if (returnCode > 0) {
                                        totalAffectedRows++;
                                    }
                                }
                                updatedResultTotal = (int[]) ArrayUtils.addAll(updatedResultTotal, updatedResult);
                                logger.info("TOTAL ROWS AFFECTED: " + totalAffectedRows + "/" + size);
                                con.setAutoCommit(false);
                                this.commit(con);
                            }
                        } catch (Exception ex) {
                            logger.info("KTTS UPDATE SYN_STOCK_TRANS_DETAIL_SERIAL FAIL!");
                            logger.error(ex.getMessage(), ex);
                        }
                    }
                }

            } catch (Exception ex) {
                logger.info("KTTS UPDATE SYN_STOCK_TRANS_DETAIL_SERIAL FAIL!");
                logger.error(ex.getMessage(), ex);
            } finally {
                if (pstmtStockTransDetailSerial != null) {
                    try {
                        pstmtStockTransDetailSerial.close();
                    } catch (Exception ex) {
                        logger.error(ex.getMessage(), ex);
                    }
                }
                logger.info("UPDATE SYN_STOCK_TRANS_DETAIL_SERIAL FINISH!");
            }
        } else {
            logger.info("List KTTS INSERT INTO SYN_STOCK_TRANS_DETAIL_SERIAL is empty!");
            return;
        }
        insertSynStockTransDetailSerial(lstWareExpNote, synStockTransDetailId, logger, maxBatchSize, updatedResultTotal, type, con);

        logger.info("UPDATE SYN_STOCK_TRANS_DETAIL_SERIAL FINISH!");
    }

    public synchronized void updateCntConstrWorkItemTask(List<CntContractBO> lstCntContract, int[] updatedResultTotal, Logger logger, long maxBatchSize,
            Connection con) {
        logger.info("UPDATE CntConstrWorkItemTask START!");
        int[] updatedResultTotalConstr = null;
        if (lstCntContract != null && !lstCntContract.isEmpty()) {
            StringBuilder sql = new StringBuilder(" UPDATE CNT_CONSTR_WORK_ITEM_TASK");
            sql.append(" SET PRICE = ?, updated_date = sysdate");
            sql.append(" WHERE CNT_CONTRACT_ID = ? ");
            sql.append(" AND CONSTRUCTION_ID = ? AND STATUS = 1 ");
            sql.append(" AND WORK_ITEM_ID IS NULL AND CAT_TASK_ID IS NULL ");
            PreparedStatement pstmt = null;
            try {
                //get cnt_contract_id and code from cnt_contract
                PreparedStatement stmt = null;

                List<Long> cntContractIdLst = new ArrayList<Long>();
                List<String> cntContractCodeLst = new ArrayList<String>();
                String getCntContractIdByCodeSQL
                        = "SELECT CNT_CONTRACT_ID, CODE FROM CNT_CONTRACT "
                        + "WHERE CONTRACT_TYPE = 0 "
                        + "AND STATUS != 0 AND SYN_STATE = 2";
                stmt = con.prepareStatement(getCntContractIdByCodeSQL);
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    cntContractIdLst.add(rs.getLong("CNT_CONTRACT_ID"));
                    cntContractCodeLst.add(rs.getString("CODE"));
                }
                stmt.close();
                rs.close();

                //get cnt_contract_id and code from cnt_contract
                PreparedStatement stmt1 = null;

                List<Long> constructionIdLst = new ArrayList<Long>();
                List<String> constructionCodeLst = new ArrayList<String>();
                String getCntContractIdByCodeSQL1
                        = "select CONSTRUCTION_ID, code "
                        + "from construction ";
                stmt1 = con.prepareStatement(getCntContractIdByCodeSQL1);
                ResultSet rs1 = stmt1.executeQuery();
                while (rs1.next()) {
                    constructionIdLst.add(rs1.getLong("CONSTRUCTION_ID"));
                    constructionCodeLst.add(rs1.getString("CODE"));
                }
                stmt.close();
                rs1.close();
                //******

                pstmt = con.prepareStatement(sql.toString());
                int count = 0;
                int size = 0;
                for (CntContractBO cntContract : lstCntContract) {
                    if (cntContract.getLstConstrConstructions() != null) {
                        size += cntContract.getLstConstrConstructions().size();
                    }
                }
                int group = 0;
                int totalAffectedRows = 0;

                for (CntContractBO cntContract : lstCntContract) {
                    //continue
                    for (ConstrConstructionsBO obj : cntContract.getLstConstrConstructions()) {
                        int i = 1;
                        try {
                            group++;
                            count++;
                            //PRICE
                            if (obj.getPriceAfterTax() != null) {
                                pstmt.setDouble(i++, obj.getPriceAfterTax());
                            } else {
                                pstmt.setNull(i++, Types.DOUBLE);
                            }
                            //get id from cnt_contract
                            //CNT_CONTRACT_ID
                            Long cntContractIdDB = -1l;
                            for (int r = 0; r < cntContractIdLst.size(); r++) {
                                if (cntContractCodeLst.get(r).equals(cntContract.getCode())) {
                                    cntContractIdDB = cntContractIdLst.get(r);
                                }
                            }

                            pstmt.setLong(i++, cntContractIdDB);
                            //CONSTRUCTION_ID

                            Long constructionIdTemp = obj.getConstructId();

                            for (int r = 0; r < constructionIdLst.size(); r++) {

                                if (constructionCodeLst.get(r) != null) {
                                    if (constructionCodeLst.get(r).equals(obj.getConstrtCode())) {
                                        constructionIdTemp = constructionIdLst.get(r);
                                    }
                                }
                            }
                            pstmt.setLong(i++, constructionIdTemp);

                            pstmt.addBatch();
                            int[] updatedResult;
                            int groupAffectedRows;
                            if (group == maxBatchSize) {
                                logger.info("KTTS EXECUTE BATCH: " + count + "/" + size);
                                groupAffectedRows = 0;
                                updatedResult = pstmt.executeBatch();
                                for (int returnCode : updatedResult) {
                                    if (returnCode > 0) {
                                        groupAffectedRows++;
                                    }
                                }
                                totalAffectedRows += groupAffectedRows;
                                updatedResultTotalConstr = (int[]) ArrayUtils.addAll(updatedResultTotalConstr, updatedResult);
                                logger.info("ROWS AFFECTED: " + groupAffectedRows + "/" + group);
                                group = 0;
                                con.setAutoCommit(false);
                                this.commit(con);
                            }
                            if (group < maxBatchSize && count == size) {
                                logger.info("KTTS EXCECUTE BATCH: " + count + "/" + size);
                                updatedResult = pstmt.executeBatch();
                                for (int returnCode : updatedResult) {
                                    if (returnCode > 0) {
                                        totalAffectedRows++;
                                    }
                                }
                                updatedResultTotalConstr = (int[]) ArrayUtils.addAll(updatedResultTotalConstr, updatedResult);
                                logger.info("TOTAL ROWS AFFECTED: " + totalAffectedRows + "/" + size);
                                con.setAutoCommit(false);
                                this.commit(con);
                            }
                        } catch (Exception ex) {
                            logger.info("KTTS UPDATE CntConstrWorkItemTask FAIL!");
                            logger.error(ex.getMessage(), ex);
                        }
                    }
                }
            } catch (Exception ex) {
                logger.info("KTTS UPDATE CntConstrWorkItemTask FAIL!");
                logger.error(ex.getMessage(), ex);
            } finally {
                if (pstmt != null) {
                    try {
                        pstmt.close();
                    } catch (Exception ex) {
                        logger.error(ex.getMessage(), ex);
                    }
                }
            }
        } else {
            logger.info("List KTTS UPDATE CntConstrWorkItemTask is empty!");
            return;
        }
        insertCntConstrWorkItemTask(lstCntContract, logger, maxBatchSize, updatedResultTotalConstr, con);
        logger.info("UPDATE CntConstrWorkItemTask FINISH!");
    }

    public synchronized void insertCntConstrWorkItemTask(List<CntContractBO> lstCntContract, Logger logger, long maxBatchSize, int[] updatedResultTotalConstr,
            Connection con) {
        logger.info("INSERT CntConstrWorkItemTask START!");
        String codeTest = "";
        Long idTest = 1l;

        if (lstCntContract != null && !lstCntContract.isEmpty()) {
            StringBuilder sql = new StringBuilder(" INSERT INTO CNT_CONSTR_WORK_ITEM_TASK");
            sql.append(" (CNT_CONSTR_WORK_ITEM_TASK_id, created_date, cnt_contract_id, construction_id, price, status) ");
            sql.append(" VALUES (");
            sql.append(" CNT_CONSTR_WORK_ITEM_TASK_SEQ.NEXTVAL,sysdate,?,?,?,1 ");
            sql.append(")");
            PreparedStatement pstmt = null;
            try {
                //get cnt_contract_id and code from cnt_contract
                PreparedStatement stmt = null;

                List<Long> constructionIdLst = new ArrayList<Long>();
                List<String> constructionCodeLst = new ArrayList<String>();
                String getCntContractIdByCodeSQL
                        = "select CONSTRUCTION_ID, code "
                        + "from construction ";
                stmt = con.prepareStatement(getCntContractIdByCodeSQL);
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    constructionIdLst.add(rs.getLong("CONSTRUCTION_ID"));
                    constructionCodeLst.add(rs.getString("CODE"));
                }
                stmt.close();
                rs.close();

                pstmt = con.prepareStatement(sql.toString());
                int count = 0;
                int size = updatedResultTotalConstr.length;
                int group = 0;
                int rowCount = 0;
                for (CntContractBO cntContract : lstCntContract) {
                    for (ConstrConstructionsBO obj : cntContract.getLstConstrConstructions()) {
                        int i = 1;

                        try {
                            if (updatedResultTotalConstr[rowCount] != 0) {
                                rowCount++;
                                size--;
                                if (count != size && group < 2000) {
                                    continue;
                                }
                            } else {
                                group++;
                                count++;
                                rowCount++;
                                //CNT_CONTRACT_ID
                                pstmt.setLong(i++, cntContract.getContractId());
                                //CONSTRUCTION_ID
                                Long constructionIdTemp = obj.getConstructId();

                                for (int r = 0; r < constructionIdLst.size(); r++) {

                                    if (constructionCodeLst.get(r) != null) {
                                        if (constructionCodeLst.get(r).equals(obj.getConstrtCode())) {
                                            constructionIdTemp = constructionIdLst.get(r);
                                        }
                                    }
                                }
                                pstmt.setLong(i++, constructionIdTemp);
                                //PRICE
                                if (obj.getPriceAfterTax() != null) {
                                    pstmt.setDouble(i++, obj.getPriceAfterTax());
                                } else {
                                    pstmt.setNull(i++, Types.DOUBLE);
                                }

                                pstmt.addBatch();
                            }

                            if (group == maxBatchSize) {
                                logger.info("KTTS INSERTED " + count + "/" + updatedResultTotalConstr.length);
                                group = 0;
                                pstmt.executeBatch();
                                con.setAutoCommit(false);
                                this.commit(con);
                            }
                            if (group < maxBatchSize && count == size) {
                                logger.info("KTTS INSERTED " + count + "/" + updatedResultTotalConstr.length);
                                pstmt.executeBatch();
                                con.setAutoCommit(false);
                                this.commit(con);
                            }
                        } catch (Exception ex) {
                            logger.info("KTTS INSERT CntConstrWorkItemTask FAIL!");
                            logger.info("code: " + codeTest + " id: " + idTest);
                            logger.error(ex.getMessage(), ex);
                            logger.error(ex.getMessage(), ex);
                        }
                    }
                }
            } catch (Exception ex) {
                logger.info("KTTS INSERT CntConstrWorkItemTask FAIL!");
                logger.error(ex.getMessage(), ex);
            } finally {
                if (pstmt != null) {
                    try {
                        pstmt.close();
                    } catch (Exception ex) {
                        logger.error(ex.getMessage(), ex);
                    }
                }
            }
        } else {
            logger.info("List KTTS INSERT CntConstrWorkItemTask is empty!");
            return;
        }
        logger.info("INSERT CntConstrWorkItemTask FINISH!");
    }

    public synchronized void insertWorkItem(List<ConstrConstructionsBO> lstConstruction, int[] insertResultTotal, Logger logger, long maxBatchSize) {
        logger.info("INSERT WORK_ITEM START!");

        if (true) {
            StringBuilder sql = new StringBuilder(" INSERT INTO WORK_ITEM");
            sql.append(" (WORK_ITEM_ID, CREATED_DATE, CAT_WORK_ITEM_TYPE_ID, CODE, NAME, CONSTRUCTION_ID, SUPERVISOR_ID, CONSTRUCTOR_ID, IS_INTERNAL, STATUS) ");
            sql.append(" VALUES (");
            sql.append(" WORK_ITEM_SEQ.NEXTVAL,sysdate,?,?,?,?,?,?,1,1 ");
            sql.append(" )");
            PreparedStatement pstmt = null;
            Connection con = null;
            try {
                con = getConnection();
                pstmt = con.prepareStatement(sql.toString());

                List<Long> catWorkItemTypeCBLst = null;
                List<String> codeCBLst = null;
                List<String> nameCBLst = null;

                List<Long> catWorkItemTypeTCLst = null;
                List<String> codeTCLst = null;
                List<String> nameTCLst = null;

                int size = 0;
                int rowCount = 0;
                //chinhpxn
                for (ConstrConstructionsBO constr : lstConstruction) {
                    if (insertResultTotal[rowCount] != 0) {
                        String constrCodeTab = constr.getConstrtCode().substring(0, 2);
                        if (constrCodeTab.equalsIgnoreCase("CB")) {
                            //get work_item with tab cb
                            catWorkItemTypeCBLst = new ArrayList<Long>();
                            codeCBLst = new ArrayList<String>();
                            nameCBLst = new ArrayList<String>();
                            PreparedStatement stmt = null;
                            StringBuilder getWorkItemSQL = new StringBuilder();
                            getWorkItemSQL.append("SELECT CAT_WORK_ITEM_TYPE_ID, CODE, NAME ");
                            getWorkItemSQL.append("FROM CAT_WORK_ITEM_TYPE ");
                            getWorkItemSQL.append("WHERE STATUS = 1 ");
                            getWorkItemSQL.append("AND TAB LIKE '%CB%' ");
                            getWorkItemSQL.append("AND CAT_CONSTRUCTION_TYPE_ID = ? ");
                            stmt = con.prepareStatement(getWorkItemSQL.toString());
                            stmt.setLong(1, constr.getCatConstrTypesId());
                            ResultSet rs = stmt.executeQuery();
                            while (rs.next()) {
                                size++;
                            }
                            stmt.close();
                            rs.close();
                        } else if (constrCodeTab.equalsIgnoreCase("TC")) {
                            //get work_item with tab tc
                            catWorkItemTypeTCLst = new ArrayList<Long>();
                            codeTCLst = new ArrayList<String>();
                            nameTCLst = new ArrayList<String>();
                            PreparedStatement stmt1 = null;
                            StringBuilder getWorkItemSQL2 = new StringBuilder();
                            getWorkItemSQL2.append("SELECT CAT_WORK_ITEM_TYPE_ID, CODE, NAME ");
                            getWorkItemSQL2.append("FROM CAT_WORK_ITEM_TYPE ");
                            getWorkItemSQL2.append("WHERE STATUS = 1 ");
                            getWorkItemSQL2.append("AND TAB LIKE '%TC%' ");
                            getWorkItemSQL2.append("AND CAT_CONSTRUCTION_TYPE_ID = ? ");
                            stmt1 = con.prepareStatement(getWorkItemSQL2.toString());
                            stmt1.setLong(1, constr.getCatConstrTypesId());
                            ResultSet rs1 = stmt1.executeQuery();
                            while (rs1.next()) {
                                size++;
                            }
                            stmt1.close();
                            rs1.close();
                        } else {
                            rowCount++;
                            continue;
                        }

                    } else {
                        rowCount++;
                        continue;
                    }
                }
                //chinhpxn
                rowCount = 0;
                int count = 0;
                int group = 0;
                int workItemSize = 0;

                for (ConstrConstructionsBO constr : lstConstruction) {
                    if (insertResultTotal[rowCount] != 0) {
                        String constrCodeTab = constr.getConstrtCode().substring(0, 2);
                        if (constrCodeTab.equalsIgnoreCase("CB")) {
                            //get work_item with tab cb
                            catWorkItemTypeCBLst = new ArrayList<Long>();
                            codeCBLst = new ArrayList<String>();
                            nameCBLst = new ArrayList<String>();
                            PreparedStatement stmt = null;
                            StringBuilder getWorkItemSQL = new StringBuilder();
                            getWorkItemSQL.append("SELECT CAT_WORK_ITEM_TYPE_ID, CODE, NAME ");
                            getWorkItemSQL.append("FROM CAT_WORK_ITEM_TYPE ");
                            getWorkItemSQL.append("WHERE STATUS = 1 ");
                            getWorkItemSQL.append("AND TAB LIKE '%CB%' ");
                            getWorkItemSQL.append("AND CAT_CONSTRUCTION_TYPE_ID = ? ");
                            stmt = con.prepareStatement(getWorkItemSQL.toString());
                            stmt.setLong(1, constr.getCatConstrTypesId());
                            ResultSet rs = stmt.executeQuery();
                            while (rs.next()) {
                                catWorkItemTypeCBLst.add(rs.getLong("CAT_WORK_ITEM_TYPE_ID"));
                                codeCBLst.add(rs.getString("CODE"));
                                nameCBLst.add(rs.getString("NAME"));
                            }
                            stmt.close();
                            rs.close();
                            //******
                            workItemSize = catWorkItemTypeCBLst.size();
                        } else if (constrCodeTab.equalsIgnoreCase("TC")) {
                            //get work_item with tab tc
                            catWorkItemTypeTCLst = new ArrayList<Long>();
                            codeTCLst = new ArrayList<String>();
                            nameTCLst = new ArrayList<String>();
                            PreparedStatement stmt1 = null;
                            StringBuilder getWorkItemSQL2 = new StringBuilder();
                            getWorkItemSQL2.append("SELECT CAT_WORK_ITEM_TYPE_ID, CODE, NAME ");
                            getWorkItemSQL2.append("FROM CAT_WORK_ITEM_TYPE ");
                            getWorkItemSQL2.append("WHERE STATUS = 1 ");
                            getWorkItemSQL2.append("AND TAB LIKE '%TC%' ");
                            getWorkItemSQL2.append("AND CAT_CONSTRUCTION_TYPE_ID = ? ");
                            stmt1 = con.prepareStatement(getWorkItemSQL2.toString());
                            stmt1.setLong(1, constr.getCatConstrTypesId());
                            ResultSet rs1 = stmt1.executeQuery();
                            while (rs1.next()) {
                                catWorkItemTypeTCLst.add(rs1.getLong("CAT_WORK_ITEM_TYPE_ID"));
                                codeTCLst.add(rs1.getString("CODE"));
                                nameTCLst.add(rs1.getString("NAME"));
                            }
                            stmt1.close();
                            rs1.close();
                            //******
                            workItemSize = catWorkItemTypeTCLst.size();
                        } else {
                            rowCount++;
                            continue;
                        }

                        int j = 0;
                        while (j < workItemSize) {
                            int i = 1;
                            try {
                                group++;
                                count++;
                                if (constrCodeTab.equalsIgnoreCase("CB")) {
                                    //CAT_WORK_ITEM_TYPE_ID
                                    pstmt.setLong(i++, catWorkItemTypeCBLst.get(j));
                                    //CODE
                                    pstmt.setString(i++, constr.getConstrtCode().trim() + "_" + codeCBLst.get(j));
                                    //NAME
                                    pstmt.setString(i++, nameCBLst.get(j));
                                    //CONSTRUCTION_ID
                                    pstmt.setLong(i++, constr.getConstructId());
                                    //SUPERVISOR_ID
                                    pstmt.setLong(i++, constr.getSysGroupId());
                                    //CONSTRUCTOR_ID
                                    pstmt.setLong(i++, constr.getSysGroupId());
                                }

                                if (constrCodeTab.equalsIgnoreCase("TC")) {
                                    //CAT_WORK_ITEM_TYPE_ID
                                    pstmt.setLong(i++, catWorkItemTypeTCLst.get(j));
                                    //CODE
                                    pstmt.setString(i++, constr.getConstrtCode().trim() + "_" + codeTCLst.get(j));
                                    //NAME
                                    pstmt.setString(i++, nameTCLst.get(j));
                                    //CONSTRUCTION_ID
                                    pstmt.setLong(i++, constr.getConstructId());
                                    //SUPERVISOR_ID
                                    pstmt.setLong(i++, constr.getSysGroupId());
                                    //CONSTRUCTOR_ID
                                    pstmt.setLong(i++, constr.getSysGroupId());
                                }

                                pstmt.addBatch();
                                j++;

                                if (group == maxBatchSize) {
                                    pstmt.executeBatch();
                                    con.setAutoCommit(false);
                                    this.commit(con);
                                    logger.info("KTTS WORK ITEM INSERTED " + group);
                                    group = 0;
                                }
                                if (group < maxBatchSize && count == size) {
                                    pstmt.executeBatch();
                                    con.setAutoCommit(false);
                                    this.commit(con);
                                    logger.info("KTTS WORK ITEM INSERTED " + size % maxBatchSize);
                                }
                            } catch (Exception ex) {
                                logger.info("KTTS INSERT work item FAIL!");
                                logger.error(ex.getMessage(), ex);
                            }
                        }
                    } else {
                        rowCount++;
                        continue;
                    }
                    rowCount++;
                }
            } catch (Exception ex) {
                logger.info("KTTS INSERT work item FAIL!");
                logger.error(ex.getMessage(), ex);
            } finally {
                if (pstmt != null) {
                    try {
                        pstmt.close();
                    } catch (Exception ex) {
                        logger.error(ex.getMessage(), ex);
                    }
                }
                if (con != null) {
                    try {
                        close(con);
                    } catch (Exception e) {
                        logger.error(e.getMessage(), e);
                    }
                }
            }
        } else {
            logger.info("List KTTS INSERT work item is empty!");
            return;
        }
        logger.info("INSERT work item FINISH!");
    }

    public synchronized void updateWareImpNote(List<MerInExpNoteDTO> lstWareImpNote, Logger logger, long maxBatchSize, int type) {
        logger.info("UPDATE SYN_STOCK_TRANS IMP START!");
        int[] updatedResultTotal = null;
        HashMap<String, String> synDetailMap = null;
        if (lstWareImpNote != null && !lstWareImpNote.isEmpty()) {
            StringBuilder sqlStockTrans = new StringBuilder("UPDATE SYN_STOCK_TRANS");
            sqlStockTrans.append(" SET STOCK_ID             = ?, ");
            sqlStockTrans.append("   CODE                   = ?, ");
            sqlStockTrans.append("   TYPE                   = ?, ");
            sqlStockTrans.append("   BUSSINESS_TYPE         = ?, ");
            sqlStockTrans.append("   BUSSINESS_TYPE_NAME    = ?, ");
            sqlStockTrans.append("   STATUS                 = ?, ");
            sqlStockTrans.append("   CREATED_BY             = ?, ");
            sqlStockTrans.append("   CREATED_BY_NAME        = ?, ");
            sqlStockTrans.append("   CREATED_DATE           = TO_DATE(?,'YYYY-MM-DD'), ");
            sqlStockTrans.append("   ORDER_CODE             = ?, ");
            sqlStockTrans.append("   ORDER_ID               = ?, ");
            sqlStockTrans.append("   CONSTRUCTION_ID        = ?, ");
            sqlStockTrans.append("   DESCRIPTION            = ?, ");
            sqlStockTrans.append("   SYN_TRANS_TYPE         = ?, ");
            sqlStockTrans.append("   CONSTRUCTION_CODE      = ?, ");
            sqlStockTrans.append("   REAL_IE_TRANS_DATE     = TO_DATE(?,'YYYY-MM-DD'), ");
            sqlStockTrans.append("   REAL_IE_USER_ID        = ?, ");
            sqlStockTrans.append("   REAL_IE_USER_NAME      = ?, ");
            sqlStockTrans.append("   CANCEL_DATE            = TO_DATE(?,'YYYY-MM-DD'), ");
            sqlStockTrans.append("   CANCEL_BY              = ?, ");
            sqlStockTrans.append("   CANCEL_BY_NAME         = ?, ");
            sqlStockTrans.append("   SHIPPER_ID             = ?, ");
            sqlStockTrans.append("   SHIPPER_NAME           = ?, ");
            sqlStockTrans.append("   CREATED_DEPT_ID        = ?, ");
            sqlStockTrans.append("   CREATED_DEPT_NAME      = ?, ");
            sqlStockTrans.append("   STOCK_CODE             = ? ");
            sqlStockTrans.append(" WHERE SYN_STOCK_TRANS_ID = ? ");
            //27

            PreparedStatement pstmtStockTrans = null;
            Connection con = null;
            try {
                con = getConnection();

                //get synStockTransId and merEntityId to get synStockTransDetailId later.
                synDetailMap = new HashMap<String, String>();
                String getSynStockTransDetailIdSQL
                        = " SELECT SYN_STOCK_TRANS_DETAIL_ID, SYN_STOCK_TRANS_ID,"
                        + " MER_ENTITY_ID "
                        + " FROM SYN_STOCK_TRANS_DETAIL_SERIAL";
                PreparedStatement pstmt = con.prepareStatement(getSynStockTransDetailIdSQL);
                ResultSet rs4 = pstmt.executeQuery();
                while (rs4.next()) {
                    String synStockTransDetailIdTemp = String.valueOf(rs4.getLong("SYN_STOCK_TRANS_DETAIL_ID"));
                    String synStockTransIdTemp = String.valueOf(rs4.getLong("SYN_STOCK_TRANS_ID"));
                    String merEntityIdTemp = String.valueOf(rs4.getLong("MER_ENTITY_ID"));
                    synDetailMap.put(synStockTransIdTemp + "|" + merEntityIdTemp, synStockTransDetailIdTemp);
                }
                pstmt.close();
                rs4.close();
                //***********

                pstmtStockTrans = con.prepareStatement(sqlStockTrans.toString());

                int count = 0;
                int totalAffectedRows = 0;
                int size = lstWareImpNote.size();
                int group = 0;
                for (MerInExpNoteDTO obj : lstWareImpNote) {
                    int i = 1;
                    try {
                        group++;
                        count++;
                        String constructionId = null;

                        String sql = "SELECT CONSTRUCTION_ID "
                                + " FROM CONSTRUCTION WHERE UPPER(CODE) = UPPER(?)";
                        PreparedStatement stmt = con.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                        stmt.setString(1, obj.getConstrtCode());
                        ResultSet rs = stmt.executeQuery();
                        while (rs.next()) {
                            constructionId = String.valueOf(rs.getLong("CONSTRUCTION_ID"));
                        }
                        stmt.close();
                        rs.close();
                        //STOCK_ID
                        pstmtStockTrans.setLong(i++, obj.getWarehouseId());
                        //CODE
                        pstmtStockTrans.setString(i++, obj.getImpNoteCode());
                        //TYPE
                        pstmtStockTrans.setLong(i++, 1);
                        //BUSSINESS_TYPE
                        pstmtStockTrans.setLong(i++, 4);
                        //BUSSINESS_TYPE_NAME
                        pstmtStockTrans.setString(i++, "Nhập thu hồi từ công trình");
                        //STATUS
                        pstmtStockTrans.setLong(i++, 2l);
                        //CREATED_BY
                        if (obj.getCreatorId() != null) {
                            pstmtStockTrans.setLong(i++, obj.getCreatorId());
                        } else {
                            pstmtStockTrans.setNull(i++, Types.INTEGER);
                        }
                        //CREATED_BY_NAME
                        if (obj.getCreatedByName() != null) {
                            pstmtStockTrans.setString(i++, obj.getCreatedByName());
                        } else {
                            pstmtStockTrans.setString(i++, "");
                        }
                        //CREATED_DATE
                        if (obj.getCreatedDate() != null) {
                            pstmtStockTrans.setString(i++, String.valueOf(obj.getCreatedDate()).substring(0, 10));
                        } else {
                            pstmtStockTrans.setNull(i++, Types.DATE);
                        }
                        //ORDER_CODE
                        pstmtStockTrans.setString(i++, obj.getImpReqCode());
                        //ORDER_ID
                        if (obj.getImpReqId() != null) {
                            pstmtStockTrans.setLong(i++, obj.getImpReqId());
                        } else {
                            pstmtStockTrans.setNull(i++, Types.INTEGER);
                        }
                        //CONSTRUCTION_ID
                        if (obj.getConstrtCode() != null) {
                            if (constructionId != null) {
                                pstmtStockTrans.setLong(i++, Long.parseLong(constructionId));
                            } else {
                                if (obj.getConstructionId() != null) {
                                    pstmtStockTrans.setLong(i++, obj.getConstructionId());
                                } else {
                                    pstmtStockTrans.setNull(i++, Types.INTEGER);
                                }
                            }
                        } else {
                            if (obj.getConstructionId() != null) {
                                pstmtStockTrans.setLong(i++, obj.getConstructionId());
                            } else {
                                pstmtStockTrans.setNull(i++, Types.INTEGER);
                            }
                        }
                        //DESCRIPTION
                        pstmtStockTrans.setString(i++, obj.getDescription());
                        //SYN_TRANS_TYPE
                        pstmtStockTrans.setString(i++, "1");
                        //CONSTRUCTION_CODE
                        pstmtStockTrans.setString(i++, obj.getConstrtCode());
                        //REAL_IE_TRANS_DATE
                        if (obj.getImportedDate() != null) {
                            pstmtStockTrans.setString(i++, String.valueOf(obj.getImportedDate()).substring(0, 10));
                        } else {
                            pstmtStockTrans.setNull(i++, Types.DATE);
                        }
                        //REAL_IE_USER_ID
                        if (obj.getCreatorId() != null) {
                            pstmtStockTrans.setString(i++, String.valueOf(obj.getCreatorId()));
                        }
                        //REAL_IE_USER_NAME
                        pstmtStockTrans.setString(i++, obj.getCreatedByName());
                        //CANCEL_DATE
                        if (obj.getCancelDate() != null) {
                            pstmtStockTrans.setString(i++, String.valueOf(obj.getCancelDate()).substring(0, 10));
                        } else {
                            pstmtStockTrans.setNull(i++, Types.DATE);
                        }
                        //CANCEL_BY
                        if (obj.getCancelBy() != null) {
                            pstmtStockTrans.setString(i++, String.valueOf(obj.getCancelBy()));
                        } else {
                            pstmtStockTrans.setNull(i++, Types.NVARCHAR);
                        }

                        //CANCEL_BY_NAME
                        if (obj.getCancelByName() != null) {
                            pstmtStockTrans.setString(i++, obj.getCancelByName());
                        } else {
                            pstmtStockTrans.setNull(i++, Types.NVARCHAR);
                        }

                        //SHIPPER_ID
                        if (obj.getShipperId() != null) {
                            pstmtStockTrans.setLong(i++, obj.getShipperId());
                        } else {
                            pstmtStockTrans.setNull(i++, Types.INTEGER);
                        }

                        //SHIPPER_NAME
                        pstmtStockTrans.setString(i++, obj.getDelivererName());
                        //CREATED_DEPT_ID
                        if (obj.getCreatorGroupId() != null) {
                            pstmtStockTrans.setLong(i++, obj.getCreatorGroupId());
                        } else {
                            pstmtStockTrans.setNull(i++, Types.INTEGER);
                        }
                        //CREATED_DEPT_NAME
                        pstmtStockTrans.setString(i++, obj.getCreatedSysGroupName());

                        //STOCK_CODE
                        pstmtStockTrans.setString(i++, obj.getWareHouseCode());

                        //SYN_STOCK_TRANS_ID
                        pstmtStockTrans.setLong(i++, obj.getReceiptNoteId());

                        pstmtStockTrans.addBatch();
                        int[] updatedResult;
                        int groupAffectedRows;
                        if (group == maxBatchSize) {
                            logger.info("KTTS EXECUTE BATCH: " + count + "/" + size);
                            groupAffectedRows = 0;
                            updatedResult = pstmtStockTrans.executeBatch();
                            for (int returnCode : updatedResult) {
                                if (returnCode > 0) {
                                    groupAffectedRows++;
                                }
                            }
                            totalAffectedRows += groupAffectedRows;
                            updatedResultTotal = (int[]) ArrayUtils.addAll(updatedResultTotal, updatedResult);
                            logger.info("ROWS AFFECTED: " + groupAffectedRows + "/" + group);
                            group = 0;
                            con.setAutoCommit(false);
                            this.commit(con);
                        }
                        if (group < maxBatchSize && count == size) {
                            logger.info("KTTS EXCECUTE BATCH: " + count + "/" + size);
                            updatedResult = pstmtStockTrans.executeBatch();
                            for (int returnCode : updatedResult) {
                                if (returnCode > 0) {
                                    totalAffectedRows++;
                                }
                            }
                            updatedResultTotal = (int[]) ArrayUtils.addAll(updatedResultTotal, updatedResult);
                            logger.info("TOTAL ROWS AFFECTED: " + totalAffectedRows + "/" + size);
                            con.setAutoCommit(false);
                            this.commit(con);
                        }
                    } catch (Exception ex) {
                        logger.info("KTTS UPDATE SYN_STOCK_TRANS IMP FAIL!");
                        logger.error(ex.getMessage(), ex);
                    }
                }

                updateSynStockTransDetailSerial(lstWareImpNote, 0l, logger, maxBatchSize, type, synDetailMap, con);
                updateSynStockTransDetail(lstWareImpNote, logger, maxBatchSize, type, con);
                updatedDetailId(logger, maxBatchSize, con);

            } catch (Exception ex) {
                logger.info("KTTS UPDATE SYN_STOCK_TRANS IMP FAIL!");
                logger.error(ex.getMessage(), ex);
            } finally {
                if (con != null) {
                    try {
                        close(con);
                    } catch (Exception e) {
                        logger.error(e.getMessage(), e);
                    }
                }
                if (pstmtStockTrans != null) {
                    try {
                        pstmtStockTrans.close();
                    } catch (Exception ex) {
                        logger.error(ex.getMessage(), ex);
                    }
                }
            }

        } else {
            logger.info("List KTTS UPDATE SYN_STOCK_TRANS IMP is empty!");
            return;
        }
        logger.info("UPDATE SYN_STOCK_TRANS IMP FINISH!");
        insertWareImpNote(lstWareImpNote, logger, maxBatchSize, updatedResultTotal, type, synDetailMap);
    }

    public synchronized void insertWareImpNote(List<MerInExpNoteDTO> lstWareImpNote, Logger logger, long maxBatchSize, int[] updatedResultTotal, int type,
            HashMap<String, String> synDetailMap) {
        logger.info("INSERT SYN_STOCK_TRANS IMP START!");

        if (lstWareImpNote != null && !lstWareImpNote.isEmpty()) {
            StringBuilder sqlStockTrans = new StringBuilder("INSERT INTO SYN_STOCK_TRANS");
            sqlStockTrans.append("   ( ");
            sqlStockTrans.append("     SYN_STOCK_TRANS_ID, ");
            sqlStockTrans.append("     STOCK_ID, ");
            sqlStockTrans.append("     CODE, ");
            sqlStockTrans.append("     TYPE, ");
            sqlStockTrans.append("     BUSSINESS_TYPE, ");
            sqlStockTrans.append("     BUSSINESS_TYPE_NAME, ");
            sqlStockTrans.append("     STATUS, ");
            sqlStockTrans.append("     CREATED_BY, ");
            sqlStockTrans.append("     CREATED_BY_NAME, ");
            sqlStockTrans.append("     CREATED_DATE, ");
            sqlStockTrans.append("     ORDER_CODE, ");
            sqlStockTrans.append("     ORDER_ID, ");
            sqlStockTrans.append("     CONSTRUCTION_ID, ");
            sqlStockTrans.append("     DESCRIPTION, ");
            sqlStockTrans.append("     SHIPMENT_CODE, ");
            sqlStockTrans.append("     SYN_TRANS_TYPE, ");
            sqlStockTrans.append("     CONSTRUCTION_CODE, ");
            sqlStockTrans.append("     REAL_IE_TRANS_DATE, ");
            sqlStockTrans.append("     REAL_IE_USER_ID, ");
            sqlStockTrans.append("     REAL_IE_USER_NAME, ");
            sqlStockTrans.append("     CANCEL_DATE, ");
            sqlStockTrans.append("     CANCEL_BY, ");
            sqlStockTrans.append("     CANCEL_BY_NAME, ");
            sqlStockTrans.append("     SHIPPER_ID, ");
            sqlStockTrans.append("     SHIPPER_NAME, ");
            sqlStockTrans.append("     CREATED_DEPT_ID, ");
            sqlStockTrans.append("     CREATED_DEPT_NAME, ");
            sqlStockTrans.append("     STOCK_CODE ");
            sqlStockTrans.append("   ) ");
            sqlStockTrans.append("   VALUES ");
            sqlStockTrans.append(" ( ");
            sqlStockTrans.append("   ?, ?, ?, 1, ?, ?, ?, ?, ?, TO_DATE(?,'YYYY-MM-DD'), ?, ?, ?, ?, ?, 1, ?, TO_DATE(?,'YYYY-MM-DD'), ?, ?, TO_DATE(?,'YYYY-MM-DD'), ?, ?, ?, ?, ?, ?, ? ");
            sqlStockTrans.append(" ) ");

            PreparedStatement pstmtStockTrans = null;

            Connection con = null;
            try {
                con = getConnection();

                pstmtStockTrans = con.prepareStatement(sqlStockTrans.toString());

                int count = 0;
                int rowCount = 0;
                int size = lstWareImpNote.size();
                int group = 0;
                for (MerInExpNoteDTO obj : lstWareImpNote) {
                    int i = 1;
                    try {
                        if (updatedResultTotal[rowCount] == 1) {
                            rowCount++;
                            size--;
                            if (size != count && group < 2000) {
                                continue;
                            }
                        } else {
                            rowCount++;
                            group++;
                            count++;

                            String constructionId = null;
                            String sql = "SELECT CONSTRUCTION_ID "
                                    + " FROM CONSTRUCTION WHERE UPPER(CODE) = UPPER(?)";
                            PreparedStatement stmt = con.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                            stmt.setString(1, obj.getConstrtCode());
                            ResultSet rs = stmt.executeQuery();
                            while (rs.next()) {
                                constructionId = String.valueOf(rs.getLong("CONSTRUCTION_ID"));

                            }
                            stmt.close();
                            rs.close();

                            //SYN_STOCK_TRANS_ID
                            pstmtStockTrans.setLong(i++, obj.getReceiptNoteId());
                            //STOCK_ID
                            pstmtStockTrans.setLong(i++, obj.getWarehouseId());
                            //CODE
                            pstmtStockTrans.setString(i++, obj.getImpNoteCode());
                            //BUSSINESS_TYPE

                            pstmtStockTrans.setLong(i++, 4);

                            //BUSSINESS_TYPE_NAME
                            pstmtStockTrans.setString(i++, "Nhập thu hồi từ công trình");
                            //STATUS
                            pstmtStockTrans.setLong(i++, 2l);

                            //CREATED_BY
                            if (obj.getCreatorId() != null) {
                                pstmtStockTrans.setLong(i++, obj.getCreatorId());
                            } else {
                                pstmtStockTrans.setNull(i++, Types.INTEGER);
                            }
                            //CREATED_BY_NAME
                            if (obj.getCreatedByName() != null) {
                                pstmtStockTrans.setString(i++, obj.getCreatedByName());
                            } else {
                                pstmtStockTrans.setString(i++, "");
                            }
                            //CREATED_DATE
                            if (obj.getCreatedDate() != null) {
                                pstmtStockTrans.setString(i++, String.valueOf(obj.getCreatedDate()).substring(0, 10));
                            } else {
                                pstmtStockTrans.setNull(i++, Types.DATE);
                            }
                            //ORDER_CODE
                            pstmtStockTrans.setString(i++, obj.getImpReqCode());
                            //ORDER_ID
                            if (obj.getImpReqId() != null) {
                                pstmtStockTrans.setLong(i++, obj.getImpReqId());
                            } else {
                                pstmtStockTrans.setNull(i++, Types.INTEGER);
                            }
                            //CONSTRUCTION_ID
                            if (obj.getConstrtCode() != null) {
                                if (constructionId != null) {
                                    pstmtStockTrans.setLong(i++, Long.parseLong(constructionId));
                                } else {
                                    if (obj.getConstructionId() != null) {
                                        pstmtStockTrans.setLong(i++, obj.getConstructionId());
                                    } else {
                                        pstmtStockTrans.setNull(i++, Types.INTEGER);
                                    }
                                }
                            } else {
                                if (obj.getConstructionId() != null) {
                                    pstmtStockTrans.setLong(i++, obj.getConstructionId());
                                } else {
                                    pstmtStockTrans.setNull(i++, Types.INTEGER);
                                }
                            }
                            //DESCRIPTION
                            pstmtStockTrans.setString(i++, obj.getDescription());
                            //SHIPMENT_CODE
                            pstmtStockTrans.setString(i++, obj.getShipmentNo());

                            //CONSTRUCTION_CODE
                            pstmtStockTrans.setString(i++, obj.getConstrtCode());
                            //REAL_IE_TRANS_DATE
                            if (obj.getImportedDate() != null) {
                                pstmtStockTrans.setString(i++, String.valueOf(obj.getImportedDate()).substring(0, 10));
                            } else {
                                pstmtStockTrans.setNull(i++, Types.DATE);
                            }
                            //REAL_IE_USER_ID
                            if (obj.getCreatorId() != null) {
                                pstmtStockTrans.setString(i++, String.valueOf(obj.getCreatorId()));
                            }
                            //REAL_IE_USER_NAME
                            pstmtStockTrans.setString(i++, obj.getCreatedByName());
                            //CANCEL_DATE
                            if (obj.getCancelDate() != null) {
                                pstmtStockTrans.setString(i++, String.valueOf(obj.getCancelDate()).substring(0, 10));
                            } else {
                                pstmtStockTrans.setNull(i++, Types.DATE);
                            }
                            //CANCEL_BY
                            if (obj.getCancelBy() != null) {
                                pstmtStockTrans.setString(i++, String.valueOf(obj.getCancelBy()));
                            } else {
                                pstmtStockTrans.setNull(i++, Types.NVARCHAR);
                            }

                            //CANCEL_BY_NAME
                            if (obj.getCancelByName() != null) {
                                pstmtStockTrans.setString(i++, obj.getCancelByName());
                            } else {
                                pstmtStockTrans.setNull(i++, Types.NVARCHAR);
                            }

                            //SHIPPER_ID
                            if (obj.getShipperId() != null) {
                                pstmtStockTrans.setLong(i++, obj.getShipperId());
                            } else {
                                pstmtStockTrans.setNull(i++, Types.INTEGER);
                            }

                            //SHIPPER_NAME
                            pstmtStockTrans.setString(i++, obj.getDelivererName());
                            //CREATED_DEPT_ID
                            if (obj.getCreatorGroupId() != null) {
                                pstmtStockTrans.setLong(i++, obj.getCreatorGroupId());
                            } else {
                                pstmtStockTrans.setNull(i++, Types.INTEGER);
                            }
                            //CREATED_DEPT_NAME
                            pstmtStockTrans.setString(i++, obj.getCreatedSysGroupName());
                            //STOCK_CODE
                            pstmtStockTrans.setString(i++, obj.getWareHouseCode());

                            pstmtStockTrans.addBatch();
                        }

                        if (group == maxBatchSize) {
                            logger.info("KTTS INSERTED " + count + "/" + size);
                            group = 0;
                            pstmtStockTrans.executeBatch();
                            con.setAutoCommit(false);
                            this.commit(con);
                        }
                        if (group < maxBatchSize && count == size) {
                            logger.info("KTTS INSERTED " + count + "/" + size);
                            pstmtStockTrans.executeBatch();
                            con.setAutoCommit(false);
                            this.commit(con);
                        }
                    } catch (Exception ex) {
                        logger.info("KTTS INSERT SYN_STOCK_TRANS IMP FAIL!");
                        logger.error(ex.getMessage(), ex);
                    }
                }

                updateSynStockTransDetailSerial(lstWareImpNote, -1l, logger, maxBatchSize, type, synDetailMap, con);
                updateSynStockTransDetail(lstWareImpNote, logger, maxBatchSize, type, con);
                updatedDetailId(logger, maxBatchSize, con);
            } catch (Exception ex) {
                logger.info("KTTS INSERT SYN_STOCK_TRANS IMP FAIL!");
                logger.error(ex.getMessage(), ex);
            } finally {
                if (con != null) {
                    try {
                        close(con);
                    } catch (Exception e) {
                        logger.error(e.getMessage(), e);
                    }
                }
                if (pstmtStockTrans != null) {
                    try {
                        pstmtStockTrans.close();
                    } catch (Exception ex) {
                        logger.error(ex.getMessage(), ex);
                    }
                }
            }
        } else {
            logger.info("List KTTS INSERT SYN_STOCK_TRANS IMP is empty!");
            return;
        }
        logger.info("INSERT SYN_STOCK_TRANS IMP FINISH!");
    }
}
