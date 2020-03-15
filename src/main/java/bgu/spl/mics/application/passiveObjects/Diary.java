package bgu.spl.mics.application.passiveObjects;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Passive object representing the diary where all reports are stored.
 * <p>
 * This class must be implemented safely as a thread-safe singleton.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You can add ONLY private fields and methods to this class as you see fit.
 */
public class Diary {
	private List<Report> reports=new LinkedList<>();//only executed missions.
	private AtomicInteger total=new AtomicInteger(0) ;//total number of recieved missions (executed+aborted)// get from the Gson file.
	private static Diary singleInstance =new Diary();
	/**
	 * Retrieves the single instance of this class.
	 */

	public static Diary getInstance() {
		return singleInstance;
	}

	public List<Report> getReports() {
		return reports;
	}

	/**
	 * adds a report to the diary
	 * @param reportToAdd - the report to add
	 */
	public synchronized void addReport(Report reportToAdd){
			reports.add(reportToAdd);
			System.out.println("Report about mission "+reportToAdd.getMissionName()+ " was added to the diary.");

	}

	/**
	 *
	 * <p>
	 * Prints to a file name @filename a serialized object List<Report> which is a
	 * List of all the reports in the diary.
	 * This method is called by the main method in order to generate the output.
	 */
	public void printToFile(String filename){
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		try {
			FileWriter writer = new FileWriter(filename);
			String output=gson.toJson(Diary.getInstance());
			writer.write(output);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Gets the total number of received missions (executed / aborted) be all the M-instances.
	 * @return the total number of received missions (executed / aborted) be all the M-instances.
	 */
	public AtomicInteger getTotal(){
		return total;
	}

	/**
	 * Increments the total number of recieved missions by 1.
	 */
	public void incrementTotal(){
		total.getAndAdd(1);
	}

}
