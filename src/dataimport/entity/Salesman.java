package dataimport.entity;

public class Salesman {
    public String firstname;
    public String surname;
    public String sales_number;
    public String gender;
    public int age;
    public String mobile_phone;

    public Salesman(String first_name, String surname, String sales_number, String gender, int age, String mobile_phone) {
        this.firstname = first_name;
        this.surname = surname;
        this.sales_number = sales_number;
        this.gender = gender;
        this.age = age;
        this.mobile_phone = mobile_phone;
    }
}
