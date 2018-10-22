/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.kpi.common.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import org.apache.log4j.Logger;

/**
 *
 * @author qlmvt_minhht1
 */
public class DataConfig {

    private static final String CONFIG_FILE_DEFAULT = "../conf/program.conf";
    private static String configFile;
    private static Logger logger = Logger.getLogger(DataConfig.class);

    public DataConfig() {
    }

    public static void init() {
        DataConfig.configFile = CONFIG_FILE_DEFAULT;
    }

    public static void init(String configFile) {
        DataConfig.configFile = configFile;
    }

    public synchronized static String getStringProperties(String propertiesName) {
        String result = "";
        Properties propertiesFile = new Properties();
        FileInputStream propsFile = null;
        try {
            propsFile = new FileInputStream(configFile);
            propertiesFile.load(propsFile);
            try {
                result = propertiesFile.getProperty(propertiesName).trim();
            } catch (Exception ex) {
                logger.error("Can not get properties [" + propertiesName + "] value: " + ex.getMessage());
            }
        } catch (FileNotFoundException e) {
            logger.error("Server config file not found", e);
            throw new RuntimeException("Server config not found.", e);

        } catch (IOException e) {
            logger.error("Error while load properties in config file " + configFile, e);
            throw new RuntimeException("Error while load properties in config file " + configFile, e);

        } finally {
            if (propsFile != null) {
                try {
                    propsFile.close();

                } catch (IOException e) {
                    logger.error("Fail to close data config properties file " + e.getMessage());
                }
            }
        }
        return result;
    }

    public synchronized static String getStringProperties(String propertiesName, String defaultValue) {
        String result = defaultValue;
        Properties propertiesFile = new Properties();
        FileInputStream propsFile = null;
        try {
            propsFile = new FileInputStream(configFile);
            propertiesFile.load(propsFile);
            try {
                result = propertiesFile.getProperty(propertiesName).trim();
            } catch (Exception ex) {
                logger.error("Can not get properties [" + propertiesName + "] value: " + ex.getMessage());
            }
        } catch (FileNotFoundException e) {
            logger.error("Server config file not found", e);
            throw new RuntimeException("Server config not found.", e);

        } catch (IOException e) {
            logger.error("Error while load properties in config file " + configFile, e);
            throw new RuntimeException("Error while load properties in config file " + configFile, e);

        } finally {
            if (propsFile != null) {
                try {
                    propsFile.close();

                } catch (IOException e) {
                    logger.error("Fail to close data config properties file " + e.getMessage());
                }
            }
        }
        return result;
    }

    public synchronized static int getIntProperties(String propertiesName) {
        int result = 0;
        Properties propertiesFile = new Properties();
        FileInputStream propsFile = null;
        try {
            propsFile = new FileInputStream(configFile);
            propertiesFile.load(propsFile);
            try {
                result = Integer.parseInt(propertiesFile.getProperty(propertiesName).trim());
            } catch (Exception ex) {
                logger.error("Can not get properties [" + propertiesName + "] value: " + ex.getMessage());
            }
        } catch (FileNotFoundException e) {
            logger.error("Server config file not found", e);
            throw new RuntimeException("Server config not found.", e);

        } catch (IOException e) {
            logger.error("Error while load properties in config file " + configFile, e);
            throw new RuntimeException("Error while load properties in config file " + configFile, e);

        } finally {
            if (propsFile != null) {
                try {
                    propsFile.close();

                } catch (IOException e) {
                    logger.error("Fail to close data config properties file " + e.getMessage());
                }
            }
        }
        return result;
    }

    public synchronized static int getIntProperties(String propertiesName, int defaultValue) {
        int result = defaultValue;
        Properties propertiesFile = new Properties();
        FileInputStream propsFile = null;
        try {
            propsFile = new FileInputStream(configFile);
            propertiesFile.load(propsFile);
            try {
                result = Integer.parseInt(propertiesFile.getProperty(propertiesName).trim());
            } catch (Exception ex) {
                logger.error("Can not get properties [" + propertiesName + "] value: " + ex.getMessage());
            }
        } catch (FileNotFoundException e) {
            logger.error("Server config file not found", e);
            throw new RuntimeException("Server config not found.", e);

        } catch (IOException e) {
            logger.error("Error while load properties in config file " + configFile, e);
            throw new RuntimeException("Error while load properties in config file " + configFile, e);

        } finally {
            if (propsFile != null) {
                try {
                    propsFile.close();

                } catch (IOException e) {
                    logger.error("Fail to close data config properties file " + e.getMessage());
                }
            }
        }
        return result;
    }

    public synchronized static long getLongProperties(String propertiesName) {
        long result = 0;
        Properties propertiesFile = new Properties();
        FileInputStream propsFile = null;
        try {
            propsFile = new FileInputStream(configFile);
            propertiesFile.load(propsFile);
            try {
                result = Integer.parseInt(propertiesFile.getProperty(propertiesName).trim());
            } catch (Exception ex) {
                logger.error("Can not get properties [" + propertiesName + "] value: " + ex.getMessage());
            }
        } catch (FileNotFoundException e) {
            logger.error("Server config file not found", e);
            throw new RuntimeException("Server config not found.", e);

        } catch (IOException e) {
            logger.error("Error while load properties in config file " + configFile, e);
            throw new RuntimeException("Error while load properties in config file " + configFile, e);

        } finally {
            if (propsFile != null) {
                try {
                    propsFile.close();

                } catch (IOException e) {
                    logger.error("Fail to close data config properties file " + e.getMessage());
                }
            }
        }
        return result;
    }

    public synchronized static long getLongProperties(String propertiesName, long defaultValue) {
        long result = defaultValue;
        Properties propertiesFile = new Properties();
        FileInputStream propsFile = null;
        try {
            propsFile = new FileInputStream(configFile);
            propertiesFile.load(propsFile);
            try {
                result = Integer.parseInt(propertiesFile.getProperty(propertiesName).trim());
            } catch (Exception ex) {
                logger.error("Can not get properties [" + propertiesName + "] value: " + ex.getMessage());
            }
        } catch (FileNotFoundException e) {
            logger.error("Server config file not found", e);
            throw new RuntimeException("Server config not found.", e);

        } catch (IOException e) {
            logger.error("Error while load properties in config file " + configFile, e);
            throw new RuntimeException("Error while load properties in config file " + configFile, e);

        } finally {
            if (propsFile != null) {
                try {
                    propsFile.close();

                } catch (IOException e) {
                    logger.error("Fail to close data config properties file " + e.getMessage());
                }
            }
        }
        return result;
    }

    public synchronized static boolean getBoolProperties(String propertiesName) {
        boolean result = false;
        Properties propertiesFile = new Properties();
        FileInputStream propsFile = null;
        try {
            propsFile = new FileInputStream(configFile);
            propertiesFile.load(propsFile);
            try {
                result = Boolean.parseBoolean(propertiesFile.getProperty(propertiesName).trim());
            } catch (Exception ex) {
                logger.error("Can not get properties [" + propertiesName + "] value: " + ex.getMessage());
            }
        } catch (FileNotFoundException e) {
            logger.error("Server config file not found", e);
            throw new RuntimeException("Server config not found.", e);

        } catch (IOException e) {
            logger.error("Error while load properties in config file " + configFile, e);
            throw new RuntimeException("Error while load properties in config file " + configFile, e);

        } finally {
            if (propsFile != null) {
                try {
                    propsFile.close();

                } catch (IOException e) {
                    logger.error("Fail to close data config properties file " + e.getMessage());
                }
            }
        }
        return result;
    }

    public synchronized static boolean getBoolProperties(String propertiesName, boolean defaultValue) {
        boolean result = defaultValue;
        Properties propertiesFile = new Properties();
        FileInputStream propsFile = null;
        try {
            propsFile = new FileInputStream(configFile);
            propertiesFile.load(propsFile);
            try {
                result = Boolean.parseBoolean(propertiesFile.getProperty(propertiesName).trim());
            } catch (Exception ex) {
                logger.error("Can not get properties [" + propertiesName + "] value: " + ex.getMessage());
            }
        } catch (FileNotFoundException e) {
            logger.error("Server config file not found", e);
            throw new RuntimeException("Server config not found.", e);

        } catch (IOException e) {
            logger.error("Error while load properties in config file " + configFile, e);
            throw new RuntimeException("Error while load properties in config file " + configFile, e);

        } finally {
            if (propsFile != null) {
                try {
                    propsFile.close();

                } catch (IOException e) {
                    logger.error("Fail to close data config properties file " + e.getMessage());
                }
            }
        }
        return result;
    }

}
