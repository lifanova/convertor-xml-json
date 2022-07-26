import ru.lifanova.convert.ConvertUtils;
import ru.lifanova.domain.Employee;
import ru.lifanova.exception.XmlParseException;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        String xmlFileName = "data.xml";
        String jsonFileName = "data.json";

        List<Employee> employeeList = null;
        try {
            employeeList = ConvertUtils.parseXML(xmlFileName);
        } catch (XmlParseException e) {
            System.out.println("[Main]: Error: Ошибка конвертации " + e.getLocalizedMessage());
            return;
        }

        String json = ConvertUtils.listToJson(employeeList);
        ConvertUtils.writeToFile(jsonFileName, json);
    }
}
