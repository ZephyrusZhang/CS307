import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class DatabaseManipulation implements DataManipulation {
    private Connection con = null;
    private ResultSet resultSet;

    @Override
    public void openDatasource() {
        try {
            Class.forName("org.postgresql.Driver");

        } catch (ClassNotFoundException e) {
            System.err.println("Cannot find the PostgresSQL driver. Check CLASSPATH.");
            System.exit(1);
        }

        try {
            String host = "localhost";
            String dbname = "postgres";
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

    @Override
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

    @Override
    public void executeDDL() {
        StringBuilder sql = new StringBuilder("");
        try {
            BufferedReader reader = new BufferedReader(new FileReader("main.sql"));
            String line;
            while ((line = reader.readLine()) != null) {
                sql.append(line);
            }
            Statement statement = con.createStatement();
            statement.executeUpdate(sql.toString());
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    //region Import Data Using Entity Class
    @Override
    public void importOneProduct(String product_code, String product_name) {
        String sql = "insert into product (product_code, product_name)" +
                "values (?, ?);";
        try {
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setString(1, product_code);
            preparedStatement.setString(2, product_name);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void importOneModel(String product_model, int unit_price, int product_id) {
        String sql = "insert into model (product_model, unit_price, product_id) " +
                "values (?, ?, ?);";
        try {
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setString(1, product_model);
            preparedStatement.setInt(2, unit_price);
            preparedStatement.setInt(3, product_id);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void importOneLocation(String country, String city) {
        String sql = "insert into location (country, city)" +
                "values (?, ?);";
        try {
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setString(1, country);
            if (city.equals("NULL")) {
                preparedStatement.setNull(2, Types.VARCHAR);
            } else {
                preparedStatement.setString(2, city);
            }

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void importOneSalesman(String name, String salesman_number, String gender, int age, String mobile_phone, int supply_center_id) {
        String sql = "insert into salesman (name, salesman_number, gender, age, mobile_phone, supply_center_id) " +
                "values (?, ?, ?, ?, ?, ?);";
        try {
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, salesman_number);
            preparedStatement.setString(3, gender);
            preparedStatement.setInt(4, age);
            preparedStatement.setString(5, mobile_phone);
            preparedStatement.setInt(6, supply_center_id);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void importOneSupplyCenter(String supply_center_name, String director_name) {
        String sql = "insert into supply_center (supply_center_name, director_name) " +
                "values (?, ?)";
        try {
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setString(1, supply_center_name);
            preparedStatement.setString(2, director_name);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void importOneEnterprise(String enterprise_name, String industry, int location_id, int supply_center_id) {
        String sql = "insert into enterprise (enterprise_name, industry, location_id, supply_center_id) " +
                "values (?, ?, ?, ?);";
        try {
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setString(1, enterprise_name);
            preparedStatement.setString(2, industry);
            preparedStatement.setInt(3, location_id);
            preparedStatement.setInt(4, supply_center_id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void importOneContract(String contract_number, String contract_date, int enterprise_id) {
        String sql = "insert into contract (contract_number, contract_date, enterprise_id) " +
                "values (?, ?, ?)";
        try {
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setString(1, contract_number);
            preparedStatement.setDate(2, Date.valueOf(contract_date));
            preparedStatement.setInt(3, enterprise_id);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void importOneOrders(int quantity, String estimated_delivery_date, String lodgement_date, int model_id, int contract_id) throws ParseException {
        String sql = "insert into orders (quantity, estimated_delivery_date, lodgement_date, model_id, contract_id) " +
                "values (?, ?, ?, ?, ?);";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date deadline = sdf.parse("2022-04-15");
        java.util.Date date;
        try {
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setInt(1, quantity);
            preparedStatement.setDate(2, Date.valueOf(estimated_delivery_date));
            if (lodgement_date.equals("")) {
                preparedStatement.setNull(3, Types.DATE);
            } else {
                date = sdf.parse(lodgement_date);
                if (date.after(deadline)) {
                    preparedStatement.setNull(3, Types.DATE);
                } else {
                    preparedStatement.setDate(3, Date.valueOf(lodgement_date));
                }
            }
            preparedStatement.setInt(4, model_id);
            preparedStatement.setInt(5, contract_id);

            preparedStatement.executeUpdate();
        } catch (SQLException | ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void cleanData() {
        String sql = "update location set city = null where city = 'NULL'";
        try {
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getLocationID(String country, String city) {
        int id = 0;
        String sql = "select * from location " +
                "where country = ? and city = ?;";
        try {
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setString(1, country);
            preparedStatement.setString(2, city);
            id = preparedStatement.executeQuery().findColumn("id");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return id;
    }

    @Override
    public void buildForeignKey() {

    }

    @Override
    public void truncateAllTables() {
        String sql = "truncate table ?;";
        try {
            PreparedStatement preparedStatement = con.prepareStatement(sql);

            preparedStatement.setString(1, "product");
            preparedStatement.executeUpdate();

            preparedStatement.setString(1, "model");
            preparedStatement.executeUpdate();

            preparedStatement.setString(1, "location");
            preparedStatement.executeUpdate();

            preparedStatement.setString(1, "salesman");
            preparedStatement.executeUpdate();

            preparedStatement.setString(1, "enterprise");
            preparedStatement.executeUpdate();

            preparedStatement.setString(1, "contract");
            preparedStatement.executeUpdate();

            preparedStatement.setString(1, "orders");
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    //endregion

    //TODO Hashmap by argument
    @Override
    public void addOneProduct(String product_code, String product_name) {
        int result = 0;
        String sql = "insert into product (product_code, product_name)" +
                "values (?,?)";
        try {
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setString(1, product_code);
            preparedStatement.setString(2, product_name);

            result = preparedStatement.executeUpdate();

        } catch (SQLException ignored) {
        }
    }

    @Override
    public void addOneModel(String product_model, int unit_price, int product_id) {
        int result = 0;
        String sql = "insert into model (product_model, unit_price, product_id)" +
                "values (?,?,?)";
        try {
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setString(1, product_model);
            preparedStatement.setInt(2, unit_price);
            preparedStatement.setInt(3, product_id);

            result = preparedStatement.executeUpdate();

        } catch (SQLException ignored) {
        }

    }

    @Override
    public void addOneLocation(String country, String city) {
        int result = 0;
        String sql = "insert into location (country, city)" +
                "values (?,?)";
        try {
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setString(1, country);
            preparedStatement.setString(2, city);

            result = preparedStatement.executeUpdate();

        } catch (SQLException ignored) {
        }
    }

    @Override
    public void addOneSalesman(String first_name, String surname, String salesman_number, String gender, int age, String mobile_phone) {
        int result = 0;
        String sql = "insert into salesman(first_name, surname, salesman_number, gender, age, mobile_phone)" +
                "values (?,?,?,?,?,?)";
        try {
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setString(1, first_name);
            preparedStatement.setString(2, surname);
            preparedStatement.setString(3, salesman_number);
            preparedStatement.setString(4, gender);
            preparedStatement.setInt(5, age);
            preparedStatement.setString(6, mobile_phone);


            result = preparedStatement.executeUpdate();

        } catch (SQLException ignored) {
        }
    }

    @Override
    public void addOneEnterprises(String enterprise_name, String industry, int location_id, String supply_center) {
        int result = 0;
        String sql = "insert into enterprise (enterprise_name, industry,location_id, supply_center)" +
                "values (?,?,?,?)";
        try {
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setString(1, enterprise_name);
            preparedStatement.setString(2, industry);
            preparedStatement.setInt(3, location_id);
            preparedStatement.setString(4, supply_center);

            result = preparedStatement.executeUpdate();

        } catch (SQLException ignored) {
        }
    }

    @Override
    public void addOneContract(String contract_number, Date contract_date, String director, int enterprise_id) {
        int result = 0;
        String sql = "insert into contract (contract_number, contract_date, director, enterprise_id)" +
                "values (?,?,?,?)";
        try {
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setString(1, contract_number);
            preparedStatement.setDate(2, contract_date);
            preparedStatement.setString(3, director);
            preparedStatement.setInt(4, enterprise_id);

            result = preparedStatement.executeUpdate();

        } catch (SQLException ignored) {
        }
    }

    @Override
    public void addOneOrder(int quantity, Date estimated_delivery_date, Date lodgement_date, int model_id, int salesman_id, int contract_id) {
        int result = 0;
        String sql = "insert into orders (quantity, estimated_delivery_date, lodgement_date,model_id, sales_id, contract_id)" +
                "values (?,?,?,?,?,?)";
        try {
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setInt(1, quantity);
            preparedStatement.setDate(2, estimated_delivery_date);
            preparedStatement.setDate(3, lodgement_date);
            preparedStatement.setInt(4, model_id);
            preparedStatement.setInt(5, salesman_id);
            preparedStatement.setInt(6, contract_id);

            result = preparedStatement.executeUpdate();

        } catch (SQLException ignored) {
        }
    }

    @Override
    public void addOneOrder(int quantity, Date estimated_delivery_date, int model_id, int salesman_id, int contract_id) {
        int result = 0;
        String sql = "insert into orders (quantity, estimated_delivery_date, lodgement_date,model_id, sales_id, contract_id)" +
                "values (?,?,?,?,?)";
        try {
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setInt(1, quantity);
            preparedStatement.setDate(2, estimated_delivery_date);
            preparedStatement.setInt(3, model_id);
            preparedStatement.setInt(4, salesman_id);
            preparedStatement.setInt(5, contract_id);

            result = preparedStatement.executeUpdate();

        } catch (SQLException ignored) {
        }
    }


}
