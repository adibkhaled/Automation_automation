package therap.icd9;

/**
 * Created with IntelliJ IDEA.
 * User: Adib
 * Date: 5/8/14
 * Time: 5:36 PM
 * To change this template use File | Settings | File Templates.
 */
/**
 * Created with IntelliJ IDEA.
 * User: Adib
 * Date: 4/22/14
 * Time: 5:39 PM
 * To change this template use File | Settings | File Templates.
 */
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@RunWith(value = Parameterized.class)
public class ReadCSV {

    public String ICD9Code;

    @Parameters
    public static Collection testData() throws IOException {
        return getTestData("E:\\automation\\Therap_selenium\\src\\therap\\icd9\\ICD-9_code.csv");
    }

    public ReadCSV(String ICD9Code)
                   {
        this.ICD9Code = ICD9Code;

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