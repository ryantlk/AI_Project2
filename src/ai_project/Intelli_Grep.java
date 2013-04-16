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
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author ryantlk
 */
public class Intelli_Grep {
	
	String testPath;

	public Intelli_Grep(String testPath) {
		this.testPath = testPath;
	}
	
	public void classify() throws IOException{
		FileVisitor<Path> fileprocessor = new ProcessFile();
		Files.walkFileTree(Paths.get(testPath), fileprocessor);
	}
	
	private static final class ProcessFile extends SimpleFileVisitor<Path>{
		@Override
		public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
			Scanner thescanner = new Scanner(new File(file.toString())).useDelimiter("\\Z");
			String fileText = thescanner.next();
			int DT, DR, L;
			DT = DR = L = 0;
			String classification = "DT";
			fileText = fileText.replaceAll("[^a-zA-Z]", " ").toLowerCase().trim();
			fileText = fileText.replaceAll("\n", " ");
			fileText = fileText.replaceAll(" +", " ");
			Pattern p = Pattern.compile("\\bdeed of trust\\b");
			Matcher m = p.matcher(fileText);
			while(m.find()){
				DT++;
			}
			p = Pattern.compile("\\bdeed of reconveyance\\b");
			m = p.matcher(fileText);
			while(m.find()){
				DR++;
			}
			if(DR > DT){
				classification = "DR";
			}
			p = Pattern.compile("\\blien\\b");
			m = p.matcher(fileText);
			while(m.find()){
				L++;
			}
			if(L > DT && L > DR){
				classification = "L";
			}
			System.out.println("Intelli-Grep Baseline, " + file.getFileName().toString() + ", " + classification);
			return FileVisitResult.CONTINUE;
		}
	}
}
