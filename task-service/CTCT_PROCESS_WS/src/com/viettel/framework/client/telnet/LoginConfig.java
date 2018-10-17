/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.framework.client.telnet;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.NodeList;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 *
 * @author qlmvt_dungnt32
 */
public class LoginConfig {

    private String loginConfigFile;

    public LoginConfig(String loginConfigFile) {
        this.loginConfigFile = loginConfigFile;
    }

    public ObjectLogin readConfig() {
        ObjectLogin objectLogin = null;
        try {
            File file = new File(loginConfigFile);
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(file);
            doc.getDocumentElement().normalize();
            NodeList commandLst = doc.getElementsByTagName("promt");
            objectLogin = new ObjectLogin();
            List<Promt> fromtList = new ArrayList<Promt>();
            Promt promt = null;
            for (int s = 0; s < commandLst.getLength(); s++) {
                Node fstNode = commandLst.item(s);
                promt = new Promt();
                if (commandLst.item(s).getNodeType() == Node.ELEMENT_NODE) {
                    Element eValue = (Element) commandLst.item(s);
                    String type = eValue.getAttribute("type");
                    promt.setId(type);
                    NodeList nodeValueList = eValue.getChildNodes();
                    String valueNode = ((Node) nodeValueList.item(0)).getNodeValue();
                    promt.setValue(valueNode.trim());
                }
                if (fstNode.getNodeType() == Node.ELEMENT_NODE) {
                    try {
                        Element element = (Element) fstNode;
                        NodeList responseNodeList = element.getElementsByTagName("response");
                        Element eResponse = (Element) responseNodeList.item(0);
                        NodeList nodeList = eResponse.getChildNodes();
                        String response = ((Node) nodeList.item(0)).getNodeValue();
                        promt.setResponse(response);
                    } catch (Exception e) {
                    }
                }
                fromtList.add(promt);
            }
            objectLogin.setPromtList(fromtList);

            NodeList shellNodeList = doc.getElementsByTagName("shellPromt");
            for (int s = 0; s < shellNodeList.getLength(); s++) {
                if (shellNodeList.item(s).getNodeType() == Node.ELEMENT_NODE) {
                    try {
                        Element eValue = (Element) shellNodeList.item(s);
                        NodeList nodeValueList = eValue.getChildNodes();
                        String valueNode = ((Node) nodeValueList.item(0)).getNodeValue();
                        objectLogin.setShellPromt(valueNode.trim());
                    } catch (Exception e) {
                    }

                }
            }
            NodeList terminatorNodeList = doc.getElementsByTagName("commandTerminator");
            for (int s = 0; s < terminatorNodeList.getLength(); s++) {
                if (terminatorNodeList.item(s).getNodeType() == Node.ELEMENT_NODE) {
                    Element eValue = (Element) terminatorNodeList.item(s);
                    String type = eValue.getAttribute("type");
                    System.out.print(type);
                    NodeList nodeValueList = eValue.getChildNodes();
                    String valueNode = ((Node) nodeValueList.item(0)).getNodeValue();
                    objectLogin.setCommandTerminator(valueNode.trim());
                }
            }

            NodeList msgLoginNodeList = doc.getElementsByTagName("msgLoginSuccess");
            for (int s = 0; s < msgLoginNodeList.getLength(); s++) {
                if (msgLoginNodeList.item(s).getNodeType() == Node.ELEMENT_NODE) {
                    Element eValue = (Element) msgLoginNodeList.item(s);
                    NodeList nodeValueList = eValue.getChildNodes();
                    String valueNode = ((Node) nodeValueList.item(0)).getNodeValue();
                    objectLogin.setMsgLoginSuccess(valueNode.trim());
                }
            }
            NodeList cmdExitNodeList = doc.getElementsByTagName("cmdExit");
            for (int s = 0; s < cmdExitNodeList.getLength(); s++) {
                if (cmdExitNodeList.item(s).getNodeType() == Node.ELEMENT_NODE) {
                    Element eValue = (Element) cmdExitNodeList.item(s);
                    NodeList nodeValueList = eValue.getChildNodes();
                    String valueNode = ((Node) nodeValueList.item(0)).getNodeValue();
                    objectLogin.setCmdExit(valueNode.trim());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.gc();
        }
        return objectLogin;
    }
}
