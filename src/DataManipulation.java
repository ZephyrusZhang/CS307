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
    //TODO Hashmap by argument
    void addOneProduct(String product_code, String product_name);
    void addOneModel(String product_model, int unit_price, int product_id);
    void addOneLocation(String countries, String city);
    void addOneSalesman(String first_name, String surname, String salesman_number, String gender, int age, String mobile_phone);
    void addOneEnterprises(String enterprise_name, String industry, int location_id, String supply_center);
    void addOneContract(String contract_number, Date contract_date, String director, int enterprise_id);
    void addOneOrder(int quantity, Date estimated_delivery_date, Date lodgement_date, int model_id, int salesman_id, int contract_id);
    void addOneOrder(int quantity, Date estimated_delivery_date, int model_id, int salesman_id, int contract_id);
}
