package dataImport.entity;

public class Contract {
    public String contract_number;
    public String contract_date;
    public String director_firstname;
    public String director_surname;
    public String client_enterprise;

    public Contract(String contract_number, String contract_date, String director_firstname, String director_surname, String client_enterprise) {
        this.contract_number = contract_number;
        this.contract_date = contract_date;
        this.director_firstname = director_firstname;
        this.director_surname = director_surname;
        this.client_enterprise = client_enterprise;
    }
}
