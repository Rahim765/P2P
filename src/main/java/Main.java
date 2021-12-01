import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {

        //sz is testing some shit
     //   ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

        File file = new File("Config.yml");

        ObjectMapper om = new ObjectMapper(new YAMLFactory());

        Node node = om.readValue(file, Node.class);

        System.out.println("node info " + node.toString());




        File file2 = new File("NodeFiles.yml");

        ObjectMapper om2 = new ObjectMapper(new YAMLFactory());

        NodeFilesReader node2 = om2.readValue(file2, NodeFilesReader.class);

        System.out.println("node info " + node2.toString());







    }
}
