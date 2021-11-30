import java.util.ArrayList;

public class NodeFiles {
    private int node_name;
    private ArrayList<String> node_files;

    public NodeFiles(int node_name, ArrayList<String> node_files) {
        this.node_name = node_name;
        this.node_files = node_files;
    }

    public NodeFiles() {
    }

    public int getNode_name() {
        return node_name;
    }

    public void setNode_name(int node_name) {
        this.node_name = node_name;
    }

    public ArrayList<String> getNode_files() {
        return node_files;
    }

    public void setNode_files(ArrayList<String> node_files) {
        this.node_files = node_files;
    }

    @Override
    public String toString() {
        return "NodeFiles{" +
                "node_name=" + node_name +
                ", node_files=" + node_files +
                '}';
    }
}
