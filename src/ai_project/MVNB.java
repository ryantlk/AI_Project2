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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

public class MVNB {
	private Path testResultFilePath;
	private String[] trainDirPath;
	private String testDirPath;

	// True test results
	private Map<String, String> testResults1;
	// Evaluated test results
	private Map<String, String> testResults2;

	private Map<String, Double> probFV1;
	private Map<String, Double> probFV2;
	private Map<String, Double> probFV3;
	private int[] numFile1;
	private int[] numFile2;
	private int[] numFile3;

	public MVNB(String p1, String p2, String p3, String p4) {
		this.trainDirPath = new String[3];
		this.trainDirPath[0] = p1;
		this.trainDirPath[1] = p2;
		this.trainDirPath[2] = p3;
		this.testDirPath = p4;

		this.probFV1 = new HashMap<String, Double>();
		this.probFV2 = new HashMap<String, Double>();
		this.probFV3 = new HashMap<String, Double>();

		// Passing val-ref of int.
		this.numFile1 = new int[1];
		this.numFile2 = new int[1];
		this.numFile3 = new int[1];
	}

	public void train() throws IOException {
		Map<String, Integer> boolFV1 = new HashMap<String, Integer>();
		Map<String, Integer> boolFV2 = new HashMap<String, Integer>();
		Map<String, Integer> boolFV3 = new HashMap<String, Integer>();

		FileVisitor<Path> fileprocessor1 = new TrainFiles(numFile1, boolFV1);
		Files.walkFileTree(Paths.get(trainDirPath[0]), fileprocessor1);
		FileVisitor<Path> fileprocessor2 = new TrainFiles(numFile2, boolFV2);
		Files.walkFileTree(Paths.get(trainDirPath[1]), fileprocessor2);
		FileVisitor<Path> fileprocessor3 = new TrainFiles(numFile3, boolFV3);
		Files.walkFileTree(Paths.get(trainDirPath[2]), fileprocessor3);

		for (Map.Entry<String, Integer> entry : boolFV1.entrySet()) {
			probFV1.put(entry.getKey(), (double) entry.getValue() / numFile1[0]);
		}
		for (Map.Entry<String, Integer> entry : boolFV2.entrySet()) {
			probFV2.put(entry.getKey(), (double) entry.getValue() / numFile2[0]);
		}
		for (Map.Entry<String, Integer> entry : boolFV3.entrySet()) {
			probFV3.put(entry.getKey(), (double) entry.getValue() / numFile3[0]);
		}
	}

	public void test() throws IOException {
		testResults2 = new HashMap<String, String>();
		FileVisitor<Path> fileprocessor0 = new TestFiles(probFV1, probFV2,
				probFV3, testResults2);
		Files.walkFileTree(Paths.get(testDirPath), fileprocessor0);
	}

	public void loadTestResults(String strPath) throws IOException {
		this.testResultFilePath = Paths.get(strPath);
		byte[] fileArray;
		fileArray = Files.readAllBytes(testResultFilePath);
		String str = new String(fileArray, "UTF-8");
		String[] splStr = str.split("\n");
		testResults1 = new HashMap<String, String>();
		for (String s : splStr) {
			String[] temp = s.split(",");
			testResults1.put(temp[0], temp[1]);
		}
	}

	public void compareTestResults() {
		double right = 0, wrong = 0;
		for (Map.Entry<String, String> entry : testResults2.entrySet()) {
			if (testResults1.get(entry.getKey()).equals(entry.getValue())) {
				right++;
			} else {
				wrong++;
			}
		}

		System.out.println((double)right / (double)(right+wrong));
	}

	void printTestResults(Map<String, String> testResults) {
		for (Map.Entry<String, String> entry : testResults.entrySet()) {
			System.out.println(entry.getKey() + "," + entry.getValue());
		}
	}

	/*
	 * TrainFiles
	 */

	static class TrainFiles extends SimpleFileVisitor<Path> {
		private int[] numFile;
		private Map<String, Integer> boolFV;

		public TrainFiles(int[] numFile, Map<String, Integer> boolFV) {
			this.numFile = numFile;
			this.numFile[0] = 0;
			this.boolFV = boolFV;
		}

		@Override
		public FileVisitResult visitFile(Path file, BasicFileAttributes attr)
				throws IOException {
			numFile[0]++;
			// System.out.println(file.getFileName().toString());

			byte[] fileArray;
			fileArray = Files.readAllBytes(file);
			String str = new String(fileArray, "UTF-8");
			str = str.replaceAll("[^a-zA-Z]", " ").toLowerCase().trim();
			str = str.replaceAll("\n", " ");
			str = str.replaceAll(" +", " ");

			StringTokenizer st = new StringTokenizer(str);
			List<String> inList = new ArrayList<String>();
			while (st.hasMoreElements()) {
				String tok = st.nextToken();
				if (inList.contains(tok)) {
				} else {
					inList.add(tok);
					if (boolFV.containsKey(tok)) {
						boolFV.put(tok, boolFV.get(tok) + 1);
					} else {
						boolFV.put(tok, 1);
					}
				}
			}
			return FileVisitResult.CONTINUE;
		}
	}

	/*
	 * TestFiles
	 */

	static class TestFiles extends SimpleFileVisitor<Path> {
		private Map<String, Double> probFV1;
		private Map<String, Double> probFV2;
		private Map<String, Double> probFV3;
		private Map<String, String> testResults;

		public TestFiles(Map<String, Double> probFV1,
				Map<String, Double> probFV2, Map<String, Double> probFV3,
				Map<String, String> testResults) {
			this.probFV1 = probFV1;
			this.probFV2 = probFV2;
			this.probFV3 = probFV3;
			this.testResults = testResults;

		}

		@Override
		public FileVisitResult visitFile(Path file, BasicFileAttributes attr)
				throws IOException {
			byte[] fileArray;
			// System.out.println(file.getFileName().toString());
			fileArray = Files.readAllBytes(file);
			String str = new String(fileArray, "UTF-8");
			str = str.replaceAll("[^a-zA-Z]", " ").toLowerCase().trim();
			str = str.replaceAll("\n", " ");
			str = str.replaceAll(" +", " ");

			StringTokenizer st = new StringTokenizer(str);
			List<String> inList = new ArrayList<String>();
			// Add tokens to list.
			while (st.hasMoreElements()) {
				String tok = st.nextToken();
				if (inList.contains(tok)) {
				} else {
					inList.add(tok);
				}
			}
			// Even number is inverse probability.
			double[] nbProb = new double[6];
			boolean[] nbSet = new boolean[6];

			for (String s : inList) {
				if (probFV1.get(s) != null) {
					if (nbSet[0]) {
						nbProb[0] += Math.log(probFV1.get(s));
					} else {
						nbProb[0] = Math.log(probFV1.get(s));
						nbSet[0] = true;
					}
					// Inverse probability
					if (nbSet[1]) {
						nbProb[1] += Math.log((1.0 - probFV1.get(s)));
					} else {
						nbProb[1] = Math.log(1.0 - probFV1.get(s));
						nbSet[1] = true;
					}
				}
				if (probFV2.get(s) != null) {
					if (nbSet[2]) {
						nbProb[2] += Math.log(probFV2.get(s));
					} else {
						nbProb[2] = Math.log(probFV2.get(s));
						nbSet[2] = true;
					}
					// Inverse probability
					if (nbSet[3]) {
						nbProb[3] += Math.log((1.0 - probFV2.get(s)));
					} else {
						nbProb[3] = Math.log(1.0 - probFV2.get(s));
						nbSet[3] = true;
					}
				}
				if (probFV3.get(s) != null) {
					if (nbSet[4]) {
						nbProb[4] += Math.log(probFV3.get(s));
					} else {
						nbProb[4] = Math.log(probFV3.get(s));
						nbSet[4] = true;
					}
					// Inverse probability
					if (nbSet[5]) {
						nbProb[5] += Math.log((1.0 - probFV3.get(s)));
					} else {
						nbProb[5] = Math.log(1.0 - probFV3.get(s));
						nbSet[5] = true;
					}
				}
			} // End For-loop

//			System.out.println(nbProb[0]);
//			System.out.println(nbProb[2]);
//			System.out.println(nbProb[4]);
			
			if (nbProb[0] > nbProb[2]) {
				if (nbProb[0] > nbProb[4]) {
					System.out.println(file.getFileName().toString() + ","
							+ "DR");
					testResults.put(file.getFileName().toString(), "DR");
				} else {
					System.out.println(file.getFileName().toString() + ","
							+ "L");
					testResults.put(file.getFileName().toString(), "L");
				}
			} else {
				if (nbProb[2] > nbProb[4]) {
					System.out.println(file.getFileName().toString() + ","
							+ "DT");
					testResults.put(file.getFileName().toString(), "DT");
				} else {
					System.out.println(file.getFileName().toString() + ","
							+ "L");
					testResults.put(file.getFileName().toString(), "L");
				}
			}

			return FileVisitResult.CONTINUE;
		}
	}
}