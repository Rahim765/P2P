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
import java.util.HashMap;
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


        URL url = new URL("http://localhost:"+port+"/"+nodeNum);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        Map<String, String> parameters = new HashMap<>();
        parameters.put("param1", "val");

        con.setDoOutput(true);
        DataOutputStream out = new DataOutputStream(con.getOutputStream());
        out.writeBytes(ParameterStringBuilder.getParamsString(parameters));
        out.flush();
        out.close();

        con.setInstanceFollowRedirects(false);
        con.setFollowRedirects(false);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        String content ="";
        String final_string = "";
        while ((inputLine = in.readLine()) != null) {
            content+=inputLine;
        }
        String[] filedic = content.split(" ");
        for (int i = 0; i <filedic.length ; i++) {
            String[] mainfile = filedic[i].split("/");
            if (mainfile[2].equals(req)){
                final_string = mainfile[2];
                inputLine = filedic[i];
                break;
            }
        }
        System.out.println(inputLine);
        in.close();

       node.setNew_files_dir(node.getNew_files_dir()+final_string+" ");
        System.out.println(node.getNew_files_dir());

//        String link = "http://localhost:" + port + "/" + nodeNum;
//        Runtime rt = Runtime.getRuntime();
//        String url = link;
//        rt.exec("rundll32 url.dll,FileProtocolHandler " + url);

    }
}
