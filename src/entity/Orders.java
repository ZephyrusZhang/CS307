package entity;

public class Orders {
    public int quantity;
    public String estimated_delivery_date;
    public String lodgement_date;
    public int model_id;
    public String contract_number;

    public Orders(int quantity, String estimated_delivery_date, String lodgement_date, int model_id, String contract_number) {
        this.quantity = quantity;
        this.estimated_delivery_date = estimated_delivery_date;
        this.lodgement_date = lodgement_date;
        this.model_id = model_id;
        this.contract_number = contract_number;
    }
}
