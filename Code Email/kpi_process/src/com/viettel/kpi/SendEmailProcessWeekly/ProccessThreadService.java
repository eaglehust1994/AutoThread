/*
 * Copyright 2012 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.viettel.kpi.SendEmailProcessWeekly;

import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author dungvv8
 */
public class ProccessThreadService implements Runnable {

    private String senderEmailCode;
    private static final Logger logger = Logger.getLogger(ProccessThreadService.class);

    public ProccessThreadService(String senderEmailCode) throws Exception {
        this.senderEmailCode = senderEmailCode;
    }

    @Override
    public void run() {
        try {
            MyDbTask db = new MyDbTask();
            List<EmailSendFrom> lstEmail = db.getEmail(senderEmailCode);
//            ArrayList<String> emailLst = db.getEmailList(typeEmail);
//            List<String> emailLst = db.getEmailList(typeEmail);

//            List<String> emailLst = new ArrayList<String>();
//            emailLst.add("hoanm1@viettel.com.vn");
            logger.info("==========Get Email Success=========");
            for (EmailSendFrom lst : lstEmail) {
                List<String> emailLst = db.getlstMail(lst);
                if (!emailLst.isEmpty()) {
                    logger.info("***** Bat dau gui mail *****");
                    try {
                        MailUtil mailUtil = new MailUtil();
                        boolean checkSendmail = mailUtil.sendMail(lst, db, emailLst);
                        // boolean checkSendmail = false;
                        //logger.info("Loai Tien Trinh: " + typeEmail);
                        if (checkSendmail) {
                            logger.info("***** Gui mail thanh cong *****");
                        } else {
                            logger.info("***** Khong gui duoc Email *****");
                        }
                    } catch (Exception ex) {
                        logger.error(ex.getMessage(), ex);
                    }
                } else {
                    logger.info("***** Khong co email nao can gui *****");
                }
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);

        }
    }
}
