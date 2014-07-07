package therap.com;

/**
 * Created with IntelliJ IDEA.
 * User: Adib
 * Date: 4/22/14
 * Time: 5:39 PM
 * To change this template use File | Settings | File Templates.
 */
    import org.openqa.selenium.firefox.FirefoxDriver;
    import org.openqa.selenium.support.ui.ExpectedConditions;
    import org.openqa.selenium.support.ui.WebDriverWait;
    import org.openqa.selenium.NoSuchElementException;
    import org.openqa.selenium.TimeoutException;
    import org.openqa.selenium.WebDriver;
    import org.openqa.selenium.WebElement;
    import org.openqa.selenium.By;
    import org.junit.*;
    import org.junit.runner.RunWith;
    import org.junit.runners.Parameterized.Parameters;
    import org.junit.runners.Parameterized;
    import java.io.BufferedReader;
    import java.io.FileReader;
    import java.io.IOException;
    import java.util.ArrayList;
    import java.util.Collection;
    import java.util.List;

    @RunWith(value = Parameterized.class)
    public class ReadCSV {

        public String LoginName;
        public String pasword;
        public String code;

        @Parameters
        public static Collection testData() throws IOException {
            return getTestData("E:\\automation\\Therap_selenium\\src\\therap\\com\\test.csv");
        }

        public ReadCSV(String LoginName,
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

