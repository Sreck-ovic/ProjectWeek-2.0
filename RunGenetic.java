import java.util.ArrayList;

public class RunGenetic{
	public static void main(String args[]){
		int totalGenerations = 10000;
		System.out.println("Setting Up Algorithm #1");
		ArrayList<Integer> test = new ArrayList<Integer>();

		final String url = "jdbc:sqlite:pweek.db";
		
		GeneticAlgo gen = new GeneticAlgo(url, new ArrayList<Integer>());
		gen.populate(100);
		gen.sort();
//		gen.getTopReg().outputResults();

		System.out.println("Genetic Algorithm #1");

		for(int i=0;i<totalGenerations;i++){
			System.out.print("\rGeneration "+ i + "/"+totalGenerations);
			gen.killandmate();
			gen.sort();
		}
		System.out.println("");

		ArrayList<Integer> badProjects = gen.getTopReg().outputResults();
		System.out.println("Bad = " + badProjects);
		System.out.println("Setting Up Algorithm #2");

		GeneticAlgo gen2 = new GeneticAlgo(url, badProjects);
		
		gen2.populate(100);
		gen2.sort();

		if (badProjects.size() > 0) {
			System.out.println("Genetic Algorithm #2");

			for (int i = 0; i < totalGenerations; i++) {
				System.out.print("\rGeneration " + i + "/" + totalGenerations);
				gen2.killandmate();
				gen2.sort();
			}
			System.out.println("");
			gen2.getTopReg().outputResultstoCSV();
		}
		else {
			gen.getTopReg().outputResultstoCSV();
		}
	}
}
