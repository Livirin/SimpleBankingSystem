package banking;

import java.util.Scanner;

public class Banking {

    private final Scanner scanner = new Scanner(System.in);
    private DataBase dataBase;
    private boolean exit;

    public Banking() {
        dataBase = new DataBase();
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
        Card card = Card.generateCard();

        try {
            dataBase.connect();
            dataBase.add(card);
        } finally {
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

        try {
            dataBase.connect();
            Card card = dataBase.getCard(cardNumber);
            if (card != null && card.checkPin(PIN)) {
                System.out.println("You have successfully logged in!");
                AccountMenu menu = new AccountMenu(card);
                menu.run();
            } else {
                System.out.println("Wrong card number or PIN!");
            }
        } finally {
            dataBase.disconnect();
        }
    }

    class AccountMenu {

        private Card card;

        public AccountMenu(Card card) {
            this.card = card;
        }

        public void run() {
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
                        viewBalance();
                        break;
                    case 2:
                        addIncome();
                        break;
                    case 3:
                        doTransfer();
                        break;
                    case 4:
                        closeAccount();
                        return;
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

        public void viewBalance() {
            System.out.println("Balance: " + card.getBalance());
        }

        public void addIncome() {
            System.out.println("Enter income:");
            int income = scanner.nextInt();
            dataBase.changeBalance(card, income);
            System.out.println("Income was added!");
        }

        public void doTransfer() {
            System.out.println("Transfer");
            System.out.println("Enter card number:");
            String receiverCardNumber = scanner.next();

            if (!LuhnAlgorithm.isValid(receiverCardNumber)) {
                System.out.println("Probably you made a mistake in the card number. Please try again!");
                return;
            }

            if (receiverCardNumber.equals(card.getNumber())){
                System.out.println("You can't transfer money to the same account!");
                return;
            }

            Card receiverCard = dataBase.getCard(receiverCardNumber);

            if (receiverCard == null) {
                System.out.println("Such a card does not exist.");
                return;
            }

            System.out.println("Enter how much money you want to transfer:");
            int transfer = scanner.nextInt();

            if (card.getBalance() < transfer) {
                System.out.println("Not enough money!");
                return;
            }

            dataBase.transfer(card, receiverCard, transfer);
            System.out.println("Success!");
        }

        public void closeAccount() {
            dataBase.delete(card);
        }
    }

}
