package com.viettel.kpi.service.common;

/**
 *
 * @author qlmvt_KhiemVK
 */
public class DataTypes {

      public enum eDataType{
        NUM,
        STR,
        DATE,
        INT,
        NONE;
      }

      public static eDataType getDataType(String dataType){
          if(dataType==null)
              return eDataType.NONE;
          if(dataType.trim().equalsIgnoreCase("NUM"))
              return eDataType.NUM;
          if(dataType.trim().equalsIgnoreCase("STR"))
              return eDataType.STR;
          if(dataType.trim().equalsIgnoreCase("STRING"))
              return eDataType.STR;
          if(dataType.trim().equalsIgnoreCase("DATE"))
              return eDataType.DATE;
          if(dataType.trim().equalsIgnoreCase("INT"))
              return eDataType.INT;
          return eDataType.NONE;
      }
}
