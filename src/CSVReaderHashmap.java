import java.io.*;
import java.util.HashMap;

public class CSVReaderHashmap {
    public static void main(String[] args) throws FileNotFoundException {
        try {
            DataManipulation dm = new DataFactory().createDataManipulation(args[0]);
            dm.openDatasource(); // 打开数据库
            long t1 = System.currentTimeMillis();
            int i = 0;
            int Location_id = 0;
            int Enterprise_id = 0;
            int Product_id = 0;
            int Model_id = 0;
            int Salesman_id = 0;
            int Contract_id = 0;
            BufferedReader reader = new BufferedReader(new FileReader("D:\\contract_info.csv"));
            String line;
            line = reader.readLine();
            HashMap<String, Integer> Location = new HashMap<>();
            HashMap<String, Integer> Enterprise = new HashMap<>();
            HashMap<String, Integer> Product = new HashMap<>();
            HashMap<String, Integer> Model = new HashMap<>();
            HashMap<String, Integer> Salesman = new HashMap<>();
            HashMap<String, Integer> Contact = new HashMap<>();
            while ((line = reader.readLine()) != null) {
                i++;
                String[] token = line.split(",");
                String[] token_name = token[15].split(" ");
                String[] token_country = token[3].split("/");
                int unit_price = Integer.parseInt(token[9]);
                int age = Integer.parseInt(token[18]);

                if (!Location.containsKey(token[3] + token[4])) {
                    if (token_country.length == 1) {
                        Location_id++;
                        Location.put(token[3] + token[4], Location_id);
                        dm.addOneLocation(token[3], token[4]);
                    } else {
                        if (!Location.containsKey(token_country[0] + token[4])) {
                            Location_id++;
                            Location.put(token_country[0] + token[4], Location_id);
                            dm.addOneLocation(token_country[0], token[4]);
                        }
                        if (!Location.containsKey(token_country[1] + token[4])) {
                            Location_id++;
                            Location.put(token_country[1] + token[4], Location_id);
                            dm.addOneLocation(token_country[1], token[4]);
                        }
                    }
                }
                if (!Enterprise.containsKey(token[1])) {
                    if (token_country.length == 1) {
                        Enterprise_id++;
                        Enterprise.put(token[1], Enterprise_id);
                        dm.addOneEnterprises(token[1], token[5], Location.get(token[3] + token[4]), token[2]);
                    } else {
                        Enterprise_id++;
                        Enterprise.put(token[1], Enterprise_id);
                        dm.addOneEnterprises(token[1], token[5], Location.get(token_country[0] + token[4]), token[2]);
                        dm.addOneEnterprises(token[1], token[5], Location.get(token_country[1] + token[4]), token[2]);
                    }
                }
                if (!Product.containsKey(token[6])) {
                    Product_id++;
                    Product.put(token[6], Product_id);
                    dm.addOneProduct(token[6], token[7]);
                }
                if (!Model.containsKey(token[8])) {
                    Model_id++;
                    Model.put(token[8], Model_id);
                    dm.addOneModel(token[8], unit_price, Product_id);
                }
                if (!Salesman.containsKey(token[16])) {
                    Salesman_id++;
                    Salesman.put(token[16], Salesman_id);
                    if (!token[3].equals("China")) {
                        dm.addOneSalesman(token_name[0], token_name[1], token[16], token[17], age, token[19]);
                    } else {
                        dm.addOneSalesman(token_name[1], token_name[0], token[16], token[17], age, token[19]);
                    }
                }
                if (!Contact.containsKey(token[0])) {
                    Contract_id++;
                    Contact.put(token[0], Contract_id);
                    dm.addOneContract(token[0], java.sql.Date.valueOf(token[11]), token[14], Enterprise.get(token[1]));
                }

                //java.sql.Date d = new java.sql.Date(2022-3-2);
                //||java.sql.Date.valueOf(token[13]).compareTo(d)!=0
                if (token[13].equals("")) {
                    dm.addOneOrder(Integer.parseInt(token[10]), java.sql.Date.valueOf(token[12]), Model.get(token[8]), Salesman.get(token[16]), Contact.get(token[0]));
                } else {
                    dm.addOneOrder(Integer.parseInt(token[10]), java.sql.Date.valueOf(token[12]), java.sql.Date.valueOf(token[13]), Model.get(token[8]), Salesman.get(token[16]), Contact.get(token[0]));
                }
            }
            dm.closeDatasource();
            long t2 = System.currentTimeMillis();
            System.out.println(t2 - t1);
        } catch (IllegalArgumentException | IOException e) {
            System.err.println(e.getMessage());
        }

    }
}



