package angelos.thesis.textToProcess;
import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import com.inubit.research.textToProcess.text.Text;
import com.inubit.research.textToProcess.transform.SearchUtils;

import edu.stanford.nlp.dcoref.CorefChain;
import edu.stanford.nlp.dcoref.CorefCoreAnnotations;
import edu.stanford.nlp.ie.util.RelationTriple;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.naturalli.NaturalLogicAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations.CollapsedCCProcessedDependenciesAnnotation;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations.CollapsedDependenciesAnnotation;
import edu.stanford.nlp.semgraph.SemanticGraphEdge;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeCoreAnnotations;
import edu.stanford.nlp.trees.tregex.TregexMatcher;
import edu.stanford.nlp.trees.tregex.TregexPattern;
import edu.stanford.nlp.trees.tregex.tsurgeon.Tsurgeon;
import edu.stanford.nlp.trees.tregex.tsurgeon.TsurgeonPattern;
import edu.stanford.nlp.util.CoreMap;

public class Main {

	public static void main(String[] args) {
		
//		Main.test();

		PrintWriter out = new PrintWriter(System.out);

		//		 In comments we show how you can build a particular pipeline
				Properties props = new Properties();
//				props.put("annotators", "tokenize, ssplit, parse, depparse");
				props.setProperty("annotators", "tokenize,ssplit,parse,pos,lemma,depparse,natlog,openie");
//				props.put("annotators", "tokenize, ssplit, parse,lemma,ner,dcoref");
//				props.put("annotators", "tokenize, ssplit, pos, lemma, ner, depparse");
//				props.put("ner.model", "edu/stanford/nlp/models/ner/english.all.3class.distsim.crf.ser.gz");
		//		props.put("ner.applyNumericClassifiers", "false");
				props.put("parse.model", "edu/stanford/nlp/models/lexparser/englishFactored.ser.gz");
				props.put("parse.originalDependencies", "true");
				props.put("dcoref.score","true");
		StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

		// Initialize an Annotation with some text to be annotated.
		Annotation annotation, annot2;
		annotation = new Annotation("Kosgi Santosh sent an email to Stanford University. He didn't get a reply. ");
		annot2 = new Annotation("#First line is a comment.\n"
				+ "A customer brings in a defective computer and"
				+ " the CRS checks the defect and hands out a repair cost calculation back."
				+ " If the customer decides that the costs are acceptable,"
				+ " the process continues, otherwise she takes her computer"
				+ " home unrepaired."
				+ " The ongoing repair consists of two activities, which are executed, in an arbitrary order."
				+ " The first activity is to check and repair the hardware, whereas the second activity checks"
				+ " and configures the software. After each of these activities, the proper system functionality"
				+ " is tested. If an error is detected another arbitrary repair activity is executed, otherwise "
				+ "the repair is finished.");
		// run all the selected Annotators on this text
		pipeline.annotate(annot2);

		pipeline.prettyPrint(annot2, out);

		PrintWriter out2 = new PrintWriter(System.out);

		// An Annotation is a Map and you can get and use the various analyses individually.
		// For instance, this gets the parse tree of the first sentence in the text.
		List<CoreMap> sentences = annot2.get(CoreAnnotations.SentencesAnnotation.class);
		if (sentences != null && ! sentences.isEmpty()) {
			if(sentences.get(0).get(CoreAnnotations.TokensAnnotation.class).get(0).value().startsWith("#")){
				System.out.println("Starts with a comment");
				sentences.remove(0);
			}
			for(CoreMap s : sentences){
				// Get the OpenIE triples for the sentence
			      Collection<RelationTriple> triples = s.get(NaturalLogicAnnotations.RelationTriplesAnnotation.class);
			      // Print the triples
			      for (RelationTriple triple : triples) {
			        System.out.println(triple.confidence + "\t" +
			            triple.subjectLemmaGloss() + "\t" +
			            triple.relationLemmaGloss() + "\t" +
			            triple.objectLemmaGloss());
			      }
				System.out.println("------------++++++++++++++");
//				System.out.println(s.toShorterString());
				Tree tree = s.get(TreeCoreAnnotations.TreeAnnotation.class);
				SemanticGraph dependencies = s.get(CollapsedCCProcessedDependenciesAnnotation.class);
//				Tree root = tree.parent(tree);
				/*
				 * value() and nodeString() outputs the same string
				 * a node holds one value. either dependency name either the word from the sentence
				 * so when you have dep at index i, the word is on i+1
				 * tree.value() prints the root
				 * tree.getChild(0).value prints the first child of root/subtree
				 */
				System.out.println("Tree "+tree.toString());			
				Iterator it1 = tree.iterator();
				Tree t;
//				while(it1.hasNext()){
//					t = (Tree) it1.next();
//					
//					System.out.println("Iter "+t.nodeNumber(tree)+" "+t.value()+" ");
//					Tree parent = t.parent(tree);
//					if(t.isLeaf()){
//						System.out.println(parent.value()+"| "+parent.parent(tree)+"| "+parent.parent(tree).parent(tree));
//					}
//				}
//				Tree t1 = SearchUtils.findTreeNode(tree, "customer");
//				Tree t2 = SearchUtils.findParentParent(tree, "checks");
//				Tree t3 = SearchUtils.findParentNode(tree, "checks");
//				Tree t4 = SearchUtils.findParentNode(tree, t3.value());
//				System.out.println(t1.value()+"++"+t2.value());
//				System.out.println(t3.value()+"++"+t4.value());
				
//				while(!t1.value().equals("S")) {
//				for(int i=0;i<5;i++){
//					t1 = SearchUtils.findParentNode(tree, t1.value());
//				}
				
				System.out.println(tree.getChild(0).value());
				System.out.println(s.get(CoreAnnotations.TextAnnotation.class));
				System.out.println(s.get(CoreAnnotations.TokensAnnotation.class).size()+"| |"+s.size());
				for (CoreLabel token: s.get(CoreAnnotations.TokensAnnotation.class)) {
			        // this is the NER label of the token
//			        String ne = token.get(CoreAnnotations.NamedEntityTagAnnotation.class);       
//			        System.out.println("NER: value "+token.value()
//			        +"\noriginalText "+token.originalText()+""
//			        		+ "\ntoShorter"+token.toShorterString()
//			        		+ "\ntoString"+token.toString());
			      }
				ArrayList<Tree> pronouns = new ArrayList<Tree>();
//				TregexPattern tgrepPattern = TregexPattern.compile("S !<< S");
//				TregexMatcher m = tgrepPattern.matcher(tree);
//				while (m.find()) {
//				    Tree subtree = m.getMatch();
//				    pronouns.add(subtree);
//				    TregexPattern pat = TregexPattern.compile("NP=n1 !<< NP");
//				    TsurgeonPattern surgery = Tsurgeon.parseOperation("prune n1");
//				    System.out.println("\nAFTER DELETE\n");
//				    Tsurgeon.processPattern(pat, surgery, tree).pennPrint();

//				}
//				System.out.println("^^^^^^^^^^^^^pronouns\n size:"+pronouns.size()+"\n"+pronouns);
			}
		}
		
		System.out.println("\nREAL EXAMPLE TEST\n");
//		pipeline.annotate(annot2);
//		pipeline.prettyPrint(annot2, out);


	}
	
	public static void test(){
		CoreNlpWrapper cnw = new CoreNlpWrapper();
		File f = new File("C:\\Users\\aggelos\\Documents\\TestData\\HU\\Ex2 - Computer Repair.txt");
		Text text = cnw.createText(f,null);
		System.out.println(text.getSentences().size()+"\n____________________________\n");
	}
	
	public static void testFindParent(){
		CoreNlpWrapper cnw = new CoreNlpWrapper();
		File f = new File("C:\\Users\\aggelos\\Documents\\TestData\\HU\\Ex2 - Computer Repair.txt");
		Text text = cnw.createText(f,null);
		System.out.println(text.getSentences().size()+"\n____________________________\n");
		
	}
	
//	public void findSubSentences(CoreMap sentence, ArrayList<Tree> subSentences) {
//		Tree tree = sentence.get(TreeCoreAnnotations.TreeAnnotation.class);
////		ArrayList<Tree> subtree = new ArrayList<Tree>();
////		TregexPattern tgrepPattern = TregexPattern.compile("SBAR !<< SBAR");
//		TregexMatcher m = tgrepPattern.matcher(tree);
//		if (m.find()) {
//		    Tree subtree = m.getMatch();
//		    subSentences.add(subtree);
//		    TsurgeonPattern surgery = Tsurgeon.parseOperation("delete");
//		    System.out.println("\nAFTER DELETE\n");
//		    Tsurgeon.processPattern(tgrepPattern, surgery, tree).pennPrint();
//		}
//		
//	}


}

//for each sentence you can get a list of collapsed dependencies as follows
//CoreMap sentence = sentences.get(0);
//SemanticGraph dependencies1 = sentence.get(CollapsedDependenciesAnnotation.class);
//String dep_type = "CollapsedDependenciesAnnotation";
//System.out.println(dep_type+" ===>>");
//System.out.println("Sentence: "+sentence.toString());
//System.out.println("DEPENDENCIES: "+dependencies1.toList());
//System.out.println("DEPENDENCIES SIZE: "+dependencies1.size());
//Set<SemanticGraphEdge> edge_set1 = dependencies1.getEdgeSet();
//int j=0;
//for(SemanticGraphEdge edge : edge_set1)
//{
//	j++;
//	System.out.println("------EDGE DEPENDENCY: "+j);
//	Iterator<SemanticGraphEdge> it = edge_set1.iterator();
//	IndexedWord dep = edge.getDependent();
//	String dependent = dep.word();
//	int dependent_index = dep.index();
//	IndexedWord gov = edge.getGovernor();
//	String governor = gov.word();
//	int governor_index = gov.index();
//	GrammaticalRelation relation = edge.getRelation();
//	System.out.println("No:"+j+" Relation: "+relation.toString()+" Dependent ID: "+dependent.index()+" Dependent: "+dependent.toString()+" Governor ID: "+governor.index()+" Governor: "+governor.toString());
//
//}//end of for
