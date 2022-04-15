import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;

import static assets.Supporting.*;

public class DataImport {
    public static void importDataUsingHashMap(DataManipulation dm) {
        String[] tokens;
        int product_id = 1, model_id = 1, location_id = 1, salesman_id = 1, supply_center_id = 1, enterprise_id = 1, contract_id = 1, orders_id = 1;
        HashMap<String, Integer> product = new HashMap<>();
        HashMap<String, Integer> model = new HashMap<>();
        HashMap<String, Integer> location = new HashMap<>();
        HashMap<String, Integer> salesman = new HashMap<>();
        HashMap<String, Integer> supply_center = new HashMap<>();
        HashMap<String, Integer> enterprise = new HashMap<>();
        HashMap<String, Integer> contract = new HashMap<>();
        HashMap<String, Integer> orders = new HashMap<>();

        try {
            BufferedReader reader = new BufferedReader(new FileReader("resources/contract_info.csv"));
            String line;
            reader.readLine();
            long startTime = System.currentTimeMillis();
            while ((line = reader.readLine()) != null) {
                tokens = line.split(",");

                //region Product
                if (!product.containsKey(tokens[6])) {
                    if (!tokens[6].matches("[a-zA-Z0-9]{7}")) {
                        throw new IllegalArgumentException("Invalid product code");
                    }
                    product.put(tokens[6], product_id);
                    product_id++;
                    dm.importOneProduct(tokens[6], tokens[7],product_id);
                }
                //endregion

                //region Model
                if (!model.containsKey(tokens[8])) {
                    model.put(tokens[8], model_id);
                    model_id++;
                    dm.importOneModel(tokens[8], Integer.parseInt(tokens[9]), product.get(tokens[6]),model_id);
                }
                //endregion

                //region Location
                if (!location.containsKey(tokens[3] + "," + tokens[4])) {
                    location.put(tokens[3] + "," + tokens[4], location_id);
                    location_id++;
                    dm.importOneLocation(tokens[3], tokens[4],location_id);
                }
                //endregion

                //region Supply Center
                if (!supply_center.containsKey(tokens[2])) {
                    supply_center.put(tokens[2], supply_center_id);
                    supply_center_id++;
                    dm.importOneSupplyCenter(tokens[2], tokens[14],supply_center_id);
                }
                //endregion

                //region Salesman
                if (!salesman.containsKey(tokens[16])) {
                    if (!tokens[16].matches("[0-9]{8}")) {
                        throw new IllegalArgumentException("Invalid salesman number");
                    }
                    if (!tokens[17].matches("Male|Female")) {
                        throw new IllegalArgumentException("Invalid gender");
                    }
                    if (Integer.parseInt(tokens[18]) < 0) {
                        throw new IllegalArgumentException("Invalid age");
                    }
                    if (!tokens[19].matches("[0-9]{11}")) {
                        throw new IllegalArgumentException("Invalid mobile phone number");
                    }
                    salesman.put(tokens[16], salesman_id);
                    salesman_id++;
                    dm.importOneSalesman(tokens[15], tokens[16], tokens[17], Integer.parseInt(tokens[18]), tokens[19], supply_center.get(tokens[2]),salesman_id);
                }
                //endregion

                //region Enterprise
                if (!enterprise.containsKey(aggregateString(tokens[1], tokens[5], tokens[3], tokens[4], tokens[2]))) {
                    enterprise.put(aggregateString(tokens[1], tokens[5], tokens[3], tokens[4], tokens[2]), enterprise_id);
                    enterprise_id++;
                    dm.importOneEnterprise(tokens[1], tokens[5], location.get(aggregateString(tokens[3], tokens[4])), supply_center.get(tokens[2]),enterprise_id);
                }
                //endregion

                //region Contract
                if (!contract.containsKey(tokens[0])) {
                    if (!tokens[0].matches("CSE[0-9]{7}")) {
                        throw new IllegalArgumentException("Invalid contract number");
                    }
                    contract.put(tokens[0], contract_id);
                    contract_id++;
                    dm.importOneContract(tokens[0], tokens[11], enterprise.get(aggregateString(tokens[1], tokens[5], tokens[3], tokens[4], tokens[2])),contract_id);
                }
                //endregion

                //region Orders
                if (!orders.containsKey(aggregateString(tokens[10], tokens[12], tokens[13], tokens[8], tokens[0], tokens[16]))) {
                    orders.put(aggregateString(tokens[10], tokens[12], tokens[13], tokens[8], tokens[0], tokens[16]), orders_id);
                    orders_id++;
                    dm.importOneOrders(Integer.parseInt(tokens[10]), tokens[12], tokens[13], model.get(tokens[8]), contract.get(tokens[0]), salesman.get(tokens[16]),orders_id);
                }
                //endregion
            }
            dm.importProduct();
            dm.importModel();
            dm.importLocation();
            dm.importSalesman();
            dm.importSupplyCenter();
            dm.importEnterprise();
            dm.importContract();
            dm.importOrder();

            long endTime = System.currentTimeMillis();
            long sec = (endTime - startTime) / 1000;
            System.out.printf("Data import costs %d s\n", sec);
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }
}
