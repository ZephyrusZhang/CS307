import entity.*;

public interface DataManipulation {
    void openDatasource();
    void closeDatasource();
    void addOneProduct(Product product);
    void addOneModel(Model model);
    void addOneLocation(Location location);
    void addOneSalesman(Salesman salesman);
    void addOneEnterprise(Enterprise enterprise);
    void addOneContract(Contract contract);
    void addOneOrders(Orders orders);
    void cleanData();
    int getLocationID(String country, String city);
    void buildForeignKey();
    void dropAllData();
}
