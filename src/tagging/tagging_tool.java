package tagging;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Scanner;

public class tagging_tool {
	//public LinkedHashMap<String, LinkedHashSet<String>> tagtool(File FunctionTextFile) throws IOException{
	public static void main(String[] args) {

		Scanner in = new Scanner(System.in);
		
		int whereIsTarget = 0;
		String whatKindTarget = "";
		String TypeofTarget = "";
		
		System.out.print("Where is Target contents? : ");
		whereIsTarget = in.nextInt();
		
		System.out.println("  if\t\twrite");
		System.out.println("sentence\tsent");
		System.out.println("phrase\t\tphra");
		System.out.print("What is the type of contents? : ");
		TypeofTarget = in.next();
		
		System.out.println("  if\t\twrite");
		System.out.println("Funtion\t\tfun");
		System.out.println("MOA\t\tmoa");
		System.out.println("Toxicity\ttox");
		System.out.print("What kind of contents? : ");
		whatKindTarget = in.next();

		
		//BufferedReader openfile = new BufferedReader(new FileReader (FunctionTextFile));
		
	//	while (openfile.readLine() != null){
			
		//}

		
	
	}
}
