package angelos.thesis.textToProcess;

public class Turbopixel {
	String[][] turboFabian = new String[][]{
		//fabian - human|system
		{"buy data", "buy details for cold calls"},
		{"vist exhibition", "participate in exhibitions"},
		{"other sources", "happen to know somebody"},
		{"determine contact person", "determine the contact person"},
		{"determine available project budget", "determine the budget"},
		{"contact customer", "try to acquire the customer"},
		{"sales presentation", "give a detailed online presentation"},
		{"technical presentation", "give a detailed online presentation"},
		
		{"create a quotation", "create a quotation"}
	};
	
	String[][] turboS0 = new String[][]{
		//fabian - human|system
		{"other sources", "happen to know somebody"},
		{"vist exhibition", "participate in exhibitions"},
		{"buy data", "buy details for cold calls"},
		{"determine contact person", "determine the contact person"},
		{"determine available project budget", "determine the budget"},
		{"contact customer", "try to acquire the customer"},
		{"sales presentation", "give it"},
		{"technical presentation", "give it"},
		
		{"create a quotation", "create a quotation"}
	};
	
	String[][] turboS1 = new String[][]{
		//fabian - human|system
		{"other sources", "happen to know somebody"},
		{"vist exhibition", "participate in exhibitions"},
		{"buy data", "buy details for cold calls"},
		{"determine contact person", "determine the contact person"},
		{"determine available project budget", "determine the budget for the project"},
		{"contact customer", "try to acquire the customer"},
		{"technical presentation", "give a technical presentation"},
		
		{"create a quotation", "create a quotation"},
		{"create a quotation", "create a quotation"}
	};
	
	public Turbopixel(){
		System.out.println("SIM | fabian: "+GraphSimilarity.graphEditSimilarity(9, 49, turboFabian, 9, 10, 17, 30, 37));		
		System.out.println("SIM | s0: "+    GraphSimilarity.graphEditSimilarity(11, 44, turboS0, 9, 10, 19, 30, 36));		
		System.out.println("SIM | s1: "+    GraphSimilarity.graphEditSimilarity(4, 39, turboS1, 9, 10, 14, 30, 28));
	}

}
