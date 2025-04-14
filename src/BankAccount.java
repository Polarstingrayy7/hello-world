import java.util.Stack;
import java.time.LocalDateTime;


public class BankAccount {
    private String name;
    private double vault; // amount in bank account
    private String password;
    private LocalDateTime creationDate;

    public static char KEY = 'K';

    private Stack<String[]> transactions = new Stack<>();

    public BankAccount(String name, double amount) {
        this.name = name;
        this.vault = amount;
        this.password = "";
        this.creationDate = LocalDateTime.now();
    }

    public BankAccount(String name, double amount, String password) {
        this.name = name;
        this.vault = amount;
        this.password = password;
        this.creationDate = LocalDateTime.now();
    }
    
    public String getName() {return name;}
    public void setName(String name) {this.name = name;}

    public double getVault() {return vault;}
    public void setVault(double vault) {this.vault = vault;}

    public String getPassword() {return encrypt(password, KEY);}
    public void setPassword(String password) {this.password = password;}

    public Stack<String[]> getTransactions() {return transactions;}

    public boolean alohomora(String attempt) {return attempt.equals(password);}

    public static String timeStamp() {
        String temp = LocalDateTime.now().toString();

        String date = LocalDateTime.now().toString().substring(0, 10).replaceAll("-", "/");
        String time = temp.substring(11, 16);

        int hour = Integer.parseInt(time.substring(0, 2));

        String sfx = "AM";
        if ((hour >= 12 && hour <= 24)) {
//            date = "" + (Double.parseDouble(date) - 12);
            sfx = "PM";}

        return date + ", " + time + sfx;}

    public void add(double amount) {
        vault += amount;
        String date = timeStamp();

        String amt;
        if (amount >= 0) {
            amt = String.format("+$%.2f", amount);
        } else {
            amt = String.format("-$%.2f", (amount * -1));
        }

        transactions.push(new String[] {date, amt});
    }



//    public String toString() {
//        StringBuilder out = new StringBuilder();
//        out.append(name).append(" | $").append(vault).append("\n");
//        for (Double transaction : transactions) {
//            if (transaction >= 0) {
//
//                out.append("+$").append(transaction).append("\n");
//            } else {
//                out.append("-$").append(transaction * -1).append("\n");
//            }
//        }
//        return out.toString();
//    }

    public String toString() {
        StringBuilder out = new StringBuilder();
        out.append(name).append(" | $").append(vault).append("\n");

        for (String[] transaction : transactions) {
            out.append(transaction[0]).append(" | ").append(transaction[1]).append("\n");
        }
        return out.toString();
    }





    public static String encrypt(String text, char key) {
        StringBuilder encrypted = new StringBuilder();

        for (int i = 0; i < text.length(); i++) {
            encrypted.append((char) (text.charAt(i) ^ key ^ i));
        }
        return encrypted.toString();

    }

    public static void main(String[] args) {

        System.out.println(timeStamp());
//        String password = "coolbitch";
//        String encrypted = encrypt(password, KEY);
//        System.out.println("Password: " + password);
//        System.out.println("Encrypted: " + encrypted);
//        System.out.println("Decrypted: " + encrypt(encrypted, KEY));

//        System.out.println("valuOf(2.99) -> " + String.valueOf(2.99));

//
//        char c = 'c' - 2;
//
//        System.out.println("'c' - 2 = " +  c);
    }
}
