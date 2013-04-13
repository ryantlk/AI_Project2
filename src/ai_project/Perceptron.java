import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Arrays;


public class Perceptron {
		List<String> FilesToLearn;
		List <wordRecord> bagOfWords2;
		int total=0;
		public Perceptron(List<String> f){
			FilesToLearn=f;
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
				f.toLowerCase();
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
		public void finalizeBag(List<String> bag){
			boolean toadd=true;
			for(String f:bag){
				for(wordRecord z:bagOfWords2){
					if(z.word==f){
						toadd=false;
					}
				}
				if(toadd){
					wordRecord r=new wordRecord(f);
					r.countP=((float)1.0)/((float)total);
					bagOfWords2.add(r);
					
				}
				toadd=true;
			}
		}
}
