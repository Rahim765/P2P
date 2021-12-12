import java.awt.image.AreaAveragingScaleFilter;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;


public class Node {
    static private int node_number;
    static private int node_port;
    static private String owned_files_dir ;
    static private String new_files_dir;
    static private ArrayList<String> owned_files;
    static private ArrayList<FriendNode> friend_nodes ;
    static private ArrayList<String> new_files = new ArrayList<>();


    public Node(int node_number, int node_port, String owned_files_dir, String new_files_dir,
                ArrayList<String> owned_files, ArrayList<FriendNode> friend_nodes) {
        this.node_number = node_number;
        this.node_port = node_port;
        this.owned_files_dir = owned_files_dir;
        this.new_files_dir = new_files_dir;
        this.owned_files = owned_files;
        this.friend_nodes = friend_nodes;

    }


    static class MyHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange t) throws IOException {

            int illegal_port =0;
            String response = "";
            String type = "";
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(t.getRequestBody()));
            String line= "";
            while ((line = in.readLine())!= null){
                response = line.split("=")[1];
                type = line.split("=")[0];
            }



            if (type.equals("FileName")) {
                response = owned_files_dir + response;
                t.sendResponseHeaders(200, response.length());
                OutputStream os = t.getResponseBody();
                os.write(response.getBytes());
                os.close();
            }else{
                illegal_port = Integer.parseInt(type.substring(0,1));
                boolean exist = false;
                for (int i = 0; i < friend_nodes.size(); i++) {
                    if (friend_nodes.get(i).getNode_name() == Integer.parseInt(response)) {
                        response = String.valueOf(friend_nodes.get(i).getNode_port());
                        exist = true;
                        break;
                    }
                }

                if (exist) {
                    t.sendResponseHeaders(200, response.length());
                    OutputStream os = t.getResponseBody();
                    os.write(response.getBytes());
                    os.close();
                }else{

                    int min =100;
                    int number =0;
                    int portNumber=0;
                    for (int i = 0; i <friend_nodes.size() ; i++) {
                        if (Math.abs(node_number-friend_nodes.get(i).getNode_name()) < min &&
                                ( ( friend_nodes.get(i).getNode_name() != illegal_port )) ){
                            min =Math.abs(node_number-friend_nodes.get(i).getNode_name());
                            number = friend_nodes.get(i).getNode_name();
                            portNumber = friend_nodes.get(i).getNode_port();
                        }
                    }
                    URL url = new URL("http://localhost:" + portNumber + "/" + number);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    con.setRequestMethod("GET");
                    Map<String, String> parameters = new HashMap<>();
                    parameters.put(node_number+"NodeName", response);

                    con.setDoOutput(true);
                    DataOutputStream out = new DataOutputStream(con.getOutputStream());
                    out.writeBytes(ParameterStringBuilder.getParamsString(parameters));
                    out.flush();
                    out.close();

                    con.setInstanceFollowRedirects(false);
                    con.setFollowRedirects(false);

                    BufferedReader in2 = new BufferedReader(
                            new InputStreamReader(con.getInputStream()));
                    String inputLine;
                    String content = "";
                    while ((inputLine = in2.readLine()) != null) {
                        content += inputLine;
                    }

                    response = content;
                    t.sendResponseHeaders(200, response.length());
                    OutputStream os = t.getResponseBody();
                    os.write(response.getBytes());
                    os.close();
                }
            }

        }
    }

    public Node() {

    }

    public int getNode_number() {
        return node_number;
    }

    public void setNode_number(int node_number) {
        this.node_number = node_number;
    }

    public int getNode_port() {
        return node_port;
    }

    public void setNode_port(int node_port) {
        this.node_port = node_port;
    }

    public String getOwned_files_dir() {
        return owned_files_dir;
    }

    public void setOwned_files_dir(String owned_files_dir) {
        this.owned_files_dir = owned_files_dir;
    }

    public String getNew_files_dir() {
        return new_files_dir;
    }

    public void setNew_files_dir(String new_files_dir) {
        this.new_files_dir = new_files_dir;
    }

    public ArrayList<String> getOwned_files() {
        return owned_files;
    }

    public void setOwned_files(ArrayList<String> owned_files) {
        this.owned_files = owned_files;
    }

    public ArrayList<FriendNode> getFriend_nodes() {
        return friend_nodes;
    }

    public void setFriend_nodes(ArrayList<FriendNode> friend_nodes) {
        this.friend_nodes = friend_nodes;
    }

    public  ArrayList<String> getNew_files() {
        return new_files;
    }

    public  void setNew_files(ArrayList<String> new_files) {
        Node.new_files = new_files;
    }

    @Override
    public String toString() {
        return "Node{" +
                "node_number=" + node_number +
                ", node_port=" + node_port +
                ", owned_files_dir='" + owned_files_dir + '\'' +
                ", new_files_dir='" + new_files_dir + '\'' +
                ", owned_files=" + owned_files +
                ", friend_nodes=" + friend_nodes +
                '}';
    }
}
