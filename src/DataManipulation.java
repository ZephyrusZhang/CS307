import entity.*;

import java.sql.Date;
import java.text.ParseException;

public interface DataManipulation {
    void openDatasource();
    void closeDatasource();
    void addRawOneProduct(Product product);
    void addRawOneModel(Model model);
    void addRawOneLocation(Location location);
    void addRawOneSalesman(Salesman salesman);
    void addRawOneEnterprise(Enterprise enterprise);
    void addRawOneContract(Contract contract);
    void addRawOneOrders(Orders orders) throws ParseException;
    void cleanRawData();
    int getLocationID(String country, String city);
    void buildForeignKey();
    void dropAllData();
    //TODO Hashmap by argument
    public int addOneProduct(String product_code, String product_name);
    public int addOneModel(String product_model, int unit_price, int product_id);
    public int addOneLocation(String countries, String city);
    public int addOneSalesman(String first_name,String surname, String salesman_number, String gender, int age, String mobile_phone);
    public int addOneEnterprises(String enterprise_name,String industry,int location_id, String supply_center);
    public int addOneContract(String contract_number, Date contract_date, String director, int enterprise_id);
    public int addOneOrder(int quantity, Date estimated_delivery_date, Date lodgement_date,int model_id, int salesman_id, int contract_id);
    public int addOneOrder(int quantity, Date estimated_delivery_date,int model_id, int salesman_id, int contract_id);
}
