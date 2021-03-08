package banking;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.util.List;

public class DataBase {

    private static SessionFactory factory;
    private Session session;

    public static void createSessionFactory(String configFile, String url) throws HibernateException {
        factory = new Configuration().configure(configFile).setProperty("hibernate.connection.url", url).addAnnotatedClass(Card.class).buildSessionFactory();
    }

    public static void closeSessionFactory() {
        if (factory != null) {
            factory.close();
        }
    }

    public void connect() {
        session = factory.openSession();
    }

    public void disconnect() {
        if (session != null) {
            session.close();
        }
    }

    public Card getCard(String cardNumber) {
        List<Card> cards = session.createQuery("FROM Card WHERE number = " + cardNumber).getResultList();
        if (!cards.isEmpty()) {
            return cards.get(0);
        } else {
            return null;
        }
    }

    public void add(Card card) {
        session.beginTransaction();
        session.save(card);
        session.getTransaction().commit();
    }

    public void changeBalance(Card card, int amount) {
        card.setBalance(card.getBalance() + amount);
        session.save(card);
    }

    public void transfer(Card senderCard, Card receiverCard, int amount) {
        session.beginTransaction();
        changeBalance(senderCard, -1 * amount);
        changeBalance(receiverCard, amount);
        session.getTransaction().commit();
    }

    public void delete(Card card)  {
        session.beginTransaction();
        session.delete(card);
        session.getTransaction().commit();
    }
}
