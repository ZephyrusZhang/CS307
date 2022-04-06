package entity;

public class Model {
    public String product_model;
    public int unit_price;
    public String product_id;

    public Model(String product_model, int unit_price, String product_id) {
        this.product_model = product_model;
        this.unit_price = unit_price;
        this.product_id = product_id;
    }
}
