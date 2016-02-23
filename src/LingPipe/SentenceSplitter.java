package LingPipe;

import com.aliasi.sentences.MedlineSentenceModel;
import com.aliasi.sentences.SentenceModel;

import com.aliasi.tokenizer.IndoEuropeanTokenizerFactory;
import com.aliasi.tokenizer.TokenizerFactory;
import com.aliasi.tokenizer.Tokenizer;

import com.aliasi.util.Files;

import java.io.BufferedReader;
import java.io.File;
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
	

	public LinkedHashMap<String, LinkedHashSet<String>> Splitter(File FunctionTextFile) throws IOException {

		taggingTargetcontents();
		
		BufferedReader br = new BufferedReader(new FileReader(FunctionTextFile));
		String textLine = null;

		while ((textLine = br.readLine()) != null) {
			System.out.println(textLine);

			String[] textLine_split = textLine.split("\t");
			String EntityOneRef_ID = textLine_split[whereIsEntityOneID];
			String EntityOneName = textLine_split[whereIsEntityOneName];
			String targetContents = textLine_split[whereIsTarget].toLowerCase().trim();

			
			if (targetContents.equals("null")) {
			} else {
				split_allTypeText(EntityOneRef_ID, EntityOneName, TypeofTarget , targetContents, whatKindTarget);
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

	static void split_allTypeText(String EntityOneID, String EntityOneName, String dataType, String allType_text, String KindofText) {

		LinkedHashSet<String> splitsentence = new LinkedHashSet<String>();

		List<String> tokenList = new ArrayList<String>();
		List<String> whiteList = new ArrayList<String>();
		Tokenizer tokenizer = TOKENIZER_FACTORY.tokenizer(allType_text.toCharArray(), 0, allType_text.length());
		tokenizer.tokenize(tokenList, whiteList);

		String[] tokens = new String[tokenList.size()];
		String[] whites = new String[whiteList.size()];
		tokenList.toArray(tokens);
		whiteList.toArray(whites);
		int[] sentenceBoundaries = SENTENCE_MODEL.boundaryIndices(tokens, whites);

		int sentStartTok = 0;
		int sentEndTok = 0;
		for (int i = 0; i < sentenceBoundaries.length; ++i) {
			String temp = "";
			sentEndTok = sentenceBoundaries[i];
			for (int j = sentStartTok; j <= sentEndTok; j++) {
				temp += tokens[j] + whites[j + 1];
			}
			sentStartTok = sentEndTok + 1;

			if (temp.contains("/")) {
				String[] temp_split = temp.split(" / ");
				for (String t : temp_split) {
					if (!t.trim().toLowerCase().equals("null")) {
						// remove phrase whose length is less than 3.
						if (t.length() < 3) {
							continue;
						}
						splitsentence.add(t.toLowerCase().trim());
					}
				}

			} else {
				// remove phrase whose length is less than 3.
				if (temp.length() < 3) {
					continue;
				}
				splitsentence.add(temp.toLowerCase().trim());
			}
		}

		if (sentences_map.containsKey(	KindofText + "\t" + dataType + "\t" + EntityOneID + "\t" + EntityOneName + "\t" + allType_text)) {
			sentences_map.put(
					KindofText + "\t" + dataType + "\t" + EntityOneID + "\t" + EntityOneName + "\t" + allType_text,
					splitsentence);
		} else {
			sentences_map.put(
					KindofText + "\t" + dataType + "\t" + EntityOneID + "\t" + EntityOneName + "\t" + allType_text,
					splitsentence);
		}
	}

}