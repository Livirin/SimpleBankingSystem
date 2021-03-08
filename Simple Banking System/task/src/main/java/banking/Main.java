package banking;

import org.hibernate.HibernateException;

public class Main {

    public static void main(String[] args) {
        String fileName;
        if (args.length > 1 && "-fileName".equals(args[0])) {
                fileName = args[1];
        } else {
            fileName = "Simple Banking System/task/banking.db";
        }
        try {
            DataBase.createSessionFactory("hibernate.cfg.xml", "jdbc:sqlite:" + fileName);
            Banking banking = new Banking();
            banking.runMainMenu();
        } catch (HibernateException e) {
            e.printStackTrace();
        }
        finally {
            DataBase.closeSessionFactory();
        }
    }
}