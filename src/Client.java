public class Client {
    public static void main(String[] args) {
        DatabaseManipulation dm = new DatabaseManipulation();
        DataImport.importDataUsingHashMap(dm);
        dm.close();
    }
}

