package dataImport.entity;

public class Orders {
    public int quantity;
    public String estimated_delivery_date;
    public String lodgement_date;
    public String product_model;
    public int sales_id;
    public String contract_number;

    public Orders(int quantity, String estimated_delivery_date, String lodgement_date, String product_model, int sales_id, String contract_number) {
        this.quantity = quantity;
        this.estimated_delivery_date = estimated_delivery_date;
        this.lodgement_date = lodgement_date;
        this.product_model = product_model;
        this.sales_id = sales_id;
        this.contract_number = contract_number;
    }
}
