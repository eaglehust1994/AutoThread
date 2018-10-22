
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.codec.binary.Base64;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author dungvv8
 */
public class deleteLogCluster {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        URL url;

        try {
            // get URL content
            Base64 base64 = new Base64();
            String encoding = base64.encodeAsString("cluster:cluster".getBytes());

//            String server = "http://10.60.101.150:9494";
            String server = "http://10.60.7.241:9696";
            String jobUrl = server + "/kettle/status/";
            System.out.println("Job Url: " + jobUrl);
            url = new URL(jobUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            //Authentication
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("Authorization", "Basic " + encoding);

            // open the stream and put it into BufferedReader
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String inputLine;
            while ((inputLine = br.readLine()) != null) {
                if (inputLine.contains("href")) {
                    System.out.println("--Contain href");
                    //"kettle/jobStatus?name=Job_Acc_Used_Dl%7C20160701083030&id=7e2ada9d-a49c-4d34-b0a8-2a1808740cd7"
                    Pattern p = Pattern.compile("\\\"(.*?)\\\"");
                    Matcher m = p.matcher(inputLine);
                    while (m.find()) {
                        String href = m.group(1);
                        if (href.contains("removeJob")) {
                            String urlStr = URLDecoder.decode(server + href);
                            System.out.println("----Url remove: " + urlStr);
//                            lst.add(urlStr);
                            URL url1 = new URL(urlStr);
                            HttpURLConnection conn1 = (HttpURLConnection) url1.openConnection();
                            //Authentication
                            conn1.setRequestMethod("GET");
                            conn1.setDoOutput(true);
                            conn1.setRequestProperty("Authorization", "Basic " + encoding);
                            //removeJob 
                            conn1.getInputStream();
                            System.out.println("------>Remove done");
                        }
                    }
                } else {
                    System.out.println("--Do not contain href");
                }
            }
            br.close();

            System.out.println("Done");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
