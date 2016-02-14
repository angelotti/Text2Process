package angelos.thesis.textToProcess;

public class HRFunctional {
	String[][] hrFabian = new String[][]{
		//fabian - human|system
		{"report vacancy", "report the vacancy to the personnel department"},
		{"review job description", "wait to get the job description"},
		{"ask for corrections", "ask again for corrections"},
		{"approve job description", "approve the job description"}
	};
	
	String[][] hrS0 = new String[][]{
		//s0 - human|system
		{"report vacancy", "report the vacancy to the personnel department"},
		{"review job description", "wait to get the job description"},
		{"ask for corrections", "ask again for corrections"},
		{"approve job description", "approve the job description"}
	};
	
	String[][] hrS1 = new String[][]{
		//s0 - human|system
		{"report vacancy", "report the vacancy to the personnel department"},
		{"review job description", "wait to get the job description"},
		{"ask for corrections", "ask again for corrections"},
		{"approve job description", "approve the job description"}
	};
	
	public HRFunctional(){
		System.out.println("SIM | fabian: "+GraphSimilarity.graphEditSimilarity(3, 19, hrFabian, 4, 5, 8, 17, 11));
		
		System.out.println("SIM | s0: "+  GraphSimilarity.graphEditSimilarity(5, 20, hrS0, 4, 5, 10, 17, 13) );
		
		System.out.println("SIM | s1: "+GraphSimilarity.graphEditSimilarity(4, 19, hrS1, 4, 5, 9, 17, 12));
	}


}
