
import java.io.IOException;
import java.io.StringReader;
import javax.xml.bind.JAXBException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.CharacterData;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 *
 * @author hoanm1
 */
class heheh {

    public static void main(String[] args) throws JAXBException, ParserConfigurationException, SAXException, IOException {

//        String xml = "<Employ><firstName>Yashwant</firstName><lastName>Chavan</lastName><STAFF_CODE>142080</STAFF_CODE></Employ>";
        String xml = "<Employ><STAFF_CODE>142080</STAFF_CODE></Employ>";
//                    + "<UserData>"
//                    + "<Row>"
//                    + "<STAFF_CODE>142080</STAFF_CODE>"
//                    + "</Row>"
//                    + "</UserData>"
//                    + "</Results>"; 
//        JAXBContext jc = JAXBContext.newInstance(Employ.class);
//        Unmarshaller unmarshaller = jc.createUnmarshaller();
//        StreamSource streamSource = new StreamSource(new StringReader(xml));
//        JAXBElement<Employ> je = unmarshaller.unmarshal(streamSource,
//                Employ.class);
//
//        Employ employee = (Employ) je.getValue();
//        System.out.println("First Name:- " + employee.getSTAFF_CODE());
        String output = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
"<Results>\n" +
"    <UserData>\n" +
"        <Row>\n" +
"            <USER_ID>400217</USER_ID>\n" +
"            <USER_RIGHT>0</USER_RIGHT>\n" +
"            <USER_NAME>142080</USER_NAME>\n" +
"            <STATUS>1</STATUS>\n" +
"            <EMAIL>hoanm1@viettel.com.vn</EMAIL>\n" +
"            <CELLPHONE>01656234438</CELLPHONE>\n" +
"            <GENDER>1</GENDER>\n" +
"            <LAST_CHANGE_PASSWORD>11/04/2018</LAST_CHANGE_PASSWORD>\n" +
"            <LOGIN_FAILURE_COUNT>0</LOGIN_FAILURE_COUNT>\n" +
"            <LAST_LOGIN_FAILURE>21/05/2018</LAST_LOGIN_FAILURE>\n" +
"            <IDENTITY_CARD>168352229</IDENTITY_CARD>\n" +
"            <FULL_NAME>Nguyễn Minh Họa</FULL_NAME>\n" +
"            <USER_TYPE_ID>1</USER_TYPE_ID>\n" +
"            <CREATE_DATE>2015-09-04</CREATE_DATE>\n" +
"            <STAFF_CODE>142080</STAFF_CODE>\n" +
"            <LOCATION_ID>201</LOCATION_ID>\n" +
"            <PASSWORDCHANGED>1</PASSWORDCHANGED>\n" +
"            <LAST_LOGIN>11/04/2018</LAST_LOGIN>\n" +
"            <PROFILE_ID>22</PROFILE_ID>\n" +
"            <LAST_RESET_PASSWORD>04/09/2015</LAST_RESET_PASSWORD>\n" +
"            <DEPT_ID>271142</DEPT_ID>\n" +
"            <POS_ID>8362</POS_ID>\n" +
"            <IGNORE_CHECK_IP>0</IGNORE_CHECK_IP>\n" +
"            <CHECK_VALID_TIME>0</CHECK_VALID_TIME>\n" +
"            <START_TIME_TO_CHANGE_PASSWORD>04/09/2015</START_TIME_TO_CHANGE_PASSWORD>\n" +
"            <LAST_LOCK>15/01/2018</LAST_LOCK>\n" +
"            <USE_SALT>1</USE_SALT>\n" +
"            <TEMP_LOCK_COUNT>0</TEMP_LOCK_COUNT>\n" +
"            <MUST_UPDATE_INFO>0</MUST_UPDATE_INFO>\n" +
"            <LANGUAGE>vi-VN</LANGUAGE>\n" +
"            <UPDATE_USER>142080</UPDATE_USER>\n" +
"            <UPDATE_DATE>2018-04-11</UPDATE_DATE>\n" +
"            <IS_VIP>0</IS_VIP>\n" +
"            <LOGIN_FAIL_ALLOW>5</LOGIN_FAIL_ALLOW>\n" +
"            <TEMPORARY_LOCK_TIME>5</TEMPORARY_LOCK_TIME>\n" +
"            <MAX_TMP_LOCK_ADAY>5</MAX_TMP_LOCK_ADAY>\n" +
"            <PASSWORD_VALID_TIME>90</PASSWORD_VALID_TIME>\n" +
"            <USER_VALID_TIME>-1</USER_VALID_TIME>\n" +
"            <ALLOW_MULTI_IP_LOGIN>0</ALLOW_MULTI_IP_LOGIN>\n" +
"            <ALLOW_LOGIN_TIME_START>0</ALLOW_LOGIN_TIME_START>\n" +
"            <ALLOW_LOGIN_TIME_END>23</ALLOW_LOGIN_TIME_END>\n" +
"            <ID>22</ID>\n" +
"            <NAME>PROFILE_DEFAULT</NAME>\n" +
"            <NEED_CHANGE_PASSWORD>1</NEED_CHANGE_PASSWORD>\n" +
"            <TIME_TO_CHANGE_PASSWORD>2</TIME_TO_CHANGE_PASSWORD>\n" +
"            <TIME_TO_PASSWORD_EXPIRE>50</TIME_TO_PASSWORD_EXPIRE>\n" +
"        </Row>\n" +
"    </UserData>\n" +
"</Results>";
        DocumentBuilder db = DocumentBuilderFactory.newInstance()
                .newDocumentBuilder();
        InputSource is = new InputSource();
        is.setCharacterStream(new StringReader(output));

        Document doc = db.parse(is);
        NodeList nodes = ((org.w3c.dom.Document) doc)
                .getElementsByTagName("LAST_LOCK");
        Element element2 = (Element) nodes.item(0);
        Node child = element2.getFirstChild();
        if (child instanceof CharacterData) {
            CharacterData cd = (CharacterData) child;
            String acb = cd.getData();
            System.out.println("First Name:- " + acb);
        }

    }
}
