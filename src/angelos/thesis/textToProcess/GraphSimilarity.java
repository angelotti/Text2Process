package angelos.thesis.textToProcess;
/**
 * 
 * @author aggelos
 * edit distance code: http://www.programcreek.com/2013/12/edit-distance-in-java/
 *
 */
public class GraphSimilarity {

	public static void main(String[] args) {
//		int dis = minDistance("me", "map");

//		System.out.println("Sim: "+syntaxSim("meaaa", "map"));
		System.out.println("Comp"+new ComputerRepair());
		System.out.println("HR"+new HRFunctional());
		System.out.println("Turbo"+new Turbopixel());


	}
	/**
	 * 
	 * @param sn, number of nodes not mapped
	 * @param se, number of edges not mapped
	 * @param mappedNodes, the string of the labels of mapped nodes
	 * @param rows, number of rows of the mappedNodes array
	 * @param N1, number of nodes of model1
	 * @param N2, number of nodes of model2
	 * @param E1, number of edges of model1
	 * @param E2, number of edges of model2
	 * @return
	 */
	
	public static double graphEditSimilarity(int sn, int se, String[][] mappedNodes,
												int rows, int N1, int N2, int E1, int E2){
		double sim,w1,w2,w3,ged; // weights
		w1 = 0.4;
		w2 = w3 = 0.30;
		ged = graphEditDistance(sn,se, mappedNodes,rows);
		double a,b,c;
		a = (w1*ged)/(N1+N2-sn);
		b = (w2*sn)/(N1+N2);
		c = (w3*se)/(E1+E2);
		System.out.println("abc: "+a+"| "+b+"| "+c+"| ");
		sim = 1 - (a+b+c);             
		return sim;
	}
	
	public static double graphEditDistance(int sn, int se, String[][] mappedNodes, int rows){
		double sum = 0;
		double gdis = 0;
		
		for(int i=0;i<rows;i++){
			sum = sum+1-syntaxSim(mappedNodes[i][0], mappedNodes[i][1]);
		}
		//		gdis = sn+se+2*sum; true edit distance
		gdis = sum; //this is the number we need for the calc of ged similarity
//		System.out.println("GraphEditDistance: "+gdis);
		return gdis;
	}
	
	public static double syntaxSim(String s1, String s2){
		double sim = 0;
		int len1 = s1.length();
		int len2 = s2.length();
		double maxLength = len1 > len2 ? len1 : len2; 
		sim = 1 - minDistance(s1,s2)/maxLength;
		return sim;
	}
	
	public static int minDistance(String word1, String word2) {
		int len1 = word1.length();
		int len2 = word2.length();
	 
		// len1+1, len2+1, because finally return dp[len1][len2]
		int[][] dp = new int[len1 + 1][len2 + 1];
	 
		for (int i = 0; i <= len1; i++) {
			dp[i][0] = i;
		}
	 
		for (int j = 0; j <= len2; j++) {
			dp[0][j] = j;
		}
	 
		//iterate though, and check last char
		for (int i = 0; i < len1; i++) {
			char c1 = word1.charAt(i);
			for (int j = 0; j < len2; j++) {
				char c2 = word2.charAt(j);
	 
				//if last two chars equal
				if (c1 == c2) {
					//update dp value for +1 length
					dp[i + 1][j + 1] = dp[i][j];
				} else {
					int replace = dp[i][j] + 1;
					int insert = dp[i][j + 1] + 1;
					int delete = dp[i + 1][j] + 1;
	 
					int min = replace > insert ? insert : replace;
					min = delete > min ? min : delete;
					dp[i + 1][j + 1] = min;
				}
			}
		}
//		System.out.println("Edit Distance: "+dp[len1][len2]);
		return dp[len1][len2];
	}

}
