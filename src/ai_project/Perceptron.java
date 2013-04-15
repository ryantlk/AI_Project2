package ai_project;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Arrays;
import java.util.*;


public class Perceptron {
		List<String> FilesToLearn;
		List <wordRecord> bagOfWords2;
		String [] myBag;
		int total=0;
		double [] weights;
		String classification;
		
		public Perceptron(List<String> f,String s){
			FilesToLearn=f;
			classification=s;
		}
		
		public void preprocess(){
			//first step do normal preprocessing
			for(String f:FilesToLearn){
				//conver all non alpha,non space chars to spaces
				char [] fr=f.toCharArray();
				for(int i=0;i<fr.length;i++){
					if(!(Character.isLetter(fr[i]))&&fr[i]!=' '){
						fr[i]=' ';
					}
				}
				f=String.valueOf(fr);
				//set everything to lower case
				f=f.toLowerCase();
				
				//parse everything at space, and put everything together, this will remove extra spaces
				String [] g=f.split(" ");
				f=g[0];
				boolean hh=false;
				for(String n:g){
					if(hh){
						f.concat(" "+n);
					}
					hh=true;
				}
			}
			//get word counts
			boolean toadd=true;
			List<wordRecord> bagOfWords=new ArrayList<wordRecord>();
			for(String f:FilesToLearn){
				for(wordRecord z:bagOfWords){
					if(f==z.word){
						toadd=false;
						z.count++;
					}
				}
				if(toadd){
					bagOfWords.add(new wordRecord(f));
				}
				toadd=true;
			}
			//calculate total words
			int totalCount=0;
			for(wordRecord f:bagOfWords){
				totalCount+=f.count;
			}
			total=totalCount;
			//get p(w/c)
			for(wordRecord f:bagOfWords){
				f.countP=((float)f.count)/((float)totalCount);
			}
			//this is kinda ugly but i am putting the word records into an array and is sorting by p(w/c)
			Object [] p=bagOfWords.toArray();
			wordRecord [] pp=new wordRecord[p.length];
			for(int i=0;i<p.length;i++){
				pp[i]=(wordRecord)p[i];
			}
			Arrays.sort(pp,new Comparator<Object>(){ public int compare(Object o1,Object o2){
						wordRecord w1=(wordRecord)o1; wordRecord w2=(wordRecord) o2;
								if(w1.countP>w2.countP)return 1; else if(w1.countP>w2.countP) return -1;
								else return 0;};});
			//grab to 20 words, or as many as we can
			int wordC=0;
			bagOfWords2=new ArrayList<wordRecord>();
			for(int i=pp.length-1;i>=0;i--){
				bagOfWords2.add(pp[i]);
				wordC++;
				if(wordC==20)
					break;
			}
		
		}
		
		public void setupbag(List<String> bag){
			myBag=new String[bag.size()];
			weights=new double[bag.size()];
			for(int i=0;i<myBag.length;i++){
				myBag[i]=bag.get(i);
				weights[i]=0.6;
			}
		}
		
		public void train(wordRecord[] wordList, String c, double alpha){
			boolean results=vote(wordList);
			int error=0;
			if(results&&(c!=classification)){
				error=1;	
			}
			if(!results&&(c==classification)){
				error=-1;
			}
			if(error!=0){
				for(int i=0;i<weights.length;i++){
					weights[i]=weights[i]+alpha*error+wordList[i].countP;
				}
			}
		}
		
		public boolean vote(wordRecord [] wordList){
			int sum=0;
			for(int i=0;i<wordList.length;i++){
				sum+=weights[i]*wordList[i].countP;
			}
			if(sum>0){
				return true;
			}
			return false;
		}
		
}
