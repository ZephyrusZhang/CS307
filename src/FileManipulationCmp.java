import assets.Supporting;
import io.QWriter;

import java.io.*;
import java.sql.*;
import java.util.*;

import static assets.Supporting.*;

public class FileManipulationCmp implements DataManipulationCmp {
    private static Connection con;
    private ResultSet resultSet;

    public static void main(String[] args) {
        FileManipulationCmp cmp = new FileManipulationCmp();
        cmp.updateSalesmanTest();
    }

    @Override
    public void insertSupplyCenterTest() {
    }

    @Override
    public void deleteModelTest() {
        List<String> cache = new ArrayList<>();
        long startTime = System.currentTimeMillis();
        try (BufferedReader reader = new BufferedReader(new FileReader("file-database/model.csv"))) {
            String[] tokens;
            String line;
            reader.readLine();
            while ((line = reader.readLine()) != null) {
                tokens = line.split(",");
                if (tokens[1].contains("Aldo")) {
                    continue;
                }
                cache.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("file-database/model.csv"))) {
            for (String s : cache) {
                writer.write(s);
                writer.write("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        long endTime = System.currentTimeMillis();
        System.out.println((endTime - startTime) + "ms");
    }

    @Override
    public void selectSalesmanTest() {
        QWriter out = new QWriter();
        long startTime = System.currentTimeMillis();
        try (BufferedReader reader = new BufferedReader(new FileReader("file-database/salesman.csv"))) {
            String[] tokens;
            String line;
            while ((line = reader.readLine()) != null) {
                tokens = line.split(",");
                if (tokens[5].equals("22923247653")) {
                    out.println(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        long endTime = System.currentTimeMillis();
        out.println((endTime - startTime) + "ms");
        out.close();
    }

    @Override
    public void updateSalesmanTest() {
        List<String> cache = new LinkedList<>();
        long startTime = System.currentTimeMillis();
        try (BufferedReader reader = new BufferedReader(new FileReader("file-database/salesman.csv"))) {
            String line;
            String[] tokens;
            reader.readLine();
            while ((line = reader.readLine()) != null) {
                tokens = line.split(",");
                if (tokens[1].contains("Mr. ")) {
                    tokens[1] = tokens[1].replace("Mr. ", "");
                }
                cache.add(tokens[0] + "," + tokens[1] + "," + tokens[2] + "," + tokens[3] + "," + tokens[4] + "," + tokens[5] + "," + tokens[6]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("file-database/salesman.csv"))) {
            writer.write("id,name,sales_number,,gender,age,mobile_phone,supply_center_id\n");
            for (String str : cache) {
                writer.write(str);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        long endTime = System.currentTimeMillis();
        System.out.println((endTime - startTime) + "ms");
    }

    public void initSalesmanSource() {
        Random random = new Random();
        int age;
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("resources/salesman.txt"))) {
            for (int i = 0; i < 1000000; i++) {
                age = random.nextInt(20);
                writer.write(faker.name().fullName() + "," + faker.number().randomNumber(8, true) + "," +
                        ((age % 2 == 0) ? "Male" : "Female") + "," + age + "," + faker.phoneNumber().subscriberNumber(11) + "," +
                        (random.nextInt(6) + 1) + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void initModel() {
        try (BufferedReader reader = new BufferedReader(new FileReader("resources/product_model.txt"))) {
            int serial_id = 1;
            String line;
            Random random = new Random();
            String[] tokens;
            HashMap<String, Integer> model = new HashMap<>();
            BufferedWriter writer = new BufferedWriter(new FileWriter("file-database/model.csv"));
            writer.write("id,product_model,unit_price,product_id\n");
            long starTime = System.currentTimeMillis();
            while ((line = reader.readLine()) != null) {
                tokens = line.split(";");
                if (!model.containsKey(tokens[2])) {
                    model.put(tokens[2], serial_id);
                    writer.write(Supporting.aggregateString(String.valueOf(serial_id), tokens[2], tokens[3], String.valueOf(random.nextInt(100))));
                    writer.write("\n");
                    serial_id++;
                }
            }
            long endTime = System.currentTimeMillis();
            System.out.println((endTime - starTime) + "ms");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void initSupplyCenter() {
        try (BufferedReader reader = new BufferedReader(new FileReader("resources/supply_center.txt"))) {
            int line_cnt = 2;
            int serial_id = 1;
            String line;
            String[] tokens;
            HashMap<String, Integer> supply_center = new HashMap<>();
            BufferedWriter writer = new BufferedWriter(new FileWriter("file-database/supply_center.csv"));
            writer.write("id,supply_center_name,director_name\n");
            long starTime = System.currentTimeMillis();
            while ((line = reader.readLine()) != null) {
                tokens = line.split(",");
                if (tokens[0].length() == 0 || tokens[1].length() == 0) {
                    throw new IllegalArgumentException(String.format("Attribute is not allowed to be null at row %d", line_cnt));
                }
                if (tokens[0].length() > 80 || tokens[1].length() > 80) {
                    throw new IllegalArgumentException(String.format("%s is too long for varchar(80) at row %d", tokens[0], line_cnt));
                }
                if (!supply_center.containsKey(tokens[0] + "," + tokens[1])) {
                    supply_center.put(tokens[0], serial_id);
                    writer.write(Supporting.aggregateString(String.valueOf(serial_id), tokens[0], tokens[1]));
                    writer.write("\n");
                    serial_id++;
                    line_cnt++;
                }
            }
            long endTime = System.currentTimeMillis();
            System.out.println((endTime - starTime) + "ms");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void initSalesman() {
        openDatasource();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("file-database/salesman.csv"))) {
            String sql = "select * from salesman;";
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
            writer.write("id,name,sales_number,,gender,age,mobile_phone,supply_center_id\n");
            while (resultSet.next()) {
                writer.write(resultSet.getInt(1) + "," + resultSet.getString(2) + "," +
                        resultSet.getString(3) + "," + resultSet.getString(4) + "," +
                        resultSet.getInt(5) + "," + resultSet.getString(6) + "," +
                        resultSet.getInt(7) + "\n");
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }

        closeDatasource();
    }

    public static void openDatasource() {
        try {
            Class.forName("org.postgresql.Driver");

        } catch (ClassNotFoundException e) {
            System.err.println("Cannot find the PostgresSQL driver. Check CLASSPATH.");
            System.exit(1);
        }

        try {
            String host = "localhost";
            String dbname = "duplicate";
            String port = "5432";
            String user = "postgres";
            String pwd = "2996362441";

            String url = "jdbc:postgresql://" + host + ":" + port + "/" + dbname;
            con = DriverManager.getConnection(url, user, pwd);

        } catch (SQLException e) {
            System.err.println("Database connection failed");
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }

    public static void closeDatasource() {
        if (con != null) {
            try {
                con.close();
                con = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
