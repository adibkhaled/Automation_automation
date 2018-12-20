package SeleniumFrameWork.prorail.nl;

public class TestField {

	String strFieldName;
	String strValue;
	String strScreen;
	String strType;
	String strID;
	String strXPath;
	String strText;
	String strInfo;
	String strName;
	
	public TestField(String strFieldName, String strValue) {
		this.strFieldName = strFieldName;
		this.strValue = strValue;
	}

  /***
   * Constructor
   * 
   * @param strScreen
   * @param strField
   * @param strType
   * @param strID
   * @param strInfo
   */
  public TestField(String strScreen, String strField, String strType, String strID, String strName, String strXPath, String strText, String strInfo) {

    this.strScreen = strScreen;
    this.strFieldName = strField;
    this.strType = strType;
    this.strID = strID;
    this.strName = strName;
    this.strXPath = strXPath;
    this.strText = strText;
    this.strInfo = strInfo;
  }
	
	/**
	 * @return the strFieldName
	 */
	public String getFieldName() {
		return strFieldName;
	}

	/**
	 * @param strFieldName the strFieldName to set
	 */
	public void setFieldName(String strFieldName) {
		this.strFieldName = strFieldName;
	}

	/**
	 * @return the strValue
	 */
	public String getValue() {
		return strValue;
	}

	/**
	 * @param strValue the strValue to set
	 */
	public void setValue(String strValue) {
		this.strValue = strValue;
	}

	/**
	 * @return the strScreen
	 */
	public String getScreen() {
		return strScreen;
	}

	/**
	 * @param strScreen the strScreen to set
	 */
	public void setScreen(String strScreen) {
		this.strScreen = strScreen;
	}

	/**
	 * @return the strType
	 */
	public String getType() {
		return strType;
	}

	/**
	 * @param strType the strType to set
	 */
	public void setType(String strType) {
		this.strType = strType;
	}

	/**
	 * @return the strID
	 */
	public String getID() {
		return strID;
	}

	/**
	 * @param strID the strID to set
	 */
	public void setID(String strID) {
		this.strID = strID;
	}

	/**
	 * @return the strXpath
	 */
	public String getXPath() {
		return strXPath;
	}

	/**
	 * @param strXpath the strXpath to set
	 */
	public void setXpath(String strXPath) {
		this.strXPath = strXPath;
	}

	/**
	 * @return the strText
	 */
	public String getText() {
		return strText;
	}

	/**
	 * @param strText the strText to set
	 */
	public void setText(String strText) {
		this.strText = strText;
	}

	/**
	 * @return the strInfo
	 */
	public String getInfo() {
		return strInfo;
	}

	/**
	 * @param strInfo the strInfo to set
	 */
	public void setInfo(String strInfo) {
		this.strInfo = strInfo;
	}
	
	/**
	 * @return the strName
	 */
	public String getName() {
		return strName;
	}

	/**
	 * @param strName the strName to set
	 */
	public void setName(String strName) {
		this.strName = strName;
	}
	
}
