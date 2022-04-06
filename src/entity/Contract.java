package entity;

public class Contract {
    public String contract_number;
    public String contract_date;
    public String name;
    public String director_surname;
    public String client_enterprise;

    public Contract(String contract_number, String contract_date, String name, String client_enterprise) {
        this.contract_number = contract_number;
        this.contract_date = contract_date;
        this.name = name;
        this.client_enterprise = client_enterprise;
    }
}
