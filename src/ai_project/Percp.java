package ai_project;

import java.io.FileNotFoundException;
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
	}

}
