/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ai_project;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.StringTokenizer;

/**
 *
 * @author ryantlk
 */
public class Naive_Bayes {
	//paths index 0 = DR, 1 = DT, 2 = L, 3 = TEST
	String paths[];

	public Naive_Bayes(String path1, String path2, String path3, String path4) {
		paths = new String[4];
		this.paths[pathIndex(path1)] = path1;
		this.paths[pathIndex(path2)] = path2;
		this.paths[pathIndex(path3)] = path3;
		this.paths[pathIndex(path4)] = path4;
	}
	
	public void train() throws IOException{
		//total word counts for classes same indexing as paths
		int totalWordCounts[] = new int[3];
		
		/* Hashmaps built from files key is word in document collection and 
		 * the value is the number of times that word occurs in the document
		 * collection.
		 */
		HashMap<String, Integer> DRHashMap = new HashMap<>();
		HashMap<String, Integer> DTHashMap = new HashMap<>();
		HashMap<String, Integer> LHashMap = new HashMap<>();
		
		//Building hashmaps from files in provided paths.
		FileVisitor<Path> fileprocessor = new ProcessFile(DRHashMap, totalWordCounts, 0);
		Files.walkFileTree(Paths.get(paths[0]), fileprocessor);
		fileprocessor = new ProcessFile(DTHashMap, totalWordCounts, 1);
		Files.walkFileTree(Paths.get(paths[1]), fileprocessor);
		fileprocessor = new ProcessFile(LHashMap, totalWordCounts, 2);
		Files.walkFileTree(Paths.get(paths[2]), fileprocessor);
		
		/* Build sorted array listed sorted by value then key from
		 * hashmap entries. Gives list of words in document collection
		 * sorted by amount then alphabetically.
		 */
		List<Map.Entry<String, Integer>> DRSortedEntrys = new ArrayList<>(DRHashMap.entrySet());
		Collections.sort(DRSortedEntrys, new MapComparator());
		List<Map.Entry<String, Integer>> DTSortedEntrys = new ArrayList<>(DTHashMap.entrySet());
		Collections.sort(DTSortedEntrys, new MapComparator());
		List<Map.Entry<String, Integer>> LSortedEntrys = new ArrayList<>(LHashMap.entrySet());
		Collections.sort(LSortedEntrys, new MapComparator());
	}
	
	private static class MapComparator implements Comparator<Map.Entry<String, Integer>>{
		/* Sorts by value then alphabetically */
		@Override
		public int compare(Map.Entry<String, Integer> a, Map.Entry<String, Integer> b) {
			if(a.getValue() == b.getValue()){
				return a.getKey().compareTo(b.getKey());
			}else{
				return b.getValue().compareTo(a.getValue());
			}
		}
		
	}
	
	private int pathIndex(String path){
		/* Returns index based on which path is given
		 * The index is returned based on the chosen order of paths[]
		 * See comment line 25
		 */
		int returnIndex;
		
		if(path.contains("DR")){
			returnIndex = 0;
		}else if(path.contains("DT")){
			returnIndex = 1;
		}else if(path.contains("L")){
			returnIndex = 2;
		}else{
			returnIndex = 3;
		}
		return returnIndex;
	}
	
	private static class ProcessFile extends SimpleFileVisitor<Path>{
		HashMap<String, Integer> themap;
		int totalWordCounts[];
		int index;
		
		public ProcessFile(HashMap themap, int totalWordCounts[], int index) {
			this.themap = themap;
			this.totalWordCounts = totalWordCounts;
			this.index = index;
		}
		
		@Override
		public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
			Scanner thescanner = new Scanner(new File(file.toString())).useDelimiter("\\Z");
			String fileText = thescanner.next();
			/* Replace all non alphabetic characters and newlines with spaces.
			 * Then reduce all whitespace to a single space.
			 */
			fileText = fileText.replaceAll("[^a-zA-Z]", " ").toLowerCase().trim();
			fileText = fileText.replaceAll("\n", " ");
			fileText = fileText.replaceAll(" +", " ");
			StringTokenizer st = new StringTokenizer(fileText);
			/* Goes through each word in the current document and if the
			 * word is in the hashmap icrements the count. Otherwise 
			 * the word is added to the hashmap.
			 */
			while(st.hasMoreTokens()){
				String token = st.nextToken();
				if(themap.containsKey(token)){
					themap.put(token, themap.get(token) + 1);
				}else{
					themap.put(token, 1);
				}
				totalWordCounts[index]++;
			}
			return FileVisitResult.CONTINUE;
		}
	}
}
