public class Employee {
    public long id;
    public String firstname;
    public String lastname;
    public String country;
    public int age;

    public Employee(){

    }

    public Employee(long id, String firstname, String lastname, String country, int age){
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.country = country;
        this.age = age;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", country='" + country + '\'' +
                ", age=" + age +
                '}';
    }
}
