package ru.lifanova.convert;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import ru.lifanova.domain.Employee;
import ru.lifanova.exception.XmlParseException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ConvertUtils {
    private static final String DATA_DIR_PATH = System.getProperty("user.dir") + "/src/main/resources/data/";

    public static List<Employee> parseXML(String fileName) throws XmlParseException {
        List<Employee> resultList = null;

        if (fileName == null || fileName.isEmpty()) {
            System.out.println("[parseXML]: Error: empty file name!");
            return null;
        }

        String filePath = DATA_DIR_PATH + fileName;
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new File(filePath));
            resultList = processAll(doc);
        } catch (SAXException | ParserConfigurationException | IOException e) {
            throw new XmlParseException(e.getMessage());
        }

        return resultList;
    }

    private static List<Employee> processAll(Document document) {
        List<Employee> resultList = new ArrayList<>();
        Node root = document.getDocumentElement();
        System.out.println("Root " + root.getNodeName());
        NodeList nodeList = root.getChildNodes();

        System.out.println(nodeList.getLength());

        for (int i = 0; i < nodeList.getLength(); i++) {
            //employee
            Node employeeNode = nodeList.item(i);
            //1
            if (Node.ELEMENT_NODE == employeeNode.getNodeType()) {
                System.out.println("Node -> " + employeeNode.getNodeType() + ", " + employeeNode.getNodeName());

                resultList.add(processEmployeeNode(employeeNode));
            }
        }

        return resultList;
    }

    private static Employee processEmployeeNode(Node employeeNode) {
        Employee employee = new Employee();

        NodeList fieldList = employeeNode.getChildNodes();
        for (int j = 0; j < fieldList.getLength(); j++) {
            Node child = fieldList.item(j);

            if (Node.ELEMENT_NODE != child.getNodeType()) {
                continue;
            }

            System.out.println("Child -> " + child.getNodeType() + ", " + child.getNodeName());
            if (child.getNodeName().equals("id")) {
                employee.setId(Long.parseLong(child.getTextContent()));
            }

            if (child.getNodeName().equals("firstName")) {
                employee.setFirstName(child.getTextContent());
            }

            if (child.getNodeName().equals("lastName")) {
                employee.setLastName(child.getTextContent());
            }

            if (child.getNodeName().equals("country")) {
                employee.setCountry(child.getTextContent());
            }

            if (child.getNodeName().equals("age")) {
                employee.setAge(Integer.parseInt(child.getTextContent()));
            }
        }

        return employee;
    }

    public static String listToJson(List<Employee> list) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        StringBuilder stringBuilder = new StringBuilder();

        for (Employee employee : list) {
            stringBuilder.append(gson.toJson(employee)).append("\n");
        }

        return stringBuilder.toString();
    }

    public static void writeToFile(String filename, String json) {
        String path = DATA_DIR_PATH + filename;
        File file = new File(path);
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
            bw.write(json);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
