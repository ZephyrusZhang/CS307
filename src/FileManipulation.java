import java.sql.Date;

public class FileManipulation implements DataManipulation {

    @Override
    public void openDatasource() {
    }

    @Override
    public void closeDatasource() {

    }

    @Override
    public void executeDDL() {

    }

    @Override
    public void importOneProduct(String product_code, String product_name) {
    }

    @Override
    public void importOneModel(String product_model, int unit_price, int product_id) {
    }

    @Override
    public void importOneLocation(String country, String city) {
    }

    @Override
    public void importOneSalesman(String name, String salesman_number, String gender, int age, String mobile_phone, int supply_center_id) {
    }

    @Override
    public void importOneSupplyCenter(String supply_center_name, String director_name) {

    }

    @Override
    public void importOneEnterprise(String enterprise_name, String industry, int location_id, int supply_center_id) {
    }

    @Override
    public void importOneContract(String contract_number, String contract_date, int enterprise_id) {
    }

    @Override
    public void importOneOrders(int quantity, String estimated_delivery_date, String lodgement_date, int model_id, int contract_id, int salesman_id) {
    }

    @Override
    public void cleanData() {

    }

    @Override
    public void alterForeignKey() {

    }

    @Override
    public void truncateAllTables() {

    }
}
