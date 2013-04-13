import java.io.FileNotFoundException;
import java.util.*;


public class Percp {
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		List<String> Dr=new ArrayList<String>();
		List<String> Dt=new ArrayList<String>();
		List<String> L=new ArrayList<String>();
		List<String>bagOfWords=new ArrayList<String>();
		java.io.File h=new java.io.File("./data/DR");
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
		Perceptron drp=new Perceptron(Dr); 
		myPerceptrons.add(drp);
		Perceptron dtp=new Perceptron(Dt); 
		myPerceptrons.add(dtp);
		Perceptron lp=new Perceptron(L); 
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
		for(Perceptron f:myPerceptrons){
			f.
		}
	}
	
	public void prep(){
		
	}

}
