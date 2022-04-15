import assets.EasyReader;

import java.io.*;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DatabaseManipulation implements DataManipulation, Closeable {
    private Connection con = null;
    private ResultSet resultSet;
    private static final int  BATCH_SIZE = 500;
    @Override
    public void openDatasource() {
        try {
            Class.forName("org.postgresql.Driver");

        } catch (ClassNotFoundException e) {
            System.err.println("Cannot find the PostgresSQL driver. Check CLASSPATH.");
            System.exit(1);
        }

        try {
            EasyReader in = new EasyReader(System.in);
            String host = in.nextLine("host");
            String dbname = in.nextLine("database name");
            String port = in.nextLine("port");
            String user = in.nextLine("user");
            String pwd = in.nextLine("password");
            if (host.equals(""))
                host = "localhost";
            if (dbname.equals(""))
                dbname = "postgres";
            if (port.equals(""))
                port = "5432";
            if (user.equals(""))
                user = "postgres";

            String url = "jdbc:postgresql://" + host + ":" + port + "/" + dbname;
            con = DriverManager.getConnection(url, user, pwd);

            con.setAutoCommit(false);


        } catch (SQLException e) {
            System.err.println("Database connection failed");
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }

    @Override
    public void closeDatasource() {
    }

    @Override
    public void executeDDL() {
        StringBuilder sql = new StringBuilder();
        try {
            BufferedReader reader = new BufferedReader(new FileReader("main.sql"));
            String line;
            while ((line = reader.readLine()) != null) {
                sql.append(line);
            }
            PreparedStatement preparedStatement = con.prepareStatement(String.valueOf(sql));
            preparedStatement.executeUpdate();
            con.commit();
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    //region Import Data Using Entity Class

    private PreparedStatement productPS;
    private PreparedStatement modelPS;
    private PreparedStatement locationPS;
    private PreparedStatement salesmanPS;
    private PreparedStatement supplyCenterPS;
    private PreparedStatement enterprisePS;
    private PreparedStatement contractPS;
    private PreparedStatement ordersPS;

    public DatabaseManipulation() {
        this.openDatasource();
        String productSQL = "insert into product (product_code, product_name) values (?, ?) on conflict do nothing;";
        String modelSQL = "insert into model (product_model, unit_price, product_id) values (?, ?, ?) on conflict do nothing;";
        String locationSQL = "insert into location (country, city) values (?, ?) on conflict do nothing;";
        String salesmanSQL = "insert into salesman (name, salesman_number, gender, age, mobile_phone, supply_center_id) " +
                "values (?, ?, ?, ?, ?, ?) on conflict do nothing;";
        String supplyCenterSQL = "insert into supply_center (supply_center_name, director_name) " +
                "values (?, ?) on conflict do nothing;";
        String enterpriseSQL = "insert into enterprise (enterprise_name, industry, location_id, supply_center_id) " +
                "values (?, ?, ?, ?) on conflict do nothing;";
        String contractSQL = "insert into contract (contract_number, contract_date, enterprise_id) " +
                "values (?, ?, ?) on conflict do nothing;";
        String ordersSQL = "insert into orders (quantity, estimated_delivery_date, lodgement_date, model_id, contract_id, salesman_id) " +
                "values (?, ?, ?, ?, ?, ?) on conflict do nothing;";
        try {
            productPS = con.prepareStatement(productSQL);
            modelPS = con.prepareStatement(modelSQL);
            locationPS = con.prepareStatement(locationSQL);
            salesmanPS = con.prepareStatement(salesmanSQL);
            supplyCenterPS = con.prepareStatement(supplyCenterSQL);
            enterprisePS = con.prepareStatement(enterpriseSQL);
            contractPS = con.prepareStatement(contractSQL);
            ordersPS = con.prepareStatement(ordersSQL);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void importOneProduct(String product_code, String product_name,int cnt) {
        try {
            productPS.setString(1, product_code);
            productPS.setString(2, product_name);
            productPS.addBatch();
            if(cnt%BATCH_SIZE==0){
                productPS.executeBatch();
                productPS.clearBatch();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void importProduct(){
        try{
        productPS.executeBatch();
        }catch(SQLException ignored){

        }
    }
    @Override
    public void importOneModel(String product_model, int unit_price, int product_id, int cnt) {
        try {
            modelPS.setString(1, product_model);
            modelPS.setInt(2, unit_price);
            modelPS.setInt(3, product_id);
            modelPS.addBatch();
            if(cnt%BATCH_SIZE==0){
                modelPS.executeBatch();
                modelPS.clearBatch();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void importModel(){
        try{
            modelPS.executeBatch();
        }catch(SQLException ignored){

        }
    }
    @Override
    public void importOneLocation(String country, String city, int cnt) {
        try {
            locationPS.setString(1, country);
            if (city.equals("NULL")) {
                locationPS.setNull(2, Types.VARCHAR);
            } else {
                locationPS.setString(2, city);
            }
            locationPS.addBatch();
            if(cnt%BATCH_SIZE==0){
                locationPS.executeBatch();
                locationPS.clearBatch();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void importLocation(){
        try{
            locationPS.executeBatch();
        }catch(SQLException ignored){

        }
    }
    @Override
    public void importOneSalesman(String name, String salesman_number, String gender, int age, String mobile_phone, int supply_center_id,int cnt) {
        try {
            salesmanPS.setString(1, name);
            salesmanPS.setString(2, salesman_number);
            salesmanPS.setString(3, gender);
            salesmanPS.setInt(4, age);
            salesmanPS.setString(5, mobile_phone);
            salesmanPS.setInt(6, supply_center_id);

            salesmanPS.addBatch();
            if(cnt%BATCH_SIZE==0){
                salesmanPS.executeBatch();
                salesmanPS.clearBatch();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void importSalesman(){
        try{
            salesmanPS.executeBatch();
        }catch(SQLException ignored){

        }
    }
    @Override
    public void importOneSupplyCenter(String supply_center_name, String director_name, int cnt) {
        try {
            supplyCenterPS.setString(1, supply_center_name);
            supplyCenterPS.setString(2, director_name);

            supplyCenterPS.addBatch();
            if(cnt%BATCH_SIZE==0){
                supplyCenterPS.executeBatch();
                supplyCenterPS.clearBatch();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void importSupplyCenter(){
        try{
            supplyCenterPS.executeBatch();
        }catch(SQLException ignored){

        }
    }
    @Override
    public void importOneEnterprise(String enterprise_name, String industry, int location_id, int supply_center_id, int cnt) {
        try {
            enterprisePS.setString(1, enterprise_name);
            enterprisePS.setString(2, industry);
            enterprisePS.setInt(3, location_id);
            enterprisePS.setInt(4, supply_center_id);
            enterprisePS.addBatch();
            if(cnt%BATCH_SIZE==0){
                enterprisePS.executeBatch();
                enterprisePS.clearBatch();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void importEnterprise(){
        try{
            enterprisePS.executeBatch();
        }catch(SQLException ignored){

        }
    }
    @Override
    public void importOneContract(String contract_number, String contract_date, int enterprise_id,int cnt) {
        try {
            contractPS.setString(1, contract_number);
            contractPS.setDate(2, Date.valueOf(contract_date));
            contractPS.setInt(3, enterprise_id);

            contractPS.addBatch();
            if(cnt%BATCH_SIZE==0){
                contractPS.executeBatch();
                contractPS.clearBatch();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void importContract(){
        try{
            contractPS.executeBatch();
        }catch(SQLException ignored){

        }
    }
    @Override
    public void importOneOrders(int quantity, String estimated_delivery_date, String lodgement_date, int model_id, int contract_id, int salesman_id,int cnt) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date deadline = sdf.parse("2022-04-15");
        java.util.Date date;
        try {
            ordersPS.setInt(1, quantity);
            ordersPS.setDate(2, Date.valueOf(estimated_delivery_date));
            if (lodgement_date.equals("")) {
                ordersPS.setNull(3, Types.DATE);
            } else {
                date = sdf.parse(lodgement_date);
                if (date.after(deadline)) {
                    ordersPS.setNull(3, Types.DATE);
                } else {
                    ordersPS.setDate(3, Date.valueOf(lodgement_date));
                }
            }
            ordersPS.setInt(4, model_id);
            ordersPS.setInt(5, contract_id);
            ordersPS.setInt(6, salesman_id);

            ordersPS.addBatch();
            if(cnt%BATCH_SIZE==0){
                ordersPS.executeBatch();
                ordersPS.clearBatch();
            }
        } catch (SQLException | ParseException e) {
            e.printStackTrace();
        }
    }
    public void importOrder(){
        try{
            ordersPS.executeBatch();
        }catch(SQLException ignored){

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
    public void alterForeignKey() {
        String sql = """
                alter table model add constraint model_product_fk foreign key (product_id) references product (id);
                alter table salesman add constraint salesman_supply_center_fk foreign key (supply_center_id) references supply_center (id);
                alter table enterprise add constraint location_enterprise foreign key (location_id) references location (id);
                alter table enterprise add constraint enterprise_supply_center_fk foreign key (supply_center_id) references supply_center (id);
                alter table contract add constraint enterprise_contract foreign key (enterprise_id) references enterprise (id);
                alter table orders add constraint contract_order foreign key (contract_id) references contract (id);
                alter table orders add constraint model_order foreign key (model_id) references model (id);
                alter table orders add constraint orders_salesman_id foreign key (salesman_id) references salesman (id);""";
        try {
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
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

    private static final ExecutorService threadPool = Executors.newFixedThreadPool(50);

    public void parallelImportSalesmanData() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("resources/salesman.txt"));
            CountDownLatch latch = new CountDownLatch(100);
            long startTime = System.currentTimeMillis();

            for (int i = 0; i < 100; i++) {
                threadPool.execute(() -> {
                    try {
                        executeBatchImportSalesman(reader);
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        latch.countDown();
                    }
                });
            }
            latch.await();
            long endTime = System.currentTimeMillis();
            System.out.println((endTime - startTime) + "ms");
        } catch (FileNotFoundException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            threadPool.shutdown();
        }
    }

    public void executeBatchImportSalesman(BufferedReader reader) {
        try {
            con.setAutoCommit(false);
            String[] tokens;
            String line;
            String sql = "insert into salesman (name, salesman_number, gender, age, mobile_phone, supply_center_id) " +
                    "values (?, ?, ?, ?, ?, ?) on conflict do nothing;";
            PreparedStatement ps = con.prepareStatement(sql);

            for (int i = 0; i < 10000; i++) {
                line = reader.readLine();
                if (line == null) break;
                tokens = line.split(",");
                ps.setString(1, tokens[0]);
                ps.setString(2, tokens[1]);
                ps.setString(3, tokens[2]);
                ps.setInt(4, Integer.parseInt(tokens[3]));
                ps.setString(5, tokens[4]);
                ps.setInt(6, Integer.parseInt(tokens[5]));
                ps.addBatch();
            }
            ps.executeBatch();
            commit();
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized void commit() {
        try {
            con.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        DatabaseManipulation dm = new DatabaseManipulation();
        dm.openDatasource();
        dm.parallelImportSalesmanData();
        dm.closeDatasource();
    }

    @Override
    public void close() {
        if (con != null) {
            try {
                con.commit();
                con.close();
                con = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
