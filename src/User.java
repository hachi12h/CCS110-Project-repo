public class User {
	
    String lastName;
    String firstName;
    String middleName;
    String age;
    String address;
    String email;
    String department;
    String project;
    String task;

    public User(String lastName, String firstName, String middleName, String age, String address, String email
    		  , String department, String project, String task) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.middleName = middleName;
        this.age = age;
        this.address = address;
        this.email = email;
        this.department = department;
        this.project = project;
        this.task = task;
    }
}
