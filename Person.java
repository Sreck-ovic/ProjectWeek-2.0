public class Person{
	int studentID;
	int currentPreference;
	int[] prefpweekIDs;
	String gender;

	//this field is how much priority the Person currently has
	int score;

	/*
	* I wasn't positive exactly what constructors we were going to need
	* so I just made all of them. We can delete the redundant ones later
	*/

	public Person(int studentID, int currentPreference, int score){
		this.studentID = studentID;
		this.currentPreference = currentPreference;
		this.score = score;
		this.prefpweekIDs=new int[8];
	}

	public Person(int studentID){
		this.studentID = studentID;
		this.currentPreference  = 1;
		this.score = 0;
		this.prefpweekIDs=new int[8];
	}

	public Person(int studentID, int score, String gender){
		this.gender=gender;
		this.studentID = studentID;
		this.score = score;
		this.currentPreference = 1;
		this.prefpweekIDs=new int[8];
	}

	//getter
	public int getStudentID(){
		return this.studentID;
	}

	public void setCurrentPreferance(int x){
		this.currentPreference=x;
		//System.out.println(this.currentPreference);
	}

	public void setPweekID(int id, int pref){
		this.prefpweekIDs[pref]=id;
	}

	public void setPweekIDs(int[] arr) {
		this.prefpweekIDs=arr;
	}

	public int prefToPweekID(int pref){
		return prefpweekIDs[pref-1];
	}

	//getter
	public int getCurrentPreference(){
		return this.currentPreference;
	}

	public void increaseCurrentPreference() {
		currentPreference++;
		//System.out.println(this.currentPreference);
	}
	public Person getClone(){
		Person p = new Person(studentID,currentPreference,score);
		p.setPweekIDs(prefpweekIDs.clone());
		return p;
	}

	//getter
	public int getScore(){
		return this.score;
	}

	public String toString() {
		return "#" + studentID+":"+currentPreference;
	}
}
