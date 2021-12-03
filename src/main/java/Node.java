import java.awt.image.AreaAveragingScaleFilter;
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

    static class MyHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange t) throws IOException {
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
