import entity.*;

import java.sql.Date;
import java.text.ParseException;

public interface DataManipulation {
    void openDatasource();
    void closeDatasource();
    void executeDDL();
    void addOneProductByEntity(Product product);
    void addOneModelByEntity(Model model);
    void addOneLocationByEntity(Location location);
    void addOneSalesmanByEntity(Salesman salesman);
    void addOneEnterpriseByEntity(Enterprise enterprise);
    void addOneContractByEntity(Contract contract);
    void addOneOrdersByEntity(Orders orders) throws ParseException;
    void cleanData();
    int getLocationID(String country, String city);
    void buildForeignKey();
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
