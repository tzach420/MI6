package bgu.spl.mics.application.passiveObjects;
import java.util.*;

/**
 * Passive data-object representing a information about an agent in MI6.
 * You must not alter any of the given public methods of this class. 
 * <p>
 * You may add ONLY private fields and methods to this class.
 */
public class Squad {

	private Map<String, Agent> agents;//serial number is the key.(006,007)
	private static Squad singleInstance=null;



	private Squad(){
		agents=new HashMap<String,Agent>();
	}

	/**
	 * Retrieves the single instance of this class.
	 */
	public static Squad getInstance() {//Initialize first time in main before running the threads.
		if(singleInstance==null){
			singleInstance=new Squad();
		}
		return singleInstance;
	}

	/**
	 * Initializes the squad. This method adds all the agents to the squad.
	 * <p>
	 * @param inventory 	Data structure containing all data necessary for initialization
	 * 						of the squad.
	 */
	public void load (Agent[] inventory) {//called only 1 time in INIT phase.(in main)
		for(Agent smith: inventory){
			agents.put(smith.getSerialNumber(),smith);
		}
	}

	/**
	 * Releases agents with serial number in serials, in case mission is aborted.
	 */
	public void releaseAgents(List<String> serials){
		for(String serial: serials) {
			agents.get(serial).release();
		}
	}

	/**
	 * simulates executing a mission by calling sleep.
	 * @param time   time ticks to sleep
	 */
	public void sendAgents(List<String> serials, int time) throws InterruptedException {//EXECUTE THE MISSION.
		System.out.println("Agents "+serials.toString()+" were sent to mission.");
		Thread.sleep(time*100);
		releaseAgents(serials);
	}

	/**
	 * acquires an agent, i.e. holds the agent until the caller is done with it
	 * @param serials   the serial numbers of the agents
	 * @return ‘false’ if an agent of serialNumber ‘serial’ is missing, and ‘true’ otherwise
	 */
	public boolean  getAgents(List<String> serials) throws InterruptedException {//FIXME -think if you want to release the agents if not all available.
		for(String serial : serials)//check that all the agents exists.
			if(!agents.containsKey(serial)){
				System.out.println("agent "+serial+" is not in the squad.");
				return false;
			}
		Collections.sort(serials);
		for(String serial : serials) {
				agents.get(serial).acquire();
			}
		return true;
	}

    /**
     * gets the agents names
     * @param serials the serial numbers of the agents
     * @return a list of the names of the agents with the specified serials.
     */
    public List<String> getAgentsNames(List<String> serials){
        List<String> toReturn= new LinkedList<>();
        for(String serial: serials)
        	toReturn.add(agents.get(serial).getName());
        return toReturn;
    }

}
