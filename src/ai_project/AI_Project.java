/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ai_project;

import java.io.IOException;

/**
 *
 * @author ryantlk
 */
public class AI_Project {

	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) throws IOException {
		if(args.length != 4){
			System.out.println("Incorrect argument count Usage: java -jar AI_Project" 
					+ " <path1> <path2> <path3> <testpath>");
			System.exit(1);
		}
		 
		Intelli_Grep grepTest = new Intelli_Grep(args[3]);
		grepTest.classify();
		
		Naive_Bayes bayesTest = new Naive_Bayes(args[0], args[1], args[2], args[3]);
		bayesTest.train();
		bayesTest.Test();
		
		Naive_Bayes_Improved bayesImpTest = new Naive_Bayes_Improved(args[0], args[1], args[2], args[3]);
		bayesImpTest.train();
		bayesImpTest.Test();
		
		//results outputed to file results.txt in current directory
		Percp testPercep=new Percp(args[0],args[1],args[2],args[3]);
		//results outputed to file resultsV2.txt in current directory
		PercepV2 testPercep2=new PercepV2(args[0],args[1],args[2],args[3]);
		
		MVNB m = new MVNB(args[0], args[1], args[2], args[3]);
		m.train();
		m.test();

		MVNBV2 m2 = new MVNBV2(args[0], args[1], args[2], args[3]);
		m2.setNumEntries(35);
		m2.train();
		m2.test();
	}
}
