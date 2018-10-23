/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.qll.task;

import com.viettel.framework.service.common.Utf8PropertyResourceBundle;
import com.viettel.security.PassTranformer;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Properties;
import java.util.PropertyResourceBundle;
import java.util.Scanner;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
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
                this.EMAIL_FROM_ADDRESS = ((String) utf.handleGetObject("EMAIL_FROM_ADDRESS"));
            } catch (Exception e) {
                System.out.println(e.getMessage());
                logger.info(e.getMessage(),e);
            }   
        } catch (IOException e) {
            System.out.println(e);
        }

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
    
    public boolean sendMail(String email, String taskName, String endTime){
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
            
            try {
                smtpHostName =(String) utf.handleGetObject("SMTP_HOST_NAME");
            } catch (Exception e) {
                logger.info(e.getMessage());
            }
            
            try {
                userName = (String) utf.handleGetObject("SMTP_AUTH_USER");
            } catch (Exception e) {
                logger.info(e.getMessage());
            }
            
            try {
                passwordReal = (String) utf.handleGetObject("SMTP_AUTH_PWD");
                password = PassTranformer.decrypt(passwordReal);
            } catch (Exception e) {
                logger.info(e.getMessage());
            }
            
             try {
                fromEmail = (String) utf.handleGetObject("EMAIL_FROM_ADDRESS");
            } catch (Exception e) {
                logger.info(e.getMessage());
            }
            this.SMTP_AUTH_USER = userName;
            this.SMTP_AUTH_PWD = password;

            checkSendMail = postMail(email, fromEmail, smtpHostName, taskName, endTime);
            
        } catch (Exception e) {
            checkSendMail = false;
            logger.info("Gui mail bi loi");
            logger.error(e.getMessage(), e);
        }       
        return true;
    }
    
    public String readFile(String filename) {
        String content = "";
        try {
            FileInputStream is = new FileInputStream(filename);
            Scanner input = new Scanner(is);
            while (input.hasNextLine()) {
                String line = input.nextLine();
                content += line;
                content += "\n";
            } //Close thread
            input.close();
            is.close();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        return content;
    }
    public boolean postMail (String toEmail,String fromEmail,String smtpHostName,String taskName, String endTime){
        boolean checkMail = true ;
        boolean debug = false;
        try {
            String subject = "[Phòng Kế Hoạch] nhắc việc";
            String content = "Đ/c cần thực hiện công việc: " + taskName + " trước " + endTime + ". Trân trọng";
            
            Properties props = new Properties();
            String config = readFile("../conf/emailconfig.txt");
            String[] configEmail = config.split("\n");
            props.put(configEmail[0], smtpHostName);
            for (int i = 1; i < configEmail.length; i++) {
                String[] spl = configEmail[i].split(",");
                props.put(spl[0], spl[1]);
            }
            Authenticator auth = new SMTPAuthenticator();
            Session session = Session.getDefaultInstance(props, auth);
            session.setDebug(debug);
            MimeMessage msg = new MimeMessage(session);
            InternetAddress addressFrom = new InternetAddress(fromEmail);
            InternetAddress addressTo = new InternetAddress(toEmail);
            msg.setFrom(addressFrom);
            msg.setSubject(subject, "UTF8");
            msg.setSentDate(new Date());
            msg.setRecipient(Message.RecipientType.TO,addressTo );
            msg.setText(content);
            Transport.send(msg);
            logger.info("gửi mail thành công đến địa chỉ :" +fromEmail);
        } catch (Exception e) {
           logger.info(fromEmail + " Error! not send");
            System.out.println(e.getMessage());
           checkMail = false;        
        }
        
        return checkMail;
    }
}