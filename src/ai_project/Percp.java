package ai_project;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.*;


public class Percp {
	
	public static void main(String[] args) {
		List<String> Dr=new ArrayList<String>();
		List<String> Dt=new ArrayList<String>();
		List<String> L=new ArrayList<String>();
		List<String>bagOfWords=new ArrayList<String>();
		java.io.File h=new java.io.File("./data/DR");
		wordRecord [] b;
		String [] fileS=h.list();
		for(String f:fileS){
			h=new java.io.File("./data/DR/"+f);
			try {
				Scanner d=new Scanner(h);
				String st=d.nextLine();
				while(d.hasNextLine()){
					st.concat(d.nextLine());
				}
				Dr.add(st);
				d.close();
			} catch (FileNotFoundException e) {
			}
		}
		h=new java.io.File("./data/DT");
		fileS=h.list();
		for(String f:fileS){
			h=new java.io.File("./data/DT/"+f);
			try {
				Scanner d=new Scanner(h);
				String st=d.nextLine();
				while(d.hasNextLine()){
					st.concat(d.nextLine());
				}
				Dt.add(st);
				d.close();
			} catch (FileNotFoundException e) {
			}
		}
		h=new java.io.File("./data/L");
		fileS=h.list();
		for(String f:fileS){
			h=new java.io.File("./data/L/"+f);
			try {
				Scanner d=new Scanner(h);
				String st=d.nextLine();
				while(d.hasNextLine()){
					st.concat(d.nextLine());
				}
				L.add(st);
				d.close();
			} catch (FileNotFoundException e) {
			}
		}
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
					if(g==z.word){
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
							if(d==q.word){
								q.count+=1;
							}
						}
						totalCount+=1;
					}
					for(wordRecord d:b){
						d.countP=((float)d.countP)/totalCount;
					}
					myPerceptrons.get(0).train(b,f.classification, alpha);
					myPerceptrons.get(1).train(b,f.classification, alpha);
					myPerceptrons.get(2).train(b,f.classification, alpha);
					for(wordRecord j:b){
						j.count=0;
					}
				}
				
			}
			alpha*=.01;
		}
		if(args.length>1){
			java.io.File testD;
			testD=new java.io.File(args[1]);
			if(testD.isDirectory()){
				List <String> testF=new ArrayList<String>();
				String [] testFiles=testD.list();
				for(String f:testFiles){
					testD=new java.io.File(args[1]+"/"+f);
					try {
						String st;
						Scanner s1=new Scanner(testD);
						st=s1.nextLine();
						while(s1.hasNextLine()){
							st.concat(s1.nextLine());
						}
						testF.add(st);
						s1.close();
					} catch (FileNotFoundException e) {
					}
				}
				PrintWriter p1=null;
				try {
					 p1=new PrintWriter("./data/results.txt");
				} catch (FileNotFoundException e) {
				}
				Perceptron testP=new Perceptron(testF,"ts");
				for(String f:testP.FilesToLearn){
					String [] g=f.split(" ");
					int totalCount=0;
					for(String d:g){
						for(wordRecord q:b){
							if(d==q.word){
								q.count+=1;
							}
						}
						totalCount+=1;
					}
					for(wordRecord d:b){
						d.countP=((float)d.countP)/totalCount;
					}
					boolean drV=myPerceptrons.get(0).vote(b);
					boolean dtV=myPerceptrons.get(1).vote(b);
					boolean lV=myPerceptrons.get(2).vote(b);
					if(drV&&!dtV&!lV){
						p1.println(f+" "+"dr");
					}
					else if(!drV&&dtV&!lV){
						p1.println(f+" "+"dt");
					}
					else if(!drV&&!dtV&&lV){
						p1.println(f+" "+"l");
					}
					else{
						while(true){
							int ranChoice=(int)(Math.random()*3.0);
							if(ranChoice==1&&drV){
								p1.println(f+" "+"dr");
								break;
							}
							else if(ranChoice==2&&dtV){
								p1.println(f+" "+"dt");
								break;
							}
							else if(ranChoice==3&&lV){
								p1.println(f+" "+"l");
								break;
							}
						}
					}
					for(wordRecord j:b){
						j.count=0;
					}
					
				}
				p1.close();
			}
		}
	}

}
