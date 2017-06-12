///////////////////////////////////////////////////////////////////////////////
//	Semester:		CS367 Spring 2017
//  PROJECT:		P3
//  FILE:			Reducer.java
//
//  TEAM:    16;
//
// Authors:
// Author1: Ahmad Zaidi, azaidi4@wisc.edu, azaidi4, LEC001
// Author2: Devin Samaranayake, dsamaranayak@wisc.edu>, dsamaranayak, LEC001
//
///////////////////////////////////////////////////////////////////////////////
import java.util.Comparator;

/**
 * The WeatherRecord class is the child class of Record to be used when merging
 * weather data. Station and Date store the stationID and date associated with
 * each weather reading that this object stores. weather stores the weather
 * readings, in the same order as the files from which they came are indexed.
 */
public class WeatherRecord extends Record{
	private int stationID; //the stationID that recorded the reading
	private int date; //the date the reading occurred
	private double [] weather; //weather readings
	/**
	 * Constructs a new WeatherRecord by passing the parameter to the parent
	 * constructor and then calling the clear method()
	 */
	public WeatherRecord(int numFiles) {
		super(numFiles); //pass number of files to parent constructor
		weather = new double[numFiles];
		clear();
	}

	/**
	 * This comparator should first compare the stations associated with
	 * the given FileLines. If they are the same, then the dates should be compared.
	 */
	private class WeatherLineComparator implements Comparator<FileLine> {
		public int compare(FileLine l1, FileLine l2) {
			// set up the comparing data

			String Station1 = l1.getString().split(",")[0];//gets ID of stationID 1
			String Station2 = l2.getString().split(",")[0];//gets ID of stationID 2
			String Date1 = l1.getString().split(",")[1];//gets date 1
			String Date2 = l2.getString().split(",")[1];//gets date 2
			int StationInt1 = Integer.parseInt(Station1);
			int StationInt2 = Integer.parseInt(Station2);
			int DateInt1 = Integer.parseInt(Date1);
			int DateInt2 = Integer.parseInt(Date2);
			// return the compared outcome
			if (StationInt1 != StationInt2) return StationInt1 - StationInt2;
			else if (DateInt1 != DateInt2) return DateInt1 - DateInt2;
			else return 0; //returns 0 if they are the same
		}

		public boolean equals(Object o) {
			return this.equals(o);
		}
	}

	/**
	 * This method should simply create and return a new instance of the
	 * WeatherLineComparator class.
	 */
	public Comparator<FileLine> getComparator() {
		return new WeatherLineComparator();
	}

	/**
	 * This method should fill each entry in the data structure containing
	 * the readings with Double.MIN_VALUE
	 */
	public void clear() {
		stationID = 0;
		date = 0;
		for (int i=0; i<weather.length; i++){
			weather[i] = Double.MIN_VALUE;
		}
	}

	/**
	 * This method should parse the String associated with the given FileLine
	 * to get the stationID, date, and reading contained therein. Then, in the
	 * data structure holding each reading, the entry with index equal to the
	 * parameter FileLine's index should be set to the value of the reading.
	 * Also, so that this method will handle merging when this WeatherRecord
	 * is empty, the stationID and date associated with this WeatherRecord should
	 * be set to the stationID and date values which were similarly parsed.
	 */
	public void join(FileLine li) {
		// get the stationID and date.
		int compareStation = Integer.parseInt(li.getString().split(",")[0]);
		int compareDate = Integer.parseInt(li.getString().split(",")[1]);
		double Reading = Double.parseDouble(li.getString().split(",")[2]);
		// merge the readings also handle the empty record merging.
		if (stationID == 0 && date == 0){
			stationID = compareStation;
			date = compareDate;
			weather[li.getFileIterator().getIndex()] = Reading;
		}
		else{ //add reading to array
			weather[li.getFileIterator().getIndex()] = Reading;
		}

	}

	/**
	 * Returns string of output with described format
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder();
		// build the formatted String.
		String stationStr = String.valueOf(stationID);
		String dateStr = String.valueOf(date);
		sb.append(stationStr+",");//add stationID to string
		sb.append(dateStr); //add date to string
		for (int i = 0; i < getNumFiles(); i++){
			if (weather[i] == Double.MIN_VALUE)
				sb.append(",-"); //if min value add "-"
			else
				sb.append(","+weather[i]);
		}
		return sb.toString();
	}
}
