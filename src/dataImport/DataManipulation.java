package dataImport;

import dataImport.entity.*;

public interface DataManipulation {
    void openDatasource();
    void closeDatasource();
    void addRawOneProduct(Product product);
    void addRawOneModel(Model model);
    void addRawOneLocation(Location location);
    void addRawOneSalesman(Salesman salesman);
    void addRawOneEnterprise(Enterprise enterprise);
    void addRawOneContract(Contract contract);
    void addRawOneOrders(Orders orders);
    void cleanRawData();
    int getLocationID(String country, String city);
    void buildForeignKey();
    void dropAllData();
}
