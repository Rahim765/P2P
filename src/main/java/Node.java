import java.awt.image.AreaAveragingScaleFilter;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class Node {
    private int node_number;
    private int node_port;
    private String owned_files_dir ;
    private String new_files_dir;
    private ArrayList<String> owned_files;
    private ArrayList<FriendNode> friend_nodes ;

    public Node(int node_number, int node_port, String owned_files_dir, String new_files_dir,
                ArrayList<String> owned_files, ArrayList<FriendNode> friend_nodes) {
        this.node_number = node_number;
        this.node_port = node_port;
        this.owned_files_dir = owned_files_dir;
        this.new_files_dir = new_files_dir;
        this.owned_files = owned_files;
        this.friend_nodes = friend_nodes;




    }


    class Temp {

        public void sendHttpGETRequest() throws IOException {
            String USER_AGENT = "Mozilla/5.0";
            String GET_URL = "http://localhost:"+Node.this.node_port+"/"+Node.this.node_number;

            URL obj = new URL(GET_URL);
            HttpURLConnection httpURLConnection = (HttpURLConnection) obj.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setRequestProperty("User-Agent", USER_AGENT);
            int responseCode = httpURLConnection.getResponseCode();
            System.out.println("GET Response Code :: " + responseCode);
            if (responseCode == HttpURLConnection.HTTP_OK) { // success
                BufferedReader in = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in .readLine()) != null) {
                    response.append(inputLine);
                } in .close();

                // print result
                System.out.println(response.toString());
            } else {
                System.out.println("GET request not worked");
            }

            for (int i = 1; i <= 8; i++) {
                System.out.println(httpURLConnection.getHeaderFieldKey(i) + " = " + httpURLConnection.getHeaderField(i));
            }

        }

    }
    static class MyHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange t) throws IOException {

                Temp temp = new Temp();
                String response = "This is the response";
                t.sendResponseHeaders(200, response.length());
                OutputStream os = t.getResponseBody();
                os.write(response.getBytes());
                os.close();

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
