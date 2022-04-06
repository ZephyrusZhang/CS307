import entity.*;

import java.sql.Date;

public class FileManipulation implements DataManipulation {

    @Override
    public void openDatasource() {
    }

    @Override
    public void closeDatasource() {

    }

    @Override
    public void addRawOneProduct(Product product) {
    }

    @Override
    public void addRawOneModel(Model model) {
    }

    @Override
    public void addRawOneLocation(Location location) {
    }

    @Override
    public void addRawOneSalesman(Salesman salesman) {
    }

    @Override
    public void addRawOneEnterprise(Enterprise enterprise) {
    }

    @Override
    public void addRawOneContract(Contract contract) {
    }

    @Override
    public void addRawOneOrders(Orders orders) {
    }

    @Override
    public void cleanRawData() {

    }

    @Override
    public int getLocationID(String country, String city) {
        return 0;
    }

    @Override
    public void buildForeignKey() {

    }

    @Override
    public void dropAllData() {

    }

    @Override
    public int addOneProduct(String product_code, String product_name) {
        return 0;
    }

    @Override
    public int addOneModel(String product_model, int unit_price, int product_id) {
        return 0;
    }

    @Override
    public int addOneLocation(String countries, String city) {
        return 0;
    }

    @Override
    public int addOneSalesman(String first_name, String surname, String salesman_number, String gender, int age, String mobile_phone) {
        return 0;
    }

    @Override
    public int addOneEnterprises(String enterprise_name, String industry, int location_id, String supply_center) {
        return 0;
    }

    @Override
    public int addOneContract(String contract_number, Date contract_date, String director, int enterprise_id) {
        return 0;
    }

    @Override
    public int addOneOrder(int quantity, Date estimated_delivery_date, Date lodgement_date, int model_id, int salesman_id, int contract_id) {
        return 0;
    }

    @Override
    public int addOneOrder(int quantity, Date estimated_delivery_date, int model_id, int salesman_id, int contract_id) {
        return 0;
    }
}
