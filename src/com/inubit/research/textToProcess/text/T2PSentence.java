package com.inubit.research.textToProcess.text;

import java.util.Collection;
import java.util.List;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.Sentence;
import edu.stanford.nlp.ling.Word;
import edu.stanford.nlp.process.PTBTokenizer;
import edu.stanford.nlp.trees.GrammaticalStructure;
import edu.stanford.nlp.trees.Tree;


/**
 * Simple Data structure, based on the Stanford sentence. NOT ANYMORE
 * Sentence in the CoreNLP provides static util methods.
 * Adds a unique ID to enable tracing
 * @author ff
 *
 */
public class T2PSentence {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4145260396296512358L;
	private int f_id = createID();
	private Tree f_tree; //syntax Tree
	private GrammaticalStructure f_gramStruc; //dependencies
	private int f_offset;
	private int sID;
	private String theSentence;
	private List<CoreLabel> wordTokens;
	
	

	/**
	 * Constructs an empty sentence.
	 */
	public T2PSentence(String s, List<CoreLabel> wordTokens, int sID) {
//		super();
		theSentence = s;
		this.sID = sID;
		this.wordTokens = wordTokens;
	}

	/**
	 * Constructs an empty list with the specified initial capacity.
	 *
	 * @param initialCapacity The initial sentence allocation size
	 */
	public T2PSentence(int initialCapacity) {
//		super(initialCapacity);
	}


	/**
	 * Constructs a sentence from the input Collection.
	 *
	 * @param w A Collection (interpreted as ordered) to make the sentence
	 *          out of.
	 */
	public T2PSentence(Collection<Word> w) {
//		super(w);
	}

	public CoreLabel getWordToken(int i) {
		return wordTokens.get(i);
	}

	public void setWordTokens(List<CoreLabel> wordTokens) {
		this.wordTokens = wordTokens;
	}
	
	public int size() {
		return wordTokens.size();
	}
	
	public int getID(){
		return f_id;
	}
	
	public int getSentenceID(){
		return sID;
	}
	
//	/**
//	 * gets the length of the sentence (in characters)
//	 * @return
//	 */
//	public int getCharLength() {
//		return getEndPosition() - getBeginPosition();
//				
//	}
//	
//	/**
//	 * return the position of the first letter of the first word in the original text.
//	 * @return
//	 */
//	public int getBeginPosition() {
//		if(size() > 0) {
//			return this.get(0).beginPosition()-f_offset;
//		}
//		return -1;	
//	}
//	
//	/**
//	 * returns the position of the last letter of the last word in the original text.
//	 * @return
//	 */
//	public int getEndPosition() {
//		if(size() > 0) {
//			return this.get(this.size()-1).endPosition()-f_offset;
//		}
//		return -1;		
//	}
	
	public String toStringFormated() {
		return PTBTokenizer.ptb2Text(toString());
	}
	
	/**
	 * returns the Stanford dependency tree
	 * @return
	 */
	public GrammaticalStructure getGrammaticalStructure() {
		return f_gramStruc;
	}
	
	/**
	 * returns the Stanford syntax tree
	 * @return
	 */
	public Tree getTree() {
		return f_tree;
	}
	

	/**
	 * sets the grammatical structure containing the stanford 
	 * dependencies
	 * @param _gs
	 */
	public void setGrammaticalStructure(GrammaticalStructure gs) {
		f_gramStruc = gs;
	}	
	
	
	/**
	 * Here the stanford syntax tree is set
	 * @param _parse
	 */
	public void setTree(Tree tree) {
		f_tree = tree;
	}
	
	/**
	 * Because of comment lines, the actual begin and end position of this sentence in a JtextField
	 * can differ
	 * @param sentenceOffset
	 */
	public void setCommentOffset(int sentenceOffset) {
		f_offset = sentenceOffset;
	}
	
	@Override
	public String toString() {
		System.out.println(theSentence);
		return super.toString();
	}
	
	
	
//	---------- a static id creation
	private static int f_lastID = 0;
	
	private static int createID(){
		return f_lastID++;
	}
	
	public static void resetIDs() {
		f_lastID = 0;
	}

	

	
}
