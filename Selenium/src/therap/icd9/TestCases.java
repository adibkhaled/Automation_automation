package therap.icd9;

/**
 * Created with IntelliJ IDEA.
 * User: Adib
 * Date: 5/11/14
 * Time: 2:47 PM
 * To change this template use File | Settings | File Templates.
 */
public class TestCases {
    //test case Id string
    public String testCaseId;
    //test case name string
    public String testCaseName;
    //test result status string
    public String TestCaseResultStatus;
    //construct a test case object
    public TestCases(String testcaseid, String testcasename, String testresultstatus)
    {
        this.setTestCaseId(testcaseid);
        this.setTestCaseName(testcasename);
        this.setTestCaseResultStatus(testresultstatus);
    }
    /**
     * @return the testCaseResultStatus
     */
    public String getTestCaseResultStatus() {
        return TestCaseResultStatus;
    }

    /**
     * @param testCaseResultStatus the testCaseResultStatus to set
     */
    public void setTestCaseResultStatus(String testCaseResultStatus) {
        TestCaseResultStatus = testCaseResultStatus;
    }

    /**
     * @return the testCaseName
     */
    public String getTestCaseName() {
        return testCaseName;
    }

    /**
     * @param testCaseName the testCaseName to set
     */
    public void setTestCaseName(String testCaseName) {
        this.testCaseName = testCaseName;
    }

    /**
     * @return the testCaseId
     */
    public String getTestCaseId() {
        return testCaseId;
    }

    /**
     * @param testCaseId the testCaseId to set
     */
    public void setTestCaseId(String testCaseId) {
        this.testCaseId = testCaseId;
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub

    }

}
