import entity.*;

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

    //region Import Data Using Entity Class
    @Override
    public void addOneProductByEntity(Product product) {
        String sql = "insert into product (product_code, product_name)" +
                "values (?, ?);";
        try {
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setString(1, product.product_code);
            preparedStatement.setString(2, product.product_name);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            if (!e.getMessage().matches("错误: 重复键违反唯一约束\".*?\"\n" +
                    " {2}详细：键值\"(.*?)=(.*?)\" 已经存在")) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void addOneModelByEntity(Model model) {
        String sql = "insert into model (product_model, unit_price, product_code)" +
                "values (?, ?, ?);";
        try {
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setString(1, model.product_model);
            preparedStatement.setInt(2, model.unit_price);
            preparedStatement.setString(3, model.product_code);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            if (!e.getMessage().matches("错误: 重复键违反唯一约束\".*?\"\n" +
                    " {2}详细：键值\"(.*?)=(.*?)\" 已经存在")) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void addOneLocationByEntity(Location location) {
        String sql = "insert into location (country, city)" +
                "values (?, ?);";
        try {
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setString(1, location.country);
            preparedStatement.setString(2, location.city);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            if (!e.getMessage().matches("错误: 重复键违反唯一约束\".*?\"\n" +
                    " {2}详细：键值\"(.*?)=(.*?)\" 已经存在")) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void addOneSalesmanByEntity(Salesman salesman) {
        String sql = "insert into salesman (first_name, surname, salesman_number, gender, age, mobile_phone)" +
                "values (?, ?, ?, ?, ?, ?);";
        try {
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setString(1, salesman.firstname);
            preparedStatement.setString(2, salesman.surname);
            preparedStatement.setString(3, salesman.sales_number);
            preparedStatement.setString(4, salesman.gender);
            preparedStatement.setInt(5, salesman.age);
            preparedStatement.setString(6, salesman.mobile_phone);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            if (!e.getMessage().matches("错误: 重复键违反唯一约束\".*?\"\n" +
                    " {2}详细：键值\"(.*?)=(.*?)\" 已经存在")) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void addOneEnterpriseByEntity(Enterprise enterprise) {
        String sql = "insert into enterprise (enterprise_name, industry, location_id, supply_center)" +
                "values (?, ?, ?, ?);";
        try {
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setString(1, enterprise.enterprise_name);
            preparedStatement.setString(2, enterprise.industry);
            preparedStatement.setInt(3, enterprise.location_id);
            preparedStatement.setString(4, enterprise.supply_center);
            preparedStatement.executeUpdate();

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            if (!e.getMessage().matches("错误: 重复键违反唯一约束\".*?\"\n" +
                    " {2}详细：键值\"(.*?)=(.*?)\" 已经存在")) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void addOneContractByEntity(Contract contract) {
        String sql = "insert into contract (contract_number, contract_date, director_firstname, director_surname, client_enterprise)" +
                "values (?, ?, ?, ?, ?);";
        try {
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setString(1, contract.contract_number);
            preparedStatement.setDate(2, Date.valueOf(contract.contract_date));
            preparedStatement.setString(3, contract.director_firstname);
            preparedStatement.setString(4, contract.director_surname);
            preparedStatement.setString(5, contract.client_enterprise);
            preparedStatement.executeUpdate();

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            if (!e.getMessage().matches("错误: 重复键违反唯一约束\".*?\"\n" +
                    " {2}详细：键值\"(.*?)=(.*?)\" 已经存在")) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void addOneOrdersByEntity(Orders orders) throws ParseException {
        String sql = "insert into orders (quantity, estimated_delivery_date, lodgement_date, product_model, sales_id, contract_number)" +
                "values (?, ?, ?, ?, ?, ?);";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date deadline = sdf.parse("2022-04-15");
        java.util.Date date;
        try {
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setInt(1, orders.quantity);
            preparedStatement.setDate(2, Date.valueOf(orders.estimated_delivery_date));
            if (orders.lodgement_date.equals("")) {
                preparedStatement.setNull(3, Types.DATE);
            } else {
                date = sdf.parse(orders.lodgement_date);
                if (date.after(deadline)) {
                    preparedStatement.setNull(3, Types.DATE);
                } else {
                    preparedStatement.setDate(3, Date.valueOf(orders.lodgement_date));
                }
            }
            preparedStatement.setString(4, orders.product_model);
            preparedStatement.setInt(5, orders.sales_id);
            preparedStatement.setString(6, orders.contract_number);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            if (!e.getMessage().matches("错误: 重复键违反唯一约束\".*?\"\n" +
                    " {2}详细：键值\"(.*?)=(.*?)\" 已经存在")) {
                e.printStackTrace();
            }
        } catch (ParseException e) {
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
    public void dropAllData() {
        String sql = "delete from ? where true;";
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

}
