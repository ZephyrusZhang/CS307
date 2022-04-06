import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;

public class DataImport {

    public static void importDataByEntity(DataManipulation dm) {
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
            BufferedReader reader = new BufferedReader(new FileReader("contract_info.csv"));
            String line;
            reader.readLine();
            long startTime = System.currentTimeMillis();

            while ((line = reader.readLine()) != null) {
                tokens = line.split(",");
                //tokens[0]->contract_number;   tokens[1]->enterprise_name; tokens[2]->supply_center;   tokens[3]->country;
                //tokens[4]->city               tokens[5]->industry;        tokens[6]->product_code;    tokens[7]->product_name;
                //tokens[8]->product_model;     tokens[9]->unit_price;      tokens[10]->quantity;       tokens[11]->contract_date;
                //tokens[12]->delivery_date;    tokens[13]->lodgement_date; tokens[14]->director_name;  tokens[15]->salesman_name;
                //tokens[16]->salesman_number;  tokens[17]->gender;         tokens[18]->age;            tokens[19]->mobile_phone;

                //region Product
                if (!product.containsKey(tokens[6])) {
                    product.put(tokens[6], product_id);
                    product_id++;
                    if (tokens[6].length() > 7) {
                        throw new IllegalArgumentException("Product name is too long");
                    } else if (tokens[7].length() > 256) {
                        throw new IllegalArgumentException("Product model is too long");
                    }
                    dm.importOneProduct(tokens[6], tokens[7]);
                }
                //endregion

                //region Model
                if (!model.containsKey(tokens[8])) {
                    model.put(tokens[8], model_id);
                    model_id++;
                    dm.importOneModel(tokens[8], Integer.parseInt(tokens[9]), product.get(tokens[6]));
                }
                //endregion

                //region Location
                if (!location.containsKey(tokens[3] + "," + tokens[4])) {
                    location.put(tokens[3] + "," + tokens[4], location_id);
                    location_id++;
                    dm.importOneLocation(tokens[3], tokens[4]);
                }
                //endregion

                //region Supply Center
                if (!supply_center.containsKey(tokens[2])) {
                    supply_center.put(tokens[2], supply_center_id);
                    supply_center_id++;
                    dm.importOneSupplyCenter(tokens[2], tokens[14]);
                }
                //endregion

                //region Salesman
                if (!salesman.containsKey(tokens[15] + "," + tokens[16] + "," + tokens[17] + "," + tokens[18] + "," + tokens[19])) {
                    salesman.put(tokens[15] + "," + tokens[16] + "," + tokens[17] + "," + tokens[18] + "," + tokens[19], salesman_id);
                    salesman_id++;
                    dm.importOneSalesman(tokens[15], tokens[16], tokens[17], Integer.parseInt(tokens[18]), tokens[19], supply_center.get(tokens[2]));
                }
                //endregion

                //region Enterprise
                if (!enterprise.containsKey(aggregateString(tokens[1], tokens[5], tokens[3], tokens[4], tokens[2]))) {
                    enterprise.put(aggregateString(tokens[1], tokens[5], tokens[3], tokens[4], tokens[2]), enterprise_id);
                    enterprise_id++;
                    dm.importOneEnterprise(tokens[1], tokens[5], location.get(aggregateString(tokens[3], tokens[4])), supply_center.get(tokens[2]));
                }
                //endregion

                //region Contract
                if (!contract.containsKey(tokens[0])) {
                    contract.put(tokens[0], contract_id);
                    contract_id++;
                    dm.importOneContract(tokens[0], tokens[11], enterprise.get(aggregateString(tokens[1], tokens[5], tokens[3], tokens[4], tokens[2])));
                }
                //endregion

                //region Orders
                if (!orders.containsKey(aggregateString(tokens[10], tokens[12], tokens[13], tokens[8], tokens[0]))) {
                    orders.put(aggregateString(tokens[10], tokens[12], tokens[13], tokens[8], tokens[0]), orders_id);
                    orders_id++;
                    dm.importOneOrders(Integer.parseInt(tokens[10]), tokens[12], tokens[13], model.get(tokens[8]), contract.get(tokens[0]));
                }
                //endregion
            }

            long endTime = System.currentTimeMillis();
            long sec = (endTime - startTime) / 1000;
            System.out.printf("Data import costs %d s\n", sec);
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    public static String aggregateString(String... args) {
        StringBuilder res = new StringBuilder();
        for (int i = 0; i < args.length - 1; i++) {
            res.append(args[i]).append(",");
        }
        res.append(args[args.length - 1]);
        return String.valueOf(res);
    }
}
