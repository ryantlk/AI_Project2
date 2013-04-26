package ai_project;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;


public class Percp {
	
	public Percp(String d1,String d2, String d3,String testDir) {
		List<String> Dr=new ArrayList<String>();
		List<String> Dt=new ArrayList<String>();
		List<String> L=new ArrayList<String>();
		List<String>bagOfWords=new ArrayList<String>();
		String [] alldir=new String[3];
		alldir[0]=d1;
		alldir[1]=d2;
		alldir[2]=d3;
		
		for(int i=0;i<3;i++){
			java.io.File h=new java.io.File(alldir[i]);
			String [] fileS=h.list();
			for(String f:fileS){
				java.io.File hh=new java.io.File(alldir[i]+"/"+f);
				try {
					FileInputStream d=new FileInputStream(hh);
					byte [] ss;
					ss=new byte[(int)h.length()];
					try {
						d.read(ss);
						String st=new String(ss);
						if(h.getName().equalsIgnoreCase("dr")){
							Dr.add(st);
						}
						else if(h.getName().equalsIgnoreCase("dt")){
							Dt.add(st);
						}
						else{
							L.add(st);
						}
						d.close();
					} catch (IOException e) {
					}
					
				} catch (FileNotFoundException e) {
				}
			}
		}
		wordRecord [] b;
		System.out.println("got l");
		List<Perceptron> myPerceptrons=new ArrayList<Perceptron>();
		Perceptron drp=new Perceptron(Dr,"Dr"); 
		myPerceptrons.add(drp);
		Perceptron dtp=new Perceptron(Dt,"Dt"); 
		myPerceptrons.add(dtp);
		Perceptron lp=new Perceptron(L,"L"); 
		myPerceptrons.add(lp);
		for(Perceptron f: myPerceptrons){
			f.preprocess();
		}
		boolean toadd=true;
		//construct the full list
		for(Perceptron f: myPerceptrons){
			for(wordRecord z:f.bagOfWords2){
				for(String g:bagOfWords){
					if(g.equalsIgnoreCase(z.word)){
						toadd=false;
					}
				}
				if(toadd){
					bagOfWords.add(z.word);
				}
				toadd=true;
			}
		}
		for(Perceptron f: myPerceptrons){
			f.setupbag(bagOfWords);
		}
		b=new wordRecord[bagOfWords.size()];
		for(int i=0;i<bagOfWords.size();i++){
			b[i]=new wordRecord(bagOfWords.get(i));
			b[i].count=0;
		}
		double alpha=0.1;
		for(int i=0;i<100;i++){
			for(Perceptron f:myPerceptrons){
				for(String s:f.FilesToLearn){
					String [] g=s.split(" ");
					int totalCount=0;
					for(String d:g){
						for(wordRecord q:b){
							if(d.equalsIgnoreCase(q.word)){
								q.count+=1;
							}
						}
						totalCount+=1;
					}
					for(wordRecord d:b){
						d.countP=((float)d.count)/totalCount;
					}
					myPerceptrons.get(0).train(b,f.classification, alpha);
					myPerceptrons.get(1).train(b,f.classification, alpha);
					myPerceptrons.get(2).train(b,f.classification, alpha);
					for(wordRecord j:b){
						j.count=0;
					}
				}
				
			}
			alpha*=.99;
		}
		System.out.println("Percptron Trained");
		if(testDir.length()>0){
			java.io.File testD;
			testD=new java.io.File(testDir);
			List <String> theFiles=new ArrayList<String>();
			if(testD.isDirectory()){
				List <String> testF=new ArrayList<String>();
				String [] testFiles=testD.list();
				for(String f:testFiles){
					testD=new java.io.File(testDir+"/"+f);
					try {
						FileInputStream d=new FileInputStream(testD);
						byte [] ss;
						ss=new byte[(int)testD.length()];
						try {
							d.read(ss);
							String st=new String(ss);
							testF.add(st);
							theFiles.add(f);
							d.close();
						} catch (IOException e) {
						}
						
					} catch (FileNotFoundException e) {
					}
				}
				PrintWriter p1=null;
				java.io.File kl=new java.io.File("./results.txt");
				try {
					kl.createNewFile();
					 p1=new PrintWriter(kl);
				} catch (FileNotFoundException e) {
					System.out.println("no file");
					System.exit(1);
				} catch (IOException e) {
					System.out.println("no file");
					System.exit(1);
				}
				int fileN=0;
				Perceptron testP=new Perceptron(testF,"ts");
				for(String f:testP.FilesToLearn){
					f=f.toLowerCase();
					f=f.replaceAll("[^a-zA-Z]", " ");
					f=f.replace("\n"," ");
					String regex = "\\s{2,}"; 
					f = f.replaceAll(regex, " "); 
					f=f.trim();
					String [] g=f.split(" ");
					int totalCount=0;
					for(String d:g){
						for(wordRecord q:b){
							if(d.equalsIgnoreCase(q.word)){
								q.count+=1;
								break;
							}
						}
						totalCount+=1;
					}
					for(wordRecord d:b){
						d.countP=((float)d.count)/totalCount;
					}
					boolean drV=myPerceptrons.get(0).vote(b);
					boolean dtV=myPerceptrons.get(1).vote(b);
					boolean lV=myPerceptrons.get(2).vote(b);
					if(drV&&!dtV&&!lV){
						p1.println("Perceptronv1,"+theFiles.get(fileN)+","+"dr");
					}
					else if(!drV&&dtV&&!lV){
						p1.println("Perceptronv1,"+theFiles.get(fileN)+","+"dt");
					}
					else if(!drV&&!dtV&&lV){
						p1.println("Perceptronv1,"+theFiles.get(fileN)+","+"l");
					}
					else{
						int ranChoice=(int)(Math.random()*3.0);
						if(ranChoice==1){
							if(drV){
								p1.println("Perceptronv1,"+theFiles.get(fileN)+","+"dr");
							}
							else{
								ranChoice=(int)(Math.random()*2);
								if(ranChoice==1){
									if(dtV){
										p1.println("Perceptronv1,"+theFiles.get(fileN)+","+"dt");
									}
									else{
										p1.println("Perceptronv1,"+theFiles.get(fileN)+","+"l");
									}
								}
								else{
									if(lV){
										p1.println("Perceptronv1,"+theFiles.get(fileN)+","+"l");
									}
									else{
										p1.println("Perceptronv1,"+theFiles.get(fileN)+","+"dt");
									}
								}
							}
						}
						else if(ranChoice==2){
							if(dtV){
								p1.println("Perceptronv1,"+theFiles.get(fileN)+","+"dt");
							}
							else{
								ranChoice=(int)(Math.random()*2);
								if(ranChoice==1){
									if(drV){
										p1.println("Perceptronv1,"+theFiles.get(fileN)+","+"dr");
									}
									else{
										p1.println("Perceptronv1,"+theFiles.get(fileN)+","+"l");
									}
								}
								else{
									if(lV){
										p1.println("Perceptronv1,"+theFiles.get(fileN)+","+"l");
									}
									else{
										p1.println("Perceptronv1,"+theFiles.get(fileN)+","+"dr");
									}
								}
							}
						}
						else{
							if(lV){
								p1.println("Perceptronv1,"+theFiles.get(fileN)+","+"l");
							}
							else{
								ranChoice=(int)(Math.random()*2);
								if(ranChoice==1){
									if(dtV){
										p1.println("Perceptronv1,"+theFiles.get(fileN)+","+"dt");
									}
									else{
										p1.println("Perceptronv1,"+theFiles.get(fileN)+","+"dr");
									}
								}
								else{
									if(drV){
										p1.println("Perceptronv1,"+theFiles.get(fileN)+","+"dr");
									}
									else{
										p1.println("Perceptronv1,"+theFiles.get(fileN)+","+"dt");
									}
								}
							}
						}
					}
					for(wordRecord j:b){
						j.count=0;
					}
					fileN++;	
				}
				p1.close();
			}
		}
	}

}
