import java.util.ArrayList;

public class RunGenetic{



	public static void main(String args[]){
		int totalGenerations = 10000;
		System.out.println("Setting Up Algorithm #1");
		ArrayList<Integer> test = new ArrayList<Integer>();
		//test.add(149);
		//test.add(167);
		GeneticAlgo gen = new GeneticAlgo("jdbc:sqlite:pweek.db", new ArrayList<Integer>());
		gen.populate(100);
		gen.sort();
//		gen.getTopReg().outputResults();

		System.out.println("Genetic Algorithm #1");
		for(int i=0;i<totalGenerations;i++){
			//System.out.println("Generation "+i);
			gen.killandmate();
			gen.sort();
		}
		ArrayList<Integer> bad = gen.getTopReg().outputResults();
		System.out.println("Bad = " + bad);
		System.out.println("Setting Up Algorithm #2");
		test.add(149);
		test.add(167);
		GeneticAlgo gen2 = new GeneticAlgo("jdbc:sqlite:pweek.db", bad);
		
		gen2.populate(100);
		gen2.sort();
//		gen.getTopReg().outputResults();

		System.out.println("Genetic Algorithm #2");
		for(int i=0;i<totalGenerations;i++){
		//	System.out.println("Generation "+i);
			gen2.killandmate();
			gen2.sort();
		}
		gen2.getTopReg().outputResultstoCSV();








	}
}
