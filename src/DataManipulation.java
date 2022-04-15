import java.sql.Date;
import java.text.ParseException;

public interface DataManipulation {
    void openDatasource();

    void executeDDL();

    void importOneProduct(String product_code, String product_name, int cnt);

    void importOneModel(String product_model, int unit_price, int product_id, int cnt);

    void importOneLocation(String country, String city, int cnt);

    void importOneSalesman(String name, String salesman_number, String gender, int age, String mobile_phone, int supply_center_id, int cnt);

    void importOneSupplyCenter(String supply_center_name, String director_name, int cnt);

    void importOneEnterprise(String enterprise_name, String industry, int location_id, int supply_center_id, int cnt);

    void importOneContract(String contract_number, String contract_date, int enterprise_id, int cnt);

    void importOneOrders(int quantity, String estimated_delivery_date, String lodgement_date, int model_id, int contract_id, int salesman_id, int cnt) throws ParseException;

    void importProduct();

    void importModel();

    void importLocation();

    void importSalesman();

    void importSupplyCenter();

    void importEnterprise();

    void importContract();

    void importOrder();

    void cleanData();
}
