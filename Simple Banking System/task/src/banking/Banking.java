package banking;

import java.sql.SQLException;
import java.util.Scanner;

public class Banking {

    private final Scanner scanner = new Scanner(System.in);
    private DataBase dataBase;
    private boolean exit;

    public Banking(String fileName) {
        try {
            dataBase = DataBase.createSQLiteDataBase(fileName);
            dataBase.connect();
            dataBase.createCardTable();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        finally {
            dataBase.disconnect();
        }
    }

    public void runMainMenu() {
        while (!exit) {
            System.out.println("1. Create an account");
            System.out.println("2. Log into account");
            System.out.println("0. Exit");
            int input = scanner.nextInt();
            switch (input) {
                case 1:
                    createAccount();
                    break;
                case 2:
                    logIntoAccount();
                    break;
                case 0:
                    System.out.println("Bye!");
                    exit = true;
                    return;
                default:
                    System.out.println("Wrong command! Please, try again");
                    break;
            }
            System.out.println();
        }
    }

    public void createAccount() {
        Card card = new Card();

        try {
            dataBase.connect();
            dataBase.insert(card.getNumber(), card.getPin());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
             dataBase.disconnect();
        }

        System.out.println("Your card has been created");
        System.out.println("Your card number:\n" + card.getNumber());
        System.out.println("Your card PIN:\n" + card.getPin());
    }

    public void logIntoAccount() {
        System.out.println("Enter your card number:");
        String cardNumber = scanner.next();
        System.out.println("Enter your PIN:");
        String PIN = scanner.next();
        Card card = null;

        try {
            dataBase.connect();
            card = dataBase.select(cardNumber);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            dataBase.disconnect();
        }

        if (card != null && card.checkPin(PIN)) {
            System.out.println("You have successfully logged in!");
            runAccountMenu(card);
        } else {
            System.out.println("Wrong card number or PIN!");
        }
    }

    public void runAccountMenu(Card card) {
        while (true) {
            System.out.println("1. Balance");
            System.out.println("2. Add income");
            System.out.println("3. Do transfer");
            System.out.println("4. Close account");
            System.out.println("5. Log out");
            System.out.println("0. Exit");
            int input = scanner.nextInt();
            switch (input) {
                case 1:
                    viewBalance(card.getNumber());
                    break;
                case 2:
                    addIncome(card.getNumber());
                    break;
                case 3:
                    doTransfer(card.getNumber());
                    break;
                case 4:
                    closeAccount(card.getNumber());
                    break;
                case 5:
                    System.out.println("You have successfully logged out!");
                    return;
                case 0:
                    System.out.println("Bye!");
                    exit = true;
                    return;
                default:
                    System.out.println("Wrong command! Please, try again");
                    break;
            }
            System.out.println();
        }
    }

    public void viewBalance(String cardNumber) {
        try {
            dataBase.connect();
            System.out.println("Balance: " + dataBase.getBalance(cardNumber));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            dataBase.disconnect();
        }
    }

    public void addIncome(String cardNumber) {
        System.out.println("Enter income:");
        int income = scanner.nextInt();

        try {
            dataBase.connect();
            dataBase.changeBalance(cardNumber, income);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            dataBase.disconnect();
        }

        System.out.println("Income was added!");
    }

    public void doTransfer(String cardNumber) {
        System.out.println("Transfer");
        System.out.println("Enter card number:");
        String receiverCardNumber = scanner.next();

        if (!Card.isValid(receiverCardNumber)) {
            System.out.println("Probably you made a mistake in the card number. Please try again!");
            return;
        }

        if (receiverCardNumber.equals(cardNumber)){
            System.out.println("You can't transfer money to the same account!");
            return;
        }

        try {
            dataBase.connect();
            if (dataBase.select(receiverCardNumber) == null) {
                System.out.println("Such a card does not exist.");
                return;
            }

            System.out.println("Enter how much money you want to transfer:");
            int transfer = scanner.nextInt();

            if (dataBase.getBalance(cardNumber) < transfer) {
                System.out.println("Not enough money!");
                return;
            }

            dataBase.transfer(cardNumber, receiverCardNumber, transfer);
            System.out.println("Success!");

        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            dataBase.disconnect();
        }
    }

    public void closeAccount(String cardNumber) {
        try {
            dataBase.connect();
            dataBase.delete(cardNumber);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            dataBase.disconnect();
        }
    }

}
