package therap.arkances;

import org.junit.runners.Parameterized;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Adib
 * Date: 6/1/14
 * Time: 10:55 AM
 * To change this template use File | Settings | File Templates.
 */
public class ReadText {
    public String LoginName;
    public String pasword;
    public String code;

    @Parameterized.Parameters
    public static Collection testData() throws IOException {
        return getTestData("E:\\automation\\Therap_selenium\\src\\therap\\com\\test.csv");
    }

    public ReadText(String LoginName,
                   String password, String code) {
        this.LoginName = LoginName;
        this.pasword = password;
        this.code = code;

    }

    public static Collection<String[]> getTestData(String fileName)
            throws IOException {
        List<String[]> records = new ArrayList<String[]>();
        String record;
        BufferedReader file = new BufferedReader(new FileReader(fileName));
        while ((record = file.readLine()) != null) {
            String fields[] = record.split(",");
            records.add(fields);
        }
        file.close();
        return records;
    }


}

