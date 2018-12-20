package SeleniumFrameWork.prorail.nl;

import java.util.ArrayList;

import SeleniumFrameWork.prorail.nl.config.*;

public class TestCase {
	
	String strNumber;
	String strWeight = "5"; //default value
	Boolean failed = false;
	ArrayList<TestField> alFields;
	public Configuration conConfig;
	
	public TestCase(String strNumber, String strWeight) {
		conConfig = Configuration.getConfigurationInstance();
		
		this.strNumber = strNumber;
		this.strWeight = strWeight;
		alFields = new ArrayList<TestField>();
	}
	
	/**
	 * @return the strNumber
	 */
	public String getNumber() {
		return strNumber;
	}

	/**
	 * @param strNumber the strNumber to set
	 */
	public void setNumber(String strNumber) {
		this.strNumber = strNumber;
	}

	/**
	 * @return the strWeight
	 */
	public String getWeight() {
		return strWeight;
	}

	/**
	 * @return the strWeight
	 */
	public int getWeightInt() {
		//if no value set, use lowest possible value
		if (strWeight.equals("") ) {
			strWeight = conConfig.getProperty("LowestPriority");
		}
		
		return Integer.parseInt(strWeight);
	}
	
	/**
	 * @param strWeight the strWeight to set
	 */
	public void setWeight(String strWeight) {
		this.strWeight = strWeight;
	}
	
	public Boolean getFailed(){
		return failed;
	}
	
	public void setFailed()
	{
		this.failed = true;
	}

	/**
	 * @return the alFields
	 */
	public ArrayList<TestField> getAlFields() {
		return alFields;
	}

	/**
	 * @param alFields the alFields to set
	 */
	public void setAlFields(ArrayList<TestField> alFields) {
		this.alFields = alFields;
	}

	/***
	 * Adds a field to the testcase.
	 * 
	 * @param tfTemp
	 * @return
	 */
	public int addTestField(TestField tfField) {
	  int intError = 0;
	  
	  alFields.add(tfField);
	  
	  return intError;
	  
  }


}
