import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
//import com.sun.tools.jdeprscan.scan.Scan;
import com.sun.net.httpserver.HttpServer;
import org.yaml.snakeyaml.Yaml;

import javax.sound.sampled.Port;
import java.applet.AppletContext;
import java.io.*;
import java.lang.reflect.Constructor;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        File file = new File("Config.yml");

        ObjectMapper om = new ObjectMapper(new YAMLFactory());

        Node node = om.readValue(file, Node.class);


        File file2 = new File("NodeFiles.yml");

        ObjectMapper om2 = new ObjectMapper(new YAMLFactory());

        NodeFilesReader node2 = om2.readValue(file2, NodeFilesReader.class);

        HttpServer server = HttpServer.create(new InetSocketAddress(node.getNode_port()), 0);
        server.createContext("/"+node.getNode_number(), new Node.MyHandler());
        server.setExecutor(null); // creates a default executor
        server.start();


        Scanner sc = new Scanner(System.in);
        String req = sc.nextLine();

        req = req.split(" ")[1];
        req = req.substring(1, req.length() - 1);
        int nodeNum = 0;

        ArrayList<NodeFiles> nf = node2.getNode_files();
        outerLoop:
        for (int i = 0; i < nf.size(); i++) {
            for (int j = 0; j < nf.get(i).getNode_files().size(); j++) {
                if (nf.get(i).getNode_files().get(j).equals(req)) {
                    nodeNum = nf.get(i).getNode_name();
                    break outerLoop;
                }
            }

        }

        int port = 0;
        for (int i = 0; i < node.getFriend_nodes().size(); i++) {
            if (nodeNum == node.getFriend_nodes().get(i).getNode_name())
                port = node.getFriend_nodes().get(i).getNode_port();
        }


        String link = "http://localhost:" + port + "/" + nodeNum;
        try {
            String urlParameters = "message=" + req;
            URL url = new URL(link);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setInstanceFollowRedirects(false);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
            connection.setRequestProperty("charset", "utf-8");
            connection.setRequestProperty("Content-Length","" + Integer.toString(urlParameters.getBytes().length));
            System.out.println(connection.getRequestProperty("Content-Length"));
            connection.setUseCaches(false);

            DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
            wr.writeBytes(urlParameters);



//            BufferedReader br = null;
//            if (100 <= conn.getResponseCode() && conn.getResponseCode() <= 399) {
//                br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
//            } else {
//                br = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
//            }
//
            int code = connection.getResponseCode();
            System.out.println(code);
            wr.flush();
            wr.close();
            connection.disconnect();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


        Runtime rt = Runtime.getRuntime();
        String url = link;
        rt.exec("rundll32 url.dll,FileProtocolHandler " + url);

    }
}
