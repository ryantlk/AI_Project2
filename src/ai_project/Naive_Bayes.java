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
import java.util.HashMap;
import java.util.Scanner;
import java.util.StringTokenizer;

/**
 *
 * @author ryantlk
 */
public class Naive_Bayes {
	//paths index 0 = DR, 1 = DT, 2 = L, 3 = TEST
	String paths[];
	HashMap<String, Integer> DRHashMap;
	HashMap<String, Integer> DTHashMap;
	HashMap<String, Integer> LHashMap;

	public Naive_Bayes(String path1, String path2, String path3, String path4) {
		paths = new String[4];
		DRHashMap = new HashMap<>();
		DTHashMap = new HashMap<>();
		LHashMap = new HashMap<>();
		this.paths[pathIndex(path1)] = path1;
		this.paths[pathIndex(path2)] = path2;
		this.paths[pathIndex(path3)] = path3;
		this.paths[pathIndex(path4)] = path4;
	}
	
	public void classify() throws IOException{
		FileVisitor<Path> fileprocessor = new ProcessFile(DRHashMap);
		Files.walkFileTree(Paths.get(paths[0]), fileprocessor);
		fileprocessor = new ProcessFile(DTHashMap);
		Files.walkFileTree(Paths.get(paths[1]), fileprocessor);
		fileprocessor = new ProcessFile(LHashMap);
		Files.walkFileTree(Paths.get(paths[2]), fileprocessor);
		System.out.println(DRHashMap);
		System.out.println(DTHashMap);
		System.out.println(LHashMap);
	}
	
	private int pathIndex(String path){
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
		
		public ProcessFile(HashMap themap) {
			this.themap = themap;
		}
		
		@Override
		public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
			Scanner thescanner = new Scanner(new File(file.toString())).useDelimiter("\\Z");
			String fileText = thescanner.next();
			fileText = fileText.replaceAll("[^a-zA-Z]", " ").toLowerCase().trim();
			fileText = fileText.replaceAll("\n", " ");
			fileText = fileText.replaceAll(" +", " ");
			StringTokenizer st = new StringTokenizer(fileText);
			while(st.hasMoreTokens()){
				String token = st.nextToken();
				if(themap.containsKey(token)){
					themap.put(token, themap.get(token) + 1);
				}else{
					themap.put(token, 1);
				}
			}
			return FileVisitResult.CONTINUE;
		}
	}
}
