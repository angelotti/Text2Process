package com.inubit.research.textToProcess.processing;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;

import com.inubit.research.textToProcess.text.T2PSentence;
import com.inubit.research.textToProcess.text.Text;

import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.ling.Word;
import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import edu.stanford.nlp.parser.lexparser.Test;
import edu.stanford.nlp.process.DocumentPreprocessor;
import edu.stanford.nlp.trees.GrammaticalStructure;
import edu.stanford.nlp.trees.GrammaticalStructureFactory;
import edu.stanford.nlp.trees.PennTreebankLanguagePack;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreebankLanguagePack;

/**
 * A Wrapper for the calls to the stanford API 
 * @author ff
 *
 */
public class T2PStanfordWrapper {
	
	private DocumentPreprocessor f_dpp = new DocumentPreprocessor();
	private LexicalizedParser f_parser;
	private TreebankLanguagePack f_tlp;
    private GrammaticalStructureFactory f_gsf;
	/**
	 * 
	 */
	public T2PStanfordWrapper() {
		try {
			ObjectInputStream in;
		    InputStream is;
		    URL u = T2PStanfordWrapper.class.getResource("/englishFactored.ser.gz");
		    if(u == null){
		    	//opening from IDE
		    	is = new FileInputStream(new File("resources/englishFactored.ser.gz"));		    		    	
		    }else{
		    	//opening from jar
		    	URLConnection uc = u.openConnection();
			    is = uc.getInputStream(); 				    
		    }
//		    Exception aux = new Exception("---------------T2PStanfordWrapper\n"); // not for throwing!
//			aux.printStackTrace(); // if you want it in stdout
		    in = new ObjectInputStream(new GZIPInputStream(new BufferedInputStream(is)));  
		    f_parser = new LexicalizedParser(in);
			f_tlp = new PennTreebankLanguagePack(); //new ChineseTreebankLanguagePack();
		    f_gsf = f_tlp.grammaticalStructureFactory();
		}catch(Exception ex) {
			ex.printStackTrace();
		}	    
		//option flags as in the Parser example, but without maxlength
		f_parser.setOptionFlags(new String[]{"-retainTmpSubcategories"});				
		//f_parser.setOptionFlags(new String[]{"-segmentMarkov"});				
		Test.MAX_ITEMS = 4000000; //enables parsing of long sentences
	}
	
	public Text createText(File f){
		return createText(f,null);
	}
	
	/**
	 * AAcreates a Text. first gets the sentences from the parsed text through the DocumentPreprocessor object
	 * in fact he gets the sentences word by word and then builds a T2PSentence and stores it in _result.
	 * @param f
	 * @param listener
	 * @return
	 */
	public Text createText(File f, ITextParsingStatusListener listener){
		try{
			Text _result = new Text();
			List<List<? extends HasWord>> _sentences = f_dpp.getSentencesFromText(f.getAbsolutePath());
//			System.out.println("----------getSentencesFromText "+_sentences.toString());
//			BufferedReader _r = new BufferedReader(new InputStreamReader(new FileInputStream(f), "Unicode"));
//			ArrayList<List<? extends HasWord>> _sentences = new ArrayList<List<? extends HasWord>>();
//			String s;
//			while((s = _r.readLine()) != null) {
//				ArrayList<Word> sent = new ArrayList<Word>();
//				sent.add(new Word(s));
//				_sentences.add(sent);
//			}
			//AA this is because for some reason that listener is initialized with the number of sentences
			if(listener != null) listener.setNumberOfSentences(_sentences.size());
			int _sentenceNumber = 1;
			int sentenceOffset = 0;
			for(List<? extends HasWord> _sentence:_sentences){
				if(_sentence.get(0).word().equals("#")) {
					//comment line - skip
					if(listener != null) listener.sentenceParsed(_sentenceNumber++);
					sentenceOffset += ((Word)_sentence.get(_sentence.size()-1)).endPosition();
					continue;
				}
//				System.out.println("AAA sentence "+_sentence);
				ArrayList<Word> _list = new ArrayList<Word>();
				for(HasWord w:_sentence){
					if(w instanceof Word){
						System.out.println("WORD  dsf "+w.toString());
						_list.add((Word) w);
					}else{
						System.out.println("Error occured while creating a Word!");
					}
				}
				T2PSentence _s = createSentence(_list);
//				System.out.println("SENTENCE  "+_s);
				_s.setCommentOffset(sentenceOffset);
				_result.addSentence(_s);	
				if(listener != null) listener.sentenceParsed(_sentenceNumber++);				
			}
			Exception aux = new Exception("---------------createText\n"); // not for throwing!
			aux.printStackTrace(); // if you want it in stdout
			return _result;
		}catch(Exception ex){
			System.out.println("Could not load file: "+f.getPath());
			ex.printStackTrace();
			return null;
		}
	}

	/**
	 * takes list of Words and applies the stanford parser to extract the dependencies. its like using coreNLP
	 * as fas as I know. every sentence holds all of the necessary grammatical syntactical info.
	 * @param _list
	 * @return
	 */
	private T2PSentence createSentence(ArrayList<Word> _list) {
		T2PSentence _s = new T2PSentence(_list);
		Tree _parse = f_parser.apply(_s);
		_s.setTree(_parse);
		GrammaticalStructure _gs = f_gsf.newGrammaticalStructure(_parse);
		_s.setGrammaticalStructure(_gs);
		return _s;
	}

}
