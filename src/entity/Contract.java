package entity;

public class Contract {
    public String contract_number;
    public String contract_date;
    public int enterprise_id;

    public Contract(String contract_number, String contract_date, int enterprise_id) {
        this.contract_number = contract_number;
        this.contract_date = contract_date;
        this.enterprise_id = enterprise_id;
    }
}
