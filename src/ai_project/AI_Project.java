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
	}
}
