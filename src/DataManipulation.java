import entity.*;

import java.text.ParseException;

public interface DataManipulation {
    void openDatasource();
    void closeDatasource();
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
    void dropAllData();
}
