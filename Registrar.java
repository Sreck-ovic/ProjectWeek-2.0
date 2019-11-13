import java.util.*;
import java.util.HashMap; 
import java.util.Map; 
import java.io.IOException;


class Registrar {
    ArrayList<Person> allPeople = new ArrayList<Person>();
    HashMap<Integer, Project> allCourses = new HashMap<>();
    ArrayList<Person> unluckyPeople = new ArrayList<Person>(); //People who were unable to be sorted into one of their choices
    Database db;
	int currentIndex;
    int sizeOfPeople;
    String url;
    Random ran = new Random();
    

    /**
        Constructs Registrar object
    */
    public Registrar(String url, boolean shuffle, ArrayList<Person> allPep, HashMap<Integer, Project> allCourse) {
        //db = new Database(url);
        this.url=url;
        for(int i=0;i<allPep.size();i++){
            this.allPeople.add(allPep.get(i).getClone());
        }
        allCourse.forEach((k,v) -> this.allCourses.put(v.getProjectID(),v.getClone()));

        if (shuffle)
             Collections.shuffle(allPeople); 

        currentIndex = 0;
        sizeOfPeople = allPeople.size();
    }


    /**
        public Person tryPlacePerson(Person p)

        Attempts to place a person p into their next project
    */

    public boolean canPlace(Person p, int pref){
        if(pref==9){
            return true;
        }
        int pweekid = p.prefToPweekID(pref);//db.getPreference(p.getStudentID(),pref);
        // if(pweekid!=p.prefToPweekID(pref)){
        //     System.out.println("AHH");
        // }
        if(pweekid==Integer.MIN_VALUE || pweekid==0){
            return false;
        }
        return !allCourses.get(pweekid).isFull();
    }
    public void randPlace(Person p){
        ArrayList<Integer> left = new ArrayList<Integer>(Arrays.asList(1,2,3,4,5,6,7,8,9));
        while(!canPlace(p,left.get(0))){
            left.remove(0);
        }
        if(left.get(0)==9){
            p.setCurrentPreferance(9);
            unluckyPeople.add(p);
            return;
        }
        int projectPref = p.prefToPweekID(left.get(0));//db.getPreference(p.getStudentID(),left.get(0));
        p.setCurrentPreferance(left.get(0));
        allCourses.get(projectPref).addStudent(p);
    }
    public void place(Person p, int pref){
        if(!canPlace(p,pref)){
            p.setCurrentPreferance(9);
            randPlace(p);
            return;
        }
        if(pref==9){
            unluckyPeople.add(p);
            return;
        }
        int projectPref=p.prefToPweekID(pref);//db.getPreference(p.getStudentID(),pref);
        p.setCurrentPreferance(pref);
        allCourses.get(projectPref).addStudent(p);
    }

    public Person tryPlacePerson(Person p) {
        //first check if the person has more choices
        if ( p.getCurrentPreference() > 8) {
            unluckyPeople.add(p);
            return null;
        }

        int nextPrefChoice = p.prefToPweekID(p.getCurrentPreference());//db.getPreference(p.getStudentID(), p.getCurrentPreference());
        if (nextPrefChoice == Integer.MIN_VALUE || nextPrefChoice == 0) {
            unluckyPeople.add(p);
            return null;
        }

        Project nextProject = allCourses.get(nextPrefChoice);

        if (!nextProject.isFull()) {
            nextProject.addStudent(p);
            return null;
        }

        Person lowestPerson = nextProject.getLowestScorePerson();

        if (lowestPerson.getScore() < p.getScore()) {
            nextProject.removeStudent(lowestPerson);
            nextProject.addStudent(p);

            lowestPerson.increaseCurrentPreference();
            return lowestPerson;
        }        
        p.increaseCurrentPreference();
        return p; //for now
    }

    /**
        public boolean hasMorePeople()

        returns true or false depending on if there are more unsorted people

        this is defined by if current index < number of people
    */
    public boolean hasMorePeople() {
        return currentIndex < sizeOfPeople;
    }

    /**
        public Person getNextPerson()

        Returns the next unsorted person

        Precondition: Registrar has more allPeople
    */
    public Person getNextPerson() {
        return allPeople.get(currentIndex++);
    }

    public int getStudentPref(int id) {
        for(int i=0;i<allPeople.size();i++){
            if(allPeople.get(i).getStudentID()==id){
                return allPeople.get(i).getCurrentPreference();
            }
        }
        return 0;
    }

    public ArrayList<Person> getUnlucky() {
        return this.unluckyPeople;
    }

    public HashMap<Integer, Project> getProjects() {
        return allCourses;
    }

    public void printProjects() {
        ArrayList<Integer> tempList = db.getAllCourseIds();
        for (int i=0; i<tempList.size(); i++)
            System.out.println(allCourses.get(tempList.get(i)));
    }

    public void printBadPeople() {
        System.out.println(unluckyPeople);
    }

    public void outputResults() {
        db = new Database(this.url);
        Saver saver = new Saver("output.csv");

        new ArrayList(allCourses.keySet());

        ArrayList<Integer> tempList = db.getAllCourseIds();
        ArrayList outputStats = new ArrayList();
        ArrayList totalStats = new ArrayList();
        ArrayList<Person> studentsInProject = new ArrayList<Person>();

        Person curPerson = new Person(1);
        int scores[] = new int[9];

        saver.write(new ArrayList<>(Arrays.asList("ID", "ChoiceNum", "ProjID", "Gender", "Grade", "Score")));

        for (int i=0; i < tempList.size(); i++) {
            studentsInProject = allCourses.get(tempList.get(i)).getEnrolledStudents();
            for (int p=0; p<studentsInProject.size(); p++) {
                curPerson = studentsInProject.get(p);
                outputStats.clear();
                
                outputStats.add(curPerson.getStudentID()); //Id
                outputStats.add(curPerson.getCurrentPreference()); //Choice
                outputStats.add(tempList.get(i)); //projectId
                outputStats.add(db.getGender(curPerson.getStudentID())); //Gender
                outputStats.add(db.getGrade(curPerson.getStudentID())); //grade
                outputStats.add((curPerson.getScore())); //score

                scores[curPerson.getCurrentPreference()-1]++;
                saver.write(outputStats);
            }
        }

        // System.out.println(unluckyPeople.size());
        for (int i=0; i<unluckyPeople.size(); i++) {
            curPerson = unluckyPeople.get(i);
            outputStats.clear();
            
            outputStats.add(curPerson.getStudentID());
            outputStats.add(curPerson.getCurrentPreference());
            outputStats.add(-1);
            outputStats.add(db.getGender(curPerson.getStudentID()));
            outputStats.add(db.getGrade(curPerson.getStudentID()));
            outputStats.add((curPerson.getScore())); //score

            scores[curPerson.getCurrentPreference()-1]++;
            saver.write(outputStats);
        }

        saver.close();

        for (int i=0; i<9; i++) {
            System.out.println("Choice"+(i+1)+": "+scores[i]);
        }
    }
}
