package SeleniumFrameWork.prorail.nl;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class WaitTimeForResponse {

	private int waitTime;
	private int sleepTime;

	public WaitTimeForResponse() {

		Properties prop = new Properties();
		InputStream input = null;
		try {

			input = new FileInputStream("wait_config.properties");
			prop.load(input);
			this.waitTime = Integer.parseInt(prop.getProperty("waitTime"));
			this.sleepTime = Integer.parseInt(prop.getProperty("sleepTime"));

		} catch (IOException io) {
			System.out.println("Property file not present or error loading propperty file");
			io.printStackTrace();

		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}

	}

	public int getWaitTime() {
		return waitTime;
	}

	public void setWaitTime(int waitTime) {
		this.waitTime = waitTime;
	}

	public int getSleepTime() {
		return sleepTime;
	}

	public void setSleepTime(int sleepTime) {
		this.sleepTime = sleepTime;
	}

}
