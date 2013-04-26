/*
 * Multi-variate naive bayes
 * Author: Christopher Dang
 * Date: April 26, 2013
 * File: MVNB.java
 */

package ai_project;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.Map;

public class MVNB {
	private Path testResultFilePath;
	private String[] trainDirPath;
	private String testDirPath;
	private Map<String, String> testResults;

	public MVNB(String p1, String p2, String p3, String p4) {
		this.trainDirPath = new String[3];
		this.trainDirPath[0] = p1;
		this.trainDirPath[1] = p2;
		this.trainDirPath[2] = p3;
		this.testDirPath = p4;
	}

	public void train() throws IOException {
		FileVisitor<Path> fileprocessor = new ProcessFile();
		Files.walkFileTree(Paths.get(trainDirPath[0]), fileprocessor);
		Files.walkFileTree(Paths.get(trainDirPath[1]), fileprocessor);
		Files.walkFileTree(Paths.get(trainDirPath[2]), fileprocessor);
	}

	public void loadTestResults(String strPath) throws IOException {
		this.testResultFilePath = Paths.get(strPath);
		byte[] fileArray;
		fileArray = Files.readAllBytes(testResultFilePath);
		String str = new String(fileArray, "UTF-8");
		String[] splStr = str.split("\n");
		testResults = new HashMap<String, String>();
		for (String s : splStr) {
			String[] temp = s.split(",");
			testResults.put(temp[0], temp[1]);
		}
	}

	void printTestResults() {
		for (Map.Entry<String, String> entry : testResults.entrySet()) {
			System.out.println(entry.getKey() + "," + entry.getValue());
		}
	}

	static class ProcessFile extends SimpleFileVisitor<Path> {
		@Override
		public FileVisitResult visitFile(Path file, BasicFileAttributes attr)
				throws IOException {
			System.out.println(file.getFileName().toString());
			

			return FileVisitResult.CONTINUE;
		}
	}
}