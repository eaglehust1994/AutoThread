/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.qll.task;

import com.viettel.framework.service.common.Utf8PropertyResourceBundle;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Clob;
import java.sql.SQLException;
import java.util.PropertyResourceBundle;
import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import org.apache.log4j.Logger;
/**
 *
 * @author kh1_cntt
 */
public class MailService {
    private String SMTP_AUTH_PWD = null;
    private String SMTP_AUTH_USER = null;
    private String SMTP_HOST_NAME = null;
    private String EMAIL_FROM_ADDRESS = null; 
    private String EMAIL_SUBJECT_TEXT = null;
    Logger logger = Logger.getLogger(MailService.class);
    
    public String getSMTP_AUTH_PWD() {
        return SMTP_AUTH_PWD;
    }

    public void setSMTP_AUTH_PWD(String SMTP_AUTH_PWD) {
        this.SMTP_AUTH_PWD = SMTP_AUTH_PWD;
    }

    public String getSMTP_AUTH_USER() {
        return SMTP_AUTH_USER;
    }

    public void setSMTP_AUTH_USER(String SMTP_AUTH_USER) {
        this.SMTP_AUTH_USER = SMTP_AUTH_USER;
    }
    
    
    public MailService(){
      
        try {
            
            InputStream input = new FileInputStream("../conf/program.conf");
            PropertyResourceBundle bunder = new PropertyResourceBundle(input);
            Utf8PropertyResourceBundle utf = new Utf8PropertyResourceBundle(bunder);
            try {
                this.SMTP_HOST_NAME = ((String) utf.handleGetObject("SMTP_HOST_NAME"));
            } catch (Exception e) {
                System.out.println(e.getMessage());
                logger.info(e.getMessage(),e);
            }
            
            try {
                this.SMTP_AUTH_USER = ((String) utf.handleGetObject("SMTP_AUTH_USER"));
            } catch (Exception e) {
                System.out.println(e.getMessage());
                logger.info(e.getMessage(),e);
            }
            
            try {
                this.SMTP_AUTH_PWD = ((String) utf.handleGetObject("SMTP_AUTH_PWD"));
            } catch (Exception e) {
                System.out.println(e.getMessage());
                logger.info(e.getMessage(),e);
            }
            
            try {
                this.EMAIL_SUBJECT_TEXT = ((String) utf.handleGetObject("EMAIL_SUBJECT_TEXT"));
            } catch (Exception e) {
                System.out.println(e.getMessage());
                logger.info(e.getMessage(),e);
            }
            
            try {
                this.EMAIL_FROM_ADDRESS = ((String) utf.handleGetObject("EMAIL_FROM_ADDRESS"));
            } catch (Exception e) {
                System.out.println(e.getMessage());
                logger.info(e.getMessage(),e);
            }   
        } catch (IOException e) {
            System.out.println(e);
        }

    }
    
    public String getStringCLob(Clob _content) {
        StringBuilder str = new StringBuilder();
        String strng;
        BufferedReader bufferRead;
        try {
            bufferRead = new BufferedReader(_content.getCharacterStream());
            while ((strng = bufferRead.readLine()) != null) {
                str.append(strng);
            }
        } catch (IOException | SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return str.toString();
    }
    
    private class SMTPAuthenticator extends Authenticator {

        private SMTPAuthenticator() {
        }

        @Override
        protected PasswordAuthentication getPasswordAuthentication() {
            String username = MailService.this.SMTP_AUTH_USER;
            String password = MailService.this.SMTP_AUTH_PWD;

            return new PasswordAuthentication(username, password);
        }
    }
    
    public boolean sendMail(TaskGroupBO warningEmail, MyDbSql myDb){
        boolean checkSendMail;
        try {
            InputStream input = new FileInputStream("../conf/program.conf");
            PropertyResourceBundle bunder = new PropertyResourceBundle(input);
            Utf8PropertyResourceBundle utf = new Utf8PropertyResourceBundle(bunder);
            String smtpHostName = "";
            String userName = "";
            String password = "";
            String passwordReal = "";
            String fromEmail = "";
            
            smtpHostName =(String) utf.handleGetObject("SMTP_HOST_NAME");
            userName = (String) utf.handleGetObject("SMTP_AUTH_USER");
            
            
        } catch (Exception e) {
        }
        
        return true;
    }
    
}
