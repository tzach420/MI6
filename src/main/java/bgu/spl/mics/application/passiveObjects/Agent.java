package bgu.spl.mics.application.passiveObjects;

import java.util.concurrent.Semaphore;

/**
 * Passive data-object representing a information about an agent in MI6.
 * You must not alter any of the given public methods of this class. 
 * <p>
 * You may add ONLY private fields and methods to this class.
 */
public class Agent {
	private String serialNumber;//start with 00.
	private String name;//can be more than 1 word.
	private boolean isAvailable=true;
	private Semaphore s;


	public Agent(String serialNumber,String name){
		this.serialNumber=serialNumber;
		this.name=name;
		s=new Semaphore(1,true);
	}
	/**
	 * Sets the serial number of an agent.
	 */
	public void setSerialNumber(String serialNumber) {
		this.serialNumber=serialNumber;
	}

	/**
     * Retrieves the serial number of an agent.
     * <p>
     * @return The serial number of an agent.
     */
	public String getSerialNumber() {
		return this.serialNumber;
	}

	/**
	 * Sets the name of the agent.
	 */
	public void setName(String name) {
		name=name;
	}

	/**
     * Retrieves the name of the agent.
     * <p>
     * @return the name of the agent.
     */
	public String getName() {
		return this.name;
	}

	/**
     * Retrieves if the agent is available.
     * <p>
     * @return if the agent is available.
     */
	public boolean isAvailable() {
		return isAvailable;
	}

	/**
	 * Acquires an agent.
	 */
	public void acquire(){//change available from true to false;
		try {
			s.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		isAvailable=false;
	}

	/**
	 * Releases an agent.
	 */
	public void release(){//change available from false to true;
		s.release();
		isAvailable=true;

	}
}
