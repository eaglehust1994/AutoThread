
import com.sun.org.apache.xerces.internal.parsers.DOMParser;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
//import javax.xml.bind.Element;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
//import org.w3c.dom.NodeList;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author dungvv8
 */
public class Test {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            // TODO code application logic here
//            System.out.println(PassTranformer.encrypt("789789aA@"));
//            long countLogClient = 11;
//            long countLogServer = 60;
//            double rate = Math.round(100d * 100 * (countLogServer - countLogClient) / countLogServer) / 100d;
//            System.out.println(rate);
//
//            Calendar cal = Calendar.getInstance();
//            cal.setTime(new Date());
//            cal.set(Calendar.HOUR_OF_DAY, - 1);
//            cal.set(Calendar.MINUTE, 0);
//            cal.set(Calendar.SECOND, 0);
//            cal.set(Calendar.MILLISECOND, 0);
//            System.out.println(cal.getTime());

//            Date time = new Date();
//            String updateTime = DateTimeUtils.convertDateTimeToString(time, "dd/MM/yyyy");
//            SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
//            Date dateTime = df.parse(updateTime);
//            Calendar cal = Calendar.getInstance();
//            cal.setTime(dateTime);
//            int week = cal.get(Calendar.WEEK_OF_YEAR);
//            int year = cal.get(Calendar.YEAR);
//            String tuan = + week + "-" + year;
//            System.out.println(tuan);
//            Date timeWeek = new Date() ;
//            Calendar c = Calendar.getInstance();
//            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
//            c.setTime(timeWeek);
//            c.add(Calendar.DATE, -1);
//            timeWeek.setTime(c.getTime().getTime());
//            String updateTime = DateTimeUtils.convertDateTimeToString(timeWeek, "ddMMyyyy");
//            System.out.println(updateTime);
//            Date timeWeek = new Date();
//            String updateTime = DateTimeUtils.convertDateTimeToString(timeWeek, "dd/MM/yyyy");
//            SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
//            Date dateTime = df.parse(updateTime);
//            Calendar cal = Calendar.getInstance();
//            cal.setTime(dateTime);
//            int week = cal.get(Calendar.WEEK_OF_YEAR);
//            int year = cal.get(Calendar.YEAR);
//            String tuan = +week + "_" + year;
//            System.out.println(tuan);
//            
//            String startDateString = "06/27/2007";
//            DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh24:mi:ss");
//            Date startDate;
//            String occuredTime = "2017-05-04 14:47:55.0";
//            String hour = occuredTime.substring(0, 19);
//            try {
//                startDate = df.parse(hour);
////                String newDateString = df.format(startDate);
////                System.out.println(newDateString);
//                System.out.println(hour);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            List<String> names = new ArrayList<>();
//            names.add("Phuc");
//            names.add("Loc");
//            names.add("Tho");
//            Object[] nameArr = names.toArray();
//            for (Object name : nameArr) {
//                System.out.println("name = " + name.toString());
//            }
          List <Long> lst= new ArrayList<>();
          lst.add(0L);
          if(lst.get(0)== null && lst.get(0)== 0 ){
              System.out.println("name = " + lst.get(0));
          }
          
        } catch (Exception ex) {
            Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
