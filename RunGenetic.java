public class RunGenetic{
	public static void main(String args[]){
		GeneticAlgo gen = new GeneticAlgo("jdbc:sqlite:pweek.db");
		gen.populate(100);
		gen.sort();
		gen.getTopReg().outputResults();
		for(int i=0;i<500;i++){
			System.out.println("Generation "+i);
			gen.killandmate();
			gen.sort();
		}
		gen.getTopReg().outputResults();
	}
}