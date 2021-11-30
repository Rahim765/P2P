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

        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        File file = new File("Config.yml");

        ObjectMapper om = new ObjectMapper(new YAMLFactory());

        Node node = om.readValue(file, Node.class);

        System.out.println("node info " + node.toString());
        
    }
}
