package LingPipe;

import com.aliasi.sentences.MedlineSentenceModel;
import com.aliasi.sentences.SentenceModel;

import com.aliasi.tokenizer.IndoEuropeanTokenizerFactory;
import com.aliasi.tokenizer.TokenizerFactory;
import com.aliasi.tokenizer.Tokenizer;

import com.aliasi.util.Files;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

/** Use SentenceModel to find sentence boundaries in text */
public class SentenceSplitter {

	static final TokenizerFactory TOKENIZER_FACTORY = IndoEuropeanTokenizerFactory.INSTANCE;
	static final SentenceModel SENTENCE_MODEL = new MedlineSentenceModel();
	static LinkedHashMap<String, LinkedHashSet<String>> sentences_map = new LinkedHashMap<String, LinkedHashSet<String>>();

	static int whereIsEntityOneID = 0;
	static int whereIsEntityOneName = 0;
	static int whereIsTarget = 0;
	static String TypeofTarget = "";	//sent? or phra?
	static String whatKindTarget = "";	//function, MOA or Toxicity
	

	public static LinkedHashMap<String, LinkedHashSet<String>> Splitter(File FunctionTextFile) throws IOException {

		taggingTargetcontents();
		
		BufferedReader br = new BufferedReader(new FileReader(FunctionTextFile));
		String textLine = null;

		while ((textLine = br.readLine()) != null) {
			System.out.println(textLine);
			
//			String[] textLine_split = textLine.split("\t");
//			String EntityOneRef_ID = textLine_split[whereIsEntityOneID];
//			String EntityOneName = textLine_split[whereIsEntityOneName];
//			String targetContents = textLine_split[whereIsTarget].trim();
			
			String[] textLine_split = textLine.split("\t");
			String EntityOneRef_ID = "";
			String EntityOneName = "";
			String targetContents = "";

			EntityOneRef_ID = textLine_split[whereIsEntityOneID];
			EntityOneName = textLine_split[whereIsEntityOneName];
			targetContents = textLine_split[whereIsTarget].trim();

			if (targetContents.equals("null")) {
			} else {
				split_allTypeText(EntityOneRef_ID, EntityOneName, TypeofTarget, targetContents, whatKindTarget);
			}

		}
		
		return sentences_map;
	}

	static void taggingTargetcontents() {
		Scanner in = new Scanner(System.in);

		System.out.print("Where is the Entity One's Reference and ID? : ");
		whereIsEntityOneID = in.nextInt() -1 ;
		
		System.out.print("Where is the Entity One's Name? : ");
		whereIsEntityOneName = in.nextInt() -1 ;
		
		System.out.print("Where is Target contents? : ");
		whereIsTarget = in.nextInt() -1 ;

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
	}

	static void split_allTypeText(String EntityOneID, String EntityOneName, String dataType, String targetContents, String KindofText) {

		LinkedHashSet<String> splitsentence = new LinkedHashSet<String>();

		List<String> tokenList = new ArrayList<String>();
		List<String> whiteList = new ArrayList<String>();
		Tokenizer tokenizer = TOKENIZER_FACTORY.tokenizer(targetContents.toCharArray(), 0, targetContents.length());
		tokenizer.tokenize(tokenList, whiteList);

		String[] tokens = new String[tokenList.size()];
		String[] whites = new String[whiteList.size()];
		tokenList.toArray(tokens);
		whiteList.toArray(whites);
		int[] sentenceBoundaries = SENTENCE_MODEL.boundaryIndices(tokens, whites);

		int sentStartTok = 0;
		int sentEndTok = 0;
		
		
		
		if(dataType.equals("sent")){
			for (int i = 0; i < sentenceBoundaries.length; ++i) {
			    String temp = "";
				sentEndTok = sentenceBoundaries[i];
			    for (int j=sentStartTok; j <= sentEndTok; j++) {
			    	temp += tokens[j]+whites[j+1];
			    }
			    sentStartTok = sentEndTok+1;

			splitsentence.add(temp.toLowerCase().trim());
			}
		}
		
		else if (dataType.equals("phra")) {
			if (targetContents.contains("/")) {
				String[] temp_split = targetContents.split(" / ");
				for (String t : temp_split) {
					if (!t.trim().toLowerCase().equals("null") && t.contains(", ")) {
						String[] comma_split = t.split(",");

						for (int k = 0; k < comma_split.length-1; k++) {
							splitsentence.add("co,ma" + comma_split[k].trim().toLowerCase());
						}
						splitsentence.add(comma_split[comma_split.length-1].trim().toLowerCase());

					} else {
						splitsentence.add(t.trim().toLowerCase());
					}
				}
			} else {
				if(targetContents.contains(", ")){
					String[] comma_split = targetContents.split(", ");
					for (int k = 0; k < comma_split.length-1; k++) {
						splitsentence.add("co,ma" + comma_split[k].trim().toLowerCase());
					}
					splitsentence.add(comma_split[comma_split.length-1].trim().toLowerCase());
				}
				else{
					splitsentence.add(targetContents.trim().toLowerCase());
				}
			}
		
		}
		
		else{
		    System.out.println("You entered odd value of type of sentence");
		}
////////////////////////////////////////////////////////////////////////////////////////////////////
//			if (TypeofTarget.equals("phra")) {
//				if (temp.contains("/")) {
//					String[] temp_split = temp.split(" / ");
//					for (String t : temp_split) {
//						if (!t.trim().toLowerCase().equals("null")) {
//							// remove phrase whose length is less than 3.
//							if (t.length() < 3) {
//								continue;
//							}
//
//							String[] comma_split = t.split(",");
//							int comma_count = comma_split.length;
//
//							String subInput = "";
//
//							for (int j = 0; j < comma_count; j++) {
//								splitsentence.add(comma_split[j].toLowerCase().trim());
//							}
//						}
//					}
//
//				} else {
//					// remove phrase whose length is less than 3.
//					if (temp.length() < 3) {
//						continue;
//					}
//					String[] comma_split = temp.split(",");
//					int comma_count = comma_split.length;
//
//					if (comma_count > 5) {
//						for (int j = 0; j < comma_count; j++) {
//
//							splitsentence.add(comma_split[j].toLowerCase().trim());
//						}
//					}
//				}
//			} else {
//				splitsentence.add(temp.toLowerCase().trim());
//			}
		    
		
///////////////////////////////////////////////////////////////////////////////////		    
//		    if (temp.contains("/")) {
//				String[] temp_split = temp.split(" / ");
//				for (String t : temp_split) {
//					if (!t.trim().toLowerCase().equals("null")) {
//						// remove phrase whose length is less than 3.
//						if (t.length() < 3) {
//							continue;
//						}
//						splitsentence.add(t.toLowerCase().trim());
//					}
//				}
//			} else {
//				// remove phrase whose length is less than 3.
//				if (temp.length() < 3) {
//					continue;
//				}
//				splitsentence.add(temp.toLowerCase().trim());
//			}
		    
		    
//////////////////////////////////////////////////////////		    
		    
		    
		    
//			if (temp.contains("/") && TypeofTarget.equals("phra")) {
//				String[] temp_split = temp.split(" / ");
//				for (String t : temp_split) {
//					if (!t.trim().toLowerCase().equals("null")) {
//						// remove phrase whose length is less than 3.
//						if (t.length() < 3) {
//							continue;
//						}
//						
//						String[] comma_split = t.split(",");
//						int comma_count = comma_split.length;
//
//						String subInput = "";
//						if (comma_count > 5) {
//							for (int j = 0; j < comma_count; j++) {
//								int countDividedFive = j % 5;
//								subInput = subInput + comma_split[j];
//
//								if (countDividedFive == 4 || j == comma_count - 1) {
//									splitsentence.add(subInput.toLowerCase().trim());
//									subInput = "";
//								}
//							}
//						}
//						
//					}
//				}
//			} else {
//				// remove phrase whose length is less than 3.
//				if (temp.length() < 3) {
//					continue;
//				}
//				String[] comma_split = temp.split(",");
//				int comma_count = comma_split.length;
//				
//				String subInput = "";
//				if (comma_count > 5) {
//					for (int j = 0; j < comma_count; j++) {
//						int countDividedFive = j % 5;
//						subInput = subInput + comma_split[j];
//
//						if (countDividedFive == 4 || j == comma_count - 1) {
//							splitsentence.add(subInput.toLowerCase().trim());
//							subInput = "";
//						}
//					}
//				}
//			}
			
			
		sentences_map.put(	KindofText + "\t" + dataType + "\t" + EntityOneID + "\t" + EntityOneName + "\t" + targetContents,	splitsentence);
	}
}