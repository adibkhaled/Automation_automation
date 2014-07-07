package therap.icd9;

/**
 * Created with IntelliJ IDEA.
 * User: Adib
 * Date: 5/11/14
 * Time: 2:48 PM
 * To change this template use File | Settings | File Templates.
 */
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.util.Date;
import java.util.List;

public class TestResultXmlUtility {

    private static DocumentBuilderFactory factory;
    private static DocumentBuilder builder;
    private static Document doc;
    /**
     * @param
     * @throws ParserConfigurationException
     */
    public void WriteTestResultToXml(String filename,List<TestCases> items) throws ParserConfigurationException
    {
        Date timeStamp = new Date();
        try{
            //define an empty document
            factory = DocumentBuilderFactory.newInstance();
            builder = factory.newDocumentBuilder();
            doc = builder.newDocument();
            //root element
            Element testReportElement=doc.createElement("TestReport");
            testReportElement.setAttribute("time", timeStamp.toString());
            doc.appendChild(testReportElement);
            //Test Cases Element
            Element testCasesElement=doc.createElement("CodeNames");
            testReportElement.appendChild(testCasesElement);
            //For each test case object add a Test Case Element in the xml file
            for(TestCases a:items)
            {
                Element testCaseElement=doc.createElement("CodeName");
                testCaseElement.setAttribute("id", a.getTestCaseId());
                testCasesElement.appendChild(testCaseElement);
                // Name element
                Element name = doc.createElement("Status");
                name.appendChild(doc.createTextNode(a.getTestCaseName()));
                testCasesElement.appendChild(name);
                // Test Result  element
                Element testResult = doc.createElement("TestResult");
                testResult.appendChild(doc.createTextNode(a.getTestCaseResultStatus()));
                testCasesElement.appendChild(testResult);
            }
            // write the content into xml file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.METHOD,"xml");
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File("E:\\automation\\Therap_selenium\\src\\therap\\icd9\\TestResult.xml"));

            // Output to console for testing
            // StreamResult result = new StreamResult(System.out);

            transformer.transform(source, result);

            //System.out.println("File saved!");

        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (TransformerException tfe) {
            tfe.printStackTrace();
        }
    }
}
