
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.opencsv.CSVReader;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException, SAXException, ParserConfigurationException {
        String[] columnMapping = {"id", "firstname", "lastname", "country", "age"};
        String fileCsv = "data.csv";
        String fileJsonCsv = "data.json";
        String fileXml = "data.xml";
        String fileJsonXml = "data2.json";
        List<Employee> list = parseCSV(columnMapping, fileCsv);
        String json = listToJson(list);
        writeString(json, fileJsonCsv);
        list = parseXML(fileXml);
        json = listToJson(list);
        writeString(json, fileJsonXml);
        String jsonFromFile = readongJson("data.json");
        List<Employee> listFromJson = jsonToList(jsonFromFile);
    }

    private static List<Employee> jsonToList(String jsonFromFile) {
        Gson gson = new GsonBuilder().create();
        List<Employee> employee = gson.fromJson(
                jsonFromFile,
                new TypeToken<List<Employee>>() {
                }.getType()
        );
        employee.forEach(System.out::println);
        return employee;
    }

    private static String readongJson(String fileName) {
        try (BufferedReader bfReader = new BufferedReader(new FileReader(fileName))) {
            StringBuilder sb = new StringBuilder();
            String s;
            while ((s = bfReader.readLine()) != null) {
                sb.append(s);
            }
            //System.out.println(sb);
            return sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static List<Employee> parseXML(String fileXml) throws IOException, SAXException, ParserConfigurationException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new File(fileXml));

        NodeList root = doc.getDocumentElement().getChildNodes();
        List<Employee> listEmployee = new ArrayList<>();

        for (int i = 0; i < root.getLength(); i++) {
            if (root.item(i).getNodeType() == Node.ELEMENT_NODE) {
                Node employeeNode = root.item(i);
                Element elN = (Element) employeeNode;
                listEmployee.add(
                        new Employee(
                                Long.parseLong(elN.getElementsByTagName("id").item(0).getTextContent()),
                                elN.getElementsByTagName("firstName").item(0).getTextContent(),
                                elN.getElementsByTagName("lastName").item(0).getTextContent(),
                                elN.getElementsByTagName("country").item(0).getTextContent(),
                                Integer.parseInt(elN.getElementsByTagName("age").item(0).getTextContent())
                        )
                );
            }
        }
        return listEmployee;
    }

    private static void writeString(String json, String fileName) {
        try (FileWriter file = new FileWriter(fileName)) {
            file.write(json);
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String listToJson(List<Employee> list) {
        Type listType = new TypeToken<List<Employee>>() {
        }.getType();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(list, listType);
        //System.out.println(json);
        return json;
    }

    private static List<Employee> parseCSV(String[] columnMapping, String fileName) {
        try (CSVReader csvReader = new CSVReader(new FileReader(fileName))) {
            ColumnPositionMappingStrategy<Employee> strategy = new ColumnPositionMappingStrategy<>();
            strategy.setType(Employee.class);
            strategy.setColumnMapping(columnMapping);
            CsvToBean<Employee> csv = new CsvToBeanBuilder<Employee>(csvReader).withMappingStrategy(strategy).build();
            List<Employee> staff = csv.parse();
            // staff.forEach(System.out::println);
            return staff;
        } catch (IOException er) {
            er.printStackTrace();
            return null;
        }
    }
}
