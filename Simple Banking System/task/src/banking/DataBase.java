package banking;

import java.sql.*;

public class DataBase {

    private String url;
    private Connection connection;
    private Statement statement;

    private DataBase(String url) {
        this.url = url;
    }

    public static DataBase createSQLiteDataBase(String fileName) throws ClassNotFoundException {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            throw new ClassNotFoundException("Not found sqlite driver");
        }
        return new DataBase("jdbc:sqlite:" + fileName);
    }

    public void connect() throws SQLException {
        try {
            connection = DriverManager.getConnection(url);
            statement = connection.createStatement();
        } catch (SQLException e) {
            throw new SQLException("Unable to connect");
        }
    }

    public void disconnect() {
        try {
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createCardTable() throws SQLException {
        statement.executeUpdate("CREATE TABLE IF NOT EXISTS card(" +
                "id INTEGER PRIMARY KEY," +
                "number TEXT NOT NULL UNIQUE," +
                "pin TEXT NOT NULL," +
                "balance INTEGER DEFAULT 0)");
    }

    public Card select(String cardNumber) throws SQLException {
        Card card = null;
        ResultSet resultSet = statement.executeQuery(String.format("SELECT * FROM card WHERE number = '%s';", cardNumber));
        while (resultSet.next()) {
            card = new Card(resultSet.getString("number"), resultSet.getString("pin"), resultSet.getInt("balance"));
        }
        return card;
    }

    public int getBalance(String cardNumber) throws SQLException {
        int balance = 0;
        ResultSet resultSet = statement.executeQuery(String.format("SELECT * FROM card WHERE number = '%s';", cardNumber));
        while (resultSet.next()) {
            balance = resultSet.getInt("balance");
        }
        return balance;
    }

    public void insert(String cardNumber, String cardPin) throws SQLException {
        statement.executeUpdate(String.format("INSERT INTO card (number, pin) VALUES ('%s', '%s');", cardNumber, cardPin));
    }

    public void changeBalance(String cardNumber, int amount) throws SQLException {
        statement.executeUpdate(String.format("UPDATE card SET balance = balance + %d WHERE number = '%s';", amount, cardNumber));
    }

    public void transfer(String cardNumber, String receiverCardNumber, int amount) throws SQLException {
        connection.setAutoCommit(false);
        changeBalance(cardNumber, -1 * amount);
        changeBalance(receiverCardNumber, amount);
        connection.commit();
        connection.setAutoCommit(true);
    }

    public void delete(String cardNumber) throws SQLException {
        statement.executeUpdate("DELETE FROM card WHERE number = " + cardNumber + ";");
        System.out.println("The account has been closed!");
    }
}
