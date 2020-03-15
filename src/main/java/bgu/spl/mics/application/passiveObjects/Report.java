package bgu.spl.mics.application.passiveObjects;

import java.util.LinkedList;
import java.util.List;

/**
 * Passive data-object representing a delivery vehicle of the store.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You may add ONLY private fields and methods to this class.
 */
public class Report {
	private String missionName;
	private String m;//serial number of M.
	private String moneyPenny;//serial number of moneypenny
	private List<String> agentsSerialNumbers;
	private String gadgetName;
	private List<String> agentNames;
	private int timeCreated;//the timeTick when the repots has been created.
	private int timeIssued;//the timeTick the mission was sent by the intelligence.
	private int qTime;//the timeTick Q recived the gadgetAvailableEvent for that mission.


	public Report(MissionInfo mission,List<String> agentsNames,String mPId,String mId,int Qtime,int timeTick) {
		missionName = mission.getMissionName();
		gadgetName = mission.getGadget();
		m = mId;
		moneyPenny = mPId;
		agentsSerialNumbers = mission.getSerialAgentsNumbers();
		agentNames=new LinkedList<>();
		for(String name: agentsNames)
			agentNames.add(name);
		timeIssued = mission.getTimeIssued();
		qTime = Qtime;
		timeCreated = timeTick;
	}


	/**
     * Retrieves the mission name.
     */
	public String getMissionName() {
		return missionName;
	}

	/**
	 * Sets the mission name.
	 */
	public void setMissionName(String missionName) {
		missionName=missionName;
	}

	/**
	 * Retrieves the M's id.
	 */
	public String getM() {
		return m;
	}

	/**
	 * Sets the M's id.
	 */
	public void setM(String m) {
		m=m;
	}

	/**
	 * Retrieves the Moneypenny's id.
	 */
	public String getMoneypenny() {
		return moneyPenny;
	}

	/**
	 * Sets the Moneypenny's id.
	 */
	public void setMoneypenny(String moneypenny) {
			moneypenny=moneyPenny;
	}

	/**
	 * Retrieves the serial numbers of the agents.
	 * <p>
	 * @return The serial numbers of the agents.
	 */
	public List<String> getAgentsSerialNumbers() {
		return agentsSerialNumbers;
	}

	/**
	 * Sets the serial numbers of the agents.
	 */
	public void setAgentsSerialNumbers(List<String> agentsSerialNumbersNumber) {
		agentsSerialNumbers=agentsSerialNumbersNumber;
	}

	/**
	 * Retrieves the agents names.
	 * <p>
	 * @return The agents names.
	 */
	public List<String> getAgentsNames() {
		return agentNames;
	}

	/**
	 * Sets the agents names.
	 */
	public void setAgentsNames(List<String> agentsNames) {
		agentsNames=agentNames;
	}

	/**
	 * Retrieves the name of the gadget.
	 * <p>
	 * @return the name of the gadget.
	 */
	public String getGadgetName() {
		return gadgetName;
	}

	/**
	 * Sets the name of the gadget.
	 */
	public void setGadgetName(String gadgetName) {
		gadgetName=gadgetName;
	}

	/**
	 * Retrieves the time-tick in which Q Received the GadgetAvailableEvent for that mission.
	 */
	public int getQTime() {
		return qTime;
	}

	/**
	 * Sets the time-tick in which Q Received the GadgetAvailableEvent for that mission.
	 */
	public void setQTime(int qTime) {
		qTime=qTime;
	}

	/**
	 * Retrieves the time when the mission was sent by an Intelligence Publisher.
	 */
	public int getTimeIssued() {
		return timeIssued;
	}

	/**
	 * Sets the time when the mission was sent by an Intelligence Publisher.
	 */
	public void setTimeIssued(int timeIssued) {
		timeIssued=timeIssued;
	}

	/**
	 * Retrieves the time-tick when the report has been created.
	 */
	public int getTimeCreated() {
		return timeCreated;
	}

	/**
	 * Sets the time-tick when the report has been created.
	 */
	public void setTimeCreated(int timeCreated) {
		timeCreated=timeCreated;
	}
}
