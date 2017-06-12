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
import java.io.*;
import java.util.*;

/**
 * Reducer solves the following problem: given a set of sorted input files (each
 * containing the same type of data), merge them into one sorted file.
 * @bugs<p> no bug
 * @author
 */
public class Reducer {
	// list of files for stocking the PQ
	private List<FileIterator> fileList;
	private String type,dirName,outFile;

	public static void main(String[] args) {
		if (args.length != 3) {
			System.out.println("Usage: java Reducer <weather|thesaurus> <dir_name> <output_file>");
			System.exit(1);
		}

		String type = args[0];
		String dirName = args[1];
		String outFile = args[2];

		Reducer r = new Reducer(type, dirName, outFile);
		r.run();

	}

	/**
	 * Constructs a new instance of Reducer with the given type (a string indicating which type of data is being merged),
	 * the directory which contains the files to be merged, and the name of the output file.
	 */
	public Reducer(String type, String dirName, String outFile) {
		this.type = type;
		this.dirName = dirName;
		this.outFile = outFile;
	}

	/**
	 * Carries out the file merging algorithm described in the assignment description.
	 * @throws FileNotFoundException
	 */
	public void run() {
		File dir = new File(dirName);
		File[] files = dir.listFiles();

		Record r = null;

		// list of files for stocking the PQ
		fileList = new ArrayList<FileIterator>();

		for(int i = 0; i < files.length; i++) {
			File f = files[i];
			if(f.isFile() && f.getName().endsWith(".txt")) {
				fileList.add(new FileIterator(f.getAbsolutePath(), i));
			}
		}

		switch (type) {
			case "weather":
				r = new WeatherRecord(fileList.size());
				break;
			case "thesaurus":
				r = new ThesaurusRecord(fileList.size());
				break;
			default:
				System.out.println("Invalid type of data! " + type);
				System.exit(1);
		}
		//create minPriorityQueue to store fileNames
		FileLinePriorityQueue fileQueue = new FileLinePriorityQueue(fileList.size(), r.getComparator());
		// initialize the comparator according to the chosen record type.
		Comparator<FileLine> cmp = r.getComparator();
		// inserting fileLines into Queue
		for (int i = 0; i < fileList.size(); i++){
			try {
				fileQueue.insert(fileList.get(i).next());
			} catch (PriorityQueueFullException e) {
				System.out.println("Error! The Queue is FULL.");
				System.exit(1);
			}
		}
		try {
			PrintWriter output = new PrintWriter(outFile);		// Create the output writer
			FileLine minLine = fileQueue.removeMin();		// Initialize the last entry in the record.
			r.join(minLine);		// Join last line removed to Record

			if (fileList.get(minLine.getFileIterator().getIndex()).hasNext())
				fileQueue.insert(fileList.get(minLine.getFileIterator().getIndex()).next());

			while (!fileQueue.isEmpty()){
				FileLine toJoin = fileQueue.removeMin();	//Stores fileName for joining to Record
				if (cmp.compare(toJoin, minLine) == 0){		//if minLine and toJoin are equal
					r.join(toJoin);
					minLine = toJoin;
					if (fileList.get(minLine.getFileIterator().getIndex()).hasNext())
						fileQueue.insert(fileList.get(minLine.getFileIterator().getIndex()).next());
				} else {			//if minLine and toJoin are not equal
					output.println(r.toString());
					r.clear();
					r.join(toJoin);
					minLine = toJoin;
					if (fileList.get(minLine.getFileIterator().getIndex()).hasNext())
						fileQueue.insert(fileList.get(minLine.getFileIterator().getIndex()).next());
				}

			}
			output.println(r.toString());		//Write to
			output.flush();
			output.close();

		} catch (PriorityQueueEmptyException e) {
			System.out.println("Error! The Queue is EMPTY!");
			System.exit(1);
		} catch (PriorityQueueFullException e) {
			System.out.println("Error! The Queue is FULL!");
			System.exit(1);
		} catch (FileNotFoundException e) {
			System.out.println("Error! There is NO file found!");
			System.exit(1);
		}
	}

}
