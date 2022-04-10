import assets.Supporting;
import com.github.javafaker.Faker;
import io.QWriter;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.HashMap;
import java.util.Objects;
import java.util.Random;

public class DatabaseManipulationCmp implements DataManipulationCmp {
    private Connection con = null;
    private ResultSet resultSet;
    private final Faker faker = new Faker();

    public static void main(String[] args) {
        DatabaseManipulationCmp cmp = new DatabaseManipulationCmp();
        cmp.openDatasource();
        cmp.insertDataIntoSalesman();
        cmp.closeDatasource();
    }

    @Override
    public void insertSupplyCenterTest() {
        String sql;
        String line;
        String[] tokens;
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("supply_center.txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        PreparedStatement preparedStatement;
        long startTime = System.currentTimeMillis();
        try {
            while ((line = Objects.requireNonNull(reader).readLine()) != null) {
                try {
                    tokens = line.split(",");
                    sql = "insert into supply_center (supply_center_name, director_name) " +
                            "values (?, ?);";
                    preparedStatement = con.prepareStatement(sql);
                    preparedStatement.setString(1, tokens[0]);
                    preparedStatement.setString(2, tokens[1]);
                    preparedStatement.executeUpdate();
                } catch (SQLException ignored) {
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        long endTime = System.currentTimeMillis();
        System.out.printf("insertEnterpriseTest() costs %.3f s\n", (endTime - startTime) / 1000.0);
    }

    @Override
    public void deleteSupplyCenterTest() {
        try (BufferedReader reader = new BufferedReader(new FileReader("product_model.txt"))) {
            String sql;
            String line;
            String[] tokens;
            PreparedStatement preparedStatement;
            int product_id = 1, model_id = 1;
            HashMap<String, Integer> product = new HashMap<>();
            HashMap<String, Integer> model = new HashMap<>();
            while ((line = reader.readLine()) != null) {
                try {
                    tokens = line.split(";");
                    if (!product.containsKey(tokens[0])) {
                        product.put(tokens[0], product_id);
                        product_id++;
                        sql = "insert into product (product_code, product_name) " +
                                "values (?, ?);";
                        preparedStatement = con.prepareStatement(sql);
                        preparedStatement.setString(1, tokens[0]);
                        preparedStatement.setString(2, tokens[1]);
                        preparedStatement.executeUpdate();
                    }

                    if (!model.containsKey(tokens[2])) {
                        model.put(tokens[2], model_id);
                        model_id++;
                        sql = "insert into model (product_model, unit_price, product_id) " +
                                "values (?, ?, ?);";
                        preparedStatement = con.prepareStatement(sql);
                        preparedStatement.setString(1, tokens[2]);
                        preparedStatement.setInt(2, Integer.parseInt(tokens[3]));
                        preparedStatement.setInt(3, product.get(tokens[0]));
                        preparedStatement.executeUpdate();
                    }
                } catch (SQLException e) {
                    if (!e.getMessage().matches("错误: 重复键违反唯一约束\".*?\"\n" +
                            " {2}详细：键值\"(.*?)=(.*?)\" 已经存在")) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

//        try {
//            String sql = "alter table model" +
//                    " add constraint fk foreign key (product_id) references product (id);";
//            PreparedStatement preparedStatement = con.prepareStatement(sql);
//            preparedStatement.executeUpdate();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
    }

    @Override
    public void selectSalesmanTest() {
        QWriter out = new QWriter();
        String sql = "select * from salesman where name like '%Aaron%';";
        long startTime = 0, endTime = 0;
        try {
            startTime = System.currentTimeMillis();
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
            endTime = System.currentTimeMillis();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        int cnt = 0;
        try {
            while (resultSet.next()) {
                out.println(Supporting.aggregateString(
                        resultSet.getString(1), resultSet.getString(2),
                        resultSet.getString(3), resultSet.getString(4),
                        resultSet.getString(5), resultSet.getString(6)
                ));
                cnt++;
            }
            out.println("total rows: " + cnt);
            out.println(sql + "costs " + (endTime - startTime) + " ms");
            out.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insertDataIntoSalesman() {
        PreparedStatement preparedStatement;
        String sql;
        Random random = new Random();
        int age;
        for (int i = 0; i < 4000000; i++) {
            sql = "insert into salesman (name, salesman_number, gender, age, mobile_phone, supply_center_id) " +
                    "values (?, ?, ?, ?, ?, ?);";
            try {
                age = random.nextInt(20);
                preparedStatement = con.prepareStatement(sql);
                preparedStatement.setString(1, faker.name().fullName());
                preparedStatement.setString(2, String.valueOf(faker.number().randomNumber(8, true)));
                preparedStatement.setString(3, (age % 2 == 0) ? "Male" : "Female");
                preparedStatement.setInt(4, age);
                preparedStatement.setString(5, faker.phoneNumber().subscriberNumber(11));
                preparedStatement.setInt(6, random.nextInt(6) + 1);

                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                if (!e.getMessage().matches("错误: 重复键违反唯一约束\".*?\"\n" +
                        " {2}详细：键值\"(.*?)=(.*?)\" 已经存在")) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void openDatasource() {
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

    public void closeDatasource() {
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
