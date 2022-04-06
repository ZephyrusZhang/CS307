package entity;

public class Salesman {
    public String name;
    public String sales_number;
    public String gender;
    public int age;
    public String mobile_phone;

    public Salesman(String first_name, String sales_number, String gender, int age, String mobile_phone) {
        this.name = first_name;
        this.sales_number = sales_number;
        this.gender = gender;
        this.age = age;
        this.mobile_phone = mobile_phone;
    }
}
