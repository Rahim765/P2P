public class FriendNode {
    private int  node_name ;
    private int node_port;

    public FriendNode(int node_name, int node_port) {
        this.node_name = node_name;
        this.node_port = node_port;
    }

    public FriendNode() {
    }

    public int getNode_name() {
        return node_name;
    }

    public void setNode_name(int node_name) {
        this.node_name = node_name;
    }

    public int getNode_port() {
        return node_port;
    }

    public void setNode_port(int node_port) {
        this.node_port = node_port;
    }

    @Override
    public String toString() {
        return "FriendNode{" +
                "node_name=" + node_name +
                ", node_port=" + node_port +
                '}';
    }
}
