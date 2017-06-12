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
import java.lang.StringBuilder;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Collections;
import java.util.Comparator;

/**
 * The Thesaurus Record extends Record and merges thesaurus data.
 * Word is the entry into thesaurus, synonyms is a list of synonyms to mainWord.
 */

public class ThesaurusRecord extends Record{
	private String mainWord;  //mainWord entry to thesaurus
	private List<String> synonyms; //Synonyms of mainWord

	/**
	 * Makes a new ThesaurusRecord.  It passes the parameter numfiles to the
	 * parent method,
	 * and subsequently calls the clear method.
	 */
	public ThesaurusRecord(int numFiles) {
		super(numFiles);
		clear();
	}

	/**
	 * This method acts like compareTo() method for Strings, applied to
	 * FileLines' Strings split at the ":".
	 */
	private class ThesaurusLineComparator implements Comparator<FileLine> {
		public int compare(FileLine l1, FileLine l2) {

			//Splits FileLine l1 at ":"
			String word1 = l1.getString().split(":")[0];
			//Splits FileLine l2 at ":"
			String word2 = l2.getString().split(":")[0];

			return word1.compareTo(word2); //compares using String's compareTo() method

		}

		public boolean equals(Object o) {
			return this.equals(o);
		}
	}

	/**
	 * Returns new instance of ThesaurusLineComparator class.
	 */
	public Comparator<FileLine> getComparator() {
		return new ThesaurusLineComparator();
	}

	/**
	 * Sets the mainWord to null and empties synonyms (the list of synonyms).
	 */
	public void clear() {
		mainWord = null;
		synonyms = new ArrayList<String>();
	}

	/**
	 * This method parses synonyms contained FileLine "w". It inserts those
	 * which which aren't already in synonyms.
	 */
	public void join(FileLine w) {
		if(mainWord == null)
			//sets mainWord to portion of w before ":"
			mainWord = w.getString().split(":")[0];

		//Creates scanner of synonyms
		Scanner scnr = new Scanner(w.getString().split(":")[1]);
		scnr.useDelimiter(",");
		while(scnr.hasNext()){
			String nextSyn = scnr.next();
			if(!synonyms.contains(nextSyn)) //Adds synonym if it is not already in the list synonyms
				synonyms.add(nextSyn);
		}
		scnr.close();
		Collections.sort(synonyms);
	}

	/**
	 * Returns string of output in the desired format.
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder(); //creates StringBuilder
		sb.append(mainWord); //adds mainWord to string
		sb.append(":");
		for(int i = 0; i < synonyms.size(); i++){ //adds synonym list to StringBuilder
			if (i == synonyms.size()-1) {
				sb.append(synonyms.get(i));
			}else{
				sb.append(synonyms.get(i));
				sb.append(",");
			}
		}
		return sb.toString(); //returns formatted string
	}
}
