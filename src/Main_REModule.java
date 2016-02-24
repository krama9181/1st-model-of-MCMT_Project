import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;

import EdgeDetector.ExtractRelation;
import LingPipe.SentenceSplitter;
import NodeDetector.NodeDetection;

public class Main_REModule {
	
	static final double CHUNK_SCORE = 1.0;
	 
	
	///Remodel change////
	
	/*
	 * 
	 * File Path Declaration ( You should change the path )
	 * 
	 */
	static String path = "D:/JUN/MCMT/End_of_project/";
	
	static String InputTextFile = path + "raw_inputData/test_input.txt"; // input text  \
	static String AnnFileFolder = path + "annotated/"; // Folder path whose annotation files will be stored.
	static String UMLSDictionaryPath = path + "dictionary/UMLS_DICTIONARY_FOR_20_SEM.txt"; // UMLS dictionary path
	static String TriggerDictionaryPath = path + "dictionary/Integrated_Triggers.txt"; // Trigger dictionary path
	static String RelationResultOutputPath = path + "Output/bs_test_ouput2.tsv"; // Final output path
	
	public static void main(String[] args) throws Exception {
		////////
		createFolder();			
		SentenceSplitter splitter = new SentenceSplitter();
		NodeDetection node_detector = new NodeDetection();
		ExtractRelation relation = new ExtractRelation();

		/*
		 * 
		 * Enter Input Texts
		 * 
		 */
		File FunctionTextFile = new File(InputTextFile);

		/*
		 * 
		 * Sentence splitter
		 * 
		 */
		LinkedHashMap<String, LinkedHashSet<String>> splitSentence_for_each_line = new LinkedHashMap<String, LinkedHashSet<String>>();
		splitSentence_for_each_line = splitter.Splitter(FunctionTextFile);

		/*
		 * 
		 * Phenotype Detector, Trigger Detector
		 * 
		 */
		node_detector.NodeDectector(splitSentence_for_each_line, UMLSDictionaryPath, TriggerDictionaryPath,	InputTextFile, AnnFileFolder);
		/*
		 * 
		 * //Relation Extraction
		 * 
		 */
		LinkedHashSet<String> output = new LinkedHashSet<String>();
		File files = new File(RelationResultOutputPath); // print output
		relation.RelationDetector(AnnFileFolder, files);					
	}
	
	public static void createFolder() {
		File annotationFolder = new File(AnnFileFolder);
		if (!annotationFolder.exists())
			annotationFolder.mkdir();
		else
			System.out.println("Already exists!");
		
	}
}
