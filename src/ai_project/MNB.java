package ai_project;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

public class MNB {

	//paths index 0 = DR, 1 = DT, 2 = L, 3 = TEST
		String paths[];
		//unique words
		int DR_Count = 0;
		int DT_Count = 0;
		int L_Count = 0;
		//total amount of words
		int DictionaryCount = 0;
		//hashes
		HashMap<String, Integer> DRHash = new HashMap<String, Integer>();
		HashMap<String, Integer> DTHash = new HashMap<String, Integer>();
		HashMap<String, Integer> LHash = new HashMap<String, Integer>();
		

		public MNB(String path1, String path2, String path3, String path4) {
			paths = new String[4];
			this.paths[pathIndex(path1)] = path1;
			this.paths[pathIndex(path2)] = path2;
			this.paths[pathIndex(path3)] = path3;
			this.paths[pathIndex(path4)] = path4;
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
		public void ConcatenatedFiles() throws IOException {
			for(int j = 0; j < 3; j++){
	            PrintWriter pw = new PrintWriter(new FileOutputStream("concat" + j + ".txt"));
	            File file = new File(this.paths[j]);
	            File[] files = file.listFiles();
	            for (int i = 0; i < files.length; i++) {
	
                    BufferedReader br = new BufferedReader(new FileReader(files[i].getPath()));
	                    String line = br.readLine();
	                    while (line != null) {
	                    	line = line.replaceAll("[^A-Za-z0-9]", " ");
	                    	line = line.trim().replaceAll(" +", " ");
	                    	line = line.toLowerCase();
	                    	pw.println(line);
	                    	line = br.readLine();
	                    }
	                    br.close();
	            }
	            pw.close();
	            //System.out.println("All files have been concatenated into concat.txt");
			}
		}
		
		public void train() throws IOException{
			this.ConcatenatedFiles();
			Integer freq;
			for(int i = 0; i < 3 ;i++){
				File file = new File("concat"+i+".txt");
				@SuppressWarnings("resource")
				BufferedReader br = new BufferedReader(new FileReader(file));
				
				String line;
				
				while((line = br.readLine()) != null){
					//go through line and grab all the words
					StringTokenizer parser = new StringTokenizer(line, " \t\n\r\f.,;:!?'"); 
					while(parser.hasMoreTokens()){				
						String currentWord = parser.nextToken();
						if(i ==0){
							freq = this.DRHash.get(currentWord);
							if(freq == null){
								freq = 0;
								this.DictionaryCount++;
							}
							this.DRHash.put(currentWord, freq+1);
							this.DR_Count++;
						}else if (i ==1){
							freq = this.DTHash.get(currentWord);
							if(freq == null){
								freq = 0;
								if(this.DRHash.get(currentWord) == null)
									this.DictionaryCount++;
							}
							this.DTHash.put(currentWord, freq+1);
							this.DT_Count++;
						}else{
							freq = this.LHash.get(currentWord);
							if(freq == null){
								freq = 0;
								if (this.DRHash.get(currentWord) == null && this.DTHash.get(currentWord) == null)
								this.DictionaryCount++;

							}
							this.LHash.put(currentWord, freq+1);
							this.L_Count++;
						}
					}
				}
			}
		}
		
		public void test() throws IOException{
			PrintWriter pw = new PrintWriter(new FileOutputStream("MNBoutput.txt"));
			this.train();
            int DT_ = 0;
            int DR_ = 0;
            int L_ = 0;
            File file = new File(this.paths[3]);//test directory
            File[] files = file.listFiles();
            for (int i = 0; i <files.length; i++) {
    			Double DTProb = 1.0;
    			Double DRProb = 1.0;
    			Double LProb = 1.0;
    			int dt_count = 0;
    			int dr_count = 0;
    			int l_count = 0;
    			int DTC = (this.DT_Count+this.DictionaryCount);
    			int DRC = (this.DR_Count+this.DictionaryCount);
    			int LC = (this.L_Count+this.DictionaryCount);
    			String result;
    	
            	BufferedReader br = new BufferedReader(new FileReader(files[i].getPath()));
                String line = br.readLine();
                while (line != null) {
                	line = line.toLowerCase();
                   	line = line.replaceAll("[^A-Za-z0-9]", " ");
                	line = line.trim().replaceAll(" +", " ");
                	StringTokenizer parser = new StringTokenizer(line, " \t\n\r\f.,;:!?'"); 
					while(parser.hasMoreTokens()){	
						
						String currentWord = parser.nextToken();
						if(this.DTHash.get(currentWord) != null){
							dt_count = this.DTHash.get(currentWord);
						}
						if(this.DRHash.get(currentWord) != null){
							dr_count = this.DRHash.get(currentWord);							
						}
						if(this.LHash.get(currentWord) != null){
							l_count = this.LHash.get(currentWord);							
						}
							double DTH = (dt_count+1);;
							DTH/= DTC;
							double DRH =(dr_count+1);
							DRH /= DRC;
							double LH =(l_count+1);
							LH /=LC;
							DTProb = DTProb+Math.log10(DTH/3);
							DRProb = DRProb+Math.log10(DRH/3);
							LProb = LProb+Math.log10(LH/3);

			                dt_count = 0;
			    			dr_count = 0;
			    			l_count = 0;

					}
                	line = br.readLine();
                }
                br.close();

                if (DTProb >= DRProb){
                	if (DTProb >= LProb){
                		result = ",DT";
                		DT_++;
                	}else{
                		result = ",L";
                		L_++;
                	}
                }else if (DRProb >= LProb){
                	result = ",DR";
                	DR_++;
                }else{
                	result = ",L";
                	L_++;
                }
                
                pw.println("Multinomial Naive Bayes," + files[i].getName() + result);
            }
		}


}
