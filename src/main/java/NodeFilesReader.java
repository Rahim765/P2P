import java.util.ArrayList;

public class NodeFilesReader {
    private ArrayList<NodeFiles> node_files;

    public NodeFilesReader(ArrayList<NodeFiles> node_files) {
        this.node_files = node_files;
    }

    public NodeFilesReader() {
    }

    public ArrayList<NodeFiles> getNode_files() {
        return node_files;
    }

    public void setNode_files(ArrayList<NodeFiles> node_files) {
        this.node_files = node_files;
    }

    @Override
    public String toString() {
        return "NodeFilesReader{" +
                "node_files=" + node_files +
                '}';
    }
}
