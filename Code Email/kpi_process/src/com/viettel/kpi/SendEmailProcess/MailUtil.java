package com.viettel.kpi.SendEmailProcess;

import com.viettel.kpi.common.utils.DbTask;
import com.viettel.kpi.client.ftp.ConnectionFTP;
import com.viettel.kpi.common.utils.Utf8PropertyResourceBundle;
import com.viettel.passprotector.PassProtector;
import com.viettel.kpi.SendEmailProcess.EmailSendFrom;
import com.viettel.kpi.SendEmailProcess.MscServer;
import com.viettel.kpi.SendEmailProcess.MyDbTask;
import com.viettel.kpi.common.utils.DateTimeUtils;
import com.viettel.security.PassTranformer;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Clob;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.PropertyResourceBundle;
import java.util.Scanner;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import org.apache.log4j.Logger;

public class MailUtil {

    private String SMTP_AUTH_PWD = null;
    private String SMTP_AUTH_USER = null;
    private String SMTP_HOST_NAME = null;
    private String EMAIL_FROM_ADDRESS = null;
    private String EMAIL_MESSAGE_TEXT_REGISTER_USER = null;
    private String EMAIL_MESSAGE_TEXT_ADDNEW_USER = null;
    private String EMAIL_MESSAGE_TEXT_UPDATE_USER = null;
    private String EMAIL_MESSAGE_TEXT_DELETE_USER = null;
    private String EMAIL_MESSAGE_TEXT_USER_FORGOT_PASSWORD = null;
    private String EMAIL_MESSAGE_TEXT_INCREASE_USER_LEVEL = null;
    private String EMAIL_MESSAGE_TEXT_DOWN_USER_LEVEL = null;
    private String EMAIL_SUBJECT_TEXT_USER_LEVEL_UP_DOWN = null;
    private String EMAIL_SUBJECT_SEND_BACK_ARTICLE_ALERT = null;
    private String EMAIL_CONTENT_SEND_BACK_ARTICLE_ALERT = null;
    private String EMAIL_SUBJECT_TEXT_BUG_REQUEST = null;
    private String EMAIL_MESSAGE_TEXT_BUG_REQUEST = null;
    private String EMAIL_SUBJECT_TEXT = null;
    private String LINK_KM = null;
    Logger logger = Logger.getLogger(MailUtil.class);

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

    public MailUtil() {
        try {
            InputStream input = new FileInputStream("../conf/program.conf");
            PropertyResourceBundle bunder = new PropertyResourceBundle(input);
            Utf8PropertyResourceBundle utf = new Utf8PropertyResourceBundle(bunder);
            try {
                this.SMTP_HOST_NAME = ((String) utf.handleGetObject("SMTP_HOST_NAME"));
            } catch (Exception ex) {
            }
            try {
                this.SMTP_AUTH_USER = ((String) utf.handleGetObject("SMTP_AUTH_USER"));
            } catch (Exception ex) {
            }
            try {
                this.SMTP_AUTH_PWD = ((String) utf.handleGetObject("SMTP_AUTH_PWD"));
            } catch (Exception ex) {
            }
            try {
                this.EMAIL_SUBJECT_TEXT = ((String) utf.handleGetObject("EMAIL_SUBJECT_TEXT"));
            } catch (Exception ex) {
            }

            this.EMAIL_FROM_ADDRESS = ((String) utf.handleGetObject("EMAIL_FROM_ADDRESS"));
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
    }

    public String getStringCLob(Clob _content) {
        StringBuffer str = new StringBuffer();
        String strng;
        BufferedReader bufferRead;
        try {
            bufferRead = new BufferedReader(_content.getCharacterStream());
            while ((strng = bufferRead.readLine()) != null) {
                str.append(strng);
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        return str.toString();
    }

    public boolean sendMail(String content, List<PersionReceiveWarnForm> lst) {
        boolean checkSendMail;
        try {
            InputStream input = new FileInputStream("../conf/program.conf");
            PropertyResourceBundle bunder = new PropertyResourceBundle(input);
            Utf8PropertyResourceBundle utf = new Utf8PropertyResourceBundle(bunder);

            // truy cap vao DB de lay danh sach gui tin nhan
            String smtpHostName = "";
            String userName = "";
            String password = "";
            String emailSubjectTxt = "";
            String emailBodyTxt = "";
            String emailFromAddress = "";
            try {
                smtpHostName = (String) utf.handleGetObject("SMTP_HOST_NAME");
            } catch (Exception ex) {
                logger.info("SMTP_HOST_NAME rong");
            }
            try {
                userName = (String) utf.handleGetObject("SMTP_AUTH_USER");
            } catch (Exception ex) {
                logger.info("SMTP_AUTH_USER rong");
            }
            try {
                password = (String) utf.handleGetObject("SMTP_AUTH_PWD");
            } catch (Exception ex) {
                logger.info("SMTP_AUTH_PWD rong");
            }
            try {
                emailSubjectTxt = (String) utf.handleGetObject("EMAIL_SUBJECT_TEXT");
            } catch (Exception ex) {
                logger.info("EMAIL_SUBJECT_TEXT rong");
            }
            try {
                emailBodyTxt = (String) utf.handleGetObject("EMAIL_MESSAGE_TEXT");
                emailBodyTxt = String.format(emailBodyTxt, new Object[]{content});
            } catch (Exception ex) {
                logger.info("EMAIL_MESSAGE_TEXT rong");
            }
            try {
                emailFromAddress = (String) utf.handleGetObject("EMAIL_FROM_ADDRESS");
            } catch (Exception ex) {
                logger.info("EMAIL_FROM_ADDRESS rong");
            }
            this.SMTP_AUTH_USER = userName;
            this.SMTP_AUTH_PWD = password;

            checkSendMail = postMail(lst, emailSubjectTxt, emailBodyTxt, emailFromAddress, smtpHostName);

        } catch (Exception ex) {
            checkSendMail = false;
            logger.info("Gui mail bi loi");
            logger.error(ex.getMessage(), ex);
        }
        return checkSendMail;
    }

    public boolean sendMail(EmailSendFrom emailSendFrom, MyDbTask db, List<String> emailLst) {
        boolean checkSendMail;
        try {
            InputStream input = new FileInputStream("../conf/program.conf");
            PropertyResourceBundle bunder = new PropertyResourceBundle(input);
            Utf8PropertyResourceBundle utf = new Utf8PropertyResourceBundle(bunder);
            String smtpHostName = "";
            String userName = "";
            String password = "";
            String passwordReal = "";
            String emailSubjectTxt = "";
            String emailBodyTxt = "";
            String fromEmail = "";
            String dir = "";

            try {
                dir = (String) utf.handleGetObject("LOCAL_DIR");
            } catch (Exception ex) {
                logger.info("LOCAL_DIR rong");
            }
            try {
                smtpHostName = (String) utf.handleGetObject("SMTP_HOST_NAME");
            } catch (Exception ex) {
                logger.info("SMTP_HOST_NAME rong");
            }
            try {
                userName = (String) utf.handleGetObject("SMTP_AUTH_USER");
            } catch (Exception ex) {
                logger.info("SMTP_AUTH_USER rong");
            }
            try {
                passwordReal = ((String) utf.handleGetObject("SMTP_AUTH_PWD"));
                password = PassTranformer.decrypt(passwordReal);
            } catch (Exception ex) {
                logger.info("SMTP_AUTH_PWD rong");
            }
            try {
                fromEmail = (String) utf.handleGetObject("EMAIL_FROM_ADDRESS");
            } catch (Exception ex) {
                logger.info("EMAIL_FROM_ADDRESS rong");
            }
            this.SMTP_AUTH_USER = userName;
            this.SMTP_AUTH_PWD = password;

            String fileName = emailSendFrom.getFileName();
            checkSendMail = postMail(emailSendFrom, emailLst, fromEmail, smtpHostName, db, dir);
        } catch (Exception ex) {
            checkSendMail = false;
            logger.info("Gui mail bi loi");
            logger.error(ex.getMessage(), ex);
        }
//        return checkSendMail;
        return true;
    }

    public boolean postMail(EmailSendFrom emailSendFrom, List<String> lstEmail,
            String fromEmail, String smtpHostName, MyDbTask db, String dir) {
        boolean checkMail = true;
        boolean debug = false;
        try {
            int emailId = emailSendFrom.getEmailId();
            String subject = emailSendFrom.getSubject();
            String content = getStringCLob(emailSendFrom.getContent());
            int isFile = emailSendFrom.getIsFile();
            String fileName = emailSendFrom.getFileName();
            String path = emailSendFrom.getPath();
            String log_server_id = emailSendFrom.getLogServerId();

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
            msg.setFrom(addressFrom);
            msg.setSubject(subject, "UTF8");
            msg.setSentDate(new Date());
            Date startTime = new Date();
            InternetAddress[] addressTo = new InternetAddress[lstEmail.size()];
            int i = 0;
            String errorEmail = "";
            for (String recipients : lstEmail) {
                try {
                    addressTo[i] = new InternetAddress(recipients);
                    msg.setRecipients(Message.RecipientType.TO, addressTo[i].getAddress());
                    Multipart multipart = new MimeMultipart();

                    if (isFile == 1) {
                        ConnectionFTP ftpClient = new ConnectionFTP();
                        MscServer msc = new MscServer();
                        try {
                            msc = db.getLogServerID(log_server_id);

                        } catch (Exception e) {
                            logger.info(" ======= Login Server File Fail ========");
                            logger.error(e.getMessage(), e);
                        }
                        String[] lstFileName = fileName.split(",");

                        DataSource source;
                        int j = 0;
                        for (String filename : lstFileName) {
                            try {
                                //hoanm1_update_28042016
                                //muc tuan
//                                Date timeWeek = new Date();
//                                String updateTime = DateTimeUtils.convertDateTimeToString(timeWeek, "dd/MM/yyyy");
//                                SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
//                                Date dateTime = df.parse(updateTime);
//                                Calendar cal = Calendar.getInstance();
//                                cal.setTime(dateTime);
//                                int week = cal.get(Calendar.WEEK_OF_YEAR);
//                                int year = cal.get(Calendar.YEAR);
//                                String tuan = +week + "_" + year;
//                                String fileWeek = filename + "_" + tuan + ".xlsx";
//                                File fileExport = new File(path + fileWeek);

                                //muc ngay
                                Date timeWeek = new Date();
                                Calendar c = Calendar.getInstance();
                                c.setTime(timeWeek);
                                c.add(Calendar.DATE, -1);
                                timeWeek.setTime(c.getTime().getTime());
                                String updateTime = DateTimeUtils.convertDateTimeToString(timeWeek, "ddMMyyyy");
                                String fileWeek = filename + "_" + updateTime + ".xlsx";
                                File fileExport = new File(path + fileWeek);
                                //hoanm1_update_28042016
                                MimeBodyPart attachPart = new MimeBodyPart();
                                source = new FileDataSource(fileExport);
                                attachPart.setDataHandler(new DataHandler(source));
                                attachPart.setFileName(fileExport.getName());
                                multipart.addBodyPart(attachPart);
                            } catch (Exception e) {
                                logger.error("Attach File Eror : " + e.getMessage(), e);
                                logger.error("Can't attach file :" + filename);
                            }
                        }
                        MimeBodyPart mbText = new MimeBodyPart();
                        mbText.setContent(content, "text/html;charset=UTF-8");
                        multipart.addBodyPart(mbText);
                        msg.setContent(multipart);
                    } else {
                        msg.setContent(content, "text/html;charset=\"UTF-8\"");
                    }
                    Transport.send(msg);
                    logger.info("Gui mail thanh cong den dia chi: " + addressTo[i].getAddress());
                } catch (Exception ex) {
                    errorEmail = errorEmail + addressTo[i].getAddress();
                    logger.info(addressTo[i].getAddress() + " Error! not send");
                    logger.error(ex.getMessage(), ex);
                }
            }
            if (!errorEmail.equals("")) {
                checkMail = false;
                errorEmail = "Don't send email : " + errorEmail + " , ";
            }
            db.updateEmail(emailId, errorEmail, startTime);
        } catch (Exception ex) {
            logger.info("Gui mail bi loi");
            logger.error(ex.getMessage(), ex);
            checkMail = false;
        }
        return checkMail;
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

    public boolean postMail(List<PersionReceiveWarnForm> recipients, String subject,
            String message, String from, String smtpHostName) {
        boolean checkMail = true;
        boolean debug = false;
        try {
            Properties props = new Properties();

            props.put("mail.smtp.host", smtpHostName);
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "false");
            props.put("mail.smtp.startssl.enable", "false");
            props.put("mail.smtp.sendpartial", "true");

            Authenticator auth = new SMTPAuthenticator();

            Session session = Session.getDefaultInstance(props, auth);
            session.setDebug(debug);

            MimeMessage msg = new MimeMessage(session);
            InternetAddress addressFrom = new InternetAddress(from);
            msg.setFrom(addressFrom);
            msg.setSubject(subject, "UTF8");
            msg.setSentDate(new Date());
            if (!recipients.isEmpty()) {
                InternetAddress[] addressTo = new InternetAddress[recipients.size()];

                int i = 0;
                int count = 0;

                for (PersionReceiveWarnForm bo : recipients) {
                    try {
                        addressTo[i] = new InternetAddress(bo.getEmail());
                        msg.setRecipients(Message.RecipientType.TO, addressTo[i].getAddress());
                        msg.setContent(message, "text/html;charset=\"UTF-8\"");
                        Transport.send(msg);
                        logger.info("Gui mail thanh cong den dia chi: " + addressTo[i].getAddress());
                        i++;
                    } catch (Exception e) {
                        count++;
                        logger.info(addressTo[i].getAddress() + " Error! not send");
                    }
                }
                if (count == recipients.size()) {
                    checkMail = false;
                }

            } else {
                logger.info("khong co danh sach gui Email");
                checkMail = false;
            }

        } catch (Exception ex) {
            logger.info("Gui mail bi loi");
            checkMail = false;
            logger.error(ex.getMessage(), ex);
        }

        return checkMail;
    }

    private class SMTPAuthenticator extends Authenticator {

        private SMTPAuthenticator() {
        }

        @Override
        protected PasswordAuthentication getPasswordAuthentication() {
            String username = MailUtil.this.SMTP_AUTH_USER;
            String password = MailUtil.this.SMTP_AUTH_PWD;

            return new PasswordAuthentication(username, password);
        }
    }
}
