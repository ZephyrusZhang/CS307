import java.sql.Date;
import java.text.ParseException;

public interface DataManipulation {
    void openDatasource();

    void closeDatasource();

    void executeDDL();

    void importOneProduct(String product_code, String product_name);

    void importOneModel(String product_model, int unit_price, int product_id);

    void importOneLocation(String country, String city);

    void importOneSalesman(String name, String salesman_number, String gender, int age, String mobile_phone, int supply_center_id);

    void importOneSupplyCenter(String supply_center_name, String director_name);

    void importOneEnterprise(String enterprise_name, String industry, int location_id, int supply_center_id);

    void importOneContract(String contract_number, String contract_date, int enterprise_id);

    void importOneOrders(int quantity, String estimated_delivery_date, String lodgement_date, int model_id, int contract_id, int salesman_id) throws ParseException;

    void cleanData();

    void alterForeignKey();

    void truncateAllTables();
}
