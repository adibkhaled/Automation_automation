package SeleniumFrameWork.prorail.nl;

import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;


public class Apl {

	static Logger log = Logger.getLogger("DebugLogger");
	static Logger testResultsLog = Logger.getLogger("TestResultsLogger");
	
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		testResultsLog.info("Starting automated test!");
		
    String strText = "Automated test stopped";
    long lngStartTime;
    long lngStopTime;
    long lngDuration;
    long lngMinutes;

    lngStartTime = System.currentTimeMillis();
    
    //This is where the action is
    FrameWork fm = new FrameWork();
    fm.start();
    
    //show duration
    lngStopTime = System.currentTimeMillis();
    lngDuration = lngStopTime - lngStartTime;
    lngMinutes = TimeUnit.MILLISECONDS.toMinutes(lngDuration);

    testResultsLog.info(strText + " after " + lngMinutes + " minutes!");
    testResultsLog.info("Total testCases: " + fm.testCasesTotalNumber);
    //Close the HTML Report.
    fm.extent.close(); 

	}

}
