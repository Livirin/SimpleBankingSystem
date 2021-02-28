package banking;

public class Main {

    public static void main(String[] args) {
        String fileName;
        if (args.length > 1 && "-fileName".equals(args[0])) {
                fileName = args[1];
        } else {
            fileName = "banking.db";
        }
        Banking banking = new Banking(fileName);
        banking.runMainMenu();
    }
}