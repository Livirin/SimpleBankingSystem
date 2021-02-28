package banking;

import java.util.Random;

public class Card {
    private static final String BIN = "400000";
    private String number;
    private String pin;
    private int balance;

    public Card() {
        Random random = new Random();
        number = BIN + String.format("%09d", random.nextInt(999999999));
        number += findChecksum(number);
        pin = String.format("%04d", random.nextInt(9999));
    }

    public Card(String number, String pin, int balance) {
        this.number = number;
        this.pin = pin;
        this.balance = balance;
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

    public boolean checkPin(String pin) {
        return this.pin.equals(pin);
    }

    public static int findChecksum(String number) {
        int sum = findLuhnSum(number);
        return sum % 10 == 0 ? 0 : 10 - sum % 10;
    }

    public static boolean isValid(String number) {
        return findLuhnSum(number) % 10 == 0;
    }

    //find the control sum for card number (except last digit) by the Luhn algorithm
    private static int findLuhnSum(String number) {
        int sum = 0;
        for (int i = 0; i < number.length(); i++) {
            int num = Integer.parseInt(String.valueOf(number.charAt(i)));
            if (i % 2 == 0) { num *= 2; }
            if (num > 9) { num -= 9; }
            sum += num;
        }
        return sum;
    }
}
