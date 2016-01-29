package angelos.thesis.textToProcess;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import com.inubit.research.textToProcess.processing.ITextParsingStatusListener;
import com.inubit.research.textToProcess.text.T2PSentence;
import com.inubit.research.textToProcess.text.Text;

import edu.stanford.nlp.dcoref.CorefChain;
import edu.stanford.nlp.dcoref.CorefCoreAnnotations;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.trees.GrammaticalStructure;
import edu.stanford.nlp.trees.GrammaticalStructureFactory;
import edu.stanford.nlp.trees.PennTreebankLanguagePack;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeCoreAnnotations;
import edu.stanford.nlp.trees.TreebankLanguagePack;
import edu.stanford.nlp.util.CoreMap;

public class CoreNlpWrapper {

	final static Charset ENCODING = StandardCharsets.UTF_8;
	private Annotation annotation; 
	private TreebankLanguagePack f_tlp;
	private GrammaticalStructureFactory f_gsf;

	public CoreNlpWrapper() {


	}

	public Text createText(File f){
		return createText(f,null);
	}

	public Text createText(File f, ITextParsingStatusListener listener){
		Text _result = new Text();
		StanfordCoreNLP pipeline = initPipeline();
		String toAnnotate = readFile(f);
		Annotation annot = new Annotation(toAnnotate);
		pipeline.annotate(annot);

		List<CoreMap> sentences = annot.get(CoreAnnotations.SentencesAnnotation.class);
		// the next 3 lines are used for the progress bar while processing
		if(listener != null) listener.setNumberOfSentences(sentences.size());
		int _sentenceNumber = 1;
		if(listener != null) listener.sentenceParsed(_sentenceNumber);
		System.out.println(sentences.size());
		if (sentences != null && ! sentences.isEmpty()) {
			if(sentences.get(0).get(CoreAnnotations.TokensAnnotation.class).get(0).value().startsWith("#")){
				// System.out.println("Starts with a comment");
				sentences.remove(0);
				if(listener != null) listener.setNumberOfSentences(sentences.size());
			}
			
			for(int i=0; i<sentences.size(); i++){
				CoreMap s = sentences.get(i);
				T2PSentence _s = new T2PSentence(s.get(CoreAnnotations.TextAnnotation.class),
												 s.get(CoreAnnotations.TokensAnnotation.class), i+1); 
				Tree t = s.get(TreeCoreAnnotations.TreeAnnotation.class);
				_s.setTree(t);

				// set the grammatical structure. later we use typedDepeCollapsed
				f_tlp = new PennTreebankLanguagePack(); 
				f_tlp.setGenerateOriginalDependencies(true);
				f_gsf = f_tlp.grammaticalStructureFactory();
				GrammaticalStructure _gs = f_gsf.newGrammaticalStructure(t);
				_s.setGrammaticalStructure(_gs);

				System.out.println(_s.toString());
				_result.addSentence(_s);
				if(listener != null) listener.sentenceParsed(_sentenceNumber++);
			}

			Map<Integer, CorefChain> corefChains = annot.get(CorefCoreAnnotations.CorefChainAnnotation.class);
			if (corefChains != null) {
				_result.setCorefChains(corefChains);
			}
		}

		return _result;
	}


	/*
	 * creates and returns a pipeline
	 */

	public StanfordCoreNLP initPipeline() {
		Properties props = new Properties();
		props.put("annotators", "tokenize, ssplit, parse,lemma,ner,dcoref");
		props.put("parse.model", "edu/stanford/nlp/models/lexparser/englishFactored.ser.gz");
		StanfordCoreNLP pipe = new StanfordCoreNLP(props);
		return pipe;

	}

	/*
	 * reads the file selected from the gui and returns it as a String
	 */
	public String readFile(File f) {
		Path path = f.toPath();
		StringBuilder result = new StringBuilder();
		try (BufferedReader reader = Files.newBufferedReader(path, ENCODING)){
			String line = null;
			while ((line = reader.readLine()) != null) {
				//process each line in some way
				result.append(line);
			}      
		} catch (IOException e) {
			e.printStackTrace();
		}
		//		System.out.println("file read: "+result);
		return result.toString();
	}

	public static void printCorefGraph(Map<Integer, CorefChain> corefChains) {
		PrintWriter pw = new PrintWriter(System.out);
		if (corefChains != null) {
			for (CorefChain chain : corefChains.values()) {
				CorefChain.CorefMention representative = chain.getRepresentativeMention();
				boolean outputHeading = false;
				for (CorefChain.CorefMention mention : chain.getMentionsInTextualOrder()) {
					if (mention == representative)
						continue; //if the mention refers to itself skip
					if (!outputHeading) {
						outputHeading = true;
						pw.println("Coreference set:");
					}
					// all offsets start at 1!
					pw.printf("\t(%d,%d,[%d,%d]) -> (%d,%d,[%d,%d]), that is: \"%s\" -> \"%s\"%n",
							mention.sentNum,
							mention.headIndex,
							mention.startIndex,
							mention.endIndex,
							representative.sentNum,
							representative.headIndex,
							representative.startIndex,
							representative.endIndex,
							mention.mentionSpan,
							representative.mentionSpan);
				}
			}
		}
		pw.flush();
	}


}
