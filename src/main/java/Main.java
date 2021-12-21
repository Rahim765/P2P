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
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter the config file name : ");
        String conf = sc.nextLine();


        File file = new File(conf);
        ObjectMapper om = new ObjectMapper(new YAMLFactory());
        Node node = om.readValue(file, Node.class);

        System.out.println("Enter the nodefiles file name : ");
        String nodefile = sc.nextLine();

        File file2 = new File(nodefile);
        ObjectMapper om2 = new ObjectMapper(new YAMLFactory());
        NodeFilesReader node2 = om2.readValue(file2, NodeFilesReader.class);


        HttpServer server = HttpServer.create(new InetSocketAddress(node.getNode_port()), 0);
        server.createContext("/" + node.getNode_number(), new Node.MyHandler());
        server.setExecutor(null);
        server.start();


        System.out.println("Enter the file name");
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

        if (port != 0) {

            System.out.println("We have the adress of node that contain the file : "+ req );
            System.out.println("sending request to node wiht port "+ port +".....");
            URL url = new URL("http://localhost:" + port + "/" + nodeNum);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            Map<String, String> parameters = new HashMap<>();
            parameters.put("FileName", req);

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
            String content = "";
            while ((inputLine = in.readLine()) != null) {
                content += inputLine;
            }
            System.out.println("the new file adress is : "+ content);
            in.close();
            String[] sp = content.split("/");
            content = sp[2];
            node.getNew_files().add(content);
            System.out.println("the new file ( "+ content +" ) was added to new directories ");
            ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());
            objectMapper.writeValue(new File("test.yml"), node);

        }else{
            System.out.println(node.getNode_number() +" deos not have the port of the node that contain the requested file ");
            int min =100;
            int number =0;
            int portNumber=0;
            for (int i = 0; i <node.getFriend_nodes().size() ; i++) {
                if (Math.abs(node.getNode_number()-node.getFriend_nodes().get(i).getNode_name()) < min){
                    min = Math.abs(node.getNode_number()-node.getFriend_nodes().get(i).getNode_name());
                    number = node.getFriend_nodes().get(i).getNode_name();
                    portNumber = node.getFriend_nodes().get(i).getNode_port();
                }
            }

            System.out.println("the nearest node to this node is the node with port "+ portNumber );
            System.out.println("sending request to node with port : "+ portNumber);
            URL url = new URL("http://localhost:" + portNumber + "/" + number);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            Map<String, String> parameters = new HashMap<>();
            parameters.put(node.getNode_number()+"NodeName", String.valueOf(nodeNum));

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
            String content = "";
            while ((inputLine = in.readLine()) != null) {
                content += inputLine;
            }

            System.out.println("the port number of the node that contains the request file is : " + content);
            System.out.println("sending file request to node with port number "+ content+" ...");

            URL url2 = new URL("http://localhost:" + content + "/" + nodeNum);
            HttpURLConnection con2 = (HttpURLConnection) url2.openConnection();
            con2.setRequestMethod("GET");
            Map<String, String> parameters2 = new HashMap<>();
            parameters2.put("FileName", req);

            con2.setDoOutput(true);
            DataOutputStream out2 = new DataOutputStream(con2.getOutputStream());
            out2.writeBytes(ParameterStringBuilder.getParamsString(parameters2));
            out2.flush();
            out2.close();

            con2.setInstanceFollowRedirects(false);
            con2.setFollowRedirects(false);

            BufferedReader in2 = new BufferedReader(
                    new InputStreamReader(con2.getInputStream()));
            String inputLine2;
            String content2 = "";
            while ((inputLine2 = in2.readLine()) != null) {
                content2 += inputLine2;
            }
            System.out.println("the requested file directori : "+content2);
            in2.close();
            String[] sp2 = content2.split("/");
            content2 = sp2[2];
            node.getNew_files().add(content2);
            System.out.println("the requested file was added to new files firectory ");
            ObjectMapper objectMapper2 = new ObjectMapper(new YAMLFactory());
            objectMapper2.writeValue(new File(conf), node);
            System.out.println(node.getNew_files().get(0));

        }
    }
}
