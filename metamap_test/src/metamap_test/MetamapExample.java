package metamap_test;
import gov.nih.nlm.nls.metamap.Ev;
import gov.nih.nlm.nls.metamap.Mapping;
import gov.nih.nlm.nls.metamap.MetaMapApi;
import gov.nih.nlm.nls.metamap.MetaMapApiImpl;
import gov.nih.nlm.nls.metamap.PCM;
import gov.nih.nlm.nls.metamap.Position;
import gov.nih.nlm.nls.metamap.Result;
import gov.nih.nlm.nls.metamap.Utterance;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;

public class MetamapExample {
	
	public static String getPhrase(String text, List<Position> list) {
		Position tmp = list.get(0);
		int start = tmp.getX();
		int end = tmp.getX() + tmp.getY();
		
		String result = text.substring(start, end);
		return result;
	}
	
	public static String getPositions(List<Position> list) {
		Position tmp = list.get(0);
		int start = tmp.getX();
		int end = tmp.getX() + tmp.getY();
		
		String result = start + "\t" + end;
		return result;
	}

/*	public static String getDictionaryTypes(List<String> list) {
		String[] semantic_Types = {"acab", "anab", "biof", "bpoc", "cgab", "clna", "comd", "dsyn", "emod", "fndg", 
				"hops", "inpo", "lbtr", "menp", "mobd", "neop", "patf", "phsf", "sosy", "tisu"};
//		String[] dictionary_Types = {"T020", "T190", "T038", "T023", "T019", "T201", "T049", "T047", "T050", "T033", 
//				"T131", "T037", "T034", "T041", "T048", "T191", "T046", "T039", "T184", "T024"};
		String[] dictionary_Types = {"Acquired Abnormality", "Anatomical Abnormality", "Biologic Function", 
				"Body Part, Organ, or Organ Component", "Congenital Abnormality", "Clinical Attribute", 
				"Cell or Molecular Dysfunction", "Disease or Syndrome", "Experimental Model of Disease", 
				"Finding", "Hazardous or Poisonous Substance", "Injury or Poisoning", "Laboratory or Test Result", 
				"Mental Process", "Mental or Behavioral Dysfunction", "Neoplastic Process", "Pathologic Function", 
				"Physiologic Function", "Sign or Symptom", "Tissue"};				
		
		for(int i = 0; i < semantic_Types.length; i++) {
			if(list.contains(semantic_Types[i])){
				String result = semantic_Types[i] + " [" + dictionary_Types[i] + "]"; 
				return result;
			}
		}
		return "None";
	}

	public static String getDictionaryTypes(List<String> list) {
		String[] semantic_Types = {"acab", "anab", "biof", "bpoc", "cgab", "clna", "comd", "dsyn", "emod", "fndg", 
				"hops", "inpo", "lbtr", "menp", "mobd", "neop", "patf", "phsf", "sosy", "tisu"};
		String[] dictionary_Types = {"Acquired Abnormality", "Anatomical Abnormality", "Biologic Function", 
				"Body Part, Organ, or Organ Component", "Congenital Abnormality", "Clinical Attribute", 
				"Cell or Molecular Dysfunction", "Disease or Syndrome", "Experimental Model of Disease", 
				"Finding", "Hazardous or Poisonous Substance", "Injury or Poisoning", "Laboratory or Test Result", 
				"Mental Process", "Mental or Behavioral Dysfunction", "Neoplastic Process", "Pathologic Function", 
				"Physiologic Function", "Sign or Symptom", "Tissue"};
		HashMap<String, String> map = new HashMap<String, String>();
		
		for(int i = 0; i < semantic_Types.length; i++) {
			map.put(semantic_Types[i], dictionary_Types[i]);			
		}
		
		String tmp = map.get(list.get(0));
		String result = list.get(0) + " [" + tmp + "]"; 
		return result;
	}
	*/
	
	public static LinkedHashSet<String> metamap(String input) throws Exception {		
		LinkedHashSet<String> result = new LinkedHashSet<String>();
		
		MetaMapApi api = new MetaMapApiImpl();
		api.setOptions("-y -k <aapp,acty,aggp,amas,amph,anim,anst,antb,arch,bacs,bact,bdsu,bdsy,bhvr,bird,blor,bmod,bodm,bsoj,carb,celc,celf,cell,chem,chvf,chvs,clas,clnd,cnce,crbs,diap,dora,drdd,edac,eehu,eico,elii,emst,enty,enzy,euka,evnt,famg,ffas,fish,fngs,food,ftcn,genf,geoa,gngm,gora,grpa,grup,hcpp,hcro,hlca,horm,humn,idcn,imft,inbe,inch,inpr,irda,lang,lbpr,lipd,mamm,mbrt,mcha,medd,mnob,moft,mosq,nnon,npop,nsba,nusq,ocac,ocdi,opco,orch,orga,orgf,orgm,orgt,ortf,phob,phpr,phsu,plnt,podg,popg,prog,pros,qlco,qnco,rcpt,rept,resa,resd,rnlw,sbst,shro,socb,spco,strd,tmco,topp,virs,vita,vtbt>");
		System.out.println("api instanciated");
		
//		String[] comma_split = input.split(",");
//		int comma_count = comma_split.length;
//		System.out.println(comma_count);
//		String subInput = "";
//		
//		for(int i=0 ; i < comma_count ; i++){
//			int countDividedFive = i % 5;
//			subInput = subInput + i + comma_split[i];
//			
//			if(countDividedFive == 4 || i == comma_count-1){
//				System.out.println(subInput);
//				subInput = "";
//			}
//			
//			
//			
//		}
		
		List<Result> resultList = api.processCitationsFromString(input);
		
		
		Result NER_result = resultList.get(0);
		
		for (Utterance utterance : NER_result.getUtteranceList()) {
			System.out.println("Utterance:");
			System.out.println(" Utterance text: " + utterance.getString());
			for (PCM pcm : utterance.getPCMList()) {
				for (Mapping map : pcm.getMappingList()) {					
					for (Ev mapEv : map.getEvList()) {					
						String phenotype_name = getPhrase(utterance.getString(), mapEv.getPositionalInfo());
						String UMLS_ID = mapEv.getConceptId();
//						String UMLS_Symantic_type = getDictionaryTypes(mapEv.getSemanticTypes());
						String UMLS_Symantic_type = mapEv.getSemanticTypes().get(0);
						String start_end = getPositions(mapEv.getPositionalInfo());
						/*
						System.out.println("  Phrase: " + phenotype_name);
						System.out.println("  Concept Id: " + UMLS_ID);
						System.out.println("  Semantic Types: " + UMLS_Symantic_type);
						System.out.println("  Positional Info: " + start_end);
						*/
						String tmp = phenotype_name + "\t" + UMLS_ID + "\t" + UMLS_Symantic_type + "\t" + start_end;
						System.out.println(tmp);
						result.add(tmp);
					}
				}
			}
		}
		return result;
		}
	
	
	public static void main(String[] args) throws Exception {	
		String input = "Lycoris radiata	To dispel phlegm and promote vomiting, resolve toxin and dissipate binds. / Poliomyelitis, muscle weakness, rheumatic arthritis, pain in joints due to rheumatalgia, throat wind, nipple moth, swelling pain in throat, phlegm-drool and congesting lung, food poisoning, seeper in chest and abdomen, malign sore and swelling toxin, hemorrhoids and fistulas, knocks and falls, intractable lichen, burns and scalds, snake bite.";

		LinkedHashSet<String> result = metamap(input);
	}
}