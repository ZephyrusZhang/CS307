import assets.Supporting;

import java.io.*;
import java.util.HashMap;

public class FileManipulationCmp implements DataManipulationCmp {

    public static void main(String[] args) {
        FileManipulationCmp cmp = new FileManipulationCmp();
        cmp.initSupplyCenter();
    }

    @Override
    public void insertSupplyCenterTest() {

    }

    @Override
    public void deleteSupplyCenterTest() {

    }

    @Override
    public void selectSalesmanTest() {

    }

    public void initSupplyCenter() {
        BufferedWriter writer = null;
        try (BufferedReader reader = new BufferedReader(new FileReader("supply_center.txt"))) {
            int serial_id = 1;
            String line;
            String[] tokens;
            HashMap<String, Integer> supply_center = new HashMap<>();
            writer = new BufferedWriter(new FileWriter("supply_center.csv"));
            while ((line = reader.readLine()) != null) {
                tokens = line.split(",");
                if (tokens[0].length() > 80 | tokens[1].length() > 80) {
                    throw new IllegalArgumentException(String.format("%s is too long for varchar(80) at row %d", tokens[0], serial_id));
                }
                if (!supply_center.containsKey(tokens[0] + "," + tokens[1])) {
                    supply_center.put(tokens[0], serial_id);
                    writer.write(Supporting.aggregateString(String.valueOf(serial_id), tokens[0], tokens[1]));
                    writer.write("\n");
                    serial_id++;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            assert writer != null;
            try {
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
