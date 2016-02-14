package angelos.thesis.textToProcess;

public class ComputerRepair {
	String[][] ComputerRepairS2 = new String[][]{
		//scenario 2 - system|human
		{"bring in defective computer", "bring in a defective computer"},
		{"receive a repair cost calculation", "receive repair cost calculation"},
		{"prepare a repair cost calculation", "prepare repair cost calculation"},
		{"take computer home unrepaired", "take computer home"},
		{"begin", "continue process"},
		{"repair the hardware", "check and repair hardware"},
		{"check whereas the second activity checks and configure hardware", "check and configure software"},
		{"test the proper system", "test system functionality"},
		{"f1", "f1"},
		{"f2", "f2"},
		{"f3", "f3"},
		{"s1", "s1"}
	};
	
	String[][] ComputerRepairFabian = new String[][]{
		{"bring in defective computer", "bring in a defective computer"},
		{"take computer home unrepaired", "take computer home"},
		{"check the hardware", "check and repair hardware"},
		{"repair the hardware whereas the second activity checks", "check and configure software"},
		{"test the proper system functionality", "test system functionality"},
		{"f1", "f1"},
		{"f2", "f2"},
		{"f3", "f3"},
		{"s1", "s1"}
		
	};
	
	String[][] ComputerRepairS0 = new String[][]{
		// human|system
		{"bring in defective computer", "bring in a defective computer"},
		{"take computer home", "take computer home unrepaired"},
		{"continue process", "continue"},
		{"check and configure software", "check the software"},
		{"check and repair hardware", "repair the hardware"},
		{"test system functionality", "test the proper system functionality"},
		{"f1", "f1"},
		{"f2", "f2"},
		{"f3", "f3"},
		{"s1", "s1"}		
	};
	
	public ComputerRepair(){
		System.out.println("SIM | fabian: "+GraphSimilarity.graphEditSimilarity(14, 37, ComputerRepairFabian, 9, 15, 17, 18, 27));
		
		System.out.println("SIM | s0: "+  GraphSimilarity.graphEditSimilarity(15, 46, ComputerRepairS0, 10, 18, 17, 25, 27) );
		
		System.out.println("SIM | s2: "+GraphSimilarity.graphEditSimilarity(8, 44, ComputerRepairS2, 12, 15, 17, 23, 27));
	}

}


//{"customer inquiries about products", "customer inquiry about products"},
//{"client inquiry query processing", "customer inquiry processing"},
//{"create quotation from inquiry", "create quotation from inquiry"}