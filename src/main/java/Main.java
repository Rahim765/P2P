import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
//import com.sun.tools.jdeprscan.scan.Scan;
import com.sun.net.httpserver.HttpServer;
import org.yaml.snakeyaml.Yaml;

import java.applet.AppletContext;
import java.io.*;
import java.lang.reflect.Constructor;
import java.net.InetSocketAddress;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        //sz is testing some shit
        //ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

        File file = new File("Config.yml");

        ObjectMapper om = new ObjectMapper(new YAMLFactory());

        Node node = om.readValue(file, Node.class);

        //System.out.println("node info " + node.toString());


        HttpServer server = HttpServer.create(new InetSocketAddress(node.getNode_port()), 0);
        server.createContext("/"+node.getNode_number(), new Node.MyHandler());
        server.setExecutor(null); // creates a default executor
        server.start();


        File file2 = new File("NodeFiles.yml");

        ObjectMapper om2 = new ObjectMapper(new YAMLFactory());

        NodeFilesReader node2 = om2.readValue(file2, NodeFilesReader.class);

        //System.out.println("node info " + node2.toString());

        Scanner sc = new Scanner(System.in);
        String req = sc.nextLine();

        req = req.split(" ")[1];
        req = req.substring(1,req.length()-1);
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
//        System.out.println(port);
//        System.out.println(nodeNum);


          String link = "http://localhost:"+port+"/"+nodeNum;
          Runtime rt = Runtime.getRuntime();
          String url = link;
          rt.exec("rundll32 url.dll,FileProtocolHandler " + url);
    }
}
