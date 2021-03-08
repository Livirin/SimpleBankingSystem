package banking;

public class LuhnAlgorithm {

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

    public static int findChecksum(String number) {
        int sum = findLuhnSum(number);
        return sum % 10 == 0 ? 0 : 10 - sum % 10;
    }

    public static boolean isValid(String number) {
        return findLuhnSum(number) % 10 == 0;
    }
}
