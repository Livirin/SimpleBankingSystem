package banking;

import javax.persistence.*;

import java.util.Random;

@Entity
@Table(name = "card")
public class Card {
    private static final String BIN = "400000";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "number")
    private String number;

    @Column(name = "pin")
    private String pin;

    @Column(name = "balance")
    private int balance;

    public Card() {
    }

    public static Card generateCard() {
        Random random = new Random();
        String number = BIN + String.format("%09d", random.nextInt(999999999));
        number += LuhnAlgorithm.findChecksum(number);
        String pin = String.format("%04d", random.nextInt(9999));
        return new Card(number, pin, 0);
    }

    public Card(String number, String pin, int balance) {
        this.number = number;
        this.pin = pin;
        this.balance = balance;
    }

    public int getId() {
        return id;
    }

    public String getNumber() {
        return number;
    }

    public String getPin() {
        return pin;
    }

    public int getBalance() {
        return balance;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public boolean checkPin(String pin) {
        return this.pin.equals(pin);
    }

}
